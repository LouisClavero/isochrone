package ch.epfl.isochrone.tiledmap;
import ch.epfl.isochrone.tiledmap.TileCache;

public class CachedTileProvider implements  TileProvider{
	
	private TileCache tileCache = new TileCache();
	private TileProvider tileProvider;
	
	public CachedTileProvider(TileProvider tileProvider){
		this.tileProvider = tileProvider;
	}

	@Override
	public Tile tileAt(int zoom, int x, int y) {
		if(tileCache.get(zoom, x, y) == null){
			tileCache.put(zoom, x, y, tileProvider.tileAt(zoom, x, y));
		}
		return tileCache.get(zoom, x, y);
	}
	


}
