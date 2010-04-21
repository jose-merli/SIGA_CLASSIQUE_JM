import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;

import org.apache.axis.AxisFault;

import service.ServiciosECOS.ServiciosECOSServiceSOAPStub;
import service.ServiciosECOS.ServiciosECOSService_ServiceLocator;

import com.atos.utils.ClsLogging;
import com.ecos.ws.solicitarEnvio.DatosGrandeCuentaTO;
import com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio;
import com.ecos.ws.solicitarEnvio.SolicitudEnvioSMS;


public class PruebaEnviosms {
	 static String url_service = "http://10.60.3.80:7001/ecos/wsecos/services/ServiciosECOSService.service";
	
	public static void main (String[] args0){
		SolicitudEnvioSMS sesms01 = new SolicitudEnvioSMS();
		sesms01.setIdClienteECOS("SIGA");
		sesms01.setIdColegio("2040");
		sesms01.setListaTOs(new String[] {"(+34)610067061"});
		sesms01.setTexto("Esto es una prueba");
		sesms01.setIsProgramado(false);
		sesms01.setIsSMSCertificado(false);
		DatosGrandeCuentaTO datos = new DatosGrandeCuentaTO();
		sesms01.setDatosGrandeCuenta(datos);
		
	
		ServiciosECOSService_ServiceLocator locator = new ServiciosECOSService_ServiceLocator();
		ServiciosECOSServiceSOAPStub stub = null;
		try {
			stub = new ServiciosECOSServiceSOAPStub(new URL(url_service),locator);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			ResultadoSolicitudEnvio response03 = stub.enviarSMS(sesms01);
			response03.toString();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
