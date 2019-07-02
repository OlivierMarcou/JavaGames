package com.oodrive.omnikles.cryptodoc.swing.component.template.easter.puissance4;

public class Verification
{
	Colonne[] col;		//tableau de 'colonne' faisant reference aux JPanel
	boolean trouve;		//vrai si on a trouve un alignement
	
	int x,y;
	int alg;
	
	//constructeur de verification
	public Verification(Colonne[] c)
	{
		this.col=c;				//on effectue une reference vers les colonnes
		this.trouve=false;		//on n'a pas encore d'alignement
	}
	
	//**************************************************************************
	// Verifie si 4 pions du joueur ayant le numero passe en parametre
	// sont alignes
	public boolean verifie(int joueur)
	{
		int posx,posy;	//position courante dans les cases
		boolean possible;	//vrai si C possible qu'il y ait un alignement e cette position
		int alg=0;			//valeur de l'alignement
		int savex=0,savey=0;
		int nbr=0;
		
		//test pour voir si 4 pions sont alignes horizontalement (6)
		
		//pour chaque ligne du puissance 4
		for (y=0;y<7 && !this.trouve;y++)
		{
			//on parcourt toutes les colonnes
			for (x=0;x<7 && !this.trouve;x++)
			{
				possible=true;
				nbr=0;
				//pour chaque case correspondant e l'alignement
				for (posx=x;possible && posx<x+4;posx++)
				{
					//l'alignement est possible si la valeur de la case
					//est la meme que le numero du joueur
					
					//on effectue un try/carch, ainsi on n'a pas e se soucier des depassement
					//de capacite des tableaux
					try
					{
						possible=(col[posx].element(y)==joueur);
						nbr++;
					}
					//s'il y a une erreur c'est que l'alignement est impossible
					catch (Exception e)
					{
						possible=false;
					}
				}	
				
				//si on a trouve 4 pions alignes
				if (possible && nbr==4)
				{
					//alors on a trouve un alignement
					this.trouve=true;
					//on sauvegarde sa direction
					alg=6;
					//ainsi que ses coordonnees de depart
					savex=x;
					savey=y;
				}
			}	
		}
		
		//test pour voir si 4 pions sont alignes verticalement (8)
		for (x=0;x<7 && !this.trouve;x++)
		{
			for (y=0;y<7 && !this.trouve;y++)
			{
				possible=true;
				nbr=0;
				for (posy=y;posy<y+4 && possible;posy++)
				{
					try
					{
						possible=(col[x].element(posy)==joueur);
						nbr++;
					}
					catch (Exception e)
					{
						possible=false;
					}
				}
				
				if (possible && nbr==4)
				{
					this.trouve=true;
					alg=8;
					savex=x;
					savey=y;
				}
			}
		}
		
		//test pour voir si 4 pions sont alignes en diagonale (9)
		for (x=0;x<7 && !this.trouve;x++)
		{
			for (y=0;y<7 && !this.trouve;y++)
			{
				possible=true;
				nbr=0;
				for (int i=0;possible && i<4;i++)
				{
					posx=x+i;
					posy=y+i;
					try
					{
						possible=(col[posx].element(posy)==joueur);
						nbr++;
					}
					catch (Exception e)
					{
						possible=false;
					}
				}
				
				if (possible && nbr==4)
				{
					this.trouve=true;
					alg=9;
					savex=x;
					savey=y;
				}
			}
		}
		
		//test pour voir si 4 pions sont alignes en diagonale (3)
		for (x=0;x<7 && !this.trouve;x++)
		{
			for (y=6;y>=0 && !this.trouve;y--)
			{
				possible=true;
				nbr=0;
				
				for (int i=0;possible && i<4;i++)
				{
					posx=x+i;
					posy=y-i;
					
					try
					{
						possible=(col[posx].element(posy)==joueur);
						nbr++;
					}
					catch (Exception e)
					{
						possible=false;
					}
				}
				
				if (possible && nbr==4)
				{
					this.trouve=true;
					alg=3;
					savex=x;
					savey=y;
				}
			}
		}
			
		return this.trouve;				
	}
	
	//**************************************************************************
	//reinitialisation de la verification
	public void reinit()
	{
		this.trouve=false;
	}
}