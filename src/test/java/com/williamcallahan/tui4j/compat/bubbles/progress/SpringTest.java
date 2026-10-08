package com.williamcallahan.tui4j.compat.bubbles.progress;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests the progress spring helper.
 * <p>
 * Upstream: bubbles/progress/progress.go, which builds its spring with
 * {@code harmonica.NewSpring(harmonica.FPS(fps), frequency, damping)}, so the frame
 * rate defines the integration step {@code 1 / fps}.
 */
class SpringTest {

    private static final double FREQUENCY = 18.0;
    private static final double DAMPING = 1.0;

    @Test
    void testWithFpsUsesTheRequestedFrameRate() {
        // when
        Spring.SpringUpdateResult thirtyFps = Spring.withFPS(30.0, FREQUENCY, DAMPING).update(0, 0, 1);
        Spring.SpringUpdateResult sixtyFps = Spring.withFPS(60.0, FREQUENCY, DAMPING).update(0, 0, 1);

        // then: one step at 30 fps advances half as far as two 60 fps steps.
        assertThat(thirtyFps.position()).isCloseTo(0.36, within(1e-9));
        assertThat(sixtyFps.position()).isCloseTo(0.09, within(1e-9));
        assertThat(thirtyFps.velocity()).isCloseTo(10.8, within(1e-9));
        assertThat(sixtyFps.velocity()).isCloseTo(5.4, within(1e-9));
    }

    @Test
    void testDefaultFrameRateIsSixty() {
        // when
        Spring.SpringUpdateResult stepped = new Spring(FREQUENCY, DAMPING).update(0, 0, 1);
        Spring.SpringUpdateResult explicit = Spring.withFPS(60.0, FREQUENCY, DAMPING).update(0, 0, 1);

        // then
        assertThat(stepped.position()).isEqualTo(explicit.position());
        assertThat(stepped.velocity()).isEqualTo(explicit.velocity());
    }
}
