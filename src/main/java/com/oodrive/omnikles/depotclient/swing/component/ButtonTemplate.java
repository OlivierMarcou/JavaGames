package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.pojo.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by olivier on 07/03/17.
 */
public class ButtonTemplate extends JButton{

    public ButtonTemplate(){
        super();
        init();
    }
    public ButtonTemplate(String texte){
        super(texte.toUpperCase());
        init();
    }

    private Color actualBackGroundColor = Design.BG_COLOR;
    private int raduisBorder = Design.BUTTON_MARGIN;
    private int margin = Design.BUTTON_RADUIS_BORDER;

    private void init() {
        setForeground(Design.FG_COLOR);
        setBackground(Design.BG_COLOR);
        setPreferredSize(Design.PREFERED_SIZE);
        setMargin(new Insets(5,0,0,0));
        setVerticalAlignment(CENTER);
        setFont(Design.BUTTON_FONT);
        setBorderPainted(false);
        setContentAreaFilled(false);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                actualBackGroundColor = Design.OVER_COLOR;
                repaint();
                revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                actualBackGroundColor = Design.BG_COLOR;
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d=(Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        g.setColor(actualBackGroundColor);
        g2d.fillRoundRect(margin, margin, w-margin-1, h-margin-1, raduisBorder, raduisBorder);
        g.setColor(Design.BUTTON_BORDER_COLOR);
        g2d.drawRoundRect(margin, margin, w-margin-1, h-margin-1, raduisBorder, raduisBorder);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        super.paintComponent(g);
    }

}
