package TypoGen;

public class TypoGenTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String testString = "Set in the offices of two psychoanalysts, and in a very bad French restaurant, BEYOND THERAPY is a love story about two very confused people.";
		String testString2 = "set in the offices of two psychoanalysts , and in a very bad french restaurant , beyond therapy is a love story about two very confused people .";
		TypoGen typoMaker = new TypoGen();
		TypoGen typoMaker2 = new TypoGen(false);
		TypoGen typoMaker3 = new TypoGen(true);
		System.out.println(typoMaker.insertTypos(testString));
		System.out.println(typoMaker2.insertTypos(testString));
		System.out.println(typoMaker3.insertTypos(testString));
		System.out.println(typoMaker.insertTypos(testString2));
		System.out.println(typoMaker2.insertTypos(testString2));
		System.out.println(typoMaker3.insertTypos(testString2));
	}
}