package me.puti.chat.manager.task;

import org.bukkit.scheduler.BukkitRunnable;

import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.player.ChatUser;

public class ChatTask extends BukkitRunnable{

	@Override
	public void run() {
		for (ChatUser user : ChatManager.getUsers().values()) {
			user.update();
		}
	}
}
