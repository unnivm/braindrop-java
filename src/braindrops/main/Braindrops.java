package braindrops.main;

import java.util.logging.*;

import org.newdawn.slick.*;
import org.newdawn.slick.command.*;
import org.newdawn.slick.geom.*;

import braindrops.entities.*;
import braindrops.ui.*;
import braindrops.util.*;

/**
 * 
 * @author Unni.Mana.
 *
 */
public class Braindrops {
	
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Rainy());
			app.setDisplayMode(1024, 768, true);
			app.setTargetFrameRate(125);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException ex) {
			Logger.getLogger(Rainy.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
