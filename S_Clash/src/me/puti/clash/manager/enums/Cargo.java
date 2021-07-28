package me.puti.clash.manager.enums;

public enum Cargo {
	Visitante("§f[Visitante]"),Membro("§7[Membro]"),SubCapitão("§a[SubCapitão]"),Capitão("&e[Capitão]");

	String string;
	
	Cargo(String string) {
		this.string = string;
	}
	
	public String getTag() {
		return string;
	}
	
	public String getName() {
		return name();
	}
}
