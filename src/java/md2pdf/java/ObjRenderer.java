package md2pdf.java;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.HashMap;
import java.util.Map;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.extend.OutputDeviceGraphicsDrawer;
import com.openhtmltopdf.render.RenderingContext;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ObjRenderer implements FSObjectDrawer {
    private final IRenderer renderer;

    private class OutputDeviceObjRenderer implements OutputDeviceGraphicsDrawer {
        private final IRenderer renderer;
        private final Element element;

        public OutputDeviceObjRenderer(final IRenderer renderer, final Element element) {
            this.renderer = renderer;
            this.element = element;
        }

        private HashMap<String, String> mapElementAttrs() {
            Node n;
            final NamedNodeMap attributes = element.getAttributes();
            final HashMap<String, String> attrs = new HashMap<>();

            for (int i = 0; i < attributes.getLength(); i++) {
                n = attributes.item(i);
                attrs.put(n.getNodeName(), n.getNodeValue());
            }

            return attrs;
        }

        public void render(final Graphics2D g) {
            renderer.render(mapElementAttrs(), g);
        }
    }

    public ObjRenderer(final IRenderer renderer) {
        this.renderer = renderer;
    }

    private float lengthPerPixelDots(final double x, final int dotsPerPixel) {
        return ((float) x / (float) dotsPerPixel);
    }

    public Map<Shape, String> drawObject(final Element e, final double x, final double y, final double width,
            final double height, final OutputDevice oDevice, final RenderingContext rContext, final int dotsPerPixel) {
        final OutputDeviceObjRenderer oDeviceGraphicsDrawer = new OutputDeviceObjRenderer(renderer, e);

        oDevice.drawWithGraphics((float) x, (float) y, lengthPerPixelDots(width, dotsPerPixel), lengthPerPixelDots(height, dotsPerPixel), oDeviceGraphicsDrawer);
        return null;
    }
}