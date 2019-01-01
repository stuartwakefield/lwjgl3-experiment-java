package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Texture {

    private final String filePath;

    public Texture(String filePath) {
        this.filePath = filePath;
    }

    public TextureBuffer load() {

        File imageFile = new File(this.filePath);

        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        boolean imageHasAlpha = image.getColorModel().hasAlpha();

        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();

        int id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, imageWidth, imageHeight, 0, imageHasAlpha ? GL11.GL_RGBA : GL12.GL_BGR, GL11.GL_UNSIGNED_BYTE, Buffer.byteBuffer(buffer.getData()));
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        return new TextureBuffer(id);
    }

}
