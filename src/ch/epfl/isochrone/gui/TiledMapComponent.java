package ch.epfl.isochrone.gui;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.isochrone.tiledmap.TileProvider;
import ch.epfl.isochrone.geo.PointOSM;

public final class TiledMapComponent extends JComponent{

	private static final long serialVersionUID = 1L;
	private int zoom;
	private List<TileProvider> tP = new ArrayList<>();

	public TiledMapComponent(int zoom) throws IllegalArgumentException{
		if(zoom<10 || zoom>19){
			throw new IllegalArgumentException("Le niveau de zoom est invalide");
		}	
		this.zoom = zoom;
	}
	
	public void addTP(TileProvider tp){
	    tP.add(tp);
	}

	public int zoom(){
		return zoom;
	}
	
	public void setZoom(int newZoom){
		this.zoom = newZoom;
		repaint();
	}

	public Dimension getPrefferedSize(){
		return new Dimension(PointOSM.maxXY(zoom), PointOSM.maxXY(zoom));
	}

	public void paintComponent(Graphics g0){
		Graphics2D g = (Graphics2D)g0;
		Rectangle rect = getVisibleRect();
		for(TileProvider tP2 : tP){
			for (int x = rect.x/256; x <= (rect.x+rect.width)/256; x++) {
				for (int y = rect.y/256; y <= (rect.y+rect.height)/256; y++) {
					g.drawImage(tP2.tileAt(zoom, x, y).image(), null, 256*x, 256*y); 
				}
			}
		}
	}
	public void removeTileProvider(TileProvider tp){
	    tP.remove(tp);
	    repaint();
	}

    public void addTileProvider(TileProvider ttp) {
        tP.add(ttp);
        repaint();        
    }
 


}
