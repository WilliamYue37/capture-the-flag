import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends Entity {
	
	private static final float SPEED = 200;

	private static final int MAX_HEALTH = 15;
	private int health = MAX_HEALTH;
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;

	private float fireAngle;
	
	public Player(World world, int x, int y) {
		super(world, x, y, 32, 32);
	}
	
	public void update(float dt) {
		if((up || down || left || right) && !(up && down && left && right)) {
			if(right && !left) {
				if(up && !down) {
					fireAngle = -(float)Math.PI / 4;
				} else if(down && !up) {
					fireAngle = (float)Math.PI / 4;
				} else {
					fireAngle = 0;
				}
			} else if(left && !right) {
				if(up && !down) {
					fireAngle = -(float)Math.PI * 3 / 4;
				} else if(down && !up) {
					fireAngle = (float)Math.PI * 3 / 4;
				} else {
					fireAngle = (float)Math.PI;
				}
			} else if(up && !down) {
				fireAngle = -(float)Math.PI / 2;
			} else if(down && !up) {
				fireAngle = (float)Math.PI / 2;
			}
		}
		
		if(up && !down && canMove(x, y - SPEED * dt) && canMove(x + width, y - SPEED * dt)) {
			y -= SPEED * dt;
		} else if(down && !up && canMove(x, y + height + SPEED * dt) && canMove(x + width, y + height + SPEED * dt)) {
			y += SPEED * dt;
		}
		
		if(left && !right && canMove(x - SPEED * dt, y) && canMove(x - SPEED * dt, y + height)) {
			x -= SPEED * dt;
		} else if(right && !left && canMove(x + width + SPEED * dt, y) && canMove(x + width + SPEED * dt, y + height)) {
			x += SPEED * dt;
		}
	}
	
	private boolean canMove(float x, float y) {
		int tileX = (int)x / Tile.WIDTH;
		int tileY = (int)y / Tile.HEIGHT;
		return !world.tileAt(tileX, tileY).isSolid();
	}
	
	public void draw(Graphics g, Camera camera) {
		g.setColor(Color.RED);
		g.fillRect((int)x - camera.getXOffset(), (int)y - camera.getYOffset(), width, height);
	}
	
	public void drawHealthBar(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(20, Client.HEIGHT - 30, MAX_HEALTH * 5, 10);
		g.setColor(Color.RED);
		g.fillRect(20, Client.HEIGHT - 30, health * 5, 10);
	}
	
	public void keyPressed(int k) {
		switch(k) {
		case KeyEvent.VK_UP :
			up = true;
			break;
		case KeyEvent.VK_DOWN :
			down = true;
			break;
		case KeyEvent.VK_LEFT :
			left = true;
			break;
		case KeyEvent.VK_RIGHT :
			right = true;
			break;
		case KeyEvent.VK_P :
			shotType = SHOTGUN;
			break;
		case KeyEvent.VK_O :
			shotType = SNIPER;
			break;
		}
	}
	
	public void keyReleased(int k) {
		switch(k) {
		case KeyEvent.VK_UP :
			up = false;
			break;
		case KeyEvent.VK_DOWN :
			down = false;
			break;
		case KeyEvent.VK_LEFT :
			left = false;
			break;
		case KeyEvent.VK_RIGHT :
			right = false;
			break;
		}
	}
	
	public static final int NO_SHOT = 0;
	public static final int SHOTGUN = 1;
	public static final int SNIPER = 2;
	private int shotType;
	public int getShotType() {
		int s = shotType;
		shotType = NO_SHOT;
		return s;
	}
	
	public void hurt(int damage) {
		health -= damage;
	}
	
	public int getFireAngle() {
		int fireAngle = (int)(this.fireAngle * 100);
		return fireAngle;
	}
	
	public int getCenterX() {
		return (int)x + width / 2;
	}
	
	public int getCenterY() {
		return (int)y + height / 2;
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
}
