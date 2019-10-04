package net.arkaine.easter.puissance4; /*******************************************************************************
 * Cette classe represente une colonne du jeu du puissance 4 afin de faciliter *
 * les ajouts d'elements dans les colonnes et des differentes verifications	   *
 *******************************************************************************/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Colonne extends JPanel
{
	JLabel[] jl_case;		//les JLabel qui representeront les cases
	JButton jb;				//le bouton qui permet de rajouter des pions dans la colonne
	JPanel jp;				//le JPanel contenant tout les elements
	int hauteur;			//nombre d'elements dans la colonne
	Puissance4IHM puis;		//reference vers l'IHM principale
	int tab[];				//tableau d'entiers contenant la correspondance des joueurs
	
	
	//constructeur de la colonne
	public Colonne(Puissance4IHM p)
	{
		//on affecte une reference vers l'IHM principale
		this.puis=p;
		//on initialise le nombre d'elements de la colonne e 0
		this.hauteur=0;
		//on initialise le tableau de valeurs correspondant
		tab=new int[7];
		//on cree le bouton et on lui affecte l'image de la fleche
		this.jb=new JButton(new ImageIcon(this.getClass().getResource("/images/fleche.gif")));
		
		//creation de l'ecouteur pour le bouton d'ajout dans la colonne
		ActionListener al=new ActionListener(){
			// quand on clique sur le bouton
			public void actionPerformed(ActionEvent e)
			{
				// si la colonne n'est pas pleine
				if (!estPleine())
				{
					//on ajoute le pion du joueur en cours
					ajouter(puis.joueurCur);
					
					//on verifie si le joueur a reussi e aligner 4 pions
					if(puis.v.verifie(puis.joueurCur))
					{
						//si c'est le cas, on desactive les boutons de toutes les colonnes
						for (int i=0;i<7;i++)
							puis.col[i].disabled();
							
						//et on affiche la fenetre gagne
						JFrame jf2=new Gagne(puis.joueurCur,puis);
						jf2.pack();
						jf2.setVisible(true);
						
						if (puis.joueurCur==1)
						{
							puis.jl.setText(puis.j.j1+" a gagne !");
							puis.joueurCur=2;
						}
						else
						{
							puis.jl.setText(puis.j.j2+" a gagne !");
							puis.joueurCur=1;
						}
					}
					else if (puis.joueurCur==1)
					{
						//si c'etait le 1, ca devient le 2
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
				//si la colonne est pleine apres l'ajout
				if (estPleine())
					//on change l'image placee sur le bouton
					jb.setIcon(new ImageIcon(this.getClass().getResource("/images/croix.gif")));
			}
		};
		
		//initialisation du tableau de JLabel pour les cases
		this.jl_case=new JLabel[7];
		//on cree le JPanel
		this.jp=new JPanel(new GridLayout(8,1));

		//on ajoute l'ecouteur au bouton
		this.jb.addActionListener(al);
		//on rajoute le bouton dans le JPanel
		this.jp.add(this.jb);
		
		//pour toutes les cases qui vont etre crees
		//on parcourt les JLabel dans le sens inverse afin que le 0 soit en bas
		for (int i=6;i>=0;i--)
		{
			//on instancie le JLabel
			this.jl_case[i]=new JLabel(new ImageIcon(this.getClass().getResource("/images/vide.gif")));
			//et on le rajoute dans le JPanel
			(this.jp).add(this.jl_case[i]);
		}
	}
	
	//**************************************************************************
	// Renvoie le JPanel correspondant e la colonn
	// cette fonction n'est appelee qu'une fois au debut du programme
	public JPanel renvoyer()
	{
		return this.jp;
	}
	
	//*************************************************************************
	// rajoute un element dans la colonne en fonction du joueur courant
	public void ajouter(int joueur)
	{
		//on affiche l'image correspondant au joueur courant
		this.jl_case[hauteur].setIcon(new ImageIcon(this.getClass().getResource("/images/" +joueur+".gif")));
		//on affecte sa valeur dans le tableau correspondant
		this.tab[hauteur]=joueur;
	
		//et on augmente le nombre d'element contenus dans la colonne
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
	// renvoie la valeur de l'element se situant en hauteur h et renvoie -1 si erreur
	public int element(int h)
	{
		int res=-1;
		
		//si la colone n'est pas vide et qu'il y a assez d'elements
		if (!estVide() && h<hauteur)
		{
			//on effectue un try/catch au cas oe il y a depassement de capacite
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
	// Desactive le bouton
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
			jl_case[i].setIcon(new ImageIcon(this.getClass().getResource("/images/vide.gif")));
			tab[i]=0;
		}
		//on reactive le bouton de la colonne
		this.jb.setEnabled(true);
		//on reinitialise le nombre d'elements dans la colonne
		this.hauteur=0;
		//et on remet l'icene de fleche sur le bouton
		this.jb.setIcon(new ImageIcon(this.getClass().getResource("/images/fleche.gif")));
	}
}