/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.consultas.CriterioDinamico;
import com.siga.consultas.form.RecuperarConsultasForm;
import com.siga.general.SIGAException;

/**
 * Administrador del Bean de Consultas 
 */
public class ConConsultaAdm extends MasterBeanAdministrador {
	
	//	Constantes	
	static public final String CONS_GENERAL_SI="S";
	static public final String CONS_GENERAL_NO="N";
	static public final String CONS_Y="AND";
	static public final String CONS_O="OR";
	static public final String CONS_NO="NOT";
	static public final String TIPO_CONSULTA_GEN="C";
	static public final String TIPO_CONSULTA_PYS="P";
	static public final String TIPO_CONSULTA_ENV="E";
	static public final String TIPO_CONSULTA_FAC="F";
	static public final String VISIBILIDAD_CONSULTA_PYS="P";
	
	
	public ConConsultaAdm(UsrBean usuario)
	{
	    super(ConConsultaBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConConsultaBean.C_IDINSTITUCION,
				ConConsultaBean.C_IDCONSULTA,
				ConConsultaBean.C_DESCRIPCION,
				ConConsultaBean.C_GENERAL,
				ConConsultaBean.C_TIPOCONSULTA,
				ConConsultaBean.C_SENTENCIA,
				ConConsultaBean.C_BASES,
				ConConsultaBean.C_IDTABLA,
				ConConsultaBean.C_IDMODULO,
				ConConsultaBean.C_FECHAMODIFICACION,
				ConConsultaBean.C_USUMODIFICACION,
				ConConsultaBean.C_ESEXPERTA};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConConsultaBean.C_IDINSTITUCION,
				ConConsultaBean.C_IDCONSULTA};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ConConsultaBean bean = null;

