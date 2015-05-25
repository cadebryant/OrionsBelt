package TypoGen;

public class ConvertText {
	/**
	 * @param args
	 * **COMMAND LINE ARGUMENTS FOR CONVERTTEXT**
	 * *Use "-inject-typos" or "--inject-typos" if you want to inject typos.
	 * *Use dataset directories as your other command line arguments.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean injectTypos = false;
		
		for (String arg: args) {
			if (arg.equals("-inject-typos") || arg.equals("--inject-typos")) {
				injectTypos = true;
			}
		}
	}
}