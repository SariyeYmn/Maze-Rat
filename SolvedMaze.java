import java.awt.Point;
import java.util.*;

abstract class AbstractSolveMaze {
	
	protected Stack<Point> pathStack = null;
	
	// Yatay ve dikey koordinatların sınırların dışında olup olmadığını doğrular.
	protected boolean isOutofBorder(int x, int y, int colNumber, int rowNumber) {
		
		if ((x == 0 && y == 1) || (x == colNumber + 1 && y == rowNumber))
			return false;
		else
			return (x > colNumber || y > rowNumber || x < 1 || y < 1) ? true : false;
	}

	abstract Stack<Point> solveMaze(Border[][] mazeBorder, Point entrance, Point exit, int colNumber, int rowNumber);
}

class DepthFirstSearchSolveMaze extends AbstractSolveMaze {
	// Derinlik öncelikli arama yöntemine göre p noktası tarafından ziyaret edilmemiş bitişik bir labirent 
	// birimi seçer ve seçilen birimin ziyaret edilen birimini true olarak ayarlayın ve solveMaze() 
	// işlevini çağırın
	/*
	DFS: Veri yapısı olarak stack kullanır.
	Bir başlangıç noktası seçilir.Daha sonra bu düğümden gidilebilecek komşu düğümlere tek tek gidilir.
	Tüm düğümler gezilmiş olana dek bu işlem devam eder.
	Yani gidilen herbir düğümün komşuları ziyaret edilerek rekürsif bir şekilde gezilir.
	Last in First Out : Son giren ilk çıkar mantığına göre push ile eleman aldıkça pop ile elemanları dışarı çıkarırız.
	*/
	
	protected Point ArroundPointDepthFirst(Border[][] mazeBorder, Point p, int colNumber, int rowNumber) {
		
		final int[] arroundPoint = { -1, 0, 1, 0, -1 };
		// Bir noktanın etrafındaki dört noktanın koordinatları değişir, sıralama sol üst ve sağ alttır
		for (int i = 0; i < 4;) {
			
			int x = p.x + arroundPoint[i];
			int y = p.y + arroundPoint[++i];
			if (!isOutofBorder(x, y, colNumber, rowNumber) && mazeBorder[y][x].isPassable()
					&& mazeBorder[y][x].getFather() == null) {
				p = new Point(x, y);
				mazeBorder[y][x].setFather(p);
				return p;
			}
		}
		return null;
	}

	@Override
	public Stack<Point> solveMaze(Border[][] mazeBorder, Point entrance, Point exit, int colNumber, int rowNumber) {
		
		// TODO Auto-generated method stub
		pathStack = new Stack<Point>();
		Deque<Point> pathDeque = new ArrayDeque<Point>();
		Point judge = new Point(0, 0);
		Point end = new Point(exit.x - 1, exit.y);
		
		for (int i = 0; i < rowNumber + 2; ++i)
			for (int j = 0; j < colNumber + 2; ++j)
				mazeBorder[i][j].setFather(null);
		mazeBorder[entrance.y][entrance.x].setFather(judge);
		pathDeque.addLast(entrance);
		Point currentPoint = entrance;
		
		while (!currentPoint.equals(end)) {
			currentPoint = ArroundPointDepthFirst(mazeBorder, currentPoint, colNumber, rowNumber);
			
			if (currentPoint == null) {
				pathDeque.removeLast();
				if (pathDeque.isEmpty())
					break;
				currentPoint = pathDeque.getLast();
			} else {
				pathDeque.addLast(currentPoint);
			}
		}
		
		mazeBorder[exit.y][exit.x].setFather(end);
		pathDeque.addLast(exit);
		
		while (!pathDeque.isEmpty())
			pathStack.push(pathDeque.removeLast());
		return pathStack;
	}
}
