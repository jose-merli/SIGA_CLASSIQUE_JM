/*
 * Created on 17/09/2008
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.general.SIGAException;

/**
 * @author angel corral
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsActaComisionAdm extends MasterBeanAdministrador {
	
	public ScsActaComisionAdm(UsrBean usr) {
		super(ScsActaComisionBean.T_NOMBRETABLA, usr);
	}

	protected String[] getCamposBean() {
		String [] campos = {
				ScsActaComisionBean.C_IDACTA,
				ScsActaComisionBean.C_IDINSTITUCION,
				ScsActaComisionBean.C_NUMEROACTA,
				ScsActaComisionBean.C_ANIOACTA,
				ScsActaComisionBean.C_FECHAREUNION,
				ScsActaComisionBean.C_FECHARESOLUCION,
				ScsActaComisionBean.C_HORAINICIOREUNION,
				ScsActaComisionBean.C_HORAFINREUNION,
				ScsActaComisionBean.C_IDPRESIDENTE,
				ScsActaComisionBean.C_IDSECRETARIO,
				ScsActaComisionBean.C_MIEMBROSCOMISION,
				ScsActaComisionBean.C_OBSERVACIONES,
				ScsActaComisionBean.C_PENDIENTES,
				ScsActaComisionBean.C_FECHAMODIFICACION,
				ScsActaComisionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getCamposActualizablesBean ()
	{
		return getCamposBean();
	}
	
	public String[] getClavesBean() {
		String[] campos = { 
				ScsActaComisionBean.C_IDACTA,
				ScsActaComisionBean.C_ANIOACTA,
				ScsActaComisionBean.C_IDINSTITUCION};
		return campos;
	}


	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsActaComisionBean bean = null;
		try{
			bean = new ScsActaComisionBean();
			bean.setIdActa (UtilidadesHash.getInteger(hash,ScsActaComisionBean.C_IDACTA));
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,ScsActaComisionBean.C_IDINSTITUCION));
			bean.setIdPresidente (UtilidadesHash.getInteger(hash,ScsActaComisionBean.C_IDPRESIDENTE));
			bean.setIdSecretario (UtilidadesHash.getInteger(hash,ScsActaComisionBean.C_IDSECRETARIO));
			bean.setAnioActa (UtilidadesHash.getInteger(hash,ScsActaComisionBean.C_ANIOACTA));
			bean.setNumeroActa (UtilidadesHash.getString (hash,ScsActaComisionBean.C_NUMEROACTA));
			bean.setFechaResolucionCAJG (UtilidadesHash.getString (hash,ScsActaComisionBean.C_FECHARESOLUCION));
			bean.setFechaReunion (UtilidadesHash.getString (hash,ScsActaComisionBean.C_FECHAREUNION));
			bean.setHoraInicioReunion (UtilidadesHash.getString (hash,ScsActaComisionBean.C_HORAINICIOREUNION));
			bean.setHoraFinReunion (UtilidadesHash.getString (hash,ScsActaComisionBean.C_HORAFINREUNION));
			bean.setObservaciones (UtilidadesHash.getString (hash,ScsActaComisionBean.C_OBSERVACIONES));
			bean.setPendientes (UtilidadesHash.getString (hash,ScsActaComisionBean.C_PENDIENTES));
			bean.setMiembrosComision (UtilidadesHash.getString (hash,ScsActaComisionBean.C_MIEMBROSCOMISION));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,ScsActaComisionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsActaComisionBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsActaComisionBean b = (ScsActaComisionBean) bean;
			
			UtilidadesHash.set(hash, ScsActaComisionBean.C_IDACTA, 			b.getIdActa());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_IDINSTITUCION, 	b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_ANIOACTA, 		b.getAnioActa());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_NUMEROACTA, 		b.getNumeroActa());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_IDPRESIDENTE, 	b.getIdPresidente());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_IDSECRETARIO, 	b.getIdSecretario());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_MIEMBROSCOMISION, b.getMiembrosComision());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_OBSERVACIONES, 	b.getObservaciones());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_FECHARESOLUCION, b.getFechaResolucionCAJG());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_FECHAREUNION, 	b.getFechaReunion());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_HORAFINREUNION, 	b.getHoraFinReunion());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_HORAINICIOREUNION, b.getHoraInicioReunion());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_PENDIENTES, b.getPendientes());
			UtilidadesHash.set(hash, ScsActaComisionBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, ScsActaComisionBean.C_USUMODIFICACION,	b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	
	
	

	
	/**
	 * @param anio 
	 * @return
	 * @throws NumberFormatException 
	 * @throws ClsExceptions
	 */
	public int getNuevoNumActa(String idInstitucion, String anio) throws ClsExceptions {
		
		int nextVal = 1;
		
		String sql = 
				" SELECT NVL(MAX(" + ScsActaComisionBean.C_NUMEROACTA + "), 0) AS " + ScsActaComisionBean.C_NUMEROACTA +
				" FROM " + ScsActaComisionBean.T_NOMBRETABLA +
				" WHERE " + ScsActaComisionBean.C_IDINSTITUCION + " = " + idInstitucion+
				" AND " + ScsActaComisionBean.C_ANIOACTA + " = " + anio;
		
		RowsContainer rc = new RowsContainer();
		
		if (rc.find(sql)) {
			Row r = (Row) rc.get(0);
			nextVal = Integer.parseInt(r.getString(ScsActaComisionBean.C_NUMEROACTA));
			nextVal++;
		}
		
		return nextVal;
		
	}
	/**
	 * Sacamos el numero de acta teniendo en cuenta que para aumentar el numero hay que reemplazar el sufijo. Elpriemr registro sera 1
	 * @param idInstitucion
	 * @param anio
	 * @param sufijo
	 * @return
	 * @throws ClsExceptions
	 */

	public int getNuevoNumActa(String idInstitucion, String anio, String sufijo) throws ClsExceptions {

		int nextVal = 1;

		StringBuilder sql =  new StringBuilder();
		sql.append(" SELECT NVL(MAX(NUMEROACTA), 0) AS NUMEROACTA FROM SCS_ACTACOMISION WHERE "); 
		sql.append(" IDINSTITUCION =  ");
		sql.append(idInstitucion);
		sql.append(" AND ANIOACTA =  ");
		sql.append(anio);
		sql.append(" and numeroacta like '%"); 
		sql.append(sufijo); 
		sql.append("%'");
				
		RowsContainer rc = new RowsContainer();
		if (rc.find(sql.toString())) {
			Row r = (Row) rc.get(0);
			String numeroActa = r.getString(ScsActaComisionBean.C_NUMEROACTA);
			numeroActa = UtilidadesString.replaceAllIgnoreCase(numeroActa, sufijo, "");
			nextVal = Integer.parseInt(numeroActa)+1;
		}

		return nextVal;

	}

	/**
	 * @param anio 
	 * @return
	 * @throws NumberFormatException 
	 * @throws ClsExceptions
	 */
	public int getNuevoIdActa(String idInstitucion, String anio) throws ClsExceptions {
		
		int nextVal = 1;
		
		String sql = 
				" SELECT NVL(MAX(" + ScsActaComisionBean.C_IDACTA + "), 0) AS " + ScsActaComisionBean.C_IDACTA +
				" FROM " + ScsActaComisionBean.T_NOMBRETABLA +
				" WHERE " + ScsActaComisionBean.C_IDINSTITUCION + " = " + idInstitucion+
				" AND " + ScsActaComisionBean.C_ANIOACTA + " = " + anio;
		
		RowsContainer rc = new RowsContainer();
		
		if (rc.find(sql)) {
			Row r = (Row) rc.get(0);
			nextVal = Integer.parseInt(r.getString(ScsActaComisionBean.C_IDACTA));
			nextVal++;
		}
		
		return nextVal;
		
	}
	
	/**
	 * @throws Exception 
	 *
	 */
	public Paginador getBusquedaActas(HashMap filtros) throws Exception{
		StringBuffer consulta = new StringBuffer();
		consulta.append("select ");
		consulta.append(" act."+ScsActaComisionBean.C_IDACTA);
		consulta.append(", act."+ScsActaComisionBean.C_IDINSTITUCION);
		consulta.append(", act."+ScsActaComisionBean.C_ANIOACTA);
		consulta.append(", act."+ScsActaComisionBean.C_NUMEROACTA);
		consulta.append(", act."+ScsActaComisionBean.C_FECHARESOLUCION);
		consulta.append(", act."+ScsActaComisionBean.C_FECHAREUNION);
		consulta.append(", act."+ScsActaComisionBean.C_IDPRESIDENTE);
		consulta.append(", act."+ScsActaComisionBean.C_IDSECRETARIO);
		consulta.append(", f_siga_getrecurso(pre."+ScsPonenteBean.C_NOMBRE+",1) as NOMBREPRESIDENTE");
		consulta.append(", f_siga_getrecurso(sec."+ScsPonenteBean.C_NOMBRE+",1) as NOMBRESECRETARIO");
		consulta.append(" from " + ScsActaComisionBean.T_NOMBRETABLA +" act");
		consulta.append(" , " + ScsPonenteBean.T_NOMBRETABLA +" pre");
		consulta.append(" , " + ScsPonenteBean.T_NOMBRETABLA +" sec");
		consulta.append(" where act." + ScsActaComisionBean.C_IDINSTITUCION + " = " + filtros.get(ScsActaComisionBean.C_IDINSTITUCION).toString());
		consulta.append(" and pre." + ScsPonenteBean.C_IDPONENTE+ "(+) = act." + ScsActaComisionBean.C_IDPRESIDENTE);
		consulta.append(" and pre." + ScsPonenteBean.C_IDINSTITUCION+ "(+) = act." + ScsActaComisionBean.C_IDINSTITUCION);
		consulta.append(" and sec." + ScsPonenteBean.C_IDPONENTE+ "(+) = act." + ScsActaComisionBean.C_IDSECRETARIO);
		consulta.append(" and sec." + ScsPonenteBean.C_IDINSTITUCION+ "(+) = act." + ScsActaComisionBean.C_IDINSTITUCION);
		if(filtros.get(ScsActaComisionBean.C_ANIOACTA)!=null && !filtros.get(ScsActaComisionBean.C_ANIOACTA).toString().equalsIgnoreCase("")){
			consulta.append(" and act." + ScsActaComisionBean.C_ANIOACTA + " = " + filtros.get(ScsActaComisionBean.C_ANIOACTA).toString());
		}
		if(filtros.get(ScsActaComisionBean.C_NUMEROACTA)!=null && !filtros.get(ScsActaComisionBean.C_NUMEROACTA).toString().equalsIgnoreCase("")){
			consulta.append(" and act." + ScsActaComisionBean.C_NUMEROACTA + " like '%" + filtros.get(ScsActaComisionBean.C_NUMEROACTA).toString()+"%'");
		}
		if(filtros.get(ScsActaComisionBean.C_FECHARESOLUCION)!=null && !filtros.get(ScsActaComisionBean.C_FECHARESOLUCION).toString().equalsIgnoreCase("")){
			consulta.append(" and act." + ScsActaComisionBean.C_FECHARESOLUCION + " = '" + filtros.get(ScsActaComisionBean.C_FECHARESOLUCION).toString() + "'");
		}
		if(filtros.get(ScsActaComisionBean.C_FECHAREUNION)!=null && !filtros.get(ScsActaComisionBean.C_FECHAREUNION).toString().equalsIgnoreCase("")){
			consulta.append(" and act." + ScsActaComisionBean.C_FECHAREUNION + " = '" + filtros.get(ScsActaComisionBean.C_FECHAREUNION).toString()+"'");
		}
		if(filtros.get(ScsActaComisionBean.C_IDPRESIDENTE)!=null && !filtros.get(ScsActaComisionBean.C_IDPRESIDENTE).toString().equalsIgnoreCase("")){
			consulta.append(" and act." + ScsActaComisionBean.C_IDPRESIDENTE + " = " + filtros.get(ScsActaComisionBean.C_IDPRESIDENTE).toString());
		}
		if(filtros.get(ScsActaComisionBean.C_IDSECRETARIO)!=null && !filtros.get(ScsActaComisionBean.C_IDSECRETARIO).toString().equalsIgnoreCase("")){
			consulta.append(" and act." + ScsActaComisionBean.C_IDSECRETARIO + " = " + filtros.get(ScsActaComisionBean.C_IDSECRETARIO).toString());
		}
		consulta.append(" order by act." + ScsActaComisionBean.C_ANIOACTA + " desc, act." + ScsActaComisionBean.C_NUMEROACTA + " desc");
		Paginador<Vector> paginador = new Paginador<Vector>(consulta.toString());
		return paginador;
		
	}

	public Hashtable<String, String> getDatosActa(String idActa, String anioActa, String idInstitucion) throws ClsExceptions {
		
		StringBuffer consulta = new StringBuffer();
		RowsContainer rc = new RowsContainer(); 
		Hashtable resultado = new Hashtable();
		consulta.append("select ");
		consulta.append(" act."+ScsActaComisionBean.C_IDACTA);
		consulta.append(", act."+ScsActaComisionBean.C_IDINSTITUCION);
		consulta.append(", act."+ScsActaComisionBean.C_ANIOACTA);
		consulta.append(", act."+ScsActaComisionBean.C_NUMEROACTA);
		consulta.append(", act."+ScsActaComisionBean.C_FECHARESOLUCION);
		consulta.append(", act."+ScsActaComisionBean.C_FECHAREUNION);
		consulta.append(", act."+ScsActaComisionBean.C_IDPRESIDENTE);
		consulta.append(", act."+ScsActaComisionBean.C_IDSECRETARIO);
		consulta.append(", act."+ScsActaComisionBean.C_HORAINICIOREUNION);
		consulta.append(", act."+ScsActaComisionBean.C_HORAFINREUNION);
		consulta.append(", act."+ScsActaComisionBean.C_OBSERVACIONES);
		consulta.append(", act."+ScsActaComisionBean.C_MIEMBROSCOMISION);
		consulta.append(", act."+ScsActaComisionBean.C_PENDIENTES);
		consulta.append(", act."+ScsActaComisionBean.C_FECHAINTERCAMBIO);
		
		consulta.append(" from " + ScsActaComisionBean.T_NOMBRETABLA +" act");
		consulta.append(" where act." + ScsActaComisionBean.C_IDINSTITUCION + " = " + idInstitucion);
		consulta.append(" and act." + ScsActaComisionBean.C_ANIOACTA+ " = " + anioActa);
		consulta.append(" and act." + ScsActaComisionBean.C_IDACTA+ " = " + idActa);
		
		if (rc.find(consulta.toString())) {
           if(rc.size()==1){
              Row fila = (Row) rc.get(0);
              resultado = fila.getRow();	  
           }
        } 
		return resultado;
	}
	
	/** Funcion update (MasterBean bean)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el update 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	
	public boolean updateDirect(MasterBean bean) throws ClsExceptions {
		try {
			this.updateDirect(bean, getClavesBean(), getCamposActualizablesBean());
		}catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());	
		}
		return false;	
	}
	
	public Vector<Hashtable<String, String>> getListadoEJGActa(String idActa, String anioActa, String idInstitucion,String longitudNumEjg) throws ClsExceptions {
		StringBuffer consulta = new StringBuffer();
		RowsContainer rc = new RowsContainer(); 
		Vector resultado = new Vector();
		consulta.append("select ");
		consulta.append(" ejg."+ScsEJGBean.C_ANIO + " as ANIO");
		consulta.append(", lpad(ejg."+ScsEJGBean.C_NUMEJG + ","+longitudNumEjg+",0) as NUMERO");
		consulta.append(", ejg."+ScsEJGBean.C_FECHAAPERTURA + " as FECHAAPERTURA");
		consulta.append(", tur."+ScsTurnoBean.C_NOMBRE + " as TURNO");
		consulta.append(", gua."+ScsGuardiasTurnoBean.C_NOMBRE  + " as GUARDIA");
		consulta.append(", sol."+ScsPersonaJGBean.C_NOMBRE+"||' '||sol."+ScsPersonaJGBean.C_APELLIDO1+"||' '||sol."+ScsPersonaJGBean.C_APELLIDO2 + " as SOLICITANTE ");
		consulta.append(", ejg."+ScsEJGBean.C_NUMERO + " as NUM");
		consulta.append(", ejg."+ScsEJGBean.C_IDINSTITUCION + " as IDINSTITUCION");
		consulta.append(", ejg."+ScsEJGBean.C_IDTIPOEJG + " as IDTIPOEJG");
		consulta.append(", ejg.IDTIPORATIFICACIONEJG,ejg.IDFUNDAMENTOJURIDICO ");
		consulta.append(", f_siga_getrecurso(r.descripcion,"+usrbean.getLanguage()+") resolucion ");
		consulta.append(", (SELECT INT.ABREVIATURA FROM CEN_INSTITUCION INT WHERE INT.IDINSTITUCION = EJG.IDINSTITUCION) INST_ABREV "); 
		consulta.append(" from " + ScsEJGBean.T_NOMBRETABLA +" ejg");
		consulta.append(" , " + ScsTurnoBean.T_NOMBRETABLA +" tur");
		consulta.append(" , " + ScsPersonaJGBean.T_NOMBRETABLA +" sol");
		consulta.append(" , " + ScsGuardiasTurnoBean.T_NOMBRETABLA +" gua");
		consulta.append(", scs_tiporesolucion r");

		consulta.append(" where ejg." + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + idInstitucion);
		consulta.append(" and ejg." + ScsEJGBean.C_ANIOACTA+ " = " + anioActa);
		consulta.append(" and ejg." + ScsEJGBean.C_IDACTA+ " = " + idActa);
		consulta.append(" and tur." + ScsTurnoBean.C_IDINSTITUCION+ "(+) = ejg." + ScsEJGBean.C_IDINSTITUCION);
		consulta.append(" and tur." + ScsTurnoBean.C_IDTURNO+ "(+) = ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO);
		consulta.append(" and gua." + ScsGuardiasTurnoBean.C_IDINSTITUCION+ "(+) = ejg." + ScsEJGBean.C_IDINSTITUCION);
		consulta.append(" and gua." + ScsGuardiasTurnoBean.C_IDTURNO+ "(+) = ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO);
		consulta.append(" and gua." + ScsGuardiasTurnoBean.C_IDGUARDIA+ "(+) = ejg." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA);
		consulta.append(" and sol." + ScsPersonaJGBean.C_IDINSTITUCION+ "(+) = ejg." + ScsEJGBean.C_IDINSTITUCION);
		consulta.append(" and sol." + ScsPersonaJGBean.C_IDPERSONA+ "(+) = ejg." + ScsEJGBean.C_IDPERSONAJG);
		consulta.append(" and ejg.idtiporatificacionejg =  r.idtiporesolucion(+) ");
		
		consulta.append(" order by NVL2(EJG.IDTIPORATIFICACIONEJG, 0,1) desc, ejg.ANIO, ejg.NUMEJG  ");
//		consulta.append(" order by  IDTIPORATIFICACIONEJG desc, ejg." + ScsEJGBean.C_ANIO + "  , ejg." + ScsEJGBean.C_NUMERO + " ");
		
		if (rc.find(consulta.toString())) {
           if(rc.size()>0){
              resultado = rc.getAll();
           }
        } 
		return resultado;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @return
	 * @throws ClsExceptions 
	 */
	public Vector getDatosInforme(String idInstitucion, String idActa, String anioActa) throws ClsExceptions {
		
		Vector salida = new Vector();
		StringBuffer consulta = new StringBuffer();
		consulta.append("select ");

		consulta.append(" act."+ScsActaComisionBean.C_IDACTA);
		consulta.append(", act."+ScsActaComisionBean.C_IDINSTITUCION);
		consulta.append(", act."+ScsActaComisionBean.C_ANIOACTA);
		consulta.append(", act."+ScsActaComisionBean.C_NUMEROACTA);
		//consulta.append(", f_siga_fechaenletra(to_char(act."+ScsActaComisionBean.C_FECHARESOLUCION+", 'dd/mm/yyyy'),'',1) as " + ScsActaComisionBean.C_FECHARESOLUCION+"ENLETRA");
		consulta.append(", decode (act."+ScsActaComisionBean.C_FECHARESOLUCION+",null, null, f_siga_fechaenletra(to_char(act."+ScsActaComisionBean.C_FECHARESOLUCION+", 'dd/mm/yyyy'),'',1)) as " + ScsActaComisionBean.C_FECHARESOLUCION+"ENLETRA");
		consulta.append(", decode (act."+ScsActaComisionBean.C_FECHAREUNION+",null, null, f_siga_fechaenletra(to_char(act."+ScsActaComisionBean.C_FECHAREUNION+", 'dd/mm/yyyy'),'',1)) as " + ScsActaComisionBean.C_FECHAREUNION+"ENLETRA");
		consulta.append(", to_char(act."+ScsActaComisionBean.C_FECHARESOLUCION+", 'dd/mm/yyyy') as " + ScsActaComisionBean.C_FECHARESOLUCION);
		consulta.append(", to_char(act."+ScsActaComisionBean.C_FECHAREUNION+", 'dd/mm/yyyy') as " + ScsActaComisionBean.C_FECHAREUNION);
		consulta.append(", to_char(act."+ScsActaComisionBean.C_HORAINICIOREUNION+", 'hh24:mi') as " + ScsActaComisionBean.C_HORAINICIOREUNION);
		consulta.append(", to_char(act."+ScsActaComisionBean.C_HORAFINREUNION+", 'hh24:mi') as " + ScsActaComisionBean.C_HORAFINREUNION );
		consulta.append(", act."+ScsActaComisionBean.C_OBSERVACIONES);
		consulta.append(", act."+ScsActaComisionBean.C_MIEMBROSCOMISION);
		consulta.append(", act."+ScsActaComisionBean.C_PENDIENTES);
		consulta.append(", f_siga_getrecurso(pres."+ScsPonenteBean.C_NOMBRE+",1) as PRESIDENTE ");
		consulta.append(", f_siga_getrecurso(sec."+ScsPonenteBean.C_NOMBRE+",1) as SECRETARIO ");
		
		consulta.append(" from " + ScsActaComisionBean.T_NOMBRETABLA +" act ");
		consulta.append(" , " + ScsPonenteBean.T_NOMBRETABLA + " sec ");
		consulta.append(" , " + ScsPonenteBean.T_NOMBRETABLA + " pres ");

		consulta.append(" where pres." + ScsPonenteBean.C_IDPONENTE+ "(+) = act." + ScsActaComisionBean.C_IDPRESIDENTE);
		consulta.append(" and pres." + ScsPonenteBean.C_IDINSTITUCION+ "(+) = act." + ScsActaComisionBean.C_IDINSTITUCION);
		consulta.append(" and sec." + ScsPonenteBean.C_IDPONENTE+ "(+) = act." + ScsActaComisionBean.C_IDSECRETARIO);
		consulta.append(" and sec." + ScsPonenteBean.C_IDINSTITUCION+ "(+) = act." + ScsActaComisionBean.C_IDINSTITUCION);

		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		keyContador++;
		htCodigos.put(new Integer(keyContador), idInstitucion);
		consulta.append(" and act." + ScsActaComisionBean.C_IDINSTITUCION + " =:");
		consulta.append(keyContador);

		keyContador++;
		htCodigos.put(new Integer(keyContador), anioActa);
		consulta.append(" and act." + ScsActaComisionBean.C_ANIOACTA + "=:");
		consulta.append(keyContador);

		keyContador++;
		htCodigos.put(new Integer(keyContador), idActa);
		consulta.append("  and act." + ScsActaComisionBean.C_IDACTA+ " = :");
		consulta.append(keyContador);

		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		salida = helperInformes.ejecutaConsultaBind(consulta.toString(), htCodigos);
		//salida.add(helperInformes.ejecutaConsultaBind(consulta.toString(), htCodigos));
		return salida;

	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @param longitudNumEjg 
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsInforme(String idInstitucion, String idActa, String anioActa, String longitudNumEjg, Boolean isImpugando) throws ClsExceptions, SIGAException {
		Vector datos = new Vector();
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		StringBuilder stringBuilder = new StringBuilder();
		if (isImpugando != null)
			stringBuilder.append(" SELECT * FROM ( ");

		stringBuilder.append(" SELECT ROWNUM AS NACUERDO,  DATOS.*, ");
		stringBuilder.append(" F_SIGA_GETRECURSO(RES.DESCRIPCION, ");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(") AS RESOLUCION, ");
		stringBuilder.append(" FUN.TEXTOPLANTILLA3, FUN.TEXTOPLANTILLA4, ");
		stringBuilder.append(" F_SIGA_GETRECURSO(FUN.DESCRIPCION, ");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(") AS FUNDAMENTO ");
		stringBuilder.append(",DECODE(GREATEST(DATOS.FECHA_IMPUGNADO, DATOS.FECHARESOLUCION),DATOS.FECHA_IMPUGNADO,0,1) ISIMPUGNADO ");

		stringBuilder.append(" ,  (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN '' ELSE TO_CHAR(DATOS.FECHAAUTO, 'DD/MM/YYYY') END) IMPUGNACION_AUTOFECHA ");
		stringBuilder.append(" , (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN '' ELSE DECODE(DATOS.FECHAAUTO, null, '', PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(DATOS.FECHAAUTO,'m',");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(" )) END) IMPUGNACION_AUTOFECHALETRA ");
		stringBuilder.append(" , (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN '' ELSE DECODE(DATOS.idtiporesolauto,  null,  '',  (SELECT f_siga_getrecurso(tra.DESCRIPCION, ");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(" )     FROM scs_tiporesolauto tra    WHERE tra.idtiporesolauto = DATOS.IDTIPORESOLAUTO)) END) IMPUGNACION_AUTORESOLUTORIO ");
		stringBuilder.append(" , (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN '' ELSE DECODE(DATOS.IDTIPOSENTIDOAUTO,  null,  '',  (SELECT f_siga_getrecurso(tsa.DESCRIPCION, ");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(" )     FROM scs_tiposentidoauto tsa    WHERE tsa.idtiposentidoauto = DATOS.IDTIPOSENTIDOAUTO)) END) IMPUGNACION_AUTOSENTIDO ");
		stringBuilder.append(" , (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN '' ELSE DECODE(DATOS.ANIORESOLUCION,  null,  '',  DATOS.ANIORESOLUCION || '/' || DATOS.NUMERORESOLUCION ||  decode(DATOS.BISRESOLUCION, null, '', 1, ' bis', ''))   END) IMPUGNACION_NUMRESOLUCION ");
		stringBuilder.append(" , (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN '' ELSE TO_CHAR(DATOS.FECHAPUBLICACION, 'DD/MM/YYYY') END) IMPUGNACION_FECHAPUBLICACION ");
		stringBuilder.append(" , (CASE WHEN greatest(DATOS.FECHA_IMPUGNADO, DATOS.Fecharesolucion) = DATOS.FECHA_IMPUGNADO THEN TO_CLOB('')   ELSE   DATOS.OBSERVACIONIMPUGNACION END) IMPUGNACION_OBSERVACIONES ");

		stringBuilder.append(" FROM (SELECT EJG.ANIO AS ANIO,  EJG.NUMEJG, ");
		stringBuilder.append(" LPAD(EJG.NUMEJG,");
		stringBuilder.append(longitudNumEjg);
		stringBuilder.append(", 0) AS NUMERO, ");
		stringBuilder.append(" TO_CHAR(EJG.FECHAAPERTURA, 'DD/MM/YYYY') AS FECHAAPERTURA, ");
		stringBuilder.append(" TUR.NOMBRE AS TURNO, ");
		stringBuilder.append(" GUA.NOMBRE AS GUARDIA, ");
		stringBuilder.append(" SOL.NOMBRE || ' ' || SOL.APELLIDO1 || ' ' || SOL.APELLIDO2 AS SOLICITANTE, ");
		stringBuilder.append(" EJG.IDINSTITUCION AS IDINSTITUCION, ");
		stringBuilder.append(" EJG.IDTIPOEJG AS IDTIPOEJG, ");
		stringBuilder.append(" EJG.RATIFICACIONDICTAMEN AS DICTAMEN, ");
		stringBuilder.append(" F_SIGA_GETRECURSO(PON.NOMBRE,");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(") AS PONENTE, ");
		stringBuilder.append(" F_SIGA_GETRECURSO(TIP.DESCRIPCION,");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(") AS TIPOEJG, ");
		stringBuilder.append(" DECODE(EJG.REQUIERENOTIFICARPROC, ");
		stringBuilder.append(" '1', ");
		stringBuilder.append(" F_SIGA_GETRECURSO_ETIQUETA('GRATUITA.OPERARRATIFICACION.MENSAJE.REQUIERENOTIFICARPROC', ");
		stringBuilder.append(this.usrbean.getLanguage());
		stringBuilder.append(" ) || ");

		stringBuilder.append(" F_SIGA_GETPROCURADORCONTR_EJG(EJG.IDINSTITUCION, ");
		stringBuilder.append(" EJG.IDTIPOEJG, ");
		stringBuilder.append(" EJG.ANIO, ");
		stringBuilder.append(" EJG.NUMERO), ");
		stringBuilder.append(" '') AS NOTIFICAR_PROCURADOR_CONTRARIO, ");

		stringBuilder.append(" F_SIGA_GETUNIDADEJG(EJG.IDINSTITUCION, ");
		stringBuilder.append(" EJG.ANIO, ");
		stringBuilder.append(" EJG.NUMERO, ");
		stringBuilder.append(" EJG.IDTIPOEJG) AS TOTAL_SOLICITANTE, ");
		stringBuilder.append(" EJG.RATIFICACIONDICTAMEN, ");
		stringBuilder.append(" DECODE(EA.FECHARESOLUCION, ");
		stringBuilder.append(" NULL, ");
		stringBuilder.append(" EJG.IDTIPORATIFICACIONEJG, ");
		stringBuilder.append(" EJGACTA.IDTIPORATIFICACIONEJG) IDTIPORATIFICACIONEJG, ");
		stringBuilder.append(" DECODE(EA.FECHARESOLUCION, ");
		stringBuilder.append(" NULL, ");
		stringBuilder.append(" EJG.IDFUNDAMENTOJURIDICO, ");
		stringBuilder.append(" EJGACTA.IDFUNDAMENTOJURIDICO) IDFUNDAMENTOJURIDICO ");
		stringBuilder.append(" , (SELECT MAX(EE.FECHAINICIO) FROM SCS_ESTADOEJG EE WHERE EE.IDESTADOEJG = 11 AND EE.IDINSTITUCION = EJG.IDINSTITUCION ");
		stringBuilder.append(" AND EE.IDTIPOEJG = EJG.IDTIPOEJG AND EE.ANIO = EJG.ANIO AND EE.NUMERO = EJG.NUMERO AND EE.FECHABAJA IS NULL) FECHA_IMPUGNADO, ");

		stringBuilder.append(" EA.FECHARESOLUCION, EJG.FECHAAUTO, EJG.IDTIPORESOLAUTO,EJG.IDTIPOSENTIDOAUTO, ");
		stringBuilder.append(" EJG.ANIORESOLUCION, EJG.NUMERORESOLUCION, EJG.BISRESOLUCION, EJG.FECHAPUBLICACION, EJG.OBSERVACIONIMPUGNACION ");

		stringBuilder.append(" FROM SCS_EJG           EJG, ");
		stringBuilder.append(" SCS_ACTACOMISION  EA, ");
		stringBuilder.append(" SCS_EJG_ACTA      EJGACTA, ");
		stringBuilder.append(" SCS_TURNO         TUR, ");
		stringBuilder.append(" SCS_PERSONAJG     SOL, ");
		stringBuilder.append(" SCS_GUARDIASTURNO GUA, ");
		stringBuilder.append(" SCS_PONENTE        PON, ");
		stringBuilder.append(" SCS_TIPOEJGCOLEGIO TIP ");

		stringBuilder.append(" WHERE TUR.IDINSTITUCION(+) = EJG.IDINSTITUCION ");
		stringBuilder.append(" AND TUR.IDTURNO(+) = EJG.GUARDIATURNO_IDTURNO ");
		stringBuilder.append(" AND GUA.IDINSTITUCION(+) = EJG.IDINSTITUCION ");
		stringBuilder.append(" AND GUA.IDTURNO(+) = EJG.GUARDIATURNO_IDTURNO ");
		stringBuilder.append(" AND GUA.IDGUARDIA(+) = EJG.GUARDIATURNO_IDGUARDIA ");
		stringBuilder.append(" AND SOL.IDINSTITUCION(+) = EJG.IDINSTITUCION ");
		stringBuilder.append(" AND SOL.IDPERSONA(+) = EJG.IDPERSONAJG ");
		stringBuilder.append(" AND PON.IDPONENTE(+) = EJG.IDPONENTE ");
		stringBuilder.append(" AND PON.IDINSTITUCION(+) = EJG.IDINSTITUCIONPONENTE ");
		stringBuilder.append(" AND TIP.IDTIPOEJGCOLEGIO(+) = EJG.IDTIPOEJGCOLEGIO ");
		stringBuilder.append(" AND TIP.IDINSTITUCION(+) = EJG.IDINSTITUCION ");
		stringBuilder.append(" AND EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION ");
		stringBuilder.append(" AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG ");
		stringBuilder.append(" AND EJGACTA.ANIOEJG = EJG.ANIO ");
		stringBuilder.append(" AND EJGACTA.NUMEROEJG = EJG.NUMERO ");
		stringBuilder.append(" AND EJGACTA.IDINSTITUCIONACTA = EA.IDINSTITUCION ");
		stringBuilder.append(" AND EJGACTA.ANIOACTA = EA.ANIOACTA ");
		stringBuilder.append(" AND EJGACTA.IDACTA = EA.IDACTA ");

		stringBuilder.append(" AND EJGACTA.IDINSTITUCIONACTA =:");
		keyContador++;
		htCodigos.put(new Integer(keyContador), idInstitucion);
		stringBuilder.append(keyContador);

		stringBuilder.append(" AND EJGACTA.ANIOACTA = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), anioActa);
		stringBuilder.append(keyContador);

		stringBuilder.append(" AND EJGACTA.IDACTA =:");
		keyContador++;
		htCodigos.put(new Integer(keyContador), idActa);
		stringBuilder.append(keyContador);

		stringBuilder.append(" ) DATOS, ");
		stringBuilder.append(" SCS_TIPORESOLUCION RES, ");
		stringBuilder.append(" SCS_TIPOFUNDAMENTOS FUN ");
		stringBuilder.append(" WHERE RES.IDTIPORESOLUCION(+) = DATOS.IDTIPORATIFICACIONEJG ");
		stringBuilder.append(" AND FUN.IDFUNDAMENTO(+) = DATOS.IDFUNDAMENTOJURIDICO ");
		stringBuilder.append(" AND FUN.IDINSTITUCION(+) = DATOS.IDINSTITUCION ");
		stringBuilder.append(" ORDER BY ANIO ASC, NUMEJG ASC ");

		if (isImpugando != null) {
			if (isImpugando)
				stringBuilder.append(") WHERE ISIMPUGNADO = 1 ");
			else
				stringBuilder.append(") WHERE ISIMPUGNADO = 0 ");

		}

		try {

			Vector vector = this.selectGenericoBindHashVacio(stringBuilder.toString(), htCodigos);
			if (vector != null) {
				GenParametrosAdm paramAdm = new GenParametrosAdm(usrbean);
				CenInstitucionAdm cenInstitucionAdm = new CenInstitucionAdm(usrbean);
				Hashtable<String, String[]> etiquetasRepetidas = new Hashtable<String, String[]>();
				String[] etiquetas = null;
				for (int i = 0; i < vector.size(); i++) {

					Hashtable registro = (Hashtable) vector.get(i);
					if (registro != null && registro.get("IDINSTITUCION") != null && !registro.get("IDINSTITUCION").toString().trim().equals("")) {
						String idInstitucionEJG = (String) registro.get("IDINSTITUCION");
						if (!etiquetasRepetidas.containsKey(idInstitucionEJG)) {
							String prefijoExpedienteCajg = paramAdm.getValor(idInstitucionEJG, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
							Vector institucionVector = cenInstitucionAdm.selectByPK(registro);
							String abreviaturaColegio = ((CenInstitucionBean) institucionVector.get(0)).getAbreviatura();
							etiquetas = new String[] { prefijoExpedienteCajg, UtilidadesString.getPrimeraMayuscula(abreviaturaColegio) };
							etiquetasRepetidas.put(idInstitucionEJG, etiquetas);
						} else {
							etiquetas = etiquetasRepetidas.get(idInstitucionEJG);

						}
						registro.put("PREFIJO_EXPEDIENTES_CAJG", etiquetas[0]);
						registro.put("ABREVIATURA_COLEGIO", etiquetas[1]);
						datos.add(registro);
					} else {
						registro.put("PREFIJO_EXPEDIENTES_CAJG", "");
						registro.put("ABREVIATURA_COLEGIO", "");
						datos.add(registro);

					}

				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error ScsActaComisionAdm.getEJGsInforme.");
		}

		return datos;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @param longitudNumEjg 
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsPendientes(String idInstitucion, String idActa, String anioActa, String longitudNumEjg) throws ClsExceptions, SIGAException {
		Vector datos = new Vector();
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		String sql = "SELECT rownum as NACUERDO, DATOS.* FROM (" +
			"SELECT EJG." + ScsEJGBean.C_ANIO + " as ANIO, " +
				" lpad(EJG."+ScsEJGBean.C_NUMEJG + ","+longitudNumEjg+",0) as NUMERO, "+
				
				" TO_CHAR(EJG." + ScsEJGBean.C_FECHAAPERTURA + ", 'dd/mm/yyyy') as " + ScsEJGBean.C_FECHAAPERTURA + ", " +
				" TUR." + ScsTurnoBean.C_NOMBRE + " as TURNO, " + 
				" GUA." + ScsGuardiasTurnoBean.C_NOMBRE  + " as GUARDIA, " + 
				" SOL." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || SOL." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || SOL." + ScsPersonaJGBean.C_APELLIDO2 + " as SOLICITANTE, " +
				" EJG." + ScsEJGBean.C_IDINSTITUCION + " as IDINSTITUCION, " +
				" EJG." + ScsEJGBean.C_IDTIPOEJG + " as IDTIPOEJG, " +
				" EJG." + ScsEJGBean.C_RATIFICACIONDICTAMEN + " as DICTAMEN, " +
				" F_SIGA_GETRECURSO(FUN." + ScsTipoFundamentosBean.C_DESCRIPCION + ", " + this.usrbean.getLanguage() + ") as FUNDAMENTO, " +
				" F_SIGA_GETRECURSO(RES.descripcion, " + this.usrbean.getLanguage() + ") as RESOLUCION, " +
				" F_SIGA_GETRECURSO(PON." + ScsPonenteBean.C_NOMBRE + ", " + this.usrbean.getLanguage() + ") as PONENTE, " +
				" F_SIGA_GETRECURSO(TIP." + ScsTipoEJGColegioBean.C_DESCRIPCION + ", " + this.usrbean.getLanguage() + ") as TIPOEJG, " +
				" FUN.TEXTOPLANTILLA3, " +
				" FUN.TEXTOPLANTILLA4, " +
				" F_SIGA_GETUNIDADEJG(EJG." + ScsEJGBean.C_IDINSTITUCION + ", EJG." + ScsEJGBean.C_ANIO + ", EJG." + ScsEJGBean.C_NUMERO + ", EJG." + ScsEJGBean.C_IDTIPOEJG + ") AS TOTAL_SOLICITANTE, " +
				" EJG." + ScsEJGBean.C_RATIFICACIONDICTAMEN + 
			" FROM " + ScsEJGBean.T_NOMBRETABLA + " EJG, " +
				" SCS_EJG_ACTA      EJGACTA,"+
				ScsTurnoBean.T_NOMBRETABLA + " TUR, " + 
				ScsPersonaJGBean.T_NOMBRETABLA + " SOL, " +
				ScsGuardiasTurnoBean.T_NOMBRETABLA + " GUA, " +
				ScsTipoFundamentosBean.T_NOMBRETABLA + " FUN, " +
				ScsPonenteBean.T_NOMBRETABLA + " PON, " +
				ScsTipoEJGColegioBean.T_NOMBRETABLA + " TIP, " +
				" SCS_TIPORESOLUCION RES " +
			" WHERE TUR." + ScsTurnoBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION +
				" AND TUR." + ScsTurnoBean.C_IDTURNO+ "(+) = EJG." + ScsEJGBean.C_GUARDIATURNO_IDTURNO +
				" AND GUA." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION +
				" AND GUA." + ScsGuardiasTurnoBean.C_IDTURNO + "(+) = EJG." + ScsEJGBean.C_GUARDIATURNO_IDTURNO +
				" AND GUA." + ScsGuardiasTurnoBean.C_IDGUARDIA + "(+) = EJG." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA +
				" AND SOL." + ScsPersonaJGBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION +
				" AND SOL." + ScsPersonaJGBean.C_IDPERSONA + "(+) = EJG." + ScsEJGBean.C_IDPERSONAJG +
				" AND PON." + ScsPonenteBean.C_IDPONENTE + "(+) = EJG." + ScsEJGBean.C_IDPONENTE +
				" AND PON." + ScsPonenteBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCIONPONENTE +
				" AND EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG + " is null" + 
				" AND FUN." + ScsTipoFundamentosBean.C_IDFUNDAMENTO + "(+) = EJG." + ScsEJGBean.C_IDFUNDAMENTOJURIDICO +
				" AND FUN.IDINSTITUCION(+) = EJG." + ScsEJGBean.C_IDINSTITUCION + 
				" AND RES.idtiporesolucion(+) = EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG +
				" AND TIP." + ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO + "(+) = EJG." + ScsEJGBean.C_IDTIPOEJGCOLEGIO +
				" AND TIP." + ScsTipoEJGColegioBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION +
				" AND EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION "+
				" AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG "+
				" AND EJGACTA.ANIOEJG = EJG.ANIO "+
				" AND EJGACTA.NUMEROEJG = EJG.NUMERO ";
				keyContador++;
				htCodigos.put(new Integer(keyContador), idInstitucion);
				sql +=" AND EJGACTA.IDINSTITUCIONACTA =  :"+keyContador;
				keyContador++;
				htCodigos.put(new Integer(keyContador), anioActa);
				sql +=" AND EJGACTA.ANIOACTA =  :"+keyContador+"";
				keyContador++;
				htCodigos.put(new Integer(keyContador), idActa);
				sql +=" AND EJGACTA.IDACTA =  :"+keyContador+"";
				
				 
			sql +=" ORDER BY EJG." + ScsEJGBean.C_ANIO + " ASC , " + 
				" EJG." + ScsEJGBean.C_NUMERO + " ASC " + 
				") DATOS";		
		
       try{    	   	    	   			
			 Vector listaExp = this.selectGenericoBindHashVacio(sql,htCodigos);
			 if (listaExp!=null){
	 				GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
	 				CenInstitucionAdm cenInstitucionAdm = new CenInstitucionAdm (usrbean);
	 				Hashtable<String, String[]> etiquetasRepetidas =  new Hashtable<String, String[]>(); 
	 				String [] etiquetas = null;
					for (int i = 0; i < listaExp.size(); i++)	{
						Hashtable registro = (Hashtable)listaExp.get(i); 
						
						if (registro != null && registro.get("IDINSTITUCION")!=null && !registro.get("IDINSTITUCION").toString().trim().equals("") ){
							String idInstitucionEJG = (String) registro.get("IDINSTITUCION");
							if(!etiquetasRepetidas.containsKey(idInstitucionEJG)){
								String prefijoExpedienteCajg =  paramAdm.getValor (idInstitucionEJG, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
								Vector institucionVector =  cenInstitucionAdm.selectByPK(registro);
								String abreviaturaColegio =  ((CenInstitucionBean)institucionVector.get(0)).getAbreviatura();
								etiquetas = new String[]{prefijoExpedienteCajg,UtilidadesString.getPrimeraMayuscula(abreviaturaColegio)};
								etiquetasRepetidas.put(idInstitucionEJG, etiquetas);
							}else{
								etiquetas = etiquetasRepetidas.get(idInstitucionEJG);
								
							}
							registro.put("PREFIJO_EXPEDIENTES_CAJG", etiquetas[0]);
							registro.put("ABREVIATURA_COLEGIO", etiquetas[1]);
							datos.add(registro);
						}else{
							registro.put("PREFIJO_EXPEDIENTES_CAJG", "");
							registro.put("ABREVIATURA_COLEGIO", "");
							datos.add(registro);
							
						}
					}
				}		       
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsActaComisionAdm.getEJGsPendientes.");	
		} 
		
		return datos;			
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @param longitudNumEjg 
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsPendientesPonentes(String idInstitucion, String idActa, String anioActa, String longitudNumEjg) throws ClsExceptions, SIGAException {
		Vector datos = new Vector();
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		String sql = "SELECT F_SIGA_GETRECURSO(PON." + ScsPonenteBean.C_NOMBRE + ", 1) as PONENTE, " +
				" REPLACE(WM_CONCAT(EJG." + ScsEJGBean.C_ANIO + " || '/' || LPAD(EJG." + ScsEJGBean.C_NUMEJG + ","+longitudNumEjg+",0)), ',', ', ') as LISTAEJG " +
				" ,EJG." + ScsEJGBean.C_IDINSTITUCION +" " +
			" FROM " + ScsEJGBean.T_NOMBRETABLA + " EJG, " + 
				" SCS_EJG_ACTA      EJGACTA,"+
				ScsPonenteBean.T_NOMBRETABLA +" PON " +
			" WHERE PON." + ScsPonenteBean.C_IDPONENTE + "(+) = EJG." + ScsEJGBean.C_IDPONENTE + 
				" AND PON." + ScsPonenteBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCIONPONENTE + 
				" AND EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG + " is null " + 
				" AND EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION "+
				" AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG "+
				" AND EJGACTA.ANIOEJG = EJG.ANIO "+
				" AND EJGACTA.NUMEROEJG = EJG.NUMERO ";
				keyContador++;
				htCodigos.put(new Integer(keyContador), idInstitucion);
				sql +=" AND EJGACTA.IDINSTITUCIONACTA =  :"+keyContador;
				keyContador++;
				htCodigos.put(new Integer(keyContador), anioActa);
				sql +=" AND EJGACTA.ANIOACTA =  :"+keyContador+"";
				keyContador++;
				htCodigos.put(new Integer(keyContador), idActa);
				sql +=" AND EJGACTA.IDACTA =  :"+keyContador+"";
				
			
			sql +=" GROUP BY PON." + ScsPonenteBean.C_NOMBRE+" ,EJG." + ScsEJGBean.C_IDINSTITUCION;
		
		try{    	   	    	   			
			Vector lista = this.selectGenericoBindHashVacio(sql,htCodigos);
			if (lista!=null){
 				GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
 				CenInstitucionAdm cenInstitucionAdm = new CenInstitucionAdm (usrbean);
 				Hashtable<String, String[]> etiquetasRepetidas =  new Hashtable<String, String[]>(); 
 				String [] etiquetas = null;
				for (int i = 0; i < lista.size(); i++)	{
					Hashtable registro = (Hashtable)lista.get(i); 
					
					if (registro != null && registro.get("IDINSTITUCION")!=null && !registro.get("IDINSTITUCION").toString().trim().equals("") ){
						String idInstitucionEJG = (String) registro.get("IDINSTITUCION");
						if(!etiquetasRepetidas.containsKey(idInstitucionEJG)){
							String prefijoExpedienteCajg =  paramAdm.getValor (idInstitucionEJG, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
							Vector institucionVector =  cenInstitucionAdm.selectByPK(registro);
							String abreviaturaColegio =  ((CenInstitucionBean)institucionVector.get(0)).getAbreviatura();
							etiquetas = new String[]{prefijoExpedienteCajg,UtilidadesString.getPrimeraMayuscula(abreviaturaColegio)};
							etiquetasRepetidas.put(idInstitucionEJG, etiquetas);
						}else{
							etiquetas = etiquetasRepetidas.get(idInstitucionEJG);
							
						}
						registro.put("PREFIJO_EXPEDIENTES_CAJG", etiquetas[0]);
						registro.put("ABREVIATURA_COLEGIO", etiquetas[1]);
						datos.add(registro);
					}else{
						registro.put("PREFIJO_EXPEDIENTES_CAJG", "");
						registro.put("ABREVIATURA_COLEGIO", "");
						datos.add(registro);
						
					}
				}
			}		           
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsActaComisionAdm.getEJGsPendientesPonentes.");	
		} 
		
		return datos;			
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @param longitudNumEjg 
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsRetirados(int idInstitucion, int idActa, int anioActa, String longitudNumEjg) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		
		String sql=getConsultaEJGsRetirados(idInstitucion, idActa, anioActa,longitudNumEjg);
 		
		Vector datos = new Vector();
		try{    	   	    	   			
			rc = this.find(sql);
 			if (rc!=null){
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}		       
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsActaComisionAdm.getEJGsPendientesPonentes.");	
		} 
		
		return datos;			
	}
	
	public Vector updateEJGsRetirados(int idInstitucion, int idActa, int anioActa, String longitudNumejg) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		
		ScsEJGAdm ejgAdm = new ScsEJGAdm(this.usrbean);
		
		String sql=getConsultaEJGsRetirados(idInstitucion, idActa, anioActa,longitudNumejg);
		
 		
		Vector datos = new Vector<Hashtable>();
		try{    	   	    	   			
			rc = this.find(sql);
 			if (rc!=null){
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}		       
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los EJGs retirados del acta.");	
		} 
		
		StringBuffer update = new StringBuffer();
		StringBuffer where = new StringBuffer();
		
		update.append("update " + ScsEJGBean.T_NOMBRETABLA + " set ");
		update.append(" " + ScsEJGBean.C_ANIOACTA + " = null, ");
		update.append(" " + ScsEJGBean.C_IDINSTITUCIONACTA + " = null, ");
		update.append(" " + ScsEJGBean.C_IDACTA + " = null ");

		where.append(" where ");
		where.append(ScsEJGBean.C_ANIOACTA + " = " + anioActa);
		where.append(" and " + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + idInstitucion);
		where.append(" and " + ScsEJGBean.C_IDACTA + " = " + idActa);
		where.append(" and " + ScsEJGBean.C_IDTIPORATIFICACIONEJG + " in (4,6)");
		
		StringBuilder delete = new StringBuilder();
		delete.append(" DELETE FROM SCS_EJG_ACTA EJGACTA ");
		delete.append(" WHERE EJGACTA.IDINSTITUCIONACTA = ");
		delete.append(idInstitucion);
		delete.append(" AND EJGACTA.ANIOACTA = ");
		delete.append(anioActa);
		delete.append(" AND EJGACTA.IDACTA = ");
		delete.append(idActa);
		delete.append(" AND EXISTS (SELECT 1 ");
		delete.append(" FROM SCS_EJG EJG ");
		delete.append(" WHERE EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG ");
		delete.append(" AND EJGACTA.ANIOEJG = EJG.ANIO ");
		delete.append(" AND EJGACTA.NUMEROEJG = EJG.NUMERO ");
		delete.append(" AND EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION ");
		delete.append(" AND EJG.IDTIPORATIFICACIONEJG IN (4, 6)) ");
		
		try {
			ejgAdm.updateSQL(update.toString()+where.toString());
			ejgAdm.deleteSQL(delete.toString());
		} catch (ClsExceptions e) {
			datos=new Vector();
			throw new SIGAException("Error al procesar los EJGs retirados del acta.",e);
		}
		
		return datos;
	
	}
	
	
	private String getConsultaEJGsRetirados(int idInstitucion, int idActa, int anioActa,String longitudNumejg){
		String sql = "select " + 
			" ejg."+ScsEJGBean.C_ANIO+"," + 
			" ejg."+ScsEJGBean.C_IDINSTITUCION+"," +
			" ejg."+ScsEJGBean.C_IDTIPOEJG+"," +
			" ejg."+ScsEJGBean.C_NUMERO+"," +
			" lpad(ejg."+ScsEJGBean.C_NUMEJG+","+longitudNumejg+",0) "+ScsEJGBean.C_NUMEJG+"," +
			" ejg."+ScsEJGBean.C_IDTIPORATIFICACIONEJG +
			" from SCS_EJG_ACTA       EJGACTA,"+ScsEJGBean.T_NOMBRETABLA +" ejg" +
			" where " +
			" EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION " +
			" AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG " +
			" AND EJGACTA.ANIOEJG = EJG.ANIO " +
			" AND EJGACTA.NUMEROEJG = EJG.NUMERO " +
			" AND EJGACTA.IDINSTITUCIONACTA =  " +idInstitucion+
			" AND EJGACTA.ANIOACTA =  " +anioActa+
			" AND EJGACTA.IDACTA =  " +idActa+
			  
			" AND ejg."+ScsEJGBean.C_IDTIPORATIFICACIONEJG +" in (4,6) " +
			  
			" order by ejg."+ScsEJGBean.C_ANIO+" asc, ejg."+ScsEJGBean.C_NUMEJG;
		return sql;
	}
	
	
	/**
	 * Metodo que devuelve un String con loss Ejgs que son necesarios sacar de los actas para poder abrir otro acta
	 * Nos devolevera los expedientes que estan en el acta tratado y que esxisten en otros actas diferentes que estan abiertos
	 * @param actaBean
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	
	public String getExpedientesRepetidosEnActasAbiertas(ScsActaComisionBean actaBean, UsrBean usr) throws ClsExceptions {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT EJG.ANIO||'/'|| EJG.NUMEJG EXPEDIENTE,AC.ANIOACTA||'/'||AC.NUMEROACTA ACTA ");
		query.append(" FROM SCS_EJG_ACTA EAC, SCS_EJG EJG, SCS_ACTACOMISION AC WHERE ");
		query.append(" EAC.IDINSTITUCIONEJG = EJG.IDINSTITUCION ");
		query.append(" AND EAC.IDTIPOEJG = EJG.IDTIPOEJG");
		query.append(" AND EAC.ANIOEJG = EJG.ANIO ");
		query.append(" AND EAC.NUMEROEJG = EJG.NUMERO ");
		query.append(" AND EAC.IDINSTITUCIONACTA = AC.IDINSTITUCION ");
		query.append(" AND EAC.IDACTA = AC.IDACTA ");
		query.append(" AND EAC.ANIOACTA = AC.ANIOACTA ");
		
		query.append(" AND EAC.IDINSTITUCIONACTA = ");
		query.append(actaBean.getIdInstitucion());
		query.append(" AND EAC.ANIOACTA =  ");
		query.append(actaBean.getAnioActa());
		query.append(" AND EAC.IDACTA =  ");
		query.append(actaBean.getIdActa());
		query.append(" AND EXISTS (SELECT 1 ");
		query.append(" FROM SCS_EJG_ACTA EAC2, SCS_ACTACOMISION AC ");
		query.append(" WHERE AC.IDINSTITUCION = EAC2.IDINSTITUCIONACTA ");
		query.append(" AND AC.IDACTA = EAC2.IDACTA ");
		query.append(" AND AC.ANIOACTA = EAC2.ANIOACTA ");
		query.append(" AND AC.FECHARESOLUCION IS NULL ");
		query.append(" AND EAC2.IDINSTITUCIONACTA = ");
		query.append(actaBean.getIdInstitucion());
		query.append(" AND EAC2.IDINSTITUCIONACTA||EAC2.ANIOACTA||EAC2.IDACTA <> EAC.IDINSTITUCIONACTA||EAC.ANIOACTA||EAC.IDACTA ");
		query.append(" AND EAC.IDINSTITUCIONEJG = EAC2.IDINSTITUCIONEJG ");
		query.append(" AND EAC.IDTIPOEJG = EAC2.IDTIPOEJG ");
		query.append(" AND EAC.ANIOEJG = EAC2.ANIOEJG ");
		query.append(" AND EAC.NUMEROEJG = EAC2.NUMEROEJG ");
		query.append(" ) ");

		StringBuilder detalleEjgsPteRetirarActas = null;
		try {
			RowsContainer rc = this.find(query.toString());
			if (rc != null && rc.size() > 0) {
				detalleEjgsPteRetirarActas = new StringBuilder();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					detalleEjgsPteRetirarActas.append("Acta: ");
					detalleEjgsPteRetirarActas.append((String) fila.getString("ACTA"));
					detalleEjgsPteRetirarActas.append(" EJG: ");
					detalleEjgsPteRetirarActas.append((String) fila.getString("EXPEDIENTE"));
					detalleEjgsPteRetirarActas.append("\n ");
				}
			} else
				return null;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error ScsActaComisionAdm.getExpedientesRepetidosEnActasAbiertas.");
		}
		return detalleEjgsPteRetirarActas.toString();
	}
	
}