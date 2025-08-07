package com.github.matschieu.color.selector.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.matschieu.color.selector.model.ColorList;
import com.github.matschieu.color.selector.model.MColor;

/**
 * @author Matschieu
 */
public class ColorCustomGeneratedPanel extends JPanel {

	public static final String SEPARATOR = "        ";

	private JComboBox comboBox;
	private JTextField[] rgbTextFields;
	private JTextField hexaTextField;
	private JPanel initialColorPanel;
	private JPanel colorSelectedPanel;
	private JPanel grayLevelPanel;

	public ColorCustomGeneratedPanel(MColor initColor, ColorList colors) {
		this.setAlignmentY(CENTER_ALIGNMENT);
		this.setAlignmentX(CENTER_ALIGNMENT);
		// Affiche un aperçu de la couleur initiale
		this.initialColorPanel = new JPanel();
		this.initialColorPanel.setBackground(initColor);
		this.initialColorPanel.setToolTipText("Couleur initiale");
		this.initialColorPanel.setPreferredSize(new Dimension(20, 20));
		this.add(this.initialColorPanel);
		// Permet de sélectionner une couleur parmi celles proposées
		this.comboBox = new JComboBox();
		this.comboBox.setRenderer(new ComboBoxColorRenderer());
		for(MColor c : colors) {
			comboBox.addItem(c);
			int ecart = 255;
			int tmpEcart = Math.abs(c.getRed() - initColor.getRed()) + Math.abs(c.getGreen() - initColor.getGreen()) +	Math.abs(c.getBlue() - initColor.getBlue());  
			if (ecart > tmpEcart) {
				ecart = tmpEcart;
				comboBox.setSelectedItem(c);
			}
		}
		this.add(comboBox);
		this.add(new JLabel(SEPARATOR));
		// Affiche les valeurs rgb de la couleur
		this.rgbTextFields = new JTextField[3];
		for(int i = 0; i < 3; i++) {
			this.add(new JLabel(i == 0 ? "R" : (i == 1 ? "V" : "B")));
			this.rgbTextFields[i] = new JTextField();
			this.rgbTextFields[i].setColumns(3);
			this.rgbTextFields[i].setEditable(false);
			this.rgbTextFields[i].setTransferHandler(new ColorTextFieldTransferHandler());
			this.rgbTextFields[i].setDragEnabled(true);
			this.rgbTextFields[i].addFocusListener(new ColorTextFieldFocus(this.rgbTextFields[i]));
			this.add(this.rgbTextFields[i]);
		}
		this.add(new JLabel(SEPARATOR));
		// Affiche la valeur hexa de la couleur
		this.add(new JLabel("HexaCode"));
		this.hexaTextField = new JTextField();
		this.hexaTextField.setColumns(6);
		this.hexaTextField.setEditable(false);
		this.hexaTextField.setTransferHandler(new ColorTextFieldTransferHandler());
		this.hexaTextField.setDragEnabled(true);
		this.hexaTextField.addFocusListener(new ColorTextFieldFocus(this.hexaTextField));
		this.add(this.hexaTextField);
		this.add(new JLabel(SEPARATOR));
		// Affiche un aperçu de la couleur sélectionnée dans la liste
		this.colorSelectedPanel = new JPanel();
		this.colorSelectedPanel.setToolTipText("Couleur générée");
		this.colorSelectedPanel.setPreferredSize(new Dimension(20, 20));
		this.add(this.colorSelectedPanel);
		// Affiche un aperçu du niveau de gris de la couleur sélectionnée dans la liste
		this.grayLevelPanel = new JPanel();
		this.grayLevelPanel.setPreferredSize(new Dimension(20, 20));
		this.grayLevelPanel.setToolTipText("Niveau de gris");
		this.add(this.grayLevelPanel);
		// Ajoute un listener qui met à jour les champs lorsqu'on sélectionne une couleur dans la liste 
		this.comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateColorCodes();
			}
		});
		// On met à jour l'affichage
		this.updateColorCodes();
	}

	private void updateColorCodes() {
		int textFieldValue = 0;
		MColor selectedColor = (MColor)comboBox.getSelectedItem();
		if (selectedColor == null)
			return;
		for(int i = 0; i < 3; i++) {
			switch (i) {
			case 0:
				textFieldValue = selectedColor.getRed();
				break;
			case 1:
				textFieldValue = selectedColor.getGreen();
				break;
			case 2:
				textFieldValue = selectedColor.getBlue();
				break;
			}
			this.rgbTextFields[i].setText("" + textFieldValue);
			this.rgbTextFields[i].setForeground(new MColor(i == 0 ? 128 : 0, i == 1 ? 128 : 0, i == 2 ? 128 : 0));
		}	
		this.hexaTextField.setText(selectedColor.getHexaString(false));
		this.colorSelectedPanel.setBackground(selectedColor);
		this.grayLevelPanel.setBackground(new MColor(selectedColor.getGrayLevel()));
	}

}
