package com.siga.beans;

//import java.io.*;
//import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.zip.*;
import com.atos.utils.*;
//import com.siga.general.*;
import com.siga.Utilidades.*;

public class CerSolicitudCertificadosTextoAdm extends MasterBeanAdministrador
{
	public CerSolicitudCertificadosTextoAdm(UsrBean usuario)
	{
	    super(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerSolicitudCertificadosTextoBean.C_IDINSTITUCION,
		        		   CerSolicitudCertificadosTextoBean.C_IDSOLICITUD,
		        		   CerSolicitudCertificadosTextoBean.C_IDTEXTO,
		        		   //CerSolicitudCertificadosTextoBean.C_TEXTO,
		        		   CerSolicitudCertificadosTextoBean.C_FECHAMODIFICACION,
						   CerSolicitudCertificadosTextoBean.C_USUMODIFICACION,
						   CerSolicitudCertificadosTextoBean.C_INCLUIRSANCIONES,
						   CerSolicitudCertificadosTextoBean.C_INCLUIRDEUDAS};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerSolicitudCertificadosTextoBean.C_IDINSTITUCION, CerSolicitudCertificadosTextoBean.C_IDSOLICITUD, CerSolicitudCertificadosTextoBean.C_IDTEXTO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerSolicitudCertificadosTextoBean bean = null;

		try
		{
			bean = new CerSolicitudCertificadosTextoBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosTextoBean.C_IDINSTITUCION));
			bean.setIdSolicitud(UtilidadesHash.getLong(hash, CerSolicitudCertificadosTextoBean.C_IDSOLICITUD));
			bean.setIdTexto(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosTextoBean.C_IDTEXTO));
			//bean.setTexto(UtilidadesHash.getString(hash, CerSolicitudCertificadosTextoBean.C_TEXTO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosTextoBean.C_USUMODIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash, CerSolicitudCertificadosTextoBean.C_FECHAMODIFICACION));
			bean.setIncluirDeudas(UtilidadesHash.getString(hash, CerSolicitudCertificadosTextoBean.C_INCLUIRDEUDAS));
			bean.setIncluirSanciones(UtilidadesHash.getString(hash, CerSolicitudCertificadosTextoBean.C_INCLUIRSANCIONES));
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

			CerSolicitudCertificadosTextoBean b = (CerSolicitudCertificadosTextoBean) bean;

			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_IDTEXTO, b.getIdTexto());
			//UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_TEXTO, b.getTexto());
			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_INCLUIRDEUDAS, b.getIncluirDeudas());
			UtilidadesHash.set(htData, CerSolicitudCertificadosTextoBean.C_INCLUIRSANCIONES, b.getIncluirSanciones());
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


   	public String getNuevoID(String idInstitucion, String idSolicitud) throws ClsExceptions
	{
		String sID="0";

		try
		{
			RowsContainer rc = new RowsContainer();

			String sSQL="";

			sSQL = " SELECT NVL(MAX(" + CerSolicitudCertificadosTextoBean.C_IDTEXTO + "),0)+1 AS " + CerSolicitudCertificadosTextoBean.C_IDTEXTO +
			//" ,"+CerSolicitudCertificadosTextoBean.C_INCLUIRDEUDAS +","+CerSolicitudCertificadosTextoBean.C_INCLUIRSANCIONES +
				   " FROM " + CerSolicitudCertificadosTextoBean.T_NOMBRETABLA +
				   " WHERE " + CerSolicitudCertificadosTextoBean.C_IDINSTITUCION + "=" + idInstitucion  + " AND " +
				   			   CerSolicitudCertificadosTextoBean.C_IDSOLICITUD + "=" + idSolicitud;

			if (rc.findForUpdate(sSQL))
			{
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();

				String sIDAux = (String)ht.get(CerSolicitudCertificadosTextoBean.C_IDTEXTO);

				if (sIDAux!=null && !sIDAux.equals(""))
				{
					sID = "" + (Integer.parseInt(sIDAux)+1);
				}
			}

			return sID;
		}

		catch (ClsExceptions e)
		{
			throw new ClsExceptions (e, "Error al obtener el MAX(IDPLANTILLA).");
		}
	}

}