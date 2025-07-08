package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Balloon extends Actor {

    public Balloon(ActorType t) {
        super(t, AtlasCache.get("balloon"), .2f, 1f, false);
    }

}
