package rescue.raiders.levels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import rescue.raiders.objects.ActorType;

public class CakeWalk extends Level {

    public CakeWalk(Stage stage) {
        super(stage);

        this.layout = new Object[][]{
            {ActorType.BASE, 24},
            {ActorType.PAD, 320},
            {ActorType.INFANTRY, 448},
            {ActorType.COVERED_TRUCK, 512},
            {ActorType.TANK, 576},
            {ActorType.TANK, 640},
            {ActorType.HUT, 2048},
            {ActorType.ENEMY_HUT, 1536},
            {ActorType.HUT, 2560},
            {ActorType.HUT, 3072},
            {ActorType.HUT, 3584},
            {ActorType.HUT, 5120},
            {ActorType.ENEMY_INFANTRY, 5120},
            {ActorType.HUT, 5632},
            {ActorType.HUT, 6656},
            {ActorType.ENEMY_TANK, 1536},
            {ActorType.ENEMY_TANK, 1600},
            {ActorType.ENEMY_TANK, 1664},
            {ActorType.ENEMY_TANK, 2688},
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