		try
		{
			bean = new ConConsultaBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConConsultaBean.C_IDINSTITUCION));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, ConConsultaBean.C_IDCONSULTA));
			bean.setDescripcion(UtilidadesHash.getString(hash, ConConsultaBean.C_DESCRIPCION));
			bean.setGeneral(UtilidadesHash.getString(hash, ConConsultaBean.C_GENERAL));
			bean.setTipoConsulta(UtilidadesHash.getString(hash, ConConsultaBean.C_TIPOCONSULTA));
			bean.setSentencia(UtilidadesHash.getString(hash, ConConsultaBean.C_SENTENCIA));
			bean.setBases(UtilidadesHash.getString(hash, ConConsultaBean.C_BASES));
			bean.setIdTabla(UtilidadesHash.getInteger(hash, ConConsultaBean.C_IDTABLA));
			bean.setIdModulo(UtilidadesHash.getInteger(hash, ConConsultaBean.C_IDMODULO));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ConConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ConConsultaBean.C_USUMODIFICACION));
			bean.setEsExperta(UtilidadesHash.getString(hash, ConConsultaBean.C_ESEXPERTA));
		
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;

	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ConConsultaBean b = (ConConsultaBean) bean;

			UtilidadesHash.set(htData, ConConsultaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConConsultaBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConConsultaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ConConsultaBean.C_GENERAL, b.getGeneral());
			UtilidadesHash.set(htData, ConConsultaBean.C_TIPOCONSULTA, b.getTipoConsulta());
			UtilidadesHash.set(htData, ConConsultaBean.C_ESEXPERTA, b.getEsExperta());
			UtilidadesHash.set(htData, ConConsultaBean.C_SENTENCIA, b.getSentencia());
			UtilidadesHash.set(htData, ConConsultaBean.C_BASES, b.getBases());
			UtilidadesHash.set(htData, ConConsultaBean.C_IDTABLA, b.getIdTabla());
			UtilidadesHash.set(htData, ConConsultaBean.C_IDMODULO, b.getIdModulo());
			UtilidadesHash.set(htData, ConConsultaBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ConConsultaBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion getNewIdConsulta (String idInstitucion)
	 * Genera el id de una nueva Consulta
	 * @param String idInstitucion
	 * @return nuevo idConsulta
	 * */
	public Integer getNewIdConsulta(String idInstitucion) throws ClsExceptions 
	{		
		int nuevoIdConsulta = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ConConsultaBean.C_IDCONSULTA+") AS ULTIMACONSULTA ";
	        
			sql += " FROM "+ConConsultaBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ConConsultaBean.C_IDINSTITUCION+" = "+idInstitucion;			
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMACONSULTA")).equals("")) {
					Integer ultimaConsultaInt = Integer.valueOf((String)htRow.get("ULTIMACONSULTA"));
					int ultimaConsulta=ultimaConsultaInt.intValue();
					ultimaConsulta++;
					nuevoIdConsulta = ultimaConsulta;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdConsulta);
	}
	
	
	/** Funcion selectBusqConsultas (UsrBean user, RecuperarConsultasForm form)
	 * Búsqueda sobre la tabla de consultas, para 'recuperar consultas' sobre las que se tiene permiso
	 * @param user
	 * @param form para acotar la busqueda
	 * @return vector con los registros encontrados.  
	 * @exception ClsExceptions 
	 * */
	public Vector selectBusqConsultas(UsrBean user, RecuperarConsultasForm form) throws ClsExceptions 
	{
		
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String subSelectPerfiles="";
	        String [] profiles = user.getProfile();
	        for (int i=0; i<profiles.length; i++){
	        	subSelectPerfiles+="'"+profiles[i]+"',";
	        }
	        subSelectPerfiles=subSelectPerfiles.substring(0,subSelectPerfiles.length()-1);
	        
	        String sql = "SELECT distinct ";
	        
		    sql += "CO."+ConConsultaBean.C_DESCRIPCION+", ";	
		    sql += "CO."+ConConsultaBean.C_GENERAL+", ";
		    sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+", ";
		    sql += "M."+ConModuloBean.C_NOMBRE+", ";	
		    sql += "CO."+ConConsultaBean.C_IDINSTITUCION+", ";	
		    sql += "CO."+ConConsultaBean.C_IDCONSULTA+", ";
		    sql += "CO."+ConConsultaBean.C_ESEXPERTA;
		    
			sql += " FROM ";
		    sql += ConConsultaBean.T_NOMBRETABLA+" CO, "+ConConsultaPerfilBean.T_NOMBRETABLA+" CP, "+ConModuloBean.T_NOMBRETABLA+" M";
		    		    		
			sql += " WHERE ";
			sql += "(CO."+ConConsultaBean.C_IDINSTITUCION+"="+user.getLocation()+ " OR CO."+ConConsultaBean.C_GENERAL+"='"+CONS_GENERAL_SI+"') AND ";
			
			//Cuando llega desde "Recuperar Consultas" el tipoConsulta es siempre "C"
			//pero se deben recuperar también las consultas expertas cuyo tipoConsulta no sea ni 
			//Facturación ni Envío a Grupos
			//si es una consulta normal de tipo general 
			if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){	
				sql += "(CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+ConConsultaAdm.TIPO_CONSULTA_GEN+"' OR ";
				sql += "(CO."+ConConsultaBean.C_ESEXPERTA+" = '1' AND ";
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" != '"+ConConsultaAdm.TIPO_CONSULTA_ENV+"' AND ";
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" != '"+ConConsultaAdm.TIPO_CONSULTA_FAC+"')) AND ";
			//si es una consulta de listas dinamicas de tipo "E" o "F"
			}else if (!form.getTipoConsulta().equals("")){	
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+form.getTipoConsulta()+"' AND ";
			}
			//si es una consulta de listas dinamicas sin tipoConsulta indicado se
			//recuperan todas las consultas de listas dinámicas y expertas de tipo "E" o "F"
			else{
				sql += "(CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+ConConsultaAdm.TIPO_CONSULTA_FAC+"' OR ";
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+ConConsultaAdm.TIPO_CONSULTA_ENV+"') AND ";
			}
			
			sql += "CO."+ConConsultaBean.C_IDINSTITUCION+" = CP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA+" AND ";
			sql += "CO."+ConConsultaBean.C_IDCONSULTA+" = CP."+ConConsultaPerfilBean.C_IDCONSULTA+" AND ";
			sql += "CP."+ConConsultaPerfilBean.C_IDINSTITUCION+" = "+user.getLocation()+" AND ";
			sql += "CP."+ConConsultaPerfilBean.C_IDPERFIL+" IN ("+subSelectPerfiles+") AND ";
			sql += "CO."+ConConsultaBean.C_IDMODULO+" = M."+ConModuloBean.C_IDMODULO;
			
			
			sql += (form.getDescripcion()!=null && !form.getDescripcion().equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(form.getDescripcion().trim(),"CO." + ConConsultaBean.C_DESCRIPCION ) : "";
			sql += (form.getIdModulo()!=null && !form.getIdModulo().equals("")) ? " AND CO." + ConConsultaBean.C_IDMODULO + " = " + form.getIdModulo() : "";
			
			sql += " ORDER BY 4,1";		
			
			ClsLogging.writeFileLog("ConConsultaAdm, sql: "+sql,3);
			
			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	/** Funcion selectTodasConsultas (UsrBean user, RecuperarConsultasForm form)
	 * Búsqueda sobre la tabla de consultas, para 'recuperar consultas' con o sin permiso
	 * @param user
	 * @param form para acotar la busqueda
	 * @return vector con los registros encontrados.  
	 * @exception ClsExceptions 
	 * */
	public Vector selectTodasConsultas(UsrBean user, RecuperarConsultasForm form) throws ClsExceptions 
	{
		
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String subSelectPerfiles="";
	        String [] profiles = user.getProfile();
	        for (int i=0; i<profiles.length; i++){
	        	subSelectPerfiles+="'"+profiles[i]+"',";
	        }
	        subSelectPerfiles=subSelectPerfiles.substring(0,subSelectPerfiles.length()-1);
	        
	        String sql = "SELECT distinct ";
	        	        
		    sql += "CO."+ConConsultaBean.C_DESCRIPCION+", ";	
		    sql += "CO."+ConConsultaBean.C_GENERAL+", ";
		    sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+", ";
		    sql += "M."+ConModuloBean.C_NOMBRE+", ";	
		    sql += "CO."+ConConsultaBean.C_IDINSTITUCION+", ";	
		    sql += "CO."+ConConsultaBean.C_IDCONSULTA+", ";	
		    sql += "CO."+ConConsultaBean.C_ESEXPERTA+", ";	
		    sql += "DECODE ((SELECT COUNT(CP."+ConConsultaPerfilBean.C_IDPERFIL+") FROM "+ConConsultaPerfilBean.T_NOMBRETABLA+" CP ";
		    sql +=          "WHERE CP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA+" = CO."+ConConsultaBean.C_IDINSTITUCION+" AND ";
		    sql +=			"CP."+ConConsultaPerfilBean.C_IDCONSULTA+" = CO."+ConConsultaBean.C_IDCONSULTA+" AND ";
		    sql +=			"CP."+ConConsultaPerfilBean.C_IDINSTITUCION+" = "+user.getLocation()+" AND ";
		    sql +=          "CP."+ConConsultaPerfilBean.C_IDPERFIL+" IN ("+subSelectPerfiles+")),0,'N','S') AS PERMISOEJECUCION ";
		    
			sql += "FROM ";
		    sql += ConConsultaBean.T_NOMBRETABLA+" CO, "+ConModuloBean.T_NOMBRETABLA+" M";
		    		    		
			sql += " WHERE ";
			sql += "(CO."+ConConsultaBean.C_IDINSTITUCION+"="+user.getLocation()+ " OR CO."+ConConsultaBean.C_GENERAL+"='"+CONS_GENERAL_SI+"') AND ";	
			
			//Cuando llega desde "Recuperar Consultas" el tipoConsulta es siempre "C"
			//pero se deben recuperar también las consultas expertas cuyo tipoConsulta no sea ni 
			//Facturación ni Envío a Grupos
			//si es una consulta normal de tipo general 
			if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){	
				sql += "(CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+ConConsultaAdm.TIPO_CONSULTA_GEN+"' OR ";
				sql += "(CO."+ConConsultaBean.C_ESEXPERTA+" = '1' AND ";
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" != '"+ConConsultaAdm.TIPO_CONSULTA_ENV+"' AND ";
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" != '"+ConConsultaAdm.TIPO_CONSULTA_FAC+"')) AND ";
			//si es una consulta de listas dinamicas de tipo "E" o "F"
			}else if (!form.getTipoConsulta().equals("")){	
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+form.getTipoConsulta()+"' AND ";
			}
			//si es una consulta de listas dinamicas sin tipoConsulta indicado se
			//recuperan todas las consultas de listas dinámicas y expertas de tipo "E" o "F"
			else{
				sql += "(CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+ConConsultaAdm.TIPO_CONSULTA_FAC+"' OR ";
				sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+ConConsultaAdm.TIPO_CONSULTA_ENV+"') AND ";
			}
			
			sql += "CO."+ConConsultaBean.C_IDMODULO+" = M."+ConModuloBean.C_IDMODULO;
			
			sql += (form.getDescripcion()!=null && !form.getDescripcion().equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(form.getDescripcion().trim(),"CO." + ConConsultaBean.C_DESCRIPCION ) : "";
			sql += (form.getIdModulo()!=null && !form.getIdModulo().equals("")) ? " AND CO." + ConConsultaBean.C_IDMODULO + " = " + form.getIdModulo() : "";
			
			sql += " ORDER BY 4,1";		
			
			ClsLogging.writeFileLog("ConConsultaAdm, sql: "+sql,3);
			
			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}


	
	/** Funcion obtenerPermisosGrupos (String idconsulta, String idinstitucion_consulta, String idinstitucion)
	 * Búsqueda sobre la tabla de perfiles para recuperar los permisos sobre una consulta
	 * @param idconsulta
	 * @param idinstitucion_consulta
	 * @param idinstitucion
	 * @return vector con los registros encontrados.  
	 * @exception ClsExceptions 
	 * */
	public Vector obtenerPermisosGrupos(String idconsulta, String idinstitucion_consulta, String idinstitucion) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 				        
	        
	        String sql = "SELECT ";
	        
		    sql += "PER."+AdmPerfilBean.C_IDPERFIL+", ";	
		    sql += "PER."+AdmPerfilBean.C_DESCRIPCION+", ";
		    sql += "nvl((SELECT 'S' FROM "+ConConsultaPerfilBean.T_NOMBRETABLA+" CP ";
		    sql += "WHERE CP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA+"="+idinstitucion_consulta+" AND ";	
		    sql += "CP."+ConConsultaPerfilBean.C_IDCONSULTA+"="+idconsulta+" AND ";	
		    sql += "CP."+ConConsultaPerfilBean.C_IDINSTITUCION+"=PER."+AdmPerfilBean.C_IDINSTITUCION+" AND ";
		    sql += "CP."+ConConsultaPerfilBean.C_IDPERFIL+"=PER."+AdmPerfilBean.C_IDPERFIL+"),'N') AS ACCESO ";
		    sql += " FROM "+AdmPerfilBean.T_NOMBRETABLA+ " PER WHERE ";
		    sql += AdmPerfilBean.C_IDINSTITUCION+"="+idinstitucion;
		    
			
			ClsLogging.writeFileLog("ConConsultaAdm, sql: "+sql,3);
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	
	
	/** Funcion selectConsultasListasDinamicas (String idInstitucion, String idListaCorreo)
	 * Búsqueda sobre la tabla de consultas, para 'recuperar consultas' asociadas a una lista de correos dinámica.
	 * @param idInstitucion
	 * @param idListaCorreo
	 * @return vector con los registros encontrados.  
	 * @exception ClsExceptions 
	 * */
	public Vector selectConsultasListasDinamicas (String idInstitucion, String idListaCorreo, String[] aPerfiles) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try {
			// Ya se anyaden los parentesis al construir la consulta:
			//es necesario quitarlos de aqui para que no de error a veces
//			String sPerfiles="('";
			String sPerfiles="'";
			
			for (int i=0; i<aPerfiles.length; i++)
			{
				sPerfiles += aPerfiles[i] + "','";
			}
			
			sPerfiles=sPerfiles.substring(0, sPerfiles.length()-2);
			// Ya se anyaden los parentesis al construir la consulta:
			//es necesario quitarlos de aqui para que no de error a veces
//			sPerfiles+=")";

			rc = new RowsContainer(); 
			
			//LMS 25/08/2006
			//Cambio para relacionar con CON_CONSULTAPERFIL
	        String sql = "SELECT DISTINCT ";
	        	        
	        sql += "CO."+ConConsultaBean.C_IDINSTITUCION+", ";	
		    sql += "CO."+ConConsultaBean.C_IDCONSULTA+", ";
		    sql += "CO."+ConConsultaBean.C_DESCRIPCION+", ";
		    sql += "CO."+ConConsultaBean.C_ESEXPERTA+" ";
		    
			sql += " FROM ";
		    sql += ConConsultaBean.T_NOMBRETABLA+" CO, ";
		    sql += ConConsultaPerfilBean.T_NOMBRETABLA+" CP ";
		    		    		
			sql += " WHERE ";
			sql += "(CO."+ConConsultaBean.C_IDINSTITUCION+"="+idInstitucion+" OR ";
			sql += "CO."+ConConsultaBean.C_GENERAL+"='S') AND ";
			sql += "CO."+ConConsultaBean.C_TIPOCONSULTA+" = '"+TIPO_CONSULTA_ENV+"' AND ";
			sql += "CO."+ConConsultaBean.C_IDCONSULTA+" || CP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA;
			sql += " NOT IN (SELECT LI."+EnvListaCorreoConsultaBean.C_IDCONSULTA +" || LI."+EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON;
			sql += " FROM "+EnvListaCorreoConsultaBean.T_NOMBRETABLA+" LI WHERE ";
			sql += "LI."+EnvListaCorreoConsultaBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";
			sql += "LI."+EnvListaCorreoConsultaBean.C_IDLISTACORREO+" = "+idListaCorreo+")";
			
			sql += " AND CO."+ConConsultaBean.C_IDINSTITUCION+"=CP."+ConConsultaPerfilBean.C_IDINSTITUCION;
			sql += " AND CO."+ConConsultaBean.C_IDCONSULTA+"=CP."+ConConsultaPerfilBean.C_IDCONSULTA+" AND";
			
			sql += " CP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA+" || CP."+ConConsultaPerfilBean.C_IDCONSULTA;
			sql += " IN (SELECT CPP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA+" || CPP."+ConConsultaPerfilBean.C_IDCONSULTA;
			sql += " FROM "+ConConsultaPerfilBean.T_NOMBRETABLA+" CPP WHERE ";
			sql += "CPP."+ConConsultaPerfilBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";
			sql += "CPP."+ConConsultaPerfilBean.C_IDPERFIL+" IN ("+sPerfiles+"))";

			sql += " ORDER BY CO."+ConConsultaBean.C_DESCRIPCION;
			
			ClsLogging.writeFileLog("ConConsultaAdm, sql: "+sql,3);
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	
	/**
	 * 
	 * @param tipoEnvio
	 * @param conBean
	 * @param cDinamicos
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public Hashtable procesarEjecutarConsulta(
			String tipoEnvio, ConConsultaBean conBean, CriterioDinamico[] cDinamicos, boolean sustituyeInstitucion) 
	throws SIGAException, ClsExceptions {

		String sentencia = conBean.getSentencia();
		boolean esExperta = conBean.getEsExperta().equals("1");
		String tipoConsulta = conBean.getTipoConsulta();
		String idinstitucion = Integer.toString(conBean.getIdInstitucion().intValue());
		String idConsulta = Integer.toString(conBean.getIdConsulta().intValue());
		
		
		//Variables para crear consulta parametrizada BIND
		int iParametroBind = 0;
		Hashtable codigosBind = new Hashtable();
		
		String sentenciaCabecera = "";
		//separando la cabecera y quitando etiquetas si es Experta
		if (esExperta){
			sentencia = sentencia.toUpperCase();
			sentenciaCabecera = sentencia.substring (
					sentencia.indexOf (ClsConstants.ETIQUETASELECTOPEN),
					sentencia.indexOf (ClsConstants.ETIQUETASELECTCLOSE));
			sentenciaCabecera = eliminarEtiquetas (sentenciaCabecera);
			//Si es de tipo Envio, se añade el filtro por direccion
			if (tipoConsulta.equals (ConConsultaAdm.TIPO_CONSULTA_ENV)){
				String filtroDireccion = " AND CEN_DIRECCIONES.IDDIRECCION=F_SIGA_GETDIRECCION(CEN_CLIENTE.IDINSTITUCION,CEN_CLIENTE.IDPERSONA,"+tipoEnvio+")";
				sentencia = sentencia.replaceFirst(ClsConstants.ETIQUETASWHERECLOSE,filtroDireccion+ClsConstants.ETIQUETASWHERECLOSE);
			}
			sentencia = eliminarEtiquetas (sentencia);
		}
		
		//sustituyendo la marca '%%idinstitucion%%' por la institucion actual
		//cuando no se trate de una consulta experta de facturacion
		while (sustituyeInstitucion && sentencia.toUpperCase().indexOf ("%%IDINSTITUCION%%") > -1) {
			iParametroBind++;
			sentencia = UtilidadesString.replaceFirstIgnoreCase(sentencia,"%%IDINSTITUCION%%", ":@"+iParametroBind+"@:");
			codigosBind.put (new Integer(iParametroBind), usrbean.getLocation());
			//sentencia = sentencia.replaceFirst("%%IDINSTITUCION%%", idinstitucion );
		}
		//sustituyendo la marca '%%idioma%%' por el idioma del USERBEAN
		sentencia = sentencia.replaceAll ("%%IDIOMA%%", usrbean.getLanguage());
		
		String[] cabeceras = null;
		if (tipoConsulta.equals (ConConsultaAdm.TIPO_CONSULTA_GEN) || esExperta){

		 //consulta general
			
			//obteniendo las cabeceras
			ConCamposSalidaAdm csAdm = new ConCamposSalidaAdm (usrbean);
			Vector v = new Vector ();
			if (! esExperta) {
				v = csAdm.getCamposSalida (idinstitucion, idConsulta);
				
				if (! v.isEmpty ()) { //consulta generica
					cabeceras = new String[v.size()];
					for (int i=0; i<v.size(); i++) {
						Row fila = (Row) v.elementAt (i);
						cabeceras[i] = fila.getString
								(ConCamposSalidaBean.C_CABECERA).toUpperCase();
					}
				}
			}
			else {
				cabeceras = sacarCabeceras (sentenciaCabecera);
			}
			
			//obteniendo los criterios si existen
			if (sentencia.indexOf (ConCriterioConsultaAdm.CONS_CRITERIOS) > -1) {	
				ConCriterioConsultaAdm ccAdm = new ConCriterioConsultaAdm (usrbean);
				String where = ccAdm.getWhere (idinstitucion, idConsulta);
				
				final Pattern pattern = Pattern.compile
						(ConCriterioConsultaAdm.CONS_CRITERIOS);
				final Matcher matcher = pattern.matcher (sentencia);
				sentencia = matcher.replaceAll (where);
				sentencia = ccAdm.sustituirParametrosColegiado (sentencia);
				sentencia = sentencia.toUpperCase();
				//sustituyendo la marca '%%idioma%%' por el idioma del USERBEAN
				sentencia = sentencia.replaceAll ("%%IDIOMA%%", usrbean.getLanguage());
			}
			
			//sustituyendo los criterios dinamicos si los hubiera
			if (! esExperta)
			{ //si no es consulta experta	
				if (sentencia.indexOf (ConCriteriosDinamicosAdm.CONS_DINAMICOS) > -1) {	
					ConCriteriosDinamicosAdm cdAdm = new ConCriteriosDinamicosAdm (usrbean);

					String criteriosDinamicos = "";
					
					for (int i=0; i<cDinamicos.length && cDinamicos[i]!=null; i++) {
						Hashtable hash = new Hashtable();
						hash.put (ConCampoConsultaBean.C_IDCAMPO, cDinamicos[i].getIdC());
						ConCampoConsultaAdm ccAdm = new ConCampoConsultaAdm (usrbean);
						ConCampoConsultaBean ccBean = new ConCampoConsultaBean();
						ccBean = (ConCampoConsultaBean) ccAdm.selectByPK (hash).firstElement();
						ConOperacionConsultaAdm ocAdm = new ConOperacionConsultaAdm (usrbean);
						Hashtable hoc = new Hashtable ();
						hoc.put (ConOperacionConsultaBean.C_IDOPERACION, cDinamicos[i].getOp());
						ConOperacionConsultaBean ocBean = (ConOperacionConsultaBean)
								((Vector)ocAdm.selectByPK (hoc)).firstElement();
						if (i>0)
							criteriosDinamicos += " AND ";
						
						//en las comparaciones de igualdad en fechas hay que quitar la hora
						if (ccBean.getTipoCampo().equals (SIGAConstants.TYPE_DATE) &&
							ocBean.getSimbolo().trim().equals ("="))
						{
//							criteriosDinamicos += "TO_CHAR ("+
//									cdAdm.getNombreCampo (cDinamicos[i].getIdC())+
//									", 'YYYY/MM/DD')";
						    criteriosDinamicos += "TRUNC("+
									cdAdm.getNombreCampo (cDinamicos[i].getIdC())+
							")";
						    
						} else {
							criteriosDinamicos += cdAdm.getNombreCampo (cDinamicos[i].getIdC());
						}
						
						criteriosDinamicos += " "+ocBean.getSimbolo()+" ";
						
						//controlando que el operador es "esta vacio"
						if (! ocBean.getSimbolo ().trim ().equals (ConOperacionConsultaBean.IS_NULL)) {
							if (ccBean.getFormato ().equals (ConCampoConsultaAdm.CONS_FORMATO)) {
								iParametroBind++;
								criteriosDinamicos += ":@"+iParametroBind+"@:";
								codigosBind.put (new Integer(iParametroBind), cDinamicos[i].getVal());
							}
							else {
								String formato = ccBean.getFormato();
								final Pattern pattern = Pattern.compile (ConCampoConsultaAdm.CONS_FORMATO);
								final Matcher matcher = pattern.matcher (formato);
								
								if (ccBean.getTipoCampo().equals(SIGAConstants.TYPE_DATE)) {
									//en las comparaciones de igualdad en fechas hay que quitar la hora
									if (ocBean.getSimbolo().trim().equals("=")) {
										while (matcher.find()) {
											iParametroBind++;
//											criteriosDinamicos += "TO_CHAR ("+
//											(matcher.replaceFirst ("---")).replaceFirst
//												("'---'", ":@"+iParametroBind)+"@:"+
//											", 'YYYY/MM/DD')";
											String aux = matcher.replaceFirst ("---");
											aux = UtilidadesString.replaceAllIgnoreCase(aux,"'---'", ":@"+iParametroBind+"@:");
											criteriosDinamicos += "TRUNC("+aux+")";
											codigosBind.put (new Integer(iParametroBind),
												GstDate.getApplicationFormatDate ("", cDinamicos[i].getVal()));
										}
									}
									else {
										while (matcher.find()) {
											iParametroBind++;
											criteriosDinamicos += (matcher.replaceFirst ("---")).replaceFirst 
												("'---'", ":@"+iParametroBind+"@:");
											codigosBind.put (new Integer(iParametroBind),
												GstDate.getApplicationFormatDate ("", cDinamicos[i].getVal()));
										}
									}
								}
								else {
									while (matcher.find()) {
										iParametroBind++;
										criteriosDinamicos += (matcher.replaceFirst ("---")).replaceFirst
											("'---'", ":@"+iParametroBind+"@:");
										codigosBind.put (new Integer(iParametroBind), cDinamicos[i].getVal());
									}
								}
							}
						}
					} //for
					
					final Pattern pattern2 = Pattern.compile (ConCriteriosDinamicosAdm.CONS_DINAMICOS);
					final Matcher matcher2 = pattern2.matcher (sentencia);
					sentencia = matcher2.replaceAll (criteriosDinamicos);
					//PaginadorCaseSensitiveBind paginador = 
					//	new PaginadorCaseSensitiveBind
					//		(sentencia, cabeceras, codigosBind);
					//La anterior linea no es necesaria ya que se ejecuta al final
					
				}
			}
			else
			{ //es consulta experta
				if ((sentencia.indexOf (ClsConstants.TIPONUMERO))>-1 ||
					(sentencia.indexOf (ClsConstants.TIPOTEXTO))>-1 ||
					(sentencia.indexOf (ClsConstants.TIPOFECHA)>-1 ||
					(sentencia.indexOf (ClsConstants.TIPOMULTIVALOR))>-1))
				{
					String criteriosDinamicos = "";
					String sentenciaAux, sentenciaAux1, sentenciaAux2;
					String operador = "";
					String etiqueta = "";
					int posEtiquetaOperador = 0;
					int posAlias = 0;
					boolean continuar = true;
					int pos_ini = 0;
					int pos_iniEtiqueta = 0;
					ConOperacionConsultaAdm ocAdm = new ConOperacionConsultaAdm (usrbean);
					sentencia=sentencia.replaceAll ("\r\n", " ");
					int j=0;
					
					//Por cada tipo de filtro
					String alias = "";
					Vector v_tipoDatos = new Vector ();
					v_tipoDatos.add (ClsConstants.TIPONUMERO);
					v_tipoDatos.add (ClsConstants.TIPOTEXTO);
					v_tipoDatos.add (ClsConstants.TIPOFECHA);
					v_tipoDatos.add (ClsConstants.TIPOMULTIVALOR);
					for (int i=0; i<v_tipoDatos.size() ;i++)
					{
						if (cDinamicos[j]!=null)
						{
							continuar=true;
							pos_ini=0;
							
							while (continuar && pos_ini<=sentencia.length()) {
								if (v_tipoDatos.get(i).toString().equals(ClsConstants.TIPONUMERO))
									etiqueta=ClsConstants.TIPONUMERO;
								else if (v_tipoDatos.get(i).toString().equals(ClsConstants.TIPOTEXTO))
									etiqueta=ClsConstants.TIPOTEXTO;
								else if (v_tipoDatos.get(i).toString().equals(ClsConstants.TIPOFECHA))
									etiqueta=ClsConstants.TIPOFECHA;
								else
									etiqueta=ClsConstants.TIPOMULTIVALOR;
								

								if (etiqueta.equals(ClsConstants.TIPOMULTIVALOR)) {
									int iMV = sentencia.indexOf(ClsConstants.TIPOMULTIVALOR);
									if (iMV > -1) {
										etiqueta = sentencia.substring(iMV, 2+sentencia.indexOf("%%", iMV + ClsConstants.TIPOMULTIVALOR.length()));
									}
								} else if (cDinamicos[j]!=null && !cDinamicos[j].getHp().equals("-1")) {
									//cuando existe select de ayuda porque estamos con la etiqueta multivalor
									etiqueta += cDinamicos[j].getHp().replaceAll
									    (ClsConstants.CONSTANTESUSTITUIRCOMILLAS,"\"");
								}
								pos_iniEtiqueta=sentencia.indexOf (etiqueta);	
								sentenciaAux=sentencia.substring (0, pos_iniEtiqueta+etiqueta.length());
								
								if (pos_iniEtiqueta>=0)
								{
								 	Hashtable hoc = new Hashtable ();
									hoc.put (ConOperacionConsultaBean.C_IDOPERACION, cDinamicos[j].getOp ());
									ConOperacionConsultaBean ocBean = (ConOperacionConsultaBean)
											((Vector)ocAdm.selectByPK (hoc)).firstElement ();
									operador = ocBean.getSimbolo ();
									
									//controlando que el operador es "esta vacio"
									if (!ocBean.getSimbolo().trim().equals(ConOperacionConsultaBean.IS_NULL)) {
										if (cDinamicos[j].getTc().equals (SIGAConstants.TYPE_DATE)) {
											iParametroBind++;
											criteriosDinamicos = "TO_DATE (:@"+iParametroBind+"@:"+", 'YYYY/MM/DD HH24:MI:SS')";
											String aux = cDinamicos[j].getVal();
											codigosBind.put (new Integer(iParametroBind), GstDate.getApplicationFormatDate("", aux));
										}
										else {
											iParametroBind++;
											criteriosDinamicos = ":@"+iParametroBind+"@:";
											codigosBind.put (new Integer(iParametroBind), cDinamicos[j].getVal());
										}
									}
									else {
										criteriosDinamicos="";
									}
									
									sentenciaAux = sentenciaAux.substring (0, pos_iniEtiqueta) +
											criteriosDinamicos +
											sentenciaAux.substring (pos_iniEtiqueta+etiqueta.length());
									
									posEtiquetaOperador = sentenciaAux.lastIndexOf (ClsConstants.ETIQUETAOPERADOR);
									sentenciaAux1 = sentenciaAux.substring (posEtiquetaOperador).
											replaceAll (ClsConstants.ETIQUETAOPERADOR, " "+operador);
									sentenciaAux = sentenciaAux.substring (0, posEtiquetaOperador) + sentenciaAux1;
									if (!cDinamicos[j].getSt().equals("-1")) {
										alias = cDinamicos[j].getSt().
												replaceAll (ClsConstants.CONSTANTESUSTITUIRCOMILLAS, "\"");	
										posAlias = sentenciaAux.lastIndexOf (alias);
										sentenciaAux2=sentenciaAux.substring (posAlias+alias.length());
										sentenciaAux=sentenciaAux.substring (0, posAlias) + sentenciaAux2;
									}
									
									sentencia = sentenciaAux+sentencia.substring(pos_iniEtiqueta+etiqueta.length());
									pos_ini = pos_iniEtiqueta+etiqueta.length();
									
									j++;
								}
								else {
									continuar=false;
								}
							} //while
						} else {
							break;
						}
						
					} //for

					
					cabeceras = sacarCabeceras (sentencia);

					
					//PaginadorCaseSensitiveBind paginador =
					//	new PaginadorCaseSensitiveBind
					//		(sentencia, cabeceras, codigosBind);
					//La anterior linea no es necesaria ya que se ejecuta al final
					
				}
			}
			
		}
		
		if (tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_ENV))
		{ //consulta de envíos
			
			cabeceras = new String[11];
			cabeceras[0]=CenClienteBean.C_IDPERSONA;
			cabeceras[1]=CenClienteBean.C_IDINSTITUCION;
			cabeceras[2]=CenDireccionesBean.C_CODIGOPOSTAL;
			cabeceras[3]=CenDireccionesBean.C_CORREOELECTRONICO;
			cabeceras[4]=CenDireccionesBean.C_DOMICILIO;
			cabeceras[5]=CenDireccionesBean.C_MOVIL;
			cabeceras[6]=CenDireccionesBean.C_FAX1;
			cabeceras[7]=CenDireccionesBean.C_FAX2;
			cabeceras[8]=CenDireccionesBean.C_IDPAIS;
			cabeceras[9]=CenDireccionesBean.C_IDPROVINCIA;
			cabeceras[10]=CenDireccionesBean.C_IDPOBLACION;
			
			final Pattern pattern3 = Pattern.compile (EnvTipoEnviosAdm.CONS_TIPOENVIO);
	        final Matcher matcher3 = pattern3.matcher (sentencia);
	        sentencia = matcher3.replaceAll (tipoEnvio);
	        
		}
		else if (tipoConsulta.equals (ConConsultaAdm.TIPO_CONSULTA_FAC))
		{ //consulta de facturacion
			
			cabeceras = new String[2];
			cabeceras[0] = CenClienteBean.C_IDINSTITUCION;
			cabeceras[1] = CenClienteBean.C_IDPERSONA;
		
		}
		
		// RGG voy a ordenar las bind variables porque las he dejado desordenadas
		// busco las ocurrencias de :@x para cambiarlas por :y ordenado
		String sentenciaAux= sentencia;
		int indice=sentenciaAux.indexOf(":@",0);
		int contadorOrdenados=0;
		Hashtable codigosOrdenados = new Hashtable();
		while (indice!=-1) {
		    String numero=sentenciaAux.substring(indice+2,sentenciaAux.indexOf("@:",indice));
		    contadorOrdenados++;
		    codigosOrdenados.put(new Integer(contadorOrdenados),(String)codigosBind.get(new Integer(numero)));
		    sentencia=sentencia.replaceFirst(":@"+numero+"@:",":"+contadorOrdenados);
		    indice=sentenciaAux.indexOf(":@",indice+2);
		}

		sentencia = UtilidadesString.replaceAllIgnoreCase(sentencia, "@FECHA@", "SYSDATE");
		sentencia = UtilidadesString.replaceAllIgnoreCase(sentencia, "@IDIOMA@", usrbean.getLanguage());
		// RGG 21/04/2009 Se intentan sustituir los parametros de las funciones de cen_colegiado
		ConCriterioConsultaAdm concritcon = new ConCriterioConsultaAdm(this.usrbean);
		sentencia = concritcon.sustituirParametrosColegiado(sentencia);		
		
		//Monta los resultados
		Hashtable hash = new Hashtable();
		hash.put("sentencia", sentencia);
		hash.put("codigosOrdenados", codigosOrdenados);
		hash.put("cabeceras", cabeceras);
		return hash;
		
	}

	
	private String[] sacarCabeceras (String consulta)
	{
		String select, selectNew, busquedaAlias, alias;
		int aliasIni, aliasFin;
		boolean fin;
		Vector camposSalida = new Vector();
		String[] cabeceras;
		
		select = consulta;
		busquedaAlias = " AS \"";
		
		fin=false;
		while ((aliasIni = select.toUpperCase().indexOf (busquedaAlias)) >= 0 && !fin) {
			selectNew = select.substring (aliasIni + busquedaAlias.length()-1).trim();
			aliasFin = selectNew.substring (1).indexOf ("\"");
			
			if (aliasFin >= 0) {
				alias = selectNew.substring (1, aliasFin+1);
				camposSalida.add (alias);
				select = selectNew.substring (aliasFin+1);
			}
			else {
				fin=true;
			}
		} //while
		
		cabeceras = null;
		if (!camposSalida.isEmpty ()) {
			cabeceras = new String[camposSalida.size()];
			for (int i=0; i<camposSalida.size(); i++) {
				cabeceras[i]= ((String) camposSalida.get (i)).toUpperCase();
			}
		}
		
		return cabeceras;
	}
	
	
	public static String eliminarEtiquetas(String select) throws SIGAException {
		select=select.toUpperCase();
		select=select.replaceAll(ClsConstants.ETIQUETASELECTOPEN,"").replaceAll(ClsConstants.ETIQUETASELECTCLOSE,"");
		select=select.replaceAll(ClsConstants.ETIQUETAFROMOPEN,"").replaceAll(ClsConstants.ETIQUETASFROMCLOSE,"");
		select=select.replaceAll(ClsConstants.ETIQUETAWHEREOPEN,"").replaceAll(ClsConstants.ETIQUETASWHERECLOSE,"");
		select=select.replaceAll(ClsConstants.ETIQUETAUNIONOPEN,"").replaceAll(ClsConstants.ETIQUETAUNIONCLOSE,"");
		select=select.replaceAll(ClsConstants.ETIQUETAUNIONALLOPEN,"").replaceAll(ClsConstants.ETIQUETAUNIONALLCLOSE,"");
		select=select.replaceAll(ClsConstants.ETIQUETAORDERBYOPEN,"").replaceAll(ClsConstants.ETIQUETASORDERBYCLOSE,"");
		select=select.replaceAll(ClsConstants.ETIQUETAGROUPBYOPEN,"").replaceAll(ClsConstants.ETIQUETASGROUPBYCLOSE,"");
		
		return select;
	}
}
