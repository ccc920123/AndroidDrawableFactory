package com.stardon.androiddrawable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Main extends JFrame {



    //Instance parameters
    JLabel imageCanvas; //canvas that store the image to modify
    JFileChooser imageChooser, projectPathChooser; //JFileChooser for the source image and project path
    JTextField projectPathField, sourceSizeTextField; //fields to store projectPath and source image size
    JButton projectPathButton, createButton; //Buttons to open the path chooser and to start drawables conversion
    JLabel sourceDensityLabel, sourceSizeLabel; //labels for source density and source size fields
    JComboBox<String> sourceDensityComboBox; //list of available densities
    LinkedHashMap<String, JCheckBox> densitiesCheckBox; //HashMap that stores the available density checkboxes
    JPanel mainPanel, densitiesPanel; //panel containing checkboxes
    private File lastUsedSourceDirectory;
    private File lastUsedProjectDirectory;
    //JProgressBar progressBar;

    File sourceImg; //source Image File object
    String sourceFileName; //source file name
    BufferedImage bufferedSource; //Source image BufferedImage object

    public Main()
    {
        super("Main Window");
        initUI(); //initialize ui elements
        initListeners(); //initialize ui event listeners
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initUI()
    {
        //create components
        imageCanvas = new JLabel(); //image to be used
        imageCanvas.setText("<html><font size=5 color =blue>点击添加<br> 目标图片</font></html>");
        imageCanvas.setText("点击添加图片");
        imageCanvas.setBackground(Color.decode("#33B5E5"));
        imageCanvas.setBorder(BorderFactory.createLineBorder(Color.black));
        imageCanvas.setToolTipText("点击选择你要转换的图片");
        projectPathChooser = new JFileChooser(); //Launch directory selection
        projectPathField = new JTextField(); //Retains  the path selected with JFileChooser
        projectPathField.setEditable(false);
        projectPathField.setText("图片储存路径");
        projectPathButton = new JButton("选择..."); //Button that launch JFileChooser
        sourceDensityLabel = new JLabel("初始文件的密度"); //Label for the source density field
        sourceDensityComboBox = new JComboBox<String>(DrawableAction.DENSITIES); //selector for the source density
        sourceSizeLabel = new JLabel("文件大小"); //Label for source image's size
        sourceSizeTextField = new JTextField(); //Field for source image's size
        sourceSizeTextField.setEditable(false);
        densitiesCheckBox = new LinkedHashMap<String, JCheckBox>(); //checkbox Map with densities
        createButton = new JButton("开始转换"); //button to begin drawable conversion
        densitiesPanel = new JPanel();
        //initialize checkboxes
        for(int i = 0; i < DrawableAction.DENSITIES.length; i++)
        {
            String density = DrawableAction.DENSITIES[i];
            densitiesCheckBox.put(density, new JCheckBox(density));
        }
        for(JCheckBox e : densitiesCheckBox.values())
        {
            e.setSelected(true);
            densitiesPanel.add(e);
        }
        densitiesPanel.add(createButton);

        //create and set LayoutManager
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        mainPanel = new JPanel();
        GroupLayout gp = new GroupLayout(mainPanel);
        gp.setAutoCreateContainerGaps(true);
        gp.setAutoCreateGaps(true);
        mainPanel.setLayout(gp);
        //set alignment criteria
        Alignment hAlign = Alignment.TRAILING;
        Alignment vAlign = Alignment.BASELINE;

        //add component into layout
        //set horizontal group
        gp.setHorizontalGroup(gp.createSequentialGroup()
                .addGroup(gp.createParallelGroup(hAlign)
                        .addComponent(imageCanvas, 80, 80, 80))
                .addGroup(gp.createParallelGroup(hAlign)
                        .addComponent(projectPathField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        //.addComponent(projectPathField)
                        .addComponent(sourceDensityLabel, Alignment.LEADING)
                        .addComponent(sourceSizeLabel, Alignment.LEADING))
                .addGroup(gp.createParallelGroup(hAlign)
                        .addComponent(projectPathButton)
                        .addComponent(sourceDensityComboBox,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(sourceSizeTextField,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, 50))
                        );

        //set vertical group
        gp.setVerticalGroup(gp.createSequentialGroup()
                .addGroup(gp.createParallelGroup(vAlign)
                        .addComponent(imageCanvas, 80, 80, 80)
                .addGroup(gp.createSequentialGroup()
                        .addGroup(gp.createParallelGroup(vAlign)
                                .addComponent(projectPathField)
                                .addComponent(projectPathButton))
                        .addGroup(gp.createParallelGroup(vAlign)
                                .addComponent(sourceDensityLabel)
                                .addComponent(sourceDensityComboBox))
                        .addGroup(gp.createParallelGroup(vAlign)
                                .addComponent(sourceSizeLabel)
                                .addComponent(sourceSizeTextField)))
                        )
                );
        this.add(mainPanel);
        this.add(densitiesPanel);
    }

    private void initListeners()
    {
        //Source Image click listener

        imageCanvas.addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub
                JFileChooser imageChooser = new JFileChooser();
                if (lastUsedSourceDirectory != null) {
                    imageChooser.setCurrentDirectory(lastUsedSourceDirectory);
                }
                imageChooser.setDialogTitle("选择转换的图片");
                imageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                imageChooser.setFileFilter(new FileFilter(){

                    @Override
                    public boolean accept(File file) {
                        if(file.getName().endsWith(".jpg") ||
                                file.getName().endsWith(".png") || file.isDirectory())
                        {
                            return true;
                        }
                        else return false;
                    }

                    @Override
                    public String getDescription() {
                        return "Images (.jpg;.png)";
                    }

                });

                imageChooser.setAcceptAllFileFilterUsed(false);
                switch(imageChooser.showOpenDialog(imageCanvas))
                {
                    case(JFileChooser.APPROVE_OPTION):
                        try {
                            sourceImg = new File(imageChooser.getSelectedFile().getPath());
                            lastUsedSourceDirectory = sourceImg.getParentFile();
                            sourceFileName = sourceImg.getName();
                            bufferedSource = ImageIO.read(sourceImg);
                            Image sourceResized = ImageUtils.resizeImage(bufferedSource, 80, 80);
                            imageCanvas.setIcon(new ImageIcon(sourceResized));
                            sourceSizeTextField.setText(Integer.toString(ImageUtils.getMaxWidth(sourceImg)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }

            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

        });

        projectPathButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                projectPathChooser = new JFileChooser();
                if (lastUsedProjectDirectory != null) {
                    projectPathChooser.setCurrentDirectory(lastUsedProjectDirectory);
                }
                projectPathChooser.setDialogTitle("应用程序的项目根目录");
                projectPathChooser.setAcceptAllFileFilterUsed(false);
                projectPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(projectPathChooser.showOpenDialog(projectPathButton) ==  JFileChooser.APPROVE_OPTION) {
                    projectPathField.setText(projectPathChooser.getSelectedFile().getPath());
                    lastUsedProjectDirectory = projectPathChooser.getSelectedFile();
                }
            }

        });


        //"Make" button action listener

        createButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub

                if(bufferedSource == null || projectPathField.getText().equals("图片储存路径"))
                {
                    JOptionPane.showMessageDialog(rootPane, "请选择图片转换后需要放置的路径", "错误", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    //disable make button
                    createButton.setEnabled(false);
                    resizeThread = new Thread(resizeRunnable);
                    resizeThread.start();
                }           
            }

        });

    }


    Thread resizeThread;
    Runnable resizeRunnable = new Runnable(){

        public void run() {
            // TODO Auto-generated method stub
            //handy references to AndroidDrawableFactory.class constants
            String[] densities = DrawableAction.DENSITIES;
            double[] density_ratio = DrawableAction.DENSITY_MULTIPLIERS;
            //create hashmap with density and density ratio
            HashMap<String, Double> densityMap = new HashMap<String, Double>();
            for(int i = 0; i < densities.length; i++)
            {
                densityMap.put(densities[i], density_ratio[i]);
            }
            double targetDensity = densityMap.get(sourceDensityComboBox.getSelectedItem().toString());
            for(Map.Entry<String, Double> e : densityMap.entrySet())
            {
                JCheckBox singleDensity = densitiesCheckBox.get(e.getKey());
                String projectPath = projectPathField.getText();
                File projectResourceRoot = new File(projectPath);
                /*if (!"res".equals(projectResourceRoot.getName())) {
                    projectResourceRoot = new File(projectResourceRoot, "res");
                }*/
                if(singleDensity.isSelected())
                {
                    String folderName = "drawable-" + e.getKey();
                    double densityRatio = e.getValue();

                    int newWidth = Math.round((float)(bufferedSource.getWidth() / targetDensity * densityRatio));
                    int newHeight = Math.round((float)(bufferedSource.getHeight() / targetDensity * densityRatio));

                    try {
                        Image newImg = ImageUtils.resizeImage(bufferedSource, newWidth, newHeight);
                        File targetDir = new File(projectResourceRoot, folderName);
                        boolean dirExists = false;
                        //check if project dir exists, if not create it
                        dirExists = targetDir.exists() || targetDir.mkdir();
                        if(dirExists)
                        {
                            BufferedImage bufImg = new BufferedImage(newImg.getWidth(null), newImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                            Graphics2D img2D = bufImg.createGraphics();
                            img2D.drawImage(newImg, null, null);
                            RenderedImage targetImg = (RenderedImage) bufImg;
                            File newFile = new File(targetDir + File.separator + sourceFileName);
                            ImageIO.write(targetImg, "png", newFile);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }                       
                }

            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                    JOptionPane.showMessageDialog(getContentPane(),  "调整完成!", "完成", JOptionPane.INFORMATION_MESSAGE);
                    createButton.setEnabled(true);
                }

            });
        }

    };
}
