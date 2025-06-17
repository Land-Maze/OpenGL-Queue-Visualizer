package stackvisualizer;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import stackvisualizer.core.Cube;
import stackvisualizer.render.Camera;
import stackvisualizer.render.Entity;
import stackvisualizer.render.Material;
import stackvisualizer.render.Mesh;
import stackvisualizer.render.ModelMatrix;
import stackvisualizer.render.Renderer;
import stackvisualizer.render.Shader;
import stackvisualizer.render.TextRenderer;
import stackvisualizer.util.Window;

public class Main {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {

        /***********************************************************
         * ============================================================
         * Initialization of the application
         * ============================================================
         ***********************************************************/
        Window window = new Window(1280, 720, "Stack Visualizer", true);
        window.init();

        Camera camera = new Camera();

        TextRenderer textRenderer = new TextRenderer("fonts/RobotoMono-VariableFont_wght.ttf", 18f);

        long lastTime = System.nanoTime();
        int frames = 0;
        float fps = 0;
        float accumulatedTime = 0;

        Runtime runtime = Runtime.getRuntime();

        /***********************************************************
         * Shared resources
         ***********************************************************/
        Shader lightShader;
        try {
            lightShader = new Shader("shaders/light.vert", "shaders/light.frag");
        } catch (IOException e) {
            System.err.println("Error loading light shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Light shader loaded successfully.");

        Mesh cubeMesh = new Mesh(Cube.getVertices());

        Material cubeMaterial = new Material(lightShader, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));

        System.out.println("Cube mesh and material created successfully.");

        /***********************************************************
         * Creating entities
         ***********************************************************/
        Entity cube1 = new Entity("Cube 1", cubeMesh, cubeMaterial, new ModelMatrix()
                .translate(new Vector3f(-2.0f, 1.0f, 0.0f))
                .scale(new Vector3f(1.0f, 1.0f, 1.0f)));
        Entity cube2 = new Entity("Cube 2", cubeMesh, cubeMaterial, new ModelMatrix()
                .translate(new Vector3f(0.0f, 0.0f, 0.0f))
                .scale(new Vector3f(1.0f, 1.0f, 1.0f)));
        Entity cube3 = new Entity("Cube 3", cubeMesh, cubeMaterial, new ModelMatrix()
                .translate(new Vector3f(2.0f, -1.0f, 0.0f))
                .scale(new Vector3f(1.0f, 1.0f, 1.0f)));

        Renderer renderer = new Renderer();
        renderer.addObject(cube1);
        renderer.addObject(cube2);
        renderer.addObject(cube3);

        int totalVertices = renderer.getObjects().stream()
                .mapToInt(entity -> entity.getMesh().getVertexCount())
                .sum();

        System.out.println("Entities created successfully.");

        /***********************************************************
         * ============================================================
         * Main loop
         * ============================================================
         ***********************************************************/

        while (!window.shouldClose()) {

            /***********************************************************
             * Calculating FPS
             ***********************************************************/
            long now = System.nanoTime();
            float delta = (now - lastTime) / 1_000_000_000.0f;
            lastTime = now;

            accumulatedTime += delta;
            frames++;
            if (accumulatedTime >= 0.1f) { // 1/10 of a second
                fps = frames / accumulatedTime;
                frames = 0;
                accumulatedTime = 0;
            }

            /***********************************************************
             * Memory management
             ***********************************************************/
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            long freeMemory = maxMemory - usedMemory;

            window.clear();

            /***********************************************************
             * Handling window events and input
             ***********************************************************/
            if (window.isResized()) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
            }

            if (window.consumeReset()) {
                camera.reset();
            }

            // Rotating with mouse
            float dx = window.consumeDeltaX();
            float dy = window.consumeDeltaY();
            camera.rotate(dx * 0.3f, -dy * 0.3f);

            // Panning with mouse
            float px = window.consumePanX();
            float py = window.consumePanY();
            camera.pan(px * 0.005f, py * 0.005f);
            // Zooming with mouse wheel
            float dz = window.consumeDeltaZ();
            camera.zoom(dz * 0.1f);

