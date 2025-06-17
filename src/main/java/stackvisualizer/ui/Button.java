package stackvisualizer.ui;

import org.joml.Vector4f;

public class Button extends UIElement {
    private String label;
    private Runnable onClick;

    public Button(String label, float x, float y, float width, float height, Runnable onClick) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onClick = onClick;

        this.textRenderer = null;
        this.rectRenderer = null;
    }

    @Override
    public void render() {
        rectRenderer.drawRectangle(x, y, width, height, new Vector4f(0.2f, 0.2f, 0.8f, 1f), screenWidth, screenHeight);
        textRenderer.renderText(label, x + 5, y + 5);
    }

    @Override
    public void update() { }

    public void click(float px, float py) {
        if (contains(px, py)) onClick.run();
    }
}
