package front.util;

public class Util {

	private Util() {}
	
	public static <T extends Comparable<T>> T clamp(T min, T val, T max) {
		return (val.compareTo(min) < 0) ? min : (val.compareTo(max) > 0) ? max : val;
	}
	
	private static String getOSProperty() {
		return System.getProperty("os.name");
	}
	
	public static boolean isWindows() {
        return (getOSProperty().indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (getOSProperty().indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (getOSProperty().indexOf("nix") >= 0 || getOSProperty().indexOf("nux") >= 0 || getOSProperty().indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (getOSProperty().indexOf("sunos") >= 0);
    }
    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }
	
}
