// Nicholas Casale ncasale@ncsu.edu
// Lingxiao Zheng lzheng5@ncsu.edu
// 3/19/15
// Expression Calculator
// Supports:
// 1. Accumulator Mode
// 2. Expression Mode
// 3. Test Mode

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class ExpressionCalculator implements Accumulator, ActionListener,Calculator
{
   // Don't show debug trace messages on the console
   private static boolean debug = false; // set as a cmd line parm
   	
   //Implement GUI instance variables	
   JFrame window = new JFrame("Accumulator Mode");
   // error field (north)
   JLabel error = new JLabel("Error messages print here");
   // Text areas (center)
   JTextArea total = new JTextArea("$0");
   JTextField argument = new JTextField("Enter numbers to be added/subtracted here.");
   JTextArea log = new JTextArea("Log of past computations");
   	
   // Clear button and Radio buttons (south)
   JButton clear = new JButton("Clear");
   JButton instr = new JButton("Instructions");
   JRadioButton Accumulator= new JRadioButton("Accumulator",true);     
   JRadioButton Calculator  = new JRadioButton("Expression"); 
   JRadioButton Test = new JRadioButton("Test");     
   JRadioButton Graphing  = new JRadioButton("Graphing");
   JLabel modeLabel = new JLabel("Mode:");
   ButtonGroup buttonGroup = new ButtonGroup();
   JPanel clearInstr = new JPanel();
   JPanel radioButtons = new JPanel();
   JPanel buttonPanel = new JPanel();
    
   // scroll for log, and split for total/argument
   JScrollPane logscroll = new JScrollPane(log);
   JSplitPane totalArgumentSplit  = 
	 new JSplitPane(JSplitPane.VERTICAL_SPLIT,total, argument);
   JSplitPane totalargSplitWithLog  = 
	 new JSplitPane(JSplitPane.VERTICAL_SPLIT, logscroll, totalArgumentSplit);
       
   // Instructions window variables
   JFrame instructions = new JFrame("Instructions");
   JTextArea instruct = new JTextArea("Instructions: "
    		+ "\n\nTo hide this screen,"
    		+ "\nPress the Instructions button again,"
    		+ "\nOr press the X in this window."
    		+ "\n\n\nAccumulator Mode: "
    		+ "\n\nInput Format:"
    		+ "\nnum\t$num"
    		+ "\n+num\t-num"
    		+ "\n+$num\t-$num"
    		+ "\n$+num\t$-num"
    		+ "\n\nPs:If entering decimal points,"
    		+ "\nplease specify to the hundredths place."
    		+ "\nDo not enter any spaces after your operators (+ and -)"
    		+ "\nFor example: +10, -1, -9.45"
   		+ "\n\nCommands:"
    		+ "\nclear to clear total amount"
   		+ "\nhelp to show the instruction"
    		+ "\nexit to quit the applicaiton"
   		+ "\nsave to save a Log.txt on disk"
   		+ "\n\nPress enter to calculate!"
   		+"\n\n\nExpression Mode: \nTo evaluate expressions"
   		+"\nOperator Precedence (H->L): "
   		+"\n   (   )\n   ^   r\n   *   /\n   +   -"
   		+"\n\n\nTest Mode: "
   		+"\nTo test equalities\nOperator:\n   =");
   JScrollPane instructScroll = new JScrollPane(instruct);
   	
   // Expression Calculator GUI objects
   JLabel expressionLabel = new JLabel("Expr.:");
   JTextField expression = new JTextField("Enter Arbitrarily Complex Expression");
   JLabel fromXLabel = new JLabel("From X = ");
   JLabel toXLabel = new JLabel("To X = ");
   JTextField fromXField = new JTextField();
   JTextField toXField = new JTextField();
   JPanel exprLabelAndExpressionPanel = new JPanel();
   JPanel exprBoundsPanel = new JPanel();
   JPanel expressionArgumentPanel = new JPanel();
   
   String mode = new String("Accumulator");
   
   public static void main(String[] args) 
   {	
      // debug mode check
      // if (debug) System.out.println("information or error trace")
      if (args.length != 0) 
      if(args[0].equalsIgnoreCase("debug")) 
	debug = true;
	try
	{
		symtable.put("e", Math.E);
		symtable.put("pi", Math.PI);
		symtable.put("x",0.0);
		
		ExpressionCalculator ec = new ExpressionCalculator();   
	} 
	catch (Exception e)
	{
	   System.out.println(e);
	}
   }
   // Constructor
   public ExpressionCalculator() 
   {
      System.out.println("\nLingxiao Zheng lzheng5@ncsu.edu "
   	+ "\nNicholas Casale ncasale@ncsu.edu\n");
   		
      // Window composition
      window.add(error,"North");
      window.add(totalargSplitWithLog, "Center");
      window.setTitle("Business Accumulator Mode");
      window.setSize(570, 600);
      window.setLocation(0, 0);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
   	    
      // Bottom Panel composition
      radioButtons.setLayout(new GridLayout(1,5)); // bottom row 
      radioButtons.add(modeLabel);
      radioButtons.add(Accumulator);// Add GUI objects in
      radioButtons.add(Calculator); // left-to-right 
      radioButtons.add(Test);       // sequence
      radioButtons.add(Graphing);
   	    
      buttonPanel.setLayout(new GridLayout(2,1));
      buttonPanel.add(clearInstr);
      buttonPanel.add(radioButtons);
   	    
      window.getContentPane().add(buttonPanel,"South"); 
      log.setEditable(false);
      total.setEditable(false); 
      totalArgumentSplit.setDividerLocation(100); // split pane separator location
      totalargSplitWithLog.setDividerLocation(300);
   	    
      // Assign action listeners to each button
      buttonGroup.add(Accumulator);
      buttonGroup.add(Calculator);   
      buttonGroup.add(Test);
      buttonGroup.add(Graphing);
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
      
      // Expression Calculator GUI Construction
      // Default argument panel is just a single text field
      fromXLabel.setFont (new Font("default",Font.BOLD,17));
      fromXField.setFont (new Font("default",Font.BOLD,17));
      toXField.setFont (new Font("default",Font.BOLD,17));
      expression.setFont (new Font("default",Font.BOLD,17));
      toXLabel.setFont (new Font("default",Font.BOLD,17));
      exprLabelAndExpressionPanel.setLayout(new GridLayout(1,2));
      exprLabelAndExpressionPanel.add(expressionLabel);
      exprLabelAndExpressionPanel.add(expression);
      exprBoundsPanel.setLayout(new GridLayout(1,4));
      exprBoundsPanel.add(fromXLabel);
      exprBoundsPanel.add(fromXField);
      exprBoundsPanel.add(toXLabel);
      exprBoundsPanel.add(toXField);
      expressionArgumentPanel.setLayout(new GridLayout(2,1));
      expressionArgumentPanel.add(expression);
      expressionArgumentPanel.add(exprBoundsPanel);  
      fromXField.addActionListener(this);
      toXField.addActionListener(this);
      expression.addActionListener(this);
   }
   
   @Override
   public String accumulate(String total, String amount)
   	throws IllegalArgumentException 
   {		
      // Test for accidental newline
      if (amount.isEmpty()) return total;
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
   
      // Redundancy methods for embedded/leading/trailing blanks & "$"
      amount = amount.trim();
      amount = amount.replace(" ", "");
      amount = amount.replace("$", "");
   			
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
      {  // Clear the input area, the total area, and the error field
	 // when this button is pushed
	 if (debug) System.out.println("The clear button was pushed.");
	 error.setText(" "); // space so that the error bar doesn't disappear
	 argument.setText(""); // clear input field
	 expression.setText("");
	 if (mode.equals("Accumulator"))
	 {
	    argument.grabFocus();	
	    total.setText("$0"); // initialize total accumulated to zero
	 }
	 else
	 {
	    expression.grabFocus();
	    total.setText("0"); // initialize total accumulated to zero
	    toXField.setText("");
	    fromXField.setText("");
	 }
      }
      if (ae.getSource() == instr)
      {
	 if (debug) System.out.println("Instructions requested.");
	 if (instructions.isShowing()) instructions.setVisible(false);
	 else instructions.setVisible(true);
      }		
      if (ae.getSource() == Accumulator)
      { // do whatever when this button is pushed.
	 mode = "Accumulator";
	 if (debug) System.out.println(mode); 
	 window.setTitle("Business Accumulator Mode");
	 totalArgumentSplit.setBottomComponent(argument);
	 argument.grabFocus();
	 argument.selectAll();
	 total.setText("$0");
      }
      if (ae.getSource() == Calculator)
      { // do whatever when this button is pushed.
	 mode = "Calculator";
	 if (debug) System.out.println(mode);
	 window.setTitle("Expression Mode");	 
	 totalArgumentSplit.setBottomComponent(expressionArgumentPanel);
	 exprBoundsPanel.remove(toXLabel);
	 exprBoundsPanel.remove(toXField);
	 fromXLabel.setText("At X = ");
	 expression.grabFocus();
	 expression.selectAll();
	 total.setText("0");	 
	 fromXField.setText("0");
      }
      if (ae.getSource() == Test)
      { // do whatever when this button is pushed.
	 mode = "Test";
	 if (debug) System.out.println(mode); 
	 window.setTitle("Test Mode");
	 // Set up window
	 totalArgumentSplit.setBottomComponent(expressionArgumentPanel);
	 exprBoundsPanel.remove(toXLabel);
	 exprBoundsPanel.remove(toXField);
	 fromXLabel.setText("At X = ");
	 expression.grabFocus();
	 expression.selectAll();
	 total.setText("0");
	 fromXField.setText("0");
      }
      if (ae.getSource() == Graphing)
      { // do whatever when this button is pushed.
	 mode = "Graphing";
	 if (debug) System.out.println(mode); 
	 error.setText("Function not implemented yet.");
	 window.setTitle("Graphing Mode");
	 // Set up window
	 totalArgumentSplit.setBottomComponent(expressionArgumentPanel);
	 fromXLabel.setText("From X = ");
	 exprBoundsPanel.add(toXLabel);
	 exprBoundsPanel.add(toXField);
	 expression.grabFocus();
	 expression.selectAll();
	 total.setText("0");
      }
      if (ae.getSource() == argument)
      {  // This action event is called whenever
	 // the enter button is pressed in the argument field
	 try
	 {
	    if (debug) System.out.println("new line was pressed in the input field");
	    // read argument field
	    String op = argument.getText().trim(); // get text, remove leading and trailing blanks
	    op = op.replace(" ","");
	    op = op.replace("+", "");
	    // test for leading $ for op
	    if (op.contains("$"))
	       op = op.replaceAll("\\$","");
	    if (debug) System.out.println(op);
	    String currentTot = total.getText();	
	    //test for leading $ for currentTot
	    if (currentTot.startsWith("$"))
	       currentTot = currentTot.substring(1);
	    String sum = accumulate(currentTot, op);
	   
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
		  total.setText("$" + sum);
		  log.append("\n" + "$"+ currentTot + " + " + "$"+ op + " = " + total.getText());
	    }
	    log.setCaretPosition(log.getDocument().getLength());
	    argument.setText("");
   	 }
	 catch (Exception e)
	 {
	    error.setText(e.getMessage());	
	 }
      }
      if ((ae.getSource() == expression) || (ae.getSource() == fromXField))
      {
    	  String expr=expression.getText().trim();
    	  String atXString=fromXField.getText().trim();
	 try
	 {
		 String result=calculate(expr,atXString);
		 total.setText(result);
		 if (expr.contains("x"))
			 log.append("\n >>> "+expr+" for x = " +atXString+"\n\n      "+result+"\n");
		 else
			 log.append("\n >>> "+expr+"\n\n      "+result+"\n"); 
		 log.setCaretPosition(log.getDocument().getLength());
	 }
	 catch(IllegalArgumentException e)
	 {
		 error.setText(e.getMessage());
		 if (!expr.contains("x"))
			 log.append("\n >>> "+expr+"\n\n      "+e.getMessage()+"\n");
		 else
			 log.append("\n >>> "+expr+" for x = " + atXString +"\n\n      "+e.getMessage()+"\n");
		 log.setCaretPosition(log.getDocument().getLength());
	 }
     }
     if (ae.getSource() == toXField)
     {
    	 
     }
   }
   
   private void checkInput(String expr,String atXString)
   throws IllegalArgumentException
   {    
	   //if expr contains x, then check atXString
	   if (expr.contains("x") || expr.contains("X"))
	   {
		   expr = expr.replace("X", "x"); 
		   if (atXString.isEmpty())
			   throw new IllegalArgumentException("Input Error: x value must be provided");

		   //check if xstring is valid
		   if (!atXString.matches("^[0-9.]+$"))
			   throw new IllegalArgumentException("InputError: X must be a number");
		
		   //check if the digit is correct
		   if (atXString.indexOf(".") != atXString.lastIndexOf("."))
			   throw new IllegalArgumentException("InputError: Invalid x value");
	   }
	   //else then we do not need to care about x

	   // mode check for proper mode. 
	   if (expr.contains("=") && !mode.equals("Test"))
		   throw new IllegalArgumentException("InputError: Go into Test mode to test equalities");
	   if (!expr.contains("=") && mode.equals("Test"))
		   throw new IllegalArgumentException("InputError: Go into Expression mode to eval expressions");
  }
   
   //*********************************
   //Parsing goes here
   
   private static double x;
   private static InputStreamReader isr;
   @Override
   public String calculate(String exp, String x)
		   throws IllegalArgumentException 
   {
	   //error message about input goes here
	   //and will be printed on the error label
	   checkInput(exp,x);
	   
	   isr=new InputStreamReader(new ByteArrayInputStream(exp.getBytes()));
	   //parse x only if x is not empty
	   if (!x.isEmpty())
		   ExpressionCalculator.x=Double.parseDouble(x);
	   double rd=0;boolean rb=false;
	   curch=' ';
	   
	   try {
		   	while(isr.ready())
		   	{
		   		getNextTok();
		   		if (curTok==EOF) break;
		   		
		   		if (Calculator.isSelected()) rd=parseExpr(false);
		   		if (Test.isSelected()) rb=parseRExpr(false);
		   		
		   		if (curTok==')')
		   			throw new IllegalArgumentException("Parse Error: Parenthese mismatch");
		   	}
	   } 
	   catch (IOException e) {}
	   if (Calculator.isSelected()) 
		   return String.format("%.2f", rd);
	   else
		   return Boolean.toString(rb);
   }

   	//Tokens
   	private static final int EOF=-1;
   	private static final int IDE=-2,NUM=-3;
   	//operators
   	private static final int ROOT=-4;
   	//values
   	private static final int PI=-5,E=-6;

   	//sym_table
   	private static ConcurrentHashMap<String,Double> symtable=new ConcurrentHashMap<String,Double>();
   	
   	//number value
   	private static double num;
	private static int curch=' ';
	private int getTok() 
			throws IllegalArgumentException
	{
		int thischar=0;
		try 
		{	
			//skip white spaces
			while (Character.isWhitespace(curch))
				curch=isr.read();
			
			//identifiers
			if (Character.isLetter(curch))
			{
				switch (curch)
				{
				case 'x':
					curch=isr.read();
					return IDE;
				case 'r':
					curch=isr.read();
					return ROOT;
				case 'e':
					curch=isr.read();
					return E;
				default:
					break;
				}

				StringBuilder s=new StringBuilder(Character.toString((char)curch));
				while(Character.isLetter(curch=isr.read()))
					s.append((char)curch);
				
				if (s.toString().equals("pi")) return PI;
				
				//anything else
				throw new IllegalArgumentException("Parse Error: Undefined Variable "+s);
			}
			
			//numbers
			if (Character.isDigit(curch) || curch=='.')
			{
				StringBuilder s=new StringBuilder(Character.toString((char) curch));
				curch=isr.read();//eat this char
				
				while(Character.isDigit(curch) || curch=='.')
				{
					s.append((char)curch);
					curch=isr.read();
				}
				num=Double.parseDouble(s.toString());
				return NUM;
			}
			
			//eof
			if (curch==-1)	return EOF;
			
			//undefined symb
			if (!Character.toString((char)curch).matches("^[\\+\\-\\*\\/\\^\\(\\)\\=]"))
				throw new IllegalArgumentException("Parse Error: Undefined Symbol "+ (char)curch);
			
			//else return this curch
			thischar=curch;
			curch=isr.read();
		}
		//If there is an reading error, skip
		catch (IOException e) {}
		return thischar;
	}

	private static int curTok;
	private int getNextTok()
	{
		return curTok=getTok();
	}
	
	private boolean parseRExpr(boolean get)
	{
		double lhs=parseExpr(get);
		boolean eq=false;
		while(true)
		{
			switch(curTok)
			{
			case '=':
				double rhs=parseExpr(true);//eat =
				//set the precision of lhs and rhs to 5
				lhs = new BigDecimal(lhs,MathContext.DECIMAL64).setScale(5,BigDecimal.ROUND_UP).doubleValue();
				rhs = new BigDecimal(rhs,MathContext.DECIMAL64).setScale(5,BigDecimal.ROUND_UP).doubleValue();
				if (lhs-rhs==0)
					eq=true;
				else
					eq=false;
				break;
			default:
				return eq;
			}
		}
	}
	
	private double parseExpr(boolean get)
			throws IllegalArgumentException
	{
		double lhs=parseTerm(get);	
		while(true)
		{
		switch(curTok)
		{
		case '+':
		{
			double rhs=parseTerm(true);
			lhs+=rhs;
			break;
		}
		case '-':
		{
			double rhs=parseTerm(true);//eat '-'
			lhs-=rhs;
			break;
		}	
		default:
			return lhs;
		}
		}
	}
	
	private double parseTerm(boolean get)
			throws IllegalArgumentException
	{
		double lhs=parseETerm(get);		
		while(true)
		{
		switch(curTok)
		{
		case '*':
		{
			double rhs=parseETerm(true);//eat '*'
			lhs*=rhs;
			break;
		}
		case '/':
		{
			double rhs=parseETerm(true);//eat '/'
			if (rhs==0) 
				throw new IllegalArgumentException("Parse Error: Divide by Zero");
			lhs/=rhs;
			break;
		}	
		case '(':
			throw new IllegalArgumentException("Parse Error: Implicit Multiplication");
		default:
			return lhs;
		}
		}
	}
	
	private double parseETerm(boolean get)
			throws IllegalArgumentException
	{		
		double lhs=parsePrimary(get);
		while(true){
		switch(curTok)
		{
		case ROOT:
		{
			double rhs=parsePrimary(true);//eat 'r'
			lhs=Math.pow(lhs, 1/rhs);
			break;
		}
		case '^':
		{
			double rhs=parsePrimary(true);//eat '^'
			lhs=Math.pow(lhs, rhs);
			break;
		}	
		default:
			return lhs;
		}	
		}
	}
	
	private double parsePrimary(boolean get) 
			throws IllegalArgumentException
	{
		if (get) getNextTok();
		
		switch (curTok)
		{
			case IDE:	
				getNextTok();//eat ide
				symtable.replace("x", x);//update symbtable
				return x;
			case PI:
				getNextTok();
				return Math.PI;
			case E:
				getNextTok();
				return Math.E;
			case NUM:
				getNextTok();//eat num
				return num;
			case '+':
				return parsePrimary(true);//eat +
			case '-':
				return -parsePrimary(true);//eat -
			case '(':
			{
				double v=parseExpr(true);//eat '('
				if (curTok !=')')
					throw new IllegalArgumentException("Parse Error: Missing )");
				getNextTok();//eat ')'
				return v;
			}
			default:
				throw new IllegalArgumentException("Parse Error: "+(char)curTok+" not a primary");
		}
	}
}