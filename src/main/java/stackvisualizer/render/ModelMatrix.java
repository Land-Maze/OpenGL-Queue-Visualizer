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
        matrix.rotationXYZ((float) Math.toRadians(angleX), (float) Math.toRadians(angleY), (float) Math.toRadians(angleZ));
    }
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        matrix.scale(scaleX, scaleY, scaleZ);
    }
    public void setPosition(Vector3f position) {
        matrix.translation(position);
    }
    public void setRotation(Vector3f rotation) {
        matrix.rotationXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
    }
    public void setScale(Vector3f scale) {
        matrix.scale(scale);
    }

    // public ModelMatrix translate(Vector3f translation) {
    //     matrix.translate(translation);
    //     return this;
    // }

    // public ModelMatrix rotateX(float angle) {
    //     matrix.rotateX(angle);
    //     return this;
    // }

    // public ModelMatrix rotateY(float angle) {
    //     matrix.rotateY(angle);
    //     return this;
    // }

    // public ModelMatrix rotateZ(float angle) {
    //     matrix.rotateZ(angle);
    //     return this;
    // }

    // public ModelMatrix scale(Vector3f scaling) {
    //     matrix.scale(scaling);
    //     return this;
    // }

    public Matrix4f getMatrix() {
        return new Matrix4f(matrix);
    }

    public Matrix4f asMatrix() {
      return new Matrix4f(matrix);
    }

    public void reset() {
        matrix.identity();
    }
    public ModelMatrix copy() {
        return new ModelMatrix().set(matrix);
    }
    public ModelMatrix set(Matrix4f other) {
        this.matrix.set(other);
        return this;
    }
    public ModelMatrix set(ModelMatrix other) {
        this.matrix.set(other.matrix);
        return this;
    }

    @Override
    public String toString() {
        return "ModelMatrix{" +
                "matrix=" + matrix +
                '}';
    }
}