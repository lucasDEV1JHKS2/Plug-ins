package me.puti.chat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.player.ChatUser;

public class Tell implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSomente jogadores podem utilizar este comando.");
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
		if (args.length == 0) {
			p.sendMessage("§cUse: /tell <jogador> <mensagem>");
			return true;
		}
		String playerName = null;
		String mensagem = null;
		try {
			playerName = args[0];
		} catch (Exception e) {
			p.sendMessage("§cInsira o jogador.");
		}
		try {
			mensagem = ChatManager.getMensagem(args, 1);
		} catch (Exception e) {
			p.sendMessage("§cInsira a mensagem");
		}
		if (mensagem != "" && playerName != "") {
			if (Bukkit.getPlayer(playerName) == null) {
				p.sendMessage("§cEste jogador não esta online no momento.");
				return true;
			}

			if (playerName.equals(p.getName())) {
				p.sendMessage("§cVocê não pode enviar mensagem para sí mesmo.");
				return true;
			}
			if (mensagem.length() == 0 || mensagem == "") {
				p.sendMessage("§cEscreve uma mensagem para enviar.");
				return true;
			}
			Player alvo = Bukkit.getPlayer(playerName);

			ChatUser user = ChatManager.getUser(p.getName());

			if (user == null) {
				p.sendMessage("§cParece que você não possui seu chat registrado.");
				return true;
			}

			p.sendMessage("§6[Privado] §7" + user.getFormatTell().replace("<message>", mensagem));
			alvo.sendMessage("§6[Privado] §7" + user.getFormatTell().replace("<message>", mensagem));
			return true;
		} else {
			p.sendMessage("§cEscreve uma mensagem para enviar.");

		}

		return true;
	}

}
