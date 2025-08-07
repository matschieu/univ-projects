package com.github.matschieu.color.selector.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.matschieu.color.selector.model.ColorCustomModel;
import com.github.matschieu.color.selector.model.MColor;

/**
 * @author Matschieu
 */
public class ColorChooserPanel extends JPanel {
	
	private List<JPanel> colorsPanelList;
	private JButton addColorButton;
	private JButton delColorButton;
	private JPanel colorsPanel;
	private ColorCustomModel model;
	
	public ColorChooserPanel(ColorCustomModel model) {
		
		this.model = model;
		this.model.getInitialColors().clear();
		
		this.colorsPanelList = new LinkedList<JPanel>();
		
		this.setLayout(new BorderLayout());
		
		this.addColorButton = new JButton("Ajouter une couleur");
		this.delColorButton = new JButton("Supprimer une couleur");
		this.delColorButton.setEnabled(false);
		
		JPanel p = new JPanel();
		p.add(this.addColorButton);
		p.add(this.delColorButton);
		
		this.add(p, BorderLayout.NORTH);
		
		this.colorsPanel = new JPanel();
		this.colorsPanel.setPreferredSize(new Dimension(670, 50));
		this.add(this.colorsPanel, BorderLayout.CENTER);
		
		this.addColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSelectedColor();
			}
		});
		this.delColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delSelectedColor();
			}
		});
	}
	
	public void addSelectedColor() {
		Color colorTmp = JColorChooser.showDialog(this, "Couleur", null);
		if (colorTmp == null)
			return;		
		MColor color = new MColor(colorTmp);

		JPanel p = new JPanel();
		p.setBackground(color);
		p.setPreferredSize(new Dimension(20, 20));
		
		if (this.model.getInitialColors().contains(color)) {
			String msg = "Cette couleur a déjà été sélectionnée";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.model.getInitialColors().add(color);
		this.colorsPanelList.add(p);
		
		this.colorsPanel.add(p);
		this.colorsPanel.setVisible(false);
		this.colorsPanel.setVisible(true);

		if (this.colorsPanelList.size() > 0)
			this.delColorButton.setEnabled(true);
		
		if (this.model.getInitialColors().size() == ColorCustomModel.MAX_INIT_COLOR) {
			String msg = "Nombre de couleurs maximum atteint\nVeuillez supprimer une couleur pour en ajouter une nouvelle";
			JOptionPane.showMessageDialog(null, msg, "Attention", JOptionPane.WARNING_MESSAGE);
			this.addColorButton.setEnabled(false);
		}
	}

	public void delSelectedColor() {
		this.model.getInitialColors().remove(this.model.getInitialColors().size() - 1);
		this.colorsPanelList.remove(this.colorsPanelList.size() - 1);		

		this.colorsPanel.removeAll();
		
		for(JPanel p : this.colorsPanelList)
			this.colorsPanel.add(p);
		this.colorsPanel.setVisible(false);
		this.colorsPanel.setVisible(true);
		
		this.addColorButton.setEnabled(true);
		if (this.colorsPanelList.size() < 1)
			this.delColorButton.setEnabled(false);
	}
}
