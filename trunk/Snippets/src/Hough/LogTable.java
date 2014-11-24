
package Hough;

/**
 *Diese Klasse stellt eine Logarithmustabelle zur Verfuegung,welche
 *alle Logarithmen zur Basis e zwischen 1 und 255 enthaelt. 
 * @author Rene Iser, Behrang Karimibabak, Simon Winkelbach
 *
 */
public class LogTable {
    
    double[] log = new double[256];
    
    
    LogTable(){
        initializeTable();
    }
    
    /**
     * Berechnung der Logarithmen
     *
     */
    public void initializeTable(){
        log[0]=0;
        for(int i=1;i<256;i++){
            log[i]=Math.log(i);
        }
    }
    
    /**
     * Berechnet log(lg)
     * @param lg Wert fuer den der Logarithmus berechnet werden soll
     * @return double 
     */
    public double getLogarithm(int lg){
        return log[lg];
    }

}
