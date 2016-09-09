package net.avicus.tutorial.plugin;

import net.avicus.tutorial.TutorialParser;
import net.avicus.tutorial.api.ActionResult;
import net.avicus.tutorial.api.ActiveTutorial;
import net.avicus.tutorial.api.Tutorial;
import net.avicus.tutorial.api.TutorialStep;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

import java.io.InputStreamReader;
import java.util.Optional;

public class TutorialPlugin extends JavaPlugin implements Listener {
    private static TutorialPlugin instance;

    public static TutorialPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void onTutorial(PlayerInteractEvent event) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("tutorial.yml")));
        Tutorial tutorial = TutorialParser.parseTutorial(config);
//        TutorialStep step1 = new TutorialStep(
//                true,
//                true,
//                true,
//                Optional.of(GameMode.CREATIVE),
//                Optional.empty(),
//                Optional.of(new Vector(0, 90, 0)),
//                Optional.of(90.0F),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.of(new Title("Testing!")),
//                Optional.empty()
//        );
//
//        TutorialStep step2 = new TutorialStep(
//                true,
//                true,
//                true,
//                Optional.of(GameMode.CREATIVE),
//                Optional.empty(),
//                Optional.of(new Vector(50, 90, -20)),
//                Optional.of(0F),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.of(new Title("What")),
//                Optional.empty()
//        );
//
//        Tutorial tutorial = new Tutorial(Optional.empty(), "my tut", step1, step2);

        ActiveTutorial active = tutorial.create(event.getPlayer(), ActionResult.EXIT, ActionResult.EXIT);
        active.start();

        getServer().getScheduler().runTaskLater(this, () -> {
            if (active.isStarted())
                active.setStep(1);
        }, 20 * 3);

        getServer().getScheduler().runTaskLater(this, () -> {
            if (active.isStarted())
                active.stop(true);
        }, 20 * 6);
    }
}
