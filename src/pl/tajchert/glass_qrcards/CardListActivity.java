package pl.tajchert.glass_qrcards;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.esponce.webservice.QRCodeClient;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class CardListActivity extends Activity {

	private static final String LIVE_CARD_ID = "bussiness_card";
	private CardManager cardManager;
	private List<Card> mCards = new ArrayList<Card>();
	private CardScrollView mCardScrollView;
	private ExampleCardScrollAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		cardManager = new CardManager(this, mCards);
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
		
		
		cardManager.createBussinessCard();
		mCards = cardManager.getCards();
		adapter.notifyDataSetChanged();
		//TimelineManager tm = TimelineManager.from(PeopleListActivity.this);
	}

	@Override
	protected void onResume() {
		
		new GetQRCodeTask().execute();
		
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
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
			InputStream is = getResources().openRawResource(R.drawable.qr_code_test);
	        BufferedInputStream bis = new BufferedInputStream(is);
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
