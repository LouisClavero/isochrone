package ch.epfl.isochrone.timetable;
import ch.epfl.isochrone.geo.*;

/**
 * Un Arret construit a partir de son nom et de sa position.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class Stop {

    private final String name;
    private final PointWGS84 position;

    /**
     * Construit un arrêt avec le nom et la position
     * 
     * @param   name
     *          nom de l'arret que l'on va construire.
     * @param   position
     *          position de l'arret que l'on va construire au système de coordonnées WGS 84
     */

    public Stop(String name, PointWGS84 position){

        this.name = name;
        this.position = position;
    }
    
    /**
     * Retourne le nom de l'arrêt
     *
     * @return  le nom de l'arrêt;
     */

    public String name(){
        return name;
    }

    /**
     * Retourne la position de l'arret
     *
     * @return  la position du système de coordonnées WGS 84 de l'arret.
     */
    
    public PointWGS84 position(){
        return position;
    }
    
    /**
     * Retourne une représentation textuelle de l'arrêt.
     *
     * @return une représentation textuelle de l'arrêt, en l'occurrence son nom. Redéfinit la 
     * méthode toString héritée de Object. 
     */

    public String toString(){
        return (name);
    }

}