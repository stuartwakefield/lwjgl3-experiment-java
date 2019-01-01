package engine.input;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

public class Cursor {

    private long window;
    private final DoubleBuffer leftBuffer;
    private final DoubleBuffer topBuffer;

    public Cursor(long window) {
        this.window = window;
        this.leftBuffer = BufferUtils.createDoubleBuffer(1);
        this.topBuffer = BufferUtils.createDoubleBuffer(1);
    }

    public CursorPos pos() {

        GLFW.glfwGetCursorPos(this.window, leftBuffer, topBuffer);

        return new CursorPos(this.leftBuffer.get(0), this.topBuffer.get(0));
    }

}
