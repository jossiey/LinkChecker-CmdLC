package osd;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Help.Ansi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

	@Option(names = { "-u", "--url", "/u", "\\u" }, paramLabel = "URL", description = "input URL argument")
	boolean url;

	@Option(names = { "-j", "--json", "/j", "\\j" }, paramLabel = "JSON", description = "output JSON format")
	boolean JSON;



	public static void main(String[] args) {

		int exitCode = new CommandLine(new CmdLC()).execute(args);
		System.out.println("\nProgram exited with code " + exitCode +".");
		System.exit(exitCode);
	}


	@Override
	public Integer call() throws FileNotFoundException, IOException {

		int[] badLink = new int[1];       badLink[0] = 0;             //boolean badLink = false;

		ArrayList<String> files = new ArrayList<String>();

		try {

			for(String arg : args) {

				if(url) {
					checkWebsite(arg, badLink);
				}
				else {				
					//invoke visitFileRecursive method
					files.addAll(visitFileRecursive(arg));
				}
			}

			for(String file: files) {


				System.out.println("\nFinding and checking file \"" + file.toString() + "\" now ...");	

				//extract url from a file
				HashSet<String> links = extractURL(file.toString());		

				if(JSON) {
					System.out.println("JSON output format");
					System.out.println("[ ");

					//looping to test each url 
					for(String url:links) {
						urlTest(url, badLink);
					}

					System.out.println(" ]");
				}

				else {
					//looping to test each url 
					for(String url: links) {
						urlTest(url, badLink);
					}
				}

			}


		}catch(FileNotFoundException ex) {

		}catch(IOException ex) {

		}

		int exit_code = badLink[0] == 1 ? 1 : 0;

		return exit_code;
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

		}catch(IOException ex) {

		}
		return links;
	}




	//check url is valid or invalid
	public void urlTest(String link, int[] badLink) throws MalformedURLException {

		try {
			URL url = new URL(link); 		

			//URL connect and response
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			int responseCode = huc.getResponseCode();

			//	System.out.println(huc.getHeaderFields());

			//set redirect
			huc.setInstanceFollowRedirects(true);	


			//print out with different format
			if(JSON) 				   
				JsonPrint(link, responseCode);
			else {
				colorPrint(link, responseCode);
				if(responseCode != HttpURLConnection.HTTP_OK) {   //200	
					badLink[0] = 1;
				}			
			}

		}catch(MalformedURLException ex) {

			String str = "@|237 " + "      UNKNOWN  " +  link + " |@";						
			System.out.println(Ansi.AUTO.string(str));	
			badLink[0] = 1;

		}catch(IOException ex) {

			String str = "@|237 " + "      UNKNOWN  " + link + " |@";						
			System.out.println(Ansi.AUTO.string(str));		
			badLink[0] = 1;
		}
	}


	//recursively visit the directory/files and subfiles
	public ArrayList<String> visitFileRecursive(String path) throws IOException {

		ArrayList<String> files = new ArrayList<String>();

		Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				files.add(file.toString());		
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException{
				System.err.println(exc);
				return FileVisitResult.CONTINUE;
			}
		});

		return files;
	}


	//extract a JSONprint function
	public void JsonPrint(String link, int responseCode){
		System.out.println("{url: '" + link + "' , status: '" + responseCode + "' }," );
	}


	//extract a colorPrint function
	public void colorPrint(String link, int responseCode){
		String str = null;
		if(responseCode == HttpURLConnection.HTTP_OK) {
			str = "@|green " + "[" + responseCode + "]" + " GOOD     " + link + " |@";	
		}
		else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == 307 || responseCode == 308) {
			str = "@|yellow " + "[" + responseCode + "]" + " REDIRECT " + link + " |@";	    		
		}
		else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND || responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
			str  = "@|red " + "[" + responseCode + "]" + " BAD      "  + link + " |@";		  		
		}
		else {
			str = "@|237 " + "[" + responseCode + "]" + " UNKNOWN  " +  link + " |@";	
		} 	
		System.out.println(Ansi.AUTO.string(str));
	}


	public String argUrl(String link) {

		StringBuilder content = new StringBuilder();

		try
		{
			// create a url object
			URL url = new URL(link);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null)
			{
				content.append(line + "\n");
			}
			bufferedReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return content.toString();		
	}


	public void checkWebsite(String link, int[] badLink) throws MalformedURLException {
		String content = argUrl(link);

		//save the urls from the file, avoiding duplication
		HashSet<String> links = new HashSet<String> ();

		//regular expression
		String urlRegex = "(https?)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
		Pattern pattern = Pattern.compile(urlRegex);
		Matcher matcher = pattern.matcher(content);

		while(matcher.find()) {
			links.add(matcher.group());
		}

		for(String url:links) {
			urlTest(url, badLink);
		}
	}
}
