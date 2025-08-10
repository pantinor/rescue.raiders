package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rescue.raiders.util.AtlasCache;

public class Infantry extends ShootableActor {

    private final TextureRegion shootingFrame;

    public Infantry(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 5;
        maxHealth = 5;
        canCollide = false;
        shootingFrame = AtlasCache.get(t.getAtlasName()).findRegion(t.getAimingName(), 0);
        shootingFrame.flip(!t.isEnemy(), false);
        this.setUserObject(AtlasCache.get("backgrounds").findRegion(t.isEnemy() ? "enemy-infantry-icon" : "infantry-icon"));
    }

    @Override
    public TextureRegion shootingTexture() {
        return shootingFrame;
    }

}
