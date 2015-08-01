package listProjetThree;

//AlphaList class creates an alphabetically sorted list 
//words that are already on the list increment the node containing that word
//word count. The class keeps track of the following performance metrics:
//number of nodes created which is the number of distinct words found, 
//the total number of comparisons made to find if a word is already 
//on the list, and the total number of reference changes made in 
//order to place every word in the list.

public class AlphaList
{
	protected int numNodes;
	protected long numOfComparisons;
	protected int numOfRefChanges;
	
	protected LLNode list;
	protected LLNode current;
	protected LLNode previous;
	
	public AlphaList()
	{
		numNodes = 0;
		numOfComparisons = 0;
		numOfRefChanges = 0;
		list = null;
		previous = null;
	}// end of constructor
	
	// adds the word to the list
	public void add(String word)
	{
				
		// Special case for if this is the first word to be added
		if(numNodes == 0)
		{
			LLNode newNode = new LLNode(word);
			list = newNode;
			numNodes++;
			return;
		}
		
		// initialize current and previous to begin the while loop
		current = list;
		previous = null;
		
		// stores the result of the output of comparTo method 
		// comparison of the word beginning added and the current node's word 
		int compResult = 0;
	
		while(current != null)
		{
			compResult = word.compareTo(current.getWord());
			numOfComparisons++;
		
			// if the current comparison is equal increment the current node
			// without creating a new node
			if(compResult == 0)
			{
				current.incrementCount();
				return;
			}
			
			// if the current comparison is alphabetically less then the current node
			// than a new node needs to be created and added
			if(compResult < 0)
			{
				// the special case that the current word being compare is alphabetically 
				// first in the list add it to the beginning of the list
				if(previous == null)
				{
					LLNode newNode = new LLNode(word);
					newNode.setLink(list);
					list = newNode;
					numOfRefChanges++;
					numNodes++;
					return;
				}
				
				// general case add the word to the spot before the current node being compared
				LLNode newNode = new LLNode(word);
				newNode.setLink(current);
				previous.setLink(newNode);
				numOfRefChanges++;
				numNodes++;
				return;
				
			}// end of if comparison is negative
			
			// if compResult is > 0 traverse to the next node
			previous = current;
			current = current.getLink();
			
		}// end of while loop
		
		// special case the word needs to be added to the end of the list
		// if the whole list is traversed then the word being compared 
		// is added to the end of the list
		LLNode newNode = new LLNode(word);
		previous.setLink(newNode);
		numNodes++;
		
	}// end of add method
	
	// returns the total number of words by summing the word count of all the nodes
	public int getNumTotalWords()
	{
		// stores the total number of words
		int wordCount = 0;
		
		current = list;
		
		while(current != null)
		{
			wordCount = wordCount + current.getCount();
			
			current = current.getLink();
		}// end of while loop
		
		return wordCount;
		
	}// end of numTotalWords method

}// end of class AlphaList
