package TypoGen;

import java.util.*;

public class TypoSet {
	private List<String> typos;
	private Random rand;
	
	public TypoSet() {
		typos = new ArrayList<String>();
		rand = new Random();
	}
	
	public void insertTypo(String typo) {
		typos.add(typo);
	}
	
	public String getTypo() {
		return typos.get(rand.nextInt(typos.size()));
	}
}