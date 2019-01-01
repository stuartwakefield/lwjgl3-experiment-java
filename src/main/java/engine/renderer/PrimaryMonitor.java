package engine.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class PrimaryMonitor {

    public static GLFWVidMode getVideoMode() {
        return GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    }

}
