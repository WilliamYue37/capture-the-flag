import java.awt.Color;
import java.awt.Graphics;

public class Flag extends Entity {
	
	private boolean red;
	
	public Flag(World world, int x, int y, boolean red) {
		super(world, x, y, 20, 20);
		this.red = red;
	}

	public void update(float dt) {
		
	}

	public void draw(Graphics g, Camera camera) {
		g.setColor(Color.GRAY);
		g.fillRect((int)x - camera.getXOffset(), (int)y - camera.getYOffset(), 5, height);
		g.setColor(red ? Color.red : Color.BLUE);
		g.fillRect((int)x + 5 - camera.getXOffset(), (int)y - camera.getYOffset(), width - 5, height - 5);
	}
}
