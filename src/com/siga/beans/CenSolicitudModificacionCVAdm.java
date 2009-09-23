/*
 * Created on Jan 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenSolicitudModificacionCVAdm extends MasterBeanAdministrador{

	/**
	 * @param tabla
	 * @param usuario
	 */
	public CenSolicitudModificacionCVAdm(UsrBean usuario) {
		super(CenSolicitudModificacionCVBean.T_NOMBRETABLA, usuario);
	}		

	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_SOLICITUDMODIFICACIONCV 
	 * @author nuria.rgonzalez 11-01-05	 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenSolicitudModificacionCVBean.C_IDSOLICITUD,
				CenSolicitudModificacionCVBean.C_MOTIVO,
				CenSolicitudModificacionCVBean.C_FECHAINICIO,
				CenSolicitudModificacionCVBean.C_FECHAFIN,
				CenSolicitudModificacionCVBean.C_DESCRIPCION,
				CenSolicitudModificacionCVBean.C_IDPERSONA,	
				CenSolicitudModificacionCVBean.C_IDINSTITUCION,	
				CenSolicitudModificacionCVBean.C_IDCV,						
				CenSolicitudModificacionCVBean.C_IDTIPOCV,		
				CenSolicitudModificacionCVBean.C_FECHAMODIFICACION,
				CenSolicitudModificacionCVBean.C_USUMODIFICACION,
				CenSolicitudModificacionCVBean.C_IDESTADOSOLIC,
				CenSolicitudModificacionCVBean.C_FECHAALTA,
				CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1,
				CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2,
				CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1, 
				CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2};
		return campos;
	}

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_SOLICITUDMODIFICACIONCV 
	 * @author nuria.rgonzalez  11-01-05		  
	 */
	protected String[] getClavesBean() {
		String[] campos = {	CenSolicitudModificacionCVBean.C_IDSOLICITUD };
		return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/**
	 * Devuelve un CenSolicitudModificacionCVBean con los campos de la tabla CEN_SOLICITUDMODIFICACIONCV 
	 * @author nuria.rgonzalez 11-01-05	 
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenSolicitudModificacionCVBean bean = null;
		try{
			bean = new CenSolicitudModificacionCVBean();
			bean.setIdSolicitud(UtilidadesHash.getLong(hash,CenSolicitudModificacionCVBean.C_IDSOLICITUD));
			bean.setMotivo(UtilidadesHash.getString(hash,CenSolicitudModificacionCVBean.C_MOTIVO));
			bean.setFechaInicio(UtilidadesHash.getString(hash,CenSolicitudModificacionCVBean.C_FECHAINICIO));
			bean.setFechaFin(UtilidadesHash.getString(hash,CenSolicitudModificacionCVBean.C_FECHAFIN));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenSolicitudModificacionCVBean.C_DESCRIPCION));			
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenSolicitudModificacionCVBean.C_IDPERSONA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenSolicitudModificacionCVBean.C_IDINSTITUCION));
			bean.setIdCV(UtilidadesHash.getInteger(hash,CenSolicitudModificacionCVBean.C_IDCV));
			bean.setIdTipoCV(UtilidadesHash.getInteger(hash,CenSolicitudModificacionCVBean.C_IDTIPOCV));			
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolicitudModificacionCVBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolicitudModificacionCVBean.C_USUMODIFICACION));
			bean.setIdEstadoSolic(UtilidadesHash.getInteger(hash,CenSolicitudModificacionCVBean.C_IDESTADOSOLIC));
			bean.setFechaAlta(UtilidadesHash.getString(hash,CenSolicitudModificacionCVBean.C_FECHAALTA));
			bean.setIdTipoCVSubtipo1(UtilidadesHash.getString(hash,CenDatosCVBean.C_IDTIPOCVSUBTIPO1));
			bean.setIdTipoCVSubtipo2(UtilidadesHash.getString(hash,CenDatosCVBean.C_IDTIPOCVSUBTIPO2));
			bean.setIdInstitucion_subt1(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDINSTITUCION_SUBT1));
			bean.setIdInstitucion_subt2(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDINSTITUCION_SUBT2));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_SOLICITUDMODIFICACIONCV 
	 * @author nuria.rgonzalez 11-01-05	 
	 * @param CenSolicitudModificacionCVBean 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenSolicitudModificacionCVBean b = (CenSolicitudModificacionCVBean) bean;
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_MOTIVO, b.getMotivo());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_FECHAINICIO, b.getFechaInicio());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_FECHAFIN, b.getFechaFin());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_IDCV, b.getIdCV());			
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_IDTIPOCV, b.getIdTipoCV());	
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_USUMODIFICACION, b.getUsuMod());	
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_IDESTADOSOLIC, b.getIdEstadoSolic());
			UtilidadesHash.set(hash, CenSolicitudModificacionCVBean.C_FECHAALTA, b.getFechaAlta());
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDTIPOCVSUBTIPO1, String.valueOf(b.getIdTipoCVSubtipo1()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDTIPOCVSUBTIPO2,String.valueOf(b.getIdTipoCVSubtipo2()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDINSTITUCION_SUBT1, String.valueOf(b.getIdInstitucion_subt1()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDINSTITUCION_SUBT2, String.valueOf(b.getIdInstitucion_subt2()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	/** 
	 * Prepara una tabla hash para insertarla en la tabla. <br/>
	 * Obtiene el campo IDSOLICITUD, <br/>	 
	 * @return  Long  - IdSolicitud secuencial  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long	getNuevoId()throws ClsExceptions, SIGAException {
		String sql;
		RowsContainer rc = null;
		int contador = 0;
		Long id=null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql = "SELECT SEQ_SOLICITUDMODIFICACIONCV.NEXTVAL FROM DUAL";
		
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
    						CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDSOLICITUD + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_MOTIVO + "," +
    						CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAINICIO + "," +														
    						CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAFIN + "," +
    						CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_DESCRIPCION + "," +														
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDPERSONA + "," +
	            			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDINSTITUCION + "," +
	            			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDCV+", "+
    						CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDCV + " AS CODIGO," +
    						CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDTIPOCV + "," +							
	            			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAALTA + "," +
	            			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDESTADOSOLIC + "," +
	            			//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
	            			ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV+" as TIPOMODIF, "+
							" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage())+
							" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
							" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV+") as TEXTOTIPOMODIF"+
							" FROM " + 
							//CenSolicitudModificacionCVBean.T_NOMBRETABLA +", "+CenTiposModificacionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+ 
							CenSolicitudModificacionCVBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+
							" WHERE " +
							//CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV) +
							//" AND " +							
							CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
							" AND " +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_IDINSTITUCION + "=" + institucion;	            
											
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				
				
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND (" +
					  CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')" +									 
					  " OR " +
					  GstDate.dateBetween0and24h(CenSolicitudModificacionCVBean.C_FECHAALTA,fechaF)+")";									 
				}							
							
				sql += " ORDER BY " + CenSolicitudModificacionCVBean.C_FECHAALTA + " DESC"; 										
							
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
	       	throw new ClsExceptions (e, "Error al obtener solicitudes de modificacion de direcciones");
	       }
	       return datos;                        
	    }	

	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idSolic - identificador de la solicitud 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerEntradaSolicitudModificacion (String idSolic /*, String language*/) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDSOLICITUD + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_MOTIVO + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAINICIO + "," +														
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAFIN + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_DESCRIPCION + "," +														
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDPERSONA + "," +
			    			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDINSTITUCION + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDCV + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDTIPOCV + "," +							
			    			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAALTA + "," +
			    			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDESTADOSOLIC + "," +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAMODIFICACION + "," +
	            			CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_USUMODIFICACION + "," +					
							UtilidadesMultidioma.getCampoMultidiomaSimple(CenTiposCVBean.T_NOMBRETABLA + "." + CenTiposCVBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS TIPOAPUNTE, " +	
							" (select " + UtilidadesMultidioma.getCampoMultidioma("p."+CenTiposCVSubtipo1Bean.C_DESCRIPCION, this.usrbean.getLanguage()) + " " +
						       "  from "+CenTiposCVSubtipo1Bean.T_NOMBRETABLA+" p"+
						       "  where p."+CenTiposCVSubtipo1Bean.C_IDTIPOCV+"="+CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenSolicitudModificacionCVBean.C_IDTIPOCV+
						       "    and p."+CenTiposCVSubtipo1Bean.C_IDINSTITUCION+"="+CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1+
						       "    and p."+CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1+"="+CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1+") DESCSUBTIPO1,"+
						           
						       "  (select " + UtilidadesMultidioma.getCampoMultidioma("p."+CenTiposCVSubtipo2Bean.C_DESCRIPCION, this.usrbean.getLanguage()) + " " +
						       "   from "+CenTiposCVSubtipo2Bean.T_NOMBRETABLA+" p"+
						       "   where p."+CenTiposCVSubtipo2Bean.C_IDTIPOCV+"="+CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenSolicitudModificacionCVBean.C_IDTIPOCV+
						       "     and p."+CenTiposCVSubtipo2Bean.C_IDINSTITUCION+"="+CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2+
						       "     and p."+CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2+"="+CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2+") DESCSUBTIPO2,"+
						       CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+","+
						       CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+","+
						       CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+","+
						       CenSolicitudModificacionCVBean.T_NOMBRETABLA+"."+CenDatosCVBean.C_IDINSTITUCION_SUBT2+
							" FROM " + CenSolicitudModificacionCVBean.T_NOMBRETABLA + 
									 " INNER JOIN " + CenTiposCVBean.T_NOMBRETABLA +
									 				" ON "+
													CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_IDTIPOCV + "=" +
													CenTiposCVBean.T_NOMBRETABLA +"."+ CenTiposCVBean.C_IDTIPOCV +														 
							" WHERE " +
							CenSolicitudModificacionCVBean.T_NOMBRETABLA +"."+ CenSolicitudModificacionCVBean.C_IDSOLICITUD + "=" + idSolic;
														
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
	public boolean procesarSolicitud(String solicitud, Integer usuario, String idioma) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
		Hashtable clave = new Hashtable();
		Hashtable cvOriginal = new Hashtable(); 
		CenDatosCVBean cvModificado = new CenDatosCVBean();		
		CenHistoricoBean beanHist = new CenHistoricoBean();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				// Obtengo la solicitud a modificar
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolicitudModificacionCVBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolicitudModificacionCVBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				// Actualizo el estado de la solicitud a realizada				
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
				else{
					// Obtengo el registro a modificar de la tabla cliente y preparo el bean correspondiente
					CenDatosCVAdm adminCV = new CenDatosCVAdm(this.usrbean);
					if (((String)hash.get(CenDatosCVBean.C_IDCV)).equalsIgnoreCase("")){
						cvModificado.setIdPersona(new Long((String)hash.get(CenSolicitudModificacionCVBean.C_IDPERSONA)));
						cvModificado.setIdInstitucion(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION)));
						cvModificado.setFechaInicio((String)hash.get(CenSolicitudModificacionCVBean.C_FECHAINICIO));
						cvModificado.setFechaFin((String)hash.get(CenSolicitudModificacionCVBean.C_FECHAFIN));
						if (((String)hash.get(CenSolicitudModificacionCVBean.C_DESCRIPCION)).equalsIgnoreCase("")){
							cvModificado.setDescripcion("-");
						}
						else{
							cvModificado.setDescripcion((String)hash.get(CenSolicitudModificacionCVBean.C_DESCRIPCION));
						}
						cvModificado.setCertificado(ClsConstants.DB_FALSE);
						cvModificado.setIdTipoCV(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCV)));
						if (hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1)).equals("")){
						cvModificado.setIdInstitucion_subt1(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1)));
						}else {
							cvModificado.setIdInstitucion_subt1(null);
						}
						if (hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2)).equals("")){
						cvModificado.setIdInstitucion_subt2(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2)));
						}else{
							cvModificado.setIdInstitucion_subt2(null);
						}
						if (hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1)).equals("")){
						cvModificado.setIdTipoCVSubtipo1((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1));
						}else{
							cvModificado.setIdTipoCVSubtipo1("");
						}
						if (hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2)).equals("")){
						cvModificado.setIdTipoCVSubtipo2((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2));
						}else{
							cvModificado.setIdTipoCVSubtipo2("");
						}
						// Fijamos los datos del Historico
						beanHist.setMotivo((String)hash.get(CenSolicitudModificacionCVBean.C_MOTIVO));			
						// Actualizo el registro Direcciones con historico				
						if (!adminCV.insertarConHistorico(cvModificado,beanHist, idioma)){
							correcto=false;
						}						
					}
					else{
						cvOriginal=adminCV.getEntradaCV((String)hash.get(CenDatosCVBean.C_IDPERSONA),(String)hash.get(CenDatosCVBean.C_IDINSTITUCION),(String)hash.get(CenDatosCVBean.C_IDCV));					
						cvModificado.setIdPersona(new Long((String)cvOriginal.get(CenDatosCVBean.C_IDPERSONA)));
						cvModificado.setIdInstitucion(new Integer((String)cvOriginal.get(CenDatosCVBean.C_IDINSTITUCION)));
						cvModificado.setIdCV(new Integer((String)cvOriginal.get(CenDatosCVBean.C_IDCV)));
						cvModificado.setFechaInicio((String)hash.get(CenSolicitudModificacionCVBean.C_FECHAINICIO));
						cvModificado.setFechaFin((String)hash.get(CenSolicitudModificacionCVBean.C_FECHAFIN));
						cvModificado.setDescripcion((String)hash.get(CenSolicitudModificacionCVBean.C_DESCRIPCION));					
						cvModificado.setCertificado((String)cvOriginal.get(CenDatosCVBean.C_CERTIFICADO));
						if (cvOriginal.get(CenDatosCVBean.C_CREDITOS)!=null && !((String)cvOriginal.get(CenDatosCVBean.C_CREDITOS)).equals(""))
							cvModificado.setCreditos(new Long((String)cvOriginal.get(CenDatosCVBean.C_CREDITOS)));
						else
							cvModificado.setCreditos(new Long(0));
						cvModificado.setIdTipoCV(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCV)));
						if (hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1)).equals("")){
						 cvModificado.setIdInstitucion_subt1(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT1)));
						}else{
							cvModificado.setIdInstitucion_subt1(null);
						}
						if (hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2)).equals("")){
						 cvModificado.setIdInstitucion_subt2(new Integer((String)hash.get(CenSolicitudModificacionCVBean.C_IDINSTITUCION_SUBT2)));
						}else{
						 cvModificado.setIdInstitucion_subt2(null);
						}
						if (hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1)).equals("")){
						 cvModificado.setIdTipoCVSubtipo1((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO1));
						}else{
							cvModificado.setIdTipoCVSubtipo1("");
						}
						if (hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2)!=null && !((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2)).equals("")){
						 cvModificado.setIdTipoCVSubtipo2((String)hash.get(CenSolicitudModificacionCVBean.C_IDTIPOCVSUBTIPO2));
						}else{
							cvModificado.setIdTipoCVSubtipo2("");
						}
						cvModificado.setFechaMovimiento((String)cvOriginal.get(CenDatosCVBean.C_FECHAMOVIMIENTO));					
						cvModificado.setOriginalHash(cvOriginal);					
						// Fijamos los datos del Historico
						beanHist.setMotivo((String)hash.get(CenSolicitudModificacionCVBean.C_MOTIVO));			
						// Actualizo el registro Direcciones con historico				
						if (!adminCV.updateConHistorico(cvModificado,beanHist, idioma)){
							correcto=false;
						}
					}
				}				
			}	
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al procesar solicitudes de modificaciones de curriculms");
       }
       return correcto;                        
    }	
	
}
