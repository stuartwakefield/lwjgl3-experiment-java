package entities;

import engine.Color;
import engine.geometry.Vec3;
import engine.input.CursorPos;
import engine.renderer.Window;

import java.util.ArrayList;
import java.util.List;

public class World {

    private Camera camera;
    private Light light;
    private List<Entity> entities;

    public World() {

        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity("cube", new Pos(0f, 0f, 0f)));
        entities.add(new Entity("pyramid", new Pos(-4f, 0f, 0f)));
        entities.add(new Entity("cube", new Pos(-1f, 2f, 1f)));
        entities.add(new Entity("cube", new Pos(-2f, 0f, -1f)));
        entities.add(new Entity("pyramid", new Pos(-4f, 2f, 0f)));
        entities.add(new Entity("cube", new Pos(-1f, 0f, 2f)));

        this.entities = entities;

    }

    public Scene update(Window window) {

        CursorPos pos = window.getCursorPosition();

        this.camera = calculateCameraPosition(pos);
        this.light = calculateLightPosition(this.camera);

        return new Scene(this.camera, this.light, this.entities);

    }

    private Camera calculateCameraPosition(CursorPos pos) {

        float x = (float) pos.left / 50;
        float y = (float) pos.top / 50;

        float radius = 5;
        float min = -10;
        float max = 10;

        Pos eye = new Pos((float) (Math.cos(x) - Math.sin(x)) * radius, Math.min(max, Math.max(min, y)), (float) (Math.cos(x) + Math.sin(x)) * radius);
        Pos center = Pos.ZERO;
        Pos up = eye.add(Pos.UP);

        return new Camera(
                eye,
                center,
                up);

    }

    private Light calculateLightPosition(Camera camera) {

        return new Light(
                camera.getCenter().subtract(camera.getEye().add(new Pos(0, 2, 0))).toVec3().normalize(),
                Color.rgb(255, 255, 255),
                0.5f,
                0.5f,
                new LightSpecularity(0.25f, 5f));

    }

}
