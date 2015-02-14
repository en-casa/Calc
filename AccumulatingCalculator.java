// Nicholas Casale & Lingxiao Zhang
// ncasale@ncsu.edu lzheng5@ncsu.edu
// Accumulating calculator
// 2/11/15
// Takes input arguments:
//

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;


public class AccumulatingCalculator implements ActionListener, Accumulator
{
	// Don't show debug trace messages on the console
	static boolean debug = false; // set as a cmd line parm
	private String sum="0";
	private String args;
	private static boolean flag=false;
	
	//Implement GUI instance variables
	JFrame window = new JFrame("Accumulator Mode");
	// error field (north)
	JLabel error = new JLabel("Error messages print here");
	// Text areas (center)
	JTextArea total = new JTextArea("Total sums is 0");
	JTextArea argument = new JTextArea("Enter numbers to be added/subtracted here."
			+ "\nIf entering decimal points, please specify to the hundredths place."
			+ "\nDo not enter any spaces after your operators (+ and -)"
			+ "\n\nPress enter to calculate!");
	JTextArea log = new JTextArea("Log of past computations");
	// Clear button and Radio buttons (south)
	JButton clear = new JButton("Clear");
	JRadioButton Accumulator= new JRadioButton("Accumulator Mode",true);     
    JRadioButton Calculator  = new JRadioButton("Calculator Mode"); 
    JRadioButton Test = new JRadioButton("Test Mode");     
    JRadioButton Graphing  = new JRadioButton("Graphing Mode");
    ButtonGroup mode = new ButtonGroup();
    JPanel clearRadio = new JPanel();
    // scroll for log, and split for total/argument
	JScrollPane  logscroll = new JScrollPane(log);
    JSplitPane   totalArgumentSplit  = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            total, argument);
    JSplitPane totalargSplitWithLog  = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
    		totalArgumentSplit, log);
    
	
	public static void main(String[] args) 
	{		
		// debug mode check
		// if (debug) System.out.println("information or error trace")
		if (args.length != 0)
		    if (args[0].equalsIgnoreCase("debug"))
			debug = true;			

		try
		{
			AccumulatingCalculator ac = new AccumulatingCalculator();
		} 
		catch (Exception e)
		{
			System.out.println(e);
		}

	}
	// Constructor
	public AccumulatingCalculator() 
	{
		System.out.println("\nLingxiao Zheng lzheng@ncsu.edu "
				+ "\nNicholas Casale ncasale@ncsu.edu\n");
		
	    // Chat Window composition
	    window.add(error,"North");
	    window.add(totalargSplitWithLog, "Center");
	    clearRadio.add(clear); // Add GUI objects in
	    clearRadio.add(Accumulator);// left-to-right
	    clearRadio.add(Calculator);  // sequence
	    clearRadio.add(Test);
	    clearRadio.add(Graphing);
	    window.getContentPane().add(clearRadio,"South");
	    window.setTitle("Business Accumulator"); 
	    log.setEditable(false);
	    total.setEditable(false);
	    totalArgumentSplit.setDividerLocation(200);//split pane separator location 
	    clear.setBackground(Color.green);
	    clear.setOpaque(true);
	    error.setForeground(Color.red);
	    error.setOpaque(true);
	    // Assign action listeners to each button
	    mode.add(Accumulator);
	    mode.add(Calculator);   
	    mode.add(Test);
	    mode.add(Graphing);
	    clear.addActionListener(this);
	    Accumulator.addActionListener(this);
	    Calculator.addActionListener(this);	
	    Test.addActionListener(this);
	    Graphing.addActionListener(this);
	    
	    window.setSize(800, 600);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setVisible(true);
	    
	    while(!flag)
	    {

	    	
	    	
	    }
	}

	@Override
	public String accumulate(String total, String amount)
			throws IllegalArgumentException 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{ // button actions
	      error.setText("  ");// clear 	
		  if (ae.getSource() == clear)
		     { // Clear the input area, the total area, and the error field
			   // when this button is pushed
		     if (debug)
		    	 System.out.println("The clear button was pushed."); 
		     
		     error.setText("  ");
		     total.setText("");
		     argument.setText("");
		     }
		  if (ae.getSource() == Accumulator)
		     { // do whatever when this button is pushed.
		     System.out.println("Accumulator Mode was pushed."); 
		     }
		  if (ae.getSource() == Calculator)
		     { // do whatever when this button is pushed.
		     System.out.println("Calculator Mode was pushed."); 
		     flag=true;
		     error.setText("Function not implemented yet.");
		     }
		  if (ae.getSource() == Test)
		     { // do whatever when this button is pushed.
		     System.out.println("Test Mode was pushed."); 
		     error.setText("Function not implemented yet.");
		     }
		  if (ae.getSource() == Graphing)
		     { // do whatever when this button is pushed.
		     System.out.println("Graphing Mode was pushed."); 
		     error.setText("Function not implemented yet.");
		     }
	}
}
