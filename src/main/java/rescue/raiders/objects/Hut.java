package rescue.raiders.objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.Random;

import rescue.raiders.util.AtlasCache;
import static rescue.raiders.game.RescueRaiders.createMiniIcon;

public class Hut extends Actor {

    private final Balloon balloon;

    public Hut(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 1f, t.isEnemy());

        this.setUserObject(createMiniIcon(t.getIconColor(), 8, 8));

        ActorType b = t.isEnemy() ? ActorType.ENEMY_BALLON : ActorType.BALLON;
        this.balloon = new Balloon(b);
        this.balloon.setUserObject(createMiniIcon(b.getIconColor(), 3, 3));
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            stage.addActor(this.balloon);
        } else {
            this.balloon.remove();
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - 5);
        this.balloon.setPosition(x + 10, new Random().nextInt(400 - 135) + 135);
    }

}
