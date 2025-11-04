package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;


public class MusicManager implements Disposable {

    private static MusicManager instance;


    private static final Map<String, Music> soundtrackMap = new HashMap<>();
    private static final Map<String, Sound> sfxMap = new HashMap<>();


    private static float musicVolume = 1.0f;
    private static float effectVolume = 1.0f;


    private static Music currentMusic;
    private static String currentMusicId;


    private MusicManager() {

        sfxMap.put("collisionSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Collision" + ".wav")));
        sfxMap.put("deadSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Dead" + ".wav")));
        sfxMap.put("getHitSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Get hit" + ".wav")));
        sfxMap.put("hpUpSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Hp up" + ".wav")));
        sfxMap.put("switchSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "switch" + ".wav")));
        sfxMap.put("beeSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Bee" + ".wav")));
        sfxMap.put("bombSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Bomb" + ".wav")));
        sfxMap.put("chainsawSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Chainsaw" + ".wav")));
        sfxMap.put("honeyShieldSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Honey Shield" + ".wav")));
        sfxMap.put("laserBeamSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Laser Beam" + ".wav")));
        sfxMap.put("laserSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Laser" + ".wav")));
        sfxMap.put("rocketSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Rocket" + ".wav")));
        sfxMap.put("spikeSound", Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Spike" + ".wav")));

        soundtrackMap.put("defeatedTheme", Gdx.audio.newMusic(Gdx.files.internal("Soundtrack/" + "Defeated theme" + ".mp3")));
        soundtrackMap.put("victoryTheme", Gdx.audio.newMusic(Gdx.files.internal("Soundtrack/" + "Victory theme" + ".mp3")));
        soundtrackMap.put("stage1Theme", Gdx.audio.newMusic(Gdx.files.internal("Soundtrack/" + "Stage1 theme" + ".mp3")));
        soundtrackMap.put("menuTheme", Gdx.audio.newMusic(Gdx.files.internal("Soundtrack/" + "Menu theme" + ".mp3")));
        soundtrackMap.put("stage2Theme", Gdx.audio.newMusic(Gdx.files.internal("Soundtrack/" + "Stage2 theme" + ".mp3")));
        soundtrackMap.put("stage3Theme", Gdx.audio.newMusic(Gdx.files.internal("Soundtrack/" + "Stage3 theme" + ".mp3")));
    }


    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }


    public static void loadMusic(String id, String source) {

        Music music = Gdx.audio.newMusic(Gdx.files.internal(source));
        if (music != null) {
            soundtrackMap.put(id, music);

        }

    }


    public static void loadEffect(String id, String source) {

        Sound effect = Gdx.audio.newSound(Gdx.files.internal(source));
        if (effect != null) {
            sfxMap.put(id, effect);
        }

    }


    public static void playMusic(String name) {
        Music music = soundtrackMap.get(name);
        if (music != null) {
            // Stop current music if playing
            if (currentMusic != null && currentMusic.isPlaying()) {
                currentMusic.stop();
            }

            currentMusic = music;
            currentMusicId = name;
            currentMusic.setVolume(musicVolume);
            currentMusic.setLooping(true);
            currentMusic.play();
        }
    }


    public static long playEffect(String name) {
        Sound effect = sfxMap.get(name);
        if (effect != null) {
            return effect.play(effectVolume);
        }
        return -1;
    }

    public static long playEffect(String name, float volume) {
        Sound effect = sfxMap.get(name);
        if (effect != null) {
            return effect.play(volume);
        }
        return -1;
    }

    public static void stopEffect(String name, long soundId) {
        Sound effect = sfxMap.get(name);
        if (effect != null) {
            effect.stop(soundId);
        }
    }

    public static void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }


    public static void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }


    public static void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public static void setMusicVolume(float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }


    public static void setEffectVolume(float volume) {
        effectVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }


    public static float getMusicVolume() {
        return musicVolume;
    }


    public static float getEffectVolume() {
        return effectVolume;
    }


    public static boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }


    public static boolean hasMusicLoaded(String id) {
        return soundtrackMap.containsKey(id);
    }


    public static boolean hasEffectLoaded(String id) {
        return sfxMap.containsKey(id);
    }


    public static String getCurrentMusicId() {
        return currentMusicId;
    }


    public static void preloadGameSounds() {
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


    public static void clean() {
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
