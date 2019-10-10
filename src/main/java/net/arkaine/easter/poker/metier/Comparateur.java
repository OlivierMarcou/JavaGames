package net.arkaine.easter.poker.metier;

import java.util.Comparator;

public class Comparateur implements Comparator<Carte>{

	@Override
	public int compare(Carte o1, Carte o2) {
		return o1.getValeur().compareTo(o2.getValeur());
	}

}
