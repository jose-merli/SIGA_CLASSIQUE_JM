package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class GenTablasMaestrasAdm extends MasterBeanAdministrador
{
	public GenTablasMaestrasAdm(UsrBean usuario)
	{
	    super(GenTablasMaestrasBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {GenTablasMaestrasBean.C_IDTABLAMAESTRA,
     		   GenTablasMaestrasBean.C_IDCAMPOCODIGO,
    		   GenTablasMaestrasBean.C_IDCAMPOCODIGOEXT,
    		   GenTablasMaestrasBean.C_LONGITUDCODIGOEXT,
    		   GenTablasMaestrasBean.C_TIPOCODIGOEXT,
		        		   GenTablasMaestrasBean.C_IDTABLATRADUCCION,
		        		   GenTablasMaestrasBean.C_IDCAMPODESCRIPCION,
		        		   GenTablasMaestrasBean.C_PATH_ACCION,
		        		   GenTablasMaestrasBean.C_ALIAS_TABLA,
		        		   GenTablasMaestrasBean.C_FLAG_BORRADO_LOGICO,
		        		   GenTablasMaestrasBean.C_FLAG_USA_LENGUAJE,
		        		   GenTablasMaestrasBean.C_LONGITUD_CODIGO,
		        		   GenTablasMaestrasBean.C_LONGITUD_DESCRIPCION,
		        		   GenTablasMaestrasBean.C_IDRECURSO,
		        		   GenTablasMaestrasBean.C_IDLENGUAJE,
		        		   GenTablasMaestrasBean.C_TIPO_CODIGO,
		        		   GenTablasMaestrasBean.C_LOCAL,
		        		   GenTablasMaestrasBean.C_FECHAMODIFICACION,
						   GenTablasMaestrasBean.C_USUMODIFICACION,
						   GenTablasMaestrasBean.C_ACEPTABAJA,
						   GenTablasMaestrasBean.C_IDTABLAREL,
						   GenTablasMaestrasBean.C_IDCAMPOCODIGOREL,
						   GenTablasMaestrasBean.C_DESCRIPCIONREL,
						   GenTablasMaestrasBean.C_QUERYTABLAREL,
						   GenTablasMaestrasBean.C_NUMEROTEXTOPLANTILLAS,};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {GenTablasMaestrasBean.C_IDTABLAMAESTRA};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    GenTablasMaestrasBean bean = null;

		try
		{
			bean = new GenTablasMaestrasBean();
			
			bean.setIdTablaMaestra(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDTABLAMAESTRA));
			bean.setIdCampoCodigo(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDCAMPOCODIGO));

			bean.setIdCampoCodigoExt(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDCAMPOCODIGOEXT));
			bean.setLongitudCodigoExt(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_LONGITUDCODIGOEXT));
			bean.setTipoCodigoExt(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_TIPOCODIGOEXT));
			
			bean.setIdTablaTraduccion(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDTABLATRADUCCION));
			bean.setIdCampoDescripcion(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDCAMPODESCRIPCION));
			bean.setPathAccion(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_PATH_ACCION));
			bean.setAliasTabla(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_ALIAS_TABLA));
			bean.setFlagBorradoLogico(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_FLAG_BORRADO_LOGICO));
			bean.setFlagUsaLenguaje(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_FLAG_USA_LENGUAJE));
			bean.setLongitudCodigo(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_LONGITUD_CODIGO));
			bean.setLongitudDescripcion(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_LONGITUD_DESCRIPCION));
			bean.setIdRecurso(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDRECURSO));
			bean.setIdLenguaje(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDLENGUAJE));
			bean.setTipoCodigo(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_TIPO_CODIGO));
			bean.setLocal(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_LOCAL));
			bean.setFechaMod(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, GenTablasMaestrasBean.C_USUMODIFICACION));
			bean.setAceptabaja(UtilidadesHash.getInteger(hash, GenTablasMaestrasBean.C_ACEPTABAJA));
			
			bean.setIdTablaRel(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDTABLAREL));
			bean.setIdCampoCodigoRel(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_IDCAMPOCODIGOREL));
			bean.setDescripcionRel(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_DESCRIPCIONREL));
			bean.setQueryTablaRel(UtilidadesHash.getString(hash, GenTablasMaestrasBean.C_QUERYTABLAREL));
			bean.setNumeroTextoPlantillas(UtilidadesHash.getInteger(hash, GenTablasMaestrasBean.C_NUMEROTEXTOPLANTILLAS));
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

			GenTablasMaestrasBean b = (GenTablasMaestrasBean) bean;

			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDTABLAMAESTRA, b.getIdTablaMaestra());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDCAMPOCODIGO, b.getIdCampoCodigo());

			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDCAMPOCODIGOEXT, b.getIdCampoCodigoExt());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_LONGITUDCODIGOEXT, b.getLongitudCodigoExt());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_TIPOCODIGOEXT, b.getTipoCodigoExt());
			
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDTABLATRADUCCION, b.getIdTablaTraduccion());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDCAMPODESCRIPCION, b.getIdCampoDescripcion());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_PATH_ACCION, b.getPathAccion());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_ALIAS_TABLA, b.getAliasTabla());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_FLAG_BORRADO_LOGICO, b.getFlagBorradoLogico());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_FLAG_USA_LENGUAJE, b.getFlagUsaLenguaje());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_LONGITUD_CODIGO, b.getLongitudCodigo());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_LONGITUD_DESCRIPCION, b.getLongitudDescripcion());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDRECURSO, b.getIdRecurso());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDLENGUAJE, b.getIdLenguaje());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_TIPO_CODIGO, b.getTipoCodigo());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_LOCAL, b.getLocal());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_ACEPTABAJA, b.getAceptabaja());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDTABLAREL, b.getIdTablaRel());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_IDCAMPOCODIGOREL, b.getIdCampoCodigoRel());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_DESCRIPCIONREL, b.getDescripcionRel());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_QUERYTABLAREL, b.getQueryTablaRel());
			UtilidadesHash.set(htData, GenTablasMaestrasBean.C_NUMEROTEXTOPLANTILLAS, b.getNumeroTextoPlantillas());
			
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
        String[] ordenCampos = {GenTablasMaestrasBean.C_ALIAS_TABLA};
        
        return ordenCampos;
    }
}