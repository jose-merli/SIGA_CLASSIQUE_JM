package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class EnvCamposEnviosAdm extends MasterBeanAdministrador
{
    public EnvCamposEnviosAdm(UsrBean usuario)
	{
	    super(EnvCamposEnviosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvCamposEnviosBean.C_IDINSTITUCION,
		        		   EnvCamposEnviosBean.C_IDENVIO,
		        		   EnvCamposEnviosBean.C_IDCAMPO,
		        		   EnvCamposEnviosBean.C_TIPOCAMPO,
		        		   EnvCamposEnviosBean.C_IDFORMATO,
		        		   EnvCamposEnviosBean.C_VALOR,
		        		   EnvCamposEnviosBean.C_FECHAMODIFICACION,
						   EnvCamposEnviosBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvCamposEnviosBean.C_IDINSTITUCION,
		        		   EnvCamposEnviosBean.C_IDENVIO,
		        		   EnvCamposEnviosBean.C_IDCAMPO,
		        		   EnvCamposEnviosBean.C_TIPOCAMPO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvCamposEnviosBean bean = null;

		try
		{
			bean = new EnvCamposEnviosBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvCamposEnviosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvCamposEnviosBean.C_IDENVIO));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, EnvCamposEnviosBean.C_IDCAMPO));
			bean.setTipoCampo(UtilidadesHash.getString(hash, EnvCamposEnviosBean.C_TIPOCAMPO));
			bean.setIdFormato(UtilidadesHash.getInteger(hash, EnvCamposEnviosBean.C_IDFORMATO));
			bean.setValor(UtilidadesHash.getString(hash, EnvCamposEnviosBean.C_VALOR));
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

			EnvCamposEnviosBean b = (EnvCamposEnviosBean) bean;

			UtilidadesHash.set(htData, EnvCamposEnviosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvCamposEnviosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvCamposEnviosBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, EnvCamposEnviosBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, EnvCamposEnviosBean.C_IDFORMATO, b.getIdFormato());
			UtilidadesHash.set(htData, EnvCamposEnviosBean.C_VALOR, b.getValor());
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

	public Vector obtenerCamposEnvios(String idInstitucion, String idEnvio, String tipocampos) throws ClsExceptions
	{
		Vector datos = new Vector();

		RowsContainer rc = null;

		try
		{
			rc = new RowsContainer();

			String sql = " SELECT C." + EnvCamposBean.C_IDCAMPO + ", " +
            					 "C." + EnvCamposBean.C_NOMBRE + ", " +
            					 "C." + EnvCamposBean.C_CAPTURARDATOS + ", " +            					 
            					 "F." + CerFormatosBean.C_IDFORMATO + ", " +
            					 "F." + CerFormatosBean.C_FORMATO + ", " +
            					 UtilidadesMultidioma.getCampoMultidioma("F." + CerFormatosBean.C_DESCRIPCION, this.usrbean.getLanguage())   + ", " +								 "E." + EnvCamposEnviosBean.C_TIPOCAMPO + ", " +
            					 "E." + EnvCamposEnviosBean.C_VALOR + 
                         " FROM " + EnvCamposEnviosBean.T_NOMBRETABLA + " E, " +
                                    EnvCamposBean.T_NOMBRETABLA + " C, " +
                                    CerFormatosBean.T_NOMBRETABLA + " F " +
                         " WHERE E." + EnvCamposEnviosBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
                                "E." + EnvCamposEnviosBean.C_IDENVIO + "=" + idEnvio + " AND " +
                                "E." + EnvCamposEnviosBean.C_IDCAMPO + "=C." + EnvCamposBean.C_IDCAMPO + " AND " +
                                "E." + EnvCamposEnviosBean.C_TIPOCAMPO + "=C." + EnvCamposBean.C_TIPOCAMPO + " AND ";
							if (tipocampos.trim().equals("E"))
			               	{
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "='" + EnvCamposAdm.K_TIPOCAMPO_E + "' AND ";
			               	}

			               	else
							if (tipocampos.trim().equals("S"))
			               	{
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "='" + EnvCamposAdm.K_TIPOCAMPO_S + "' AND ";
			               	}

			               	else
			               	{
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "!='" + EnvCamposAdm.K_TIPOCAMPO_E + "' AND ";
			                    sql += "C." + EnvCamposBean.C_TIPOCAMPO + "!='" + EnvCamposAdm.K_TIPOCAMPO_S + "' AND ";
			               	}
				         sql += "E." + EnvCamposEnviosBean.C_IDFORMATO + "=F." + CerFormatosBean.C_IDFORMATO + "(+) AND " +
				                "E." + EnvCamposEnviosBean.C_TIPOCAMPO + "=F." + CerFormatosBean.C_TIPOCAMPO + "(+)";

         if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);

					String sIdCampo = fila.getString(EnvCamposBean.C_IDCAMPO);
					String sNombre = fila.getString(EnvCamposBean.C_NOMBRE);
					String sIdFormato = fila.getString(CerFormatosBean.C_IDFORMATO);
					String sFormato = fila.getString(CerFormatosBean.C_FORMATO);					
					String sDescripcion = fila.getString(CerFormatosBean.C_DESCRIPCION);
					String sTipoCampo = fila.getString(EnvCamposEnviosBean.C_TIPOCAMPO);
					String sCapturarDatos = fila.getString(EnvCamposBean.C_CAPTURARDATOS);
					String sValor = fila.getString(EnvCamposEnviosBean.C_VALOR);
					
				    Hashtable ht = new Hashtable();

				    ht.put(EnvCamposBean.C_IDCAMPO, sIdCampo);
				    ht.put(EnvCamposBean.C_NOMBRE, sNombre);
				    ht.put(EnvCamposBean.C_CAPTURARDATOS, sCapturarDatos);
				    ht.put(CerFormatosBean.C_IDFORMATO, sIdFormato);
				    ht.put(CerFormatosBean.C_FORMATO, sFormato);
				    ht.put(CerFormatosBean.C_DESCRIPCION, sDescripcion);
				    ht.put(EnvCamposEnviosBean.C_TIPOCAMPO, sTipoCampo);
				    ht.put(EnvCamposEnviosBean.C_VALOR, sValor);

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