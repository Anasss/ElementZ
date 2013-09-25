/*
 * Copyright (c) 2009 - Ahcene Bounceur
 */
package org.elementz;

//--------------------------------------------------------------------------
// On appellera dans ce qui suit famille à n boules un groupe de n boules
// voisines ayant la même couleur
// Famille en ligne : famille qui se trouve sur la même ligne
// Famille en colonne : famille qui se trouve sur la même colonne
//--------------------------------------------------------------------------
// But du jeu :
// Il faut permuter deux boules voisines de telle sorte à former
// des familles en lignes et/ou en colonne à 3, 4 ou 5 boules.
//--------------------------------------------------------------------------

import java.util.Random;

public class ElementZ_Model {

	// Matrice du jeu.
	private int[][] matrix = new int[8][8];
	// Matrice des familles.
	private boolean[][] matrixb = new boolean[8][8];
	// Le score courant.
	private int score;
	
	// Générateur de valeurs aléatoires
	//  -> indiquer une valeur de graine pendant le développement
	//  -> supprimer la valeur de graine pour la production de la version finale
	private static Random x = new Random(4812);
	// private static Random x = new Random();


	public ElementZ_Model() {
		merge(); // Mélange les boules
		
/************************************************************/
// ATTENTION ! 
// Préparation du plateau à réactiver à la fin des tests unitaires !
				
//		prepar(); // Prépare le plateau sans qu'il y ait de familles
					// en ligne ou en colonne
/************************************************************/
		
		score = 0; // Initialise le score
	}

	public int getScore() {
		return score;
	}

	public void setXY(int x, int y, int value) {
		matrix[x][y] = value;
	}

	public int getXY(int x, int y) {
		return matrix[x][y];
	}

