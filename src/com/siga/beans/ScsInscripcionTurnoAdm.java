
package com.siga.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.action.GestionInscripcionesTGAction;
import com.siga.gratuita.form.InscripcionTGForm;

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
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA+" "+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA+" "+ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA
							,ScsInscripcionTurnoBean.C_FECHADENEGACION,			ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION						
		};
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
							ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,		ScsInscripcionTurnoBean.C_FECHAMODIFICACION
							,ScsInscripcionTurnoBean.C_FECHADENEGACION,			ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION
							,			ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA
		};
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
			bean.setObservacionesValBaja((String)hash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA));
			bean.setObservacionesDenegacion((String)hash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION));
			bean.setFechaDenegacion((String)hash.get(ScsInscripcionTurnoBean.C_FECHADENEGACION));
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
			hash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA, b.getObservacionesValBaja());
			hash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION, b.getObservacionesDenegacion());
			hash.put(ScsInscripcionTurnoBean.C_FECHADENEGACION, b.getFechaDenegacion());
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
	
	/**
	 * JPT: Me ha costado llegar hasta aqui.
	 * 1. Expedientes > Tipos Expedientes: Crear un tipo de expediente con fase, clasificación, estados y permiso de acceso.
	 * 2. Expedientes > Gestionar Expedientes: Introducir colegiado, estado y datos obligatorios.
	 * 3. Cada vez que cambias el estado, sale una pantalla con una serie de checks:
	 * - Baja Turno de Oficio: Da de baja al colegiado del turno de oficio.
	 * - Baja Colegial: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 * - Baja en ejercicio: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 * - Inhabilitación perpetua: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 * - Suspensión ejercicio: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 * 
	 * @param idPersona
	 * @param idInstitucion
	 * @param motivo
	 * @throws SIGAException
	 * @throws ClsExceptions
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public void insertarBajaEnTurnos (String idPersona, String idInstitucion, String motivo) throws SIGAException, ClsExceptions, NumberFormatException, ParseException {
		
		/* Obtiene una lista de las inscripciones de turno a dar de baja. 
		   Al dar de baja un turno, también se da de baja de sus inscripciones de guardia*/
		List alInscripciones = this.getInscripcionesTurnoParaBaja(idInstitucion, idPersona);
		
		// Recorre las inscripciones de turno
		if (alInscripciones != null && alInscripciones.size()>0){			
			for (int i = 0; i < alInscripciones.size(); i++) {
				
				// Obtiene la inscripcion de turno
				Hashtable turnoHash = (Hashtable)alInscripciones.get(i);
				
				// Obtiene datos de la inscripcion de turno
				String sIdTurno = UtilidadesHash.getString(turnoHash, "IDTURNO");
				String sFechaSolicitud = UtilidadesHash.getString(turnoHash, "FECHASOLICITUD");
				String sFechaSolicitudBaja = UtilidadesHash.getString(turnoHash, "FECHASOLICITUDBAJA");
				String sFechaValidacion = UtilidadesHash.getString(turnoHash, "FECHAVALIDACION");				

				// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
				InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
					new Integer(idInstitucion), 
					new Integer(sIdTurno), 
					Long.valueOf(idPersona),
					sFechaSolicitud, 
					this.usrbean, 
					true);
				
				/* JPT: INICIO - Calculo la fecha de baja para las inscripciones de turno y guardia del colegiado */
				
				InscripcionTGForm miForm = new InscripcionTGForm();
				GestionInscripcionesTGAction gInscripciones = new GestionInscripcionesTGAction();
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				
				// Relleno con los datos necesarios para el metodo que calcula la fecha de baja 
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(sIdTurno);
				miForm.setIdPersona(idPersona);
				miForm.setFechaValidacionTurno(sFechaValidacion);
				
				// Obtengo la fecha de baja minima para la inscripcion de turno
				String fechaBajaInscripciones = gInscripciones.calcularFechaBajaInscripcionTurno(miForm, this.usrbean);		
				
				// Obtengo la fecha actual y la convierto en el formato adecuado
				Date dFechaHoy = new Date(); 										
				String sFechaHoy = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy							
				
				// Comparo la fecha minima de baja del turno, con la fecha actual
				int comparacion = GstDate.compararFechas(fechaBajaInscripciones, sFechaHoy);
					
				// Obtenemos la fecha mayor
				if (comparacion < 0) {
					fechaBajaInscripciones = sFechaHoy;
				}					
				
				/* JPT: FIN - Calculo la fecha de baja para las inscripciones de turno y guardia del colegiado */				
				
				if(sFechaSolicitudBaja != null && !sFechaSolicitudBaja.equals("")) {
					inscripcion.validarBaja(
						fechaBajaInscripciones, 
						sFechaValidacion,
						motivo,
						null, 
						this.usrbean);
					
				} else {
					inscripcion.solicitarBaja(
						"sysdate",
						motivo,
						fechaBajaInscripciones,
						motivo,
						sFechaValidacion,
						null, 
						this.usrbean);	
				}			
			}
		}
	}
	
	public String getQueryNumeroColegiadosIncritos(String institucion, String turno, String fecha) throws ClsExceptions{
		  
		  
		String consulta="";
		
		
		  try{			

		   consulta=" select count(*)  as NLETRADOSTURNO"+
		            " from "+ScsInscripcionTurnoBean.T_NOMBRETABLA+ " ins , "+ ScsTurnoBean.T_NOMBRETABLA+" turno"	+	             
		            " where ins.idinstitucion =" + institucion +
		            " and ins.idturno ="+ turno+
		            " and ins.idinstitucion=turno.idinstitucion "+
		            " and ins.idturno=turno.idturno";
		   if(fecha==null){
		         // consulta   += " and ins.fechabaja  IS NULL"; 
		  	}else{
		  		if(fecha.equalsIgnoreCase("sysdate")){
		  			fecha = "TRUNC(SYSDATE)";
		  			
		  		}else{
		  			fecha = "'"+fecha+"'";
		  			
		  		}
		  		
		  		consulta   +="   And Ins.Fechavalidacion Is Not Null " +
		  		"   And Trunc(Ins.Fechavalidacion) <= "+fecha+" " +
				"   And (Ins.Fechabaja Is Null Or " +
				"        Trunc(Ins.Fechabaja) > "+fecha+") " ;

			   
		  	}
		           
              }
		  catch (Exception e){
			  throw new ClsExceptions(e,"Excepcion en ScsInscripcionTurnoAdm.getQueryNumeroColegiadosIncritos() Consulta SQL:"+consulta);
			}
	
			return consulta;
			  
		  }

	/**
	 * Busca inscripciones de turno
	 * 
	 * @param inscripcionTurnoForm
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsInscripcionTurnoBean> getInscripcionesTurno(InscripcionTGForm inscripcionTurnoForm) throws ClsExceptions {
		// Variables
		Hashtable<Integer, Object> htCodigos;
		int contador;
		StringBuffer sql; // texto de la sentencia
		
		// construyendo consulta base
		htCodigos = new Hashtable<Integer, Object>();
		contador = 0;
		sql = new StringBuffer();
		sql.append(" SELECT ");
			sql.append(" T.NOMBRE NOMBRETURNO, ");
			sql.append(" T.ABREVIATURA ABREVIATURA, ");
			sql.append(" T.VALIDARINSCRIPCIONES, ");
			sql.append(" T.GUARDIAS, ");
			sql.append(" DECODE(COL.COMUNITARIO, '1', COL.NCOMUNITARIO, COL.NCOLEGIADO) NCOLEGIADO, ");
			sql.append(" PER.NOMBRE, ");
			sql.append(" PER.APELLIDOS1, ");
			sql.append(" PER.APELLIDOS2, ");
			sql.append(" I.IDINSTITUCION, ");
			sql.append(" I.IDPERSONA, ");
			sql.append(" I.IDTURNO, ");
			sql.append(" I.FECHASOLICITUD, ");
			sql.append(" I.OBSERVACIONESSOLICITUD, ");
			sql.append(" I.FECHAVALIDACION, ");
			sql.append(" I.OBSERVACIONESVALIDACION, ");
			sql.append(" I.FECHASOLICITUDBAJA, ");
			sql.append(" I.OBSERVACIONESBAJA, ");
			sql.append(" I.FECHABAJA, ");
			sql.append(" I.OBSERVACIONESVALBAJA, ");
			sql.append(" I.FECHADENEGACION, ");
			sql.append(" I.OBSERVACIONESDENEGACION, ");
			sql.append(" TO_CHAR(NVL(I.FECHADENEGACION, I.FECHAVALIDACION), 'dd/mm/yyyy') FECHAVALORALTA, ");
			sql.append(" TO_CHAR(NVL(I.FECHADENEGACION, I.FECHABAJA), 'dd/mm/yyyy') FECHAVALORBAJA ");
		sql.append(" FROM SCS_INSCRIPCIONTURNO I, ");
			sql.append(" CEN_COLEGIADO COL, ");
			sql.append(" CEN_PERSONA PER, ");
			sql.append(" SCS_TURNO T ");
		sql.append(" WHERE I.IDINSTITUCION = COL.IDINSTITUCION ");
			sql.append(" AND I.IDPERSONA = COL.IDPERSONA ");
			sql.append(" AND COL.IDPERSONA = PER.IDPERSONA ");
			sql.append(" AND I.IDINSTITUCION = T.IDINSTITUCION ");
			sql.append(" AND I.IDTURNO = T.IDTURNO ");
			
		sql.append(" AND I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), inscripcionTurnoForm.getIdInstitucion());
		
		if (inscripcionTurnoForm.getIdTurno() != null && !inscripcionTurnoForm.getIdTurno().equalsIgnoreCase("") && !inscripcionTurnoForm.getIdTurno().equalsIgnoreCase("-1")) {
			sql.append(" AND I.IDTURNO = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionTurnoForm.getIdTurno());
		}
		
		if (inscripcionTurnoForm.getIdPersona() != null && !inscripcionTurnoForm.getIdPersona().equalsIgnoreCase("")) {
			sql.append(" AND I.IDPERSONA = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionTurnoForm.getIdPersona());
		}

		String campoFecha = null;
		StringBuffer orderBy = new StringBuffer();
		if (inscripcionTurnoForm.getTipo().equals("A"))
			campoFecha = "I.FECHASOLICITUD";
		
		else if (inscripcionTurnoForm.getTipo().equals("B"))
			campoFecha = "I.FECHASOLICITUDBAJA";

		if (inscripcionTurnoForm.getFechaActiva() != null) {
			String fecha = inscripcionTurnoForm.getFechaActiva();
			if (fecha == null || fecha.equalsIgnoreCase("sysdate"))
				fecha = "trunc(sysdate)";
			else
				// sy viene en formato dd/mm/yyyy, si no habra que añadirel trnuc
				fecha = "'" + fecha + "'";

			sql.append(" AND ( ");

			// VALIDADOS DE ALTA
			sql.append("  (I.FECHAVALIDACION IS NOT NULL AND ");
			sql.append("  TRUNC(I.FECHAVALIDACION) <= ");
			sql.append(fecha);

			sql.append("  AND (I.FECHABAJA IS NULL ");
			sql.append("  OR (I.FECHABAJA IS NOT NULL AND ");
			sql.append("  TRUNC(I.FECHABAJA) > ");
			sql.append(fecha);
			sql.append(")))");

			// pendientes de baja

			sql.append(" OR ");
			// PENDIENTES DE BAJA
			sql.append(" (I.FECHASOLICITUDBAJA IS NOT NULL AND I.FECHABAJA IS NULL AND I.FECHADENEGACION IS NULL) ");

			// BAJA DENEGADA
			sql.append("  OR ");
			sql.append("  (I.FECHADENEGACION IS NOT NULL AND I.FECHASOLICITUDBAJA IS NOT NULL) ");
			sql.append("  ) ");
			orderBy.append(" I.FECHABAJA ");

		} else {
			// OBTIENE EL ULTIMO TURNO
			sql.append(" AND I.FECHASOLICITUD = ");
			sql.append(" (SELECT MAX(IT2.FECHASOLICITUD) ");
				sql.append(" FROM SCS_INSCRIPCIONTURNO IT2 ");
				sql.append(" WHERE IT2.IDINSTITUCION = I.IDINSTITUCION ");
				sql.append(" AND IT2.IDTURNO = I.IDTURNO ");
				sql.append(" AND IT2.IDPERSONA = I.IDPERSONA) ");
			
			if (inscripcionTurnoForm.getTipo().equals("A")) {
				orderBy.append(campoFecha);
				sql.append(" AND I.FECHABAJA IS NULL ");
				sql.append(" AND I.FECHASOLICITUDBAJA IS NULL ");
				
				if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("S")) {						
					// ORDENAMOS POR ALGO???
					
				} else if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("P")) {
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NULL ");

				} else if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("C")) {
					orderBy.append(",I.FECHAVALIDACION");
					sql.append(" AND I.FECHAVALIDACION IS NOT NULL ");
					sql.append(" AND I.FECHADENEGACION IS NULL ");

				} else if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("D")) {
					orderBy.append(",I.FECHADENEGACION");
					sql.append(" AND I.FECHAVALIDACION IS NULL ");
					sql.append(" AND I.FECHADENEGACION IS NOT NULL ");
				}

			} else if (inscripcionTurnoForm.getTipo().equals("B")) {
				orderBy.append(campoFecha);
				sql.append(" AND I.FECHASOLICITUDBAJA IS NOT NULL ");
				
				if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("S")) {						
					// ORDENAMOS POR ALGO???
					
				} else if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("P")) {
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHADENEGACION IS NULL ");

				} else if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("C")) {
					orderBy.append(",I.FECHABAJA");
					sql.append(" AND I.FECHABAJA IS NOT NULL ");
					sql.append(" AND I.FECHADENEGACION IS NULL  ");

				} else if (inscripcionTurnoForm.getEstado() != null && inscripcionTurnoForm.getEstado().equals("D")) {
					orderBy.append(",I.FECHADENEGACION");
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHADENEGACION IS NOT NULL  ");
				}
			}
		}

		if (inscripcionTurnoForm.getFechaDesde() != null && !inscripcionTurnoForm.getFechaDesde().equals("")) {
			sql.append(" AND ");
			sql.append(campoFecha);
			sql.append(">=:");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionTurnoForm.getFechaDesde());
		}
		
		if (inscripcionTurnoForm.getFechaHasta() != null && !inscripcionTurnoForm.getFechaHasta().equals("")) {
			sql.append(" AND ");
			sql.append(campoFecha);
			sql.append("<=:");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionTurnoForm.getFechaHasta());
		}

		sql.append(" ORDER BY ");
		//sql.append(orderBy);
		sql.append(" NOMBRETURNO ");

		List<ScsInscripcionTurnoBean> alInscripcion = new ArrayList<ScsInscripcionTurnoBean>();
		try {
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				ScsInscripcionTurnoBean inscripcionBean = null;
				ScsTurnoBean turno = null;
				CenPersonaBean persona = null;
				CenColegiadoBean colegiado = null;

				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					inscripcionBean = (ScsInscripcionTurnoBean) this.hashTableToBean(htFila);
					inscripcionBean.setFechaValorAlta(UtilidadesHash.getString(htFila, "FECHAVALORALTA"));
					inscripcionBean.setFechaValorBaja(UtilidadesHash.getString(htFila, "FECHAVALORBAJA"));
					String estado = "No aplica";
					
					if (!inscripcionBean.getFechaBaja().equals("")) {
						estado = "Baja Confirmada";
					
					} else { 						
						if (inscripcionBean.getFechaValidacion().equals("")) {
							if (inscripcionBean.getFechaSolicitudBaja().equals("")) {
								if (inscripcionBean.getFechaDenegacion().equals("")) {
									estado = "Alta Pendiente";
									
								} else {
									estado = "Alta Denegada";
								}
	
							} else {
								if (inscripcionBean.getFechaDenegacion().equals("")) {
									estado = "Baja Pendiente";
									
								} else {
									estado = "Baja Denegada";
								}
							}
							
						} else {
	
							if (inscripcionBean.getFechaSolicitudBaja().equals("")) {
								estado = "Alta Confirmada";
								
							} else {
								if (inscripcionBean.getFechaDenegacion().equals("")) {
									estado = "Baja Pendiente";
									
								} else {
									estado = "Baja Denegada";
								} // IF FECHA DENEGACION
							} // IF FECHA SOLICITUD BAJA
						} // IF FECHA VALIDACION
					} // IF FECHA BAJA
					
					inscripcionBean.setEstado(estado);
					
					turno = new ScsTurnoBean();
					inscripcionBean.setTurno(turno);
					
					persona = new CenPersonaBean();
					colegiado = new CenColegiadoBean();
					persona.setColegiado(colegiado);
					inscripcionBean.setPersona(persona);

					turno.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_IDINSTITUCION));
					turno.setIdTurno(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_IDTURNO));
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					turno.setAbreviatura(UtilidadesHash.getString(htFila, "ABREVIATURA"));
					turno.setValidarInscripciones(UtilidadesHash.getString(htFila, ScsTurnoBean.C_VALIDARINSCRIPCIONES));
					turno.setGuardias(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_GUARDIAS));

					colegiado.setIdInstitucion(UtilidadesHash.getInteger(htFila, CenColegiadoBean.C_IDINSTITUCION));
					colegiado.setNColegiado(UtilidadesHash.getString(htFila, CenColegiadoBean.C_NCOLEGIADO));
					persona.setIdPersona(UtilidadesHash.getLong(htFila, CenPersonaBean.C_IDPERSONA));
					persona.setNombre(UtilidadesHash.getString(htFila, CenPersonaBean.C_NOMBRE));
					persona.setApellido1(UtilidadesHash.getString(htFila, CenPersonaBean.C_APELLIDOS1));
					persona.setApellido2(UtilidadesHash.getString(htFila, CenPersonaBean.C_APELLIDOS2));
					
					alInscripcion.add(inscripcionBean);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		
		return alInscripcion;
	} // getInscripcionesTurno()

	public ScsInscripcionTurnoBean getInscripcionTurno(Integer idInstitucion,Integer idTurno,Long idPersona,String fechaSolicitud,boolean conDatosTurno)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		if(conDatosTurno){
		sql.append(" SELECT  ");
		sql.append(" T.NOMBRE NOMBRETURNO, ");
		sql.append(" T.DESCRIPCION DESCRIPCIONTURNO,  T.ABREVIATURA ABREVIATURATURNO, ");
		sql.append(" T.GUARDIAS GUARDIAS, T.VALIDARJUSTIFICACIONES , ");
		sql.append(" T.VALIDARINSCRIPCIONES , T.DESIGNADIRECTA , ");
		sql.append(" T.REQUISITOS,  T.IDPERSONA_ULTIMO IDPERSONAULTIMO, ");
		sql.append(" T.ACTIVARRETRICCIONACREDIT, T.LETRADOACTUACIONES, T.LETRADOASISTENCIAS, ");
		sql.append(" A.NOMBRE AREA,    A.IDAREA IDAREA,   M.NOMBRE MATERIA, ");
		sql.append(" M.IDMATERIA IDMATERIA,   Z.NOMBRE ZONA,  Z.IDZONA IDZONA, ");
		sql.append(" SZ.NOMBRE SUBZONA,  SZ.IDSUBZONA IDSUBZONA,  ");
		sql.append(" PKG_SIGA_SJCS.FUN_SJ_PARTIDOSJUDICIALES(SZ.IDINSTITUCION,SZ.IDSUBZONA,Z.IDZONA) PARTIDOJUDICIAL, ");

		
		sql.append("   PP.NOMBREPARTIDA PARTIDAPRESUPUESTARIA, ");
		sql.append(" PP.IDPARTIDAPRESUPUESTARIA IDPARTIDAPRESUPUESTARIA, F_SIGA_GETRECURSO(GF.NOMBRE, 1) GRUPOFACTURACION, ");
		sql.append(" GF.IDGRUPOFACTURACION IDGRUPOFACTURACION, ");
		sql.append(" OC.IDORDENACIONCOLAS, OC.ALFABETICOAPELLIDOS, ");
		sql.append(" OC.FECHANACIMIENTO, OC.NUMEROCOLEGIADO ,   OC.ANTIGUEDADCOLA , ");
		
		
		sql.append(" DECODE(COL.COMUNITARIO,'1',COL.NCOMUNITARIO, COL.NCOLEGIADO) NCOLEGIADO, ");
		sql.append(" PER.NOMBRE,PER.APELLIDOS1,PER.APELLIDOS2 ");
		sql.append(" ,I.IDINSTITUCION,    I.IDPERSONA,    I.IDTURNO, ");
		sql.append(" I.FECHASOLICITUD,  I.OBSERVACIONESSOLICITUD, ");
		sql.append(" I.FECHAVALIDACION,  I.OBSERVACIONESVALIDACION, ");
		sql.append(" I.FECHASOLICITUDBAJA,     I.OBSERVACIONESBAJA,    I.FECHABAJA ");
		sql.append(" FROM SCS_INSCRIPCIONTURNO I,CEN_COLEGIADO COL,CEN_PERSONA PER, SCS_TURNO T, ");
		sql.append(" SCS_ORDENACIONCOLAS OC,SCS_PARTIDAPRESUPUESTARIA PP, SCS_GRUPOFACTURACION GF, ");
		sql.append(" SCS_MATERIA M,   SCS_AREA A,    SCS_SUBZONA SZ, ");
		sql.append(" SCS_ZONA Z ");
		sql.append(" WHERE I.IDINSTITUCION = COL.IDINSTITUCION  AND I.IDPERSONA = COL.IDPERSONA ");
		sql.append(" AND COL.IDPERSONA = PER.IDPERSONA ");
		sql.append(" AND I.IDINSTITUCION = T.IDINSTITUCION  AND I.IDTURNO= T.IDTURNO ");
		sql.append(" AND OC.IDORDENACIONCOLAS = T.IDORDENACIONCOLAS ");
		
		sql.append(" AND PP.IDINSTITUCION(+) = T.IDINSTITUCION ");
		sql.append(" AND PP.IDPARTIDAPRESUPUESTARIA(+) =       T.IDPARTIDAPRESUPUESTARIA ");
		sql.append(" AND GF.IDINSTITUCION = T.IDINSTITUCION ");
		sql.append(" AND GF.IDGRUPOFACTURACION =		       T.IDGRUPOFACTURACION ");
		sql.append(" AND M.IDINSTITUCION = T.IDINSTITUCION ");
		sql.append(" AND M.IDAREA = T.IDAREA		   AND M.IDMATERIA = T.IDMATERIA ");
		sql.append(" AND A.IDINSTITUCION = M.IDINSTITUCION   AND A.IDAREA = M.IDAREA ");
		sql.append(" AND SZ.IDINSTITUCION(+) = T.IDINSTITUCION   AND SZ.IDZONA(+) = T.IDZONA ");
		sql.append(" AND SZ.IDSUBZONA(+) = T.IDSUBZONA   AND Z.IDINSTITUCION(+) = T.IDINSTITUCION ");
		sql.append(" AND Z.IDZONA(+) = T.IDZONA AND ");
		}else{
			sql.append("SELECT * from scs_inscripcionturno I WHERE ");
			
		}

		
		
		
		sql.append(" I.IDINSTITUCION = ");
		sql.append(idInstitucion);

		sql.append(" AND I.IDTURNO = ");
		sql.append(idTurno);

		sql.append(" AND I.IDPERSONA = ");
		sql.append(idPersona);
		
		//en el caso de la guardia no tenemos fecha de solicitud de turno. 
		//En vez de obtenerla metemos que la fecha sea de baja sea null ya 
		//que solo puede tener activa una inscripcion al mismo turno
		if(fechaSolicitud!=null){
			sql.append(" AND I.FECHASOLICITUD = TO_DATE('");
			sql.append(fechaSolicitud);
			sql.append("','YYYY/MM/DD HH24:MI:SS')");
		}else{
			sql.append(" AND I.FECHABAJA IS NULL ");
		}


		ScsInscripcionTurnoBean inscripcionBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql.toString())) {
				
				ScsTurnoBean turno = null;
				CenPersonaBean persona = null;
				CenColegiadoBean colegiado = null;
				ScsPartidaPresupuestariaBean partidaPresupuestaria =null;
				ScsMateriaBean materia = null;
				ScsAreaBean area = null;
				ScsZonaBean zona = null;
				ScsSubzonaBean subZona =null;
				CenPartidoJudicialBean partidoJudicial = null;
				ScsGrupoFacturacionBean grupoFacturacion = null;
				ScsOrdenacionColasBean ordenacionColas = null;

				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean)this.hashTableToBean(htFila);
					if(conDatosTurno){
					
						turno = new ScsTurnoBean();
						inscripcionBean.setTurno(turno);
						persona = new CenPersonaBean();
						colegiado = new CenColegiadoBean();
						persona.setColegiado(colegiado);
						inscripcionBean.setPersona(persona);
	
						turno.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDINSTITUCION));
						turno.setIdTurno(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDTURNO));
						turno.setNombre(UtilidadesHash.getString(htFila,"NOMBRETURNO"));
						turno.setDescripcion(UtilidadesHash.getString(htFila,"DESCRIPCIONTURNO"));
						turno.setAbreviatura(UtilidadesHash.getString(htFila,"ABREVIATURATURNO"));
						turno.setGuardias(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_GUARDIAS));
						turno.setValidarJustificaciones(UtilidadesHash.getString(htFila,ScsTurnoBean.C_VALIDARJUSTIFICACIONES));
						turno.setValidarInscripciones(UtilidadesHash.getString(htFila,ScsTurnoBean.C_VALIDARINSCRIPCIONES));
						turno.setDesignaDirecta(UtilidadesHash.getString(htFila,ScsTurnoBean.C_DESIGNADIRECTA));
						turno.setRequisitos(UtilidadesHash.getString(htFila,ScsTurnoBean.C_REQUISITOS));
						turno.setIdPersonaUltimo(UtilidadesHash.getLong(htFila,ScsTurnoBean.C_IDPERSONAULTIMO));
						turno.setActivarRestriccionAcreditacion(UtilidadesHash.getString(htFila,ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT));
						turno.setVisibilidad(UtilidadesHash.getString(htFila,ScsTurnoBean.C_VISIBILIDAD));
						turno.setIdTipoTurno(UtilidadesHash.getString(htFila,ScsTurnoBean.C_IDTIPOTURNO));
						turno.setLetradoActuaciones(UtilidadesHash.getString(htFila,ScsTurnoBean.C_LETRADOACTUACIONES));
						turno.setLetradoAsistencias(UtilidadesHash.getString(htFila,ScsTurnoBean.C_LETRADOASISTENCIAS));
						
						
						ordenacionColas = new ScsOrdenacionColasBean();
						ordenacionColas.setAlfabeticoApellidos(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS));
						ordenacionColas.setAntiguedadCola(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_ANTIGUEDADCOLA));
						ordenacionColas.setFechaNacimiento(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_FECHANACIMIENTO));
						ordenacionColas.setNumeroColegiado(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_NUMEROCOLEGIADO));
						ordenacionColas.setIdOrdenacionColas(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_IDORDENACIONCOLAS));
						partidaPresupuestaria = new ScsPartidaPresupuestariaBean();
						partidaPresupuestaria.setIdPartidaPresupuestaria(UtilidadesHash.getInteger(htFila,ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA));
						partidaPresupuestaria.setNombrePartida(UtilidadesHash.getString(htFila,"PARTIDAPRESUPUESTARIA"));
						materia = new ScsMateriaBean();
						materia.setIdMateria(UtilidadesHash.getInteger(htFila,ScsMateriaBean.C_IDMATERIA));
						materia.setNombre(UtilidadesHash.getString(htFila,"MATERIA"));
						area = new ScsAreaBean();
						area.setIdArea(UtilidadesHash.getInteger(htFila,ScsAreaBean.C_IDAREA));
						area.setNombre(UtilidadesHash.getString(htFila,"AREA"));
						zona = new ScsZonaBean();
						zona.setIdZona(UtilidadesHash.getInteger(htFila,ScsZonaBean.C_IDZONA));
						zona.setNombre(UtilidadesHash.getString(htFila,"ZONA"));
						subZona = new ScsSubzonaBean();
						subZona.setIdSubzona(UtilidadesHash.getInteger(htFila,ScsSubzonaBean.C_IDSUBZONA));
						subZona.setNombre(UtilidadesHash.getString(htFila,"SUBZONA"));
						partidoJudicial = new CenPartidoJudicialBean();
	//					partidoJudicial.setIdPartido(UtilidadesHash.getInteger(htFila,CenPartidoJudicialBean.C_IDPARTIDO));
						partidoJudicial.setNombre(UtilidadesHash.getString(htFila,"PARTIDOJUDICIAL"));
						grupoFacturacion = new ScsGrupoFacturacionBean();
						grupoFacturacion.setIdGrupoFacturacion(UtilidadesHash.getInteger(htFila,ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION));
						grupoFacturacion.setNombre(UtilidadesHash.getString(htFila,"GRUPOFACTURACION"));
						turno.setPartidaPresupuestaria(partidaPresupuestaria);
						turno.setMateria(materia);
						turno.setArea(area);
						turno.setZona(zona);
						turno.setSubZona(subZona);
						turno.setPartidoJudicial(partidoJudicial);
						turno.setGrupoFacturacion(grupoFacturacion);
						turno.setOrdenacionColas(ordenacionColas);
						
						colegiado.setIdInstitucion(UtilidadesHash.getInteger(htFila,CenColegiadoBean.C_IDINSTITUCION));
						colegiado.setNColegiado(UtilidadesHash.getString(htFila,CenColegiadoBean.C_NCOLEGIADO));
						persona.setIdPersona(UtilidadesHash.getLong(htFila,CenPersonaBean.C_IDPERSONA));
						persona.setNombre(UtilidadesHash.getString(htFila,CenPersonaBean.C_NOMBRE));
						persona.setApellido1(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS1));
						persona.setApellido2(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS2));
					}
				}
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		return inscripcionBean;

	} 
	public ScsInscripcionTurnoBean getInscripcionTurno(Integer idInstitucion,Integer idTurno,Long idPersona)throws ClsExceptions{

		ScsInscripcionTurnoBean inscripcionBean = null;
		try {
			inscripcionBean = new ScsInscripcionTurnoBean();
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.usrbean);
			ScsTurnoBean turnoBean = turnoAdm.getTurnoInscripcion(idInstitucion, idTurno);
			inscripcionBean.setTurno(turnoBean);
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
			CenPersonaBean persona = personaAdm.getPersonaColegiado(idPersona,idInstitucion);
			inscripcionBean.setPersona(persona);
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		return inscripcionBean;

	}
	
	/**
	 * JPT: En caso de fecha nula, obtiene la ultima inscripcion de turno que tiene los siguientes datos:
	 * - Tiene fecha de validacion
	 * - Tiene fecha de baja 
	 * - No tiene fecha de denegacion
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param fecha
	 * @return
	 * @throws ClsExceptions
	 */
	public ScsInscripcionTurnoBean getInscripcion(Integer idInstitucion, Integer idTurno, Long idPersona, String fecha) throws ClsExceptions {
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador = 0;
		
		sql.append(" select * from scs_inscripcionturno where   SCS_INSCRIPCIONTURNO.idinstitucion = :");
	    contador ++;
		sql.append(contador);		
		htCodigos.put(new Integer(contador),idInstitucion);
		
		sql.append(" AND SCS_INSCRIPCIONTURNO.idpersona = :");
	    contador ++;	    
		sql.append(contador);		
		htCodigos.put(new Integer(contador),idPersona);
		
		sql.append(" AND  SCS_INSCRIPCIONTURNO.IDTURNO = :");
		contador ++;		
		sql.append(contador);		
		htCodigos.put(new Integer(contador),idTurno);
		
		// si la fecha es nula es porque queremos la ultima inscripcion CON BAJA
		if(fecha!=null){
			sql.append(" AND SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL ");
			sql.append(" AND (SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL OR ");
			
			sql.append(" TRUNC(SCS_INSCRIPCIONTURNO.FECHABAJA) >= ");
			if(fecha.equalsIgnoreCase("sysdate")){
				sql.append("TRUNC(sysdate)");
				
			}else{
				contador ++;
				sql.append(":");
				sql.append(contador);
				htCodigos.put(new Integer(contador),fecha);					
			}			
			
			sql.append(") ");
			sql.append(" AND (SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL AND ");
			sql.append(" TRUNC(SCS_INSCRIPCIONTURNO.FECHAVALIDACION) <= ");
			if(fecha.equalsIgnoreCase("sysdate")){
				sql.append("TRUNC(sysdate)");
				
			}else{
				contador ++;
				sql.append(":");
				sql.append(contador);
				htCodigos.put(new Integer(contador),fecha);	
				
			}
			sql.append(") ");
			
		} else {					
			sql.append(" AND SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL ");
			sql.append(" AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NOT NULL ");			
			//sql.append(" AND SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL "); 
			
			sql.append(" ORDER BY SCS_INSCRIPCIONTURNO.FECHABAJA ");			
		}		
			
		ScsInscripcionTurnoBean inscripcionBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.findBind(sql.toString(),htCodigos)) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean)this.hashTableToBean(htFila);					
				}
			} 
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		
		return inscripcionBean;
	}
	
	/**
	 * Obtiene las inscripciones de turno de una persona para dar de baja
	 * 
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable> getInscripcionesTurnoParaBaja(String idInstitucion, String idPersona)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT I.IDINSTITUCION, ");
			sql.append(" I.IDPERSONA, ");
			sql.append(" I.IDTURNO, ");
			sql.append(" I.FECHASOLICITUD, ");
			sql.append(" I.OBSERVACIONESSOLICITUD, ");
			sql.append(" I.FECHAVALIDACION, ");
			sql.append(" I.OBSERVACIONESVALIDACION, ");
			sql.append(" I.FECHASOLICITUDBAJA, ");
			sql.append(" I.OBSERVACIONESBAJA, ");
			sql.append(" I.FECHABAJA, ");
			sql.append(" T.VALIDARINSCRIPCIONES ");
		sql.append(" FROM SCS_INSCRIPCIONTURNO I,");
			sql.append(" SCS_TURNO T ");
		sql.append(" WHERE I.IDTURNO = T.IDTURNO ");
		sql.append(" AND I.IDINSTITUCION = T.IDINSTITUCION ");
		sql.append(" AND I.FECHABAJA IS NULL ");
		sql.append(" AND I.FECHADENEGACION IS NULL ");
		
		sql.append(" AND I.IDINSTITUCION = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		
		sql.append(" AND I.IDPERSONA = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idPersona);
		
		List<Hashtable> alInscripciones = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alInscripciones = new ArrayList<Hashtable>();
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();            		
        			alInscripciones.add(htFila);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return alInscripciones;					
	}
	
	public List<Hashtable> getInscripcionesTurnoPendientesValidar(String idInstitucion, String idTurno,boolean isValidacionInscripcion)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append(" I.IDINSTITUCION,    I.IDPERSONA,    I.IDTURNO, ");
		sql.append(" I.FECHASOLICITUD,  I.OBSERVACIONESSOLICITUD, ");
		sql.append(" I.FECHAVALIDACION,  I.OBSERVACIONESVALIDACION, ");
		sql.append(" I.FECHASOLICITUDBAJA,     I.OBSERVACIONESBAJA,    I.FECHABAJA ");
		sql.append(" FROM SCS_INSCRIPCIONTURNO I ");
		sql.append(" WHERE  ");
		//si es validacion de alta
		
		if(isValidacionInscripcion)
			sql.append(" I.FECHABAJA IS NULL AND I.FECHAVALIDACION IS NULL ");
		else//si no es que es de baja
			sql.append(" I.FECHABAJA IS NULL AND I.FECHASOLICITUDBAJA IS NOT NULL ");
		
		sql.append(" AND I.FECHADENEGACION IS NULL ");
		sql.append(" AND I.IDINSTITUCION = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" AND I.IDTURNO = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTurno);
		
		
		List<Hashtable> alInscripciones = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alInscripciones = new ArrayList<Hashtable>();
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
        			alInscripciones.add(htFila);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alInscripciones;
		
		
		
	}
	
	public void validarInscripcionesPendientes(String idInstitucion,String idTurno) throws ClsExceptions{
		
		UserTransaction tx =null;
	    try {
//	    	tx = usrbean.getTransaction();
//	    	tx.begin();
	    	validarInscripciones(idInstitucion, idTurno);
	    	validarBajas(idInstitucion, idTurno);
//	    	tx.commit();
	    	
	    	
	    	
			
		} catch (Exception e) {
//			try {
//				tx.rollback();
//			} catch (Exception e1) {
//			}
			throw new ClsExceptions("Error al validarInscripcionesPendientes");
		}
		
	}
	
	private void validarInscripciones(String idInstitucion,String idTurno) throws ClsExceptions, NumberFormatException, ParseException{
		
		
		List alInscripcionesValidar = getInscripcionesTurnoPendientesValidar(idInstitucion,idTurno,true);
		
		if (alInscripcionesValidar != null &&alInscripcionesValidar.size()>0){
			//dando de baja todos los turnos
			InscripcionTurno inscripcion;
			for (int i = 0; i < alInscripcionesValidar.size(); i++) {
				Hashtable turnoHash = (Hashtable)alInscripcionesValidar.get(i);
				Long idPersona = UtilidadesHash.getLong(turnoHash, "IDPERSONA");
//				String tipoGuardias = UtilidadesHash.getString(turnoHash, "GUARDIAS");
				inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(idInstitucion), new Integer(idTurno), idPersona,
						(String) turnoHash.get("FECHASOLICITUD"), usrbean, false);
				inscripcion.validarAlta("sysdate", "Validado al modificar configuracion de turno",  usrbean);
//				String fechaSolicitudBaja =(String) turnoHash.get("FECHASOLICITUDBAJA");

			}
		}
		
		
	
		
	}
	private void validarBajas(String idInstitucion,String idTurno) throws ClsExceptions, NumberFormatException, ParseException{
		
		
		List alInscripcionesValidar = getInscripcionesTurnoPendientesValidar(idInstitucion,idTurno,false);
		
		if (alInscripcionesValidar != null &&alInscripcionesValidar.size()>0){
			//dando de baja todos los turnos
			InscripcionTurno inscripcion;
			for (int i = 0; i < alInscripcionesValidar.size(); i++) {
				Hashtable turnoHash = (Hashtable)alInscripcionesValidar.get(i);
				Long idPersona = UtilidadesHash.getLong(turnoHash, "IDPERSONA");
//				String tipoGuardias = UtilidadesHash.getString(turnoHash, "GUARDIAS");
				inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(idInstitucion), new Integer(idTurno), idPersona,
						(String) turnoHash.get("FECHASOLICITUD"), usrbean, false);
				inscripcion.validarBaja("sysdate",(String) turnoHash.get("FECHAVALIDACION"),"--", null, usrbean);

			}
		}
	}
	
	/**
	 * Obtiene de BD las inscripciones ordenadas para formar la cola de un turno dada una fecha
	 * 
	 * @param idinstitucion
	 * @param idturno
	 * @param fecha
	 * @param order
	 * @return
	 * @throws ClsExceptions
	 */
 	public Vector<ScsInscripcionTurnoBean> getColaTurno(String idinstitucion, String idturno, String fecha, String order)
		throws ClsExceptions
	{
		if (idinstitucion == null || idinstitucion.equals(""))
			return null;
		if (idturno == null || idturno.equals(""))
			return null;
		if (fecha == null || fecha.equals(""))
			fecha = "null";
		else if(!fecha.trim().equalsIgnoreCase("sysdate"))
			fecha = "'"+fecha.trim()+"'";
		
		String consulta = 
		"Select "+
			"       (case when Ins.Fechavalidacion Is Not Null "+
			"              And Trunc(Ins.Fechavalidacion) <= nvl("+fecha+",  Ins.Fechavalidacion) "+
			"              And (Ins.Fechabaja Is Null Or "+
			"                   Trunc(Ins.Fechabaja) > nvl("+fecha+", '01/01/1900')) "+
			"             then '1' "+
			"             else '0' "+
			"        end) Activo, "+
			" Ins.Idinstitucion,"+
			"       Ins.Idturno, " +
			" TO_CHAR(TRUNC(Ins.fechavalidacion),'DD/MM/YYYY') AS fechavalidacion, "+
		    "   TO_CHAR(trunc(Ins.fechabaja),'DD/MM/YYYY') AS fechabaja, "+
		    "    Ins.fechasolicitud AS fechaSolicitud, "+
		    "       Per.Nifcif,"+
			"       Per.Idpersona,"+
			"       Per.Nombre, " +
			"       Per.Apellidos1, " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) apellidos2, " +
			"       Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) Ncolegiado, " +
			
			"       Per.Apellidos1 || " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) "+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+", " +
			"       Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) "+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+", " +
			"       Per.Fechanacimiento "+ScsOrdenacionColasBean.C_FECHANACIMIENTO+", " +
			"       Ins.Fechavalidacion "+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+" " +
			"  From Scs_Turno              Tur, " +
			"       Cen_Colegiado          Col, " +
			"       Cen_Persona            Per, " +
			"       Scs_Inscripcionturno   Ins " +
			" Where Col.Idpersona = Per.Idpersona " +
			"   And Ins.Idinstitucion = Tur.Idinstitucion " +
			"   And Ins.Idturno = Tur.Idturno " +
			"   And Ins.Idinstitucion = Col.Idinstitucion " +
			"   And Ins.Idpersona = Col.Idpersona " +
			
			//cuando no se pasa fecha, se sacan todas las validadas (en cualquier fecha)
			"   And Ins.Fechavalidacion Is Not Null " +
