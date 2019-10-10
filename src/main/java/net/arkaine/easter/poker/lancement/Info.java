package net.arkaine.easter.poker.lancement;

import net.arkaine.easter.poker.ihm.PanelInfo;

public class Info {
	
	public static void addTexte(String texte){
		PanelInfo.getInstance().ajouterTexte(texte, PanelInfo.STYLE_NORMAL);
	}
	
	public static void addInfo(String texte){
		PanelInfo.getInstance().ajouterTexte("      "+texte, PanelInfo.STYLE_INFO);
	}
	
	public static void addTitre(String texte){
		PanelInfo.getInstance().ajouterTexte(texte, PanelInfo.STYLE_TITRE);
	}

}
