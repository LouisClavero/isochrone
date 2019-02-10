package ch.epfl.isochrone.timetable;

import ch.epfl.isochrone.math.Math;

/**
 * Une date composée de jour, mois et année.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class Date {

    private final int day;
    private final int year;
    private final Month month;

    public enum DayOfWeek{
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    };

    public enum Month{
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    };

    /**
     * Construit une nouvelle Date dont le mois est de type DayOfWeek
     *
     * @param   day
     *          le jour entier de la Date à construire
     * @param   month
     *          le mois (de l'enumeration DayOfWeek) de la Date à construire.
     * @param   year
     *          l'année entière de la Date à construire.       
     * @throws  IllegalArgumentException
     *          Si le nombre de jour est strictement inférieur à 1 ou s'il est supérieur au nombre de jours 
     *          dans le mois passé en argument. 
     */

    
    public Date(int day, Month month, int year) throws IllegalArgumentException{
        if (day < 1 || day > daysInMonth(month, year)){
            throw new IllegalArgumentException("le jour est invalide");
        }
        this.day = day;
        this.month = month;
        this.year = year;

    }
    
    /**
     * Construit une nouvelle Date dont le mois est de type entier
     *
     * @param   day
     *          le jour entier de la Date à construire
     * @param   month
     *          le mois entier de la Date à construire.
     * @param   year
     *          l'année entière de la Date à construire.       
     * @throws  IllegalArgumentException
     *          Si le nombre de jour est strictement inférieur à 1 ou s'il est supérieur au nombre de jours 
     *          Si le mois n'appartient pas à l'intervalle [1;12]. 
     */

    public Date(int day, int month, int year) throws IllegalArgumentException{
        if (day < 1 || day> daysInMonth(intToMonth(month), year) || month < 1 || month > 12){
            throw new IllegalArgumentException("le jour ou le mois est invalide");
        }
        this.day = day;
        this.month = intToMonth(month);
        this.year = year;

    }
    
    /**
     *  construit une date en fonction d'une date Java
     *
     * @param   date
     *          Une date java      
     * @return  Une date formée à partir d'une date java.
     */


    @SuppressWarnings("deprecation")
    public Date(java.util.Date date){
        this(date.getDate(), date.getMonth() + 1, date.getYear() + 1900);
    }
    
    /**
     *  retourne le jour du mois de la date
     *  @return  le jour du mois de la date, compris entre 1 et 31
     */

    public int day(){
        return day;
    }
    
    /**
     *  retourne le mois de la date de l'enumeration
     *  @return  le mois de la date, de type DayOfWeek
     */

    public Month month(){
        return month;
    }
    
    /**
     *  retourne le mois de la date entier
     *  @return  le mois de la date, de type entier
     */

    public int intMonth(){
        return month.ordinal() + 1;
    }
    
    /**
     *  retourne l'année de la date entière
     *  @return  l'année de la date, de type entière
     */

    public int year(){
        return year;
    }

    /**
     *  retourne le jour de la semaine de la date. 
     *  @return  le jour de la semaine de la date appartenant à l'énumeration DayOfWeek
     */
    
    public DayOfWeek dayOfWeek(){
        DayOfWeek[] jours = DayOfWeek.values();
        return jours[Math.modF(fixed() - 1, 7)];
    }
    
    /**
     * retourne la date distante du nombre de jours donnés de la date à laquelle on l'applique
     * @param   daysDiff
     *          le nombre de jour séparant la date à laquelle on l'applique et celle passé en argument
     * @return  la date distante du nombre de jours donnés de la date à laquelle on l'applique
     */
    
    public Date relative(int daysDiff){

        return fixedToDate(fixed() + daysDiff);
    }
    
    /** 
     * retourne la date Java correspondant à cette date. 
     * 
     * @return la date Java correspondant à cette date.
     */

    @SuppressWarnings("deprecation")
    public java.util.Date toJavaDate(){

        return new java.util.Date(year - 1900, monthToInt(month)-1, day);       
    }
    
    /**
     * Retourne une représentation textuelle de la date.
     *
     * @return  retourne la représentation textuelle de la date, qui doit 
     * être formée de l'année, du numéro du mois (sur deux chiffres) et du 
     * jour (sur deux chiffres), séparés par un tiret (-)
     */

    public String toString(){
        if(monthToInt(month) < 10){
            return (year + "-" + "0" + monthToInt(month) + "-" + day);
        }
        else return (year + "-" + monthToInt(month) + "-" + day);
    }
    
    /**
     * retourn vrai si la date à laquelle on l'applique est égale à la date passée en argument
     * @param   date
     *          date que l'on souhaite comparer à celle qu'on applique
     * @return  vrai si les deux dates sont égales
     *          faux si les deux dates sont différentes
     */    

    public boolean equals(Object that){
        if (that instanceof Date && compareTo((Date)that) == 0){
            return true;
        }
        else return false;
    }

    /**
     * retourne l'entier correspondant à la date.
     * @return l'entier correspondant à la date exrimée en jour, mois et année.
     */
    
    public int hashCode(){
        return dateToFixed(day, month, year);

    }
    
    /**
     * 
     * @param   that
     *          date passée en argument que l'on souhaite comparer à la date à laquelle on l'applique
     * @return  1 si that est inférieure à la date à laquelle on l'applique.
     *          0 si les deux dates sont égales.
     *          -1 si that est supérieur à la date à laquelle on l'applique.
     */

    public int compareTo(Date that){
        final int INFERIOR = -1;
        final int EQUAL = 0;
        final int SUPERIOR = 1;

        if(fixed() < that.fixed()){
            return INFERIOR;
        }
        else if(fixed() == that.fixed()){
            return EQUAL;
        }
        else return SUPERIOR;

    }

    private static Month intToMonth(int m) throws IllegalArgumentException{
        if (m < 1 || m > 12){
            throw new IllegalArgumentException("le mois est invalide");
        }
        Month[] mois = Month.values();
        return mois[m-1];
    }

    private static int monthToInt(Month m){
        return (m.ordinal()) + 1;

    }

    private static boolean isLeapYear(int y){
        if ((Math.modF(y, 4) == 0 && !(Math.modF(y, 100) == 0)) || Math.modF(y, 400) == 0){
            return true;
        }
        else return false;
    }

    private static int daysInMonth(Month m, int y){

        if(m == Month.JANUARY || m == Month.MARCH || m == Month.MAY || m == Month.JULY
                || m == Month.AUGUST || m == Month.OCTOBER || m == Month.DECEMBER){
            return 31;
        }
        else if (m == Month.APRIL || m == Month.JUNE || m == Month.SEPTEMBER || m == Month.NOVEMBER){
            return 30;
        }
        else if (m == Month.FEBRUARY && isLeapYear(y)==true){
            return 29;
        }
        else if (m == Month.FEBRUARY) {
            return 28;
        }
        else return 0;

    }

    private static int dateToFixed(int d, Month m, int y){
        int c;
        if(monthToInt(m) <= 2){
            c = 0;
        }
        else if(monthToInt(m) > 2 && (Math.modF(y, 4) == 0 && Math.modF(y, 100) != 0) || Math.modF(y, 400) == 0){
            c = -1;
        }
        else c = -2;
        return 365*(y-1)+Math.divF(y-1,4) - Math.divF(y-1, 100) + Math.divF(y-1, 400) +Math.divF((367*monthToInt(m) - 362), 12)+c+d;

    }

    private static Date fixedToDate(int n){
        
        int d0 = n-1;
        int n400 = Math.divF(d0, 146097);
        int d1 = Math.modF(d0, 146097);
        int n100 = Math.divF(d1, 36524);
        int d2 = Math.modF(d1, 36524);
        int n4 = Math.divF(d2, 1461);
        int d3 = Math.modF(d2, 1461);
        int n1 = Math.divF(d3, 365);
        int y0 = 400*n400 + 100*n100 + 4*n4 + n1;

        int y;
        if(n100 == 4 || n1 == 4){
            y = y0;
        }
        else y = y0 + 1;

        int p = n - dateToFixed(1, intToMonth(1), y);

        int c;
        if(n < dateToFixed(1, intToMonth(3), y)){
            c = 0;
        }
        else if (n >= dateToFixed(1, intToMonth(3), y) && (Math.modF(y, 4) == 0 && Math.modF(y, 100) != 0) || Math.modF(y, 400) == 0){
            c = 1;
        }
        else c = 2;

        int m = Math.divF((12*(p + c) + 373), 367);
        int d = n - dateToFixed(1, intToMonth(m), y) + 1;
        return new Date (d, intToMonth(m), y);

    }

    private int fixed(){

        return dateToFixed(day, month, year);

    }

}