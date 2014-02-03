/*
 * Created on 03-feb-2005
 *
 * nuria.rodriguezg - 14-2-2005 - Incorporacion - InsertPeticionAlta
 * 
 * Modificado por david.sanchezp el 10/03/2005 para incluir los 3 nuevos campos de la tabla (para el TPV).
 */
package com.siga.beans;

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
import com.siga.Utilidades.UtilidadesBDAdm;
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
			Hashtable codigos = new Hashtable();
			String select = "SELECT " + 
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + ", " +
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + ", " +
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + ", " +
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + ", " +
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + ", " +
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + ", " +
			" (select count(1)" +
            " from pys_serviciossolicitados ps " +
            " where ps.idinstitucion = PYS_PETICIONCOMPRASUSCRIPCION.Idinstitucion " +
            " and ps.idpeticion = PYS_PETICIONCOMPRASUSCRIPCION.Idpeticion) as HAY_SERVICIOS, " +
			" (select count(1) " +
			"   from pys_productossolicitados ps, pys_productosinstitucion pi " +
			" where ps.idinstitucion=pi.idinstitucion " +
			"  and ps.idtipoproducto=pi.idtipoproducto  " +  
			"  and ps.idproducto=pi.idproducto     " +
			"  and ps.idproductoinstitucion=pi.idproductoinstitucion " +
			"  and ps.idinstitucion = " +
			"      PYS_PETICIONCOMPRASUSCRIPCION.Idinstitucion " +
			"  and ps.idpeticion = PYS_PETICIONCOMPRASUSCRIPCION.Idpeticion " +
			"  and ps.aceptado <> 'D' " +
			"  and pi.tipocertificado in ('C','M','D')) as NUM_CERTIFICADOS, " + 
			" (select count(1) from pys_productossolicitados ps where ps.idinstitucion=PYS_PETICIONCOMPRASUSCRIPCION.Idinstitucion and ps.idpeticion=PYS_PETICIONCOMPRASUSCRIPCION.Idpeticion and ps.aceptado<>'D') + " +
			" (select count(1) from pys_serviciossolicitados ss where ss.idinstitucion=PYS_PETICIONCOMPRASUSCRIPCION.Idinstitucion and ss.idpeticion=PYS_PETICIONCOMPRASUSCRIPCION.Idpeticion and ss.aceptado<>'D') as NUM_PENDIENTE , " +
			CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", " +
			"(SELECT " +UtilidadesMultidioma.getCampoMultidioma( PysEstadoPeticionBean.T_NOMBRETABLA + "." + PysEstadoPeticionBean.C_DESCRIPCION ,this.usrbean.getLanguage())+ 
			" FROM "   + PysEstadoPeticionBean.T_NOMBRETABLA + 
			" where "  + PysEstadoPeticionBean.T_NOMBRETABLA + "." + PysEstadoPeticionBean.C_IDESTADOPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + ") AS DESCRIPCION_ESTADO, " +
			CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", " +
			CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", " +
			CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " ";
			String from = 	"FROM " + 
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + ", " + CenPersonaBean.T_NOMBRETABLA + " ";
			String where = 	"WHERE " + 
			PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + " = " + idInstitucion + " AND " +
			CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA;
//			pdm INC-2955		
			if (datos.getBuscarEstadoPeticion() != null && !datos.getBuscarEstadoPeticion().equals("")) {
				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " = " + datos.getBuscarEstadoPeticion();
			}				



			if (datos.getBuscarTipoPeticion() != null && !datos.getBuscarTipoPeticion().equals("")) {
				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = '" + datos.getBuscarTipoPeticion() + "' ";
			}

			if (datos.getBuscarIdPeticionCompra() != null && !datos.getBuscarIdPeticionCompra().equals("")) {
//				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + " = " + datos.getBuscarIdPeticionCompra();
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta((datos.getBuscarIdPeticionCompra()).toString().trim(),PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION);

			}


			String fDesde = datos.getBuscarFechaDesde(); 
			String fHasta = datos.getBuscarFechaHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				where += " AND " + GstDate.dateBetweenDesdeAndHasta(PysPeticionCompraSuscripcionBean.C_FECHA,fDesde,fHasta);
			}


