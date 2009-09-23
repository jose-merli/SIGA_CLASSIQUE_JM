package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import javax.transaction.UserTransaction;

public class AdmPerfilRolAdm extends MasterBeanAdministrador
{
	public AdmPerfilRolAdm(UsrBean usuario)
	{
	    super(AdmPerfilRolBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmPerfilRolBean.C_IDINSTITUCION,
						   AdmPerfilRolBean.C_IDPERFIL,
						   AdmPerfilRolBean.C_IDROL,
						   AdmPerfilRolBean.C_GRUPO_POR_DEFECTO,
						   AdmPerfilRolBean.C_FECHAMODIFICACION, 
						   AdmPerfilRolBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmPerfilRolBean.C_IDINSTITUCION, AdmPerfilRolBean.C_IDPERFIL, AdmPerfilRolBean.C_IDROL};

		return claves;
	}
	
    /*protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmPerfilRolBean.C_GRUPO_POR_DEFECTO,
     		   			   AdmPerfilRolBean.C_FECHAMODIFICACION, 
     		   			   AdmPerfilRolBean.C_USUMODIFICACION};
		
		return campos;
    }*/

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		AdmPerfilRolBean bean = null;

		try
		{
			bean = new AdmPerfilRolBean();
			
			bean.setIdInstitucion(UtilidadesHash.getString(hash, AdmPerfilRolBean.C_IDINSTITUCION));
			bean.setIdPerfil(UtilidadesHash.getString(hash, AdmPerfilRolBean.C_IDPERFIL));
			bean.setIdRol(UtilidadesHash.getString(hash, AdmPerfilRolBean.C_IDROL));
			bean.setGrupoPorDefecto(UtilidadesHash.getString(hash, AdmPerfilRolBean.C_GRUPO_POR_DEFECTO));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmPerfilRolBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmPerfilRolBean.C_USUMODIFICACION));
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

			AdmPerfilRolBean b = (AdmPerfilRolBean) bean;

			UtilidadesHash.set(htData, AdmPerfilRolBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmPerfilRolBean.C_IDPERFIL, b.getIdPerfil());
			UtilidadesHash.set(htData, AdmPerfilRolBean.C_IDROL, b.getIdRol());
			UtilidadesHash.set(htData, AdmPerfilRolBean.C_GRUPO_POR_DEFECTO, b.getGrupoPorDefecto());
			UtilidadesHash.set(htData, AdmPerfilRolBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmPerfilRolBean.C_USUMODIFICACION, b.getUsuMod());
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
    
 	public Vector selectConDescripciones(String idInstitucion) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();

			String sql = "SELECT R." + AdmRolBean.C_IDROL + " AS " + AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL + ", R." + 
									   AdmRolBean.C_DESCRIPCION + " AS " + AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION + ", (" + 
									 " SELECT P." + AdmPerfilBean.C_IDPERFIL + 
									 " FROM " + AdmPerfilBean.T_NOMBRETABLA + " P, " + 
									 			AdmPerfilRolBean.T_NOMBRETABLA + " PR " +
									 " WHERE PR." + AdmPerfilRolBean.C_IDROL + " = R." + AdmRolBean.C_IDROL + " AND " +
									 		 "PR." + AdmPerfilRolBean.C_IDINSTITUCION + " = " + idInstitucion + " AND " +
									 		 "PR." + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO + " = 'S' AND " +
									 		 "PR." + AdmPerfilRolBean.C_IDPERFIL + " = P." + AdmPerfilBean.C_IDPERFIL + " AND " +
									 		 "PR." + AdmPerfilRolBean.C_IDINSTITUCION + " = P." + AdmPerfilBean.C_IDINSTITUCION +
									 ") AS " + AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_IDPERFIL + ", (" +
									 " SELECT P." + AdmPerfilBean.C_DESCRIPCION + 
									 " FROM " + AdmPerfilBean.T_NOMBRETABLA + " P, " + 
									 		  AdmPerfilRolBean.T_NOMBRETABLA + " PR " +
									 " WHERE PR." + AdmPerfilRolBean.C_IDROL + " = R." + AdmRolBean.C_IDROL + " AND " +
									 		"PR." + AdmPerfilRolBean.C_IDINSTITUCION + " = " + idInstitucion + " AND " + 
									 		"PR." + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO + " = 'S' AND " +
									 		"PR." + AdmPerfilRolBean.C_IDPERFIL + " = P." + AdmPerfilBean.C_IDPERFIL + " AND " +
									 		"PR." + AdmPerfilRolBean.C_IDINSTITUCION + " = P." + AdmPerfilBean.C_IDINSTITUCION + 
									 ") AS " + AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_DESCRIPCION + ", (" +
									 " SELECT PR." + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO + 
									 " FROM " + AdmPerfilBean.T_NOMBRETABLA + " P, " + 
									 			AdmPerfilRolBean.T_NOMBRETABLA + " PR " +
									 " WHERE PR." + AdmPerfilRolBean.C_IDROL + " = R." + AdmRolBean.C_IDROL + " AND " +
									 		 "PR." + AdmPerfilRolBean.C_IDINSTITUCION + " = " + idInstitucion + " AND " +
									 		 "PR." + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO + " = 'S' AND " +
									 		 "PR." + AdmPerfilRolBean.C_IDPERFIL + " = P." + AdmPerfilBean.C_IDPERFIL + " AND " +
									 		 "PR." + AdmPerfilRolBean.C_IDINSTITUCION + " = P." + AdmPerfilBean.C_IDINSTITUCION +
									 ") AS " + AdmPerfilRolBean.T_NOMBRETABLA + "_" + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO +
									 
						" FROM " + AdmRolBean.T_NOMBRETABLA + " R " +
						" ORDER BY R." + AdmRolBean.C_DESCRIPCION;

			if (rc.query(sql)) 
			{
				for (int i = 0; i < rc.size(); i++)	
				{
					Row fila = (Row) rc.get(i);
					Hashtable htDatos = fila.getRow();
					
					String idRol = (String)htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL);
					String descRol = (String)htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION);
					String idPerfil = (String)htDatos.get(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_IDPERFIL);
					String descPerfil = (String)htDatos.get(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_DESCRIPCION);
					String grupoPorDefecto = (String)htDatos.get(AdmPerfilRolBean.T_NOMBRETABLA + "_" + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO);
					
				    Hashtable ht = new Hashtable();
				    
				    ht.put(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL, idRol);
				    ht.put(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION, descRol);
				    ht.put(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_IDPERFIL, idPerfil);
				    ht.put(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_DESCRIPCION, descPerfil);
				    ht.put(AdmPerfilRolBean.T_NOMBRETABLA + "_" + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO, grupoPorDefecto);
					
					datos.add(ht);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
 	
 	public boolean setGrupoPorDefecto(UsrBean userbean, String idInstitucion, String idRol, String idPerfil)
 	{
 	   UserTransaction tx = null;
 	   
 	    try
 	    {
 	        tx = userbean.getTransaction();
 	        
 	        tx.begin();
 	        
 	        Row row = new Row();
 	        RowsContainer rc = new RowsContainer();
 	        
 	        String sql = "";
 	        //sql += "SELECT " + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO;
 	        //sql += ", " + AdmPerfilRolBean.C_IDINSTITUCION;
 	        sql += "SELECT " + AdmPerfilRolBean.C_IDINSTITUCION;
 	        sql += ", " + AdmPerfilRolBean.C_IDROL;
 	        sql += " FROM " + AdmPerfilRolBean.T_NOMBRETABLA;
 	        sql += " WHERE " + AdmPerfilRolBean.C_IDINSTITUCION + " = " + idInstitucion;
 	        sql += " AND " + AdmPerfilRolBean.C_IDROL + " = '" + idRol + "'";

 	        String[] aClaves1 = {AdmPerfilRolBean.C_IDINSTITUCION, AdmPerfilRolBean.C_IDROL};
 	        String[] aValores1 = {AdmPerfilRolBean.C_GRUPO_POR_DEFECTO};

 	        if (rc.findForUpdate(sql))
 	        {
 	            for (int i=0; i<rc.size(); i++)
 	            {
 	                row = (Row)rc.get(i);
 	                row.setCompareData((Hashtable)row.getRow().clone());
 	                row.setValue(AdmPerfilRolBean.C_GRUPO_POR_DEFECTO, "N");
 	                
 	                row.update(AdmPerfilRolBean.T_NOMBRETABLA, aClaves1, aValores1);
 	            }
 	        }

 	        sql = "";
 	        sql += "SELECT " + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO;
 	        sql += ", " + AdmPerfilRolBean.C_IDINSTITUCION;
 	        sql += ", " + AdmPerfilRolBean.C_IDROL;
 	        sql += ", " + AdmPerfilRolBean.C_IDPERFIL;
 	        sql += " FROM " + AdmPerfilRolBean.T_NOMBRETABLA;
 	        sql += " WHERE " + AdmPerfilRolBean.C_IDINSTITUCION + " = " + idInstitucion;
 	        sql += " AND " + AdmPerfilRolBean.C_IDPERFIL + " = '" + idPerfil + "'";
 	        sql += " AND " + AdmPerfilRolBean.C_IDROL + " = '" + idRol + "'";

 	        String[] aClaves2 = {AdmPerfilRolBean.C_IDINSTITUCION, AdmPerfilRolBean.C_IDROL, AdmPerfilRolBean.C_IDPERFIL};
 	        String[] aValores2 = {AdmPerfilRolBean.C_GRUPO_POR_DEFECTO};
 	        
 	        row = new Row();

 	        if (row.findForUpdate(sql)) 
 	        {
 	            row.setCompareData((Hashtable)row.getRow().clone());
 	            row.setValue(AdmPerfilRolBean.C_GRUPO_POR_DEFECTO, "S");
 	            
 	            row.update(AdmPerfilRolBean.T_NOMBRETABLA, aClaves2, aValores2);
 	        }
 	        
 	        tx.commit();
 	            
 	        return true;
 	    }
 	    
 	    catch(Exception e)
 	    {
 	        try
 	        {
 	            tx.rollback();
 	        }
 	        
 	        catch(Exception ex)
 	        {
 	            e.printStackTrace();
 	        }
 	        
 	        return false;
 	    }
 	}
}