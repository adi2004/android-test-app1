
package Hough;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.*;

/**
 * 
 * Diese Klasse realisert die Hough-Transformation.
 * Es koennen sowohl Punkte als auch Linien transformiert werden.
 * Zusaetzlich besteht die moeglichkeit sich eine vorher ausgewaehlte Anzahl von Punkten
 * mit maximalem Wert im Hough-Raum zuruecktransformieren zu lassen und sich die
 * detektierten Geraden grafisch darstellen zu lassen.
 * Die Ausgabe des Houghraumes erfolgt zur besseren Darstellung
 * in logarithmierter Form.
 * @author Rene Iser, Behrang Karimibabak, Simon Winkelbach
 *
 */
public class HoughApplet extends JApplet implements MouseInputListener{
    
    /*
     * A ist das Eingangsbild,B stellt den Houghraum dar.
     * calculateMax wird benoetigt um die Maxima zu finden,es ist
     * eine Kopie von B. Naehere Erklaerung siehe calcMax().
     */
    
    protected iRPImage A,B,calculateMax; 
    protected ImageWindow input,hough;
    protected HoughViewer viewer;
    protected int x1,x2,y1,y2;  //Punkte um die Geraden zu zeichnen.
    protected int d1,d2,r1,r2;
    protected int numberOfMax=1; //Anzahl der gesuchten Maxima.
    protected int[][] maxLineCordinatesArray; // Maxima werden hier gespeichert. Pro Maximum
    // 4 Felder, da zwei Punkte definiert werden,um die Gerade im Bildraum 
    // einzuzeichnen.
    protected int[][] maxPosArray=new int[5][2];    
    protected int maxCounter; // Zaehler wie viele Maxima bereits gefunden wurden.
    protected boolean mouseOnLeftImage=false,mouseOnRightImage=false;
    protected boolean drawTheLine=false; // Ist die Variable auf 'true' werden Linien in
    // die Bilder selbst gezeichnet.
    protected boolean point,line,firstMousePress,findMax,updateArray; //Boolsche Werten dienen der
    //Fallunterscheidung
    protected boolean chooseCordSystem;
    protected final int picSize=300;
    protected final int xFromLeft=10;
    protected final int yFromTop=20;
    protected final int betweenThePics=20;
    protected final int scalingOfA=6;
    protected int xDep,yDep,xDes,yDes; //depart und destination
    protected JPanel operationPanel,messagePanel;
    protected JPanel[] toolMainPanel,toolSubPanel;
    protected JDialog toolDialog;
    protected JButton clear;
    protected JButton getMax,pointButton,lineButton;
    protected JButton cordSystem;
    protected JButton chooseTool;
    protected JLabel lineToolLabel,pointToolLabel,message1,message2;
    protected String[] tools={"Punkt","Linie"};
    protected String[] maxima={"1","2","3","4","5"};
    protected String plaf;
    protected ImageIcon dialogIcon;
    
    public void init() {
        
 
        Image dialogImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("dialogIcon.GIF"));
        dialogIcon=new ImageIcon(dialogImage);
        
        this.initializeLabels();
        this.initializeImages();
        viewer=new HoughViewer(input,hough);
        point=true;
        line=false;
        updateArray=false;
        firstMousePress=true;
        chooseCordSystem=false;

 
        
  
 
        this.initializeButtons();
        this.initialzeDialog();
 
        
        messagePanel=new JPanel();
        messagePanel.setPreferredSize(new Dimension(220,50));
        messagePanel.setBorder(BorderFactory.createEtchedBorder());
        messagePanel.add(message1);
        messagePanel.add(message2);
        
