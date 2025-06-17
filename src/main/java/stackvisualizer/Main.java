package stackvisualizer;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import stackvisualizer.Meshes.Cube;
import stackvisualizer.render.Camera;
import stackvisualizer.render.Entity;
import stackvisualizer.render.Material;
import stackvisualizer.render.Mesh;
import stackvisualizer.render.ModelMatrix;
import stackvisualizer.render.RectRenderer;
import stackvisualizer.render.Renderer;
import stackvisualizer.render.Shader;
import stackvisualizer.render.TextRenderer;
import stackvisualizer.ui.Button;
import stackvisualizer.ui.Label;
import stackvisualizer.ui.UIManager;
import stackvisualizer.util.Window;

public class Main {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        /***********************************************************
         * ============================================================
         * Initialization of the application
         * ============================================================
         ***********************************************************/
        
        UIManager uiManager = null;

        Window window = new Window(1280, 720, "Stack Visualizer", true, uiManager);
        window.init();

        TextRenderer textRenderer = new TextRenderer("fonts/RobotoMono-VariableFont_wght.ttf", 18f);
        RectRenderer rectRenderer = new RectRenderer();
        rectRenderer.init();
        uiManager = new UIManager(rectRenderer, textRenderer);


        Camera camera = new Camera();

        /***********************************************************
         * ============================================================
         * Adding UI elements
         * ============================================================
         ***********************************************************/

        /***********************************************************
         * Top left corner text overlay
         ***********************************************************/
        uiManager.add("fps-label", new Label("FPS: 0", 10, 20, 200, 30));
        uiManager.add("total-vertices-label", new Label("Total Vertices: 0", 10, 40, 200, 30));
        uiManager.add("view-position-label", new Label("View Position: [x: 0.0, y: 0.0, z: 0.0]", 10, 60, 300, 30));
        uiManager.add("controls-label", new Label("Use mouse to rotate, pan, and zoom", 10, 80, 300, 30));
        uiManager.add("reset-camera-label", new Label("Press <Space> to reset camera", 10, 100, 300, 30));
        uiManager.add("reset-scene-label", new Label("Press <R> to reset the scene", 10, 120, 300, 30));

        /***********************************************************
         * Bottom left corner text overlay
         ***********************************************************/
        uiManager.add("total-memory-label", new Label("Total Memory: 0 MB", 10, window.getHeight() - 80, 300, 30));
        uiManager.add("used-memory-label", new Label("Used Memory: 0 MB", 10, window.getHeight() - 60, 300, 30));
        uiManager.add("free-memory-label", new Label("Free Memory: 0 MB", 10, window.getHeight() - 40, 300, 30));
        uiManager.add("max-memory-label", new Label("Max Memory: 0 MB", 10, window.getHeight() - 20, 300, 30));

        /***********************************************************
         * Bottom center overlay
         ***********************************************************/
        uiManager.add("place-box-button", new Button("Add",
                window.getWidth() / 2 - 50,
                window.getHeight() - 60,
                100,
                40,
                () -> {
                    System.out.println("Place Box button clicked.");
                }));

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

            var click_position = window.consumeLeftMouseButtonClick();
            if(click_position != null) {
                double mouseX = click_position.x;
                double mouseY = click_position.y;
                uiManager.onClick(mouseX, mouseY);
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
            uiManager.get("fps-label").setText(String.format("FPS: %.2f", fps));
            uiManager.get("total-vertices-label").setText(String.format("Total Vertices: %d", totalVertices));
            uiManager.get("view-position-label").setText(
                    String.format("View Position: [x: %.2f, y: %.2f, z: %.2f]", viewPos.x, viewPos.y, viewPos.z));

            /***********************************************************
             * Bottom left corner text overlay
             ***********************************************************/
            uiManager.get("total-memory-label")
                    .setText(String.format("Total Memory: %.2f MB", runtime.totalMemory() / 1024.0 / 1024.0));
            uiManager.get("used-memory-label")
                    .setText(String.format("Used Memory: %.2f MB", usedMemory / 1024.0 / 1024.0));
            uiManager.get("free-memory-label")
                    .setText(String.format("Free Memory: %.2f MB", freeMemory / 1024.0 / 1024.0));
            uiManager.get("max-memory-label")
                    .setText(String.format("Max Memory: %.2f MB", maxMemory / 1024.0 / 1024.0));

            uiManager.updateAll(new Vector2f(window.getWidth(), window.getHeight()));
            uiManager.renderAll();

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