package me.puti.caixas.manager.caixa;

public enum CaixaType {
	Commum,Basica,Rara,Lendaria;

	
	public static String getType(CaixaType type) {
		String tipo = "";
		switch (type) {
		case Commum:
			tipo = "Commum";
			break;
		case Basica:
			tipo = "Básica";
			break;
		case Lendaria:
			tipo = "Lendária";
			break;
		case Rara:
			tipo = "Rara";
			break;
		default:
			tipo = "Commum";
			break;
		}
		
		return tipo;
	}
}
