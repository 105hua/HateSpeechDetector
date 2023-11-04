/* Licensed under GNU General Public License v3.0 */
package me.joshua.hatespeechdetector;

import java.util.logging.Logger;
import me.joshua.hatespeechdetector.events.ChatMsg;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class HateSpeechDetector extends JavaPlugin {

  public static Logger pluginLogger;
  public static Server serverVar;
  public static final double probabilityLimit = 50.0; // Debug variable for probability limit.

  @Override
  public void onEnable() {
    pluginLogger = this.getLogger();
    serverVar = this.getServer();
    this.getServer().getPluginManager().registerEvents(new ChatMsg(), this);
    pluginLogger.info("Hate Speech Detector is enabled!");
  }

  @Override
  public void onDisable() {
    pluginLogger.info("Hate Speech Detector is now disabled.");
  }
}
