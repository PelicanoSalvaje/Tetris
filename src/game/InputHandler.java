package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;



public class InputHandler implements KeyListener{

	public InputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	public class Key{
		private boolean pressed = false;
		
		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {
			pressed = isPressed;
		}
		
	}
	
	public ArrayList<Key>keys = new ArrayList<Key>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key right = new Key();
	public Key left = new Key();
	public Key space = new Key();
	
	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_W) {up.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_S) {down.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_D) {right.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_A) {left.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_SPACE) {space.toggle(isPressed);}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
		
	}


}
