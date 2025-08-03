package rescue.raiders.objects;

import java.util.List;
import rescue.raiders.game.GameStage;
import static rescue.raiders.game.RescueRaiders.FIELD_WIDTH;
import rescue.raiders.util.AtlasCache;

public class Jeep extends Actor {

    public Jeep(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 10;
        maxHealth = 10;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        GameStage stage = (GameStage) getStage();
        List<rescue.raiders.objects.Actor> actors = stage.gameActors();

        boolean move = true;
        for (rescue.raiders.objects.Actor a : actors) {
            if (a.type.isEnemy()) {
                //move = false;
            }
        }

        if (move) {
            setX(type.isEnemy() ? getX() - 1 : getX() + 1);
            hitbox.x = this.getX();
            hitbox.y = this.getY();
        }

        if (getX() < 0 || getX() > FIELD_WIDTH) {
            this.remove();
        }

    }

}