            // Reseting scene with 'R' key
            if (window.consumeResetScene()) {
                camera.reset();
                renderer.reset();
                totalVertices = 0;
                System.out.println("Scene reset.");
            }

            if (window.consumePlaceRandomObject()) {
                Entity randomCube = new Entity("Random Cube", cubeMesh, new Material(lightShader, new Vector4f(
                        (float) Math.random(),
                        (float) Math.random(),
                        (float) Math.random(),
                        1.0f)),
                        new ModelMatrix().translate(new Vector3f(
                                (float) (Math.random() * 4 - 2),
                                (float) (Math.random() * 4 - 2),
                                (float) (Math.random() * 4 - 2))).scale(new Vector3f(
                                        0.5f + (float) Math.random(),
                                        0.5f + (float) Math.random(),
                                        0.5f + (float) Math.random())));
                renderer.addObject(randomCube);
                totalVertices += randomCube.getMesh().getVertexCount();
                System.out.println("Random object placed: " + randomCube.getName());
            }

            if (window.consumePlace100RandomObjects()) {
                for (int i = 0; i < 100; i++) {
                    Entity randomCube = new Entity("Random Cube " + i, cubeMesh, new Material(lightShader, new Vector4f(
                            (float) Math.random(),
                            (float) Math.random(),
                            (float) Math.random(),
                            1.0f)),
                            new ModelMatrix().translate(new Vector3f(
                                    (float) (Math.random() * 20 - 10),
                                    (float) (Math.random() * 20 - 10),
                                    (float) (Math.random() * 20 - 10))).scale(new Vector3f(
                                            0.5f + (float) Math.random(),
                                            0.5f + (float) Math.random(),
                                            0.5f + (float) Math.random())));
                    renderer.addObject(randomCube);
                    totalVertices += randomCube.getMesh().getVertexCount();
                }
                System.out.println("100 random objects placed.");
            }

            /***********************************************************
             * Rendering the scene
             ***********************************************************/

            Matrix4f view = camera.getViewMatrix();
            Matrix4f projection = camera.getProjectionMatrix(45.0f, window.getAspectRatio(), 0.1f, 100.0f);

            Vector3f viewPos = camera.getPosition();

            renderer.renderAll(view, projection, viewPos, (float) glfwGetTime());

            /***********************************************************
             * ============================================================
             * Rendering text overlay
             * ============================================================
             ***********************************************************/
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            /***********************************************************
             * Top left corner text overlay
             ***********************************************************/

            textRenderer.renderText(String.format("FPS: %.2f", fps), 10, 20);
            textRenderer.renderText(String.format("Total Vertices: %d", totalVertices), 10, 40);
            textRenderer.renderText(
                    String.format("View Position: [x: %.2f, y: %.2f, z: %.2f]", viewPos.x, viewPos.y, viewPos.z), 10,
                    60);
            textRenderer.renderText("Use mouse to rotate, pan, and zoom", 10, 80);
            textRenderer.renderText("Press <Space> to reset camera", 10, 100);
            textRenderer.renderText("Press <R> to reset the scene", 10, 120);
            textRenderer.renderText("Press <X> to place random object (distance = 2)", 10, 140);
            textRenderer.renderText("Press <F> to place 100 random object (distance = 10)", 10, 160);

            /***********************************************************
             * Bottom left corner text overlay
             ***********************************************************/

            textRenderer.renderText(String.format("Used Memory: %.2f MB", usedMemory / 1024.0 / 1024.0), 10,
                    window.getHeight() - 40);
            textRenderer.renderText(String.format("Free Memory: %.2f MB", freeMemory / 1024.0 / 1024.0), 10,
                    window.getHeight() - 20);
            textRenderer.renderText(String.format("Max Memory: %.2f MB", maxMemory / 1024.0 / 1024.0), 10,
                    window.getHeight() - 60);
            textRenderer.renderText(String.format("Total Memory: %.2f MB", runtime.totalMemory() / 1024.0 / 1024.0), 10,
                    window.getHeight() - 80);

            window.update();
        }

        // Cleanup
        {
            window.destroy();
            renderer.getObjects().forEach(Entity::cleanup);
            textRenderer.cleanup();
            System.out.println("Application closed successfully.");
        }
    }
}