import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Case
{	
	private boolean bomb;
	private boolean clicked;

	private int nbBomb;

	private int posY, posX;
	private char affichage; // ' ' = initial | num = nbBombAutour | D = drapeau | B = bombe

	private JButton btn;

	private Demineur dem;
	private Fenetre  fen;

	public Case(int y, int x, Demineur dem, Fenetre fen)
	{
		this.bomb = false;
		this.clicked = false;

		this.posY = y;
		this.posX = x;

		this.affichage = ' ';

		this.btn = new JButton();
		this.btn.setOpaque(true);
        this.btn.setBorderPainted(true);

        this.dem = dem;
        this.fen = fen;

        this.nbBomb = 0;
	}

	public char getAffichage()			{ return this.affichage;	}
	public boolean getBomb() 			{ return this.bomb;			}
	public void setBomb()				{ this.bomb = true;			}
	public void setNbBomb(int nbBomb)	{ this.nbBomb = nbBomb;		}
	public void setClicked()			{ this.clicked = true;		}
	public boolean getClicked()			{ return this.clicked;		}
	public void setAffichage(char c )	{ this.affichage = c; 		}
	public JButton getBtn() 			{ return this.btn;			}

	//-----Méthode setDrapeau()
	//Place un drapeau s'il n'y en a pas, retire le drapeau s'il y en a un.
	public void setDrapeau()
	{

		if(this.getAffichage() != 'D')
		{
			this.btn.setOpaque(true);
			this.btn.setBackground(Color.green);
			this.setAffichage('D');
			fen.moreDrap();
		}
		else
		{
			this.btn.setOpaque(true);
			this.btn.setBackground(Color.gray);
			fen.lessDrap();
			if(this.getBomb())
				this.setAffichage('B');
			else
				this.setAffichage((char)(this.nbBomb+'0'));
		}
	}

	//-----Méthode verifBomb(Case[][])
	//Verifie si le nombre de bombes autour de la case permet de jouer.
	public boolean verifBomb(Case[][] tabCase)
	{
		if(this.posY == 0 || this.posY == dem.getNbLig()-1)
			if(this.posX == 0 || this.posX == dem.getNbCol()-1)
				if (this.calculNbAutour(tabCase, 'B') >= 4)
					return false;
			else
				if(this.calculNbAutour(tabCase, 'B') >= 6)
					return false;
		else
			if(this.posX == 0 || this.posX == dem.getNbCol()-1)
				if (this.calculNbAutour(tabCase, 'B') >= 6)
					return false;
			else
				if(this.calculNbAutour(tabCase, 'B') >= 9)
					return false;

		return true;
	}

	
	//-----Méthode calculNbAutour(Case[][], char)
	//Verifie le nb d'occurence du caractere c autour de la case.	
	public int calculNbAutour(Case[][] tabCase, char c)
	{
		int nbOccur = 0;
		//Pour les cases du milieu
		if(	(this.posY > 0 && this.posY < dem.getNbLig()-1) &&
			(this.posX > 0 && this.posX < dem.getNbCol()-1))
		{
			for (int i=-1;i<2;i++) 
			{
				for (int j=-1;j<2;j++) 
				{	
					if(tabCase[posY+i][posX+j].getAffichage() == c)
						nbOccur ++;

				}	
			}
		}

		//Pour les cases du coté gauche
		if((this.posY > 0 && this.posY < dem.getNbLig()-1 )&&
			this.posX == 0 )
		{
			for (int i=-1;i<2;i++) 
			{
				for (int j=0;j<2;j++) 
				{	
					if(tabCase[posY+i][posX+j].getAffichage() == c)
						nbOccur ++;

				}	
			}
		}

		//Pour les cases du coté droit
		if((this.posY > 0 && this.posY < dem.getNbLig()-1 )&&
			this.posX == dem.getNbCol()-1 )
		{
			for (int i=-1;i<2;i++) 
			{
				for (int j=-1;j<1;j++) 
				{	
					if(tabCase[posY+i][posX+j].getAffichage() == c)
						nbOccur ++;

				}	
			}
		}

		//Pour les cases du haut
		if(this.posY == 0 &&
		  (this.posX > 0 && this.posX < dem.getNbCol()-1 ))
		{
			for (int i=0;i<2;i++) 
			{
				for (int j=-1;j<2;j++) 
				{	
					if(tabCase[posY+i][posX+j].getAffichage() == c)
						nbOccur ++;

				}	
			}
		}

		//Pour les cases du bas
		if(this.posY == dem.getNbLig()-1 &&
		  (this.posX > 0 && this.posX < dem.getNbCol()-1 ))
		{
			for (int i=-1;i<1;i++) 
			{
				for (int j=-1;j<2;j++) 
				{	
					if(tabCase[posY+i][posX+j].getAffichage() == c)
						nbOccur ++;

				}	
			}
		}
		//Pour le coin sup gauche
		if(this.posY == 0 && this.posX == 0)
		{
			if(tabCase[0][1].getAffichage() == c)
				nbOccur ++;
			if(tabCase[1][1].getAffichage() == c)
				nbOccur ++;
			if(tabCase[1][0].getAffichage() == c)
				nbOccur ++;
		}
		//Pour le coin inf gauche
		if(this.posY == dem.getNbLig()-1 && this.posX == 0)
		{
			if(tabCase[dem.getNbLig()-2][0].getAffichage() == c)
				nbOccur ++;
			if(tabCase[dem.getNbLig()-2][1].getAffichage() == c)
				nbOccur ++;
			if(tabCase[dem.getNbLig()-1][0].getAffichage() == c)
				nbOccur ++;
		}
		//Pour le coin sup droit
		if(this.posY == 0 && this.posX == dem.getNbCol()-1)
		{
			if(tabCase[0][ dem.getNbCol()-2].getAffichage() == c)
				nbOccur ++;
			if(tabCase[1][ dem.getNbCol()-1].getAffichage() == c)
				nbOccur ++;
			if(tabCase[1][ dem.getNbCol()-2].getAffichage() == c)
				nbOccur ++;
		}
		//Pour le coin inf droit
		if(this.posY == dem.getNbLig()-1  && this.posX == dem.getNbCol()-1)
		{
			if(tabCase[dem.getNbLig()-2][ dem.getNbCol()-2].getAffichage() == c)
				nbOccur ++;
			if(tabCase[dem.getNbLig()-2][ dem.getNbCol()-1].getAffichage() == c)
				nbOccur ++;
			if(tabCase[dem.getNbLig()-1][ dem.getNbCol()-2].getAffichage() == c)
				nbOccur ++;
		}
		if(c == 'B')
			this.setNbBomb(nbOccur);

		return nbOccur;
	}
}