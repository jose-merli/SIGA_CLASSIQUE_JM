package com.siga.servlets;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;

import com.atos.utils.ClsLogging;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

/**
 * Servlet del proceso automatico de facturacion. Utiliza a su vez al servlet SIGASvlProcesoFacturacion
 */
public class SIGASvlProcesoAutomaticoFacturacion extends SIGAServletAdapter
{
	private static final long serialVersionUID = 1L;
	private static String sNombreProceso = "ProcesoAutomaticoFacturacion";
	
	/**
	 * Inicializacion del servidor: se ejecuta cuando se inicia/arranca el servidor
	 */
	public void init() throws ServletException
	{
		super.init();

		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

		iniciarTimer();
		
		ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
	}

	/**
	 * Reinicia el timer para que se vuelva a ejecutar el proceso automatico de facturacion pasado un tiempo.
	 * El tiempo se define por una propiedad interna, que es modificable por BD.
	 * 
	 * Se ejecuta al inicio del servidor y cada vez que termina cada pasada del proceso (SIGASvlProcesoFacturacion)
	 * @throws ServletException
	 */
	public void iniciarTimer() throws ServletException
	{
		ReadProperties properties = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sIntervalo = properties.returnProperty("facturacion.programacionAutomatica.tiempo.ciclo");

		if (sIntervalo == null || sIntervalo.trim().equals("") || sIntervalo.trim().equals("0")) {
			ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
			ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervalo + ").", 3);

		} else {
			long lIntervalo = Long.parseLong(sIntervalo) * 60 * 1000;
			Timer timer = new Timer();
			timer.schedule(tarea, lIntervalo);

			ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
			ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " minuto(s).", 3);
		}
	}

	private TimerTask tarea = new TimerTask () {
		/**
		 * Este metodo se ejecuta cuando el timer termina y lanza este proceso de nuevo.
		 * Solo sirve para dejar registro en LOG.
		 * 
		 * Nota: no se puede utilizar para reiniciar el timer, ya que no sabemos cuando termina el proceso
		 */
		public void run() {
			ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + ".", 3);
	
			try {
				// invocando al servlet
				ReadProperties properties = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String urlSiga = properties.returnProperty("general.urlSIGA");
				URL url = new URL(urlSiga + "SIGASvlProcesoFacturacion.svrl");
				url.getContent();
				
				// registrando el exito en la invocacion
				ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
	
			} catch (Exception e) {
				// registrando el error en la invocacion
				ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
				e.printStackTrace();
			}
		}
	};

	/**
	 * Finalizacion del servlet: se ejecuta cuando se termina/apaga el servidor
	 */
	public void destroy()
	{
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

		ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
		ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
	}
}
