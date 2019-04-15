import java.awt.Color;
import java.awt.Graphics;

public class Projectile extends Entity {

	private float dx;
	private float dy;
	private float lifeTime;
	
	public Projectile(World world, int x, int y, float angle, float lifeTime, float speed) {
		super(world, x - 5, y - 5, 10, 10);
		this.dx = (float)Math.cos(angle) * speed;
		this.dy = (float)Math.sin(angle) * speed;
		this.lifeTime = lifeTime;
	}
	
	public void update(float dt) {
		x += dx * dt;
		y += dy * dt;
		
		if((lifeTime -= dt) <= 0)
			shouldRemove = true;
		if(x < -width || y < -height || x > world.getWidth() * Tile.WIDTH || y > world.getHeight() * Tile.HEIGHT) 
			shouldRemove = true; 
		if(world.tileAt(((int)x + width / 2) / Tile.WIDTH, ((int)y + height / 2) / Tile.HEIGHT).isSolid())
			shouldRemove = true;
	}

	public void draw(Graphics g, Camera camera) {
		g.setColor(Color.BLUE);
		g.fillOval((int)x - camera.getXOffset(), (int)y - camera.getYOffset(), width, height);
	}
}
