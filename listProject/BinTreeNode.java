package listProjetThree;

// This class creates a Binary tree list node that represents the individual 
// words. String word stores the word itself, the number of times it is found is 
// stored in int count, and a link to the next left node is referenced by leftChild
// and a link to the next right node is referenced by rightChild

public class BinTreeNode
{
	private String word;
	private int count;
	private BinTreeNode leftChild;
	private BinTreeNode rightChild;

	public BinTreeNode(String word)
	{
		this.word = word;
		count = 1;
		leftChild = null;
		rightChild = null;
	}
	
	public String getWord()
	{		
		return this.word;
	}

	public BinTreeNode getLeftChild()
	{
		return leftChild;
	}

	public void setLeftChild(BinTreeNode leftChild)
	{
		this.leftChild = leftChild;
	}

	public BinTreeNode getRightChild()
	{
		return rightChild;
	}

	public void setRightChild(BinTreeNode rightChild)
	{
		this.rightChild = rightChild;
	}

	public int getCount()
	{
		return this.count;
	}
	
	public void incrementCount()
	{
		count++;
	}

}
