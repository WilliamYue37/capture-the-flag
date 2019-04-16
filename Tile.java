import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {

	public static final String GRASS = "g";
	public static final String WALL = "w";
	public static final String WATER = "W";
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	
	private BufferedImage image;
	private int x;
	private int y;
	private String code;
	
	public Tile(String code, int x, int y) {
		this.x = x;
		this.y = y;
		this.code = code;
		setAttribs();
	}
	
	private void setAttribs() {
		switch(code) {
		case GRASS :
			image = GRASS_IMAGE;
			break;
		case WALL :
			image = WALL_IMAGE;
			break;
		case WATER :
			image = ((int)(Math.random() * 2) == 0) ? WATER_1_IMAGE : WATER_2_IMAGE;
			break;
		default :
			System.err.println("Unknown tile code: " + code);
			System.exit(0);
		}
	}
	
	public void draw(Graphics g, Camera camera) {
		g.drawImage(image, x * WIDTH - camera.getXOffset(), y * HEIGHT - camera.getYOffset(), WIDTH, HEIGHT, null);
	}
	
	public String getCode() {
		return code;
	}
	
	private static BufferedImage GRASS_IMAGE;
	private static BufferedImage WALL_IMAGE;
	private static BufferedImage WATER_1_IMAGE;
	private static BufferedImage WATER_2_IMAGE;
	public static void create() {
		BufferedImage tileSet = loadImage("tileSet.png");
		GRASS_IMAGE = tileSet.getSubimage(0, 0, 32, 32);
		WALL_IMAGE = tileSet.getSubimage(32, 0, 32, 32);
		WATER_1_IMAGE = tileSet.getSubimage(32, 32, 32, 32);
		WATER_2_IMAGE = tileSet.getSubimage(0, 32, 32, 32);
	}
	
	private static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isSolid() {
		return code.equals(WALL);
	}
}
