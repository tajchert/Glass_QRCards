package pl.tajchert.glass_qrcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.google.android.glass.app.Card;

public class CardManager {
	
	private List<Card> mCards = new ArrayList<Card>();
	
	public void setCards(List<Card> mCards) {
		this.mCards = mCards;
	}


	private Context context;

	public CardManager(Context context, List<Card> cardList) {
		if(cardList == null || context == null){
			throw new IllegalArgumentException("CardList or context can't be null");
		}
		this.context = context;
		this.mCards = cardList;
	}
	public void createScanCard() {
		Card scanCard = new Card(context);
		scanCard.setText(context.getResources().getString(R.string.card_scan_code));
		scanCard.setFootnote("");
		if(mCards == null){
			mCards = new ArrayList<Card>();
		}
		mCards.add(scanCard);
	}
	public void createScanProgressCard() {
		Card scanCard = new Card(context);
		scanCard.setText(context.getResources().getString(R.string.card_scan_in_progress));
		scanCard.setFootnote("");
		if(mCards == null){
			mCards = new ArrayList<Card>();
		}
		mCards.add(scanCard);
	}
	
	public void createBussinessCard() {
		Card bussinessCard = new Card(context);
		//noPeopleCard.setText(context.getResources().getString(R.string.no_accurancy_card));
		bussinessCard.setFootnote("");
		bussinessCard.setImageLayout(Card.ImageLayout.FULL);
		bussinessCard.addImage(R.drawable.ic_launcher);
		
		if(mCards == null){
			mCards = new ArrayList<Card>();
		}
		mCards.add(bussinessCard);
	}
	

	public void createListCards(Set<String> scans ) {
		if(mCards == null){
			mCards = new ArrayList<Card>();
		}
		if(scans != null && scans.size() > 0 ){
			for (String scan : scans) {
				Card cardScan = new Card(context);
				String content = scan + "";
				cardScan.setText(content); // Main text area
				cardScan.setFootnote("");
				cardScan.setImageLayout(Card.ImageLayout.LEFT);
				// cardPerson.addImage(R.drawable.ic_launcher);
				// cardPerson.addImage(person.avatar);
				mCards.add(cardScan);
			}
		}
	}


	public List<Card> getCards() {
		return mCards;
	}

}
