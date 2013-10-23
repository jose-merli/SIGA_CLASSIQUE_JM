
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Bean de administracion de la tabla SCS_PERMUTA_CABECERA
 * 
 * @author Jorge PT
 * @since 09-10-2013
 */

public class ScsPermutaCabeceraAdm extends MasterBeanAdministrador {


	/**
 	 * Constructor del bean de administracion de la tabla.
	 * @param Integer usuario: usuario.
	 */
	public ScsPermutaCabeceraAdm (UsrBean usuario) {
		super(ScsPermutaCabeceraBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	ScsPermutaCabeceraBean.C_IDPERMUTACABECERA,	
							ScsPermutaCabeceraBean.C_IDINSTITUCION, 					
							ScsPermutaCabeceraBean.C_IDTURNO,
							ScsPermutaCabeceraBean.C_IDGUARDIA,		
							ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS,
							ScsPermutaCabeceraBean.C_IDPERSONA,		
							ScsPermutaCabeceraBean.C_FECHA,
							ScsPermutaCabeceraBean.C_FECHAMODIFICACION,		
							ScsPermutaCabeceraBean.C_USUMODIFICACION};
		return campos;
	}
	
	protected String[] getClavesBean() {
		String[] campos = {ScsPermutaCabeceraBean.C_IDPERMUTACABECERA, ScsPermutaCabeceraBean.C_IDINSTITUCION};
		return campos;
	}
	
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPermutaCabeceraBean bean = null;
		try{
			bean = new ScsPermutaCabeceraBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsPermutaCabeceraBean.C_IDINSTITUCION));
			bean.setIdPermutaCabecera(UtilidadesHash.getInteger(hash,ScsPermutaCabeceraBean.C_IDPERMUTACABECERA));
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash,ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsPermutaCabeceraBean.C_IDTURNO));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsPermutaCabeceraBean.C_IDGUARDIA));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsPermutaCabeceraBean.C_IDPERSONA));
			bean.setFecha(UtilidadesHash.getString(hash,ScsPermutaCabeceraBean.C_FECHA));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;	
	}
	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsPermutaCabeceraBean b = (ScsPermutaCabeceraBean) bean;
			hash.put(ScsPermutaCabeceraBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsPermutaCabeceraBean.C_IDPERMUTACABECERA, String.valueOf(b.getIdPermutaCabecera()));
			hash.put(ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS, String.valueOf(b.getIdCalendarioGuardias()));
			hash.put(ScsPermutaCabeceraBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsPermutaCabeceraBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsPermutaCabeceraBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			hash.put(ScsPermutaCabeceraBean.C_FECHA, b.getFecha());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
		
	protected String[] getOrdenCampos() {
		return null;
	}		
	
	/**
	 * Revisamos los registros de la tabla SCS_PERMUTA_CABECERA, y borramos los que no tengan relaciones con SCS_PERMUTAGUARDIAS
	 * @return resultado de la funcion
	 */
	public boolean revisarPermutasCalendario(String idInstitucion) {	
		boolean salida = false;
		try {			
			String sql = " DELETE FROM " + ScsPermutaCabeceraBean.T_NOMBRETABLA +
				" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + idInstitucion +
					" AND NOT EXISTS (" +
						" SELECT 1" +
						" FROM " + ScsPermutaGuardiasBean.T_NOMBRETABLA + 
						" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDINSTITUCION +
							" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERMUTACABECERA + " IN (" +
								ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDPERCAB_SOLICITANTE + "," +
								ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDPERCAB_CONFIRMADOR + 
							" ) " +
						" ) ";

			deleteSQL(sql);
			salida = true;
			
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}	
	
	/**
	 * Funcion que realiza los cambios previos a la modificacion de la permuta para SCS_PERMUTA_CABECERA
	 * @param beanPermutasGuardias
	 * @return resultado de la funcion
	 */
	public boolean modificarPrevioPermutasCalendario(ScsPermutaGuardiasBean beanPermutasGuardias) {	
		Integer idInstitucion = beanPermutasGuardias.getIdInstitucion();
		Integer idPermutaCabeceraSolicitante = beanPermutasGuardias.getIdPermutaCabeceraSolicitante();
		Integer idPermutaCabeceraConfirmador = beanPermutasGuardias.getIdPermutaCabeceraConfirmador();
		boolean salida = false;
		try {			
			String sql = " UPDATE " + ScsPermutaCabeceraBean.T_NOMBRETABLA +
				" SET " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_FECHA + " = NULL, " +
					ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS + " = NULL " +
				" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + idInstitucion +				
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERMUTACABECERA + " IN (" + idPermutaCabeceraSolicitante + ", " + idPermutaCabeceraConfirmador + ")";
						
			salida = updateSQL(sql);
			
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}				
	
	/**
	 * Funcion que realiza los cambios posteriores a la modificacion de la permuta para SCS_PERMUTA_CABECERA
	 * @param beanPermutasGuardias
	 * @return resultado de la funcion
	 */
	public boolean modificarPosteriorPermutasCalendario(ScsPermutaGuardiasBean beanPermutasGuardias) {
		Integer idInstitucion = beanPermutasGuardias.getIdInstitucion();
		Integer idCalendarioSolicitante = beanPermutasGuardias.getIdCalendarioGuardiasSolicitan();
		Integer idCalendarioConfirmador = beanPermutasGuardias.getIdCalendarioGuardiasConfirmad();
		String fechaConfirmador = beanPermutasGuardias.getFechaInicioConfirmador();
		String fechaSolicitante = beanPermutasGuardias.getFechaInicioSolicitante();
		Integer idPermutaCabeceraSolicitante = beanPermutasGuardias.getIdPermutaCabeceraSolicitante();
		Integer idPermutaCabeceraConfirmador = beanPermutasGuardias.getIdPermutaCabeceraConfirmador();
		boolean salida = false;
		
		try {			
			String sql = " UPDATE " + ScsPermutaCabeceraBean.T_NOMBRETABLA +
				" SET " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_FECHA + " = TO_DATE('" + fechaSolicitante + "', '" + ClsConstants.DATE_FORMAT_SQL + "'), " +
					ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS + " = " + idCalendarioSolicitante +
				" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + idInstitucion +				
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERMUTACABECERA + " = " + idPermutaCabeceraSolicitante;	
			salida = updateSQL(sql);
			
			if (salida) {			
				sql = " UPDATE " + ScsPermutaCabeceraBean.T_NOMBRETABLA +
					" SET " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_FECHA + " = TO_DATE('" + fechaConfirmador + "', '" + ClsConstants.DATE_FORMAT_SQL + "'), " +
						ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS + " = " + idCalendarioConfirmador +
					" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + idInstitucion +				
						" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERMUTACABECERA + " = " + idPermutaCabeceraConfirmador;	
				salida = updateSQL(sql);
			}
			
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}		
	
	/**
	 * Funcion que realiza los cambios previos a la sustitucion de una guardia para SCS_PERMUTA_CABECERA
	 * @param beanPermutaCabecera
	 * @return resultado de la funcion
	 */
	public boolean sustituirPrevioPermutasCalendario(ScsPermutaCabeceraBean beanPermutaCabecera) {	
		Integer idInstitucion = beanPermutaCabecera.getIdInstitucion();
		Integer idTurno = beanPermutaCabecera.getIdTurno();
		Integer idGuardia = beanPermutaCabecera.getIdGuardia();
		Integer idCalendarioGuardias = beanPermutaCabecera.getIdCalendarioGuardias();
		Integer idPersona = beanPermutaCabecera.getIdPersona();
		String fecha = beanPermutaCabecera.getFecha();
		boolean salida = false;
		try {			
			String sql = " UPDATE " + ScsPermutaCabeceraBean.T_NOMBRETABLA +
				" SET " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERSONA + " = NULL " +
				" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + idInstitucion +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDTURNO + " = " + idTurno +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDGUARDIA + " = " + idGuardia +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS + " = " + idCalendarioGuardias +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERSONA + " = " + idPersona +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_FECHA + " = TO_DATE('" + fecha + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
						
			salida = updateSQL(sql);
			
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}		
	
	/**
	 * Funcion que realiza los cambios posteriores a la sustituacion de una guardia para SCS_PERMUTA_CABECERA
	 * @param beanPermutaCabecera
	 * @return resultado de la funcion
	 */
	public boolean sustituirPosteriorPermutasCalendario(ScsPermutaCabeceraBean beanPermutaCabecera) {
		Integer idInstitucion = beanPermutaCabecera.getIdInstitucion();
		Integer idTurno = beanPermutaCabecera.getIdTurno();
		Integer idGuardia = beanPermutaCabecera.getIdGuardia();
		Integer idCalendarioGuardias = beanPermutaCabecera.getIdCalendarioGuardias();
		Integer idPersona = beanPermutaCabecera.getIdPersona();
		String fecha = beanPermutaCabecera.getFecha();
		boolean salida = false;
		
		try {			
			String sql = " UPDATE " + ScsPermutaCabeceraBean.T_NOMBRETABLA +
				" SET " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERSONA + " = " + idPersona +
				" WHERE " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDINSTITUCION + " = " + idInstitucion +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDTURNO + " = " + idTurno +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDGUARDIA + " = " + idGuardia +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS + " = " + idCalendarioGuardias +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_IDPERSONA + " IS NULL " +
					" AND " + ScsPermutaCabeceraBean.T_NOMBRETABLA + "." + ScsPermutaCabeceraBean.C_FECHA + " = TO_DATE('" + fecha + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";			
			salida = updateSQL(sql);
			
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}			
}	