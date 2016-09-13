package net.avicus.tutorial.api;

import net.avicus.tutorial.plugin.TutorialPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Objects;

public class ActiveTutorialListener implements Listener {
    private final ActiveTutorial tutorial;

    public ActiveTutorialListener(ActiveTutorial tutorial) {
        this.tutorial = tutorial;
    }

    private boolean isThePlayer(Entity entity) {
        return Objects.equals(this.tutorial.getPlayer(), entity);
    }

    private boolean onAction(ActionResult result) {
        switch (result) {
            case ALLOW:
                return false;
            case DISALLOW:
                return true;
            case EXIT:
                Bukkit.getServer().getScheduler().runTask(TutorialPlugin.getInstance(), () -> {
                    this.tutorial.stop(true);
                });
                return false;
            default:
                return false;
        }
    }

    @EventHandler
    public void onLeaveFreeze(EntityDismountEvent event) {
        if (!isThePlayer(event.getEntity()))
            return;

        if (!event.getDismounted().hasMetadata(SimpleTutorialStep.FROZEN_METADATA))
            return;

        if (this.tutorial.getCurrentStep().isFrozen())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(PlayerCommandPreprocessEvent event) {
        if (!isThePlayer(event.getPlayer()))
            return;

        event.setCancelled(onAction(this.tutorial.getCommandResult()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!isThePlayer(event.getPlayer()))
            return;

        event.setCancelled(onAction(this.tutorial.getChatResult()));
    }
}
