
// Author, Thomas Hoyt
// 10/10/2014
// Dr. Larry Thomas
// Programming Project #1 
// Program asks the user for a well-formed infix expression, and scans it in.
// The program then converts the infix expression to a postfix expression, and takes
// the postfix expression and evaluates it. If the result is larger than an int value 
// then a overLoadException is thrown.

import java.util.*;

public class ProgramingProjectOne 
{

	// postfixExpression stores postfix expression converted from the infix expression
	static String postfixExpression = "";
	// converterStack stores the operators in their proper precedence
	static LLStack<String> converterStack = new LLStack<String>();
	
	
	public static void main(String[] args) throws StackUnderFlowException
	{

		String infixExpression;
		String postfix = "";
	
		Scanner input = new Scanner(System.in);
		
		// get expression from user
		System.out.print("Please enter a Well-Formed Infix Expression."
				+ "\nUsing only parentheses, integers, and operators"
				+ "\n(+,-,*,/,%,Q,C,<,>,^)\nQ and C must be followed by () enclosing"
				+ " the aurgument : ");
		infixExpression = input.nextLine(); 	
		input.close();
		
		//  try to convert to postfix if it is an illegal expression a exception is caught
		try
		{
			postfix = converter(infixExpression); 
		}
		catch(ConverterException ex)
		{
			System.out.print(ex.getMessage());
			System.exit(0);
		}
		
		
		// Display the conversion
		System.out.println("Converted to Postfix the expression " + postfix);
		
		// try catch block catches any exceptions thrown while converting or evaluating 
		// the users entered expression
		try
		{
			System.out.print("evaluates to " + postfixEvaluator(postfix));
		}
		catch(OverFlowException ex)
		{
			System.out.print(ex.getMessage());
		}
		
	}// end of main method
	
	// converts infix expression to postfix expression
	static String converter(String infixExpression) throws StackUnderFlowException, ConverterException 
	{	
		// counts the number of parentheses
		int pCount = 0;
		
		// for loop runs until there are no characters left in the infix expression
		// processing all the tokens in the infix expression
		for(int count = 0; count < infixExpression.length(); count++)
		{
			// get the next token in the infix expression
			char token = infixExpression.charAt(count);
			
			// if the next token is a operator a space is appended to the postfix 
			// expression to space it from an int. Then the processOperator
			// method is called to process the operator
			if(token == '+' || token =='-'|| token == '*' || token == '/' 
					|| token == '^' || token == 'Q' || token == 'C' 
					|| token == '<' || token == '>' || token == '%' )
			{
				postfixExpression += " ";
				processOperator(token);
			}
			
			else if(token == '(' )
			{
				converterStack.push(String.valueOf(token));
				pCount++;
			}
				
			// is the next token is a ')' all the previous operators
			// are appended to postfixExpression within processRightParen
			else if(token == ')' )
			{
				processRightParen();
				pCount++;
			}
				
			// if the next token is a space it is not processed
			else if(token == ' ')
				continue;
			
			// if the next token is an int it is appended to 
			// postfixExpression this line allows for multiple digit ints
			else if (Character.isDigit(token))
				postfixExpression += token;
			
			else throw new ConverterException("Illegal Symbol used");
			
			
		}// end of for loop
		
		// if there is not an even number of parentheses the expression
		// is not well formed
		if(pCount % 2 != 0)
			throw new ConverterException("Expression is not well formed");
		
		// while loop appends remaining operators to the postfixExpression
		while(!converterStack.isEmpty())
		{
			String top = converterStack.top();
			converterStack.pop();
			postfixExpression += (" " + top);
		}// end of while
		
		return postfixExpression;
	
	}// end of method converter
	
	// processes the operators appends them to the postfixExpression 
	// or push them onto the stack depending on the precedence of the operator
	static void processOperator(char token) throws StackUnderFlowException
	{
		String topToken = null;
		
		if(!converterStack.isEmpty())
			topToken = converterStack.top();

		while(!converterStack.isEmpty() && !(topToken.equals("(")) && (precedenceCheck(token,topToken.charAt(0))))
		{
			converterStack.pop();
			postfixExpression += topToken;
			topToken = converterStack.top();
		}// end of while loop
		
		converterStack.push(String.valueOf(token));
		
	}// end of method processOperator
	
