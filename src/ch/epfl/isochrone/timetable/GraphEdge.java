package ch.epfl.isochrone.timetable;
import ch.epfl.isochrone.math.Math;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Un Arc du Graphe des horaire construit à partir d'une destination, d'un temps de marche et d'un Set de trajets
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class GraphEdge {
    private final Stop destination;
    private final int walkingTime;
    private Integer[] packedTrips;
    
    
    /**
     * retourne la transformation de l'heure de depart et de l'heure d'arrivée en un entier packTrip
     * @param departureTime
     *          heure de départ qui sera dans la partie gauche de l'entier packTrip
     * @param arrivalTime
     *          heure d'arrivée qui permettra de calculer la durée du trajet qui sera stocké dans la partie droite de l'entier packTrip
     * @throws IllegalArgumentException
     *          si l'heure de départ est négative ou supérieur a 107999 (1jour = 24h)
     *          si l'heure d'arrivée est antérieure à l'heure de départ
     *          si la différence entre l'heure d'arrivée et l'heure de départ est supérieur a 9999. 
     * @return  l'entier packTrip, constitué de l'heure de départ à gauche et de la durée à droite 
     */

    public static int packTrip(int departureTime, int arrivalTime) throws IllegalArgumentException {

        if ( (departureTime < 0) || (departureTime > 107999)) {

            throw new IllegalArgumentException("L'heure de départ est invalide");

        }
        else if ( (arrivalTime - departureTime < 0) || (arrivalTime - departureTime > 9999) ){

            throw new IllegalArgumentException("La différence entre l'heure d'arrivée et celle de départ est invalide");
        }

        return (departureTime*10000 + (arrivalTime-departureTime));

    }

    /**
     * retourne l'heure de départ issu d'un entier de type packTrip
     * @param packedTrip
     *          l'entier contenant l'heure de départ et la durée du trajet
     * @return la partie gauche de l'entier passé en argument, à savoir l'heure de départ
     */

    public static int unpackTripDepartureTime(int packedTrip){

        return Math.divF(packedTrip, 10000);

    }
    
    /**
     * retourne la durée du trajet issu d'un entier de type packTrip
     * @param packedTrip
     *          l'entier contenant l'heure de départ et la durée du trajet
     * @return la partie droite de l'entier passé en argument, à savoir la durée du trajet
     * 
     */

    public static int unpackTripDuration(int packedTrip){

        return Math.modF(packedTrip,  10000);

    }
    
    /**
     * retourne l'heure d'arrivée issue d'un entier de type packTrip
     * @param packedTrip
     *          l'entier contenant l'heure de départ et la durée du trajet
     * @return la somme de l'heure de départ et de la durée, à savoir l'heure d'arrivée du trajet
     * 
     */
    

    public static int unpackTripArrivalTime(int packedTrip){

        return ( unpackTripDepartureTime(packedTrip) + unpackTripDuration(packedTrip) );

    }
    
    /**
     * construit un arc de Graphe des horaires 
     * @param destination
     *          arret de destination du trajet
     * @param walkingTime
     *          temps de marche pour aller à l'arret de destination
     * @param packedTrips
     *          Set de tous les entiers packTrip contenant les heures de départ et durée des trajets existants
     * @throws IllegalArgumentException
     *          si le temps de marche est strictement inférieur à -1
     */

    public GraphEdge(Stop destination, int walkingTime, Set<Integer> packedTrips) throws IllegalArgumentException{

        if (walkingTime < -1){

            throw new IllegalArgumentException("Le temps de marche est inférieur à -1");
        }

        this.destination=destination;
        this.walkingTime=walkingTime;
        Integer[] temp = new Integer[packedTrips.size()];
        packedTrips.toArray(temp);
        Arrays.sort(temp);

    }
    
    /*
     * retourne la destination de l'arc
     * @return la destination de l'arc de Graphe d'horaire
     */

    public Stop destination(){

        return destination;

    }

    /**
     * retourn l'heure d'arrivée la plus rapide en fonction de l'heure de départ passée en argument
     * @param departureTime
     *          heure de départ pour laquelle on veut savoir quelle est l'heure d'arrivée la plus rapide
     * @return  l'entier d'heure d'arrivée la plus rapide, ou INFINITE si le temps de marche vaut -1 et qu'il n'y a plus de transport
     */
    
    public int earliestArrivalTime(int departureTime){

        int arrival = earliestArrivalTransport(departureTime);
        int walk = departureTime + walkingTime;
        if (walkingTime == -1){
            if(arrival == -1){
                return SecondsPastMidnight.INFINITE;
            }
            return arrival;
        }
        else {
            if(arrival < walk && (arrival != -1)) {
                return arrival;
            }
            else return walk;           
        }
    }
    
    /**
     * Methode privée qui indique l'heure d'arrivée la plus rapide en utilisant uniquement les services
     * @param departureTime
     *          heure de départ passée en argument pour tenter d'obtenir une heure d'arrivée la plus rapide en service
     * @return heure d'arrivée la plus rapide via des services uniquement
     */

    private int earliestArrivalTransport(int departureTime){

        int i = java.util.Arrays.binarySearch(packedTrips,  departureTime * 10000);
        if (i>=0) {
            return unpackTripArrivalTime(packedTrips[i]);
        }
        else {
            i=-(i+1);
            if (i== packedTrips.length){
                return -1;
            }
            return unpackTripArrivalTime(packedTrips[i]);
        }
    }

    public static class Builder {

        private final Stop destination;
        private final  Set<Integer> packedTrips;
        private int walkingTime;
        
        /**
         * Construit un Batisseur d'arc de Graphe d'horaire
         * @param   destination
         *          arret de destination de l'arc
         * @return  un batisseur de 'arc de Graphe d'horaire
         */

        public Builder(Stop destination){

            this.destination = destination;
            this.packedTrips = new HashSet<>();
            this.walkingTime= -1;

        }
        
        /**
         * Redéfinit le temps de marche depuis un nouveau temps de marche passé en argument 
         * @param newWalkingTime
         *          nouveau temps de marche
         * @throws IllegalArgumentException
         *          si le temps de marche est strictement inférieur à -1
         * @return  le nouveau temps de marche passé en argument et inclut dans le batisseur
         */

        public GraphEdge.Builder setWalkingTime(int newWalkingTime) throws IllegalArgumentException {

            if(newWalkingTime < -1){
                throw new IllegalArgumentException("Le temps de marche est invalide");
            }

            walkingTime = newWalkingTime;
            return this;

        }
        
        /**
         * ajoute un trajet au batisseur d'arc de Grape d'horaire
         * @param departureTime
         *          heure de départ du trajet à ajouter
         * @param arrivalTime
         *          heure d'arrivée du trajet à ajouter
         * @throws IllegalArgumentException
         *          si l'heure de départ est négative ou supérieur à 107999 (1jour = 24h)
         *          si l'heure d'arrivée est antérieure à l'heure de départ
         *          si la durée du trajet est supérieur à 9999
         * @return  this (la methode ne retourne rien, elle ne fait qu'ajouter au batisseur 
         *          un nouveau trajet a partir des heures de départ et d'arrivée passées en argument
         */

        public GraphEdge.Builder addTrip(int departureTime, int arrivalTime) throws IllegalArgumentException {

            if ( (departureTime < 0) || (departureTime > 107999)) {

                throw new IllegalArgumentException("L'heure de départ est invalide");

            }
            else if ( (arrivalTime - departureTime < 0) || (arrivalTime - departureTime > 9999) ){

                throw new IllegalArgumentException("La différence entre l'heure d'arrivée et celle de départ est invalide");
            }

            int newTrip=packTrip(departureTime, arrivalTime);
            packedTrips.add(newTrip);
            return this;

        }
        
        /**
         * Construit un arc de Graphe d'horaire

         * @return  Un arc de graphe d'horaire ayant été construit avec l'arret de destination,
         *          le temps de marche et le Set contenant l'ensemble des trajets.
         *  
         */          


        public GraphEdge build(){

            return new GraphEdge(destination, walkingTime, packedTrips);

        }
    }

}