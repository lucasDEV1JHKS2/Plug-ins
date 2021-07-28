package me.puti.controle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.controle.manager.S_Pluginer;
import me.puti.controle.manager.plugin.S_Plugin;

public class Controle extends JavaPlugin {

	private static Controle instance;

	@Override
	public void onEnable() {
		instance = this;
		new S_Pluginer(instance);
	}

	@Override
	public void onDisable() {
		if (S_Pluginer.getPlugins().size() > 0) {
			for (S_Plugin plugin : S_Pluginer.getPlugins()) {
				plugin.getUpdate().save();
			}
		}
		Log("§c[S_Controle] O plugin foi desabilitado.");
	}

	public static void Log(String message) {
		Bukkit.getConsoleSender().sendMessage(message.replace("&", "§"));
	}

	public static Controle getInstance() {
		return instance;
	}

}
