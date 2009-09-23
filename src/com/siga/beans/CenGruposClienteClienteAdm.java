/*
 * VERSIONES:
 * raul.ggonzalez - 21-01-2005 - Creación
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
public class CenGruposClienteClienteAdm extends MasterBeanAdmVisible {

	/** 
	 * Constructor para modificacion
	 * @param  usu  identificador de usuario de modificacion
	 */
	 
	public CenGruposClienteClienteAdm (UsrBean usu) {
		super (CenGruposClienteClienteBean.T_NOMBRETABLA, usu);
	}

	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenGruposClienteClienteAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenGruposClienteClienteBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	
	
	
	/** 
	 * Obtiene los campos del bean 
	 * @return array de string con los nombres de los campos 
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenGruposClienteClienteBean.C_IDPERSONA, 		
				    		CenGruposClienteClienteBean.C_IDINSTITUCION,
				    		CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO,
							CenGruposClienteClienteBean.C_IDGRUPO,
							CenGruposClienteClienteBean.C_FECHAMODIFICACION,
							CenGruposClienteClienteBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 * Obtiene los campos clave del bean 
	 * @return array de string con los nombres de los campos clave
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenGruposClienteClienteBean.C_IDPERSONA, 
							CenGruposClienteClienteBean.C_IDINSTITUCION,
							CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO,
							CenGruposClienteClienteBean.C_IDGRUPO};
		return claves;
	}
	
	/** 
	 * Convierte un hashtable en bean 
	 * @param hash con los datos
	 * @return MasterBean
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenGruposClienteClienteBean bean = null;
		
		try {
			bean = new CenGruposClienteClienteBean();
			bean.setIdPersona		   (UtilidadesHash.getLong(hash, CenGruposClienteClienteBean.C_IDPERSONA));
			bean.setIdInstitucion	   (UtilidadesHash.getInteger(hash, CenGruposClienteClienteBean.C_IDINSTITUCION));
			bean.setIdInstitucionGrupo (UtilidadesHash.getInteger(hash, CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO));
			bean.setIdGrupo			   (UtilidadesHash.getInteger(hash, CenGruposClienteClienteBean.C_IDGRUPO));
			bean.setFechaMod		   (UtilidadesHash.getString(hash, CenGruposClienteClienteBean.C_FECHAMODIFICACION));
			bean.setUsuMod			   (UtilidadesHash.getInteger(hash, CenGruposClienteClienteBean.C_USUMODIFICACION));
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
			CenGruposClienteClienteBean b = (CenGruposClienteClienteBean) bean;
			UtilidadesHash.set(htData, CenGruposClienteClienteBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, CenGruposClienteClienteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO, b.getIdInstitucionGrupo());
			UtilidadesHash.set(htData, CenGruposClienteClienteBean.C_IDGRUPO, b.getIdGrupo());
			UtilidadesHash.set(htData, CenGruposClienteClienteBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenGruposClienteClienteBean.C_USUMODIFICACION, b.getUsuMod());
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
			throw new ClsExceptions (e, "Excepcion en CenGruposClientesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/** Metodo para buscar los grupos fijos para una institucion y una persona. 
	 *  @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaGruposFijoPersona(String idInstitucion, String idPersona,String lenguaje) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT ";
			select += "  F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+") "+CenGruposClienteBean.C_NOMBRE ;
			
			select += " , grupoCliente."+CenGruposClienteClienteBean.C_IDGRUPO;
			select += " , grupoCliente."+CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO+" AS IDINSTITUCION_GRUPO ";
			
			//FROM:
			select += " FROM "+CenGruposClienteBean.T_NOMBRETABLA+" grupo,";
			select += CenGruposClienteClienteBean.T_NOMBRETABLA+" grupoCliente";

			//Filtro:
			select += " WHERE grupoCliente."+CenGruposClienteClienteBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND grupoCliente."+CenGruposClienteClienteBean.C_IDPERSONA+"="+idPersona;
			select += " AND grupoCliente."+CenGruposClienteClienteBean.C_IDGRUPO+"=grupo."+CenGruposClienteBean.C_IDGRUPO;
			select += " AND grupoCliente."+CenGruposClienteClienteBean.C_IDINSTITUCION_GRUPO+"=grupo."+CenGruposClienteBean.C_IDINSTITUCION;	
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
}