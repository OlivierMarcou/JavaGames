package net.arkaine.easter.poker.ihm.listeners;

import net.arkaine.easter.poker.ihm.FenetrePrincipale;
import net.arkaine.easter.poker.lancement.Info;
import net.arkaine.easter.poker.process.Notifieur;
import net.arkaine.easter.poker.process.Partie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerMiser implements ActionListener {
	
	private static final ActionListener instance = new ListenerMiser();

	@Override
	public void actionPerformed(ActionEvent e) {
		int somme=Integer.parseInt(FenetrePrincipale.getInstance().getSommeAMiser().getValue().toString());
		Info.addInfo("Vous misez "+somme);
		Partie.getInstance().miser(somme);
		Notifieur.getInstance().start();
	}

	/**
	 * @return the instance
	 */
	public static ActionListener getInstance() {
		return instance;
	}

}
