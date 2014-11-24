package Hough;

import javax.swing.*;
import java.awt.*;
/**
 * Diese Klasse beinhaltet lediglich
 * die benoetigen ImageWindow Objekte und 
 * ruft deren paint-Methoden auf.
 * @author Rene Iser, Behrang Karimibabak, Simon Winkelbach
 * 
 */
public class HoughViewer extends JPanel{

    ImageWindow input,hough;
    
    HoughViewer(ImageWindow input,ImageWindow hough){
        this.input=input;
        this.hough=hough;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        input.paintComponent(g);
        hough.paintComponent(g);
    }
    
}
