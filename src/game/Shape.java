package game;

/**
 * 
 * @author Sergio
 *
 */

/*
 * Class to make all Shapes and store the data of each one
 */
public class Shape {
	
	/*
	 * An integer that stores how much the shapes have to move
	 * 
	 */
	private int cellSize;
	
	/*
	 * A boolean that check if the Shape has landed
	 * 
	 */
	private boolean landed = false;
	
	private int[][][] cords;
	
	public Shape(int startx,int starty, int cellSize) {
		this.cellSize = cellSize;
		
		/*
		 * An integer tri-dimensional array to store all coordinates and kind of shapes of the gamee
		 * 
		 * 1st Position: What kind of shape is.
		 * 2nd Position: What block of the shape is. (Every shape is composed of 4 blocks)
		 * 3rd Position: Coordinate of the axis (0 = x, 1 = y)
		 * 
		 */
		
		int[][][] cords2 = {
				// LSHAPE
				{{startx,starty},{startx, starty + cellSize * 2},{startx - cellSize, starty + cellSize * 2},{startx,starty + cellSize}},
				// JSHAPE
				{{startx,starty},{startx + cellSize, starty + cellSize * 2},{startx, starty + cellSize * 2},{startx, starty + cellSize}},
				// ISHAPE
				{{startx,starty},{startx, starty + cellSize},{startx, starty + cellSize * 2},{startx,starty + cellSize * 3}},
				// OSHAPE
				{{startx,starty},{startx + cellSize, starty + cellSize},{startx, starty + cellSize},{startx + cellSize, starty}},
				// SSHAPE
				{{startx,starty},{startx + cellSize, starty},{startx - cellSize, starty + cellSize},{startx, starty + cellSize}},	
				// ZSHAPE
				{{startx,starty},{startx + cellSize, starty + cellSize},{startx - cellSize, starty},{startx, starty + cellSize}},
				//TSHAPE
				{{startx,starty},{startx + cellSize, starty + cellSize},{startx - cellSize, starty + cellSize},{startx, starty + cellSize}}
		};
		
		this.cords = cords2;
		
	}
	
	/**
	 * MOVE THE SHAPE DOWN
	 * @param   a   What shape want to move
	 * 
	 */
	public void moveShape(int a) {
		if (landed == false) {
			for (int i = 0; i < 4; i++) {
				this.cords[a][i][1] += this.cellSize;
			}
			
		}
		
	}
	
	/**
	 * MOVE THE SHAPE TO THE LEFT  
	 * @param   a   What shape want to move
	 * 
	 */
	
	public void moveShapeLeft(int a) {
		for (int i=0;i < 4; i++) {
			this.cords[a][i][0] -= this.cellSize;
		}
	}
	
	/**
	 * MOVE THE SHAPE TO THE RIGHT  
	 * @param   a   What shape want to move
	 * 
	 */
	
	public void moveShapeRight(int a) {
		for (int i=0;i < 4; i++) {
			this.cords[a][i][0] += this.cellSize;
		}
	}
	
	/**
	 * SET CLASS VARIABLE LANDED TO TRUE
	 * 
	 */
	public void landed() {
		this.landed = true;
	}
	
	/**
	 * GET THE COORDINATES	
	 *  @param   a   What shape want to move
	 *  @param   b   What block of the shape want to move
	 * 	@param   c   What axis want to move (x o y)
	 * 
	 *  @return The coordinates
	 * 
	 */
	public int getCords(int a, int b, int c){
		return cords[a][b][c];
	}
	
}
