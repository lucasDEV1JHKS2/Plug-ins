package me.puti.chat.manager.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.puti.chat.api.S_Config;
import me.puti.chat.manager.ChatManager;
import me.puti.clans.Clans;
import me.puti.economia.publish.EconomiaAPI;
import me.puti.perms.S_Perms;
import me.puti.rankup.Rankup;

public class ChatUser {

	public enum ChatStatus {
		Ativado, Desativado;
	}

	String name = "";
	String prefix = "";
	String rank = "";
	String clanName = "";
	String magnata = "";
	ChatStatus tell = ChatStatus.Ativado;
	ChatStatus chat = ChatStatus.Ativado;

	public ChatUser(String name) {
		this.name = name;
		S_Config data = ChatManager.getdata();
		this.tell = ((data.getString("data." + getName()) != null)
				? ChatStatus.valueOf(data.getString("data." + getName() + ".tell"))
				: ChatStatus.Ativado);
		this.tell = ((data.getString("data." + getName()) != null)
				? ChatStatus.valueOf(data.getString("data." + getName() + ".chat"))
				: ChatStatus.Ativado);
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(getName());
	}

	public boolean IsTell(ChatStatus status) {
		if (status == tell) {
			return true;
		} else {
			return false;
		}
	}

	public void setTell(ChatStatus status) {
		this.tell = status;
	}

	public void setChat(ChatStatus status) {
		this.chat = status;
	}

	public boolean IsChat(ChatStatus status) {
		if (status == chat) {
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix.replace("&", "§");
	}

	public String getRank() {
		return rank.replace("&", "§");
	}

	public String getClanName() {
		if (clanName == null || clanName == "") {
			return "";
		} else {
			return " §7[" + clanName.replace("&", "§") + "§7] ";
		}
	}

	public String getMagnata() {
		return magnata;
	}

	public String getFormat() {
		return getMagnata() + " " + getRank() + getClanName() + getPrefix() + " §7" + getName() + " §7: <message>";
	}
	
	public String getFormatTell() {
		return getPrefix() + " §7" + getName() + " §7: <message>";
	}

	public void sendMessage(String message) {
		Bukkit.getPlayer(getName()).sendMessage(message.replace("&", "§"));
	}

	public void update() {
		try {
			if (Rankup.getRankUser(name).getRank() != null) {
				this.rank = Rankup.getRankUser(name).getRank().getName().replace("&", "§");
			}
			this.prefix = S_Perms.getUser(name).getGrupo().getPrefix().replace("&", "§");
			if (Clans.getClan(name) != null) {
				this.clanName = Clans.getClan(name).getTagColorida().replace("&", "§");
			} else {
				this.clanName = "";
			}
			this.magnata = EconomiaAPI.IsMagnata(getName());
		} catch (Exception e) {
			sendMessage("&cNão foi possível atualizar suas tags ao chat.");
			e.printStackTrace();
		}

	}

}
