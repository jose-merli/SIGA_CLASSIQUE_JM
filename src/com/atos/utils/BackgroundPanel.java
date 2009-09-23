
package com.atos.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * This is a special panel that captures a region of the screen and paints it
 * into itself.
 * <p>
 * It is useful to simulate a trasparent window. See <code>SplashWindow</code>
 * for more details.
 * <p>
 * <b>Note:</b> Please note that capturing large screen portions can be slow,
 * so try to adjust the capture just to the needed space.
 *
 */
public  class BackgroundPanel extends JPanel {
  /**
   * The captured image
   */
  private BufferedImage im;
  
  /**
   * Left coordinate of the rectangle of the screen being captured.
   */
  private int x;
  /**
   * Upper coordinate of the rectangle of the screen being captured.
   */  
  private int y;
  /**
   * Width of the rectangle of the screen being captured.
   */
  private int width;
  /**
   * Height of the rectangle of the screen being captured.
   */
  private int height;
  
  private Robot robot;
  
  /**
   * Constructs a <code>BackgroundPanel</code> of a specified region in the
   * screen.
   *
   * @param x Left coordinate in pixels of the part of screen being captured.
   * @param y Upper coordinate in pixels of the part of screen being captured.
   * @param width  Width of the rectangle being captured. 
   * @param height Height of the rectangle being captured.
   */
  public BackgroundPanel(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    
    try {
      robot = new Robot();
    } catch(Exception e) {  
      e.printStackTrace();
    }
    
    capture();
  }
  
  /**
   * Catpures the desired rectangle of screen, defined by <code>x</code>,
   * <code>y</code>, <code>width</code> and <code>height</code>.
   */
  public void capture() {
    try {
      im = robot.createScreenCapture(new Rectangle(x, y, width, height));
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    repaint();
  }
  
  /**
   * Paints the component with the captured image.
   *
   * @param   g   The graphics object where to paint the captured image.
   * @see     #capture()    capture()
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    if (im != null) {
      g.drawImage(im, 0, 0, null);
    }
  }
}
