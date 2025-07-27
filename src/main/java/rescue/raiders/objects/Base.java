package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.createMiniIcon;
import rescue.raiders.util.AtlasCache;

public class Base extends Actor {

    public Base(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 1f, false);
        this.setUserObject(createMiniIcon(t.getIconColor(), 6, 6));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - 5);
    }

}
