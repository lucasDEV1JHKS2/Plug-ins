package me.puti.chat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.player.ChatUser;

public class PlayerEvents implements Listener{
	
	@EventHandler
	void Event(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (ChatManager.getUser(p.getName()) == null) {
			ChatUser user = new ChatUser(p.getName());
			user.update();
			ChatManager.addUser(user);
		}
	}
	

}
