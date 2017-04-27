package com.oodrive.omnikles.cryptodoc.swing.component.template.easter.puissance4; /*******************************************************************************
 * Fenêtre qui s'affiche lorsque l'un des deux joueurs gagne la partie		   *
 *******************************************************************************/

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Gagne extends JFrame
{
	Puissance4IHM puis;		//référence vers l'IHM appelante
	
	//constructeur
	public Gagne(int i, Puissance4IHM p)
	{
		//affectation du titre
		super("Gagné !");
		
		//référence vers l'IHM
		this.puis=p;
		
		//écouteur pour le bouton nouvelle partie
		ActionListener al_new=new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//on réinititalise le programme
				puis.reinit();
				
				//on cache la boîte de dialogue courante
				setVisible(false);
			}
		};
		
		//écouteur pour le bouton quitter
//		ActionListener al_quit=new ActionListener(){
//			public void actionPerformed(ActionEvent e)
//			{
//				p.closeGame();
//			}
//		};
		
		//création des boutons nouvelle partie et quitter
		JButton jb_new=new JButton("Nouvelle partie");
//		JButton jb_quitter=new JButton("Quitter");
		
		//ajout des écouteurs
		jb_new.addActionListener(al_new);
//		jb_quitter.addActionListener(al_quit);
		
		//Création du JPanel qui contiendra les boutons et ajout des boutons
		JPanel jp_bouton=new JPanel();
		jp_bouton.add(jb_new);
//		jp_bouton.add(jb_quitter);
		
		String nom="";
		
		//Création des JLabel contenant le texte de victoire
		if (i==1)
			nom=puis.j.j1;
		else
			nom=puis.j.j2;
		
		JLabel jl=new JLabel("Félicitations "+nom+" !",(int) JLabel.CENTER_ALIGNMENT);
		JLabel jl2=new JLabel("Vous avez gagné !", (int) JLabel.CENTER_ALIGNMENT);
		
		//Création des JLabel contenant les images
		JLabel jl_etoile=new JLabel(new ImageIcon(this.getClass().getResource("/image/etoile.gif")));
		JLabel jl_etoile2=new JLabel(new ImageIcon(this.getClass().getResource("/image/etoile2.gif")));
		
		//Création des JPanel
		JPanel jp=new JPanel(new BorderLayout());
		JPanel jp2=new JPanel(new BorderLayout());
		
		//ajout des étoiles
		jp.add(jl_etoile,BorderLayout.EAST);
		jp.add(jl_etoile2,BorderLayout.WEST);
		
		//ajout des textes et des boutons
		jp2.add(jl,BorderLayout.NORTH);
		jp2.add(jl2,BorderLayout.CENTER);
		jp2.add(jp_bouton,BorderLayout.SOUTH);
		
		//réunion des deux JPanel
		jp.add(jp2,BorderLayout.CENTER);
		
		//affectation du JPanel à la JFrame
		this.getContentPane().add(jp);
	}
}