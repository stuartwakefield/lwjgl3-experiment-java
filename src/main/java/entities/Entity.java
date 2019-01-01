package entities;

public class Entity {

    private final String model;
    private Pos position;

    public Entity(String model, Pos position) {
        this.model = model;
        this.position = position;
    }

    public void moveTo(Pos position) {
        this.position = position;
    }

    public void move(float x, float y, float z) {
        this.moveTo(this.position.add(x, y, z));
    }

    public String getModel() {
        return this.model;
    }

    public Pos getPosition() {
        return this.position;
    }

}
