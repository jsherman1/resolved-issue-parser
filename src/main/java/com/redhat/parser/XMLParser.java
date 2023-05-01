package com.redhat.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.redhat.util.ParserUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
    public void parse(URL fileURL, boolean includeComponent, boolean addStyle) {

        // initialize component to empty string as this will not be set from the XML export from Jira
        final String component = "";
        int processed = 0;
        String link = "";
        String summary = "";
        StringBuffer sb = new StringBuffer();

        try {
            // Instantiate the DocumentBuilderFactory and DocumentBuilder objects
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML input file to obtain the DOM Document object
            Document document = builder.parse(new File(fileURL.toURI()));

            // Normalize the XML structure (removes empty text nodes and joins adjacent text nodes)
            document.getDocumentElement().normalize();

            if(addStyle) {
                sb.append(ParserUtil.addStyle());
            }
            sb.append(ParserUtil.addTable(includeComponent));

            // Get a list of item elements
            System.out.println("Gathering list of resolved issues...");
            NodeList nodeList = document.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                // get the child nodes of the item element: link and summary
                NodeList itemNodeList = node.getChildNodes();

                // iterate on the child elements of the item
                for (int j = 0; j < itemNodeList.getLength(); j++) {
                    Node itemNode = itemNodeList.item(j);
                    if (itemNode.getNodeType() == itemNode.ELEMENT_NODE) {
                        Element element = (Element) itemNode;

                        if (element.getTagName().equals("link")) {
                            link = element.getTextContent();
                        } else if (element.getTagName().equals("summary")) {
                            summary = element.getTextContent();
                        }
                    }
                }

                sb.append(ParserUtil.processLine(link + " - " + summary, component, includeComponent));
                processed++;
            }

            if (processed > 0) {
                ParserUtil.write(fileURL, sb, processed);
            } else {
                System.out.println("No issues were found");
            }

        } catch (ParserConfigurationException | IOException | SAXException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
