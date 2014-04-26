package pl.tajchert.glass_qrcards;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppPref {
	private Set<String> scans;
	private SharedPreferences prefs;
	private static final String SCAN_SET = "pl.tajchert.qrcodes.scanset";

	/**
	 * @param prefs
	 */
	public AppPref(Context context) {
		super();
		this.prefs = context.getSharedPreferences("pl.tajchert.glass_qrcards", Context.MODE_PRIVATE);
	}
	
	public Set<String> getScans(){
		getSharedSet();
		return scans;
	}
	
	public void addScan(String content){
		getSharedSet();
		scans.add(content);
		setSharedSet(scans);
	}
	
	private void getSharedSet() {
		scans = new HashSet<String>(prefs.getStringSet(SCAN_SET, new HashSet<String>()));
		/*scans = prefs.getStringSet(SCAN_SET, null);
		if(scans == null){
			scans = new HashSet<String>();
		}*/
		Log.d(Tools.TAG, "scans.size: " + scans.size());
	}

	private void setSharedSet(Set<String> in) {
		Log.d(Tools.TAG, "SAVE scans.size: " + scans.size());
		prefs.edit().putStringSet(SCAN_SET, in).commit();
	}
	
	

}
