package net.avicus.tutorial.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

public interface TutorialStep {

  void show(Player player);

  void hide(Player player);

  boolean isFlyEnabled();

  boolean isFrozen();

  boolean isClearInventory();

  Optional<Double> getCountdown();

  Optional<String> getWorldName();

  Optional<GameMode> getGameMode();

  Optional<Vector> getPosition();

  Optional<Float> getYaw();

  Optional<Float> getPitch();

  Optional<Title> getTitle(Player player);

  Optional<List<TextComponent>> getChat(Player player);

  Optional<Map<Integer, ItemStack>> getInventory();
}
