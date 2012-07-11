package org.orome.liquidwar;

public class TesteurOptim {

	/**
	 * 
	 * Cette classe a pour but de réaliser le test des principes algorithmiques derrières liquidwar.
	 * 
	 * @param args
	 */
	
	//static final int sizeX=2000,sizeY=2000, nbPoints=10000;
	static final int sizeX=500,sizeY=500, nbPoints=10000;
	
	boolean [][] matrix; /* définit les points déjà calculés : false := à calculer */
	int [][] distances; /* contient les distances de chacun des points de la grille */
	int origX,origY ; /* coordonnées de la position initiale : distances[origX][origY]=0 */
	//List<Coords> current, next;
	int [][] current, next, tempo; // Plutôt qu'une liste de coords, un tableau, ligne 0 -> x, ligne 1 -> y
	// Par convention, on posera -1, -1 dans la dernière case utilisée
	/*
	 * current contient les points dont la distance a été calculée, dont les successeurs vont être calculés (étape "n")
	 * 
	 * next contient les éléments dont la distance est en train d'être calculés mais pour l'étape suivante (étape "n+1")
	 * 
	 */
	int curDist; /* distance courante (étape "n")*/
	int totalCalc; /* Nombre total de points à calculer */

	public TesteurOptim(int x, int y){
		origX= x+1; origY=y+1; /* Dans la matrice tout est décalé de 1 */
		this.reset();
	}
	public TesteurOptim(){
		this(0,0);
	}
	
	public void reset(){
		totalCalc = sizeX*sizeY;
		matrix = new boolean[sizeX+2][sizeY+2];
		/* Pour des questions de simplicité, la matrice est plongée dans une plus grande les ligne/colonne 0 et size+1 sont positionnées à true */
		for(int i =0; i<sizeX+1; i++)matrix[i][0]=matrix[i][sizeY+1]=true;
		for(int i =0; i<sizeY+1; i++)matrix[0][i]=matrix[sizeX+1][i]=true;
		//System.out.println("Matrix : "+matrix[0][0]);
		distances = new int[sizeX+2][sizeY+2];
		/* Les valeurs des ligne/colonne 0 et size+1 sont sans importance */
		//System.out.println("Matrix : "+distances[0][0]);
		matrix[origX][origY]=true;
		distances[origX][origY]=0;
		current = new int[nbPoints][2];
		current[0][0]=origX;
		current[0][1]=origY;
		for(int ll=0; ll<2; ll++)current[1][ll]=-1;
		//current.add(orig);
		curDist=0; 
		//Construction de next, seulement dans le constructeur
		next = new int[nbPoints][2];
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
	
	public void display (int x1, int y1, int x2, int y2){
		if (x1<0) x1=0;
		if (y1<0) y1=0;
		if (x2>(sizeX+2)) x2=sizeX+2;
		if (y2>(sizeY+2)) y2=sizeY+2;
		for(int xx=x1; xx<x2; xx++){
			for(int yy=y1; yy<y2; yy++){
				System.out.print(distances[xx][yy]+" ");
			}			
			System.out.println();
		}
		System.out.println();
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
		 * Pour être utilisé dans une console
		 * 
		 */
		TesteurOptim tt = new TesteurOptim(5,5);
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
		tt.display(0, 0,10,10);
		tt.display(5,5,15,15);
		//tt.display(10, 10,20,20);
		//tt.display(310, 310,320,320);
		tt.display(495,495, 510,510);
		
	}

}
