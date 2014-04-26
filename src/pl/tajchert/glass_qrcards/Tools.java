package pl.tajchert.glass_qrcards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class Tools {
	public static final String TAG = "QR_CARDS"; 
	public static final String SEPARATOR = ";-;-;";
	
	public static boolean isNetworkAvailable(Context context) {
		HttpGet httpGet = new HttpGet("http://www.google.com");
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		try {
			httpClient.execute(httpGet);
			return true;
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	public static Location getLastLocation(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		List<String> providers = manager.getProviders(criteria, true);
		List<Location> locations = new ArrayList<Location>();
		for (String provider : providers) {
			Location location = manager.getLastKnownLocation(provider);
			if (location != null && location.getAccuracy() != 0.0) {
				locations.add(location);
			}
		}
		Collections.sort(locations, new Comparator<Location>() {
			@Override
			public int compare(Location location, Location location2) {
				return (int) (location.getAccuracy() - location2.getAccuracy());
			}
		});
		if (locations.size() > 0) {
			return locations.get(0);
		}
		return null;
	}

}
