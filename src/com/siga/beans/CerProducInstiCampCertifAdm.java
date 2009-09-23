package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class CerProducInstiCampCertifAdm extends MasterBeanAdministrador
{
	public CerProducInstiCampCertifAdm(UsrBean usuario)
	{
	    super(CerProducInstiCampCertifBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerProducInstiCampCertifBean.C_IDINSTITUCION,
		        		   CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO,
		        		   CerProducInstiCampCertifBean.C_IDPRODUCTO,
		        		   CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION,
		        		   CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO,
		        		   CerProducInstiCampCertifBean.C_IDFORMATO,
		        		   CerProducInstiCampCertifBean.C_TIPOCAMPO,
		        		   CerProducInstiCampCertifBean.C_VALOR,
		        		   CerProducInstiCampCertifBean.C_FECHAMODIFICACION, 
						   CerProducInstiCampCertifBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerProducInstiCampCertifBean.C_IDINSTITUCION, CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, CerProducInstiCampCertifBean.C_IDPRODUCTO, CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, CerProducInstiCampCertifBean.C_TIPOCAMPO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerProducInstiCampCertifBean bean = null;

		try
		{
			bean = new CerProducInstiCampCertifBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CerProducInstiCampCertifBean.C_IDINSTITUCION));
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash, CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto(UtilidadesHash.getInteger(hash, CerProducInstiCampCertifBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion(UtilidadesHash.getInteger(hash, CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION));
			bean.setIdCampoCertificado(UtilidadesHash.getInteger(hash, CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO));
			bean.setIdFormato(UtilidadesHash.getInteger(hash, CerProducInstiCampCertifBean.C_IDFORMATO));
			bean.setTipoCampo(UtilidadesHash.getString(hash, CerProducInstiCampCertifBean.C_TIPOCAMPO));
			bean.setValor(UtilidadesHash.getString(hash, CerProducInstiCampCertifBean.C_VALOR));
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

			CerProducInstiCampCertifBean b = (CerProducInstiCampCertifBean) bean;

			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_IDPRODUCTO, b.getIdProducto());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, b.getIdProductoInstitucion());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, b.getIdCampoCertificado());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_IDFORMATO, b.getIdFormato());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, CerProducInstiCampCertifBean.C_VALOR, b.getValor());
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
    
    public Vector obtenerCampos(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion) throws ClsExceptions
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		
		try
		{
			rc = new RowsContainer();
			
			String sql = " SELECT C." + CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO + "," +
								 "C." + CerCamposCertificadosBean.C_TIPOCAMPO + "," +
								 "C." + CerCamposCertificadosBean.C_CAPTURARDATOS + "," +
							     "C." + CerCamposCertificadosBean.C_NOMBRE + "," +
							     "P." + CerProducInstiCampCertifBean.C_VALOR + "," +
							     "F." + CerFormatosBean.C_IDFORMATO + "," +
							     "F." + CerFormatosBean.C_FORMATO + ", " +
							     UtilidadesMultidioma.getCampoMultidioma("F." + CerFormatosBean.C_DESCRIPCION, this.usrbean.getLanguage())  +
						 " FROM " + CerProducInstiCampCertifBean.T_NOMBRETABLA + " P, " +
						            CerCamposCertificadosBean.T_NOMBRETABLA + " C, " +
						            CerFormatosBean.T_NOMBRETABLA + " F " +
			             " WHERE P." + CerProducInstiCampCertifBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
			                    "P." + CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO + "=" + idTipoProducto + " AND " +
			                    "P." + CerProducInstiCampCertifBean.C_IDPRODUCTO + "=" + idProducto + " AND " +
			                    "P." + CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION + "=" + idProductoInstitucion + " AND " +
			                    "P." + CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO + "=C." + CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO + " AND " +
			                    "P." + CerProducInstiCampCertifBean.C_TIPOCAMPO + "=C." + CerCamposCertificadosBean.C_TIPOCAMPO + " AND " +
			                    "P." + CerProducInstiCampCertifBean.C_IDFORMATO + "=F." + CerFormatosBean.C_IDFORMATO + "(+) AND " +
			                    "P." + CerProducInstiCampCertifBean.C_TIPOCAMPO + "=F." + CerFormatosBean.C_TIPOCAMPO + "(+)";

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					
					String sIdNombre = fila.getString(CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO);
					String sTipoCampo = fila.getString(CerCamposCertificadosBean.C_TIPOCAMPO);
					String sCapturarDatos = fila.getString(CerCamposCertificadosBean.C_CAPTURARDATOS);
					String sNombre = fila.getString(CerCamposCertificadosBean.C_NOMBRE);
					String sValor = fila.getString(CerProducInstiCampCertifBean.C_VALOR);
					String sIdFormato = fila.getString(CerFormatosBean.C_IDFORMATO);
					String sFormato = fila.getString(CerFormatosBean.C_FORMATO);
					String sDescripcion = fila.getString(CerFormatosBean.C_DESCRIPCION);

				    Hashtable ht = new Hashtable();
				    
				    ht.put(CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO, sIdNombre);
				    ht.put(CerCamposCertificadosBean.C_TIPOCAMPO, sTipoCampo);
				    ht.put(CerCamposCertificadosBean.C_CAPTURARDATOS, sCapturarDatos);
				    ht.put(CerCamposCertificadosBean.C_NOMBRE, sNombre);
				    ht.put(CerProducInstiCampCertifBean.C_VALOR, sValor);
				    ht.put(CerFormatosBean.C_IDFORMATO, sIdFormato);
				    ht.put(CerFormatosBean.C_FORMATO, sFormato);
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