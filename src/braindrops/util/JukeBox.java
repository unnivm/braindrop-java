package braindrops.util;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class JukeBox {

	private Music[] music;
	private Music nowPlaying;
	private int songIndex;
	private AtomicBoolean running = new AtomicBoolean(true);

	public JukeBox() throws SlickException {
		Music[] music = {
				new Music("resources/CosmicBliss.ogg"),
				new Music("resources/Quest.ogg"),
				new Music("resources/WiseMoment.ogg"),
				new Music("resources/EuphoricDay.ogg")};

		this.music = music;
	}

	public synchronized void playMusic() {
		new Thread(new Runnable() {
			public void run() {
				while (running.get()) {

					if (nowPlaying == null || !nowPlaying.playing()) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Random rand = new Random();
						songIndex = rand.nextInt(music.length);

						nowPlaying = music[songIndex];
						nowPlaying.setPosition(0);
						nowPlaying.play();
					}
				}
			}
		}).start();
	}

	public void stopMusic() {
		running = new AtomicBoolean(false);
	}
}