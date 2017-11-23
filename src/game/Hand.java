package game;

import java.util.LinkedList;

/**
 * @author curious
 *
 */
public class Hand {
    
    public static final int HAND_WIDTH_PIXELS = 300; 
    private final int HAND_DUPLICATES_OFFSET = Card.IMG_HEIGHT + Card.IMG_HEIGHT / 2;

    private LinkedList<Card> CardsInHand; 

    public Hand() {
        CardsInHand = new LinkedList<Card>();
    }

    public String toString(){
        return CardsInHand.toString();
    }

    public void addCard(Card card){
        CardsInHand.add(card);
    }
    
    /**
     * 
     * @param ID
     * @param PlayerCount
     * @param isDuplicates
     * @param panel
     * @return - 0 if didn't displayed, 1 if displayed
     */
    public int draw(int ID, int PlayerCount, int isDuplicates, int isHidden, WitchFrame panel){
        //TODO: Draw all hand cards at once, not one by one; - see disable auto redraw
    	double dX = 0;
        double dY = 0;
        double dupX = 0;
        double dupY = 0;
        
        int CardCount = CardsInHand.size();
        
        double CardX = 0, CardY = 0;
        
        Position CurrPos = null;
        
        DEBUG.WRITE("Begin card calculations:\nID " + ID + "\nPC " + PlayerCount + "\nisDup " + isDuplicates + "\nCardCount " + CardCount, DEBUG.DebugTypes.LOG_DEBUG);
       
        if (CardCount == 0) {
            return 0;
        }
        
        if (ID == 1){
          	dX = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_WIDTH);
        	CardX = (WitchGame.MAIN_WINDOW_WIDTH / 2.0) - ((dX * CardCount) / 2.0);
        	CardY = WitchGame.MAIN_WINDOW_HEIGHT - (Card.IMG_HEIGHT + Card.IMG_HEIGHT / 2.0);
        	dupY = -(HAND_DUPLICATES_OFFSET * isDuplicates);
        }
        else
        {
	    	switch (PlayerCount){
		    	case 2 :
		          	dX = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_WIDTH);
		        	CardX = (WitchGame.MAIN_WINDOW_WIDTH / 2.0) - ((dX * CardCount) / 2.0);
		        	CardY = (Card.IMG_HEIGHT + Card.IMG_HEIGHT / 2.0);
		          	dupY = HAND_DUPLICATES_OFFSET * isDuplicates;
				break;
		    	case 3 :
		    		switch (ID){
			    		case 2 :
			    			CardX = (Card.IMG_WIDTH + Card.IMG_WIDTH / 2.0);
			    			dY = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_HEIGHT);
			    			CardY = (WitchGame.MAIN_WINDOW_HEIGHT / 2.0) - ((dY * CardCount) / 2.0);
			    			dupX = HAND_DUPLICATES_OFFSET * isDuplicates;
			    		break;
			    		case 3 :
			    			CardX = WitchGame.MAIN_WINDOW_WIDTH - (Card.IMG_WIDTH / 2.0); //!!!
			    			dY = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_HEIGHT);
			    			CardY = (WitchGame.MAIN_WINDOW_HEIGHT / 2.0) - ((dY * CardCount) / 2.0);
			    			dupX = -(HAND_DUPLICATES_OFFSET * isDuplicates);
			    		break;
		    		}
				break;
		    	case 4 :
		    		switch (ID){
			    		case 2 :
			    			CardX = (Card.IMG_WIDTH + Card.IMG_WIDTH / 2.0);
			    			dY = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_HEIGHT);
			    			CardY = (WitchGame.MAIN_WINDOW_HEIGHT / 2.0) - ((dY * CardCount) / 2.0);
			    			dupX = HAND_DUPLICATES_OFFSET * isDuplicates;
			    		break;
				    	case 3 :
				          	dX = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_WIDTH);
				        	CardX = (WitchGame.MAIN_WINDOW_WIDTH / 2.0) - ((dX * CardCount) / 2.0);
				        	CardY = (Card.IMG_HEIGHT + Card.IMG_HEIGHT / 2.0);
				        	dupY = HAND_DUPLICATES_OFFSET * isDuplicates;
						break;
			    		case 4 :
			    			CardX = WitchGame.MAIN_WINDOW_WIDTH - (Card.IMG_WIDTH / 2.0); //!!!
			    			dY = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_HEIGHT);
			    			CardY = (WitchGame.MAIN_WINDOW_HEIGHT / 2.0) - ((dY * CardCount) / 2.0);
			    			dupX = -(HAND_DUPLICATES_OFFSET * isDuplicates);
			    		break;
		    		}
				break;
		    	case 5 :
		    		switch (ID){
                        case 2 :
                            CardX = (Card.IMG_WIDTH + Card.IMG_WIDTH / 2.0);
                            dY = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_HEIGHT);
                            CardY = (WitchGame.MAIN_WINDOW_HEIGHT / 2.0) - ((dY * CardCount) / 2.0);
                            dupX = HAND_DUPLICATES_OFFSET * isDuplicates;
                        break;
                        
                        case 3 :
                            dX = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_WIDTH);
                            CardX = (WitchGame.MAIN_WINDOW_WIDTH / 3.0) - ((dX * CardCount) / 2.0) - 20;
                            CardY = (Card.IMG_HEIGHT + Card.IMG_HEIGHT / 2);
                            dupY = HAND_DUPLICATES_OFFSET * isDuplicates;
                        break;
                        
                        case 4 :
                            dX = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_WIDTH);
                            CardX = (WitchGame.MAIN_WINDOW_WIDTH * 2/ 3.0) - ((dX * CardCount) / 2.0) + 20;
                            CardY = (Card.IMG_HEIGHT + Card.IMG_HEIGHT / 2.0);
                            dupY = HAND_DUPLICATES_OFFSET * isDuplicates;
                        break;
                        
                        case 5 :
                            CardX = WitchGame.MAIN_WINDOW_WIDTH - (Card.IMG_WIDTH / 2.0); //!!!
                            dY = Math.min(HAND_WIDTH_PIXELS / CardCount, Card.IMG_HEIGHT);
                            CardY = (WitchGame.MAIN_WINDOW_HEIGHT / 2.0) - ((dY * CardCount) / 2.0);
                            dupX = -(HAND_DUPLICATES_OFFSET * isDuplicates);
                        break;
		    		}
	    		break;
	    	}
        }
    	
    	DEBUG.WRITE("Calculated: X "+ CardX + " Y " + CardY + " dX " + dX + " dY " + dY + " dupX " + dupX + " dupY " + dupY, DEBUG.DebugTypes.LOG_DEBUG);

    	for (int i = 0; i < CardCount; i++){
        	CurrPos = new Position((int)(CardX + dX*(CardCount - i) + dupX) - Card.IMG_WIDTH, (int)(CardY + dY*(CardCount - i) + dupY) - Card.IMG_HEIGHT);
            CardsInHand.get((CardCount - 1 - i)).draw(CurrPos, panel, isHidden);
        }
    	return 1;
    }
}
