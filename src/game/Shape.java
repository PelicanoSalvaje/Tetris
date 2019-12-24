package game;

public class Shape {

	public enum shapes{LShape, JShape, IShape, OShape, SShape, ZShape, TShape};
	
	public int startx, starty;
	public int cellSize;
	
	public boolean landed = false;
	
	
	private int[][][] cords;
	
	public Shape(int startx,int starty, int cellSize) {
		this.startx = startx;
		this.starty = starty;
		this.cellSize = cellSize;
		
		int[][][] cords2 = {
				// LSHAPE
				{{startx,starty},{startx,starty + cellSize},{startx, starty + cellSize * 2},{startx - cellSize, starty + cellSize * 2}},
				// JSHAPE
				{{startx,starty},{startx, starty + cellSize},{startx, starty + cellSize * 2},{startx + cellSize, starty + cellSize * 2}},
				// ISHAPE
				{{startx,starty},{startx, starty + cellSize},{startx, starty + cellSize * 2},{startx,starty + cellSize * 3}},
				// OSHAPE
				{{startx,starty},{startx + cellSize, starty},{startx, starty + cellSize},{startx + cellSize, starty + cellSize}},
				// SSHAPE
				{{startx,starty},{startx + cellSize, starty},{startx, starty + cellSize},{startx - cellSize, starty + cellSize}},	
				// ZSHAPE
				{{startx,starty},{startx - cellSize, starty},{startx, starty + cellSize},{startx + cellSize, starty + cellSize}},
				//TSHAPE
				{{startx,starty},{startx, starty + cellSize},{startx - cellSize, starty + cellSize},{startx + cellSize, starty + cellSize}}
		};
		
		this.cords = cords2;
		
	}
	
	public void moveShape(int a) {
		if (landed == false) {
			for (int i = 0; i < 4; i++) {
				this.cords[a][i][1] += this.cellSize;
			}
			
		}
		
	}
	
	public void moveShapeLeft(int a) {
		for (int i=0;i < 4; i++) {
			this.cords[a][i][0] -= this.cellSize;
		}
	}
	
	public void moveShapeRight(int a) {
		for (int i=0;i < 4; i++) {
			this.cords[a][i][0] += this.cellSize;
		}
	}
	
	public int getCords(int a, int b, int c){
		return cords[a][b][c];
	}
	
}
