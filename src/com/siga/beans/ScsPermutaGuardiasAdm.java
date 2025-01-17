
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

/**
 * Bean de administracion de la tabla SCS_PERMUTAGUARDIAS
 * 
 * @author david.sanchezp
 * @since 24/1/2005
 * @version adrian.ayala 14/05/2008: sustitucion del campo tipodias
 */

public class ScsPermutaGuardiasAdm extends MasterBeanAdministrador {


	/**
 	 * Constructor del bean de administracion de la tabla.
	 * @param Integer usuario: usuario.
	 */
	public ScsPermutaGuardiasAdm (UsrBean usuario) {
		super( ScsPermutaGuardiasBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	ScsPermutaGuardiasBean.C_IDINSTITUCION, 			
							ScsPermutaGuardiasBean.C_NUMERO,
							ScsPermutaGuardiasBean.C_ANULADA,					
							ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE,
							ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE,		
							ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN,
							ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE,		
							ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE,
							ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE,		
							ScsPermutaGuardiasBean.C_FECHASOLICITUD,
							ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR,
							ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR,		
							ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD,
							ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR,		
							ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR,
							ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR,		
							ScsPermutaGuardiasBean.C_FECHACONFIRMACION,
							ScsPermutaGuardiasBean.C_FECHAMODIFICACION,		
							ScsPermutaGuardiasBean.C_USUMODIFICACION,
							ScsPermutaGuardiasBean.C_IDPERCAB_SOLICITANTE,
							ScsPermutaGuardiasBean.C_IDPERCAB_CONFIRMADOR};
		return campos;
	}
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsPermutaGuardiasBean.C_IDINSTITUCION, ScsPermutaGuardiasBean.C_NUMERO};
		return campos;
	}
	
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPermutaGuardiasBean bean = null;
		try{
			bean = new ScsPermutaGuardiasBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDINSTITUCION));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_NUMERO));
			bean.setAnulada(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_ANULADA));
			bean.setIdCalendarioGuardiasSolicitan(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN));
			bean.setIdTurnoSolicitante(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE));
			bean.setIdGuardiaSolicitante(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE));
			bean.setIdPersonaSolicitante(UtilidadesHash.getLong(hash,ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE));
			bean.setMotivosSolicitante(UtilidadesHash.getString(hash,ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE));
			bean.setFechaInicioSolicitante(UtilidadesHash.getString(hash,ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE));
			bean.setFechaSolicitud(UtilidadesHash.getString(hash,ScsPermutaGuardiasBean.C_FECHASOLICITUD));
			bean.setIdCalendarioGuardiasConfirmad(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD));
			bean.setIdTurnoConfirmador(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR));
			bean.setIdGuardiaConfirmador(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR));
			bean.setIdPersonaConfirmador(UtilidadesHash.getLong(hash,ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR));
			bean.setMotivosConfirmador(UtilidadesHash.getString(hash,ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR));
			bean.setFechaInicioConfirmador(UtilidadesHash.getString(hash,ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR));
			bean.setFechaConfirmacion(UtilidadesHash.getString(hash,ScsPermutaGuardiasBean.C_FECHACONFIRMACION));
			bean.setIdPermutaCabeceraSolicitante(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDPERCAB_SOLICITANTE));
			bean.setIdPermutaCabeceraConfirmador(UtilidadesHash.getInteger(hash,ScsPermutaGuardiasBean.C_IDPERCAB_CONFIRMADOR));
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
			ScsPermutaGuardiasBean b = (ScsPermutaGuardiasBean) bean;
			hash.put(ScsPermutaGuardiasBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsPermutaGuardiasBean.C_NUMERO, String.valueOf(b.getNumero()));
			hash.put(ScsPermutaGuardiasBean.C_ANULADA, String.valueOf(b.getAnulada()));
			hash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN, String.valueOf(b.getIdCalendarioGuardiasSolicitan()));
			hash.put(ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE, String.valueOf(b.getIdTurnoSolicitante()));
			hash.put(ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE, String.valueOf(b.getIdGuardiaSolicitante()));
			hash.put(ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE, String.valueOf(b.getIdPersonaSolicitante()));
			hash.put(ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE, b.getMotivosSolicitante());
			hash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE, b.getFechaInicioSolicitante());
			hash.put(ScsPermutaGuardiasBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			hash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD, String.valueOf(b.getIdCalendarioGuardiasConfirmad()));
			hash.put(ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR, String.valueOf(b.getIdTurnoConfirmador()));
			hash.put(ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR, String.valueOf(b.getIdGuardiaConfirmador()));
			hash.put(ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR, String.valueOf(b.getIdPersonaConfirmador()));
			hash.put(ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR, b.getMotivosConfirmador());
			hash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR, b.getFechaInicioConfirmador());
			hash.put(ScsPermutaGuardiasBean.C_FECHACONFIRMACION, b.getFechaConfirmacion());
			hash.put(ScsPermutaGuardiasBean.C_IDPERCAB_SOLICITANTE, b.getIdPermutaCabeceraSolicitante());
			hash.put(ScsPermutaGuardiasBean.C_IDPERCAB_CONFIRMADOR, b.getIdPermutaCabeceraConfirmador());
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
	 * Devuelve un String con la consulta SQL para obtener los datos de un solicitante a partir del IDPERSONA del Confirmador. 
	 * @param Hashtable hash: datos clave del solicitante
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarDatosSolicitanteDesdeConfirmador(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idturno="", idguardia="", idpersona="", idcalendario="", fechaInicio="";
		try {
			idinstitucion = (String)hash.get("IDINSTITUCION");
			idturno = (String)hash.get("IDTURNO");
			idguardia = (String)hash.get("IDGUARDIA");
			idcalendario = (String)hash.get("IDCALENDARIOGUARDIAS");
			idpersona = (String)hash.get("IDPERSONA");
			fechaInicio = (String)hash.get("FECHAINICIO");
			fechaInicio = GstDate.getFormatedDateShort("",fechaInicio);
			
			consulta = "SELECT permuta.*,";
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,";			
			consulta += " guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",";			
			consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO;
			consulta += " FROM "+ScsPermutaGuardiasBean.T_NOMBRETABLA+" permuta,";
			consulta += ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";			
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR+"="+idturno;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR+"="+idguardia;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR+"="+idpersona;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR+"= TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD+"="+idcalendario;
			//JOIN
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"=permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"=permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"=permuta."+ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"=permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"=permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"=permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.buscarDatosSolicitanteDesdeConfirmador(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	
	
	/** 
	 * Devuelve un String con la consulta SQL para obtener los datos de un confirmador a partir de un solicitante. 
	 * @param Hashtable hash: datos clave del solicitante
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarDatosConfirmador(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idturno="", idguardia="", idpersona="", idcalendario="", fechaInicio="";
		try {
			idinstitucion = (String)hash.get("IDINSTITUCION");
			idturno = (String)hash.get("IDTURNO");
			idguardia = (String)hash.get("IDGUARDIA");
			idcalendario = (String)hash.get("IDCALENDARIOGUARDIAS");
			idpersona = (String)hash.get("IDPERSONA");
			fechaInicio = (String)hash.get("FECHAINICIO");
			fechaInicio = GstDate.getFormatedDateShort("",fechaInicio);
			
			consulta = "SELECT permuta.*,";
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,";			
			consulta += " guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",";			
			consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO;
			consulta += " FROM "+ScsPermutaGuardiasBean.T_NOMBRETABLA+" permuta,";
			consulta += ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";			
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR+"="+idturno;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR+"="+idguardia;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR+"="+idpersona;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR+"= TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD+"="+idcalendario;
			//JOIN
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"=permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"=permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"=permuta."+ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"=permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"=permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"=permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.buscarDatosConfirmador(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve la fecha de permuta y la fecha de inicio del solicitante si existe la permuta. 
	 * @param Hashtable hash
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarPermutaConfirmador(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idturno="", idguardia="", idpersona="", idcalendario="", fechaInicio="";
		try {
			idinstitucion = (String)hash.get(ScsPermutaGuardiasBean.C_IDINSTITUCION);
			idturno = (String)hash.get(ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR);
			idguardia = (String)hash.get(ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR);
			idcalendario = (String)hash.get(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD);
			idpersona = (String)hash.get(ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR);
			fechaInicio = (String)hash.get(ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR);
			fechaInicio = GstDate.getFormatedDateShort("",fechaInicio);
			
			consulta = "SELECT permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE+",";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_FECHACONFIRMACION+",";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_NUMERO;
			consulta += " FROM "+ScsPermutaGuardiasBean.T_NOMBRETABLA+" permuta,";
			consulta += ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard";
			consulta += " WHERE ";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR+"="+idturno;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR+"="+idguardia;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR+"="+idpersona;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR+"= TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD+"="+idcalendario;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"=permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"=permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"=permuta."+ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"=permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"=permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"=permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.buscarPermutaConfirmador(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	

	/** 
	 * Devuelve un String con la consulta SQL que devuelve la fecha de permuta y la fecha de inicio del confirmador si existe la permuta. 
	 * @param Hashtable hash
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarPermutaSolicitante(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idturno="", idguardia="", idpersona="", idcalendario="", fechaInicio="";
		try {
			idinstitucion = (String)hash.get(ScsPermutaGuardiasBean.C_IDINSTITUCION);
			idturno = (String)hash.get(ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE);
			idguardia = (String)hash.get(ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE);
			idcalendario = (String)hash.get(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN);
			idpersona = (String)hash.get(ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE);
			fechaInicio = (String)hash.get(ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE);
			fechaInicio = GstDate.getFormatedDateShort("",fechaInicio);
			
			consulta = "SELECT permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR+",";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_FECHASOLICITUD+",";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_NUMERO;
			consulta += " FROM "+ScsPermutaGuardiasBean.T_NOMBRETABLA+" permuta,";
			consulta += ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard";
			consulta += " WHERE ";
			consulta += " permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE+"="+idturno;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE+"="+idguardia;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE+"="+idpersona;
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE+"= TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " AND permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN+"="+idcalendario;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"=permuta."+ScsPermutaGuardiasBean.C_IDINSTITUCION;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"=permuta."+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"=permuta."+ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"=permuta."+ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"=permuta."+ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"=permuta."+ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.buscarPermutaSolicitante(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	

	/** 
	 * Consulta para la busqueda del id NUMERO de la tabla SCS_PERMUTAGUARDIAS
	 * 
	 * @param String idinstitucion.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String nuevoNumero(String idinstitucion) throws ClsExceptions{
		String consulta = "";		
		
		try {							
			consulta = "SELECT MAX(permutas."+ScsPermutaGuardiasBean.C_NUMERO+") +1 AS "+ScsPermutaGuardiasBean.C_NUMERO;
			consulta += " FROM "+ScsPermutaGuardiasBean.T_NOMBRETABLA+" permutas";
			consulta += " WHERE ";
			consulta += " permutas."+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;			
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.nuevoIdSaltosTurnos(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	
	
	/** 
	 * Calcula un nuevo id NUMERO para la tabla.
	 * 
	 * @param String idinstitucion.
	 * @return String con el nuevo identificador. Devuelve 1 si no existe.
	 * @throws ClsExceptions
	 */	
	public String getNuevoNumero(String idinstitucion)throws ClsExceptions {
		Vector registros = new Vector();
		String nuevoId = "";
		
		try {
			registros = this.selectGenerico(this.nuevoNumero(idinstitucion));
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.getNuevoNumero().");
		}
		nuevoId = (String)((Hashtable)registros.get(0)).get(ScsPermutaGuardiasBean.C_NUMERO);
		if (nuevoId==null || nuevoId.equals(""))
			return "1";
		else 
			return nuevoId;		
	}
	
	/** 
	 * Consulta para la busqueda de los datos del Calendario de Guardias.
	 * 
	 * @param String idinstitucion.
	 * @param String idpersona.
	 * @param String orden: orden de la consulta SQL
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarSolicitantesPermuta (String idinstitucion,
											 String idpersona,
											 String orden,
											 UsrBean usr)
			throws ClsExceptions
	{
		String consulta = "";		

		try
		{
			consulta =	
				"SELECT " +
				//mhg ini
				"		FJG."+FcsFacturacionJGBean.C_NOMBRE+"|| ' (' || TO_CHAR(FJG."+FcsFacturacionJGBean.C_FECHADESDE+", 'DD/MM/YYYY') || '-' ||" +
				"		TO_CHAR(FJG."+FcsFacturacionJGBean.C_FECHAHASTA+", 'DD/MM/YYYY') || ')' NOMBREFACTURACION," +
				//mhg fin
				"		CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"," +
				"       CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDTURNO+"," +
				"       CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"," +
				"       CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDPERSONA+"," +
				"       CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"," +
				"       CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"," +
				"       CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"," +
				"       TURNO."+ScsTurnoBean.C_NOMBRE+" AS TURNO," +
				"       GUARDIA."+ScsGuardiasTurnoBean.C_NOMBRE+" AS GUARDIA," +
				"       GUARDIA."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+" AS SELECCIONLABORABLES," +
				"       GUARDIA."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+" AS SELECCIONFESTIVOS," +
				"       F_SIGA_NUMEROPERMUTAGUARDIAS" +
				"         (CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"," +
				"          CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDTURNO+"," +
				"          CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"," +
				"          CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDPERSONA+"," +
				"          CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_INICIO +
				"         ) AS ESTADO";
				
			/*Funcion que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
			- RETORNA SUSTITUIR(1) || ANULAR(1) || BORRAR(1) || PERMUTAR(1) || ASISTENCIA(1)
			-- SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
			-- ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
			-- BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
			-- PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
			-- ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia */      
			consulta += " ,PKG_SIGA_ACCIONES_GUARDIAS.FUNC_ACCIONES_GUARDIAS(";
			consulta +=" CABECERAGUARDIAS." + ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += ", CABECERAGUARDIAS." + ScsCabeceraGuardiasBean.C_IDTURNO;
			consulta += ", CABECERAGUARDIAS." + ScsCabeceraGuardiasBean.C_IDGUARDIA;
			consulta += ", CABECERAGUARDIAS." + ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += ", CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_INICIO + ") AS FUNCIONPERMUTAS";
				
			consulta += "       ,CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FACTURADO+
			     "  ,CABECERAGUARDIAS.VALIDADO,"+
			      " F_SIGA_TIENE_ACTS_VALIDADAS(CABECERAGUARDIAS.IDINSTITUCION,  CABECERAGUARDIAS.IDTURNO, CABECERAGUARDIAS.IDGUARDIA,"+
			       "CABECERAGUARDIAS.IDCALENDARIOGUARDIAS,  CABECERAGUARDIAS.IDPERSONA, CABECERAGUARDIAS.FECHAINICIO) AS ACT_VALIDADAS"+
				"  FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" CABECERAGUARDIAS," +
				"       "+ScsGuardiasTurnoBean.T_NOMBRETABLA+ " GUARDIA," +
				"       "+ScsTurnoBean.T_NOMBRETABLA +" TURNO," +
				"       "+FcsFacturacionJGBean.T_NOMBRETABLA +" FJG" +
				"" +
				" WHERE GUARDIA."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDINSTITUCION + 
				"   AND GUARDIA."+ScsGuardiasTurnoBean.C_IDTURNO+" = CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDTURNO +
				"   AND GUARDIA."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDGUARDIA +
				"   AND GUARDIA."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = TURNO."+ScsTurnoBean.C_IDINSTITUCION +
				"   AND GUARDIA."+ScsGuardiasTurnoBean.C_IDTURNO+" = TURNO."+ScsTurnoBean.C_IDTURNO +
				//mhg ini
				"   AND CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+" = FJG."+FcsFacturacionJGBean.C_IDINSTITUCION + "(+)" +
				"   AND CABECERAGUARDIAS.IDFACTURACION = FJG."+FcsFacturacionJGBean.C_IDFACTURACION + "(+)" +
				//mhg fin
				"   AND CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+" = "+idinstitucion +
				"   AND CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_IDPERSONA+" = "+idpersona +
				"" +
				" ORDER BY "; 
			if (orden.equalsIgnoreCase("FECHA"))
				consulta += "CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" DESC, TURNO, GUARDIA";
			else
				//consulta += "TURNO, GUARDIA, CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" DESC";
				consulta += "GUARDIA, CABECERAGUARDIAS."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" DESC";
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsPermutaGuardiasAdm.nuevoIdSaltosTurnos(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	} //buscarSolicitantesPermuta ()
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsPermutaGuardiasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/**
	 * Ejecuta un delete por 4 campos ID: IDINSTITUCION,IDTURNO,IDGUARDIA,IDCALENDARIOGUARDIAS
	 * @param Hashtable hash: tabla hash con los campos: 
	 * - String idinstitucion
	 * - String idcalendarioguardias
	 * - String idturno
	 * - String idguardia  
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deletePermutasCalendario(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
		boolean salida = false;
		StringBuffer sqlSolicitante = new StringBuffer();
		StringBuffer sqlConfirmador = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			

			//Como solicitante:			
			sqlSolicitante.append(" delete from "+ScsPermutaGuardiasBean.T_NOMBRETABLA);
			sqlSolicitante.append(" where "+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			sqlSolicitante.append(" and "+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN+"="+idcalendarioguardias);
			sqlSolicitante.append(" and "+ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE+"="+idturno);
			sqlSolicitante.append(" and "+ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE+"="+idguardia);
			
			//Como confirmador:
			sqlConfirmador.append(" delete from "+ScsPermutaGuardiasBean.T_NOMBRETABLA);
			sqlConfirmador.append(" where "+ScsPermutaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			sqlConfirmador.append(" and "+ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD+"="+idcalendarioguardias);
			sqlConfirmador.append(" and "+ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR+"="+idturno);
			sqlConfirmador.append(" and "+ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR+"="+idguardia);
			deleteSQL(sqlSolicitante.toString());
			deleteSQL(sqlConfirmador.toString());
//			ClsMngBBDD.executeUpdate(sqlSolicitante.toString());			
//			ClsMngBBDD.executeUpdate(sqlConfirmador.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}
	
	
	public Hashtable  buscarPermutaConfirmadorSolicitante(String idinstitucion, String idturno,String idpersona,String idguardia,String idcalendario, String fechaInicio) throws ClsExceptions{
		
		String consulta1 = "";
		String consulta2 = "";
		String consulta3 = "";
		Hashtable miTurno = null;	
		try{		
		//Confirmador
		consulta1 = "SELECT permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR + " IDPERSONA, " +
						" permuta." + ScsPermutaGuardiasBean.C_FECHACONFIRMACION + ", " +
						" permuta." + ScsPermutaGuardiasBean.C_FECHASOLICITUD + ", " +
						" coleg." + CenColegiadoBean.C_COMUNITARIO + ", "+ 
						" permuta." + ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR + ", " +	
						" permuta." + ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR + ", " +
						" DECODE(coleg." + CenColegiadoBean.C_COMUNITARIO + ", '1', coleg." + CenColegiadoBean.C_NCOMUNITARIO +", coleg."+CenColegiadoBean.C_NCOLEGIADO + ") NUMEROCONFIRMADOR, " + 		
						" perso." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || perso." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || perso." + CenPersonaBean.C_NOMBRE + " NOMBRECONFIRMADOR, " +
						" permuta." + ScsPermutaGuardiasBean.C_FECHAMODIFICACION +
					" FROM " + ScsPermutaGuardiasBean.T_NOMBRETABLA + " permuta, " +
						ScsCabeceraGuardiasBean.T_NOMBRETABLA + " guard, " +	
						CenPersonaBean.T_NOMBRETABLA + " perso, " +			
						CenColegiadoBean.T_NOMBRETABLA + " coleg " +
					" WHERE perso." + CenPersonaBean.C_IDPERSONA + " = permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR +
						" AND coleg." + CenColegiadoBean.C_IDPERSONA + " = permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR +
						" AND coleg." + CenColegiadoBean.C_IDINSTITUCION + " = permuta." + ScsPermutaGuardiasBean.C_IDINSTITUCION +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDPERSONA + " = " + idpersona +		
						" AND guard." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " = TO_DATE('" + fechaInicio + "','DD/MM/YYYY') " +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " = " + idguardia +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDTURNO + " = " + idturno +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " = " + idinstitucion +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS + " = " + idcalendario +				
						" AND guard." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " = permuta." + ScsPermutaGuardiasBean.C_IDINSTITUCION +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS + " = permuta." + ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDTURNO + " = permuta." + ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " = permuta." + ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDPERSONA + " = permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE +
						" AND guard." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " = permuta." + ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE;
		
		//Solicitante
		consulta2 = "SELECT permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE +" IDPERSONA, " +
						" permuta." + ScsPermutaGuardiasBean.C_FECHACONFIRMACION + ", " +
						" permuta." + ScsPermutaGuardiasBean.C_FECHASOLICITUD + ", " +
						" coleg." + CenColegiadoBean.C_COMUNITARIO + ", " + 
						" permuta." + ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE + ", " +		
						" permuta." + ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE + ", " +	
						" DECODE(coleg." + CenColegiadoBean.C_COMUNITARIO + ", '1', coleg." + CenColegiadoBean.C_NCOMUNITARIO + ", coleg." + CenColegiadoBean.C_NCOLEGIADO + ") NUMEROCONFIRMADOR, " +		
						" perso." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || perso." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || perso." + CenPersonaBean.C_NOMBRE + " NOMBRECONFIRMADOR, " +
						" permuta." + ScsPermutaGuardiasBean.C_FECHAMODIFICACION +
					" FROM " + ScsPermutaGuardiasBean.T_NOMBRETABLA + " permuta, " +
						ScsCabeceraGuardiasBean.T_NOMBRETABLA + " guard, " +
						CenPersonaBean.T_NOMBRETABLA + " perso, " +			
						CenColegiadoBean.T_NOMBRETABLA + " coleg " +
					" WHERE perso." + CenPersonaBean.C_IDPERSONA + " = permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE +
						" AND coleg." + CenColegiadoBean.C_IDPERSONA + " = permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE +
						" AND coleg." + CenColegiadoBean.C_IDINSTITUCION + " = permuta." + ScsPermutaGuardiasBean.C_IDINSTITUCION +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDPERSONA + " = " + idpersona +		
						" AND guard." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " = TO_DATE('" + fechaInicio + "','DD/MM/YYYY')" +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " = " + idguardia +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDTURNO + " = " + idturno +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " = " + idinstitucion +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS + " = " + idcalendario +				
						" AND guard." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " = permuta." + ScsPermutaGuardiasBean.C_IDINSTITUCION +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS + " = permuta." + ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDTURNO + " = permuta." + ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " = permuta." + ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR +
						" AND guard." + ScsCabeceraGuardiasBean.C_IDPERSONA + " = permuta." + ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR +
						" AND guard." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " = permuta." + ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR;
		
		consulta3= consulta1 +" UNION "+ consulta2 + " ORDER BY " + ScsPermutaGuardiasBean.C_FECHAMODIFICACION + " DESC";
		
		// Como puede obtener
		Vector vResultado = this.ejecutaSelect(consulta3);
		if (vResultado.size() > 0)
			miTurno = (Hashtable) vResultado.get(0);
		
		}catch (Exception e){
			miTurno = new Hashtable();
			//throw new ClsExceptions (e, "Error al ejecutar el 'consulta 3 en buscarPermutaConfirmadorSolicitante()' en B.D. ");
		}
		return miTurno;
		
	}
		
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
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
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
		
	/**
	 * Funcion que comprueba si existe en SCS_PERMUTAGUARDIAS, el solicitante o el confirmador permutado pendiente
	 * @param beanCabeceraGuardiasSolicitante
	 * @param beanCabeceraGuardiaConfirmador
	 * @return
	 */
	public boolean existeSolicitanteCompradorPermutadosPendiente(ScsCabeceraGuardiasBean beanCabeceraGuardiasSolicitante, ScsCabeceraGuardiasBean beanCabeceraGuardiaConfirmador) {
		boolean salida = true;		
		try {			
			String sql = " SELECT " + ScsPermutaGuardiasBean.T_NOMBRETABLA + ".* " + 
				" FROM " + ScsPermutaGuardiasBean.T_NOMBRETABLA + 
				" WHERE " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDINSTITUCION + " = " + beanCabeceraGuardiasSolicitante.getIdInstitucion() +
					 " AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE + " = " + beanCabeceraGuardiasSolicitante.getIdTurno() +
					 " AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE + " = " + beanCabeceraGuardiasSolicitante.getIdGuardia() +
					 " AND " + ScsPermutaGuardiasBean.C_FECHACONFIRMACION + " IS NULL " +
					 " AND ( " +
						" ( " + 
							ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN + " = " + beanCabeceraGuardiasSolicitante.getIdCalendario() +
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE + " = " + beanCabeceraGuardiasSolicitante.getIdPersona() + 
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE + " = TO_DATE('" + beanCabeceraGuardiasSolicitante.getFechaInicio() + "', '" + ClsConstants.DATE_FORMAT_SQL + "') " + 
						" ) OR ( " +
							ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN + " = " + beanCabeceraGuardiaConfirmador.getIdCalendario() +
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE + " = " + beanCabeceraGuardiaConfirmador.getIdPersona() + 
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE + " = TO_DATE('" + beanCabeceraGuardiaConfirmador.getFechaInicio() + "', '" + ClsConstants.DATE_FORMAT_SQL + "') " + 
						" ) OR ( " +
							ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD + " = " + beanCabeceraGuardiasSolicitante.getIdCalendario() +
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR + " = " + beanCabeceraGuardiasSolicitante.getIdPersona() + 
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR + " = TO_DATE('" + beanCabeceraGuardiasSolicitante.getFechaInicio() + "', '" + ClsConstants.DATE_FORMAT_SQL + "') " + 
						" ) OR ( " +
							ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD + " = " + beanCabeceraGuardiaConfirmador.getIdCalendario() +
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR + " = " + beanCabeceraGuardiaConfirmador.getIdPersona() + 
							" AND " + ScsPermutaGuardiasBean.T_NOMBRETABLA + "." + ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR + " = TO_DATE('" + beanCabeceraGuardiaConfirmador.getFechaInicio() + "', '" + ClsConstants.DATE_FORMAT_SQL + "') " + 
						" ) " + 
					" ) ";		
			
			Vector vPermutas = selectSQL(sql);
			
			if (vPermutas.size() == 0) {
				salida = false;
			}
			
		} catch (Exception e) {
			salida = true;
		}
		return salida;
	}		
}	