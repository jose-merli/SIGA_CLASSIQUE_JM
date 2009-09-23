/*
 * VERSIONES:
 * 
 * nuria.rgonzalez - 02-02-5 - Creacion
 * Modificada el 12-9-2005 por david.sanchezp para incluir la clave nueva idinstitucion.
 */

/**
*
* Clase que gestiona la tabla PYS_SERVICIOS de la BBDD
* 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysServiciosAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysServiciosAdm(UsrBean usu) {
		super(PysServiciosBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysServiciosBean.C_IDTIPOSERVICIOS,
							PysServiciosBean.C_IDSERVICIO,
							PysServiciosBean.C_DESCRIPCION,
							PysServiciosBean.C_FECHAMODIFICACION,
							PysServiciosBean.C_USUMODIFICACION,
							PysServiciosBean.C_IDINSTITUCION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysServiciosBean.C_IDTIPOSERVICIOS,
							PysServiciosBean.C_IDSERVICIO,
							PysServiciosBean.C_IDINSTITUCION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysServicios  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysServiciosBean bean = null;
		
		try {
			bean = new PysServiciosBean();
			bean.setIdTipoServicios(UtilidadesHash.getInteger(hash,PysServiciosBean.C_IDTIPOSERVICIOS));
			bean.setIdServicio(UtilidadesHash.getLong(hash,PysServiciosBean.C_IDSERVICIO));			
			bean.setDescripcion(UtilidadesHash.getString(hash,PysServiciosBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString (hash,PysServiciosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysServiciosBean.C_USUMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysServiciosBean.C_IDINSTITUCION));
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
			PysServiciosBean b = (PysServiciosBean) bean; 
			UtilidadesHash.set(htData,PysServiciosBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(htData,PysServiciosBean.C_IDSERVICIO, b.getIdServicio());			
			UtilidadesHash.set(htData,PysServiciosBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,PysServiciosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,PysServiciosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData,PysServiciosBean.C_IDINSTITUCION, b.getIdInstitucion());
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
	 * Realiza un select sobre la tabla con una clausula where pasada como parametro
	 * @param  where - clausula where
	 * @return  Vector - Resultado de la busqueda  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					
					//////////////
					//Antes :
					// MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow());
					// Ahota:
					PysServiciosBean registro = (PysServiciosBean) this.hashTableToBeanInicial(fila.getRow()); 
					Hashtable clave = new Hashtable ();
					UtilidadesHash.set(clave, PysTipoServiciosBean.C_IDTIPOSERVICIOS, registro.getIdTipoServicios());
					PysTipoServiciosAdm tipoAdm = new PysTipoServiciosAdm(this.usrbean);
					Vector v = tipoAdm.select(clave);
					if ((v == null) || v.size() != 1) {
						registro.setTipoServicio(null);
					}
					else {
						registro.setTipoServicio((PysTipoServiciosBean)v.get(0));
					}
					//////////////

					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					//////////////
					//Antes :
					// MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow());
					// Ahota:
					PysServiciosBean registro = (PysServiciosBean) this.hashTableToBeanInicial(fila.getRow()); 
					Hashtable clave = new Hashtable ();
					UtilidadesHash.set(clave, PysTipoServiciosBean.C_IDTIPOSERVICIOS, registro.getIdTipoServicios());
					PysTipoServiciosAdm tipoAdm = new PysTipoServiciosAdm(this.usrbean);
					Vector v = tipoAdm.select(clave);
					if ((v == null) || v.size() != 1) {
						registro.setTipoServicio(null);
					}
					else {
						registro.setTipoServicio((PysTipoServiciosBean)v.get(0));
					}
					//////////////

					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#insert(com.siga.beans.MasterBean)
	 */
	public boolean insert(MasterBean bean) throws ClsExceptions {
		try {
			PysServiciosBean servicio = (PysServiciosBean) bean;
			servicio.setIdServicio(this.getNuevoID(servicio));
			if (super.insert(servicio)) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar el producto en B.D.");
		}
	}

	/**
	 * Obtiene un nuevo ID para el servicio que se le pasa
	 * @author daniel.campos 12-02-05
	 * @version 1	 
	 * @param Servicio datos del servicio.
	 * @return nuevo ID del servicio.
	 */
	public Long getNuevoID (PysServiciosBean bean)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + PysServiciosBean.C_IDSERVICIO + ") + 1) AS " + PysServiciosBean.C_IDSERVICIO + 
			  			 " FROM " + this.nombreTabla +
						 " WHERE " +  PysServiciosBean.C_IDTIPOSERVICIOS + " = " + bean.getIdTipoServicios()+
						 " AND "+PysServiciosBean.C_IDINSTITUCION+"="+bean.getIdInstitucion();


			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Long idServicio = UtilidadesHash.getLong(prueba, PysServiciosBean.C_IDSERVICIO);
				if (idServicio == null) {
					return new Long(0);
				}
				else return idServicio;								
			}
		}	
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
           		if (e instanceof ClsExceptions){
           			throw (ClsExceptions)e;
           		}
           		else {
           			throw new ClsExceptions(e,"Error al ejecutar el 'getNuevoID' de servicio.");
           		}
       		}	
	    }
		return null;
	}	
	
	
	/**
	 * Obtiene la fecha de generación mayor de todas las facturaciones programadas generadas 
	 * de las series de facturación que contienen un determinado servicio <code>bean</code>
	 * @param bean
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String obtenerFechaMayorServicioFacturado (
			String idInstitucion,String idTipoServicios,String idServicios,String idServiciosInstitucion)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = "SELECT trunc(max(FP."+FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS+")) AS FECHA " + 
						 "	FROM "+FacFacturacionProgramadaBean.T_NOMBRETABLA + " FP , " + FacFacturaBean.T_NOMBRETABLA + " F, " +FacFacturacionSuscripcionBean.T_NOMBRETABLA +" FS " +
						 " WHERE F." + FacFacturaBean.C_IDINSTITUCION + "=FS." + FacFacturacionSuscripcionBean.C_IDINSTITUCION +
						 "   AND F." + FacFacturaBean.C_IDFACTURA + "=FS." + FacFacturacionSuscripcionBean.C_IDFACTURA +
						 
						 "   AND FP." + FacFacturacionProgramadaBean.C_IDINSTITUCION + "=F." + FacFacturaBean.C_IDINSTITUCION +
						 "   AND FP." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + "=F." + FacFacturaBean.C_IDPROGRAMACION +
						 "   AND FP." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + "=F." + FacFacturaBean.C_IDSERIEFACTURACION +
						 
						 "   AND FS." + FacFacturacionSuscripcionBean.C_IDINSTITUCION + "=" + idInstitucion +
						 "   AND FS." + FacFacturacionSuscripcionBean.C_IDTIPOSERVICIOS + "=" + idTipoServicios +
						 "   AND FS." + FacFacturacionSuscripcionBean.C_IDSERVICIO + "=" + idServicios +
						 "   AND FS." + FacFacturacionSuscripcionBean.C_IDSERVICIOSINSTITUCION + "=" + idServiciosInstitucion +
				         
						 "   AND FP."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION+" IS NOT NULL ";
		
			
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				return UtilidadesHash.getString(prueba, "FECHA");							
			}
		}	
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
           		if (e instanceof ClsExceptions){
           			throw (ClsExceptions)e;
           		}
           		else {
           			throw new ClsExceptions(e,"Error al ejecutar el 'obtenerFechaMayorServicioFacturado' de servicio.");
           		}
       		}	
	    }
		return null;
	}	
	

}