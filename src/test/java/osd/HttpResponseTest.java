package osd;

import static org.junit.Assert.assertEquals;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/** Unit test for simple App. */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("jdk.internal.reflect.*")
@PrepareForTest({URL.class, HttpURLConnection.class})
public class HttpResponseTest {
  @Mock URL u;
  @Mock HttpURLConnection huc;
  @Mock String url;
  @Mock CmdLC cmdLC;

  @Before
  public void setup() throws Exception {
    url = "http://www.google.com";
    u = PowerMockito.mock(URL.class);
    PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
    huc = PowerMockito.mock(HttpURLConnection.class);
    PowerMockito.when(u.openConnection()).thenReturn(huc);
  }

  @Test
  public void goodLink() throws Exception {

    PowerMockito.when(cmdLC.urlTest(url)).thenReturn(200);

     CmdLC cmd =new CmdLC();
    assertEquals(200, cmd.urlTest(url));
  }

  @Test
  public void badLink() throws Exception {

    url = "http://google.com/h";
    PowerMockito.when(cmdLC.urlTest(url)).thenReturn(404);

    //  CmdLC cmd =new CmdLC();
    assertEquals(404, cmdLC.urlTest(url));
  }

  @Test
  public void testException() throws Exception {

    CmdLC cmd = new CmdLC();
    assertEquals(0, cmd.urlTest(""));
  }
}
