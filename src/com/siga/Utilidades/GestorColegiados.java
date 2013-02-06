/*
 * Created on 23-ene-2013
 *
 */
package com.siga.Utilidades;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsGestionAutomaticaLog;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;


/**
 * @author aalg. inc 504
 */

/**
 * Clase GestorColegiados
 * <BR>
 * Realiza automaticamente las gestiones necesarias de los colegiados
 */

public class GestorColegiados 
{

	/** 
	 * aalg:
	 * Gestiona automaticamente la actualización del estado (ejerciente o no ejerciente) según los estados y la fecha actual
	 * @param  institucion - identificador institucion
	 * @param  usuario - userBean involucrado 
	 * @return  boolean - si se ha llevado a cabo la operacion  
	 */	
	public boolean GestionAutomaticaColegiados () throws ClsExceptions {
		
		boolean resultado=true;
		boolean correcto;
		
		UsrBean userBean = UsrBean.UsrBeanAutomatico(Integer.toString(ClsConstants.INSTITUCION_CGAE));
		
		try{						
			// Gestion de solicitudes de DATOS GENERALES 
			CenColegiadoAdm adminDB = new CenColegiadoAdm(userBean);
			correcto=adminDB.calcularSituacionEjercicio();
			if (correcto){							
				ClsGestionAutomaticaLog.insertar("Realizada la actualización de SITUACIONEJERCICIO del colegiado");
			}
			else{
				ClsGestionAutomaticaLog.insertar("Fallo en la actualización de SITUACIONEJERCICIO del colegiado");
			}
						
		}
		catch(Exception ee){
			ClsLogging.writeFileLogError("Error en GestionAutomaticaColegiados -> " + ee.getMessage(),ee,3);
			throw new ClsExceptions(ee,"Error en GestionAutomaticaColegiados -> " + ee.getMessage());		
		}		
		return resultado;
	}
	
}
