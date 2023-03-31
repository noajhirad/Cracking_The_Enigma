package EngineManager;


import XMLFilesEx3.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XMLReader {

    private String path = null;
    private File f = null;
    public XMLReader(String newPath){ path = newPath; }
    public XMLReader(File f){
        path = f.getPath();
        this.f = f;
    }

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "XMLFilesEx3";

    public CTEEnigma readFile() throws FileNotFoundException, JAXBException{
        CTEEnigma enigma = null;

        if (f == null)
            f = new File(path);

        InputStream inputStream = new FileInputStream(f);
        enigma = deserializeFrom(inputStream);

        return enigma;
    }

    public static CTEEnigma deserializeFrom(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(inputStream);
    }

}