//			Persona
			if ((datos.getBuscarNombre() != null) && (!datos.getBuscarNombre().trim().equals(""))) {

				where += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarNombre().trim(),CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE )+ ") ";

			}
			if ((datos.getBuscarApellido1() != null) && (!datos.getBuscarApellido1().trim().equals(""))) {

				where += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarApellido1().trim(),CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 )+ ") ";
			}
			if ((datos.getBuscarApellido2() != null) && (!datos.getBuscarApellido2().trim().equals(""))) {

				where += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarApellido2().trim(),CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 )+ ") ";
			}
//			Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
//			de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
//			la consulta adecuada. 			
			if ((datos.getBuscarNifcif() != null) && (!datos.getBuscarNifcif().trim().equals(""))) {
				if (ComodinBusquedas.hasComodin(datos.getBuscarNifcif())){	

					where += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(datos.getBuscarNifcif().trim(),CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF )+ ") ";
				}else{
					where +=" AND "+ComodinBusquedas.prepararSentenciaNIF(datos.getBuscarNifcif(),"UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ")");
				}
			}
			if ((datos.getBuscarNColegiado() != null) && (!datos.getBuscarNColegiado().trim().equals(""))) {
				from += ", " + CenColegiadoBean.T_NOMBRETABLA + " "; 
				where += " AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + 
				" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION;
				where += " AND ( " +

				"("+ComodinBusquedas.tratarNumeroColegiado(datos.getBuscarNColegiado(),CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO )+ ") "+
				" OR " +

				"("+ComodinBusquedas.tratarNumeroColegiado(datos.getBuscarNColegiado(),CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO )+ ") "+
				")"; 
			}

			String orderBy = " ORDER BY " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " DESC , "  /* + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", " */ + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + " ";

			String consulta = select + from + where + orderBy;


			paginador = new PaginadorBind(consulta.toString(),codigos);	


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
		PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.usrbean);
		PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm (this.usrbean);
		PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm (this.usrbean);
		UsrBean userBean;

		Long idPersona;			
		Integer idTipoArticulo, idInstitucion,  idInstitucionPresentador;
		Long idArticulo, idArticuloInstitucion, idPeticion;
		int claseArticulo;
		String formaPago=null, numeroCuenta=null, fecha=null;			
		Vector vArticulos = new Vector();
		Vector registros = new Vector();
		Hashtable hash = new Hashtable();
		Hashtable hArticulos = new Hashtable();
		Articulo articulo;
		
		try {
			vArticulos = carro.getListaArticulos();
			
			userBean = carro.getUsrBean();			
												
			if(vArticulos.size() == 0){
				throw new ClsExceptions("Error al insertar la peticion del carro de la compra.");
			}
			idInstitucion = carro.getIdinstitucion();
			idInstitucionPresentador = carro.getIdinstitucionPresentador();
			fecha = UtilidadesBDAdm.getFechaBD("");
			idPersona = carro.getIdPersona();			
			
			// Petición de alta
			idPeticion = ppcsa.getNuevoID(idInstitucion);
			if(!ppcsa.insertPeticionAlta(idPersona, idInstitucion, idPeticion, carro.getNumOperacion())){
				throw new ClsExceptions("Error al insertar la peticion del carro de la compra.");
			}	
					
			//Insertamos los articulos (productos y servicios), cada uno en su tabla:
			for(int i=0; i < vArticulos.size(); i++){				
				articulo = (Articulo)vArticulos.get(i);
				claseArticulo = articulo.getClaseArticulo();
				int numeroArticulos = articulo.getCantidad();
				
				//Insertamos todos los productos:									
				if(claseArticulo == Articulo.CLASE_PRODUCTO) {		
					productosAdm.insertProducto(articulo, idPeticion, idInstitucionPresentador, idPersona);
				//Insertamos todos los productos:
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
		    
		    PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.usrbean);
		    Vector v = ppcsa.select("where idinstitucion="+beanPeticion.getIdInstitucion().toString()+ " AND idpeticion="+beanPeticion.getIdPeticion().toString());
		    if (v!=null && v.size()>0) {
		        b = (PysPeticionCompraSuscripcionBean) v.get(0);
		    }
		    
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Erro al confirmar la peticion de compra o suscripcion");
		}
		return b;	
	}
	

}
 