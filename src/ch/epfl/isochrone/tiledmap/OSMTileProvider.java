package ch.epfl.isochrone.tiledmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class OSMTileProvider implements TileProvider {

	private URL baseAddress;
	private BufferedImage errorTile;
	
	public OSMTileProvider(URL baseAddress) throws IOException{
		this.baseAddress = baseAddress;
		errorTile = ImageIO.read(getClass().getResource("/images/error-tile.png"));
	}

	
	@Override
	public Tile tileAt(int zoom, int x, int y) {
        BufferedImage image;
		try{
			image = ImageIO.read(new URL(baseAddress + "/" + zoom + "/" + x + "/" + y));
		}
		catch(Exception e){
			image = errorTile;
		}
		return new Tile(zoom, x, y, image);
	}

	
}
