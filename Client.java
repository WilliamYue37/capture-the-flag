import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Client extends JPanel implements ActionListener, KeyListener, MouseListener {
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private static final int X_OFFSET = 8;
	private static final int Y_OFFSET = 31;
	
	public static void main(String[] args) {
		Tile.create();
		JFrame frame = new JFrame("Capture The Flag!");
		frame.setSize(WIDTH + X_OFFSET, HEIGHT + Y_OFFSET);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.setResizable(false);
		Client client = new Client();
		frame.add(client);
		frame.addKeyListener(client);
		frame.addMouseListener(client);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private World world;
	private String code = Tile.GRASS;

	private long lastTime;
	
	private Client() {
		world = new World("res/world.txt");
		Timer timer = new Timer(1, this);
		timer.start();		
		lastTime = System.currentTimeMillis();
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		world.draw(g);
	}
	
	private void update() {
		float dt = (System.currentTimeMillis() - lastTime) / 1000f;
		lastTime = System.currentTimeMillis();
		world.update(dt);
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		switch(k) {
		case KeyEvent.VK_G :
			code = Tile.GRASS;
			break;
		case KeyEvent.VK_W :
			code = Tile.WALL;
			break;
		case KeyEvent.VK_R :
			code = Tile.WATER;
			break;
		case KeyEvent.VK_S :
			world.save();
			break;
		}
		
		world.keyPressed(k);
	}
	
	public void keyReleased(KeyEvent e) {
		world.keyReleased(e.getKeyCode());
	}
	
	public void mousePressed(MouseEvent e) {
		int x = (e.getX() + world.getCamera().getXOffset() - X_OFFSET) / Tile.WIDTH;
		int y = (e.getY() + world.getCamera().getYOffset() - Y_OFFSET) / Tile.HEIGHT;
		world.changeTile(x, y, code);
	}
	
	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
