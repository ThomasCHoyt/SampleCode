package listProjetThree;

// This class creates a link list node that represents the individual 
// words. String word stores the word itself, the number of times it is found is 
// stored in int count, and a link to the next node is referenced by LLNode link
public class LLNode
{
	private String word;
	private int count;
	private LLNode link;

	public LLNode(String word)
	{		
		this.word = word;
		count = 1;
		link = null;	
	}

	public void setLink(LLNode link)
	{
		this.link = link;
	}
	
	public LLNode getLink()
	{
		return link;
	}

	public String getWord()
	{		
		return this.word;
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
