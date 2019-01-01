package engine.instrumentation;

public class FrameTimeSamplerEvent {

    public final long totalFrameCount;
    public final double frameTime;
    public final double frameRate;

    public FrameTimeSamplerEvent(long totalFrameCount, double frameTime, double frameRate) {
        this.totalFrameCount = totalFrameCount;
        this.frameTime = frameTime;
        this.frameRate = frameRate;
    }

}