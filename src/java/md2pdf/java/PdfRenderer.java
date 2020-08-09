package md2pdf.java;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

/**
 * PdfRenderer
 */
public class PdfRenderer {

    public static void setLinkTag(final Element head, final String href) {
        head.appendElement("link").attr("type", "text/css").attr("rel", "stylesheet").attr("href", href);
    }

    public static void setStyleTag(final Element head, final String style) {
        head.appendElement("style").attr("type", "text/css").text(style);
    }

    public static void setMetaTag(final Element head, final String name, final String content) {
        head.appendElement("meta").attr("name", name).attr("content", content);
    }

    public static Element selectHead(final org.jsoup.nodes.Document htmlDoc) {
        return htmlDoc.head();
    }

    public static org.jsoup.nodes.Document makeHtmlDoc(final String html, final String baseUri) {
        final Parser p = Parser.htmlParser().settings(ParseSettings.preserveCase);

        return Jsoup.parse(html, baseUri, p);
    }

    // TODO: Watermark rendering.
    public static boolean renderPdf(final org.jsoup.nodes.Document htmlDoc, final String baseUri,
            final String pdfPath, final Map<String, IRenderer> objFns) {
        FileOutputStream os;
        PdfRendererBuilder builder;
        boolean isRendered = false;
        org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(htmlDoc);

        try {
            os = new FileOutputStream(pdfPath);
        } catch (final FileNotFoundException e) {
            System.err.println(e.getMessage());
            return isRendered;
        }

        try {
            builder = new PdfRendererBuilder();
            builder.useFastMode();

            ObjRendererFactory.setObjectDrawerFactory(builder, objFns);
            
            builder.useSVGDrawer(new BatikSVGDrawer());
            builder.withW3cDocument(w3cDoc, baseUri);
            builder.toStream(os);
            builder.run();
            isRendered = true;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                os.close();
            } catch (final IOException e) {
                System.err.println(e.getMessage());
            }

            return isRendered;
        }
    }

    public static boolean renderPdf(final org.jsoup.nodes.Document htmlDoc, final String baseUri,
            final String pdfPath) {
        return renderPdf(htmlDoc, baseUri, pdfPath, null);
    }
}