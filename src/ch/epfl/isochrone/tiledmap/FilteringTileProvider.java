package ch.epfl.isochrone.tiledmap;

import java.awt.image.BufferedImage;

public abstract class FilteringTileProvider implements TileProvider{


	private TileProvider tileProvider;

	@Override
	public Tile tileAt(int zoom, int x, int y) {
		Tile tile = tileProvider.tileAt(zoom, x, y);
		BufferedImage image = tile.image();
		for(int i=0; i<257; i++){
			for(int j=0; j<257; j++){
                 image.setRGB(i, j, transformARGB(image.getRGB(i, j)));
			}
		}
		return new Tile(zoom, x, y, image);
	}

	public FilteringTileProvider(TileProvider tileProvider){
		this.tileProvider = tileProvider;
	}


	abstract public int transformARGB(int argb);
}
