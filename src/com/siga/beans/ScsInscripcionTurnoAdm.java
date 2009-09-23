
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.general.SIGAException;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_INSCRIPCIONTURNO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsInscripcionTurnoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsInscripcionTurnoAdm (UsrBean usuario) {
		super( ScsInscripcionTurnoBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposTabla ()
	 * Devuelve los campos que queremos recuperar desde el select
	 * para rellenar la tabla de la página "listarTurnos.jsp"
	 * 
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposTabla(){
		String[] campos = {	ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA+" "+ScsInscripcionTurnoBean.C_IDPERSONA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDINSTITUCION+" "+ScsInscripcionTurnoBean.C_IDINSTITUCION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO+" "+ScsInscripcionTurnoBean.C_IDTURNO,					
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD+" "+ScsInscripcionTurnoBean.C_FECHASOLICITUD,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+" "+ScsInscripcionTurnoBean.C_FECHAVALIDACION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHABAJA+" "+ScsInscripcionTurnoBean.C_FECHABAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA+" "+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD+" "+ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, 	
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION+" "+ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA+" "+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA};
		return campos;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsInscripcionTurnoBean.C_IDPERSONA,				ScsInscripcionTurnoBean.C_IDINSTITUCION,
							ScsInscripcionTurnoBean.C_IDTURNO,					ScsInscripcionTurnoBean.C_FECHASOLICITUD,
							ScsInscripcionTurnoBean.C_FECHAVALIDACION,			ScsInscripcionTurnoBean.C_FECHABAJA,
							ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, 	ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,
							ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,		ScsInscripcionTurnoBean.C_USUMODIFICACION,
							ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,		ScsInscripcionTurnoBean.C_FECHAMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsInscripcionTurnoBean.C_IDINSTITUCION, ScsInscripcionTurnoBean.C_IDPERSONA, ScsInscripcionTurnoBean.C_IDTURNO,	ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		return campos;
	}
	
	/** Funcion getClavesTabla ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 *  con formato "NombreTabla.NombreCampo"
	 * 
	 */
	protected String[] getClavesTabla() {
		String[] campos = {	ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO, 	
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsInscripcionTurnoBean bean = null;
		try{
			bean = new ScsInscripcionTurnoBean();
			bean.setIdPersona(Long.valueOf((String)hash.get(ScsInscripcionTurnoBean.C_IDPERSONA)));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsInscripcionTurnoBean.C_IDINSTITUCION)));
			bean.setIdTurno(Integer.valueOf((String)hash.get(ScsInscripcionTurnoBean.C_IDTURNO)));
			bean.setFechaSolicitud((String)hash.get(ScsInscripcionTurnoBean.C_FECHASOLICITUD));
			bean.setFechaValidacion((String)hash.get(ScsInscripcionTurnoBean.C_FECHAVALIDACION));
			bean.setFechaBaja((String)hash.get(ScsInscripcionTurnoBean.C_FECHABAJA));
			bean.setFechaSolicitudBaja((String)hash.get(ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA));
			bean.setObservacionesSolicitud((String)hash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD));
			bean.setObservacionesValidacion((String)hash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION));
			bean.setObservacionesBaja((String)hash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsInscripcionTurnoBean b = (ScsInscripcionTurnoBean) bean;
			hash.put(ScsInscripcionTurnoBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			hash.put(ScsInscripcionTurnoBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsInscripcionTurnoBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			hash.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION, b.getFechaValidacion());
			hash.put(ScsInscripcionTurnoBean.C_FECHABAJA, b.getFechaBaja());
			hash.put(ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA, b.getFechaSolicitudBaja());
			hash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, b.getObservacionesSolicitud());
			hash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, b.getObservacionesValidacion());
			hash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA, b.getObservacionesBaja());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return null;
	}
	/** Funcion getOrdenLetrados ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que rellena tabla de la pagina "listarTurnos.jsp"
	 */
	protected String[] getOrdenLetrados() {
		String[] vector ={ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA};
		return vector;
	}
	
	/** Funcion select(String where)
	 *	@param where clausula "where" de la sentencia "select" que queremos ejecutar
	 *  @return Vector todos los registros que se seleccionen en BBDD 
	 *  
	 *
	 */
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposTabla());
			sql += where;
			sql += this.getOrdenLetrados()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenLetrados()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesTabla());

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow()); 
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
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposTabla());
			sql += where;
			sql += this.getOrdenLetrados()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenLetrados()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesTabla());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}	
	/**
	 * Efectúa un SELECT en la tabla SCS_INSCRIPCIONTURNO con los datos introducidos. 
	 * 
	 * @param sql. Consulta a realizar
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectTabla(String sql){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
	
	public boolean insertarBajaenTurno (boolean usarTransaccion, UsrBean user, String idPersona, String idInstitucion, String motivo) throws SIGAException , ClsExceptions{
	
		boolean error = false;

		if (usarTransaccion){
			
			UserTransaction tx = user.getTransaction();	
			
			try{
			    tx.begin(); 
			
			
				Hashtable hTurno = new Hashtable();
				hTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION,idInstitucion);
				hTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA,idPersona);
				Vector vTurno = select(hTurno);
				
				for (int i=0; i < vTurno.size(); i++){
					ScsInscripcionTurnoBean bTurno = (ScsInscripcionTurnoBean)vTurno.elementAt(i);
					bTurno.setOriginalHash(this.beanToHashTable(bTurno));
					bTurno.setFechaBaja("sysdate");
					bTurno.setObservacionesBaja(motivo);
					error = !(update(bTurno)) || error;
				}
				
				ScsInscripcionGuardiaAdm gAdm = new ScsInscripcionGuardiaAdm(this.usrbean);
				Hashtable hGuardia = new Hashtable();
				hGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,idInstitucion);
				hGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA,idPersona);
				Vector vGuardia = gAdm.select(hGuardia);
				
				if (!vGuardia.isEmpty()){
					for (int i=0; i < vGuardia.size(); i++){
						ScsInscripcionGuardiaBean bGuardia = (ScsInscripcionGuardiaBean)vGuardia.elementAt(i);
						bGuardia.setOriginalHash(gAdm.beanToHashTable(bGuardia));
						bGuardia.setFechaBaja("sysdate");
						bGuardia.setObservacionesBaja(motivo);
						error = !(gAdm.update(bGuardia)) || error;
					}
				}
				
				if (error){
					tx.rollback();
			        throw new SIGAException("error.messages.inserted");
		        } else {
		        	tx.commit(); 		        	
			    }  	
				return true;
			}catch(Exception e){
				try {
		            tx.rollback();
		            return false;
		        } catch (Exception e1) {
		            throw new ClsExceptions(e1,"Se ha producido un error en el rollback");
		        }
			}
			
		}else{
			Hashtable hTurno = new Hashtable();
			hTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION,idInstitucion);
			hTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA,idPersona);
			Vector vTurno = this.select(hTurno);
			
			for (int i=0; i < vTurno.size() && !error; i++){
				ScsInscripcionTurnoBean bTurno = (ScsInscripcionTurnoBean)vTurno.elementAt(i);
				bTurno.setOriginalHash(this.beanToHashTable(bTurno));
				bTurno.setFechaBaja("sysdate");
				bTurno.setObservacionesBaja(motivo);
				if (!this.update(bTurno))
					error = true;
			}
			
			ScsInscripcionGuardiaAdm gAdm = new ScsInscripcionGuardiaAdm(this.usrbean);
			Hashtable hGuardia = new Hashtable();
			hGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,idInstitucion);
			hGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA,idPersona);
			Vector vGuardia = gAdm.select(hGuardia);
			
			if (!vGuardia.isEmpty() && !error){
				for (int i=0; i < vGuardia.size() && !error; i++){
					ScsInscripcionGuardiaBean bGuardia = (ScsInscripcionGuardiaBean)vGuardia.elementAt(i);
					bGuardia.setOriginalHash(gAdm.beanToHashTable(bGuardia));
					bGuardia.setFechaBaja("sysdate");
					bGuardia.setObservacionesBaja(motivo);
					if (!gAdm.update(bGuardia))
						error = true;
				}
			}
			
			if (error){				
		        throw new SIGAException("error.messages.updated");
	        }
			return true;
		}
	}

	 

	
}