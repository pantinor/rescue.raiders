package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import static rescue.raiders.util.Main.*;

public class Tank extends Movable {

	public Tank(String name, boolean enemy) {
		super(name, AtlasCache.get("tank"), 0.10f, 1f, true);
		setPosition(SPAWN, FIELD_HEIGHT);

		if (enemy) {
			addAction(Actions.moveTo(0, FIELD_HEIGHT, 250f));
		} else {
			addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 250f));
		}

		
		this.setUserObject(createMiniIcon(Color.YELLOW, 6,6));

	}
	
	class TankAction implements Runnable {
		boolean active = true;
		public void run() {

		}
	}


}
