package ch.epfl.isochrone.timetable;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.epfl.isochrone.timetable.SecondsPastMidnight;

/**
 * Un arbre de Trajets les plus rapides
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */


public final class FastestPathTree {

    private final  Stop startingStop;
    private final Map<Stop, Integer> arrivalTime;
    private final Map<Stop, Stop> predecessor;
    
    /**
     * Construit un arbre de trajets les plus rapides
     * @param startingStop
     *          arret de départ du trajet
     * @param arrivalTime
     *          heure d'arrivée du trajet
     * @param predecessor
     *          Set d'arrets précédants l'arret de départ
     * @throws IllegalArgumentException
     *          si l'ensemble des clefs de la table des heures n'est pas égal à 
     *          celui des clefs de la tables des prédécesseurs plus l'arrêt de départ.
     */

    public FastestPathTree(Stop startingStop, Map<Stop, Integer> arrivalTime, Map<Stop, Stop> predecessor)throws IllegalArgumentException{   
        if(!(arrivalTime.keySet().containsAll(predecessor.keySet()) 
                && arrivalTime.keySet().contains(startingStop) 
                && arrivalTime.keySet().size()== predecessor.keySet().size()+1)){
            throw new IllegalArgumentException("l'ensemble des clefs de la table des heures n'est pas égal à celui des clefs de la tables des prédécesseurs plus l'arrêt de départ.");
        }

        this.startingStop = startingStop;
        this.arrivalTime = new HashMap<Stop, Integer>(arrivalTime);
        this.predecessor = new HashMap<Stop, Stop>(predecessor);

    }
    
    /**
     * retourne l'arret de depart
     * @return l'arret de depart de l'arbre de trajets les plus rapides
     */

    public Stop startingStop(){
        return startingStop;
    }
    
    /**
     * retourne l'heure de départ
     * @return l'heure de départ, qui n'est autre que l'heure de première arrivée à l'arrêt de départ
     */

    public int startingTime(){
        return arrivalTime.get(startingStop);
    }
    
    /**
     * retourne les arrets qui ont heure de première arrivée
     * @return  l'ensemble des arrêts pour lesquels une heure de première arrivée existe
     */

    public Set<Stop> stops(){
        return new HashSet<Stop>(arrivalTime.keySet());
    }

    /**
     * retourne l'heure d'arrivée à l'arret donné ou INFINITE
     * @param   stop
     *          Arret pour lequel on veut connaitre l'heure d'arrivée
     * @return  qui retourne l'heure d'arrivée à l'arrêt donné ou 
     *          SecondsPastMidnight.INFINITE si l'arrêt donné n'est pas dans 
     *          la table des heures d'arrivée passée au constructeur. 
     */
    
    public int arrivalTime(Stop stop){
        if(!arrivalTime.containsKey(stop)){
            return SecondsPastMidnight.INFINITE;
        }
        else return arrivalTime(stop);

    }
    
    /**
     * retourne le chemin pour aller de l'arret de départ à l'arret passé en argument
     * @param stop
     *          arret auquel on souhaite se rendre
     * @throws IllegalArgumentException
     *          si l'arret n'est pas dans la table des heures d'arrivees
     * @return  une liste contenant le chemin ordonnée pour se rendre de l'arret de départ (premier élément
     *          de la liste) à l'arret passé en argument(dernier élément de la liste)
     */

    public List<Stop> pathTo(Stop stop) throws IllegalArgumentException{
        if(!arrivalTime.containsKey(stop)){
            throw new IllegalArgumentException("l'arrêt n'est pas présent dans la table des heures d'arrivée.");
        }
        Stop tempStop;
        tempStop = stop;
        ArrayList<Stop> chemin = new ArrayList<Stop>();
        chemin.add(0, stop);
        while(!(tempStop == startingStop)){
            tempStop = predecessor.get(tempStop);
            chemin.add(0, tempStop);
            
        }
        chemin.add(0, startingStop);
        return chemin;
    }
    
    public final static class Builder{
        
        private final  Stop startingStop;
        private final Map<Stop, Integer> arrivalTime;
        private final Map<Stop, Stop> predecessor;
        private int startingTime;
        
        /**
         * Construit un batisseur d'arbre de trajets les plus rapides
         * @param startingStop
         *          arret de depart
         * @param startingTime
         *          heure de départ
         * @throws IllegalArgumentException
         *          si l'heure de départ est négative
         * @return un batisseur d'arbre de trajets les plus rapides en fonction d'un arret de départ
         *          et d'une heure de départ passés en arguments
         */
        
        public Builder(Stop startingStop, int startingTime) throws IllegalArgumentException{
            if(startingTime<0){
                throw new IllegalArgumentException("l'heure de départ est négative.");
            }
            this.startingStop = startingStop;
            this.startingTime = startingTime;
            this.predecessor = new HashMap<Stop, Stop>();
            this.arrivalTime = new HashMap<>();
        }
        
        /**
         * redéfinit l'heure de première arrivée et le prédécesseur de l'arret donnée dans l'arbre
         * @param stop
         *          arret dont on veut redefinir le prédécesseur
         * @param time
         *          heure que l'ont veut mettre a jour
         * @param predecessor
         *          arret que l'on veut redfinir
         * @throws IllegalArgumentException
         *          si la nouvelle heure d'arrivée est antérieure à l'heure de départ
         * @return this (la methode ne retourn rien, elle (re)définit l'heure de première arrivée et 
         *              le prédécesseur de l'arrêt donné dans l'arbre en construction)
         */
        
        public Builder setArrivalTime(Stop stop, int time, Stop predecessor) throws IllegalArgumentException{
            if(time - startingTime<0){
                throw new IllegalArgumentException("l'heure est invalide.");
            }
            startingTime = time;
            
            return this;
        }
        
        /**
         * retourne l'heure de première arrivée à l'arret donné
         * @param stop
         *          arret pour lequel on veut connaitre l'heure d'arrivée première
         * @return  INFINITE si aucune heure d'arrivée n'a été attribuée a cet arrêt
         *          l'heure de première arrivée à l'arret donné
         */
        
        public int arrivalTime(Stop stop){
            if(!arrivalTime.containsKey(stop)){
                return SecondsPastMidnight.INFINITE;
            }
            else return arrivalTime(stop);
        }
        
        /**
         * Construit un arbre d'arret les plus rapides. 
         * @return  Un arbre d'arret les plus rapides ayant été construit avec l'arret de départ, la table 
         *          des heures d'arrivees et la table des arrets précédants.
         * 
         */  
                
        public FastestPathTree build(){
            return new FastestPathTree(startingStop, arrivalTime, predecessor);
        }
    }
}