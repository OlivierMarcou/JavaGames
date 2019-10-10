package net.arkaine.easter.poker.process;

import net.arkaine.easter.poker.ihm.FenetrePrincipale;
import net.arkaine.easter.poker.lancement.Info;

public class IA  {

	private static final IA instance = new IA();
	private AideDecisionIA aideDecision;

	private IA(){
		super();
		aideDecision = new AideDecisionIA();
	}

	public void jouer(int tour) {
		if(FenetrePrincipale.getInstance().getSommeRestante()==0 || FenetrePrincipale.getInstance().getSommeAdverse()==0){
			Partie.getInstance().miseAdverse(0);
		}
		else {
			if(tour==0){
				aideDecision = new AideDecisionIA();
				if(Math.max(0, FenetrePrincipale.getInstance().getMise().getValeurMise()- FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise())<60){
					Partie.getInstance().miseAdverse(Math.max(0, FenetrePrincipale.getInstance().getMise().getValeurMise()- FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise()));
				}
				else {
					Partie.IA_PASSER=true;
				}
			}
			else {
				aideDecision = new AideDecisionIA();
				Decision decision = aideDecision.decider(tour);
				switch (decision) {
				case PASSER :
					Partie.IA_PASSER=true;
					Info.addInfo("IA passe");
					break;
				case PAROLE :
					int difference=FenetrePrincipale.getInstance().getMise().getValeurMise()-FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise();
					if(difference>0){
						if(Math.random()>0.5){
							Partie.getInstance().miseAdverse(difference);
							Info.addInfo("IA mise "+difference);
						}
						else {
							Partie.IA_PASSER=true;
							Info.addInfo("IA passe");
						}
					}
					else {
						FenetrePrincipale.getInstance().getMiseAdversaire().setValeurMise(0);
						Info.addInfo("IA parole");
					}
					break;
				case MISER :
					int aMiser=aideDecision.calculerMontantMise(tour, FenetrePrincipale.getInstance().getSommeAdverse());
					int difference2=FenetrePrincipale.getInstance().getMise().getValeurMise()-FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise();
					aMiser=Math.max(difference2, aMiser);
					Partie.getInstance().miseAdverse(aMiser);
					Info.addInfo("IA mise "+aMiser);
					break;
				}
			}
			OutilsThread.getInstance().attendre();
		}
	}

	/**
	 * @return the instance
	 */
	public static IA getInstance() {
		return instance;
	}

}
