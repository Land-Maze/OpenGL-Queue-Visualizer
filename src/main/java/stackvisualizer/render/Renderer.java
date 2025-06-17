package stackvisualizer.render;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Renderer {
  private final List<Entity> objects = new ArrayList<>();

  public void addObject(Entity obj) {
    objects.add(obj);
  }

  public void renderAll(Matrix4f view, Matrix4f projection, Vector3f viewPos, float time) {
    Vector3f lightPos = new Vector3f(
        (float) Math.sin(time) * 5.0f,
        1.0f + (float) Math.sin(time * 2.0f) * 0.5f,
        (float) Math.cos(time) * 5.0f);
    for (Entity obj : objects) {
      obj.render(view, projection, viewPos, lightPos);
    }
  }

  public void reset() {
    objects.clear();
  }

  public List<Entity> getObjects() {
    return objects;
  }
}