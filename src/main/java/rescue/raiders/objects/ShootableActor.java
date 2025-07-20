package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ShootableActor extends Actor {

    public ShootableActor(ActorType t, TextureAtlas atlas, float frameRate, float scale, boolean flip) {
        super(t, atlas, frameRate, scale, flip);
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
