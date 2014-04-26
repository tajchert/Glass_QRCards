package pl.tajchert.glass_qrcards;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.esponce.webservice.QRCodeClient;
import com.google.android.glass.app.Card;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class CardListActivity extends Activity {

	private static final String LIVE_CARD_ID = "bussiness_card";
	private CardManager cardManager;
	private List<Card> mCards = new ArrayList<Card>();
	private CardScrollView mCardScrollView;
	private ExampleCardScrollAdapter adapter;
	
	//QRCode stuff
	private File pictureFile;
	private static final int TAKE_PICTURE_REQUEST = 1;
	private String path;
	private AppPref appPref;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//Cards start
		cardManager = new CardManager(this, mCards);
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.setOnItemClickListener(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
		//Cards end
		//takePicture() ;
		
		//TODO tmp
		//cardManager.createBussinessCard();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		appPref = new AppPref(this);
		cardManager.createScanCard();
		cardManager.createListCards(appPref.getScans());
		mCards = cardManager.getCards();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	

	private void takePicture() {
		//This is due to some problem (?) that user can approve photo only after 2-3 seconds, otherwise scanning would fail
		//Toast.makeText(getApplicationContext(), "Wait...", Toast.LENGTH_LONG).show();
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, TAKE_PICTURE_REQUEST);
	    mCards = new ArrayList<Card>();
		cardManager.setCards(mCards);
	    cardManager.createScanProgressCard();
	    adapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(Tools.TAG, "onActivityResult: " + resultCode);
	    if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
	        String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
	        processPicture(picturePath);
	    }else{
	    	//show too fast card
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}

	private void processPicture(final String picturePath) {
	    pictureFile = new File(picturePath);
	    path = picturePath;
	    if (pictureFile.exists()) {
	    	Log.d(Tools.TAG, "PICTURE READY");
	    	new GetQRCodeTask().execute();
	    }
	}
	

	
	//Picture to qrcode content via Esponce.com
	public class GetQRCodeTask extends AsyncTask<Void, Void, Boolean> {
		private String content;
		@Override
		protected Boolean doInBackground(Void... params) {
			QRCodeClient client = new QRCodeClient();
			//For scanning a file from /res folder
			//InputStream fileInputStream = getResources().openRawResource(R.drawable.qr_code_test_p);
			BitmapFactory.Options bfOptions=new BitmapFactory.Options();
            bfOptions.inDither=false;                     //Disable Dithering mode
            bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage=new byte[32 * 1024];
            
			Bitmap bMap = BitmapFactory.decodeFile(path ,bfOptions);
			bMap = Bitmap.createScaledBitmap(bMap, 1200, 900, false);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			bMap.compress(CompressFormat.PNG, 0, bos); 
			byte[] bitmapdata = bos.toByteArray();
			ByteArrayInputStream fileInputStream = new ByteArrayInputStream(bitmapdata);
			
	        BufferedInputStream bis = new BufferedInputStream(fileInputStream);
			try {
				content = client.decode(bis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		@Override
		protected void onPostExecute(final Boolean success) {
			if(content != null && appPref != null){
				Log.d(Tools.TAG, "Content: " + content);
				appPref.addScan(content);
			}
			mCards = new ArrayList<Card>();
			cardManager.setCards(mCards);
			cardManager.createScanCard();
			cardManager.createListCards(appPref.getScans());
			adapter.notifyDataSetChanged();
		}
	}
	
	private class ExampleCardScrollAdapter extends CardScrollAdapter implements OnItemClickListener{
		@Override
		public int getCount() {
			return mCards.size();
		}
		@Override
		public Object getItem(int position) {
			return mCards.get(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mCards.get(position).getView();
		}
		@Override
		public int getPosition(Object item) {
			return mCards.indexOf(item);
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.d(Tools.TAG, "CLICKED: " + position);
			Intent i = null;
			switch (position) {
				case 0:
					takePicture();
					break;
				case 1:
					break;
			}
			if(position != 0 && i != null){
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("dance", position);
				Log.d(Tools.TAG, "putExtra: "+ position);
				startService(i);
			}
		}
	}
}