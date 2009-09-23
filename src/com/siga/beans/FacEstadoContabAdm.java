/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
*
*/
public class FacEstadoContabAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacEstadoContabAdm(UsrBean usu) {
		super(FacEstadoContabBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacEstadoContabBean.C_IDESTADO,
				FacEstadoContabBean.C_DESCRIPCION,
				FacEstadoContabBean.C_LENGUAJE};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacEstadoContabBean.C_IDESTADO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = {FacEstadoContabBean.C_IDESTADO};
		return orden;
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacEstadoContabBean bean = null;
		
		try {
			bean = new FacEstadoContabBean();
			bean.setIdEstado(UtilidadesHash.getInteger(hash,FacEstadoContabBean.C_IDESTADO));			
			bean.setDescripcion(UtilidadesHash.getString(hash,FacEstadoContabBean.C_DESCRIPCION));
			bean.setLenguaje(UtilidadesHash.getString(hash,FacEstadoContabBean.C_LENGUAJE));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacEstadoContabBean b = (FacEstadoContabBean) bean;
			UtilidadesHash.set(htData,FacEstadoContabBean.C_IDESTADO,b.getIdEstado());
			UtilidadesHash.set(htData,FacEstadoContabBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData,FacEstadoContabBean.C_LENGUAJE,b.getLenguaje());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	public String obtenerDescripcion(String idEstado, String idLenguaje) throws SIGAException, ClsExceptions
	{
		String valor = "";
		Hashtable codigos = new Hashtable();
		try
		{
			codigos.put(new Integer(1),idLenguaje);
			codigos.put(new Integer(2),idEstado);
			String sql = " SELECT f_siga_getrecurso_etiqueta(DESCRIPCION,:1) AS DESCRIPCION FROM FAC_ESTADOCONTAB WHERE IDESTADO=:2";
			RowsContainer rc =this.findBind(sql,codigos);
			if(rc!=null && rc.size()>0){
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc.get(i);
					Hashtable htAux=r1.getRow();
					valor = (String) htAux.get("DESCRIPCION");
				}
			}
		}
		catch(Exception e)
		{
		    throw new ClsExceptions(e,"Error al obtener asiento contable servicio");
		}
		return valor;
	}

}
