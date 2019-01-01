package engine.renderer;

public class FrameSize {

    private final Integer width;
    private final Integer height;

    public FrameSize(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

}
