package com.oodrive.omnikles.cryptodoc.swing.component.template.easter.demin;
//import com.incors.plaf.kunststoff.*; //look kunststoff si vous avez
import javax.swing.*;


public class Main {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel()); //look kunststoff
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    new Demineur(16,30,99,3);//hop, on lance le jeux en expert
  }
}
