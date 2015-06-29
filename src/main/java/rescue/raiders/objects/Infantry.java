package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Infantry extends Actor {

    public Infantry(ActorType t) {
        super(t, AtlasCache.get("soldier"),  0.10f, 1f, true);
    }

}
