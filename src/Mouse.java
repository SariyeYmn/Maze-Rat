import java.awt.Image;
import javax.swing.ImageIcon;

public class Mouse {

	private int x;
	private int y;
	private Image mouse, cheese, grass, wall;

	public Mouse(int x, int y) {  // Constructor
		setX(x);
		setY(y);

		Direction(2);

	}

	public Image Direction(int secim) {
		
		ImageIcon img;
		
		if (secim == 1) { // Aşağı yön
			
			img = new ImageIcon("C:\\Users\\pc\\Pictures\\fare1.png");
			mouse = img.getImage();
			return mouse;
			
		} else if (secim == 2) { // Sağ yön
			
			img = new ImageIcon("C:\\Users\\pc\\Pictures\\fare2.png");
			mouse = img.getImage();
			return mouse;
			
		} else if (secim == 3) { // Yukarı yön
			
			img = new ImageIcon("C:\\Users\\pc\\Pictures\\fare3.png");
			mouse = img.getImage();
			return mouse;
			
		} else if (secim == 4) { // Sol yön
			
			img = new ImageIcon("C:\\Users\\pc\\Pictures\\fare4.png");
			mouse = img.getImage();
			return mouse;
			
		} else if (secim == 5) {  //Peynir
			
			img = new ImageIcon("C:\\Users\\pc\\Pictures\\cheese.png");
			cheese = img.getImage();
			return cheese;
			
		} else if (secim == 6) {  //Açık Yol
			
			img = new ImageIcon("C:\\Users\\pc\\Downloads\\grass2.png");
			grass = img.getImage();
			return grass;
			
		} else if (secim == 7) {  //Duvar
			
			img = new ImageIcon("C:\\Users\\pc\\Downloads\\wall2.png");
			wall = img.getImage();
			return wall;
			
		} else {
			
			return mouse;
		}

	}

	public Image getMouse() {
		
		return mouse;
	}

	public void setMouse(Image mouse) {
		
		this.mouse = mouse;
	}

	public int getX() {
		
		return x;
	}

	public void setX(int x) {
		
		this.x = x;
	}

	public int getY() {
		
		return y;
	}

	public int setY(int y) {
		
		this.y = y;
		return y;
	}

}