package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Actor extends com.badlogic.gdx.scenes.scene2d.Actor {

    ActorType type;
    Rectangle hitbox;
    TextureRegion tr;
    Animation anim;
    float frameCounter = 0;
    float scale;
    boolean flip;
    
    public Actor(ActorType t) {
        super();
        this.type = t;
    }

    public Actor(ActorType t, TextureAtlas atlas, float scale, boolean flip) {
        super();
        this.type = t;
        setName(t.getName());
        this.scale = scale;
        this.flip = flip;
        this.tr = atlas.findRegion(t.getName());
        tr.flip(flip, false);
        this.hitbox = new Rectangle(0, 0, tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
        this.setUserObject(createMiniIcon(t.getIconColor(), 4, 4));
    }

    public Actor(ActorType t, TextureAtlas atlas, float frameRate, float scale, boolean flip) {
        super();
        this.type = t;
        setName(t.getName());
        this.scale = scale;
        this.flip = flip;
        Array<AtlasRegion> regions = atlas.findRegions(t.getName());
        this.anim = new Animation(frameRate, regions);
        TextureRegion r = anim.getKeyFrame(0, true);
        this.hitbox = new Rectangle(0, 0, r.getRegionWidth() * scale, r.getRegionHeight() * scale);
        for (TextureRegion tr : regions) {
            tr.flip(flip, false);
        }
        this.setUserObject(createMiniIcon(t.getIconColor(), 4, 4));
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
        frameCounter += Gdx.graphics.getDeltaTime();
        TextureRegion frame = (anim != null ? anim.getKeyFrame(frameCounter, true) : this.tr);
        batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        hitbox.x = this.getX();
        hitbox.y = this.getY();
    }

    public Rectangle getHitBox() {
        return hitbox;
    }

    public final TextureRegion createMiniIcon(Color c, int w, int h) {
        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pix.setColor(c.r, c.g, c.b, .85f);
        pix.fillRectangle(0, 0, w, h);
        TextureRegion t = new TextureRegion(new Texture(pix));
        pix.dispose();
        return t;
    }

}
