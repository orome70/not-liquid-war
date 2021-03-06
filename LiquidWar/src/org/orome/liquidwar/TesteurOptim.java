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

public class TesteurOptim {

	/**
	 * 
	 * Cette classe a pour but de réaliser le test des principes algorithmiques derrières liquidwar.
	 * 
	 * @param args
	 */
	
	//static final int sizeX=2000,sizeY=2000, maxPoints=10000;
	static final int sizeX=500,sizeY=500, maxPoints=10000;
	
	boolean [][] matrix, matFighters; 
	/* 
	 * matrix: coordinates which have already been calculated: *false* -> to compute
	 * matFighers: positions where fighters are: *true* -> there is a fighter   
	 * 
	 * */
	int [][] distances; 
	/* 
	 * distances: length of the shortest path from a given position to the target
	 *  
	 * */
	int origX,origY ; 
	/*
	 * Coordinates of the target
	 * In particular: distances[origX][origY]=0
	 *  
	 */
	int [][] current, next, tempo, fighters;
	/*
	 * Each of these are are int [maxPoint][2] arrays
	 * current: contains the coordinates which have been updated in distances
	 * next: contains the coordinates which will be computed on the next "turn"
	 * tempo: enable to switch between current and next :-P
	 * fighters: contains the coordinates of encountered fighters
	 * 
	 * line 0 -> x
	 * line 1 -> y
	 * After the last value array[last][0]=array[last][1]=-1
	 */
	int curDist, totalCalc, totalFighters; 
	/* 
	 * curDist: Current length (step "n")
	 * totalCalc: number of points in the map
	 * totalFighters: number of fighters in the map 
	 * 
	 * */


	public TesteurOptim(int x, int y,int fighters){
		origX= x+1; origY=y+1; 
		/* 
		 * In each matrix everything is shifted by 1
		 * 
		 * */
		this.reset();
		this.totalFighters=fighters;
		this.addFighters(fighters);
		
	}
	public TesteurOptim(int fighters){
		this(0,0,fighters);
	}
	public TesteurOptim(){
		this(10);
	}
	
	public void reset(){
		totalCalc = sizeX*sizeY;
		matrix = new boolean[sizeX+2][sizeY+2];
		/*
		 * In order to simplify computations (arround the borders) each matrix is embedded into a 
		 * larger one. Lines/row at 0 and size+1. In matrix they all have value *true*. 
		 */
		for(int i =0; i<sizeX+1; i++)matrix[i][0]=matrix[i][sizeY+1]=true;
		for(int i =0; i<sizeY+1; i++)matrix[0][i]=matrix[sizeX+1][i]=true;
		distances = new int[sizeX+2][sizeY+2];
		this.matFighters = new boolean[sizeX+2][sizeY+2];
		/* For these two matrices, values in line/row 0 and size+1 are not relevant */
		matrix[origX][origY]=true;
		distances[origX][origY]=0;
		current = new int[maxPoints][2];
		current[0][0]=origX;
		current[0][1]=origY;
		for(int ll=0; ll<2; ll++)current[1][ll]=-1;
		//current.add(orig);
		curDist=0; 
		//Construction de next, seulement dans le constructeur
		next = new int[maxPoints][2];
	}
	public void update(){
		/*
		 * This function updates/computes the matrix *distances*
		 * and should perform the movement of the fighters.
		 *
		 * It assumes that origX and origY contains the position of the target
		 * And that matrix is *false* everywhere computation is needed.
		 * 
		 * At the moment we assume that computation should be performed at all 
		 * position of the map (totalCalc).
		 */
		int courCalc=1, i, j; /* On a déjà calculé la distance de l'origine */
		int indexCur,indexNext, curX, curY;
		while (courCalc < totalCalc){
			/* boucle principale, on veut calculer tous les points on suppose "current calculés" */
			curDist++;
			// Next : vide
			next[0][0]=next[0][1]=-1;
			indexCur=0;indexNext=0;
			while(current[indexCur][0]!=-1){
				/* Traitement de l'élément courant : on donne "curDist" à tous ses voisins non calculés */
				curX=current[indexCur][0];
				curY=current[indexCur][1];
				i=-1;
				while (i<2){
					j=-1;
					while (j<2){
						if (!matrix[curX+i][curY+j]){/* Pas encore calculé */
							matrix[curX+i][curY+j]=true;
							distances[curX+i][curY+j]=curDist;
							// Ajout dans next
							next[indexNext][0]=curX+i;							
							next[indexNext][1]=curY+j;
							indexNext++;
							courCalc++;
						}/* else : rien à faire */
						j++;
					}
					i++;
				}
				indexCur++;
			}
			// Marqueur de fin dans next
			for(int ll=0; ll<2; ll++)next[indexNext][ll]=-1;
			/* next -> current (on se contente de permuter les tableaux */
			tempo=current;
			current = next;
			next = tempo;
		}
		/*
		 * Théoriquement tout les points de la grille ont une valeur à jour.
		 */
	}
	
