import java.awt.Point;
import java.util.Random;

// Soyut sınıf oluşturuldu.
abstract class AbstractCreateMaze {
	
	// Yatay ve dikey koordinatların sınırların dışında olup olmadığını kontrol ettik.
	protected boolean isOutofBorder(int x, int y, int colNumber, int rowNumber) {
		if ((x == 0 && y == 1) || (x == colNumber + 1 && y == rowNumber))
			return false;
		else
			return (x > colNumber || y > rowNumber || x < 1 || y < 1) ? true : false;
	}

	abstract void createMaze(Border[][] mazeBorder, int colNumber, int rowNumber);
}

// Labirent Oluşturma Algoritması
class RecursiveDivisionCreateMaze extends AbstractCreateMaze {

	// Belirli bir duvarda rastgele bir kapı açtık.
	protected void openAdoor(Border[][] mazeBorder, Point p1, Point p2, Random r) {
		
		if(p1.y==p2.y&&p1.x==p2.x){
			mazeBorder[p1.y][p1.x].setPassable(true);
			return;
		}
		
		if (p1.y == p2.y) {
			int pos = p1.x + r.nextInt((p2.x - p1.x) / 2 ) * 2; // Kapıyı random pozisyonlarda açtık.
			mazeBorder[p1.y][pos].setPassable(true);
		} 
		else if (p1.x == p2.x) {
			int pos = p1.y + r.nextInt((p2.y - p1.y) / 2 ) * 2;
			mazeBorder[pos][p1.x].setPassable(true);
		}
		
	}

	
	// Özyinelemeli labirent oluşturduk.
	private void recursiveCreateMaze(Border[][] mazeBorder, Point start, int height, int width, Random rand) {
		
		if (height <= 2 || width <= 2)
			return;
		
		// Çift sıra halinde bir duvar inşa ettik.
		int drawx = start.y + rand.nextInt(height / 2) * 2 + 1;
		for (int i = start.x; i < start.x + width; ++i)
			mazeBorder[drawx][i].setPassable(false);
		
		// Eşit sütunlarda bir duvar oluşturduk.
		int drawy = start.x + rand.nextInt(width / 2) * 2 + 1;
		for (int i = start.y; i < start.y + height; ++i)
			mazeBorder[i][drawy].setPassable(false);
		
		// Sol duvardan başlayarak, dört duvardaki üç kapıyı saat yönünün tersine sırayla rastgele açtık, 
		// sol duvarı 1 olarak gösterdik.
		int opendoor =  rand.nextInt(4)+ 1;
		
		switch (opendoor) {
		
		case 1:
			openAdoor(mazeBorder, new Point(drawy, drawx + 1), new Point(drawy, start.y + height - 1), rand);// 2
			openAdoor(mazeBorder, new Point(drawy + 1, drawx), new Point(start.x + width - 1, drawx), rand);// 3
			openAdoor(mazeBorder,new Point(drawy, start.y), new Point(drawy, drawx - 1) , rand);// 4
			break;
			
		case 2:
			openAdoor(mazeBorder, new Point(drawy + 1, drawx), new Point(start.x + width - 1, drawx), rand);// 3
			openAdoor(mazeBorder,new Point(drawy, start.y), new Point(drawy, drawx - 1) , rand);// 4
			openAdoor(mazeBorder, new Point(start.x, drawx), new Point(drawy - 1, drawx), rand);// 1
			break;
			
		case 3:
			openAdoor(mazeBorder,new Point(drawy, start.y), new Point(drawy, drawx - 1) , rand);// 4
			openAdoor(mazeBorder, new Point(start.x, drawx), new Point(drawy - 1, drawx), rand);// 1
			openAdoor(mazeBorder, new Point(drawy, drawx + 1), new Point(drawy, start.y + height - 1), rand);// 2
			break;
			
		case 4:
			openAdoor(mazeBorder, new Point(start.x, drawx), new Point(drawy - 1, drawx), rand);// 1
			openAdoor(mazeBorder, new Point(drawy, drawx + 1), new Point(drawy, start.y + height - 1), rand);// 2
			openAdoor(mazeBorder, new Point(drawy + 1, drawx), new Point(start.x + width - 1, drawx), rand);// 3
			break;
			
		default:
			break;
			
		}
		
		// Sol üst köşe
        recursiveCreateMaze(mazeBorder, start,drawx-start.y, drawy-start.x, rand);
        
        // Sağ üst köşe
        recursiveCreateMaze(mazeBorder, new Point(drawy+1,start.y),drawx-start.y, width-drawy+start.x-1, rand);
        
        // Sol alt köşe
        recursiveCreateMaze(mazeBorder, new Point(start.x,drawx+1),height - drawx + start.y - 1, drawy-start.x, rand);
        
        // Sağ alt köşe
        recursiveCreateMaze(mazeBorder,new Point(drawy+1,drawx+1),height - drawx + start.y - 1, width-drawy+start.x-1, rand);
        
	}

	@Override 
	
	// Soyut metot override edildi.
	public void createMaze(Border[][] mazeBorder, int colNumber, int rowNumber) {
		
		// TODO Otomatik oluşturulan yöntem taslağı
		Random rand = new Random();
		for (int i = 1; i < colNumber + 1; ++i)
			for (int j = 1; j < rowNumber + 1; ++j)
				mazeBorder[j][i].setPassable(true);
		
		Point start = new Point(1, 1);
		recursiveCreateMaze(mazeBorder, start, rowNumber, colNumber, rand);
		
	}

}
    