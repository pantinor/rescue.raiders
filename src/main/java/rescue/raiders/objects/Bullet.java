package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

public class Bullet extends com.badlogic.gdx.scenes.scene2d.Actor {

    Rectangle hitbox;
    private static TextureRegion tr;
    private final float radians;
    private static final float speed = 1000;
    private final Object source;

    static {
        Pixmap pix = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pix.setColor(1f, 1f, 1f, .85f);
        pix.fillRectangle(0, 0, 3, 3);
        tr = new TextureRegion(new Texture(pix));
        pix.dispose();
    }

    public Bullet(Object source, float startX, float startY, float degrees) {
        this.source = source;
        this.radians = MathUtils.degreesToRadians * degrees;
        this.hitbox = new Rectangle(0, 0, 3, 3);
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
        batch.draw(tr, this.getX(), this.getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float vx = (float) (speed * Math.cos(radians));
        float vy = (float) (speed * Math.sin(radians));

        float dt = Gdx.graphics.getDeltaTime();
        float dx = vx * dt;
        float dy = vy * dt;

        setPosition(getX() + dx, getY() + dy);

        hitbox.x = this.getX();
        hitbox.y = this.getY();

        if (this.getStage() != null) {
            Array<com.badlogic.gdx.scenes.scene2d.Actor> actors = this.getStage().getActors();
            for (int i = 0; i < actors.items.length; i++) {
                
                if (this.source.equals(actors.items[i])) {
                    continue; // don't hit the shooter
                }

                if (actors.items[i] instanceof rescue.raiders.objects.Actor) {
                    rescue.raiders.objects.Actor target = (rescue.raiders.objects.Actor) actors.items[i];
                    if (target.hits(this.hitbox)) {

                        if (target instanceof Helicopter && !(this.source instanceof Helicopter)) {
                            ((Helicopter) target).takeDamage(1);
                        } else if (target instanceof AAGun && !(this.source instanceof AAGun)) {
                            ((AAGun) target).takeDamage(1);
                        }

                        this.remove(); // Remove bullet on impact
                        break;
                    }
                }
            }
        }
    }

    public Rectangle getHitBox() {
        return hitbox;
    }

}
