import java.awt.Graphics;

public abstract class Entity {

	protected float x;
	protected float y;
	protected int width;
	protected int height;
	protected World world;
	
	protected boolean shouldRemove = false;
	
	public Entity(World world, int x, int y, int width, int height) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void update(float dt);
	public abstract void draw(Graphics g, Camera camera);
	
	public boolean shouldRemove() {
		return shouldRemove;
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
}
