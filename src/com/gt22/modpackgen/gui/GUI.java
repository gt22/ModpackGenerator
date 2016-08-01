package com.gt22.modpackgen.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI
{
	public static void init()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new MainFrame();
			}
		});
	}
}
