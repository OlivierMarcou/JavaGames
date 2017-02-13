package com.oodrive.omnikles.depotclient.swing.component;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olivier on 09/02/17.
 */
public class AnimatedProgressBar extends JPanel {

    private HashMap<Integer, ImageIcon> images = new HashMap<>();
    private int actualIcon = 0;
    private int animationLen = 0;
    private JLabel icon = new JLabel();
    private JLabel text = new JLabel();
    private int sizeX= 400, sizeY =40;

    public JLabel getIcon() {
        return icon;
    }

    public void setIcon(JLabel icon) {
        this.icon = icon;
    }

    public JLabel getText() {
        return text;
    }

    public void setText(JLabel text) {
        this.text = text;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public HashMap<Integer, ImageIcon> getImages() {
        return images;
    }

    public void setImages(HashMap<Integer, ImageIcon> images) {
        this.images = images;
    }

    public int getActualIcon() {
        return actualIcon;
    }

    public void setActualIcon(int percent) {
        int newIcon = (images.size()*percent) /100;
        if(images.get(newIcon)!=null){
            this.actualIcon = newIcon;
            icon.setIcon(images.get(newIcon));
//            revalidate();
//            repaint();
        }
    }


    public AnimatedProgressBar(InputStream imageGif){
        setBackground(new Color(0x97abb8));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        add(icon, c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        add(text, c);

        decoposeGif(imageGif);
        icon.setIcon(images.get(actualIcon));
    }

    private void decoposeGif(InputStream imageGif){
        try {
            String[] imageatt = new String[]{
                    "imageLeftPosition",
                    "imageTopPosition",
                    "imageWidth",
                    "imageHeight"
            };

            ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream ciis = ImageIO.createImageInputStream(imageGif);
            reader.setInput(ciis, false);

            int noi = reader.getNumImages(true);
            BufferedImage master = null;

            for (int i = 0; i < noi; i++) {
                BufferedImage image = reader.read(i);
                IIOMetadata metadata = reader.getImageMetadata(i);

                Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node nodeItem = children.item(j);

                    if(nodeItem.getNodeName().equals("ImageDescriptor")){
                        Map<String, Integer> imageAttr = new HashMap<String, Integer>();

                        for (int k = 0; k < imageatt.length; k++) {
                            NamedNodeMap attr = nodeItem.getAttributes();
                            Node attnode = attr.getNamedItem(imageatt[k]);
                            imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
                        }
                        if(i==0){
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                        }
                        master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                        images.put(i, new ImageIcon(master.getScaledInstance(sizeX, sizeY,0)));
                    }
                }
//                ImageIO.write(master,"GIF",new File(i+".gif"));
            }
            animationLen=noi;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
