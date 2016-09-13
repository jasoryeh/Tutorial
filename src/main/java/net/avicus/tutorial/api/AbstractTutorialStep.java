package net.avicus.tutorial.api;

import net.avicus.tutorial.plugin.TutorialPlugin;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.github.paperspigot.Title;

import java.util.List;

/**
 * A single step in a tutorial.
 */
public abstract class AbstractTutorialStep implements TutorialStep {
    public static final String FROZEN_METADATA = "tutorial.freeze";

    /**
     * Display this tutorial step to the player.
     * @param player
     */
    public void show(Player player) {
        // Dismount any entity
        if (player.getVehicle() != null)
            player.getVehicle().eject();

        // Gamemode
        if (getGameMode().isPresent()) {
            player.setGameMode(getGameMode().get());
        }

        // Fly
        player.setFlying(isFlyEnabled());

        // World
        if (getWorldName().isPresent()) {
            World world = Bukkit.getWorld(getWorldName().get());
            Location location = player.getLocation();
            location.setWorld(world);
            player.teleport(location);
        }

        // Position
        if (getPosition().isPresent()) {
            Location location = getPosition().get().toLocation(
                    player.getWorld(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch());
            player.teleport(location);
        }

        // Yaw/Pitch
        if (getYaw().isPresent()) {
            Location location = player.getLocation();
            location.setYaw(getYaw().get());
            player.teleport(location);
        }
        if (getPitch().isPresent()) {
            Location location = player.getLocation();
            location.setPitch(getPitch().get());
            player.teleport(location);
        }

        // Chat
        List<TextComponent> chat = getChat(player).orElse(null);
        if (chat != null) {
            chat.forEach(player::sendMessage);
        }

        // Title
        Title title = getTitle(player).orElse(null);
        if (title != null) {
            player.sendTitle(title);
        }

        // Clear inventory
        if (isClearInventory())
            player.getInventory().clear();

        // Items
        if (getInventory().isPresent()) {
            for (int slot : getInventory().get().keySet()) {
                ItemStack stack = getInventory().get().get(slot);
                player.getInventory().setItem(slot, stack);
            }
        }

        // Freeze
        if (isFrozen()) {
            ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            stand.setVisible(false);
            stand.setSmall(true);
            stand.setMarker(true);
            stand.setGravity(false);
            stand.setPassenger(player);
            stand.setMetadata(FROZEN_METADATA, new FixedMetadataValue(TutorialPlugin.getInstance(), true));
        }
    }

    /**
     * Hide this tutorial step from the player.
     * @param player
     */
    public void hide(Player player) {
        player.hideTitle();
        player.resetTitle();

        if (player.getVehicle() != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle.hasMetadata(FROZEN_METADATA)) {
                vehicle.removeMetadata(FROZEN_METADATA, TutorialPlugin.getInstance());
                player.getVehicle().remove();
            }
        }
    }
}
