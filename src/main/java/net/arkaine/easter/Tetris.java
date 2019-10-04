package net.arkaine.easter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import static java.awt.font.TextAttribute.WEIGHT_BOLD;
import static java.lang.Thread.sleep;

public class Tetris extends JFrame {
	private JPanel general = new JPanel();

	private static final long serialVersionUID = -8715353373678321308L;
	private boolean isPause = false;
	private boolean isOver = false;
	private int level = 1;
	//private Point tetrisSize = new Point(12,24);

	private int levelSize;
	private Point tetrisSize;
	private int briqueSize;
	private int piecesNumber = 9;


	private Point pieceOrigin;
	private int currentPiece = (int) (Math.random()* piecesNumber);
	private int rotation;
	private int nextPiece =(int) (Math.random()* piecesNumber);

	private long score;
	private Color[][] well;

	private final Point[][][] Tetraminos = {
			// I-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			// J-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			// L-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			// O-Piece
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			// S-Piece
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},
			
			// T-Piece
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			// Z-Piece
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			},
			// PLOT-Piece
			{
				{ new Point(0, 0)},
				{ new Point(0, 0)},
				{ new Point(0, 0)},
				{ new Point(0, 0)}
			},
			// CROSS-Piece
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) , new Point(1, 2) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) , new Point(1, 2) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) , new Point(1, 2) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) , new Point(1, 2) }
			}
	};
	
	private final Color[] tetraminoColors = {
		Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red, Color.WHITE, Color.MAGENTA
	};

	// Creates a border around the well and initializes the dropping piece
	private void init() {
		well = new Color[tetrisSize.x][tetrisSize.y];
		for (int i = 0; i < tetrisSize.x; i++) {
			for (int j = 0; j < (tetrisSize.y-1); j++) {
				if (i == 0 || i == tetrisSize.x-1 || j == tetrisSize.y-2) {
					well[i][j] = Color.GRAY;
				} else {
					well[i][j] = Color.BLACK;
				}
			}
		}
		newPiece();
	}

	// Put a new, random piece into the dropping position
	public void newPiece() {
		pieceOrigin = new Point(5, 2);
		if (this.isActive() && collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)){
			isOver = true;
			this.repaint();
		}
		rotation = 0;
		currentPiece = nextPiece;
		nextPiece = (int) (Math.random()* piecesNumber);
	}

	private void TwoSeconds() {
		try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void AllRed(Graphics g) {
			g.setColor(Color.RED);
			g.fillRect(0,0, this.getWidth(), this.getHeight());
			g.setColor(Color.WHITE);
		AttributedString attributedString = getBoldText(g, "GAME OVER");
		g.drawString(attributedString.getIterator(),20,200);
		AttributedString attributedString2 = getBoldText(g, "SCORE :" +score);
		g.drawString(attributedString2.getIterator(),20, 300);
	}

	private AttributedString getBoldText(Graphics g, String gameover) {
		AttributedString attributedString = new AttributedString(gameover);
		attributedString.addAttribute(TextAttribute.SIZE, 32);
		attributedString.addAttribute(TextAttribute.FONT, g.getFont());
		attributedString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
		attributedString.addAttribute(TextAttribute.WEIGHT, WEIGHT_BOLD);
		return attributedString;
	}

	private void AllGray(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0,0, this.getWidth(), this.getHeight());
		g.setColor(Color.WHITE);
		AttributedString attributedString = getBoldText(g, "PAUSE");
		g.drawString(attributedString.getIterator(),20,200);
	}

	// Collision test for the dropping piece
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}
	
	// Rotate the piece clockwise or counterclockwise
	public void rotate(int i) {
		int newRotation = (rotation + i) % 4;
		if (newRotation < 0) {
			newRotation = 3;
		}
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}
	
	// Move the piece left or right
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;	
		}
		repaint();
	}
	
	// Drops the piece one line or fixes it to the well if it can't drop
	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y += 1;
		} else {
			fixToWell();
		}	
		repaint();
	}
	
	// Make the dropping piece part of the well, so it is available for
	// collision detection.
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clearRows();
		newPiece();
	}
	
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < tetrisSize.x-1; i++) {
				well[i][j+1] = well[i][j];
			}
		}
	}
	
	// Clear completed rows from the field and award score according to
	// the number of simultaneously cleared rows.
	public void clearRows() {
		boolean gap;
		int numClears = 0;
		
		for (int j = tetrisSize.y-3; j > 0; j--) {
			gap = false;
			for (int i = 1; i < tetrisSize.x-1; i++) {
				if (well[i][j] == Color.BLACK) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				numClears += 1;
			}
		}
		
		switch (numClears) {
		case 1:
			score += 100;
			break;
		case 2:
			score += 300;
			break;
		case 3:
			score += 500;
			break;
		case 4:
			score += 800;
			break;
		}
		level = (int) ((score+levelSize) / levelSize);
	}
	
	// Draw the falling piece
	private void drawPiece(Graphics g) {		
		g.setColor(tetraminoColors[currentPiece]);
		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * (briqueSize+1),
					   (p.y + pieceOrigin.y) * (briqueSize+1),
					briqueSize, briqueSize);
		}
	}


	private void drawNextPiece(Graphics g) {
		g.setColor(tetraminoColors[nextPiece]);
		for (Point p : Tetraminos[nextPiece][0]) {
			int alpha = 127;
			g.setColor(new Color(200,200,150, alpha));
			g.fillRect((p.x* (briqueSize+1) )+210,
					   (p.y * (briqueSize+1))+40,
					briqueSize, briqueSize);
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics offgc;
		Image offscreen = null;
		offscreen = createImage(this.getWidth(), this.getHeight());
		offgc = offscreen.getGraphics();
		// Paint the well
		if(isOver){
			AllRed(offgc);
		}else{
			if(isPause){
				AllGray(offgc);
			}else{
				offgc.fillRect(0, 0, (briqueSize+1) * tetrisSize.x, (briqueSize+1) * tetrisSize.y-1);
				for (int i = 0; i < tetrisSize.x; i++) {
					for (int j = 0; j < tetrisSize.y-1; j++) {
						offgc.setColor(well[i][j]);
						offgc.fillRect((briqueSize+1) * i, (briqueSize+1) * j, briqueSize, briqueSize);
					}
				}
				// Draw the currently falling piece
				drawPiece(offgc);
				// Display the score
				offgc.setColor(Color.WHITE);
				offgc.drawString("score : " + score, 10, 60);
				offgc.drawString("level : " + level, 10, 80);
				drawNextPiece(offgc);
			}
		}
    	g.drawImage(offscreen, 0, 0, this);
	}

	public Tetris(int levelSize, Point tetrisSize, int briqueSize) {
		this.levelSize = levelSize;
		this.tetrisSize = tetrisSize;
		this.briqueSize = briqueSize;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(tetrisSize.x*(briqueSize+1)+10, (briqueSize+1)*(tetrisSize.y-1)+briqueSize);
		setVisible(true);

		init();

		// Keyboard controls
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					isPause = false;
					rotate(-1);
					break;
				case KeyEvent.VK_DOWN:
					isPause = false;
					rotate(+1);
					break;
				case KeyEvent.VK_LEFT:
					isPause = false;
					move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					isPause = false;
					move(+1);
					break;
				case KeyEvent.VK_SPACE:
					isPause = false;
					dropDown();
					score += 1;
					break;
				case KeyEvent.VK_P:
					isPause = !isPause;
					break;

				}
				repaint();
			}
			public void keyReleased(KeyEvent e) {
			}
		});
		
		// Make the falling piece drop every second
		new Thread() {
			@Override public void run() {
				while (true) {
					try {
						sleep(1000-(level*20));
						if(!isPause && !isOver)
							dropDown();
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}
