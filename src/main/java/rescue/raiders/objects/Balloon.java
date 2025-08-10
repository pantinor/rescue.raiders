package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;
import rescue.raiders.util.AtlasCache;

public class Balloon extends Actor {

    private final Animation<TextureRegion> chain;
    private boolean actionsAdded = false;

    public Balloon(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), .2f, 1f, true);
        health = 5;
        maxHealth = 5;

        Array<TextureAtlas.AtlasRegion> ch = AtlasCache.get("chain").findRegions("chain");
        this.chain = new Animation(0.05f, ch, Animation.PlayMode.LOOP);

        this.setUserObject(AtlasCache.get("backgrounds").findRegion(t.isEnemy() ? "enemy-balloon-icon" : "balloon-icon"));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        if (!actionsAdded) {

            SequenceAction seq1 = Actions.action(SequenceAction.class);

            seq1.addAction(Actions.moveTo(x, 600, 12f));

            seq1.addAction(Actions.run(new Runnable() {
                public void run() {
                    chain.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                }
            }));

            seq1.addAction(Actions.moveTo(x, FIELD_HEIGHT + 45, 12f));

            seq1.addAction(Actions.run(new Runnable() {
                public void run() {
                    chain.setPlayMode(Animation.PlayMode.LOOP);
                }
            }));

            addAction(Actions.forever(seq1));
            actionsAdded = true;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        TextureRegion ctr = chain.getKeyFrame(frameCounter);
        int h = (int) (getY() - 70);
        ctr.setRegion(ctr.getRegionX(), ctr.getRegionY(), ctr.getRegionWidth(), h);
        batch.draw(ctr, this.getX() + 25, FIELD_HEIGHT + 45);

        super.draw(batch, parentAlpha);

    }

}
