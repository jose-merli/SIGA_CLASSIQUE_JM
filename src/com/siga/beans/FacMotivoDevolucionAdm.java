/*
 * VERSIONES:
 * RGG 03/01/2007 Creacion
 */
package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
 *  Administrador de la tabla FAC_MOTIVODEVOLUCION
 *  @author RGG AtosOrigin
 */
public class FacMotivoDevolucionAdm extends MasterBeanAdministrador
{
	public FacMotivoDevolucionAdm(UsrBean usuario)
	{
	    super(FacMotivoDevolucionBean.T_NOMBRETABLA, usuario);
	}


	protected String[] getCamposBean()
	{
		String[] campos = {FacMotivoDevolucionBean.C_IDMOTIVO,
							FacMotivoDevolucionBean.C_CODIGO,
							FacMotivoDevolucionBean.C_NOMBRE};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {FacMotivoDevolucionBean.C_IDMOTIVO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		FacMotivoDevolucionBean bean = null;

		try
		{
			bean = new FacMotivoDevolucionBean();
			
			bean.setIdMotivo(UtilidadesHash.getString(hash, FacMotivoDevolucionBean.C_IDMOTIVO));
			bean.setCodigo(UtilidadesHash.getString(hash, FacMotivoDevolucionBean.C_CODIGO));
			bean.setNombre(UtilidadesHash.getString(hash, FacMotivoDevolucionBean.C_NOMBRE));
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

			FacMotivoDevolucionBean b = (FacMotivoDevolucionBean) bean;

			UtilidadesHash.set(htData, FacMotivoDevolucionBean.C_IDMOTIVO, b.getIdMotivo());
			UtilidadesHash.set(htData, FacMotivoDevolucionBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(htData, FacMotivoDevolucionBean.C_NOMBRE, b.getNombre());
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
		String[] orden = {FacMotivoDevolucionBean.C_NOMBRE};

		return orden;
    }
    
    /**
     * Obtiene los motivos de devolucion
     * @param idioma
     * @return
     * @throws ClsExceptions
     * @throws SIGAException
     */
    public Vector obtenerMotivosDevolucion(String idioma) throws ClsExceptions, SIGAException {
    	String sql = "SELECT " + FacMotivoDevolucionBean.C_CODIGO + " AS ID," + 
    					" F_SIGA_GETRECURSO(" + FacMotivoDevolucionBean.C_NOMBRE + ", " + idioma + ") AS DESCRIPCION" +
    				" FROM " + FacMotivoDevolucionBean.T_NOMBRETABLA +
    				" ORDER BY DESCRIPCION"; 
    	
    	return this.selectGenerico(sql);
    }
    
}