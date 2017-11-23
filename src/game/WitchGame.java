package game;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.armedbear.lisp.Cons;

import game.ImageResources;

public class WitchGame implements ActionListener, MouseListener{
	// MAIN WINDOW CONSTANTS
	public static final int MAIN_WINDOW_WIDTH     = 1024;
	public static final int MAIN_WINDOW_HEIGHT    = 768;
	private static final String MAIN_WINDOW_LABLE = "Witch game";
	private static final int BUTTON_X             = 50;
	private static final int BUTTON_Y             = MAIN_WINDOW_HEIGHT - 100;
	private static final int BUTTON_HEIGTH        = 50;
	private static final int BUTTON_WIDTH         = 150;
	private static final String BUTTON_TEXTS[]    = {"OK"};     // append button texts here and increase count at  BUTTON_TEXTS_COUNT
	private static final String AI_NAMES[]        = {"GLaDOS", "EDI", "Legion", "Edge"};
	private static final int BUTTON_TEXTS_COUNT   = 1;
	private static final int HIGHLIGHT_FRAME_X    = 0;
	private static final int HIGHLIGHT_FRAME_Y    = 303;
	private static final int HIGHLIGHT_FRAME_WIDTH     = 60;
	private static final int HIGHLIGHT_FRAME_HEIGHT    = 80;
	private static final int PASSED_CARD_X             = MAIN_WINDOW_WIDTH / 2;
	private static final int PASSED_CARD_Y             = MAIN_WINDOW_HEIGHT / 2;
	    
	// variables
	private static WitchFrame MainFrame;
	private static Hand UniversalHand;
	private static boolean ButtonPressed = false; 
	private static boolean MouseClicked = false;
	private static Component MouseEventSource = null;
	
	//hacks
	public static int AI = 0;

	public WitchGame() {
		
		MainFrame = new WitchFrame(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);
		MainFrame.setTitle(MAIN_WINDOW_LABLE);
		
		MainFrame.show();

	}
	
