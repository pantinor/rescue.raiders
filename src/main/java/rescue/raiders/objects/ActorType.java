package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;
import java.lang.reflect.Constructor;

public enum ActorType {

    HELI("copter", "copter", Color.BLUE, false, Helicopter.class),
    ENEMY_HELI("enemy-copter", "copter1", Color.RED, true, EnemyCopter.class),
    //
    ENGINEER("soldier", "engineer", Color.BLUE, false, Engineer.class),
    ENEMY_ENGINEER("soldier", "enemy-engineer", Color.RED, true, Engineer.class),
    INFANTRY("soldier", "infantry", Color.BLUE, false, Infantry.class),
    ENEMY_INFANTRY("soldier", "enemy-infantry", Color.RED, true, Infantry.class),
    //
    TANK("tank", "tank", Color.YELLOW, false, Tank.class),
    ENEMY_TANK("tank", "tank", Color.MAGENTA, true, Tank.class),
    JEEP("jeep", "jeep", Color.BLUE, false, Jeep.class),
    ENEMY_JEEP("jeep", "jeep", Color.RED, true, Jeep.class),
    TREAD_TRUCK("tread-truck", "truck", Color.BLUE, false, TreadTruck.class),
    ENEMY_READ_TRUCK("tread-truck", "truck", Color.RED, true, TreadTruck.class),
    COVERED_TRUCK("covered-truck", "truck", Color.BLUE, false, CoveredTruck.class),
    ENEMY_COVERED_TRUCK("covered-truck", "truck", Color.RED, true, CoveredTruck.class),
    ROCKET_LAUNCHER("rocket-launcher", "launcher", Color.BLUE, false, RocketLauncher.class),
    ENEMY_ROCKET_LAUNCHER("rocket-launcher", "launcher", Color.RED, true, RocketLauncher.class),
    //
    BASE("backgrounds", "base", Color.CYAN, false, Base.class),
    ENEMY_BASE("backgrounds", "enemy-base", Color.PURPLE, true, Base.class),
    TURRET("turret", "turret", Color.WHITE, false, AAGun.class),
    ENEMY_TURRET("turret", "enemy-turret", Color.RED, true, AAGun.class),
    PAD("backgrounds", "pad", Color.GREEN, false, Pad.class),
    ENEMY_PAD("backgrounds", "pad", Color.OLIVE, true, Pad.class),
    BALLON("balloon", "balloon-green", Color.ORANGE, false, Balloon.class),
    ENEMY_BALLON("balloon", "balloon-red", Color.RED, true, Balloon.class),
    HUT("backgrounds", "hut", Color.TEAL, false, Hut.class),
    ENEMY_HUT("backgrounds", "hut", Color.NAVY, true, Hut.class);

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
