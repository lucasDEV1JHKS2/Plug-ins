package me.puti.controle.manager.updatePlugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.puti.controle.Controle;
import me.puti.controle.api.S_Config;

public class Update {

	List<String> logs = new ArrayList<String>();
	String name;

	public Update(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<String> getLogs() {
		return logs;
	}

	public void printLogs(CommandSender p) {
		int limite = 0;
		for (String log : logs) {
			if (limite <= 10) {
				p.sendMessage(log.replace("&", "§"));
				limite++;
			}
		}
		Controle.Log(""+logs.size());
	}

	private String getDate() {
		DateTimeFormatter _current = DateTimeFormatter.ofPattern("yyyy/MMMM/dd HH:mm:ss");
		return _current.format(LocalDateTime.now());
	}

	private String getHours() {
		DateTimeFormatter _current = DateTimeFormatter.ofPattern("dd-MMMM");
		return _current.format(LocalDateTime.now());
	}

	public void logWarn(String message) {
		logs.add("[" + getDate() + "/LOG:WARN] > " + message.replace("§", "&"));
	}

	public void logErro(String message) {
		logs.add("[" + getDate() + "/LOG:ERRO] > " + message.replace("§", "&"));
	}

	public void save() {
		if (logs.size() > 0) {
			String name = getHours() + " " + getName();
			S_Config save = new S_Config(Controle.getInstance(), name + ".yml");

			save.set(getName(), logs);
			save.saveConfig();
		}
	}

}
