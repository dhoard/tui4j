package com.williamcallahan.tui4j.compat.bubbles.spinner;

import com.williamcallahan.tui4j.compat.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of github.com/charmbracelet/bubbles/spinner/spinner_test.go.
 */
class SpinnerTest {

    /**
     * Registers terminal info so rendering tests do not depend on another test
     * class having initialized the shared provider first.
     */
    @BeforeEach
    void setUp() {
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
    }

    @Test
    void testSpinnerTypes() {
        assertSpinnerType(SpinnerType.LINE, new String[]{"|", "/", "-", "\\"}, Duration.ofSeconds(1).dividedBy(10));
        assertSpinnerType(SpinnerType.DOT, new String[]{"⣾ ", "⣽ ", "⣻ ", "⢿ ", "⡿ ", "⣟ ", "⣯ ", "⣷ "}, Duration.ofSeconds(1).dividedBy(10));
        assertSpinnerType(SpinnerType.MINI_DOT, new String[]{"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"}, Duration.ofSeconds(1).dividedBy(12));
        assertSpinnerType(SpinnerType.JUMP, new String[]{"⢄", "⢂", "⢁", "⡁", "⡈", "⡐", "⡠"}, Duration.ofSeconds(1).dividedBy(10));
        assertSpinnerType(SpinnerType.PULSE, new String[]{"█", "▓", "▒", "░"}, Duration.ofSeconds(1).dividedBy(8));
        assertSpinnerType(SpinnerType.POINTS, new String[]{"∙∙∙", "●∙∙", "∙●∙", "∙∙●"}, Duration.ofSeconds(1).dividedBy(7));
        assertSpinnerType(SpinnerType.GLOBE, new String[]{"🌍", "🌎", "🌏"}, Duration.ofSeconds(1).dividedBy(4));
        assertSpinnerType(SpinnerType.MOON, new String[]{"🌑", "🌒", "🌓", "🌔", "🌕", "🌖", "🌗", "🌘"}, Duration.ofSeconds(1).dividedBy(8));
        assertSpinnerType(SpinnerType.MONKEY, new String[]{"🙈", "🙉", "🙊"}, Duration.ofSeconds(1).dividedBy(3));
    }

    @Test
    void testSpinnerUsesProvidedType() {
        Spinner spinner = new Spinner(SpinnerType.LINE);
        assertThat(spinner.view()).isEqualTo("|");
    }

    private static void assertSpinnerType(SpinnerType type, String[] expectedFrames, Duration expectedDuration) {
        assertThat(type.frames()).containsExactly(expectedFrames);
        assertThat(type.duration()).isEqualTo(expectedDuration);
    }
}
