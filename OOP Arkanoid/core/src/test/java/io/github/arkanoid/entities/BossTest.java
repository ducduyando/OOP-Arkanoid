package io.github.arkanoid.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.GdxTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static io.github.arkanoid.core.Constants.*;

/**
 * Test class for Boss
 */
public class BossTest extends GdxTestRunner {

    @Mock
    private Texture mockNormalSprite;

    @Mock
    private Texture mockTakeDamageSprite;

    @Mock
    private Texture mockDeathSprite;

    @Mock
    private Texture mockSkill1Texture;

    @Mock
    private Texture mockSkill2Texture;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBossStateEnum() {
        // Test that Boss.State enum values exist
        Boss.State[] states = Boss.State.values();
        assertTrue(states.length > 0);

        boolean hasNormal = false;
        boolean hasTakingDamage = false;
        boolean hasDying = false;

        for (Boss.State state : states) {
            if (state == Boss.State.NORMAL) hasNormal = true;
            if (state == Boss.State.TAKING_DAMAGE) hasTakingDamage = true;
            if (state == Boss.State.DYING) hasDying = true;
        }

        assertTrue(hasNormal);
        assertTrue(hasTakingDamage);
        assertTrue(hasDying);
    }

}

