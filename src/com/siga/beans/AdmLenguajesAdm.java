package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
//import com.siga.general.SIGAException;
import com.siga.general.SIGAException;

public class AdmLenguajesAdm extends MasterBeanAdministrador
{
	public AdmLenguajesAdm(UsrBean usuario)
	{
	    super(AdmLenguajesBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmLenguajesBean.C_IDLENGUAJE,
						   AdmLenguajesBean.C_DESCRIPCION,
						   AdmLenguajesBean.C_CODIGOEXT};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmLenguajesBean.C_IDLENGUAJE};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmLenguajesBean.C_IDLENGUAJE,
     		   			   AdmLenguajesBean.C_DESCRIPCION,
     		   			   AdmLenguajesBean.C_CODIGOEXT};
		
		return campos;
    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		AdmLenguajesBean bean = null;

		try
		{
			bean = new AdmLenguajesBean();
			
			bean.setIdLenguaje(UtilidadesHash.getString(hash, AdmLenguajesBean.C_IDLENGUAJE));
			bean.setDescripcion(UtilidadesHash.getString(hash, AdmLenguajesBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, AdmLenguajesBean.C_CODIGOEXT));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			AdmLenguajesBean b = (AdmLenguajesBean) bean;

			UtilidadesHash.set(htData, AdmLenguajesBean.C_IDLENGUAJE, b.getIdLenguaje());
			UtilidadesHash.set(htData, AdmLenguajesBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, AdmLenguajesBean.C_CODIGOEXT, b.getCodigoExt());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return null;
    }
    
	public String getLenguajeExt (String idLenguaje) throws SIGAException
	{
		String salida ="ES";
		try {
			
			Hashtable datos = new Hashtable();
			if(idLenguaje!=null)
				datos.put("IDLENGUAJE",idLenguaje);
			else
				return salida;
			
			Vector v = this.select(datos);
			if (v!=null && v.size()>0) {
				AdmLenguajesBean b = (AdmLenguajesBean) v.get(0);
				salida = b.getCodigoExt();
			}
			return salida;
		}
		catch (Exception e) {
			throw new SIGAException(e);
		}
	}
    
	public AdmLenguajesBean getLenguajeInstitucion (String idInstitucion) throws Exception
	{
	    AdmLenguajesBean salida =null;
		try {
			
			Hashtable datos = new Hashtable();
			datos.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
			CenInstitucionAdm admIns = new CenInstitucionAdm(this.usrbean);
			Vector v = admIns.selectByPK(datos);
			if (v!=null && v.size()>0) {
				CenInstitucionBean b = (CenInstitucionBean) v.get(0);
				Hashtable datos2 = new Hashtable();
				datos2.put(AdmLenguajesBean.C_IDLENGUAJE,b.getIdLenguaje());
				Vector v2 = this.selectByPK(datos2);
				if (v2!=null && v2.size()>0) {
					salida = (AdmLenguajesBean) v2.get(0);
				}
			}
			return salida;
		}
		catch (Exception e) {
			throw e;
		}
	}
    
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + AdmLenguajesBean.C_IDLENGUAJE + ", " + 
								      UtilidadesMultidioma.getCampoMultidioma(AdmLenguajesBean.C_DESCRIPCION, this.usrbean.getLanguage()) +" , " +
									  AdmLenguajesBean.C_CODIGOEXT +", " +
									  AdmLenguajesBean.C_FECHAMODIFICACION +", " +
									  AdmLenguajesBean.C_USUMODIFICACION +
						   " FROM " + AdmLenguajesBean.T_NOMBRETABLA + " ";
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
			String sql = " SELECT " + AdmLenguajesBean.C_IDLENGUAJE + ", " + 
		      UtilidadesMultidioma.getCampoMultidioma(AdmLenguajesBean.C_DESCRIPCION, this.usrbean.getLanguage()) +" , " +
			  AdmLenguajesBean.C_CODIGOEXT +", " +
			  AdmLenguajesBean.C_FECHAMODIFICACION +", " +
			  AdmLenguajesBean.C_USUMODIFICACION +
			  " FROM " + AdmLenguajesBean.T_NOMBRETABLA + " ";
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

	public String getCodigoExt(String codigoInterno) throws ClsExceptions 
	{
		try {
			String salida = "";
			Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),codigoInterno);
    		Vector v = this.selectBind(" WHERE IDLENGUAJE=:1" ,codigos);
			if (v!=null && v.size()>0) {
			    AdmLenguajesBean bean = (AdmLenguajesBean) v.get(0);
			    salida = bean.getCodigoExt();
			}
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	
}