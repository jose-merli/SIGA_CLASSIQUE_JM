package com.atos.utils;
/**
 * @author ASD
 * @version 1.0
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class ExampleSplashWindow extends JApplet
                        implements ActionListener {

//public class ExampleSplashWindow {
  private static SplashWindow splash;
  
  public ExampleSplashWindow() {
	
    }

  public void actionPerformed(java.awt.event.ActionEvent e) {
        /*
        if (e.getActionCommand().equals("disable")) {
            b2.setEnabled(false);
            b1.setEnabled(false);
            b3.setEnabled(true);
        } else { 
            b2.setEnabled(true);
            b1.setEnabled(true);
            b3.setEnabled(false);
        }
        */
    }

public static void main(String[] args) {
	Frame frame = new Frame("Aplicacion: SIGA");

	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	ExampleSplashWindow applet = new ExampleSplashWindow();
	applet.init();
	frame.add(applet, BorderLayout.CENTER);
	frame.pack();
	frame.show();
    }

/*  
  public static void main(String[] args) {
            
    // Creation of the SplashWindow, using transparency
    //splash = new SplashWindow("/LoadingScreen.png", true);
    splash = new SplashWindow("/siga.png", true);
    
    // Between every increment of the progress bar there will be a delay of ne second.
    splash.setDelay(1000);

    // Setting progress bar properties.
    splash.setProgressBarMaximum(10);
    splash.setProgressBarValue(0);
    splash.setProgressBarBounds(29, 86, 215, 4);
    splash.setProgressBarColor(Color.red);
    splash.setProgressBarVisible(true);
    
    // Setting progress text properties
    splash.setProgressTextBounds(31, 67, 200, 15);
    splash.setProgressTextFont(new Font("SYSTEM", Font.PLAIN, 10));
    //splash.setProgressTextColor(Color.yellow.brighter());
    splash.setProgressTextVisible(true);
    
    // Setting a title label
    JLabel title = new JLabel();
    title.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 17));
    title.setForeground(new Color(64, 0, 0));
    title.setText("Sistema de Gestión");    
    title.setBounds(20, 10, 220, 30);
    splash.add(title);
    
    JLabel title2 = new JLabel();
    title2.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 17));
    title2.setForeground(new Color(64, 0, 0));
    title2.setText("                    de la Abogacía");
    title2.setBounds(20, 23, 220, 30);
    splash.add(title2);
        
    // Setting a subtitle label
    JLabel subtitle = new JLabel();
    subtitle.setFont(new Font("Dialog", Font.PLAIN, 11));
    subtitle.setForeground(new Color(32, 0, 0));
    subtitle.setText("Version 1.0");
    subtitle.setBounds(24, 32, 200, 30);
    splash.add(subtitle);
    
    // Setting a copyright notice
    JLabel copyright = new JLabel();
    copyright.setFont(new Font("Dialog", Font.PLAIN, 9));
    //copyright.setForeground(Color.lightGray);    
    copyright.setForeground(Color.red);    
    copyright.setText("(C) 2004, AtosOrigin. http://www.atosorigin.com");
    copyright.setBounds(50, 50, 200, 20);
    splash.add(copyright);
    
        
    // Showing splash window
    splash.setVisible(true);
    
    // Wait
    try {
      Thread.sleep(800);
    } catch (InterruptedException e) {}
    
    // Simulate we are loading something
    for (int i = 0 ; i < 10 ; i++) {
      splash.setProgressText("Cargando aplicación (" + (i + 1) + ")...");
      splash.incrementProgressBarValue(1);
    }
    
    // wait
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}
    
    // hide
    splash.close();
    
    // bye
    System.exit(0);
  }
  
  */
  
  public void init() {

  System.out.println("SIGA SplashWindow\n");
            
    // Creation of the SplashWindow, using transparency
    //splash = new SplashWindow("/LoadingScreen.png", true);
    splash = new SplashWindow("/siga.png", true);
    
    // Between every increment of the progress bar there will be a delay of ne second.
    splash.setDelay(1000);

    // Setting progress bar properties.
    splash.setProgressBarMaximum(10);
    splash.setProgressBarValue(0);
    splash.setProgressBarBounds(29, 86, 215, 4);
    splash.setProgressBarColor(Color.red);
    splash.setProgressBarVisible(true);
    
    // Setting progress text properties
    splash.setProgressTextBounds(31, 67, 200, 15);
    splash.setProgressTextFont(new Font("SYSTEM", Font.PLAIN, 10));
    //splash.setProgressTextColor(Color.yellow.brighter());
    splash.setProgressTextVisible(true);
    
    // Setting a title label
    JLabel title = new JLabel();
    title.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 17));
    title.setForeground(new Color(64, 0, 0));
    title.setText("Sistema de Gestión");    
    title.setBounds(20, 10, 220, 30);
    splash.add(title);
    
    JLabel title2 = new JLabel();
    title2.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 17));
    title2.setForeground(new Color(64, 0, 0));
    title2.setText("                    de la Abogacía");
    title2.setBounds(20, 23, 220, 30);
    splash.add(title2);
        
    // Setting a subtitle label
    JLabel subtitle = new JLabel();
    subtitle.setFont(new Font("Dialog", Font.PLAIN, 11));
    subtitle.setForeground(new Color(32, 0, 0));
    subtitle.setText("Version 1.0");
    subtitle.setBounds(24, 32, 200, 30);
    splash.add(subtitle);
    
    // Setting a copyright notice
    JLabel copyright = new JLabel();
    copyright.setFont(new Font("Dialog", Font.PLAIN, 9));
    //copyright.setForeground(Color.lightGray);    
    copyright.setForeground(Color.red);    
    copyright.setText("(C) 2004, AtosOrigin. http://www.atosorigin.com");
    copyright.setBounds(50, 50, 200, 20);
    splash.add(copyright);
    
    /*
    // Setting a "close" button
    JButton closeButton = new JButton();
    closeButton.setFont(new java.awt.Font("Dialog", 0, 10));
    closeButton.setText("X");
    closeButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
    closeButton.setBounds(270, 12, 17, 17);
    closeButton.setOpaque(false);
    closeButton.setForeground(new Color(64, 0, 0));
    closeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeActionPerformed(evt);
      }
    });
    closeButton.setVisible(true);
    splash.add(closeButton);
    */
    
    // Showing splash window
    splash.setVisible(true);
    
    // Wait
    try {
      Thread.sleep(800);
    } catch (InterruptedException e) {}
    
    // Simulate we are loading something
    // 10 segundos
    //for (int i = 0 ; i < 10 ; i++) {
	for (int i = 0 ; i < 5 ; i++) {
      splash.setProgressText("Cargando aplicación (" + (i + 1) + ")...");
      splash.incrementProgressBarValue(1);
    }
    
    // wait
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}
    
    // hide
    splash.close();
    
    // bye
    System.exit(0);
    }
  
  // When the user clicks the closeButton the Splash Window will hide (but
  // the program will continue working.
  private static void closeActionPerformed(java.awt.event.ActionEvent evt) {
    splash.close();
  }
}
