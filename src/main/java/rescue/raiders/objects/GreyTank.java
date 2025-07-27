package rescue.raiders.objects;

import static rescue.raiders.game.RescueRaiders.createMiniIcon;
import rescue.raiders.util.AtlasCache;

public class GreyTank extends ShootableActor {

    public GreyTank(ActorType t) {
        super(t, AtlasCache.get(t.getAtlasName()), 0.10f, 1f, true);
        health = 20;
        maxHealth = 20;
        this.setUserObject(createMiniIcon(t.getIconColor(), 6, 6));
    }

}
