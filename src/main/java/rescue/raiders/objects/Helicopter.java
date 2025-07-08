package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.*;
import rescue.raiders.game.RescueRaiders;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.util.AtlasCache;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;

public class Helicopter extends Actor implements InputProcessor {

    private float ax, avx, ay, avy, ak, ad, angle;
    private int lastMouseX, lastMouseY;
    private boolean left, right, up, down;
    private float px = SCREEN_WIDTH / 2, py = SCREEN_HEIGHT / 2, pvx = 0.0f, pvy = 0.0f, pd = 0.9f;
    private boolean west;

    private Music snd = Sounds.get(Sound.HELICOPTER_ENGINE);

    private Animation<TextureRegion> flipped;
    private Array<AtlasRegion> turningLeft;
    private Array<AtlasRegion> turningRight;
    private int turningIndex = 0;

    private int health = 100;
    private int fuel = 100;
    private TextureRegion healthBar;
    private TextureRegion fuelBar;

    public Helicopter(ActorType t) {
        super(t, AtlasCache.get("copter"), 0.02f, 1f, false);

        Array<AtlasRegion> east = AtlasCache.get("copter").findRegions(t.getName());
        for (TextureRegion tr : east) {
            tr.flip(true, false);
        }

        this.flipped = new Animation(0.02f, east);

        this.turningRight = AtlasCache.get("copter").findRegions("turning");
        this.turningLeft = AtlasCache.get("copter").findRegions("turning");
        this.turningLeft.reverse();

        healthBar = new TextureRegion(RescueRaiders.fillRectangle(SCREEN_WIDTH, STATUS_BAR_HEIGHT, Color.GREEN));
        fuelBar = new TextureRegion(RescueRaiders.fillRectangle(SCREEN_WIDTH, STATUS_BAR_HEIGHT, Color.ORANGE));

        ax = 0.0f;
        ay = 0.0f;
        avx = 0.0f;
        avy = 0.0f;
        ak = 0.03f;// spring constant
        ad = 0.7f; // damping factor

        Sounds.play(Sound.HELICOPTER_ENGINE);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        angle = -ax * 0.008f;// angleFactor = 0.005

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

    public float yup(float y) {
        return RescueRaiders.yup(y);
    }

    public float getAngle() {
        return this.angle;
    }

    public void takeDamage(int damage) {
        health -= damage;

        double percent = (double) this.health / 100.0;
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

    public boolean isAlive() {
        return health > 0;
    }

    private void crash() {
        // trigger explosion, game over, etc.
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

    public boolean isWest() {
        return this.west;
    }

    public void shoot(Stage stage) {
        float angleInDegrees = west ? 180 + angle * 100 : angle < 0 ? 360 + angle * 100 : angle * 100;
        Bullet b = new Bullet(this, west ? this.getX() + 15 : this.getX() + 55, this.getY() + 10, angleInDegrees);
        stage.addActor(b);
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

        pvx *= pd;
        pvy *= pd;
        avx += -ak * ax;
        avx *= ad;
        avy += -ak * ay;
        avy *= ad;

        setX(px);
        setY(yup(py));

        hitbox.x = px;
        hitbox.y = yup(py);

        //System.out.println("velocityX: " +ax+ " velocityY: " +ay);
    }

    public void checkCrash() {
        if (Math.abs(ax) > 5) {
            //crash
        }
        py = FIELD_HEIGHT;

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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
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
