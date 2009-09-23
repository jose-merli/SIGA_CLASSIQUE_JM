package com.siga.gui.processTree;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class SIGADialog extends JDialog
{
	private JApplet parentApplet;
	private boolean frameSizeAdjusted = false;
	
	private JTextField dateField = new JTextField();
	JButton confirmButton = new JButton();
	JButton cancelButton  = new JButton();
	private GridLayout gridLayout1 = new GridLayout();
	private JButton jButton1 = new JButton();
	private Border border1;
	private JPanel containerPane = new JPanel();
	Color defaultColor = new Color(238,237,243);
	private JPanel jPanel1 = new JPanel();// Default background

	public SIGADialog()
  	{
		setModal(true);
		setTitle("  Please Confirm Modification");
		getContentPane().setLayout(null);
		this.setResizable(false);
		getContentPane().setBackground(new Color(238,237,243));
		setSize(300,200);
		setForeground(new java.awt.Color(238,237,243));
		
		confirmButton.setText("Apply");
		confirmButton.setActionCommand("Confirmar");
		getContentPane().add(confirmButton);
		confirmButton.setBackground(new java.awt.Color(255,199,59));
		confirmButton.setForeground(new java.awt.Color(72,36,79));
		confirmButton.setFont(new Font("Dialog", Font.BOLD, 11));
		confirmButton.setBounds(40,165,95,25);
		border1 = BorderFactory.createMatteBorder(2,2,2,2,new Color(247, 140, 0));
		confirmButton.setBackground(new Color(247, 218, 151));
		confirmButton.setBorder(border1);
		
		cancelButton.setText("Cancel");
		cancelButton.setActionCommand("Cancelar");
		getContentPane().add(cancelButton);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 11));
		cancelButton.setBackground(new java.awt.Color(255,199,59));
		cancelButton.setForeground(new java.awt.Color(72,36,79));
		cancelButton.setBounds(160,165,95,25);
		cancelButton.setBackground(new Color(247, 218, 151));
		cancelButton.setBorder(border1);

		cancelButton.addActionListener(new ActionListener()
		{
      		public void actionPerformed(ActionEvent e)
      		{
        		doCancel();
      		}
    	});
    	
    	confirmButton.addActionListener(new ActionListener()
    	{
      		public void actionPerformed(ActionEvent e)
      		{
        		doCancel();
      		}
    	});
    	
    	dialogInit();
	}

	public SIGADialog(String sTitle)
  	{
    	this();
    	setTitle(sTitle);
  	}

  	public void doCancel()
  	{
    	//System.out.println("do cancel");
  	}

  	public void doOk()
  	{
    	//System.out.println("do ok");
  	}
}