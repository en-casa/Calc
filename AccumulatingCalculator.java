// Nicholas Casale & Lingxiao Zhang
// ncasale@ncsu.edu lzheng5@ncsu.edu
// Accumulating calculator
// 2/11/15
// Takes input arguments:
// debug (if you seek debug mode)

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.*;
import java.io.*;
import java.util.Date;

public class AccumulatingCalculator implements ActionListener, Accumulator
{
	// Don't show debug trace messages on the console
	private static boolean debug = false; // set as a cmd line parm
	
	//Implement GUI instance variables
	JFrame window = new JFrame("Accumulator Mode");
	// error field (north)
	JLabel error = new JLabel("Error messages print here");
	// Text areas (center)
	JTextArea total = new JTextArea("0");
	JTextField argument = new JTextField("Enter numbers to be added/subtracted here.");
	JTextArea log = new JTextArea("Log of past computations");
	
	// Clear button and Radio buttons (south)
	JButton clear = new JButton("Clear");
	JButton instr = new JButton("Instructions");
	JRadioButton Accumulator= new JRadioButton("Accumulator",true);     
    JRadioButton Calculator  = new JRadioButton("Calculator"); 
    JRadioButton Test = new JRadioButton("Test");     
    JRadioButton Graphing  = new JRadioButton("Graphing");
    JLabel modeLabel = new JLabel("Mode:");
    ButtonGroup mode = new ButtonGroup();
    JPanel clearInstr = new JPanel();
    JPanel Radio = new JPanel();
    JPanel south = new JPanel();
 
