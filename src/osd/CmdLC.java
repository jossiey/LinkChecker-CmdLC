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
import java.nio.file.Paths;
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
		description = "Finding and reporting dead links in a file along with " + 
				"a list showing good URL with green, bad URL with red," +
				" redirect URL with yellow, others with unknown"
		)

public class CmdLC implements Callable<Integer> { 

//	@Parameters(index = "0", description = "The file which contains URLs need to be checked")
	@Parameters( /* file name */ description = "The files which contains URLs need to be checked")
	private ArrayList<String> files;

	public static void main(String[] args) {

		int exitCode = new CommandLine(new CmdLC()).execute(args);
		System.exit(exitCode);
	}


	//to save the url from the file, avoiding duplication
	private HashSet<String> links = new HashSet<String> ();


	//extract url from a file
	public void extractURL(String file) throws FileNotFoundException, IOException{

		try{

			String content = new String(Files.readAllBytes(Paths.get(file)));	      

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
	}


	int counter = 0, total = 0;

	//check url is valid or invalid
	public void urlTest(String link) throws MalformedURLException {

		try {
			URL url = new URL(link); 
			total++;		

			//	url.openStream().close();      // == openConnection().getInputStream()  read content of the website

			HttpURLConnection huc = (HttpURLConnection) url.openConnection();

			int responseCode = huc.getResponseCode();

			if(responseCode == 200) {   //HTTP_OK				
				counter++;  			

				String str = "@|green " + "[" + responseCode + "]" + " GOOD     " + link + " |@";		
				System.out.println(Ansi.AUTO.string(str));				

			}

			else if(responseCode >= 300 || responseCode < 400)	 {   //301 Moved Permanently 									

				String str = "@|yellow " + "[" + responseCode + "]" + " REDIRECT"
						+ " " + link + " |@";						
				System.out.println(Ansi.AUTO.string(str));		
			}

			else if(responseCode == 404 || responseCode == 400 ) {   //400 File not found									

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


	@Override
	public Integer call() throws FileNotFoundException, IOException {

		try {
			
			for(String file : files) {
				
			System.out.println("\nFinding and checking file \"" + file + "\" now ...");	
			//extract url from a file
			extractURL(file);
			
			//looping to test each url 
			for(String url: links) {
				urlTest(url);
			}
			
			//summary
			System.out.printf("Total valid URLs are: %d\nTotal checked URLs are: %d\n", counter, total);
			counter = 0; 
			total =0;
			}
		
		}catch(FileNotFoundException ex) {
			// System.out.println(ex);
		}catch(IOException ex) {
			// System.out.println(ex);
		}

		//summary
		//System.out.printf("Total valid URLs are: %d\nTotal checked URLs are: %d\n", counter, total);

		return 0;
	}


}
