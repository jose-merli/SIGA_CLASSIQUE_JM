
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import java.util.Vector;


/**
* Bean de administracion de la tabla SCS_INCLUSIONGUARDIASENLISTAS
* 
* @author david.sanchezp
* @since 27/12/2004
*/

public class ScsInclusionGuardiasEnListasAdm extends MasterBeanAdministrador {


	/**
 	 * Constructor del bean de administracion de la tabla.
	 * @param Integer usuario: usuario.
	 */
	public ScsInclusionGuardiasEnListasAdm (UsrBean usuario) {
		super( ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION, 			ScsInclusionGuardiasEnListasBean.C_IDTURNO,					
							ScsInclusionGuardiasEnListasBean.C_IDGUARDIA,				ScsInclusionGuardiasEnListasBean.C_IDLISTA,		
							ScsInclusionGuardiasEnListasBean.C_ORDEN,					ScsInclusionGuardiasEnListasBean.C_FECHAMODIFICACION, 
							ScsInclusionGuardiasEnListasBean.C_USUMODIFICACION};
		return campos;
	}
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION, ScsInclusionGuardiasEnListasBean.C_IDTURNO,
							ScsInclusionGuardiasEnListasBean.C_IDGUARDIA, ScsInclusionGuardiasEnListasBean.C_IDLISTA,
							ScsInclusionGuardiasEnListasBean.C_ORDEN };
		return campos;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsInclusionGuardiasEnListasBean bean = null;
		try{
			bean = new ScsInclusionGuardiasEnListasBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsInclusionGuardiasEnListasBean.C_IDTURNO));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsInclusionGuardiasEnListasBean.C_IDGUARDIA));
			bean.setIdLista(UtilidadesHash.getInteger(hash,ScsInclusionGuardiasEnListasBean.C_IDLISTA));
			bean.setOrden(UtilidadesHash.getInteger(hash,ScsInclusionGuardiasEnListasBean.C_ORDEN));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;	
	}
	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			String aux="";
			ScsInclusionGuardiasEnListasBean b = (ScsInclusionGuardiasEnListasBean) bean;
			hash.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsInclusionGuardiasEnListasBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsInclusionGuardiasEnListasBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA, String.valueOf(b.getIdLista()));
			hash.put(ScsInclusionGuardiasEnListasBean.C_ORDEN, String.valueOf(b.getOrden()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

		
	protected String[] getOrdenCampos() {
		return null;
	}
	
	/** 
	 * Devuelve un String con un numero que indica si la posicion de esa guardia esta ya usada.
	 * Si ese numero es mayor que cero es que el numero de orden esta cogido por otra guardia. 
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String comprobarPosicionGuardias(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idlista="", orden="";
		
		try {
			//Datos de la consulta obtenidos de una tabla hash
			idinstitucion = (String)hash.get("IDINSTITUCION");
			idlista = (String)hash.get("IDLISTA");
			orden = (String)hash.get("ORDEN");

			consulta = "SELECT COUNT(*) AS TOTAL FROM "+ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND "+ScsInclusionGuardiasEnListasBean.C_IDLISTA+"="+idlista;
			consulta += " AND "+ScsInclusionGuardiasEnListasBean.C_ORDEN+"="+orden;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsInclusionGuardiasEnListasAdm.comprobarPosicionGuardias(). Consulta SQL:"+consulta);
		}
		
		return consulta;
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
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsInclusionGuardiasEnListasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	
	
}