package view;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import model.Conference;
import model.Paper;
import model.Review;
import model.Reviewer;
import model.SubProgramChair;
import model.User;

public class SubProgramChairUI implements Serializable{

	private static final int SUBMIT_REC = 1;
	private static final int ASSIGN_REV = 2;
	
	/**
	 * Serial ID for storage
	 */
	private static final long serialVersionUID = -871062617576421985L;
	private SubProgramChair mySubProgramChair;
	private Conference myConference;
	private Calendar myCalendar;
	
	public SubProgramChairUI() {
		myCalendar = Calendar.getInstance();
	}
	
	/**
	 * Displays the Menu options for the Sub-Program Chair.
	 * @author Jeremy Wolf
	 */
	public void scMenu(Conference theConference, SubProgramChair sC) {
		mySubProgramChair = sC;
		myConference = theConference;
		int selection = -1;
		Scanner scanner = new Scanner(System.in);
		
		while(selection != 0) {
			displayDetails();
		
			System.out.println("Make a Selection: ");
			System.out.println("1) Submit a Recommendation");
			System.out.println("2) Assign Reviewer to a paper");
			System.out.println("0) Back\n");
		


			selection = scanner.nextInt();
			System.out.println("___________________________________________________ \n");
			if(selection == SUBMIT_REC) {
				submitRecommendation();
			} else if (selection == ASSIGN_REV) {
				assignReviewer();
			}
		}
	}
	
	
	public void assignReviewer() {
		Paper tempPaper = null;
		Reviewer tempRev = null;
		displayDetails();
		System.out.println("Select a Paper to be Reviewed");
		int selection = displayPapers();
		if (selection != 0) {
			tempPaper = mySubProgramChair.getPaperList().get(selection - 1);
			displayDetails();
			System.out.println("Paper: " + tempPaper.getTitle() + "\n");
			System.out.println("Select a User to review the Paper:");
			selection = displayUsers();
			if (selection != 0) {
				boolean status = mySubProgramChair.isAuthor(selection, tempPaper);
				
				if (status) {
					cantReview();
				} else {
					tempRev = mySubProgramChair.createReviewer(selection); 
					boolean wasAdded = tempRev.addPaper(tempPaper);
					
					if (wasAdded) {
						System.out.println("The paper has been assigned to the reviewer.");
					} else {
						System.out.println("A Reviewer can't have more then 4 papers");
				}
				
				}
			}
			System.out.println("___________________________________________________ \n");
		}
	}

	public void submitRecommendation() {
		int selection = 1;
		Scanner scanner = new Scanner(System.in);
		
		displayDetails();
		
		System.out.println("Select a paper to make a recommendation:");
		selection = displayPapers();

		if (selection != 0) {
			Paper tempPaper = mySubProgramChair.getPaperList().get(selection - 1);
			displayRatings(tempPaper);
			System.out.println("\nSelect Recommendation:");
			System.out.println("1) Recommend");
			System.out.println("2) Deny");
			System.out.println("0) Back");
	    	selection = scanner.nextInt();
	    	mySubProgramChair.makeRecommendation(selection, tempPaper);
	    	System.out.println("Recommendation submited");
	    	System.out.println("___________________________________________________ \n");
		}
	}
	
	public void displayRatings(Paper thePaper) {
		displayDetails();
		System.out.println("Paper: " + thePaper.getTitle());
		int counter = 1;
		for(Review rev : thePaper.getReviews()) {
			System.out.println("Review " + counter + ": ");
			System.out.println("\tRating is " + rev.getRating());
			counter++;
		}
	}

	public int displayPapers() {
		int optionCounter = 1;
		int selection = -1;
		Scanner scanner = new Scanner(System.in);
		
		for (Paper printPaper: mySubProgramChair.getPaperList()) {
			System.out.print(optionCounter + ") ");
			System.out.print(printPaper.getTitle()+ "\n");
			optionCounter++;
		}
		System.out.println("0) Back\n");
		selection = scanner.nextInt();
		System.out.println("___________________________________________________ \n");
		
		return selection;
	}
	
	/**
	 * Displays the details to be on the top of the screen
	 * @author Jeremy Wolf
	 */
	public void displayDetails() {
		
		System.out.println("MSEE System");
		Date today = myCalendar.getTime();
		System.out.println("Date: " + today.toString());
		System.out.println("User: " + mySubProgramChair.getID());
		System.out.println("Conference: " + myConference.getName());
		System.out.println("Role: Sub-ProgramChair \n");
	}
	
	/**
	 * Displays the Users to the console
	 * @author Jeremy Wolf
	 * @return an Int value for the menu selection.
	 */
	public int displayUsers() {
		Scanner scanner = new Scanner(System.in);
		int optionCounter = 1;
		int selection = -1;
		for (User tempUser: mySubProgramChair.getList()) {
			System.out.print(optionCounter + ") ");
			System.out.print(tempUser.getFirst() + " " + tempUser.getLast() + "\n");
			optionCounter++;
			}
		
		System.out.println("0) Back"); 
		selection = scanner.nextInt();
		return selection;
	}
	
	/**
	 * UI printout for Author is cant review.
	 */
	public void cantReview() {
		System.out.println("An Author can't review their own paper.");
		System.out.println("___________________________________________________ \n");	
	}
}
