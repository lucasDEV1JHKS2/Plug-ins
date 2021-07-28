package me.puti.chat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.player.ChatUser;
import me.puti.perms.S_Perms;

public class Global implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSomente jogadores podem falar no chat.");
			return true;
		}
		
		if (args.length == 0) {
			sender.sendMessage("§c/g <mensagem>.");
			return true;
		}
		Player p = (Player) sender;
		if (ChatManager.getAction(p.getName()) != null) {
			switch (ChatManager.getAction(p.getName())) {
			case Cancelar:
				return true;
			case Surdo:
				return true;
			case Bloquear:
				return true;
			default:
				break;
			}
		}
		if (ChatManager.getUser(p.getName()) != null) {
			ChatUser user = ChatManager.getUser(p.getName());
			String msg = ChatManager.getMensagem(args, 0);
			
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (S_Perms.getUser(all).hasPermissao("chat.cor")) {
					all.sendMessage("");
					all.sendMessage("§e[G]§7 " + user.getFormat().replace("<message>", msg.replace("&", "§")));
					all.sendMessage("");
				} else {
					all.sendMessage("§e[G]§7 " + user.getFormat().replace("<message>", msg.replace("&", "§")));
				}

			}
		} else {
			p.sendMessage("§cOpa! parece que o chat paro de funcionar, que triste!");
		}
		return true;
	}

}
