package osd;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Help.Ansi;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Command(
		name = "lc",
		mixinStandardHelpOptions = true, 
		version = "@|bold,underline command lc -- CmdLC 0.1|@",
		headerHeading = "%n@|bold,underline Usage|@:%n%n",
		synopsisHeading = "%n",
		descriptionHeading = "%n@|bold,underline Description|@:%n%n",
		parameterListHeading = "%n@|bold,underline Parameters|@:%n",
		optionListHeading = "%n@|bold,underline Options|@:%n",
		header = "Uses command lc to check broken URLs",
		description = "Finding and reporting dead links in one file or multiple files " + 
				"along with a list showing good URL with green, " +
				"bad URL with red, redirect URL with yellow, others with unknown"
		)

public class CmdLC implements Callable<Integer> { 

	@Parameters( /* file name */ description = "The files which contains URLs need to be checked")
	private ArrayList<String> args;
	

	public static void main(String[] args) {

		int exitCode = new CommandLine(new CmdLC()).execute(args);
		System.exit(exitCode);
	}

	
	@Override
	public Integer call() throws FileNotFoundException, IOException {

		try {
			for(String arg : args) {
				
				//invoke visitFileRecursive method
				visitFileRecursive(arg);
			}

		}catch(FileNotFoundException ex) {
			// System.out.println(ex);
		}catch(IOException ex) {
			// System.out.println(ex);
		}

		return 0;
	}


	//extract urls from a file
	public HashSet<String> extractURL(String file) throws FileNotFoundException, IOException{
		
		//save the urls from the file, avoiding duplication
		HashSet<String> links = new HashSet<String> ();

		try{

			String content = new String(Files.readAllBytes(Paths.get(file)));	  

			//regular expression
			String urlRegex = "(https?)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
			Pattern pattern = Pattern.compile(urlRegex);
			Matcher matcher = pattern.matcher(content);

			while(matcher.find()) {
				links.add(matcher.group());
			}

		}catch(FileNotFoundException ex) {					
			//System.out.println(ex + "\n");
		}catch(IOException ex) {
			//System.out.println(ex);
		}
		return links;
	}



	int counter = 0, total = 0;

	//check url is valid or invalid
	public void urlTest(String link) throws MalformedURLException {

		try {
			URL url = new URL(link); 
			total++;		

			//URL connect and response
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			int responseCode = huc.getResponseCode();


			//	System.out.println(huc.getHeaderFields());

			//set redirect
			huc.setInstanceFollowRedirects(true);

			//set URL status
			if(responseCode == HttpURLConnection.HTTP_OK) {   //200				
				counter++;  			

				String str = "@|green " + "[" + responseCode + "]" + " GOOD     " + link + " |@";		
				System.out.println(Ansi.AUTO.string(str));				

			}

			else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == 307 || responseCode == 308)	 {   //301 Moved Permanently 									

				String str = "@|yellow " + "[" + responseCode + "]" + " REDIRECT " + link + " |@";						
				System.out.println(Ansi.AUTO.string(str));	
				
				// issue-6 redirection by Eunbee Kim
				String redirectURL = huc.getHeaderField("Location");
				urlTest(redirectURL);				
			}

			else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND || responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {   //400 HTTP_BAD_REQUEST , 404 HTTP_NOT_FOUND								

				String str = "@|red " + "[" + responseCode + "]" + " BAD      "  + link + " |@";						
				System.out.println(Ansi.AUTO.string(str));			
			}

			else {      //410 Gone	
				String str = "@|237 " + "[" + responseCode + "]" + " UNKNOWN  " +  link + " |@";			
				System.out.println(Ansi.AUTO.string(str));	
			}

		}catch(MalformedURLException ex) {

			String str = "@|237 " + "      UNKNOWN  " +  link + " |@";						
			System.out.println(Ansi.AUTO.string(str));				

		}catch(IOException ex) {

			String str = "@|237 " + "      UNKNOWN  " + link + " |@";						
			System.out.println(Ansi.AUTO.string(str));				
		}
	}


	//recursively visit the directory/files and subfiles
	public void visitFileRecursive(String path) throws IOException {

		Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				//files.add(file.toString());

				System.out.println("\nFinding and checking file \"" + file.toString() + "\" now ...");	
				//extract url from a file
				HashSet<String> links = extractURL(file.toString());

				//looping to test each url 
				for(String url: links) {
					urlTest(url);
				}

				//summary
				System.out.printf("Total valid URLs are: %d\nTotal checked URLs are: %d\n", counter, total);
				counter = 0; 
				total =0;

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				System.out.println("All files in the directory \"" + dir.toString() + "\" are checked.");
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException{
				System.err.println(exc);
				return FileVisitResult.CONTINUE;
			}
		});
	}


}
