package entities;

import java.util.List;

public class Scene {

    private final Camera camera;
    private final Light light;
    private final List<Entity> entities;

    public Scene(Camera camera, Light light, List<Entity> entities) {
        this.camera = camera;
        this.light = light;
        this.entities = entities;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Light getLight() {
        return this.light;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

}
