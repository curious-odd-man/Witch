package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;


public class WitchFrame {
	private JFrame frame;
	private JPanel contentPane;
	private JLayeredPane Lpane;
	private ImageIcon img = null;
	private ArrayList<Object> buttons = new ArrayList<>();
	private JTextPane ScoreBoard; 
	private StyledDocument ScoreBoardDoc;
	
	public WitchFrame(){
		this(0, 0);
	}
	
	public WitchFrame(int width, int height){
	    Dimension Dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    int x = (Dim.width - width)/2;
	    int y = (Dim.height - height)/2;

		frame = new JFrame();
		
		this.setSize(width, height);
		
		frame.setLocation(x, y);
		frame.setResizable(false);
		frame.setLocation(x, y);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Lpane = new JLayeredPane();
		Lpane.setBounds(0, 0, 1023, 767);
		
		frame.add(Lpane, BorderLayout.CENTER);
		
		JLabel lbl = null;
		
		//BG Image
        try {
            img = game.ImageResources.getSubimageAsIcon("Card (" + ((int)Math.round(Math.random() * 4) + 1) + ").jpg", 0, 0, 1023, 767);
            DEBUG.WRITE("img = " + img.toString(), DEBUG.DebugTypes.LOG_DEBUG);
            lbl = new JLabel(img);
            lbl.setBounds(0, 0, 1023, 767);
           
        } catch (IllegalArgumentException e1) {
            DEBUG.WRITE("arg " + e1.toString(), DEBUG.DebugTypes.LOG_ERROR);
        }

        //Game panel
        contentPane = new JPanel();
        contentPane.setBounds(0, 0, 1023, 767);
		contentPane.setOpaque(false);
        contentPane.setLayout(null);
        
        
        ScoreBoard = new JTextPane();
        ScoreBoard.setBounds(1024 - 310, 768 - 185, 300, 150);
        ScoreBoard.setOpaque(true);
        ScoreBoard.setLayout(null);
        ScoreBoard.setBackground(Color.WHITE);
        ScoreBoardDoc = ScoreBoard.getStyledDocument();
        
        
        Lpane.add(lbl, new Integer(0), 0);
        Lpane.add(contentPane, new Integer(1), 0);
        Lpane.add(ScoreBoard, new Integer(2), 0);
	}
	
	public void addPlayerToScoreBoard(String Player){
	    try {
            ScoreBoardDoc.insertString(ScoreBoardDoc.getLength(), Player + "\n", null);
        } catch (BadLocationException e) {
            DEBUG.WRITE("ERROR ADDING STRING :" + Player, DEBUG.DebugTypes.LOG_ERROR);
        }
	}
	
	public void clearScoreBoard() {
	    try {
            ScoreBoardDoc.remove(0, ScoreBoardDoc.getLength());
        } catch (BadLocationException e) {
            DEBUG.WRITE("ERROR CLEARING" , DEBUG.DebugTypes.LOG_ERROR);
        }
	}
	
	public void removeComponent(Component Comp) {
	    contentPane.remove(Comp);
	}
	
	public void removeComponentXY(int x, int y) {
	    Component compAtGivenPoint = contentPane.getComponentAt(x, y);
        if(compAtGivenPoint != null){
            contentPane.remove(compAtGivenPoint);
        }
	}
	
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	public void setSize(int width, int height){
		frame.setSize(width, height);
	}
	
	public void show(){
		Redrawer redrawer = new Redrawer(this.frame);
		redrawer.start();
		frame.setVisible(true);
	}
	
	public void clearImages(){
		Component[] components = contentPane.getComponents();
		for(Component comp : components){
			if(comp instanceof JLabel){
				JLabel lbl = (JLabel) comp;
				if( lbl.getText() == null){
					contentPane.remove(comp);
				}
			}
		}
	}
	
	public JLabel drawImage(ImageIcon image, Position position, int width, int height){
		JLabel lbl = new JLabel(image);
		addComponent(lbl, position, width, height);
		return lbl;
	}
	
	public JLabel drawClickableImage(ImageIcon image, Position position, int width, int height, Object ListenerClass){
	    JLabel lbl = new JLabel(image);
	    lbl.addMouseListener((MouseListener) ListenerClass);
	    lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    addComponent(lbl, position, width, height);
	    return lbl;
	}
	
	public void addConsole(JTextPane Console, Position position, int width, int height){
	    addComponent(Console, position, width, height);
	}
	
	public void addButton(JButton btn, Position position, int width, int height){
		buttons.add(btn);
		addComponent(btn, position, width, height);
	}
	
	public void addLabel(JLabel lbl, Position position, int width, int height){
		addComponent(lbl, position, width, height);
	}
	
	private void addComponent(Component comp, Position pos, int width, int height){
		Component compAtGivenPoint = contentPane.getComponentAt(pos.getX(), pos.getY());
		if(compAtGivenPoint != null){
			contentPane.remove(compAtGivenPoint);
		}
		comp.setBounds(pos.getX(), pos.getY(), width, height);
		contentPane.add(comp);
	}
	
	public JFrame getFrame() {
        return frame;
	}
	
	class Redrawer extends Thread{
		private JFrame frame = null;
		public Redrawer(JFrame frame){
			this.frame = frame;
		}
		
		public void run(){
			while(true){
				try{
					Thread.sleep(100);
					this.frame.revalidate();
					// TODO: remove auto repaint and implement repaint by call; or check setIgnoreRepaint for cards;
					this.frame.repaint();
				} catch (InterruptedException ex){}
			}
		}
	}

}
