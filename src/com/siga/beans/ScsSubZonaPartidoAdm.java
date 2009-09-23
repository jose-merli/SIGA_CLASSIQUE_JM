/*
 * VERSIONES:
 * raul.ggonzalez - 21-01-2005 - Creación
 */

package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Clase Administradora de la tabla SCS_SUBZONAPARTIDO
 * @author AtosOrigin 21-01-2005
 */

// RGG cambio visibilidad  public class CenSubZonaPartidoAdm extends MasterBeanAdministrador {
public class ScsSubZonaPartidoAdm extends MasterBeanAdmVisible {

	/** 
	 * Constructor para modificacion
	 * @param  usu  identificador de usuario de modificacion
	 */
	 
	public ScsSubZonaPartidoAdm (UsrBean usu) {
		super (ScsSubZonaPartidoBean.T_NOMBRETABLA, usu);
	}

	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
/*
	public ScsSubZonaPartidoAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( ScsSubZonaPartidoBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	
*/	
	
	/** 
	 * Obtiene los campos del bean 
	 * @return array de string con los nombres de los campos 
	 */
	protected String[] getCamposBean() {
		String [] campos = {ScsSubZonaPartidoBean.C_IDPARTIDO, 		
				    		ScsSubZonaPartidoBean.C_IDINSTITUCION,
				    		ScsSubZonaPartidoBean.C_IDSUBZONA,
							ScsSubZonaPartidoBean.C_IDZONA,
							ScsSubZonaPartidoBean.C_FECHAMODIFICACION,
							ScsSubZonaPartidoBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 * Obtiene los campos clave del bean 
	 * @return array de string con los nombres de los campos clave
	 */
	protected String[] getClavesBean() {
		String [] claves = {ScsSubZonaPartidoBean.C_IDPARTIDO, 
							ScsSubZonaPartidoBean.C_IDINSTITUCION,
							ScsSubZonaPartidoBean.C_IDSUBZONA,
							ScsSubZonaPartidoBean.C_IDZONA};
		return claves;
	}
	
	/** 
	 * Convierte un hashtable en bean 
	 * @param hash con los datos
	 * @return MasterBean
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ScsSubZonaPartidoBean bean = null;
		
		try {
			bean = new ScsSubZonaPartidoBean();
			bean.setIdPartido		   (UtilidadesHash.getInteger(hash, ScsSubZonaPartidoBean.C_IDPARTIDO));
			bean.setIdInstitucion	   (UtilidadesHash.getInteger(hash, ScsSubZonaPartidoBean.C_IDINSTITUCION));
			bean.setIdSubZona		   (UtilidadesHash.getInteger(hash, ScsSubZonaPartidoBean.C_IDSUBZONA));
			bean.setIdZona			   (UtilidadesHash.getInteger(hash, ScsSubZonaPartidoBean.C_IDZONA));
			bean.setFechaMod		   (UtilidadesHash.getString(hash, ScsSubZonaPartidoBean.C_FECHAMODIFICACION));
			bean.setUsuMod			   (UtilidadesHash.getInteger(hash, ScsSubZonaPartidoBean.C_USUMODIFICACION));
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
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsSubZonaPartidoBean b = (ScsSubZonaPartidoBean) bean;
			UtilidadesHash.set(htData, ScsSubZonaPartidoBean.C_IDPARTIDO, b.getIdPartido());
			UtilidadesHash.set(htData, ScsSubZonaPartidoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsSubZonaPartidoBean.C_IDSUBZONA, b.getIdSubZona());
			UtilidadesHash.set(htData, ScsSubZonaPartidoBean.C_IDZONA, b.getIdZona());
			UtilidadesHash.set(htData, ScsSubZonaPartidoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ScsSubZonaPartidoBean.C_USUMODIFICACION, b.getUsuMod());
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
}

