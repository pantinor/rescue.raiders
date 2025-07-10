package rescue.raiders.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class Sounds {

    private static Map<rescue.raiders.util.Sound, Sound> soundCache = new HashMap<>();

    public static Sound play(rescue.raiders.util.Sound s) {
        Sound sound = get(s);
        if (sound != null) {
            if (s.getLooping()) {
                sound.loop(1f);
            } else {
                sound.play(1f);
            }
        }
        return sound;
    }

    private static Sound get(rescue.raiders.util.Sound s) {
        if (soundCache.containsKey(s)) {
            return soundCache.get(s);
        } else {
            try {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/audio/ogg/" + s.getFile()));
                soundCache.put(s, sound);
                return sound;
            } catch (Exception e) {
                Gdx.app.error("SoundManager", "Could not load sound: " + s, e);
                return null;
            }
        }
    }

    public static void dispose() {
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}
