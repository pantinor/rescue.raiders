package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Infantry extends ShootableActor {

    public Infantry(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 5;
        maxHealth = 5;
    }

}
