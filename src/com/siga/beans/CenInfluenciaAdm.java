package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

public class CenInfluenciaAdm extends MasterBeanAdministrador
{
	public CenInfluenciaAdm(UsrBean usuario)
	{
	    super(CenInfluenciaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CenInfluenciaBean.C_IDINSTITUCION,
						   CenInfluenciaBean.C_IDPARTIDO,};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {  CenInfluenciaBean.C_IDINSTITUCION,CenInfluenciaBean.C_IDPARTIDO};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {CenInfluenciaBean.C_IDINSTITUCION,
     		   			   CenInfluenciaBean.C_IDPARTIDO,
     		   			   CenInfluenciaBean.C_FECHAMODIFICACION, 
     		   			   CenInfluenciaBean.C_USUMODIFICACION};
		
		return campos;

    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CenInfluenciaBean bean = null;

		try
		{
			bean = new CenInfluenciaBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenInfluenciaBean.C_IDINSTITUCION));
			bean.setIdPartido(UtilidadesHash.getInteger(hash, CenInfluenciaBean.C_IDPARTIDO));
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

			CenInfluenciaBean b = (CenInfluenciaBean) bean;

			UtilidadesHash.set(htData, CenInfluenciaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenInfluenciaBean.C_IDPARTIDO, b.getIdPartido());
			
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
        String[] ordenCampos = {CenInfluenciaBean.C_IDINSTITUCION,CenInfluenciaBean.C_IDPARTIDO};
        
        return ordenCampos;
    }
    
    /**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	
	
	public Vector selectGenerico(String consulta) throws ClsExceptions,SIGAException {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try {
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}			
		} 
		catch (ClsExceptions e) {
			throw e;		
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenInfluenciaAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	
    
    
    
}
