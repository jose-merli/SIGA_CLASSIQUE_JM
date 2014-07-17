/*
 * VERSIONES:
 * MJM 15/07/2014 Creación
 */
package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

/**
 *  Administrador de la tabla FAC_PROPOSITOS
 *  @author MJM
 */
public class FacPropositosAdm extends MasterBeanAdministrador
{
	public FacPropositosAdm(UsrBean usuario)
	{
	    super(FacPropositosBean.T_NOMBRETABLA, usuario);
	}


	protected String[] getCamposBean()
	{
		String[] campos = {FacPropositosBean.C_IDPROPOSITO,
							FacPropositosBean.C_CODIGO,
							FacPropositosBean.C_TIPOSEPA,
							FacPropositosBean.C_NOMBRE};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {FacPropositosBean.C_IDPROPOSITO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		FacPropositosBean bean = null;

		try
		{
			bean = new FacPropositosBean();
			
			bean.setIdProposito(UtilidadesHash.getInteger(hash,FacPropositosBean.C_IDPROPOSITO));
			bean.setCodigo(UtilidadesHash.getString(hash,FacPropositosBean.C_CODIGO));
			bean.setTipoSEPA(UtilidadesHash.getInteger(hash,FacPropositosBean.C_TIPOSEPA));
			bean.setNombre(UtilidadesHash.getString(hash,FacPropositosBean.C_NOMBRE));
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

			FacPropositosBean b = (FacPropositosBean) bean;

			UtilidadesHash.set(htData, FacPropositosBean.C_IDPROPOSITO, b.getIdProposito());
			UtilidadesHash.set(htData, FacPropositosBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(htData, FacPropositosBean.C_TIPOSEPA, b.getTipoSEPA());
			UtilidadesHash.set(htData, FacPropositosBean.C_NOMBRE, b.getNombre());
			
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
		String[] orden = {FacPropositosBean.C_NOMBRE};

		return orden;
    }
    
    public Vector selectPropositos() throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT "+ FacPropositosBean.C_IDPROPOSITO + "," + FacPropositosBean.C_CODIGO + "," + FacPropositosBean.C_TIPOSEPA + "," +
			UtilidadesMultidioma.getCampoMultidioma(FacPropositosBean.C_NOMBRE,this.usrbean.getLanguage()) +
			 " FROM "+FacPropositosBean.T_NOMBRETABLA;
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
    
    
    public Integer selectIdPropositoPorCodigo(String codigo) throws ClsExceptions 
	{
		
		Integer id=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT IDPROPOSITO FROM " + FacPropositosBean.T_NOMBRETABLA +
				" WHERE CODIGO = '" + codigo +"'";
		
			if (rc.find(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable datos = fila.getRow();			
				
				id=UtilidadesHash.getInteger(datos, "IDPROPOSITO");;								
			}
		}	


		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D.");		
		}
		return id;
	}

}