import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;


public class PutoTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		try {
			prueba1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void prueba1() throws IOException{
		System.setProperty("javax.net.ssl.keyStore", "C:\\bea92\\jrockit150_10\\jre\\bin\\SIGA.pfx");
		System.setProperty("javax.net.ssl.keyStorePassword", "1111");
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.trustStore", "C:\\bea92\\jrockit150_10\\jre\\bin\\keystore");
		System.setProperty("javax.net.ssl.trustStorePassword", "keystore");
		System.setProperty("javax.net.debug", "ssl" );
//		System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl" );
//		Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider() );
		
	    String httpsURL = "https://demo.redabogacia.org/ecos/wsecos/services/ServiciosECOSService.service";
//	    httpsURL = "http://www.redabogacia.org";

	    URL myurl = new URL(httpsURL);
	    java.net.URLConnection con = (java.net.URLConnection)myurl.openConnection();
	    InputStream ins = con.getInputStream();

	    InputStreamReader isr=new InputStreamReader(ins);
	    BufferedReader in =new BufferedReader(isr);

	    String inputLine;

	    while ((inputLine = in.readLine()) != null)
	        System.out.println(inputLine);

	    in.close();
		
	}

}
