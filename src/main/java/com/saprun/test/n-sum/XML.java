/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit;

import com.saprun.test.magnit.ex.XMLReadException;
import com.saprun.test.magnit.ex.XMLWriteException;
import com.saprun.test.magnit.pojo.Entries;
import com.saprun.test.magnit.pojo.Entry;
import com.saprun.test.magnit.pojo.TransformEntries;
import com.saprun.test.magnit.pojo.TransformEntry;
import com.saprun.test.magnit.util.Util;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 11:30:24
 */
public class XML {

    /* Наименования элементов и атрибутов для XML */
    public static final String ELEMENT_NAME_ENTRIES = "entries";
    public static final String ELEMENT_NAME_ENTRY = "entry";
    public static final String ELEMENT_NAME_FIELD = "field";

    /**
     * Запись в файл.
     * @param lib библиотека.
     * @param outFileName наименование сохраняемого файла.
     * @param data данные для записи в XML.
     * @param pretty форматирование. True - форматирование с переводом на новую строку и отступами.
     * @return True - успешное сохранение в файл.
     * @throws XMLWriteException
     */
    public static boolean write(XMLLib lib, String outFileName, Collection<Integer> data, boolean pretty) throws XMLWriteException {
        try {
            if(lib == XMLLib.JAXB)
                return writeJAXB(outFileName, data, pretty);
            else
                return writeJdom(outFileName, data, pretty);
        }
        catch(Exception ex) {
            throw new XMLWriteException("Can't write to file", ex);
        }
    }

    private static boolean writeJdom(String outFileName, Collection<Integer> data, boolean pretty) throws IOException {
        if(data == null) return false;
        if(Util.isEmpty(outFileName)) return false;

        Element rootElement = new Element(ELEMENT_NAME_ENTRIES);
        Document document = new Document(rootElement);
        for(Integer num : data) {
            Element entryElement = new Element(ELEMENT_NAME_ENTRY);
            entryElement.addContent(new Element(ELEMENT_NAME_FIELD).addContent(String.valueOf(num)));
            rootElement.addContent(entryElement);
        }

        try(FileWriter writer = new FileWriter(outFileName)) {
            XMLOutputter out = new XMLOutputter(pretty? Format.getPrettyFormat() : Format.getCompactFormat());
            out.output(document, writer);
            writer.close();

            return true;
        }
    }

    private static boolean writeJAXB(String outFileName, Collection<Integer> data, boolean pretty) throws JAXBException, IOException {
        if(data == null) return false;
        if(Util.isEmpty(outFileName)) return false;

        Entries entries = new Entries();
        for(Integer num : data) {
            entries.addEntry(new Entry(num));
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(Entries.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, pretty);
        try(FileWriter writer = new FileWriter(outFileName)) {
            marshaller.marshal(entries, writer);
            writer.close();

            return true;
        }
    }

    /**
     * Чтение из файла.
     * @param lib библиотека.
     * @param inFileName наименование читаемого файла.
     * @return True - успешное чтение из файла.
     * @throws XMLReadException
     */
    public static Collection<Integer> read(XMLLib lib, String inFileName) throws XMLReadException {
        try {
            if(lib == XMLLib.JAXB)
                return readJAXB(inFileName);
            else
                return readJdom(inFileName);
        }
        catch(Exception ex) {
            throw new XMLReadException("Can't read from file", ex);
        }
    }

    private static Collection<Integer> readJdom(String inFileName) throws JDOMException, IOException {
        if(Util.isEmpty(inFileName)) return Collections.EMPTY_LIST;

        try(FileReader reader = new FileReader(inFileName)) {
            SAXBuilder builder = new SAXBuilder(XMLReaders.NONVALIDATING);
            Document document = builder.build(reader);

            Element rootElement = document.getRootElement();
            List<Element> entryElements = rootElement.getChildren(ELEMENT_NAME_ENTRY);
            List<Integer> result = new ArrayList<>(entryElements.size());
            for(Element entryElement : entryElements) {
                try {
                    result.add(Integer.parseInt(entryElement.getAttributeValue(ELEMENT_NAME_FIELD)));
                }
                catch(NumberFormatException ex) {
                    throw new JDOMException("Attribute value parse error: " + ex.getMessage());
                }
            }

            return result;
        }
    }

    private static Collection<Integer> readJAXB(String inFileName) throws JAXBException, IOException {
        if(Util.isEmpty(inFileName)) return Collections.EMPTY_LIST;

        JAXBContext jaxbContext = JAXBContext.newInstance(TransformEntries.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        try(FileReader reader = new FileReader(inFileName)) {
            TransformEntries entries = (TransformEntries) unmarshaller.unmarshal(reader);
            reader.close();

            List<Integer> result = new ArrayList<>(entries.getEntries().size());
            for(TransformEntry entry : entries.getEntries()) {
                result.add(entry.getField());
            }

            return result;
        }
    }

    /**
     * Тарнсформация форматов.
     * @param inFileName наименование трансформируемого файла.
     * @param outFileName наименование файла, в который будет записан результат.
     * @param xsltFile файл с правилами для трансформации.
     * @return True - успешная трансформация.
     * @throws IOException
     * @throws TransformerException
     */
    public static boolean transform(String inFileName, String outFileName, String xsltFile) throws IOException, TransformerException {
        if(Util.isEmpty(inFileName)) return false;
        if(Util.isEmpty(outFileName)) return false;
        if(Util.isEmpty(xsltFile)) return false;

        try(FileReader reader = new FileReader(inFileName);
                FileWriter writer = new FileWriter(outFileName);
                InputStream xsltIn = XML.class.getResourceAsStream(xsltFile);) {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(xsltIn);
            Transformer transformer = factory.newTransformer(xslt);

            Source inSource = new StreamSource(reader);
            transformer.transform(inSource, new StreamResult(writer));

            return true;
        }
    }
}
