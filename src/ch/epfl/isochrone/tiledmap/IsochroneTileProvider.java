package ch.epfl.isochrone.tiledmap;
import ch.epfl.isochrone.timetable.FastestPathTree;


import ch.epfl.isochrone.timetable.Stop;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Set;

public final class IsochroneTileProvider implements TileProvider{

	private FastestPathTree fpt;
	private ColorTable colorTable;
	private double walkingSpeed;
	private TileProvider tileProvider;
	private Set<Stop> stops;

	public IsochroneTileProvider(FastestPathTree fpt, ColorTable colorTable, double walkingSpeed) throws IllegalArgumentException{
		if(walkingSpeed<0){
			throw new IllegalArgumentException("La vitesse de marche est invalide");
		}
		this.fpt = fpt;
		this.colorTable = colorTable;
		this.walkingSpeed = walkingSpeed;

	}

	@Override
	public Tile tileAt(int zoom, int x, int y) {
		Tile tile = tileProvider.tileAt(zoom, x, y);
		BufferedImage image = tile.image();
		for(Stop s1 : stops){
			int T = colorTable.duration() - (fpt.arrivalTime(s1) - fpt.startingTime());
			if(T>0){
				double R = walkingSpeed*T;
				Graphics2D g = image.createGraphics();
				g.setColor(colorTable.intervalColor(T));
				g.fill(new Ellipse2D.Double(s1.position().longitude(), s1.position().latitude(), R, R));
			}
		}
		return new Tile(zoom, x, y, image);
	}
}
