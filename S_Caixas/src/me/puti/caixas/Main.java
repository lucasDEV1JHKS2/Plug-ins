package me.puti.caixas;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.caixas.manager.Manager;

public class Main extends JavaPlugin{
	
	private static Main instance;
	
	
	@Override
	public void onEnable() {
		instance = this;
		new Manager(instance);
	}
	
	@Override
	public void onDisable() {

		Log("&e[S_Caixas] O plugin foi desabilitado.");
	}
	
	
	public static Main getInstance() {
		return instance;
	}
	
	public static void Log(String message) {
		Bukkit.getConsoleSender().sendMessage(message.replace("&","§"));
	}
	

}
