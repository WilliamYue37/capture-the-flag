import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

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
	
	private static Random rand = new Random(2019);
	private void fireShotgun() {
		final int shells = 6;
		for(int i = 0; i < shells; i++) {
			float error = rand.nextFloat() * 0.4f - 0.2f;
			float speed = rand.nextFloat() * 100 + 400;
			world.addProjectile(new Projectile(world, getCenterX(), getCenterY(), fireAngle + error, 0.3f, speed));
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
			shotType = SHOTGUN;
			break;
		case KeyEvent.VK_O :
			fireSniper();
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
	
	private static final int NO_SHOT = 0;
	private static final int SHOTGUN = 1;
	private static final int SNIPER = 2;
	private int shotType;
	public int getShotType() {
		int s = shotType;
		shotType = NO_SHOT;
		return s;
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
}
