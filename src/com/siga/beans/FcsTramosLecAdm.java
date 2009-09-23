//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsTramosLecAdm extends MasterBeanAdministrador {

	
	public FcsTramosLecAdm(UsrBean usuario) {
		super(FcsTramosLecBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsTramosLecBean.C_FECHAMODIFICACION,		FcsTramosLecBean.C_IDTRAMOLEC,
							FcsTramosLecBean.C_MAXIMOSMI,				FcsTramosLecBean.C_MINIMOSMI,
							FcsTramosLecBean.C_RETENCION,				FcsTramosLecBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsTramosLecBean.C_IDTRAMOLEC};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String[] campos = {FcsTramosLecBean.C_MINIMOSMI};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsTramosLecBean bean = null;
		
		try {
			bean = new FcsTramosLecBean();
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsTramosLecBean.C_FECHAMODIFICACION));
			bean.setIdTramoLec		(UtilidadesHash.getInteger(hash,FcsTramosLecBean.C_IDTRAMOLEC));
			bean.setMaximosMi		(UtilidadesHash.getInteger(hash,FcsTramosLecBean.C_MAXIMOSMI));
			bean.setMinimosMi		(UtilidadesHash.getInteger(hash,FcsTramosLecBean.C_MINIMOSMI));
			bean.setRetencion		(UtilidadesHash.getInteger(hash,FcsTramosLecBean.C_RETENCION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsTramosLecBean.C_USUMODIFICACION));
			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsTramosLecBean b = (FcsTramosLecBean) bean;
			UtilidadesHash.set(htData, FcsTramosLecBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsTramosLecBean.C_IDTRAMOLEC, b.getIdTramoLec().toString());
			UtilidadesHash.set(htData, FcsTramosLecBean.C_MAXIMOSMI, b.getMaximosMi().toString());
			UtilidadesHash.set(htData, FcsTramosLecBean.C_MINIMOSMI, b.getMinimosMi().toString());
			UtilidadesHash.set(htData, FcsTramosLecBean.C_RETENCION, b.getRetencion().toString());
			UtilidadesHash.set(htData, FcsTramosLecBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsTramosLecBean.C_USUMODIFICACION, b.getUsuMod().toString());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsTramosLecAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Calcular el identificador del movimiento que se va a insertar. Necesita que el hashtable que se le pasa
	 * tenga una key IdInstitucion con el cod de institucion del usuario logado  
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX("+ FcsTramosLecBean.C_IDTRAMOLEC+") + 1) AS "+ FcsTramosLecBean.C_IDTRAMOLEC+" FROM " + nombreTabla;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(FcsTramosLecBean.C_IDTRAMOLEC).equals("")) {
					entrada.put(FcsTramosLecBean.C_IDTRAMOLEC,"1");
				}
				else entrada.put(FcsTramosLecBean.C_IDTRAMOLEC,(String)prueba.get(FcsTramosLecBean.C_IDTRAMOLEC));								
			}
		}	
		catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error en clase FcsTramosLecAdm 'prepararInsert()'" + e.getErrorType());		
		}
		return entrada;
	}
	
	/** 
	 * Devuelve un boolean indicando si un intervalo de tramos de retenciones se solapa o no con otro
	 * true: se solapa
	 * false: no se solapa  
	 * 
	 * @param desde String valor minimo del tramo
	 * @param hasta String valor maximo del tramo
	 * @return resultado boolean true si el numero de solapamientos es 0.
	 */
	public boolean solapadoCon (String desde, String hasta, String idTramo){
		
		//resultado a devolver
		boolean resultado=false;
		
		//variables que haran falta
		FcsTramosLecAdm tramoAdm = new FcsTramosLecAdm (this.usrbean);
		Hashtable contador = new Hashtable();
		
		//preparamos la consulta
		String consulta = " select count (*) CONTADOR from " + FcsTramosLecBean.T_NOMBRETABLA + " " +
							" where ((" + FcsTramosLecBean.C_MINIMOSMI + " <= " + desde + " " +
							" and " + FcsTramosLecBean.C_MAXIMOSMI + " >" + desde + " ) or " + 
							" (" + FcsTramosLecBean.C_MINIMOSMI + " < " + hasta + " " + 
							" and " + FcsTramosLecBean.C_MAXIMOSMI + " >=" + hasta + ")) " +
							" and " + FcsTramosLecBean.C_IDTRAMOLEC + " <> " + idTramo + " ";
		
		//ejecutamos la consulta
		try{
			contador = (Hashtable)((Vector)tramoAdm.selectGenerico(consulta)).get(0);
			//devolverá true si el contador es = 0
			resultado = (!((String)contador.get("CONTADOR")).equalsIgnoreCase("0"));
		}catch(Exception e){
			resultado = false;
		}
		
		return resultado;
	}
}


