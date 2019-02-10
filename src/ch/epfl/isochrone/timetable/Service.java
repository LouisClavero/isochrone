package ch.epfl.isochrone.timetable;
import java.util.HashSet;

import java.util.Set;

/**
 * Un Service construit a partir de son nom, de ses dates de début et fin, de ses jours opérants, de ses dates incluses et exclues.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class Service {

    private final String name;
    private Date startingDate;
    private Date endingDate;
    private Set<Date.DayOfWeek> operatingDays;
    private Set<Date> excludedDates;
    private Set<Date> includedDates;

    /**
     * Construit un nouveau service
     * @param   name
     *          nom du service à construiree
     * @param   startingDate
     *          date de début du service
     * @param   endingDate
     *          date de fin du service
     * @param   operatingDays
     *          Set de jours de la semaines opérants
     * @param   excludedDates
     *          Set de dates exclues
     * @param   includedDates
     *          Set de dates inclues
     * @throws  IllegalArgumentException
     *          si la date de fin est antérieure à la date de début
     *          si une des dates inclues ou exclues est en dehors de la plage de validité du service
     *          si le Set des dates incluses contient une date exclue
     */


    public Service(String name, Date startingDate, Date endingDate, Set<Date.DayOfWeek> operatingDays, Set<Date> excludedDates,
            Set<Date> includedDates) throws IllegalArgumentException{

        if (endingDate.compareTo(startingDate) == -1){
            throw new IllegalArgumentException(" la date de fin est antérieure à la date de début");
        }
        for(Date eD : excludedDates){
            if(eD.compareTo(startingDate) == -1 || eD.compareTo(endingDate) == 1){
                throw new IllegalArgumentException("une des dates listées dans les exceptions est en dehors de la plage de validité du service");
            }
        }
        for(Date iD : includedDates){
            if(iD.compareTo(startingDate) == -1 || iD.compareTo(endingDate) == 1){
                throw new IllegalArgumentException("une des dates listées dans les exceptions est en dehors de la plage de validité du service");
            }
        }
        for(Date eD : excludedDates){
            if(includedDates.contains(eD)){
                throw new IllegalArgumentException("l'intersection entre les dates exclues et incluses n'est pas vide");
            }
        }
        this.name=name;
        this.startingDate=startingDate;
        this.endingDate=endingDate;
        this.operatingDays = new HashSet<Date.DayOfWeek>(operatingDays);
        this.excludedDates = new HashSet<Date>(excludedDates);
        this.includedDates = new HashSet<Date>(includedDates);


    }

    /**
     * Retourne le nom du service
     *
     * @return  le nom du service;
     */

    public String name(){

        return name;
    }

    /**
     * verifie  si le service fonctionne à la date passée en argument
     * @param   date
     *          date pour laquelle on veut verifier si le service est actif
     * @return  vrai si le service est actif à la date donnée
     *          faux si le service n'est pas actif à la date donnée
     */

    public boolean isOperatingOn(Date date){
        return (date.compareTo(startingDate)>=0 && date.compareTo(endingDate) <= 0 &&
                operatingDays.contains(date.dayOfWeek()) && (!excludedDates.contains(date) || includedDates.contains(date)));
    }

    /**
     * Retourne une représentation textuelle du Service.
     *
     * @return retourne une représentation textuelle du service, en l'occurrence
     * son nom. Redéfinit la méthode toString héritée de Object. 
     */

    public String toString(){
        return (name);
    }


    public static final class Builder{

        private Date startingDate;
        private Date endingDate;
        private final String name;
        private Set<Date.DayOfWeek> operatingDays = new HashSet<>();
        private Set<Date> excludedDates = new HashSet<>();
        private Set<Date> includedDates = new HashSet<>();


        /**
         * Construit un Batisseur de Service
         * @param   name
         *          nom du Service
         * @param   startingDate
         *          date de Début du service
         * @param   endingDatee
         *          date de Fin du service
         * @throws  IllegalArgumentException
         *          si la date de fin de service est antérieure à la date de début
         * @return  un batisseur de Service 
         */


        public Builder(String name, Date startingDate, Date endingDate) throws IllegalArgumentException{

            if (startingDate.compareTo(endingDate) >= 0){
                throw new IllegalArgumentException("la date de fin est antérieure à la date de début");
            }

            this.startingDate=startingDate;
            this.endingDate=endingDate;
            this.name=name;
        }

        /**
         * Retourne le nom du Service.Builder
         *
         * @return  le nom du Service.Builder;
         */

        public String name(){

            return name;

        }

        /**
         * ajoute un jour opérant au Builder
         * @param day
         *        jour opérant que l'on souhaite ajouter au Builder
         * @return this (la methode ne retourne rien en elle-même, elle ne fait qu'ajouter un élément au Builder)
         */

        public Builder addOperatingDay(Date.DayOfWeek day){

            operatingDays.add(day);
            return this;

        }

        /**
         * ajoute   une Date Exclues au Builder
         * @param   date
         *          date exclues que l'on souhaite ajouter au Builder 
         * @throws  IllegalArgumentException
         *          si la date passée en argument n'est pas dans la plage de validité du service ou si le Set
         *          des dates inclues contient la date passée en argument
         * @return  this (la methode ne retourne rien en elle-même, elle ne fait qu'ajouter un élément au Builder)
         */

        public Builder addExcludedDate(Date date) throws IllegalArgumentException{

            if (date.compareTo(startingDate) == -1 || date.compareTo(endingDate) == 1 || includedDates.contains(date)){

                throw new IllegalArgumentException("la date n'est pas dans la plage de validité du service en construction, ou elle fait partie des dates incluses");

            }

            excludedDates.add(date);
            return this;

        }

        /**
         * ajoute   une Date Incluses au Builder
         * @param   date
         *          date inclue que l'on souhaite ajouter au Builder 
         * @throws  IllegalArgumentException
         *          si la date passée en argument n'est pas dans la plage de validité du service ou si le Set
         *          des dates exclues contient la date passée en argument
         * @return  this (la methode ne retourne rien en elle-même, elle ne fait qu'ajouter un élément au Builder)
         */

        public Builder addIncludedDate(Date date) throws IllegalArgumentException{

            if (date.compareTo(startingDate) == -1 || date.compareTo(endingDate) == 1 || excludedDates.contains(date)){

                throw new IllegalArgumentException("la date n'est pas dans la plage de validité du service en construction, ou elle fait partie des dates exclues");

            }

            includedDates.add(date);
            return this;

        }

        /**
         * Construit un Service a partir du Batisseur

         * @return  Un Service ayant été construit par partie via le Batisseur
         */

        public Service build(){

            return new Service(name, startingDate, endingDate, operatingDays, excludedDates, includedDates);
        }

    }

}