	private void initMatrixb() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				matrixb[i][j] = false;
			}
		}
	}

	// Détection des familles en ligne à n boules :
	// la valeur "true" est placée dans les cases correspondant
	// aux boules faisant partie d'une famille à n boules.
	public void detectLine(int n) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < (8 - n + 1); j++) {
				boolean lineOk = true;
				int first = matrix[i][j];
				int k = j;
				while (k < j + n && lineOk) {
					if (matrix[i][k] != first && matrix[i][k] != 0)
						lineOk = false;
					k++;
				}
				if (lineOk) {
					for (k = j; k < (j + n); k++)
						matrixb[i][k] = true;
					j += n + 1;
				}
			}
		}
	}

	// Détection des familles en colonne à n boules :
	public void detectCol(int n) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < (8 - n + 1); j++) {
				boolean lineOk = true;
				int first = matrix[j][i];
				int k = j;
				while (k < j + n && lineOk) {
					if (matrix[k][i] != first && matrix[k][i] != 0)
						lineOk = false;
					k++;
				}
				if (lineOk) {
					for (k = j; k < (j + n); k++)
						matrixb[k][i] = true;
					j += n + 1;
				}
			}
		}
	}

	// Détection des familles en ligne à 3, 4 ou 5 boules :
	public void detectLines() {
		for (int i = 5; i > 2; i--) {
			detectLine(i);
		}
	}

	// Détection des familles en colonne à 3, 4 ou 5 boules :
	public void detectCols() {
		for (int i = 5; i > 2; i--) {
			detectCol(i);
		}
	}

	// Détection des familles à 3, 4 ou 5 boules :
	public void detectLinesAndCols() {
		for (int i = 5; i > 2; i--) {
			detectLine(i);
			detectCol(i);
		}
	}

	// Suppression des familles en lignes à 3, 4 ou 5 boules :
	public void deleteTheSame() {
		// - Parcours de matrixb à la recherche des boules identifiées
		//   comme appartenant à une famille.
		// - Suppression des boules corrrespondantes (remise à zéro dans matrix)
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (matrixb[i][j])
					matrix[i][j] = 0;
			}
		}
		initMatrixb();
	}

	// déplacement des boules
	public void gravity() {
		// - Détection des trous dans matrix (valeurs 0)
		// - Décalage en colonne des boules situées au dessus des trous
		for (int j = 0; j < 8; j++) {
			int k = 7;
			for (int i = 7; i >= 0; i--) {
				if (matrix[i][j] != 0) {
					matrix[k][j] = matrix[i][j];
					k--;
				}
			}
			for (int i = k; i >= 0; i--) {
				matrix[i][j] = 0;
			}
		}
	}

	// remplissage des trous
	public void fillGaps() {
		// Utilisation du générateur de nombres aléatoires :
		// x.nextInt(6) + 1
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (matrix[i][j] == 0)
					matrix[i][j] = x.nextInt(6) + 1;
			}
		}
	}

	// suppression des familles créées par hasard lors du remplissage des trous
	public boolean notReadyToPlay() {
		detectLinesAndCols();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (matrixb[i][j])
					return true;
		return false;
	}

	// remise en état du plateau avant l'action du joueur
	public void prepar() {
		while (notReadyToPlay()) {
			score += somme();
			deleteTheSame();
			gravity();
			fillGaps();
		}
	}

	// permutation
	public void permut(int i, int j, int ip, int jp) {
		// Permute les boules de coordonnées (i,j) et (ip,jp)
		// SEULEMENT SI elles sont adjacentes !
		if (((i == ip) && (j == (jp + 1))) || ((i == ip) && (j == (jp - 1)))
				|| ((j == jp) && (i == (ip + 1)))
				|| ((j == jp) && (i == (ip - 1)))) {
			int s = matrix[i][j];
			matrix[i][j] = matrix[ip][jp];
			matrix[ip][jp] = s;
		}
	}

	// action du joueur
	public void play(int x, int y, int xp, int yp) {
		permut(x, y, xp, yp);
		detectLinesAndCols();
		if (matrixb[x][y] || matrixb[xp][yp])
			prepar();
		else
			permut(x, y, xp, yp);
	}

	// remplissage aléatoire
	public void merge() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				matrix[i][j] = x.nextInt(6) + 1;
			}
		}
	}

	// calcul de score
	public int somme() {
		int s = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				s += (matrixb[i][j] ? 1 : 0);
			}
		}
		return s;
	}

	public String toString() {
		// Fourni les codes couleur et l'indication des familles
		String s = "";
		String s1;
		String s2;
		for (int i = 0; i < 8; i++) {
			s1 = "";
			s2 = "";
			for (int j = 0; j < 8; j++) {
				s1 += matrix[i][j] + " ";
				s2 += (matrixb[i][j] ? 1 : 0) + " ";
			}
			s += s1 + "            " + s2 + '\n';
		}
		return s;
	}

	/******************************************************************************/
	
	public static void main(String[] argv) {
		// Fonction main contenant des jeux de tests pour les méthodes à compléter.
		// 	En commentaire : la trace d'exécution attendue.

		ElementZ_Model jeu = new ElementZ_Model();
		jeu.matrix[4][1] = 4;
		jeu.matrix[4][2] = 4;
		jeu.matrix[4][3] = 4;
		jeu.detectLinesAndCols();
		System.out.println(jeu); // au moins 1 séquence horizontale de 3 boules est détectée
		/*	3 1 5 3 3 1 6 6             0 0 0 0 0 0 0 0 
			1 2 3 3 3 4 3 3             0 0 1 1 1 0 0 0 
			4 3 3 6 4 6 5 5             0 0 0 0 0 0 1 1 
			2 1 2 6 2 1 5 5             0 0 0 0 0 0 1 1 
			5 4 4 4 6 2 5 5             0 1 1 1 0 0 1 1 
			5 3 2 5 6 4 5 5             0 0 0 0 0 0 1 1 
			3 1 4 2 2 6 1 4             0 0 0 0 0 0 0 0 
			2 3 5 6 2 3 3 5             0 0 0 0 0 0 0 0 
		*/
		
		jeu.deleteTheSame();
		System.out.println(jeu); // les séquences sont supprimées (présence de trous)
		/*	3 1 5 3 3 1 6 6             0 0 0 0 0 0 0 0 
			1 2 0 0 0 4 3 3             0 0 0 0 0 0 0 0 
			4 3 3 6 4 6 0 0             0 0 0 0 0 0 0 0 
			2 1 2 6 2 1 0 0             0 0 0 0 0 0 0 0 
			5 0 0 0 6 2 0 0             0 0 0 0 0 0 0 0 
			5 3 2 5 6 4 0 0             0 0 0 0 0 0 0 0 
			3 1 4 2 2 6 1 4             0 0 0 0 0 0 0 0 
			2 3 5 6 2 3 3 5             0 0 0 0 0 0 0 0 
		*/

		jeu.gravity();
		System.out.println(jeu); // les colonnes sont décalées (les trous sont en haut)
		/*	3 0 0 0 0 1 0 0             0 0 0 0 0 0 0 0 
			1 1 0 0 3 4 0 0             0 0 0 0 0 0 0 0 
			4 2 5 3 4 6 0 0             0 0 0 0 0 0 0 0 
			2 3 3 6 2 1 0 0             0 0 0 0 0 0 0 0 
			5 1 2 6 6 2 6 6             0 0 0 0 0 0 0 0 
			5 3 2 5 6 4 3 3             0 0 0 0 0 0 0 0 
			3 1 4 2 2 6 1 4             0 0 0 0 0 0 0 0 
			2 3 5 6 2 3 3 5             0 0 0 0 0 0 0 0 
		*/

		jeu.fillGaps();
		System.out.println(jeu); // les trous sont comblés
		/* 	3 3 5 6 4 1 5 4             0 0 0 0 0 0 0 0 
			1 1 2 2 3 4 1 4             0 0 0 0 0 0 0 0 
			4 2 5 3 4 6 1 1             0 0 0 0 0 0 0 0 
			2 3 3 6 2 1 1 3             0 0 0 0 0 0 0 0 
			5 1 2 6 6 2 6 6             0 0 0 0 0 0 0 0 
			5 3 2 5 6 4 3 3             0 0 0 0 0 0 0 0 
			3 1 4 2 2 6 1 4             0 0 0 0 0 0 0 0 
			2 3 5 6 2 3 3 5             0 0 0 0 0 0 0 0 
		*/
		
		jeu.permut(7, 6, 7, 7);
		System.out.println(jeu); // les 2 dernières boules sont permutées
		/*	3 3 5 6 4 1 5 4             0 0 0 0 0 0 0 0 
			1 1 2 2 3 4 1 4             0 0 0 0 0 0 0 0 
			4 2 5 3 4 6 1 1             0 0 0 0 0 0 0 0 
			2 3 3 6 2 1 1 3             0 0 0 0 0 0 0 0 
			5 1 2 6 6 2 6 6             0 0 0 0 0 0 0 0 
			5 3 2 5 6 4 3 3             0 0 0 0 0 0 0 0 
			3 1 4 2 2 6 1 4             0 0 0 0 0 0 0 0 
			2 3 5 6 2 3 5 3             0 0 0 0 0 0 0 0 
		*/
		
		jeu.permut(7, 4, 7, 7);
		System.out.println(jeu); // pas de changement
		/*	3 3 5 6 4 1 5 4             0 0 0 0 0 0 0 0 
			1 1 2 2 3 4 1 4             0 0 0 0 0 0 0 0 
			4 2 5 3 4 6 1 1             0 0 0 0 0 0 0 0 
			2 3 3 6 2 1 1 3             0 0 0 0 0 0 0 0 
			5 1 2 6 6 2 6 6             0 0 0 0 0 0 0 0 
			5 3 2 5 6 4 3 3             0 0 0 0 0 0 0 0 
			3 1 4 2 2 6 1 4             0 0 0 0 0 0 0 0 
			2 3 5 6 2 3 5 3             0 0 0 0 0 0 0 0 
		*/

	}

}
