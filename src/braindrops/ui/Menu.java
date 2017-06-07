package braindrops.ui;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

public class Menu {

	public final String STARTGAME = "START GAME";
	public final String HISCORES = "HISCORES";
	public final String CREDITS = "CREDITS";
	public final String QUIT = "QUIT";
	
	
	private String[] menus = { STARTGAME, HISCORES, CREDITS, QUIT };

	private TrueTypeFont ttf = null;

	private TrueTypeFont bigFont = null;

	private int x;

	private int y;

	private int fontSize = 42;

	private ArrayList<MenuItem> list = new ArrayList<MenuItem>();

	private Rainy rainy;

    private int pos = -1;
    
    private boolean downKey = false;
    
    private boolean upKey = false;
    
	public void init(GameContainer gc) {

		ttf = createFont(fontSize);
		bigFont = createFont(52);

		x = (gc.getWidth() / 2) - 175;
		y = (gc.getHeight() / 2) - 144;

		MenuItem item = new MenuItem();
		Rectangle r = new Rectangle(x, y, 280, 40);
		item.setName(menus[0]);
		item.setFont(ttf);
		item.setRectangle(r);
		list.add(item);
		y += 80;

		item = new MenuItem();
		r = new Rectangle(x + 35, y, 230, 50);
		item.setName(menus[1]);
		item.setFont(ttf);
		item.setRectangle(r);
		list.add(item);
		y += 80;

		item = new MenuItem();
		r = new Rectangle(x + 55, y, 185, 50);
		item.setName(menus[2]);
		item.setFont(ttf);
		item.setRectangle(r);
		list.add(item);
		y += 80;

		item = new MenuItem();
		r = new Rectangle(x + 95, y, 100, 50);
		item.setName(menus[3]);
		item.setFont(ttf);
		item.setRectangle(r);
		list.add(item);

	}

	public void render(GameContainer gc, Graphics g) {
		displayMenu(g);
	}

	/*
	 * Render menu items
	 */
	private void displayMenu(Graphics g) {
		if (g == null)
			return;

		for (MenuItem item : list) {
			g.setFont(item.getFont());
			g.drawString(item.getName(), item.getRectangle().getX(), item
					.getRectangle().getY());
		}
	}

	/*
	 * checks whether a menu item has been selected by the user
	 */
	public void selectMenuItem(float x, float y) {

		Rectangle r;
		int index = 0;

		for (MenuItem item : list) {
			r = item.getRectangle();

			if (r.contains(x, y)) {
				
				updateMenuItem(index, bigFont);
				if( downKey) resetMenuItems(item);
				
				rainy.setMenuItem(item.getName());
			} else {
				if( !downKey && !upKey ) {
					updateMenuItem(index, ttf);
				}
			}

			++index;
		}
		
	}

	/*
	 * registers for a menu selection event
	 */
	public void addMenuListener(Rainy rainy) {
		this.rainy = rainy;
	}
	
	/*
	 * updates selected menu item back to the menu list
	 */
	private void updateMenuItem(MenuItem item, int index) {
		list.set(index, item);
	}

	/*
	 * Creates TrueTypeFont
	 */
	private TrueTypeFont createFont(int size) {
		Font font = new Font("SansSerif", Font.BOLD, size);
		return new TrueTypeFont(font, true);
	}

	
	public void pressUpArrow() {
		
		if( pos <= 0) { 
			updateMenuItem(pos, ttf);
			pos = 4;
		}
		
		pos--;
		rainy.setMenuItem(updateMenuItem(pos, bigFont).getName());
				
		if( pos <= 2 ) {
	    	updateMenuItem(pos + 1, ttf);
	    }
		
	    upKey = true;
	}
	
	public void pressDownArrow() {
		if( pos >= 3) { 
			updateMenuItem(pos, ttf);
			pos = -1;
		}
		
		pos++;
		rainy.setMenuItem(updateMenuItem(pos, bigFont).getName());
	    
	    ///// gets the previous menu item and update to normal font
	    if( pos > 0 ) {
	    	updateMenuItem(pos-1, ttf);
	    }
	    downKey = true;
	}
	
	private MenuItem updateMenuItem( int pos, TrueTypeFont font ) {
    	MenuItem item = list.get(pos);
    	item.setFont(font);
    	updateMenuItem(item, pos);
    	return item;
	}
	
	private void resetMenuItems(MenuItem item) {
		for(int i = 0; i<list.size(); i++) {
			if(! item.getName().equals(list.get(i).getName()) ) {
				updateMenuItem(i, ttf);
			}
		}
	}
	
	public void releaseDownArrow() {
	    downKey = false;
	}
	
	public void releaseUpArrow() {
	    upKey = false;
	}
	
	private class MenuItem {
		private Rectangle rectangle;

		public TrueTypeFont getFont() {
			return font;
		}

		public void setFont(TrueTypeFont font) {
			this.font = font;
		}

		private TrueTypeFont font;

		public Rectangle getRectangle() {
			return rectangle;
		}

		public void setRectangle(Rectangle rectangle) {
			this.rectangle = rectangle;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private String name;
	}
	
}
