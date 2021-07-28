package me.puti.armazem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.player.Armazem;

public class Main extends JavaPlugin {

	private static Main instance;
	@SuppressWarnings("unused")
	private static ArmazemAPI armazemApi = null;

	@Override
	public void onEnable() {
		instance = this;
		this.armazemApi = new ArmazemAPI();
		new Manager(instance);

	}

	@Override
	public void onDisable() {
		if (Manager.getArmazens().size() > 0) {
			for (Armazem am : Manager.getArmazens().values()) {
				try {
					am.save();
				} catch (Exception e) {
					Main.Log("§c[S_Armazem] Não foi possível salvar o armazem do jogador " + am.getName());
				}
			}
		} else {
			Log("§c[S_Armazem] Nenhum armazem criado durante a estádia online do servidor.");
		}

	}

	public static void Log(String message) {
		Bukkit.getConsoleSender().sendMessage(message.replace("&", "§"));
	}

	public static Main getInstance() {
		return instance;
	}

}
