package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import java.util.List;
import rescue.raiders.game.GameStage;

public class Bullet extends com.badlogic.gdx.scenes.scene2d.Actor {

    private Rectangle hitbox;
    private final float radians;
    private final rescue.raiders.objects.Actor source;
    private final float startX;
    private final float startY;
    private final int damage;

    private static final float SPEED = 1000f;
    public static final float MAX_BULLET_DISTANCE = 900;
    private static TextureRegion TEXTURE;

    static {
        Pixmap pix = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pix.setColor(1f, 1f, 1f, .85f);
        pix.fillRectangle(0, 0, 3, 3);
        TEXTURE = new TextureRegion(new Texture(pix));
        pix.dispose();
    }

    public Bullet(rescue.raiders.objects.Actor source, float startX, float startY, float degrees, int damage) {
        this.source = source;
        this.radians = MathUtils.degreesToRadians * degrees;
        this.hitbox = new Rectangle(0, 0, 3, 3);
        this.startX = startX;
        this.startY = startY;
        this.damage = damage;

        setPosition(startX, startY);

        SequenceAction seq1 = Actions.action(SequenceAction.class);
        seq1.addAction(Actions.delay(1.5f));
        seq1.addAction(Actions.removeActor(this));
        addAction(seq1);
    }

    public boolean hits(Rectangle r) {
        if (hitbox.overlaps(r)) {
            return true;
        }
        return false;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        hitbox.x = x;
        hitbox.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(TEXTURE, this.getX(), this.getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float newX = getX() + SPEED * (float) Math.cos(radians) * delta;
        float newY = getY() + SPEED * (float) Math.sin(radians) * delta;
        setPosition(newX, newY);
        hitbox.setPosition(newX, newY);

        float distance = Vector2.dst(startX, startY, newX, newY);
        if (distance > MAX_BULLET_DISTANCE) {
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

            float adist = Vector2.dst(source.getX(), source.getY(), target.getX(), target.getY());
            if (adist > MAX_BULLET_DISTANCE) {
                continue;
            }

            if (target.hits(hitbox) && target.type.isEnemy() ^ sourceIsEnemy) {
                target.takeDamage(this.damage);
                remove();// remove bullet on impact
                break;
            }
        }

    }

    public Rectangle getHitBox() {
        return hitbox;
    }

}
