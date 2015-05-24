package TypoGen;

/* **HOW TO USE TYPOGEN**
 * TypoGen typoMaker = new TypoGen();
 * String outputText = typoMaker.insertTypos(inputText);
 */
import java.util.*;
import java.io.*;

public class TypoGen {
	private InputStream typoFIS;
	private InputStream typos2;
	private InputStreamReader typoISR;
	private InputStreamReader typoISR2;
	private BufferedReader typoBR;
	private BufferedReader typoBR2;
	private List<String> typoData;
	private Map<String, TypoSet> typoSets;
	private String comma;
	private String typoLine;

	public TypoGen() {
		typoFIS = TypoGen.class.getResourceAsStream("typos1.txt");
		typos2 = TypoGen.class.getResourceAsStream("typos2.txt");
		typoData = new ArrayList<String>();
		typoSets = new HashMap<String, TypoSet>();
		comma = "[,]";
		typoLine = "";

		try {
			typoISR = new InputStreamReader(typoFIS);
			typoISR2 = new InputStreamReader(typos2);
			typoBR = new BufferedReader(typoISR);
			typoBR2 = new BufferedReader(typoISR2);

			/* Read lines from typos1.txt.
			 * Both typos1.txt and typos2.txt are based on data
			 * from the following file:
			 * https://raw.githubusercontent.com/iwek/typos/master/typos.txt
			 */
			while (typoBR.ready()) {
				typoLine = typoBR.readLine();
				typoData.add(typoLine);
			}

			// Read lines from typos2.txt.
			while (typoBR2.ready()) {
				typoLine = typoBR2.readLine();
				typoData.add(typoLine);
			}

			typoFIS.close();
			typos2.close();
			typoISR.close();
			typoISR2.close();
			typoBR.close();
			typoBR2.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		for (String typoPair: typoData) {
			String[] splitPair = typoPair.split(comma);

			if (splitPair.length == 2) {
				if (typoSets.containsKey(splitPair[1])) {
					TypoSet currentSet = typoSets.remove(splitPair[1]);
					currentSet.insertTypo(splitPair[0]);
					typoSets.put(splitPair[1], currentSet);
				} else {
					TypoSet currentSet = new TypoSet();
					currentSet.insertTypo(splitPair[0]);
					typoSets.put(splitPair[1], currentSet);
				}
			}
		}
	}

	public String tokenizeText(String inputText) {
		String outputText = "";
		char[] inputChars = inputText.toCharArray();

		for (char ch: inputChars) {
			if (Character.isLetterOrDigit(ch)) {
				if (Character.isUpperCase(ch)) {
					outputText = outputText + Character.toLowerCase(ch);
				} else {
					outputText = outputText + ch;
				}
			} else {
				outputText = outputText + ' ' + ch + ' ';
			}
		}

		return outputText;
	}

	public String insertTypos(String inputText) {
		String spacebar = "[ ]+";
		String outputText = "";
		String tokenizedInput = tokenizeText(inputText);
		String[] splitTokens = tokenizedInput.split(spacebar);

		for (String token: splitTokens) {
			if (typoSets.containsKey(token)) {
				TypoSet currentSet = typoSets.get(token);
				outputText = outputText + currentSet.getTypo() + ' ';
			} else {
				outputText = outputText + token + ' ';
			}
		}

		return outputText;
	}
}