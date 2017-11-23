package game;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card{
    public final static int IMG_WIDTH = 50;
    public static final int IMG_HEIGHT = 70;
    private Rank rank;
    private Suite suite;
    
    
    public Card(int rank, int suite){
        if (rank == 666) {
            rank = 12;
        }
        this.rank = Rank.values()[rank - 2];
        this.suite = Suite.values()[suite];
    }
    
    public static JLabel DrawCardBack(Position position, WitchFrame panel, Object ListenerClass) {
        return panel.drawClickableImage(ImageResources.getSubimageAsIconFromDefault(13 * Card.IMG_WIDTH, 2 * Card.IMG_HEIGHT, Card.IMG_WIDTH, Card.IMG_HEIGHT), position, IMG_WIDTH, IMG_HEIGHT, ListenerClass);
    }
    
    @Override
    public String toString(){
        return rank.toString()+suite.toString();
    }
    
    public Suite getSuite(){
        return suite;
    }
    
    public Rank getRank(){
        return rank;
    }
	
	public void draw(Position position, WitchFrame panel, int isHidden)
	{
	    if (isHidden == 0) {
	        panel.drawImage(getImageFromSet(), position, IMG_WIDTH, IMG_HEIGHT);
	    } else {
	        panel.drawImage(ImageResources.getSubimageAsIconFromDefault(13 * Card.IMG_WIDTH, 2 * Card.IMG_HEIGHT, Card.IMG_WIDTH, Card.IMG_HEIGHT), position, IMG_WIDTH, IMG_HEIGHT);
	    }
	}
	
	public ImageIcon getImageFromSet(){
	    int Mask[] = {2, 1, 3, 0};   // Suite Enumerator changed :)
		int iX = (12 - this.getRank().ordinal()) * IMG_WIDTH;
		int iY = Mask[this.getSuite().ordinal()] * IMG_HEIGHT;
		return ImageResources.getSubimageAsIconFromDefault(iX, iY, IMG_WIDTH, IMG_HEIGHT);
	}
}
