/**
 * main class for storing run-time parameters
 */
package musiczones;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * @author Jason Zerbe
 */
public class MusicZones {
	protected static AssetManager myAssetManager = null;
	protected static Context myApplicationContext = null;
	protected static String myApplicationExternalRootStorageStr = null;
	protected static String myApplicationPackageNameStr = null;
	protected static boolean isDebugOn = false;
	protected static boolean isLowMem = false;
	protected static boolean isOnline = true;
	protected static boolean isIndexLocalHost = false;
	protected static int webInterfacePortInt = 2320;

	public static AssetManager getAssets() {
		return myAssetManager;
	}

	public static void setAssets(AssetManager theAssetManager) {
		myAssetManager = theAssetManager;
	}

	public static Context getApplicationContext() {
		return myApplicationContext;
	}

	public static void setApplicationContext(Context theContext) {
		myApplicationContext = theContext;
	}

	public static String getAppExternalStorageRoot() {
		return myApplicationExternalRootStorageStr;
	}

	public static void setAppExternalStorageRoot(
			String theAppExternalStorageRoot) {
		myApplicationExternalRootStorageStr = theAppExternalStorageRoot;
	}

	public static String getAppPackageName() {
		return myApplicationPackageNameStr;
	}

	public static void setAppPackageName(String theAppPackageName) {
		myApplicationPackageNameStr = theAppPackageName;
	}

	public static boolean getIsDebugOn() {
		return isDebugOn;
	}

	public static void setIsDebugOn(boolean theDebugIsOn) {
		isDebugOn = theDebugIsOn;
	}

	public static boolean getIsLowMem() {
		return isLowMem;
	}

	public static void setIsLowMem(boolean theIsLowMemOn) {
		isLowMem = theIsLowMemOn;
	}

	public static boolean getIsOnline() {
		return isOnline;
	}

	public static void setIsOnline(boolean theIsOnlineFlag) {
		isOnline = theIsOnlineFlag;
	}

	public static boolean getIsIndexLocalHost() {
		return isIndexLocalHost;
	}

	public static void setIndexLocalHost(boolean theIndexLocalHostFlag) {
		isIndexLocalHost = theIndexLocalHostFlag;
	}

	public static int getWebInterfacePort() {
		return webInterfacePortInt;
	}

	public static void setWebInterfacePort(int theWebInterfacePortInt) {
		webInterfacePortInt = theWebInterfacePortInt;
	}
}
