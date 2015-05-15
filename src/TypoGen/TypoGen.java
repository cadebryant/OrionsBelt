package TypoGen;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class TypoGen {
	private URL typoFileURL;
	private URI typoFileURI;
	private Path typoFilePath;
	private List<String> typoFile;
	private Map<String, TypoSet> typoSets;
	
	public TypoGen() {
		try {
			typoFileURL = new URL("https://raw.githubusercontent.com/iwek/typos/master/typos.txt");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			typoFileURI = typoFileURL.toURI();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		typoFilePath = Paths.get(typoFileURI);
		typoSets = new HashMap<String, TypoSet>();
		
		try {
			typoFile = Files.readAllLines(typoFilePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}