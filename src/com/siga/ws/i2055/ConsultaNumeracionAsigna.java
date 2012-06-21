package com.siga.ws.i2055;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.EcomColaAdm;
import com.siga.beans.EcomColaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;

public class ConsultaNumeracionAsigna {
	
	public void obtenerNumeracion(UsrBean usrBean, Integer idInstitucion, Integer anio,	Integer numero, String idTipoEJG) throws ClsExceptions {
		EcomColaBean ecomColaBean = new EcomColaBean();		
		ecomColaBean.setIdInstitucion(idInstitucion);
		ecomColaBean.setIdOperacion(EcomColaBean.OPERACION.ASIGNA_CONSULTA_NUMERO.getId());		
		
		EcomColaAdm ecomColaAdm = new EcomColaAdm(usrBean);
		if (!ecomColaAdm.insert(ecomColaBean)) {
			throw new ClsExceptions("No se ha podido insertar en la cola de comunicaciones.");
		}
		ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usrBean);
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put(ScsEJGBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ScsEJGBean.C_ANIO, anio);
		hash.put(ScsEJGBean.C_NUMERO, numero);
		hash.put(ScsEJGBean.C_IDTIPOEJG, idTipoEJG);
		Vector v = scsEJGAdm.selectByPK(hash);
		if (v == null || v.size() != 1) {
			throw new ClsExceptions("No se ha encontrado el EJG idInstitucion-anio-numero-idTipoEJG=" + idInstitucion + "-" + anio + "-" + numero + "-" + idTipoEJG);
		}
		ScsEJGBean scsEJGBean = (ScsEJGBean) v.get(0);
		scsEJGBean.setIdEcomCola(ecomColaBean.getIdEcomCola());
		if (!scsEJGAdm.update(scsEJGBean)) {
			throw new ClsExceptions("No se ha actualizado el ejg " + scsEJGBean.getAnio() + "/" + scsEJGBean.getNumEJG());			
		}
	}
}
