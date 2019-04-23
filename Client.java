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

import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class Client extends JPanel implements ActionListener, KeyListener, MouseListener, Runnable {
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private static final int X_OFFSET = 8;
	private static final int Y_OFFSET = 31;

	static FastScanner in;
	static PrintWriter out;
	
	private static int playerID = 0;
	
	public void run() {
		try {
			ProcessBuilder builder = new ProcessBuilder("client"); 
	        Process pro = builder.start();
	        in = new FastScanner(pro.getInputStream());
	        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(pro.getOutputStream())));
			
			while(true) {
				Player player = world.getPlayer();
				out.println(playerID + " " + player.getX() + " " + player.getY() + " " + player.getShotType() + " " + player.getFireAngle());
				out.flush();
				handleInput(in.nextLine());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Tile.create();
		JFrame frame = new JFrame("Capture The Flag!");
		frame.setSize(WIDTH + X_OFFSET, HEIGHT + Y_OFFSET);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.setResizable(false);
		Client client = new Client();
		client.run();
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
		world = new World("world.txt");
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
	
	private void handleInput(String data) {
		System.out.println(data);
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

	static class FastScanner {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;

        public FastScanner(InputStream stream) {
            this.stream = stream;
        }

        int read() {
            if (numChars == -1)
                throw new InputMismatchException();
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        boolean isSpaceChar(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        boolean isEndline(int c) {
            return c == '\n' || c == '\r' || c == -1;
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public String next() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public String nextLine() {
            int c = read();
            while (isEndline(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            } while (!isEndline(c));
            return res.toString();
        }
    }
}
