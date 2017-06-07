package braindrops.util;

import java.util.Random;


public class GameManager {

	public static int EVT_LOST_DROP = 1;
	public static int EVT_WRONG_DROP = 2;
	public static int EVT_CORRECT_DROP = 3;

	public static int GEVT_LEVEL_UP = 4;

	public static final float MIN_DROP_SPEED = 1.5f;
	public static int MAX_BIG_DROPS;
	private int score = 0;
	private int level = 1;
	private float dropSpeed = MIN_DROP_SPEED;
	private int levelMax = 114;

	private static boolean isRunning;

	private int STREAK = 0;
	
	public GameManager() {
		MAX_BIG_DROPS = 4;
		isRunning = true;

	}

	public int processEvent(int eventCode) {

		if (eventCode == EVT_LOST_DROP) {
			score--;
			dropSpeed -= 0.1f;
			STREAK = 0;
		} else if (eventCode == EVT_CORRECT_DROP) {
			score++;
			dropSpeed += (level * 0.1f);
			STREAK++;
		} else if (eventCode == EVT_WRONG_DROP) {
			score -= 2;
			dropSpeed -= (level * 0.4f);
			STREAK = 0;
		}
		
		return normalizeScore();
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public float getDropSpeed() {
		return dropSpeed;
	}

	public int getLevelMax() {
		return 112 * level + 2;
	}

	private int normalizeScore() {
		if (score < 0)
			score = 0;
		if (dropSpeed < MIN_DROP_SPEED)
			dropSpeed = MIN_DROP_SPEED;
		if (dropSpeed > 5.9f)
			dropSpeed = 5.9f;

		if (score > levelMax) {
			score = 0;
			level++;

			return GEVT_LEVEL_UP;
		}

		return 0;

	}

	public static void stop() {
		isRunning = false;
	}

	public static boolean isRunning() {
		return isRunning;
	}

	public int getStreak() {
		return calculateStreak();
	}
	
	public void setStreak(int streak) {
		this.STREAK = streak;
	}
	
	private int calculateStreak() {
		if( STREAK < 10 ) return 0;
		Random r = null;
		 if ( STREAK >= 10 ) {
	         r = new Random();
	        
	    }
	 return  r.nextInt(STREAK) + 1;	
	}

	public void setMaxPoint() {
		levelMax = Integer.MAX_VALUE;
	}
}
