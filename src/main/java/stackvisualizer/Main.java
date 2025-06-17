package stackvisualizer;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import stackvisualizer.render.Camera;
import stackvisualizer.render.Renderer;
import stackvisualizer.render.Shader;
import stackvisualizer.util.Window;

public class Main {
    public static void main(String[] args) {

        System.out.println("Stack Visualizer");

        Window window = new Window(1280, 720, "Stack Visualizer", true);
        window.init();

        Renderer renderer = new Renderer();
        Camera camera = new Camera(new Vector3f(0, 0, 3));
        Shader shader = null;

        renderer.init();

        try {
            shader = new Shader("src/main/resources/shaders/cube.vert", "src/main/resources/shaders/cube.frag");
        } catch (IOException e) {
            System.err.println("Error loading shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Main loop
        while (!window.shouldClose()) {
            window.clear();

            Matrix4f model = new Matrix4f().translate(0, 0, -3);
            Matrix4f view = camera.getViewMatrix();
            Matrix4f projection = camera.getProjectionMatrix(45.0f, window.getAspectRatio(), 0.1f, 100.0f);

            renderer.render(shader, model, view, projection, new Vector4f(0.2f, 0.5f, 0.9f, 1.0f));

            window.update();
        }

        // Cleanup
        {
            window.destroy();
        }
    }
}