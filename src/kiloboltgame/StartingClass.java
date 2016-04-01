package kiloboltgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import kiloboltgame.framework.Animation;

public class StartingClass extends Applet implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum GameState {
		Runing, Dead
	}
	
	GameState state = GameState.Runing;

	private static Robot robot;
	public static Heliboy hb, hb2;
	private Image image, character, character2, character3, background, currentSprite, characterDown, characterJump,
			heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;
	private Animation anim, hanim;
	static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight;
	public static int score = 0;
	private Font font = new Font(null, Font.BOLD, 30);
	private ArrayList<Tile> tileArray = new ArrayList<Tile>();

	@Override
	public void init() {

		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Q-Bot Alpha");
		try {
			base = getCodeBase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		character = getImage(base, "data/character.png");
		character2 = getImage(base, "data/character2.png");
		character3 = getImage(base, "data/character3.png");

		characterDown = getImage(base, "data/down.png");
		characterJump = getImage(base, "data/jumped.png");

		heliboy = getImage(base, "data/heliboy.png");
		heliboy2 = getImage(base, "data/heliboy2.png");
		heliboy3 = getImage(base, "data/heliboy3.png");
		heliboy4 = getImage(base, "data/heliboy4.png");
		heliboy5 = getImage(base, "data/heliboy5.png");

		background = getImage(base, "data/background.png");

		tiledirt = getImage(base, "data/tiledirt.png");
		tilegrassBot = getImage(base, "data/tilegrassbot.png");
		tilegrassTop = getImage(base, "data/tilegrasstop.png");
		tilegrassLeft = getImage(base, "data/tilegrassleft.png");
		tilegrassRight = getImage(base, "data/tilegrassright.png");

		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);

		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);

		currentSprite = anim.getImage();

	}

	@Override
	public void start() {

		bg1 = new Background(0, 0); // set position background 1
		bg2 = new Background(2160, 0); // set position background 2
		robot = new Robot();

		try {
			loadMap("data/map1.txt"); // load file map.txt into buffer
										// "tileArray"
		} catch (IOException e) {
			e.printStackTrace();
		}

		hb = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void run() {
		while (true) {
			robot.update();

			if (robot.isJumped()) {
				currentSprite = characterJump;
			} else if (robot.isJumped() == false && robot.isDucked() == false) {
				currentSprite = anim.getImage();
			}

			ArrayList<Projectile> projectiles = robot.getProjectile();
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile p = projectiles.get(i);
				if (p.isVisible() == true) {
					p.Update();
				} else {
					projectiles.remove(i);
				}
			}
			updateTiles();
			hb.Update();
			hb2.Update();
			bg1.update();
			bg2.update();
			animate();
			repaint();

			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadMap(String filename) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;

		BufferedReader reader = new BufferedReader(new FileReader(filename));

		while (true) {
			String line = reader.readLine();

			if (line == null)
				break;

			if (!line.startsWith("!")) {
				lines.add(line);
				width = width > line.length() ? width : line.length();
			}
		}

		height = lines.size();

		for (int j = 0; j < height; j++) {
			String line = lines.get(j);

			for (int i = 0; i < line.length(); i++) {
				System.out.println(i + "is i");

				if (i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, Character.getNumericValue(ch));
					tileArray.add(t);
				}
			}
		}
		reader.close();
	}

	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
		paintTiles(g);

		ArrayList<Projectile> projectiles = robot.getProjectile();
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = projectiles.get(i);
			g.setColor(Color.YELLOW);
			g.fillRect(p.getX(), p.getY(), 10, 5);
		}

		/*
		 * g.drawRect((int) Robot.rect.getX(), (int) Robot.rect.getY(), (int)
		 * Robot.rect.getWidth(), (int) Robot.rect.getHeight());
		 * g.drawRect((int) Robot.rect2.getX(), (int) Robot.rect2.getY(), (int)
		 * Robot.rect2.getWidth(), (int) Robot.rect2.getHeight());
		 * g.drawRect((int) Robot.yellowRed.getX(), (int)
		 * Robot.yellowRed.getY(), (int) Robot.yellowRed.getWidth(), (int)
		 * Robot.yellowRed.getHeight()); g.drawRect((int) Robot.rect3.getX(),
		 * (int) Robot.rect3.getY(), (int) Robot.rect3.getWidth(), (int)
		 * Robot.rect3.getHeight()); g.drawRect((int) Robot.rect4.getX(), (int)
		 * Robot.rect4.getY(), (int) Robot.rect4.getWidth(), (int)
		 * Robot.rect4.getHeight()); g.drawRect((int) Robot.footleft.getX(),
		 * (int) Robot.footleft.getY(), (int) Robot.footleft.getWidth(), (int)
		 * Robot.footleft.getHeight()); g.drawRect((int) Robot.fooright.getX(),
		 * (int) Robot.fooright.getY(), (int) Robot.fooright.getWidth(), (int)
		 * Robot.fooright.getHeight());
		 */

		g.drawImage(currentSprite, robot.getCenterX() - 61, robot.getCenterY() - 63, this);

		g.drawImage(hanim.getImage(), hb.getCenterX() - 48, hb.getCenterY() - 48, this);
		g.drawImage(hanim.getImage(), hb2.getCenterX() - 48, hb2.getCenterY() - 48, this);

		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(score), 740, 30);

	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Move up");
			break;

		case KeyEvent.VK_DOWN:
			currentSprite = characterDown;
			if (robot.isJumped() == false) {
				robot.setDucked(true);
				robot.setSpeedX(0);
			}
			break;

		case KeyEvent.VK_LEFT:
			robot.moveLeft();
			robot.setMovingLeft(true);
			break;

		case KeyEvent.VK_RIGHT:
			robot.moveRight();
			robot.setMovingRight(true);
			break;

		case KeyEvent.VK_SPACE:
			robot.jump();
			break;

		case KeyEvent.VK_CONTROL:
			if (robot.isJumped() == false && robot.isDucked() == false) {
				robot.shoot();
				robot.setReadyToFire(false);
			}
			break;

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Stop moving up");
			break;

		case KeyEvent.VK_DOWN:
			currentSprite = anim.getImage();
			robot.setDucked(false);
			break;

		case KeyEvent.VK_LEFT:
			robot.stopLeft();
			break;

		case KeyEvent.VK_RIGHT:
			robot.stopRight();
			break;

		case KeyEvent.VK_SPACE:
			break;
		case KeyEvent.VK_CONTROL:
			robot.setReadyToFire(true);
			break;

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static Background getBg1() {
		return bg1;
	}

	public static Background getBg2() {
		return bg2;
	}

	public static Robot getRobot() {
		return robot;
	}

	private void animate() {
		anim.update(10);
		hanim.update(50);
	}

	private void updateTiles() {
		int f = tileArray.size();
		for (int i = 0; i < f; i++) {
			Tile t = tileArray.get(i);
			t.update();
		}
	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tileArray.size(); i++) {
			Tile t = (Tile) tileArray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
	}

}
