import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Fenetre extends JFrame implements MouseListener
{

	private JPanel[][] tabP;
	private JPanel p;
	private JPanel panelCmd;

	private Case[][] tabCase;
	private boolean alreadyClicked = false;
	private int lig, col;

	private boolean lose = false;
	private boolean  win = false;

	private JButton restart = new JButton("Recommencer");
	private JButton difficulties = new JButton("Difficultés");

	private JLabel nbDrap;
	private static int drapPose = 0;

	private Demineur dem;
	private char diff;

	public Fenetre(int lig, int col, int bom, char d)
	{
		dem = new Demineur(lig, col, bom, this);
		Fenetre.drapPose =0;
		this.nbDrap = new JLabel("Drapeaux posés : "+ drapPose +"/"+bom);
		this.diff = d;
		this.tabCase = dem.getTab();
		this.lig = lig;
		this.col = col;

		this.setTitle("Démineur");
		this.setSize(1000,1000);
		this.setLocation(50,50);
		this.setBackground(Color.blue);
		this.tabP = new JPanel[lig][col];
		addMouseListener(this);
		p = new JPanel(new GridLayout(lig, col));

		restart.addMouseListener(this);
		difficulties.addMouseListener(this);
		panelCmd = new JPanel();
		panelCmd.add(restart);
		panelCmd.add(difficulties);
		panelCmd.add(nbDrap);

		//Creation de panel
		for (int i=0;i<lig;i++) 
		{
			for (int j=0;j<col;j++) 
			{
				this.tabP[i][j] = new JPanel(new BorderLayout());
				this.tabP[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				this.p.add(tabP[i][j]);
				this.tabP[i][j].addMouseListener(this);		
			}	
		}

		//Ajout des boutons
		for (int i=0;i<lig;i++) 
		{
			for (int j=0;j<col;j++) 
			{
				this.tabP[i][j].add(tabCase[i][j].getBtn());
				this.tabP[i][j].setName(""+i+" "+j);
				tabCase[i][j].getBtn().setName(""+i+" "+j);	
				tabCase[i][j].getBtn().setBorderPainted(false);
				tabCase[i][j].getBtn().setBackground(Color.gray);
				tabCase[i][j].getBtn().addMouseListener(this);	
			}	
		}

		this.add(p);
		this.add(panelCmd, BorderLayout.EAST);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(true);
		this.setVisible(true);
	}

	public void mousePressed(MouseEvent e)
	{
		char type =  ' ';
		JButton btn = new JButton();
		JPanel panel = new JPanel();

		try{
			btn = (JButton) e.getSource();
			if(btn.getName() != null)
				type = 'b';
			else
				type = 'e';
		}
		catch(Exception ex){}

		try{
			panel = (JPanel) e.getSource();
			type = 'p';
		}
		catch(Exception ex){}

		if(type == 'b')
		{
			//Recupération des coordonnées du boutons selectionnés.
			String btnName = btn.getName();
			String[] coord = btnName.split(" ");
			int posY = Integer.parseInt(coord[0]);
			int posX = Integer.parseInt(coord[1]);

			//SI l'utilisateur clique gauche sur un bouton
			if(e.getModifiers() == 16 && this.tabCase[posY][posX].getAffichage() != 'D' && !lose)
			{
				if(! this.alreadyClicked)
				{
					this.alreadyClicked = true;
					dem.firstClick(lig, col, tabCase, posY, posX, this.tabP);
				}

				if(this.tabCase[posY][posX].getAffichage() == 'B')
					this.lose();
				else
				{
					this.removeBtn(this.tabP[posY][posX],this.tabCase[posY][posX], posY, posX);
				} 

				if(dem.win(tabCase))
					this.win();
			}

			//Si l'utilisateur clique droit sur un bouton
			if(e.getModifiers() == 4 && !lose )
			{
				this.tabCase[posY][posX].setDrapeau();
				if(dem.win(tabCase))
					this.win();
			}
			this.validate();
		}
		if(type =='p')
		{
			String panelName = panel.getName();
			String[] coord = panelName.split(" ");
			int posY = Integer.parseInt(coord[0]);
			int posX = Integer.parseInt(coord[1]);

			if(e.getModifiers() == 16 && this.tabCase[posY][posX].getAffichage() != ' ' && !lose)
			{
				if((char)(this.tabCase[posY][posX].calculNbAutour(tabCase, 'D')+'0') == this.tabCase[posY][posX].getAffichage())
				{
					for (int i=posY-1;i<posY+2;i++) 
					{
						for (int j=posX-1;j<posX+2;j++) 
						{
							if(this.canRemove(i,j) && this.tabCase[i][j].getAffichage() != 'D')
								this.removeBtn(this.tabP[i][j],this.tabCase[i][j], i, j);	
						}	
					}
				}
				if(dem.win(tabCase))
					this.win();

				this.validate();
			}

		}
		if(type == 'e')
		{
			if(btn.getLabel().equals("Recommencer"))
				this.restart();

			if(btn.getLabel().equals("Difficultés"))
				new FenetreDiff(this);
		}
		
	}

	public void removeBtn(JPanel p, Case cse, int posY, int posX)
	{
		p.remove(cse.getBtn());
		if(cse.getAffichage() == 'B')
			this.lose();


		JLabel lbl = new JLabel("       "+cse.getAffichage());
		this.tabP[posY][posX].add(lbl);
		
		//Recursive qui permet de supprimer les cases vides de proche en proche
		if(cse.getAffichage() ==  ' ' && (! cse.getClicked()) )
		{
			cse.setClicked();

			for (int i=posY-1 ; i<posY+2 ; i++ ) 
			{
				for ( int j=posX-1 ; j<posX+2 ; j++ ) 
				{
					if(this.canRemove(i,j) && (! this.tabCase[i][j].getBomb()))
						this.removeBtn(this.tabP[i][j], this.tabCase[i][j], i,j);	
				}
			}
		}

		this.validate();
	}

	public boolean canRemove(int posY, int posX)
	{
		if(posX == -1 || posX >= dem.getNbCol())
			return false;
		if(posY == -1 || posY >= dem.getNbLig())
			return false;

		return true;
	}

	public void moreDrap()
	{
		drapPose++;
		this.nbDrap.setText("Drapeaux posés : "+ drapPose +"/"+dem.getNbBom());
	}
	public void lessDrap()
	{
		drapPose--;
		this.nbDrap.setText("Drapeaux posés : "+ drapPose +"/"+dem.getNbBom());
	}

	public void lose()
	{
		Demineur.lose();
		this.lose = true;
		this.setBackground(Color.red);
		this.p.setBackground(Color.red);
	}

	public void win()
	{
		this.setBackground(Color.green);
		this.p.setBackground(Color.green);
	}

	public void restart()
	{
		
		switch(this.diff)
		{
			case 'F' : new Fenetre(9,9,10,this.diff);break;
			case 'M' : new Fenetre(16,16,40,this.diff);break;
			case 'D' : new Fenetre(30,16,99,this.diff);break;
			case 'E' : new Fenetre(30,24,180,this.diff);break;
		}
		this.dispose();
	}
	//Methode useless
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public static void main(String [] args) 
    { 
    	System.gc();
    	new Fenetre(9,9,10,'F'); 
    }
}