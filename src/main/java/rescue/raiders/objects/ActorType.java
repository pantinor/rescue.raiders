package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;
import java.lang.reflect.Constructor;

public enum ActorType {

    HELI("shobu-copter", "copter-with-blades", Color.WHITE, false, Copter.class),
    ENEMY_HELI("copter", "copter", Color.YELLOW, true, EnemyCopter.class),
    //
    ENGINEER("soldier", "engineer", Color.ROYAL, false, Engineer.class),
    ENEMY_ENGINEER("soldier", "enemy-engineer", Color.SCARLET, true, Engineer.class),
    INFANTRY("soldier", "infantry", Color.BLUE, false, Infantry.class),
    ENEMY_INFANTRY("soldier", "enemy-infantry", Color.RED, true, Infantry.class),
    //
    TANK("tan-tank", "driving", Color.SKY, false, TanTank.class),
    ENEMY_TANK("grey-tank", "driving", Color.SCARLET, true, GreyTank.class),
    BLUE_TANK("blue-tank", "driving", Color.NAVY, false, BlueTank.class),
    ENEMY_LARGE_TANK("large-tank", "driving", Color.PINK, true, LargeTank.class),
    JEEP("jeep", "jeep", Color.BLUE, false, Jeep.class),
    ENEMY_JEEP("jeep", "jeep", Color.RED, true, Jeep.class),
    TREAD_TRUCK("tread-truck", "truck", Color.BLUE, false, TreadTruck.class),
    ENEMY_TREAD_TRUCK("tread-truck", "truck", Color.RED, true, TreadTruck.class),
    COVERED_TRUCK("covered-truck", "truck", Color.BLUE, false, CoveredTruck.class),
    ENEMY_COVERED_TRUCK("covered-truck", "truck", Color.RED, true, CoveredTruck.class),
    ROCKET_LAUNCHER("rocket-launcher", "launcher", Color.BLUE, false, RocketLauncher.class),
    ENEMY_ROCKET_LAUNCHER("rocket-launcher", "launcher", Color.RED, true, RocketLauncher.class),
    //
    BASE("backgrounds", "base", Color.GREEN, false, Base.class),
    ENEMY_BASE("backgrounds", "enemy-base", Color.ORANGE, true, Base.class),
    TURRET("turret", "turret", Color.WHITE, false, AAGun.class),
    ENEMY_TURRET("turret", "enemy-turret", Color.RED, true, AAGun.class),
    PAD("backgrounds", "pad", Color.GREEN, false, Pad.class),
    ENEMY_PAD("backgrounds", "pad", Color.ORANGE, true, Pad.class),
    BALLON("backgrounds", "blimp", Color.GRAY, false, Balloon.class),
    ENEMY_BALLON("backgrounds", "enemy-blimp", Color.RED, true, Balloon.class),
    HUT("backgrounds", "hut", Color.GRAY, false, Hut.class),
    ENEMY_HUT("backgrounds", "enemy-hut", Color.RED, true, Hut.class);

    private final String atlasName;
    private final String regionName;
    private final Color iconColor;
    private final boolean isEnemy;
    private final Class clazz;

    private ActorType(String atlasName, String regionName, Color iconColor, boolean isEnemy, Class clazz) {
        this.atlasName = atlasName;
        this.regionName = regionName;
        this.iconColor = iconColor;
        this.isEnemy = isEnemy;
        this.clazz = clazz;
    }

    public String getAtlasName() {
        return atlasName;
    }

    public String getRegionName() {
        return regionName;
    }

    public Color getIconColor() {
        return iconColor;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public Class getClazz() {
        return clazz;
    }

    public Actor getInstance() {
        Actor actor = null;
        try {
            Constructor constructor = this.clazz.getConstructor(ActorType.class);
            actor = (Actor) constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actor;
    }

}
