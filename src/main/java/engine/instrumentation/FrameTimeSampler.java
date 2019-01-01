package engine.instrumentation;

import org.lwjgl.glfw.GLFW;

public class FrameTimeSampler {

    private boolean started = false;

    // Tracks the total number of frames rendered (P.S. don’t worry about integer overflow, would take
    // roughly 5 billion years at 60 FPS, could use an int as would still take about 14 months).
    private long totalFrameCount = 0;

    // Tracks the total frame count from the last sample
    private long lastCount = 0;

    // Get the starting time for the samples
    private double lastTime = 0;

    // calculate sample every sampleTime seconds
    private final double sampleTime;

    // Callback to invoke with each sample
    private final FrameTimeSamplerCallback callback;

    public FrameTimeSampler(double sampleTime, FrameTimeSamplerCallback callback) {
        this.sampleTime = sampleTime;
        this.callback = callback;
    }

    public void start() {
        this.totalFrameCount = 0;
        this.lastCount = 0;
        this.lastTime = GLFW.glfwGetTime();
        this.started = true;
    }

    public void next() {

        if (!this.started)
            throw new IllegalStateException("Sampler has not been started!");

        this.totalFrameCount ++;
        double currentTime = GLFW.glfwGetTime();
        double deltaTime = currentTime - this.lastTime;

        if (deltaTime > this.sampleTime) {
            double sampleCount = this.totalFrameCount - this.lastCount;

            // BEWARE: Division by zero, should not be possible
            double sampleTime = deltaTime / sampleCount;
            double sampleRate = 1 / sampleTime;

            this.callback.invoke(new FrameTimeSamplerEvent(this.totalFrameCount, sampleTime, sampleRate));

            this.lastCount = this.totalFrameCount;
            this.lastTime = currentTime;
        }

    }

}
