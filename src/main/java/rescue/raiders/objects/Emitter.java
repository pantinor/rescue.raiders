package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Emitter extends com.badlogic.gdx.scenes.scene2d.Actor {

    public enum Type {
        FIRE("explosion.p"),
        STARS("stars.p"),
        SMOKE("smoke.p");

        private String file;

        private Type(String f) {
            this.file = f;
        }

        public String file() {
            return this.file;
        }
    }

    private final ParticleEffect effect;

    public Emitter(float x, float y, Type type) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("assets/particle/" + type.file()), Gdx.files.internal("assets/particle"));
        effect.setPosition(x, y);
        effect.scaleEffect(.5f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        effect.draw(batch, Gdx.graphics.getDeltaTime());
    }

}
