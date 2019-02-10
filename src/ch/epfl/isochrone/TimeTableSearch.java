package ch.epfl.isochrone;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.epfl.isochrone.timetable.Date;
import ch.epfl.isochrone.timetable.FastestPathTree;
import ch.epfl.isochrone.timetable.Graph;
import ch.epfl.isochrone.timetable.SecondsPastMidnight;
import ch.epfl.isochrone.timetable.Stop;
import ch.epfl.isochrone.timetable.TimeTable;
import ch.epfl.isochrone.timetable.TimeTableReader;

public class TimeTableSearch {

    private static final double walkingSpeed = 1.25;
    private static final int maxWalkingTime = 300;

    /**
     * Classe TimeTableSearch
     * @param args
     *          argument string contenant les informations a transformer en Dates et horaire
     * @throws IOException
     * @return 
     */

    public static void main(String[] args) throws IOException{

        Date d = stringToDate(args[1]);	    
        int departureTime = stringToHour(args[2]);	    
        TimeTableReader t = new TimeTableReader("/time-table/");		
        TimeTable time = t.readTimeTable();		
        Graph g = t.readGraphForServices(time.stops(), time.servicesForDate(d), maxWalkingTime, walkingSpeed);

        Map<String, Stop> stopMap = generateMapStop(time.stops());
        FastestPathTree fpt = g.fastestPaths(stopMap.get(args[0]), departureTime);
        List<String> names = new ArrayList<String>();

        for(Stop s2: fpt.stops()){
            names.add(s2.name());
        }

        java.util.Collections.sort(names);

        for(String n: names){
            System.out.println(stopMap.get(n) + " : " + SecondsPastMidnight.toString(fpt.arrivalTime(stopMap.get(n))));
            System.out.println(" via: " + fpt.pathTo(stopMap.get(n)));
        }

    }

    private static Date stringToDate(String string) {

        String[] tab = string.split("-");
        return new Date(Integer.parseInt(tab[2]), Integer.parseInt(tab[1]), 
                Integer.parseInt(tab[0]));
    }

    private static int stringToHour(String string) {
        String[] tab = string.split(":");
        return SecondsPastMidnight.fromHMS(Integer.parseInt(tab[0]),
                Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }

    private static Map<String, Stop> generateMapStop(Set<Stop> stops) {
        Map<String, Stop> map = new HashMap<>();
        for (Stop stop : stops) {
            map.put(stop.name(), stop);
        }
        return map;

    }

}