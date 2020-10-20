import javax.swing.*;
import java.awt.*;

public class Demineur
{
	private int nbLig = 30;
	private int nbCol = 24;
	private int nbBom = 180;
	private boolean init = false;
	private Fenetre fen;

	private Case[][] tabCase;

	public Demineur(int lig, int col, int bom, Fenetre fen)
	{
		this.nbLig = lig;
		this.nbCol = col;
		this.nbBom = bom;

		this.tabCase = new Case[nbLig][nbCol];

		//Initialisation du tableau
		for (int i = 0;i<nbLig;i++) 
		{
			for (int j = 0;j<nbCol;j++ ) 
			{
				tabCase[i][j] = new Case(i,j, this, fen);		
			}	
		}
	}

	//Methode toString
	public static String toString(int nbLig, int nbCol, Case[][] tabCase)
	{
		String sRet = "";
		for (int i = 0;i<nbLig;i++) 
		{
			for (int j = 0;j<nbCol;j++ ) 
			{
				sRet += tabCase[i][j].getAffichage();	
			}
			sRet += "\n";	
		}
		return sRet;
	}

	//Méthode qui est appelee lors du premier clique
	public void firstClick(int nbLig, int nbCol, Case[][] tabCase, int ligCase, int colCase, JPanel[][] tabPanel)
	{
		init = true;
		for (int i = 0;i< this.nbBom;i++ ) 
		{
			int coordLig = (int) (Math.random()*nbLig);
			int coordCol = (int) (Math.random()*nbCol);

			Case casechose = tabCase[coordLig][coordCol];

			if(casechose.getAffichage() != 'B' &&
				(coordLig != ligCase   || (coordCol != colCase && coordCol != colCase-1 && coordCol != colCase+1)) &&
				(coordLig != ligCase-1 || (coordCol != colCase && coordCol != colCase-1 && coordCol != colCase+1)) &&
				(coordLig != ligCase+1 || (coordCol != colCase && coordCol != colCase-1 && coordCol != colCase+1)) &&
				casechose.verifBomb(tabCase))
			{
				casechose.setAffichage('B');
				casechose.setBomb();	
			}
			else
				i--;
		}
		for (int i = 0;i<nbLig;i++) 
		{
			for (int j = 0;j<nbCol;j++ ) 
			{
				if(tabCase[i][j].getAffichage() != 'B')
				{
					tabCase[i][j].setAffichage((char)(tabCase[i][j].calculNbAutour(tabCase, 'B')+'0'));
					if(tabCase[i][j].getAffichage() == '0')
						tabCase[i][j].setAffichage(' ');
				}
				System.out.print(tabCase[i][j].getAffichage());
			}
			System.out.println();	
		}		
	}

	public static void lose()
	{
		System.out.println("Vous avez perdu");
		
	}

	public boolean win(Case[][] tabCase)
	{
		for(int i = 0; i<this.nbLig; i++)
			for(int j = 0; j<this.nbCol; j++)
				if(tabCase[i][j].getBomb() && tabCase[i][j].getAffichage() != 'D')
					return false;

		return(init);
	}

	public  int getNbLig(){ return this.nbLig;	}
	public  int getNbCol(){ return this.nbCol;	}
	public  int getNbBom(){ return this.nbBom;	}
	public  Case[][] getTab(){  return this.tabCase;	}

	public static void main(String[] args)
	{
	}


}