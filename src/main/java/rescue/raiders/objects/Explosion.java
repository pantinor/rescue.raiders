package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;

public class Explosion extends com.badlogic.gdx.scenes.scene2d.Actor {

    ParticleEffect effect;
    int emitterIndex;
    Array<ParticleEmitter> emitters;
    int particleCount = 10;

    public Explosion(float x, float y) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("assets/particle/explosion.p"), Gdx.files.internal("assets/particle"));
        effect.setPosition(x, y);
        emitters = new Array(effect.getEmitters());
        effect.getEmitters().clear();
        effect.getEmitters().add(emitters.get(0));
        effect.scaleEffect(.5f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        effect.draw(batch, Gdx.graphics.getDeltaTime());
    }

}
