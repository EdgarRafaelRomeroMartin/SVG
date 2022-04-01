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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class Prueba extends JFrame {

    public static final String TAG = Prueba.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(TAG);

    private JMenuBar menuBar;
    private Canvas dibujo;
    private JDesktopPane desktopPane;

    Document svgDoc = null;

    public Prueba() {
        super("Prueba SVG");
        this.setSize(800, 600);
        buildGUI();
    }


    private Document getNewDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder crear = dbf.newDocumentBuilder();

            doc = crear.newDocument();
            Element root = doc.createElement("svg");
            doc.appendChild(root);
        } catch (ParserConfigurationException e) {
            LOGGER.severe(e.getMessage());
            doc = null;
        }
        return doc;
    }

    private Document getNewDocument2() {
        DocumentFrame documentFrame= (DocumentFrame) desktopPane.getSelectedFrame();
        if (documentFrame != null) {

        }
        documentFrame.repaint();
        return null;
    }

    private void buildGUI() {
        desktopPane = new JDesktopPane();
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
//Sus

        JMenuItem OpenItem = new JMenuItem("Open");
        OpenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(OpenItem);
        ///sus
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File( System.getProperty("user.dir")));
                int returnVal = fc.showSaveDialog(Prueba.this);
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
                NewLine dialog = new NewLine(Prueba.this, dibujo);
                dialog.setVisible(true);
              desktopPane.add(dialog);
            }
        });
        shapeMenu.add(lineItem);

        JMenuItem circleItem = new JMenuItem("Circle",
                KeyEvent.VK_C);
        circleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewCircle dialog = new NewCircle(Prueba.this, dibujo);
                dialog.setVisible(true);
                desktopPane.add(dialog);
            }
        });
        shapeMenu.add(circleItem);
        JMenuItem rectleItem = new JMenuItem("Rectangulo",
                KeyEvent.VK_R);
        rectleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewRect dialog = new NewRect(Prueba.this, dibujo);
                dialog.setVisible(true);
                desktopPane.add(dialog);
            }
        });
        shapeMenu.add(rectleItem);

        //
        JMenuItem rotateItem = new JMenuItem("Rotar",
                KeyEvent.VK_R);
        rotateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateMenuItemMenuItemActionPerformed(e);
            }
        });
        shapeMenu.add(rotateItem);

        this.setJMenuBar(menuBar);
        dibujo = new Canvas(svgDoc);


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        pack();

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

    private void openMenuItemActionPerformed(ActionEvent evt) {
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
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                LOGGER.severe(ex.getMessage());
            }

            Document doc = null;
            try {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

            } catch (SAXException ex) {
                LOGGER.severe(ex.getMessage());
            } catch (IOException ex) {
                LOGGER.severe(ex.getMessage());
            }


            DocumentFrame intFrame = new DocumentFrame(file.getName(),doc);

            desktopPane.add(intFrame);
            intFrame.setVisible(true);

        } else {

        }
    }
    private void rotateMenuItemMenuItemActionPerformed(ActionEvent evt) {
        Object[] options = {"15", "30", "45", "60","75", "90"};

        String s = (String) JOptionPane.showInputDialog(
                this,
                "Angulo de rotación:",
                "Rotar",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                "15");
        if ((s != null) && (s.length() > 0)) {
            double angulo = Double.parseDouble(s);
            DocumentFrame documentFrame =  (DocumentFrame) desktopPane.getSelectedFrame();

            if( documentFrame != null ) {
                System.out.println( documentFrame.getTitle() );

                Util.rotateSVG(documentFrame.getDocument(), angulo);

            }
            documentFrame.repaint();
        }
    }

    private void scaleMenuItemMenuItemActionPerformed(ActionEvent evt) {

        Object[] options = {"25%", "50%", "75%", "100%"};

        String s = (String) JOptionPane.showInputDialog(
                this,
                "Porcentaje a escalar:",
                "Scale",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                "25%");

        if ((s != null) && (s.length() > 0)) {
            double scaleFactor = 1.0;
            if (s.equals("25%")) {
                scaleFactor = 1.25;
            }
            if (s.equals("50%")) {
                scaleFactor = 1.50;
            }
            if (s.equals("75%")) {
                scaleFactor = 1.75;
            }
            if (s.equals("100%")) {
                scaleFactor = 2.0;
            }

            DocumentFrame documentFrame =  (DocumentFrame) desktopPane.getSelectedFrame();

            if( documentFrame != null ) {
                System.out.println( documentFrame.getTitle() );

                Util.scaleSVG(documentFrame.getDocument(), scaleFactor);

                Dimension d = documentFrame.getPreferredSize();
                d.height = (int) (d.height * scaleFactor);
                d.width  = (int) (d.width * scaleFactor);
                documentFrame.setSize( d );
            }
            documentFrame.repaint();
        }
    }
    public static void main(String args[]) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        } catch (InstantiationException ex) {
            LOGGER.severe(ex.getMessage());
        } catch (IllegalAccessException ex) {
            LOGGER.severe(ex.getMessage());
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.severe(ex.getMessage());
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Prueba().setVisible(true);
            }
        });

    }
}
