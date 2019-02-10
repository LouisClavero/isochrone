package ch.epfl.isochrone.math;

/**
 * Une classe facilitant les calculs dans certaines methodes des autres classes.
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class Math{

    /**
     * retourne le sinus hyperbolique inverse de son argument
     *
     * @param   x
     *          un nombre réel.
     * @return le sinus hyperbolique inverse du nombre x passé en argument
     */

    public static double asinh(double x){
        return java.lang.Math.log(x + java.lang.Math.sqrt(1+java.lang.Math.pow(x, 2)));
    }
    
    /**
     * retourne le resultat de la fonction haversin
     *
     * @param   x
     *          un nombre réel.
     * @return le resultat de la fonction haversin du nombre x passé en argument.
     */

    public static double haversin(double x) {
        return java.lang.Math.pow(java.lang.Math.sin(x/2), 2);
    }
    
    /**
     * retourne le quotient de la division par défaut de n par d
     *
     * @param   n
     *          nombre réel que l'on va diviser
     * @param   d
     *          nombre réel par lequel on va diviser n.
     * @return le quotient de la division euclidienne de n par d
     */

    public static int divF(int n, int d){
        int I;
        if(Integer.signum(n%d) == -Integer.signum(d)){
            I = 1;
        }
        else I = 0;
        return n/d - I;

    }
    
    /**
     * retourne le reste de la division par défaut de n par d
     *
     * @param   n
     *          nombre réel que l'on va diviser
     * @param   d
     *          nombre réel par lequel on va diviser n.
     * @return le reste de la division euclidienne de n par d
     */

    public static int modF(int n, int d){
        int I;
        if(Integer.signum(n%d) == -Integer.signum(d)){
            I = 1;
        }
        else I = 0;
        return n%d + I*d;

    }
}
