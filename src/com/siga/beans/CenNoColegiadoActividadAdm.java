/*
 * VERSIONES:
 * FERNANDO.GOMEZ - 25-04-2008 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Clase Administradora de la tabla CEN_GRUPOSCLIENTE_CLIENTE
 * @author AtosOrigin 21-01-2005
 */

// RGG cambio visibilidad  public class CenGruposClienteClienteAdm extends MasterBeanAdministrador {
public class CenNoColegiadoActividadAdm extends MasterBeanAdmVisible {

	/** 
	 * Constructor para modificacion
	 * @param  usu  identificador de usuario de modificacion
	 */
	 
	public CenNoColegiadoActividadAdm (UsrBean usu) {
		super (CenNoColegiadoActividadBean.T_NOMBRETABLA, usu);
	}

	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenNoColegiadoActividadAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenNoColegiadoActividadBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	
	
	
	/** 
	 * Obtiene los campos del bean 
	 * @return array de string con los nombres de los campos 
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenNoColegiadoActividadBean.C_IDPERSONA, 		
							CenNoColegiadoActividadBean.C_IDINSTITUCION,
							CenNoColegiadoActividadBean.C_IDACTIVIDADPROFESIONAL,
							CenNoColegiadoActividadBean.C_FECHAMODIFICACION,
							CenNoColegiadoActividadBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 * Obtiene los campos clave del bean 
	 * @return array de string con los nombres de los campos clave
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenNoColegiadoActividadBean.C_IDPERSONA, 
							CenNoColegiadoActividadBean.C_IDINSTITUCION,
							CenNoColegiadoActividadBean.C_IDACTIVIDADPROFESIONAL};
		return claves;
	}
	
	/** 
	 * Convierte un hashtable en bean 
	 * @param hash con los datos
	 * @return MasterBean
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenNoColegiadoActividadBean bean = null;
		
		try {
			bean = new CenNoColegiadoActividadBean();
			bean.setIdPersona		   (UtilidadesHash.getLong(hash, CenNoColegiadoActividadBean.C_IDPERSONA));
			bean.setIdInstitucion	   (UtilidadesHash.getInteger(hash, CenNoColegiadoActividadBean.C_IDINSTITUCION));
			bean.setIdActividadProfesional (UtilidadesHash.getInteger(hash, CenNoColegiadoActividadBean.C_IDACTIVIDADPROFESIONAL));
			bean.setFechaMod		   (UtilidadesHash.getString(hash, CenNoColegiadoActividadBean.C_FECHAMODIFICACION));
			bean.setUsuMod			   (UtilidadesHash.getInteger(hash, CenNoColegiadoActividadBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Convierte un bean en hashtable 
	 * @param MasterBean con los datos
	 * @return hashtable con los datos
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenNoColegiadoActividadBean b = (CenNoColegiadoActividadBean) bean;
			UtilidadesHash.set(htData, CenNoColegiadoActividadBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, CenNoColegiadoActividadBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenNoColegiadoActividadBean.C_IDACTIVIDADPROFESIONAL, b.getIdActividadProfesional());
			UtilidadesHash.set(htData, CenNoColegiadoActividadBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenNoColegiadoActividadBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Obtiene los campos clave del bean 
	 * @return array de string con los nombres de los campos clave
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenNoColegiadoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/** Metodo para buscar los grupos fijos para una institucion y una persona. 
	 *  @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaGruposFijoPersona(String idInstitucion, String idPersona) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT f_siga_getrecurso(grupo."+CenActividadProfesionalBean.C_DESCRIPCION+","+this.usrbean.getLanguage()+") AS NOMBRE";
			select += " , grupo."+CenActividadProfesionalBean.C_DESCRIPCION+"";
			select += " , grupoCliente."+CenNoColegiadoActividadBean.C_IDACTIVIDADPROFESIONAL;
			
			//FROM:
			select += " FROM "+CenActividadProfesionalBean.T_NOMBRETABLA+" grupo,";
			select += CenNoColegiadoActividadBean.T_NOMBRETABLA+" grupoCliente";

			//Filtro:
			select += " WHERE grupoCliente."+CenNoColegiadoActividadBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND grupoCliente."+CenNoColegiadoActividadBean.C_IDPERSONA+"="+idPersona;
			select += " AND grupoCliente."+CenNoColegiadoActividadBean.C_IDACTIVIDADPROFESIONAL+"=grupo."+CenActividadProfesionalBean.C_IDACTIVIDADPROFESIONAL;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
}