	/** Method will create button and wait until user presses it
	 * @param ButtonTextID - Text ID from BUTTON_TEXTS[]
	 */
	public void waitButtonPress(int ButtonTextID){
	    DEBUG.WRITE("Waiting user to press a button", DEBUG.DebugTypes.LOG_DEBUG);
	    ButtonPressed = false;
	    JButton Btn = new JButton();
	    Btn.setVisible(true);
	    
	    if (ButtonTextID < BUTTON_TEXTS_COUNT) {
	        Btn.setText(BUTTON_TEXTS[ButtonTextID]);
	    } else {
	        Btn.setText("ERROR");
	    }
	    Btn.setEnabled(true);
	    Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    Btn.addActionListener(this);
	    MainFrame.addButton(Btn, new Position(BUTTON_X, BUTTON_Y), BUTTON_WIDTH, BUTTON_HEIGTH);
	    while (ButtonPressed == false) {
	        try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                DEBUG.WRITE("Wait interrupted " + e.toString(), DEBUG.DebugTypes.LOG_SYSTEM);
            }
	    }
	    ButtonPressed = false;
	    DEBUG.WRITE("Got user button press", DEBUG.DebugTypes.LOG_DEBUG);
	    MainFrame.removeComponent(Btn);
	    Btn = null;
	}
	
	/** method will draw PlayerTable on screen
	 * 
	 */
	public static void endPlayerTable() {
	    //DEBUG.WRITE("PlayerTable = " + PlayerTable, DEBUG.DebugTypes.LOG_ERROR);
	    //PlayerTable.clear();
	     MainFrame.clearScoreBoard();
	}
	
	/**    Appends new Entry to PlayerTable
	 * 
	 * @param Entry - new entry
	 */
	public static void addPlayerEntry(String Entry) {
	    //PlayerTable.add(Entry);
	    MainFrame.addPlayerToScoreBoard(Entry);
	}
	
	/** Method will wait until user clicks a mouse and then sets MouseClicked to false;
	 * 
	 */
	public void waitKeyClick(){
	    MouseClicked = false;
	    while (MouseClicked == false) {
	        try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            DEBUG.WRITE("Wait interrupted " + e.toString(), DEBUG.DebugTypes.LOG_SYSTEM);
	        }
	    }
	    MouseClicked = false;
	}
	
	/** Method will create Hand instance.
	 * 
	 */
	public static void CreateHand (){
		DEBUG.WRITE("Create", DEBUG.DebugTypes.LOG_DEBUG);
	    UniversalHand = null;
	    UniversalHand = new Hand();
	}
	
	/** Method will add card to created Hand instance
	 * 
	 * @param Card - list from list in form of (CardRank  CardSuite)
	 */
	public static void ReceiveCardFromLISP(Cons Card) {
		DEBUG.WRITE("Receive (" + Card.car().intValue() + " " + Card.cdr().car().intValue() + ")" , DEBUG.DebugTypes.LOG_DEBUG);
	    UniversalHand.addCard(new Card(Card.car().intValue(), Card.cdr().car().intValue()));
	}
	
	/** Method will display all Cards in Hand. Position is calculated depending on input params
	 * 
	 * @param PlayerID     - player number
	 * @param PlayerCount  - total player count
	 * @param isDuplicates - 1 if Hand is list of duplicates, else 0
	 * @return Value returned by draw();
	 */
	public static int DrawHand(int PlayerID, int PlayerCount, int isDuplicates, int isHidden) {
		DEBUG.WRITE("Draw", DEBUG.DebugTypes.LOG_DEBUG);
	    return UniversalHand.draw(PlayerID, PlayerCount, isDuplicates, isHidden, MainFrame);
	}
	
	/** This method will erase all card images from desk
	 * 
	 */
	public void removeAllCardsFromDesk() {
	    MainFrame.clearImages();
	}
	
	/** Method will ask user to enter number
	 * @param      Text to be displayed to user
	 * @param      InitialValue - default value of user choice 
	 * @return     Number Entered by player
	 */
	public static int askNumber(String Text, int InitialValue) {
		
		int 	Output = 0;
		boolean isCorrect = false;
	    
	    do{
	    	try{
	    		Output = Integer.parseInt(JOptionPane.showInputDialog(Text, InitialValue));
	    		isCorrect = true;
	    	}
	    	catch(NumberFormatException Ex){
	    		isCorrect = false;
	    	}
	    }while(!isCorrect);
	        
        return Output;
	}
	
	/** Method will ask user to enter player name; 
	 * if Current player is AI - default names are proposed
	 * 
	 * @param Text    - question text
 	 * @param HumanOrGLaDOS - 1 if player, 0 if AI
	 * @return  - String: name of a player
	 */
	public static String askText(String Text, Cons HumanOrGLaDOS) {
	    String Output = null;

	    String InitialValue = null;
	    
	    if (HumanOrGLaDOS.car().intValue() == 1) {
	        InitialValue = new String("Player Name");
	    } else {
	        InitialValue = AI_NAMES[AI++];
	    }
	    Output = JOptionPane.showInputDialog(Text, InitialValue);

	    return Output;
	}
	
	
	/**Method will ask user to pick a card from a set
	 * 
	 * @param TotalCards - Cards number to pick from
	 * @return index of a picked card
	 */
	public int getUserSelectedCard(int CardCount) {
	    Position CurrPos;
	    JLabel[] ImagesAdded = new JLabel[CardCount];
	    double dX = Math.min(Hand.HAND_WIDTH_PIXELS / CardCount, Card.IMG_WIDTH);
        double CardX = (WitchGame.MAIN_WINDOW_WIDTH / 2.0) - ((dX * CardCount) / 2.0);
        double CardY = WitchGame.MAIN_WINDOW_HEIGHT / 2.0;
        int ClickedCard = -1;
        
	    
	    for (int i = 0; i < CardCount; i++){
	        CurrPos = new Position((int)(CardX + dX*(CardCount - i)) - Card.IMG_WIDTH, (int)(CardY) - Card.IMG_HEIGHT);
	        ImagesAdded[i] = Card.DrawCardBack(CurrPos, MainFrame, this);
	    }
	    
	    waitKeyClick();
	    
	    // remove images and find source
	    for (int i = 0; i < CardCount; i++){
	        if (ImagesAdded[i].equals(MouseEventSource)) {
	            ClickedCard = i;
	        }
	        MainFrame.removeComponent(ImagesAdded[i]);
	        ImagesAdded[i] = null;
	    }
	    
	    DEBUG.WRITE("Found source at " + ClickedCard, DEBUG.DebugTypes.LOG_DEBUG);
	    MouseEventSource = null;
	    // numbering starts from 0
        return ClickedCard;
	}
	
	/** Method will set highlight On or Off for Component
	 * 
	 * @param On           - switch on or off
	 * @param Comp         - component to act on
	 */
	public void highlightCardImage(boolean On, Component Comp) {  
	    if (On) {
	        JLabel SelectionFrame = new JLabel(ImageResources.getSubimageAsIconFromDefault(HIGHLIGHT_FRAME_X, HIGHLIGHT_FRAME_Y, HIGHLIGHT_FRAME_WIDTH, HIGHLIGHT_FRAME_HEIGHT));
	        SelectionFrame.setVisible(false);
	        MainFrame.addLabel(SelectionFrame, new Position(Comp.getX() - 5, Comp.getY() - 5), HIGHLIGHT_FRAME_WIDTH, HIGHLIGHT_FRAME_HEIGHT);
	        DEBUG.WRITE("highlight: " + On + " Coord " + (Comp.getX() - 5) + ":"+ (Comp.getY() - 5) , DEBUG.DebugTypes.LOG_DEBUG);
	        SelectionFrame.setVisible(true);
	    } else {
	        MainFrame.removeComponentXY(Comp.getX() - 5, Comp.getY() - 5);
	    }
	}
	
	/**Method will draw single card (that is passed) on constant coordinates 
	 * 
	 * @param Rank   - card rank
	 * @param Suite  - card suite
	 */
	public void drawPassedCard(int Rank, int Suite) {
	    Card TheCard = new Card(Rank, Suite);
	    Position Pos = new Position(PASSED_CARD_X, PASSED_CARD_Y);
	    TheCard.draw(Pos, MainFrame, 0);
	}
	
	/**    Method will show the looser name and his score 
	 * 
	 * @param LooserName
	 * @param LooserScore
	 */
	public static void pronounceLooser(String Text) {
	    DEBUG.WRITE("LooserName: " + Text , DEBUG.DebugTypes.LOG_ERROR);
	    JOptionPane.showMessageDialog(null, Text);
	}
	
	/** Catches the action and sets ButtonPressed to true;
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		Object Source = arg0.getSource();
		DEBUG.WRITE("Got user button press on:" + Source.toString(), DEBUG.DebugTypes.LOG_DEBUG);
		ButtonPressed = true;
	}

    @Override
    public void mouseClicked(MouseEvent arg0) {
        DEBUG.WRITE("Mouse Clicked = " + arg0.toString(), DEBUG.DebugTypes.LOG_DEBUG);
        MouseEventSource = arg0.getComponent();
        highlightCardImage(false, MouseEventSource);
        MouseClicked = true;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        //DEBUG.WRITE("Mouse entered = " + arg0.toString(), DEBUG.DebugTypes.LOG_DEBUG);
        highlightCardImage(true, arg0.getComponent());
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        //DEBUG.WRITE("Mouse exited = " + arg0.toString(), DEBUG.DebugTypes.LOG_DEBUG);
        highlightCardImage(false, arg0.getComponent());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
}
