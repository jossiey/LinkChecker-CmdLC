package osd;

import java.net.HttpURLConnection;

import picocli.CommandLine.Help.Ansi;

public class Print {

  // extract a jsonPrint function
  public void jsonPrint(String link, int responseCode) {
    System.out.println("{url: '" + link + "' , status: '" + responseCode + "' },");
  }

  // extract a colorPrint function
  public void colorPrint(String link, int responseCode) {
    String str = null;
    if (responseCode == HttpURLConnection.HTTP_OK) {
      str = "@|green " + "[" + responseCode + "]" + " GOOD     " + link + " |@";
    } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM
        || responseCode == 307
        || responseCode == 308) {
      str = "@|yellow " + "[" + responseCode + "]" + " REDIRECT " + link + " |@";
    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND
        || responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
      str = "@|red " + "[" + responseCode + "]" + " BAD      " + link + " |@";
    } else {
      str = "@|237 " + "[" + responseCode + "]" + " UNKNOWN  " + link + " |@";
    }
    System.out.println(Ansi.AUTO.string(str));
  }
}
