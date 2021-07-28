package me.puti.controle.manager.plugin;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import me.puti.controle.manager.updatePlugin.Update;

public class S_Plugin {

	String name;
	Update update = null;
	boolean enable = false;

	public S_Plugin(String pluginName) {
		this.name = pluginName;
		this.update = new Update(pluginName);
		this.enable = getPlugin().isEnabled();
	}

	public String getName() {
		return name;
	}
	
	public void printlogs(CommandSender p) {
		getUpdate().printLogs(p);
	}

	public Plugin getPlugin() {
		PluginManager pl = Bukkit.getPluginManager();
		return pl.getPlugin(getName());
	}

	public void disable() {
		getUpdate().logWarn("&eplugin habilitado.");
		PluginManager pl = Bukkit.getPluginManager();
		pl.disablePlugin(getPlugin());
	}

	public void enable() {
		getUpdate().logWarn("&cplugin disabilitado.");
		PluginManager pl = Bukkit.getPluginManager();
		pl.enablePlugin(getPlugin());
	}

	public void erro(Exception ex, boolean isdisable) {
		getUpdate().logErro("&cS_Exception --> " + ex);
		if (isdisable) {
			disable();
		}
	}

	public Update getUpdate() {
		return update;
	}

	public boolean IsEnable() {
		return enable;
	}
	
	public void update() {
		this.enable = getPlugin().isEnabled();
	}

	public void reload() {
		getUpdate().logWarn("&erecarregando plugin.");
		disable();
		enable();
	}

}
