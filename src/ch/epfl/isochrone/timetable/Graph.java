package ch.epfl.isochrone.timetable;
import java.util.ArrayList;

import ch.epfl.isochrone.timetable.GraphEdge;

import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

/**
 * Un Graphe d'horaire construit à partir d'un set d'arrets et d'une table d'arrets suivants
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class Graph {

    private Set<Stop> stops;
    private Map<Stop, List<GraphEdge>> outgoingEdges;



    private Graph(Set<Stop> stops, Map<Stop, List<GraphEdge>> outgoingEdges){

        this.stops = stops;
        this.outgoingEdges = outgoingEdges;
    }

    /**
     * retourne un arbre des trajets les plus rapides
     * @param startingStop
     *          arret de depart de l'arbre
     * @param departureTime
     *          heure de départ à partir du startingStop (entier)
     * @throws IllegalArgumentException
     *          si l'heure de départ est négative
     *          si le set des arrets ne contient pas l'arret de départ
     * @return un arbre des trajets les plus rapides construit à partir de son Batisseur
     */

    public FastestPathTree fastestPaths(Stop startingStop, int departureTime) throws IllegalArgumentException{

        if(departureTime<0){
            throw new IllegalArgumentException("L'heure de départ est invalide");
        }
        else if(!stops.contains(startingStop)){
            throw new IllegalArgumentException("L'arret de départ est invalide");
        }

        final FastestPathTree.Builder FPT = new FastestPathTree.Builder(startingStop, departureTime);
        PriorityQueue<Stop> pQ = new PriorityQueue<Stop>(stops.size(), new Comparator<Stop>(){

            @Override
            public int compare(Stop stop0, Stop stop1) {
                return Integer.compare(FPT.arrivalTime(stop0), FPT.arrivalTime(stop1));
            }
        });
        pQ.addAll(stops);
        while (!pQ.isEmpty()) {
            Stop s1 = pQ.remove();
            if (FPT.arrivalTime(s1)!=SecondsPastMidnight.INFINITE && outgoingEdges.get(s1) != null) {
                for (GraphEdge g : outgoingEdges.get(s1)) {
                    Stop s2 = g.destination();
                    int h1 = g.earliestArrivalTime(FPT.arrivalTime(s1));
                    int h2 = FPT.arrivalTime(s2);
                    if (h1<h2) {
                        FPT.setArrivalTime(s2, h1, s1);
                        pQ.remove(s2);
                        pQ.add(s2);
                    }
                }
            }
        }
        return FPT.build();
    }


    public final static class Builder{

        private HashSet<Stop> stops;
        private Map<Stop, List<GraphEdge>> outgoingEdges;
        private Map<Stop, Map<Stop, GraphEdge.Builder>> graphBuilder;

        /**
         * construit un batisseur de Graph d'horaire à partir d'un set d'arrets
         * @param stops
         *          le set d'arret qui va permettre de construire le batisseur
         * @return  un batisseur formés par le Set d'arrets passé en argument
         */

        public Builder(Set<Stop> stops){

            this.stops = new HashSet<> (stops);
            this.outgoingEdges = new HashMap<Stop, List<GraphEdge>>();
            this.graphBuilder = new HashMap<Stop, Map<Stop, GraphEdge.Builder>>();

        }

        /**
         * ajoute un trajet entre les arrets de depart et d'arrivees donnés, aux heures données en secondes après minuit
         * @param fromStop
         *          Arret de départ
         * @param toStop
         *          Arret d'arrivée
         * @param departureTime
         *          heure de départ (secondes après minuit)
         * @param arrivalTime
         *          heure d'arrivée (secondes après minuit)
         * @throws IllegalArgumentException
         *          si les heures d'arrivee ou de départs sont négatives
         *          si l'heure d'arrivée est inférieure à l'heure de départ
         *          si l'ensemble des arrets ne contient pas l'arret de départ ou l'arret d'arrivée
         * @return  this (la methode ne retourne rien, elle ne fait qu'ajouter un trajet avec les 
         *          heures de départ et arrivée en "gettant" le batisseur.
         */

        public Builder addTripEdge(Stop fromStop, Stop toStop, int departureTime, int arrivalTime) throws IllegalArgumentException{
            if (departureTime < 0 || arrivalTime < 0){
                throw new IllegalArgumentException("l'heure d'arrivée ou l'heure de départ est invalide");
            }
            else if (arrivalTime - departureTime < 0){
                throw new IllegalArgumentException("l'heure d'arrivée est antérieure à l'heure de départ");
            }
            else if(!(stops.contains(fromStop)) || !(stops.contains(toStop))){
                throw new IllegalArgumentException("l'arrêt de départ ou l'arrêt d'arrivée est invalide");
            }
            getBuilder(fromStop, toStop).addTrip(departureTime, arrivalTime);
            return this;
        }

        private GraphEdge.Builder getBuilder(Stop fromStop, Stop toStop){
            if(!graphBuilder.containsKey(fromStop)){
                graphBuilder.put(fromStop, new HashMap<Stop, GraphEdge.Builder>());
            }
            if (!graphBuilder.get(fromStop).containsKey(toStop)) {
                graphBuilder.get(fromStop).put(toStop, new GraphEdge.Builder(toStop));               
            }
            return graphBuilder.get(fromStop).get(toStop);
        }

        /**
         * Ajoute un trajet a pied au batisseur avec le temps maximal de marche (si le trajet a pied 
         * est plus long qu'en Service, c'est inutile) et la vitesse de marche
         * @param maxWalkingTime
         *          Temps de marche maximal pour que le trajet a pied soit utile et plus interessant qu'un
         *          service
         * @param walkingSpeed
         *          vitesse de marche qui permet de calculer le temps de marche requis entre deux arrets
         * @throws IllegalArgumentException
         *          si le temps de marche maximal est négatif
         *          si la vitesse de marche est négative ou nulle
         * @return this (la methode ne retourne rien, elle ne fait qu'ajouter un trajet a pied au batisseur en cours de construction)
         */

        public Builder addAllWalkEdges(int maxWalkingTime, double walkingSpeed) throws IllegalArgumentException{
            if(walkingSpeed <= 0 || maxWalkingTime < 0){
                throw new IllegalArgumentException("la vitesse de marche ou le temps maximum de marche est invalide");
            }
            List<Stop> arrets = new ArrayList<Stop>(stops);
            for(int i=0; i<arrets.size()-1; i++){
                for(int j=i+1; j<arrets.size(); j++){
                    double distance = arrets.get(i).position().distanceTo(arrets.get(j).position());

                    if(distance/walkingSpeed <= maxWalkingTime){
                        getBuilder(arrets.get(i), arrets.get(j)).setWalkingTime((int)Math.round(distance/walkingSpeed));
                        getBuilder(arrets.get(j), arrets.get(i)).setWalkingTime((int)Math.round(distance/walkingSpeed));
                    }
                }
            }
            return this;

        }        

        /**
         * Construit un Graph d'horaire a partir de son Batisseur

         * @return  Un graphe d'horaire ayant été construit avec le Set des arrets et la Table qui 
         *          aux arrets associent les arrets suivants et leur trajet.
         * 
         */          

        public Graph build(){

            for (Stop s : stops) {
                ArrayList<Stop> stop = new ArrayList<>(stops);
                List<GraphEdge> gE = new ArrayList<>();
                for (int i = 0; i < stop.size(); i++) {
                    if(!stop.get(i).name().equals(s.name())){
                        gE.add(getBuilder(s, stop.get(i)).build());
                    }
                }
                outgoingEdges.put(s, gE);
            }
            return new Graph(stops, outgoingEdges);

        }
    }
}

