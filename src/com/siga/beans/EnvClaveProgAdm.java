package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class EnvClaveProgAdm extends MasterBeanAdministrador
{
 

    public EnvClaveProgAdm(UsrBean usuario)
	{
	    super(EnvClaveProgBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvClaveProgBean.C_IDTIPOINFORME,
		        		   EnvClaveProgBean.C_CLAVE,
						   EnvClaveProgBean.C_TIPO};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvClaveProgBean.C_IDTIPOINFORME,
     		   EnvClaveProgBean.C_CLAVE,
			   EnvClaveProgBean.C_TIPO,
           	EnvValorCampoClaveBean.C_FECHAMODIFICACION,
        	EnvValorCampoClaveBean.C_USUMODIFICACION};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvClaveProgBean bean = null;

		try
		{
			bean = new EnvClaveProgBean();

			
			bean.setIdTipoInforme(UtilidadesHash.getString(hash, EnvClaveProgBean.C_IDTIPOINFORME));
			bean.setClave(UtilidadesHash.getString(hash, EnvClaveProgBean.C_CLAVE));
			bean.setTipo(UtilidadesHash.getString(hash, EnvClaveProgBean.C_TIPO));
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

			EnvClaveProgBean b = (EnvClaveProgBean) bean;

			UtilidadesHash.set(htData, EnvClaveProgBean.C_IDTIPOINFORME, b.getIdTipoInforme());
			UtilidadesHash.set(htData, EnvClaveProgBean.C_CLAVE, b.getClave());
			UtilidadesHash.set(htData, EnvClaveProgBean.C_TIPO, b.getTipo());
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
			                     "C." + EnvCamposBean.C_TIPOCAMPO + ", " +
			                     "C." + EnvCamposBean.C_CAPTURARDATOS + ", " +
			                     "C." + EnvCamposBean.C_NOMBRE + ", " +
			                     "P." + EnvClaveProgBean.C_VALOR + ", " +
			                     "F." + CerFormatosBean.C_IDFORMATO + ", " +
			                     UtilidadesMultidioma.getCampoMultidioma("F." + CerFormatosBean.C_DESCRIPCION, this.usrbean.getLanguage())  +
			             " FROM " + EnvClaveProgBean.T_NOMBRETABLA + " P, " +
			                        EnvCamposBean.T_NOMBRETABLA + " C, " +
			                        CerFormatosBean.T_NOMBRETABLA + " F " +
			             " WHERE P." + EnvClaveProgBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
			                    "P." + EnvClaveProgBean.C_IDTIPOENVIOS + "=" + idTipoEnvio + " AND " +
			                    "P." + EnvClaveProgBean.C_IDPLANTILLAENVIOS + "=" + idPlantillaEnvios + " AND " +
			                    "P." + EnvClaveProgBean.C_IDCAMPO + "=C." + EnvCamposBean.C_IDCAMPO + " AND ";
				            if (tipoCampo.trim().equals("E"))
				           	{
				                sql += "C." + EnvCamposBean.C_TIPOCAMPO + "='" + EnvCamposAdm.K_TIPOCAMPO_E + "' AND ";
				           	}
				           	else
			                if (tipoCampo.trim().equals("S"))
			               	{
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "='" + EnvCamposAdm.K_TIPOCAMPO_S + "' AND ";
			               	}
			               	else
			               	{
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "!='" + EnvCamposAdm.K_TIPOCAMPO_E + "' AND ";
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "!='" + EnvCamposAdm.K_TIPOCAMPO_S + "' AND ";
			               	}

			             sql += "P." + EnvClaveProgBean.C_TIPOCAMPO + "=C." + EnvCamposBean.C_TIPOCAMPO + " AND " +
			                    "P." + EnvClaveProgBean.C_IDFORMATO + "=F." + CerFormatosBean.C_IDFORMATO + "(+) AND " +
			                    "P." + EnvClaveProgBean.C_TIPOCAMPO + "=F." + CerFormatosBean.C_TIPOCAMPO + "(+)";

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);

					String sIdNombre = fila.getString(EnvCamposBean.C_IDCAMPO);
					String sTipoCampo = fila.getString(EnvCamposBean.C_TIPOCAMPO);
					String sCapturarDatos = fila.getString(EnvCamposBean.C_CAPTURARDATOS);
					String sNombre = fila.getString(EnvCamposBean.C_NOMBRE);
					String sValor = fila.getString(EnvClaveProgBean.C_VALOR);
					String sIdDescripcion = fila.getString(CerFormatosBean.C_IDFORMATO);
					String sDescripcion = fila.getString(CerFormatosBean.C_DESCRIPCION);


				    Hashtable ht = new Hashtable();

				    ht.put(EnvCamposBean.C_IDCAMPO, sIdNombre);
				    ht.put(EnvCamposBean.C_TIPOCAMPO, sTipoCampo);
				    ht.put(EnvCamposBean.C_CAPTURARDATOS, sCapturarDatos);
				    ht.put(EnvCamposBean.C_NOMBRE, sNombre);
				    ht.put(EnvClaveProgBean.C_VALOR, sValor);
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