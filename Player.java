import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player extends Entity {
	
	private static final float SPEED = 200;

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
	
	private void fireShotgun() {
		for(int i = 0; i < 10; i++) {
			float error = (float)Math.random() * 0.4f - 0.2f;
			float speed = (float)Math.random() * 100 + 400;
			world.addProjectile(new Projectile(world, getCenterX(), getCenterY(), fireAngle + error, 0.5f, speed));
		}
	}
	
	private void fireSniper() {
		world.addProjectile(new Projectile(world, getCenterX(), getCenterY(), fireAngle, 4, 1000));
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
			fireShotgun();
			break;
		case KeyEvent.VK_O :
			fireSniper();
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
	
	public float getFireAngle() {
		return fireAngle;
	}
	
	public int getCenterX() {
		return (int)x + width / 2;
	}
	
	public int getCenterY() {
		return (int)y + height / 2;
	}
}
