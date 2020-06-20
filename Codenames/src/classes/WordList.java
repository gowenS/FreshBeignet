package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WordList {
	String legalWords = "";
	
	public WordList() {
		String file = "/usr/word_lists/codenames_words.txt";
		StringBuilder out = new StringBuilder();
		String line = "";
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
                out.append(line);
                out.append(",");
            }
			out.deleteCharAt(out.length()-1);
			bufferedReader.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		legalWords = out.toString();
	}
	
	public WordList(String in) {
		legalWords = in;
	}		
	
	public String getInitialWordList() {
		return legalWords;
	}
	
	public String[] getNewGameWordList() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(legalWords.split(",")));
		Collections.shuffle(words);
		StringBuilder outUse = new StringBuilder();
		StringBuilder outRemaining = new StringBuilder();
		for (int i = 0; i < 25; i++) {
			if(i>0) outUse.append(",");
			outUse.append(words.remove(0));
		} 
		for (int j = 0 ; j < words.size(); j++ ) {
			if(j>0) outRemaining.append(",");
			outRemaining.append(words.get(j));
		}		
		return new String[]{outUse.toString(),outRemaining.toString()};
	}
	
	public ArrayList<String> getCurrentGameArray() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(legalWords.split(",")));
		return words;
	}	
	
//	public static void main(String[] args) {
//		WordList list = new WordList();
//		String[] build = list.getWordList();
//		System.out.println(build[0]);
//		System.out.print(build[1]);
//		String fullList = list.getInitialWordList();
//		System.out.println(fullList);
//		System.out.println(fullList.length());
//	}	
}
