package stackvisualizer.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f position;
    private final Vector3f front;
    private final Vector3f up;
    private final Vector3f right;
    private final Vector3f worldUp;

    private float yaw;
    private float pitch;

    public Camera(Vector3f position) {
        this.position = position;
        this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.yaw = -90.0f; // look towards -Z by default
        this.pitch = 0.0f;

        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f();
        this.right = new Vector3f();
        updateVectors();
    }

    public Matrix4f getViewMatrix() {
        Vector3f center = new Vector3f(position).add(front);
        return new Matrix4f().lookAt(position, center, up);
    }

    public Matrix4f getProjectionMatrix(float fov, float aspect, float near, float far) {
        return new Matrix4f().perspective((float) Math.toRadians(fov), aspect, near, far);
    }

    public void move(Vector3f delta) {
        position.add(delta);
    }

    public void rotate(float deltaYaw, float deltaPitch) {
        yaw += deltaYaw;
        pitch += deltaPitch;

        // Clamp pitch to avoid gimbal lock
        pitch = Math.max(-89.0f, Math.min(89.0f, pitch));
        updateVectors();
    }

    private void updateVectors() {
        Vector3f newFront = new Vector3f();
        newFront.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        newFront.y = (float) Math.sin(Math.toRadians(pitch));
        newFront.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.set(newFront).normalize();

        right.set(front).cross(worldUp).normalize();
        up.set(right).cross(front).normalize();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getFront() {
        return front;
    }
}
