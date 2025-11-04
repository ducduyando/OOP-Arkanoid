package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class MusicResource {
    public static final Sound COLLISION_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Collision" + ".wav"));
    public static final Sound DEAD_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Dead" + ".wav"));
    public static final Sound GET_HIT_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Get hit" + ".wav"));
    public static final Sound HP_UP_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Hp up" + ".wav"));
    public static final Sound SWITCH_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "switch" + ".wav"));
    public static final Sound BEE_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Bee" + ".wav"));
    public static final Sound BOMB_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Bomb" + ".wav"));
    public static final Sound CHAINSAW_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Chainsaw" + ".wav"));
    public static final Sound HONEY_SHIELD_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Honey Shield" + ".wav"));
    public static final Sound LASER_BEAM_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Laser Beam" + ".wav"));
    public static final Sound ROCKET_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Rocket" + ".wav"));
    public static final Sound SPIKE_SOUND = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Spike" + ".wav"));

    public static final Sound DEFEATED_THEME = Gdx.audio.newSound(Gdx.files.internal("Soundtrack/" + "Defeated theme" + ".mp3"));
    public static final Sound VICTORY_THEME = Gdx.audio.newSound(Gdx.files.internal("Soundtrack/" + "Victory theme" + ".mp3"));
    public static final Sound STAGE1_THEME = Gdx.audio.newSound(Gdx.files.internal("Soundtrack/" + "Stage1 theme" + ".mp3"));
    public static final Sound MENU_THEME = Gdx.audio.newSound(Gdx.files.internal("Soundtrack/" + "Menu theme" + ".mp3"));
    public static final Sound STAGE2_THEME = Gdx.audio.newSound(Gdx.files.internal("Soundtrack/" + "Stage2 theme" + ".mp3"));
    public static final Sound STAGE3_THEME = Gdx.audio.newSound(Gdx.files.internal("Soundtrack/" + "Stage3 theme" + ".mp3"));

}
