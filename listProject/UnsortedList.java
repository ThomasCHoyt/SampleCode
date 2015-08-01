package listProjetThree;

//UnsortedList class creates an unsorted list in which every
//new word found is added to the beginning of the list, and 
//words that are already on the list increment the node containing that word
//word count. The class keeps track of the following performance metrics:
//number of nodes created which is the number of distinct words found, 
//the total number of comparisons made to find if a word is already 
//on the list, and the total number of reference changes made in 
//order to place every word in the list.

public class UnsortedList
{
	
	protected int numNodes;
	protected long numOfComparisons;
	protected int numOfRefChanges;
	
	protected LLNode list;
	protected LLNode current;
	
	public UnsortedList()
	{
		numNodes = 0;
		numOfComparisons = 0;
		numOfRefChanges = 0;
		list = null;
	}// end of constructor
	
	// adds the word to the list
	public void add(String word)
	{
		
		// if a word is already on the list its nodes count is incremented 
		// without a new node being created
		if(contains(word))
		{
			current.incrementCount();
			return;
		}
		
		// if a word is not on the list a new node is created and added 
		// at the beginning of the list
		LLNode newNode = new LLNode(word);
		newNode.setLink(list);
		list = newNode;
		numOfRefChanges++;
		numNodes++;
	}// end of add method
	
	// finds out if a word is already on the list and returns boolean value
	// increments numOfComparisons each iteration of the while loop
	private boolean contains(String word)
	{
		// initialize current for the while loop
		current = list;
		
		// while loop traverses the list checking each node
		// to see if it contains "word"
		while(current != null)
		{
			numOfComparisons++;
			
			if((current.getWord()).equals(word))
				return true;
			
			current = current.getLink();
		}
		
		return false;
			
	}// end of contains method
	
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
	
}// end of class UnsortedList
