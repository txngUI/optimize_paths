package util;

public class FlatEarthDist {

	private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;
    
    public static double distance(double ln1, double lt1, double ln2, double lt2) {
        double x = lt1 * d2r;
        double y = lt2 * d2r;
        return (int) (Math.acos( Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km / 1000);
    }
}