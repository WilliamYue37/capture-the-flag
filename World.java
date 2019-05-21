import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

	private int width;
	private int height;
	private Tile[][] tiles;
	private String path;
	
	private Camera camera;
	private Player player;
	private List<OtherPlayer> otherPlayers;
	private List<Projectile> projectiles;
	private Flag redFlag;
	private Flag blueFlag;

	private int score = 0;
	private int otherScore = 0;
	
	public World(String path, int width, int height) {
		this.path = path;
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(Tile.GRASS, x, y);
			}
		}

		camera = new Camera(width, height);
		player = new Player(this, 100, 100, true);
		redFlag = new Flag(this, 100, 500, true);
		blueFlag = new Flag(this, 500, 500, false);
		projectiles = new ArrayList<Projectile>();
		otherPlayers = new ArrayList<OtherPlayer>();
	}
	
	public void updatePlayer(int id, int x, int y) {
		for(int i = 0; i < otherPlayers.size(); i++) {
			if(otherPlayers.get(i).getID() == id) {
				otherPlayers.get(i).setX(x);
				otherPlayers.get(i).setY(y);
				return;
			}
		}

		otherPlayers.add(new OtherPlayer(this, x, y, id));
	}
	
	public World(String path) {
		this.path = path;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			width = Integer.parseInt(br.readLine());
			height = Integer.parseInt(br.readLine());
			camera = new Camera(width, height);
			tiles = new Tile[width][height];
			
			int playerX = Integer.parseInt(br.readLine());
			int playerY = Integer.parseInt(br.readLine());
			player = new Player(this, playerX, playerY, true);
			
			int redX = Integer.parseInt(br.readLine());
			int redY = Integer.parseInt(br.readLine());
			redFlag = new Flag(this, redX, redY, true);
			
			int blueX = Integer.parseInt(br.readLine());
			int blueY = Integer.parseInt(br.readLine());
			blueFlag = new Flag(this, blueX, blueY, false);
			
			for(int y = 0; y < height; y++) {
				String[] data = br.readLine().split(" ");
				for(int x = 0; x < width; x++) {
					tiles[x][y] = new Tile(data[x], x, y);
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		projectiles = new ArrayList<Projectile>();
		otherPlayers = new ArrayList<OtherPlayer>();
	}

	private static Random rand = new Random(2019);
	public void fireShotgun(int x, int y, float fireAngle, boolean red) {
		final int shells = 6;
		for(int i = 0; i < shells; i++) {
			float error = rand.nextFloat() * 0.4f - 0.2f;
			float speed = rand.nextFloat() * 100 + 400;
			projectiles.add(new Projectile(this, x, y, fireAngle + error, 0.3f, speed, red));
		}
	}
	
	public void fireSniper(int x, int y, float fireAngle, boolean red) {
		projectiles.add(new Projectile(this, x, y, fireAngle, 4, 1000, red));
	}
	
	private void updateScore() {
		if(player.justDied()) {
			otherScore++;
		}
		
		for(OtherPlayer otherPlayer : otherPlayers) {
			if(otherPlayer.justDied()) {
				if(otherPlayer.isRed() == player.isRed()) {
					otherScore++;
				} else {
					score++;
				}
			}
		}
	}
	
	public void update(float dt) {
		player.update(dt);
		redFlag.update(dt);
		blueFlag.update(dt);
		camera.centerOn(player.getCenterX(), player.getCenterY());
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(dt);
			if(projectiles.get(i).shouldRemove()) {
				projectiles.remove(i--);
			}
		}
		
		updateScore();
	}
	
	public void draw(Graphics g) {
		if(player.isDead()) {
			player.drawDeadScreen(g);
			return;
		}

		int xStart = Math.max(camera.getXOffset() / Tile.WIDTH - 1, 0);
		int yStart = Math.max(camera.getYOffset() / Tile.HEIGHT - 1, 0);
		int xEnd = Math.min((camera.getXOffset() + Client.WIDTH) / Tile.WIDTH + 1, width);
		int yEnd = Math.min((camera.getYOffset() + Client.HEIGHT) / Tile.HEIGHT + 1, height);
		for(int x = xStart; x < xEnd; x++) {
			for(int y = yStart; y < yEnd; y++) {
				tiles[x][y].draw(g, camera);
			}
		}
		
		for(int i = 0; i < otherPlayers.size(); i++) {
			if(camera.playerOnScreen(otherPlayers.get(i))) {
				otherPlayers.get(i).draw(g, camera);
			}
		}
		player.draw(g, camera);
		redFlag.draw(g, camera);
		blueFlag.draw(g, camera);
		for(int i = 0; i < projectiles.size(); i++)
			projectiles.get(i).draw(g, camera);
		
		player.drawHealthBar(g);
		drawScore(g);
	}
	
	private void drawScore(Graphics g) {
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 128));
		g.setColor(player.isRed() ? Color.RED : Color.BLUE);
		g.drawString(score + "", 10, Client.HEIGHT - 40);
		g.setColor(player.isRed() ? Color.BLUE : Color.RED);
		g.drawString(otherScore + "", Client.WIDTH - 100, Client.HEIGHT - 40);
	}
	
	public void changeTile(int x, int y, String code) {
		tiles[x][y] = new Tile(code, x, y);
	}
	
	public void save() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
			bw.write(width + "\n" + height + "\n");
			bw.write(player.getX() + "\n" + player.getY() + "\n");
			bw.write(redFlag.getX() + "\n" + redFlag.getY() + "\n");
			bw.write(blueFlag.getX() + "\n" + blueFlag.getY() + "\n");
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					bw.write(tiles[x][y].getCode() + " ");
				}
				
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void keyPressed(int k) {
		player.keyPressed(k);
	}
	
	public void keyReleased(int k) {
		player.keyReleased(k);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean canMove(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) {
			return false;
		} else {
			return !tiles[x][y].isSolid();
		}
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public List<OtherPlayer> getOtherPlayers() {
		return otherPlayers;
	}

	public Flag getFlag(boolean red) {
		return red ? redFlag : blueFlag;
	}
	
	private static final int GOAL = 5;
	
	public boolean won() {
		return score >= GOAL;
	}

	public boolean lost() {
		return otherScore >= GOAL;
	}
}
