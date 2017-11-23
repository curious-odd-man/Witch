package game;

public enum Suite {
	DIAMONDS, HEARTS, CLUBS, SPADES;
	
	@Override
	public String toString(){
		switch (this) {
		case DIAMONDS:
			return "Diamonds";
		case CLUBS:
			return "Clubs";
		case SPADES:
			return "Spades";
		case HEARTS:
			return "Hearts";
		default:
			break;
		}
		return "Unknown suite";
	}
}
