package net.arkaine.easter.poker.ihm.listeners;

import net.arkaine.easter.poker.lancement.Info;
import net.arkaine.easter.poker.process.Notifieur;
import net.arkaine.easter.poker.process.Partie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerParole implements ActionListener {

	private static final ListenerParole instance = new ListenerParole();
	
	private ListenerParole(){
		super();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Partie.getInstance().suivre();
		Info.addInfo("Vous checkez");
		Notifieur.getInstance().start();
	}
	
	/**
	 * @return the instance
	 */
	public static ListenerParole getInstance() {
		return instance;
	}
	

}
