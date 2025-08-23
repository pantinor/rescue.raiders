package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.util.AtlasCache;

public class Pad extends Actor {

    public Pad(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 1f, false);
        canCollide = false;

        health = 100;
        maxHealth = 100;
        this.hitbox = new Rectangle(0, 0, tr.getRegionWidth(), 30);

        Array<TextureAtlas.AtlasRegion> regions = AtlasCache.get(t.getAtlasName()).findRegions(t.getRegionName());
        this.anim = new Animation(2f, regions);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - 5);
    }
}
