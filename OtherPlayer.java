public class OtherPlayer extends Player {

	private int id;
	
	public OtherPlayer(World world, int x, int y, int id) {
		super(world, x, y);
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
