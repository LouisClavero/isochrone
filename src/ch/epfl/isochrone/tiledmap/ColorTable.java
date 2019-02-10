package ch.epfl.isochrone.tiledmap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class ColorTable {

    List<Color> colorList = new ArrayList<Color>();
    int duration;

   
    public ColorTable(int duration, List<Color> colorList) throws IllegalArgumentException{
       
        if(duration<0 || colorList.isEmpty()){
            throw new IllegalArgumentException("la durée est négative ou il n'y a pas de couleur dans la liste");
        }
        this.duration=duration;
        this.colorList = colorList;

        this.colorList.add(new Color(1.0f, 0.0f, 0.0f));
        this.colorList.add(new Color(1.0f, 0.5f, 0.0f));
        this.colorList.add(new Color(1.0f, 1.0f, 0.0f));
        this.colorList.add(new Color(0.5f, 1.0f, 0.0f));
        this.colorList.add(new Color(0.0f, 1.0f, 0.0f));
        this.colorList.add(new Color(0.0f, 0.5f, 0.5f));
        this.colorList.add(new Color(0.0f, 0.0f, 1.0f));
        this.colorList.add(new Color(0.0f, 0.0f, 0.5f));
        this.colorList.add(new Color(0.0f, 0.0f, 0.0f));

    }

    public int duration(){
       
        return duration;
       
    }
   
    public Color intervalColor(int zone) throws IllegalArgumentException{
       
        if(zone < 0 || zone > colorList.size()-1){
            throw new IllegalArgumentException("l'index est négatif ou plus grand que la taille de la liste des couleurs");
        }
       
        return colorList.get(zone);
        
    }
   
    public int zoneNumber(List<Color> colorList){
       
        return colorList.size();
       
    }
   
}