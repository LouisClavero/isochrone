package ch.epfl.isochrone.geo;

/**
 * Un point dans le système de coordonnées OSM.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class PointOSM {



    private final double x;
    private final double y;
    private final int zoom;

    /**
     * Retourne la valeur max des coordonnés en fonction du zoom.
     *
     * @param zoom
     *         Le niveau de zoom du système de coordonnées OSM.
     * @throws IllegalArgumentException
     *         Si le zoom est négatif.
     * @return la dimension maximale des coordonnées au
     *         niveau de zoom donné.
     */

    public static int maxXY(int zoom) throws IllegalArgumentException{
        if(zoom<0){
            throw new IllegalArgumentException("le zoom est invalide");
        }
        double z = zoom + 8;

        return (int) java.lang.Math.pow(2, z);
    }

    /**
     * Construit un nouveau point du système de coordonnées OSM
     *
     * @param   zoom
     *          Le niveau de zoom du système de coordonnées OSM.
     * @param   x
     *          la coordonnée x (horyzontale) du Point que l'on veut construire.
     * @param   y
     *          la coordonnée y (verticale) du point que l'on veut construire.       
     * @throws  IllegalArgumentException
     *          Si le zoom est négatif ou si les coordonnés sont négatives ou supérieures à leurs valeurs maximales
     *          par rapport au zoom donné.
     */


    public PointOSM(int zoom, double x, double y) throws IllegalArgumentException{
        if(zoom<0){
            throw new IllegalArgumentException("le zoom est invalide");
        }
        else if (x < 0 || x > maxXY(zoom)){
            throw new IllegalArgumentException("la coordonnée x est invalide");
        }
        else if(y < 0  || y > maxXY(zoom)){
            throw new IllegalArgumentException("la coordonnée y est invalide");
        }
        this.x = x;
        this.y = y;
        this.zoom = zoom;

    }

    /**
     * Retourne la valeur de la coordonnée x.
     *        
     * @return la valeur de la coordonnée horyzontale x du Point au système de 
     * coordonnées OSM.
     */

    public double x(){
        return x;
    }

    /**
     * Retourne la valeur de la coordonnée y.
     *        
     * @return la valeur de la coordonnée verticale y du Point au système de 
     * coordonnées OSM.
     */

    public double y(){
        return y;
    }

    /**
     * Retourne  l'entier le plus proche de la coordonnée x du point.
     *        
     * @return l'entier le plus proche de la coordonnée x du point au système de 
     * coordonnées OSM.
     */

    public int roundedX(){
        return (int) java.lang.Math.round(x);
    }

    /**
     * Retourne  l'entier le plus proche de la coordonnée y du point.
     *        
     * @return l'entier le plus proche de la coordonnée y du point au système de 
     * coordonnées OSM.
     */

    public int roundedY(){
        return (int) java.lang.Math.round(y);
    }

    /**
     * Retourne le niveau de zoom du système de coordonnées du point. 
     *        
     * @return le niveau de zoom du point au système de   
     * coordonnées OSM.
     */

    public int zoom(){
        return zoom;
    }

    /**
     * Retourne le même point mais au niveau de zoom passé en argument
     *
     * @param   newZoom
     *          Le niveau de zoom au niveau duquel on souhaite obtenir les coordonnées du même point.
     * @throws  IllegalArgumentException
     *          Si le zoom est négatif 
     * @return  Le même point mais avec les coordonnées adaptées au niveau de zoom passé
     *          en argument.
     */

    public PointOSM atZoom(int newZoom) throws IllegalArgumentException{
        if(zoom < 0){
            throw new IllegalArgumentException("le zoom est invalide");
        }
        double x2 = x*java.lang.Math.pow(2, newZoom - zoom);
        double y2 = y*java.lang.Math.pow(2, newZoom - zoom);
        return new PointOSM(newZoom, x2, y2);
    }

    /**
     * Retourne le même point mais au système de coordonnées WGS 84.
     *
     * @return  Le même point mais au système de coordonnées WGS 84.
     *       
     */

    public PointWGS84 toWGS84(){
    	return new PointWGS84((((2*java.lang.Math.PI)/maxXY(zoom))*x) - java.lang.Math.PI, 
    			java.lang.Math.atan(java.lang.Math.sinh(java.lang.Math.PI - (((2*java.lang.Math.PI)/maxXY(zoom))*y))));
    }

    /**
     * Retourne une représentation textuelle du point.
     *
     * @return  retourne une représentation textuelle du point, qui doit être 
     *          formée du niveau de zoom, de la coordonnée x et de la coordonnée y, séparés 
     *          par des virgules et entourés d'une paire de parenthèse
     */

    public String toString(){

        return  "(" + zoom + ", " + x + ", " + y + ")";

    }

}