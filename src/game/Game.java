package game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author Sergio
 *
 */

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Tetris";
	
	private boolean running = false;
	private int actualShape = -1;
	private int shapeKind;
	private Random rand = new Random();
	private boolean landFast = false;
	private boolean gamePaused = false;
	
	private int lines = 0;
	private int score = 0;
	private InputHandler input = new InputHandler(this);
	
	private final int rows = 20, cols = 10;
	private final int cellSize = 25;
	private int tickCounter = 0;
	private int ticksForMove = 20, ticksForMoveDown = 10;
	private int actualTicks = 30;
	

	private Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.ORANGE, Color.CYAN};
	private Color[] shapeColor = new Color[200];
	
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<Integer> kinds = new ArrayList<Integer>();
	
	
	/**
	 * START method is called once the game starts
	 * It set running variable to true so the RUN method can start running the game
	 * 
	 * This method open a new thread
	 * and add the frist shape
	 */
	private synchronized void start() {
		if(running) {
			return;
		}
		
		running = true;
		new Thread(this).start();
		this.addShape();
		
	}
	
	/**
	 * STOP method is called once the RUN method ends
	 * It kills the program
	 */
	private synchronized void stop() {
		if (!running) {
			return;
		}
		
		running = false;
		
		System.exit(1);
		
	}
	
	/**
	 * RUN Method is called when the game starts
	 * Basically it is the game engine
	 * It set the tick-rate and shows the frames per second
	 * 
	 */
	public void run() {
		
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			//GAME LOOP
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " Ticks, Fps " + frames);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();
	}
	
	/**
	 * This method is called every tick of the game
	 * 
	 * Here is where all mechanics and comprobations of the game are made
	 * 
	 */
	private void tick() {
	
		if (!gamePaused) {
			this.checkLanding();
			this.checkMovement();
			
			tickCounter++;
			actualTicks++;
		}
		
		if (shapes.size() >= 90) {
			restart();
		}
		
	}
	/**
	 * WHEN PLAYER LOSE ASK FOR RESTART OR QUIT
	 * IF RESTART IS CHOSEN IT RESTART ALL VARIABLES TO START THE GAME AGAIN
	 */
	private void restart() {
		
		this.gamePaused = true;
		int reiniciar = JOptionPane.showOptionDialog(null,"¿ Qué deseas hacer? ", "¡ HAS PERDIDO ! ", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,new Object[] {"Reiniciar partida","Salir"}, null);
		
		if (reiniciar == 1) {
			stop();
		}else if (reiniciar == 0) {
			
			actualShape = -1;
			lines = 0;
			score = 0;
			
			System.out.println("Reiniciad");
			shapeColor = new Color[shapeColor.length - 1];
			
			shapes.clear();
			kinds.clear();
			
			gamePaused = false;
			
			this.addShape();
			
		}
	}
	
	/**
	 * Set a random color for the actual shape
	 */
	private void setColor() {
		shapeColor[actualShape] = colors[rand.nextInt(colors.length)];
	}
	
	
	/**
	 * ADDSHAPE METHOD
	 * This method add a new Shape when its called
	 * 
	 */
	
	private void addShape() {
		this.shapes.add(new Shape(300, 50,cellSize));
		shapeKind = rand.nextInt(7);
		this.kinds.add(shapeKind);
		actualShape++;
		this.setColor();
		landFast = false;
	}
	
	/**
	 * DELETE THE SPECIFIED LINE
	 * MOVE DOWN THE UPPER LINES
	 * @param starty The y position of the line that have to remove.
	 * 
	 * IT ADD ONE TO THE LINES VARIABLE WHEN IT DELETES A LINE
	 * 
	 */
	
	private void deleteLine(int starty) {
		int y = starty;
		int x = 200;
		int counter = 0;
		
		// DELETE THE LINE OF BLOCKS
		// MOVE THE UPPER BLOCKS DOWN
		
		// ITERATE THROUGH ALL ROWS AND COLLS
		for (; y >= 200; y -= cellSize) {
			for (; x <= 425; x += cellSize) {
				
				// ITERATE THROUGH ALL SHAPES
				for (int s = 0; s < shapes.size(); s++) {
					for (int j = 0; j < 4; j++) {
						if (x == shapes.get(s).getCords(kinds.get(s), j, 0) && y == shapes.get(s).getCords(kinds.get(s), j, 1)) {
							if (counter < 10) {
								shapes.get(s).deleteBlock(kinds.get(s), j);
								counter++;
							}else if (counter >= 10){
								shapes.get(s).moveBlockDown(kinds.get(s), j);
							}
						}
					}
				}
				
			}
			
			x = 200;
		}
		
		lines += 1;	
	}
	
	/**
	 * CHECK IF THERE IS A LINE
	 *
	 * GOES THROUGH ALL ROWS AND COLS AND CHECK EVERY BLOCK OF EVERY SHAPE AND COUNT IT
	 * IF IT IS TEN IT CALL A FUNCTION TO DELETE LINE 
	 * 
	 * IN ORDER TO THE LINES REMOVED IT ADD THE CORRESPONDENT SCORE
	 */
	
	private void checkLine() {
		
		int startX = 200;
		int startY = 200;
		int points = 0;
		int contador = 0;
		
		if (shapes.size() > 0) {
		// Recorrer cada fila y columna{
			for(int i = 1; i <= rows; i++) {
				for(int j = 1; j <= cols; j++) {
					
					// Recorrer cada ficha
					for (int s = 0; s < shapes.size(); s++) {
						for (int x = 0; x < 4; x++) {
							if (startX == shapes.get(s).getCords(kinds.get(s), x, 0) && startY == shapes.get(s).getCords(kinds.get(s), x, 1))
								contador++;		
						}
					}
					startX += cellSize;
				}
				
				if (contador == 10) {
					this.deleteLine(startY);
					points++;
				}
					
				startX = 200;
				startY += cellSize;
				contador = 0;
				
			}
			
		}
		
		switch(points) {
		case 1:
			this.score += 40;
			break;
		case 2:
			this.score += 100;
			break;
		case 3: 
			this.score += 300;
			break;
		case 4:
			this.score += 1200;
			break;
		}
		
	}
			
		
	
	/**
	 *  PRIVATE METHOD THAT IS EXECUTED EVERY TICK OF THE GAME
	 *  
	 *  IT HANDLES ALL MOVEMENT ACTIONS
	 *  - SHAPE MOVING DOWN CONSTANTLY
	 *  - MOVE SHAPE FAST DOWN 
	 *  - MOVE SHAPE LEFT AND RIGHT AND CHECK COLLISIONS
	 *  - MOVE SHAPE DOWN FASTLY IF DOWN GETS PRESSED
	 *  
	 */
	
	private void checkMovement() {
		// MOVE CONSTANTLY DOWN
		if (tickCounter > 60) {
			
			shapes.get(actualShape).moveShape(kinds.get(actualShape));
			tickCounter = 0;
		}
		
		// MOVE THE PIECE DOWN FASTER UNTIL IT LANDS IF LANDFAST IS TRUE
		
		if (actualTicks > 5) {
			if (landFast) {
				shapes.get(actualShape).moveShape(kinds.get(actualShape));
				actualTicks = 0;
			}
			
		}
		
		// MOVE SHAPE LEFT AND RIGHT AND CHECK COLLISIONS
		
			if (actualTicks > ticksForMove) {
				if (input.left.isPressed() && shapes.get(actualShape).getCords(kinds.get(actualShape), 2, 0) >= 225) {
					shapes.get(actualShape).moveShapeLeft(kinds.get(actualShape));
					actualTicks = 0;
					
				}else if (input.right.isPressed() && shapes.get(actualShape).getCords(kinds.get(actualShape), 1, 0) <= 400) {
					shapes.get(actualShape).moveShapeRight(kinds.get(actualShape));
					actualTicks = 0;
				}
				
				if (input.up.isPressed() && shapes.get(actualShape).getCords(kinds.get(actualShape), 1, 0) <= 400 && shapes.get(actualShape).getCords(kinds.get(actualShape), 2, 0) >= 225) {
					shapes.get(actualShape).setRotation();
					actualTicks = 0;
				}
				
				// IF SPACE IS PRESSED IT SET LANDFAST TO TRUE
				if (input.space.isPressed()) {
					landFast = true;
					actualTicks = 0;
				}
			}
			
			// MOVE DOWN FASTER
			if (actualTicks > ticksForMoveDown) {
				if (input.down.isPressed()) {
					shapes.get(actualShape).moveShape(kinds.get(actualShape));
					actualTicks=0;
				}
			}
	
	}
	
	/**
	 * PRIVATE METHOD THAT CHECK ALL POSIBLE LANDINGS
	 * 
	 */
	
	private void checkLanding() {
		for (int j = 0; j < 4; j++) {
					
			// LAND IF TOUCH GROUND
			
			if (shapes.get(actualShape).getCords(kinds.get(actualShape), j, 1) >= 500) {
				shapes.get(actualShape).landed();
				this.addShape();
				this.checkLine();
			}
			
			// LAND IF TOUCH OTHER SHAPE
			if (shapes.size() > 1) {
				
				for (int i = 0; i < shapes.size() - 1; i++) {
					for (int x = 0; x < 4; x++){
						if (shapes.get(actualShape).getCords(kinds.get(actualShape), j, 1) == shapes.get(i).getCords(kinds.get(i), x, 1) - cellSize && shapes.get(actualShape).getCords(kinds.get(actualShape), j, 0) == shapes.get(i).getCords(kinds.get(i), x, 0)) {
							shapes.get(actualShape).landed();
							this.addShape();
							this.checkLine();
						}
					}
				}
			}
		}
	}
	
	/**
	 * RENDER method is the entrusted of graphics
	 * 
	 * All painting of the game are made here
	 * 
	 */
	
	public void render() {
		
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		// SET BACKGROUND TO DARK
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
	
		// DRAW VARIABLES
			// SET FONT AND COLOR
		g.setColor(Color.WHITE);
		g.setFont(new Font("Calibri",Font.BOLD, 30));
		
	
		// DRAW LINES
		g.drawString("Líneas: ", 550, 50);
		g.drawString(Integer.toString(lines), 675 , 50);
		
		// DRAW SCORE
		g.drawString("Puntuación: ", 550, 80);
		g.drawString(Integer.toString(score), 725 , 80);
		
		// DRAW EVERY SHAPE
		if (gamePaused == false) {
			if (shapes.size() > 0) {
				if (shapeColor.length > 0) {
					for (int j = 0; j < shapes.size(); j++) {
						for (int i = 0; i < 4; i++) {
							g.setColor(shapeColor[j]);
							g.fillRect(shapes.get(j).getCords(kinds.get(j), i, 0), shapes.get(j).getCords(kinds.get(j), i, 1), cellSize, cellSize);
						}
					}
				}
			}
		}
		
		
		// DRAW GRID //
		
		g.setColor(Color.white);
		g.fillRect(180, 20, 20,525);
		g.fillRect(450, 20, 20,525);
		
		g.fillRect(180, 10, 290,20);
		g.fillRect(180, 525, 290,20);
		
		g.setColor(new Color(46,46,46));
		
		
		
		// DRAW ROWS
		for (int i = 1; i <= rows + 1; i++) {
			g.fillRect(WIDTH/2, i * cellSize, cellSize * cols,5);
		}
		
		// DRAW COLS
		for (int i = 1; i <= cols + 1; i++) {
			g.fillRect((i + 7)* cellSize, cellSize, 5, 505);
		}
		
		
		
		g.dispose();
		bs.show();
	}

	
	/**
	 * MAIN METHOD
	 * 
	 * It set up all elements of JFrame 
	 * and calls the start method
	 * 
	 */
	public static void main(String[] args) {

		Game game = new Game();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
		
	}

	
}
