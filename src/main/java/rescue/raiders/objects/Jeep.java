package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Jeep extends Actor {

    public Jeep(ActorType t) {
        super(t, AtlasCache.get(t.getName()), 0.10f, 1f, true);
    }
}
