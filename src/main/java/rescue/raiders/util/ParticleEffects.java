package rescue.raiders.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ParticleEffects {

    private final ParticleEffect explosionEffect = new ParticleEffect();
    private final ParticleEffectPool explosionPool;

    private final ParticleEffect bloodEffect = new ParticleEffect();
    private final ParticleEffectPool bloodPool;

    private final Array<ParticleEffectPool.PooledEffect> active = new Array<>();

    public ParticleEffects(Texture fireSprite, Texture smokeSprite) {

        ParticleEmitter fire = buildFire10(new Sprite(fireSprite));
        ParticleEmitter smoke = buildSmoke2(new Sprite(smokeSprite));
        ParticleEmitter blood = buildBloodSpatter(new Sprite(fireSprite));

        explosionEffect.getEmitters().add(fire);
        explosionEffect.getEmitters().add(smoke);

        bloodEffect.getEmitters().add(blood);

        explosionPool = new ParticleEffectPool(explosionEffect, 8, 32);
        bloodPool = new ParticleEffectPool(bloodEffect, 8, 32);
    }

    public void render(Batch batch, float delta) {
        for (int i = active.size - 1; i >= 0; i--) {
            if (updateAndDraw(batch, active.get(i), delta)) {
                active.removeIndex(i);
            }
        }
    }

    private boolean updateAndDraw(Batch batch, ParticleEffectPool.PooledEffect fx, float delta) {
        fx.update(delta);
        fx.draw(batch);
        if (fx.isComplete()) {
            fx.free();
            return true;
        }
        return false;
    }

    public void addExplosion(float x, float y, boolean facingWest, int count) {
        float base = facingWest ? 180f - 20f : 20f;
        float spread = 25f;
        for (int i = 0; i < count; i++) {
            float jitter = MathUtils.random(-8f, 8f);
            active.add(spawn(x, y, base + jitter, spread));
        }
    }

    public void addBloodSpatter(float x, float y, boolean west) {
        float degrees = west ? 180f : 0f;
        for (int i = 0; i < 8; i++) {
            active.add(spawnBlood(x, y, degrees));
        }
    }

    private ParticleEffectPool.PooledEffect spawn(float x, float y, float centerDeg, float spreadDeg) {
        ParticleEffectPool.PooledEffect fx = explosionPool.obtain();
        for (ParticleEmitter e : fx.getEmitters()) {
            float min = centerDeg - spreadDeg;
            float max = centerDeg + spreadDeg;
            if (min > max) {
                float t = min;
                min = max;
                max = t;
            }
            e.getAngle().setActive(true);
            e.getAngle().setLow(0f);
            e.getAngle().setHigh(min, max);

        }
        fx.setPosition(x, y);
        fx.start();
        return fx;
    }

    private ParticleEffectPool.PooledEffect spawnBlood(float x, float y, float degrees) {
        ParticleEffectPool.PooledEffect fx = bloodPool.obtain();
        for (ParticleEmitter e : fx.getEmitters()) {

            float spread = 40f;
            float min = degrees - spread / 2f;
            float max = degrees + spread / 2f;

            e.getAngle().setActive(true);
            e.getAngle().setLow(0f);
            e.getAngle().setHigh(min, max);
            e.getAngle().setRelative(false);
        }
        fx.setPosition(x, y);
        fx.start();
        return fx;
    }

    private static ParticleEmitter buildFire(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire");

        e.setAttached(true);
        e.setContinuous(false);
        e.setAligned(false);
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setBehind(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(20);

        e.getDelay().setActive(false);

        e.getDuration().setLow(1f, 1f);

        e.getEmission().setActive(true);
        e.getEmission().setLow(0f, 0f);
        e.getEmission().setHigh(10f, 10f);
        e.getEmission().setRelative(false);
        e.getEmission().setScaling(new float[]{1f});
        e.getEmission().setTimeline(new float[]{0f});

        e.getLife().setActive(true);
        e.getLife().setLow(0f, 0f);
        e.getLife().setHigh(1000f, 1000f);
        e.getLife().setRelative(false);
        e.getLife().setScaling(new float[]{1f});
        e.getLife().setTimeline(new float[]{0f});

        e.getLifeOffset().setActive(false);
        e.getXOffsetValue().setActive(false);
        e.getYOffsetValue().setActive(false);

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(SpawnShape.point);

        e.getSpawnWidth().setActive(true);
        e.getSpawnWidth().setLow(0f, 0f);
        e.getSpawnWidth().setHigh(0f, 0f);
        e.getSpawnWidth().setRelative(false);
        e.getSpawnWidth().setScaling(new float[]{1f});
        e.getSpawnWidth().setTimeline(new float[]{0f});

        e.getSpawnHeight().setActive(true);
        e.getSpawnHeight().setLow(0f, 0f);
        e.getSpawnHeight().setHigh(0f, 0f);
        e.getSpawnHeight().setRelative(false);
        e.getSpawnHeight().setScaling(new float[]{1f});
        e.getSpawnHeight().setTimeline(new float[]{0f});

        e.getXScale().setActive(true);
        e.getXScale().setLow(0f, 0f);
        e.getXScale().setHigh(80f, 80f);
        e.getXScale().setRelative(false);
        e.getXScale().setScaling(new float[]{1f});
        e.getXScale().setTimeline(new float[]{0f});
        e.getYScale().set(e.getXScale());

        e.getVelocity().setActive(true);
        e.getVelocity().setLow(0f, 0f);
        e.getVelocity().setHigh(50f, 50f);
        e.getVelocity().setRelative(false);
        e.getVelocity().setScaling(new float[]{1f});
        e.getVelocity().setTimeline(new float[]{0f});

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f, 0f);
        e.getAngle().setHigh(1f, 360f); // min/max from file
        e.getAngle().setRelative(false);
        e.getAngle().setScaling(new float[]{1f});
        e.getAngle().setTimeline(new float[]{0f});

        e.getRotation().setActive(true);
        e.getRotation().setLow(1f, 360f);
        e.getRotation().setHigh(-180f, 180f);
        e.getRotation().setRelative(true);
        e.getRotation().setScaling(new float[]{0f, 1f});
        e.getRotation().setTimeline(new float[]{0f, 1f});

        e.getWind().setActive(false);
        e.getGravity().setActive(false);

        e.getTint().setTimeline(new float[]{0f});
        e.getTint().setColors(new float[]{
            1.0f, 0.3372549f, 0.0f
        });

        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f, 0f);
        e.getTransparency().setHigh(1f, 1f);
        e.getTransparency().setRelative(false);
        e.getTransparency().setScaling(new float[]{1f, 1f, 0f});
        e.getTransparency().setTimeline(new float[]{0f, 0.6712329f, 1f});

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();

        return e;
    }

    private static ParticleEmitter buildSmoke(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("smoke");

        e.setAttached(false);
        e.setContinuous(false);
        e.setAligned(false);
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setBehind(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(20);

        e.getDelay().setActive(false);

        e.getDuration().setLow(1f, 1f);

        e.getEmission().setActive(true);
        e.getEmission().setLow(0f, 0f);
        e.getEmission().setHigh(3f, 3f);
        e.getEmission().setRelative(false);
        e.getEmission().setScaling(new float[]{1f});
        e.getEmission().setTimeline(new float[]{0f});
        e.getLife().setActive(true);
        e.getLife().setLow(0f, 0f);
        e.getLife().setHigh(3000f, 3000f);
        e.getLife().setRelative(false);
        e.getLife().setScaling(new float[]{1f});
        e.getLife().setTimeline(new float[]{0f});

        e.getXOffsetValue().setActive(false);
        e.getYOffsetValue().setActive(false);

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(SpawnShape.square);

        e.getSpawnWidth().setActive(true);
        e.getSpawnWidth().setLow(0f, 0f);
        e.getSpawnWidth().setHigh(180f, 180f);
        e.getSpawnWidth().setRelative(false);
        e.getSpawnWidth().setScaling(new float[]{1f});
        e.getSpawnWidth().setTimeline(new float[]{0f});

        e.getSpawnHeight().setActive(true);
        e.getSpawnHeight().setLow(0f, 0f);
        e.getSpawnHeight().setHigh(110f, 110f);
        e.getSpawnHeight().setRelative(false);
        e.getSpawnHeight().setScaling(new float[]{1f});
        e.getSpawnHeight().setTimeline(new float[]{0f});

        e.getXScale().setActive(true);
        e.getXScale().setLow(0f, 0f);
        e.getXScale().setHigh(256f, 256f);
        e.getXScale().setRelative(false);
        e.getXScale().setScaling(new float[]{1f});
        e.getXScale().setTimeline(new float[]{0f});
        e.getYScale().set(e.getXScale());

        e.getVelocity().setActive(true);
        e.getVelocity().setLow(-25f, -25f);
        e.getVelocity().setHigh(25f, 25f);
        e.getVelocity().setRelative(false);
        e.getVelocity().setScaling(new float[]{0f, 1f});
        e.getVelocity().setTimeline(new float[]{0f, 1f});

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f, 0f);
        e.getAngle().setHigh(1f, 360f);
        e.getAngle().setRelative(false);
        e.getAngle().setScaling(new float[]{1f});
        e.getAngle().setTimeline(new float[]{0f});

        e.getRotation().setActive(true);
        e.getRotation().setLow(1f, 360f);
        e.getRotation().setHigh(-30f, 30f);
        e.getRotation().setRelative(true);
        e.getRotation().setScaling(new float[]{0f, 1f});
        e.getRotation().setTimeline(new float[]{0f, 1f});

        e.getWind().setActive(false);
        e.getGravity().setActive(false);

        e.getTint().setTimeline(new float[]{0f, 1f});
        e.getTint().setColors(new float[]{
            0.6f, 0.6f, 0.6f,
            0.3f, 0.3f, 0.3f
        });

        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f, 0f);
        e.getTransparency().setHigh(1f, 1f);
        e.getTransparency().setRelative(false);
        e.getTransparency().setScaling(new float[]{0f, 0.49122807f, 0f});
        e.getTransparency().setTimeline(new float[]{0f, 0.5068493f, 1f});

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();

        return e;
    }

    private static ParticleEmitter buildFire2(Sprite sprite) {
        ParticleEmitter fire = new ParticleEmitter();
        fire.setName("fire");
        fire.setAdditive(true);
        fire.setPremultipliedAlpha(false);
        fire.setContinuous(false);
        fire.setAttached(false);
        fire.setMinParticleCount(5);
        fire.setMaxParticleCount(200);

        fire.getSpawnShape().setActive(true);
        fire.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);

        fire.getDelay().setActive(true);
        fire.getDelay().setLow(0f);

        fire.getDuration().setActive(false);

        fire.getEmission().setActive(true);
        fire.getEmission().setHigh(1600f);

        fire.getLife().setActive(true);
        fire.getLife().setHigh(220f, 380f);

        fire.getAngle().setActive(true);
        fire.getAngle().setLow(0f);
        fire.getAngle().setHigh(360f);

        fire.getVelocity().setActive(true);
        fire.getVelocity().setHigh(160f, 260f);
        fire.getVelocity().setTimeline(new float[]{0f, 0.6f, 1f});
        fire.getVelocity().setScaling(new float[]{1f, 0.5f, 0f});

        fire.getGravity().setActive(true);
        fire.getGravity().setHigh(-60f);
        fire.getGravity().setTimeline(new float[]{0f, 1f});
        fire.getGravity().setScaling(new float[]{1f, 1f});

        fire.getXScale().setActive(true);
        fire.getXScale().setHigh(1f, 8f);
        fire.getXScale().setTimeline(new float[]{0f, 0.2f, 1f});
        fire.getXScale().setScaling(new float[]{1f, 0.7f, 0f});
        fire.getYScale().set(fire.getXScale());

        fire.getRotation().setActive(true);
        fire.getRotation().setHigh(-360f, 360f);

        fire.getTransparency().setActive(true);
        fire.getTransparency().setLow(0f);
        fire.getTransparency().setHigh(1f);
        fire.getTransparency().setRelative(false);
        fire.getTransparency().setTimeline(new float[]{0f, 0.05f, 0.4f, 1f});
        fire.getTransparency().setScaling(new float[]{0f, 1f, 0.6f, 0f});

        fire.getTint().setActive(true);
        fire.getTint().setTimeline(new float[]{0f, 0.35f, 1f});
        fire.getTint().setColors(new float[]{
            1.00f, 0.95f, 0.85f,
            1.00f, 0.78f, 0.22f,
            0.95f, 0.42f, 0.08f
        });

        fire.setSprites(new Array<>(new Sprite[]{sprite}));
        fire.preAllocateParticles();

        return fire;
    }

    private static ParticleEmitter buildSmoke2(Sprite sprite) {
        ParticleEmitter smoke = new ParticleEmitter();
        smoke.setName("smoke");
        smoke.setAdditive(false);
        smoke.setPremultipliedAlpha(false);
        smoke.setContinuous(false);
        smoke.setAttached(false);

        smoke.setMinParticleCount(5);
        smoke.setMaxParticleCount(200);

        smoke.getDuration().setLow(250);
        smoke.getEmission().setHigh(300);
        smoke.getLife().setHigh(900, 1200);
        smoke.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);
        smoke.getAngle().setHigh(0, 360);
        smoke.getVelocity().setHigh(40, 90);

        smoke.getXScale().setActive(true);
        smoke.getXScale().setHigh(2f, 10f);
        smoke.getXScale().setTimeline(new float[]{0f, 0.3f, 1f});
        smoke.getXScale().setScaling(new float[]{0.5f, 1.2f, 1.6f});
        smoke.getYScale().set(smoke.getXScale());

        smoke.getTransparency().setActive(true);
        smoke.getTransparency().setLow(0f);
        smoke.getTransparency().setHigh(1f);
        smoke.getTransparency().setRelative(false);
        smoke.getTransparency().setTimeline(new float[]{0f, 0.2f, 1f});
        smoke.getTransparency().setScaling(new float[]{0f, 0.5f, 0f});

        smoke.getGravity().setActive(true);
        smoke.getGravity().setHigh(20f);
        smoke.getGravity().setTimeline(new float[]{0f, 1f});
        smoke.getGravity().setScaling(new float[]{0.6f, 1.0f});

        smoke.getWind().setActive(true);
        smoke.getWind().setHigh(-12f, 12f);
        smoke.getWind().setTimeline(new float[]{0f, 1f});
        smoke.getWind().setScaling(new float[]{1f, 1f});

        smoke.getTint().setTimeline(new float[]{0f, 1f});
        smoke.getTint().setColors(new float[]{
            0.55f, 0.55f, 0.55f,
            0.35f, 0.35f, 0.35f
        });

        smoke.setSprites(new Array<>(new Sprite[]{sprite}));

        smoke.preAllocateParticles();
        return smoke;
    }

    private static ParticleEmitter buildFire5(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire5_jet");
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);
        e.setAligned(false);
        e.setBehind(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(120);

        e.getDelay().setActive(false);

        e.getDuration().setLow(160f);

        e.getEmission().setActive(true);
        e.getEmission().setHigh(900f);

        e.getLife().setActive(true);
        e.getLife().setHigh(280f, 360f);

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);            // overwritten by your spawn() spread anyway
        e.getAngle().setHigh(350f, 370f);   // narrow by default; still allow your spread override
        e.getAngle().setRelative(false);

        e.getVelocity().setActive(true);
        e.getVelocity().setHigh(280f, 360f);
        e.getVelocity().setTimeline(new float[]{0f, 0.4f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.7f, 0.2f});

        e.getGravity().setActive(true);
        e.getGravity().setHigh(-90f);       // pulls upward slightly (negative y)

        e.getXScale().setActive(true);
        e.getXScale().setHigh(6f, 14f);
        e.getXScale().setTimeline(new float[]{0f, 0.2f, 1f});
        e.getXScale().setScaling(new float[]{0.2f, 1f, 0f});
        e.getYScale().set(e.getXScale());

        e.getRotation().setActive(true);
        e.getRotation().setHigh(-90f, 90f);

        e.getTransparency().setActive(true);
        e.getTransparency().setTimeline(new float[]{0f, 0.05f, 0.5f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0.7f, 0f});

        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);

        e.getTint().setTimeline(new float[]{0f, 0.3f, 1f});
        e.getTint().setColors(new float[]{
            1.00f, 0.95f, 0.85f, // white-hot
            1.00f, 0.78f, 0.22f, // bright orange
            0.95f, 0.42f, 0.08f // deep orange
        });

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

    private static ParticleEmitter buildFire6(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire6_cone");
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(220);

        e.getDuration().setLow(220f);

        e.getEmission().setActive(true);
        e.getEmission().setHigh(1500f);

        e.getLife().setActive(true);
        e.getLife().setHigh(220f, 340f);

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);
        e.getAngle().setHigh(300f, 420f);   // wide cone by default (your spread override will narrow if needed)
        e.getAngle().setRelative(false);

        e.getVelocity().setActive(true);
        e.getVelocity().setHigh(180f, 260f);
        e.getVelocity().setTimeline(new float[]{0f, 0.5f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.6f, 0.1f});

        e.getGravity().setActive(true);
        e.getGravity().setHigh(-70f);

        e.getWind().setActive(true);
        e.getWind().setHigh(-40f, 40f);     // gives a bit of lateral sway

        e.getXScale().setActive(true);
        e.getXScale().setHigh(8f, 18f);
        e.getXScale().setTimeline(new float[]{0f, 0.15f, 1f});
        e.getXScale().setScaling(new float[]{0.2f, 0.9f, 0f});
        e.getYScale().set(e.getXScale());

        e.getRotation().setActive(true);
        e.getRotation().setHigh(-180f, 180f);

        e.getTransparency().setActive(true);
        e.getTransparency().setTimeline(new float[]{0f, 0.08f, 0.45f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0.5f, 0f});
        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);
        e.getTint().setTimeline(new float[]{0f, 0.4f, 1f});
        e.getTint().setColors(new float[]{
            1.00f, 0.95f, 0.85f,
            1.00f, 0.70f, 0.15f,
            0.90f, 0.30f, 0.05f
        });

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

    private static ParticleEmitter buildFire7(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire7_emberBurst");
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(80);

        e.getDuration().setLow(80f);

        e.getEmission().setActive(true);
        e.getEmission().setHigh(600f);

        e.getLife().setActive(true);
        e.getLife().setHigh(120f, 200f); // short pop

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);
        e.getAngle().setHigh(0f, 360f);
        e.getAngle().setRelative(false);

        e.getVelocity().setActive(true);
        e.getVelocity().setHigh(220f, 360f);
        e.getVelocity().setTimeline(new float[]{0f, 0.25f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.4f, 0f});

        e.getGravity().setActive(true);
        e.getGravity().setHigh(-40f); // slight lift

        e.getXScale().setActive(true);
        e.getXScale().setHigh(4f, 10f);
        e.getXScale().setTimeline(new float[]{0f, 0.1f, 1f});
        e.getXScale().setScaling(new float[]{0.2f, 0.9f, 0f});
        e.getYScale().set(e.getXScale());

        e.getRotation().setActive(true);
        e.getRotation().setHigh(-360f, 360f);

        e.getTransparency().setActive(true);
        e.getTransparency().setTimeline(new float[]{0f, 0.1f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0f});
        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);
        // More yellow/white for spark feel
        e.getTint().setTimeline(new float[]{0f, 1f});
        e.getTint().setColors(new float[]{
            1.00f, 0.98f, 0.80f,
            1.00f, 0.85f, 0.30f
        });

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

    private static ParticleEmitter buildFire8(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire8_pillar");
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(260);

        e.getDuration().setLow(320f);

        e.getEmission().setActive(true);
        e.getEmission().setHigh(1200f);

        e.getLife().setActive(true);
        e.getLife().setHigh(420f, 600f);

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);
        e.getAngle().setHigh(340f, 380f); // mostly upward by default
        e.getAngle().setRelative(false);

        e.getVelocity().setActive(true);
        e.getVelocity().setHigh(160f, 220f);
        e.getVelocity().setTimeline(new float[]{0f, 0.6f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.7f, 0.2f});

        e.getGravity().setActive(true);
        e.getGravity().setHigh(-110f); // strong lift

        e.getWind().setActive(true);
        e.getWind().setHigh(-25f, 25f);

        e.getXScale().setActive(true);
        e.getXScale().setHigh(10f, 26f);
        e.getXScale().setTimeline(new float[]{0f, 0.25f, 1f});
        e.getXScale().setScaling(new float[]{0.15f, 1f, 0f});
        e.getYScale().set(e.getXScale());

        e.getRotation().setActive(true);
        e.getRotation().setHigh(-90f, 90f);

        e.getTransparency().setActive(true);
        e.getTransparency().setTimeline(new float[]{0f, 0.07f, 0.55f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0.6f, 0f});
        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);
        e.getTint().setTimeline(new float[]{0f, 0.35f, 1f});
        e.getTint().setColors(new float[]{
            1.00f, 0.95f, 0.85f,
            1.00f, 0.76f, 0.20f,
            0.90f, 0.28f, 0.05f
        });

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

    private static ParticleEmitter buildFire9(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire9_gouts");
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(140);

        e.getDuration().setLow(200f);

        e.getEmission().setActive(true);
        e.getEmission().setHigh(700f);

        e.getLife().setActive(true);
        e.getLife().setHigh(200f, 360f);

        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.point);

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);
        e.getAngle().setHigh(0f, 360f);
        e.getAngle().setRelative(false);

        e.getVelocity().setActive(true);
        e.getVelocity().setHigh(140f, 320f);
        e.getVelocity().setTimeline(new float[]{0f, 0.35f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.5f, 0.1f});

        e.getGravity().setActive(true);
        e.getGravity().setHigh(-80f);

        // Make sizes pulse by using a more aggressive scale curve
        e.getXScale().setActive(true);
        e.getXScale().setHigh(5f, 22f);
        e.getXScale().setTimeline(new float[]{0f, 0.12f, 0.35f, 1f});
        e.getXScale().setScaling(new float[]{0.2f, 1.0f, 0.6f, 0f});
        e.getYScale().set(e.getXScale());

        e.getRotation().setActive(true);
        e.getRotation().setHigh(-240f, 240f);

        e.getTransparency().setActive(true);
        e.getTransparency().setTimeline(new float[]{0f, 0.08f, 0.45f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0.6f, 0f});
        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);
        e.getTint().setTimeline(new float[]{0f, 0.5f, 1f});
        e.getTint().setColors(new float[]{
            1.00f, 0.92f, 0.70f,
            1.00f, 0.65f, 0.15f,
            0.88f, 0.25f, 0.05f
        });

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

    private static ParticleEmitter buildFire10(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("fire10_ring");
        e.setAdditive(true);
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);

        e.setMinParticleCount(0);
        e.setMaxParticleCount(160);

        e.getDuration().setLow(120f);

        e.getEmission().setActive(true);
        e.getEmission().setHigh(900f);

        e.getLife().setActive(true);
        e.getLife().setHigh(160f, 280f);

        // Start from a small circle and expand (use square spawn + scale to simulate ring feel)
        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.square);
        e.getSpawnWidth().setActive(true);
        e.getSpawnWidth().setHigh(16f, 24f);
        e.getSpawnHeight().setActive(true);
        e.getSpawnHeight().setHigh(16f, 24f);

        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);
        e.getAngle().setHigh(0f, 360f);
        e.getAngle().setRelative(false);

        e.getVelocity().setActive(true);
        e.getVelocity().setHigh(180f, 300f);
        e.getVelocity().setTimeline(new float[]{0f, 0.4f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.5f, 0.05f});

        e.getGravity().setActive(true);
        e.getGravity().setHigh(-50f);

        e.getXScale().setActive(true);
        e.getXScale().setHigh(6f, 16f);
        e.getXScale().setTimeline(new float[]{0f, 0.18f, 1f});
        e.getXScale().setScaling(new float[]{0.0f, 1.0f, 0f}); // pop then vanish
        e.getYScale().set(e.getXScale());

        e.getRotation().setActive(true);
        e.getRotation().setHigh(-180f, 180f);

        e.getTransparency().setActive(true);
        e.getTransparency().setTimeline(new float[]{0f, 0.12f, 0.55f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0.7f, 0f});
        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);
        e.getTint().setTimeline(new float[]{0f, 0.35f, 1f});
        e.getTint().setColors(new float[]{
            1.00f, 0.96f, 0.85f,
            1.00f, 0.74f, 0.18f,
            0.92f, 0.30f, 0.06f
        });

        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

    private static ParticleEmitter buildBloodSpatter(Sprite sprite) {
        ParticleEmitter e = new ParticleEmitter();
        e.setName("blood_spatter");

        // Rendering/behavior
        e.setAdditive(false);              // blood should not be additive
        e.setPremultipliedAlpha(false);
        e.setContinuous(false);
        e.setAttached(false);
        e.setAligned(false);
        e.setBehind(false);

        // Pooling / capacity
        e.setMinParticleCount(0);
        e.setMaxParticleCount(200);

        // One-shot burst
        e.getDelay().setActive(false);
        e.getDuration().setLow(80f);       // short burst window

        // Emit a lot at once
        e.getEmission().setActive(true);
        e.getEmission().setLow(0f);
        e.getEmission().setHigh(100f);
        e.getEmission().setRelative(false);
        e.getEmission().setTimeline(new float[]{0f});
        e.getEmission().setScaling(new float[]{1f});

        // Lifetime: short-to-medium so droplets travel then fade
        e.getLife().setActive(true);
        e.getLife().setLow(180f);
        e.getLife().setHigh(420f);
        e.getLife().setRelative(false);
        e.getLife().setTimeline(new float[]{0f});
        e.getLife().setScaling(new float[]{1f});

        // Spawn: tight origin, use point or tiny square for a bit of thickness
        e.getSpawnShape().setActive(true);
        e.getSpawnShape().setShape(ParticleEmitter.SpawnShape.square);
        e.getSpawnWidth().setActive(true);
        e.getSpawnWidth().setLow(0f);
        e.getSpawnWidth().setHigh(6f);     // tiny patch at impact
        e.getSpawnWidth().setRelative(false);
        e.getSpawnWidth().setTimeline(new float[]{0f});
        e.getSpawnWidth().setScaling(new float[]{1f});

        e.getSpawnHeight().setActive(true);
        e.getSpawnHeight().setLow(0f);
        e.getSpawnHeight().setHigh(6f);
        e.getSpawnHeight().setRelative(false);
        e.getSpawnHeight().setTimeline(new float[]{0f});
        e.getSpawnHeight().setScaling(new float[]{1f});

        // Direction: your spawn() method will overwrite angle; defaults still reasonable
        e.getAngle().setActive(true);
        e.getAngle().setLow(0f);
        e.getAngle().setHigh(0f, 360f);
        e.getAngle().setRelative(false);
        e.getAngle().setTimeline(new float[]{0f});
        e.getAngle().setScaling(new float[]{1f});

        // Speed: fast initial spray that dies off
        e.getVelocity().setActive(true);
        e.getVelocity().setLow(150f);
        e.getVelocity().setHigh(250f);
        e.getVelocity().setRelative(false);
        e.getVelocity().setTimeline(new float[]{0f, 0.45f, 1f});
        e.getVelocity().setScaling(new float[]{1f, 0.5f, 0.1f});

        // Gravity: positive pulls downward (your earlier fire used negative for upward lift)
        e.getGravity().setActive(true);
        e.getGravity().setLow(0f);
        e.getGravity().setHigh(120f);      // tune for heavier/lighter droplets
        e.getGravity().setTimeline(new float[]{0f, 1f});
        e.getGravity().setScaling(new float[]{1f, 1f});

        // Optional small lateral randomness
        e.getWind().setActive(true);
        e.getWind().setLow(-20f);
        e.getWind().setHigh(20f);
        e.getWind().setTimeline(new float[]{0f, 1f});
        e.getWind().setScaling(new float[]{1f, 1f});

        // Size: small droplets; some variance
        e.getXScale().setActive(true);
        e.getXScale().setLow(0f);
        e.getXScale().setHigh(1f, 3f);     // pixels-ish; adjust to your sprite
        e.getXScale().setRelative(false);
        e.getXScale().setTimeline(new float[]{0f, 0.2f, 1f});
        e.getXScale().setScaling(new float[]{0.3f, 1.0f, 0.0f});
        e.getYScale().set(e.getXScale());

        // Rotation: a bit of tumble
        e.getRotation().setActive(true);
        e.getRotation().setLow(0f);
        e.getRotation().setHigh(-180f, 180f);
        e.getRotation().setRelative(true);
        e.getRotation().setTimeline(new float[]{0f, 1f});
        e.getRotation().setScaling(new float[]{0f, 1f});

        // Color: deep red to darker red over life
        e.getTint().setTimeline(new float[]{0f, 1f});
        e.getTint().setColors(new float[]{
            0.75f, 0.05f, 0.05f, // bright/wet red
            0.35f, 0.02f, 0.02f // darker as it ages
        });

        // Alpha: appear quickly, then fade
        e.getTransparency().setActive(true);
        e.getTransparency().setLow(0f);
        e.getTransparency().setHigh(1f);
        e.getTransparency().setRelative(false);
        e.getTransparency().setTimeline(new float[]{0f, 0.08f, 0.6f, 1f});
        e.getTransparency().setScaling(new float[]{0f, 1f, 0.7f, 0f});

        // Use the provided sprite
        e.setSprites(new Array<>(new Sprite[]{sprite}));
        e.preAllocateParticles();
        return e;
    }

}
