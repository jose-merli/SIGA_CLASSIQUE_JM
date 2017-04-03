/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creaci�n
 * nuria.rgonzalez - 08-03-05 - incorporaci�n: getCamposFacturacion(), getTablasFacturacion(), selectDatosFacturacion(String sWhere, String[] orden), getNuevoID(FacFacturacionProgramadaBean bean).
 */

package com.siga.beans;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.envios.Envio;
import com.siga.facturacion.form.ConfirmarFacturacionForm;
import com.siga.general.SIGAException;



public class FacFacturacionProgramadaAdm extends MasterBeanAdministrador {
	
	public FacFacturacionProgramadaAdm (UsrBean usu) {
		super (FacFacturacionProgramadaBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacFacturacionProgramadaBean.C_IDINSTITUCION, 		
							FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,
							FacFacturacionProgramadaBean.C_IDPROGRAMACION, 	
							FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS,
							FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS,	
							FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS,
							FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS,
							FacFacturacionProgramadaBean.C_FECHAREALGENERACION,
							FacFacturacionProgramadaBean.C_FECHACONFIRMACION,
							FacFacturacionProgramadaBean.C_FECHAPROGRAMACION,
							FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION,
							FacFacturacionProgramadaBean.C_IDPREVISION,
							FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM ,
							FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION ,
							FacFacturacionProgramadaBean.C_IDESTADOPDF ,
							FacFacturacionProgramadaBean.C_IDESTADOENVIO ,
							FacFacturacionProgramadaBean.C_GENERAPDF ,
							FacFacturacionProgramadaBean.C_ENVIO ,
							FacFacturacionProgramadaBean.C_ARCHIVARFACT ,
							FacFacturacionProgramadaBean.C_FECHACARGO,
							FacFacturacionProgramadaBean.C_CONFDEUDOR,
							FacFacturacionProgramadaBean.C_CONFINGRESOS,
							FacFacturacionProgramadaBean.C_CTACLIENTES,
							FacFacturacionProgramadaBean.C_CTAINGRESOS,
							FacFacturacionProgramadaBean.C_VISIBLE,
							FacFacturacionProgramadaBean.C_DESCRIPCION,
							FacFacturacionProgramadaBean.C_IDTIPOENVIOS,
							FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL,
							FacFacturacionProgramadaBean.C_FECHAMODIFICACION,
							FacFacturacionProgramadaBean.C_USUMODIFICACION,
							FacFacturacionProgramadaBean.C_FECHAPRESENTACION,
							FacFacturacionProgramadaBean.C_FECHARECIBOSPRIMEROS,
							FacFacturacionProgramadaBean.C_FECHARECIBOSRECURRENTES,
							FacFacturacionProgramadaBean.C_FECHARECIBOSCOR1,
							FacFacturacionProgramadaBean.C_FECHARECIBOSB2B,
							FacFacturacionProgramadaBean.C_LOGERROR,
							FacFacturacionProgramadaBean.C_NOMBREFICHERO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, FacFacturacionProgramadaBean.C_IDPROGRAMACION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacFacturacionProgramadaBean bean = null;
		
		try {
			bean = new FacFacturacionProgramadaBean();
			bean.setIdInstitucion			(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion		(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION));
			bean.setIdProgramacion			(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION));
			bean.setFechaInicioProductos	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS));
			bean.setFechaFinProductos	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS));
			bean.setFechaInicioServicios	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS));
			bean.setFechaFinServicios	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS));
			bean.setFechaRealGeneracion	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAREALGENERACION));
			bean.setFechaConfirmacion	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHACONFIRMACION));
			bean.setFechaProgramacion	 	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION));
			bean.setFechaPrevistaGeneracion	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION));
			bean.setIdPrevision				(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPREVISION));

			bean.setFechaPrevistaConfirmacion(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM));
			bean.setIdEstadoConfirmacion(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION));
			bean.setIdEstadoPDF(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDESTADOPDF));
			bean.setIdEstadoEnvio(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDESTADOENVIO));
			bean.setGenerarPDF(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_GENERAPDF));
			bean.setEnvio(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_ENVIO));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDTIPOENVIOS));
			bean.setIdTipoPlantillaMail(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL));
			bean.setArchivarFact(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_ARCHIVARFACT));

			bean.setConfDeudor(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CONFDEUDOR));
			bean.setConfIngresos(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CONFINGRESOS));
			bean.setCtaClientes(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CTACLIENTES));
			bean.setCtaIngresos(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_CTAINGRESOS));
			bean.setVisible(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_VISIBLE));
			bean.setDescripcion(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_DESCRIPCION));
			bean.setFechaCargo				(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHACARGO));
			bean.setFechaMod				(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAMODIFICACION));
			bean.setUsuMod					(UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_USUMODIFICACION));
			
			bean.setFechaPresentacion		(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPRESENTACION));
			bean.setFechaRecibosPrimeros	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHARECIBOSPRIMEROS));
			bean.setFechaRecibosRecurrentes	(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHARECIBOSRECURRENTES));
			bean.setFechaRecibosCOR1		(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHARECIBOSCOR1));
			bean.setFechaRecibosB2B			(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHARECIBOSB2B));
			bean.setLogerror				(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_LOGERROR));
			bean.setNombrefichero			(UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_NOMBREFICHERO));		

		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacFacturacionProgramadaBean b = (FacFacturacionProgramadaBean) bean;
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDPROGRAMACION, b.getIdProgramacion());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS, b.getFechaInicioProductos());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS, b.getFechaFinProductos());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS, b.getFechaInicioServicios());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS, b.getFechaFinServicios());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAREALGENERACION, b.getFechaRealGeneracion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, b.getFechaConfirmacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION, b.getFechaProgramacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION, b.getFechaPrevistaGeneracion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDPREVISION, b.getIdPrevision());

			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM, b.getFechaPrevistaConfirmacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, b.getIdEstadoConfirmacion());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDESTADOPDF, b.getIdEstadoPDF());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDESTADOENVIO, b.getIdEstadoEnvio());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_GENERAPDF, b.getGenerarPDF());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_ENVIO, b.getEnvio());
			if(b.getIdTipoPlantillaMail() != null){
				UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL, b.getIdTipoPlantillaMail());
			}else{
				UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL, "");
			}
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_ARCHIVARFACT, b.getArchivarFact());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHACARGO, b.getFechaCargo());

			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CONFDEUDOR, b.getConfDeudor());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CONFINGRESOS, b.getConfIngresos());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CTACLIENTES, b.getCtaClientes());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_CTAINGRESOS, b.getCtaIngresos());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_VISIBLE, b.getVisible());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_DESCRIPCION, b.getDescripcion());

			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacFacturacionProgramadaBean.C_USUMODIFICACION, b.getUsuMod());

			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHAPRESENTACION, b.getFechaPresentacion());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHARECIBOSRECURRENTES, b.getFechaRecibosRecurrentes());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHARECIBOSPRIMEROS, b.getFechaRecibosPrimeros());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHARECIBOSCOR1, b.getFechaRecibosCOR1());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_FECHARECIBOSB2B, b.getFechaRecibosB2B());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_LOGERROR, b.getLogerror());
			UtilidadesHash.setForCompare(htData, FacFacturacionProgramadaBean.C_NOMBREFICHERO, b.getNombrefichero());	
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
	
	public Vector selectTabla(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = "Select Nvl(Max("+FacFacturacionProgramadaBean.T_NOMBRETABLA+"."+FacFacturacionProgramadaBean.C_IDPROGRAMACION+"), 0) + 1 IDPROGRAMACION";
			sql += " From "+FacFacturacionProgramadaBean.T_NOMBRETABLA;
			sql += where;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
	/**
	 * Devuelve un array con los nombres de los campos del bean FAC_FacturacionProgramada + el campo NombreAbreviado de FAC_SereiFacturacion necesarios para construir la select en 'PROGRAMAR FACTURACION'.
	 * @author nuria.rgonzalez 10-03-05
	 */
	protected String[] getCamposFacturacion() {
		int num = getCamposBean().length;
		String[] aux = new String [num+1];
		String[] campos = getCamposBean();
		for(int i = 0; i < num; i++){
			aux[i] = FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + campos[i];
		}
		aux[num] = FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO;		
				
		return aux;
	}
	
	/**
	 * Devuelve un string con el nombre de las tablas FAC_FACTURACIONPROGRAMADA, FAC_SERIEFACTURACION y las relaciones entre ellas para construir la Query.
	 * @author nuria.rgonzalez 10-03-05
	 */
	protected String getTablasFacturacion(){
		String campos = FacFacturacionProgramadaBean.T_NOMBRETABLA + " , " + FacSerieFacturacionBean.T_NOMBRETABLA;
		return campos;
	}
	
	/**
	 * Devuelve un Vector con los datos de la Facturacion Programada  del cliente pasado como par�metro.
	 * @author nuria.rgonzalez 10-03-05
	 * @version 1	 
	 * @param sWhere, string que contiene la sentencia where
	 * @param orden, array que contiene los campos por los que queremos ordenar los resultados
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public Vector selectDatosFacturacion(String sWhere, String[] orden) throws ClsExceptions, SIGAException{
		
		Vector v = null;		
		RowsContainer rc = null;
		String where = sWhere;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasFacturacion(), this.getCamposFacturacion());
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(orden);  
			sql += "desc";
            rc = this.find(sql);
            if (rc!=null) {
 				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}

		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosFacturacion");
		}
		return v;
	}

	/**
	 * Devuelve un Vector con los datos de la Facturacion Programada  del cliente pasado como par�metro
	 * @param sWhereBind
	 * @param codigos
	 * @param orden
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector selectDatosFacturacionBean(String sWhereBind, Hashtable codigos, String[] orden) throws ClsExceptions {
		Vector vResultado = null;		
		try{			
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasFacturacion(), this.getCamposFacturacion()) +
					sWhereBind +
					UtilidadesBDAdm.sqlOrderBy(orden) + " ASC";
			
			RowsContainer rc = new RowsContainer();
            rc = this.findBind(sql,codigos);
            if (rc!=null) {
            	vResultado = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable hRegistro = (Hashtable) fila.getRow(); 
					MasterBean bRegistro = (MasterBean) this.hashTableToBean(hRegistro); 
					if (bRegistro != null) 
						vResultado.add(bRegistro);
				}
			}
            
		} catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosFacturacion");
		}
		
		return vResultado;
	}

	/**
	 * Notas Jorge PT 118: Obtiene un nuevo identificador de facturacion programada
	 * @param bean
	 * @return
	 * @throws ClsExceptions
	 */
	public Long getNuevoID(FacFacturacionProgramadaBean bean) throws ClsExceptions {
		return this.getNuevoID(bean.getIdInstitucion().toString(), bean.getIdSerieFacturacion().toString());
	}	

	/**
	 * Notas Jorge PT 118: Obtiene un nuevo identificador de facturacion programada
	 * @param idInstitucion
	 * @param idSerieFacturacion
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public Long getNuevoID(String idInstitucion, String idSerieFacturacion) throws ClsExceptions {
		try {		
			String sql = " SELECT NVL(MAX(" + FacFacturacionProgramadaBean.C_IDPROGRAMACION + "), 0) + 1 AS " + FacFacturacionProgramadaBean.C_IDPROGRAMACION + 
			  			 " FROM " + FacFacturacionProgramadaBean.T_NOMBRETABLA +
						 " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion +
						 " AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;
			
			RowsContainer rc = new RowsContainer();
			if (rc.query(sql) && rc.size()>0)	{
				Row fila = (Row) rc.get(0);
				String id = fila.getString(FacFacturacionProgramadaBean.C_IDPROGRAMACION);
				return Long.valueOf(id);
			}
			
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al obtener un nuevo identificador de facturaci�n programada");		
		}
		
		return null;
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
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}

	
	/**
	 * Realiza el tratamiento de una facturaci�n para ponerle los estados de confirmacion, generacion de pdf y envio de facturas adecuados segun sus datos.
	 * @param bean Obtendremos de el los datos
	 * @return bean con los estados actualizados
	 * @throws ClsExceptions
	 */
	public FacFacturacionProgramadaBean tratamientoEstadosProgramacion(FacFacturacionProgramadaBean bean) throws ClsExceptions {
		
		FacFacturacionProgramadaBean aux = bean;
		try { 
			
			Integer idTratamientoActual = UtilidadesHash.getInteger(bean.getOriginalHash(), FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION);
			
			// confirmacion
			if (bean.getFechaPrevistaConfirmacion()!=null && !bean.getFechaPrevistaConfirmacion().trim().equals("")) {
				// Se ha programado la confirmacion
				if(idTratamientoActual.intValue() == FacEstadoConfirmFactBean.GENERADA.intValue() || idTratamientoActual.intValue() == FacEstadoConfirmFactBean.ERROR_CONFIRMACION.intValue()){
					bean.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA);
				
				}else if(idTratamientoActual.intValue() == FacEstadoConfirmFactBean.ERROR_GENERACION.intValue()){
					//Pasamos de estado error a estado generado porgramacion
					bean.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.GENERACION_PROGRAMADA);
				
				}else{
					bean.setIdEstadoConfirmacion(idTratamientoActual);
				}
			} else {
				//No hay fecha programada de confirmacion.
				if(idTratamientoActual.intValue() == FacEstadoConfirmFactBean.ERROR_GENERACION.intValue()){
					//Pasamos de estado error a estado generado porgramacion
					bean.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.GENERACION_PROGRAMADA);
				}else{
					bean.setIdEstadoConfirmacion(idTratamientoActual);
				}					
			}
			// PDF
			if (bean.getFechaPrevistaConfirmacion()!=null && !bean.getFechaPrevistaConfirmacion().trim().equals("")) {
				if (bean.getGenerarPDF().equals("1")) {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PROGRAMADA);
				} else {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
				}
			} else { 
				if (bean.getGenerarPDF().equals("1")) {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PENDIENTE);
				} else {
					bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
				}
			}
			// Envio
			if (bean.getFechaPrevistaConfirmacion()!=null && !bean.getFechaPrevistaConfirmacion().trim().equals("")) {
				if (bean.getEnvio().equals("1")) {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_PROGRAMADA);
				} else {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
				}
			} else { 
				if (bean.getEnvio().equals("1")) {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_PENDIENTE);
				} else {
					bean.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
				}
			}
			
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.tratamientoEstadosProgramacion().");
		}
		return aux;	
	}

	/**
	 * Realiza las comprobaciones oportunas de plantillas necesarias para que la confirmaci�n de la facturaci�n funcione sin problemas
	 * @return Vector con la lista de mensajes tipo idrecurso
	 * @throws ClsExceptions
	 */
	public Vector comprobarRecursosProgramacion(FacFacturacionProgramadaBean bean) throws ClsExceptions {
		
		Vector v = new Vector();
		try { 
			//String idserieidprogramacion = bean.getIdSerieFacturacion().toString()+"_" + bean.getIdProgramacion().toString();
			String institucion = bean.getIdInstitucion().toString();
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			//ReadProperties p = new ReadProperties ("SIGA.properties");
			
			// directorio de fichero bancario 
			String pathFichero = p.returnProperty("facturacion.directorioBancosOracle");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
    		pathFichero += sBarra+institucion;
    		File aux = new File(pathFichero);
    		if (!aux.exists()) {
    			v.add("messages.facturacion.comprueba.noPathFicheroBancario"); // No existe el path de generacion de fichero bancario
    		} else 
    		if (!aux.canWrite()) {
    			v.add("messages.facturacion.comprueba.noPermisosPathFicheroBancario"); // EL path de generaci�n de fichero bancarios no tiene los permisos adecuados.
    		} 
    		
			// Obtengo la plantilla a utilizar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.usrbean);
			Vector plantillas=plantillaAdm.getPlantillaSerieFacturacion(institucion,bean.getIdSerieFacturacion().toString());
			if (plantillas==null || plantillas.size()==0) {
				v.add("messages.facturacion.comprueba.plantillaFacturaNoConfigurada"); // No esta configurada una plantilla de factura.
			} else { 
				String plantilla=plantillas.firstElement().toString();

				//TODO Esta comprobacion de rutas no me parece correcta: deberia pasarse la ruta de o a algun metodo y no hacerlo aqui como en una isla sin relacion con nada a la vista
				// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
			    String rutaTemporal = p.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+p.returnProperty("facturacion.directorioTemporalFacturasJava");
	    		String barraTemporal = "";
	    		if (rutaTemporal.indexOf("/") > -1){ 
	    			barraTemporal = "/";
	    		}
	    		if (rutaTemporal.indexOf("\\") > -1){ 
	    			barraTemporal = "\\";
	    		}    		
	    		rutaTemporal += barraTemporal+institucion.toString();
				File rutaFOP=new File(rutaTemporal);
				if (!rutaFOP.exists()) {
	    			v.add("messages.facturacion.comprueba.noPathTemporalFacturas"); // No existe el path temporal de facturas
	    		} else 
	    		if (!rutaFOP.canWrite()) {
	    			v.add("messages.facturacion.comprueba.noPermisosPathFicheroBancario"); // El path temporal de facturas no tiene los permisos adecuados.
	    		} 
			
				// Obtencion de la ruta de donde se obtiene la plantilla adecuada			
			    String rutaPlantilla = p.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+p.returnProperty("facturacion.directorioPlantillaFacturaJava");
			    String barraPlantilla="";
	    		if (rutaPlantilla.indexOf("/") > -1){
	    			barraPlantilla = "/";
	    		}
	    		if (rutaPlantilla.indexOf("\\") > -1){
	    			barraPlantilla = "\\";
	    		}
	    		rutaPlantilla += barraPlantilla+institucion.toString()+barraPlantilla+plantilla;
				File rutaModelo=new File(rutaPlantilla);
				//Comprobamos que exista la ruta y sino la creamos
				if (!rutaModelo.exists()){
					v.add("messages.facturacion.comprueba.noPlantillaFacturacion"); // No existe la plantilla de facturaci�n					
				}
			}
			
    		if (bean.getEnvio().equals("1")) {
				// plantilla de envios
				GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
				String preferencia = paramAdm.getValor(institucion.toString(),"ENV","TIPO_ENVIO_PREFERENTE","1");
				Integer tipoEnvio = Envio.calculaTipoEnvio(preferencia);
	
				// Recojo una plantilla valida cualquiera:
				EnvPlantillasEnviosAdm plantillasEnviosAdm = new EnvPlantillasEnviosAdm(this.usrbean);
				Vector plantillasValidas=plantillasEnviosAdm.getIdPlantillasValidos(institucion,tipoEnvio.toString());
				//Si no hay plantillas:
				if (plantillasValidas==null || plantillasValidas.size()==0) {
					v.add("messages.facturacion.comprueba.noConfigPlantillaEnvios"); // No esta configurada ninguna plantilla de envios.
				} else { 
					// RGG Damos por supuesto que el proceso de crear envios automaticamwnte obtiene la primera plantilla que encuentra
					String idplantillaenvios=plantillasValidas.firstElement().toString();
					EnvPlantillaGeneracionAdm admPlantilla= new EnvPlantillaGeneracionAdm(this.usrbean);  
					Vector plantillasEnvio = admPlantilla.obtenerListaPlantillas(institucion,tipoEnvio.toString(),idplantillaenvios);
					if (tipoEnvio.intValue()!=1 && (plantillasEnvio==null || plantillasEnvio.size()==0)) {
						v.add("messages.facturacion.comprueba.noConfigPlantillaGeneracionEnvios"); // No estan configuradas plantillas de generacion para la plantilla seleccionada
					} else {
			   	        String sWhere = " WHERE " + GenParametrosBean.C_PARAMETRO + "='PATH_PLANTILLAS' AND "+
	                    GenParametrosBean.C_MODULO + "='CER' AND " + 
	                    GenParametrosBean.C_IDINSTITUCION + "=0";
						GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);
						Vector vParametros = admParametros.select(sWhere);
						if (vParametros==null || vParametros.size()==0)
						{
							v.add("messages.facturacion.comprueba.pathPlantillasMalConfiguradoParametros"); // El path de plantillas esta mal configurado
						}

						GenParametrosBean beanParametros = (GenParametrosBean)vParametros.elementAt(0);
						String sPath = beanParametros.getValor();

	
			   	        if (tipoEnvio.intValue()!=1) {
							// RGG Damos por supuesto que el proceso de crear envios automaticamwnte obtiene la primera plantilla de generacion que encuentra en la plantilla
							EnvPlantillaGeneracionBean beanPL = (EnvPlantillaGeneracionBean) plantillasEnvio.get(0); 	
				   	        String sCompuesto = beanPL.getIdTipoEnvios() + "_" + beanPL.getIdPlantillaEnvios() + "_" + beanPL.getIdPlantilla();

				   	        String sNombreFinal = sPath + File.separator + institucion + File.separator + sCompuesto;
				   	        File f = new File(sNombreFinal);
							if (!f.exists()) {
								v.add("messages.facturacion.comprueba.noFicheroPlantillaEnvios"); // No existe la plantilla configurada de envios
				    		} else 
				    		if (!f.canRead()) {
				    			v.add("messages.facturacion.comprueba.noPermisosFicheroPlantillaEnvios"); // La plantilla de envios configurada no tiene los permisos adecuados.
				    		} 
			   	        }
					}
				}
    		}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.comprobarRecursosProgramacion().");
		}
		return v;	
	}
   
    public Paginador getProgramacioneFacturacionPaginador(ConfirmarFacturacionForm confirmarFacturacionForm) throws ClsExceptions, SIGAException{
		
    	Integer idInstitucion	=  Integer.valueOf(this.usrbean.getLocation());	
		
		//Este select interno si devuelve un numero > 0 querra decir que debo pedir la fecha de cargo
		String selectInterno = "SELECT count(*) FROM "+FacFacturaBean.T_NOMBRETABLA+" fac "+
							   " WHERE fac."+FacFacturaBean.C_IDINSTITUCION+" = facProg."+FacFacturacionProgramadaBean.C_IDINSTITUCION+
							   " AND fac."+FacFacturaBean.C_IDSERIEFACTURACION+" = facProg."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+
							   " AND fac."+FacFacturaBean.C_IDPROGRAMACION+" = facProg."+FacFacturacionProgramadaBean.C_IDPROGRAMACION+
							   " AND fac."+FacFacturaBean.C_IDFORMAPAGO+"="+ClsConstants.TIPO_FORMAPAGO_FACTURA;
							   
		StringBuffer select = new StringBuffer("SELECT  ");
		
		select.append(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHACONFIRMACION);
		select.append(",");
		select.append(FacFacturacionProgramadaBean.C_FECHAREALGENERACION);
		select.append(",");
		
		select.append("IDESTADOCONFIRMACION,IDESTADOPDF,IDESTADOENVIO");

		select.append(",ARCHIVARFACT,IDPROGRAMACION");
		select.append(",facProg.");
		select.append(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION);
		
		select.append(",facProg.");
		select.append(FacFacturacionProgramadaBean.C_USUMODIFICACION);
		
		select.append(",facProg.");
		select.append(FacFacturacionProgramadaBean.C_NOMBREFICHERO);
		
		select.append(",facProg.");
		select.append(FacFacturacionProgramadaBean.C_LOGERROR);		
		
		select.append(",facProg.");
		select.append(FacFacturacionProgramadaBean.C_DESCRIPCION);
		select.append(",");
		
		select.append(" ("+selectInterno+") AS FECHACARGO, ");
		select.append(" serieFac."+FacSerieFacturacionBean.C_NOMBREABREVIADO);
		select.append(" FROM "+FacFacturacionProgramadaBean.T_NOMBRETABLA+" facProg, ");
		select.append(FacSerieFacturacionBean.T_NOMBRETABLA+" serieFac");
		select.append(" WHERE facProg." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion);
		select.append(" AND facProg."+FacFacturacionProgramadaBean.C_IDINSTITUCION+"= serieFac."+FacSerieFacturacionBean.C_IDINSTITUCION);
		select.append(" AND facProg."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+"= serieFac."+FacSerieFacturacionBean.C_IDSERIEFACTURACION);
		select.append(" AND NVL(facProg."+FacFacturacionProgramadaBean.C_VISIBLE+", 'N') = 'S' ");
						
		// filtros
		if (confirmarFacturacionForm.getEstadoConfirmacion()!= null && !confirmarFacturacionForm.getEstadoConfirmacion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION+"="+confirmarFacturacionForm.getEstadoConfirmacion());
		}
		if (confirmarFacturacionForm.getEstadoPDF()!= null && !confirmarFacturacionForm.getEstadoPDF().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOPDF+"="+confirmarFacturacionForm.getEstadoPDF());
		}
		if (confirmarFacturacionForm.getEstadoEnvios()!= null && !confirmarFacturacionForm.getEstadoEnvios().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOENVIO+"="+confirmarFacturacionForm.getEstadoEnvios());
		}
		if (confirmarFacturacionForm.getArchivadas()!=null && confirmarFacturacionForm.getArchivadas().trim().equals("1")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_ARCHIVARFACT+"='1'");
		} else {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_ARCHIVARFACT+"='0'");
		} 
		if (confirmarFacturacionForm.getFechaDesdeConfirmacion()!=null && !confirmarFacturacionForm.getFechaDesdeConfirmacion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHACONFIRMACION+">=TO_DATE ('" + confirmarFacturacionForm.getFechaDesdeConfirmacion() + "', 'DD/MM/YYYY')");
		}
		if (confirmarFacturacionForm.getFechaHastaConfirmacion()!=null && !confirmarFacturacionForm.getFechaHastaConfirmacion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHACONFIRMACION+"<TO_DATE ('" + confirmarFacturacionForm.getFechaHastaConfirmacion() + "', 'DD/MM/YYYY')");
		}
		if (confirmarFacturacionForm.getFechaDesdeGeneracion()!=null && !confirmarFacturacionForm.getFechaDesdeGeneracion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION+">=TO_DATE ('" + confirmarFacturacionForm.getFechaDesdeGeneracion() + "', 'DD/MM/YYYY')");
		}
		if (confirmarFacturacionForm.getFechaHastaGeneracion()!=null && !confirmarFacturacionForm.getFechaHastaGeneracion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION+"<TO_DATE ('" + confirmarFacturacionForm.getFechaHastaGeneracion() + "', 'DD/MM/YYYY')");
		}
		if (confirmarFacturacionForm.getFechaDesdePrevistaGeneracion()!=null && !confirmarFacturacionForm.getFechaDesdePrevistaGeneracion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION+">=TO_DATE ('" + confirmarFacturacionForm.getFechaDesdePrevistaGeneracion() + "', 'DD/MM/YYYY')");
		}
		if (confirmarFacturacionForm.getFechaHastaPrevistaGeneracion()!=null && !confirmarFacturacionForm.getFechaHastaPrevistaGeneracion().trim().equals("")) {
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION+"<TO_DATE ('" + confirmarFacturacionForm.getFechaHastaPrevistaGeneracion() + "', 'DD/MM/YYYY')");
		}
		
		// filtro por fecha de productos
		boolean hayFechaDesdeProductos = true;
		String fechaDesdeProductos = confirmarFacturacionForm.getFechaDesdeProductos();
		if (fechaDesdeProductos == null || fechaDesdeProductos.trim().equals("")) {
			fechaDesdeProductos = "01/01/1900"; //inicio de los tiempos
			hayFechaDesdeProductos = false;
		}
		boolean hayFechaHastaProductos = true;
		String fechaHastaProductos = confirmarFacturacionForm.getFechaHastaProductos();
		if (fechaHastaProductos == null || fechaHastaProductos.trim().equals("")) {
			fechaHastaProductos = "31/12/2999"; //fin de los tiempos
			hayFechaHastaProductos = false;
		}
		if (hayFechaDesdeProductos || hayFechaHastaProductos) {
			// NOTA: para decidir si dos periodos se solapan, el inicio de uno ha de ser anterior que el fin del otro, y viceversa 
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS+"<=TO_DATE ('" + fechaHastaProductos + "', 'DD/MM/YYYY')");
			select.append(" AND facProg."+FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS+">=TO_DATE ('" + fechaDesdeProductos + "', 'DD/MM/YYYY')");
		}
		
		// filtro por tipo de producto
		if (confirmarFacturacionForm.getIdTipoProducto()!=null && !confirmarFacturacionForm.getIdTipoProducto().trim().equals("") &&
			confirmarFacturacionForm.getIdProducto()!=null && !confirmarFacturacionForm.getIdProducto().trim().equals("")) 
		{
			select.append(" And Exists (Select 1 ");
			select.append("     From "+FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+" Tippro ");
			select.append("    Where Tippro."+FacTiposProduIncluEnFactuBean.C_IDINSTITUCION+" = Facprog."+FacFacturacionProgramadaBean.C_IDINSTITUCION+" ");
			select.append("      And Tippro."+FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION+" = Facprog."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" ");
			select.append("      And Tippro."+FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO+" = " +confirmarFacturacionForm.getIdTipoProducto()+" ");
			select.append("      And Tippro."+FacTiposProduIncluEnFactuBean.C_IDPRODUCTO+" = "+confirmarFacturacionForm.getIdProducto()+") ");
		}
		
		select.append( " ORDER BY "+FacFacturacionProgramadaBean.C_FECHAREALGENERACION+" DESC");
    	Paginador paginador= new Paginador(select.toString());
		
		return paginador;                        
	}	
    
    
   

}
