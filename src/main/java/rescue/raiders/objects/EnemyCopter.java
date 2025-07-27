package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;
import static rescue.raiders.game.RescueRaiders.FIELD_WIDTH;
import static rescue.raiders.game.RescueRaiders.GAME;
import static rescue.raiders.game.RescueRaiders.SCREEN_HEIGHT;
import static rescue.raiders.game.RescueRaiders.yup;
import rescue.raiders.util.AtlasCache;

public class EnemyCopter extends Actor {

    private static final float CRASH_SPEED_THRESHOLD = 8;
    private static final float GROUND_LEVEL = FIELD_HEIGHT - 5;

    private float turningTime = 0f;
    private static final float TURN_FRAME_DURATION = 0.05f;

    private float ax, avx, ay, avy;
    private float px, py, pvx, pvy;

    private static final float PD = 0.9f; // drag simulating friction or air resistance on position movement
    private static final float AK = 0.03f; // spring constant
    private static final float AD = 0.7f;  // damping factor

    private boolean left, right, up, down;
    private boolean west = true;

    private final Animation<TextureRegion> flipped;
    private final Array<TextureAtlas.AtlasRegion> turningLeft;
    private final Array<TextureAtlas.AtlasRegion> turningRight;
    private int turningIndex = -1;

    public EnemyCopter(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.02f, 1f, false);

        Array<TextureAtlas.AtlasRegion> east = AtlasCache.get(t.getAtlasName()).findRegions(t.getRegionName());
        for (TextureRegion tr : east) {
            tr.flip(true, false);
        }

        this.flipped = new Animation(0.02f, east);

        Array<TextureAtlas.AtlasRegion> turningRight1 = AtlasCache.get(t.getAtlasName()).findRegions("turning");
        Array<TextureAtlas.AtlasRegion> turningRight2 = AtlasCache.get(t.getAtlasName()).findRegions("turning");
        for (TextureRegion tr : turningRight2) {
            tr.flip(true, false);
        }
        for (int i = turningRight2.size - 1; i >= 0; i--) {
            turningRight1.add(turningRight2.get(i));
        }
        this.turningRight = turningRight1;

        this.turningLeft = new Array<>();
        for (TextureAtlas.AtlasRegion region : this.turningRight) {
            TextureAtlas.AtlasRegion copy = new TextureAtlas.AtlasRegion(region);
            copy.flip(true, false);
            this.turningLeft.add(copy);
        }

        ax = 0.0f;
        ay = 0.0f;
        avx = 0.0f;
        avy = 0.0f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float delta = Gdx.graphics.getDeltaTime();

        float speed = (float) Math.hypot(pvx, pvy);

        int tiltIndex = Math.max(0, Math.min(6, (int) speed));

        TextureRegion frame = null;

        if (west) {
            if (turningIndex != -1) {
                turningTime += delta;
                if (turningTime >= TURN_FRAME_DURATION) {
                    turningIndex++;
                    turningTime = 0f;
                }
                if (turningIndex >= 14) {
                    turningIndex = -1;
                } else {
                    frame = turningLeft.get(turningIndex);
                }
            }
            if (frame == null) {
                frame = anim.getKeyFrames()[tiltIndex];
            }
        } else {
            if (turningIndex != -1) {
                turningTime += delta;
                if (turningTime >= TURN_FRAME_DURATION) {
                    turningIndex++;
                    turningTime = 0f;
                }
                if (turningIndex >= 14) {
                    turningIndex = -1;
                } else {
                    frame = turningRight.get(turningIndex);
                }
            }
            if (frame == null) {
                frame = flipped.getKeyFrames()[tiltIndex];
            }
        }

        batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, yup(y));
        px = x;
        py = yup(y);
    }

    @Override
    public void act(float delta) {

        super.act(delta);

        ax += avx;
        ay += avy;
        px += pvx;
        py += pvy;

        if (px < 0) {
            px = 0;
        }

        if (px > FIELD_WIDTH) {
            px = FIELD_WIDTH;
        }

        if (left) {
            pvx += 0.05 * ax;
            avx -= 0.5;
        }
        if (right) {
            pvx += 0.05 * ax;
            avx += 0.5;
        }
        if (up) {
            pvy += 0.05 * ay;
            avy -= 0.5;
        }
        if (down) {
            pvy += 0.05 * ay;
            avy += 0.5;
        }

        pvx *= PD;
        pvy *= PD;

        avx += -AK * ax;
        avx *= AD;
        avy += -AK * ay;
        avy *= AD;

        float nextY = SCREEN_HEIGHT - py + pvy * delta;

        if (nextY <= GROUND_LEVEL) {
            float speed = (float) Math.hypot(pvx, pvy);
            if (speed > CRASH_SPEED_THRESHOLD) {
                crash();
            } else {
                py = SCREEN_HEIGHT - GROUND_LEVEL;
            }
        }

        setX(px);
        setY(yup(py));

        hitbox.x = px;
        hitbox.y = yup(py);
    }

    private void crash() {
        boolean goingWest = pvx < 0;
        GAME.addExplosion(getX(), GROUND_LEVEL, goingWest, 10);
        remove();
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void turn() {
        turningIndex = 0;
        west = !west;
    }
}
