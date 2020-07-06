package com.stardon.androiddrawable;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

public class DrawableAction extends AnAction {
    public static final String[] DENSITIES = { "ldpi", "mdpi", "hdpi", "xhdpi",
            "xxhdpi", "xxxhdpi" };
    public static final double[] DENSITY_MULTIPLIERS = { .75, 1, 1.5, 2, 3, 4 };

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
    @Override
    public void actionPerformed(AnActionEvent e) {
        initLaF();
        Main mainWindow = new Main();
        mainWindow.pack();
        mainWindow.setTitle("Android图片转换工具");
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.setVisible(true);

    }
    private static void initLaF() {
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        try {
            boolean hasGTK = false;
            for (UIManager.LookAndFeelInfo info : lafs) {
                if (info.getName().equals("GTK+")) {
                    hasGTK = true;
                    break;
                }
            }
            if (hasGTK) {
                UIManager
                        .setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
