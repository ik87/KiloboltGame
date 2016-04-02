package kiloboltgame;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Robot {

	final int JUMPSPEED = -15; // set the speedY when clicked jump
	final int MOVESPEED = 5; // set the speedX when clicked row left or right

	private int centerX = 100; // center X UP corner
	private int centerY = 377;
	private boolean jumped = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean ducked = false;
	private boolean readyToFire = true;
	
	private static Background bg1;
	private static Background bg2;

	private int speedX = 0;
	private int speedY = 0;

	public static Rectangle rect;
	public static Rectangle rect2;
	public static Rectangle rect3; // left hand
	public static Rectangle rect4; // right hand
	public static Rectangle yellowRed;
	public static Rectangle footleft;
	public static Rectangle fooright;
	

	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	public void update() {

		if (speedX < 0) {
			centerX += speedX;
		}
		if (speedX <= 0) {
			bg1.setSpeedX(0);
			bg2.setSpeedX(0);
		}
		if (centerX <= 200 && speedX > 0) {
			centerX += speedX;
		}
		if (speedX > 0 && centerX > 200) {
			bg1.setSpeedX(-MOVESPEED / 5);
			bg2.setSpeedX(-MOVESPEED / 5);
		}

		// Updates Y position

		centerY += speedY; // gravity

		speedY += 1;

		if (speedY > 3) {
			jumped = true;
		}

		if (centerX + speedX < 60) {
			centerX = 61;
		}
		// collision object
		rect.setRect(centerX - 34, centerY - 63, 68, 63); // body top
		rect2.setRect(rect.getX(), rect.getY() + 63, 68, 64); // body bottom
		rect3.setRect(rect.getX() - 26, rect.getY() + 32, 26, 20); // left hand
		rect4.setRect(rect.getX() + 68, rect.getY() + 32, 26, 20); // right hand
		footleft.setRect(centerX - 50, centerY + 20, 50, 15); // left foot
		fooright.setRect(centerX, centerY + 20, 50, 15); // right foot

		yellowRed.setRect(centerX - 110, centerY - 110, 180, 180); // the rect
																	// pre
																	// detect
																	// collision
																	// (25
																	// tiles)

	}

	public void moveRight() {
		if (ducked == false)
			speedX = MOVESPEED;
	}

	public void moveLeft() {
		if (ducked == false)
			speedX = -MOVESPEED;
	}

	public void stopRight() {
		setMovingRight(false);
		stop();

	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (isMovingRight() == false && isMovingLeft() == false) {
			speedX = 0;
		}

		if (isMovingRight() == false && isMovingLeft() == true) {
			moveLeft();
		}

		if (isMovingRight() == true && isMovingLeft() == false) {
			moveRight();
		}
	}

	public void jump() {
		if (jumped == false) {
			speedY = JUMPSPEED;
			jumped = true;
		}

	}

	public void shoot() {
		if (readyToFire) {
			Projectile p = new Projectile(centerX + 50, centerY - 25);
			projectiles.add(p);
		}
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public boolean isJumped() {
		return jumped;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isDucked() {
		return ducked;
	}

	public void setDucked(boolean ducked) {
		this.ducked = ducked;
	}

	public ArrayList<Projectile> getProjectile() {
		return projectiles;
	}

	public boolean isReadyToFire() {
		return readyToFire;
	}

	public void setReadyToFire(boolean readyToFire) {
		this.readyToFire = readyToFire;
	}

	public static Robot getNull() {

		bg1 = null;
		bg2 = null;

		rect = null;
		rect2 = null;
		rect3 = null;
		rect4 = null;
		yellowRed = null;
		footleft = null;
		fooright = null;

		return null;

	}

	public static void init() {
		bg1 = StartingClass.getBg1();
		bg2 = StartingClass.getBg2();
		rect = new Rectangle(0, 0, 0, 0);
		rect2 = new Rectangle(0, 0, 0, 0);
		rect3 = new Rectangle(0, 0, 0, 0); // left hand
		rect4 = new Rectangle(0, 0, 0, 0); // right hand
		yellowRed = new Rectangle(0, 0, 0, 0);
		footleft = new Rectangle(0, 0, 0, 0);
		fooright = new Rectangle(0, 0, 0, 0);
	}

}