        operationPanel=new JPanel();
        Dimension d = new Dimension(this.getWidth(),70);
        operationPanel.setPreferredSize(d);
        operationPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        operationPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,1));
        operationPanel.add(chooseTool);
        operationPanel.add(getMax);
        operationPanel.add(clear);
        operationPanel.add(cordSystem);
        operationPanel.add(messagePanel);
 
        
  
 
        this.initializeActionListeners();
        
        this.changeLookAndFeel();
 
        getContentPane().add("North",operationPanel);
        getContentPane().add(viewer);
    	addMouseListener(this);
    	addMouseMotionListener(this);
    	
        
    }
    /**
     * Initialisierung der Dialogboxen,welche aufgerufen werden,
     * wenn man das Tool wechselt.
     */
    
    public void initialzeDialog(){
        toolMainPanel=new JPanel[1];
        toolMainPanel[0]=new JPanel();
        toolMainPanel[0].setLayout(new BorderLayout(0,0));
      
        toolSubPanel=new JPanel[2];
        toolSubPanel[0]=new JPanel();
        toolSubPanel[0].setPreferredSize(new Dimension(150,80));
        toolSubPanel[0].setLayout(new FlowLayout(FlowLayout.LEFT,10,15));
        toolSubPanel[0].add(lineButton);
        toolSubPanel[0].add(pointButton);
        
        toolSubPanel[1]=new JPanel();
        toolSubPanel[1].setPreferredSize(new Dimension(150,30));
        toolSubPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
        toolSubPanel[1].add(new JLabel("    "));
        toolSubPanel[1].add(lineToolLabel);
        toolSubPanel[1].add(new JLabel("         "));
        toolSubPanel[1].add(pointToolLabel);
        
        toolMainPanel[0].add(BorderLayout.NORTH,toolSubPanel[0]);
        toolMainPanel[0].add(BorderLayout.CENTER,toolSubPanel[1]);
        
        JOptionPane toolPane = new JOptionPane();
        toolPane.setOptions(toolMainPanel);
        toolPane.setMessage("");
        
        toolDialog=toolPane.createDialog(this,"select tool");
        
    }
    
    /**
     * Konfiguration der Labels fuer die Tools,bzw.
     * der Statusleiste im Startzustand.
     *
     */
    public void initializeLabels(){

        lineToolLabel=new JLabel("line");
        pointToolLabel=new JLabel("point");
        message1=new JLabel("Drag mouse over the input image");
        message2=new JLabel("to draw some points");
        
    }
    
    /**
     * Aufbau von Eingangsbild und Houghraum
     *
     */
    public void initializeImages(){
        A=new iRPImage(picSize,picSize);
        B=new iRPImage(picSize,picSize);
        B.setLogImage(true);
        input=new ImageWindow(A,xFromLeft,yFromTop);
        input.setTitle("input image");
        hough=new ImageWindow(B,xFromLeft+picSize+betweenThePics,yFromTop);
        hough.setTitle("hough space");
        hough.setRealVaues(true);
        
        input.setCoordinateSystemVariable("x","y");
        hough.setCoordinateSystemVariable("a","b");
    }
    
    /**
     * Erstellen der Buttons.
     * Alle Buttons werden mit Icons versehen und 
     * bekommen eine Umrandung.
     */
    
    public void initializeButtons(){
        Image clearImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("reset.gif"));
        clear=new JButton(new ImageIcon(clearImage));
        clear.setToolTipText("clear images");
        clear.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        Image cordImage=Toolkit.getDefaultToolkit().getImage(getClass().getResource("cordSystem.gif"));
        cordSystem= new JButton(new ImageIcon(cordImage)); 
        cordSystem.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        cordSystem.setToolTipText("activates/deactives coordinate systems");
        
        
        Image maxImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("max.gif"));
        getMax=new JButton(new ImageIcon(maxImage));
        getMax.setToolTipText("calculates number of maxima you have chosen");
        getMax.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        Image toolImage=Toolkit.getDefaultToolkit().getImage(getClass().getResource("tool.jpg"));
        ImageIcon toolIcon=new ImageIcon(toolImage);
        chooseTool=new JButton(toolIcon);
        chooseTool.setToolTipText("choose tool");
        chooseTool.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        Image lineImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("lineDialog.gif"));
        ImageIcon lineIcon = new ImageIcon(lineImage);
        lineButton=new JButton(lineIcon);
        lineButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        Image pointToolImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("pointDialog.gif"));
        ImageIcon pointToolIcon = new ImageIcon(pointToolImage);
        pointButton=new JButton(pointToolIcon);
        pointButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        
    }
    
    /**
     * Mit Hilfe der ActionListener werden beim
     * Klicken auf die Buttons die entsprechenden Methoden
     * aufgerufen,z.B. wird beim druecken auf den choosetool-button
     * die Dialogbox mit der Toolauswahl geoeffnet.
     */
    public void initializeActionListeners(){
      	cordSystem.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	        if (!chooseCordSystem){
    	            drawCoordinateSystems(true);
    	            chooseCordSystem=true;
    	            repaint();
    	        }
    	        else {
    	            drawCoordinateSystems(false);  
    	            chooseCordSystem=false;
    	            repaint();
    	        }
    	    }
        });
     	
 
    	
    	//Aufraeumen der Oberflaeche
    	clear.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e){
    	        resetPictures();
    	    }
    	});
    	
    	//Berechnet die Maxima.
    	getMax.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	        getNumberOfMax();
    	        
    	    }
        });
    	
       	chooseTool.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	        toolDialog.show();
    	        
    	    }
        });
       	
      	lineButton.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	        toolDialog.setVisible(false);
    	        getTool("line");
    	        
    	    }
        });
      	
     	pointButton.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	        toolDialog.setVisible(false);
    	        getTool("Punkt");
    	        
    	    }
        });
    	
    	
 
    }
    
    /**
     * Aenderung der Optik
     *
     */
    public void changeLookAndFeel(){
      	try{
    		plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    		UIManager.setLookAndFeel(plaf);
    		SwingUtilities.updateComponentTreeUI(this);
    		SwingUtilities.updateComponentTreeUI(chooseTool);
    		SwingUtilities.updateComponentTreeUI(cordSystem);
      		SwingUtilities.updateComponentTreeUI(getMax);
       		SwingUtilities.updateComponentTreeUI(clear);
    		SwingUtilities.updateComponentTreeUI(operationPanel);
       		SwingUtilities.updateComponentTreeUI(viewer);
       		SwingUtilities.updateComponentTreeUI(lineButton);
       		SwingUtilities.updateComponentTreeUI(pointButton);
       		
    		} catch (UnsupportedLookAndFeelException e) {
    		    System.err.println(e.toString());
    		} catch (ClassNotFoundException e) {
    		    System.err.println(e.toString());
    		} catch (InstantiationException e) {
    		    System.err.println(e.toString());
    		} catch (IllegalAccessException e) {
    		    System.err.println(e.toString());
    		}
    }
    
    /**
     * Wenn keine Gerade in die Bilder selbst gezeichnet
     * wird,so wird abhaengig wo sich der Mauszeiger gerade
     * befindet,die entsprechend transformiert Gerade in dem jeweils
     * anderen Bild angezeigt.
     * Wurden gerade die Maxima detektiert,so werden die Geraden in das
     * Eingangsbild eingezeichnet(wenn findMax=='true' gilt).
     */
    public void paint(Graphics g){

    	super.paint(g);

    	// If the Mouse is Over the Left Picture, paint the "MouseOver-line" to the
    	// right picture, and else if the mouse is over the right picture, paint the
    	// "MouseOver-line" to the Left picture
    	if(!drawTheLine){
        	if(mouseOnLeftImage)
        	{
        		g.setColor(Color.CYAN);
        		showLine(g, x1+xFromLeft+picSize+betweenThePics , y1+yFromTop+70,
        		x2+xFromLeft+picSize+betweenThePics , y2+yFromTop+70);
        	}

        	if(mouseOnRightImage)
        	{
        		g.setColor(Color.red);
        		showLine(g, x1+xFromLeft, y1+yFromTop+70, x2+xFromLeft, y2+yFromTop+70);
        	}
    	}
    	if(findMax){
    	    
    	    for (int i=0;i<numberOfMax;i++){
    	        int a1=maxLineCordinatesArray[i][0];
    	        int b1=maxLineCordinatesArray[i][1];
    	        int a2=maxLineCordinatesArray[i][2];
    	        int b2=maxLineCordinatesArray[i][3];
    	        g.setColor(Color.YELLOW);
    	        g.drawLine(a1+xFromLeft,b1+yFromTop+70,a2+xFromLeft,b2+yFromTop+70);
    	        g.setColor(Color.GREEN);
    	        g.drawRect(maxPosArray[i][0]+325,maxPosArray[i][1]+85,10,10);
    	    }
    	}

    }
    
    /**
     * Soll das Koordinatensystem eingezeichet werden?
     * @param draw ist draw gleich 'true' werden die KS eingezeichnet
     */
    public void drawCoordinateSystems(boolean draw) {
		    input.setCordFlag(draw);
		    hough.setCordFlag(draw);
    }
    
    /**
     * Auswahl der Tools.
     * Zur Auswahl stehen Geraden und Punkte.
     * Methode wird von der Combobox tool aufgerufen.
     * @param s Name des Tools
     */
    
    public void getTool(String s){
 
        if(s=="Punkt"){
            input.setLineFlag(false);
            point=true;
            line=false;
            message1.setText("Drag mouse over the input image");
            message2.setText("to draw some points");
        }
        else {
  	        input.setLineFlag(true);
            point=false;
            line=true;
            message1.setText("Keep mouse button pressed and");
            message2.setText("drag it over the input image");
        }
 
    }
    
    /**
     * Diese Methode wird aufgerufen,wenn der max-Button aktiviert
     * wurde. Eine Dialogbox oeffnet sich um die Anzahl der 
     * zu suchenden Maxima abzufragen. Wenn der zurueckgegebene String
     * nicht null ist,wird calcMax() aufgerufen,um die Maxima zu finden.
     */
    public void getNumberOfMax(){
        
        String max=(String)JOptionPane.showInputDialog(this,"Number of Maxima","Maxima",
                JOptionPane.QUESTION_MESSAGE,dialogIcon,
                maxima,maxima[0]);
        
        if(max != null){
            if(max=="1"){
                numberOfMax=1;
            }
            if(max=="2"){
                numberOfMax=2;
            }
            if(max=="3"){
                numberOfMax=3;
            }
            if(max=="4"){
                numberOfMax=4;
            }
            if(max=="5"){
                numberOfMax=5;
            }
            
   	        findMax=true;
   	        updateArray=true;
	        calcMax();
	        repaint();
        }
        

    }
    
    /**
     * Findet die Maxima. Zuerst wird das Bild des
     * Houghraumes kopiert,da dieses nicht direkt manipuliert
     * werden soll. Wenn ein Maximum gefunden wurde,werden die 
     * Koordinaten zuruecktransformiert.
     * 
     */
    
    public void calcMax() {
  
        maxLineCordinatesArray=new int[5][4];
        calculateMax=B.copy();
        int x,y;
        int maxPosition;
        for (int i=0;i<numberOfMax;i++){
            maxPosition=calculateMax.GetMax();
            x= maxPosition % picSize;
            y = (maxPosition-x)/picSize;
            maxPosArray[i][0]=x;
            maxPosArray[i][1]=y;
            if(calculateMax.GetPixel(maxPosition)>0){
 

                for (int k=x-10;k<=x+10;k++){
                    for(int j=y-10;j<=y+10;j++){
                        if(k>=0 && k<calculateMax.sizeX && j>=0 && j<calculateMax.sizeY)
                        calculateMax.SetPixel(k,j,0);
                    }
                }
            
         		x =  x - (picSize/2);
        		y = (y - (picSize/2)) * -1;
                BackTransform(x,y);   
                maxCounter +=1;
            }
        }
        maxCounter=0; 
        repaint();
 
    }
    
    /**
     * Zuruecksetzen der Bilder.
     */
    public void resetPictures(){

    	for(int i=0; i<A.image.length; i++)
    	{
    		A.image[i] = B.image[i] = 0;
    		B.logImage[i]=0;
    	}


    	
    	 findMax=false;
    	 updateArray=false;
        repaint();
    }
    
    /**
     * Zeichnet eine transformierte Linie in die Bilder.
     */
    public void showLine(Graphics g, int r1, int r2, int t1 , int t2)
    {
    	Graphics abb = g;
    	g.drawLine(r1, r2, t1, t2);
    }


    public void update(Graphics g)
    {
    	paint(g);
    }
    
    /**
     * Die Methode tranformiert einen Punkt des Houghraumes
     * zurueck in das Eingangsbild. Dabei wird davon davon
     * ausgegangen,dass (0,0) im Zentrum des Houghbildes liegt.
     * Dementsprechend ist der maximale/minimale Wert fuer a bzw b gleich
     * +- picSize/2.
     * @param a x-Koordinate des Punktes im Houghraum
     * @param b y-Koordinate des Punktes im Houghraum
     */
    
    public void BackTransform(int a, int b)
    {

    	int startX=0, startY=0, endX=0, endY=0,y=0;

    	double newA;

    	// newA has to be computed for the used scalingOfA-value
    	newA = ((double)a*(double)scalingOfA/(double)picSize);

    	// start with the left side of the image looking for a startpoint
    	startX = -(picSize/2);
    	y = (int)((newA*(double)startX)+(double)b);

    	// if y-value of startPoint > our image --> y=const, search for x
    	if(y>(picSize/2))
    	{
    		startY=(picSize/2);
    		startX = (int)(((double)startY-(double)b)/newA);
    	}

    	else
    	// if y-value of startPoint < our image --> y=const, search for x
    	if(y<-(picSize/2))
    	{
    		startY=-(picSize/2);
    		startX = (int)(((double)startY-(double)b)/newA);
    	}

    	else
    	// The point is found and lies on the left corner of the image
    	{
    		startX = -(picSize/2);
    		startY = y;
    	}


    	// search for end-point on the right side of the image
    	endX = (picSize/2);
    	y = (int)((newA*(double)endX)+(double)b);

    	// if y-value of endPoint > our image --->  y=const, search for x
    	if(y>(picSize/2))
    	{
    		endY=(picSize/2);
    		endX = (int)(((double)endY-(double)b)/newA);
    	}

    	else

    	// if y-value of endPoint < our image --->  y=const, search for x
    	if(y<-(picSize/2))
    	{
    		endY=-(picSize/2);
    		endX = (int)(((double)endY-(double)b)/newA);
    	}

    	else
    	// The point is found and lies on the right corner of the image
    	{
    		endX = (picSize/2);
    		endY = y;
    	}


    	// Set the global values of x1, x2, y1, y2
    	// The different coord-system scalings are also considered here
    	x1 = startX +(picSize/2);
    	y1 = (startY-(picSize/2))*(-1);
    	x2 = endX + (picSize/2);
    	y2 = (endY-(picSize/2))*(-1);
    	
    	if(findMax && updateArray){
    	    
    	    maxLineCordinatesArray[maxCounter][0]=x1;
    	    maxLineCordinatesArray[maxCounter][1]=y1;
       	    maxLineCordinatesArray[maxCounter][2]=x2;
       	    maxLineCordinatesArray[maxCounter][3]=y2;

    	}
    	
    }
    
    /**
     * Die Methode transformiert einen Punkt des Eingangsbildes
     * in den Houghraum. Auch hier wird davon ausgegangen,dass
     * (0,0) im Zentrum des Bildes liegt.
     * @param x x-Koordinate des Punktes im Eingangsbild
     * @param y y-Koordinate des Punktes im Eingangsbild
     */
    public void Transformation(int x, int y)
    {


    	int startA=0, startB=0, endA=0, endB=0, b ,a;
    	double temp;

        a = -(scalingOfA/2);
        b = y - ( x * a);

    	if(b>(picSize/2))
       	{
    		startB = (picSize/2);
    		temp = (((double)y-(double)startB)/(double)x);
    		startA = (int)(((temp + (double)(scalingOfA/2)) * (double)picSize) / (double)scalingOfA);
    	}

    	else
    	if(b<-(picSize/2))
    	{
    		startB = -(picSize/2);
    		temp = (((double)y-(double)startB)/x);
    		startA = (int)(((temp + (double)(scalingOfA/2)) * (double)picSize) / (double)scalingOfA);

    	}

    	else
    	{
    		startB = b;
    		startA = 0;
    	}




        a = (scalingOfA/2);
        b = y - ( x * a);

    	if(b>(picSize/2))
       	{
    		endB = (picSize/2);
    		temp = (((double)y-(double)endB)/(double)x);
    		endA = (int)(((temp + (double)(scalingOfA/2)) * (double)picSize) / (double)scalingOfA);
    	}

    	else
    	if(b<-(picSize/2))
    	{
    		endB = -(picSize/2);
    		temp = (((double)y-(double)endB)/(double)x);
    		endA = (int)(((temp + (double)(scalingOfA/2)) * (double)picSize) / (double)scalingOfA);
    	}
    	else
    	{
    		endB = b;
    		endA = picSize;
    	}

    	// Set the global coords- considering coord-systems
    	x1 = startA;
    	y1 = (startB-(picSize/2))*(-1);
    	x2 = endA;
    	y2 = (endB-(picSize/2))*(-1);

    	//drawLine = true  if mouse is clicked --> drawLine into Hough-space-image
    	if(drawTheLine)
    	{
    		B.drawLine(startA,(startB-(picSize/2))*(-1),endA,(endB-(picSize/2))*(-1));
    	}
    }
    
    protected double round(double basis, int stellen)
    {
        basis *= Math.pow(10,stellen);
        basis = Math.round(basis);
        return basis/Math.pow(10,stellen);
    }
    
 
    
    /**
     * Die Methode transformiert jeden Punkt auf den Geraden
     * zwischen (a1,b1) und (a2,b2) in den Houghraum
     * @param a1
     * @param b1
     * @param a2
     * @param b2
     */
    public void findPointsOnLine(int a1,int b1,int a2,int b2){
      	double a,b,delta,x,y;

    	
      	if(Math.abs(b2-b1)<Math.abs(a2-a1))
      	{
    		if(a1>a2) delta=-1;
    		else delta=1;
      		for(a = a1; a != a2+delta; a+=delta)//for(a = a1; a < a2; a++)
        	{
    			b = (double)b1 + ((double)(b2-b1)* ((double)a-(double)a1)/((double)a2-(double)a1));
    			//Die Abfrage ist zufällig wegen ArrayindexofBoundexception!!
    			if(((int)b != picSize) && ((int)a !=picSize))
    			{   	
    			
   			    
      	    	x =  a - (picSize/2);
    	    	y = (b - (picSize/2)) * -1; // *(-1) to get the b rising from bottom to top
    			Transformation((int)x,(int)y);
    				//SetPixel((int)a,(int)b, value);
    			}
    		}
    	}
      	else
      	{

    		if (b1>b2) delta=-1;
    		else delta=1;
         	for(b = b1; b != b2+delta; b+=delta)
         	{
    			a =(double)a1 + ((double)(a2-a1)* ((double)b-(double)b1)/((double)b2-(double)b1));
    			if((int)b != picSize && ((int)a != picSize))
    			{   
   			    
      	    	x =  a - (picSize/2);
    	    	y = (b - (picSize/2)) * -1; // *(-1) to get the b rising from bottom to top
    			Transformation((int)x,(int)y);
    				//SetPixel((int)a , (int)b , value);
    			}
       		}
      	}
    }


    /**
     * Wurden Maxima in Bild-und Houghraum eingezeichnet,
     * werden diese wieder geloescht.
     */

    public void mouseClicked(MouseEvent e) {
        
        drawTheLine=false;
        findMax=false;
        updateArray=false;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
      
        
    }

    public void mouseExited(MouseEvent e) {
        
        
    }

    /**
     * Wenn als Tool der Punkt aktiv ist,wird
     * der Punkt transformiert.
     */
    public void mousePressed(MouseEvent e) {

        int cordX, cordY;
        cordX = e.getX() - xFromLeft;
        cordY = e.getY() - yFromTop-70;

 
        drawTheLine=false;

        if(point){
            drawTheLine = true;
       	  //Check if mouse is clicked in the left image
        	  if((cordX >= 0) && (cordX <= picSize) && (cordY >= 0) && (cordY <= picSize))
        	  {
        		  d1 = cordX;  // testing
                  r1 = cordY;  // testing

        		 if (A.GetPixel(cordX,cordY) == 0)
        		 {
        		 	A.SetPixel(cordX, cordY, 255);
        		 	//change the coordinates and put (0,0) in the middle of the left picture
        		 	cordX =  cordX - (picSize/2);
        		 	cordY = (cordY - (picSize/2)) * -1; // *(-1) to get the y rising from bottom to top


        		 	Transformation(cordX, cordY);
        	   	 }
        	  }
        	  B.takeTheLogarithm();          
        	  repaint();
        }
       
    }

    /**
     * Ist als Tool die line aktiv,wird die Gerade
     * in das Eingangsbild eingezeichnet und anschließend
     * wird durch den Aufruf von findPointsOnLine(..) jeder
     * Punkt transformiert.
     */
    public void mouseReleased(MouseEvent e) {
        firstMousePress=true;
        /*
         * Die Abfrage xDep>0.. ist wichtig,da bei einem mouse click
         * diese Methode ebenfalls aufgerufen wird.Wurde z.B. eine Linie
         * gezogen und anschließend irgendwo mit der Maus auf die Ober-
         * flaeche geklickt,wird diese Methode aufgerufen und die Line
         * wird zweimal eingezeichnet. 
         */
        if (line && xDep>0 && yDep>0 && xDes>0 && yDes>0 ){
            drawTheLine=true;
            A.drawLineWithConstantValue(xDep-xFromLeft,yDep-yFromTop-70,xDes-xFromLeft,yDes-yFromTop-70,255);
            input.setLineCoordinates(0,0,0,0,Color.BLACK);
            //findPointsOnLine(xDep-xFromLeft,yDep-yFromTop-70,xDes-xFromLeft,yDes-yFromTop-70);
            this.findPointsOnLine(xDep-xFromLeft,yDep-yFromTop-70,xDes-xFromLeft,yDes-yFromTop-70);
            B.takeTheLogarithm();
            repaint();
            
        }
        xDep=0;yDep=0;xDes=0;yDes=0;
        drawTheLine=false;
     }

    /**
     *Ist die Maus ueber dem Eingangsbild wird 
     *abhaengig vom Tool entweder eine Gerade oder ein
     *Punkt gezeichnet. 
     */
    public void mouseDragged(MouseEvent e) {
         
    	int cordX, cordY;
    	cordX = e.getX() - xFromLeft;
    	cordY = e.getY() - yFromTop-70;
    	drawTheLine=true;
   	   	if((cordX >= 0) && (cordX < picSize) && (cordY >= 0) && (cordY < picSize)){
   		    
   	    	if (line){
  
   	    	    if(firstMousePress){
   	    	        xDep=e.getX();
   	    	        yDep=e.getY();
   	    	        firstMousePress=false;
 
   	    	        
   	    	        
   	    	    }
   		        xDes=e.getX();
   	    	    yDes=e.getY();
   	    	    input.setLineCoordinates(xDep,yDep-70,xDes,yDes-70,Color.BLUE);
   	    	    
   	    	    
   	    	}
   	    	else {

   	       		if (A.GetPixel(cordX,cordY) == 0)
        		{
 
        	    	A.SetPixel(cordX, cordY, 255);
        	    	cordX =  cordX - (picSize/2);
        	    	cordY = (cordY - (picSize/2)) * -1; // *(-1) to get the y rising from bottom to top
        			Transformation(cordX,cordY);
        			B.takeTheLogarithm();
        		}
              }
    	   	}
 

        	repaint();
    }

    
    /**
     * Abhaengig von der Position der Maus
     * wird entweder hin-oder ruecktransformiert.
     * Außerdem werden die aktuelle (x,y)-Position und der
     * Grauwert dem jeweiligen ImageWindow Objekt uebergeben 
     */
    public void mouseMoved(MouseEvent e) {

    	int cordX, cordY;
    	int cordA, cordB;
    	int value;

    	
    	// set (0,0) where image-image lies (top left corner)
    	cordX = e.getX() - xFromLeft;
    	cordY = e.getY() - yFromTop-70;

    	// set (0,0) where Hough-image lies (top left corner)
    	cordA = e.getX() - xFromLeft - picSize - betweenThePics;
    	cordB = e.getY() - yFromTop-70;
    	

    	updateArray=false;
    	

    	  // Check if mouse is clicked in the left image
    	  if((cordX >= 0) && (cordX <picSize) && (cordY >= 0) && (cordY < picSize))
    	  {
    	      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    		 mouseOnLeftImage = true;
    		 mouseOnRightImage=false;
    		 
    		 value=A.GetPixel(cordX,cordY);
    		 input.updateMousePosition(cordX,cordY,value);

    		 // Change the coordinates and put (0,0) in the middle of the left picture
    		 cordX = cordX - (picSize/2);
    		 cordY =(cordY - (picSize/2)) * -1;             // *(-1) to get the y rising from bottom to top
    		 //information.setText("x: "+cordX+" y: "+cordY); // change the Label for x,y,a and b coords
    	     Transformation(cordX, cordY);
       	     repaint();
       	  }
    	  else if((cordA >=0) && (cordA<picSize) && (cordB >=0) && (cordB < picSize))
       	  {
    		  mouseOnLeftImage = false;
    		  mouseOnRightImage=true;
    		  setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    	      value=B.GetPixel(cordA,cordB);        
    		  cordA =  cordA - (picSize/2);
    		  cordB = (cordB - (picSize/2))*-1 ;
    		  hough.updateMousePosition(round(((double)cordA*(double)scalingOfA/(double)picSize),2),cordB,value);
    		  //information.setText("a: "+ round(((double)cordA*(double)scalingOfA/(double)picSize),2) +" b: "+cordB);
    		  BackTransform(cordA,cordB);
    		  repaint();
    	  }
    	  else{
    	      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    		  mouseOnLeftImage = false;
    		  mouseOnRightImage=false; 
    		  repaint();
    	  }


        
    }

}
