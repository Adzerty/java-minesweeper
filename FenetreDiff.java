import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class FenetreDiff extends JFrame implements MouseListener
{
	private JPanel panel = new JPanel();
	private JButton btnFacile = new JButton("Facile 9x9 10bombes");
	private JButton btnMoyen = new JButton("Moyen 16x16 40bombes");
	private JButton btnDifficile = new JButton("Difficile 30x16 99bombes");
	private JButton btnExtreme = new JButton("Extrême 30x24 180bombes");
	private Fenetre f;

	FenetreDiff(Fenetre f)
	{
		this.f = f;
		this.setTitle("Difficultés");
		this.setSize(210,200);
		this.setLocation(200,200);

		btnFacile.addMouseListener(this);
		btnFacile.setName("F");

		btnMoyen.addMouseListener(this);
		btnMoyen.setName("M");

		btnDifficile.addMouseListener(this);
		btnDifficile.setName("D");

		btnExtreme.addMouseListener(this);
		btnExtreme.setName("E");

		this.panel.add(btnFacile);
		this.panel.add(btnMoyen);
		this.panel.add(btnDifficile);
		this.panel.add(btnExtreme);

		this.add(panel);
		// this.pack();
		this.setVisible(true);
	}
	//Methode useless
	public void mousePressed(MouseEvent e)
	{
		JButton btn = (JButton) e.getSource();
		switch(btn.getName())
		{
			case "F" : new Fenetre(9,9,10,'F');f.dispose();this.dispose();break;
			case "M" : new Fenetre(16,16,40,'M');f.dispose();this.dispose();break;
			case "D" : new Fenetre(30,16,99,'D');f.dispose();this.dispose();break;
			case "E" : new Fenetre(30,24,180,'E');f.dispose();this.dispose();break;
		}
	}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public static void main(String [] args) {}
}

