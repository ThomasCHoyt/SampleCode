package listProjetThree;

//SecondSelfAdjList class creates an self-adjusting list 
//words that are already on the list increment the node containing that word
//word count and move that node up one spot towards the front of the list. 
//The class keeps track of the following performance metrics:
//number of nodes created which is the number of distinct words found, 
//the total number of comparisons made to find if a word is already 
//on the list, and the total number of reference changes made in 
//order to place every word in the list.

public class SecondSelfAdjList
{
	protected int numNodes;
	protected long numOfComparisons;
	protected int numOfRefChanges;
	
	protected LLNode list;
	protected LLNode current;
	protected LLNode previous;
	protected LLNode preprevious;
	public SecondSelfAdjList()
	{
		numNodes = 0;
		numOfComparisons = 0;
		numOfRefChanges = 0;
		list = null;
		previous = null;
		preprevious = null;
	}// end of constructor
	
	// adds the word to the list
	public void add(String word)
	{
		// if the word is on the list already its node is moved one spot towards the front of the list
		if(contains(word))
		{
			// special case that word is already at the beginning of the list
			if(previous == null)
			{
				current.incrementCount();
				return;
			}
			
			// special case the word is second from the first node
			if(preprevious == null)
			{
				numOfRefChanges++;
				previous.setLink(current.getLink());
				
				numOfRefChanges++;
				list = current;
				current.setLink(previous);
				current.incrementCount();
				return;
			}
			
			// move the current node up one spot
			numOfRefChanges++;
			previous.setLink(current.getLink());
			
			numOfRefChanges++;
			preprevious.setLink(current);
			
			numOfRefChanges++;
			current.setLink(previous);
			current.incrementCount();
			
			return;
			
		}// end of if contains
		
		// if the list does not contain the word a new node is 
		// created and added to the front of the list
		numNodes++;
		LLNode newNode = new LLNode(word);
		numOfRefChanges++;
		newNode.setLink(list);
		list = newNode;
		
	}// end of add method
	
	// finds out if a word is already on the list and returns boolean value
	// increments numOfComparisons each iteration of the while loop
	private boolean contains(String word)
	{
		// initialize current, previous, and preprevious for the while loop
		current = list;
		previous = null;
		preprevious = null;
		
		// while loop traverses the list checking each node
		// to see if it contains "word"
		while(current != null)
		{
			numOfComparisons++;

			if((current.getWord()).equals(word))
			{
				return true;
			}
			
			preprevious = previous;
			previous = current;
			current = current.getLink();
			
		}
		
		return false;
			
	}// end of contains method
	
	// returns a string of the first words and their counts on the list. The number of words
	// in the string is specified by the int passed to the method
	public String printList(int count)
	{
		String words = "First " + count + " words and their counts on the list:";
		
		current = list;
		for(int i = 0; i < count; i++)
		{
			if(i % 10 == 0)
				words = words + "\n";
			
			words += current.getWord() + " " + current.getCount() + " ";			
			current = current.getLink();
		}
		return words;
	}
	
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

}// end of SecondSelfAdjList class
