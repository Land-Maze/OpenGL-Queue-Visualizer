package stackvisualizer.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModelMatrix {
    private final Matrix4f matrix;

    public ModelMatrix() {
        this.matrix = new Matrix4f();
    }

    public void setPosition(float x, float y, float z) {
        matrix.translation(x, y, z);
    }

    public void setRotation(float angleX, float angleY, float angleZ) {
        matrix.rotationXYZ((float) Math.toRadians(angleX), (float) Math.toRadians(angleY),
                (float) Math.toRadians(angleZ));
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        matrix.scale(scaleX, scaleY, scaleZ);
    }

    public void setPosition(Vector3f position) {
        matrix.translation(position);
    }

    public void setRotation(Vector3f rotation) {
        matrix.rotationXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y),
                (float) Math.toRadians(rotation.z));
    }

    public void setScale(Vector3f scale) {
        matrix.scale(scale);
    }

    public ModelMatrix translate(Vector3f translation) {
        matrix.translate(translation);
        return this;
    }

    public ModelMatrix rotate(Vector3f rotation) {
        matrix.rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y),
                (float) Math.toRadians(rotation.z));
        return this;
    }

    public ModelMatrix scale(Vector3f scale) {
        matrix.scale(scale);
        return this;
    }

    public ModelMatrix translate(float x, float y, float z) {
        matrix.translate(x, y, z);
        return this;
    }

    public ModelMatrix rotate(float angleX, float angleY, float angleZ) {
        matrix.rotateXYZ((float) Math.toRadians(angleX), (float) Math.toRadians(angleY),
                (float) Math.toRadians(angleZ));
        return this;
    }

    public ModelMatrix scale(float scaleX, float scaleY, float scaleZ) {
        matrix.scale(scaleX, scaleY, scaleZ);
        return this;
    }

    public Matrix4f getMatrix() {
        return new Matrix4f(matrix);
    }

    public void reset() {
        matrix.identity();
    }

    public ModelMatrix copy() {
        return new ModelMatrix().set(this);
    }

    private ModelMatrix set(ModelMatrix other) {
        this.matrix.set(other.getMatrix());
        return this;
    }

    @Override
    public String toString() {
        return "ModelMatrix{" +
                "matrix=" + matrix +
                '}';
    }
}