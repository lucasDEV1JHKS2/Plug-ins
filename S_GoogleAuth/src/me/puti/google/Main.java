package me.puti.google;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.google.manager.Manager;

public class Main extends JavaPlugin{

	private static Main instance;
	
    @Override
    public void onEnable() {
    	instance = this;
    	new Manager(instance);
    	
    }
    
    @Override
    public void onDisable() {

    	Log("&c[S_GoogleAuth] O plugin foi desabilitado.");
    }

    
    public static void Log(String message) {
    	Bukkit.getConsoleSender().sendMessage(message.replace("&", "§"));
    }
    
    public static Main getInstance() {
    	return instance;
    }

}
