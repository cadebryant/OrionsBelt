package TypoGen;

import java.util.*;

public class TypoSet {
	private List<String> typos;

	public TypoSet() {
		typos = new ArrayList<String>();
	}

	public void insertTypo(String typo) {
		typos.add(typo);
	}

	public String getTypo(int typoRate, String correctSpelling) {
		Random rand = new Random();

		if (rand.nextInt(100) + 1 <= typoRate) {
			return typos.get(rand.nextInt(typos.size()));
		} else {
			return correctSpelling;
		}
	}
}