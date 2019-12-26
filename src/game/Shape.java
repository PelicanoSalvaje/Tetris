package game;

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
	
	/* 
	 * Integer that set the rotation of the shape
	 */
	private int rotation = 0;
	
	
	private int[][][][] cords;
	
	public Shape(int startx,int starty, int cellSize) {
		this.cellSize = cellSize;
		
		/*
		 * An integer quad-dimensional array to store all coordinates and kind of shapes of the gamee
		 * 
		 * 1st Position: What kind of shape is.
		 * 2nd Position: Rotation of that shape.
		 * 3nd Position: What block of the shape is. (Every shape is composed of 4 blocks)
		 * 	Every coordinate of each block is specifically setted, thats because when i check if it collides with borders i check the rightest block with the right border and so on
		 * 		Block 0: the center of the shape
		 * 		Block 1: The rightest block of the shape
		 * 		Block 2: The leftest block of the shape
		 * 		Block 3: The last block
		 * 4rd Position: Coordinate of the axis (0 = x, 1 = y)
		 * 
		 */
		
		int[][][][] cords2 = {
				// LSHAPE
				{
					{{startx,starty},{startx + cellSize, starty + cellSize * 2},{startx, starty + cellSize * 2},{startx, starty + cellSize}},
					{{startx,starty},{startx + cellSize, starty}, {startx - cellSize, starty}, {startx+ cellSize, starty + cellSize}},
					{{startx,starty},{startx + cellSize, starty}, {startx - cellSize, starty + cellSize}, {startx - cellSize, starty}},
					{{startx,starty},{startx,starty + cellSize}, {startx - cellSize, starty - cellSize}, {startx, starty - cellSize}}
				// JSHAPE
				},
				{
					{{startx,starty},{startx, starty + cellSize * 2},{startx - cellSize, starty + cellSize * 2},{startx,starty + cellSize}},
					{{startx,starty},{startx + cellSize, starty}, {startx - cellSize, starty}, {startx - cellSize, starty + cellSize}},
					{{startx,starty},{startx + cellSize, starty}, {startx - cellSize, starty - cellSize}, {startx - cellSize, starty}},
					{{startx,starty},{startx,starty + cellSize}, {startx - cellSize, starty + cellSize}, {startx, starty - cellSize}}
					
				},
				// ISHAPE
				{
					{{startx,starty},{startx, starty + cellSize},{startx, starty + cellSize * 2},{startx,starty + cellSize * 3}},
					{{startx,starty},{startx + cellSize, starty}, {startx + cellSize * 2, starty}, {startx - cellSize, starty}},
					{{startx,starty},{startx, starty + cellSize},{startx, starty + cellSize * 2},{startx,starty + cellSize * 3}},
					{{startx,starty},{startx + cellSize, starty}, {startx + cellSize * 2, starty}, {startx - cellSize, starty}},
				},
				// OSHAPE
				{
					{{startx,starty},{startx + cellSize, starty + cellSize},{startx, starty + cellSize},{startx + cellSize, starty}},
					{{startx,starty},{startx + cellSize, starty + cellSize},{startx, starty + cellSize},{startx + cellSize, starty}},
					{{startx,starty},{startx + cellSize, starty + cellSize},{startx, starty + cellSize},{startx + cellSize, starty}},
					{{startx,starty},{startx + cellSize, starty + cellSize},{startx, starty + cellSize},{startx + cellSize, starty}},
				},
				// SSHAPE
				{
					{{startx,starty},{startx + cellSize, starty + cellSize},{startx - cellSize, starty},{startx, starty + cellSize}},
					{{startx,starty},{startx + cellSize, starty},{startx, starty + cellSize}, {startx + cellSize, starty - cellSize}},
					{{startx,starty}, {startx + cellSize, starty}, {startx - cellSize, starty + cellSize}, {startx, starty + cellSize}},
					{{startx,starty},{startx, starty + cellSize},{startx - cellSize, starty - cellSize}, {startx - cellSize, starty}},
				
				},
				// ZSHAPE
				{
					{{startx,starty},{startx + cellSize, starty},{startx - cellSize, starty - cellSize},{startx, starty - cellSize}},
					{{startx,starty},{startx + cellSize, starty}, {startx, starty + cellSize},{startx + cellSize, starty - cellSize}},
					{{startx,starty},{startx + cellSize, starty + cellSize},{startx - cellSize, starty},{startx, starty + cellSize}},
					{{startx,starty},{startx, starty - cellSize}, {startx - cellSize, starty + cellSize},{startx - cellSize, starty}},
				},
				//TSHAPE
				{
					{{startx,starty},{startx + cellSize, starty},{startx - cellSize, starty},{startx, starty - cellSize}},
					{{startx,starty}, {startx + cellSize, starty}, {startx, starty + cellSize}, {startx, starty - cellSize}},
					{{startx,starty},{startx + cellSize, starty},{startx - cellSize, starty},{startx, starty + cellSize}},
					{{startx,starty}, {startx, starty + cellSize},{startx - cellSize, starty}, {startx, starty - cellSize}},
					
				}	
		};
		
		this.cords = cords2;
		
	}
	
	public void deleteBlock(int a, int b) {
		
		this.cords[a][this.rotation][b][0] = 2000;
		this.cords[a][this.rotation][b][1] = 2000;
	}
	
	public int getRotation() {
		return this.rotation;
	}
	/**
	 * SET THE ROTATION OF THE SHAPE
	 * IT ADD ONE TO THE ROTATION VARIABLE IF THIS IS LESS THAN 3
	 * 
	 */
	
	public void setRotation() {
		if (this.rotation <= 2) {
			this.rotation++;
		}else {
			this.rotation = 0;
		}
	}
	
	/**
	 * MOVE THE SHAPE DOWN
	 * @param   a   What shape want to move
	 * 
	 */
	public void moveShape(int a) {
		if (landed == false) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					this.cords[a][j][i][1] += this.cellSize;
				}
				
			}
			
		}
		
		
		
	}
	
	/**
	 * MOVE THE SHAPE TO THE LEFT  
	 * @param   a   What shape want to move
	 * 
	 */
	
	public void moveShapeLeft(int a) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.cords[a][j][i][0] -= this.cellSize;
			}
			
		}
	}
	
	/**
	 * MOVE THE SHAPE TO THE RIGHT  
	 * @param   a   What shape want to move
	 * 
	 */
	
	public void moveShapeRight(int a) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.cords[a][j][i][0] += this.cellSize;
			}
			
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
		return cords[a][this.rotation][b][c];
	}
	
}
