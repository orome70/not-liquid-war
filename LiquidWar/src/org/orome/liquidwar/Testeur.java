package org.orome.liquidwar;

/*
This set of programs is an attempt to produce an Android clone of Liquid War 6.
Copyright (C)  2005, 2006, 2007, 2008, 2009, 2010, 2011  Christian Mauduit <ufoot@ufoot.org>
Liquid War 6 homepage : http://www.gnu.org/software/liquidwar6/

The present project is currently *not* connected to the original author of Liquid War.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Author: Orome orome@orome.org

*/


import java.util.List;
import java.util.ArrayList;

public class Testeur {

	/**
	 * 
	 * Cette classe a pour but de réaliser le test des principes algorithmiques derrières liquidwar.
	 * 
	 * @param args
	 */
	
	//static final int sizeX=2000,sizeY=2000;
	static final int sizeX=500,sizeY=500;
	
	class Coords {
		int coX, coY;
		public Coords(int coX, int coY){
			this.coX=coX;
			this.coY=coY;
		}
	}
	
	boolean [][] matrix; /* définit les points déjà calculés : false := à calculer */
	int [][] distances; /* contient les distances de chacun des points de la grille */
	Coords orig ; /* coordonnées de la position initiale : distances[orig.coX][orig.coY]=0 */
	List<Coords> current, next;
	/*
	 * current contient les points dont la distance a été calculée, dont les successeurs vont être calculés (étape "n")
	 * 
	 * next contient les éléments dont la distance est en train d'être calculés mais pour l'étape suivante (étape "n+1")
	 * 
	 */
	int curDist; /* distance courante (étape "n")*/
	
	public Testeur(int x, int y){
		orig =new Coords(x+1,y+1); /* Dans la matrice tout est décalé de 1 */
		this.reset();
	}
	public Testeur(){
		this(0,0);
	}
	
	public void reset(){
		matrix = new boolean[sizeX+2][sizeY+2];
		/* Pour des questions de simplicité, la matrice est plongée dans une plus grande les ligne/colonne 0 et size+1 sont positionnées à true */
		for(int i =0; i<sizeX; i++)matrix[i][0]=matrix[i][sizeY+1]=true;
		for(int i =0; i<sizeY; i++)matrix[0][i]=matrix[sizeX+1][i]=true;
		//System.out.println("Matrix : "+matrix[0][0]);
		distances = new int[sizeX+2][sizeY+2];
		/* Les valeurs des ligne/colonne 0 et size+1 sont sans importance */
		//System.out.println("Matrix : "+distances[0][0]);
		matrix[orig.coX][orig.coY]=true;
		distances[orig.coX][orig.coY]=0;
		current = new ArrayList<Coords>();
		current.add(orig);
		curDist=0; 
	}
	/*
	 * Cette fonction réalise la mise à jour de la matrice distances
	 * 
	 * en partant du principe que current contient la position initiale, 
	 * et que les deux matrices sont convenablement initialisées.  
	 * 
	 */
	public void update(){
		/*
		 * De façon provisoire, on va supposer qu'on va calcuer la distance 
		 * pour *tous* les points de l'image, donc sizeX*sizey 
		 */
		final int totalCalc = sizeX*sizeY;
		int courCalc=1, i, j; /* On a déjà calculé la distance de l'origine */
		while (courCalc < totalCalc){
			/* boucle principale, on veut calculer tous les points on suppose "current calculés" */
			curDist++;
			next = new ArrayList<Coords>();
			for(Coords cur : current){
				/* Traitement de l'élément courant : on donne "curDist" à tous ses voisins non calculés */
				i=-1;
				while (i<2){
					j=-1;
					while (j<2){
						if (! matrix[cur.coX+i][cur.coY+j]){/* Pas encore calculé */
							matrix[cur.coX+i][cur.coY+j]=true;
							distances[cur.coX+i][cur.coY+j]=curDist;
							next.add(new Coords(cur.coX+i,cur.coY+j));
							courCalc++;
						}/* else : rien à faire */
						j++;
					}
					i++;
				}
			}
			/* next -> current */
			current = next;
		}
		/*
		 * Théoriquement tout les points de la grille ont une valeur à jour.
		 */
	}
	
	/*
	 * 
	 * La méthode simulation réalise n mise à jour + reset puis fournit comme résultat 
	 * le nombre de secondes écoulées.
	 * 
	 */
	public double simulation(int nbIter){
		long timeNow = System.currentTimeMillis();
		for(int k=0;k<nbIter;k++){
			this.update();
			this.reset();
		}
		long timeNow2 = System.currentTimeMillis();
		return (timeNow2-timeNow)/1000.0;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Testeur tt = new Testeur();
		System.out.println("Début \n");
		long timeNow = System.currentTimeMillis();
		for(int k=0;k<100;k++){
			tt.update();
			tt.reset();
		}
		System.out.println("Fin \n");
		long timeNow2 = System.currentTimeMillis();
		System.out.println("Temps total : "+((timeNow2-timeNow)/1000.0)+" secondes\n");
	}

}
