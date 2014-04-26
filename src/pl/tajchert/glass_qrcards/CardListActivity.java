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
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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
	private File pictureFile;
	private static final int TAKE_PICTURE_REQUEST = 1;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//Cards start
		cardManager = new CardManager(this, mCards);
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
		//Cards end
		
		takePicture();
		//TODO tmp
		cardManager.createBussinessCard();
		mCards = cardManager.getCards();
		adapter.notifyDataSetChanged();
		//TimelineManager tm = TimelineManager.from(PeopleListActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	

	private void takePicture() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, TAKE_PICTURE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
	        String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
	        processPicture(picturePath);
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
	

	private class ExampleCardScrollAdapter extends CardScrollAdapter {
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
	}
	
	public class GetQRCodeTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			QRCodeClient client = new QRCodeClient();
			//For scanning a file from /res folder
			//InputStream fileInputStream = getResources().openRawResource(R.drawable.qr_code_test_p);
			Bitmap bMap = BitmapFactory.decodeFile(path);
			bMap = Bitmap.createScaledBitmap(bMap, 1200, 900, false);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			bMap.compress(CompressFormat.JPEG, 75, bos); 
			byte[] bitmapdata = bos.toByteArray();
			ByteArrayInputStream fileInputStream = new ByteArrayInputStream(bitmapdata);
			
	        BufferedInputStream bis = new BufferedInputStream(fileInputStream);
	        String content = null;
			try {
				content = client.decode(bis);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        Log.d(Tools.TAG, "Content: " + content);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
		}

		@Override
		protected void onCancelled() {
		}
	}
	
}
