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
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {

        System.out.println("Stack Visualizer");

        Window window = new Window(1280, 720, "Stack Visualizer", true);
        window.init();

        Renderer renderer = new Renderer();
        Camera camera = new Camera();
        Shader shader;

        Vector3f lightPos = new Vector3f(1.2f, 1.0f, 2.0f);

        renderer.init();

        try {
            shader = new Shader("src/main/resources/shaders/cube.vert", "src/main/resources/shaders/cube.frag");
        } catch (IOException e) {
            System.err.println("Error loading shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        long lastTime = System.nanoTime();
        int frames = 0;
        float fps = 0;
        
        // Main loop
        while (!window.shouldClose()) {
            long now = System.nanoTime();
            float delta = (now - lastTime) / 1_000_000_000.0f;
            lastTime = now;

            frames++;
            if (delta >= 0.0f) {
                fps = frames / delta;
                frames = 0;
                lastTime = now;
            }
            System.out.printf("FPS: %.2f\n", fps);

            window.clear();

            if (window.consumeReset()) {
                camera.reset();
            }

            float dx = window.consumeDeltaX();
            float dy = window.consumeDeltaY();
            camera.rotate(dx * 0.3f, -dy * 0.3f);
            float dz = window.consumeDeltaZ();
            camera.zoom(dz * 0.1f);

            float px = window.consumePanX();
            float py = window.consumePanY();
            camera.pan(px * 0.005f, py * 0.005f);

            Matrix4f model = new Matrix4f();
            Matrix4f view = camera.getViewMatrix();
            Matrix4f projection = camera.getProjectionMatrix(45.0f, window.getAspectRatio(), 0.1f, 100.0f);

            Vector3f viewPos = camera.getPosition();

            renderer.render(shader, model, view, projection, new Vector4f(0.902f, 0.31f, 0.549f, 1.0f), lightPos, viewPos);

            window.update();
        }

        // Cleanup
        {
            window.destroy();
        }
    }
}