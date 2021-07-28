package me.puti.armazem.manager.player;

import java.util.HashMap;

public enum ArmazemType {
	PEGUENO,MEDIO,GRANDE;
	
	private static HashMap<ArmazemType, Double> valores = new HashMap<ArmazemType, Double>();



	public static Double getValor(ArmazemType type) {
		if (valores.get(type) == null) {
			return 0D;
		}
		return valores.get(type);
	}
	
	public static void addValor(ArmazemType type, Double valor) {
		valores.put(type, valor);
	}
	
	public static int getStack(ArmazemType type) {
		if (type == PEGUENO) {
			return 16;
		}else if (type == MEDIO) {
			return 32;
		}else if (type == GRANDE) {
			return 64;
		}
		
		return 16;
	}
}
