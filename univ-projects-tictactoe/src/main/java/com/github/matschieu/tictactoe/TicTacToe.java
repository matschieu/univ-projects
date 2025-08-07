package com.github.matschieu.tictactoe;

import javax.swing.*;

public class TicTacToe {

	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private GridTerm grid;
	private CustomGrid ggrid;

	public TicTacToe(boolean robot) {
		//System.out.println("\n * TIC TAC TOE *\n");
		this.player1 = new GraphicPlayer();
		if (robot) this.player2 = new RobotPlayer();
		else this.player2 = new GraphicPlayer();
		this.currentPlayer = player1;
		this.grid = new GridTerm();
		this.ggrid = new CustomGrid(grid, "* TIC TAC TOE *");
		player1.setPawn(Pawn.CROSS);
		player2.setPawn(Pawn.CIRCLE);
	}

	public void start() {
		Coordinate coord = null;
		//System.out.println("\n" + grid.toString());
		while(!gameIsFinished()) {
			if(currentPlayer instanceof GraphicPlayer) ggrid.setCurrentPlayer((GraphicPlayer)currentPlayer);
			coord = currentPlayer.play();
			try { 
				grid.coordinateVerify(coord);
				if (grid.isEmpty(coord)) {
					grid.setCase(coord, currentPlayer.getPawn());
					ggrid.repaint();
					if (currentPlayer == player1) currentPlayer = player2;
					else currentPlayer = player1;
					//System.out.println("\n" + grid.toString());
				}
				//else System.err.println(term.RED + "\nErreur : case deja jouee\n" + term.DEFAULT_COLOR);
			}
			//catch(Exception e) { System.err.println(term.RED + "\n" + e.toString() + term.DEFAULT_COLOR); }
			catch(Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
		}
		if (gridIsTotallyFilled() && !alignmentFound()) {
			//System.out.println("Match nul...\n");
			JOptionPane.showMessageDialog(null, "Match nul...");
		}
		else {
			Player winner = getWinner();
			if (winner == player1) player1.setTTTClassement();
			else player2.setTTTClassement();
			//System.out.println(winner.getName() + " a gagne !\n");
			JOptionPane.showMessageDialog(null, winner.getName() + " a gagne !");
		}
		player1.setNbPart();
		player2.setNbPart();
		//System.out.println(player1.getName() + " : " + player1.getTTTClassement() + " (" + player1.getNbPart() + " parties) - " + player2.getName() + " : " + player2.getTTTClassement() + " (" + player2.getNbPart() + " parties)\n");
		JOptionPane.showMessageDialog(null, player1.getName() + " : " + player1.getTTTClassement() + " (" + player1.getNbPart() + " parties) - " + player2.getName() + " : " + player2.getTTTClassement() + " (" + player2.getNbPart() + " parties)");
		if (gameContinue()) {
			grid = new GridTerm();
			ggrid.setGridTerm(grid);
			ggrid.repaint();
			start();
		}
		//else System.out.println("\nA bientot :)\n");
		else JOptionPane.showMessageDialog(null, "A bientot :)");
	}

	public boolean gameContinue() {
		int choice = -1;
		while (choice != 1 && choice != 0)
			try {
				//System.out.print("Continuer (1=oui, 0=non) ? ");
				//choice = (new InputTerm()).inputInt();
				if (JOptionPane.showConfirmDialog(null, "Continuer ?", "", JOptionPane.YES_NO_OPTION) == 1) choice = 0;
				else choice = 1;
			}
			//catch (Exception e) { System.err.println(term.RED + e.toString() + "\n" + term.DEFAULT_COLOR); }
			catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
		return choice == 1;
	}

	public Player getWinner() {
		if (currentPlayer == player1) return player2;
		return player1;
	}
	
	protected boolean gridIsTotallyFilled() {
		for(int i = 0; i < grid.getRows(); i++)
			for(int j = 0; j < grid.getCols(); j++)
				try { if (grid.getPawn(new Coordinate(j, i)) == Pawn.EMPTY_PAWN) return false; }
				catch(Exception e) { return false; }
		return true;
	}
	
	protected int valueForSymbole(Pawn pawn) {
		if (pawn == Pawn.CROSS) return +1;
		else if (pawn == Pawn.CIRCLE) return -1;
		return 0;
	}
	
	protected boolean gameIsFinished() { return gridIsTotallyFilled() || alignmentFound(); }
	
	protected boolean alignmentFound() {
		int sumRow = 0;
		int sumCol = 0;
		int sumDiag1 = 0;
		int sumDiag2 = 0;
		for(int i = 0; i < grid.getRows(); i++) {
			for(int j = 0; j < grid.getCols(); j++)
				try {
					sumRow += valueForSymbole(grid.getPawn(new Coordinate(j, i)));
					sumCol += valueForSymbole(grid.getPawn(new Coordinate(i, j)));
				}
				catch(Exception e) { return false; }
			if (sumRow == 3 || sumRow == -3) return true;
			if (sumCol == 3 || sumCol == -3) return true;
			sumCol = sumRow = 0;
		}
		for(int i = 0; i < grid.getRows(); i++) {
			try {
				sumDiag1 += valueForSymbole(grid.getPawn(new Coordinate(grid.getCols() - 1 - i, i)));
				sumDiag2 += valueForSymbole(grid.getPawn(new Coordinate(i, i)));
			}
			catch(Exception e) { return false; }
		}
		if (sumDiag1 == 3 || sumDiag1 == -3) return true;
		if (sumDiag2 == 3 || sumDiag2 == -3) return true;
		return false;
	}
	
}
