import engine.Setup;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    int WIDTH = 800;
    int HEIGHT = 600;

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static String getNativeLibraryPath() {
        return new File("native").getAbsolutePath();
    }

    private static void setLWJGLLibraryPath(String path) {
        logger.info("Set LWJGL libraries path: {}", path);
        System.setProperty("org.lwjgl.librarypath", path);
    }

    private void configure() {
        setLWJGLLibraryPath(getNativeLibraryPath());
    }


    public void run() {

        // TODO: Refactor renderer into a tree, processing at a later stage
        // TODO: Create a function that takes the world and returns the renderable models

        configure();
        Setup setup = new Setup(WIDTH, HEIGHT);

        try {
            setup.init();
            setup.loop().start();
        } catch (Exception ex) {
            logger.error("Exception occurred", ex);
        } finally {
            setup.close();
        }

    }

    public static void main(String[] args) {
        new Main().run();
    }

}
