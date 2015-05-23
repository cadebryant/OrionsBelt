package TypoGen;

public class TypoGenTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String testString = "Set in the offices of two psychoanalysts, and in a very bad French restaurant, BEYOND THERAPY is a love story about two very confused people.";
		TypoGen typoMaker = new TypoGen();
		System.out.println(typoMaker.insertTypos(testString));
		System.out.println(typoMaker.insertTypos(testString));
		System.out.println(typoMaker.insertTypos(testString));
	}
}