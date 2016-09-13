package net.avicus.tutorial.api;

import net.avicus.tutorial.plugin.TutorialPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A single step in a tutorial.
 */
public class TutorialStep {
    public static final String FROZEN_METADATA = "tutorial.freeze";

    private final boolean freeze;
    private final boolean clearInventory;
    private final boolean fly;
    private final Optional<Double> countdown;
    private final Optional<GameMode> gamemode;
    private final Optional<String> worldName;
    private final Optional<Vector> position;
    private final Optional<Float> yaw;
    private final Optional<Float> pitch;
    private final Optional<List<String>> chat;
    private final Optional<Title> title;
    private final Optional<Map<Integer, ItemStack>> inventory;

    /**
     * Constructor
     * @param freeze If the player should be frozen in place during this step.
     * @param clearInventory If the player's inventory should be cleared.
     * @param fly If the player should be set to flying upon entering this step.
     * @param countdown Specify when this step should end.
     * @param gamemode Set the gamemode of the player during this step.
     * @param worldName Teleport the player to a different world.
     * @param position Teleport the player to a new position.
     * @param yaw The player's yaw.
     * @param pitch The player's pitch
     * @param chat Send a series of chat messages to the player.
     * @param title Send a title (and subtitle) to the player.
     * @param inventory Modify the player's inventory.
     */
    public TutorialStep(boolean freeze,
                        boolean clearInventory,
                        boolean fly,
                        Optional<Double> countdown,
                        Optional<GameMode> gamemode,
                        Optional<String> worldName,
                        Optional<Vector> position,
                        Optional<Float> yaw,
                        Optional<Float> pitch,
                        Optional<List<String>> chat,
                        Optional<Title> title,
                        Optional<Map<Integer, ItemStack>> inventory) {
        this.freeze = freeze;
        this.clearInventory = clearInventory;
        this.fly = fly;
        this.countdown = countdown;
        this.gamemode = gamemode;
        this.worldName = worldName;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.chat = chat;
        this.title = title;
        this.inventory = inventory;
    }

    public Optional<Double> getCountdown() {
        return this.countdown;
    }

    public boolean isFrozen() {
        return this.freeze;
    }

    /**
     * Display this tutorial step to the player.
     * @param player
     */
    public void show(Player player) {
        // Dismount any entity
        if (player.getVehicle() != null)
            player.getVehicle().eject();

        // Gamemode
        if (this.gamemode.isPresent()) {
            player.setGameMode(this.gamemode.get());
        }

        // Fly
        player.setFlying(this.fly);

        // World
        if (this.worldName.isPresent()) {
            World world = Bukkit.getWorld(this.worldName.get());
            Location location = player.getLocation();
            location.setWorld(world);
            player.teleport(location);
        }

        // Position
        if (this.position.isPresent()) {
            Location location = this.position.get().toLocation(
                    player.getWorld(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch());
            player.teleport(location);
        }

        // Yaw/Pitch
        if (this.yaw.isPresent()) {
            Location location = player.getLocation();
            location.setYaw(this.yaw.get());
            player.teleport(location);
        }
        if (this.pitch.isPresent()) {
            Location location = player.getLocation();
            location.setPitch(this.pitch.get());
            player.teleport(location);
        }

        // Chat
        if (this.chat.isPresent()) {
            List<String> lines = this.chat.get();
            lines.forEach(player::sendMessage);
        }

        // Title
        if (this.title.isPresent()) {
            player.sendTitle(this.title.get());
        }

        // Clear inventory
        if (this.clearInventory)
            player.getInventory().clear();

        // Items
        if (this.inventory.isPresent()) {
            for (int slot : this.inventory.get().keySet()) {
                ItemStack stack = this.inventory.get().get(slot);
                player.getInventory().setItem(slot, stack);
            }
        }

        // Freeze
        if (this.freeze) {
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
