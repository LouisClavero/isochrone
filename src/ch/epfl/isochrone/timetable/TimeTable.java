package ch.epfl.isochrone.timetable;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

/**
 * Une table d'horaire construite à partir d'un Set stops et d'un Set services.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */


public class TimeTable {

    private Set<Stop> stops;
    private Set<Service> services;
    
    /**
     * construit une table d'horaire a partir des Set passés en argument
     * @param   stops
     *          Set des différents stops que l'on veut ajouter à la table
     * @param   services
     *          Set des différents services que l'on veut ajouter à la table
     */

    public TimeTable(Set<Stop> stops, Collection<Service> services){

        this.stops = new HashSet<Stop>(stops);
        this.services = new HashSet<Service>(services);

    }
    
    /**
     * retourne un Hashset de stops
     * @return Hashset de stop
     */

    public Set<Stop> stops(){

        return new HashSet<Stop>(stops);

    }
    
    /**
     * ajoute un service à un set si ce service est actif à la date passée en argument
     * @param   date
     *          date pour laquelle on veut verifier si il existe des services et les ajouter à un set
     * @return set servicesForDates qui contient l'ensemble des services actifs à la date donnée en argument
     */

    public Set<Service> servicesForDate(Date date){

        Set<Service> servicesForDate = new HashSet<Service>();
        for(Service s1: services){

            if (s1.isOperatingOn(date)){
                servicesForDate.add(s1);
            }    
        }
        return servicesForDate;

    }

    public static class Builder {

        private Set<Stop> stops = new HashSet<Stop>();
        private Collection<Service> services = new HashSet<Service>();
        
        /**
         * ajoute un arret au Batisseur de Table d'horaire
         * @param newStop
         *          arret que l'on souhaite ajouter au batisseur
         * @return this (la methode ne retourne rien, elle ne fait qu'ajouter un arret au batisseur de Table d'horaire
         */

        public Builder addStop(Stop newStop){

            stops.add(newStop);
            return this;

        }
        
        /**
         * ajoute un Service au Batisseur de Table d'horaire
         * @param newService
         *          service que l'on souhaite ajouter au batisseur de la table d'horaire
         * @return this (la methode ne retourne rien, elle ne fait qu'ajouter un arret au batisseur de Table d'horaire
         */

        public Builder addService(Service newService){

            services.add(newService);
            return this;

        }
        
        /**
         * Construit une table d'horaire a partir de son Batisseur

         * @return  Une table d'horaire ayant été construite par partie via le Batisseur
         */

        public TimeTable build() {

            return new TimeTable(stops, services);

        }
    }


}