
public class Camera {

	private int width;
	private int height;
	
	private int xOffset;
	private int yOffset;
	
	public Camera(int width, int height) {
		this.width = width * Tile.WIDTH;
		this.height = height * Tile.HEIGHT;
	}
	
	public boolean playerOnScreen(Player player) {
		if(player.getX() + 32 < xOffset) return false;
		if(player.getY() + 32 < yOffset) return false;
		if(player.getX() > xOffset + Client.WIDTH) return false;
		if(player.getY() > yOffset + Client.HEIGHT) return false;
		return true;
	}
	
	public void centerOn(int x, int y) {
		xOffset = x - Client.WIDTH / 2;
		yOffset = y - Client.HEIGHT / 2;
		if(xOffset < 0) xOffset = 0;
		if(yOffset < 0) yOffset = 0;
		if(xOffset > width - Client.WIDTH) xOffset = width - Client.WIDTH;
		if(yOffset > height - Client.HEIGHT) yOffset = height - Client.HEIGHT;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
}
