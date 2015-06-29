package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import static rescue.raiders.game.RescueRaiders.*;

public class Tank extends Actor {

    public Tank(ActorType t) {
        super(t, AtlasCache.get(t.getName()), 0.10f, 1f, true);
        setPosition(SPAWN, FIELD_HEIGHT);

        if (t.isIsEnemy()) {
            addAction(Actions.moveTo(0, FIELD_HEIGHT, 250f));
        } else {
            addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 250f));
        }

        this.setUserObject(createMiniIcon(t.getIconColor(), 6, 6));
    }

    class TankAction implements Runnable {

        boolean active = true;

        public void run() {

        }
    }

}
