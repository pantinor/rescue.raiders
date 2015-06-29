package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;


public class AAGun extends Actor {

    public AAGun(ActorType t) {
        super(t, AtlasCache.get("turret"), .65f, false);
    }

}
