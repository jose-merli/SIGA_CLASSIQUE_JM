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

import com.siga.administracion.SIGAConstants;

/**
 * @author Carmen.Garcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccessControl implements SIGAConstants, Serializable {
	private Hashtable procesos = null;

	private Hashtable accesos = null;

	public final String Hidden = "HIDDEN";

	public AccessControl() {

	}

	public String checkAccessByProcessNumber(String[] profiles, String process,
			int institucion) {
		if (accesos == null) {
			accesos = new Hashtable();
			procesos = new Hashtable();
			getProcess(profiles, institucion);
		}
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
			int institucion) {
		if (accesos == null) {
			accesos = new Hashtable();
			procesos = new Hashtable();
			getProcess(profiles, institucion);
		}
		String processNumber = (String) procesos.get(process);
		if (processNumber == null) {
			return ACCESS_NONE;
		}
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

		String queryAccess = "select a.idproceso, p.transaccion,decode(max(decode(a.derechoacceso,1,9)), 9, 1, max(a.derechoacceso)) derechoacceso, "
				+ " p.descripcion, p.idparent "
				+ "from adm_tiposacceso a, gen_procesos p where idperfil in ("
				+ perfiles
				+ ") and a.idproceso=p.idproceso and "
				+ ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION
				+ "="
				+ institucion
				+ "and p.descripcion not like 'HIDDEN%' " //Para los procesos hidden no se cogen los permisos propios si no los del padre.
				//Esto se necesita asi, porque si alguien (como susi) crea un proceso hidden y no le asigna un drecho acceso 
				//habra conflictos con otros peerfiles que se creen desde cero daran conflicto con los existentes
				//PREGUNTAR A NVL(CARLOS (si está todavía),ADRIAN)
				+ " group by a.idproceso, p.transaccion,  p.descripcion, p.idparent ";

		Connection con = null;
		Statement stmtAccess = null;
		ResultSet rsAccess = null;
		try {
			con = ClsMngBBDD.getReadConnection();
			stmtAccess = con.createStatement();
			rsAccess = stmtAccess.executeQuery(queryAccess);
			Hashtable hidden=new Hashtable();

			while (rsAccess.next()) {
				String processNumber = rsAccess.getString(1);
				String processTrans = rsAccess.getString(2);
				int tipoAccess = rsAccess.getInt(3);
				String descripcion = rsAccess.getString(4);
				String pater = rsAccess.getString(5);

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

/*				if (descripcion != null && descripcion.startsWith(Hidden)) {
					hidden.put(processNumber,pater);
				}  */
			}
			rsAccess.close();
			rsAccess = null;
/*			Enumeration e=hidden.keys();
			while (e.hasMoreElements()) {
				String process=(String)e.nextElement();
				String pater=(String)hidden.get(process);
				accesos.put(process,accesos.get(pater));		
			}
*/			
			rellenaProcesosHidden();
		} catch (Exception e) {
			//ORA-00942: tabla o vista no encontrada
			//ORA-00923: palabra clave FROM no encontrada donde se esperaba
			//ORA-00933: comando SQL no terminado correctamente
			//	                   throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
			ClsLogging.writeFileLogError("ERROR: " + e.toString(), e, 1);

		} finally {

			try {
				if (rsAccess != null)
					rsAccess.close();
				if (stmtAccess != null)
					stmtAccess.close();
				ClsMngBBDD.closeConnection(con);
			} catch (Exception ex) {
				ClsLogging.writeFileLogError("ERROR cerrando la conexion: "
						+ ex.toString(),ex, 1);
			}
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
			Hashtable hidden=new Hashtable();

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