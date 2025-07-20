package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

public class Engineer extends ShootableActor {

    public Engineer(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 5;
        maxHealth = 5;
    }

}
