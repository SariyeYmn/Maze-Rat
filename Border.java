import java.awt.Point;

class Border {
	
	private boolean Passable;   // Farenin geçip geçemeyeceği
	private Point Father; 		// Geçiş yapılıp yapılmadığı
	
	public Border() {
		setPassable(false);
		setFather(null);
	}

	public boolean isPassable() {
		return Passable;
	}

	public void setPassable(boolean isPassable) {
		this.Passable = isPassable;
	}

	public Point getFather() {
		return Father;
	}
	
	public void setFather(Point father) {
		Father = father;
	}
	
}
   