package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

public class Bullet extends com.badlogic.gdx.scenes.scene2d.Actor {

    private Rectangle hitbox;
    private final float radians;
    private final rescue.raiders.objects.Actor source;
    private final float startX;
    private final float startY;

    private static final float SPEED = 1000f;
    private static final float MAX_BULLET_DISTANCE = 900;
    public static final float MAX_BULLET_DISTANCE_SQUARED = MAX_BULLET_DISTANCE * MAX_BULLET_DISTANCE;
    private static TextureRegion TEXTURE;

    static {
        Pixmap pix = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pix.setColor(1f, 1f, 1f, .85f);
        pix.fillRectangle(0, 0, 3, 3);
        TEXTURE = new TextureRegion(new Texture(pix));
        pix.dispose();
    }

    public Bullet(rescue.raiders.objects.Actor source, float startX, float startY, float degrees) {
        this.source = source;
        this.radians = MathUtils.degreesToRadians * degrees;
        this.hitbox = new Rectangle(0, 0, 3, 3);
        this.startX = startX;
        this.startY = startY;

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

        float distance = Vector2.dst2(startX, startY, newX, newY);
        if (distance > MAX_BULLET_DISTANCE_SQUARED) {
            remove();
            return;
        }

        Stage stage = getStage();
        if (stage == null) {
            return;
        }

        Array<com.badlogic.gdx.scenes.scene2d.Actor> actors = stage.getActors();
        boolean sourceIsEnemy = source.type.isEnemy();

        for (int i = 0, n = actors.size; i < n; i++) {
            com.badlogic.gdx.scenes.scene2d.Actor actor = actors.get(i);
            if (actor == source) {
                continue;
            }
            if (!(actor instanceof rescue.raiders.objects.Actor)) {
                continue;
            }

            float adist = Vector2.dst2(source.getX(), source.getY(), actor.getX(), actor.getY());
            if (adist > MAX_BULLET_DISTANCE_SQUARED) {
                continue;
            }

            rescue.raiders.objects.Actor target = (rescue.raiders.objects.Actor) actor;

            if (target.hits(hitbox) && target.type.isEnemy() ^ sourceIsEnemy) {
                target.takeDamage(1);
                remove();// remove bullet on impact
                break;
            }
        }

    }

    public Rectangle getHitBox() {
        return hitbox;
    }

}
