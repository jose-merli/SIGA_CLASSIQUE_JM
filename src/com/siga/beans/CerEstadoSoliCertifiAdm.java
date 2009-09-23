package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class CerEstadoSoliCertifiAdm extends MasterBeanAdministrador
{
	public CerEstadoSoliCertifiAdm(UsrBean usuario)
	{
	    super(CerEstadoSoliCertifiBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO,
		        		   CerEstadoSoliCertifiBean.C_DESCRIPCION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerEstadoSoliCertifiBean bean = null;

		try
		{
			bean = new CerEstadoSoliCertifiBean();

			bean.setIdEstadoSolicitudCertificado(UtilidadesHash.getInteger(hash, CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CerEstadoSoliCertifiBean.C_DESCRIPCION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del Hashtable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			CerEstadoSoliCertifiBean b = (CerEstadoSoliCertifiBean) bean;

			UtilidadesHash.set(htData, CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO, b.getIdEstadoSolicitudCertificado());
			UtilidadesHash.set(htData, CerEstadoSoliCertifiBean.C_DESCRIPCION, b.getDescripcion());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el Hashtable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return null;
    }
    
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CerEstadoSoliCertifiBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
						 			  CerEstadoSoliCertifiBean.C_FECHAMODIFICACION + ", " +
						 			  CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO + ", " +
						 			  CerEstadoSoliCertifiBean.C_USUMODIFICACION + 						 			
						   " FROM " + CerEstadoSoliCertifiBean.T_NOMBRETABLA + " ";
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}

	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CerEstadoSoliCertifiBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
			  CerEstadoSoliCertifiBean.C_FECHAMODIFICACION + ", " +
			  CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO + ", " +
			  CerEstadoSoliCertifiBean.C_USUMODIFICACION + 						 			
			  " FROM " + CerEstadoSoliCertifiBean.T_NOMBRETABLA + " ";
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}    
}