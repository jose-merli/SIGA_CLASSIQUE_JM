/*
 * Created on Jan 14, 2005
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
public class CenSolicModiCuentasAdm extends MasterBeanAdministrador {

	/**	
	 * @param usuario
	 */
	public CenSolicModiCuentasAdm(UsrBean usuario) {
		super( CenSolicModiCuentasBean.T_NOMBRETABLA, usuario);
	}
	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_SOLICMODICUENTAS 
	 * @author nuria.rgonzalez 14-01-05	 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenSolicModiCuentasBean.C_IDSOLICITUD,
				CenSolicModiCuentasBean.C_MOTIVO,				CenSolicModiCuentasBean.C_ABONOCARGO,	
				CenSolicModiCuentasBean.C_ABONOSJCS,			CenSolicModiCuentasBean.C_CBO_CODIGO,	
				CenSolicModiCuentasBean.C_CODIGOSUCURSAL,		CenSolicModiCuentasBean.C_DIGITOCONTROL,		
				CenSolicModiCuentasBean.C_NUMEROCUENTA,			CenSolicModiCuentasBean.C_TITULAR,	
				CenSolicModiCuentasBean.C_IDPERSONA,			CenSolicModiCuentasBean.C_IDINSTITUCION,			
				CenSolicModiCuentasBean.C_IDCUENTA,				CenSolicModiCuentasBean.C_FECHAMODIFICACION,
				CenSolicModiCuentasBean.C_USUMODIFICACION,		CenSolicModiCuentasBean.C_IDESTADOSOLIC,
				CenSolicModiCuentasBean.C_FECHAALTA,			CenSolicModiCuentasBean.C_IBAN};
		return campos;
	}
	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_SOLICMODICUENTAS 
	 * @author nuria.rgonzalez 14-01-05	  
	 */
	protected String[] getClavesBean() {
		String[] campos = {	CenSolicModiCuentasBean.C_IDSOLICITUD};
		return campos;
	}


	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/**
	 * Devuelve un CenSolicModiCuentasBean con los campos de la tabla CEN_SOLICMODICUENTAS 
	 * @author nuria.rgonzalez 14-01-05	 
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenSolicModiCuentasBean bean = null;
		try{
			bean = new CenSolicModiCuentasBean();
			bean.setIdSolicitud(UtilidadesHash.getLong(hash,CenSolicModiCuentasBean.C_IDSOLICITUD));
			bean.setMotivo(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_MOTIVO));
			bean.setAbonoCargo(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_ABONOCARGO));
			bean.setAbonoSJCS(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_ABONOSJCS));
			bean.setCbo_Codigo(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_CBO_CODIGO));
			bean.setCodigoSucursal(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_CODIGOSUCURSAL));
			bean.setDigitoControl(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_DIGITOCONTROL));
			bean.setNumeroCuenta(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_NUMEROCUENTA));
			bean.setTitular(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_TITULAR));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenSolicModiCuentasBean.C_IDPERSONA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenSolicModiCuentasBean.C_IDINSTITUCION));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,CenSolicModiCuentasBean.C_IDCUENTA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolicModiCuentasBean.C_USUMODIFICACION));
			bean.setIdEstadoSolic(UtilidadesHash.getInteger(hash,CenSolicModiCuentasBean.C_IDESTADOSOLIC));
			bean.setFechaAlta(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_FECHAALTA));
			bean.setIban(UtilidadesHash.getString(hash,CenSolicModiCuentasBean.C_IBAN));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_SOLICMODICUENTAS 
	 * @author nuria.rgonzalez 14-01-05	 
	 * @param CenSolicModiCuentasBean 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenSolicModiCuentasBean b = (CenSolicModiCuentasBean) bean;
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_MOTIVO, b.getMotivo());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_ABONOCARGO, b.getAbonoCargo());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_ABONOSJCS, b.getAbonoSJCS());			
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_CBO_CODIGO, b.getCbo_Codigo());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_CODIGOSUCURSAL, b.getCodigoSucursal());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_DIGITOCONTROL, b.getDigitoControl());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_NUMEROCUENTA, b.getNumeroCuenta());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_TITULAR, b.getTitular());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_IDCUENTA, b.getIdCuenta());				
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_IDESTADOSOLIC, b.getIdEstadoSolic());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_FECHAALTA, b.getFechaAlta());
			UtilidadesHash.set(hash, CenSolicModiCuentasBean.C_IBAN, b.getIban());
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
		Long id=null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql ="SELECT SEQ_SOLICMODICUENTAS.NEXTVAL FROM DUAL";
		
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
	            StringBuilder sql = new StringBuilder();
	            sql.append("SELECT ");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDSOLICITUD);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_MOTIVO);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_ABONOCARGO);
	            sql.append(",");	            
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_ABONOSJCS);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_CBO_CODIGO);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_CODIGOSUCURSAL);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_DIGITOCONTROL);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_NUMEROCUENTA);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_TITULAR);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDPERSONA);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDINSTITUCION);
	            sql.append(",");							
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDCUENTA);
	            sql.append(" AS CODIGO,");							
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_FECHAALTA);
	            sql.append(",");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDESTADOSOLIC);
	            sql.append(",");						
	            sql.append(UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoSolicitudModifBean.T_NOMBRETABLA + "." + CenEstadoSolicitudModifBean.C_DESCRIPCION, this.usrbean.getLanguage()));
	            sql.append(" AS ESTADO, ");
	            sql.append(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS);
	            sql.append(" as TIPOMODIF, ");
	            sql.append(" (SELECT ");
	            sql.append(UtilidadesMultidioma.getCampoMultidioma(CenTiposModificacionesBean.C_DESCRIPCION, this.usrbean.getLanguage()));
	            sql.append(" from ");
	            sql.append(CenTiposModificacionesBean.T_NOMBRETABLA);
	            sql.append(" where ");
	            sql.append(CenTiposModificacionesBean.C_IDTIPOMODIFICACION);
	            sql.append(" = ");
	            sql.append(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS);
	            sql.append(") as TEXTOTIPOMODIF,");
							
	            sql.append(" (SELECT ");
	            sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenCuentasBancariasBean.C_ABONOCARGO);
	            sql.append(" FROM ");
	            sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
	            sql.append(" WHERE ");
	            sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenCuentasBancariasBean.C_IDINSTITUCION);
	            sql.append(" = ");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDINSTITUCION);
	            sql.append(" AND ");
	            sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenCuentasBancariasBean.C_IDPERSONA);
	            sql.append(" = ");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDPERSONA);
	            sql.append(" AND ");
	            sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenCuentasBancariasBean.C_IDCUENTA);
	            sql.append(" = ");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDCUENTA);
	            sql.append(") AS ABONOCARGO_CUENTABANCARIA ");
							
				sql.append(" FROM ");  
				sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(", ");
	            sql.append(CenEstadoSolicitudModifBean.T_NOMBRETABLA);
	            sql.append(" WHERE ");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDESTADOSOLIC);
	            sql.append(" = ");
	            sql.append(CenEstadoSolicitudModifBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenEstadoSolicitudModifBean.C_IDESTADOSOLIC);
	            sql.append(" AND ");
	            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
	            sql.append(".");
	            sql.append(CenSolicModiCuentasBean.C_IDINSTITUCION);
	            sql.append(" = ");
	            sql.append(institucion);	            
											
				if (!estado.trim().equals("")){								 
		            sql.append(" AND ");
		            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
		            sql.append(".");
		            sql.append(CenSolicModiCuentasBean.C_IDESTADOSOLIC);
		            sql.append(" = ");
		            sql.append(estado);									 
				}				
				
				if (!fechaI.trim().equals("")){								 
		            sql.append(" AND ");
		            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
		            sql.append(".");
		            sql.append(CenSolicModiCuentasBean.C_FECHAALTA);
		            sql.append(">= TO_DATE ('");
		            sql.append(fechaI);
		            sql.append("', 'DD/MM/YYYY')");
				}							

				if (!fechaF.trim().equals("")){								 
		            sql.append(" AND (");
		            sql.append(CenSolicModiCuentasBean.T_NOMBRETABLA);
		            sql.append(".");
		            sql.append(CenSolicModiCuentasBean.C_FECHAALTA);
		            sql.append("<= TO_DATE ('");
		            sql.append(fechaF);
		            sql.append("', 'DD/MM/YYYY')");									 
		            sql.append(" OR ");
		            sql.append(GstDate.dateBetween0and24h(CenSolicModiCuentasBean.C_FECHAALTA,fechaF));
		            sql.append(")");									 
				}							
							
	            sql.append(" ORDER BY ");
	            sql.append(CenSolicModiCuentasBean.C_FECHAALTA);
	            sql.append(" DESC"); 										
							
	            if (rc.find(sql.toString())) {
	            	for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       } catch (Exception e) {
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
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDSOLICITUD + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_MOTIVO + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_ABONOCARGO + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_ABONOSJCS + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IBAN + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_CBO_CODIGO + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_CODIGOSUCURSAL + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_DIGITOCONTROL + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_NUMEROCUENTA + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_TITULAR + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDPERSONA + "," +
			    			CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDINSTITUCION + "," +							
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDCUENTA + "," +							
			    			CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_FECHAALTA + "," +
			    			CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_IDESTADOSOLIC + "," +
							CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_FECHAMODIFICACION + "," +
	            			CenSolicModiCuentasBean.T_NOMBRETABLA + "." + CenSolicModiCuentasBean.C_USUMODIFICACION +
							" FROM " + CenSolicModiCuentasBean.T_NOMBRETABLA + 
							" WHERE " +
							CenSolicModiCuentasBean.T_NOMBRETABLA +"."+ CenSolicModiCuentasBean.C_IDSOLICITUD + "=" + idSolic;
														
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
	 * @param  solicitudes - solicitud a denegar
	 * @return  boolean - reasultado de la operacion  
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
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolicModiCuentasBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolicModiCuentasBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
			}	
       }
		catch (SIGAException e) {
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al denegar la solicitud de datos sobre cuentas bancarias");
       }
       return correcto;                        
    }	

	/** 
	 * Procesa la solicitude de modificacion pasada como parametro (su estado pasa a realizado <br/>
	 * y se actualiza el registro adecuado con la informacion suministrada, adem�s de la entrada <br/>
	 * pertinente en el historico) 
	 * @param  solicitud - solicitud a procesar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public boolean procesarSolicitud(String solicitud, Integer usuario, String idioma, boolean bProcesoAltaCuentaCargos) throws ClsExceptions, SIGAException {

		boolean correcto=true;
		Vector original = new Vector();		
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();		
		Hashtable cuentaOriginal = new Hashtable(); 
		CenCuentasBancariasBean cuentaModificada = new CenCuentasBancariasBean();		
		CenHistoricoBean beanHist = new CenHistoricoBean();		
	
       try {
			if (!solicitud.equalsIgnoreCase("")){
				// Obtengo la solicitud a modificar
				original=this.obtenerEntradaSolicitudModificacion(solicitud);
				Row row = (Row)original.firstElement();
				hashOriginal = row.getRow();
				hash=(Hashtable)hashOriginal.clone();
				hash.put(CenSolicModiCuentasBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));
				
				String idEstadoOriginal = (String)hashOriginal.get(CenSolicModiCuentasBean.C_IDESTADOSOLIC);
				if (idEstadoOriginal!=null && !idEstadoOriginal.equals("10"))
					return true;
				
				// Actualizo el estado de la solicitud a realizada				
				if (!this.update(hash,hashOriginal)){
					correcto=false;
				}	
				else{
					// Obtengo el registro a modificar de la tabla cliente y preparo el bean correspondiente
					CenCuentasBancariasAdm adminCuentas = new CenCuentasBancariasAdm(this.usrbean);					
					cuentaOriginal=adminCuentas.getEntradaCuenta((String)hash.get(CenCuentasBancariasBean.C_IDPERSONA),(String)hash.get(CenCuentasBancariasBean.C_IDINSTITUCION),(String)hash.get(CenCuentasBancariasBean.C_IDCUENTA));
					String abonoCargoOrig = (String) cuentaOriginal.get(CenCuentasBancariasBean.C_ABONOCARGO);
					cuentaModificada.setIdPersona(new Long((String)cuentaOriginal.get(CenCuentasBancariasBean.C_IDPERSONA)));
					cuentaModificada.setIdInstitucion(new Integer((String)cuentaOriginal.get(CenCuentasBancariasBean.C_IDINSTITUCION)));
					cuentaModificada.setIdCuenta(new Integer((String)cuentaOriginal.get(CenCuentasBancariasBean.C_IDCUENTA)));					
					cuentaModificada.setAbonoCargo((String)hash.get(CenSolicModiCuentasBean.C_ABONOCARGO));
					cuentaModificada.setAbonoSJCS((String)hash.get(CenSolicModiCuentasBean.C_ABONOSJCS));
					cuentaModificada.setCbo_Codigo((String)hash.get(CenSolicModiCuentasBean.C_CBO_CODIGO));
					cuentaModificada.setIban((String)hash.get(CenSolicModiCuentasBean.C_IBAN));
					cuentaModificada.setCodigoSucursal((String)hash.get(CenSolicModiCuentasBean.C_CODIGOSUCURSAL));
					cuentaModificada.setDigitoControl((String)hash.get(CenSolicModiCuentasBean.C_DIGITOCONTROL));
					cuentaModificada.setNumeroCuenta((String)hash.get(CenSolicModiCuentasBean.C_NUMEROCUENTA));
					cuentaModificada.setTitular((String)hash.get(CenSolicModiCuentasBean.C_TITULAR));
					cuentaModificada.setFechaBaja((String)cuentaOriginal.get(CenCuentasBancariasBean.C_FECHABAJA));
					cuentaModificada.setCuentaContable((String)cuentaOriginal.get(CenCuentasBancariasBean.C_CUENTACONTABLE));
					cuentaModificada.setOriginalHash(cuentaOriginal);
					// Fijamos los datos del Historico
					beanHist.setMotivo((String)hash.get(CenSolicModiCuentasBean.C_MOTIVO));		
					//BEGIN BNS 11/12/12 INCIDENCIA INC_08950_SIGA
					if (adminCuentas.updateConHistoricoYfecBaj(cuentaModificada, beanHist, usuario, this.usrbean, abonoCargoOrig, idioma, bProcesoAltaCuentaCargos) <0)
						correcto = false;
					
				}				
			}	
       }
		catch (SIGAException e) {
			correcto = false;
			throw e;
		}
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al procesar solicitudes de modificaciones de cuentas");
       }
       return correcto;                        
    }	
}
