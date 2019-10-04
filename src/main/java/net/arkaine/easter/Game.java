package net.arkaine.easter;

import javax.swing.*;


/**
 * Created by olivier on 16/09/16.
 */


public class Game {

    public static void main(String[] args){
        new MenuEasterEggs();
    }


    public static void changeLookAndFeel(int index, JFrame frame){
        try {
            String name = UIManager.getInstalledLookAndFeels()[index].getClassName();
            UIManager.setLookAndFeel(name);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
