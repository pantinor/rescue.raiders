package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rescue.raiders.util.AtlasCache;

public class Engineer extends ShootableActor {

    private final TextureRegion shootingFrame;

    public Engineer(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 5;
        maxHealth = 5;
        String sname = t.isEnemy() ? "enemy-engineer-shooting" : "engineer-shooting";
        shootingFrame = AtlasCache.get(t.getAtlasName()).findRegion(sname, 0);
        shootingFrame.flip(!t.isEnemy(), false);
    }

    @Override
    public TextureRegion shootingTexture() {
        return shootingFrame;
    }

}
