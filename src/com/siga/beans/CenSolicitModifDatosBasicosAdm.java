/*
 * VERSIONES:
 * 
 * miguel.villegas - 04-01-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CENSOLICITMODIFDATOSBASICOS de la BBDD
* 
*/
public class CenSolicitModifDatosBasicosAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenSolicitModifDatosBasicosAdm(UsrBean usu) {
		super(CenSolicitModifDatosBasicosBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenSolicitModifDatosBasicosBean.C_IDSOLICITUD,
							CenSolicitModifDatosBasicosBean.C_MOTIVO,
							CenSolicitModifDatosBasicosBean.C_PUBLICIDAD,
							CenSolicitModifDatosBasicosBean.C_GUIAJUDICIAL,
							CenSolicitModifDatosBasicosBean.C_ABONOS,
							CenSolicitModifDatosBasicosBean.C_CARGOS,							
							CenSolicitModifDatosBasicosBean.C_IDINSTITUCION,
							CenSolicitModifDatosBasicosBean.C_IDPERSONA,							
							CenSolicitModifDatosBasicosBean.C_IDLENGUAJE,
							CenSolicitModifDatosBasicosBean.C_FECHAALTA,							
							CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC,							
							CenSolicitModifDatosBasicosBean.C_FECHAMODIFICACION,
							CenSolicitModifDatosBasicosBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenSolicitModifDatosBasicosBean.C_IDSOLICITUD};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenSolicitModifDatosBasicosBean bean = null;
		
