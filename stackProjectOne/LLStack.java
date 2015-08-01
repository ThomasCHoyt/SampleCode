
// Implements a stack of type T link list nodes
public class LLStack<T>
{
	protected LLNode<T> top;
	
	protected int length = 0;
	
	public LLStack()
	{
		top = null;
	}
	
	// adds a node to the stack
	public void push(T element)
	{	
		LLNode<T> newNode = new LLNode<T>(element);
		newNode.setLink(top);
		top = newNode;
		length++;
	}
	
	// removes the top node of the stack
	public void pop() throws StackUnderFlowException
	{
		if(!isEmpty())
			{
				top = top.getLink();
				length--;
			}
		
		else throw new StackUnderFlowException("Pop attempted on empty stack.");
	}
	
	// returns the element stored in the top node 
	public T top() throws StackUnderFlowException
	{
		if(!isEmpty()) 
			return top.getInfo();
		
		else throw new StackUnderFlowException("Pop attempted on empty stack.");
	}
	
	// returns true if the stack is empty false if it is not
	public boolean isEmpty()
	{
		return (top == null);
	}
	
	// returns the length number of nodes in the stack
	public int getLength()
	{
		return length;
	}

}
