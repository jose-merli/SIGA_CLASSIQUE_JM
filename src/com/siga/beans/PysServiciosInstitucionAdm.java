/*
 * nuria.rgonzalez Creado 2-2-2005
 * 
 */
package com.siga.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 *
 */
public class PysServiciosInstitucionAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysServiciosInstitucionAdm(UsrBean usu) {
		super(PysServiciosInstitucionBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysServiciosInstitucionBean.C_IDINSTITUCION,
							PysServiciosInstitucionBean.C_IDTIPOSERVICIOS ,
							PysServiciosInstitucionBean.C_IDSERVICIO,
							PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,
							PysServiciosInstitucionBean.C_DESCRIPCION,
							PysServiciosInstitucionBean.C_CUENTACONTABLE,
							PysServiciosInstitucionBean.C_INICIOFINALPONDERADO,
							PysServiciosInstitucionBean.C_PORCENTAJEIVA,
							PysServiciosInstitucionBean.C_MOMENTOCARGO,
							PysServiciosInstitucionBean.C_SOLICITARBAJA,
							PysServiciosInstitucionBean.C_SOLICITARALTA,
							PysServiciosInstitucionBean.C_AUTOMATICO,
							PysServiciosInstitucionBean.C_FECHABAJA,
							PysServiciosInstitucionBean.C_SUFIJO,
							PysServiciosInstitucionBean.C_FECHAMODIFICACION,
							PysServiciosInstitucionBean.C_USUMODIFICACION,
							PysServiciosInstitucionBean.C_IDCONSULTA,
							PysServiciosInstitucionBean.C_FACTURACIONPONDERADA,
							PysServiciosInstitucionBean.C_CRITERIOS};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysServiciosInstitucionBean.C_IDINSTITUCION,
							PysServiciosInstitucionBean.C_IDTIPOSERVICIOS ,
							PysServiciosInstitucionBean.C_IDSERVICIO,
							PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysServiciosInstitucionBean bean = null;
		
