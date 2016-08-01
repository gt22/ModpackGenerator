package com.gt22.modpackgen.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelBase extends JPanel
{
	protected GridBagConstraints gc;
	protected PanelBase()
	{
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.WEST;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
	}
	protected void xadd(Component c, int x, int y)
	{
		gc.gridx = x;
		gc.gridy = y;
		add(c, gc);
	}
}
