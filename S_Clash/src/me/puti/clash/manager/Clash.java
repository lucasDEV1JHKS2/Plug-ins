package me.puti.clash.manager;

import java.util.HashMap;

import org.bukkit.Sound;

public class Clash  {

	String name, tag;
	HashMap<String,ClashPlayer> _members = new HashMap<String,ClashPlayer>();
	
	public Clash (String tag, String name) {
		
	}
	
	public void entrar(ClashPlayer clashPlayer) {
		_members.put(clashPlayer.getName(),clashPlayer);
		
		sendMessage("§a Novo Membro! §f["+clashPlayer.getName()+"]");
		sendMessage("");
		sendMessage("§e  Seja bem vindo ao Clash §f["+getName()+"]");
		sendMessage("",Sound.HORSE_JUMP);
	}
	
	public void sair(String name) {
		_members.remove(name);
		
		sendMessage("§cO membro [§f"+name+"§c] se retirou da clash.",Sound.PORTAL_TRIGGER);
	}
	
	public void sendMessage(String message) {
		for (ClashPlayer member : _members.values()) {
			member.sendMessage(""+message);
		}
	}
	
	public void sendMessage(String message, Sound sound) {
		for (ClashPlayer member : _members.values()) {
			member.sendMessage(""+message);
			member.Sound(sound);
		}
	}
	
	public String getTag() {
		return "&7["+tag.replace("&", "§")+"&7]";
	}
	
	public String getName() {
		return "&7["+name.replace("&", "§")+"&7]";
	}

}
