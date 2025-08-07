package com.github.matschieu.maths;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
*@author Matschieu
*/
public class MatrixPanel extends JPanel {

	private static final int TEXT_FIELD_SIZE = 5;

	private JTextField[][] matrixValues;
	private JTextField[] vectorValues;
	private JTextField[] resultValues;

	public MatrixPanel(LinearSystem system) throws NullMatrixException, NullVectorException {
		if (system.getMatrix() == null) throw new NullMatrixException();
		else if (system.getVector() == null) throw new NullVectorException();
		init(system.getMatrix(), system.getVector());
	}

	private void init(SquaredMatrix mat, Vector vect) {
		setLayout(new GridLayout(1, 1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(mat.getSize(), mat.getSize() + 4));
		matrixValues = new JTextField[mat.getSize()][mat.getSize()];
		vectorValues = new JTextField[vect.getSize()];
		resultValues = new JTextField[vect.getSize()];
		for(int i = 0; i < mat.getSize(); i++) {
			for(int j = 0; j < mat.getSize(); j++) {
				matrixValues[i][j] = new JTextField("" + mat.get(i, j), TEXT_FIELD_SIZE);
				matrixValues[i][j].setEditable(false);
				matrixValues[i][j].setBackground(new Color(255, 255, 255));
				panel.add(matrixValues[i][j]);
			}
			resultValues[i] = new JTextField("x" + i, TEXT_FIELD_SIZE);
			vectorValues[i] = new JTextField("" + vect.get(i), TEXT_FIELD_SIZE);
			resultValues[i].setEditable(false);
			resultValues[i].setBackground(new Color(255, 255, 255));
			vectorValues[i].setEditable(false);
			vectorValues[i].setBackground(new Color(255, 255, 255));
			if (i == mat.getSize() / 2) panel.add(new JLabel("        x"));
			else panel.add(new JLabel(" "));
			panel.add(resultValues[i]);
			if (i == mat.getSize() / 2) panel.add(new JLabel("        ="));
			else panel.add(new JLabel(" "));
			panel.add(vectorValues[i]);
		}
		JScrollPane scrollPane = new JScrollPane(panel);
		add(scrollPane);
	}

	public void initResult() { for(int i = 0; i < resultValues.length; i++) resultValues[i].setText("x" + i); }
	public void initVector() { for(int i = 0; i < vectorValues.length; i++) vectorValues[i].setText(""); }
	public void displayMatrix() {
		for(int i = 0; i < matrixValues.length; i++)
			for(int j = 0; j < matrixValues.length; j++) matrixValues[i][j].setText("");
	}

	public int displayResult(LinearSystem system) {
		Vector res = system.getRes();
		int cpt = 0;
		for(int i = 0; i < res.getSize(); i++)
			if (res.get(i) == Matrix.INF) {
				resultValues[i].setText("0");
				cpt++;
			}
			else resultValues[i].setText("" + res.get(i));
		return cpt;
	}
	public void displayVector(LinearSystem system) {
		Vector vect = system.getVector();
		for(int i = 0; i < vect.getSize(); i++) vectorValues[i].setText("" + vect.get(i));
	}
	public void displayMatrix(LinearSystem system) {
		SquaredMatrix mat = system.getMatrix();
		for(int i = 0; i < mat.getSize(); i++)
			for(int j = 0; j < mat.getSize(); j++) matrixValues[i][j].setText("" + mat.get(i, j));
	}

}
