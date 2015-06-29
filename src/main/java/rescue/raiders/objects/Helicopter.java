package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.*;
import rescue.raiders.game.RescueRaiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.util.AtlasCache;

public class Helicopter extends Actor implements InputProcessor {

    float ax, avx, ay, avy, ak, ad, angle;
    boolean left, right, up, down;
    float px = SCREEN_WIDTH / 2, py = SCREEN_HEIGHT / 2, pvx = 0.0f, pvy = 0.0f, pd = 0.9f;
    long lastKeyUp;
    boolean west;

    Animation flipped;
    Array<AtlasRegion> turningLeft;
    Array<AtlasRegion> turningRight;
    int turningIndex = 0;
    
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

        ax = 0.0f;
        ay = 0.0f;
        avx = 0.0f;
        avy = 0.0f;
        ak = 0.03f;// spring constant
        ad = 0.7f; // damping factor
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

    public float yup(float y) {
        return RescueRaiders.yup(y);
    }

    public float getAngle() {
        return this.angle;
    }

    public boolean isWest() {
        return this.west;
    }

    public void shoot(Stage stage) {
        float angleInDegrees = west ? 180 + angle * 100 : angle < 0 ? 360 + angle * 100 : angle * 100;
        Bullet b = new Bullet(west ? this.getX() + 15 : this.getX() + 55, this.getY() + 10, angleInDegrees);
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

        long diff = System.currentTimeMillis() - lastKeyUp;

        switch (keycode) {
            case Keys.LEFT:
                if (diff < 700) {
                    if (!west) {
                        turningIndex = 0;
                    }
                    west = true;
                }
                left = true;
                break;
            case Keys.RIGHT:
                if (diff < 700) {
                    if (west) {
                        turningIndex = 0;
                    }
                    west = false;
                }
                right = true;
                break;
            case Keys.UP:
                up = true;
                break;
            case Keys.DOWN:
                down = true;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Keys.LEFT:
                lastKeyUp = System.currentTimeMillis();
                left = false;
                break;
            case Keys.RIGHT:
                lastKeyUp = System.currentTimeMillis();
                right = false;
                break;
            case Keys.UP:
                up = false;
                break;
            case Keys.DOWN:
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
        // TODO Auto-generated method stub
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
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

}
