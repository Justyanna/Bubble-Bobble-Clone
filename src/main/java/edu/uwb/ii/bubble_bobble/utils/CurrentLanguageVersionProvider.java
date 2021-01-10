package edu.uwb.ii.bubble_bobble.utils;

import edu.uwb.ii.bubble_bobble.scenes.menu.MenuSceneController;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class CurrentLanguageVersionProvider {

    public static String currentLanguageVersion = "language_versions/eng.xml";

    public static Document loadXml() {
        Document doc = null;
        try {
            InputStream in = MenuSceneController.class.getClassLoader()
                    .getResourceAsStream(CurrentLanguageVersionProvider.currentLanguageVersion);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }
}
