package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.util.AtlasCache;
import rescue.raiders.util.Sound;

public class LargeTank extends ShootableActor {

    private final Animation<TextureRegion> shootingAnimation;

    public LargeTank(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 20;
        maxHealth = 20;
        this.setUserObject(AtlasCache.get("backgrounds").findRegion(t.isEnemy() ? "enemy-super-tank-icon" : "super-tank-icon"));
        
        Array<TextureAtlas.AtlasRegion> ch = AtlasCache.get(t.getAtlasName()).findRegions("shooting");
        for (TextureRegion tr : ch) {
            tr.flip(false, false);
        }
        this.shootingAnimation = new Animation(0.2f, ch, Animation.PlayMode.LOOP);
    }

    @Override
    public Animation<TextureRegion> shootingAnim() {
        return this.shootingAnimation;
    }

    @Override
    public float shootingTimeDuration() {
        return 20 * 0.2f;
    }

    @Override
    public int bulletDamage() {
        return 4;
    }

    @Override
    public Sound shootingSound() {
        return Sound.GENERIC_BOOM;
    }

}
