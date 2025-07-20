package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.createMiniIcon;
import rescue.raiders.util.AtlasCache;

public class Base extends Actor {

    public Base(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), .65f, false);
        this.setUserObject(createMiniIcon(t.getIconColor(), 12, 8));
    }

}