    // scroll for log, and split for total/argument
	JScrollPane  logscroll = new JScrollPane(log);
    JSplitPane   totalArgumentSplit  = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            total, argument);
    JSplitPane totalargSplitWithLog  = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
    		logscroll, totalArgumentSplit);
    
    // Instructions window variables
    JFrame instructions = new JFrame("Instructions");
    JTextArea instruct = new JTextArea("Instructions: "
    		+ "\n\nTo hide this screen,"
    		+ "\nPress the Instructions button again,"
    		+ "\nOr press the X in this window."
    		+ "\n\nFor Accumulator Mode: "
    		+ "\nIf entering decimal points,"
    		+ "\nplease specify to the hundredths place."
			+ "\nDo not enter any spaces after your operators (+ and -)"
    		+ "\nFor example: +10, -1, -9.45"
			+ "\n\nCommands:"
    		+ "\nclear to clear total amount"
			+ "\nhelp to show the instruction"
    		+ "\nexit to quit the applicaiton"
			+ "\nsave to save a Log.txt on disk"
			+ "\n\nPress enter to calculate!");
    JScrollPane instructScroll = new JScrollPane(instruct);
	
	public static void main(String[] args) 
	{	
		// debug mode check
		// if (debug) System.out.println("information or error trace")
		if ((args.length != 0) && args[0].equalsIgnoreCase("debug")) debug = true;
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
		
	    // Window composition
	    window.add(error,"North");
	    window.add(totalargSplitWithLog, "Center");
	    window.setTitle("Business Accumulator Mode");
	    window.setSize(500, 600);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Bottom Panel composition
	    // Button colors
	    clear.setBackground(Color.green);
	    clear.setOpaque(true);
	    instr.setBackground(Color.yellow);
	    instr.setOpaque(true);
	    error.setForeground(Color.red);
	    modeLabel.setFont(new Font("default",Font.BOLD,18));
	    
	    clearInstr.setLayout(new GridLayout(1,2)); // top row
	    clearInstr.add(clear);
	    clearInstr.add(instr);
	    
	    Radio.setLayout(new GridLayout(1,5)); // bottom row 
	    Radio.add(modeLabel);
	    Radio.add(Accumulator);// Add GUI objects in
	    Radio.add(Calculator); // left-to-right 
	    Radio.add(Test);       // sequence
	    Radio.add(Graphing);
	    
	    south.setLayout(new GridLayout(2,1));
	    south.add(clearInstr);
	    south.add(Radio);
	    
	    window.getContentPane().add(south,"South"); 
	    log.setEditable(false);
	    total.setEditable(false);
	    totalArgumentSplit.setDividerLocation(100);//split pane separator location
	    totalargSplitWithLog.setDividerLocation(300);
	    
	    // Assign action listeners to each button
	    mode.add(Accumulator);
	    mode.add(Calculator);   
	    mode.add(Test);
	    mode.add(Graphing);
	    clear.addActionListener(this);
	    instr.addActionListener(this);
	    Accumulator.addActionListener(this);
	    Calculator.addActionListener(this);	
	    Test.addActionListener(this);
	    Graphing.addActionListener(this);
	    argument.addActionListener(this);
	    
	    // last tweaks, make visible
	    log.setFont (new Font("default",Font.PLAIN,15));
	    total.setFont(new Font("default",Font.BOLD,40));
	    argument.setFont(new Font("default", Font.PLAIN, 20));
	    window.setVisible(true);
	    
	    // Instructions window (don't set visible until button press)
	    instructions.add(instructScroll);
	    instructions.setSize(400, 500);
	    instructions.setLocation(500, 0);
	    instruct.setEditable(false);
	    argument.grabFocus();
	    argument.selectAll();
	}

	@Override
	public String accumulate(String total, String amount)
			throws IllegalArgumentException 
	{		
			// Test for accidental newline
			if (amount.isEmpty()) return total;
			// test for leading $
			if (amount.startsWith("$"))
				amount = amount.substring(1);
			//commands
			if (amount.equals("clear"))
			{
				clear.doClick();
				return amount;
			}
			if (amount.equals("help"))
			{
				instr.doClick();
				return amount;
			}
			if (amount.equals("exit")||amount.equals("save"))
				return amount;
			if (!amount.matches("[-0-9+.]+"))
				throw new IllegalArgumentException("Amount not numeric");

			// Redundancy methods for embedded/leading/trailing blanks
			amount = amount.trim();
			amount = amount.replace(" ", "");
			
			// Test for 2 numbers after decimal on input
			int indexDotAmount = amount.indexOf('.');
			if(indexDotAmount != -1)
			{	
				String afterDot = amount.substring(indexDotAmount+1);
				if (afterDot.length() != 2) 
				{
					error.setText("Amount needs 2 #'s after decimal point. "
							+ "Total remains the same.");
					return total;
				}
			}
			// Compute new total
			Double t=new Double(total);
			Double a=new Double(amount);
			String s=Double.toString(t+a);
			// Round to 2 decimal places, convert back to string
		    BigDecimal  totalBD = new BigDecimal(s,MathContext.DECIMAL64);//set precision to 16 digits
			totalBD = totalBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.
			String newTotal = totalBD.toPlainString();// no exponents	
			if (debug) System.out.println("New Total: " + newTotal);
			int indexDot = newTotal.indexOf('.');
			if (newTotal.contains(".00")) newTotal = newTotal.substring(0, indexDot);
			return newTotal;	
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{ // button actions
	      error.setText(" ");// clear 	
		  if (ae.getSource() == clear)
		  { // Clear the input area, the total area, and the error field
			   // when this button is pushed
		     if (debug) System.out.println("The clear button was pushed.");
		     error.setText(" "); // space so that the error bar doesn't disappear
		     total.setText("0"); // initialize total accumulated to zero
		     argument.setText(""); // clear input field
		     argument.grabFocus();
		  }
		  if (ae.getSource() == instr)
		  {
			  if (debug) System.out.println("Instructions requested.");
			  if (instructions.isShowing()) instructions.setVisible(false);
			  else instructions.setVisible(true);
		  }		
		  if (ae.getSource() == Accumulator)
		  { // do whatever when this button is pushed.
		     if (debug) System.out.println("Accumulator Mode was pushed."); 
		     window.setTitle("Business Accumulator Mode");
		  }
		  if (ae.getSource() == Calculator)
		  { // do whatever when this button is pushed.
			 if (debug) System.out.println("Calculator Mode was pushed."); 
		     error.setText("Function not implemented yet.");
		     window.setTitle("Calculator Mode");
		  }
		  if (ae.getSource() == Test)
		  { // do whatever when this button is pushed.
			 if (debug) System.out.println("Test Mode was pushed."); 
		     error.setText("Function not implemented yet.");
		     window.setTitle("Test Mode");
		  }
		  if (ae.getSource() == Graphing)
		  { // do whatever when this button is pushed.
			 if (debug) System.out.println("Graphing Mode was pushed."); 
		     error.setText("Function not implemented yet.");
		     window.setTitle("Graphing Mode");
		  }
		  if (ae.getSource() == argument)
		  { 	// This action event is called whenever
			  	// the enter button is pressed in the argument field
			  try
			  {
				  if (debug) System.out.println("new line was pressed in the input field");
				  // read argument field
				  String op = argument.getText().trim(); // get text, remove leading and trailing blanks
				  op = op.replace(" ","");
				  op = op.replace("+", "");
				  String currentTot = total.getText();		
				  String sum=accumulate(currentTot,op);
				  switch (sum)
				  {
				  case "clear":
					  log.append("\nclear");
					  break;
				  case "help":
					  log.append("\nhelp");
					  break;
				  case "exit":
					  System.exit(0);
				  case "save":
					  log.append("\nsave to local");
					  String history=log.getText();
					  System.out.println(history);
					  BufferedWriter bw=new BufferedWriter(new FileWriter("Log.txt",true));
					  bw.write(new Date().toString()+"\n");
					  bw.write(history+"\n\n");
					  bw.close();
					  break;
				  default:
					  total.setText(sum);
					  log.append("\n" + currentTot + " + " + op + " = " + total.getText());
				  }
				  log.setCaretPosition(log.getDocument().getLength());
				  argument.setText("");
			  }
			  catch (Exception e)
			  {
				  error.setText(e.getMessage());
			  }
		  }  
	}
}