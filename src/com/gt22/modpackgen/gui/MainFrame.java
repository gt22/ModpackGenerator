package com.gt22.modpackgen.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import com.gt22.modpackgen.Const;

public class MainFrame extends JFrame
{
	private MainPanel mainpanel;
	public JFileChooser iconchooser, descchooser;
	public MainFrame()
	{
		setSize(Const.startwidth, Const.startheight);
		setTitle("Modpack generator");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		setVisible(true);
		setResizable(false);
	}
	
	private void initComponents()
	{
		add(mainpanel = new MainPanel(this), BorderLayout.CENTER);
		iconchooser = new JFileChooser(new File("."));
		descchooser = new JFileChooser(new File("."));
		iconchooser.setFileFilter(new FileFilter()
		{
			
			@Override
			public String getDescription()
			{
				return "*.png";
			}
			
			@Override
			public boolean accept(File f)
			{
				return f.getName().endsWith(".png") || f.isDirectory();
			}
		});
		descchooser.setFileFilter(new FileFilter()
		{
			
			@Override
			public String getDescription()
			{
				return "*.txt, *.html";
			}
			
			@Override
			public boolean accept(File f)
			{
				return f.getName().endsWith(".txt") || f.getName().endsWith(".html") || f.isDirectory();
			}
		});
		iconchooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getActionCommand().equals("ApproveSelection"))
				{
					mainpanel.setIcon(iconchooser.getSelectedFile());
				}
			}
		});
		descchooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getActionCommand().equals("ApproveSelection"))
				{
					mainpanel.setDesc(descchooser.getSelectedFile());
				}
			}
		});	
	}
	
	public void open(JFileChooser c)
	{
		c.showOpenDialog(getContentPane());
	}
}
