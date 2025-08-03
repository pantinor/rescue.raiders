package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.List;
import rescue.raiders.game.GameStage;
import static rescue.raiders.game.RescueRaiders.FIELD_WIDTH;
import rescue.raiders.util.AtlasCache;

public class CoveredTruck extends Actor {

    public CoveredTruck(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, false);
        health = 10;
        maxHealth = 10;
        
        Array<TextureAtlas.AtlasRegion> regions = AtlasCache.get(t.getAtlasName()).findRegions(t.getRegionName());
        this.anim = new Animation(0.10f, regions);
        if (t.isEnemy()) {
            for (TextureRegion ltr : regions) {
                ltr.flip(true, false);
            }
        }
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
