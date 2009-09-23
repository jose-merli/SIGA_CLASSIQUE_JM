package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class EnvCampoClaveAdm extends MasterBeanAdministrador
{
 

    public EnvCampoClaveAdm(UsrBean usuario)
	{
	    super(EnvCampoClaveBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvCampoClaveBean.C_IDTIPOINFORME,
		        		   EnvCampoClaveBean.C_CLAVE,
						   EnvCampoClaveBean.C_CAMPO};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvCampoClaveBean.C_IDTIPOINFORME,
     		   EnvCampoClaveBean.C_CLAVE,
			   EnvCampoClaveBean.C_CAMPO,
	           	EnvValorCampoClaveBean.C_FECHAMODIFICACION,
	        	EnvValorCampoClaveBean.C_USUMODIFICACION};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvCampoClaveBean bean = null;

		try
		{
			bean = new EnvCampoClaveBean();

			
			bean.setIdTipoInforme(UtilidadesHash.getString(hash, EnvCampoClaveBean.C_IDTIPOINFORME));
			bean.setClave(UtilidadesHash.getString(hash, EnvCampoClaveBean.C_CLAVE));
			bean.setCampo(UtilidadesHash.getString(hash, EnvCampoClaveBean.C_CAMPO));
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

			EnvCampoClaveBean b = (EnvCampoClaveBean) bean;

			UtilidadesHash.set(htData, EnvCampoClaveBean.C_IDTIPOINFORME, b.getIdTipoInforme());
			UtilidadesHash.set(htData, EnvCampoClaveBean.C_CLAVE, b.getClave());
			UtilidadesHash.set(htData, EnvCampoClaveBean.C_CAMPO, b.getCampo());
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

   /* public Vector obtenerCampos(String idInstitucion, String idTipoEnvio, String idPlantillaEnvios, String tipoCampo) throws ClsExceptions
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;

		try
		{
			rc = new RowsContainer();

			String sql = " SELECT C." + EnvCamposBean.C_IDCAMPO + ", " +
			                     "C." + EnvCamposBean.C_CAMPOCAMPO + ", " +
			                     "C." + EnvCamposBean.C_CAPTURARDATOS + ", " +
			                     "C." + EnvCamposBean.C_NOMBRE + ", " +
			                     "P." + EnvCampoClaveBean.C_VALOR + ", " +
			                     "F." + CerFormatosBean.C_IDFORMATO + ", " +
			                     UtilidadesMultidioma.getCampoMultidioma("F." + CerFormatosBean.C_DESCRIPCION, this.usrbean.getLanguage())  +
			             " FROM " + EnvCampoClaveBean.T_NOMBRETABLA + " P, " +
			                        EnvCamposBean.T_NOMBRETABLA + " C, " +
			                        CerFormatosBean.T_NOMBRETABLA + " F " +
			             " WHERE P." + EnvCampoClaveBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
			                    "P." + EnvCampoClaveBean.C_IDTIPOENVIOS + "=" + idTipoEnvio + " AND " +
			                    "P." + EnvCampoClaveBean.C_IDPLANTILLAENVIOS + "=" + idPlantillaEnvios + " AND " +
			                    "P." + EnvCampoClaveBean.C_IDCAMPO + "=C." + EnvCamposBean.C_IDCAMPO + " AND ";
				            if (tipoCampo.trim().equals("E"))
				           	{
				                sql += "C." + EnvCamposBean.C_CAMPOCAMPO + "='" + EnvCamposAdm.K_TIPOCAMPO_E + "' AND ";
				           	}
				           	else
			                if (tipoCampo.trim().equals("S"))
			               	{
			                    sql += "C." + EnvCamposBean.C_CAMPOCAMPO + "='" + EnvCamposAdm.K_TIPOCAMPO_S + "' AND ";
			               	}
			               	else
			               	{
			                    sql += "C." + EnvCamposBean.C_CAMPOCAMPO + "!='" + EnvCamposAdm.K_TIPOCAMPO_E + "' AND ";
			                    sql += "C." + EnvCamposBean.C_CAMPOCAMPO + "!='" + EnvCamposAdm.K_TIPOCAMPO_S + "' AND ";
			               	}

			             sql += "P." + EnvCampoClaveBean.C_CAMPOCAMPO + "=C." + EnvCamposBean.C_CAMPOCAMPO + " AND " +
			                    "P." + EnvCampoClaveBean.C_IDFORMATO + "=F." + CerFormatosBean.C_IDFORMATO + "(+) AND " +
			                    "P." + EnvCampoClaveBean.C_CAMPOCAMPO + "=F." + CerFormatosBean.C_CAMPOCAMPO + "(+)";

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);

					String sIdNombre = fila.getString(EnvCamposBean.C_IDCAMPO);
					String sTipoCampo = fila.getString(EnvCamposBean.C_CAMPOCAMPO);
					String sCapturarDatos = fila.getString(EnvCamposBean.C_CAPTURARDATOS);
					String sNombre = fila.getString(EnvCamposBean.C_NOMBRE);
					String sValor = fila.getString(EnvCampoClaveBean.C_VALOR);
					String sIdDescripcion = fila.getString(CerFormatosBean.C_IDFORMATO);
					String sDescripcion = fila.getString(CerFormatosBean.C_DESCRIPCION);


				    Hashtable ht = new Hashtable();

				    ht.put(EnvCamposBean.C_IDCAMPO, sIdNombre);
				    ht.put(EnvCamposBean.C_CAMPOCAMPO, sTipoCampo);
				    ht.put(EnvCamposBean.C_CAPTURARDATOS, sCapturarDatos);
				    ht.put(EnvCamposBean.C_NOMBRE, sNombre);
				    ht.put(EnvCampoClaveBean.C_VALOR, sValor);
				    ht.put(CerFormatosBean.C_IDFORMATO, sIdDescripcion);
				    ht.put(CerFormatosBean.C_DESCRIPCION, sDescripcion);

				    datos.add(ht);
				}
			}
		}

		catch(Exception e)
		{
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}

		return datos;
	}*/
}