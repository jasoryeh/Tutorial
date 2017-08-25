package net.avicus.tutorial.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
}
