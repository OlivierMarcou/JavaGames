package net.arkaine.easter.poker.ihm.listeners;

import net.arkaine.easter.poker.lancement.Info;
import net.arkaine.easter.poker.process.Notifieur;
import net.arkaine.easter.poker.process.Partie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerPasser implements ActionListener {

	private static final ListenerPasser instance = new ListenerPasser();
	
	private ListenerPasser(){
		super();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Partie.PASSER=true;
		Info.addInfo("Vous passez");
		Notifieur.getInstance().start();
	}
	
	/**
	 * @return the instance
	 */
	public static ListenerPasser getInstance() {
		return instance;
	}
	

}
