package ui;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FrameControl extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;


	public JFileChooser fc;
	public File[] files;

	public JButton start;
	public JButton stop;
	public JButton next;
	public JButton chooseFile;
	public FramePicture framePicture;
	public int index;
	
	public FrameControl(FramePicture framePicture) {
		
		this.framePicture = framePicture;
		
		this.setTitle("MojitoControl");
		this.setSize(400, 100);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
		this.setLocation(x, y);

		JPanel panel = new JPanel(new GridLayout(1,4));

		start = new JButton("Start");
		start.addActionListener(this);

		stop = new JButton("Pause");
		stop.addActionListener(this);
		
		next = new JButton("Next");
		next.addActionListener(this);
		
		chooseFile = new JButton("Select file");
		chooseFile.addActionListener(this);

		panel.add(chooseFile);
		panel.add(start);
		panel.add(stop);
		panel.add(next);
		
		
		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		
		
	    this.setContentPane(panel); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
		this.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == chooseFile) {
	        int returnVal = fc.showOpenDialog(FrameControl.this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	files = fc.getSelectedFiles();
	        	if(files.length>0) {
	        		framePicture.setPanels(files, 0);
	        		index = 0;
	        	}
	        }
		}
		else if(e.getSource() == start) {
			
		}
		else if(e.getSource() == stop) {
			
		}
		else if(e.getSource() == next) {
			index++;
    		framePicture.setPanels(files, index);
		}
	}
	
	
	
	
}
