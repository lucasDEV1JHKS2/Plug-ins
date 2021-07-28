package me.puti.clash.manager;

import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.puti.clash.manager.enums.Cargo;

public class ClashPlayer {

	private String name;
	private Player player;
	private Clash clash;
	private Cargo cargo;

	public ClashPlayer(String name) {
		this.name = name;
		this.player = ((Bukkit.getPlayer(name) != null) ? Bukkit.getPlayer(name) : null);
	}

	public ClashPlayer(Player player) {
		this.player = player;
		this.name = player.getName();
	}

	public void sendMessage(String message) {
		player = ((Bukkit.getPlayer(name) != null) ? Bukkit.getPlayer(name) : null);
		if (player != null) {
			player.sendMessage(message.replace("&", "§"));
			return;
		}
	}

	public void Sound(Sound sound) {
		player = ((Bukkit.getPlayer(name) != null) ? Bukkit.getPlayer(name) : null);
		if (player != null) {
			player.playSound(player.getLocation(), sound, 1, 1);
		}
	}

	public Player getPlayer() {
		return player;
	}
	
	public  Clash getClash() {
		return clash;
	}
	
	
	public Cargo getCargo() {
		return cargo;
	}

	public void setClash(Clash clash) {
		this.clash = clash;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	public String getName() {
		return name;
	}

}
