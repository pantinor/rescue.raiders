package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Engineer extends Actor {

    public Engineer(ActorType t) {
        super(t, AtlasCache.get("soldier"), 0.10f, 1f, true);
    }

}
