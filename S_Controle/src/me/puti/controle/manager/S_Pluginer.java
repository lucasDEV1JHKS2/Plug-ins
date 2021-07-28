package me.puti.controle.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.controle.Controle;
import me.puti.controle.commands.SystemCommand;
import me.puti.controle.manager.plugin.S_Plugin;

public class S_Pluginer {

	private static List<S_Plugin> plugins = new ArrayList<S_Plugin>();
	private static S_Pluginer instance;
	
	public S_Pluginer() {
	}

	public S_Pluginer(JavaPlugin plugin) {
		plugin.getCommand("system").setExecutor(new SystemCommand());
		load_plugins(plugin);
		instance = this;
		
		Controle.Log("§e[S_Controle] O plugin foi habilitado.");
	}

	public static List<S_Plugin> getPlugins() {
		return plugins;
	}
	
	public static List<String> PluginToArray(){
		List<String> plugins = new ArrayList<String>();
		for (S_Plugin plugin : getPlugins()) {
			plugins.add(plugin.getName());
		}
		
		return plugins;
	}

	public static S_Plugin getPlugin(String name) {
		for (S_Plugin plugin : getPlugins()) {
			if (plugin.getName().equals(name)) {
				return plugin;
			}
		}
		return null;
	}

	public static S_Pluginer getInstance() {
		return instance;
	}

	public void reload_plugins(JavaPlugin instance) {
		plugins = new ArrayList<S_Plugin>();
		load_plugins(instance);
	}

	public void load_plugins(JavaPlugin instance) {
		try {
			int outros = 0;
			PluginManager pl = Bukkit.getPluginManager();
			for (Plugin plugin : pl.getPlugins()) {
				if (plugin.getName().startsWith("S_")) {
					if (!(plugin.getName().equals(instance.getName()))) {
						S_Plugin s_plugin = new S_Plugin(plugin.getName());
						plugins.add(s_plugin);
					}
				} else {
					outros++;
				}
			}
			if (outros > 0) {
				Controle.Log("&e[S_Controle] Foram ");
				return;
			}
		} catch (Exception e) {
			Controle.Log("&c[S_Controle] Não conseguir registrar os plugins.");
		}
	}

}
