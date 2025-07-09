package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rescue.raiders.game.GameStage;
import rescue.raiders.util.AtlasCache;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;

public class AAGun extends Actor {

    long lastShotTime;

    public AAGun(ActorType t) {
        super(t, AtlasCache.get("turret"), .05f, .65f, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        GameStage stage = (GameStage) getStage();

        Helicopter heli = stage.getHelicopter();

        float tx = heli != null ? heli.getX() : 0;
        float ty = heli != null ? heli.getY() : 0;
        float x = this.getX();
        float y = this.getY();

        float ang = getAngleToTarget(tx, ty, x, y);

        float index = getFrame(ang);
        TextureRegion frame = anim.getKeyFrames()[(int) index];
        batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);

        float dst = distance(tx, ty, x, y);
        if (isAlive() && this.type == ActorType.ENEMY_TURRET && dst < 600000 && (25 < ang && ang < 165)) {
            if (System.currentTimeMillis() - lastShotTime > 200) {
                Bullet b = new Bullet(this, x + 25, y + 15, ang);
                this.getStage().addActor(b);
                lastShotTime = System.currentTimeMillis();
                Sounds.play(Sound.TURRET_GUNFIRE);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    private float distance(float x1, float y1, float x2, float y2) {
        final float x_d = x2 - x1;
        final float y_d = y2 - y1;
        return x_d * x_d + y_d * y_d;
    }

    private float getAngleToTarget(float tx, float ty, float x, float y) {
        float ang = (float) Math.toDegrees(Math.atan2(tx - x, y - ty));
        if (ang < 0) {
            ang = 360 + ang;
        }
        ang = (ang - 90) % 360;
        return ang;
    }

    private float getFrame(float ang) {

        if (!isAlive()) {
            return 0;
        }

        float frame = (ang - 45) / (90.0f / 16.0f);

        if (frame < 0) {
            frame = 0;
        } else if (frame > 15f) {
            frame = 15f;
        }
        return frame;
    }

}
