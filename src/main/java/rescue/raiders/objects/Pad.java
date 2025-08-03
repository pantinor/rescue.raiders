package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Pad extends Actor {

    public Pad(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), .65f, false);
        canCollide = false;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - 5);
    }
}
