package stackvisualizer.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
  private final Vector3f target = new Vector3f(0, 0, 0);
  private float distance = 5.0f;
  private float yaw = 0.0f;
  private float pitch = 20.0f;

  public Matrix4f getViewMatrix() {
    Vector3f position = new Vector3f(
        (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)) * distance),
        (float) (Math.sin(Math.toRadians(pitch)) * distance),
        (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)) * distance)).add(target);

    return new Matrix4f().lookAt(position, target, new Vector3f(0, 1, 0));
  }

  public Matrix4f getProjectionMatrix(float fov, float aspect, float near, float far) {
    return new Matrix4f().perspective((float) Math.toRadians(fov), aspect, near, far);
  }

  public void rotate(float deltaYaw, float deltaPitch) {
    yaw += deltaYaw;
    pitch = Math.max(-89.9f, Math.min(89.9f, pitch + deltaPitch));
  }

  public void zoom(float delta) {
    distance = Math.max(0.1f, distance + delta);
  }

  public void pan(float dx, float dy) {
    Matrix4f view = getViewMatrix();
    Vector3f right = new Vector3f(view.m00(), view.m10(), view.m20()).normalize();
    Vector3f up = new Vector3f(view.m01(), view.m11(), view.m21()).normalize();

    right.mul(-dx);
    up.mul(dy);
    target.add(right).add(up);
  }

  public void reset() {
    target.set(0, 0, 0);
    distance = 5.0f;
    yaw = 0.0f;
    pitch = 20.0f;
  }

  public Vector3f getPosition() {
    return new Vector3f(
        (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)) * distance),
        (float) (Math.sin(Math.toRadians(pitch)) * distance),
        (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)) * distance)).add(target);
  }

}
