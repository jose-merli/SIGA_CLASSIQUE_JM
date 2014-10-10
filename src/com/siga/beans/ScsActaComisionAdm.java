/*
 * Created on 17/09/2008
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.general.SIGAException;
import com.siga.ws.PCAJGConstantes;

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
	 * Metodo que comprueba si los cambos de la query estan recogidos en java, as�
	 * si cambia una columna lanzar� un error. Este m�todo es solo de debug y luego puede
	 * comentarse su contenido si la versi�n est� estable
	 * @param vector
	 * @throws SIGAException
	 */
	private void compruebaCampos(Vector vector) throws SIGAException {
		if (vector != null && vector.size() > 0) {
			if (vector.get(0) instanceof Hashtable) {
				 
				Hashtable ht = (Hashtable)vector.get(0);
				Enumeration enumeration = ht.keys();
				while (enumeration.hasMoreElements()) {
					String key = (String) enumeration.nextElement();
					try {
						PCAJGConstantes.class.getField(key);
					} catch (Exception e) {
						throw new SIGAException("No se encuentra la columna " + key);
					}					
				}
			}
		}
	}
	
	/**
	 * Obtiene un vector de hastables con los datos de la query que se pase por parametro
	 * @param sql
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Vector getDatos(String sql) throws ClsExceptions, SIGAException {
		Vector datos = this.selectGenerico(sql);
		return datos;
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
	public Paginador getBusquedaActas(String idInstitucion, HashMap filtros) throws Exception{
		
		String anio, numero, fecharesolucion, fechareunion, idpresidente, idsecretario;
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
			consulta.append(" and act." + ScsActaComisionBean.C_NUMEROACTA + " = " + filtros.get(ScsActaComisionBean.C_NUMEROACTA).toString());
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
	
	public Vector<Hashtable<String, String>> getListadoEJGActa(String idActa, String anioActa, String idInstitucion) throws ClsExceptions {
		
		StringBuffer consulta = new StringBuffer();
		RowsContainer rc = new RowsContainer(); 
		Vector resultado = new Vector();
		consulta.append("select ");
		consulta.append(" ejg."+ScsEJGBean.C_ANIO + " as ANIO");
		consulta.append(", ejg."+ScsEJGBean.C_NUMEJG + " as NUMERO");
		consulta.append(", ejg."+ScsEJGBean.C_FECHAAPERTURA + " as FECHAAPERTURA");
		consulta.append(", tur."+ScsTurnoBean.C_NOMBRE + " as TURNO");
		consulta.append(", gua."+ScsGuardiasTurnoBean.C_NOMBRE  + " as GUARDIA");
		consulta.append(", sol."+ScsPersonaJGBean.C_NOMBRE+"||' '||sol."+ScsPersonaJGBean.C_APELLIDO1+"||' '||sol."+ScsPersonaJGBean.C_APELLIDO2 + " as SOLICITANTE ");
		consulta.append(", ejg."+ScsEJGBean.C_NUMERO + " as NUM");
		consulta.append(", ejg."+ScsEJGBean.C_IDINSTITUCION + " as IDINSTITUCION");
		consulta.append(", ejg."+ScsEJGBean.C_IDTIPOEJG + " as IDTIPOEJG");
		consulta.append(", ejg.IDTIPORATIFICACIONEJG,ejg.IDFUNDAMENTOJURIDICO ");
		consulta.append(", f_siga_getrecurso(r.descripcion,"+usrbean.getLanguage()+") resolucion ");
		
		consulta.append(" from " + ScsEJGBean.T_NOMBRETABLA +" ejg");
		consulta.append(" , " + ScsTurnoBean.T_NOMBRETABLA +" tur");
		consulta.append(" , " + ScsPersonaJGBean.T_NOMBRETABLA +" sol");
		consulta.append(" , " + ScsGuardiasTurnoBean.T_NOMBRETABLA +" gua");
		consulta.append(", scs_tiporesolucion r");

		consulta.append(" where ejg." + ScsEJGBean.C_IDINSTITUCION + " = " + idInstitucion);
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
		RowsContainer rc = new RowsContainer(); 
		Hashtable resultado = new Hashtable();
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
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsInforme(String idInstitucion, String idActa, String anioActa) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		
		String sql = "SELECT rownum as NACUERDO, DATOS.* FROM (" +
			"SELECT EJG." + ScsEJGBean.C_ANIO + " as ANIO, " +
				" EJG." + ScsEJGBean.C_NUMEJG + " as NUMERO, " +
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
				" DECODE(EJG." + ScsEJGBean.C_REQUIERENOTIFICARPROC + ", '1', F_SIGA_GETRECURSO_ETIQUETA('gratuita.operarRatificacion.mensaje.requiereNotificarProc', " + this.usrbean.getLanguage() + ") || " +
					" F_SIGA_GETPROCURADORCONTR_EJG(EJG." + ScsEJGBean.C_IDINSTITUCION + ", EJG." + ScsEJGBean.C_IDTIPOEJG + ", EJG." + ScsEJGBean.C_ANIO + ", EJG." + ScsEJGBean.C_NUMERO + "), '') as NOTIFICAR_PROCURADOR_CONTRARIO, " +
				" FUN.TEXTOPLANTILLA3, " +
				" FUN.TEXTOPLANTILLA4, " +
				" F_SIGA_GETUNIDADEJG(EJG." + ScsEJGBean.C_IDINSTITUCION + ", EJG." + ScsEJGBean.C_ANIO + ", EJG." + ScsEJGBean.C_NUMERO + ", EJG." + ScsEJGBean.C_IDTIPOEJG + ") AS TOTAL_SOLICITANTE, " +
				" EJG." + ScsEJGBean.C_RATIFICACIONDICTAMEN + 
			" FROM " + ScsEJGBean.T_NOMBRETABLA + " EJG, " +
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
				" AND PON." + ScsPonenteBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION +
				" AND EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG + " <> 0" + 
				" AND FUN." + ScsTipoFundamentosBean.C_IDFUNDAMENTO + "(+) = EJG." + ScsEJGBean.C_IDFUNDAMENTOJURIDICO +
				" AND FUN.IDINSTITUCION(+) = EJG." + ScsEJGBean.C_IDINSTITUCION + 
				" AND RES.idtiporesolucion(+) = EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG +
				" AND TIP." + ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO + "(+) = EJG." + ScsEJGBean.C_IDTIPOEJGCOLEGIO +
				" AND TIP." + ScsTipoEJGColegioBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION + 
				" AND EJG." + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + idInstitucion +
				" AND EJG." + ScsEJGBean.C_ANIOACTA + " = " + anioActa +
				" AND EJG." + ScsEJGBean.C_IDACTA + " = " + idActa +  
			" ORDER BY EJG." + ScsEJGBean.C_ANIO + " ASC , " + 
				" EJG." + ScsEJGBean.C_NUMEJG + " ASC " + 
			") DATOS";
		
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
			throw new ClsExceptions (e, "Error ScsActaComisionAdm.getEJGsInforme.");	
		} 
		
		return datos;			
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsPendientes(String idInstitucion, String idActa, String anioActa) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		
		String sql = "SELECT rownum as NACUERDO, DATOS.* FROM (" +
			"SELECT EJG." + ScsEJGBean.C_ANIO + " as ANIO, " +
				" EJG." + ScsEJGBean.C_NUMEJG + " as NUMERO, " +
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
				" AND PON." + ScsPonenteBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION +
				" AND EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG + " = 0 " + 
				" AND FUN." + ScsTipoFundamentosBean.C_IDFUNDAMENTO + "(+) = EJG." + ScsEJGBean.C_IDFUNDAMENTOJURIDICO +
				" AND FUN.IDINSTITUCION(+) = EJG." + ScsEJGBean.C_IDINSTITUCION + 
				" AND RES.idtiporesolucion(+) = EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG +
				" AND TIP." + ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO + "(+) = EJG." + ScsEJGBean.C_IDTIPOEJGCOLEGIO +
				" AND TIP." + ScsTipoEJGColegioBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION + 
				" AND EJG." + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + idInstitucion +
				" AND EJG." + ScsEJGBean.C_ANIOACTA + " = " + anioActa +
				" AND EJG." + ScsEJGBean.C_IDACTA + " = " + idActa +  
			" ORDER BY EJG." + ScsEJGBean.C_ANIO + " ASC , " + 
				" EJG." + ScsEJGBean.C_NUMERO + " ASC " + 
			") DATOS";		
		
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
			throw new ClsExceptions (e, "Error ScsActaComisionAdm.getEJGsPendientes.");	
		} 
		
		return datos;			
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsPendientesPonentes(String idInstitucion, String idActa, String anioActa) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		
		String sql = "SELECT F_SIGA_GETRECURSO(PON." + ScsPonenteBean.C_NOMBRE + ", 1) as PONENTE, " +
				" REPLACE(WM_CONCAT(EJG." + ScsEJGBean.C_ANIO + " || '/' || EJG." + ScsEJGBean.C_NUMEJG + "), ',', ', ') as LISTAEJG " +
			" FROM " + ScsEJGBean.T_NOMBRETABLA + " EJG, " + 
				ScsPonenteBean.T_NOMBRETABLA +" PON " +
			" WHERE PON." + ScsPonenteBean.C_IDPONENTE + "(+) = EJG." + ScsEJGBean.C_IDPONENTE + 
				" AND PON." + ScsPonenteBean.C_IDINSTITUCION + "(+) = EJG." + ScsEJGBean.C_IDINSTITUCION + 
				" AND EJG." + ScsEJGBean.C_IDTIPORATIFICACIONEJG + " = 0 " + 
				" AND EJG." + ScsEJGBean.C_IDINSTITUCIONACTA + " = " + idInstitucion +
				" AND EJG." + ScsEJGBean.C_ANIOACTA + " = " + anioActa +
				" AND EJG." + ScsEJGBean.C_IDACTA + " = " + idActa +
			" GROUP BY PON." + ScsPonenteBean.C_NOMBRE;
		
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
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idActa
	 * @param anioActa
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	public Vector getEJGsRetirados(int idInstitucion, int idActa, int anioActa) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		Vector ejgPendientes = new Vector();
		
		String sql=getConsultaEJGsRetirados(idInstitucion, idActa, anioActa);
 		
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
	
	public Vector updateEJGsRetirados(int idInstitucion, int idActa, int anioActa) throws ClsExceptions, SIGAException {
		RowsContainer rc = new RowsContainer();
		Vector ejgPendientes = new Vector();
		
		ScsEJGAdm ejgAdm = new ScsEJGAdm(this.usrbean);
		
		String sql=getConsultaEJGsRetirados(idInstitucion, idActa, anioActa);
		
 		
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
		
		
		try {
			ejgAdm.updateSQL(update.toString()+where.toString());
		} catch (ClsExceptions e) {
			datos=new Vector();
			throw new SIGAException("Error al procesar los EJGs retirados del acta.",e);
		}
		
		return datos;
	
	}
	
	
	private String getConsultaEJGsRetirados(int idInstitucion, int idActa, int anioActa){
		String sql = "select " + 
		  " ejg."+ScsEJGBean.C_ANIO+"," + 
		  " ejg."+ScsEJGBean.C_IDINSTITUCION+"," +
		  " ejg."+ScsEJGBean.C_IDTIPOEJG+"," +
		  " ejg."+ScsEJGBean.C_NUMERO+"," +
		  " ejg."+ScsEJGBean.C_NUMEJG+"," +
		  " ejg."+ScsEJGBean.C_IDTIPORATIFICACIONEJG +
		  " from "+ScsEJGBean.T_NOMBRETABLA +" ejg" +
		  " where ejg."+ScsEJGBean.C_IDTIPORATIFICACIONEJG +" in (4,6) " +
		  " and ejg."+ScsEJGBean.C_IDINSTITUCIONACTA+"="+ idInstitucion+
		  " and ejg."+ScsEJGBean.C_ANIOACTA+"="+ anioActa+
		  " and ejg."+ScsEJGBean.C_IDACTA+"="+idActa+
		  " order by ejg."+ScsEJGBean.C_ANIO+" asc, ejg."+ScsEJGBean.C_NUMEJG;
		return sql;
	}
}