/*
			"   And Trunc(Ins.Fechavalidacion) <= nvl("+fecha+",  Ins.Fechavalidacion) " +
				//cuando no se pasa fecha, se sacan aunque esten de baja
			"   And (Ins.Fechabaja Is Null Or   Trunc(Ins.Fechabaja) > nvl("+fecha+", '01/01/1900')) " +*/
			"   And Tur.Idinstitucion = "+idinstitucion+" " +
			"   And Tur.Idturno = "+idturno+" ";
		
		if (! (order == null || order.equals("")))
			consulta += " order by " + order;
		
		Vector<ScsInscripcionTurnoBean> datos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
            if (rc.find(consulta)) {
            	datos = new Vector<ScsInscripcionTurnoBean>();
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		ScsInscripcionTurnoBean inscripcionBean = new ScsInscripcionTurnoBean();
            		inscripcionBean.setIdPersona(UtilidadesHash.getLong(htFila, ScsInscripcionTurnoBean.C_IDPERSONA));
            		inscripcionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsInscripcionTurnoBean.C_IDINSTITUCION));
            		inscripcionBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsInscripcionTurnoBean.C_IDTURNO));
            		inscripcionBean.setFechaValidacion(UtilidadesHash.getString(htFila, ScsInscripcionTurnoBean.C_FECHAVALIDACION));
            		inscripcionBean.setFechaSolicitud(UtilidadesHash.getString(htFila, ScsInscripcionTurnoBean.C_FECHASOLICITUD));
            		inscripcionBean.setFechaBaja(UtilidadesHash.getString(htFila, ScsInscripcionTurnoBean.C_FECHABAJA));
            		inscripcionBean.setEstado(UtilidadesHash.getString(htFila, "ACTIVO"));
            		CenPersonaBean personaBean = new CenPersonaBean(
            				inscripcionBean.getIdPersona(),(String)htFila.get(CenPersonaBean.C_NOMBRE),(String)htFila.get(CenPersonaBean.C_APELLIDOS1),
            				(String)htFila.get(CenPersonaBean.C_APELLIDOS2),(String)htFila.get(CenColegiadoBean.C_NCOLEGIADO));
            		personaBean.setNIFCIF(UtilidadesHash.getString(htFila, CenPersonaBean.C_NIFCIF));
            		inscripcionBean.setPersona(personaBean);
            		datos.add(inscripcionBean);
            	}
            } 
       } catch (Exception e) {
    	   throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
       }
		return datos;
	} //getColaTurno()
 	
 	public String getQueryInscripcionesTurnos(String idInstitucion, String idPersona, boolean historico,String fecha) throws ClsExceptions 
	{
		String sql = new String();
		sql = "Select ";
		sql += ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDTURNO													+ " IDTURNO";
		
		sql +=","+ ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE + " NOMBRE";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_ABREVIATURA+ " ABREVIATURA";
		sql +=","+ScsAreaBean.T_NOMBRETABLA+"."+ScsAreaBean.C_NOMBRE+ " AREA";
		sql +=","+ScsAreaBean.T_NOMBRETABLA+"."+ScsAreaBean.C_IDAREA	+ " IDAREA";
		sql +=","+ScsMateriaBean.T_NOMBRETABLA+"."+ScsMateriaBean.C_NOMBRE	+" MATERIA";
		sql +=","+ScsMateriaBean.T_NOMBRETABLA+"."+ScsMateriaBean.C_IDMATERIA	+" IDMATERIA";
		sql +=", decode("+ScsTurnoBean.T_NOMBRETABLA+".VISIBILIDAD,'1', 'Alta','Baja') ESTADOLOGICO ";
		sql +=","+ScsZonaBean.T_NOMBRETABLA+"."+ScsZonaBean.C_NOMBRE+" ZONA";
		sql +=","+ScsZonaBean.T_NOMBRETABLA+"."+ScsZonaBean.C_IDZONA+" IDZONA";
		sql +=","+ScsSubzonaBean.T_NOMBRETABLA+"."+ScsSubzonaBean.C_NOMBRE+" SUBZONA";
		sql +=","+ScsSubzonaBean.T_NOMBRETABLA+"."+ScsSubzonaBean.C_IDSUBZONA	+" IDSUBZONA";
		sql +=","+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_NOMBRE+" PARTIDOJUDICIAL";
		sql +=","+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_IDPARTIDO	+" IDPARTIDOJUDICIAL";
		sql +=","+ScsPartidaPresupuestariaBean.T_NOMBRETABLA+"."+ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA	+" PARTIDAPRESUPUESTARIA";
		sql +=","+ScsPartidaPresupuestariaBean.T_NOMBRETABLA+"."+ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA+" IDPARTIDAPRESUPUESTARIA";
		sql +=","+UtilidadesMultidioma.getCampoMultidiomaSimple(ScsGrupoFacturacionBean.T_NOMBRETABLA+"."+ScsGrupoFacturacionBean.C_NOMBRE,this.usrbean.getLanguage())+" GRUPOFACTURACION";
		sql +=","+ScsGrupoFacturacionBean.T_NOMBRETABLA+"."+ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION	+" IDGRUPOFACTURACION";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS	+ " GUARDIAS";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARJUSTIFICACIONES+ " VALIDARJUSTIFICACIONES";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARINSCRIPCIONES	+" VALIDARINSCRIPCIONES";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_DESIGNADIRECTA+" DESIGNADIRECTA";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_DESCRIPCION	+" DESCRIPCION";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_REQUISITOS	+" REQUISITOS";
		sql +=","+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDPERSONAULTIMO+" IDPERSONAULTIMO";
		sql +=","+ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS	+" IDORDENACIONCOLAS";
		sql +=","+ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+" ALFABETICOAPELLIDOS";
		sql +=","+ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_FECHANACIMIENTO	+" EDAD";
		sql +=","+ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO	+" ANTIGUEDAD";
		sql +=","+ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA	+" ANTIGUEDADENCOLA";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD	+" OBSERVACIONESSOLICITUD";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION	+" OBSERVACIONESVALIDACION";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA+" OBSERVACIONESBAJA";
		
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD+" FECHASOLICITUD";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA+" FECHASOLICITUDBAJA";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+" FECHAVALIDACION";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHABAJA+" FECHABAJA";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION+" OBSERVACIONESDENEGACION";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA+" OBSERVACIONESVALBAJA";
		sql +=","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHADENEGACION+" FECHADENEGACION";
		sql +=",NVL("+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHADENEGACION+","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+") FECHAVALOR";
		sql +=",TO_CHAR(NVL("+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHADENEGACION+","+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHABAJA+"),'dd/mm/yyyy') FECHAVALORBAJA";

		sql +=", Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES(SCS_SUBZONA.idinstitucion,SCS_SUBZONA.IDSUBZONA,SCS_ZONA.IDZONA) PARTIDOS ";					
		sql +=", DECODE(FECHAVALIDACION, NULL, TO_CHAR(FECHASOLICITUD, 'YYYYMMDD'), '00000000') ||"+
		" DECODE(FECHABAJA, NULL, TO_CHAR(FECHASOLICITUD, 'YYYYMMDD'), '00000000') ||"+
		" DECODE(FECHABAJA, NULL, '99999999', TO_CHAR(FECHABAJA, 'YYYYMMDD')) ORDEN";
		
		sql +=" FROM SCS_TURNO,SCS_PARTIDAPRESUPUESTARIA,SCS_GRUPOFACTURACION,SCS_MATERIA,SCS_AREA,SCS_SUBZONA,SCS_ZONA,CEN_PARTIDOJUDICIAL,SCS_INSCRIPCIONTURNO,SCS_ORDENACIONCOLAS";
		
		
		String where=	" WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion (+)= SCS_TURNO.idinstitucion"+            
						" AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria (+)= SCS_TURNO.idpartidapresupuestaria"+  
						" AND SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion"+
						" AND SCS_GRUPOFACTURACION.idgrupofacturacion = SCS_TURNO.idgrupofacturacion"+
						" AND SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion"+
						" AND SCS_MATERIA.idarea = SCS_TURNO.idarea"+
						" AND SCS_MATERIA.idmateria = SCS_TURNO.idmateria"+
						" AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion"+
						" AND SCS_AREA.idarea = SCS_MATERIA.idarea"+
						" AND SCS_SUBZONA.idinstitucion (+)= SCS_TURNO.idinstitucion"+
						" AND SCS_SUBZONA.idzona (+)= SCS_TURNO.idzona"+
						" AND SCS_SUBZONA.idsubzona (+)= SCS_TURNO.idsubzona"+
						" AND SCS_ZONA.idinstitucion (+)= SCS_TURNO.idinstitucion"+
						" AND SCS_ZONA.idzona (+)= SCS_TURNO.idzona"+
						" AND CEN_PARTIDOJUDICIAL.idpartido (+)= SCS_SUBZONA.idpartido"+
						" and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas "+
						" AND SCS_TURNO.idinstitucion ="+idInstitucion+
						" AND SCS_INSCRIPCIONTURNO.idinstitucion = "+idInstitucion+
						" AND SCS_INSCRIPCIONTURNO.idturno = SCS_TURNO.idturno"+
						
						" AND SCS_INSCRIPCIONTURNO.idpersona = "+idPersona+" ";
						if (!historico){
							where +=" AND SCS_TURNO.VISIBILIDAD = '1'";
							where += getWhereInscripcion(fecha);
						}
		sql += where;
		sql += " ORDER BY NOMBRE,FECHASOLICITUD";
		return sql;
	}
 	private String getWhereInscripcion(String fecha){
 		String where = "";
 		if(fecha!=null){
			//String fechaFmt =GstDate.getApplicationFormatDate("", fecha);
			if(fecha.equalsIgnoreCase("sysdate"))
				fecha = "trunc(sysdate)";
			else
				fecha = "'"+fecha+"'";
		}else{
			fecha = "trunc(sysdate)";
		}
			
		//NO SACAR LAS CANCELADAS
		where += " AND ( ";
		   //PENDIENTES DE ALTA
		where += " (SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL AND SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NULL ";
		where += " AND SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NULL) ";
		     
		where += " OR ";
		     //VALIDADOS DE ALTA
		where += " (SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL AND ";
		where += " TRUNC(SCS_INSCRIPCIONTURNO.FECHAVALIDACION) <= "+fecha.trim()+" "; 
		where += " AND (SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL ";
		where += " OR (SCS_INSCRIPCIONTURNO.FECHABAJA IS NOT NULL AND ";
		where += " TRUNC(SCS_INSCRIPCIONTURNO.FECHABAJA) > "+fecha.trim()+")))" ;
	  
		where += " OR ";
		     // PENDIENTES DE BAJA
		where += " (SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NOT NULL AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL AND SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL) ";
		       
		       // BAJA DENEGADA
		where += " OR "; 
		where += " (SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NOT NULL AND SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NOT NULL) ";
		where += " ) ";
		

		return where;
	
 	}
 	public PaginadorBind getTurnosClientePaginador (Integer idInstitucion, Long idPersona, boolean historico,String fecha) throws ClsExceptions, SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigos = new Hashtable();
			String select = getQueryInscripcionesTurnos(idInstitucion.toString(), idPersona.toString(), historico,fecha);
			paginador = new PaginadorBind(select,codigos);

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getServiciosSolicitadosPaginador.");
		}
		return paginador;  
		
	}

	/**
	 * Obtiene las inscripcion activa de la persona en la guardia en la fecha dada
	 */
	public ScsInscripcionTurnoBean getInscripcionActiva(
			String idinstitucion,
			String idturno,
			String idpersona,
			String fecha) throws ClsExceptions
	{
		if (idinstitucion == null || idinstitucion.equals(""))		return null;
		if (idturno == null || idturno.equals(""))					return null;
		if (idpersona == null || idpersona.equals(""))				return null;
		if (fecha == null || fecha.equals(""))						return null;
		if(!fecha.equals("sysdate"))
			fecha = "'"+fecha+"'";
		
		String consulta = 
			"Select " +
			getBaseConsultaInscripciones() +
			
			"   And Ins.Fechavalidacion Is Not Null " +
			"   And Tur.Idinstitucion = "+idinstitucion+" " +
			"   And Tur.Idturno = "+idturno+" " +
			
		    "   And Ins.Fechavalidacion Is Not Null " +
			"   And Trunc(Ins.Fechavalidacion) <= nvl("+fecha+",  Ins.Fechavalidacion) " +
			"   And (Ins.Fechabaja Is Null Or " +
			"        Trunc(Ins.Fechabaja) > nvl("+fecha+", '01/01/1900')) "+
			"    and Ins.idpersona ="+idpersona;
		
		
		Vector<ScsInscripcionTurnoBean> datos = null;
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta)) {
				datos = new Vector<ScsInscripcionTurnoBean>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();

					ScsInscripcionTurnoBean inscripcionBean = new ScsInscripcionTurnoBean();
					inscripcionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsInscripcionTurnoBean.C_IDINSTITUCION));
					inscripcionBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsInscripcionTurnoBean.C_IDTURNO));
					inscripcionBean.setIdPersona(UtilidadesHash.getLong(htFila, ScsInscripcionTurnoBean.C_IDPERSONA));
					inscripcionBean.setFechaSolicitud(UtilidadesHash.getString(htFila, ScsInscripcionTurnoBean.C_FECHASOLICITUD));
					inscripcionBean.setFechaValidacion(UtilidadesHash.getString(htFila, ScsInscripcionTurnoBean.C_FECHAVALIDACION));
					inscripcionBean.setFechaBaja(UtilidadesHash.getString(htFila, ScsInscripcionTurnoBean.C_FECHABAJA));
					CenPersonaBean personaBean = new CenPersonaBean(inscripcionBean.getIdPersona(), (String) htFila
							.get(CenPersonaBean.C_NOMBRE), (String) htFila.get(CenPersonaBean.C_APELLIDOS1),
							(String) htFila.get(CenPersonaBean.C_APELLIDOS2), (String) htFila
									.get(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO));
					inscripcionBean.setPersona(personaBean);
					datos.add(inscripcionBean);
				}
			}else
				return null;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos.get(0);
	} //getInscripcionActiva()

	public String getBaseConsultaInscripciones() {
		return new String(
			"       Ins.Idinstitucion,"+
			"       Ins.Idturno, " +
			"       Per.Idpersona, " +
			"       Ins.fechasolicitud, "+
			"       TO_CHAR(TRUNC(Ins.fechavalidacion),'DD/MM/YYYY') As Fechavalidacion, "+
		    "       TO_CHAR(trunc(Ins.fechabaja),'DD/MM/YYYY') As Fechabaja, "+
		    
			"       Per.Nombre, " +
			"       Per.Apellidos1, " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) apellidos2, " +
			"       Per.Apellidos1 || " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) "+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+", " +
			"       Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) "+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+", " +
			"       Per.Fechanacimiento "+ScsOrdenacionColasBean.C_FECHANACIMIENTO+", " +
			"       Ins.Fechavalidacion AS "+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+" " +
			"  From Scs_Turno              Tur, " +
			"       Cen_Colegiado          Col, " +
			"       Cen_Persona            Per, " +
			"       Scs_Inscripcionturno   Ins " +
			" Where Col.Idpersona = Per.Idpersona " +
			"   And Ins.Idinstitucion = Tur.Idinstitucion " +
			"   And Ins.Idturno = Tur.Idturno " +
			"   And Ins.Idinstitucion = Col.Idinstitucion " +
			"   And Ins.Idpersona = Col.Idpersona ");
	}
	
	/**
	 * JPT: Obtiene la fecha de inscripciones de guardias que es neceseario comprobar para dar de baja una inscripcion de turno
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public String getFechaGuardiasTurno(String idInstitucion, String idTurno, String idPersona) throws ClsExceptions {
		String fecha = "";
		
		String sql = "SELECT NVL(IG.FECHABAJA, IG.FECHAVALIDACION) FECHAGUARDIAS " +
			" FROM SCS_INSCRIPCIONGUARDIA IG " +
			" WHERE IG.IDINSTITUCION = " + idInstitucion + 
				" AND IG.IDTURNO = " + idTurno +
				" AND IG.IDPERSONA = " + idPersona +
				" AND (IG.FECHABAJA IS NOT NULL " +
				" OR (IG.FECHABAJA IS NULL " +
				" AND IG.FECHAVALIDACION IS NOT NULL)) " +
			" ORDER BY FECHAGUARDIAS DESC";
				 			
		try {
			RowsContainer rc = new RowsContainer();			
			rc.query(sql);
			
			if (rc.size() > 0) {
				Row fila = (Row) rc.get(0);
				fecha = fila.getString("FECHAGUARDIAS");
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar ScsInscripcionTurnoAdm.getFechaGuardiasTurno.");
		}
		
		return fecha;
	}
	
	/**
	 * Borrar un turno que está en estado de alta pendiente
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param FechaSolicitud
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean borrarInscripcionTurnoPendiente(String idInstitucion, String idTurno, String idPersona, String FechaSolicitud) throws ClsExceptions {
		boolean resultado = false;
		
		UserTransaction tx = null;
	    try {
	    	tx = usrbean.getTransaction();
	    	tx.begin();
	    	
	    	// Preparamos las fechas:
	    	// Si provocan excepcione es porque no estan en formato largo sino en corto. En ese caso tomamos directamente su valor del bean:
	    	String sFechaSolicitud = "";
	    	try {
	    		sFechaSolicitud = GstDate.getFormatedDateShort("ES", FechaSolicitud);
			
			} catch (Exception e) {
				sFechaSolicitud = FechaSolicitud;
			}
	    	
	    	// Hay que dar de baja las guardias del mismo dia que la inscripcion de turno
	    	String sql = "DELETE SCS_INSCRIPCIONGUARDIA IG " +
	    		" WHERE IG.IDINSTITUCION = " + idInstitucion + 
					" AND IG.IDTURNO = " + idTurno +
					" AND IG.IDPERSONA = " + idPersona +
					" AND IG.FECHASUSCRIPCION >= TO_DATE('" + sFechaSolicitud + "', '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "') "; 	    	
	    	resultado = deleteSQL(sql);
	    	
	    	sql = "DELETE SCS_INSCRIPCIONTURNO IT " +
	    		" WHERE IT.IDINSTITUCION = " + idInstitucion + 
					" AND IT.IDTURNO = " + idTurno +
					" AND IT.IDPERSONA = " + idPersona +
					" AND IT.FECHASOLICITUD = TO_DATE('" + FechaSolicitud + "', '" + ClsConstants.DATE_FORMAT_SQL + "') "; 	    	
	    	if (deleteSQL(sql)) {
	    		resultado = true;
	    		tx.commit();
	    		
	    	} else {
	    		resultado = false;
	    		tx.rollback();
	    	}
	    	
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
			}
			throw new ClsExceptions("Error al borrarInscripcionTurnoPendiente");
		}	
	    
	    return resultado;
	}	
	
	/**
	 * Obtiene la inscripcion de turno de la persona, que esta validada y sin dar de baja
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public ScsInscripcionTurnoBean getInscripcionValidadaSinBaja(Integer idInstitucion, Integer idTurno, Long idPersona) throws ClsExceptions {

		String sql = " SELECT I.IDINSTITUCION, " +
				" I.IDPERSONA, " +
				" I.IDTURNO, " +
				" I.FECHASOLICITUD, " +
				" I.OBSERVACIONESSOLICITUD, " +
				" I.FECHAVALIDACION, " +
				" I.OBSERVACIONESVALIDACION, " +
				" I.FECHASOLICITUDBAJA, " +
				" I.OBSERVACIONESBAJA, " +
				" I.FECHABAJA " +
			" FROM SCS_INSCRIPCIONTURNO I " +
			" WHERE I.IDINSTITUCION = " + idInstitucion +
				" AND I.IDTURNO = " + idTurno +
				" AND I.IDPERSONA = " + idPersona +
				" AND I.FECHAVALIDACION IS NOT NULL " +
				" AND I.FECHABAJA IS NULL";
				
		ScsInscripcionTurnoBean inscripcionBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean) this.hashTableToBean(htFila);
				}
			} 
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		
		return inscripcionBean;
	}	
	
	/**
	 * Obtiene la ultima inscripcion de turno, del turno indicado
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public ScsInscripcionTurnoBean getInscripcionUltima (Integer idInstitucion, Integer idTurno, Long idPersona) throws ClsExceptions {
		String sql = " SELECT I.IDINSTITUCION, " +
				" I.IDPERSONA, " +
				" I.IDTURNO, " +
				" I.FECHASOLICITUD, " +
				" I.OBSERVACIONESSOLICITUD, " +
				" I.FECHAVALIDACION, " +
				" I.OBSERVACIONESVALIDACION, " +
				" I.FECHASOLICITUDBAJA, " +
				" I.OBSERVACIONESBAJA, " +
				" I.FECHABAJA " +			
			" FROM SCS_INSCRIPCIONTURNO I " +
			" WHERE I.IDINSTITUCION = " + idInstitucion +
				" AND I.IDTURNO = " + idTurno +
				" AND I.IDPERSONA = " + idPersona +
				" AND I.FECHAVALIDACION IS NOT NULL " +
			" ORDER BY FECHAVALIDACION DESC";
				
		ScsInscripcionTurnoBean inscripcionBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean) this.hashTableToBean(htFila);
				}
			} 
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		
		return inscripcionBean;
	}		
	
	/**
	 * Obtiene las inscripciones de turno de un turno y una institucion, que no estan dadas de baja
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<ScsInscripcionTurnoBean> getInscripcionesTurnoSinBaja(String idInstitucion, String idTurno) throws ClsExceptions {

		String sql = " SELECT I.IDINSTITUCION, " +
				" I.IDPERSONA, " +
				" I.IDTURNO, " +
				" I.FECHASOLICITUD, " +
				" I.OBSERVACIONESSOLICITUD, " +
				" I.FECHAVALIDACION, " +
				" I.OBSERVACIONESVALIDACION, " +
				" I.FECHASOLICITUDBAJA, " +
				" I.OBSERVACIONESBAJA, " +
				" I.FECHABAJA " +
			" FROM SCS_INSCRIPCIONTURNO I " +
			" WHERE I.IDINSTITUCION = " + idInstitucion +
				" AND I.IDTURNO = " + idTurno +
				" AND I.FECHAVALIDACION IS NOT NULL " +
				" AND I.FECHABAJA IS NULL";
		
		Vector<ScsInscripcionTurnoBean> datos = null;
		try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql)) {
				
				datos = new Vector<ScsInscripcionTurnoBean>();
    			for (int i = 0; i < rc.size(); i++){

					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) this.hashTableToBean(htFila);
					
					datos.add(inscripcionBean);
				}
			} 
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		
		return datos;
	}		
}