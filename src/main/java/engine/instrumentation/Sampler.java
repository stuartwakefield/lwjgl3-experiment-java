package engine.instrumentation;

import org.lwjgl.glfw.GLFW;

public class Sampler {

    private boolean started = false;
    private double lastTime = 0;
    private final double sampleTime;
    private final SamplerCallback callback;

    public Sampler(double sampleTime, SamplerCallback callback) {
        this.sampleTime = sampleTime;
        this.callback = callback;
    }

    public void start() {
        this.lastTime = GLFW.glfwGetTime();
        this.started = true;
    }

    public void next() {

        if (!this.started)
            throw new IllegalStateException("Sampler has not been started!");

        double currentTime = GLFW.glfwGetTime();
        double deltaTime = currentTime - this.lastTime;

        if (deltaTime > this.sampleTime) {
            this.callback.invoke();

            this.lastTime = currentTime;
        }

    }

}
