package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import application.GameMainController;

public class Memory_maker {
	
	public static void historyStart(List<Category> categories) throws Exception {
		//The absolute path is found regardless of location of the code.
		String path = GameMainController.path;
		//	String fullPath = (new File((new File(System.getProperty("java.class.path"))).getAbsolutePath())).getAbsolutePath();
		//	String [] relevantPath = fullPath.split(":");
		//	String path = (new File(relevantPath[0])).getParentFile().getAbsolutePath();
			
			// The winnings file is created and stored with 0 initially.
			File winnings = new File(path + "/.winnings");
			winnings.createNewFile();
			if (winnings.exists()) {
				BufferedWriter initialWriter = new BufferedWriter(new FileWriter(winnings));
				initialWriter.write("0");
				initialWriter.close();
			}
			else {
				throw new Exception("Winnings not made.");
			}
			// The history file is created and stored for updating.
			File history = new File(path + "/.History");
			Boolean successfullyMade = false;
			if (!history.exists()) {
				successfullyMade = history.mkdir();
			}
			
			// If it is, it copies all the files in categories into it.
			if (successfullyMade) {
				for (Category category:categories) {
					File newCategory = new File(path+"/.History/"+category.categoryName());
					if (!newCategory.exists()) {
						try {
							newCategory.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					//Add clue stuff here
					File inputFile = new File(path+"/.History/"+category.categoryName());
					BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
					for (Clue clue: category.getAllClues()) {
						String lineToAdd = clue.showClue() +"@"+clue.showClueType()+"@"+clue.showAnswer()+"@"+clue.showValue()+"@false";
						writer.write(lineToAdd + System.getProperty("line.separator"));
					}
					writer.close(); 
				}
			}
			else if (!history.exists()) {
				throw new Exception("History could not be made.");
			}
			
	}
	
	public static void update(Clue clue, Category category, Boolean correct) throws Exception {
		//Updating in the Model as well.
		GameMainController.getModel().markClueAsAnswered(category, clue);
		//The absolute path is found regardless of location of the code.
		String path = GameMainController.path;
		//String fullPath = (new File((new File(System.getProperty("java.class.path"))).getAbsolutePath())).getAbsolutePath();
		//String [] relevantPath = fullPath.split(":");
		//String path = (new File(relevantPath[0])).getParentFile().getAbsolutePath();
		File winnings = new File(path + "/.winnings");
		// Winnings should be present when updating them, so if it's not, an exception is thrown.
		if (!winnings.exists()){
			throw new Exception("Winnings file not made yet.");
		}
		
		// The clue answered has its value used (added if correct/subtracted if incorrect) with the old value
		// from the winnings file. Then the new value is added to a new file, which gets renamed to ".winnings"
		// replacing the old one.
		File oldWinnings = new File(path+"/.winnings");
		File newWinnings = new File(path+"/.myTempFile.txt");

		BufferedReader winningReader = new BufferedReader(new FileReader(oldWinnings));
		BufferedWriter winningWriter = new BufferedWriter(new FileWriter(newWinnings));

		String winningLine;

		while((winningLine = winningReader.readLine()) != null) {
			int alreadyWon = Integer.parseInt(winningLine);
			int result = alreadyWon;
			if (correct) {
				result = alreadyWon + clue.returnValue();
			}
			//Older code to deduct value on wrong naswer, but not doing that for this.
			/*else {
				result = alreadyWon - clue.returnValue();
			}*/
		    winningWriter.write(result + System.getProperty("line.separator"));
		}
		winningWriter.close(); 
		winningReader.close();
		newWinnings.renameTo(oldWinnings);
		
		// History should be present when updating them, so if it's not, an exception is thrown.
		File history = new File(path + "/.History");
		if (!history.exists()) {
			throw new Exception("History file not made yet.");
		}
		
		// The clue answered has its file located in history, and the file is copied line by line, excluding
		// the one of the clue answered, and then the new file gets renamed to the old one, replacing it.
		File inputFile = new File(path+"/.History/"+category.categoryName());
		File tempFile = new File(path+"/.History/myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String lineToRemove = clue.showClue()+"@"+clue.showClueType()+"@"+clue.showAnswer()+"@"+clue.showValue()+"@false";
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    if(!currentLine.equals(lineToRemove)) {
		    	writer.write(currentLine + System.getProperty("line.separator"));
		    }
		    else {
		    	writer.write(currentLine.substring(0, currentLine.length()-4) + "true");
		    }
		}
		writer.close(); 
		reader.close();
		tempFile.renameTo(inputFile);
		
	}
	
	/**
	 * reset() utilises to deleteDir to delete the winnings and history files, to remove the users progress and reset the game.
	 */
	public static void reset() {
		//The absolute path is found regardless of location of the code.
		String path = GameMainController.path;
		//String fullPath = (new File((new File(System.getProperty("java.class.path"))).getAbsolutePath())).getAbsolutePath();
		//String [] relevantPath = fullPath.split(":");
		//String path = (new File(relevantPath[0])).getParentFile().getAbsolutePath();
		File history = new File(path + "/.History");
		File winnings = new File(path + "/.winnings");
		//Checks the files and deletes them if they exist using deleteDir.
		if (history.exists()) {
			deleteDir(history);
		}
		if (winnings.exists()) {
			deleteDir(winnings);
		}
	}
	
	/**
	 * deleteDir is basically a helper function.
	 * It deletes files recursively, hence used to delete directories.
	 * It's major use is to be used to delete the history directory when the game is reset.
	 * @param fileOrDir
	 */
	public static void deleteDir(File fileOrDir) {
	    File[] files = fileOrDir.listFiles();
	    if(files != null) {
	        for (final File file : files) {
	            deleteDir(file);
	        }
	    }
	    fileOrDir.delete();
	}

}
