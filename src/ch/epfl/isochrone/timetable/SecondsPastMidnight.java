package ch.epfl.isochrone.timetable; 
import ch.epfl.isochrone.math.Math; 

/**
 * Le nombre de seconde écoulées après minuit.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class SecondsPastMidnight { 
    
    public static final int INFINITE = 200000; 
    
    /**
     * Retourne le nombre de secondes à partir du nombre d'heures, de minutes et de secondes.
     *
     * @param   hours
     *          Le nombre d'heure.
     * @param   minutes
     *          Le nombre de minute.
     * @param   seconds
     *          Le nombre de secondes.
     * @throws  IllegalArgumentException
     *          Si le nombre de secondes et de minutes n'est pas dans l'intervalle [0;60]
     *          Si le nombre d'heures n'est pas dans l'intervalle [0;30] (30 est arbitraire).
     * @return  le nombre total de secondes par rapport au nombre d'heures, de minutes et de 
     *          secondes passées en argument.
     *          niveau de zoom donné.
     */
    
    public static int fromHMS(int hours, int minutes, int seconds) throws IllegalArgumentException{ 
        
        if(hours < 0 || hours >= 30){ 
            
            throw new IllegalArgumentException("l'heure est invalide"); 
        } 
        else if (minutes < 0 || minutes >= 60){ 
            
            throw new IllegalArgumentException("le nombre de minutes est invalide");
        } 
        else if (seconds < 0 || seconds >= 60){ 
            
            throw new IllegalArgumentException("le nombre de secondes est invalide"); 
        } 
        return hours*3600 + minutes*60 + seconds; 
        
    } 
    
    /**
     * convertit l'heure d'une date Java  en un nombre de secondes après minuit. 
     *
     * @param   date
     *          L'heure d'une date au format java.
     * @return  le nombre de secondes via la methode fromHMS après avoir récupérer les 
     *          heures, minutes et secondes de la date Java.
     */
    
    @SuppressWarnings("deprecation") 
    public static int fromJavaDate(java.util.Date date){ 
        
        return fromHMS(date.getHours(), date.getMinutes(), date.getSeconds()); 
        
    } 
    
    /**
     * Retourne le nombre d'heures de l'heure (représentée par un nombre de secondes après minuit) passée en argument.
     *
     * @param   spm
     *          nombre de secondes après minuit.
     * @throws  IllegalArgumentException
     *          si le nombre de secondes après minuit est supérieur ou égal à 108000 (un jour, 24h).
     * @return  le nombre d'heures de l'heure passée en argument.
     */
        
    public static int hours(int spm) throws IllegalArgumentException { 
        
        if(spm < 0 || spm > 107999){ 
            
            throw new IllegalArgumentException("le nombre de secondes après minuit est invalide"); 
            
        } 
        return Math.divF(spm, 3600); 
        
    } 
    
    /**
     * Retourne le nombre de minutes de l'heure (représentée par un nombre de secondes après minuit) passée en argument.
     *
     * @param   spm
     *          nombre de secondes après minuit.
     * @throws  IllegalArgumentException
     *          si le nombre de secondes après minuit est supérieur ou égal à 108000 (un jour, 24h) ou négatif.
     * @return  le nombre de minutes de l'heure passée en argument.
     */
    
    public static int minutes(int spm) throws IllegalArgumentException { 
        
        if(spm < 0 || spm > 107999){ 
            
            throw new IllegalArgumentException("le nombre de secondes après minuit est invalide"); 
        } 
        
        return Math.divF(Math.modF(spm, 3600), 60); 
        
    } 
    
    /**
     * Retourne le nombre de secondes de l'heure (représentée par un nombre de secondes après minuit) passée en argument.
     *
     * @param   spm
     *          nombre de secondes après minuit.
     * @throws  IllegalArgumentException
     *          si le nombre de secondes après minuit est supérieur ou égal à 108000 (un jour, 24h) ou négatif.
     * @return  le nombre de secondes de l'heure passée en argument.
     */
    
    public static int seconds (int spm) throws IllegalArgumentException{ 
        
        if(spm < 0 || spm > 107999){ 
            
            throw new IllegalArgumentException("le nombre de secondes après minuit est invalide");
        } 
        
        return Math.modF(Math.modF(spm, 3600), 60);
    } 
    
    /**
     * retourne la représentation textuelle du nombre de secondes après minuit passé en argument.
     *
     * @param   spm
     *          nombre de secondes après minuit.
     * @throws  IllegalArgumentException
     *          si le nombre de secondes après minuit est supérieur ou égal à 108000 (un jour, 24h) ou négatif.
     * @return  retourne la représentation textuelle du nombre de secondes après minuit passé en argument. 
     * Cette représentation consiste en un nombre d'heures, de minutes et de secondes, chacun représenté par 
     * deux chiffres, séparés par un double point (:)
     */
    
    public static String toString(int spm) throws IllegalArgumentException{ 
        
       if(spm < 0 || spm > 107999){
           
           throw new IllegalArgumentException("le nombre de secondes après minuit est invalide");
       }
       
       return String.format("%02d:%02d:%02d", hours(spm), minutes(spm), seconds(spm));
    } 
    
}