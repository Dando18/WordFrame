package front.util;

public class Util {

	private Util() {}
	
	public static <T extends Comparable<T>> T clamp(T min, T val, T max) {
		return (val.compareTo(min) < 0) ? min : (val.compareTo(max) > 0) ? max : val;
	}

}
