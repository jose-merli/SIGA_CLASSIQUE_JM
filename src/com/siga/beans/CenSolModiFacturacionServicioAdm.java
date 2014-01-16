//VERSIONES:
//raul.ggonzalez 07-02-2005 creacion

package com.siga.beans;

import java.util.*;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;

/**
* Administrador de la tabla CEN_SOLMODIFACTURACIONSERVICIOS
* @author AtosOrigin 07-02-2005
*/
public class CenSolModiFacturacionServicioAdm extends MasterBeanAdministrador {

/**
 * Constructor de la clase
 * @param usuario Con el usuario de modificacion
 */
	public CenSolModiFacturacionServicioAdm(UsrBean usuario) {
		super(CenSolModiFacturacionServicioBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Obtiene un array con los nombres de los campos de la tabla
	 * @return String[] con los nombres de los campos
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenSolModiFacturacionServicioBean.C_FECHAALTA,				CenSolModiFacturacionServicioBean.C_IDSOLICITUD,
							CenSolModiFacturacionServicioBean.C_FECHAMODIFICACION,					CenSolModiFacturacionServicioBean.C_IDPETICION,
							CenSolModiFacturacionServicioBean.C_IDCUENTA,				CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS,
							CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC,			CenSolModiFacturacionServicioBean.C_MOTIVO,
							CenSolModiFacturacionServicioBean.C_IDINSTITUCION,				CenSolModiFacturacionServicioBean.C_USUMODIFICACION,
							CenSolModiFacturacionServicioBean.C_IDPERSONA,			
							CenSolModiFacturacionServicioBean.C_IDSERVICIO,		
							CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION};
		return campos;
	}

