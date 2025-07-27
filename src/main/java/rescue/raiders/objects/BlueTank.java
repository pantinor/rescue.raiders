package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.createMiniIcon;
import rescue.raiders.util.AtlasCache;

public class BlueTank extends ShootableActor {

    public BlueTank(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, false);
        health = 20;
        maxHealth = 20;
        this.setUserObject(createMiniIcon(t.getIconColor(), 6, 6));
    }

}
