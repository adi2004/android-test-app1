package Hough;

import javax.swing.*;

import java.awt.*;

/**
* Diese Klasse verwaltet ein Image in einem Applet.
* Es besteht die Moeglichkeit,einer Instanz dieser Klasse
* die aktuelle Mausposition,sowie Grauwert des Bildes zu uebergeben,
* damit  beides unterhalb des Bildes ausgeben werden kann.
* Ausserdem stehen verschiedene set-Methoden zur Verfuegung,um
* Kreise,Linien und Rechtecke in das Bild einzuzeichnen,da diese
* in den Applets haeufig gebraucht werden.
*  
* @author Rene Iser, Behrang Karimibabak, Simon Winkelbach
*
*/
public class ImageWindow extends JPanel {
	iRPImage img;
	int xPos,yPos,x,y,width,height,a1,a2,b1,b2;
	int grayValue;
	double xMousePosition,yMousePosition;
	JLabel position,value;
	Color lineColor,fullColor;
	Font f;
	String title="";
	boolean line,rect,circle,drawCordSystem,xy,uv,realValues;
	String xVal,yVal;
	
	ImageWindow(iRPImage img,int xPos,int yPos){
	    setLayout(null);
		this.img=img;
		this.xPos=xPos;
		this.yPos=yPos;
		line=false;
		rect=false;
		circle=false;
		realValues=false;
		drawCordSystem=false;
		f=new Font("Helvetica",6,12);
		x=0;y=0;width=0;height=0;a1=0;a2=0;b1=0;b2=0;
		xMousePosition=0;yMousePosition=0;grayValue=0;
	
	}
	
	/**
	 * Zeichnet das Bild,bzw Figuren wie Kreise,Linien etc.
	 * auf die Oberflaeche.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.draw3DRect(xPos,yPos+img.sizeY+1,img.sizeX-1,20,true);
		img.PaintImage(g,xPos,yPos);
		if (line) setLine(g);
		if (rect) setRect(g);
		if (circle) {
		    setCircle(g);
		}
		else if(rect){
		    setRect(g);	
		}

		
		if (drawCordSystem) paintCordSystem(g);
		if (!(title=="")) {
		    g.setColor(Color.BLACK);
		    g.setFont(f);
		    g.drawString(title,xPos,yPos-3);
		}
		g.setFont(f);
		g.setColor(Color.BLACK);
		if(realValues){
		    g.drawString("("+xVal+","+yVal+") : ("+xMousePosition+","+yMousePosition+")" +
		    		"    gray value : "+grayValue+"",xPos+3,yPos+img.sizeY+15);
		}
		else{
		    g.drawString("("+xVal+","+yVal+") : ("+(int)xMousePosition+","+ (int)yMousePosition+") " +
		    		"   gray value : "+grayValue+"",xPos+3,yPos+img.sizeY+15); 
		}
		
	}
	
	/**
	 * Mit dieser Methode kann ein Koordinatensystem
	 *  eingezeichnet werden.
	 */
	public void paintCordSystem(Graphics g){
	    
	    if (xVal=="x" && yVal=="y"){
		    g.setColor(Color.GREEN);
		    
	    }
	    else {
	        g.setColor(Color.RED);
	        
	    }

	    g.drawLine(xPos+img.sizeX/2,yPos,xPos+img.sizeX/2,yPos+img.sizeY);
	    g.drawLine(xPos,yPos+img.sizeY/2,xPos+img.sizeX,yPos+img.sizeY/2);
	    g.drawLine(xPos+img.sizeX/2,yPos,(xPos+img.sizeX/2)-6,yPos+6);
	    g.drawLine(xPos+img.sizeX/2,yPos,(xPos+img.sizeX/2)+6,yPos+6);
	    g.drawLine(xPos+img.sizeX,yPos+img.sizeY/2,xPos+img.sizeX-6,(yPos+img.sizeY/2)-6);
	    g.drawLine(xPos+img.sizeX,yPos+img.sizeY/2,xPos+img.sizeX-6,(yPos+img.sizeY/2)+6);
	    g.drawString(yVal,xPos+img.sizeX/2+10,yPos+10);
	    g.drawString(xVal,xPos+img.sizeX-10,yPos+img.sizeY/2+17);
	}
	
	/**
	 * Die Methode setzt x,y,Laenge und Hoehe
	 * des Rechtecks fest. Ein Kreis wird dann passend in
	 * das Rechteck hinein gezeichnet.
	 * @param x x-Koordinate des Kreises/Rechtecks
	 * @param y y-Koordinate des Kreises/Rechtecks
	 * @param width Die Laenge des Rechtecks
	 * @param height die Hoehe des Rechtecks
	 * @param c die Farbe,wird nicht mehr benutzt
	 */
	protected void setCircleOrCoordinates(int x,int y,int width,int height,Color c){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.fullColor=c;
	}
	
