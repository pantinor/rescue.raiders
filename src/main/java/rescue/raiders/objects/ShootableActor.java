package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.List;
import rescue.raiders.game.GameStage;
import static rescue.raiders.game.RescueRaiders.FIELD_WIDTH;
import rescue.raiders.util.Sound;

public class ShootableActor extends Actor {

    private float shootingTime = 0f;
    private boolean shooting = false;

    public ShootableActor(ActorType t, TextureAtlas atlas, float frameRate, float scale, boolean flip) {
        super(t, atlas, frameRate, scale, flip);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        frameCounter += Gdx.graphics.getDeltaTime();
        if (shooting) {
            TextureRegion stx = shootingTexture() != null ? shootingTexture() : this.tr;
            TextureRegion frame = (shootingAnim() != null ? shootingAnim().getKeyFrame(frameCounter, true) : stx);
            batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
        } else {
            TextureRegion frame = (anim != null ? anim.getKeyFrame(frameCounter, true) : this.tr);
            batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.shooting = false;
        boolean move = true;
        Vector2 thisPos = new Vector2(getX(), getY());

        GameStage stage = (GameStage) getStage();
        List<rescue.raiders.objects.Actor> actors = stage.gameActors();

        for (rescue.raiders.objects.Actor other : actors) {

            if (other == this) {
                continue;
            }

            if (type.isEnemy() && other.getX() > this.getX()) {
                continue;
            }

            if (!type.isEnemy() && other.getX() < this.getX()) {
                continue;
            }

            boolean enemies = this.type.isEnemy() != other.type.isEnemy();

            if (enemies) {

                Vector2 otherPos = new Vector2(other.getX(), other.getY());
                float distance = thisPos.dst(otherPos);

                if (distance <= 200) {
                    shoot();
                    move = false;
                    break;
                }
            }
        }

        if (move) {
            setX(type.isEnemy() ? getX() - 1 : getX() + 1);
            hitbox.x = this.getX();
            hitbox.y = this.getY();
        }

        if (getX() < 0 || getX() > FIELD_WIDTH) {
            this.remove();
        }
    }

    public TextureRegion shootingTexture() {
        return null;
    }

    public Animation<TextureRegion> shootingAnim() {
        return null;
    }

    public float shootingTimeDuration() {
        return 0.60f;
    }

    public int bulletDamage() {
        return 1;
    }

    public Sound shootingSound() {
        return Sound.INFANTRY_GUNFIRE;
    }

    private void shoot() {
        shooting = true;
        shootingTime += Gdx.graphics.getDeltaTime();
        if (shootingTime >= shootingTimeDuration()) {
            float angleInDegrees = this.type.isEnemy() ? 180 : 0;
            Bullet b = new Bullet(this, this.getX() + this.hitbox.getWidth() / 2, this.getY() + 13, angleInDegrees, bulletDamage());
            getStage().addActor(b);
            shootingTime = 0;
            //Sounds.play(shootingSound());
        }

    }

}
