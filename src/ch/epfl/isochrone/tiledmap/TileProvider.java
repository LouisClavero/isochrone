package ch.epfl.isochrone.tiledmap;

public interface TileProvider {
	public Tile tileAt(int zoom, int x, int y);
}
