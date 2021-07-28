package me.puti.chat.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.chat.Main;
import me.puti.chat.api.S_Config;
import me.puti.chat.commands.Global;
import me.puti.chat.commands.Tell;
import me.puti.chat.events.Local;
import me.puti.chat.events.PlayerEvents;
import me.puti.chat.manager.api.ChatAction;
import me.puti.chat.manager.menu.ChatMenu;
import me.puti.chat.manager.player.ChatUser;
import me.puti.chat.manager.task.ChatTask;

public class ChatManager {

	private static HashMap<String, ChatUser> players = new HashMap<String, ChatUser>();
	private static S_Config data;
	private static List<String> plugins = new ArrayList<String>();
	private static HashMap<String, ChatAction> chat_actions = new HashMap<String, ChatAction>();
	
	public ChatManager() {
		// TODO: Construct
	}
	
	public ChatManager(JavaPlugin plugin) {
		plugins.add("S_Perms");
		plugins.add("S_Economia");
		plugins.add("S_Clans");
		plugins.add("S_Rankup");
		plugin.getCommand("g").setExecutor(new Global());
		plugin.getCommand("tell").setExecutor(new Tell());
		Load_Apis();
		Load_Events(plugin);
		Load_Configs(plugin);
		
		Main.Log("&e[S_Chat] O plugin foi habilitado.");
	}
	
	public void Load_Apis() {
		for (String plugin : plugins) {
			if (Bukkit.getPluginManager().getPlugin(plugin) == null) {
				Main.Log("&c[S_Chat] O plugin de chat não completo o carregamento de dados, não foi encontrado a api : "+plugin);
			}
		}
	}
	
	public static ChatAction getAction(String name) {
		return chat_actions.get(name);
	}
	
	public static void setAction(String name, ChatAction action) {
		chat_actions.put(name, action);
	}

	public void Load_Configs(JavaPlugin plugin) {
		data = new S_Config(plugin, "data.yml");
		
		if (!(data.existeConfig())) {
			data.saveDefaultConfig();
		}
	}
	
	public static S_Config getdata() {
		return data;
	}
	
	public void ChatTask(JavaPlugin plugin) {
		new ChatTask().runTaskTimerAsynchronously(plugin, 20L, 60L);
	}
	
	private void Load_Events(JavaPlugin plugin) {
		PluginManager pl = Bukkit.getPluginManager();
		pl.registerEvents(new Local(), plugin);
		pl.registerEvents(new ChatMenu(), plugin);
		pl.registerEvents(new PlayerEvents(), plugin);
		pl.registerEvents(new ChatMenu(), plugin);
		}
	
	public static HashMap<String, ChatUser> getUsers(){
		return players;
	}

	public static void addUser(ChatUser user) {
		if (getUser(user.getName()) == null) {
			players.put(user.getName(), user);
		}
	}
	
	public static void removeUser(String name) {
		if (getUser(name) != null) {
			players.remove(name);
		}
	}

	public static ChatUser getUser(String name) {
		return players.get(name);
	}
	
	public static String getMensagem(String[] mensagem, int args) {
		String msg = "";
		for (int i = args;i < mensagem.length;i++) {
			msg = msg+" "+mensagem[i].replace("&", "§");
		}
		return msg;
	}
}
