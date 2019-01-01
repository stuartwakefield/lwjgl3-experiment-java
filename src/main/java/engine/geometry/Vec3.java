package engine.geometry;

import engine.renderer.Buffer;

import java.nio.FloatBuffer;

public class Vec3 {

    public static Vec3 ZERO = new Vec3(0);
    public static Vec3 ONE = new Vec3(1);
    public static Vec3 UP = new Vec3(0, 1, 0);
    public static Vec3 DOWN = new Vec3(0, -1, 0);
    public static Vec3 LEFT = new Vec3(-1, 0, 0);
    public static Vec3 RIGHT = new Vec3(1, 0, 0);
    public static Vec3 BACK = new Vec3(0, 1, 0);
    public static Vec3 FRONT = new Vec3(0, -1, 0);

    public final float[] values;

    public Vec3(float[] values) {
        this.values = values;
    }

    public Vec3(float x, float y, float z) {
        this.values = new float[] { x, y, z };
    }

    public Vec3(float xyz) {
        this(xyz, xyz, xyz);
    }

    public float x() { return values[0]; }
    public float y() { return values[1]; }
    public float z() { return values[2]; }

    public Vec3 add(Vec3 vec) {
        return add(this, vec);
    }

    public Vec3 add(float x, float y, float z) {
        return add(this.x(), this.y(), this.z(), x, y, z);
    }

    public Vec3 subtract(Vec3 vec) {
        return substract(this, vec);
    }

    public Vec3 subtract(float x, float y, float z) {
        return subtract(this.x(), this.y(), this.z(), x, y, z);
    }

    public Vec3 cross(Vec3 vec) {
        return cross(this, vec);
    }

    public Vec3 normalize() {
        return normalize(this);
    }

    public static float dot(Vec3 a, Vec3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    public static float magnitude(Vec3 vec) {
        return (float) Math.sqrt(dot(vec, vec));
    }

    public static Vec3 normalize(Vec3 vec) {
        return divide(vec, magnitude(vec));
    }

    public static Vec3 divide(Vec3 vec, float divisor) {
        return new Vec3(vec.x() / divisor, vec.y() / divisor, vec.z() / divisor);
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x()
        );
    }

    public static Vec3 substract(Vec3 a, Vec3 b) {
        return new Vec3(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
    }

    public static Vec3 subtract(float ax, float ay, float az, float bx, float by, float bz) {
        return new Vec3(ax - bx, ay - by, az - bz);
    }

    public static Vec3 add(Vec3 a, Vec3 b) {
        return add(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }

    public static Vec3 add(float ax, float ay, float az, float bx, float by, float bz) {
        return new Vec3(ax + bx, ay + by, az + bz);
    }

    public FloatBuffer toBuffer() {
        return Buffer.floatBuffer(this.values);
    }


}
