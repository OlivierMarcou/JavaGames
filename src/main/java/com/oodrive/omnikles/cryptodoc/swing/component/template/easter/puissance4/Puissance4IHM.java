package com.oodrive.omnikles.cryptodoc.swing.component.template.easter.puissance4; /************************************
 * IHM du programme de puissance 4 	*
 ************************************/			


import javax.swing.*;
import java.awt.*;

public class Puissance4IHM extends JFrame
{
	JPanel tout;		//JPanel contenant toutes les colonnes du puissance 4
	Colonne[] col;		//tableau de 'colonne' contenant les références vers chaque objet 'colonne'
	int joueurCur;		//numéro du joueur courant (1 ou 2)
	Verification v;
	Joueur j;
	JLabel jl;

	String defaut="C'est le tour de ";
	
	//constructeur de l'IHM principale
	public Puissance4IHM(String titre, Joueur jo)
	{
		//affectation du titre à la Frame
		super(titre);
	
		this.j=jo;
		
		//Le joueur courant est le joueur 1
		this.joueurCur=1;
		
		//instanciation du JPanel général 
		this.tout=new JPanel(new GridLayout(1,7));

		//instanciation du tableau de 'colonne'
		this.col=new Colonne[7];
		
		//pour toutes les colonnes du puissance 4
		for (int i=0;i<7;i++)
		{
			//on crée un nouvel objet colonne
			this.col[i]=new Colonne(this);
			//et on récupère le JPanel correspondant qu'on ajoute dans 'tout'
			(this.tout).add((this.col[i]).renvoyer());
		}
		
		//Création d'une instance de vérification
		v=new Verification(this.col);

		jl=new JLabel(defaut + this.j.j1);
		
		JPanel jpl=new JPanel();
		jpl.add(jl);
		
		//ajout de 'tout' dans la JFrame principale
		this.getContentPane().add(this.tout,BorderLayout.CENTER);
		this.getContentPane().add(jpl,BorderLayout.SOUTH);
	}


	//**************************************************************************
	// Réinitialisation du programme
	public void reinit()
	{
		//on réinititalise la vérification
		v.reinit();
		
		//on réinitialise les colonnes
		for (int i=0;i<7;i++)
		{
			this.col[i].reinit();
		}
		
		if (joueurCur==1)
		{
			jl.setText(defaut+j.j1);
		}
		else
		{
			jl.setText(defaut+j.j2);
		}
	}
}