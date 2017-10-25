package com.oodrive.omnikles.cryptodoc.swing.component.template.easter.puissance4; /*******************************************************************************
 * Cette classe représente une colonne du jeu du puissance 4 afin de faciliter *
 * les ajouts d'éléments dans les colonnes et des différentes vérifications	   *
 *******************************************************************************/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Colonne extends JPanel
{
	JLabel[] jl_case;		//les JLabel qui représenteront les cases
	JButton jb;				//le bouton qui permet de rajouter des pions dans la colonne
	JPanel jp;				//le JPanel contenant tout les éléments
	int hauteur;			//nombre d'éléments dans la colonne
	Puissance4IHM puis;		//référence vers l'IHM principale
	int tab[];				//tableau d'entiers contenant la correspondance des joueurs
	
	
	//constructeur de la colonne
	public Colonne(Puissance4IHM p)
	{
		//on affecte une référence vers l'IHM principale
		this.puis=p;
		//on initialise le nombre d'éléments de la colonne à 0
		this.hauteur=0;
		//on initialise le tableau de valeurs correspondant
		tab=new int[7];
		//on crée le bouton et on lui affecte l'image de la fleche
		this.jb=new JButton(new ImageIcon(this.getClass().getResource("/image/fleche.gif")));
		
		//création de l'écouteur pour le bouton d'ajout dans la colonne
		ActionListener al=new ActionListener(){
			// quand on clique sur le bouton
			public void actionPerformed(ActionEvent e)
			{
				// si la colonne n'est pas pleine
				if (!estPleine())
				{
					//on ajoute le pion du joueur en cours
					ajouter(puis.joueurCur);
					
					//on vérifie si le joueur a réussi à aligner 4 pions
					if(puis.v.verifie(puis.joueurCur))
					{
						//si c'est le cas, on désactive les boutons de toutes les colonnes
						for (int i=0;i<7;i++)
							puis.col[i].disabled();
							
						//et on affiche la fenètre gagne
						JFrame jf2=new Gagne(puis.joueurCur,puis);
						jf2.pack();
						jf2.setVisible(true);
						
						if (puis.joueurCur==1)
						{
							puis.jl.setText(puis.j.j1+" Win !");
							puis.joueurCur=2;
						}
						else
						{
							puis.jl.setText(puis.j.j2+" Win !");
							puis.joueurCur=1;
						}
					}
					else if (puis.joueurCur==1)
					{
						//si c'était le 1, ca devient le 2
						puis.joueurCur=2;
						puis.jl.setText(puis.defaut+puis.j.j2);
					}
					else
					{
						//et inversement
						puis.joueurCur=1;
						puis.jl.setText(puis.defaut+puis.j.j1);
					}
				}	
				//si la colonne est pleine après l'ajout
				if (estPleine())
					//on change l'image placée sur le bouton
					jb.setIcon(new ImageIcon(this.getClass().getResource("/image/croix.gif")));
			}
		};
		
		//initialisation du tableau de JLabel pour les cases
		this.jl_case=new JLabel[7];
		//on crée le JPanel
		this.jp=new JPanel(new GridLayout(8,1));

		//on ajoute l'écouteur au bouton
		this.jb.addActionListener(al);
		//on rajoute le bouton dans le JPanel
		this.jp.add(this.jb);
		
		//pour toutes les cases qui vont être créés
		//on parcourt les JLabel dans le sens inverse afin que le 0 soit en bas
		for (int i=6;i>=0;i--)
		{
			//on instancie le JLabel
			this.jl_case[i]=new JLabel(new ImageIcon(this.getClass().getResource("/image/vide.gif")));
			//et on le rajoute dans le JPanel
			(this.jp).add(this.jl_case[i]);
		}
	}
	
	//**************************************************************************
	// Renvoie le JPanel correspondant é la colonn
	// cette fonction n'est appelée qu'une fois au début du programme
	public JPanel renvoyer()
	{
		return this.jp;
	}
	
	//*************************************************************************
	// rajoute un élément dans la colonne en fonction du joueur courant
	public void ajouter(int joueur)
	{
		//on affiche l'image correspondant au joueur courant
		this.jl_case[hauteur].setIcon(new ImageIcon(this.getClass().getResource("/image/" +joueur+".gif")));
		//on affecte sa valeur dans le tableau correspondant
		this.tab[hauteur]=joueur;
	
		//et on augmente le nombre d'élement contenus dans la colonne
		this.hauteur++;
	}
	
	
	//**************************************************************************
	// teste si la colonne est pleine
	public boolean estPleine()
	{
		return hauteur==7;
	}
	
	//**************************************************************************
	// teste si la colonne est vide
	public boolean estVide()
	{
		return hauteur==0;
	}
	
	
	//**************************************************************************
	// renvoie la valeur de l'élément se situant en hauteur h et renvoie -1 si erreur
	public int element(int h)
	{
		int res=-1;
		
		//si la colone n'est pas vide et qu'il y a assez d'éléments
		if (!estVide() && h<hauteur)
		{
			//on effectue un try/catch au cas ou il y a dépassement de capacité
			//du tableau
			try
			{
				res=tab[h];
			}
			//si c'est le cas, on renvoit -1
			catch (Exception e)
			{
				res=-1;
			}
		}
		
		return res;
	}
	
	
	//**************************************************************************
	// Désactive le bouton
	public void disabled()
	{
		this.jb.setEnabled(false);
	}
	
	//**************************************************************************
	//reinitialise la colonne
	public void reinit()
	{
		//on vide toutes les cases
		for (int i=0;i<7;i++)
		{
			jl_case[i].setIcon(new ImageIcon(this.getClass().getResource("/image/vide.gif")));
			tab[i]=0;
		}
		//on réactive le bouton de la colonne
		this.jb.setEnabled(true);
		//on réinitialise le nombre d'éléments dans la colonne
		this.hauteur=0;
		//et on remet l'icone de fléche sur le bouton
		this.jb.setIcon(new ImageIcon(this.getClass().getResource("/image/fleche.gif")));
	}
}