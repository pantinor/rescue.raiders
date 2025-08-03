package rescue.raiders.levels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import rescue.raiders.objects.ActorType;

public class MidnightOasis extends Level {

    public MidnightOasis(Stage stage) {
        super(stage);

        this.layout = new Object[][]{
            {ActorType.BASE, 24},
            {ActorType.PAD, 320},
            {ActorType.INFANTRY, 784},
            {ActorType.INFANTRY, 848},
            {ActorType.ENEMY_ROCKET_LAUNCHER, 1168},
            {ActorType.INFANTRY, 1024},
            {ActorType.INFANTRY, 1088},
            {ActorType.INFANTRY, 1616},
            {ActorType.INFANTRY, 1776},
            {ActorType.ENEMY_ROCKET_LAUNCHER, 1936},
            {ActorType.INFANTRY, 2560},
            {ActorType.INFANTRY, 2800},
            {ActorType.INFANTRY, 3040},
            {ActorType.ENEMY_TANK, 704},
            {ActorType.ENEMY_TANK, 768},
            {ActorType.ENEMY_TANK, 944},
            {ActorType.ENEMY_TANK, 1008},
            {ActorType.ENEMY_TANK, 1104},
            {ActorType.ENEMY_TANK, 1184},
            {ActorType.ENEMY_TANK, 1536},
            {ActorType.ENEMY_HUT, 2688},
            {ActorType.ENEMY_TANK, 2720},
            {ActorType.ENEMY_TANK, 2960},
            {ActorType.ENEMY_TANK, 3120},
            {ActorType.ENEMY_TANK, 3200},
            {ActorType.HUT, 3584},
            {ActorType.ENEMY_TANK, 3584},
            {ActorType.HUT, 4096},
            {ActorType.INFANTRY, 3904},
            {ActorType.INFANTRY, 4096},
            {ActorType.ENEMY_HUT, 3328},
            {ActorType.ENEMY_TURRET, 1888},
            {ActorType.ENEMY_HUT, 4000},
            {ActorType.ENEMY_ROCKET_LAUNCHER, 3984},
            {ActorType.ENEMY_ROCKET_LAUNCHER, 4416},
            {ActorType.ENEMY_TURRET, 4224},
            {ActorType.ENEMY_TURRET, 4704},
            {ActorType.ENEMY_TURRET, 4704},
            {ActorType.HUT, 4608},
            {ActorType.ENEMY_HUT, 5120},
            {ActorType.ENEMY_TURRET, 5248},
            {ActorType.ENEMY_HUT, 5504},
            {ActorType.ENEMY_TURRET, 5632},
            {ActorType.ENEMY_HUT, 1792},
            {ActorType.ENEMY_HUT, 4608},
            {ActorType.ENEMY_HUT, 6400},
            {ActorType.ENEMY_TURRET, 6496},
            {ActorType.ENEMY_JEEP, 1218},
            {ActorType.ENEMY_JEEP, 2146},
            {ActorType.ENEMY_COVERED_TRUCK, 4034},
            {ActorType.ENEMY_PAD, 7872},
            {ActorType.ENEMY_BASE, 8168}
        };

        init();

    }

}
