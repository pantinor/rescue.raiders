package rescue.raiders.objects;

import com.badlogic.gdx.math.Rectangle;
import rescue.raiders.util.AtlasCache;

public class Base extends Actor {

    public Base(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 1f, false);
        health = 100;
        maxHealth = 100;
        this.hitbox = new Rectangle(0, 0, tr.getRegionWidth(), 65);
        this.setUserObject(AtlasCache.get("backgrounds").findRegion(t.isEnemy() ? "enemy-base-icon" : "base-icon"));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x - tr.getRegionWidth() / 2, y - 5);
    }

}
