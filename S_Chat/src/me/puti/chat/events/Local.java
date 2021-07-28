package me.puti.chat.events;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.player.ChatUser;
import me.puti.perms.S_Perms;

public class Local implements Listener {

	@EventHandler
	void Event(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatManager.getAction(p.getName()) != null) {
			switch (ChatManager.getAction(p.getName())) {
			case Cancelar:
				e.setCancelled(true);
				return;
			case Surdo:
				e.setCancelled(true);
				e.setFormat("");
				e.setMessage("");
				return;
			case Bloquear:
				e.setCancelled(true);
				e.setFormat("");
				e.setMessage("");
				return;
			default:
				break;
			}
		}
		if (ChatManager.getUser(p.getName()) != null) {
			e.setCancelled(true);
			ChatUser user = ChatManager.getUser(p.getName());
			
			List<Entity> nebs = p.getNearbyEntities(25, 25, 25);
			if (!(nebs instanceof Player)) {
				if (S_Perms.getUser(p).hasPermissao("chat.admin")) {
					p.sendMessage("");
					p.sendMessage("§e[L]§7 " + user.getFormat().replace("<message>", e.getMessage().replace("&", "§")));
					p.sendMessage("");
				} else {
					p.sendMessage("§e[L]§7 " + user.getFormat().replace("<message>", e.getMessage().replace("&", "§")));
				}
					p.sendMessage("§cNenhum jogador por perto.");
				return;
			}
			for (Entity all : p.getNearbyEntities(25, 25, 25)) {
				if (all instanceof Player) {
					Player allP = (Player) all;
					if (S_Perms.getUser(allP).hasPermissao("chat.admin")) {
						all.sendMessage("");
						all.sendMessage("§e[L]§7 " + user.getFormat().replace("<message>", e.getMessage().replace("&", "§")));
						all.sendMessage("");
					} else {
						all.sendMessage("§e[L]§7 " + user.getFormat().replace("<message>", e.getMessage().replace("&", "§")));
					}
				}
			}
		} else {
			p.sendMessage("§cOpa! parece que o chat paro de funcionar, que triste!");
		}
	}

}
