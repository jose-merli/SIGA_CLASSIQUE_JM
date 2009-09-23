/*
 * VERSIONES:
 * miguel.villegas - 4-2-2005 - Creacion 
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla PYS_PRECIOSSERVICIOS de la BBDD
* 
*/

public class PysPreciosServiciosAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysPreciosServiciosAdm(UsrBean usu) {
		super(PysPreciosServiciosBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysPreciosServiciosBean.C_IDINSTITUCION,
							PysPreciosServiciosBean.C_IDTIPOSERVICIOS ,
							PysPreciosServiciosBean.C_IDSERVICIO,
							PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,
							PysPreciosServiciosBean.C_IDPERIODICIDAD,
							PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,
							PysPreciosServiciosBean.C_VALOR,
							PysPreciosServiciosBean.C_CRITERIOS,
							PysPreciosServiciosBean.C_IDCONSULTA,
							PysPreciosServiciosBean.C_PORDEFECTO,
							PysPreciosServiciosBean.C_FECHAMODIFICACION,
							PysPreciosServiciosBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysPreciosServiciosBean.C_IDINSTITUCION,
							PysPreciosServiciosBean.C_IDTIPOSERVICIOS ,
							PysPreciosServiciosBean.C_IDSERVICIO,
							PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,
							PysPreciosServiciosBean.C_IDPERIODICIDAD,
							PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysPreciosServiciosBean bean = null;
		
		try {
			bean = new PysPreciosServiciosBean();
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,PysPreciosServiciosBean.C_IDINSTITUCION));
			bean.setIdTipoServicios (UtilidadesHash.getInteger(hash,PysPreciosServiciosBean.C_IDTIPOSERVICIOS));
			bean.setIdServicio (UtilidadesHash.getLong(hash,PysPreciosServiciosBean.C_IDSERVICIO));
			bean.setIdServiciosInstitucion (UtilidadesHash.getLong(hash,PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION));
			bean.setIdPeriodicidad (UtilidadesHash.getInteger(hash,PysPreciosServiciosBean.C_IDPERIODICIDAD ));
			bean.setIdPreciosServicios (UtilidadesHash.getInteger(hash,PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS ));
			bean.setValor (UtilidadesHash.getDouble(hash,PysPreciosServiciosBean.C_VALOR));
			bean.setCriterios (UtilidadesHash.getString(hash, PysPreciosServiciosBean.C_CRITERIOS));			
			bean.setIdConsulta (UtilidadesHash.getLong(hash,PysPreciosServiciosBean.C_IDCONSULTA ));
			bean.setPorDefecto (UtilidadesHash.getString(hash, PysPreciosServiciosBean.C_PORDEFECTO));
			bean.setFechaMod(UtilidadesHash.getString(hash,PysPreciosServiciosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysPreciosServiciosBean.C_USUMODIFICACION));			
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
			PysPreciosServiciosBean b = (PysPreciosServiciosBean) bean;
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDINSTITUCION,b.getIdInstitucion ());			
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDSERVICIO,b.getIdServicio());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,b.getIdServiciosInstitucion());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDPERIODICIDAD ,b.getIdPeriodicidad());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS ,b.getIdPreciosServicios());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_VALOR ,b.getValor());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_CRITERIOS ,b.getCriterios());			
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_IDCONSULTA ,b.getIdConsulta());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_PORDEFECTO ,b.getPorDefecto());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,PysPreciosServiciosBean.C_USUMODIFICACION, b.getUsuMod());
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
	 * Recoge los distintos precios asociados a un determinado servicio<br/>
	 * @param  institucion - identificador de la institucion 
	 * @param  tipoServicio - identificador del tipo de servicio
	 * @param  servicio - identificador del servicio	 
	 * @param  servInst - identificador del servicio institucion	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector getPrecios (String institucion, String tipoServicio, String servicio, String servInst) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDINSTITUCION  + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDSERVICIO + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDTIPOSERVICIOS  + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION  + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDPERIODICIDAD + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_VALOR + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_CRITERIOS + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDCONSULTA + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_PORDEFECTO + "," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(PysPeriodicidadBean.T_NOMBRETABLA + "." + PysPeriodicidadBean.C_DESCRIPCION ,this.usrbean.getLanguage())+" AS PERIODICIDAD " +							
							" FROM " + PysPreciosServiciosBean.T_NOMBRETABLA + 
								" INNER JOIN " + PysPeriodicidadBean.T_NOMBRETABLA + 
									" ON " + PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDPERIODICIDAD + "=" + PysPeriodicidadBean.T_NOMBRETABLA + "." + PysPeriodicidadBean.C_IDPERIODICIDAD +  
							" WHERE " +
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDTIPOSERVICIOS + "=" + tipoServicio +
							" AND " +
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDSERVICIO + "=" + servicio +
							" AND " +							
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION + "=" + servInst +
							" AND " +							
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDINSTITUCION + "=" + institucion+
							" ORDER BY " + PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_VALOR + " ";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener la informacion los precios asociados a un servicio.");
	       		}	
	      }
	       return datos;                        
	    }	

	
	public Vector getPreciosPorDefecto (String institucion, String tipoServicio, String servicio, String servInst) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDINSTITUCION  + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDSERVICIO + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDTIPOSERVICIOS  + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION  + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDPERIODICIDAD + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_VALOR + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_CRITERIOS + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDCONSULTA + "," +
	            			PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_PORDEFECTO + "," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(PysPeriodicidadBean.T_NOMBRETABLA + "." + PysPeriodicidadBean.C_DESCRIPCION ,this.usrbean.getLanguage())+" AS PERIODICIDAD " +							
							" FROM " + PysPreciosServiciosBean.T_NOMBRETABLA + 
								" INNER JOIN " + PysPeriodicidadBean.T_NOMBRETABLA + 
									" ON " + PysPreciosServiciosBean.T_NOMBRETABLA + "." + PysPreciosServiciosBean.C_IDPERIODICIDAD + "=" + PysPeriodicidadBean.T_NOMBRETABLA + "." + PysPeriodicidadBean.C_IDPERIODICIDAD +  
							" WHERE " +
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDTIPOSERVICIOS + "=" + tipoServicio +
							" AND " +
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDSERVICIO + "=" + servicio +
							" AND " +							
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION + "=" + servInst +
							" AND " +							
							PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_IDINSTITUCION + "=" + institucion+ 
							" AND " +
							PysPreciosServiciosBean.T_NOMBRETABLA + "."+ PysPreciosServiciosBean.C_PORDEFECTO + " = " + ClsConstants.DB_TRUE +
							" ORDER BY " + PysPreciosServiciosBean.T_NOMBRETABLA +"."+ PysPreciosServiciosBean.C_VALOR + " ";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila.getRow());
	               }
	            } 
	       }
	       catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener la informacion los precios asociados a un servicio.");
	       		}	
	      }
	       return datos;                        
	    }	

	/** 
	 * Obtiene el id para el campo IDPRECIOSSERVICIOS
	 * @param  institucion - id institucion 
	 * @param  tipo - id tipo servicio
	 * @param  servicio - id servicio
	 * @param  servInst - id servicio institucion
	 * @param  periodicidad - id periodicidad
	 * @return  String - identificador solicitado  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public String getIdPreciosServicios (String institucion, String tipo, String servicio, String servInst, String periodicidad)throws ClsExceptions, SIGAException 
	{
		String maximo="0";	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDPRECIOSSERVICIOS)+1) AS IDPRECIOSSERVICIOS FROM " + nombreTabla +
					" WHERE " +
					PysPreciosServiciosBean.C_IDINSTITUCION + "=" + institucion + 
					" AND " +
					PysPreciosServiciosBean.C_IDTIPOSERVICIOS + "=" + tipo + 					
					" AND " +
					PysPreciosServiciosBean.C_IDSERVICIO + "=" + servicio +
					" AND " +
					PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION + "=" + servInst + 					
					" AND " +
					PysPreciosServiciosBean.C_IDPERIODICIDAD + "=" + periodicidad;
										
		try {					
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS).equals("")) {
					maximo="1";
				}
				else{
					maximo=(String)prueba.get(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS);
				}													
			}			
			
			
		}	
		catch (ClsExceptions e) {		
			throw e;
		}
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
       			throw new ClsExceptions(e,"Error al ejecutar el identificador IdPreciosServicios.");
       		}	
	    }
		return maximo;
	}
	
}