package pl.tajchert.glass_qrcards;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {
	private Set<String> scans;
	private SharedPreferences prefs;
	private static final String SCAN_SET = "pl.tajchert.qrcodes.scan.set";

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
		scans = null;
		scans = new HashSet<String>(prefs.getStringSet(SCAN_SET, new HashSet<String>()));
	}

	private void setSharedSet(Set<String> in) {
		prefs.edit().putStringSet(SCAN_SET, in).commit();
	}
}
