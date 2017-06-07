package braindrops.util;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class FontLoader {

	private static TrueTypeFont scoreFont;
	private static TrueTypeFont wordFont;
	
	public static TrueTypeFont loadFont(String fontName, float size) {
		Font awtFont = new Font("SansSerif", Font.PLAIN, (int)size);
		TrueTypeFont font = new TrueTypeFont(awtFont, false);
		
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream(fontName);
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(size);
			font = new TrueTypeFont(awtFont2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return font;
	}
	
	public static TrueTypeFont getScoreFont() {
		if ( scoreFont == null ) {
			loadScoreFont();
		}
		
		return scoreFont;
	}
	
	public static TrueTypeFont getWordFont() {
		if ( wordFont == null ) {
			loadWordFont();
		}
		
		return wordFont;
	}
	
	public static void loadScoreFont() {
		scoreFont = new TrueTypeFont(new Font("SansSerif", Font.PLAIN, 18), true);
	}
	
	public static void loadWordFont() {
		wordFont = new TrueTypeFont(new Font("SansSerif", Font.PLAIN, 18), true);
	}
}
