package me.puti.caixas.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.caixas.Main;
import me.puti.caixas.manager.Manager;
import me.puti.caixas.manager.caixa.Caixa;
import me.puti.controle.manager.S_Pluginer;
import me.puti.perms.S_Perms;

public class CaixaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}

		Player p = (Player) sender;
		if (args.length == 0) {
			p.sendMessage("§7 -- Comandos --");
			p.sendMessage("");
			if (S_Perms.getUser(p).hasPermissao("caixa.admin")) {
				p.sendMessage("§e /caixa give §7- Pega um caixa específica.");
				p.sendMessage("§e /caixa listar §7- Listar todas as caixas.");
				p.sendMessage("§e /caixa recarregar §7- Recarregar as caixas.");
			}
			p.sendMessage("§e /caixa ver §7- Visualizar os itens da caixa.");
			p.sendMessage("");
			return true;
		}
		if (args.length > 0) {
			String subcomando = args[0];
			if (!(S_Perms.getUser(p).hasPermissao("caixa.admin"))) {
				if (subcomando.equalsIgnoreCase("recarregar") || subcomando.equalsIgnoreCase("give")) {
					p.sendMessage("§cVocê não possui permissão para utilizar este comando.");
					return true;
				}
			}
			/*
			 * Recarregar Command
			 */
			if (subcomando.equalsIgnoreCase("recarregar")) {
				try {
					Manager.recarregar();
					p.sendMessage("§eTodas as caixas foram recarregadas.");
					S_Pluginer.getPlugin("S_Caixas").getUpdate()
							.logWarn("&eo jogador " + p.getName() + " recarregou as caixas.");
				} catch (Exception e) {
					S_Pluginer.getPlugin("S_Caixas").getUpdate()
							.logErro("&cOcorreu um erro ao tenta recarregar as caixas, localizado em: "
									+ e.getLocalizedMessage());
					p.sendMessage(
							"§cNão foi possível recarregar o plugin, veja o console e informe o erro ao desenvolvedor.");
					Main.Log("§cErro ao abaixo:");
					e.printStackTrace();
				}
				return true;
			}

			/*
			 * 
			 */
			if (subcomando.equalsIgnoreCase("ver")) {
				String caixaName = "";
				try {
					caixaName = args[1];
				} catch (Exception e) {
					p.sendMessage("§cArgumento inválido");
				}

				if (Manager.getCaixa(caixaName) != null) {
					Caixa caixa = Manager.getCaixa(caixaName);

					caixa.Ver(p);
					return true;
				} else {
					p.sendMessage("§cCaixas: " + Manager.getCCaixas());
				}
			}

			/*
			 * Listar Command
			 */
			if (subcomando.equalsIgnoreCase("listar")) {
				if (Manager.getCaixas().size() == 0) {
					p.sendMessage("§cNenhum caixa existente.");
					return true;
				} else {
					p.sendMessage("§cCaixas: " + Manager.getCCaixas());
					return true;
				}
			}

			/*
			 * Give Command
			 */
			if (subcomando.equalsIgnoreCase("give")) {
				String argumento = "";
				try {
					argumento = args[1];
				} catch (Exception e) {
					p.sendMessage("§cUse: /caixa give <jogador/todos> <" + Manager.getCCaixas() + ">.");
					return true;
				}

				if (argumento.equalsIgnoreCase("Todos")) {
					String caixaName = "";
					try {
						caixaName = args[2];
					} catch (Exception e) {
						p.sendMessage("§cUse: /caixa give " + argumento + " <" + Manager.getCCaixas() + ">.");
						return true;
					}

					if (Manager.getCaixa(caixaName) != null) {
						Caixa caixa = Manager.getCaixa(caixaName);
						for (Player jogador : Bukkit.getOnlinePlayers()) {
							caixa.Dar(jogador);
							jogador.sendMessage("§eVocê acabou de receber um caixa " + caixa.getName() + "§e.");
						}
						p.sendMessage("§eTodas as caixas foram entregues.");
						return true;
					} else {
						p.sendMessage("§cCaixas: " + Manager.getCCaixas());
						return true;
					}
				} else {
					String caixaName = "";
					try {
						caixaName = args[2];
					} catch (Exception e) {
						p.sendMessage("§cUse: /caixa give " + argumento + " <" + Manager.getCCaixas() + ">.");
					}

					if (Manager.getCaixa(caixaName) != null) {
						Caixa caixa = Manager.getCaixa(caixaName);
						caixa.Dar(p);
						p.sendMessage("§eVocê acabou de receber um caixa " + caixa.getName() + "§e.");
						return true;
					} else {
						p.sendMessage("§cCaixas: " + Manager.getCCaixas());
					}
					return true;
				}
			}
			return true;
		}
		return true;
	}

}
