package pl.tajchert.glass_qrcards;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.glass.app.Card;

public class CardManager {
	
	private List<Card> mCards = new ArrayList<Card>();
	private Context context;

	public CardManager(Context context, List<Card> cardList) {
		if(cardList == null || context == null){
			throw new IllegalArgumentException("CardList or context can't be null");
		}
		this.context = context;
		this.mCards = cardList;
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
	

	/*public void createListCards() {
		mCards = new ArrayList<Card>();
		
		if(Tools.contactList != null && Tools.contactList.size() > 0 ){
			for (Person person: Tools.contactList) {
						Card cardPerson = new Card(context);
						String content = person.name + "";
						cardPerson.setText(content); // Main text area
						
						cardPerson.setFootnote(person.phoneNum +"");;
						cardPerson.setImageLayout(Card.ImageLayout.LEFT);
						//cardPerson.addImage(R.drawable.ic_launcher);
						cardPerson.addImage(person.avatar);
						mCards.add(cardPerson);
					
			}
			addEndCard();
		}else if(Tools.contactList.size() <= 0){
			createNoPeopleCard();
		}else{
			addDownloadingCard();
		}
	}*/


	public List<Card> getCards() {
		return mCards;
	}

}
