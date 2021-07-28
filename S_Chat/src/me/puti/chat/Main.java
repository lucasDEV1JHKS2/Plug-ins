package me.puti.chat;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.chat.manager.ChatManager;

public class Main extends JavaPlugin{

	private static Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
		new ChatManager(instance);
		
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		
		Main.Log("&c[S_Chat] O plugin foi desabilitado.");
	}
	
	public static void Log(String message) {
		Bukkit.getConsoleSender().sendMessage(message.replace("&", "§"));
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}
