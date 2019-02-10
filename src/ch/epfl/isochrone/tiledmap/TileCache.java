package ch.epfl.isochrone.tiledmap;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.epfl.isochrone.geo.PointOSM;
import ch.epfl.isochrone.tiledmap.Tile;

public class TileCache {

	private LinkedHashMap<Long, Tile> cache = new LinkedHashMap<Long, Tile>(){
		
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<Long,Tile> e){
			return size() > 100;
		}
	};

	public static long packPosition(int zoom, int x, int y) throws IllegalArgumentException{
		if(zoom<0 || zoom>20){
			throw new IllegalArgumentException("le niveau de zoom est invalide");
		}
		else if (x<0 || x>java.lang.Math.pow(2, 20)){
			throw new IllegalArgumentException("la valeur de x est invalide");
		}
		else if (y<0 || y>java.lang.Math.pow(2, 20)){
			throw new IllegalArgumentException("la valeur de y est invalide");
		}
		return (long) (y*java.lang.Math.pow(10, 9) + x*100 + zoom);
	}

	public void put(int zoom, int x, int y, Tile tile){
		cache.put(packPosition(zoom, x, y), tile);
	}

	public Tile get(int zoom, int x, int y){
		if(cache.containsKey(packPosition(zoom, x, y))){
			return cache.get(new PointOSM(zoom, x, y));
		}else{
			return null;	
		}

	}	
}	