	// processes everything on the converterStack between ')' and '('
	static void processRightParen() throws StackUnderFlowException
	{
		String topToken = converterStack.top();
		
		while(!topToken.equals("("))
		{
			postfixExpression += (" " + topToken);
			converterStack.pop();
			topToken = converterStack.top();
		}
		converterStack.pop();
		
	}// end method processRightParen
	
	// Checks the precedence between two operators 
	static boolean precedenceCheck(char token,char topToken)
	{
		char [] operatorsInOrder = {'^','Q','C','%','*','/','+','-','<','>'};
		
		int tokenIndex = 0 ;
		int topTokenIndex = 0;
		
		//finds precedence of token
		for(int count = 0; count < operatorsInOrder.length; count++)
		{
			if(token == operatorsInOrder[count])
				tokenIndex = count;
		}// end of for loop
		
		// finds precedence of topToken
		for(int count = 0; count < operatorsInOrder.length; count++)
		{
			if(topToken == operatorsInOrder[count])
				topTokenIndex = count;
		}
		
		return(tokenIndex <= topTokenIndex);
	}// end of method precedenceCheck
	
	// evaluates the postfixExpression
	static int postfixEvaluator(String postfixExpression) throws OverFlowException, StackUnderFlowException 
	{
		// stack stores the ints in the order they are received and after operations are performed  
		LLStack<Long> stack = new LLStack<Long>();
		
		// stores the value of the next integer
		  long value;
		  // stores the next operator
		  String operator;
		  
		  // stores operands 1 and 2
		  long operand1 = 0;
		  long operand2 = 0;
		  
		  //stores the result of a operation
		  long result = 0;
		  
		  // initialize the scanner to scan the postfix expression being evaluated
		  Scanner tokenizer = new Scanner(postfixExpression);
		  
		  //while loop runs until there are no more tokens
		  while (tokenizer.hasNext())
		  {
			// Process operand
		    if (tokenizer.hasNextInt())
		    {
		      value = tokenizer.nextInt();
		      
		      stack.push(value);
		    }
		    // Process operator
		    else
		    {
		      operator = tokenizer.next();
		        
		      // Obtain first top operand from stack if there are two on the stack
			  if(stack.getLength() > 1)
			  {
				  operand2 = stack.top();	      
			      stack.pop();
			  }
		    
		      // Obtain second bottom operand from stack.
		      operand1 = stack.top();		      
		      stack.pop();
	      
		      // Perform operation.
		      switch(operator)
		      {
		      case "/": result = operand1 / operand2;
		      			break;
		      case "*": result = operand1 * operand2;
		      			break;
		      case "+": result = operand1 + operand2;
		      			break;
		      case "-": result = operand1 - operand2;
		      			break;
		      case "^": result = (int)Math.pow(operand1, operand2);
		      			break;
		      case "Q": result = (int)Math.sqrt(operand1);	      
		      			stack.push(operand2);
		      			break;
		      case "C": result = (int)Math.cbrt(operand1);
		      			stack.push(operand2);
		      			break;
		      case "<": result = operand1 << operand2;
		      			break;
		      case ">": result = operand1 >> operand2;
		     			break;
		      case "%": result = operand1 % operand2;
		      			break;					
		      			
		      }// end of switch
		      
		   // Push result of operation onto stack.
		      stack.push(result);
		      
		   }// end of else
		    
		}// end of while
		  
		  // obtain final result from the stack
		  result = stack.top();
		    stack.pop();
		    
		    // if the result is bigger than an int throw an exception
		    if(result > 2147483647)
		    	throw new OverFlowException("over flow the result is to big");
		    
		    // Return the final result
		    return (int)result;
		    
	}// end of postfixProcessor
	
}// end of class ProgramingProjectOne
