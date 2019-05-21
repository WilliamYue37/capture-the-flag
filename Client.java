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
	
	public void run() {
		try {
			ProcessBuilder builder = new ProcessBuilder("client"); 
	        Process pro = builder.start();
	        in = new FastScanner(pro.getInputStream());
	        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(pro.getOutputStream())));
			
	        Player player = world.getPlayer();
			while(true) {
				handleInput(in.nextLine());
				if(player.canShoot()) {
					int shotType = player.getShotType();
					out.println(player.getX() + " " + player.getY() + " " + shotType + " " + player.getFireAngle());
					if(shotType != Player.NO_SHOT) {
						player.shot();
					}
				} else {
					out.println(player.getX() + " " + player.getY() + " 0 0");
				}
				
				out.flush();
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
		frame.add(client);
		frame.addKeyListener(client);
		frame.addMouseListener(client);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		client.run();
	}
	
	private World world;
	private long lastTime;
	
	private Client() {
		world = new World("world.txt");
		Timer timer = new Timer(1, this);
		timer.start();		
		lastTime = System.currentTimeMillis();
	}
	
	public void paint(Graphics g) {
		if(world.won()) {
			g.setColor(world.getPlayer().isRed() ? Color.RED : Color.BLUE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.YELLOW);
			g.drawString("WINNER!!!!!", WIDTH / 2 - 100, HEIGHT / 2);
		} else if(world.lost()) {
			g.setColor(world.getPlayer().isRed() ? Color.BLUE : Color.RED);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.YELLOW);
			g.drawString("LOSER!!!!!", WIDTH / 2 - 100, HEIGHT / 2);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			world.draw(g);
		}
	}
	
	private void update() {
		if(world.won() || world.lost()) { return; }
		float dt = (System.currentTimeMillis() - lastTime) / 1000f;
		lastTime = System.currentTimeMillis();
		world.update(dt);
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}
	
	private int playerID = -1;
	private void handleInput(String data) {
		if(data.charAt(0) == 'p') {
			if(playerID == -1) {
				playerID = Integer.parseInt(data.substring(7));
				boolean red = playerID % 2 == 0;
				world.getPlayer().setRed(red);
			}
			return;
		}
		
		String[] players = data.split(",");
		for(int i = 1; i <= players.length; i++) {
			String[] tokens = players[i - 1].split(" ");
			int x = Integer.parseInt(tokens[0]);
			int y = Integer.parseInt(tokens[1]);
			if(i != playerID && playerID != -1)
				world.updatePlayer(i, x, y);
			
			int shotType = Integer.parseInt(tokens[2]);
			float fireAngle = Integer.parseInt(tokens[3]) / 100f;
			if(shotType == Player.SHOTGUN) {
				world.fireShotgun(x + 16, y + 16, fireAngle, i % 2 == 0);
			} else if(shotType == Player.SNIPER) {
				world.fireSniper(x + 16, y + 16, fireAngle, i % 2 == 0);
			}
		}
	}
	
//	private String code = Tile.GRASS;
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
//		switch(k) {
//		case KeyEvent.VK_G :
//			code = Tile.GRASS;
//			break;
//		case KeyEvent.VK_W :
//			code = Tile.WALL;
//			break;
//		case KeyEvent.VK_R :
//			code = Tile.WATER;
//			break;
//		case KeyEvent.VK_S :
//			world.save();
//			break;
//		}
		
		world.keyPressed(k);
	}
	
	public void keyReleased(KeyEvent e) {
		world.keyReleased(e.getKeyCode());
	}
	
	public void mousePressed(MouseEvent e) {
//		int x = (e.getX() + world.getCamera().getXOffset() - X_OFFSET) / Tile.WIDTH;
//		int y = (e.getY() + world.getCamera().getYOffset() - Y_OFFSET) / Tile.HEIGHT;
//		world.changeTile(x, y, code);
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
