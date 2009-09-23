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
//import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacEstadoConfirmFactAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacEstadoConfirmFactAdm(UsrBean usu) {
		super(FacEstadoConfirmFactBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacEstadoConfirmFactBean.C_IDESTADO,
				FacEstadoConfirmFactBean.C_ALIAS,
				FacEstadoConfirmFactBean.C_DESCRIPCION,
				FacEstadoConfirmFactBean.C_TIPO,
				FacEstadoConfirmFactBean.C_LENGUAJE};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacEstadoConfirmFactBean.C_IDESTADO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = {FacEstadoConfirmFactBean.C_TIPO, FacEstadoConfirmFactBean.C_IDESTADO};
		return orden;
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacEstadoConfirmFactBean bean = null;
		
		try {
			bean = new FacEstadoConfirmFactBean();
			bean.setIdEstado(UtilidadesHash.getInteger(hash,FacEstadoConfirmFactBean.C_IDESTADO));			
			bean.setAlias(UtilidadesHash.getString(hash,FacEstadoConfirmFactBean.C_ALIAS));
			bean.setDescripcion(UtilidadesHash.getString(hash,FacEstadoConfirmFactBean.C_DESCRIPCION));
			bean.setTipo(UtilidadesHash.getString(hash,FacEstadoConfirmFactBean.C_TIPO));
			bean.setLenguaje(UtilidadesHash.getString(hash,FacEstadoConfirmFactBean.C_LENGUAJE));
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
			FacEstadoConfirmFactBean b = (FacEstadoConfirmFactBean) bean;
			UtilidadesHash.set(htData,FacEstadoConfirmFactBean.C_IDESTADO,b.getIdEstado());
			UtilidadesHash.set(htData,FacEstadoConfirmFactBean.C_ALIAS,b.getAlias()); 
			UtilidadesHash.set(htData,FacEstadoConfirmFactBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData,FacEstadoConfirmFactBean.C_TIPO,b.getTipo());
			UtilidadesHash.set(htData,FacEstadoConfirmFactBean.C_LENGUAJE,b.getLenguaje());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	public String getDescripcion(String idEstado,String tipo,String lenguaje) throws ClsExceptions {
		String salida = "No encontrada";
		try {
			Hashtable ht=new Hashtable();
			ht.put(FacEstadoConfirmFactBean.C_IDESTADO,idEstado);
			ht.put(FacEstadoConfirmFactBean.C_TIPO,tipo.toUpperCase());
			Vector v = this.select(ht);
			if (v!=null && v.size()>0) {
				FacEstadoConfirmFactBean bean = (FacEstadoConfirmFactBean)v.get(0);
				salida=UtilidadesString.getMensajeIdioma(bean.getLenguaje().toUpperCase(),bean.getDescripcion());
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al obtener la descripcion del estado tipo "+tipo);
		}
		return salida;
	}
	
	public Hashtable getEstadosConfirmacionFacturacion(String lenguaje) throws ClsExceptions {
		Hashtable htEstados=new Hashtable();
		try {
			
			Vector v = this.select();
			
			if (v!=null && v.size()>0) {
				for (int i = 0; i < v.size(); i++) {
					FacEstadoConfirmFactBean bean = (FacEstadoConfirmFactBean)v.get(i);
					htEstados.put(bean.getIdEstado().toString(), UtilidadesString.getMensajeIdioma(lenguaje,bean.getDescripcion()));
						
				}
				
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al obtener la descripcion del estado tipo ");
		}
		return htEstados;
	}
	
	
	/** 
	 * Obtiene los importes apuntados en compras para una institución y abono, y la cuenta contable del producto o servicio. 
	 * @param  institucion - identificador de la institucion
	 * @param  idAbono - Identificador de abono	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	/* Ejemplo de metodo	
	public Vector obtenerCuentasValorPYS (String institucion, String idAbono) throws ClsExceptions {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql =" select NVL(ROUND(a.CANTIDAD * a.PRECIOUNITARIO, 2), 0) NETO, c.CUENTACONTABLE CUENTA "; 
	            	sql +=" from   fac_lineafactura a, pys_compra b, pys_productosinstitucion c";
		            sql +="  where  a.IDFACTURA = b.IDFACTURA";
			        sql +="  and    a.NUMEROLINEA = b.NUMEROLINEA";
				    sql +="  and    a.IDINSTITUCION = b.IDINSTITUCION";
					sql +="  and    b.IDINSTITUCION = c.IDINSTITUCION  ";
					sql +="  and    b.IDTIPOPRODUCTO = c.IDTIPOPRODUCTO ";
					sql +="  and    b.IDPRODUCTO = c.IDPRODUCTO";
					sql +="  and    b.IDPRODUCTOINSTITUCION = c.IDPRODUCTOINSTITUCION";
					sql +="  and    a.IDFACTURA IN (SELECT E.IDFACTURA FROM FAC_ABONO E"; 
					sql +="  					   WHERE E.IDINSTITUCION=a.IDINSTITUCION";
					sql +=" AND	 E.IDABONO = "+idAbono+")";
					sql +="  and    a.IDINSTITUCION = "+institucion;
					sql +=" union all ";
					sql +="   select NVL(ROUND(a2.CANTIDAD * a2.PRECIOUNITARIO, 2), 0) NETO, c2.CUENTACONTABLE CUENTA "; 
					sql +="  from   fac_lineafactura a2, fac_facturacionsuscripcion b2, pys_serviciosinstitucion c2";
					sql +="  where  a2.IDFACTURA = b2.IDFACTURA";
					sql +="  and    a2.NUMEROLINEA = b2.NUMEROLINEA";
					sql +="  and    a2.IDINSTITUCION = b2.IDINSTITUCION";
					sql +="  and    b2.IDINSTITUCION = c2.IDINSTITUCION  ";
					sql +="  and    b2.IDTIPOSERVICIOS = c2.IDTIPOSERVICIOS"; 
					sql +="  and    b2.IDSERVICIO = c2.IDSERVICIO";
					sql +="  and    b2.IDSERVICIOSINSTITUCION = c2.IDSERVICIOSINSTITUCION";
					sql +="  and    a2.IDFACTURA IN (SELECT E2.IDFACTURA FROM FAC_ABONO E2 ";
					sql +="  					   WHERE E2.IDINSTITUCION=a2.IDINSTITUCION";
					sql +=" 					   AND	 E2.IDABONO = "+idAbono+")";
                    sql +="  and    a2.IDINSTITUCION = " + institucion;
                    

	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return datos;                        
	    }
*/
}
