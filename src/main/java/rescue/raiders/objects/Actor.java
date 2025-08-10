package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import rescue.raiders.game.GameStage;
import static rescue.raiders.game.RescueRaiders.GAME;
import rescue.raiders.util.Sound;
import rescue.raiders.util.Sounds;
import static rescue.raiders.game.RescueRaiders.fillRectangle;

public class Actor extends com.badlogic.gdx.scenes.scene2d.Actor {

    ActorType type;
    Rectangle hitbox;
    TextureRegion tr;
    Animation<TextureRegion> anim;
    float frameCounter = 0;
    float scale;

    int health = 20;
    int maxHealth = 20;
    boolean canCollide = true;

    //stationary actors - hut, pad, base
    public Actor(ActorType t, TextureAtlas atlas, float scale, boolean flip) {
        super();
        this.type = t;
        setName(t.toString().toLowerCase());
        this.scale = scale;

        TextureAtlas.AtlasRegion original = atlas.findRegion(t.getRegionName());
        this.tr = new TextureAtlas.AtlasRegion(original);
        this.tr.flip(flip, false);

        this.hitbox = new Rectangle(0, 0, tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
        this.setUserObject(fillRectangle(t.getIconColor(), 4, 4));
    }

    //moving actors - infantry, engineer, balloon, AAGun, Tank, Jeep, Helicopter
    public Actor(ActorType t, TextureAtlas atlas, float animframeRate, float scale, boolean flip) {
        super();
        this.type = t;
        setName(t.toString().toLowerCase());
        this.scale = scale;
        Array<AtlasRegion> regions = atlas.findRegions(t.getRegionName());
        this.anim = new Animation(animframeRate, regions);
        TextureRegion r = anim.getKeyFrame(0, true);
        this.hitbox = new Rectangle(0, 0, r.getRegionWidth() * scale, r.getRegionHeight() * scale);
        if (!t.isEnemy()) {
            for (TextureRegion ltr : regions) {
                ltr.flip(flip, false);
            }
        }
        this.setUserObject(fillRectangle(t.getIconColor(), 4, 4));
    }
    
    public ActorType type() {
        return this.type;
    }

    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;

            if (health <= 0) {
                Sounds.play(Sound.SPLAT);
                GAME.addExplosion(getX(), getY(), !this.type.isEnemy(), 10);
                remove();
            }
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean hits(Rectangle r) {
        return hitbox.overlaps(r);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        hitbox.x = x;
        hitbox.y = y;
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        hitbox.x = x;
        hitbox.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        frameCounter += Gdx.graphics.getDeltaTime();
        TextureRegion frame = (anim != null ? anim.getKeyFrame(frameCounter, true) : this.tr);
        batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
    }

    public Rectangle getHitBox() {
        return hitbox;
    }

    @Override
    public boolean remove() {
        if (this.getStage() != null) {
            GameStage gs = (GameStage) this.getStage();
            gs.removeActor(this);
        }
        return super.remove();
    }

}
