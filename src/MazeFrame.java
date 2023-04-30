import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class MazeFrame extends JFrame {
	
		private char solveMaze = 0;
		private char createMaze = 0;
	
		private JPanel contentPane;
		private Maze maze;
		private boolean isPause = false;
		private boolean bgmStart = true;
		private JPanel panel_1;
		private JPanel panel_2;
		private Panel panel;
		private JButton button;
		private JButton button_1;
		private JButton button_2;
		private JButton button_3;
		private JButton prompt;
		private JButton bgm;
		private JPanel panel_3;
		private JLabel label;
		private JPanel panel_5;
		private JPanel panel_6;
		private JLabel label_1;
		private JPanel panel_8;
		private JPanel panel_9;
		private JLabel lblBordersWidth;
		private JSpinner spinner_1;
		private JPanel panel_10;
		private JPanel panel_7;
		private JPanel panel_11;
		private JPanel panel_12;
		private JRadioButton rdbtnNewRadioButton;
		private ButtonGroup createMazeButton = new ButtonGroup();
		private JRadioButton rdbtnDepthFirstSearch;
		
		private ButtonGroup solveMazeButton = new ButtonGroup();

	// Çerçeveyi oluşturma.
	public MazeFrame(int rowNumber, int colNumber) {
		
		setTitle("Mouse in Maze");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Pencereyi kapatmaya yarar
		setSize(900 , 700);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 30, 15, 30));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		label = new JLabel("Welcome to the Mouse in the Maze\r\n");
		label.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		panel_3.add(label, BorderLayout.NORTH);

		panel_5 = new JPanel();
		panel_3.add(panel_5, BorderLayout.CENTER);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_5.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		label_1 = new JLabel(" Please adjust the size of the maze : ");
		label_1.setFont(new Font("SimSun-ExtB", Font.PLAIN, 14));
		panel_6.add(label_1, BorderLayout.NORTH);

		panel_7 = new JPanel();
		panel_6.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));

		panel_8 = new JPanel();
		panel_7.add(panel_8);

		panel_11 = new JPanel();
		panel_7.add(panel_11);

		panel_12 = new JPanel();
		panel_11.add(panel_12);

		panel_10 = new JPanel();
		panel_7.add(panel_10);

		panel_9 = new JPanel();
		panel_10.add(panel_9);

		lblBordersWidth = new JLabel("The size of the maze: ");
		panel_9.add(lblBordersWidth);

		spinner_1 = new JSpinner();
		spinner_1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				maze.setBorderWidth(Integer.parseInt(spinner_1.getValue().toString()));
				maze.repaint();
			}
		});
		
		spinner_1.setModel(new SpinnerNumberModel(16, 13 ,19 , 1));
		panel_9.add(spinner_1);

		panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		maze = new Maze(rowNumber, colNumber);
		panel_2.add(maze);

		panel = new Panel();
		panel_2.add(panel, BorderLayout.NORTH);

		// Başlatma 
		button = new JButton("Restart");
		button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (isBgmStart())
					maze.setAudioThreadStart();
				maze.init();
				maze.createMaze();
				button_1.setEnabled(true);
				button_1.setText("Pause");
				setPause(false);
				button_2.setEnabled(false);
				button_3.setEnabled(true);
				prompt.setEnabled(true);
				maze.requestFocus();
			}
		});
		
		panel.add(button);

		// Süreyi durdurma 
		button_1 = new JButton("Pause");
		button_1.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (!maze.isWin()) {
					if (!isPause() && maze.isStartTiming() && button_1.isEnabled()) {
						button_1.setText("Continue");
						((Timers) maze.getTimeText()).stop();
						setPause(true);
					} else if (isPause() && maze.isStartTiming() && button_1.isEnabled()) {
						button_1.setText("Pause");
						((Timers) maze.getTimeText()).proceed();
						setPause(false);
						maze.requestFocus();
					}
				}
			}
		});
		panel.add(button_1);

		// Yardım
		prompt = new JButton("Prompt");
		prompt.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!maze.isWin()) {
					maze.computerSolveMazeForMousePosition();
					maze.requestFocus();
				}
			}
		});
		panel.add(prompt);

		// Oyuncu oynama butonu
		button_2 = new JButton("Player do");
		button_2.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (button_2.isEnabled() && maze.isComputerDo()) {
					maze.setComputerDo(false);
					button_3.setEnabled(true);
					button_2.setEnabled(false);
					button_1.setEnabled(true);
					prompt.setEnabled(true);
					maze.resetStepNumber();
					maze.resetTimer();
					maze.setThreadStop();
					maze.setMousePosition(maze.getEntrance());
					maze.requestFocus();
				}
			}
		});
		button_2.setEnabled(false);
		panel.add(button_2);

		// Bilgisayara çözdürme
		button_3 = new JButton("Computer do");
		button_3.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (button_3.isEnabled() && !maze.isComputerDo() && maze.computerSolveMaze()) {
					button_2.setEnabled(true);
					button_3.setEnabled(false);
					button_1.setEnabled(false);
					prompt.setEnabled(false);
				}
			}
		});
		panel.add(button_3);

		bgm = new JButton();
		bgm.setIcon(new ImageIcon(System.getProperty("user.dir") + "/media//bgmStart.JPG"));
		bgm.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (isBgmStart()) {
					maze.setAudioThreadStop();
					bgm.setIcon(new ImageIcon(System.getProperty("user.dir") + "/media//bgmClose.JPG"));
				} else {
					maze.setAudioThreadStart();
					bgm.setIcon(new ImageIcon(System.getProperty("user.dir") + "/media//bgmStart.JPG"));
				}
				setBgmStart(!isBgmStart());
			}
		});
		panel.add(bgm);

		solveMazeButton.add(rdbtnDepthFirstSearch);
		
		rdbtnDepthFirstSearch.setSelected(true);

		createMazeButton.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton.setSelected(true);

		maze.requestFocus();
	}

	// UYGULAMAYI BAŞLATMA - main() metodu
	public static void main(String[] args) {
		
		try {
			
			MazeFrame mazeFrame = new MazeFrame(17, 17);
			Toolkit tool = mazeFrame.getToolkit(); // Bir Toolkit() nesnesi alın
			Image myimage = tool.getImage(System.getProperty("user.dir") + "/media//maze.jpg"); // Araçtan görüntü al
			mazeFrame.setIconImage(myimage);
			mazeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mazeFrame.setVisible(true);
			mazeFrame.setSize(1000, 500);
			mazeFrame.maze.setAudioThreadStart();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
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

	public boolean isBgmStart() {
		return bgmStart;
	}
	
	public void setBgmStart(boolean bgmStart) {
		this.bgmStart = bgmStart;
	}
}