	/**
	 * Wird ein nichtleerer String uebergeben
	 * wird der Titel beim naechsten repaint() aufruf
	 * mit ausgegeben.
	 */
	protected void setTitle(String t){
	    this.title=t;
	}
	
	/**
	 * Zeichnet einen Kreis.
	 */
	protected void setCircle(Graphics g){
		
		g.setColor(Color.YELLOW);
		g.drawOval(x,y,width,height);

	}
	
	
	
	/**
	 * Zeichnet ein Rechteck
	 */
	protected void setRect(Graphics g){
	    g.setColor(Color.YELLOW);
	    g.drawRect(x,y,width,height);
	}
	
	/**
	 * Ist drawCordSystem gleich 'true',so
	 * wird beim naechsten repaint() ein Koordinaten-
	 * system mit eingezeichnet.
	 */
	
	protected void setCordFlag(boolean cord){
	    this.drawCordSystem=cord;
	}
	
	/**
	 * Zeichnet eine Linie.
	 */
	
	protected void setLine(Graphics g){
	    g.setColor(lineColor);
	    g.drawLine(a1,b1,a2,b2);
	    
	}
	
	/**
	 * Setzt die Koordinaten fuer die Linie fest.
	 * Die Linie wird von (a1,b1) nach (a2,b2) mit der
	 * entsprechenden Farbe gezeichnet.
	 * @param a1
	 * @param b1
	 * @param a2
	 * @param b2
	 * @param c
	 */
	
	protected void setLineCoordinates(int a1,int b1,int a2,int b2,Color c){
	    this.lineColor=c;
	    this.a1=a1;
	    this.a2=a2;
	    this.b1=b1;
	    this.b2=b2;
	}
	
	/**
	 * Ist line gleich 'true',so
	 * wird beim naechsten repaint() eine 
	 * Linie mit eingezeichnet.
	 */
	
	protected void setLineFlag(boolean line){
	    this.line=line;
	    
	}
	
	/**
	 * @see setLineFlag(boolean line)
	 */
	
	protected void setRectFlag(boolean rect){
	    this.rect=rect;
	}
	
	/**
	 * @see setLineFlag(boolean line)
	 */
	
	protected void setCircleFlag(boolean circle){
	    this.circle = circle;
	}
	
	
	/**
	 * setzt alle boolschen Werte auf
	 * false.
	 */
	
	protected void resetImportantFlags() {
	    this.circle=false;
	    this.rect = false;
	    this.line=false;
	    
	}
	
	/**
	 * Hier kann dem Objekt ein neues Bild 
	 * zugewiesen werden.
	 * @param I
	 */
	
	protected void changeImage(iRPImage I){
	    this.img = I;
	    repaint();
	}
	
	/**
	 * Festlegung der Achsenbezeichnung des
	 * Koordinatensytems.
	 */
	
	protected void setCoordinateSystemVariable(String x,String y){
	    this.xVal=x;
	    this.yVal=y;
	}
	
	/**
	 * Mit dieser Methode wird festgelegt, ob die
	 * Achesen des Koordinatensystems reellwertig  oder
	 * nur ganzzahlig sind.
	 * Die Klasse wurde um diese Funktionalität erweitert, da
	 * die x-Achse des Houghraumes lediglich von -3 bis +3 geht,
	 * was dementsprechend auch reelle Werte zur Folge hat, 
	 * während sich die x- Achse des Bildraumes regulär von -150 bis
	 * +150 bewegt.
	 * @param real ist der Wert 'true' werden reelwertige Koordinaten angezeigt
	 */
	
	protected void setRealVaues(boolean real){
	    this.realValues=real;
	}
	
	/**
	 * Werden hier Position und Grauwert angegeben, so
	 * werden diese Unterhalb des Bildes ausgegeben.
	 * @param x x-Koordinate des Punktes
	 * @param y y-Koordinate des Punkes
	 * @param v Grauwert.
	 */
	
	protected void updateMousePosition(int x,int y,int v){
	    this.xMousePosition=x-img.sizeX/2;
	    this.yMousePosition=(y-img.sizeY/2)*-1;
	    this.grayValue=v;
	}
	
	
	/**
	 * Diese Methode setzt ebenfalls Position und Grauwert neu.
	 * Hier können allerdings reelle Werte uebergeben werden.
	 * @param x x-Koordinate des Punktes
	 * @param y y-Koordinate des Punkes
	 * @param v Grauwert.
	 */
	protected void updateMousePosition(double x,double y,int v){
	    this.xMousePosition=x;
	    this.yMousePosition=y;
	    this.grayValue=v;
	}

}
