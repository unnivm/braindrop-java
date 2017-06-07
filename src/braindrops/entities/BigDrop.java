package braindrops.entities;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import braindrops.util.ColorGenerator;
import braindrops.util.FontLoader;
import braindrops.util.MessageBox;

public class BigDrop {

	private Image dropImage = null;
	private float x;
	private float y;
	private Word word;
	private float speed;
	private Color color;

	private boolean reverse = false;

	public BigDrop(GameContainer gc, float speed, boolean reverse) {

		try {
			dropImage = new Image("resources/rain.png");
			int minX = 10;
			int maxX = gc.getWidth() - dropImage.getWidth();
			this.x = (float) (Math.random() * maxX + minX);

			this.reverse = reverse;
			// this.y = -2 * dropImage.getHeight();
			flipYAxis(gc.getHeight());

			this.speed = speed;

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			MessageBox msgBox = new MessageBox(
					"Error: Couldn't load resources/rain.png", gc);
			msgBox.show();
		}

		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		if ( r.nextInt(101) > 65) {
			color = new ColorGenerator().getColor();
		} else {
			color = new Color(102, 171, 249);
		}
		
		word = new Word();

	}

	public void createDrop() {

	}

	public void update() {
		if (!reverse)
			y += speed;
		else
			y -= speed;
	}

	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, dropImage.getWidth(), dropImage.getHeight());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isPositive() {
		return word.isPositive();
	}

	public double[] getCenter(String word, Font font) {
		int wordWidth = font.getWidth(word);

		double[] coords = { x + (dropImage.getWidth() - wordWidth) / 2,
				y + (dropImage.getHeight() / 2) };

		return coords;

	}

	public void draw(Graphics g) {

		dropImage.draw(x, y, color);
		g.setColor(Color.white);
		double[] center = getCenter(word.getWord(), g.getFont());

		FontLoader.getWordFont().drawString((float) center[0],
				(float) center[1], word.getWord());

	}

	public boolean collided(float mouseX, float mouseY) {
		int diffX = (int) (mouseX - x);
		int diffY = (int) (mouseY - y);

		if (dropImage.getColor(diffX, diffY).getAlpha() < 255) {
			return true;
		}

		return false;

	}

	public void flipYAxis(float h) {
		if (reverse) {
			this.y = 3 * dropImage.getHeight();
		} else {
			this.y = -2 * dropImage.getHeight();
		}
	}

	public void setReverse(final boolean reverse) {
		this.reverse = reverse;
	}

	public boolean getReverse() {
		return reverse;
	}

}
