package rescue.raiders.levels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import rescue.raiders.objects.ActorType;

public class CakeWalk extends Level {

    public CakeWalk(Stage stage) {
        super(stage);

        this.layout = new Object[][]{
            {ActorType.BASE, 0},
            {ActorType.PAD, 800},
            //{ActorType.TANK, 0},
            //{ActorType.TANK, 100},
            {ActorType.HUT, 1000},
            {ActorType.ENEMY_HUT, 1536},
            {ActorType.HUT, 2560},
            //{ActorType.HUT, 3072},
            //{ActorType.HUT, 3584},
            //{ActorType.HUT, 5120},
            {ActorType.ENEMY_RIFLEMAN, 2000},
            {ActorType.ENEMY_RIFLEMAN, 2100},
            {ActorType.ENEMY_RIFLEMAN, 2200},
            {ActorType.ENEMY_RIFLEMAN, 2300},
            {ActorType.HUT, 5632},
            {ActorType.HUT, 6656},
            //{ActorType.ENEMY_TANK, 1536},
            //{ActorType.ENEMY_TANK, 1600},
            //{ActorType.ENEMY_TANK, 1664},
            //{ActorType.ENEMY_TANK, 2688},
            {ActorType.HUT, 4096},
            {ActorType.ENEMY_TANK, 4736},
            {ActorType.HUT, 4608},
            {ActorType.ENEMY_JEEP, 1728},
            {ActorType.ENEMY_PAD, 7872},
            {ActorType.ENEMY_BASE, 8000}
        };

        init();

    }

}
