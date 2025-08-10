package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;
import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadLocalRandom;

public enum ActorType {

    HELI("shobu-copter", "copter-with-blades", Color.WHITE, false, Copter.class),
    ENEMY_HELI("copter", "copter", Color.YELLOW, true, EnemyCopter.class),
    //

    TOMMY_GUNNER("soldier", "tommy-running", "tommy-aiming", Color.BLUE, false, Infantry.class),
    M60_GUNNER("soldier", "m60-running", "m60-aiming", Color.BLUE, false, Infantry.class),
    MG_GUNNER("soldier", "mg-running", "mg-aiming", Color.BLUE, false, Infantry.class),
    BAZOOKA("soldier", "bazooka-running", "bazooka-aiming", Color.BLUE, false, Engineer.class),
    RIFLEMAN("soldier", "rifle-running", "rifle-aiming", Color.BLUE, false, Engineer.class),
    FLAMETHROWER("soldier", "flame-running", "flame-aiming", Color.BLUE, false, Engineer.class),
    //
    ENEMY_TOMMY_GUNNER("soldier", "gray-tommy-running", "gray-tommy-aiming", Color.RED, true, Infantry.class),
    ENEMY_M60_GUNNER("soldier", "gray-m60-running", "gray-m60-aiming", Color.RED, true, Infantry.class),
    ENEMY_MG_GUNNER("soldier", "gray-mg-running", "gray-mg-aiming", Color.RED, true, Infantry.class),
    ENEMY_FLAMETHROWER("soldier", "gray-flame-running", "gray-flame-aiming", Color.RED, true, Engineer.class),
    ENEMY_BAZOOKA("soldier", "gray-bazooka-running", "gray-bazooka-aiming", Color.RED, true, Engineer.class),
    ENEMY_RIFLEMAN("soldier", "gray-rifle-running", "gray-rifle-aiming", Color.RED, true, Engineer.class),
    //
    TANK("tan-tank", "driving", Color.SKY, false, TanTank.class),
    ENEMY_TANK("grey-tank", "driving", Color.SCARLET, true, GreyTank.class),
    BLUE_TANK("blue-tank", "driving", Color.NAVY, false, BlueTank.class),
    ENEMY_LARGE_TANK("large-tank", "driving", Color.PINK, true, LargeTank.class),
    //
    JEEP("jeep", "jeep", Color.BLUE, false, Jeep.class),
    ENEMY_JEEP("jeep", "jeep", Color.RED, true, Jeep.class),
    TREAD_TRUCK("tread-truck", "truck", Color.BLUE, false, TreadTruck.class),
    ENEMY_TREAD_TRUCK("tread-truck", "truck", Color.RED, true, TreadTruck.class),
    COVERED_TRUCK("covered-truck", "truck", Color.BLUE, false, CoveredTruck.class),
    ENEMY_COVERED_TRUCK("covered-truck", "truck", Color.RED, true, CoveredTruck.class),
    //
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

    private static final ActorType[] FRIENDLY_INFANTRY = {
        TOMMY_GUNNER, M60_GUNNER, MG_GUNNER
    };
    private static final ActorType[] ENEMY_INFANTRY = {
        ENEMY_TOMMY_GUNNER, ENEMY_M60_GUNNER, ENEMY_MG_GUNNER
    };
    private static final ActorType[] FRIENDLY_ENGINEERS = {
        BAZOOKA, RIFLEMAN, FLAMETHROWER
    };
    private static final ActorType[] ENEMY_ENGINEERS = {
        ENEMY_BAZOOKA, ENEMY_RIFLEMAN, ENEMY_FLAMETHROWER
    };

    private final String atlasName;
    private final String regionName;
    private final String aimingName;
    private final Color iconColor;
    private final boolean isEnemy;
    private final Class clazz;

    private ActorType(String atlasName, String regionName, Color iconColor, boolean isEnemy, Class clazz) {
        this.atlasName = atlasName;
        this.regionName = regionName;
        this.aimingName = null;
        this.iconColor = iconColor;
        this.isEnemy = isEnemy;
        this.clazz = clazz;
    }

    private ActorType(String atlasName, String regionName, String aimingName, Color iconColor, boolean isEnemy, Class clazz) {
        this.atlasName = atlasName;
        this.regionName = regionName;
        this.aimingName = aimingName;
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

    public String getAimingName() {
        return aimingName;
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

    private static Actor randomFrom(ActorType[] pool) {
        int i = ThreadLocalRandom.current().nextInt(pool.length);
        return pool[i].getInstance();
    }

    public static Actor getInfantry(boolean enemy) {
        return randomFrom(enemy ? ENEMY_INFANTRY : FRIENDLY_INFANTRY);
    }

    public static Actor getEngineer(boolean enemy) {
        return randomFrom(enemy ? ENEMY_ENGINEERS : FRIENDLY_ENGINEERS);
    }

}
