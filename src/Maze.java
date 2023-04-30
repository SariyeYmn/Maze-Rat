import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class Maze extends JPanel {
	
	private Point entrance = null;
	private Point exit = null;
	private int rowNumber;		// Satır sayısı
	private int colNumber;		// Sütun sayısı
	private int BorderWidth;	// Labirentin genişliği
	private Mouse mouse;
	private Border[][] mazeBorder;
	private boolean startTiming = false;
	private JPanel panel = new JPanel();
	private JTextField timeText = new Timers(), stepNumberText = new JTextField("0");
	private boolean computerDo = false;
	private Thread thread = null;
	private Thread audioThread = null;
	private int stepNmber;
	private static final char DepthFirstSearchSolveMaze = 0;
	private char solveMaze = DepthFirstSearchSolveMaze;
	private boolean promptSolveMaze = false;
	private char createMaze;

	public Maze(int row, int col) {
		
		this.setRowNumber(row);
		this.setColNumber(col);
		this.BorderWidth = 16;
		
		mazeBorder = new Border[getRowNumber() + 2][getColNumber() + 2];
		setLayout(new BorderLayout(0, 0));
		getTimeText().setForeground(Color.BLUE);
		getTimeText().setFont(new Font("宋体", Font.PLAIN, 14));
		getTimeText().setHorizontalAlignment(JTextField.CENTER);
		
		stepNumberText.setEnabled(false);
		getStepNumberText().setForeground(Color.BLUE);
		getStepNumberText().setFont(new Font("宋体", Font.PLAIN, 16));
		getStepNumberText().setHorizontalAlignment(JTextField.CENTER);
		
		Label timeLabel = new Label("Time:"), stepLabel = new Label("StepNumber:");
		timeLabel.setAlignment(Label.RIGHT);
		stepLabel.setAlignment(Label.RIGHT);
		
		panel.setLayout(new GridLayout(1, 4));
		add(panel, BorderLayout.NORTH);
		panel.add(timeLabel);
		panel.add(getTimeText());
		panel.add(stepLabel);
		panel.add(getStepNumberText());
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (!isComputerDo()) {
					requestFocus();
				}
			}
		});
		
		setKeyListener();
		createMaze();
	}

	public void init() {
		
		mazeBorder = new Border[getRowNumber() + 2][getColNumber() + 2];
		setPromptSolveMaze(false);
		setComputerDo(false);
		setThreadStop();
		resetStepNumber();
		resetTimer();
		
		for (int i = 1; i < getRowNumber() + 1; ++i)
			for (int j = 1; j < getColNumber() + 1; ++j) {
				mazeBorder[i][j] = new Border();
			}
		
		for (int i = 0; i < getRowNumber() + 2; ++i) {
			mazeBorder[i][0] = new Border();
			mazeBorder[i][getColNumber() + 1] = new Border();
		}
		
		for (int j = 0; j < getColNumber() + 2; ++j) {
			mazeBorder[0][j] = new Border();
			mazeBorder[getRowNumber() + 1][j] = new Border();
		}
		
		mouse = new Mouse(0, 1);
		setEntrance(new Point(0, 1));
		setExit(new Point(getColNumber() + 1, getRowNumber()));
		mazeBorder[getEntrance().y][getEntrance().x].setPassable(true);
		mazeBorder[getExit().y][getExit().x].setPassable(true);
	}

	// Labirenti tamamladın mı?
	public boolean isWin() {
		if (getExit().x == mouse.getX() && getExit().y == mouse.getY()) {
			return true;
		}
		return false;
	}

	// Labirent tamamlandıysa, oyun sonu iletişim kutusu açılır.
	private void GameOverMessage() {
		
		((Timers) getTimeText()).stop();
		JOptionPane.showMessageDialog(null,
				"You found the cheese and completed the maze congratulations !\n" + "The time you use to complete the maze : "
						+ timeText.getText() + "\nThe number of steps you use to complete the maze : "
						+ stepNmber,
				"GameOver", JOptionPane.INFORMATION_MESSAGE);
	}

	// Yatay ve dikey koordinatların sınırların dışında olup olmadığını doğrular.
	private boolean isOutofBorder(int x, int y) {
		
		if ((x == 0 && y == 1) || (x == getColNumber() + 1 && y == getRowNumber()))
			return false;
		else
			return (x > getColNumber() || y > getRowNumber() || x < 1 || y < 1) ? true : false;
	}

	// Bir labirent çizer.
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		for (int i = 0; i < getRowNumber() + 2; ++i)
			for (int j = 0; j < getColNumber() + 2; ++j) {
			
				if (mazeBorder[i][j].isPassable())
					g.drawImage(mouse.Direction(6), (j + 1) * BorderWidth, (i + 1) * BorderWidth + 30, null);
				else
					g.drawImage(mouse.Direction(7), (j + 1) * BorderWidth, (i + 1) * BorderWidth + 30, null);
			}
		
		// Peynir labirente eklendi.
		g.drawImage(mouse.Direction(5),(getColNumber() + 2) * BorderWidth, (getRowNumber() + 1) * BorderWidth + 30,null);
		// Fare labirente eklendi.
		g.drawImage(mouse.getMouse(), (mouse.getX() + 1) * BorderWidth, (mouse.getY() + 1) * BorderWidth + 30, null);	
		
		// Kullanıcının labirenti çözmesine yardımcı olur.
		if (isPromptSolveMaze()) {
			
			Stack<Point> pathStack = promptsolveMaze();
			g.setColor(Color.WHITE);
			Point start = pathStack.pop();
			
			while (!pathStack.isEmpty()) {
				Point end = pathStack.pop();
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(3.0f));
				g2.drawLine((int) (start.getX() + 1) * BorderWidth + BorderWidth / 2,
						(int) (start.getY() + 1) * BorderWidth + 30 + BorderWidth / 2,
						(int) (end.getX() + 1) * BorderWidth + BorderWidth / 2,
						(int) (end.getY() + 1) * BorderWidth + 30 + BorderWidth / 2);
				start = end;
			}
		}
	}

	// Klavye yön tuşlarını ayarlar.
	synchronized private void move(int c) {
		
		int tx = mouse.getX(), ty = mouse.getY();
		switch (c) {
		
			case KeyEvent.VK_LEFT:
				mouse.Direction(4); // Farenin yönü sola döndü.
				--tx;
				break;
				
			case KeyEvent.VK_RIGHT:
				mouse.Direction(2); // Farenin yönü sağa döndü.
				++tx;
				break;
				
			case KeyEvent.VK_UP:
				mouse.Direction(3); // Farenin yönü yukarı döndü.
				--ty;
				break;
				
			case KeyEvent.VK_DOWN:
				mouse.Direction(1); // Farenin yönü aşağı döndü.
				++ty;
				break;
				
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
				
			default:
				// Klavyedeki diğer tuşlara basmanın hala klavye ses efektleri üretmesini ve pedometreyi 
				// artırmasını önleyin
				tx = 0;
				ty = 0;
				break;
				}
		
		if (!isOutofBorder(tx, ty) && mazeBorder[ty][tx].isPassable()) {
			
			mouse.setX(tx);
			mouse.setY(ty);
			++stepNmber;
			stepNumberText.setText(Integer.toString(stepNmber));
			
			if (!isStartTiming()) {
				setStartTiming(!isStartTiming());
				((Timers) getTimeText()).start();
			}
		}

	}

	private void setKeyListener() {
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (!isWin()) {
					int c = e.getKeyCode();
					move(c);
					repaint();
					if (isWin() && !isComputerDo())
						GameOverMessage();
				}
			}
		});
	}

	// Zamanlayıcıyı sıfıra döndürür.
	public void resetTimer() {
		setStartTiming(false);
		getTimeText().setText("00:00:00");
		((Timers) timeText).restart();
	}

	// Pedometreyi sıfıra döndürür.
	public void resetStepNumber() {
		setStepNmber(0);
		stepNumberText.setText(Integer.toString(stepNmber));
	}

	// Farenin konumunu ayarlar.
	public void setMousePosition(Point p) {
		mouse.setX(p.x);
		mouse.setY(p.y);
		mouse.Direction(2); // Fare sağ yönde hareket edecek.
		    
		repaint();
	}

	// Bir labirent inşa ettik.
	public void createMaze() {
		
		init();
		AbstractCreateMaze c = null;
		c = new RecursiveDivisionCreateMaze();
		c.createMaze(mazeBorder, getColNumber(), getRowNumber());
		repaint();
	}

	// Labirentten bir çıkış yolu bulur.
	private Stack<Point> solveMaze(Point p) {
		
		AbstractSolveMaze a = null;
		a = new DepthFirstSearchSolveMaze();
		return a.solveMaze(mazeBorder, p, getExit(), getColNumber(), getRowNumber());
	}

	// Fare belirli bir pozisyondayken labirentten çıkış yolunu bulur.
	private Stack<Point> promptsolveMaze() {
		
		AbstractSolveMaze a = null;
		a = new DepthFirstSearchSolveMaze();
		return a.solveMaze(mazeBorder, new Point(mouse.getX(), mouse.getY()), getExit(), getColNumber(), getRowNumber());
	}

	// Labirentten belirli bir konumdaki farenin yolu, labirentte belirli bir süre boyunca görüntülenir.
	private void computerSolveMazeForMousePositionForTime(int time) {
		
		if (getThread() == null)
			setThread(new Thread() {
				
				@Override
				public void run() {
					
					while (!isInterrupted())
						try {
							setPromptSolveMaze(true);
							repaint();
							Thread.sleep(time);
							setPromptSolveMaze(false);
							repaint();
							setThreadStop();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							break;
						}
				}
			});
		getThread().start();
	}

	// Zaman içinde belirli bir pozisyonda labirentten çıkan farenin yolunu gösterir.
	public boolean computerSolveMazeForMousePosition() {
		
		setThreadStop();
		((Timers) getTimeText()).stop();
		int time = 0;
		
		Object[] selections = { "forever", "10s", "5s", "3s", "1s" };
		Object select = JOptionPane.showInputDialog(null, "Please select the speed of which the mouse runs",
				"Mouse in Maze", JOptionPane.INFORMATION_MESSAGE, null, selections, selections[2]);
		
		if (select != null) {
			switch ((String) select) {
			
				case "forever":
					time = 2000000000;
					break;
					
				case "10s":
					time = 10000;
					break;
					
				case "5s":
					time = 5000;
					break;
					
				case "3s":
					time = 3000;
					break;
					
				case "1s":
					time = 1000;
					break;
					
				default:
					break;
				}
			
			computerSolveMazeForMousePositionForTime(time);
			((Timers) getTimeText()).proceed();
			return true;
			
		} else
			return false;
	}

	// Hızda, bilgisayar labirentten ilk konumdaki fareyi yalnızca computerSolveMaze() metodu için yürütecektir.
	private void computerSolveMazeForSpeed(int speed) {
		
		setComputerDo(true);
		Point p = null;
		
		if (isWin())
			p = getEntrance();
		else
			p = new Point(mouse.getX(), mouse.getY());
		
		Stack<Point> stack = solveMaze(p);
		resetTimer();
		resetStepNumber();
		
		if (getThread() == null)
			setThread(new Thread() {
				
				@Override
				public void run() {
					
					while (!isInterrupted())
						try {
							while (!stack.isEmpty()) {
								Point p = stack.pop();
								setMousePosition(p);
								++stepNmber;
								stepNumberText.setText(Integer.toString(stepNmber));
								Thread.sleep(speed);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							break;
						}
				}
			});
		
		getThread().start();
	}

	// Bilgisayar labirentin dışına ilk pozisyonda fareyi yürütecek.
	public boolean computerSolveMaze() {
		
		int speed = 0;
		setThreadStop();
		Object[] selections = { "lower seed", "low speed", "medium speed", "high speed", "higher speed" };
		Object select = JOptionPane.showInputDialog(null, "Please select the speed of which the mouse runs",
				"Mouse in Maze", JOptionPane.INFORMATION_MESSAGE, null, selections, selections[2]);
		
		if (select != null) {
			
			switch ((String) select) {
				case "lower seed":
					speed = 400;
					break;
				case "low speed":
					speed = 300;
					break;
				case "medium speed":
					speed = 200;
					break;
				case "high speed":
					speed = 100;
					break;
				case "higher speed":
					speed = 20;
					break;
				default:
					break;
			}
			
			computerSolveMazeForSpeed(speed);
			return true;
		} else
			return false;
	}

	public int getBorderWidth() {
		return BorderWidth;
	}

	public void setBorderWidth(int BorderWidth) {
		this.BorderWidth = BorderWidth;
	}

	public JTextField getTimeText() {
		return timeText;
	}

	public boolean isStartTiming() {
		return startTiming;
	}

	public void setStartTiming(boolean startTiming) {
		this.startTiming = startTiming;
	}

	public Point getEntrance() {
		return entrance;
	}

	public void setEntrance(Point entrance) {
		this.entrance = entrance;
	}

	public Point getExit() {
		return exit;
	}

	private void setExit(Point exit) {
		this.exit = exit;
	}

	public boolean isComputerDo() {
		return computerDo;
	}

	public void setComputerDo(boolean computerDo) {
		this.computerDo = computerDo;
	}

	public Thread getThread() {
		return thread;
	}

	private void setThread(Thread thread) {
		this.thread = thread;
	}

	public void setThreadStop() {
		
		if (getThread() != null) {
			if (isPromptSolveMaze())
				setPromptSolveMaze(false);
			thread.interrupt();
			setThread(null);
		}
	}
	
	public JTextField getStepNumberText() {
		return stepNumberText;
	}

	public int getStepNmber() {
		return stepNmber;
	}

	public void setStepNmber(int stepNmber) {
		this.stepNmber = stepNmber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	public char getSolveMaze() {
		return solveMaze;
	}

	public void setSolveMaze(char solveMaze) {
		this.solveMaze = solveMaze;
	}

	public char getCreateMaze() {
		return createMaze;
	}

	public void setCreateMaze(char createMaze) {
		this.createMaze = createMaze;
	}

	public boolean isPromptSolveMaze() {
		return promptSolveMaze;
	}

	public void setPromptSolveMaze(boolean promptSolveMaze) {
		this.promptSolveMaze = promptSolveMaze;
	}
	
	private Thread getAudioThread() {
		return audioThread;
	}

	private void setAudioThread(Thread audioThread) {
		this.audioThread = audioThread;
	}

	public void setAudioThreadStart() {
		
		if (getAudioThread() == null) {
			
			setAudioThread(new Thread() {
				@Override
				
				public void run() {
					AudioClip sound = null;
					
					while (!isInterrupted())
						try {
							// Geçerli java projesinin altındaki dosya.
							File file1 = new File("media//background.wav");
							sound = Applet.newAudioClip(file1.toURI().toURL());
							sound.play();
							Thread.sleep(24000);
						} catch (Exception e) {
							break;
						}
					sound.stop();
				}
			});
			getAudioThread().start();
		}
	}

	public void setAudioThreadStop() {
		
		if (getAudioThread() != null) {
			getAudioThread().interrupt();
			setAudioThread(null);
		}
	}
}
