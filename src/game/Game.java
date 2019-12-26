package game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

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
	
	//private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private boolean running = false;
	private int actualShape = -1;
	private int shapeKind;
	private Random rand = new Random();
	private Color[] shapeColor = new Color[100];
	private boolean landFast = false;
	
	private InputHandler input = new InputHandler(this);
	
	private final int rows = 20, cols = 10;
	private final int cellSize = 25;
	private int tickCounter = 0;
	private int ticksForMove = 30, ticksForMoveDown = 10;
	private int actualTicks = 30;
	
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<Integer> kinds = new ArrayList<Integer>();
	
	private void setColor() {
		shapeColor[actualShape] = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
	}
	
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
	
		
		try {
			this.checkMovement();
			this.checkLanding();
		}catch (ArrayIndexOutOfBoundsException e) {
			running = false;
			System.out.println("HAS PERDIDO BRO");
			stop();
		}
		
		
		
		tickCounter++;
		actualTicks++;
		
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
				
				if (input.up.isPressed()) {
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
					}
					
					// LAND IF TOUCH OTHER SHAPE
					if (shapes.size() > 1) {
						
						for (int i = 0; i < shapes.size() - 1; i++) {
							for (int x = 0; x < 4; x++){
								if (shapes.get(actualShape).getCords(kinds.get(actualShape), j, 1) == shapes.get(i).getCords(kinds.get(i), x, 1) - cellSize && shapes.get(actualShape).getCords(kinds.get(actualShape), j, 0) == shapes.get(i).getCords(kinds.get(i), x, 0)) {
									shapes.get(actualShape).landed();
									this.addShape();
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
	
		
		// DRAW EVERY SHAPE
		
		if (shapes.size() > 0) {
			for (int j = 0; j < shapes.size(); j++) {
				for (int i = 0; i < 4; i++) {
					g.setColor(shapeColor[j]);
					g.fillRect(shapes.get(j).getCords(kinds.get(j), i, 0), shapes.get(j).getCords(kinds.get(j), i, 1), cellSize, cellSize);
				}
			}
			
		}
		
		// DRAW GRID //
		
		g.setColor(Color.DARK_GRAY);
		
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
