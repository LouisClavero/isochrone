package ch.epfl.isochrone.geo;

import ch.epfl.isochrone.math.Math;

/**
 * Un point dans le système de coordonnées WGS 84.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class PointWGS84 {

    private double latitude;
    private double longitude;

    /**
     * Construit un nouveau point du système de coordonnées WGS 84.
     *
     * @param   longitude
     *          la distance angulaire du point à un méridien de référence   
     * @param   latitude
     *          la distance angulaire du point à l'équateur       
     * @throws  IllegalArgumentException
     *          Si la longitude est en dehors de l'intervalle [-PI;PI]
     *          Si la latitude est en dehors de l'intervalle [-PI/2;PI/2] 
     */

    public PointWGS84(double longitude, double latitude) throws IllegalArgumentException{
        if(longitude < (-java.lang.Math.PI) || longitude > java.lang.Math.PI){
            throw new IllegalArgumentException("la longitude est invalide");
        }
        else if(latitude < (-java.lang.Math.PI)/2 || latitude > (java.lang.Math.PI)/2){
            throw new IllegalArgumentException("la latitude est invalide");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Retourne la valeur de la longitude.
     *        
     * @return la valeur de la longitude du Point au système de 
     * coordonnées WGS 84.
     */

    public double longitude(){
        return longitude;
    }

    /**
     * Retourne la valeur de la latitude.
     *        
     * @return la valeur de la latitude du Point au système de 
     * coordonnées WGS 84.
     */

    public double latitude(){
        return latitude;
    }

    /**
     * Retourne la valeur de la distance entre le point et celui passé en argument.
     *        
     * @return retourne la distance, en mètres, 
     * séparant le récepteur (c-à-d le point auquel on l'applique) 
     * du point passé en argument. 
     */

    public double distanceTo(PointWGS84 that){
        return 2*6378137*java.lang.Math.asin(java.lang.Math.sqrt(Math.haversin(latitude - that.latitude)+ java.lang.Math.cos(latitude)*java.lang.Math.cos(that.latitude)*Math.haversin(longitude - that.longitude)));
    }
    
    /**
     * Retourne le même point mais au système de coordonnées WGS 84.
     *
     * @param   zoom
     *          niveau de zoom du point.
     * @throws  IllegalArgumentException
     *          si le zoom est invalide cad négatif.
     * @return  Le même point mais au système de coordonnées OSM au 
     *          niveau de zoom pqssé en argument.
     *       
     */

    public PointOSM toOSM(int zoom) throws IllegalArgumentException{
        if(zoom < 0){
            throw new IllegalArgumentException("le zoom est invalide");
        }
        double x = ((java.lang.Math.pow(2, zoom + 8))/(2*java.lang.Math.PI))*(longitude + java.lang.Math.PI);
        double y = ((java.lang.Math.pow(2, zoom + 8))*(java.lang.Math.PI - Math.asinh(java.lang.Math.tan(latitude))))/(2*java.lang.Math.PI);
        return new PointOSM(zoom, x, y);
    }
    
    /**
     * Retourne une représentation textuelle du point.
     *
     * @return  retourne une représentation textuelle du point, 
     * qui doit être formée de la longitude et de la latitude en degrés, 
     * séparées par une virgule et entourées d'une paire de parenthèses.
     */

    public String toString(){
        return "(" + java.lang.Math.toDegrees(longitude) + ", " + java.lang.Math.toDegrees(latitude) + ")";
    }
}