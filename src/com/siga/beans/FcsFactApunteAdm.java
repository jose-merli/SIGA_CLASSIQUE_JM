package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsFactApunteAdm extends MasterBeanAdministrador {

	
	public FcsFactApunteAdm(UsrBean usuario) {
		super(FcsFactApunteBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactApunteBean.C_FECHAINICIO,			FcsFactApunteBean.C_FECHAMODIFICACION,
							FcsFactApunteBean.C_IDAPUNTE,				
							FcsFactApunteBean.C_IDFACTURACION,			FcsFactApunteBean.C_IDGUARDIA,
							FcsFactApunteBean.C_IDHITO,					FcsFactApunteBean.C_IDINSTITUCION,
							FcsFactApunteBean.C_IDPERSONA,				FcsFactApunteBean.C_IDTURNO,
							FcsFactApunteBean.C_MOTIVO,					FcsFactApunteBean.C_NUMACTUACIONES,
							FcsFactApunteBean.C_NUMACTUACIONESTOTAL,	FcsFactApunteBean.C_NUMASISTENCIAS,
							FcsFactApunteBean.C_NUMASISTENCIASTOTAL,	FcsFactApunteBean.C_PRECIOAPLICADO,
							FcsFactApunteBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactApunteBean.C_IDINSTITUCION,		FcsFactApunteBean.C_IDFACTURACION,
				FcsFactApunteBean.C_IDAPUNTE};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactApunteBean bean = null;
		
		try {
			bean = new FcsFactApunteBean();
			bean.setFechaInicio			(UtilidadesHash.getString(hash,FcsFactApunteBean.C_FECHAINICIO));
			bean.setFechaMod			(UtilidadesHash.getString(hash,FcsFactApunteBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion		(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_IDFACTURACION));
			bean.setIdGuardia			(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_IDGUARDIA));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getLong(hash,FcsFactApunteBean.C_IDPERSONA));
			bean.setIdTurno				(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_IDTURNO));
			bean.setPrecioAplicado		(UtilidadesHash.getDouble(hash,FcsFactApunteBean.C_PRECIOAPLICADO));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_USUMODIFICACION));
			bean.setIdApunte			(UtilidadesHash.getLong(hash,FcsFactApunteBean.C_IDAPUNTE));
			bean.setIdHito				(UtilidadesHash.getLong(hash,FcsFactApunteBean.C_IDHITO));
			bean.setMotivo				(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_MOTIVO));
			bean.setNumActuaciones		(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_NUMACTUACIONES));
			bean.setNumActuacionesTotal	(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_NUMACTUACIONESTOTAL));
			bean.setNumAsistencias		(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_NUMASISTENCIAS));
			bean.setNumAsistenciasTotal	(UtilidadesHash.getInteger(hash,FcsFactApunteBean.C_NUMASISTENCIASTOTAL));
			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsFactApunteBean b = (FcsFactApunteBean) bean;
			UtilidadesHash.set(htData, FcsFactApunteBean.C_FECHAINICIO, b.getFechaInicio().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDGUARDIA, b.getIdGuardia().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDTURNO, b.getIdTurno().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDAPUNTE, b.getIdApunte().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_MOTIVO, b.getMotivo().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_IDHITO, b.getIdHito().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_NUMACTUACIONES, b.getNumActuaciones().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_NUMACTUACIONESTOTAL, b.getNumActuacionesTotal().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_NUMASISTENCIAS, b.getNumAsistencias().toString());
			UtilidadesHash.set(htData, FcsFactApunteBean.C_NUMASISTENCIASTOTAL, b.getNumAsistenciasTotal().toString());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsFactGuardiasColegiadoAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	
	public boolean exiteApunteGuardia(String idinstitucion,String idturno,String idguardia,String idpersona, String fInicio){
		String sql = "";	
		Vector v = new Vector();
		boolean salida = false;

		//fechaInicio = "TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";

	    Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idinstitucion);
	    codigos.put(new Integer(2),idturno);
	    codigos.put(new Integer(3),idguardia);
	    codigos.put(new Integer(4),idpersona);
	    codigos.put(new Integer(5),fInicio);
	    
		sql = "select 1 from fcs_fact_apunte ap ";
		sql+= " where ap.idinstitucion=:1 and ap.idturno=:2 and ap.idguardia=:3 and ap.idpersona=:4 and ap.fechainicio=:5";

		try {
			v = this.selectGenericoBind(sql,codigos);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
					
		if (v.size() > 0)
			salida = true;
		
		return salida;
	}
	
	
}
