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
        rectRenderer.drawRectangle(x, y, width, height, new Vector4f(0.5f, 0.2f, 0f, 0.2f), screenWidth, screenHeight);
        textRenderer.renderText(label, x + (width - textRenderer.getTextWidth(label)) / 2, y + (height - textRenderer.getTextHeight()) / 2 + 5);
    }

    @Override
    public void update() { }

    public void click(double px, double py) {
        if (contains(px, py)) onClick.run();
    }
}
