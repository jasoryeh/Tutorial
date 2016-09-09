package net.avicus.tutorial.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerState {
    private final Location location;
    private final GameMode gamemode;
    private final boolean canFly;
    private final boolean flying;
    private final Map<Integer, ItemStack> inventory;
    private final Collection<PotionEffect> effects;

    public PlayerState(Location location,
                       GameMode gamemode,
                       boolean canFly,
                       boolean flying,
                       Map<Integer, ItemStack> inventory,
                       Collection<PotionEffect> effects) {
        this.location = location;
        this.gamemode = gamemode;
        this.canFly = canFly;
        this.flying = flying;
        this.inventory = inventory;
        this.effects = effects;
    }

    public void apply(Player player) {
        player.teleport(this.location);
        player.setGameMode(this.gamemode);
        player.setAllowFlight(this.canFly);
        player.setFlying(this.flying);
        for (int index : this.inventory.keySet())
            player.getInventory().setItem(index, this.inventory.get(index));
        player.addPotionEffects(this.effects);
    }

    public static PlayerState of(Player player) {
        Location location = player.getLocation();

        GameMode gamemode = player.getGameMode();

        boolean canFly = player.getAllowFlight();

        boolean flying = player.isFlying();

        Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < player.getInventory().getSize(); i++)
            items.put(i, player.getInventory().getItem(i));

        Collection<PotionEffect> effects = player.getActivePotionEffects();

        return new PlayerState(location, gamemode, canFly, flying, items, effects);
    }
}
