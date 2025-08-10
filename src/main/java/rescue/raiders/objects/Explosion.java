package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.util.AtlasCache;

import java.util.EnumMap;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;

public class Explosion extends Actor {

    public enum Type {
        SMALL("small-explosion", Sound.BALLOON_EXPLOSION),
        MEDIUM("medium-explosion", Sound.GENERIC_EXPLOSION),
        LARGE("large-explosion", Sound.GENERIC_EXPLOSION_2),
        HUGE("huge-explosion", Sound.EXPLOSION_LARGE),
        BOMB("bomb-explosion", Sound.EXPLOSION_LARGE);

        private final String regionName;
        private final Sound sound;

        private Type(String regionName, Sound sound) {
            this.sound = sound;
            this.regionName = regionName;
        }

    }

    private static final float FRAME_DURATION = 0.07f;

    private static TextureAtlas EXPLOSIONS_ATLAS;
    private static final EnumMap<Type, Animation<TextureAtlas.AtlasRegion>> CACHE = new EnumMap<>(Type.class);

    private final Animation<TextureAtlas.AtlasRegion> anim;
    private float stateTime = 0f;

    public Explosion(Type type, float x, float y) {
        if (EXPLOSIONS_ATLAS == null) {
            EXPLOSIONS_ATLAS = AtlasCache.get("explosions");
        }
        this.anim = CACHE.computeIfAbsent(type, Explosion::buildAnimation);

        setPosition(x, y);

        Sounds.play(type.sound);
    }

    private static Animation<TextureAtlas.AtlasRegion> buildAnimation(Type type) {
        Array<TextureAtlas.AtlasRegion> frames = EXPLOSIONS_ATLAS.findRegions(type.regionName);
        return new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.NORMAL);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (anim.isAnimationFinished(stateTime)) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureAtlas.AtlasRegion frame = anim.getKeyFrame(stateTime);
        if (frame != null) {
            batch.draw(frame, getX(), getY());
        }
    }

}
