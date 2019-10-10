package net.arkaine.easter.poker.ihm.listeners;

import net.arkaine.easter.poker.lancement.Info;
import net.arkaine.easter.poker.process.Notifieur;
import net.arkaine.easter.poker.process.Partie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerSuivre implements ActionListener {

	private static final ListenerSuivre instance = new ListenerSuivre();

	private ListenerSuivre(){
		super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Partie.getInstance().suivre();
		Info.addInfo("Vous suivez");
		Notifieur.getInstance().start();
	}

	/**
	 * @return the instance
	 */
	public static ListenerSuivre getInstance() {
		return instance;
	}


}
