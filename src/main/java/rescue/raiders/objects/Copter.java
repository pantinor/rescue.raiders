package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import java.util.List;
import rescue.raiders.game.GameStage;
import rescue.raiders.game.RescueRaiders;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;
import static rescue.raiders.game.RescueRaiders.FIELD_WIDTH;
import static rescue.raiders.game.RescueRaiders.GAME;
import static rescue.raiders.game.RescueRaiders.HELI_START_X;
import static rescue.raiders.game.RescueRaiders.HELI_START_Y;
import static rescue.raiders.game.RescueRaiders.HUD_HEIGHT;
import static rescue.raiders.game.RescueRaiders.SCREEN_HEIGHT;
import static rescue.raiders.game.RescueRaiders.SCREEN_WIDTH;
import static rescue.raiders.game.RescueRaiders.STATUS_BAR_HEIGHT;
import static rescue.raiders.game.RescueRaiders.yup;
import rescue.raiders.util.AtlasCache;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;

public class Copter extends Actor implements InputProcessor {

    private static final float CRASH_SPEED_THRESHOLD = 8;
    private static final float GROUND_LEVEL = FIELD_HEIGHT - 5;

    private float turningTime = 0f;
    private static final float TURN_FRAME_DURATION = 0.05f;

    private float ax, avx, ay, avy, angle;
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

    private int fuel = 100;
    private final TextureRegion healthBar;
    private final TextureRegion fuelBar;
    boolean destroyed = false;
    private com.badlogic.gdx.audio.Sound copterSound;

    public Copter(ActorType t) {
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

        healthBar = new TextureRegion(RescueRaiders.fillRectangleGradient(SCREEN_WIDTH, STATUS_BAR_HEIGHT, new Color(0x66a0f1)));
        fuelBar = new TextureRegion(RescueRaiders.fillRectangleGradient(SCREEN_WIDTH, STATUS_BAR_HEIGHT, Color.ORANGE));

        copterSound = Sounds.play(Sound.HELICOPTER_ENGINE);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        angle = -ax * 0.008f;

        frameCounter += Gdx.graphics.getDeltaTime();

        TextureRegion frame = null;

        if (west) {
            if (turningIndex != -1) {
                turningTime += frameCounter;
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
                frame = anim.getKeyFrame(frameCounter, true);
            }
        } else {
            if (turningIndex != -1) {
                turningTime += frameCounter;
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
                frame = flipped.getKeyFrame(frameCounter, true);
            }
        }

        batch.draw(frame, this.getX(), this.getY(), 0, 0, frame.getRegionWidth(), frame.getRegionHeight(), 1, 1, 100 * angle);
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

        GAME.addExplosion(getX(), getY() < GROUND_LEVEL ? GROUND_LEVEL : getY(), goingWest, 10);

        Stage stage = getStage();

        remove();
        setPosition(HELI_START_X, HELI_START_Y);

        SequenceAction seq = Actions.sequence(
                Actions.delay(5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setDestroyed(false);
                        stage.addActor(Copter.this);
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

    public void shoot() {
        float angleInDegrees = west ? 180 + angle * 100 : angle < 0 ? 360 + angle * 100 : angle * 100;
        Bullet b = new Bullet(this, west ? this.getX() + 15 : this.getX() + 55, this.getY() + 10, angleInDegrees, 2);
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

        GameStage stage = (GameStage) getStage();
        List<rescue.raiders.objects.Actor> actors = stage.gameActors();
        boolean crashed = false;
        for (rescue.raiders.objects.Actor other : actors) {
            if (other == this) {
                continue;
            }
            if (other.type.isEnemy() && other.canCollide && hits(other.hitbox)) {
                crashed = true;
                break;
            }
        }
        if (crashed) {
            crash();
        }

        setX(px);
        setY(yup(py));

        hitbox.x = px;
        hitbox.y = yup(py);
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.A:
                left = true;
                break;
            case Input.Keys.D:
                right = true;
                break;
            case Input.Keys.W:
                up = true;
                break;
            case Input.Keys.S:
                down = true;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.A:
                left = false;
                break;
            case Input.Keys.D:
                right = false;
                break;
            case Input.Keys.W:
                up = false;
                break;
            case Input.Keys.S:
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
        if (button == Input.Buttons.RIGHT) {
            turningIndex = 0;
            west = !west;
            return true;
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
