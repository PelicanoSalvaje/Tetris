package game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Tetris";
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private boolean running = false;
	private int actualShape = 0;
	private int shapeKind;
	private Random rand = new Random();
	private Color[] shapeColor = new Color[100];
	
	private InputHandler input = new InputHandler(this);
	
	private final int rows = 20, cols = 10;
	private final int cellSize = 25;
	private int tickCounter = 0;
	private int ticksForMove = 30;
	private int actualTicks = 30;
	
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	private void setColor() {
		shapeColor[actualShape] = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
	}
	
	private synchronized void start() {
		if(running) {
			return;
		}
		
		running = true;
		new Thread(this).start();
		
	}
	
	private synchronized void stop() {
		if (!running) {
			return;
		}
		
		running = false;
		
		System.exit(1);
		
	}
	
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
	
	private void tick() {
		
		if (shapes.size() <1) {
			addShape();
			shapeKind = rand.nextInt(7);
			System.out.println(shapeKind);
			this.setColor();
			System.out.println("Añadida");
		}
		
		if (tickCounter > 60) {
			
			shapes.get(actualShape).moveShape(shapeKind);
			tickCounter = 0;
		}
		
		
		if (actualTicks > ticksForMove) {
			if (input.left.isPressed()) {
				shapes.get(actualShape).moveShapeLeft(shapeKind);
				actualTicks = 0;
			}else if (input.right.isPressed()) {
				shapes.get(actualShape).moveShapeRight(shapeKind);
				actualTicks = 0;
			}
			
		}
		
		tickCounter++;
		actualTicks++;
		
	}
	
	private void addShape() {
		this.shapes.add(new Shape(325, 25,cellSize));
	}
	
	public void render() {
		
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
	
		
		if (shapes.size() > 0) {
			for (int j = 0; j < shapes.size(); j++) {
				for (int i = 0; i < 4; i++) {
					g.setColor(shapeColor[actualShape]);
					g.fillRect(shapes.get(actualShape).getCords(shapeKind, i, 0), shapes.get(actualShape).getCords(shapeKind, i, 1), cellSize, cellSize);
				}
			}
			
		}
		
		// DRAW GRID //
		
		g.setColor(Color.white);
		
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
