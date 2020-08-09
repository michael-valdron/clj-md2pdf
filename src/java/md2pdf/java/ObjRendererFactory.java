package md2pdf.java;

import java.util.HashMap;
import java.util.Map;

import com.openhtmltopdf.extend.FSObjectDrawerFactory;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.w3c.dom.Element;

public class ObjRendererFactory implements FSObjectDrawerFactory {
    private final Map<String, IRenderer> renderers;

    public static void setObjectDrawerFactory(final PdfRendererBuilder builder, final Map<String, IRenderer> objFns) {
        builder.useObjectDrawerFactory(new ObjRendererFactory(objFns));
    }

    private ObjRendererFactory(final Map<String, IRenderer> objFns) {
        this.renderers = objFns;
    }

    public ObjRenderer createDrawer(final Element e) {
        String elementId;

        if (e.hasAttribute("id")) {
            elementId = e.getAttribute("id");

            if (renderers.containsKey(elementId)) {
                return new ObjRenderer(renderers.get(elementId));
            }
        }

        return null;
    }

    public boolean isReplacedObject(final Element e) {
        return false;
    }
}