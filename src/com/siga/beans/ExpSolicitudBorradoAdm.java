/*
 * VERSIONES:
 * 
 * juan.grau - 14-02-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
*
* Clase que gestiona la tabla EXP_SOLICITUDBORRADO de la BBDD
* 
*/
public class ExpSolicitudBorradoAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public ExpSolicitudBorradoAdm(UsrBean usu) {
		super(ExpSolicitudBorradoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {ExpSolicitudBorradoBean.C_IDSOLICITUD,
							ExpSolicitudBorradoBean.C_ANIOEXPEDIENTE,
							ExpSolicitudBorradoBean.C_IDINSTITUCION,
							ExpSolicitudBorradoBean.C_IDPERSONA,							
							ExpSolicitudBorradoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
							ExpSolicitudBorradoBean.C_FECHAALTA,							
							ExpSolicitudBorradoBean.C_IDESTADOSOLIC,							
							ExpSolicitudBorradoBean.C_FECHAMODIFICACION,
							ExpSolicitudBorradoBean.C_USUMODIFICACION,
							ExpSolicitudBorradoBean.C_IDTIPOEXPEDIENTE,
							ExpSolicitudBorradoBean.C_MOTIVO,
							ExpSolicitudBorradoBean.C_NUMEROEXPEDIENTE
		};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {ExpSolicitudBorradoBean.C_IDSOLICITUD};
		return claves;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ExpSolicitudBorradoBean bean = null;
		
		try {
			bean = new ExpSolicitudBorradoBean();
			bean.setIdSolicitud (UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_IDSOLICITUD));
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_IDINSTITUCION));
			bean.setIdPersona (UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_IDPERSONA));			
			bean.setIdInstitucion_tipoExpediente(UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setFechaAlta(UtilidadesHash.getString(hash,ExpSolicitudBorradoBean.C_FECHAALTA));
			bean.setIdEstadoSolic (UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_IDESTADOSOLIC));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_ANIOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_IDTIPOEXPEDIENTE));
			bean.setMotivo(UtilidadesHash.getString(hash,ExpSolicitudBorradoBean.C_MOTIVO));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_NUMEROEXPEDIENTE));			
			bean.setFechaMod(UtilidadesHash.getString(hash,ExpSolicitudBorradoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ExpSolicitudBorradoBean.C_USUMODIFICACION));			
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
			ExpSolicitudBorradoBean b = (ExpSolicitudBorradoBean) bean;
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_ANIOEXPEDIENTE,b.getAnioExpediente());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_IDINSTITUCION,b.getIdInstitucion ());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_IDPERSONA,b.getIdPersona ());			
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,b.getIdInstitucion_tipoExpediente());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_IDESTADOSOLIC,b.getIdEstadoSolic());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_FECHAALTA,b.getFechaAlta());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_IDTIPOEXPEDIENTE,b.getIdTipoExpediente());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_MOTIVO,b.getMotivo());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_NUMEROEXPEDIENTE,b.getNumeroExpediente());
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_FECHAMODIFICACION,b.getFechaMod());			
			UtilidadesHash.set(htData,ExpSolicitudBorradoBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idSolic - identificador de la solicitud 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerEntradaSolicitudModificacion (String idSolic) throws ClsExceptions {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT * FROM " + ExpSolicitudBorradoBean.T_NOMBRETABLA + 
							" WHERE " +
							ExpSolicitudBorradoBean.C_IDSOLICITUD + " = " + idSolic;
														
	            if (rc.findForUpdate(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de solicitudes de modificacion.");
	       }
	       return datos;                        
	    }

	/** 
	 * Recoge informacion de la tabla a partir de la informacion suministrada por la busqueda<br/>
	 * @param  institucion - identificador de la institucion
	 * @param  estado - estado de la solicitud	 
	 * @param  fechaI - inicio de rango de la fecha de alta
	 * @param  fechaF - fin de rango de la fecha de alta
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getSolicitudes(String institucion, String estado, String fechaI, String fechaF) throws ClsExceptions {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " + 
						ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDSOLICITUD + "," +
						ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDPERSONA + "," +
            			ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDINSTITUCION + "," +
            			ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_FECHAALTA + "," +
            			ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDESTADOSOLIC + "," +
            			ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_MOTIVO + "," +
            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
            			ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES+" as TIPOMODIF, "+
						" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage())+
						" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
						" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES+") as TEXTOTIPOMODIF"+
						" FROM " + 
						ExpSolicitudBorradoBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+ 
						" WHERE " + 
						ExpSolicitudBorradoBean.T_NOMBRETABLA +"."+ ExpSolicitudBorradoBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
						" AND " +
						ExpSolicitudBorradoBean.T_NOMBRETABLA +"."+ ExpSolicitudBorradoBean.C_IDINSTITUCION + "=" + institucion;        
						
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						ExpSolicitudBorradoBean.T_NOMBRETABLA +"."+ ExpSolicitudBorradoBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				

				String auxFechaInicio = "";
				String auxFechaFin = "";
				if (!fechaI.trim().equals("")) 
	 				auxFechaInicio = GstDate.getApplicationFormatDate("",fechaI);
				if (!fechaF.trim().equals("")) 
	 				auxFechaFin = GstDate.getApplicationFormatDate("",fechaF);
				
				if ((fechaI!=null && !fechaI.trim().equals("")) || (fechaF!=null && !fechaF.trim().equals(""))) {
					sql += " AND " + GstDate.dateBetweenDesdeAndHasta(ExpSolicitudBorradoBean.T_NOMBRETABLA +"."+ ExpSolicitudBorradoBean.C_FECHAALTA,auxFechaInicio,auxFechaFin);
				} 
				
				/* RGG cambio funcion fechas desde hasta
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						ExpSolicitudBorradoBean.T_NOMBRETABLA +"."+ ExpSolicitudBorradoBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND " +
						ExpSolicitudBorradoBean.T_NOMBRETABLA +"."+ ExpSolicitudBorradoBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')";									 
				}							
				*/
				sql += " ORDER BY " + ExpSolicitudBorradoBean.C_FECHAALTA; 										
							
	            if (rc.find(sql)) {
	            	for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener solicitudes de borrado");
	       }
	       return datos;                        
	    }	
	
	
	/** 
	 * Deniega la solicitud de modificacion pasada como parametro (su estado pasa a denegado) <br/>
	 * @param  solicitud - solicitud a denegar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean denegarSolicitud(String solicitud) throws ClsExceptions {

		boolean correcto=true;
		Vector original = new Vector();
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				
				String idEstadoOriginal = (String)hashOriginal.get(ExpSolicitudBorradoBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				hash=(Hashtable)hashOriginal.clone();
				hash.put(ExpSolicitudBorradoBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
			}	
       }
       catch (Exception e) {
       		throw new ClsExceptions (e, "Error al denegar la solicitud de datos basicos");
       }
       return correcto;                        
    }
	
	/** 
	 * Procesa la solicitud de modificacion pasada como parametro (su estado pasa a realizado <br/>
	 * y se actualiza el registro adecuado con la informacion suministrada, además de la entrada <br/>
	 * pertinente en el historico) 
	 * @param  solicitud - solicitud a procesar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean procesarSolicitud(String solicitud, Integer usuario, String idioma) throws ClsExceptions {

		boolean correcto=true;
		Vector original = new Vector();
		Vector busqueda = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
		CenHistoricoBean beanHist = new CenHistoricoBean();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				// Obtengo la solicitud a modificar
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				String idInstitucion = row.getString(ExpExpedienteBean.C_IDINSTITUCION);
				String idInstitucionTipoExpediente = row.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE);
				String idTipoExpediente = row.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE);
				String numeroExpediente = row.getString(ExpExpedienteBean.C_NUMEROEXPEDIENTE);
				String anioExpediente = row.getString(ExpExpedienteBean.C_ANIOEXPEDIENTE);
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				hash.put(ExpSolicitudBorradoBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));
				
				String idEstadoOriginal = (String)hashOriginal.get(ExpSolicitudBorradoBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return false;
				
				
				// Actualizo el estado de la solicitud a realizar				
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
				else{
					// Obtengo el registro a modificar de la tabla expedientes y preparo el bean correspondiente
				    /* monto la hash con la pk para obtener el expediente */
				    Hashtable pk = new Hashtable();
				    pk.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				    pk.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucionTipoExpediente);
				    pk.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				    pk.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numeroExpediente);
				    pk.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
					ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.usrbean);
					ExpExpedienteBean expOriginal = (ExpExpedienteBean) expAdm.selectByPKForUpdate(pk).firstElement();
					expOriginal.setEsVisible("N");
					expOriginal.setEsVisibleEnFicha("N");
					expOriginal.setAnotacionesCanceladas("SYSDATE");
					
					// Fijamos los datos del Historico
					beanHist.setMotivo(row.getString(ExpSolicitudBorradoBean.C_MOTIVO));			
					// Actualizo el registro cliente con historico				
					if (!updateConHistorico(expOriginal,beanHist, idioma)){
						correcto=false;
					}								
				}				
			}	
       }
       catch (ClsExceptions ee) {
       	correcto=false;
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al procesar solicitudes de datos basicos");
       }       
       return correcto;                        
    }
	
	/**
	 * Actualiza el bean de expediente y rellena la tabla de historicos (CEN_HISTORICO)
	 * @param bean -> bean de expediente
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (ExpExpedienteBean bean, CenHistoricoBean beanHis, String idioma) throws ClsExceptions 
	{
	    ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.usrbean);
		try {
			if (expAdm.update(bean)) {
				CenHistoricoAdm admHis = new CenHistoricoAdm(this.usrbean);
				beanHis.setIdHistorico(admHis.getNuevoID(beanHis));
				if (admHis.insertCompleto(beanHis, bean, CenHistoricoAdm.ACCION_UPDATE, idioma)) {				    
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	
	/**
	 * Obtiene el bean de solicitud a partir de su idSolicitud
	 * @param String idSolicitud
	 */
	public ExpSolicitudBorradoBean getSolicitud (String idSolicitud) throws ClsExceptions 
	{
		try {
		    Hashtable pk = new Hashtable();
		    pk.put(ExpSolicitudBorradoBean.C_IDSOLICITUD,idSolicitud);
		    Vector vBean = this.selectByPK(pk);			
			return (ExpSolicitudBorradoBean)vBean.firstElement();
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar datos en B.D.");
		}
	}
	/** 
	 * Obtiene el campo IDSOLICITUD, <br/>	 
	 * @return  Long  - IdSolicitud secuencial  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long	getNuevoId()throws ClsExceptions {
		String sql;
		RowsContainer rc = null;
		int contador = 0;
		Long id=null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql ="SELECT SEQ_SOLICITBORRADOANOTACIONES.NEXTVAL FROM DUAL";
		
		try {		
			if (rc.findForUpdate(sql)) {	
				Row fila = (Row) rc.get(0);
				id = Long.valueOf((String)fila.getRow().get("NEXTVAL"));														
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en BBDD");		
		}		
		return id;
	}

}

