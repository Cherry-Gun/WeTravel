package com.xlw.utils;

import com.xlw.exception.AndroidOnRoadException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by xinliwei on 2015/7/7.
 *
 * 自定义的XML解析工具,分别提供了DOM解析方法和XmlPullParser解析方法
 */
public class XMLParser {
    /**
     *
     * @param is InputStream类型的输入流
     * @param tag 要解析的XML文档标签
     * @return 返回解析后的字符串数组
     * @throws IOException
     */
    public static List<String> pullParse(InputStream is, String tag) throws IOException {
        List<String> contents = new ArrayList<>();
        XmlPullParserFactory factory;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            // 赋给解析器一个新的输入流
            parser.setInput(is, null);
            int eventType = parser.getEventType();

            // 持续直到文件结束为止
            while (eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();	//移动下一个标签
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals(tag)) {
                    // 提取文本内容
                    String text = parser.nextText();
                    contents.add(text);
                }
            }
        } catch (XmlPullParserException e) {
            throw new IOException("XML输入流解析异常");
        } catch (IOException e) {
            throw new IOException("XML解析IO异常");
        }

        // 返回解析出来的内容
        return contents;
    }

    public static List<String> pullParse(String url, String tag) throws AndroidOnRoadException {
        List<String> contents = new ArrayList<>();
        XmlPullParserFactory factory;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            // 赋给解析器一个新的输入流
            InputStream is = HttpUtil.openHttpConnection(url);
            parser.setInput(is, null);
            int eventType = parser.getEventType();

            // 持续直到文件结束为止
            while (eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();	//移动下一个标签
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals(tag)) {
                    // 提取文本内容
                    String text = parser.nextText();
                    contents.add(text);
                }
            }
        } catch (XmlPullParserException e) {
            throw new AndroidOnRoadException("XML输入流解析异常");
        } catch (IOException e) {
            throw new AndroidOnRoadException("XML解析IO异常");
        }

        // 返回解析出来的内容
        return contents;
    }

    /**
     *
     * @param is InputStream类型的输入流
     * @param tag 要解析的XML文档标签
     * @return 返回解析后的字符串数组
     * @throws Exception
     */
    public static List<String> domParse(InputStream is, String tag) throws Exception {
        List<String> contents = new ArrayList<>();

        // 调用自定义的createDocument()方法，解析输入流为一个Document文档
        Document document = ceateDocument(is);

        Element rootElement = document.getDocumentElement(); // <ArrayString>根元素

        NodeList nodes = rootElement.getElementsByTagName(tag);
        if (nodes != null & nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Element stringNode = (Element) nodes.item(i); // <string>元素
                Node node = stringNode.getFirstChild(); // <string>节点的第一个子节点，是个文本节点
                String text = node.getNodeValue(); // 该文本节点的值

                contents.add(text);	// 将解析出来的每一个省份名称加入到数组中
            }
        }

        // 返回省份
        return contents;
    }

    public static List<String> domParse(String url, String tag) throws Exception {
        List<String> contents = new ArrayList<>();

        // 调用自定义的createDocument()方法，解析输入流为一个Document文档
        InputStream is = HttpUtil.openHttpConnection(url);
        Document document = ceateDocument(is);

        Element rootElement = document.getDocumentElement(); // <ArrayString>根元素

        NodeList nodes = rootElement.getElementsByTagName(tag);
        if (nodes != null & nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Element stringNode = (Element) nodes.item(i); // <string>元素
                Node node = stringNode.getFirstChild(); // <string>节点的第一个子节点，是个文本节点
                String text = node.getNodeValue(); // 该文本节点的值

                contents.add(text);	// 将解析出来的每一个省份名称加入到数组中
            }
        }

        // 返回省份
        return contents;
    }

    // 将输入流构造为DOM Document文档对象
    private static Document ceateDocument(InputStream in) throws Exception {
        Document document = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            document = db.parse(in);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new Exception("解析配置异常");
        }

        return document;
    }
}
