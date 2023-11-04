/* Licensed under GNU General Public License v3.0 */
package me.joshua.hatespeechdetector.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.joshua.hatespeechdetector.FukWrapper;
import me.joshua.hatespeechdetector.HateSpeechDetector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatMsg implements Listener {
  @EventHandler
  public static void onPlayerMessage(AsyncChatEvent event) {
    Player offendingPlayer = event.getPlayer();
    TextComponent playerMsgComponent = (TextComponent) event.message();
    String messageContent = playerMsgComponent.content();
    double probability = FukWrapper.analyzeText(messageContent);
    if (probability == -1.0) {
      TextComponent failureComponent =
          Component.text("Failed to get an analysis of your message, it has not been sent.");
      offendingPlayer.sendMessage(failureComponent);
      event.setCancelled(true);
    } else if (probability >= HateSpeechDetector.probabilityLimit) {
      event.setCancelled(true); // Cancel the event
      TextComponent offenseComponent =
          Component.text(
              "Your message has been detected as hatespeech.", // TODO Add event will be logged when
              // done.
              NamedTextColor.RED,
              TextDecoration.BOLD);
      offendingPlayer.sendMessage(offenseComponent);
    }
  }
}
