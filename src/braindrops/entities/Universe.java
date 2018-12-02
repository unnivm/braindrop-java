package braindrops.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;

/**
 * 
 * @author Binu
 * 
 * Universe class
 */
public class Universe {
	// *********SETTINGS****************************
	private float mWind = 1.0f;
	private float mGravity = 9.81f;
	private float mDrops = 1000;

	private int mRepaintTimeMS = 8;
	private float mDdropInitialVelocity = 20;
	private GameContainer gc;
	private Graphics g;

	private ArrayList<Rain> rainV;
	private ArrayList<Drop> dropV;

	public Universe(GameContainer gc, Graphics g) {
		rainV = new ArrayList<Rain>();
		dropV = new ArrayList<Drop>();

		this.gc = gc;
		this.g = g;
	}


	public int getHeight() {
		return this.gc.getHeight();
	}

	public int getWidth() {
		return this.gc.getWidth();
	}

	public void checkRainCollision(Rectangle r) {
		Iterator<Rain> iterator = rainV.iterator();

		while (iterator.hasNext()) {
			Rain rain = iterator.next();
			if (r.contains(rain.x, rain.y)
					|| r.contains(rain.prevX, rain.prevY)) {
				iterator.remove();
			}
		}
	}

	public void draw() {

		// DRAW DROPS
		Iterator<Drop> iterator2 = dropV.iterator();
		while (iterator2.hasNext()) {
			Drop drop = iterator2.next();
			drop.update();
			drop.draw(g);

			if (drop.y >= getHeight()) {
				iterator2.remove();
			}
		}

		// DRAW RAIN
		Iterator<Rain> iterator = rainV.iterator();
		while (iterator.hasNext()) {
			Rain rain = iterator.next();
			rain.update();
			rain.draw(g);

			if (rain.y >= (.75 * gc.getHeight())) {

				long dropCount = 1 + Math.round(Math.random() * 4);
				for (int i = 0; i < dropCount; i++) {
					dropV.add(new Drop(rain.x, rain.y, 1));
				}
				iterator.remove();

			}
		}

		// CREATE NEW RAIN
		if (rainV.size() < mDrops) {
			rainV.add(new Rain());
		}
	}

	public void addDropAt(float x, float y, int size) {
		long dropCount = 1 + Math.round(Math.random() * 4);
		for (int i = 0; i < dropCount; i++) {
			dropV.add(new Drop(x, y, size));
		}
	}

	// *****************************************
	class Rain {
		float x;
		float y;
		float prevX;
		float prevY;

		public Rain() {
			Random r = new Random();
			x = r.nextInt(getWidth());
			y = 0;
		}

		public void update() {
			prevX = x;
			prevY = y;

			x += mWind;
			y += mGravity;
		}

		public void draw(Graphics g) {
			g.setColor(new Color(26, 119, 172));
			g.drawLine(x, y, prevX, prevY);
		}
	}

	// *****************************************
	private class Drop {

		float x0;
		float y0;
		float v0; // initial velocity
		float t; // time
		float angle;
		float x;
		float y;
		int size;

		public Drop(float x0, float y0, int size) {
			super();
			this.x0 = x0;
			this.y0 = y0;
			this.size = size;
			v0 = (float) mDdropInitialVelocity;
			angle = (float) Math.toRadians(Math.round(Math.random() * 180));
		}

		private void update() {
			t += mRepaintTimeMS / 100f;
			x = (float) (x0 + v0 * t * Math.cos(angle));
			y = (float) (y0 - (v0 * t * Math.sin(angle) - mGravity * t * t / 2));
		}

		public void draw(Graphics g2) {
			Ellipse circle = new Ellipse(x, y, size, size);
			g2.setColor(new Color(26, 119, 172));
			g2.fill(circle);
		}
	}

}