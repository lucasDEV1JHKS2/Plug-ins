package me.puti.armazem.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.puti.armazem.manager.Manager;

public class PlayerEvent implements Listener {

	@EventHandler
	void Event(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!(Manager.IsConfig(p.getName() + ".yml"))) {
			Manager.PlayerConfig(p.getName() + ".yml");
		}
	}
	
	
	@EventHandler
	void Event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Manager.getArmazem(p.getName()) != null) {
			try {
				Manager.getArmazem(p.getName()).save();
			} catch (Exception Ignorado) {}
		}
	}

	

}
