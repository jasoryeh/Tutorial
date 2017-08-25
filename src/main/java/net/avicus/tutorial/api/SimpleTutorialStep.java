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

/**
 * A single step in a tutorial.
 */
public class SimpleTutorialStep extends AbstractTutorialStep {

  private final boolean freeze;
  private final boolean clearInventory;
  private final boolean fly;
  private final Optional<Double> countdown;
  private final Optional<GameMode> gamemode;
  private final Optional<String> worldName;
  private final Optional<Vector> position;
  private final Optional<Float> yaw;
  private final Optional<Float> pitch;
  private final Optional<List<TextComponent>> chat;
  private final Optional<Title> title;
  private final Optional<Map<Integer, ItemStack>> inventory;

  /**
   * Constructor
   *
   * @param freeze If the player should be frozen in place during this step.
   * @param clearInventory If the player's inventory should be cleared.
   * @param fly If the player should be set to flying.
   * @param countdown Countdown to next tutorial step.
   * @param gamemode Set the gamemode of the player during this step.
   * @param worldName Teleport the player to a different world.
   * @param position Teleport the player to a new position.
   * @param yaw The player's yaw.
   * @param pitch The player's pitch
   * @param chat Send a series of chat messages to the player.
   * @param title Send a title (and subtitle) to the player.
   * @param inventory Modify the player's inventory.
   */
  public SimpleTutorialStep(boolean freeze,
      boolean clearInventory,
      boolean fly,
      Optional<Double> countdown,
      Optional<GameMode> gamemode,
      Optional<String> worldName,
      Optional<Vector> position,
      Optional<Float> yaw,
      Optional<Float> pitch,
      Optional<List<TextComponent>> chat,
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

  @Override
  public boolean isFlyEnabled() {
    return this.fly;
  }

  @Override
  public boolean isFrozen() {
    return this.freeze;
  }

  @Override
  public boolean isClearInventory() {
    return this.clearInventory;
  }

  @Override
  public Optional<Double> getCountdown() {
    return this.countdown;
  }

  @Override
  public Optional<String> getWorldName() {
    return this.worldName;
  }

  @Override
  public Optional<GameMode> getGameMode() {
    return this.gamemode;
  }

  @Override
  public Optional<Vector> getPosition() {
    return this.position;
  }

  @Override
  public Optional<Float> getYaw() {
    return this.yaw;
  }

  @Override
  public Optional<Float> getPitch() {
    return this.pitch;
  }

  @Override
  public Optional<Title> getTitle(Player player) {
    return this.title;
  }

  @Override
  public Optional<List<TextComponent>> getChat(Player player) {
    return this.chat;
  }

  @Override
  public Optional<Map<Integer, ItemStack>> getInventory() {
    return this.inventory;
  }
}
