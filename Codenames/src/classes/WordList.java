package classes;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WordList {
	
	public String[] WordList() {
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
		return getWordList(out.toString());
	}
	
	public String[] getWordList(String in) {
		List<String> words = Arrays.asList(in.split(","));
		Collections.shuffle(words);
		StringBuilder outUse = new StringBuilder();
		StringBuilder outRemaining = new StringBuilder();
		for (int i = 0; i < 25; i++) {
			outUse.append(words.remove(0));
		} 
		for (int j = 0 ; j < words.size(); j++ ) {
			outRemaining.append(words.get(j));
		}		
		return new String[]{outUse.toString(),outRemaining.toString()};
	}
	
}
