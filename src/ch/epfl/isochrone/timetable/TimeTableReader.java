

package ch.epfl.isochrone.timetable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.timetable.Date.DayOfWeek;

/**
 * Un lecteur de de Table d'horaire
 *
 * @author Louis Clavero (233933)
 * @author Thomas Artiach (238868)
 */

public final class TimeTableReader {

    private final String baseResourceName;


    /**
     * Construit un lecteur d'horaire ayant la chaine donnée comme préfixe des
 ressources
     * @param baseResourceName
     * chaine donnée servant de préfixe des ressources
     */

    
    public TimeTableReader(String baseResourceName) {
        
        this.baseResourceName = baseResourceName;
    }

    /**
     * Lit et retourn l'horaire
     * @throws IOException
     * en cas d'erreur d'entrée-sortie ou d'autres exceptions en cas d'erreur de
 format de données.
     * @return l'horaire après l'avoir lu dans le fichier
     *
     */

    public TimeTable readTimeTable() throws IOException{
        return new TimeTable(stopsReader(), calendarDatesReader());
    }


    /**
     * lit et retourne le graphe des horaires pour les arrêts donnés
     * @param stops
     * Set d'arrets
     * @param services
     * Set de services
     * @param walkingTime
     * temps de marche entier
     * @param walkingSpeed
     * vitesse de marche (double)
     * @throws IOException
     * en cas d'erreur d'entrée-sortie ou d'autres exceptions en cas d'erreur de
 format de données.
     * @return le graphe des horaires pour les arrêts donnés, en ne considérant
 que les trajets dont le service
     * fait partie de l'ensemble donné. Ce graphe inclut également la totalité des
 trajets à pied entre
     * arrêts qui sont faisables en un temps inférieur ou égal à celui donné (en
 secondes), à la vitesse
     * de marche donnée (en mètres par secondes).
     */

    public Graph readGraphForServices(Set<Stop> stops, Set<Service> services, int
            walkingTime, double walkingSpeed) throws IOException{

        Graph.Builder graphb = new Graph.Builder(stops);
        BufferedReader stopTimes = new BufferedReader(new
                InputStreamReader(getClass().getResourceAsStream(baseResourceName+"stop_times.csv"), StandardCharsets.UTF_8));
        String line;
        Map<String, Stop> stopNames = new HashMap<String, Stop>();
        Map<String, Service> serviceName = new HashMap<String, Service>();
        for (Service service : services) {
            serviceName.put(service.name(), service);
        }
        for (Stop s : stops) {
            stopNames.put(s.name(), s);
        }
        while ((line = stopTimes.readLine()) != null) {
            String[] tableau2 = line.split(";");
            if (serviceName.containsKey(tableau2[0])) {
                graphb.addTripEdge(stopNames.get(tableau2[1]), stopNames.get(tableau2[3]), Integer.parseInt(tableau2[2]), Integer.parseInt(tableau2[4]));
                }
            }
            stopTimes.close();
        graphb.addAllWalkEdges(walkingTime, walkingSpeed);
        return graphb.build();
    }


    private Set<Service> calendarDatesReader() throws IOException{
        Set<Service.Builder> build = new HashSet<Service.Builder>(calendarReader());
        Set<Service> services = new HashSet<Service>();

        BufferedReader calendarDate = new BufferedReader(new
                InputStreamReader(getClass().getResourceAsStream(baseResourceName+"calendar_dates.csv"), StandardCharsets.UTF_8));
        String line;
        while ((line = calendarDate.readLine()) != null) {
            String[] tableau1 = line.split(";");
            for(Service.Builder b1 : calendarReader()){
                if(b1.name().equals(tableau1[0])){
                    if(tableau1[2].equals("1")){
                        b1.addIncludedDate(new Date(
                                Integer.parseInt(tableau1[1])%100,
                                (Integer.parseInt(tableau1[1])%10000)/100,
                                Integer.parseInt(tableau1[1])/10000));
                    }
                    else {
                        b1.addExcludedDate(new Date(
                                Integer.parseInt(tableau1[1])%100,
                                (Integer.parseInt(tableau1[1])%10000)/100,
                                Integer.parseInt(tableau1[1])/10000));
                    }
                }
            }
        }
        for(Service.Builder sb : build){
            services.add(sb.build());
        }
        calendarDate.close();
        return services;
    }

    private Set<Service.Builder> calendarReader() throws IOException{
        Set<Service.Builder> serviceBuilder = new HashSet<Service.Builder>();
        BufferedReader calendar = new BufferedReader(new
                InputStreamReader(getClass().getResourceAsStream(baseResourceName+"calendar.csv"), StandardCharsets.UTF_8));
        String line;
        DayOfWeek[] dayList = DayOfWeek.values();
        while ((line = calendar.readLine()) != null) {

            String[] tableau = line.split(";");
            Service.Builder service = new Service.Builder(tableau[0],
                    new Date(Integer.parseInt(tableau[8])%100,
                            (Integer.parseInt(tableau[8])%10000)/100,
                            Integer.parseInt(tableau[8])/10000),
                            new Date(Integer.parseInt(tableau[9])%100,
                                    (Integer.parseInt(tableau[9])%10000)/100,
                                    Integer.parseInt(tableau[9])/10000)
                    );
            for(int i=1; i<8; i++){
                if(tableau[i].equals("1")){
                    service.addOperatingDay(dayList[i-1]);
                    serviceBuilder.add(service);
                }
            }

        }
        calendar.close();
        return serviceBuilder;
    }

    private Set<Stop> stopsReader() throws IOException{ 
        Set<Stop> stopBuilder = new HashSet<Stop>();
        BufferedReader stops = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(baseResourceName+"stops.csv"),
                StandardCharsets.UTF_8));
        String line;
        while ((line = stops.readLine()) != null) {
            String[] tableau3 = line.split(";");
            stopBuilder.add(new Stop(tableau3[0], new PointWGS84(
                    Math.toRadians(Double.parseDouble(tableau3[2])),
                    Math.toRadians(Double.parseDouble(tableau3[1])))));
        }
        stops.close();
        return stopBuilder;
    }

}