	public void obstacle (int x1, int y1, int x2, int y2){
		/*
		 * Construction d'un obstacle carré
		 * 
		 * Naturellement on peut l'utiliser plusieurs fois pour produire n'importe
		 * quel forme d'obstacle.
		 */
		if (x1<1) x1=1;
		if (y1<1) y1=1;
		if (x2>(sizeX+1)) x2=sizeX+1;
		if (y2>(sizeY+1)) y2=sizeY+1;
		for(int xx=x1; xx<x2; xx++){
			for(int yy=y1; yy<y2; yy++){
				if (!matrix[xx][yy]){
					matrix[xx][yy]=true;	
					this.totalCalc--;	
				}	
			}			
		}
	}

	public void addFighters(int fight){
		/*
		 * We add fighters on the matFighters (one by one)
		 * 
		 * Then each time a position is added in "next", whenever 
		 * it contains a fighter is will be added to fighters
		 * 
		 * Currently we add fighters from (250,250) one (half-)line at a time
		 */
		
		int k=250;
		for(int i=k; i<sizeX && fight>0; i++){
			for(int j=k; j<sizeY&&fight>0; j++){
				this.matFighters[i][j]=true;
				fight--;
			}
		}
	}
	private void moveFighter(int coX, int coY){
		/*
		 * 
		 * Computes the move of matFighters [coX][coY]
		 * 
		 * If there is an empty spot "at same distance" it moves
		 * (last empty spot)
		 * If there is an empty spot "at smaller distance" it moves 
		 * with higher priority (first fit)
		 * 
		 */
		int i,j,movX=-1,movY=-1,distOrig=this.distances[coX][coY];
		i=-1;
		while (i<2){
			j=-1;
			while (j<2){
				if (matrix[coX+i][coY+j] && !this.matFighters[coX+i][coY+j] && (this.distances[coX+i][coY+j]<=distOrig))
				{
					/* computed && empty && dist <= */
					movX=coX+i; movY=coY+j;
					if (this.distances[movX][movY]<distOrig)i=j=2;
					/* leave the loops as soon as smaller distance is found*/
				}
				/* no else */
				if (i==0)j++;/* To avoid i==j==0 */
				j++;
			}
			i++;
		}
		if (movX!=-1){
			/* There is an update */
			this.matFighters[movX][movY]=true;
			this.matFighters[coX][coY]=false;
		}

	}
	public String display (int x1, int y1, int x2, int y2){
		String disRes="";
		if (x1<0) x1=0;
		if (y1<0) y1=0;
		if (x2>(sizeX+2)) x2=sizeX+2;
		if (y2>(sizeY+2)) y2=sizeY+2;
		for(int xx=x1; xx<x2; xx++){
			for(int yy=y1; yy<y2; yy++){
				if (this.matFighters[xx][yy])
					disRes+="*"+" ";
				else disRes+=distances[xx][yy]+" ";
			}			
			disRes+="\n";
		}
		disRes+="\n";
		return disRes;
	}
	/*
	 * 
	 * La méthode simulation réalise n mise à jour + reset puis fournit comme résultat 
	 * le nombre de secondes écoulées.
	 * 
	 */
	public double simulation(int nbIter){
		long timeNow = System.currentTimeMillis();
		for(int k=0;k<10;k++){
			this.obstacle(8, 8, 10, 10);
			this.update();
			this.reset();
		}
		long timeNow2 = System.currentTimeMillis();
		return (timeNow2-timeNow)/1000.0;
	}

	public static void main(String[] args) {
		/*
		 * Might be used in console mode.
		 * 
		 */
		TesteurOptim tt = new TesteurOptim(5,5,0);
		System.out.println("Début \n");
		long timeNow = System.currentTimeMillis();
		for(int k=0;k<100;k++){
			tt.obstacle(8, 8, 10, 10);
			tt.update();
			tt.reset();
		}
		long timeNow2 = System.currentTimeMillis();
		System.out.println("Fin \n");
		tt.obstacle(8, 8, 10, 10);
		tt.update();
		System.out.println("Temps total : "+((timeNow2-timeNow)/1000.0)+" secondes\n");
		System.out.print(tt.display(0, 0,10,10));
		System.out.print(tt.display(5,5,15,15));
		//tt.display(10, 10,20,20);
		//tt.display(310, 310,320,320);
		tt.display(495,495, 510,510);
		
	}

}
