package ch.epfl.isochrone.tiledmap;
import ch.epfl.isochrone.math.Math;

public class TransparentTileProvider extends FilteringTileProvider{

	private double opacity;

	public TransparentTileProvider(TileProvider tileProvider, double opacity) throws IllegalArgumentException{
		super(tileProvider);
		if(opacity<0 || opacity >1){
			throw new IllegalArgumentException("l'opacité est invalide");
		}
		this.opacity = opacity;
	}


	@Override
	public int transformARGB(int argb) {
		return argb - 1/255*Math.modF(Math.divF(argb, (int) java.lang.Math.pow(2, 24)), (int) java.lang.Math.pow(2, 8))
				+ (int) java.lang.Math.round(opacity*255)*(int) java.lang.Math.pow(2, 24);
	}
}