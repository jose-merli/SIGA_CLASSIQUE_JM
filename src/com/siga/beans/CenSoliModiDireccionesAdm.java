/*
 * Created on Jan 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.GstDate;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.action.Direccion;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenSoliModiDireccionesAdm extends MasterBeanAdministrador {

	/**	
	 * @param usuario
	 */
	public CenSoliModiDireccionesAdm(UsrBean usuario) {
		super( CenSoliModiDireccionesBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_SOLIMODIDIRECCIONES 
	 * @author nuria.rgonzalez 04-01-05	 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenSoliModiDireccionesBean.C_IDSOLICITUD,
				CenSoliModiDireccionesBean.C_IDINSTITUCION,		CenSoliModiDireccionesBean.C_IDPERSONA,	
				CenSoliModiDireccionesBean.C_IDDIRECCION,		CenSoliModiDireccionesBean.C_MOTIVO,
				CenSoliModiDireccionesBean.C_DOMICILIO,			CenSoliModiDireccionesBean.C_CODIGOPOSTAL,	
				CenSoliModiDireccionesBean.C_TELEFONO1,			CenSoliModiDireccionesBean.C_TELEFONO2,	
				CenSoliModiDireccionesBean.C_MOVIL,				CenSoliModiDireccionesBean.C_FAX1,		
				CenSoliModiDireccionesBean.C_FAX2,				CenSoliModiDireccionesBean.C_CORREOELECTRONICO,	
				CenSoliModiDireccionesBean.C_PAGINAWEB,			CenSoliModiDireccionesBean.C_PREFERENTE,			
				CenSoliModiDireccionesBean.C_IDPAIS,			CenSoliModiDireccionesBean.C_IDPROVINCIA,
				CenSoliModiDireccionesBean.C_IDPOBLACION,		CenSoliModiDireccionesBean.C_FECHAMODIFICACION,CenSoliModiDireccionesBean.C_POBLACIONEXTRANJERA,
				CenSoliModiDireccionesBean.C_USUMODIFICACION,	CenSoliModiDireccionesBean.C_IDESTADOSOLIC,
				CenSoliModiDireccionesBean.C_FECHAALTA};
		return campos;
	}

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_SOLIMODIDIRECCIONES 
	 * @author nuria.rgonzalez 04-01-05	  
	 */
	protected String[] getClavesBean() {
		String[] campos = {	CenSoliModiDireccionesBean.C_IDSOLICITUD};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/**
	 * Devuelve un CenSoliModiDireccionesBean con los campos de la tabla CEN_SOLIMODIDIRECCIONES 
	 * @author nuria.rgonzalez 04-01-05	 
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenSoliModiDireccionesBean bean = null;
		try{
			bean = new CenSoliModiDireccionesBean();
			bean.setIdSolicitud(UtilidadesHash.getLong(hash,CenSoliModiDireccionesBean.C_IDSOLICITUD));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenSoliModiDireccionesBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenSoliModiDireccionesBean.C_IDPERSONA));
			bean.setIdDireccion(UtilidadesHash.getLong(hash,CenSoliModiDireccionesBean.C_IDDIRECCION));
			bean.setMotivo(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_MOTIVO));
			bean.setDomicilio(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_CODIGOPOSTAL));
			bean.setTelefono1(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_TELEFONO2));
			bean.setMovil(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_MOVIL));
			bean.setFax1(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_FAX2));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_CORREOELECTRONICO));
			bean.setPaginaweb(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_PAGINAWEB));			
			bean.setPreferente(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_PREFERENTE));
			bean.setIdPais(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_POBLACIONEXTRANJERA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSoliModiDireccionesBean.C_USUMODIFICACION));
			bean.setIdEstadoSolic(UtilidadesHash.getInteger(hash,CenSoliModiDireccionesBean.C_IDESTADOSOLIC));
			bean.setFechaAlta(UtilidadesHash.getString(hash,CenSoliModiDireccionesBean.C_FECHAALTA));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_SOLIMODIDIRECCIONES 
	 * @author nuria.rgonzalez 04-01-05	 
	 * @param CenSoliModiDireccionesBean 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenSoliModiDireccionesBean b = (CenSoliModiDireccionesBean) bean;
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDDIRECCION, b.getIdDireccion());			
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_MOTIVO, b.getMotivo());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_TELEFONO1, b.getTelefono1());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_TELEFONO2, b.getTelefono2());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_FAX2, b.getFax2());			
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_PAGINAWEB, b.getPaginaweb());			
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_PREFERENTE, b.getPreferente());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_IDESTADOSOLIC, b.getIdEstadoSolic());
			UtilidadesHash.set(hash, CenSoliModiDireccionesBean.C_FECHAALTA, b.getFechaAlta());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
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
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDSOLICITUD + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPERSONA + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDINSTITUCION + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDDIRECCION + " AS CODIGO," +							
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_MOTIVO + "," +							
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_CODIGOPOSTAL + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_DOMICILIO + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_POBLACIONEXTRANJERA + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_TELEFONO1 + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_TELEFONO2 + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_MOVIL + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FAX1 + "," +
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FAX2 + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_CORREOELECTRONICO + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_PAGINAWEB + "," +							
    						CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPAIS + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPROVINCIA + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPOBLACION + "," +							
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FECHAALTA + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDESTADOSOLIC + "," +
	            			//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
	            			ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES+" as TIPOMODIF, "+
			     			" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage())+
							" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
							" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES+") as TEXTOTIPOMODIF"+
							" FROM " + 
							//CenSoliModiDireccionesBean.T_NOMBRETABLA +", "+CenTiposModificacionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+ 
							CenSoliModiDireccionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+
							" WHERE " +
							//CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES) +
							//" AND " +							
							CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
							" AND " +
							CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDINSTITUCION + "=" + institucion;	            
											
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						  CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				
				
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						  CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND (" +
						  CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')" +									 
						  " OR " +
						  GstDate.dateBetween0and24h(CenSoliModiDireccionesBean.C_FECHAALTA,fechaF)+")";
				}							
							
				sql += " ORDER BY " + CenSoliModiDireccionesBean.C_FECHAALTA +  " DESC"; 										
							
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
	public Vector obtenerEntradaSolicitudModificacion (String idSolic) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDSOLICITUD + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPERSONA + "," +
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDINSTITUCION + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDDIRECCION + "," +							
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_MOTIVO + "," +							
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_POBLACIONEXTRANJERA + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_CODIGOPOSTAL + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_DOMICILIO + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_TELEFONO1 + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_TELEFONO2 + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_MOVIL + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FAX1 + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FAX2 + "," +
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_CORREOELECTRONICO + "," +
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_PAGINAWEB + "," +							
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_PREFERENTE + "," +							
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPAIS + "," +
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPROVINCIA + "," +
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPOBLACION + "," +							
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FECHAALTA + "," +
			    			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDESTADOSOLIC + "," +
							CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_FECHAMODIFICACION + "," +
	            			CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_USUMODIFICACION + "," +
	        				CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE +" AS POBLACION, " + 
							CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_NOMBRE +" AS PROVINCIA, " +
							UtilidadesMultidioma.getCampoMultidiomaSimple(CenPaisBean.T_NOMBRETABLA + "." + CenPaisBean.C_NOMBRE, this.usrbean.getLanguage()) + " AS PAIS " +							

							" FROM " + CenSoliModiDireccionesBean.T_NOMBRETABLA + 
									 " LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA +
									 				" ON "+
													CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDPOBLACION + "=" +
													CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION +
									 " LEFT JOIN " + CenProvinciaBean.T_NOMBRETABLA +
									 				" ON "+
													CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDPROVINCIA + "=" +
													CenProvinciaBean.T_NOMBRETABLA +"."+ CenProvinciaBean.C_IDPROVINCIA +
									 " LEFT JOIN " + CenPaisBean.T_NOMBRETABLA +
									 				" ON "+
													CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDPAIS + "=" +
													CenPaisBean.T_NOMBRETABLA +"."+ CenPaisBean.C_IDPAIS +														
							" WHERE " +
							CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDSOLICITUD + "=" + idSolic;	            

	            /* RGG 17-02-2005 cambio los LEFT JOIN por COMAS para 
	             *  el tratamiento de visibilidad de campos (NO APLICADO)
	             * 		

							" FROM " + CenSoliModiDireccionesBean.T_NOMBRETABLA + 
							" (+), " + CenPoblacionesBean.T_NOMBRETABLA +
							" (+), " + CenProvinciaBean.T_NOMBRETABLA +
							" (+), " + CenPaisBean.T_NOMBRETABLA +

							// PRIMERA JOIN
							" WHERE "+
							CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDPOBLACION + "=" +
							CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION +
			 				// SEGUNDA JOIN
			 				" AND "+
							CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDPROVINCIA + "=" +
							CenProvinciaBean.T_NOMBRETABLA +"."+ CenProvinciaBean.C_IDPROVINCIA +
			 				// TERCERA JOIN
			 				" AND "+
							CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDPAIS + "=" +
							CenPaisBean.T_NOMBRETABLA +"."+ CenPaisBean.C_IDPAIS +														

						// RESTO DE CONDICIONES
						" AND " +
						CenSoliModiDireccionesBean.T_NOMBRETABLA +"."+ CenSoliModiDireccionesBean.C_IDSOLICITUD + "=" + idSolic;	            
							
*/
	            
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
		
		sql ="SELECT SEQ_SOLIMODIDIRECCIONES.NEXTVAL FROM DUAL";
		
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
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSoliModiDireccionesBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSoliModiDireccionesBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
			}	
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al denegar la solicitud de datos sobre direcciones");
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
	public boolean procesarSolicitud(String solicitud, /*Integer usuario,*/ String idioma) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();
		Vector direcciones = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
		Hashtable clave = new Hashtable();
		Hashtable dirOriginal = new Hashtable(); 
		CenDireccionesBean dirModificada = new CenDireccionesBean();		
		CenHistoricoBean beanHist = new CenHistoricoBean();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				// Obtengo la solicitud a modificar
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSoliModiDireccionesBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSoliModiDireccionesBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				// Actualizo el estado de la solicitud a realizada				
				if (!this.update(hash,hashOriginal)){
					correcto=false;
					
				}else{
				
					// Obtengo el registro a modificar de la tabla cliente y preparo el bean correspondiente
					Direccion direccion = new Direccion();	
					CenDireccionesAdm adminDir = new CenDireccionesAdm(this.usrbean);					
					dirOriginal=adminDir.getEntradaDireccionGeneral((String)hash.get(CenDireccionesBean.C_IDPERSONA),(String)hash.get(CenDireccionesBean.C_IDINSTITUCION),(String)hash.get(CenDireccionesBean.C_IDDIRECCION));
					dirModificada.setIdPersona(new Long((String)dirOriginal.get(CenDireccionesBean.C_IDPERSONA)));
					dirModificada.setIdInstitucion(new Integer((String)dirOriginal.get(CenDireccionesBean.C_IDINSTITUCION)));
					dirModificada.setIdDireccion(new Long((String)dirOriginal.get(CenDireccionesBean.C_IDDIRECCION)));					
					dirModificada.setDomicilio((String)hash.get(CenSoliModiDireccionesBean.C_DOMICILIO));
					dirModificada.setCodigoPostal((String)hash.get(CenSoliModiDireccionesBean.C_CODIGOPOSTAL));
					dirModificada.setTelefono1((String)hash.get(CenSoliModiDireccionesBean.C_TELEFONO1));					
					dirModificada.setTelefono2((String)hash.get(CenSoliModiDireccionesBean.C_TELEFONO2));
					dirModificada.setMovil((String)hash.get(CenSoliModiDireccionesBean.C_MOVIL));
					dirModificada.setFax1((String)hash.get(CenSoliModiDireccionesBean.C_FAX1));
					dirModificada.setFax2((String)hash.get(CenSoliModiDireccionesBean.C_FAX2));
					dirModificada.setCorreoElectronico((String)hash.get(CenSoliModiDireccionesBean.C_CORREOELECTRONICO));
					dirModificada.setPaginaweb((String)hash.get(CenSoliModiDireccionesBean.C_PAGINAWEB));
					dirModificada.setFechaBaja((String)dirOriginal.get(CenDireccionesBean.C_FECHABAJA));
					dirModificada.setPreferente((String)hash.get(CenSoliModiDireccionesBean.C_PREFERENTE));
					dirModificada.setIdPais((String)hash.get(CenSoliModiDireccionesBean.C_IDPAIS));
					dirModificada.setIdProvincia((String)hash.get(CenSoliModiDireccionesBean.C_IDPROVINCIA));
					dirModificada.setIdPoblacion((String)hash.get(CenSoliModiDireccionesBean.C_IDPOBLACION));
					if (!dirModificada.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
						dirModificada.setIdProvincia("");
						dirModificada.setIdPoblacion("");
					}
					dirModificada.setPoblacionExtranjera((String)hash.get(CenSoliModiDireccionesBean.C_POBLACIONEXTRANJERA));
					dirModificada.setOriginalHash(dirOriginal);					
					// Fijamos los datos del Historico
					beanHist.setMotivo((String)hash.get(CenSoliModiDireccionesBean.C_MOTIVO));		
					
					// Se llama a la interfaz Direccion para actualizar una nueva direccion
					Direccion dirAux = direccion.actualizarDireccion(dirModificada, "", beanHist, null, this.usrbean);
									
					//Si existe algún fallo en la actualizacion se llama al metodo exito con el error correspondiente
					if(dirAux.isFallo()){
						correcto=false;
					}
					
					// Nos quedamos con una copia de la direccion original (pedido por Jaen) y la insertamos hacemos
					CenDireccionesBean beanDir = new CenDireccionesBean();
					beanDir=(CenDireccionesBean) adminDir.hashTableToBean(dirOriginal);
					String oldId = beanDir.getIdDireccion().toString();
					beanDir.setIdDireccion( adminDir.getNuevoID(beanDir));
					beanDir.setFechaBaja(GstDate.getHoyJava());
					
					//estableciendo los datos del tipo de direccion
					String tiposDir = "";
					CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm (this.usrbean);
					CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (this.usrbean);
					Vector vTipos = new Vector();
					vTipos=tipoDirAdm.getTiposDireccion(beanDir.getIdInstitucion().toString(),beanDir.getIdPersona().toString(), oldId);
					if ((vTipos != null) && (vTipos.size() > 0)){
						for (int i = 0; i <= vTipos.size()-1; i++) {
							CenDireccionTipoDireccionBean tipoDir = (CenDireccionTipoDireccionBean) tipoDirAdm.hashTableToBean((Hashtable)vTipos.get(i));
							tiposDir = tipoDir.getIdTipoDireccion()+",";
						}
					}

					// Se llama a la interfaz Direccion para actualizar una nueva direccion
					dirAux = new Direccion(); 
					dirAux = direccion.insertar(beanDir, tiposDir, beanHist, null, this.usrbean);
									
					//Si existe algún fallo en la inserción se llama al metodo exito con el error correspondiente
					if(dirAux.isFallo()){
						correcto=false;
					}					
				}
			}
		
       } catch (SIGAException e) {
    	   throw e;
       
       } catch (Exception e) {
    	   throw new ClsExceptions (e, "Error al procesar solicitudes de modificaciones de direcciones");
       }
       
       return correcto;                        
    }	
	
	public boolean solicitarModificacionDireccionesPreferentes (Long idPersona, String idInstitucion,  String idDireccion, String preferencia, HttpServletRequest request) throws SIGAException, ClsExceptions 
	{
		boolean salida = false;
		
		Hashtable hDireccion= new Hashtable();
		
		String[] idDir;
		String[] pref;
		String modificarPreferencia="";
		Vector vDir=new Vector();
		
		try {
			
			idDir=idDireccion.split("@");
			pref=preferencia.split("#");
			CenDireccionesAdm direccionesAdm =new CenDireccionesAdm(this.usrbean);
			CenSoliModiDireccionesAdm adm =new CenSoliModiDireccionesAdm(this.usrbean);
			CenSoliModiDireccionesBean solicDirBean=new CenSoliModiDireccionesBean();
			CenDireccionesBean dirBean=new CenDireccionesBean();
			dirBean.setIdPersona(idPersona);
			dirBean.setIdInstitucion(new Integer(idInstitucion));
			dirBean.setPreferente("");
			Hashtable hDirPreferencia=new Hashtable();
			
			
			
			
			
			
			for (int i=0; i<idDir.length; i++){
				
				UtilidadesHash.set(hDireccion,CenDireccionesBean.C_IDDIRECCION,idDir[i]);
				UtilidadesHash.set(hDireccion,CenDireccionesBean.C_IDPERSONA,idPersona);
				UtilidadesHash.set(hDireccion,CenDireccionesBean.C_IDINSTITUCION,idInstitucion);
				vDir=direccionesAdm.select(hDireccion);
				if ( (vDir != null) && (vDir.size() > 0) ){
					dirBean = (CenDireccionesBean) vDir.get(0);
					if (request.getSession().getAttribute("preferenciaDir")==null){
						hDirPreferencia.put(idDir[i],dirBean.getPreferente());
						request.getSession().setAttribute("preferenciaDir",hDirPreferencia);
					}
					Hashtable aux=(Hashtable)request.getSession().getAttribute("preferenciaDir");
					modificarPreferencia=(String)aux.get(idDir[i]);
					for(int j=0;j<pref.length; j++){
					  modificarPreferencia= 	UtilidadesString.replaceAllIgnoreCase(modificarPreferencia, pref[j], "");
					 
					}
					hDirPreferencia.put(idDir[i], modificarPreferencia);
					request.getSession().setAttribute("preferenciaDir",hDirPreferencia);
					dirBean.setPreferente(modificarPreferencia);
				}	
				
				
				solicDirBean.setIdSolicitud(adm.getNuevoId());
				solicDirBean.setIdInstitucion(dirBean.getIdInstitucion());
				solicDirBean.setIdPersona(dirBean.getIdPersona());
				solicDirBean.setIdDireccion(dirBean.getIdDireccion());
				solicDirBean.setMotivo("solicitud automatica de preferencia de direcciones");
				solicDirBean.setCodigoPostal(dirBean.getCodigoPostal());
				solicDirBean.setDomicilio(dirBean.getDomicilio());
				solicDirBean.setTelefono1(dirBean.getTelefono1());
				solicDirBean.setTelefono2(dirBean.getTelefono2());
				solicDirBean.setMovil(dirBean.getMovil());
				solicDirBean.setFax1(dirBean.getFax1());
				solicDirBean.setFax2(dirBean.getFax2());
				solicDirBean.setCorreoElectronico(dirBean.getCorreoElectronico());
				solicDirBean.setPaginaweb(dirBean.getPaginaweb());
				solicDirBean.setIdPais(dirBean.getIdPais());
				solicDirBean.setIdProvincia(dirBean.getIdProvincia());
				solicDirBean.setIdPoblacion(dirBean.getIdPoblacion());
				solicDirBean.setPoblacionExtranjera(dirBean.getPoblacionExtranjera());
				if (!solicDirBean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
					solicDirBean.setIdProvincia("");
					solicDirBean.setIdPoblacion("");
				}
				solicDirBean.setPreferente(dirBean.getPreferente());
				solicDirBean.setIdEstadoSolic(new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
				solicDirBean.setFechaAlta("sysdate");
				
				
				
				
				
				if(!adm.insert(solicDirBean)){
					throw new SIGAException (adm.getError());
				}
			}
			
		}
		catch(SIGAException e){
			throw e;
		}catch(Exception e){
			
			throw new ClsExceptions (e, "Error al actualizar la solicitud de preferencias de direcciones.");
		}
		return salida;
		
	}
}
