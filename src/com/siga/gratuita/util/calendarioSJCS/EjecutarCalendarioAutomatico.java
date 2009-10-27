/*
 * Created on Mar 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;

/**
 * @author A203486
 *
 * Esta Clase permite probar la creacion del calendario automatico usando la Clase "CalendarioSJCS".
 * 
 * Los pasos a seguir para probar la creacion del calendario automatico son:
 * 
 * 1.- El metodo que calcula la matriz de periodos de dias de guardia obtrenido por metodos del CGAE es <br>
 * "calcularMatrizPeriodosDiasGuardia()" que a su vez usa la clase "CalendarioAutomatico".
 * 
 * 2.- Mediante el metodo "pintarCalendarioSJCS()" podemo ver las fechas de los periodos de guardia obtenidos.
 * 
 * 3.- Finalmente, gracias el metodo "calcularMatrizLetradosGuardia()" realizaremos la creacion del calendario <br>
 * automatico usando esa matriz de periodos de guardias.
 * 
 */
public class EjecutarCalendarioAutomatico {

	
	public static void ejecutarPruebaCalendario (UsrBean usr){
		Integer idInstitucion = new Integer("2032");
		Integer idTurno = new Integer("23");
		Integer idGuardia = new Integer("145");
		Integer idCalendarioGuardias = new Integer("1");
		UserTransaction tx = null;
		
		try {
			tx = usr.getTransaction();
			tx.begin();
			
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("--> INICIO: Ejecuta la prueba del Calendario <--",3);
			CalendarioSJCS calendarioSJCS = new CalendarioSJCS(idInstitucion,idTurno,idGuardia,idCalendarioGuardias, usr);
			
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("--> Obtiene la Matriz de Peridos de Dias de Guardia...",3);
			calendarioSJCS.calcularMatrizPeriodosDiasGuardia();
			
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("--> Datos del Calendario...",3);
			calendarioSJCS.pintarCalendarioSJCS();
			
			//Obtenemos los dias a Agrupar
			//ArrayList lDiasASeparar = new ArrayList();
			List lDiasASeparar = calendarioSJCS.getDiasASeparar(idInstitucion, idTurno, idGuardia, usr);
			
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("--> Obtengo la matriz de letrados de Guardia para los periodos calculados...",3);
			int salidaError = calendarioSJCS.calcularMatrizLetradosGuardia(lDiasASeparar);
			
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("--> Salida del resultado de ejecutar CalcularMatrizLetradosGuardia:",3);
			calendarioSJCS.pintarSalidaCalcularMatrizLetradosGuardia(salidaError);

			ClsLogging.writeFileLog("--> Salida del resultado de ejecutar pintarMatrizPeriodosLetrados:",3);
			calendarioSJCS.pintarMatrizPeriodosLetrados();
			
			ClsLogging.writeFileLog("--> FIN <--",3);
			ClsLogging.writeFileLog("",3);
			ClsLogging.writeFileLog("",3);

			//Controlo si debo hacer un rollback en caso de cualquier error:
			if (salidaError == 0)
				tx.commit();
			else
				tx.rollback();
		} catch (Exception e){
			ClsLogging.writeFileLog("ERROR en ejecutarPruebaCalendario.",3);
			try {
				tx.rollback();
				e.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();	
			}			
		}
	}
	
	public static void ejecutarExternoSIGA() {
		UsrBean usr = new UsrBean();
		usr.setLocation("2032");
		usr.setLanguageExt("ES");
		usr.setLanguage("1");
		
		EjecutarCalendarioAutomatico.ejecutarPruebaCalendario(usr);
	}
}
