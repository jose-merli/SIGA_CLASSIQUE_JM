package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
//import com.siga.general.SIGAException;

public class CenTipoSociedadAdm extends MasterBeanAdministrador
{
	public CenTipoSociedadAdm(UsrBean usuario)
	{
	    super(CenTipoSociedadBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CenTipoSociedadBean.C_DESCRIPCION,CenTipoSociedadBean.C_CODIGOEXT,
				CenTipoSociedadBean.C_LETRACIF};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {  CenTipoSociedadBean.C_LETRACIF};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {CenTipoSociedadBean.C_LETRACIF,
				CenTipoSociedadBean.C_DESCRIPCION,CenTipoSociedadBean.C_CODIGOEXT,
							CenTipoSociedadBean.C_FECHAMODIFICACION, 
							CenTipoSociedadBean.C_USUMODIFICACION};
		
		return campos;

    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		CenTipoSociedadBean bean = null;

		try
		{
			bean = new CenTipoSociedadBean();
			
			bean.setCodigoExt(UtilidadesHash.getString(hash, CenTipoSociedadBean.C_CODIGOEXT));
			bean.setDescripcion(UtilidadesHash.getString(hash, CenTipoSociedadBean.C_DESCRIPCION));
			bean.setLetraCif(UtilidadesHash.getString(hash, CenTipoSociedadBean.C_LETRACIF));
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

			CenTipoSociedadBean b = (CenTipoSociedadBean) bean;

			UtilidadesHash.set(htData, CenTipoSociedadBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, CenTipoSociedadBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CenTipoSociedadBean.C_LETRACIF, b.getLetraCif());
			
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
        String[] ordenCampos = {CenTipoSociedadBean.C_LETRACIF,CenTipoSociedadBean.C_DESCRIPCION};
        
        return ordenCampos;
    }
    
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			//String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			String sql=" SELECT "+CenTipoSociedadBean.C_CODIGOEXT+", "+
			            UtilidadesMultidioma.getCampoMultidioma(CenTipoSociedadBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+
						CenTipoSociedadBean.C_FECHAMODIFICACION+", "+
						CenTipoSociedadBean.C_LETRACIF+", "+
						CenTipoSociedadBean.C_USUMODIFICACION+
						" FROM "+CenTipoSociedadBean.T_NOMBRETABLA;
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
			String sql=" SELECT "+CenTipoSociedadBean.C_CODIGOEXT+", "+
            UtilidadesMultidioma.getCampoMultidioma(CenTipoSociedadBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+
			CenTipoSociedadBean.C_FECHAMODIFICACION+", "+
			CenTipoSociedadBean.C_LETRACIF+", "+
			CenTipoSociedadBean.C_USUMODIFICACION+
			" FROM "+CenTipoSociedadBean.T_NOMBRETABLA;
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

