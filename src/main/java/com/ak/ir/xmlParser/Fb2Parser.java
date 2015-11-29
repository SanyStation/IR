package com.ak.ir.xmlParser;

import com.ak.ir.index.ZoneIndex;
import com.ak.ir.utils.IRUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by olko06141 on 27.11.2015.
 */
public class Fb2Parser {

    private final ZoneIndex index;
    private XPath xPath = XPathFactory.newInstance().newXPath();

    public Fb2Parser(ZoneIndex index) {
        this.index = index;
    }

    public void parseDocument(String filePath, int docID) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        Document xmlDocument = getXmlDocument(filePath);
        for (Fb2Rule rule : Fb2Rule.class.getEnumConstants()) {
            String row = (String) xPath.evaluate(rule.getXpath(), xmlDocument, XPathConstants.STRING);
            for (String word : row.split(IRUtils.SPACE_SYMBOL)) index.update(word, docID, rule.getZone());
        }
    }

    private Document getXmlDocument(String filePath) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }
}
