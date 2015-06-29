package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;
import java.lang.reflect.Constructor;

public enum ActorType {

    HELI("copter", Color.BLUE, false, Helicopter.class),
    ENEMY_HELI("enemy-copter", Color.RED, true, Helicopter.class),
    
    TANK("tank", Color.YELLOW, false, Tank.class),
    ENEMY_TANK("enemy-tank", Color.MAGENTA, true, Tank.class),
    
    ENGINEER("engineer", Color.BLUE, false, Engineer.class),
    ENEMY_ENGINEER("enemy-engineer", Color.RED, true, Engineer.class),
    
    INFANTRY("infantry", Color.BLUE, false, Infantry.class),
    ENEMY_INFANTRY("enemy-infantry", Color.RED, true, Infantry.class),
    
    JEEP("jeep", Color.BLUE, false, Jeep.class),
    ENEMY_JEEP("enemy-jeep", Color.RED, true, Jeep.class),
    
    BASE("base", Color.CYAN, false, Base.class),
    ENEMY_BASE("enemy-base", Color.PURPLE, true, Base.class),
    
    TURRET("turret", Color.WHITE, false, AAGun.class),
    ENEMY_TURRET("enemy-turret", Color.RED, true, AAGun.class),
    
    PAD("pad", Color.GREEN, false, Pad.class),
    ENEMY_PAD("pad", Color.OLIVE, true, Pad.class),
    
    BALLON("balloon", Color.ORANGE, false, Balloon.class),
    ENEMY_BALLON("enemy-balloon", Color.RED, true, Balloon.class),
    
    HUT("hut", Color.TEAL, false, Hut.class),
    ENEMY_HUT("enemy-hut", Color.NAVY, true, Hut.class);

    private final String name;
    private final Color iconColor;
    private final boolean isEnemy;
    private final Class clazz;

    private ActorType(String name, Color iconColor, boolean isEnemy, Class clazz) {
        this.name = name;
        this.iconColor = iconColor;
        this.isEnemy = isEnemy;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Color getIconColor() {
        return iconColor;
    }

    public boolean isIsEnemy() {
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
