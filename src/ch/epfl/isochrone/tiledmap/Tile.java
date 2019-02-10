package ch.epfl.isochrone.tiledmap;

import java.awt.image.BufferedImage;

public class Tile{
	
	private int zoom;
	private int x;
	private int y;
	private BufferedImage image;
	
	public Tile(int zoom, int x, int y, BufferedImage image){
		this.zoom = zoom;
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	public int zoom(){
		return zoom;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public BufferedImage image(){
		return image;
	}

}
