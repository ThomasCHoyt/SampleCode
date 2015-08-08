package listProjetThree;


// Author Thomas Hoyt
// 12/7/2014
// Instructor Dr. Larry Thomas
// Programming Project #2 Lists
// This program analyzes the performance of Five different styles of linked lists;
// mainly to show the vast improvement of the Binary tree over linear based list
// The first list adds each word to the beginning of the list so it is unsorted.
// The second list is sorted alphabetically. The third list still places new words at the 
// front of the list but it is also self-adjusting. When it finds a word is already on the
// list that word is moved to the front of the list. The fourth list places new words 
// at the front and is also self-adjusting. When it finds a word that is already on the list
// that word is incremented forward one spot towards the front of the list. The Binary Tree 
// list either adds leaf nodes or increments the word count in an existing node.
// The five different lists are compared by how long they take to run, the 
// number of comparisons are made to place each word scanned in, the total number of 
// reference changes that are made, the total number of words, and the total number of distinct words.

//it is assumed that the file being analyzed exists with-in the listProjectTwo folder

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class ListProjectThree
{
	// constant string that is the name of the .txt file of Hamlet declared here at the 
	// start of the code so that if a different file needs to be tested it can be changed here
	static final String FILE_NAME = "Hamlet.txt";
	
	// start time and end time are used to calculate the total time each list runs
	static long startTime;
	static long endTime;
	
	// With-in the main method each of the four lists are created and analyzed one at a time
	public static void main(String[] args) throws IOException
	{
		// used to store the current word being processed into a list
		String word;
				
		// start the timer for the first list
		startTime = System.currentTimeMillis();
		
		// the first list which is unsorted and new words are added the the front
		UnsortedList unSortedList = new UnsortedList();
		
		// create the scanner and set the delimiter
		Scanner input = new Scanner(new File(FILE_NAME));
		input.useDelimiter("[^\\w']+");
		
		// while there are more words continue to process them
		while(input.hasNext())
		{
			// scan in the next word
			word = input.next();
			
			// convert all words to lower case for comparisons
			word = word.toLowerCase();
			
			// if the last character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(word.charAt(word.length() - 1) == 39)
				word = word.substring(0, (word.length() - 1));
			
			// if the first character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(!(word.equals("")) && (word.charAt(0) == 39))
				word = word.substring(1, (word.length()));
			
			unSortedList.add(word);
			
		}// end of while loop
		
		// end the timer
		endTime = System.currentTimeMillis();
		
		// output the unsorted list results to the counsel
		System.out.print("----------------------------------------\n"
				+"Unsorted List Results\nTime: " + (endTime - startTime) + " milliseconds" 
				+ "\nTotal number of words: " + unSortedList.getNumTotalWords()
				+ "\nTotal number of distinct words: " + unSortedList.numNodes
				+ "\nTotal number of comparisons: " + unSortedList.numOfComparisons
				+ "\nTotal number of reference changes: " + unSortedList.numOfRefChanges + "\n");
		
		// close the file
		input.close();
		
		// start the timer for alphabetical list
		startTime = System.currentTimeMillis();
		
		// create the second alphabetical list 
		AlphaList alphaList = new AlphaList();
		
		// create the scanner and set the delimiter
		input = new Scanner(new File(FILE_NAME));
		input.useDelimiter("[^\\w']+");
		
		// while there are more words continue to process them
		while(input.hasNext())
		{
			// scan in the next word
			word = input.next();
			
			// convert all words to lower case for comparisons
			word = word.toLowerCase();
			
			// if the last character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(word.charAt(word.length() - 1) == 39)
				word = word.substring(0, (word.length() - 1));
			
			// if the first character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(!(word.equals("")) && (word.charAt(0) == 39))
				word = word.substring(1, (word.length()));
			
			alphaList.add(word);
			
		}// end of while loop
		
		// end timer
		endTime = System.currentTimeMillis();
		
		// output the results of the Alphabetical list results to the counsel
		System.out.print("----------------------------------------\n"
				+ "Alphabetical List Results\nTime: " + (endTime - startTime) + " milliseconds" 
				+ "\nTotal number of words: " + alphaList.getNumTotalWords()
				+ "\nTotal number of distinct words: " + alphaList.numNodes
				+ "\nTotal number of comparisons: " + alphaList.numOfComparisons
				+ "\nTotal number of reference changes: " + alphaList.numOfRefChanges + "\n");
		
		// close the file
		input.close();
		
		// start the timer for 1st self-adjusting list
		startTime = System.currentTimeMillis();
		
		FirstSelfAdjList firstSelfAdj = new FirstSelfAdjList();
		
		// create the scanner and set the delimiter
		input = new Scanner(new File(FILE_NAME));
		input.useDelimiter("[^\\w']+");
		
		// while there are more words continue to process them
		while(input.hasNext())
		{
			// scan in the next word
			word = input.next();
			
			// convert all words to lower case for comparisons
			word = word.toLowerCase();
			
			// if the last character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(word.charAt(word.length() - 1) == 39)
				word = word.substring(0, (word.length() - 1));
			
			// if the first character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(!(word.equals("")) && (word.charAt(0) == 39))
				word = word.substring(1, (word.length()));
			
			firstSelfAdj.add(word);
			
		}// end of while loop
		
		// end timer
		endTime = System.currentTimeMillis();
		
		// output the results of the first self-adjusting list results to the counsel
		System.out.print("----------------------------------------\n"
				+"First Self-Adjusting List Results\nTime: "
				+ (endTime - startTime) + " milliseconds" 
				+ "\nTotal number of words: " + firstSelfAdj.getNumTotalWords()
				+ "\nTotal number of distinct words: " + firstSelfAdj.numNodes
				+ "\nTotal number of comparisons: " + firstSelfAdj.numOfComparisons
				+ "\nTotal number of reference changes: " + firstSelfAdj.numOfRefChanges + "\n");
		
		// close the file
		input.close();
		
		// start the timer for second self-adjusting list
		startTime = System.currentTimeMillis();
		
		// create the second self-adjusting list
		SecondSelfAdjList secondSelfAdjList = new SecondSelfAdjList();
		
		// create the scanner and set the delimiter
		input = new Scanner(new File(FILE_NAME));
		input.useDelimiter("[^\\w']+");
		
		// while there are more words continue to process them
		while(input.hasNext())
		{
			// scan in the next word
			word = input.next();
			
			// convert all words to lower case for comparisons
			word = word.toLowerCase();
			
			// if the last character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(word.charAt(word.length() - 1) == 39)
				word = word.substring(0, (word.length() - 1));
			
			// if the first character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(!(word.equals("")) && (word.charAt(0) == 39))
				word = word.substring(1, (word.length()));
			
			secondSelfAdjList.add(word);
			
		}// end of while loop
		
		// end timer
		endTime = System.currentTimeMillis();
		
		// output the results of the second self-adjusting list results to the counsel
		System.out.print("----------------------------------------\n"
				+"Second Self-Adjusting List Results\nTime: "
				+ (endTime - startTime) + " milliseconds" 
				+ "\nTotal number of words: " + secondSelfAdjList.getNumTotalWords()
				+ "\nTotal number of distinct words: " + secondSelfAdjList.numNodes
				+ "\nTotal number of comparisons: " + secondSelfAdjList.numOfComparisons
				+ "\nTotal number of reference changes: " + secondSelfAdjList.numOfRefChanges + "\n");
		
		// close the file
		input.close();
		
		// start the timer for Binary Tree list
		startTime = System.currentTimeMillis();
		
		BinTreeList binTree = new BinTreeList();
		
		// create the scanner and set the delimiter
		input = new Scanner(new File(FILE_NAME));
		input.useDelimiter("[^\\w']+");
		
		// while there are more words continue to process them
		while(input.hasNext())
		{
			// scan in the next word
			word = input.next();
			
			// convert all words to lower case for comparisons
			word = word.toLowerCase();
			
			// if the last character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(word.charAt(word.length() - 1) == 39)
				word = word.substring(0, (word.length() - 1));
			
			// if the first character of the word is a apostrophe (ASCII 39) 
			// remove it from the word
			if(!(word.equals("")) && (word.charAt(0) == 39))
				word = word.substring(1, (word.length()));
			
			binTree.add(word);
			
		}// end of while loop
		
		// end timer
		endTime = System.currentTimeMillis();
		
		// output the results of the Binary Tree list results to the counsel
		System.out.print("----------------------------------------\n"
				+"Binary Tree List Results\nTime: "
				+ (endTime - startTime) + " milliseconds" 
				+ "\nTotal number of words: " + binTree.getNumTotalWords()
				+ "\nTotal number of distinct words: " + binTree.getNumDistinctWords()
				+ "\nTotal number of comparisons: " + binTree.numOfComparisons
				+ "\nTotal number of reference changes: " + binTree.numOfRefChanges + "\n");
		
		// close the file
		input.close();
		
	}// end of main

}// end if class ListProjectTwo
