package me.puti.discord;


import java.util.Collections;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Bot extends JavaPlugin {
	
	private static String _TOKEN = "NjUwNTA3NzY3MTQ4MzE0NjQ2.XeMWkQ.TNTEK43tuFz5nsZEhQYk_K3K528";
	private static JDA jda;
	
	
	@Override
	public void onEnable() {
		carrega_Bot();
	}
	
	
	public void carrega_Bot() {
		try {
			jda = JDABuilder.createDefault(_TOKEN,Collections.emptyList())
            .addEventListeners(new Bot())
            .setActivity(Activity.playing("Desenvolvendo meus sistemas..."))
            .build();
			Log("&eBot conectado com sucesso.");
		} catch (LoginException e) {
			Log("&eNão foi possível conectar ao bot.");
		}
	}


	public void Log(String message) {
		Bukkit.getConsoleSender().sendMessage("&a[S_Bot]§7 "+message.replace("&", "§"));
	}
}
