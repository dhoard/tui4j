package com.williamcallahan.tui4j.compat.bubbles.progress;

/**
 * Port of the progress spring helper.
 * Upstream: github.com/charmbracelet/bubbles/progress (Spring)
 * <p>
 * Bubbles: progress/progress.go.
 */
public class Spring {

    /**
     * Port of the spring update strategy.
     * Upstream: github.com/charmbracelet/bubbles/progress (springUpdateFn)
     * <p>
     * Bubbles: progress/progress.go.
     */
    public interface SpringUpdate {
        /**
         * Applies an incoming message and returns the next model state.
         *
         * @param position position
         * @param velocity velocity
         * @param target target
         * @return next model state and command
         */
        SpringUpdateResult update(
            double position,
            double velocity,
            double target
        );
    }

    /**
     * Compatibility port of SpringUpdateResult to preserve upstream behavior.
     * <p>
     * Bubbles: progress/progress.go.
     *
     * @param position updated spring position
     * @param velocity updated spring velocity
     */
    public record SpringUpdateResult(double position, double velocity) {}

    private static final double FPS = 60.0;

    private final double frequency;
    private final double damping;
    private final double deltaTime;

    /**
     * Creates Spring to keep this component ready for use, stepping at the
     * default 60 frames per second.
     *
     * @param frequency frequency
     * @param damping damping
     */
    public Spring(double frequency, double damping) {
        this(frequency, damping, 1.0 / FPS);
    }

    /**
     * Creates Spring with an explicit integration step.
     *
     * @param frequency frequency
     * @param damping damping
     * @param deltaTime seconds advanced per update
     */
    private Spring(double frequency, double damping, double deltaTime) {
        this.frequency = frequency;
        this.damping = damping;
        this.deltaTime = deltaTime;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param position position
     * @param velocity velocity
     * @param target target
     * @return next model state and command
     */
    public SpringUpdateResult update(
        double position,
        double velocity,
        double target
    ) {
        double dt = deltaTime;

        double displacement = position - target;
        double springForce = -frequency * frequency * displacement;
        double dampingForce = -2.0 * damping * frequency * velocity;

        double acceleration = springForce + dampingForce;

        velocity += acceleration * dt;
        position += velocity * dt;

        return new SpringUpdateResult(position, velocity);
    }

    /**
     * Creates a spring using the legacy FPS signature.
     *
     * @param fps frames per second; one update advances the simulation by
     *            {@code 1 / fps} seconds
     * @param frequency spring frequency
     * @param damping spring damping
     * @return spring instance
     */
    public static Spring withFPS(double fps, double frequency, double damping) {
        return new Spring(frequency, damping, 1.0 / fps);
    }
}
