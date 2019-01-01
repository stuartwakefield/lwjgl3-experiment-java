package engine.geometry;

import com.jogamp.opengl.math.FloatUtil;
import engine.renderer.Buffer;

import java.nio.FloatBuffer;

public class Mat4 {

    public final float[] values;

    public Mat4(float[] values) {
        this.values = values;
    }

    public FloatBuffer toBuffer() {
        return Buffer.floatBuffer(this.values);
    }

    public static Mat4 translate(float x, float y, float z) {
        return new Mat4(FloatUtil.makeTranslation(new float[16], 0, true, x, y, z));
    }

    public static Mat4 translate(Vec3 vec) {
        return translate(vec.x(), vec.y(), vec.z());
    }

    public static Mat4 scale(float scale) {
        return new Mat4(FloatUtil.makeScale(new float[16], 0, true, scale, scale, scale));
    }

    public static Mat4 lookAt(Vec3 eye, Vec3 center, Vec3 up) {
        return new Mat4(FloatUtil.makeLookAt(new float[16], 0, eye.values, 0, center.values, 0, up.values, 0, new float[16]));
    }

    public static Mat4 perspective(float fov, float aspect, float near, float far) {
        return new Mat4(FloatUtil.makePerspective(new float[16], 0, true, fov, aspect, near, far));
    }

    public static Mat4 mult(Mat4 a, Mat4 b) {
        return new Mat4(FloatUtil.multMatrix(a.values, b.values));
    }

}
