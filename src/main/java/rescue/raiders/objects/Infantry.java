package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static rescue.raiders.game.RescueRaiders.GAME;
import rescue.raiders.util.AtlasCache;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;

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

    @Override
    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;
            if (health <= 0) {
                Sounds.play(Sound.SPLAT);
                GAME.addBloodSpatter(getX(), getY(), !this.type.isEnemy());
                remove();
            }
        }
    }

}
