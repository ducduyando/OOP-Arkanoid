package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;


public class MusicManager implements Disposable {

    private static MusicManager instance;


    private Map<String, Music> soundtrackMap;
    private Map<String, Sound> sfxMap;


    private float musicVolume = 1.0f;
    private float effectVolume = 1.0f;


    private Music currentMusic;
    private String currentMusicId;


    private MusicManager() {
        soundtrackMap = new HashMap<>();
        sfxMap = new HashMap<>();

        sfxMap["collisionSound"] = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Collision" + ".wav"));
    }


    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }


    public void loadMusic(String id, String source) {

        Music music = Gdx.audio.newMusic(Gdx.files.internal(source));
        if (music != null) {
            soundtrackMap.put(id, music);

        }

    }


    public void loadEffect(String id, String source) {

        Sound effect = Gdx.audio.newSound(Gdx.files.internal(source));
        if (effect != null) {
            sfxMap.put(id, effect);

        }

    }


    public void playMusic(String id) {
        Music music = soundtrackMap.get(id);
        if (music != null) {
            // Stop current music if playing
            if (currentMusic != null && currentMusic.isPlaying()) {
                currentMusic.stop();
            }

            currentMusic = music;
            currentMusicId = id;
            currentMusic.setVolume(musicVolume);
            currentMusic.setLooping(true);
            currentMusic.play();
        }
    }


    public void playEffect(String id) {
        Sound effect = sfxMap.get(id);
        if (effect != null) {
            effect.play(effectVolume);
        }
    }


    public void playEffect(String id, float volume) {
        Sound effect = sfxMap.get(id);
        if (effect != null) {
            effect.play(volume);
        }
    }

    public void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }


    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }


    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(this.musicVolume);
        }
    }


    public void setEffectVolume(float volume) {
        this.effectVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }


    public float getMusicVolume() {
        return musicVolume;
    }


    public float getEffectVolume() {
        return effectVolume;
    }


    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }


    public boolean hasMusicLoaded(String id) {
        return soundtrackMap.containsKey(id);
    }


    public boolean hasEffectLoaded(String id) {
        return sfxMap.containsKey(id);
    }


    public String getCurrentMusicId() {
        return currentMusicId;
    }


    public void preloadGameSounds() {
        // Load common game sounds
        loadEffect("paddle_hit", "sounds/paddle_hit.wav");
        loadEffect("brick_break", "sounds/brick_break.wav");
        loadEffect("ball_bounce", "sounds/ball_bounce.wav");
        loadEffect("power_up", "sounds/power_up.wav");
        loadEffect("boss_hit", "sounds/boss_hit.wav");
        loadEffect("game_over", "sounds/game_over.wav");
        loadEffect("victory", "sounds/victory.wav");

        loadMusic("menu_music", "music/menu.ogg");
        loadMusic("game_music", "music/game.ogg");
        loadMusic("boss_music", "music/boss.ogg");


    }


    public void clean() {
        // Stop current music
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            currentMusicId = null;
        }

        for (Map.Entry<String, Music> entry : soundtrackMap.entrySet()) {
            entry.getValue().dispose();
        }
        soundtrackMap.clear();

        // Dispose all effects
        for (Map.Entry<String, Sound> entry : sfxMap.entrySet()) {
            entry.getValue().dispose();
        }
        sfxMap.clear();

    }

    @Override
    public void dispose() {
        clean();
        instance = null;
    }
}
