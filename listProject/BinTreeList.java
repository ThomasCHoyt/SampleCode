package listProjetThree;

// BinTreeList class creates an binary tree list 
// words that are already on the list increment the node containing that word
// word count. The class keeps track of the total number of comparisons 
// made to find if a word is already on the list, and the total number of reference 
// changes made in order to place every word in the list. 

public class BinTreeList
{
	protected long numOfComparisons;
	protected int numOfRefChanges;
	
	protected BinTreeNode list;
	protected BinTreeNode current;
	protected BinTreeNode previous;

	public BinTreeList()
	{
		numOfComparisons = 0;
		numOfRefChanges = 0;
		list = null;
		previous = null;
		
	}// end of constructor
	
	// adds each word to the list either by incrementing a node or creating a 
	// new node and linking the node as a leaf node
	public void add(String word)
	{
		// initializes the current node, previous node, and 
		// leftOrRight for the while loop. leftOrRight is set to 0 if
		// the search through the tree needs to go left or 1 if the 
		// search through the tree needs to go right
		current = list;
		previous = list;
		int leftOrRight = -1;
		
		// while loop iterates through the tree searching for the current word scanned in
		while(current != null)
		{
			numOfComparisons++;
		
			// if the word is less than the word in the current node go left
			// through the tree
			if(word.compareTo(current.getWord()) < 0)
			{
				previous = current;
				current = current.getLeftChild();
				leftOrRight = 0;
				continue;
			}
				
			// if the word is greater than the word in the current node go right
			// through the tree
			else if(word.compareTo(current.getWord()) > 0)
			{
				previous = current;
				current = current.getRightChild();
				leftOrRight = 1;
				continue;
			}
			
			// else the current node contains the word
			// so increment currents word count
			else
			{
				current.incrementCount();
				return;	
			}
							
		}// end of while loop

		// if the while loop reached a null link a new leaf node is 
		// created and added to either the left or right link of the previous node
		// depending on the last comparison result
		BinTreeNode newNode = new BinTreeNode(word);
		numOfRefChanges++;
		
		if(leftOrRight == 0) previous.setLeftChild(newNode);

		else if(leftOrRight == 1) previous.setRightChild(newNode);
		
		// else is for special case that this is the first word on the list
		else list = newNode;
	
	}// end of add method
	
	// returns the total number of words by passing the root node of the 
	// binary tree and a initial integer value of zero to the private method totalWords
	// which returns the total words
	public int getNumTotalWords()
	{
		int total = 0;
		return totalWords(this.list, total);
	}// end of getNumTotalWords method
	
	// recursive method that traverses the binary tree adding the 
	// word counts of each node and returns the 
	// total number of words 
	private int totalWords(BinTreeNode node, int total)
	{	
		if(node == null) return total;
	
		total = totalWords(node.getLeftChild(), total);
	
		total = total + node.getCount();
		
		total = totalWords(node.getRightChild(), total);
		
		return total;
	}// end of totalWords method
	
	// returns the total number of disticnt words by passing the root node of the 
	// binary tree and a initial integer value of zero to the private method distinctWords
	// which returns the total number of distinct words
	public int getNumDistinctWords()
	{
		int total = 0;
		
		return distinctWords(list, total);
		
	}// end of getNumDistinctWords method
	
	// recursive method that traverses the binary tree incrementing a 
	// count integer at each node tracking the number of nodes in the 
	// binary tree the and returns the total number of distinct words 
	// which is the number of nodes
	private int distinctWords(BinTreeNode node, int total)
	{
		if(node == null) return total;
		
		total = distinctWords(node.getLeftChild(), total);
		
		total++;
		
		total = distinctWords(node.getRightChild(), total);
		
		return total;
	}// end of distinctWords method

}// end of BinTreeList class
