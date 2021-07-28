package me.puti.economia.api;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.puti.economia.database.MetodosEconomy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LegendChat implements Listener {
  @EventHandler
  private void onChat(ChatMessageEvent e) {
    String TGMT;
    Player p = e.getSender();
    String magnata = MetodosEconomy.Maganta();
    if (!magnata.equalsIgnoreCase(p.getName())) {
      TGMT = "";
    } else {
      TGMT = "§a[$] ";
    } 
    if (e.getTags().contains("rico"))
      e.setTagValue("rico", TGMT); 
  }
}
