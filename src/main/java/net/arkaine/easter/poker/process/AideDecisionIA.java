package net.arkaine.easter.poker.process;


import net.arkaine.easter.poker.ihm.FenetrePrincipale;
import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.combinaison.AbstractCombinaison;

public class AideDecisionIA {

	private AbstractCombinaison combinaisonIA;
	private AbstractCombinaison combinaisonPublique;
	private AbstractCombinaison combinaisonIAProbable;
	private AbstractCombinaison combinaisonPubliqueProbable;
	private boolean paroleInterdite;

	public AideDecisionIA(){
		paroleInterdite=false;
	}

	public int[] getOpportunite(int tour){
		int[] opportunites;
			actucaliserCombinaisons();
			int amelioration=combinaisonIA.getValeur()-combinaisonPublique.getValeur();
			int risque=combinaisonPubliqueProbable.getValeur()-combinaisonIAProbable.getValeur();
			if(risque<=0){
				if(amelioration>0){
					opportunites = new int[]{1, 5, 85};
				}
				else {
					opportunites = new int[]{30, 70, 30};
				}
			}
			else {
				if(amelioration>0){
					opportunites = new int[]{50, 20, 50};
				}
				else {
					opportunites = new int[]{90, 10, 0};
				}
			}
		return opportunites;
	}

	private void actucaliserCombinaisons(){
		Carte[] cartesAdversaire = new Carte[]{
				FenetrePrincipale.getInstance().getFlop1().getCarte(), 
				FenetrePrincipale.getInstance().getFlop2().getCarte(),
				FenetrePrincipale.getInstance().getFlop3().getCarte(), 
				FenetrePrincipale.getInstance().getTurn().getCarte(), 
				FenetrePrincipale.getInstance().getRiver().getCarte(),
				FenetrePrincipale.getInstance().getCarteAdversaire1().getCarte(),
				FenetrePrincipale.getInstance().getCarteAdversaire2().getCarte()
		};
		Carte[] cartesCommunes = new Carte[]{
				FenetrePrincipale.getInstance().getFlop1().getCarte(), 
				FenetrePrincipale.getInstance().getFlop2().getCarte(), 
				FenetrePrincipale.getInstance().getFlop3().getCarte(), 
				FenetrePrincipale.getInstance().getTurn().getCarte(), 
				FenetrePrincipale.getInstance().getRiver().getCarte(),
		};
		VerifierCombinaisons vcAdversaire = new VerifierCombinaisons();
		combinaisonIA = vcAdversaire.deteminerCombinaison(cartesAdversaire);
		combinaisonIAProbable = vcAdversaire.getCombinaisonProbable();
		VerifierCombinaisons vcPublique = new VerifierCombinaisons();
		combinaisonPublique = vcPublique.deteminerCombinaison(cartesCommunes);
		combinaisonPubliqueProbable = vcPublique.getCombinaisonProbable();
	}

	public Decision decider(int tour){
		Decision res;
		int[] opportunites=getOpportunite(tour);
		int somme=0;
		for(int i : opportunites){
			somme+=i;
		}
		double alea = somme*Math.random();
		int iPasser=opportunites[0];
		float iParole=(opportunites[0]+opportunites[0]);
		if(alea<iPasser){
			if(FenetrePrincipale.getInstance().getMise().getValeurMise()-FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise()==0 || FenetrePrincipale.getInstance().getMise().getValeurMise()<=0){
				res=Decision.PAROLE;
			}
			else {
				res=Decision.PASSER;
			}
		}
		else if (!paroleInterdite && (FenetrePrincipale.getInstance().getMise().getValeurMise()-FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise()==0 || FenetrePrincipale.getInstance().getMise().getValeurMise()<=0) && alea<iParole){
			res=Decision.PAROLE;
		}
		else {
			res=Decision.MISER;
			paroleInterdite=true;
		}
		return res;
	}

	public int calculerMontantMise(int tour, int montantRestant){
		int[] opportunites=getOpportunite(tour);
		int res=(int)(Math.random()*Math.random()*Math.random()*montantRestant*opportunites[2]/100);
		if(res > montantRestant*90/100){
			res=montantRestant;
		}
		else {
			res=res/10;
			res=res*10;
		}
		return res;
	}

}
