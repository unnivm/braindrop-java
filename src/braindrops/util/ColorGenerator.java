package braindrops.util;

import java.util.Random;

import org.newdawn.slick.Color;

public class ColorGenerator {

	private Random r;

	public ColorGenerator() {
		r = new Random();
	}
	
	public Color getColor() {
		
		Color c = new Color(r.nextInt(170), r.nextInt(110), r.nextInt(256));
		
		while ( !inRange(c) ) {
			seed();
			c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		}
		
		return c;
	}
	
	private void seed() {
		r.setSeed(System.currentTimeMillis() << 2 );
	}
	
	private boolean inRange(Color c) {
		int distance = (int) Math.sqrt( Math.pow(255 - c.getRed(), 2) + Math.pow(255 - c.getGreen(), 2) + Math.pow(255 - c.getBlue(), 2));
		
		return distance > 100;
		
	}
}
