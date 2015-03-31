/*
 * Created on 03-feb-2005
 *
 * nuria.rodriguezg - 14-2-2005 - Incorporacion - InsertPeticionAlta
 * 
 * Modificado por david.sanchezp el 10/03/2005 para incluir los 3 nuevos campos de la tabla (para el TPV).
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.Articulo;
import com.siga.general.CarroCompra;
import com.siga.general.SIGAException;
import com.siga.productos.form.GestionSolicitudesForm;

/**
 * @author daniel.campos
 *
 */
public class PysPeticionCompraSuscripcionAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public PysPeticionCompraSuscripcionAdm(UsrBean usuario) {
		super(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysPeticionCompraSuscripcionBean.C_FECHA,
							PysPeticionCompraSuscripcionBean.C_FECHAMODIFICACION,
							PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION,
							PysPeticionCompraSuscripcionBean.C_IDINSTITUCION,
							PysPeticionCompraSuscripcionBean.C_IDPERSONA,
							PysPeticionCompraSuscripcionBean.C_IDPETICION,
							PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA,
							PysPeticionCompraSuscripcionBean.C_TIPOPETICION,
							PysPeticionCompraSuscripcionBean.C_USUMODIFICACION,
							PysPeticionCompraSuscripcionBean.C_NUM_AUT,
							PysPeticionCompraSuscripcionBean.C_NUM_OPERACION,
							PysPeticionCompraSuscripcionBean.C_REFERENCIA};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] campos = {PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, PysPeticionCompraSuscripcionBean.C_IDPETICION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PysPeticionCompraSuscripcionBean bean = null;
		
		try {
			bean = new PysPeticionCompraSuscripcionBean();
			bean.setFecha(UtilidadesHash.getString(hash, PysPeticionCompraSuscripcionBean.C_FECHA));
			bean.setFechaMod(UtilidadesHash.getString(hash, PysPeticionCompraSuscripcionBean.C_FECHAMODIFICACION));
			bean.setIdEstadoPeticion(UtilidadesHash.getInteger(hash, PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, PysPeticionCompraSuscripcionBean.C_IDPERSONA));
			bean.setIdPeticion(UtilidadesHash.getLong(hash, PysPeticionCompraSuscripcionBean.C_IDPETICION));
			bean.setIdPeticionAlta(UtilidadesHash.getLong(hash, PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA));
			bean.setTipoPeticion(UtilidadesHash.getString(hash, PysPeticionCompraSuscripcionBean.C_TIPOPETICION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, PysPeticionCompraSuscripcionBean.C_USUMODIFICACION));
			bean.setNumAut(UtilidadesHash.getString(hash, PysPeticionCompraSuscripcionBean.C_NUM_AUT));
			bean.setNumOperacion(UtilidadesHash.getString(hash, PysPeticionCompraSuscripcionBean.C_NUM_OPERACION));
			bean.setReferencia(UtilidadesHash.getString(hash, PysPeticionCompraSuscripcionBean.C_REFERENCIA));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			PysPeticionCompraSuscripcionBean b = (PysPeticionCompraSuscripcionBean) bean;
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION, b.getIdEstadoPeticion());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_IDPETICION, b.getIdPeticion());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA, b.getIdPeticionAlta());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_TIPOPETICION, b.getTipoPeticion());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_NUM_AUT, b.getNumAut());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_NUM_OPERACION, b.getNumOperacion());
			UtilidadesHash.set(htData, PysPeticionCompraSuscripcionBean.C_REFERENCIA, b.getReferencia());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public PaginadorBind getPeticionesPaginador(GestionSolicitudesForm datos, Integer idInstitucion)throws ClsExceptions,SIGAException {
		PaginadorBind paginador=null;
		try {
			String sql = "SELECT " + 
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + ", " +
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + ", " +
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + ", " +
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + ", " +
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + ", " +
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + ", " +
							PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 +																
						" FROM " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + ", " + 
							CenPersonaBean.T_NOMBRETABLA +
						" WHERE " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA;
			
			// pdm INC-2955		
			if (datos.getBuscarEstadoPeticion() != null && !datos.getBuscarEstadoPeticion().equals("")) {
				sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " = " + datos.getBuscarEstadoPeticion();
			}				

			if (datos.getBuscarTipoPeticion() != null && !datos.getBuscarTipoPeticion().equals("")) {
				sql += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = '" + datos.getBuscarTipoPeticion() + "' ";
			}

			if (datos.getBuscarIdPeticionCompra() != null && !datos.getBuscarIdPeticionCompra().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta((datos.getBuscarIdPeticionCompra()).toString().trim(), PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION);
			}

			String fDesde = datos.getBuscarFechaDesde(); 
			String fHasta = datos.getBuscarFechaHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				sql += " AND " + GstDate.dateBetweenDesdeAndHasta(PysPeticionCompraSuscripcionBean.C_FECHA, fDesde, fHasta);
			}

			if (datos.getBuscarNombre()!=null && !datos.getBuscarNombre().trim().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarNombre().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE);
			}
			
			if (datos.getBuscarApellido1()!=null && !datos.getBuscarApellido1().trim().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarApellido1().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1);
			}
			
			if (datos.getBuscarApellido2()!=null && !datos.getBuscarApellido2().trim().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarApellido2().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2);
			}
			
			// Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte la consulta adecuada. 			
			if (datos.getBuscarNifcif()!=null && !datos.getBuscarNifcif().trim().equals("")) {
				if (ComodinBusquedas.hasComodin(datos.getBuscarNifcif())){	
					sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarNifcif().trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF);
				} else {
					sql += " AND " + ComodinBusquedas.prepararSentenciaNIF(datos.getBuscarNifcif(), " UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ") ");
				}
			}
						
			if (datos.getBuscarNColegiado()!=null && !datos.getBuscarNColegiado().trim().equals("")) {
				sql += " AND EXISTS ( " +
							" SELECT 1 " +
							" FROM " + CenColegiadoBean.T_NOMBRETABLA +
							" WHERE " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
								" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA +
								" AND ( " + 
									" (" + ComodinBusquedas.tratarNumeroColegiado(datos.getBuscarNColegiado(), CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO) + ") " +
									" OR (" + ComodinBusquedas.tratarNumeroColegiado(datos.getBuscarNColegiado(), CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO ) + ") " +
								" ) " + 
						" ) ";
			}
			
			// Compruebo si existe el estado del pago en los productos o servicios
			if (datos.getEstadoPago()!=null && !datos.getEstadoPago().equals("")) {
				sql += " AND ( " +
							" EXISTS ( " +
								" SELECT 1 " +
								" FROM " + PysProductosSolicitadosBean.T_NOMBRETABLA +
								" WHERE " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION +
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION +                  
									" AND '" + datos.getEstadoPago() + "' = F_SIGA_ESTADOCOMPRA ( " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + 
									" ) " +
							" ) OR EXISTS ( " +
								" SELECT 1 " +
								" FROM " + PysServiciosSolicitadosBean.T_NOMBRETABLA +
								" WHERE " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION +
									" AND " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION +                  
									" AND '" + datos.getEstadoPago() + "' = F_SIGA_ESTADOSUSCRIPCION ( " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIO + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + ", " +
										PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + 
									" ) " +
							" ) " +
						" ) ";
			}

			sql += " ORDER BY " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " DESC , " + 
						PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + " ";

			paginador = new PaginadorBind(sql, new Hashtable());	


		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getPeticionesPaginador.");
		}
		
		return paginador;                        
	}		
	
	/**
	 * getPeticionDetalle
	 * Obtiene la informacion asociada a una peticion con sus productos y servicios
	 * @param datos con los que realizar la consulta
	 * @param idInstitucion
	 * @return Vector con los resultados
	 * @throws SIGAException, ClsExceptions
	 */
	public Vector getPeticionDetalle (Hashtable datos, Integer idInstitucion) throws SIGAException, ClsExceptions {
		try {
			PysProductosSolicitadosAdm produtosAdm = new PysProductosSolicitadosAdm (this.usrbean);
			Vector productos = produtosAdm.getProductosSolicitados (datos, idInstitucion, false);
	
			PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm (this.usrbean);
			Vector servicios = serviciosAdm.getServiciosSolicitados (datos, idInstitucion, false);
			
			Vector resultado = new Vector ();
			if (productos != null) {
				resultado = (Vector) productos.clone();	
				productos.clear();
			}
			if (servicios != null) {
				for (int i = 0; i < servicios.size(); i++) {
					resultado.add ((Hashtable)servicios.get(i));
				}
				servicios.clear();
			}
			return resultado;
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
	   				throw new ClsExceptions(e,"Error al obtener los datos de las peticiones al detalle.");
	   			}
	   		}	
	    }
	}
	
	/**
	 * getNumProductosServiciosPendientes
	 * Obtiene el numero de articulos (productos o servicios) en estado pendiente 
	 * @param idInstiducion
	 * @param idPeticion
	 * @return long el numero de productos y servicios en estado pendiente
	 * @throws SIGAException, ClsExceptions
	 */
	public long getNumProductosServiciosPendientes (Integer idInstiducion, Long idPeticion) throws SIGAException, ClsExceptions {

		try {
			long num = 0;
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstiducion);
			UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
			UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);
			PysProductosSolicitadosAdm productoAdm = new PysProductosSolicitadosAdm(this.usrbean); 
			Vector v1 = productoAdm.select(claves);
			if (v1 != null) {
				num = v1.size();
			}
			claves.clear();
			UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDINSTITUCION, idInstiducion);
			UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
			UtilidadesHash.set(claves, PysServiciosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);
			PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm(this.usrbean); 
			Vector v2 = serviciosAdm.select(claves);
			if (v2 != null) {
				num += v2.size();
			}
			return num;	
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
	   				throw new ClsExceptions(e,"Error al obtener el numero de articulos pendientes.");
	   			}
	   		}	
	    }
	}
	
	
	/**
	 * Obtiene el campo IDPETICION
	 * @author nuria.rodriguezg 14-02-2005
	 * @version 1	 
	 * @param Bean datos de la peticion de compra.
	 * @return nuevo ID.
	 */
	public Long getNuevoID (Integer idInstitucion)throws SIGAException, ClsExceptions 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + PysPeticionCompraSuscripcionBean.C_IDPETICION + ") + 1) AS " + PysPeticionCompraSuscripcionBean.C_IDPETICION + 
			  			 " FROM " + this.nombreTabla +
						 " WHERE " + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + " = " + idInstitucion;

			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Long idPeticion = UtilidadesHash.getLong(prueba, PysPeticionCompraSuscripcionBean.C_IDPETICION);
				if (idPeticion == null) {
					return new Long(1);
				}
				else return idPeticion;								
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
	   				throw new ClsExceptions(e,"Error al obtener el identificador de peticion.");
	   			}
	   		}	
	    }
		return null;
	}	


	
	/**
	 * insertPeticionAlta
	 * Inserta un registro en PYS_PETICIONCOMPRASUSCRIPCION (productos o servicios) en estado pendiente 
	 * @param idInstiducion
	 * @param idInstiducionPresentador
	 * @param idPeticion
	 * @throws SIGAException, ClsExceptions
	 */
	public boolean insertPeticionAlta(Long idPersona, Integer idInstitucion, Long idPeticion, String numOperacion) throws SIGAException {
		try {
			PysPeticionCompraSuscripcionBean bean = new PysPeticionCompraSuscripcionBean();
			bean.setIdInstitucion(idInstitucion);
			bean.setIdPeticion(idPeticion);
			bean.setTipoPeticion(ClsConstants.TIPO_PETICION_COMPRA_ALTA);
			bean.setIdPersona(idPersona);
			bean.setFecha("sysdate");
			bean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE));
			bean.setNumOperacion(numOperacion);
					
			return insert(bean);
			
			}catch (Exception e) {
				throw new SIGAException (e);
			}		
	}
	
	public boolean insertPeticionBaja(Long idPersona, Integer idInstitucion, Long idPeticion, Long idPeticionAlta) throws SIGAException, ClsExceptions {

		PysPeticionCompraSuscripcionBean bean = new PysPeticionCompraSuscripcionBean();
		try {
			bean.setIdInstitucion(idInstitucion);
			bean.setIdPeticion(idPeticion);
			bean.setTipoPeticion(ClsConstants.TIPO_PETICION_COMPRA_BAJA);
			bean.setIdPersona(idPersona);
			bean.setFecha("sysdate");
			bean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE));
			bean.setIdPeticionAlta(idPeticionAlta);

			// Obtiene el numero de operacion del alta
			String sqlWhere = " WHERE " + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND " + PysPeticionCompraSuscripcionBean.C_IDPETICION + " = " + idPeticionAlta;		
			Vector vPeticiones = this.select(sqlWhere);
			if (vPeticiones != null && vPeticiones.size()>0) {
				PysPeticionCompraSuscripcionBean bPeticionAlta = (PysPeticionCompraSuscripcionBean) vPeticiones.get(0);
				bean.setNumOperacion(bPeticionAlta.getNumOperacion());
			}
			
		} catch (Exception e) {
			if (e instanceof SIGAException) {
				throw (SIGAException) e;
		   	} else {
	   			if (e instanceof ClsExceptions){
   					throw (ClsExceptions) e;
   				} else {
					throw new ClsExceptions(e,"Error al obtener los datos de las peticiones.");
		   		}
		   	}	
		}		
		
		return insert(bean);
	}
	
	/**
	 * Primero: Inserta la peticion de alta de la compra.
	 * Segundo: Inserta los articulos del carro en las tablas de Productos y Servicios.
	 * @param carro
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public Long insertarCarro(CarroCompra carro) throws SIGAException, ClsExceptions{
		PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm (this.usrbean);
		PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm (this.usrbean);

		Long idPersona;			
		Integer idInstitucion,  idInstitucionPresentador;
		Long idPeticion;
		Articulo articulo;
		
		try {
			ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	
												
			if(arrayListaArticulosOrdenada.size() == 0){
				throw new ClsExceptions("Error al insertar la peticion del carro de la compra.");
			}
			idInstitucion = carro.getIdinstitucion();
			idInstitucionPresentador = carro.getIdinstitucionPresentador();
			idPersona = carro.getIdPersona();			
			
			// Petición de alta
			idPeticion = this.getNuevoID(idInstitucion);
			if(!this.insertPeticionAlta(idPersona, idInstitucion, idPeticion, carro.getNumOperacion())){
				throw new ClsExceptions("Error al insertar la peticion del carro de la compra.");
			}	
					
			//Insertamos los articulos (productos y servicios), cada uno en su tabla:
			for (int i=0; i<arrayListaArticulosOrdenada.size(); i++) {				
				articulo = (Articulo)arrayListaArticulosOrdenada.get(i);
				articulo.setOrden(i+1);
				
				if(articulo.getClaseArticulo() == Articulo.CLASE_PRODUCTO) {		
					productosAdm.insertProducto(articulo, idPeticion, idInstitucionPresentador, idPersona);
				} else {
					serviciosAdm.insertServicio(articulo, idPeticion, idPersona);
				}		
			}
			
		} catch (SIGAException siga) {
			throw siga;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al insertar el carro de la compra");
		}
		return idPeticion;
	}
	
	/**
	 * Obtiene si hay una peticion de baja.
	 * @param idinstitucion
	 * @param idpeticion
	 * @return boolean
	 */
	public boolean getTipoPeticion(Integer idinstitucion, Long idpeticionalta, Long idpersona){
		boolean hay = false;

		try {
			String where = " WHERE "+PysPeticionCompraSuscripcionBean.C_IDINSTITUCION+"="+idinstitucion.toString()+
						   " AND "+PysPeticionCompraSuscripcionBean.C_IDPERSONA+"="+idpersona.toString()+
						   " AND "+PysPeticionCompraSuscripcionBean.C_TIPOPETICION+"='"+ClsConstants.TIPO_PETICION_COMPRA_BAJA+"'";
			if (idpeticionalta!=null)
				where += " AND "+PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA+"="+idpeticionalta.toString();
			Vector v = this.select(where);
			if (!v.isEmpty())
				hay = true;
		} catch(Exception e){
			hay = false;
		}
		return hay;
	}
	
	/**
	 * Inserta en un vector cada fila como una tabla hash del resultado de ejecutar la query
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
			throw new ClsExceptions (e, "Excepcion en PysPeticionCompraSuscripcionAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	public PysPeticionCompraSuscripcionBean aprobarPeticionCompra(PysPeticionCompraSuscripcionBean beanPeticion) throws ClsExceptions {
	    PysPeticionCompraSuscripcionBean b = null;
		try { 
		    PysProductosSolicitadosAdm productoSolAdm = new PysProductosSolicitadosAdm (this.usrbean);
			
		    Vector prod = productoSolAdm.obtenerProductosSolicitados(beanPeticion);
		    for (int i=0;i<prod.size();i++) {
			    // Producto
		        PysProductosSolicitadosBean bP = (PysProductosSolicitadosBean) prod.get(i);
		        if(bP.getIdFormaPago()!=null){ //Si no es NO FACTURABLE
					if (!productoSolAdm.confirmarProducto(bP.getIdInstitucion(), bP.getIdPeticion(), bP.getIdTipoProducto(), bP.getIdProducto(), bP.getIdProductoInstitucion(), new Double(0),"0")) {
					    throw new ClsExceptions("Error al confirmar producto");
					}
		        }
		    }
		    
		    Vector v = this.select("where idinstitucion="+beanPeticion.getIdInstitucion().toString()+ " AND idpeticion="+beanPeticion.getIdPeticion().toString());
		    if (v!=null && v.size()>0) {
		        b = (PysPeticionCompraSuscripcionBean) v.get(0);
		    }
		    
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Erro al confirmar la peticion de compra o suscripcion");
		}
		return b;	
	}
	
	/**
	 * Obtiene los datos extras de la pagina de gestion de solicitudes
	 * @param vDatos
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerDatosGestionSolicitud (Vector vDatos) throws ClsExceptions {
		Vector<Hashtable<String,Object>> vResultado = new Vector<Hashtable<String,Object>>();
		try {
			for (int i=0; i<vDatos.size(); i++) {
				Row rDatos = (Row)vDatos.get(i);
				Hashtable<String,Object> hDatos = rDatos.getRow();
				
				/* --------------- 1. Obtiene la descripcion del estado de la peticion -------------------------------- */
				String sIdEstadoPeticion = UtilidadesHash.getString(hDatos, PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				String sCampo = "DESCRIPCION_ESTADO";
				String sql = "SELECT " + UtilidadesMultidioma.getCampoMultidioma(PysEstadoPeticionBean.T_NOMBRETABLA + "." + PysEstadoPeticionBean.C_DESCRIPCION, this.usrbean.getLanguage()) +
								" FROM " + PysEstadoPeticionBean.T_NOMBRETABLA + 
								" WHERE " + PysEstadoPeticionBean.T_NOMBRETABLA + "." + PysEstadoPeticionBean.C_IDESTADOPETICION + " = " + sIdEstadoPeticion;
				
				String sValorCampo = "";
				Hashtable<String,Object> hResultado = this.selectGenericoHash(sql);
				if (hResultado!=null) {
					sValorCampo = UtilidadesHash.getString(hResultado, PysEstadoPeticionBean.C_DESCRIPCION);					
				}
				hDatos.put(sCampo, sValorCampo);
				
				/* --------------- 2.1. Obtiene datos para icono - Comprueba si tiene servicios -------------------------------- */
				String sIdInstitucion = UtilidadesHash.getString(hDatos, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				String sIdPeticion = UtilidadesHash.getString(hDatos, PysPeticionCompraSuscripcionBean.C_IDPETICION);
				sCampo = "TIPO_ICONO";
				sql = " SELECT COUNT(1) AS " + sCampo +
						" FROM " + PysServiciosSolicitadosBean.T_NOMBRETABLA +
						" WHERE " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDINSTITUCION + " = " + sIdInstitucion +
							" AND " + PysServiciosSolicitadosBean.T_NOMBRETABLA + "." + PysServiciosSolicitadosBean.C_IDPETICION + " = " + sIdPeticion;
				
				boolean bBuscarIcono = true;
				hResultado = this.selectGenericoHash(sql);
				if (hResultado!=null) {
					sValorCampo = UtilidadesHash.getString(hResultado, sCampo);
					if (sValorCampo!=null && !sValorCampo.equals("0")) {
						hDatos.put(sCampo, "0"); // Si tiene servicios => aparece sin icono (0)
						bBuscarIcono = false;
					}
				}
				
				if (bBuscarIcono) {
					/* --------------- 2.2. Obtiene datos para icono - Comprueba si tiene certificados -------------------------------- */
					sql = " SELECT COUNT(1) AS " + sCampo +  
							" FROM " + PysProductosSolicitadosBean.T_NOMBRETABLA + ", " +
							PysProductosInstitucionBean.T_NOMBRETABLA +
						" WHERE " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION +
							" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO +
							" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO +
							" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION +
							" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + sIdInstitucion +
							" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + sIdPeticion +
							" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_ACEPTADO + " <> 'D' " +
							" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_TIPOCERTIFICADO + " IN ('C','M','D')";
					
					hResultado = this.selectGenericoHash(sql);
					if (hResultado!=null) {
						sValorCampo = UtilidadesHash.getString(hResultado, sCampo);
						if (sValorCampo!=null && !sValorCampo.equals("0")) {
							hDatos.put(sCampo, "0"); // Si tiene certificados => aparece sin icono (0)
							bBuscarIcono = false;
						}
					}
					
					if (bBuscarIcono) {
						/* --------------- 2.3. Obtiene datos para icono - Comprueba si tiene factura -------------------------------- */
						sql = " SELECT COUNT(1) AS " + sCampo +   
							" FROM " + PysCompraBean.T_NOMBRETABLA + ", " +
								FacFacturaBean.T_NOMBRETABLA +
							" WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + sIdInstitucion +
								" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + " = " + sIdPeticion +
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION +
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDFACTURA;
						
						hResultado = this.selectGenericoHash(sql);
						if (hResultado!=null) {
							sValorCampo = UtilidadesHash.getString(hResultado, sCampo);
							if (sValorCampo!=null && !sValorCampo.equals("0")) {
								hDatos.put(sCampo, "1"); // Si tiene factura => aparece icono para descarga (1)
								bBuscarIcono = false;
							}
						}
						
						if (bBuscarIcono) {
							/* --------------- 2.4. Obtiene datos para icono - Comprueba si tiene productos facturables -------------------------------- */
							sql = " SELECT COUNT(1) AS " + sCampo +   
									" FROM " + PysProductosSolicitadosBean.T_NOMBRETABLA +
									" WHERE " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + sIdInstitucion +
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + sIdPeticion +
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_ACEPTADO + " NOT IN ('B', 'D', 'P')";
							
							hResultado = this.selectGenericoHash(sql);
							if (hResultado!=null) {
								sValorCampo = UtilidadesHash.getString(hResultado, sCampo);
								if (sValorCampo!=null && !sValorCampo.equals("0")) {
									hDatos.put(sCampo, "2"); // Si tiene productos facturables => aparece icono para facturacion rapida (2)
									bBuscarIcono = false;
								}
							}
							
							if (bBuscarIcono) {
								hDatos.put(sCampo, "0"); // Por defecto => aparece sin icono (0)
							}
						}						
					}					
				}
				
				vResultado.add(hDatos);	
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Erro al obtener los datos en obtenerDatosGestionSolicitud");
		}
		
		return vResultado;
	}
}