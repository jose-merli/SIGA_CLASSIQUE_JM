/**
 * VERSIONES:
 * 
 * jose.barrientos - 29/01/2009 Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

//import org.apache.batik.dom.util.HashTable;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla Fcs_DestinatarioRetenciones de la BBDD
* 
*/
public class FcsDestinatariosRetencionesAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FcsDestinatariosRetencionesAdm(UsrBean usu) {
		super(FcsDestinatariosRetencionesBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 *  
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FcsDestinatariosRetencionesBean.C_IDINSTITUCION,
							FcsDestinatariosRetencionesBean.C_IDDESTINATARIO,
							FcsDestinatariosRetencionesBean.C_NOMBRE,
							FcsDestinatariosRetencionesBean.C_CODIGOEXT,
							FcsDestinatariosRetencionesBean.C_CUENTACONTABLE,
							FcsDestinatariosRetencionesBean.C_ORDEN,
							FcsDestinatariosRetencionesBean.C_FECHAMODIFICACION,
							FcsDestinatariosRetencionesBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FcsDestinatariosRetencionesBean.C_IDINSTITUCION,
							FcsDestinatariosRetencionesBean.C_IDDESTINATARIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * 
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  FcsDestinatariosRetencionesBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FcsDestinatariosRetencionesBean bean = null;
		
		try {
			bean = new FcsDestinatariosRetencionesBean();
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,FcsDestinatariosRetencionesBean.C_IDINSTITUCION));
			bean.setIdDestinatario(UtilidadesHash.getInteger(hash,FcsDestinatariosRetencionesBean.C_IDDESTINATARIO));
			bean.setNombre (UtilidadesHash.getString(hash,FcsDestinatariosRetencionesBean.C_NOMBRE));
			bean.setCodigoExt(UtilidadesHash.getString(hash,FcsDestinatariosRetencionesBean.C_CODIGOEXT));
			bean.setCuentaContable(UtilidadesHash.getString(hash,FcsDestinatariosRetencionesBean.C_CUENTACONTABLE));
			bean.setOrden(UtilidadesHash.getString(hash,FcsDestinatariosRetencionesBean.C_ORDEN));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsDestinatariosRetencionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsDestinatariosRetencionesBean.C_USUMODIFICACION));	
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * 
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsDestinatariosRetencionesBean b = (FcsDestinatariosRetencionesBean) bean;
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_IDDESTINATARIO,b.getIdDestinatario());
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_CODIGOEXT,b.getCodigoExt());
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_CUENTACONTABLE,b.getCuentaContable());
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_ORDEN,b.getOrden());
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,FcsDestinatariosRetencionesBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	
	/**
	 * Busca destinatarios dentro de una institucion segun su nombre o parte de este
	 * 
	 * @param idInstitucion
	 * @param nombre
	 * @return Vector con los destinatarios correspondientes a la busqueda
	 * @throws ClsExceptions
	 */
	public Vector buscarDestinatario(String idInstitucion, String nombre) throws ClsExceptions {
		Vector v = new Vector();
		String consulta = "";
		try{
			consulta =  "select * from " + FcsDestinatariosRetencionesBean.T_NOMBRETABLA; 
			consulta += " where "+ FcsDestinatariosRetencionesBean.C_IDINSTITUCION +"="+ idInstitucion;
			// Solo incluiremos la busqueda por nombre cuando se haya introducido algo en el campo
			if((nombre!=null)&&(!nombre.equalsIgnoreCase(""))){
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),FcsDestinatariosRetencionesBean.T_NOMBRETABLA+"."+FcsDestinatariosRetencionesBean.C_NOMBRE);
			}
			// Devolvemos el resultado ordenado por el campo ORDEN
			consulta +=" order by orden";
			
			// v = this.selectGenerico(consulta);
			RowsContainer rc = this.findNLS(consulta);
			if(rc.size()>0) v = rc.getAll();
			
		}catch (Exception e){
			throw new ClsExceptions (e, "error");
		}
		return v;
	}
	
	/**
	 * Nos devuelve el destinatario asociado a una institucion por su numero
	 * 
	 * @param idInstitucion
	 * @param idDestinatario
	 * @return Hashtable con todos los datos del destinatario
	 * @throws ClsExceptions
	 */
	public Hashtable getDestinatario(String idInstitucion, String idDestinatario) throws ClsExceptions {
		String consulta = "";
		Hashtable codigos = new Hashtable();
		Hashtable registro = null;
		int contador = 0;
		try{
			contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			// Recuperamos todos los datos del destinatario
			consulta =  "select * from " + FcsDestinatariosRetencionesBean.T_NOMBRETABLA; 
		    consulta += " where "+ FcsDestinatariosRetencionesBean.C_IDINSTITUCION +" =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idDestinatario);
			consulta += " and "+ FcsDestinatariosRetencionesBean.C_IDDESTINATARIO +" =:" + contador;
			RowsContainer rc = this.findBind(consulta, codigos);
	    	if (rc != null && rc.size() > 0) {  
	    	    Row fila = (Row) rc.get(0);
				registro = (Hashtable)fila.getRow(); 
				
			}
		}catch (Exception e){
			throw new ClsExceptions (e, "error");
		}
		return registro;
	}
	
	/**
	 * Genera un nuevo iddestinatario para una institucion
	 * 
	 * @param idInstitucion
	 * @return Integer con el nuevo id
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Integer getNuevoId(String idInstitucion) throws ClsExceptions, SIGAException {
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			// Buscamos el iddestinatario maximo para una institucion y le sumamos 1
			String sql = " SELECT (MAX(" + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO + ") + 1) AS " + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO + 
			  			 " FROM " + FcsDestinatariosRetencionesBean.T_NOMBRETABLA + 
						 " WHERE " + FcsDestinatariosRetencionesBean.T_NOMBRETABLA +"." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION + " = " + idInstitucion;

			rc = this.findForUpdate(sql);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer idDestinatario = UtilidadesHash.getInteger(prueba, FcsDestinatariosRetencionesBean.C_IDDESTINATARIO);
				if (idDestinatario == null) {
					return new Integer(1);
				}
				else return idDestinatario;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;	
	}
}
