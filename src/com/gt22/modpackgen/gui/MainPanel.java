package com.gt22.modpackgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.gt22.modpackgen.FileUtils;
import com.gt22.modpackgen.Generator;

public class MainPanel extends PanelBase
{
	private static final String[] mcversions = new String[] {"1.7.10", "1.8", "1.8.9", "1.9", "1.9.4", "1.10", "1.10.2"};
	private JLabel id, name, ver, mcver, icon, descpath, modpath, errors;
	private JTextField nametxt, vertxt, idtxt;
	private JComboBox<String> mcvertxt;
	private JCheckBox useScript;
	private JButton chooseIcon, chooseDesc, chooseMinecraft, generate;
	private File desc, iconf, mpdir;
	public MainPanel(MainFrame instance)
	{
		initComponents();
		initListners(instance);
	}
	
	private void initComponents()
	{
		xadd(name = new JLabel("modpack name"), 0, 0);
		xadd(nametxt = new JTextField(10), 1, 0);
		xadd(id = new JLabel("modpack id"), 0, 1);
		xadd(idtxt = new JTextField(10), 1, 1);
		xadd(ver = new JLabel("modpack version"), 0, 2);
		xadd(vertxt = new JTextField(10), 1, 2);
		xadd(mcver = new JLabel("minecraft version"), 0, 3);
		xadd(mcvertxt = new JComboBox<String>(mcversions), 1, 3);
		xadd(chooseIcon = new JButton("Choose icon"), 0, 4);
		xadd(icon = new JLabel(), 1, 4);
		xadd(chooseDesc = new JButton("Choose description"), 0, 5);
		xadd(descpath = new JLabel(), 1, 5);
		xadd(chooseMinecraft = new JButton("Choose modpack forled"), 0, 6);
		xadd(modpath = new JLabel(), 1, 6);
		xadd(useScript = new JCheckBox("Use minetweaker scripts"), 0, 7);
		icon.setPreferredSize(new Dimension(126, 126));
		gc.weighty = 10;
		gc.anchor = GridBagConstraints.SOUTHWEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		xadd(errors = new JLabel(), 0, 9);
		xadd(generate = new JButton("Generate"), 0, 10);
		mcvertxt.setEditable(true);
	}
	
	private void initListners(MainFrame instance)
	{
		chooseIcon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				instance.open(instance.iconchooser);
			}
		});
		chooseDesc.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				instance.open(instance.descchooser);
			}
		});
		chooseMinecraft.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				instance.open(instance.mpchooser);
			}
		});
		generate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				generate();
			}
		});
	}
	
	private void generate()
	{
		errors.setForeground(new Color(255, 0, 0));
		if(nullOrEmpty(nametxt.getText()))
		{
			errors.setText("Enter name");
			return;
		}
		if(nullOrEmpty(idtxt.getText()))
		{
			errors.setText("Enter id");
			return;
		}
		if(nullOrEmpty(vertxt.getText()))
		{
			errors.setText("Enter version");
			return;
		}
		if(nullOrEmpty((String) mcvertxt.getSelectedItem()))
		{
			errors.setText("Enter minecraft version");
			return;
		}
		if(iconf == null)
		{
			errors.setText("Select icon");
			return;
		}
		BufferedImage img;
		try
		{
			img = ImageIO.read(iconf);
		}
		catch (IOException e)
		{
			errors.setText("Something wrong with icon");
			e.printStackTrace();
			return;
		}
		if(img.getHeight() != 126 || img.getWidth() != 126)
		{
			errors.setText("Image must be 126*126");
			return;
		}
		if(desc == null)
		{
			errors.setText("Select description file");
			return;
		}
		if(!FileUtils.containsFile(mpdir, "mods") || !FileUtils.containsFile(mpdir, "config") || useScript.isSelected() && !FileUtils.containsFile(mpdir, "scripts"))
		{
			errors.setText("Invalid modpack folder");
			return;
		}
		try
		{
			Generator.generate(idtxt.getText(), nametxt.getText(), vertxt.getText(), (String) mcvertxt.getSelectedItem(), iconf, desc, mpdir, useScript.isSelected());
		}
		catch (IOException e)
		{
			errors.setText("Unable to generate modpack");
			e.printStackTrace();
			return;
		}
		errors.setForeground(new Color(0, 255, 0));
		errors.setText("Mopack generated");
	}
	
	private boolean nullOrEmpty(String s)
	{
		return s == null || s.length() == 0;
	}
	
	public void setIcon(File icon)
	{
		try
		{
			this.iconf = icon;
			this.icon.setIcon(new ImageIcon(ImageIO.read(icon)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setDesc(File desc)
	{
		this.desc = desc;
		descpath.setText(desc.getAbsolutePath());
	}
	
	public void setMpdir(File mpdir)
	{
		this.mpdir = mpdir;
		modpath.setText(mpdir.getAbsolutePath());
	}
}
