package braindrops.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;

import braindrops.entities.BigDrop;
import braindrops.entities.Universe;
import braindrops.util.FontLoader;
import braindrops.util.GameManager;
import braindrops.util.JukeBox;
import braindrops.util.Landscape;

public class Rainy extends BasicGame implements InputProviderListener,
		MenuListener {

	private GameManager gm;

	private Command escape = new BasicCommand("escape");
	/** The input provider abstracting input */
	private InputProvider provider;
	private Image scape = null;
	private Image newScape = null;

	private ArrayList<BigDrop> bigDrops;
	private ArrayList<BigDrop> removeList;
	private int bigDropsOnScreen = 0;
	private Universe universe;
	private GameContainer gc;
	private Image watermark;
	private Image cursor;

	private Menu menu;
	private boolean startGame = false;
	private String itmSelected = null;
	private boolean showMenu = true;
	private int counter = 0;
	private Landscape landscape;

	public Rainy() {
		super("");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		this.gc = gc;
		prepareGame();
		setProperties();

	}

	public void removeDrop(BigDrop drop) {
		removeList.add(drop);
	}

	private void prepareGame() throws SlickException {
		gm = new GameManager();

		prepareGraphics();
		prepareMenu(gc);
		prepareInputs();
		prepareMusic();
		prepareLevel();
	}

	private void prepareInputs() {
		provider = new InputProvider(gc.getInput());
		provider.addListener(this);
		provider.bindCommand(new KeyControl(Input.KEY_ESCAPE), escape);
	}

	private synchronized void prepareMusic() throws SlickException {

		JukeBox jukebox = new JukeBox();
		jukebox.playMusic();
	}

	private void prepareGraphics() throws SlickException {

		universe = new Universe(gc, gc.getGraphics());
		watermark = new Image("resources/hhi-logo.png");
		cursor = new Image("resources/bucket.png");
		landscape = new Landscape(gc.getWidth(), gc.getHeight());
		FontLoader.loadScoreFont();
		FontLoader.loadWordFont();

	}

	private void prepareMenu(GameContainer gc) {
		menu = new Menu();
		menu.init(gc);
		menu.addMenuListener(this);
	}

	private void setProperties() {
		gc.setMaximumLogicUpdateInterval(60);
		gc.setTargetFrameRate(60);
		gc.setAlwaysRender(true);
		gc.setShowFPS(false);
		gc.setVSync(true);
		gc.setForceExit(true);
		gc.setMouseGrabbed(true);
	}

	private synchronized void fade() {

		if (newScape == null) {
			scape.draw(0, 0);
			return;
		}

		if (scape == null) {
			newScape.setAlpha(1f);
			scape = newScape;
			scape.draw(0, 0);
			return;
		}

		if (scape.getAlpha() <= 0f) {
			scape = newScape;
			newScape = null;
			scape.draw(0, 0);
		} else {
			scape.setAlpha(scape.getAlpha() - 0.01f);
			newScape.setAlpha(newScape.getAlpha() + 0.01f);
			scape.draw(0, 0);
			newScape.draw(0, 0);
		}
	}

	private synchronized void prepareLevel() throws SlickException {

		if (bigDrops != null) {
			Iterator<BigDrop> bigDropIterator = bigDrops.iterator();

			while (bigDropIterator.hasNext()) {
				BigDrop drop = bigDropIterator.next();
				universe.addDropAt(drop.getX(), drop.getY(), 30);
				removeDrop(drop);
			}
		} else {
			bigDrops = new ArrayList<BigDrop>();
		}

		removeList = new ArrayList<BigDrop>();

		try {

			newScape = landscape.getLandscape(gm.getLevel());
			newScape.setAlpha(0f);
		} catch (Exception e) {
			gm.setMaxPoint();
			return;
		}

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

		if (!startGame)
			return;
		Iterator<BigDrop> bigDropIterator = bigDrops.iterator();

		ArrayList<BigDrop> tmp = new ArrayList<BigDrop>();
		tmp.addAll(bigDrops);

		while (bigDropIterator.hasNext()) {
			BigDrop drop = bigDropIterator.next();
			drop.update();

			if (drop.getReverse() == false
					&& drop.getY() > gc.getHeight()
							- drop.getBoundingBox().getHeight()) {

				if (drop.isPositive()) {
					gm.processEvent(GameManager.EVT_LOST_DROP);
				}

				universe.addDropAt(drop.getX(), drop.getY(), 30);

				bigDropsOnScreen--;
				removeDrop(drop);

				playDropPositive();

			}

			if (drop.getReverse() && drop.getY() < 0) {
				universe.addDropAt(drop.getX(), drop.getY() + 5, 30);
				removeDrop(drop);
				bigDropsOnScreen--;
			}

			if (drop.getBoundingBox().intersects(
					new Rectangle(gc.getInput().getAbsoluteMouseX(), gc
							.getInput().getAbsoluteMouseY(), cursor.getWidth(),
							cursor.getHeight()))) {
				if (drop.isPositive()) {

					processGameCode(gm
							.processEvent(GameManager.EVT_CORRECT_DROP));

					playDropPositive();

				} else {

					processGameCode(gm.processEvent(GameManager.EVT_WRONG_DROP));

					playDropNegative();

				}

				bigDropsOnScreen--;
				removeDrop(drop);
			}

		}

		if (bigDropsOnScreen < GameManager.MAX_BIG_DROPS) {
			addBigDrop(gc, false);
		}

		int streak = gm.getStreak();
		if (streak > 0) {
			addBigDrop(gc, true);
			gm.setStreak(0);
		}

		bigDrops.removeAll(removeList);
		removeList.clear();
	}

	public void controlPressed(Command command) {
		if (command.equals(escape)) {
			// gc.exit();
			// System.exit(0);

			if (!startGame && showMenu) {
				gc.exit();
				System.exit(0);
			}

			startGame = false;
			showMenu = true;

		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

		if (!GameManager.isRunning())
			return;

		fade();

		if (showMenu)
			renderMenu(gc, g);

		if (startGame) {
			universe.draw();
			watermark.setAlpha(0.6f);
			watermark.draw(gc.getWidth() - watermark.getWidth(), 0);
			g.setColor(new Color(188.0f, 227.0f, 229.0f, 0.3f));

			for (BigDrop drop : bigDrops) {
				drop.draw(g);
			}
		}

		float mouseX = gc.getInput().getAbsoluteMouseX();
		float mouseY = gc.getInput().getAbsoluteMouseY();

		displayCursor(mouseX, mouseY);

		if (startGame) {
			universe.checkRainCollision(new Rectangle(mouseX, mouseY, cursor
					.getWidth(), cursor.getHeight()));
			int rainCollectorCorner = 10;
			float rainCollectorWidth = 500f;
			float rainCollectorHeight = 50;
			float rainCollectorX = (gc.getWidth() - rainCollectorWidth) / 2;
			float rainCollectorY = gc.getHeight() - 100;
			float rainCollectorFilled = ((float) gm.getScore() / gm
					.getLevelMax()) * 500;

			g.setColor(new Color(26, 119, 172));

			RoundedRectangle rainCollector = new RoundedRectangle(
					rainCollectorX, rainCollectorY, rainCollectorWidth,
					rainCollectorHeight, rainCollectorCorner);

			g.draw(rainCollector);
			g.fillRoundRect(rainCollectorX, rainCollectorY,
					rainCollectorFilled, rainCollectorHeight,
					rainCollectorCorner);
			g.setColor(Color.white);

			String scoreString = gm.getScore() + "/" + gm.getLevelMax();
			FontLoader.getScoreFont().drawString(
					rainCollectorX
							+ ((rainCollectorWidth - FontLoader.getScoreFont()
									.getWidth(scoreString)) / 2),
					gc.getHeight() - 90, scoreString);
		}
	}

	@Override
	public void controlReleased(Command arg0) {
		// TODO Auto-generated method stub

	}

	private void playDropPositive() throws SlickException {

		Sound snd = null;
		snd = new Sound("resources/drop.wav");
		snd.play();

	}

	private void playDropNegative() throws SlickException {
		Sound snd = null;
		snd = new Sound("resources/negative.wav");
		snd.play();
	}

	private void processGameCode(int code) throws SlickException {
		if (code == 0) {
			return;
		}

		// process any non-zero game manager codes here

		if (code == GameManager.GEVT_LEVEL_UP) {
			prepareLevel();
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {

		if (itmSelected != null) {
			if (menu.STARTGAME.equals(itmSelected)) {
				startGame = true;
			} else if (menu.QUIT.equals(itmSelected)) {
				GameManager.stop();
			}
		}

		if (!GameManager.isRunning()) {
			System.exit(0);
		}
	}

	private void renderMenu(GameContainer gc, Graphics g) {
		if (startGame)
			return;
		menu.render(gc, g);
		menu.selectMenuItem(gc.getInput().getAbsoluteMouseX(), gc.getInput()
				.getAbsoluteMouseY());
	}

	private void displayCursor(float mouseX, float mouseY) {
		cursor.draw(mouseX, mouseY);
	}

	@Override
	public void setMenuItem(String menu) {
		itmSelected = menu;
	}

	/*
	 * @GameContainer gc
	 * 
	 * @return boolean
	 * 
	 * adds a big rain drop to rain drop basket without overlapping each other
	 */
	private boolean addBigDrop(GameContainer gc, boolean reverse) {
		boolean stat = false;

		if (bigDrops.size() == 0) {
			bigDrops.add(new BigDrop(gc, gm.getDropSpeed(), false));
			bigDropsOnScreen++;
			return true;
		}

		stat = createBigDrop(gc, reverse);
		return stat;
	}

	private boolean createBigDrop(GameContainer gc, final boolean reverse) {
		boolean stat = false;
		BigDrop drop = null;
		drop = new BigDrop(gc, gm.getDropSpeed(), reverse);
		for (BigDrop bd : bigDrops) {
			if (bd.getBoundingBox().contains(drop.getX(), drop.getY())
					|| bd.getBoundingBox().intersects(drop.getBoundingBox())) {
				stat = false;
				break;
			}
			stat = true;
		}

		if (stat) {
			bigDrops.add(drop);
			bigDropsOnScreen++;
		}

		return stat;
	}

	@Override
	public void keyPressed(int key, char c) {

		switch (key) {

		case 28:
			if (itmSelected != null) {
				if (menu.STARTGAME.equals(itmSelected)) {
					startGame = true;
					showMenu = false;
				} else if (menu.QUIT.equals(itmSelected)) {
					GameManager.stop();
					if (!GameManager.isRunning()) {
						System.exit(0);
					}
				}
			}
			break;

		case 208:
			menu.pressDownArrow();
			break;

		case 200:
			menu.pressUpArrow();
			break;

		default:
			break;
		}
	}

}