		try {
			bean = new PysServiciosInstitucionBean();
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,PysServiciosInstitucionBean.C_IDINSTITUCION));
			bean.setIdTipoServicios (UtilidadesHash.getInteger(hash,PysServiciosInstitucionBean.C_IDTIPOSERVICIOS));
			bean.setIdServicio (UtilidadesHash.getLong(hash,PysServiciosInstitucionBean.C_IDSERVICIO));
			bean.setIdServicioInstitucion (UtilidadesHash.getLong(hash,PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));
			bean.setDescripcion (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_DESCRIPCION ));
			bean.setCuentacontable (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_CUENTACONTABLE ));
			bean.setInicioFinalPonderado (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_INICIOFINALPONDERADO));
			bean.setPorcentajeIva (UtilidadesHash.getFloat(hash, PysServiciosInstitucionBean.C_PORCENTAJEIVA));			
			bean.setMomentoCargo (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_MOMENTOCARGO ));
			bean.setSolicitarBaja (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_SOLICITARBAJA ));
			bean.setSolicitarAlta (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_SOLICITARALTA));
			bean.setAutomatico (UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_AUTOMATICO));
			bean.setFechaBaja(UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_FECHABAJA));
			bean.setSufijo(UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_SUFIJO));
			bean.setIdConsulta(UtilidadesHash.getLong(hash,PysServiciosInstitucionBean.C_IDCONSULTA));
			bean.setCriterios(UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_CRITERIOS));
			bean.setFacturacionPonderada(UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_FACTURACIONPONDERADA));
			
			bean.setFechaMod(UtilidadesHash.getString(hash,PysServiciosInstitucionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysServiciosInstitucionBean.C_USUMODIFICACION));
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
			PysServiciosInstitucionBean b = (PysServiciosInstitucionBean) bean;
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_IDINSTITUCION,b.getIdInstitucion ());			
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_IDSERVICIO,b.getIdServicio());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,b.getIdServiciosInstitucion());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_DESCRIPCION ,b.getDescripcion());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_CUENTACONTABLE ,b.getCuentacontable());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_CUENTACONTABLE ,b.getInicioFinalPonderado());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_PORCENTAJEIVA ,b.getPorcentajeIva());			
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_MOMENTOCARGO ,b.getMomentoCargo());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_SOLICITARBAJA ,b.getSolicitarBaja());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_SOLICITARALTA,b.getSolicitarAlta());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_AUTOMATICO,b.getAutomatico());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_SUFIJO, b.getSufijo());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_CRITERIOS, b.getCriterios());
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_FACTURACIONPONDERADA, b.getFacturacionPonderada());

			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_FECHAMODIFICACION, b.getFechaMod());			
			UtilidadesHash.set(htData,PysServiciosInstitucionBean.C_USUMODIFICACION, b.getUsuMod());
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
	 * Obtiene los datos de un servicio
	 * @param idInstitucion
	 * @param idServicio
	 * @param idServicioInstitucion
	 * @param idTipo
	 * @return un hashtable con los datos
	 * @throws ClsExceptions, SIGAException
	 */
	public Hashtable getServicio (Integer idInstitucion, Long idServicio, Long idServicioInstitucion, Integer idTipo) throws ClsExceptions, SIGAException
	{
		try{
			String select = "SELECT " +	
			            UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.T_NOMBRETABLA + "." + PysTipoServiciosBean.C_DESCRIPCION ,this.usrbean.getLanguage())+" AS DESCRIPCION_TIPO, " + 
						PysServiciosBean.T_NOMBRETABLA + "." + PysServiciosBean.C_DESCRIPCION + " AS DESCRIPCION_SERVICIO, " +
						PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION + " AS DESCRIPCION_S_INSTITUCION, " +
						PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_PORCENTAJEIVA + ", " +
						PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FECHABAJA + ", " +
						PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FACTURACIONPONDERADA + ", " +
						PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_MOMENTOCARGO;

			String from = " FROM " + 
						PysServiciosInstitucionBean.T_NOMBRETABLA + ", " + PysServiciosBean.T_NOMBRETABLA + ", " + PysTipoServiciosBean.T_NOMBRETABLA;
		
			String where = " WHERE " + 
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION + " = " + idInstitucion + " AND " +
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + " = " + idServicio + " AND " +
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + idTipo + " AND " +
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + " = " + idServicioInstitucion + " AND " +
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + " = " + PysServiciosBean.T_NOMBRETABLA + "." + PysServiciosBean.C_IDSERVICIO + " AND " + 
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + PysServiciosBean.T_NOMBRETABLA + "." + PysServiciosBean.C_IDTIPOSERVICIOS + " AND " +
			
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION + " = " + PysServiciosBean.T_NOMBRETABLA + "." + PysServiciosBean.C_IDINSTITUCION + " AND " +
			
			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + " = " + PysTipoServiciosBean.T_NOMBRETABLA + "." +  PysTipoServiciosBean.C_IDTIPOSERVICIOS;
		
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			String sql = select + from + where;
			if (rc.query(sql)) {
				if (rc.size() ==  1)  
					return (Hashtable)(((Row) rc.get(0)).getRow()); 
			}
		}
		catch(ClsExceptions e){
			throw e;
		}
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
       			throw new ClsExceptions(e,"Error en consulta BBDDs.");
       		}	
	    }
		return null;
	}

	/** 
	 * Recoge informacion de la tabla a partirde la descripcion del servicio<br/>
	 * y la forma de pago de este <br/>
	 * @param  desProducto - descripcion del producto 
	 * @param  idForma de pago - descripcion del producto
	 * @return  Vector - Filas de tabla seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public Vector obtenerServiciosInstitucion (String tipo, String categoria, String descripcion, String institucion, String pago, String estado) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_IDSERVICIO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_FACTURACIONPONDERADA  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_DESCRIPCION  + "," +
							PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FECHABAJA + ", " +
							PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_AUTOMATICO + ", " +							
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"." + PysServiciosInstitucionBean.C_PORCENTAJEIVA  + "," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.T_NOMBRETABLA +"." + PysTipoServiciosBean.C_DESCRIPCION  ,this.usrbean.getLanguage())+" AS TIPO," +
	            			PysServiciosBean.T_NOMBRETABLA +"." + PysServiciosBean.C_DESCRIPCION  + " AS CATEGORIA," +
	            			PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_DESCRIPCION + " AS IVA" + 
							" FROM " + 
							PysServiciosInstitucionBean.T_NOMBRETABLA + 
							" INNER JOIN "+ 
							PysTipoServiciosBean.T_NOMBRETABLA +
							" ON "+
								PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" +
								PysTipoServiciosBean.T_NOMBRETABLA +"."+ PysTipoServiciosBean.C_IDTIPOSERVICIOS +
							" INNER JOIN "+ 
							PysServiciosBean.T_NOMBRETABLA +
							" ON " +
								PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" +
								PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDTIPOSERVICIOS +						
								" AND " +
								PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" +
								PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDSERVICIO +
								
								" AND " +
								PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION + "=" +
								PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDINSTITUCION +
								
							" INNER JOIN "+ 
							PysTipoIvaBean.T_NOMBRETABLA +
							" ON "+
								PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_PORCENTAJEIVA + "=" +
								PysTipoIvaBean.T_NOMBRETABLA +"."+ PysTipoIvaBean.C_VALOR;
	            
	    			// Si se empleo la forma de pago como parametro de busqueda
					if (!pago.trim().equalsIgnoreCase("")){
						sql +=" INNER JOIN "+ 
									PysFormaPagoServiciosBean.T_NOMBRETABLA +
									" ON " +
										PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" +
										PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS +						
										" AND " +
										PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" +
										PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDSERVICIO +
										" AND " +
										PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION + "=" +
										PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDINSTITUCION +
										" AND " +
										PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + "=" +
										PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION;
					}	            
	            
					sql +=  " WHERE " +	PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION + "=" + institucion;

					if ((estado != null ) && (!estado.trim().equalsIgnoreCase(""))) {
						if (estado.equalsIgnoreCase("ALTA") || estado.equalsIgnoreCase("MANUAL") || estado.equalsIgnoreCase("AUTOMATICA")) {
							sql += " AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FECHABAJA + " IS NULL ";
							if (estado.equalsIgnoreCase("MANUAL")) {
								sql += " AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_AUTOMATICO + " = '0' ";
							}
							if (estado.equalsIgnoreCase("AUTOMATICA")) {
								sql += " AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_AUTOMATICO + " = '1' ";
							}
						}
						if (estado.equalsIgnoreCase("BAJA")) {
							sql += " AND " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FECHABAJA + " IS NOT NULL ";									
						}
					}
			
        			// Si se empleo el tipo como parametro de busqueda
					if (!tipo.trim().equalsIgnoreCase("")){
						sql +=" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + tipo;
					}
					
        			// Si se empleo la categoria como parametro de busqueda
					if (!categoria.trim().equalsIgnoreCase("")){
						sql +=" AND " + PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" + categoria;
					}						

        			// Si se empleo el nombre como parametro de busqueda
					if (!descripcion.trim().equals("")){
						sql +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(),PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION ); 
					}
					
        			// Si se empleo la forma de pago como parametro de busqueda
					if (!pago.trim().equalsIgnoreCase("")){
						sql +=" AND " + PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDFORMAPAGO + "=" + pago;
					}							
					
        			// Ordenado por...						
        			sql += " ORDER BY " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION; 
	            
	            if (rc.findNLS(sql)) {
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
	       			throw new ClsExceptions(e,"Error al obtener servicios de la institucion.");
	       		}	
		   }
	       return datos;                        
	    }	

	/** 
	 * Recoge toda informacion del del registro seleccionado a partir de sus claves<br/>
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoServ - identificador del tipo de servicio
	 * @param  idServ - identificador del servicio	 
	 * @param  idServInst - identificador del servicio institucion	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector obtenerInfoServicio (String idInst, String idTipoServ, String idServ, String idServInst) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION  + "," +
							PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FECHAMODIFICACION + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_CUENTACONTABLE + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_PORCENTAJEIVA + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_INICIOFINALPONDERADO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_MOMENTOCARGO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_SOLICITARBAJA + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_SOLICITARALTA + "," +
							PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FECHABAJA + ", " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_AUTOMATICO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_SUFIJO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDCONSULTA + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_CRITERIOS + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_FACTURACIONPONDERADA + "," +
	            			PysServiciosBean.T_NOMBRETABLA + "." + PysServiciosBean.C_DESCRIPCION + " AS CATEGORIA," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.T_NOMBRETABLA + "." + PysTipoServiciosBean.C_DESCRIPCION ,this.usrbean.getLanguage())+" AS TIPO " +							
							" FROM " + PysServiciosInstitucionBean.T_NOMBRETABLA + "," + PysServiciosBean.T_NOMBRETABLA + "," +PysTipoServiciosBean.T_NOMBRETABLA + 
							" WHERE " +
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDTIPOSERVICIOS +
							" AND " +
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" + PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDSERVICIO +
							" AND " +							
							PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDTIPOSERVICIOS + "=" + PysTipoServiciosBean.T_NOMBRETABLA +"."+ PysTipoServiciosBean.C_IDTIPOSERVICIOS +
							
							" AND " +							
							PysServiciosBean.T_NOMBRETABLA +"."+ PysServiciosBean.C_IDINSTITUCION + "=" + PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION +
							
							" AND " +							
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION + "=" + idInst +
							" AND " +
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + idTipoServ +
	            			" AND " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" + idServ +
	            			" AND " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + "=" + idServInst;
							
							// Ordenado por...						
							sql += " ORDER BY " + PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_DESCRIPCION; 
							
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
	           		if (e instanceof ClsExceptions){
	           			throw (ClsExceptions)e;
	           		}
	           		else {
	           			throw new ClsExceptions(e,"Error al obtener la informacion sobre un producto.");
	           		}
	       		}	
		   }
	       return datos;                        
	    }	
	
	/** 
	 * Recoge las formas de pago relacionadas con un determinado registro por sus claves <br/>
	 * y su atributo Internet
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoServ - identificador del tipo de servicio
	 * @param  idServ - identificador del producto	 
	 * @param  idServInst - identificador del producto institucion	  
	 * @param  internet - descripcion de la forma de pago 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector obtenerFormasPago (String idInst, String idTipoServ, String idServ, String idServInst, String internet) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIO + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDTIPOSERVICIOS  + "," +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA + "." + PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION  + "," +
	            			PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_IDFORMAPAGO + "," +
							PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_FECHAMODIFICACION + 							
							" FROM " + PysServiciosInstitucionBean.T_NOMBRETABLA + ", " + PysFormaPagoServiciosBean.T_NOMBRETABLA + 
							" WHERE " +
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION + "=" + idInst +
							" AND " +
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + idTipoServ +
	            			" AND " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" + idServ +
	            			" AND " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + "=" + idServInst +
	            			" AND " +							
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDINSTITUCION + "=" + PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_IDINSTITUCION +
							" AND " +
							PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS  +
	            			" AND " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIO + "=" + PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_IDSERVICIO  +
	            			" AND " +
	            			PysServiciosInstitucionBean.T_NOMBRETABLA +"."+ PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION + "=" + PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION +
	            			" AND " +
	            			PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_INTERNET + "=" + "'" + internet + "'";							
							// Ordenado por...						
							sql += " ORDER BY " + PysFormaPagoServiciosBean.T_NOMBRETABLA + "." + PysFormaPagoServiciosBean.C_INTERNET; 
							
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
		   			if (e instanceof ClsExceptions){
		   				throw (ClsExceptions)e;
		   			}
		   			else {
		   				throw new ClsExceptions(e,"Error al obtener formas de pago de la institucion.");
		   			}
		   		}	
	       }
	       return datos;                        
	    }		

	/** 
	 * Prepara una tabla para insertarla en la tabla. <br/>
	 * Obtiene el campo IDSERVICIOSIINSTITUCION y los campos SOLICITARBAJA, <br/>
	 * SOLICITARALTA en caso de que no estuviesen "marcados" en el formulario <br/>
	 * Ademas incorpora el campo momento cargo "proximo periodo"
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions, SIGAException 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDSERVICIOSINSTITUCION) + 1) AS IDSERVICIOSINSTITUCION FROM " + nombreTabla +
					" WHERE " +
					PysServiciosInstitucionBean.C_IDINSTITUCION + "=" + (String)entrada.get(PysServiciosInstitucionBean.C_IDINSTITUCION) + 
					" AND " +
					PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + (String)entrada.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS) + 					
					" AND " +
					PysServiciosInstitucionBean.C_IDSERVICIO + "=" + (String)entrada.get(PysServiciosInstitucionBean.C_IDSERVICIO);
										
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION).equals("")) {
					entrada.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,"1");
				}
				else entrada.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,(String)prueba.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));								
			}
			if (!entrada.containsKey(PysServiciosInstitucionBean.C_SOLICITARBAJA)) {
				entrada.put(PysServiciosInstitucionBean.C_SOLICITARBAJA,"0");
			}
			
			if (!entrada.containsKey(PysServiciosInstitucionBean.C_SOLICITARALTA)) {
				entrada.put(PysServiciosInstitucionBean.C_SOLICITARALTA,"0");
			}
			
			if (!entrada.containsKey(PysServiciosInstitucionBean.C_AUTOMATICO)) {
				entrada.put(PysServiciosInstitucionBean.C_AUTOMATICO,"0");
			}
			
			entrada.put(PysServiciosInstitucionBean.C_MOMENTOCARGO,ClsConstants.MOMENTO_CARGO_PROXFACTURA);
			
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
	   				throw new ClsExceptions(e,"Error al ejecutar el 'prepararInsert' en B.D.");
	   			}
	   		}	
	    }
		return entrada;
	}	
	
	/** 
	 * Obtiene el maximo campo IDSERVICIOSIINSTITUCION para una entrada determinada, <br/>
	 * @param  institucion - id institucion 
	 * @param  tipo - id tipo servicio
	 * @param  servicio - id servicio
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public String getMaxIdServiciosInstitucion (String institucion, String tipo, String servicio)throws ClsExceptions, SIGAException 
	{
		String maximo="0";	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT MAX(IDSERVICIOSINSTITUCION) AS IDSERVICIOSINSTITUCION FROM " + nombreTabla +
					" WHERE " +
					PysServiciosInstitucionBean.C_IDINSTITUCION + "=" + institucion + 
					" AND " +
					PysServiciosInstitucionBean.C_IDTIPOSERVICIOS + "=" + tipo + 					
					" AND " +
					PysServiciosInstitucionBean.C_IDSERVICIO + "=" + servicio;
										
		try {					
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION).equals("")) {
					maximo="1";
				}
				else{
					long aux = new Long((String)prueba.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION)).longValue() + 1; 
					maximo= new Long(aux).toString();
				}													
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
	   				throw new ClsExceptions(e,"Error al ejecutar obtener maximo en B.D.");
	   			}
	   		}	
	    }
		return maximo;
	}
	
	
	
	/** 
	 * Busca los servicios relacionados con un determinado anticipo por sus claves 
	 * 
	 * @param  categoriaServicio 
	 * @param  tipoServicio
	 * @param  nombreServicio	 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector getBuscarServiciosAnticipo(String categoriaServicio, String tipoServicio, String nombreServicio) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
	       try {
	    	   
	    	    String idioma = this.usrbean.getLanguage();
	    	    String idinstitucion = this.usrbean.getLocation();
	    	   
	            RowsContainer rc = new RowsContainer(); 
	            
	            String sql=" select si.idinstitucion AS IDINST, si.idtiposervicios AS IDTIPOSERV, si.idservicio AS IDSERV, si.idserviciosinstitucion AS IDESERVINST, "; 
	            contador++;
	            codigosBind.put(new Integer(contador), idioma);
	            sql+=" f_siga_getrecurso(ts.descripcion, :"+contador+")"+" AS NOMBRECATSERVICIO , s.descripcion as NOMBRETIPOSERVICIO, si.descripcion as NOMBRESERVICIO";
	            sql+=" from pys_tiposervicios ts, pys_servicios s, pys_serviciosinstitucion si";
	            sql+=" where ts.idtiposervicios =s.idtiposervicios";
	            sql+=" and   s.idinstitucion = si.idinstitucion";
	            sql+=" and   s.idtiposervicios = si.idtiposervicios";
	            sql+=" and   s.idservicio = si.idservicio";
	            contador++;
	            codigosBind.put(new Integer(contador), idinstitucion);
	            sql+=" and   si.idinstitucion= :"+contador;
	            if((categoriaServicio != null) && (!categoriaServicio.equals(""))){
	            contador++;
	            codigosBind.put(new Integer(contador),categoriaServicio);
	            sql+=" and   si.idtiposervicios =:" +contador;
	            }
	            if((tipoServicio != null) &&(!tipoServicio.equals(""))){
	            contador++;
	            codigosBind.put(new Integer(contador),tipoServicio);
	            sql+=" and   si.idservicio= :"+contador;
	            }
	            if((nombreServicio != null) &&(!nombreServicio.equals(""))){
	            contador++;
	            sql+=" and "+ComodinBusquedas.prepararSentenciaCompletaBind(nombreServicio,  "si.descripcion" , contador, codigosBind);
	            //sql+=" and   si.descripcion like ':nombre%'";
	            }
	            sql+=" order by si.descripcion";
							
	            if (rc.findNLSBind(sql, codigosBind)) {
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
		   			if (e instanceof ClsExceptions){
		   				throw (ClsExceptions)e;
		   			}
		   			else {
		   				throw new ClsExceptions(e,"Error al buscar los servicios de un anticipo.");
		   			}
		   		}	
	       }
	       return datos;                        
	    }
	
	
	
	/** 
	 * Gestiona automaticamente todas las solicitudes pendientes de una determinada institucion
	 * @param  institucion - identificador institucion
	 * @param  usuario - userBean involucrado 
	 * @return  boolean - si se ha llevado a cabo la operacion  
	 */	
	public boolean ComprobacionServiciosAutomaticos (Integer institucion) throws ClsExceptions {
		
		boolean resul=true;
		boolean correcto;
		Vector vector=new Vector();
		Enumeration listaSolicitudes;
		Hashtable solicitud = new Hashtable();
		
		UsrBean userBean = UsrBean.UsrBeanAutomatico(institucion.toString());
		//userBean.setLanguage(idioma);
		
		UserTransaction tx = null;
		
		try{			
			
			// Comienzo control de transacciones
			tx = userBean.getTransactionPesada();
			
			String resultado[] = EjecucionPLs.ejecutarPL_Revision_Auto(institucion.toString(),
					  "",
					  ""+new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO));
			if ((resultado == null) || (!resultado[0].equals("0")))
			throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_AUTO. Param: "+institucion.toString() + " Resultado: "+resultado[0]+"-"+resultado[1]);
		}
		catch(Exception ee){
			ClsLogging.writeFileLogError("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_AUTO para la institucion "+institucion,ee,this.usrbean, 3);
			try{	
				tx.rollback();
			} catch (Exception e) {
			}	
		}		
		return resul;
	}
	
		
}