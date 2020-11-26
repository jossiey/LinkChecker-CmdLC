package osd;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PrintTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @After
  public void tearDown() {
    System.setOut(standardOut);
  }

  @Test
  public void testJsonPrint() {

    /*
     *  test one string link and one integer responseCode being passed in
     */
    String expected = "{url: 'https://www.senecacollege.ca' , status: '200' },";
    Print print = new Print();
    print.jsonPrint("https://www.senecacollege.ca", 200);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  /*
   *  test response code equals 200
   */
  @Test
  public void colorPrint_good() {

    String expected = "[200] GOOD     https://www.senecacollege.ca";
    Print print = new Print();
    print.colorPrint("https://www.senecacollege.ca", 200);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  /*
   *  test response code equals 301, 307, 308
   */
  @Test
  public void colorPrint_redirect_301() {
    String expected = "[301] REDIRECT https://www.senecacollege.ca";
    Print print = new Print();
    print.colorPrint("https://www.senecacollege.ca", 301);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  @Test
  public void colorPrint_redirect_307() {
    String expected = "[307] REDIRECT https://www.senecacollege.ca";
    Print print = new Print();
    print.colorPrint("https://www.senecacollege.ca", 307);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  @Test
  public void colorPrint_redirect_308() {
    String expected = "[308] REDIRECT https://www.senecacollege.ca";
    Print print = new Print();
    print.colorPrint("https://www.senecacollege.ca", 308);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  /*
   *  test response code equals 400, 404
   */
  @Test
  public void colorPrint_bad_400() {
    String expected = "[400] BAD      https://www.googletagmanager.com/gtm.js?id=";
    Print print = new Print();
    print.colorPrint("https://www.googletagmanager.com/gtm.js?id=", 400);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  @Test
  public void colorPrint_bad_404() {
    String expected = "[404] BAD      https://www.googletagmanager.com/gtm.js?id=";
    Print print = new Print();
    print.colorPrint("https://www.googletagmanager.com/gtm.js?id=", 404);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }

  /*
   *  test response code equals unknown 0, 999
   */
  @Test
  public void colorPrint_unknown() {
    String expected = "[999] UNKNOWN  https://www.linkedin.com/school/seneca-college/";
    Print print = new Print();
    print.colorPrint("https://www.linkedin.com/school/seneca-college/", 999);
    assertEquals(expected, outputStreamCaptor.toString().trim());
  }
}
