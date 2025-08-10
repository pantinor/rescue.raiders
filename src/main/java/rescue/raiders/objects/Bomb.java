package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import rescue.raiders.game.GameStage;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;

public class Bomb extends com.badlogic.gdx.scenes.scene2d.Actor {

    private final Rectangle hitbox;
    private final rescue.raiders.objects.Actor source;
    private final int damage;

    private static final float GRAVITY = -900f;
    private static final float TERMINAL_VEL = -900f;
    private float vx;
    private float vy;

    private final Texture texture;

    public Bomb(rescue.raiders.objects.Actor source, Texture texture, float startX, float startY, int damage) {
        this.source = source;

        this.hitbox = new Rectangle(0, 0, 3, 3);
        this.damage = damage;
        this.texture = texture;

        setPosition(startX, startY);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        hitbox.x = x;
        hitbox.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.texture, this.getX(), this.getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        vy += GRAVITY * delta;
        if (vy < TERMINAL_VEL) {
            vy = TERMINAL_VEL;
        }

        float newX = getX() + vx * delta;
        float newY = getY() + vy * delta;

        setPosition(newX, newY);

        if (newY < FIELD_HEIGHT) {
            this.getStage().addActor(new Explosion(Explosion.Type.MEDIUM, newX, newY));
            remove();
            return;
        }

        GameStage stage = (GameStage) getStage();
        if (stage == null) {
            return;
        }

        boolean sourceIsEnemy = source.type.isEnemy();
        List<rescue.raiders.objects.Actor> actors = stage.gameActors();

        for (rescue.raiders.objects.Actor target : actors) {
            if (target == source) {
                continue;
            }

            if (target.hits(hitbox)) {
                if (target.type.isEnemy() ^ sourceIsEnemy) {
                    target.takeDamage(this.damage);
                }
                this.getStage().addActor(new Explosion(Explosion.Type.LARGE, newX, newY));
                remove();
                break;
            }
        }
    }

    private void explode() {

    }

}
