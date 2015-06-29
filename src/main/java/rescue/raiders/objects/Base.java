package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Base extends Actor {

    public Base(ActorType t) {
        super(t, AtlasCache.get("backgrounds"), .65f, false);
        this.setUserObject(createMiniIcon(t.getIconColor(), 12, 8));
    }

}
