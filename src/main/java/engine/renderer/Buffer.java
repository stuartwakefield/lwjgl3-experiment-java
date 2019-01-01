package engine.renderer;

import org.lwjgl.BufferUtils;

import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Buffer {

    public static FloatBuffer floatBuffer(float[] values) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static IntBuffer intBuffer(int[] values) {

        IntBuffer buffer = BufferUtils.createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static ByteBuffer byteBuffer(byte[] values) {

        ByteBuffer buffer = BufferUtils.createByteBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

}
