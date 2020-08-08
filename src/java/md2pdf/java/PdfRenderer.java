package md2pdf.java;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

/**
 * PdfRenderer
 */
public class PdfRenderer {

    public static void setLinkTag(Element head, String href) {
        head.appendElement("link")
            .attr("type", "text/css")
            .attr("rel", "stylesheet")
            .attr("href", href);
    }

    public static void setStyleTag(Element head, String style) {
        head.appendElement("style")
            .attr("type", "text/css")
            .text(style);
    }

    public static void setMetaTag(Element head, String name, String content) {
        head.appendElement("meta")
            .attr("name", name)
            .attr("content", content);
    }

    public static Element selectHead(org.jsoup.nodes.Document htmlDoc) {
        return htmlDoc.head();
    }

    public static org.jsoup.nodes.Document makeHtmlDoc(String html, String baseUri) {
        Parser p = Parser.htmlParser()
            .settings(ParseSettings.preserveCase);
        
        return Jsoup.parse(html, baseUri, p);
    }

    public static boolean renderPdf(org.jsoup.nodes.Document htmlDoc, String baseUri, String pdfPath) {
        FileOutputStream os;
        PdfRendererBuilder builder;
        boolean isRendered = false;
        org.w3c.dom.Document w3cDoc = new W3CDom()
            .fromJsoup(htmlDoc);

        try {
            os = new FileOutputStream(pdfPath);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return isRendered;
        }

        try {
            builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withW3cDocument(w3cDoc, baseUri);
            builder.toStream(os);
            builder.run();
            isRendered = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            return isRendered;
        }
    }
}