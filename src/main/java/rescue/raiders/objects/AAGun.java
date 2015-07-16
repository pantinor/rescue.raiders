package rescue.raiders.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rescue.raiders.game.RescueRaiders;
import rescue.raiders.util.AtlasCache;


public class AAGun extends Actor {

    public AAGun(ActorType t) {
        super(t, AtlasCache.get("turret"), .05f, .65f, false);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        float index = getFrameRelativeMouse(RescueRaiders.heli.getX(), RescueRaiders.heli.getY(), this.getX(), this.getY());
        TextureRegion frame = anim.getKeyFrames()[(int)index];
        batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
    }
    
    
    private float getFrameRelativeMouse(float mouseX, float mouseY, float originX, float originY) {
        float ang = (float) Math.toDegrees(Math.atan2(mouseX - originX, originY - mouseY));
        if (ang < 0) {
            ang = 360 + ang;
        }
        ang = (ang - 90) % 360;
        ang = ang - 45;

        float frame = ang / (90.0f/16.0f);
        
        if (frame < 0) {
            frame = 0;
        } else if (frame > 15f) {
            frame = 15f;
        }
        return frame;
    }

}
