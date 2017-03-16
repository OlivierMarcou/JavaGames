package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.pojo.Design;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olivier on 06/03/17.
 */
public class LineProgressBar extends Canvas   {

    private Dimension sizeMe = new Dimension(10, 2);
    private Color colorLine = Design.LINE_COLOR_FOOTER;
    private int etapes= 10;
    private int etapeActuelle=1;

    public int getEtapes() {
        return etapes;
    }

    public void setEtapes(int etapes) {
        this.etapes = etapes;
    }

    public int getEtapeActuelle() {
        return etapeActuelle;
    }

    public void setEtapeActuelle(int etapeActuelle) {
        this.etapeActuelle = etapeActuelle;
        repaint();
        revalidate();
    }


    LineProgressBar(JFrame parent){
        setBackground(Design.BG_COLOR_FOOTER);
        sizeMe = new Dimension(parent.getSize().width, Design.HEIGHT_PROGRESS_LINE_FOOTER);
        setSize(sizeMe.width, sizeMe.height);
        setMaximumSize(sizeMe);
        setMinimumSize(sizeMe);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int width = 0;
        if(etapes > 0)
            width = (sizeMe.width / etapes) * etapeActuelle;
        g2d.setBackground(Design.BG_COLOR_FOOTER);
        g2d.setColor(colorLine);
        Stroke stroke2 = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        g2d.setStroke(stroke2);
        g2d.drawLine(0, 100, width, 100);
//        g2d.drawLine(0, 0, width, 0);
//        g2d.drawLine(30, 100, 450, 100);
        g2d.dispose();
    }



}
