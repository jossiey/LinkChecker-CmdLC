package osd;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class ExceptionTest {

  /*
   * test file missing
   */
  @Test
  public void testMissingFile() throws IOException {
    CmdLC cmdLC = new CmdLC();

    try {
      cmdLC.extractURL("");
      //	fail("Expected an Exception to be thrown");
    } catch (FileNotFoundException e) {
      assertEquals(e.getMessage(), "Not Found");
    } catch (IOException e) {
      assertEquals(e.getMessage(), "");
    }
  }

  @Test
  public void testFakeFile() throws IOException {
    CmdLC cmdLC = new CmdLC();

    try {
      cmdLC.extractURL("/fake");
      //	fail("Expected an Exception to be thrown");
    } catch (FileNotFoundException e) {
      assertEquals(e.getMessage(), "Not Found");
    } catch (IOException e) {
      assertEquals(e.getMessage(), "");
    }
  }
}
