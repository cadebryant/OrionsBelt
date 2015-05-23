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
	
	public String getTypo() {
		Random rand = new Random();
		return typos.get(rand.nextInt(typos.size()));
	}
}