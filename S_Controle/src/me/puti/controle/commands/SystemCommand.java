package me.puti.controle.commands;



import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.controle.manager.S_Pluginer;
import me.puti.controle.manager.plugin.S_Plugin;
import me.puti.perms.S_Perms;

public class SystemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (S_Perms.getUser(p).hasPermissao("plugins.admin")) {

				return true;
			}
		}

		if (args.length == 0) {
			sender.sendMessage("§cUse: /system <plugin> <ação>.");
			return true;
		}

		String pluginName = "";
		String acao = "";
		try {
			pluginName = args[0];
		} catch (Exception e) {
			sender.sendMessage("§cInsira um argumento válido.");
		}

		if (args.length == 1 && !(pluginName.equalsIgnoreCase("plugins"))) {
			if (S_Pluginer.getPlugin(pluginName) == null && !(pluginName.equalsIgnoreCase("todos"))) {
				sender.sendMessage("§c" + pluginName + " não e um dos plugins do SrPuti_, verifiquei novamente.");
				return true;
			}

			sender.sendMessage("§e Ações - " + pluginName);
			sender.sendMessage("");
			sender.sendMessage(" §e* /system " + pluginName + " desativar §7- Desativar o plugin.");
			sender.sendMessage(" §e* /system " + pluginName + " ligar §7- Ligar o plugin.");
			sender.sendMessage(" §e* /system " + pluginName + " reiniciar §7- Recarregar o plugin.");
			if (!pluginName.equalsIgnoreCase("todos")) {
				sender.sendMessage(" §e* /system " + pluginName + " logs §7- Ver as logs recentes do plugin.");
				sender.sendMessage(" §e* /system " + pluginName + " status §7- Ver estátisticas do plugin.");
			}
			sender.sendMessage("");
			return true;
		} else {
			if (S_Pluginer.getPlugin(pluginName) != null) {
				S_Plugin plugin = S_Pluginer.getPlugin(pluginName);
				try {
					acao = args[1];
				} catch (Exception e) {
					sender.sendMessage("§cInsira uma ação válida para este plugin.");
				}

				if (acao.equalsIgnoreCase("desativar")) {

					if (!(plugin.IsEnable())) {
						sender.sendMessage("§c" + pluginName + " já está desativado no momento.");
						return true;
					}

					plugin.disable();
					plugin.update();
					sender.sendMessage("§e" + pluginName + " foi desativado.");
					return true;
				}

				if (acao.equalsIgnoreCase("ligar")) {

					if (plugin.IsEnable()) {
						sender.sendMessage("§c" + pluginName + " já está ligado no momento.");
						return true;
					}

					plugin.enable();
					plugin.update();
					sender.sendMessage("§e" + pluginName + " foi ligado.");
					return true;
				}

				if (acao.equalsIgnoreCase("reiniciar")) {

					if (!(plugin.IsEnable())) {
						sender.sendMessage("§cLigue-ó primeiro antes de reiniciar os sistema dele.");
						return true;
					}

					plugin.reload();
					sender.sendMessage("§e" + pluginName + " foi reiniciado.");
					return true;
				}

				if (acao.equalsIgnoreCase("logs")) {
					plugin.printlogs(sender);
					return true;
				}

				if (acao.equalsIgnoreCase("status")) {
					sender.sendMessage("§e Status - " + pluginName);
					sender.sendMessage("");
					sender.sendMessage("§eOnline: " + ((plugin.IsEnable()) ? "§aO" : "§cX"));
					sender.sendMessage(
							"§eMotificações: " + ((plugin.getUpdate().getLogs().size() > 0) ? "§aO" : "§cX"));
					sender.sendMessage("");
					return true;
				}

			} else if (args[0].equalsIgnoreCase("todos")) {
				try {
					acao = args[1];
				} catch (Exception e) {
					sender.sendMessage("§cInsira uma ação válida para este plugin.");
				}
				if (acao.equalsIgnoreCase("desativar")) {

					for (S_Plugin plugin : S_Pluginer.getPlugins()) {
						if (!(plugin.IsEnable())) {
							sender.sendMessage("§c" + plugin.getName() + " já está desativado no momento.");
						}

						plugin.disable();
						plugin.update();
					}
					sender.sendMessage("§e" + pluginName.toUpperCase() + " foram desativados.");
					return true;
				}

				if (acao.equalsIgnoreCase("ligar")) {

					for (S_Plugin plugin : S_Pluginer.getPlugins()) {

						if (plugin.IsEnable()) {
							sender.sendMessage("§c" + plugin.getName() + " já está ligado no momento.");
						}

						plugin.enable();
						plugin.update();
					}
					sender.sendMessage("§e" + pluginName.toUpperCase() + " foram ligados.");
					return true;
				}

				if (acao.equalsIgnoreCase("reiniciar")) {

					for (S_Plugin plugin : S_Pluginer.getPlugins()) {

						if (!(plugin.IsEnable())) {
							sender.sendMessage("§cLigue-ó "+plugin.getName()+" primeiro antes de reiniciar os sistema dele.");
							return true;
						}

						plugin.reload();
					}
					sender.sendMessage("§e" + pluginName.toUpperCase() + " foram reiniciados.");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("plugins")) {
				sender.sendMessage("§eS_Plugins ("+S_Pluginer.getPlugins().size()+"): §7"+S_Pluginer.PluginToArray());
			}
		}

		return true;
	}

}
