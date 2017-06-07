package braindrops.util;

import java.io.File;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Landscape {

	private Image currentLandscape;
	private Image nextLandscape;
	private Image[] landscapes;

	public Landscape(int preferredWidth, int preferredHeight) {
		
		landscapes = new Image[10];
		
		for (int i = 1;; i++) {
			try {
				landscapes[i - 1] = new Image("resources/level" + i + ".jpg").getScaledCopy(preferredWidth, preferredHeight);
			} catch ( Exception e ) {
				break;
			}	
		}
	}

	public Image getLandscape(int level) {
		if (level > landscapes.length) {
			return null;
		}

		return landscapes[level - 1];
	}
}
