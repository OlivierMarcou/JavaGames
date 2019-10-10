package net.arkaine.easter.poker.lancement;

import net.arkaine.easter.poker.ihm.FenetrePrincipale;
import net.arkaine.easter.poker.process.Partie;

import javax.swing.*;

public class Lanceur {
	
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){}
		FenetrePrincipale.getInstance();
		Partie.getInstance().start();
	}

}