		try {
			bean = new CenSolicitModifDatosBasicosBean();
			bean.setIdSolicitud (UtilidadesHash.getLong(hash,CenSolicitModifDatosBasicosBean.C_IDSOLICITUD));
			bean.setMotivo(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_MOTIVO));
			bean.setPublicidad(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_PUBLICIDAD));			
			bean.setGuiaJudicial(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_GUIAJUDICIAL));
			bean.setAbonos(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_ABONOS));
			bean.setCargos(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_CARGOS));						
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,CenSolicitModifDatosBasicosBean.C_IDINSTITUCION));
			bean.setIdPersona (UtilidadesHash.getLong(hash,CenSolicitModifDatosBasicosBean.C_IDPERSONA));			
			bean.setIdLenguaje (UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_IDLENGUAJE));
			bean.setFechaAlta(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_FECHAALTA));
			bean.setIdEstadoSolic (UtilidadesHash.getInteger(hash,CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolicitModifDatosBasicosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolicitModifDatosBasicosBean.C_USUMODIFICACION));			
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
			CenSolicitModifDatosBasicosBean b = (CenSolicitModifDatosBasicosBean) bean;
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_MOTIVO,b.getMotivo());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_PUBLICIDAD,b.getPublicidad());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_GUIAJUDICIAL,b.getGuiaJudicial());			
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_ABONOS,b.getAbonos());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_CARGOS,b.getCargos());						
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_IDINSTITUCION,b.getIdInstitucion ());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_IDPERSONA,b.getIdPersona ());			
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_IDLENGUAJE,b.getIdLenguaje());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC,b.getIdEstadoSolic());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_FECHAALTA,b.getFechaAlta());
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_FECHAMODIFICACION,b.getFechaMod());			
			UtilidadesHash.set(htData,CenSolicitModifDatosBasicosBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
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
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDSOLICITUD + "," +
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_MOTIVO + "," +							
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_PUBLICIDAD + "," +
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_GUIAJUDICIAL + "," +
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_ABONOS + "," +
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_CARGOS + "," +							
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDPERSONA + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDINSTITUCION + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDLENGUAJE + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_FECHAALTA + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "," +
	            			//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
	            			ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES+" as TIPOMODIF, "+
							" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage()) +
							" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
							" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES+") as TEXTOTIPOMODIF"+
							" FROM " + 
							//CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +", "+CenTiposModificacionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+ 
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+
							" WHERE " +
							//CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES) +
							//" AND " +							
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
							" AND " +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDINSTITUCION + "=" + institucion;	            
											
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				
				
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND (" +
					  CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')" +									 
					  " OR " +
					  GstDate.dateBetween0and24h(CenSolicitModifDatosBasicosBean.C_FECHAALTA,fechaF)+")";									 
				}							
							
				sql += " ORDER BY " + CenSolicitModifDatosBasicosBean.C_FECHAALTA + " DESC"; 										
							
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
	       	throw new ClsExceptions (e, "Error al obtener solicitudes de datos basicos");
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
	public Vector getSolicitudesModifEspecifTotales(String institucion, String estado, String fechaI, String fechaF) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="(SELECT " +
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDSOLICITUD + "," +
    						CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_MOTIVO + "," +							
    												
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDPERSONA + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDINSTITUCION + ", 0 as CODIGO, " +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_FECHAALTA + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
	            			ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES+" as TIPOMODIF, "+
							" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage())+
							" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
							" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES+") as TEXTOTIPOMODIF"+
							
							" FROM " + 
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+
							" WHERE " +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
							" AND " +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDINSTITUCION + "=" + institucion;	            
											
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				
				
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND (" +
					  CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')" +									 
					  " OR " +
					  GstDate.dateBetween0and24h(CenSolicitModifDatosBasicosBean.C_FECHAALTA,fechaF)+")";									 
				}							
				sql+=" ) union";
				sql+= " (SELECT " +
					CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDSOLICITUD + "," +
					CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_MOTIVO + "," +	
					CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDPERSONA + "," +
     			    CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDINSTITUCION + "," +
					CenSoliModiDireccionesBean.T_NOMBRETABLA + "." + CenSoliModiDireccionesBean.C_IDDIRECCION + " as CODIGO," +							
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
		sql+=" ) union ";
		sql+= " (SELECT " +
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDSOLICITUD + "," +
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_MOTIVO + "," +
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDPERSONA + "," +
	
		
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDINSTITUCION + "," +							
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDCUENTA + " as CODIGO, " +							
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_FECHAALTA + "," +
		CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDESTADOSOLIC + "," +
		//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
		UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
		ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS+" as TIPOMODIF, "+
		" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION,this.usrbean.getLanguage())+
		" from "+CenTiposModificacionesBean.T_NOMBRETABLA+
		" where "+CenTiposModificacionesBean.C_IDTIPOMODIFICACION+"="+ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS+") as TEXTOTIPOMODIF"+
		" FROM " + 
		//CenSolicModiCuentasBean.T_NOMBRETABLA +", "+CenTiposModificacionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+ 
		CenSolicModiCuentasBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+
		" WHERE " +
		//CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS) +
		//" AND " +							
		CenSolicModiCuentasBean.T_NOMBRETABLA +"."+ CenSolicModiCuentasBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
		" AND " +
		CenSolicModiCuentasBean.T_NOMBRETABLA +"."+ CenSolicModiCuentasBean.C_IDINSTITUCION + "=" + institucion;	            
						
		if (!estado.trim().equals("")){								 
		sql +=" AND " +
			  CenSolicModiCuentasBean.T_NOMBRETABLA +"."+ CenSolicModiCuentasBean.C_IDESTADOSOLIC + "=" + estado;									 
		}				
		
		if (!fechaI.trim().equals("")){								 
		sql +=" AND " +
			  CenSolicModiCuentasBean.T_NOMBRETABLA +"."+ CenSolicModiCuentasBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
		}							
		
		if (!fechaF.trim().equals("")){								 
		sql +=" AND (" +
		  CenSolicModiCuentasBean.T_NOMBRETABLA +"."+ CenSolicModiCuentasBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')" +									 
		  " OR " +
		  GstDate.dateBetween0and24h(CenSolicModiCuentasBean.C_FECHAALTA,fechaF)+")";									 
		}							
		
		sql+=" ) union ";
		sql+=" (SELECT " +
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDSOLICITUD + "," +
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_MOTIVO + "," +
															
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDPERSONA + "," +
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDINSTITUCION + "," +
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDCV + " as CODIGO, " +
					
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_FECHAALTA + "," +
		CenSolicitudModificacionCVBean.T_NOMBRETABLA + "." + CenSolicitudModificacionCVBean.C_IDESTADOSOLIC + "," +
		//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
		UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +
		ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV+" as TIPOMODIF, "+
		" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION,this.usrbean.getLanguage())+
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
	   sql+=" ) union ";
	   
	   sql+=" (SELECT " +
					CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDSOLICITUD + "," +
					CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_MOTIVO + "," +
					CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDPERSONA + "," +
					CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDINSTITUCION + "," +
	    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDCUENTA + " as CODIGO, " +
	    			
	    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_FECHAALTA + "," +
	    			CenSolModiFacturacionServicioBean.T_NOMBRETABLA + "." + CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC + "," +							
       			//CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION + " AS MODIFICACION," +							
       			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO, " +

       			ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION+" as TIPOMODIF, "+
       			" (SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION,this.usrbean.getLanguage())+
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
		sql+=" ) union ";
		sql+=" (SELECT " + 
		ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDSOLICITUD + "," +
		ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_MOTIVO + "," +
		ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDPERSONA + "," +
		ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDINSTITUCION + ", 0 as CODIGO, " +
		ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_FECHAALTA + "," +
		ExpSolicitudBorradoBean.T_NOMBRETABLA + "." + ExpSolicitudBorradoBean.C_IDESTADOSOLIC + "," +
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
		sql+=" )";	
		sql += " ORDER BY 6 DESC"; 										
							
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
	       	throw new ClsExceptions (e, "Error al obtener solicitudes de datos basicos");
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
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDSOLICITUD + "," +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_MOTIVO + "," +							
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_PUBLICIDAD + "," +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_GUIAJUDICIAL + "," +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_ABONOS + "," +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_CARGOS + "," +							
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDPERSONA + "," +
			    			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDINSTITUCION + "," +
			    			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDLENGUAJE + "," +
			    			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_FECHAALTA + "," +
			    			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC + "," +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_FECHAMODIFICACION + "," +
	            			CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + "." + CenSolicitModifDatosBasicosBean.C_USUMODIFICACION +
							" FROM " + CenSolicitModifDatosBasicosBean.T_NOMBRETABLA + 
							" WHERE " +
							CenSolicitModifDatosBasicosBean.T_NOMBRETABLA +"."+ CenSolicitModifDatosBasicosBean.C_IDSOLICITUD + "=" + idSolic;
														
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
	
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
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
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
					
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
			}	
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       		throw new ClsExceptions (e, "Error al denegar la solicitud de datos basicos");
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
		Vector busqueda = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
		Hashtable clave = new Hashtable();
		Hashtable clienteOriginal = new Hashtable(); 
		CenClienteBean clienteModificado = new CenClienteBean();		
		CenHistoricoBean beanHist = new CenHistoricoBean();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				// Obtengo la solicitud a modificar
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10")) {
					return true;
				} else {
					// Actualizo el estado de la solicitud a realizada				
					if (!this.update(hash,hashOriginal)){
						correcto=false;
					}	
					else{
						// Obtengo el registro a modificar de la tabla cliente y preparo el bean correspondiente
						CenClienteAdm adminCliente = new CenClienteAdm(this.usrbean);
						clienteOriginal=adminCliente.getEntradaCliente((String)hash.get(CenClienteBean.C_IDPERSONA),(String)hash.get(CenClienteBean.C_IDINSTITUCION));
						clienteModificado.setIdPersona(new Long((String)clienteOriginal.get(CenClienteBean.C_IDPERSONA)));
						clienteModificado.setIdInstitucion(new Integer((String)clienteOriginal.get(CenClienteBean.C_IDINSTITUCION)));
						clienteModificado.setFechaAlta((String)clienteOriginal.get(CenClienteBean.C_FECHAALTA));					
						clienteModificado.setCaracter((String)clienteOriginal.get(CenClienteBean.C_CARACTER));
						clienteModificado.setFotografia((String)clienteOriginal.get(CenClienteBean.C_FOTOGRAFIA));
/* *** El campo sexo se obtiene de la tabla cen_persona						
						clienteModificado.setSexo((String)clienteOriginal.get(CenClienteBean.C_SEXO));
/***************/					
						clienteModificado.setPublicidad((String)hash.get(CenSolicitModifDatosBasicosBean.C_PUBLICIDAD));
						clienteModificado.setGuiaJudicial((String)hash.get(CenSolicitModifDatosBasicosBean.C_GUIAJUDICIAL));
						clienteModificado.setAbonosBanco((String)hash.get(CenSolicitModifDatosBasicosBean.C_ABONOS));
						clienteModificado.setCargosBanco((String)hash.get(CenSolicitModifDatosBasicosBean.C_CARGOS));
						clienteModificado.setAsientoContable((String)clienteOriginal.get(CenClienteBean.C_ASIENTOCONTABLE));
						clienteModificado.setComisiones((String)clienteOriginal.get(CenClienteBean.C_COMISIONES));
						clienteModificado.setIdTratamiento(new Integer((String)clienteOriginal.get(CenClienteBean.C_IDTRATAMIENTO)));
						clienteModificado.setIdLenguaje((String)hash.get(CenSolicitModifDatosBasicosBean.C_IDLENGUAJE));										
						clienteModificado.setOriginalHash(clienteOriginal);					
						// Fijamos los datos del Historico
						beanHist.setMotivo((String)hash.get(CenSolicitModifDatosBasicosBean.C_MOTIVO));			
						// Actualizo el registro cliente con historico				
						if (!adminCliente.updateConHistorico(clienteModificado,beanHist, idioma)){
							correcto=false;
						}
						
						else
						{
							// Lanzamos el proceso de revision de suscripciones del letrado
							String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado((String)hash.get(CenClienteBean.C_IDINSTITUCION),
																									  (String)hash.get(CenClienteBean.C_IDPERSONA),
																									  "",
																									  ""+usuario);
							if ((resultado == null) || (!resultado[0].equals("0")))
								throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
						}
					}				
				}						
				}				
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al procesar solicitudes de datos basicos");
       }       
       return correcto;                        
    }	
}
