package rescue.raiders.levels;

import rescue.raiders.objects.ActorType;

public class Level1 extends Level {

    public Level1() {

        this.layout = new Object[][] {
            {ActorType.BASE, 0},
            {ActorType.PAD, 500},
            {ActorType.HUT, 1000},
            {ActorType.TURRET, 2000},
            {ActorType.HUT, 3000},
            {ActorType.TURRET, 4000},
            {ActorType.HUT, 5000},
            {ActorType.HUT, 6000},
            {ActorType.ENEMY_HUT, 7000},
            {ActorType.ENEMY_HUT, 8000},
            {ActorType.ENEMY_TURRET, 9000},
            {ActorType.ENEMY_HUT, 10000},
            {ActorType.ENEMY_TURRET, 11000},
            {ActorType.ENEMY_HUT, 12000},
            {ActorType.ENEMY_PAD, 13500},
            {ActorType.ENEMY_BASE, 13850}
        };

        init();

    }

}
