package rescue.raiders.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Sounds {

    public static Map<Sound, Music> sounds = new HashMap<Sound, Music>();

    public static Music play(Sound sound) {
        Music m = get(sound);
        m.play();
        return m;
    }

    public static Music get(Sound sound) {
        Music m = sounds.get(sound);
        if (m == null) {
            m = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/ogg/" + sound.getFile()));
            m.setVolume(sound.getVolume());
            m.setLooping(sound.getLooping());

            sounds.put(sound, m);
        }
        return m;
    }

}
