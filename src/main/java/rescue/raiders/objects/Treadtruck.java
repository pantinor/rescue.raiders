package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class TreadTruck extends Actor {

    public TreadTruck(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 10;
        maxHealth = 10;
    }

    
    @Override
    public void act(float delta) {
        super.act(delta);

        boolean move = true;
        for (com.badlogic.gdx.scenes.scene2d.Actor a : getStage().getActors()) {
            if (a instanceof Actor) {
                Actor actor = (Actor) a;
                if (actor.type.isEnemy()) {
                    //move = false;
                }
            }
        }

        if (move) {
            setX(type.isEnemy() ? getX() - 1 : getX() + 1);
            hitbox.x = this.getX();
            hitbox.y = this.getY();
        }

    }
}
