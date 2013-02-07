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
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CENSOLICITUDESMODIFICACION de la BBDD
* 
*/
public class CenSolicitudesModificacionAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenSolicitudesModificacionAdm(UsrBean usu) {
		super(CenSolicitudesModificacionBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenSolicitudesModificacionBean.C_IDSOLICITUD,
							CenSolicitudesModificacionBean.C_DESCRIPCION,
							CenSolicitudesModificacionBean.C_IDINSTITUCION,
							CenSolicitudesModificacionBean.C_IDPERSONA,							
							CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION,
							CenSolicitudesModificacionBean.C_FECHAALTA,							
							CenSolicitudesModificacionBean.C_IDESTADOSOLIC,							
							CenSolicitudesModificacionBean.C_FECHAMODIFICACION,
							CenSolicitudesModificacionBean.C_USUMODIFICACION,
							CenSolicitudesModificacionBean.C_IDINSTITUCIONORIGEN,
							CenSolicitudesModificacionBean.C_USUMODIFICACIONORIGEN};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenSolicitudesModificacionBean.C_IDSOLICITUD};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenSolicitudesModificacionBean bean = null;
		
		try {
			bean = new CenSolicitudesModificacionBean();
			bean.setIdSolicitud (UtilidadesHash.getLong(hash,CenSolicitudesModificacionBean.C_IDSOLICITUD));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenSolicitudesModificacionBean.C_DESCRIPCION));
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,CenSolicitudesModificacionBean.C_IDINSTITUCION));
			bean.setIdPersona (UtilidadesHash.getLong(hash,CenSolicitudesModificacionBean.C_IDPERSONA));			
			bean.setIdTipoModificacion (UtilidadesHash.getInteger(hash,CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION));
			bean.setFechaAlta(UtilidadesHash.getString(hash,CenSolicitudesModificacionBean.C_FECHAALTA));
			bean.setIdEstadoSolic (UtilidadesHash.getInteger(hash,CenSolicitudesModificacionBean.C_IDESTADOSOLIC));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolicitudesModificacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolicitudesModificacionBean.C_USUMODIFICACION));
			bean.setIdInstitucionOrigen(UtilidadesHash.getInteger(hash,CenSolicitudesModificacionBean.C_IDINSTITUCIONORIGEN));
			bean.setUsuModificacionOrigen(UtilidadesHash.getInteger(hash,CenSolicitudesModificacionBean.C_USUMODIFICACIONORIGEN));
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
			CenSolicitudesModificacionBean b = (CenSolicitudesModificacionBean) bean;
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_IDINSTITUCION,b.getIdInstitucion ());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_IDPERSONA,b.getIdPersona ());			
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION,b.getIdTipoModificacion());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_IDESTADOSOLIC,b.getIdEstadoSolic());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_FECHAALTA,b.getFechaAlta());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_FECHAMODIFICACION,b.getFechaMod());			
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_IDINSTITUCIONORIGEN,b.getIdInstitucionOrigen());
			UtilidadesHash.set(htData,CenSolicitudesModificacionBean.C_USUMODIFICACIONORIGEN,b.getUsuModificacionOrigen());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Prepara una tabla hash para insertarla en la tabla. <br/>
	 * Obtiene el campo IDSOLICITUD, <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions, SIGAException 
	{
		String values;
		String sql;
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql ="SELECT SEQ_SOLICITUDESMODIFICACION.NEXTVAL FROM DUAL";
		
		try {		
			if (rc.query(sql)) {						
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				entrada.put(CenSolicitudesModificacionBean.C_IDSOLICITUD,(String)prueba.get("NEXTVAL"));
				entrada.put(CenSolicitudesModificacionBean.C_FECHAALTA,"sysdate");
				entrada.put(CenSolicitudesModificacionBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));				
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en BBDD");		
		}
		
		return entrada;
	}
			
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idSolic - identificador de la persona 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerEntradaSolicitudesModificacion (String idSolic) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDSOLICITUD  + "," +							
    						CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_DESCRIPCION  + "," +
							CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDINSTITUCION + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDPERSONA  + "," +	            			
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDESTADOSOLIC + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_FECHAALTA + "," +
							CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_FECHAMODIFICACION + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_USUMODIFICACION +
							" FROM " + CenSolicitudesModificacionBean.T_NOMBRETABLA + 
							" WHERE " +
							CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDSOLICITUD + "=" + idSolic;
														
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
	 * Recoge informacion de la tabla a partir de la informacion suministrada por la busqueda<br/>
	 * @param  institucion - identificador de la institucion
	 * @param  tipoMod - tipo de modificacion
	 * @param  estado - estado de la solicitud	 
	 * @param  fechaI - inicio de rango de la fecha de alta
	 * @param  fechaF - fin de rango de la fecha de alta
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getSolicitudes(String institucion, String tipoMod, String estado, String fechaI, String fechaF) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDSOLICITUD + "," +
    						CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_DESCRIPCION + "," +							
							CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDPERSONA + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDINSTITUCION + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_FECHAALTA + "," +
	            			CenSolicitudesModificacionBean.T_NOMBRETABLA + "." + CenSolicitudesModificacionBean.C_IDESTADOSOLIC + "," +
	            			CenClienteBean.T_NOMBRETABLA + "." + CenClienteBean.C_LETRADO + " AS LETRADO ," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenTiposModificacionesBean.T_NOMBRETABLA + "." + CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS MODIFICACION," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO " +
							" FROM " + 
							CenSolicitudesModificacionBean.T_NOMBRETABLA +", "+ CenTiposModificacionesBean.T_NOMBRETABLA +", "+CenEstadoSolicitudModifBean.T_NOMBRETABLA+", "+CenClienteBean.T_NOMBRETABLA+ 
							" WHERE " + 
							CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + CenTiposModificacionesBean.T_NOMBRETABLA +"."+ CenTiposModificacionesBean.C_IDTIPOMODIFICACION +
							" AND " +
							CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDESTADOSOLIC + "=" + CenEstadoSolicitudModifBean.T_NOMBRETABLA +"."+ CenEstadoSolicitudModifBean.C_IDESTADOSOLIC +
							" AND " +
							CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDPERSONA + "=" + CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA +
							" AND " +
							CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDINSTITUCION + "=" + CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDINSTITUCION +
							" AND " +
							CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDINSTITUCION + "=" + institucion;	            
							
				if (!tipoMod.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION + "=" + tipoMod;									 
				}
				
				if (!estado.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_IDESTADOSOLIC + "=" + estado;									 
				}				

				String auxFechaInicio = "";
				String auxFechaFin = "";
				if (!fechaI.trim().equals("")) 
	 				auxFechaInicio = GstDate.getApplicationFormatDate("",fechaI);
				if (!fechaF.trim().equals("")) 
	 				auxFechaFin = GstDate.getApplicationFormatDate("",fechaF);
				
				if ((fechaI!=null && !fechaI.trim().equals("")) || (fechaF!=null && !fechaF.trim().equals(""))) {
					sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_FECHAALTA,auxFechaInicio,auxFechaFin);
				} 
				
				/* RGG cambio funcion fecha desde hasta 
				if (!fechaI.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_FECHAALTA + ">= TO_DATE ('" + fechaI + "', 'DD/MM/YYYY')";
				}							

				if (!fechaF.trim().equals("")){								 
					sql +=" AND " +
						  CenSolicitudesModificacionBean.T_NOMBRETABLA +"."+ CenSolicitudesModificacionBean.C_FECHAALTA + "<= TO_DATE ('" + fechaF + "', 'DD/MM/YYYY')";									 
				}							
				*/
				
				sql += " ORDER BY " + CenSolicitudesModificacionBean.C_FECHAALTA + " DESC"; 										
							
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
	       	throw new ClsExceptions (e, "Error al obtener productos del historial");
	       }
	       return datos;                        
	    }	
	
	/** 
	 * Procesa la solicitud de modificacion pasada como parametro (su estado pasa a realizado) <br/> 
	 * @param  solicitud - solicitud a procesar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean procesadoSolicitud(String solicitud) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();				
	
		try {
       	
       		CenSolicitudesModificacionAdm admin=new CenSolicitudesModificacionAdm(this.usrbean);
       	
			if (!solicitud.equalsIgnoreCase("")){
				original=admin.obtenerEntradaSolicitudesModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				//hash = row.getRow();
				hash.put(CenSolicitudesModificacionBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));						
				if (!admin.update(hash,hashOriginal)){
					correcto = false;
				}	
			}	
		}
		catch (SIGAException e) {
			throw e;
		}

		catch (Exception e) {
			throw new ClsExceptions (e, "Error al procesar solicitudes genericas");
		}       
		return correcto;                        
    }
	
	/** 
	 * Procesa la solicitud de modificacion pasada como parametro (su estado pasa a realizado) <br/> 
	 * @param  solicitud - solicitud a procesar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean denegacionSolicitud(String solicitud) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();				
	
		try {
       	
       		CenSolicitudesModificacionAdm admin=new CenSolicitudesModificacionAdm(this.usrbean);
       	
			if (!solicitud.equalsIgnoreCase("")){
				original=admin.obtenerEntradaSolicitudesModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				//hash = row.getRow();
				hash.put(CenSolicitudesModificacionBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));						
				if (!admin.update(hash,hashOriginal)){
					correcto = false;
				}	
			}	
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al denegar solicitudes genericas");
		}       
		return correcto;                        
    }
	
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	
}
