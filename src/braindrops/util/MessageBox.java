package braindrops.util;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

public class MessageBox extends Rectangle {

	private static final long serialVersionUID = 1L;
	private String message;
	private TrueTypeFont msgFont;
	private GameContainer gc;

	public MessageBox(String message, GameContainer gc) {
		super((gc.getWidth() / 2 - gc.getWidth() / 8), (gc.getHeight() / 2 - gc
				.getHeight() / 16) - gc.getHeight() / 16, gc.getWidth() / 4, gc
				.getHeight() / 8);
		this.gc = gc;
		this.message = message;
		msgFont = new TrueTypeFont(new Font("SansSerif", Font.PLAIN, 15), true);
		
	}

	public void show() {
		// TODO: This isn't actually displaying
		GameManager.stop();
		gc.getGraphics().clear();
		msgFont.drawString((gc.getWidth()/2-gc.getWidth()/8),( gc.getHeight()/2-gc.getHeight()/16)-gc.getHeight()/32, message, Color.red);
		gc.getGraphics().setColor(new Color(173.0f, 232.0f, 159.0f, 0.6f));
		gc.getGraphics().fill(this);
		gc.getGraphics().setColor(new Color(173.0f, 232.0f, 159.0f, 1.0f));
		gc.getGraphics().draw(this);
	}
}
