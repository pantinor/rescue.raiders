package rescue.raiders.util;

public enum Sound {

    BALLOON_EXPLOSION("balloon-explosion.ogg", false, 0.1f),
    CHAIN_SNAPPING("chain-snapping.ogg", false, 0.1f),
    CRASH_GLASS("crash-glass.ogg", false, 0.1f),
    DOOR_CLOSING("door-closing.ogg", false, 0.1f),
    EXPLOSION_LARGE("explosion-large.ogg", false, 0.1f),
    GENERIC_BOOM("generic-boom.ogg", false, 0.1f),
    GENERIC_EXPLOSION_2("generic-explosion-2.ogg", false, 0.1f),
    GENERIC_EXPLOSION("generic-explosion.ogg", false, 0.1f),
    GENERIC_GUNFIRE("generic-gunfire.ogg", false, 0.1f),
    HELICOPTER_ENGINE("helicopter-engine.ogg", true, 0.1f),
    HELICOPTER_ROTATE("helicopter-rotate.ogg", false, 0.1f),
    IMPACT_WRENCH_1("impact-wrench-1.ogg", false, 0.1f),
    IMPACT_WRENCH_2("impact-wrench-2.ogg", false, 0.1f),
    IMPACT_WRENCH_3("impact-wrench-3.ogg", false, 0.1f),
    INFANTRY_GUNFIRE("infantry-gunfire.ogg", false, 0.1f),
    METAL_HIT_1("metal-hit-1.ogg", false, 0.1f),
    METAL_HIT_2("metal-hit-2.ogg", false, 0.1f),
    METAL_HIT_3("metal-hit-3.ogg", false, 0.1f),
    METAL_HIT_4("metal-hit-4.ogg", false, 0.1f),
    METAL_HIT_5("metal-hit-5.ogg", false, 0.1f),
    METAL_HIT_BREAK("metal-hit-break.ogg", false, 0.1f),
    METAL_HIT_LIGHT_1("metal-hit-light-1.ogg", false, 0.1f),
    METAL_HIT_LIGHT_2("metal-hit-light-2.ogg", false, 0.1f),
    METAL_HIT_LIGHT_3("metal-hit-light-3.ogg", false, 0.1f),
    METAL_HIT_LIGHT_4("metal-hit-light-4.ogg", false, 0.1f),
    METAL_HIT_LIGHT_5("metal-hit-light-5.ogg", false, 0.1f),
    METAL_HIT_LIGHT("metal-hit-light.ogg", false, 0.1f),
    MISSILE_LAUNCH("missile-launch.ogg", false, 0.1f),
    ORDER_COMPLETE("order-complete.ogg", false, 0.1f),
    ORDER_DENIED("order-denied.ogg", false, 0.1f),
    ORDER_START("order-start.ogg", false, 0.1f),
    PARACHUTE_OPEN("parachute-open.ogg", false, 0.1f),
    POPSOUND1("popsound1.ogg", false, 0.1f),
    POPSOUND2("popsound2.ogg", false, 0.1f),
    RADAR_JAMMING("radar-jamming.ogg", false, 0.1f),
    REPAIRING("repairing.ogg", false, 0.1f),
    SHRAPNEL_HIT_2("shrapnel-hit-2.ogg", false, 0.1f),
    SHRAPNEL_HIT_3("shrapnel-hit-3.ogg", false, 0.1f),
    SHRAPNEL_HIT_4("shrapnel-hit-4.ogg", false, 0.1f),
    SHRAPNEL_HIT("shrapnel-hit.ogg", false, 0.1f),
    SOCKET_WRENCH_1("socket-wrench-1.ogg", false, 0.1f),
    SOCKET_WRENCH_2("socket-wrench-2.ogg", false, 0.1f),
    SOCKET_WRENCH_3("socket-wrench-3.ogg", false, 0.1f),
    SPLAT("splat.ogg", false, 0.1f),
    SPLAT1("splat1.ogg", false, 0.1f),
    SPLAT2("splat2.ogg", false, 0.1f),
    SPLAT3("splat3.ogg", false, 0.1f),
    TINKER_WRENCH("tinker-wrench.ogg", false, 0.1f),
    TURRET_GUNFIRE("turret-gunfire.ogg", false, 0.1f),
    VIOLIN_C5_PIZZICATO_NON_VIBRATO("violin-c5-pizzicato-non-vibrato.ogg", false, 0.1f),
    VIOLIN_G4_PIZZICATO_NON_VIBRATO("violin-g4-pizzicato-non-vibrato.ogg", false, 0.1f),
    WILHEM_SCREAM("wilhem-scream.ogg", false, 0.1f);

    private String file;
    private boolean looping;
    private float volume;

    private Sound(String name, boolean looping, float volume) {
        this.file = name;
        this.looping = looping;
        this.volume = volume;
    }

    public String getFile() {
        return this.file;
    }

    public boolean getLooping() {
        return this.looping;
    }

    public float getVolume() {
        return this.volume;
    }

}
