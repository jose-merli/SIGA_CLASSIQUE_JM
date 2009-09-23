package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class EnvCamposPlantillaAdm extends MasterBeanAdministrador
{
    public static final String K_IDCAMPO_ASUNTO="1";
    public static final String K_IDCAMPO_CUERPO="2";
    public static final String K_IDCAMPO_SMS="1";

    public EnvCamposPlantillaAdm(UsrBean usuario)
	{
	    super(EnvCamposPlantillaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvCamposPlantillaBean.C_IDINSTITUCION,
		        		   EnvCamposPlantillaBean.C_IDTIPOENVIOS,
		        		   EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS,
		        		   EnvCamposPlantillaBean.C_IDCAMPO,
		        		   EnvCamposPlantillaBean.C_TIPOCAMPO,
		        		   EnvCamposPlantillaBean.C_IDFORMATO,
		        		   EnvCamposPlantillaBean.C_VALOR,
		        		   EnvCamposPlantillaBean.C_FECHAMODIFICACION,
						   EnvCamposPlantillaBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvCamposPlantillaBean.C_IDINSTITUCION, EnvCamposPlantillaBean.C_IDTIPOENVIOS, EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, EnvCamposPlantillaBean.C_IDCAMPO, EnvCamposPlantillaBean.C_TIPOCAMPO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvCamposPlantillaBean bean = null;

		try
		{
			bean = new EnvCamposPlantillaBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvCamposPlantillaBean.C_IDINSTITUCION));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvCamposPlantillaBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, EnvCamposPlantillaBean.C_IDCAMPO));
			bean.setTipoCampo(UtilidadesHash.getString(hash, EnvCamposPlantillaBean.C_TIPOCAMPO));
			bean.setIdFormato(UtilidadesHash.getInteger(hash, EnvCamposPlantillaBean.C_IDFORMATO));
			bean.setValor(UtilidadesHash.getString(hash, EnvCamposPlantillaBean.C_VALOR));
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

			EnvCamposPlantillaBean b = (EnvCamposPlantillaBean) bean;

			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_IDFORMATO, b.getIdFormato());
			UtilidadesHash.set(htData, EnvCamposPlantillaBean.C_VALOR, b.getValor());
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

    public Vector obtenerCampos(String idInstitucion, String idTipoEnvio, String idPlantillaEnvios, String tipoCampo) throws ClsExceptions
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
			                     "P." + EnvCamposPlantillaBean.C_VALOR + ", " +
			                     "F." + CerFormatosBean.C_IDFORMATO + ", " +
			                     UtilidadesMultidioma.getCampoMultidioma("F." + CerFormatosBean.C_DESCRIPCION, this.usrbean.getLanguage())  +
			             " FROM " + EnvCamposPlantillaBean.T_NOMBRETABLA + " P, " +
			                        EnvCamposBean.T_NOMBRETABLA + " C, " +
			                        CerFormatosBean.T_NOMBRETABLA + " F " +
			             " WHERE P." + EnvCamposPlantillaBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
			                    "P." + EnvCamposPlantillaBean.C_IDTIPOENVIOS + "=" + idTipoEnvio + " AND " +
			                    "P." + EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS + "=" + idPlantillaEnvios + " AND " +
			                    "P." + EnvCamposPlantillaBean.C_IDCAMPO + "=C." + EnvCamposBean.C_IDCAMPO + " AND ";
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

			             sql += "P." + EnvCamposPlantillaBean.C_TIPOCAMPO + "=C." + EnvCamposBean.C_TIPOCAMPO + " AND " +
			                    "P." + EnvCamposPlantillaBean.C_IDFORMATO + "=F." + CerFormatosBean.C_IDFORMATO + "(+) AND " +
			                    "P." + EnvCamposPlantillaBean.C_TIPOCAMPO + "=F." + CerFormatosBean.C_TIPOCAMPO + "(+)";

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);

					String sIdNombre = fila.getString(EnvCamposBean.C_IDCAMPO);
					String sTipoCampo = fila.getString(EnvCamposBean.C_TIPOCAMPO);
					String sCapturarDatos = fila.getString(EnvCamposBean.C_CAPTURARDATOS);
					String sNombre = fila.getString(EnvCamposBean.C_NOMBRE);
					String sValor = fila.getString(EnvCamposPlantillaBean.C_VALOR);
					String sIdDescripcion = fila.getString(CerFormatosBean.C_IDFORMATO);
					String sDescripcion = fila.getString(CerFormatosBean.C_DESCRIPCION);


				    Hashtable ht = new Hashtable();

				    ht.put(EnvCamposBean.C_IDCAMPO, sIdNombre);
				    ht.put(EnvCamposBean.C_TIPOCAMPO, sTipoCampo);
				    ht.put(EnvCamposBean.C_CAPTURARDATOS, sCapturarDatos);
				    ht.put(EnvCamposBean.C_NOMBRE, sNombre);
				    ht.put(EnvCamposPlantillaBean.C_VALOR, sValor);
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
	}
}