	/**
	 * Obtiene un array con las claves de la tabla
	 * @return String[] con las claves
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenSolModiFacturacionServicioBean.C_IDSOLICITUD};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/**
	 * Convierte de hastable de valores de la tabla a un bean de la tabla
	 * @param hash Hashtable con los valores
	 * @return MasterBean transaformado con los datos
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenSolModiFacturacionServicioBean bean = null;
		
		try {
			bean = new CenSolModiFacturacionServicioBean();
			bean.setFechaAlta(UtilidadesHash.getString(hash,CenSolModiFacturacionServicioBean.C_FECHAALTA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolModiFacturacionServicioBean.C_FECHAMODIFICACION));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,CenSolModiFacturacionServicioBean.C_IDCUENTA));
			bean.setIdEstadoSolic(UtilidadesHash.getInteger(hash,CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC));
			bean.setIdInstitucion(UtilidadesHash.getLong(hash,CenSolModiFacturacionServicioBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenSolModiFacturacionServicioBean.C_IDPERSONA));
			bean.setIdServicio(UtilidadesHash.getLong(hash,CenSolModiFacturacionServicioBean.C_IDSERVICIO));
			bean.setIdServiciosInstitucion(UtilidadesHash.getInteger(hash,CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION));
			bean.setIdSolicitud(UtilidadesHash.getLong(hash,CenSolModiFacturacionServicioBean.C_IDSOLICITUD));
			bean.setIdPeticion(UtilidadesHash.getLong(hash,CenSolModiFacturacionServicioBean.C_IDPETICION));
			bean.setIdTipoServicios(UtilidadesHash.getInteger(hash,CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS));
			bean.setMotivo(UtilidadesHash.getString(hash,CenSolModiFacturacionServicioBean.C_MOTIVO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolModiFacturacionServicioBean.C_USUMODIFICACION));
			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/**
	 * Convierte de un bean de la tabla a hastable de valores de la tabla 
	 * @param bean MasterBean con los valores
	 * @return Hashtable transaformado con los datos
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenSolModiFacturacionServicioBean b = (CenSolModiFacturacionServicioBean) bean;
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_FECHAALTA, 		b.getFechaAlta());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDCUENTA, 			b.getIdCuenta());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC, 		b.getIdEstadoSolic());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDPERSONA, 			b.getIdPersona());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDSERVICIO, 	b.getIdServicio());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION, 		b.getIdServiciosInstitucion());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDSOLICITUD, 		b.getIdSolicitud());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDPETICION, 		b.getIdPeticion());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS, 		b.getIdTipoServicios());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_MOTIVO, 			b.getMotivo());
			UtilidadesHash.set(htData, CenSolModiFacturacionServicioBean.C_USUMODIFICACION, 		b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Obtiene un nuevo valor para el campos IdSolicitud (Insercion)	
	 * @return  Long  - IdSolicitud secuencial  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long	getNuevoId()throws ClsExceptions, SIGAException{
		String sql;
		RowsContainer rc = null;
		int contador = 0;
		Long id=null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql ="SELECT SEQ_SOLMODIFACTURACIONSERVICIO.NEXTVAL FROM DUAL";
		
		try {		
			if (rc.findForUpdate(sql)) {						
				Row fila = (Row) rc.get(0);
				id = Long.valueOf((String)fila.getRow().get("NEXTVAL"));														
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en BBDD");		
		}		
		return id;
	}

	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idSolic - identificador de la solicitud 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerEntradaSolicitudModificacion (String idSolic) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSOLICITUD + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDINSTITUCION + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSERVICIO + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDPERSONA + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDCUENTA + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDPETICION + "," +
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_MOTIVO + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_FECHAALTA + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC + "," +							
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_FECHAMODIFICACION + "," +
	            			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_USUMODIFICACION + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
	            			CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + 
							" FROM " + CenSolModiFacturacionServicioBean.T_NOMBRETABLA + 
									 " INNER JOIN " + CenCuentasBancariasBean.T_NOMBRETABLA +
									 				" ON "+ 
													CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDCUENTA + "=" +
													CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDCUENTA + " AND " +														 
													CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDINSTITUCION + "=" +
													CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDINSTITUCION + " AND " +
													CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDPERSONA + "=" +
													CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDPERSONA +
							" WHERE " +
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDSOLICITUD + "=" + idSolic;
														
	            if (rc.findForUpdate(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de solicitudes de modificacion.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge informacion de las tablas a partir de la informacion suministrada por la busqueda<br/>
	 * @param  institucion - identificador de la institucion
	 * @param  estado - estado de la solicitud	 
	 * @param  fechaI - inicio de rango de la fecha de alta
	 * @param  fechaF - fin de rango de la fecha de alta
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getSolicitudes(String institucion, String estado, String fechaI, String fechaF) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
				
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSOLICITUD + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDINSTITUCION + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSERVICIO + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDPERSONA + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDCUENTA + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDPETICION + "," +
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_MOTIVO + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_FECHAALTA + "," +
			    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC + "," +							
	            			//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
	            			ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION+" as TIPOMODIF, "+
			       			" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage())+
							" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
							" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION+") as TEXTOTIPOMODIF"+
							" FROM " + 
							//CenSolModiFacturacionServicioBean.T_NOMBRETABLA +", "+CenTiposModificacionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+ 
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+
							" WHERE " +
							//CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION) +
							//" AND " +							
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
							" AND " +
							CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDINSTITUCION + "=" + institucion;	            
											
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						  CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				
				
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						  CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND (" +
					  CenSolModiFacturacionServicioBean.T_NOMBRETABLA +"."+ CenSolModiFacturacionServicioBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')" +									 
					  " OR " +
					  GstDate.dateBetween0and24h(CenSolModiFacturacionServicioBean.C_FECHAALTA,fechaF)+")";									 
				}							
							
				sql += " ORDER BY " + CenSolModiFacturacionServicioBean.C_FECHAALTA + " DESC"; 										
							
	            if (rc.find(sql)) {
	            	for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener solicitudes de modificacion de facturacion");
	       }
	       return datos;                        
	    }

	/** 
	 * Deniega la solicitud de modificacion pasada como parametro (su estado pasa a denegado) <br/>
	 * @param  solicitud - solicitud a denegar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean denegarSolicitud(String solicitud) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
	
       try {       	
			if (!solicitud.equalsIgnoreCase("")){
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolicitudModificacionCVBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolicitudModificacionCVBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
			}	
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al denegar solicitudes de datos del curriculum");
       }
       return correcto;                        
    }
	
	
	/** 
	 * Procesa la solicitude de modificacion pasada como parametro (su estado pasa a realizado <br/>
	 * y se actualiza el registro adecuado con la informacion suministrada, además de la entrada <br/>
	 * pertinente en el historico) 
	 * @param  solicitud - solicitud a procesar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean procesarSolicitud(String solicitud, String idioma) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();
		Hashtable clave = new Hashtable();
		Hashtable servicioOriginal = new Hashtable(); 
		PysServiciosSolicitadosBean servicioModificado = new PysServiciosSolicitadosBean();		
		CenHistoricoBean beanHist = new CenHistoricoBean();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				// Obtengo la solicitud a modificar
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				
				// Actualizo el estado de la solicitud a realizada				
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
				else{
					// Obtengo el registro a modificar de la tabla cliente y preparo el bean correspondiente
					PysServiciosSolicitadosAdm adminSolic = new PysServiciosSolicitadosAdm(this.usrbean);					
					clave.put(PysServiciosSolicitadosBean.C_IDINSTITUCION,hash.get(CenSolModiFacturacionServicioBean.C_IDINSTITUCION));
					clave.put(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS,hash.get(CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS));
					clave.put(PysServiciosSolicitadosBean.C_IDSERVICIO,hash.get(CenSolModiFacturacionServicioBean.C_IDSERVICIO));
					clave.put(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION,hash.get(CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION));
					clave.put(PysServiciosSolicitadosBean.C_IDPETICION,hash.get(CenSolModiFacturacionServicioBean.C_IDPETICION));
					servicioOriginal=((PysServiciosSolicitadosBean)adminSolic.selectForUpdate(clave).firstElement()).getOriginalHash();
					// Preparo en bean modificado
					servicioModificado=((PysServiciosSolicitadosBean)adminSolic.selectForUpdate(clave).firstElement());
					servicioModificado.setOriginalHash(servicioModificado.getOriginalHash());					
					// Fijamos los datos del Historico
					beanHist.setMotivo((String)hash.get(CenSolModiFacturacionServicioBean.C_MOTIVO));			
					// Actualizo el registro Cuentas con historico				
					if (!adminSolic.updateConHistorico(servicioModificado,beanHist, idioma)){
						correcto=false;
					}								
				}				
			}	
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al procesar solicitudes de modificaciones de servicios solicitados");
       }
       return correcto;                        
    }
}
