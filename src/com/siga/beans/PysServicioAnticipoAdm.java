/*
 * VERSIONES:
 * 
 * jose.barrientos	- 28-11-2008 - Creacion
 *	
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class PysServicioAnticipoAdm extends MasterBeanAdministrador {
	
	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysServicioAnticipoAdm(UsrBean usu) {
		super(PysServicioAnticipoBean.T_NOMBRETABLA, usu);
	}
	
	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {		
			PysServicioAnticipoBean.C_IDINSTITUCION,
			PysServicioAnticipoBean.C_IDPERSONA,
			PysServicioAnticipoBean.C_IDANTICIPO,
			PysServicioAnticipoBean.C_IDTIPOSERVICIOS,
			PysServicioAnticipoBean.C_IDSERVICIO,
			PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION,
			PysServicioAnticipoBean.C_USUMODIFICACION,
			PysServicioAnticipoBean.C_FECHAMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {
			PysServicioAnticipoBean.C_IDINSTITUCION, 
			PysServicioAnticipoBean.C_IDPERSONA, 
			PysServicioAnticipoBean.C_IDANTICIPO, 
			PysServicioAnticipoBean.C_IDTIPOSERVICIOS, 
			PysServicioAnticipoBean.C_IDSERVICIO, 
			PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysServicioAnticipoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysServicioAnticipoBean bean = null;
		
		try {
			bean = new PysServicioAnticipoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysServicioAnticipoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,PysServicioAnticipoBean.C_IDPERSONA));			
			bean.setIdAnticipo(UtilidadesHash.getInteger(hash,PysServicioAnticipoBean.C_IDANTICIPO));
			bean.setIdTipoServicio(UtilidadesHash.getInteger(hash,PysServicioAnticipoBean.C_IDTIPOSERVICIOS));	
			bean.setIdServicio(UtilidadesHash.getInteger(hash,PysServicioAnticipoBean.C_IDSERVICIO));	
			bean.setIdServiciosInstitucion(UtilidadesHash.getInteger(hash,PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION));
			
			bean.setUsuMod (UtilidadesHash.getInteger (hash, PysServicioAnticipoBean.C_USUMODIFICACION));
			bean.setFechaMod (UtilidadesHash.getString (hash, PysServicioAnticipoBean.C_FECHAMODIFICACION));
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
			PysServicioAnticipoBean b = (PysServicioAnticipoBean) bean; 
			UtilidadesHash.set(htData,PysServicioAnticipoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,PysServicioAnticipoBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData,PysServicioAnticipoBean.C_IDANTICIPO, b.getIdAnticipo());
			UtilidadesHash.set(htData,PysServicioAnticipoBean.C_IDTIPOSERVICIOS, b.getIdTipoServicio());
			UtilidadesHash.set(htData,PysServicioAnticipoBean.C_IDSERVICIO, b.getIdServicio());
			UtilidadesHash.set(htData,PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION, b.getIdServiciosInstitucion());
			
			UtilidadesHash.set (htData, PysServicioAnticipoBean.C_FECHAMODIFICACION,	b.getFechaMod());
			UtilidadesHash.set (htData, PysServicioAnticipoBean.C_USUMODIFICACION,		b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/** 
	 * Obtiene un vector con la lista de servicios configurados para un anticipo
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idAnticipo - identificador del anticipo        
	 * @return  Vector - Vector de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getServiciosAnticipo (String idInstitucion, String idPersona, String idAnticipo) throws ClsExceptions{
		Vector salida = new Vector();
		Hashtable codigos = new Hashtable();
		int contador = 0;
		String where = " select S.IDINSTITUCION, S.IDPERSONA, S.IDANTICIPO, S.IDTIPOSERVICIOS, S.IDSERVICIO, S.IDSERVICIOSINSTITUCION, I.DESCRIPCION";
			where += " from PYS_SERVICIOANTICIPO S, PYS_SERVICIOSINSTITUCION I";
			where += " WHERE S.IDINSTITUCION=I.IDINSTITUCION";
			where += " AND   S.IDTIPOSERVICIOS = I.IDTIPOSERVICIOS";
			where += " AND   S.IDSERVICIO = I.IDSERVICIO";
			where += " AND   S.IDSERVICIOSINSTITUCION = I.IDSERVICIOSINSTITUCION";
			
		try{
			contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			where += " AND   S.idinstitucion=:"+ contador;
			contador ++;
			codigos.put(new Integer(contador), idPersona);
			where += " AND   S.idpersona =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idAnticipo);
			where += " AND   S.idanticipo =:" + contador;	
			where += " ORDER BY I.DESCRIPCION";
			
	    	RowsContainer rc = this.findBind(where, codigos);
	    	if (rc != null && rc.size() > 0) {  
	    	    for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						salida.add(registro);
				}
			}

		}
		catch(Exception e){
			throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
		return salida;	
	}
	
	public static boolean getAnticipoLetradoActivo(String idInstitucion, String idPersona, String idTipoServicios, String idServicio, String idServiciosInstitucion)  throws ClsExceptions{
		boolean salida = false;
		Hashtable codigos = new Hashtable();
		int contador = 0;
		try{
			String sql = "SELECT SA.IDANTICIPO ";
			sql += " FROM pys_servicioanticipo SA, PYS_ANTICIPOLETRADO AL " +
				" WHERE AL.IDINSTITUCION = SA.IDINSTITUCION " +
				" AND AL.IDPERSONA = SA.IDPERSONA " +
				" AND AL.IDANTICIPO = SA.IDANTICIPO "+ 
				" AND (AL.IMPORTEINICIAL - (SELECT NVL(SUM(L.IMPORTEANTICIPADO),0) "+ 
				" 		FROM   PYS_LINEAANTICIPO L  "+
				" 		WHERE  L.IDINSTITUCION=AL.IDINSTITUCION "+
				" 		AND    L.IDPERSONA=AL.IDPERSONA "+
				" 		AND    L.IDANTICIPO=AL.IDANTICIPO "+
				" 		AND    TRUNC(L.FECHAEFECTIVA) < SYSDATE)) > 0 ";
				contador ++;
			codigos.put(new Integer(contador), idInstitucion);
			sql += " AND   SA.idinstitucion=:"+ contador;
			contador ++;
			codigos.put(new Integer(contador), idPersona);
			sql += " AND   SA.idpersona =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idTipoServicios);
			sql += " AND   SA.idtiposervicios =:" + contador;
			contador ++;
			codigos.put(new Integer(contador), idServicio);
			sql += " AND   SA.idservicio =:" + contador;	
			contador ++;
			codigos.put(new Integer(contador), idServiciosInstitucion);
			sql += " AND   SA.idserviciosinstitucion =:" + contador;	
			
			RowsContainer rc= new RowsContainer();
			if(rc.findBind(sql,codigos)){
			    Vector v = rc.getAll();
			    if (v!=null && v.size()>0) {
			        salida = true;
			    }
			}
			
		}catch(Exception e){ 
				throw new ClsExceptions(e,e.toString());
		}
		return salida;
	}
	
}