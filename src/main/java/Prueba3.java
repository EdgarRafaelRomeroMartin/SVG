import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class Prueba3 extends JFrame {

    public static final String TAG = Prueba3.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(TAG);

    private JMenuBar menuBar;


    Document svgDoc = null;
    private Canvas dibujo;
    public Prueba3() {
        super("Prueba 3");
        dibujo = new Canvas(svgDoc);
        svgDoc = openMenu();
        buildGUI();

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Prueba3().setVisible(true);
            }
        });
    }

    private Document getNewDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();

            doc = builder.newDocument();
            Element root = doc.createElement("svg");
            root.setAttribute("width", "640");
            root.setAttribute("height", "480");
            root.setAttribute("version", "1.1");
            root.setAttribute("xmlns", "http://www.w3.org/2000/svg");
            doc.appendChild(root);
        } catch (ParserConfigurationException e) {
            LOGGER.severe(e.getMessage());
            doc = null;
        }
        return doc;
    }

    private void buildGUI() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(newItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File( System.getProperty("user.dir")));
                int returnVal = fc.showSaveDialog(Prueba3.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    saveDocument(svgDoc,file);

                    LOGGER.info(file.getName());
                } else {
                    LOGGER.info("Cancel saving");
                }

            }
        });
        fileMenu.add(saveItem);

        JMenuItem quitItem = new JMenuItem("Quit",
                KeyEvent.VK_Q);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(quitItem);

        JMenu shapeMenu = new JMenu("Shape");
        menuBar.add(shapeMenu);

        JMenuItem lineItem = new JMenuItem("Line",
                KeyEvent.VK_L);
        lineItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewLine dialog = new NewLine(Prueba3.this, dibujo);
                dialog.setVisible(true);

            }
        });
        shapeMenu.add(lineItem);

        JMenuItem circleItem = new JMenuItem("Circle",
                KeyEvent.VK_C);
        circleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewCircle dialog = new NewCircle(Prueba3.this, dibujo);
                dialog.setVisible(true);
            }
        });
        shapeMenu.add(circleItem);
        JMenuItem rectleItem = new JMenuItem("Rectangulo",
                KeyEvent.VK_R);
        rectleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewRect dialog = new NewRect(Prueba3.this, dibujo);
                dialog.setVisible(true);
            }
        });
        shapeMenu.add(rectleItem);

        this.getContentPane().add(dibujo);
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640, 480);
    }

    public  final void saveDocument(Document xml, File file) {

        Transformer tf = null;

        FileWriter out = null;
        try {
            tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            out = new FileWriter(file);
            tf.transform(new DOMSource(xml), new StreamResult(out));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        } catch (TransformerException e) {
            LOGGER.severe(e.getMessage());
        }

    }

    private Document openMenu() {
        final JFileChooser fc = new JFileChooser();

        String userDir = System.getProperty("user.dir");

        fc.setCurrentDirectory(new File(userDir));

        // Abrir dialogo para seleccion de archivo
        fc.setDialogTitle("Seleccionar Imagen SVG");
        fc.setAcceptAllFileFilterUsed(false);

        // Mostrar unicamente archivos SVG
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos SVG", "svg");
        fc.addChoosableFileFilter(filter);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Obtener archivo seleccionado
            File file = fc.getSelectedFile();
            System.out.println(file);
            //This is where a real application would open the file.

            // Con el archivo seleccionado, crear un documento DOM
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            Document doc = null;
            DocumentBuilder dBuilder = null;



            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                LOGGER.severe(ex.getMessage());
            }


            try {
                doc = dBuilder.parse(file);

                Element root = doc.createElement("svg");
                doc.getDocumentElement().appendChild(root);
                doc.getDocumentElement().normalize();
            } catch (SAXException ex) {
                LOGGER.severe(ex.getMessage());
            } catch (IOException ex) {
                LOGGER.severe(ex.getMessage());
            }
            DocumentFrame intFrame = new DocumentFrame(file.getName(),doc);

            dibujo.add(intFrame);

            intFrame.setVisible(true);

            }

        return null;
    }

    }

