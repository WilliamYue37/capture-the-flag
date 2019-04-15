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

public class World {

	private int width;
	private int height;
	private Tile[][] tiles;
	private String path;
	
	private Camera camera;
	private Player player;
	private List<Projectile> projectiles;
	private Flag redFlag;
	private Flag blueFlag;
	
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
		player = new Player(this, 100, 100);
		redFlag = new Flag(this, 100, 500, true);
		blueFlag = new Flag(this, 500, 500, false);
		projectiles = new ArrayList<Projectile>();
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
			player = new Player(this, playerX, playerY);
			
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
	}
	
	public void draw(Graphics g) {
		int xStart = Math.max(camera.getXOffset() / Tile.WIDTH - 1, 0);
		int yStart = Math.max(camera.getYOffset() / Tile.HEIGHT - 1, 0);
		int xEnd = Math.min((camera.getXOffset() + Client.WIDTH) / Tile.WIDTH + 1, width);
		int yEnd = Math.min((camera.getYOffset() + Client.HEIGHT) / Tile.HEIGHT + 1, height);
		for(int x = xStart; x < xEnd; x++) {
			for(int y = yStart; y < yEnd; y++) {
				tiles[x][y].draw(g, camera);
			}
		}
		
		player.draw(g, camera);
		redFlag.draw(g, camera);
		blueFlag.draw(g, camera);
		for(Projectile projectile : projectiles) {
			projectile.draw(g, camera);
		}
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
	
	public void addProjectile(Projectile projectile) {
		projectiles.add(projectile);
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
	
	public Tile tileAt(int x, int y) {
		return tiles[x][y];
	}
	
	public Camera getCamera() {
		return camera;
	}
}
