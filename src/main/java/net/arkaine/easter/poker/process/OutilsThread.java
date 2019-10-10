package net.arkaine.easter.poker.process;

public class OutilsThread {
	
	private static final OutilsThread instance = new OutilsThread();
	
	private OutilsThread(){
		super();
	}
	
	public void attendre(){
		try {
			//Thread.sleep(500);
			Thread.sleep(500);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	public void attendreOperationUtilisateur(){
		synchronized (Notifieur.getInstance()) {
			try {
				Notifieur.getInstance().wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the instance
	 */
	public static OutilsThread getInstance() {
		return instance;
	}

}
