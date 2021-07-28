package me.puti.economia.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.puti.economia.database.MetodosEconomy;
import me.puti.economia.manager.User;
import me.puti.economia.manager.Users;


public class Join implements Listener{

	
	@EventHandler(priority = EventPriority.NORMAL)
	void Event(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!(Users.Contains(p.getName()))) {
			User user = new User(p.getName(), MetodosEconomy.PegarMoney(p.getName()));
			Users.addUser(user);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	void Event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Users.Contains(p.getName())) {
			Users.getUser(p.getName()).save();
		}
	}

}
