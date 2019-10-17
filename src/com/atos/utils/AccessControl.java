/*
 * Created on 04-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.atos.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.MasterBean;

/**
 * @author Carmen.Garcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccessControl implements SIGAConstants, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8857440990175753266L;

	private Hashtable procesos = null;

	private Hashtable accesos = null;

	public final String Hidden = "HIDDEN";

	public AccessControl() {

	}

	public String checkAccessByProcessNumber(String[] profiles, String process,
			int institucion, HttpServletRequest request) {
		if (accesos == null) {
			accesos = new Hashtable();
			procesos = new Hashtable();
			getProcess(profiles, institucion);
		}
		
		ClsLogging.writeFileLog("Permisos de este usuario: " + procesos,request,3);
		ClsLogging.writeFileLog("Test de acceso a proceso: " + process,request,3);
		Integer access = (Integer) accesos.get(process);
		if (access != null) {
			switch (access.intValue()) {
			case 0:
				return ACCESS_NONE;
			case 1:
				return ACCESS_DENY;
			case 2:
				return ACCESS_READ;
			case 3:
				return ACCESS_FULL;
			}
		}

		return ACCESS_NONE;
	}

	public String checkAccessByProcessName(String[] profiles, String process,
			int institucion, HttpServletRequest request) {
		if (accesos == null) {
			accesos = new Hashtable();
			procesos = new Hashtable();
			getProcess(profiles, institucion);
		}
		String processNumber = (String) procesos.get(process);
		if (processNumber == null) {
			return ACCESS_NONE;
		}
		
		ClsLogging.writeFileLog("Permisos de este usuario: " + procesos,10);
		ClsLogging.writeFileLog("Test de acceso a proceso numero: " + processNumber,10);
		Integer access = (Integer) accesos.get(processNumber);
		if (access != null) {
			switch (access.intValue()) {
			case 0:
				return ACCESS_NONE;
			case 1:
				return ACCESS_DENY;
			case 2:
				return ACCESS_READ;
			case 3:
				return ACCESS_FULL;
			}
		}

		return ACCESS_NONE;
	}

	private void getProcess(String prof[], int institucion) {
		String perfiles = "";
		for (int i = 0; i < prof.length; i++) {
			perfiles += ",'" + prof[i] + "'";
		}
		if (perfiles.length() > 0)
			perfiles = perfiles.substring(1);

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select a.idproceso, ");
		queryBuilder.append("       p.transaccion, ");
		queryBuilder.append("       decode(max(decode(a.derechoacceso,1,9)), 9, 1, max(a.derechoacceso)) derechoacceso, ");
		queryBuilder.append("       p.descripcion, ");
		queryBuilder.append("       p.idparent ");
		queryBuilder.append("  from adm_tiposacceso a, gen_procesos p where idperfil in ( ");
		queryBuilder.append(perfiles);
		queryBuilder.append(")  and a.idproceso=p.idproceso and  ");
		queryBuilder.append(ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION);
		queryBuilder.append("= ");
		queryBuilder.append(institucion);
		queryBuilder.append("   and p.descripcion not like 'HIDDEN%' ");
		//Para los procesos hidden no se cogen los permisos propios si no los del padre.
		//Esto se necesita asi, porque si alguien (como susi) crea un proceso hidden y no le asigna un drecho acceso 
		//habra conflictos con otros peerfiles que se creen desde cero daran conflicto con los existentes
		//PREGUNTAR A NVL(CARLOS (si está todavía),ADRIAN)
		queryBuilder.append(" group by a.idproceso, p.transaccion,  p.descripcion, p.idparent ");

		String queryAccess = queryBuilder.toString();
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.query(queryAccess)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = fila.getRow(); 
					String processNumber = registro.get("IDPROCESO") == null ? null : (String) registro.get("IDPROCESO");
					String processTrans = registro.get("TRANSACCION") == null ? null : (String) registro.get("TRANSACCION");
					int tipoAccess = registro.get("DERECHOACCESO") == null ? 0 : Integer.valueOf((String) registro.get("DERECHOACCESO"));

					Integer accessTypeStored = (Integer) accesos.get(processNumber);
					if (accessTypeStored == null) {
						accesos.put(processNumber, new Integer(tipoAccess));
						if (processTrans != null && processTrans.length() != 0)
							procesos.put(processTrans, processNumber);
					} else {
						if (accessTypeStored.intValue() == 1)
							continue;
						if (accessTypeStored.intValue() < tipoAccess
								|| tipoAccess == 1) {
							accesos.put(processNumber, new Integer(tipoAccess));
						}
					}
				}
			}

			rellenaProcesosHidden();
		} catch (Exception e) {
			//ORA-00942: tabla o vista no encontrada
			//ORA-00923: palabra clave FROM no encontrada donde se esperaba
			//ORA-00933: comando SQL no terminado correctamente
			//	                   throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
			ClsLogging.writeFileLogError("ERROR: " + e.toString(), e, 1);
		}
	}
	
	private void rellenaProcesosHidden() {
	
		String queryAccess = "select idproceso, transaccion, descripcion, idparent from gen_procesos"; 

		Connection con = null;
		Statement stmtAccess = null;
		ResultSet rsAccess = null;
		try {
			con = ClsMngBBDD.getReadConnection();
			stmtAccess = con.createStatement();
			rsAccess = stmtAccess.executeQuery(queryAccess);

			while (rsAccess.next()) {
				String processNumber = rsAccess.getString(1);
				String processTrans = rsAccess.getString(2);
				String descripcion = rsAccess.getString(3);
				String pater = rsAccess.getString(4);

				if (descripcion != null && descripcion.startsWith(Hidden) && accesos.get(processNumber)==null
						&& accesos.get(pater)!=null) {
					  procesos.put(processTrans, processNumber);
					  accesos.put(processNumber,accesos.get(pater));
				}
			}
			rsAccess.close();
			rsAccess = null;
		} catch (Exception e) {
			//ORA-00942: tabla o vista no encontrada
			//ORA-00923: palabra clave FROM no encontrada donde se esperaba
			//ORA-00933: comando SQL no terminado correctamente
			//	                   throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
			ClsLogging.writeFileLogError("ERROR: " + e.toString(),e, 1);

		} finally {

			try {
				if (rsAccess != null)
					rsAccess.close();
				if (stmtAccess != null)
					stmtAccess.close();
				ClsMngBBDD.closeConnection(con);
			} catch (Exception ex) {
				ClsLogging.writeFileLogError("ERROR cerrando la conexion: "
						+ ex.toString(), ex, 1);
			}
		}
	}
	
}