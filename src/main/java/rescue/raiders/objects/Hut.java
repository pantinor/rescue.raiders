package rescue.raiders.objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.Random;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;

import rescue.raiders.util.AtlasCache;
import static rescue.raiders.game.RescueRaiders.fillRectangle;

public class Hut extends Actor {

    private final Balloon balloon;

    public Hut(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 1f, !t.isEnemy());
        this.setUserObject(AtlasCache.get("backgrounds").findRegion(t.isEnemy() ? "enemy-hut-icon" : "hut-icon"));

        health = 30;
        maxHealth = 30;

        ActorType b = t.isEnemy() ? ActorType.ENEMY_BALLON : ActorType.BALLON;
        this.balloon = new Balloon(b);
        this.balloon.setUserObject(fillRectangle(b.getIconColor(), 4, 4));
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
        super.setPosition(x, y);
        this.balloon.setPosition(x - 5, new Random().nextInt(600 - FIELD_HEIGHT - 45));
    }

}
