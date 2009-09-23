/*
 * Created on 16-mar-2006
 *
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author S230298
 *
 */
public class FcsPagoApunteAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public FcsPagoApunteAdm(UsrBean usuario) {
		super(FcsPagoApunteBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoApunteBean.C_FECHAINICIO,			FcsPagoApunteBean.C_FECHAMODIFICACION,
							FcsPagoApunteBean.C_IDAPUNTE,				FcsPagoApunteBean.C_IDCALENDARIOGUARDIAS,
							FcsPagoApunteBean.C_IDFACTURACION,			FcsPagoApunteBean.C_IDGUARDIA,
							FcsPagoApunteBean.C_IDINSTITUCION,			FcsPagoApunteBean.C_IDPAGOSJG,
							FcsPagoApunteBean.C_IDPERSONA,				FcsPagoApunteBean.C_IDPERSONASOCIEDAD,
							FcsPagoApunteBean.C_IDTURNO,
							FcsPagoApunteBean.C_IMPORTEIRPF,
							FcsPagoApunteBean.C_IMPORTEPAGADO,
							FcsPagoApunteBean.C_PORCENTAJEIRPF,
							FcsPagoApunteBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] campos = {FcsPagoApunteBean.C_FECHAINICIO,			FcsPagoApunteBean.C_IDAPUNTE,
							FcsPagoApunteBean.C_IDCALENDARIOGUARDIAS,	FcsPagoApunteBean.C_IDFACTURACION,
							FcsPagoApunteBean.C_IDGUARDIA,				FcsPagoApunteBean.C_IDINSTITUCION,
							FcsPagoApunteBean.C_IDPAGOSJG,				FcsPagoApunteBean.C_IDPERSONA,
							FcsPagoApunteBean.C_IDTURNO};
		return campos;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoApunteBean bean = null;
		
		try {
			bean = new FcsPagoApunteBean();
			bean.setFechaInicio(UtilidadesHash.getString(hash,FcsPagoApunteBean.C_FECHAINICIO));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsPagoApunteBean.C_FECHAMODIFICACION));
			bean.setIdApunte(UtilidadesHash.getLong(hash,FcsPagoApunteBean.C_IDAPUNTE));
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_IDFACTURACION));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_IDGUARDIA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_IDINSTITUCION));
			bean.setIdPagosJG(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_IDPAGOSJG));
			bean.setIdPersona(UtilidadesHash.getLong(hash,FcsPagoApunteBean.C_IDPERSONA));
			bean.setIdPersonaSociedad(UtilidadesHash.getLong(hash,FcsPagoApunteBean.C_IDPERSONASOCIEDAD));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_IDTURNO));
			bean.setImporteIRPF(UtilidadesHash.getDouble(hash,FcsPagoApunteBean.C_IMPORTEIRPF));
			bean.setImportePagado(UtilidadesHash.getDouble(hash,FcsPagoApunteBean.C_IMPORTEPAGADO));
			bean.setPorcentajeIRPF(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_PORCENTAJEIRPF));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsPagoApunteBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean b) throws ClsExceptions {
		Hashtable htData = null;
		FcsPagoApunteBean bean = (FcsPagoApunteBean) b;
		try {
			htData = new Hashtable();			
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_FECHAINICIO,			bean.getFechaInicio());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_FECHAMODIFICACION,	bean.getFechaMod());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDAPUNTE,			bean.getIdApunte());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDCALENDARIOGUARDIAS,bean.getIdCalendarioGuardias());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDFACTURACION,		bean.getIdFacturacion());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDGUARDIA,			bean.getIdGuardia());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDINSTITUCION,		bean.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDPAGOSJG,			bean.getIdPagosJG());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDPERSONA,			bean.getIdPersona());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDPERSONASOCIEDAD,	bean.getIdPersonaSociedad());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IDTURNO,				bean.getIdTurno());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IMPORTEIRPF,			bean.getImporteIRPF());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_IMPORTEPAGADO,		bean.getImportePagado());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_PORCENTAJEIRPF,		bean.getPorcentajeIRPF());
			UtilidadesHash.set(htData, FcsPagoApunteBean.C_USUMODIFICACION,		bean.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
}
