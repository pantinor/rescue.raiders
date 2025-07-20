package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.*;
import rescue.raiders.game.RescueRaiders;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.util.AtlasCache;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;

public class Helicopter extends Actor implements InputProcessor {

    private static final float CRASH_SPEED_THRESHOLD = 8;
    private static final float GROUND_LEVEL = FIELD_HEIGHT - 5;

    /**
     * ax / ay are the current tilt offsets in the X and Y directions. avx / avy
     * are the velocities of those tilts.
     *
     * They store the tilt‐rates on X and Y, letting helicopter gracefully
     * accelerate into a bank, then spring and damp back toward level.
     */
    private float ax, avx, ay, avy, angle;
    private float px, py, pvx, pvy;

    private static final float PD = 0.9f;//drag simulating friction or air resistance on position movement
    private static final float AK = 0.03f;// spring constant
    private static final float AD = 0.7f; // damping factor

    private boolean left, right, up, down;
    private boolean west;

    private final Animation<TextureRegion> flipped;
    private final Array<AtlasRegion> turningLeft;
    private final Array<AtlasRegion> turningRight;
    private int turningIndex = 0;

    private int fuel = 100;
    private final TextureRegion healthBar;
    private final TextureRegion fuelBar;
    boolean destroyed = false;
    private com.badlogic.gdx.audio.Sound copterSound;

    public Helicopter(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.02f, 1f, false);

        Array<AtlasRegion> east = AtlasCache.get(t.getAtlasName()).findRegions(t.getRegionName());
        for (TextureRegion tr : east) {
            tr.flip(true, false);
        }

        this.flipped = new Animation(0.02f, east);

        this.turningRight = AtlasCache.get(t.getAtlasName()).findRegions("turning");
        this.turningLeft = AtlasCache.get(t.getAtlasName()).findRegions("turning");
        this.turningLeft.reverse();

        healthBar = new TextureRegion(RescueRaiders.fillRectangle(SCREEN_WIDTH, STATUS_BAR_HEIGHT, Color.GREEN));
        fuelBar = new TextureRegion(RescueRaiders.fillRectangle(SCREEN_WIDTH, STATUS_BAR_HEIGHT, Color.ORANGE));

        ax = 0.0f;
        ay = 0.0f;
        avx = 0.0f;
        avy = 0.0f;

        copterSound = Sounds.play(Sound.HELICOPTER_ENGINE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        angle = -ax * 0.008f; //angleFactor

        frameCounter += Gdx.graphics.getDeltaTime();
        TextureRegion frame = null;

        if (west) { //facing west
            if (turningIndex != -1) {
                frame = turningLeft.get(turningIndex);
                turningIndex++;
                if (turningIndex == 6) {
                    turningIndex = -1;
                }
            } else {
                frame = anim.getKeyFrame(frameCounter, true);
            }
        } else {
            if (turningIndex != -1) {
                frame = turningRight.get(turningIndex);
                turningIndex++;
                if (turningIndex == 6) {
                    turningIndex = -1;
                }
            } else {
                frame = flipped.getKeyFrame(frameCounter, true);
            }
        }

        batch.draw(frame, this.getX(), this.getY(), 0, 0, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale, 1, 1, 100 * angle);

    }

    public void drawStatusBars(SpriteBatch batch) {
        batch.draw(healthBar, 0, SCREEN_HEIGHT - HUD_HEIGHT - STATUS_BAR_HEIGHT);
        batch.draw(fuelBar, 0, SCREEN_HEIGHT - HUD_HEIGHT - STATUS_BAR_HEIGHT - STATUS_BAR_HEIGHT);
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;

        double percent = (double) this.health / this.maxHealth;
        double bar = percent * SCREEN_WIDTH;

        if (this.health < 0) {
            bar = 0;
        }
        if (bar > SCREEN_WIDTH) {
            bar = SCREEN_WIDTH;
        }
        healthBar.setRegion(0, 0, (int) bar, STATUS_BAR_HEIGHT);

        if (health <= 0) {
            health = 0;
            crash();
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        if (this.destroyed) {
            this.copterSound.pause();
            ax = 0;
            ay = 0;
            avx = 0;
            avy = 0;
            pvx = 0;
            pvy = 0;
            left = false;
            right = false;
            up = false;
            down = false;
        } else {
            this.copterSound.resume();
        }
    }

    private void crash() {

        boolean goingWest = pvx < 0;

        setDestroyed(true);

        GAME.addExplosion(getX(), GROUND_LEVEL, goingWest, 10);

        Stage stage = getStage();

        remove();
        setPosition(HELI_START_X, HELI_START_Y);

        SequenceAction seq = Actions.sequence(
                Actions.delay(5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setDestroyed(false);
                        stage.addActor(Helicopter.this);
                    }
                }));
        stage.addAction(seq);
    }

    private void decrementFuel() {
        double percent = this.fuel / 100;
        double bar = percent * (double) SCREEN_WIDTH;
        if (this.fuel < 0) {
            bar = 0;
        }
        if (bar > SCREEN_WIDTH) {
            bar = SCREEN_WIDTH;
        }
        fuelBar.setRegion(0, 0, (int) bar, STATUS_BAR_HEIGHT);
    }

    @Override
    public void shoot() {
        float angleInDegrees = west ? 180 + angle * 100 : angle < 0 ? 360 + angle * 100 : angle * 100;
        Bullet b = new Bullet(this, west ? this.getX() + 15 : this.getX() + 55, this.getY() + 10, angleInDegrees);
        getStage().addActor(b);
        Sounds.play(Sound.INFANTRY_GUNFIRE);
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
        
        //TODO hitting the enemy ballons or chains

        setX(px);
        setY(yup(py));

        hitbox.x = px;
        hitbox.y = yup(py);

    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.A:
                left = true;
                break;
            case Keys.D:
                right = true;
                break;
            case Keys.W:
                up = true;
                break;
            case Keys.S:
                down = true;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Keys.A:
                left = false;
                break;
            case Keys.D:
                right = false;
                break;
            case Keys.W:
                up = false;
                break;
            case Keys.S:
                down = false;
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == 1) {
            turningIndex = 0;
            if (west) {
                west = false;
            } else {
                west = true;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean scrolled(float f, float f1) {
        return false;
    }

}
