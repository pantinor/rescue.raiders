package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;
import static rescue.raiders.game.RescueRaiders.fillRectangle;

public class Base extends Actor {

    public Base(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 1f, false);
        this.setUserObject(AtlasCache.get("backgrounds").findRegion(t.isEnemy() ? "enemy-base-icon" : "base-icon"));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - 5);
    }

}
