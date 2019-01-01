package entities;

import engine.geometry.Vec3;

public class Pos {

    public static final Pos ZERO = new Pos(0f, 0f, 0f);
    public static final Pos UP = new Pos(0f, 1f, 0f);
    public static final Pos DOWN = new Pos(0f, -1f, 0f);
    public static final Pos LEFT = new Pos(-1f, 0f, 0f);
    public static final Pos RIGHT = new Pos(1f, 0f, 0f);
    public static final Pos FRONT = new Pos(0f, 0f, -1f);
    public static final Pos BACK = new Pos(0f, 0f, 1f);

    public final Vec3 vec;

    public Pos(float x, float y, float z) {
        this(new Vec3(x, y, z));
    }

    public Pos(Vec3 vec) {
        this.vec = vec;
    }

    public Pos add(float x, float y, float z) {
        return new Pos(this.vec.add(x, y, z));
    }

    public Pos add(Pos other) {
        return add(other.x(), other.y(), other.z());
    }

    public Pos subtract(float x, float y, float z) {
        return new Pos(this.vec.subtract(x, y, z));
    }

    public Pos subtract(Pos other) {
        return subtract(other.x(), other.y(), other.z());
    }

    public Float x() {
        return vec.x();
    }

    public Float y() {
        return vec.y();
    }

    public Float z() {
        return vec.z();
    }

    public Vec3 toVec3() {
        return vec;
    }

}
