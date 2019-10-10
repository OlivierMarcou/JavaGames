package net.arkaine.easter.poker.process;

public class Notifieur extends Thread {
	
	private static Thread instance = new Notifieur();
	
	private Notifieur(){
		super();
	}
	
	@Override
	public void run(){
		synchronized (Partie.getInstance()) {
			//Partie.getInstance().notifyAll();
			instance = new Notifieur();
		}
	}

	public static Thread getInstance() {
		return instance;
	}
	
	

}
