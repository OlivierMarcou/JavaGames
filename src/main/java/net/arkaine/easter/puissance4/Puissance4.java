package net.arkaine.easter.puissance4; /*******************************************************************************
 *																			   *
 * 							Puissance 4 v1.0								   *
 *																			   *
 *					realise par Cyril Kern "DeathAngel"						   *
 *							cyril.kern@free.fr								   *
 *																			   *
 *******************************************************************************/
 
 
 /* Regles du jeu :
  *
  *	Le puissance 4 est un jeu pour deux joueurs
  * Le but est d'aligner en premier 4 pions de sa couleur (bleu ou rouge)
  *
  * Chaque joueur joue e tour de rele, la partie s'arrete des que 4 pions sont alignes
  *
  */							


import javax.swing.*;

class Puissance4 extends JFrame
{
	public static void main(String[] args)
	{
		//appel de la JFrame joueur
		JFrame jf=new Joueur();

	}
}