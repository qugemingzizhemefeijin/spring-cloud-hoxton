package com.atguigu.springcloud.other.document.tika;

import org.apache.commons.io.IOUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.DocumentSelector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.*;
import org.apache.tika.parser.digestutils.CommonsDigester;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.apache.tika.sax.boilerpipe.BoilerpipeContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TikaExampleTest2 {

    private static final int MAX_MARK = 20 * 1024 * 1024;//20MB

    private static Parser parser;

    public static void main(String[] args) throws Exception {
        TikaConfig config = TikaConfig.getDefaultConfig();

        parser = new DigestingParser(new AutoDetectParser(config),
                new CommonsDigester(MAX_MARK, CommonsDigester.DigestAlgorithm.MD5 + "," + CommonsDigester.DigestAlgorithm.SHA256), false);

        ParseContext context = new ParseContext();
        context.set(DocumentSelector.class, new ImageDocumentSelector());
        context.set(Parser.class, new ImageSavingParser(parser));

        Metadata metadata = new Metadata();
        TikaInputStream input = TikaInputStream.get(Paths.get("C:\\Users\\a\\Desktop\\20220919-预警联系人.xlsx"), metadata);
        if (input.markSupported()) {
            int mark = -1;
            if (input.hasFile()) {
                mark = (int) input.getLength();
            }
            if (mark == -1) {
                mark = MAX_MARK;
            }
            input.mark(mark);
        }

        // ContentHandler handler = new BodyContentHandler();

        StringWriter htmlBuffer = new StringWriter();
        StringWriter textBuffer = new StringWriter();
        StringWriter textMainBuffer = new StringWriter();
        StringWriter xmlBuffer = new StringWriter();
        StringBuilder metadataBuffer = new StringBuilder();

        ContentHandler handler =
                new TeeContentHandler(getHtmlHandler(htmlBuffer), getTextContentHandler(textBuffer),
                        getTextMainContentHandler(textMainBuffer), getXmlContentHandler(xmlBuffer));

        parser.parse(input, handler, metadata, context);

        String[] names = metadata.names();
        Arrays.sort(names);

        for (String name : names) {
            for (String val : metadata.getValues(name)) {
                metadataBuffer.append(name);
                metadataBuffer.append(": ");
                metadataBuffer.append(val);
                metadataBuffer.append("\n");
            }
        }

        System.out.println(metadataBuffer.toString());
        //System.out.println(htmlBuffer.toString());
        //System.out.println(textBuffer.toString());
        //System.out.println(textMainBuffer.toString());
        //System.out.println(xmlBuffer.toString());

        System.out.println("================");

//        RecursiveParserWrapperHandler recursiveParserWrapperHandler =
//                new RecursiveParserWrapperHandler(new BasicContentHandlerFactory(
//                        BasicContentHandlerFactory.HANDLER_TYPE.BODY, -1), -1);
//        RecursiveParserWrapper wrapper = new RecursiveParserWrapper(parser);
//        wrapper.parse(input, recursiveParserWrapperHandler, new Metadata(), new ParseContext());
//        StringWriter jsonBuffer = new StringWriter();
//        JsonMetadataList.setPrettyPrinting(true);
//        JsonMetadataList.toJson(recursiveParserWrapperHandler.getMetadataList(), jsonBuffer);
//
//        System.out.println(jsonBuffer.toString());
    }

    private static ContentHandler getHtmlHandler(Writer writer) throws TransformerConfigurationException {
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
        handler.setResult(new StreamResult(writer));
        return new ContentHandlerDecorator(handler) {
            @Override
            public void startElement(String uri, String localName, String name, Attributes atts)
                    throws SAXException {
                if (XHTMLContentHandler.XHTML.equals(uri)) {
                    uri = null;
                }
                if (!"head".equals(localName)) {
                    if ("img".equals(localName)) {
                        AttributesImpl newAttrs;
                        if (atts instanceof AttributesImpl) {
                            newAttrs = (AttributesImpl) atts;
                        } else {
                            newAttrs = new AttributesImpl(atts);
                        }

                        for (int i = 0; i < newAttrs.getLength(); i++) {
                            if ("src".equals(newAttrs.getLocalName(i))) {
                                String src = newAttrs.getValue(i);
                                if (src.startsWith("embedded:")) {
                                    String filename = src.substring(src.indexOf(':') + 1);
                                    try {
                                        File img = new ImageSavingParser(parser).requestSave(filename);
                                        String newSrc = img.toURI().toString();
                                        newAttrs.setValue(i, newSrc);
                                    } catch (IOException e) {
                                        System.err.println(
                                                "Error creating temp image file " + filename);
                                        // The html viewer will show a broken image too to alert them
                                    }
                                }
                            }
                        }
                        super.startElement(uri, localName, name, newAttrs);
                    } else {
                        super.startElement(uri, localName, name, atts);
                    }
                }
            }

            @Override
            public void endElement(String uri, String localName, String name) throws SAXException {
                if (XHTMLContentHandler.XHTML.equals(uri)) {
                    uri = null;
                }
                if (!"head".equals(localName)) {
                    super.endElement(uri, localName, name);
                }
            }

            @Override
            public void startPrefixMapping(String prefix, String uri) {
            }

            @Override
            public void endPrefixMapping(String prefix) {
            }
        };
    }

    private static ContentHandler getTextContentHandler(Writer writer) {
        return new BodyContentHandler(writer);
    }

    private static ContentHandler getTextMainContentHandler(Writer writer) {
        return new BoilerpipeContentHandler(writer);
    }

    private static ContentHandler getXmlContentHandler(Writer writer)
            throws TransformerConfigurationException {
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
        handler.setResult(new StreamResult(writer));
        return handler;
    }

    private static class ImageDocumentSelector implements DocumentSelector {
        public boolean select(Metadata metadata) {
            String type = metadata.get(Metadata.CONTENT_TYPE);
            return type != null && type.startsWith("image/");
        }
    }

    private static class ImageSavingParser extends AbstractParser {
        private Map<String, File> wanted = new HashMap<>();
        private Parser downstreamParser;
        private File tmpDir;

        private ImageSavingParser(Parser downstreamParser) {
            this.downstreamParser = downstreamParser;

            try {
                File t = Files.createTempFile("tika", ".test").toFile();
                tmpDir = t.getParentFile();
            } catch (IOException e) {
                //swallow
            }
        }

        public File requestSave(String embeddedName) throws IOException {
            String suffix = ".tika";

            int splitAt = embeddedName.lastIndexOf('.');
            if (splitAt > 0) {
                embeddedName.substring(splitAt);
            }

            File tmp = Files.createTempFile("tika-embedded-", suffix).toFile();
            wanted.put(embeddedName, tmp);
            return tmp;
        }

        public Set<MediaType> getSupportedTypes(ParseContext context) {
            return downstreamParser.getSupportedTypes(context);
        }

        public void parse(InputStream stream, ContentHandler handler, Metadata metadata,
                          ParseContext context) throws IOException, SAXException, TikaException {
            String name = metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY);
            if (name != null && wanted.containsKey(name)) {
                FileOutputStream out = new FileOutputStream(wanted.get(name));
                IOUtils.copy(stream, out);
                out.close();
            } else {
                if (downstreamParser != null) {
                    downstreamParser.parse(stream, handler, metadata, context);
                }
            }
        }

    }

}
