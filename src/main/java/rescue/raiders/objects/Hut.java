package rescue.raiders.objects;

import java.util.Random;

import rescue.raiders.util.AtlasCache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

public class Hut extends Actor {

    private final Balloon balloon;
    private boolean balloonAdded = false;
    private Animation chain;
    
    public Hut(ActorType t) {

        super(t, AtlasCache.get("backgrounds"), .65f, false);
        this.setUserObject(createMiniIcon(t.getIconColor(), 8,8));
        
        ActorType b = t.isIsEnemy() ? ActorType.ENEMY_BALLON : ActorType.BALLON ;
        this.balloon = new Balloon(b);
        this.balloon.setUserObject(createMiniIcon(b.getIconColor(), 3,3));
        
        Array<TextureAtlas.AtlasRegion> ch = AtlasCache.get("chain").findRegions("chain");
        this.chain = new Animation(0.05f, ch, Animation.PlayMode.LOOP);

    }

    @Override
    public void setPosition(float x, float y) {

        super.setPosition(x, y);

        this.balloon.setX(x + 60);
        this.balloon.setY(new Random().nextInt(400 - 135) + 135);

        this.balloon.clearActions();

        SequenceAction seq1 = Actions.action(SequenceAction.class);
        //seq1.addAction(Actions.delay(.05f));
        //seq1.addAction(Actions.run(new MeteorAction()));
        seq1.addAction(Actions.moveTo(x + 60, 400, 8f));
        
        seq1.addAction(Actions.run(new Runnable() {
        public void run() {
            Hut.this.chain.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        }
        }));

        seq1.addAction(Actions.moveTo(x + 60, 135, 8f));
        
        seq1.addAction(Actions.run(new Runnable() {
        public void run() {
            Hut.this.chain.setPlayMode(Animation.PlayMode.LOOP);
        }
        }));

        this.balloon.addAction(Actions.forever(seq1));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (!balloonAdded) {
            this.getStage().addActor(this.balloon);
            balloonAdded = true;
        }

        frameCounter += Gdx.graphics.getDeltaTime();
        batch.draw(tr, this.getX(), this.getY(), tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
                
        TextureRegion ctr = chain.getKeyFrame(frameCounter);
        int h = (int)(this.balloon.getY() - 282 + 165);
        ctr.setRegion(ctr.getRegionX(), ctr.getRegionY(), ctr.getRegionWidth(), h);
        batch.draw(ctr, this.getX() + 72, this.getY() + 80);

    }

}
