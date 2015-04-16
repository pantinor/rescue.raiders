package rescue.raiders.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class RescueRaiders extends Game {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.APP_WIDTH;
		config.height = Constants.APP_HEIGHT;
		new LwjglApplication(new RescueRaiders(), config);
	}

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

}