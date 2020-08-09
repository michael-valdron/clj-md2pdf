package md2pdf.java;

import java.awt.Graphics2D;
import java.util.HashMap;

public interface IRenderer {
    public void render(HashMap<String, String> elements, Graphics2D g);
}