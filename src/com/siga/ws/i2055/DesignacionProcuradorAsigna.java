package com.siga.ws.i2055;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.EcomColaAdm;
import com.siga.beans.EcomColaBean;

public class DesignacionProcuradorAsigna {
	
	public void obtenerDesignaciones(UsrBean usrBean, Integer idInstitucion) throws ClsExceptions {
		
		EcomColaBean ecomColaBean = new EcomColaBean();		
		ecomColaBean.setIdInstitucion(idInstitucion);
		ecomColaBean.setIdOperacion(EcomColaBean.OPERACION.ASIGNA_OBTENER_PROCURADOR.getId());		
		
		EcomColaAdm ecomColaAdm = new EcomColaAdm(usrBean);
		if (!ecomColaAdm.insert(ecomColaBean)) {
			throw new ClsExceptions("No se ha podido insertar en la cola de comunicaciones.");
		}
	}
}
