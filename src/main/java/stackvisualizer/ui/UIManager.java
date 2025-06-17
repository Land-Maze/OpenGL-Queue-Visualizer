package stackvisualizer.ui;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import stackvisualizer.render.RectRenderer;
import stackvisualizer.render.TextRenderer;

public class UIManager {
  private Map<String, UIElement> elements = new HashMap<>();
  RectRenderer rectRenderer;
  TextRenderer textRenderer;

  public UIManager(RectRenderer rectRenderer, TextRenderer textRenderer) {
    this.rectRenderer = rectRenderer;
    this.textRenderer = textRenderer;
  }

  public void add(String key, UIElement e) {
    if (e == null) return; // Check for NullPointerException
    e.rectRenderer = this.rectRenderer;
    e.textRenderer = this.textRenderer;
    elements.put(key, e);
  }
  
  public UIElement get(String key) {
    return elements.get(key);
  }

  public void remove(String key) {
    elements.remove(key);
  }

  public void onClick(double xpos, double ypos) {
    for (UIElement e : elements.values()) {
      if (e instanceof Button b) b.click(xpos, ypos);
      if (e instanceof TextInput t) t.click(xpos, ypos);
    }
  }

  public void onCharInput(char c) {
    for (UIElement e : elements.values()) {
      if (e instanceof TextInput t && t.isActive()) {
        t.type(c);
      }
    }
  }

  public void renderAll() {
    for (UIElement e : elements.values()) {
      e.render();
    }
  }

  public void updateAll(Vector2f screenSize) {
    for (UIElement e : elements.values()) {
      e.screenWidth = (int) screenSize.x;
      e.screenHeight = (int) screenSize.y;
      e.update();
    }
  }
}
