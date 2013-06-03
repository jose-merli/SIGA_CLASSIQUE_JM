
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
import com.siga.Utilidades.UtilidadesString;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.action.GestionInscripcionesTGAction;
import com.siga.gratuita.form.InscripcionTGForm;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_INSCRIPCIONTURNO
 */
public class ScsInscripcionTurnoAdm extends MasterBeanAdministrador {
	
	private static final String camposSelect =  ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDINSTITUCION + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHAVALIDACION + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHABAJA + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHADENEGACION + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION + ", " +
												ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA;

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsInscripcionTurnoAdm (UsrBean usuario) {
		super( ScsInscripcionTurnoBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Devuelve los campos que queremos recuperar desde el select
	 * para rellenar la tabla de la página "listarTurnos.jsp"
	 * 
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposTabla(){
		String[] campos = {	ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDINSTITUCION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO,					
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHAVALIDACION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHABAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, 	
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHADENEGACION,
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION};
		return campos;
	}
	
	/** @return conjunto de datos con los nombres de todos los campos del bean */
	protected String[] getCamposBean() {
		String[] campos = {	ScsInscripcionTurnoBean.C_IDPERSONA,				ScsInscripcionTurnoBean.C_IDINSTITUCION,
							ScsInscripcionTurnoBean.C_IDTURNO,					ScsInscripcionTurnoBean.C_FECHASOLICITUD,
							ScsInscripcionTurnoBean.C_FECHAVALIDACION,			ScsInscripcionTurnoBean.C_FECHABAJA,
							ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, 	ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,
							ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,		ScsInscripcionTurnoBean.C_USUMODIFICACION,
							ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,		ScsInscripcionTurnoBean.C_FECHAMODIFICACION,
							ScsInscripcionTurnoBean.C_FECHADENEGACION,			ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION,
							ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA};
		return campos;
	}
	
	/** @return conjunto de datos con los nombres de todos los campos que forman la claves del bean */
	protected String[] getClavesBean() {
		String[] campos = {	ScsInscripcionTurnoBean.C_IDINSTITUCION, 	ScsInscripcionTurnoBean.C_IDPERSONA, 
							ScsInscripcionTurnoBean.C_IDTURNO,			ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		return campos;
	}
	
	/** @return conjunto de datos con los nombres de todos los campos que forman la claves del bean con formato "NombreTabla.NombreCampo" */
	protected String[] getClavesTabla() {
		String[] campos = {	ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO, 	
							ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD};
		return campos;
	}

	/** 
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	protected ScsInscripcionTurnoBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		try {
			ScsInscripcionTurnoBean bean = new ScsInscripcionTurnoBean();
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
	
			return bean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar hashTableToBean()");
		}			
	}

	/**
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		try {
			Hashtable hash = new Hashtable();
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
			
			return hash;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar beanToHashTable()");
		}			
	}

	/** @return String[] conjunto de valores con los campos por los que se deberá ordenar la select que se ejecute sobre esta tabla */
	protected String[] getOrdenCampos(){
		return null;
	}
	/** @return String[] conjunto de valores con los campos por los que se deberá ordenar la select que rellena tabla de la pagina "listarTurnos.jsp" */
	protected String[] getOrdenLetrados() {
		String[] vector ={ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA};
		return vector;
	}
	
	/** 
	 *	@param where clausula "where" de la sentencia "select" que queremos ejecutar
	 *  @return Vector todos los registros que se seleccionen en BBDD 
	 */
	public Vector select(String where) throws ClsExceptions {
		try {
			Vector datos = new Vector();
			
			// Acceso a BBDD
			RowsContainer rc = new RowsContainer(); 
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
	
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar select()");
		}
	}

	public Vector selectGenericaBind (String where, Hashtable data) throws ClsExceptions {
		try {		
			Vector datos = new Vector();
			
			// Acceso a BBDD
			RowsContainer rc = new RowsContainer(); 
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
				
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar selectGenericaBind()");
		}			
	}	
	
	/**
	 * Efectúa un SELECT en la tabla SCS_INSCRIPCIONTURNO con los datos introducidos.
	 * 
	 * @param sql
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector selectTabla(String sql) throws ClsExceptions {
		try {
			Vector v = new Vector();
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
	
			return v;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar selectTabla()");
		}			
	}
	
	public String getQueryNumeroColegiadosIncritos(String institucion, String turno, String fecha) {
		String consulta=" select count(*) as NLETRADOSTURNO " +
			" from " + ScsInscripcionTurnoBean.T_NOMBRETABLA + " ins, " + 
				ScsTurnoBean.T_NOMBRETABLA + " turno " +	             
			" where ins.idinstitucion = " + institucion +
				" and ins.idturno = " + turno +
				" and ins.idinstitucion = turno.idinstitucion "+
				" and ins.idturno = turno.idturno ";
	
		if(fecha!=null){
			if(fecha.equalsIgnoreCase("sysdate")){
				fecha = "TRUNC(SYSDATE)";		  			
			} else {
				fecha = "'"+fecha+"'";		  			
			}

			consulta += " And Ins.Fechavalidacion Is Not Null " +
				" And Trunc(Ins.Fechavalidacion) <= "+fecha+" " +
				" And (Ins.Fechabaja Is Null Or " +
				" Trunc(Ins.Fechabaja) > "+fecha+") " ;			   
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
		try {
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
			
			return alInscripcion;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesTurno()");
		}			
	} // getInscripcionesTurno()

	/**
	 * Obtiene los datos de un turno
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param fechaSolicitud
	 * @param conDatosTurno
	 * @return
	 * @throws ClsExceptions
	 */
	public ScsInscripcionTurnoBean getInscripcionTurno(Integer idInstitucion, Integer idTurno, Long idPersona, String fechaSolicitud,boolean conDatosTurno) throws ClsExceptions {
		try {
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
			
			} else {
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
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionTurno()");
		}						
	} 
	
	public ScsInscripcionTurnoBean getInscripcionTurno(Integer idInstitucion, Integer idTurno, Long idPersona) throws ClsExceptions {
		try {
			ScsInscripcionTurnoBean inscripcionBean = new ScsInscripcionTurnoBean();
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.usrbean);
			ScsTurnoBean turnoBean = turnoAdm.getTurnoInscripcion(idInstitucion, idTurno);
			inscripcionBean.setTurno(turnoBean);
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
			CenPersonaBean persona = personaAdm.getPersonaColegiado(idPersona,idInstitucion);
			inscripcionBean.setPersona(persona);
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionTurno()");
		}				
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
	public ScsInscripcionTurnoBean getInscripcionTurno(Integer idInstitucion, Integer idTurno, Long idPersona, String fecha) throws ClsExceptions {
		try {
			StringBuffer sql = new StringBuffer();
			Hashtable htCodigos = new Hashtable();
			int contador = 0;
			
			sql.append(" select ");
			sql.append(camposSelect);
			sql.append(" from scs_inscripcionturno ");
			
			sql.append(" where SCS_INSCRIPCIONTURNO.idinstitucion = :");
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
				} else {
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
			RowsContainer rc = new RowsContainer(); 
	
			if (rc.findBind(sql.toString(),htCodigos)) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean)this.hashTableToBean(htFila);					
				}
			} 
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcion()");
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
 	public Vector<ScsInscripcionTurnoBean> getColaTurno(String idinstitucion, String idturno, String fecha, String order) throws ClsExceptions {
 		try {
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
	
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getColaTurno()");
		}			
	} //getColaTurno()
 	
 	public String getQueryInscripcionesTurnos(String idInstitucion, String idPersona, boolean historico, String fecha) {
		String sql = new String();
		sql = "Select ";
		sql += ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDTURNO + " IDTURNO";		
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
 	
 	private String getWhereInscripcion(String fecha) {
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
		
		// ALTA PENDIENTE 
		where += " (SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL " +
			" AND SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NULL " +
			" AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL) ";
					     
		// FECHAS DE ALTA DENTRO DEL PERIODO DE VIDA DEL TURNO
		where += " OR (SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL " +
			" AND TRUNC(SCS_INSCRIPCIONTURNO.FECHAVALIDACION) <= " + fecha.trim() + 
			" AND (SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL " + 
			" OR TRUNC(SCS_INSCRIPCIONTURNO.FECHABAJA) >= " + fecha.trim() + "))) ";
		
		return where;
 	}
 	
 	public PaginadorBind getTurnosClientePaginador (Integer idInstitucion, Long idPersona, boolean historico, String fecha) throws ClsExceptions {
 		try {
			Hashtable codigos = new Hashtable();
			String select = getQueryInscripcionesTurnos(idInstitucion.toString(), idPersona.toString(), historico, fecha);
			PaginadorBind paginador = new PaginadorBind(select,codigos);
			
			return paginador;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getTurnosClientePaginador()");
		}			
	}

	/**
	 * Obtiene las inscripcion activa de la persona en la guardia en la fecha dada
	 */
	public ScsInscripcionTurnoBean getInscripcionTurnoActiva(String idinstitucion, String idturno, String idpersona, String fecha) throws ClsExceptions {
		try {
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
					CenPersonaBean personaBean = new CenPersonaBean(inscripcionBean.getIdPersona(), 
						(String) htFila.get(CenPersonaBean.C_NOMBRE), 
						(String) htFila.get(CenPersonaBean.C_APELLIDOS1),
						(String) htFila.get(CenPersonaBean.C_APELLIDOS2), 
						(String) htFila.get(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO));
					inscripcionBean.setPersona(personaBean);
					datos.add(inscripcionBean);
				}
			}else
				return null;
	
			return datos.get(0);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionActiva()");
		}					
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
		try {
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
					 			
			RowsContainer rc = new RowsContainer();			
			rc.query(sql);
			
			if (rc.size() > 0) {
				Row fila = (Row) rc.get(0);
				fecha = fila.getString("FECHAGUARDIAS");
			}
			
			return fecha;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getFechaGuardiasTurno()");
		}				
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
		try {
			boolean resultado = false;
		    	
	    	// Preparamos las fechas:
	    	// Si provocan excepciones es porque no estan en formato largo sino en corto. En ese caso tomamos directamente su valor del bean:
	    	String sFechaSolicitud = "";
	    	try {
	    		sFechaSolicitud = GstDate.getFormatedDateShort("ES", FechaSolicitud);
			} catch (ClsExceptions e) { 
				sFechaSolicitud = FechaSolicitud;
			}
	    	
	    	// Hay que dar de baja las guardias del turno
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
	    	} else {
	    		resultado = false;
	    	}
		    			    
		    return resultado;
		    
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar borrarInscripcionTurnoPendiente()");
		}			    
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
	public ScsInscripcionTurnoBean getInscripcionTurnoValidadaSinBaja(Integer idInstitucion, Integer idTurno, Long idPersona) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +
				" FROM SCS_INSCRIPCIONTURNO " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND IDPERSONA = " + idPersona +
					" AND FECHAVALIDACION IS NOT NULL " +
					" AND FECHABAJA IS NULL";
					
			ScsInscripcionTurnoBean inscripcionBean = null;
			RowsContainer rc = new RowsContainer(); 
	
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean) this.hashTableToBean(htFila);
				}
			} 
			
			return inscripcionBean;

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionValidadaSinBaja()");
		}		
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
	public ScsInscripcionTurnoBean getInscripcionTurnoUltimaValidada(Integer idInstitucion, Integer idTurno, Long idPersona) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +	
				" FROM SCS_INSCRIPCIONTURNO " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND IDPERSONA = " + idPersona +
					" AND FECHAVALIDACION IS NOT NULL " +
				" ORDER BY FECHAVALIDACION DESC";
					
			ScsInscripcionTurnoBean inscripcionBean = null;
			RowsContainer rc = new RowsContainer(); 
	
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionTurnoBean) this.hashTableToBean(htFila);
				}
			} 
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionUltima()");
		}				
	}		
	
	/**
	 * Obtiene las inscripciones de turno de un turno, que no estan dadas de baja
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<ScsInscripcionTurnoBean> getInscripcionesTurnosSinBajaTurno(String idInstitucion, String idTurno, boolean bValidada) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +
				" FROM SCS_INSCRIPCIONTURNO " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND FECHABAJA IS NULL ";
			
			if (bValidada) {
				sql += " AND FECHAVALIDACION IS NOT NULL ";
			} else {
				sql += " AND (FECHADENEGACION IS NULL OR FECHAVALIDACION IS NOT NULL) ";
			}
			
			Vector<ScsInscripcionTurnoBean> datos = null;					
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
			
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesTurnosSinBajaTurno()");
		}	
	}
	
	/**
	 * Obtiene las inscripciones de turno de una persona, que no estan dadas de baja
	 * 
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<ScsInscripcionTurnoBean> getInscripcionesTurnosSinBajaPersona(String idInstitucion, String idPersona) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +
				" FROM SCS_INSCRIPCIONTURNO " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDPERSONA = " + idPersona +
					" AND FECHABAJA IS NULL " +
					" AND (FECHADENEGACION IS NULL OR FECHAVALIDACION IS NOT NULL)";
			
			Vector<ScsInscripcionTurnoBean> datos = null;
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
			
	       return datos;		
	       
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesTurnosSinBajaPersona()");
		}		       
	}	
	
	/**
	 * Obtiene las inscripciones de turno pendientes de validar
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param isValidacionInscripcion
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<ScsInscripcionTurnoBean> getInscripcionesTurnoPendientesValidar(String idInstitucion, String idTurno, boolean bAltaInscripcion) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +
				" FROM SCS_INSCRIPCIONTURNO " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND FECHADENEGACION IS NULL " + 
					" AND FECHABAJA IS NULL ";
			
			if (bAltaInscripcion)
				sql += " AND FECHAVALIDACION IS NULL ";
			else
				sql += " AND FECHASOLICITUDBAJA IS NOT NULL ";
			
			Vector<ScsInscripcionTurnoBean> datos = null;
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
			
	       return datos;		
	       
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesTurnoPendientesValidar()");
		}		  	       
	}	
	
	/**
	 * Cancela todas las inscripciones de turno de un turno que no estan de baja
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @throws ClsExceptions
	 */
	public void cancelarInscripcionesTurnosTurno(String idInstitucion, String idTurno) throws ClsExceptions {
		try {					
			// Obtiene las inscripciones de turno de un turno, que no estan dadas de baja
			Vector<ScsInscripcionTurnoBean> listaInscripcionesTurno = this.getInscripcionesTurnosSinBajaTurno(idInstitucion, idTurno, false);
			
			if (listaInscripcionesTurno != null) {			    	
		    	// Recorro la lista de inscripciones de turno
		    	for (int i = 0; i < listaInscripcionesTurno.size(); i++) {
				    		
		    		// Obtengo la inscripcion de turno
		    		ScsInscripcionTurnoBean insTurno = (ScsInscripcionTurnoBean) listaInscripcionesTurno.get(i);
				    		
					/* JPT: INICIO - Calculo la fecha de baja para las inscripciones de turno */
					
					InscripcionTGForm miForm = new InscripcionTGForm();
					GestionInscripcionesTGAction gInscripciones = new GestionInscripcionesTGAction();
					SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
					
					// Relleno con los datos necesarios para el metodo que calcula la fecha de baja 
					miForm.setIdInstitucion(insTurno.getIdInstitucion().toString());
					miForm.setIdTurno(insTurno.getIdTurno().toString());
					miForm.setIdPersona(insTurno.getIdPersona().toString());
					miForm.setFechaValidacionTurno(insTurno.getFechaValidacion());
					
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
					
					/* JPT: FIN - Calculo la fecha de baja para las inscripciones de turno */
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
					InscripcionTurno inscripcion = new InscripcionTurno(insTurno);
					
					String motivo = "Baja de turno";		
					
					if (insTurno.getFechaSolicitudBaja() != null && !insTurno.getFechaSolicitudBaja().equals("")) {
						inscripcion.validarBaja(
							fechaBajaInscripciones, 
							insTurno.getFechaValidacion(),
							motivo,
							null, 
							this.usrbean);
						
					} else {
						inscripcion.solicitarBaja(
							"sysdate",
							motivo,
							fechaBajaInscripciones,
							motivo,
							insTurno.getFechaValidacion(),
							null, 
							this.usrbean);	
					}						
		    	}
			}
	    	
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar cancelarInscripcionesTurnosTurno()");
		}		    	
	}
	
	/**
	 * Cancela todas las inscripciones de turno de una persona que no estan de baja
	 * 
	 * @param idPersona
	 * @param idInstitucion
	 * @param motivo
	 * @param fechaEstado
	 * @throws ClsExceptions
	 */
	public void cancelarInscripcionesTurnosPersona (String idPersona, String idInstitucion, String motivo, String fechaEstado) throws ClsExceptions {
		try {
			/* Obtiene las inscripciones de turno de una persona, que no estan dadas de baja. 
			   Al dar de baja un turno, también se da de baja de sus inscripciones de guardia */
			Vector<ScsInscripcionTurnoBean> listaInscripcionesTurno = this.getInscripcionesTurnosSinBajaPersona(idInstitucion, idPersona);
			
			if (listaInscripcionesTurno != null) {			
		    	// Recorro la lista de inscripciones de turno
		    	for (int i = 0; i < listaInscripcionesTurno.size(); i++) {
				    		
		    		// Obtengo la inscripcion de turno
		    		ScsInscripcionTurnoBean insTurno = (ScsInscripcionTurnoBean) listaInscripcionesTurno.get(i);
				    		
					/* JPT: INICIO - Calculo la fecha de baja para las inscripciones de turno */
					
					InscripcionTGForm miForm = new InscripcionTGForm();
					GestionInscripcionesTGAction gInscripciones = new GestionInscripcionesTGAction();
					SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
					
					// Relleno con los datos necesarios para el metodo que calcula la fecha de baja 
					miForm.setIdInstitucion(insTurno.getIdInstitucion().toString());
					miForm.setIdTurno(insTurno.getIdTurno().toString());
					miForm.setIdPersona(insTurno.getIdPersona().toString());
					miForm.setFechaValidacionTurno(insTurno.getFechaValidacion());
					
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
					
					/* JPT: FIN - Calculo la fecha de baja para las inscripciones de turno */
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
					InscripcionTurno inscripcion = new InscripcionTurno(insTurno);	
					
					if (insTurno.getFechaSolicitudBaja() != null && !insTurno.getFechaSolicitudBaja().equals("")) {
						inscripcion.validarBaja(
							fechaBajaInscripciones, 
							insTurno.getFechaValidacion(),
							motivo,
							null, 
							this.usrbean);
						
					} else {
						if(!fechaEstado.equalsIgnoreCase("sysdate") && fechaEstado.length()==10) {
							try {
								// jbd // inc7718 //Esta fecha tiene que ser larga
								fechaEstado = UtilidadesString.formatoFecha(fechaEstado, ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA);						
							} catch (Exception e) {
								// Si ha fallado será porque el formato ya es el adecuado DATE_FORMAT_JAVA  
							}
						}
						
						inscripcion.solicitarBaja(
							fechaEstado,
							motivo,
							fechaBajaInscripciones,
							motivo,
							insTurno.getFechaValidacion(),
							null, 
							this.usrbean);	
					}						
		    	}		    
			}
	    	
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar cancelarInscripcionesTurnosPersona()");
		}		    	
	}
	
	/**
	 * Valida toda inscipcion de turno o guardia existente
	 * @param idInstitucion
	 * @param idTurno
	 * @throws ClsExceptions
	 */
	public void validarInscripcionesPendientes(String idInstitucion,String idTurno) throws ClsExceptions {
		String sMotivo = "Validado al modificar la configuracion de turno";
		
		try {
			this.validarAltasPendientes(idInstitucion, idTurno, sMotivo);
			this.validarBajasPendientes(idInstitucion, idTurno, sMotivo);

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarInscripcionesPendientes()");
		}	
	}
	
	/**
	 * Valida toda inscripcion de alta de turno o guardia pendiente
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param sMotivo
	 * @throws ClsExceptions
	 */
	private void validarAltasPendientes (String idInstitucion, String idTurno, String sMotivo) throws ClsExceptions {		
		try {		
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			
			/* 1. VALIDO TODAS LAS INSCRIPCIONES DE TURNO PENDIENTES DE ALTA (PUEDE HABER VARIAS, UNA POR CADA COLEGIADO INSCRITO AL TURNO) */
			
			// Obtiene una lista con las inscripciones de alta de turno pendientes 
			Vector<ScsInscripcionTurnoBean> listaTurnosPendientes = this.getInscripcionesTurnoPendientesValidar(idInstitucion, idTurno, true);
			
			if (listaTurnosPendientes!=null){
				for (int i=0; i<listaTurnosPendientes.size(); i++) {
					
					// Obtiene los datos del turno
					ScsInscripcionTurnoBean insTurno = (ScsInscripcionTurnoBean)listaTurnosPendientes.get(i);
			
					/* JPT: INICIO - Calculo la fecha de alta para la inscripcion de turno */
					
					// Obtengo la fecha actual y la convierto en el formato adecuado
					Date dFechaHoy = new Date(); 										
					String fechaAltaInscripcionTurno = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy						
					
					// Obtengo la ultima inscripcion del turno
					ScsInscripcionTurnoBean insTurnoUltimaConBaja = this.getInscripcionTurno(insTurno.getIdInstitucion(), insTurno.getIdTurno(), insTurno.getIdPersona(), null);					
					
					// Compruebo que tenga los datos necesarios
					if (insTurnoUltimaConBaja!=null && insTurnoUltimaConBaja.getFechaBaja()!=null && !insTurnoUltimaConBaja.getFechaBaja().equals("")) {
						
						// Convierto la fecha de baja en el formato adecuado
						Date dFechaBaja = GstDate.convertirFechaHora(insTurnoUltimaConBaja.getFechaBaja());
						
						// Le sumo un dia
						dFechaBaja.setTime(dFechaBaja.getTime() + ClsConstants.DATE_MORE);
						String sFechaBaja = sdf.format(dFechaBaja); // Fecha con formato dd/MM/yyyy					
						
						// Obtenemos la fecha mayor
						int comparacion = GstDate.compararFechas(fechaAltaInscripcionTurno, sFechaBaja);
						if (comparacion < 0) {
							fechaAltaInscripcionTurno = sFechaBaja;
						}			
					}
					
					/* JPT: FIN - Calculo la fecha de alta para la inscripcion de turno */				
					
					// Creo un objeto inscripcion de turno y valido el turno
					InscripcionTurno inscripcion = new InscripcionTurno(insTurno);	
					
					// JPT: Como es pendiente, nunca debe llamar a solicitarAlta
					// Da de alta la inscripcion de turno y todas las guardias activas del turno
					inscripcion.validarAlta(
						fechaAltaInscripcionTurno, 
						sMotivo,
						this.usrbean);			
				}
			}		
			
			/* 2. VALIDO TODAS LAS INSCRIPCIONES DE GUARDIAS PENDIENTES DE ALTA (PUEDE HABER VARIAS DENTRO DE INSCRIPCIONES DE TURNOS NO PENDIENTES DE ALTA Y UNA POR CADA COLEGIADO INSCRITO) */ 
	
			// Obtiene una lista con las inscripciones de alta de turno pendientes 
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.usrbean);
			Vector<ScsInscripcionGuardiaBean> listaGuardiasPendientes = inscripcionGuardiaAdm.getInscripcionesGuardiasPendientesValidar(idInstitucion, idTurno, true);
			
			if (listaGuardiasPendientes!=null){
				for (int i=0; i<listaGuardiasPendientes.size(); i++) {
					
					// Obtiene los datos de la guardia
					ScsInscripcionGuardiaBean insGuardia = (ScsInscripcionGuardiaBean)listaGuardiasPendientes.get(i);
					
					/* JPT: INICIO - Calculo la fecha de alta para la inscripcion de guardia */	
					
					// Obtengo la fecha actual y la convierto en el formato adecuado
					Date dFechaHoy = new Date(); 										
					String fechaAltaInscripcionGuardia = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy	
					
					// Obtengo la ultima inscripcion de la guardia		
					ScsInscripcionGuardiaBean insGuardiaBaja = inscripcionGuardiaAdm.getInscripcionGuardiaUltimaBaja(insGuardia.getIdInstitucion(), insGuardia.getIdTurno(), insGuardia.getIdPersona(), insGuardia.getIdGuardia());					
					
					// Compruebo que tenga los datos necesarios
					if (insGuardiaBaja!=null && insGuardiaBaja.getFechaBaja()!=null && !insGuardiaBaja.getFechaBaja().equals("")) {
						
						// Convierto la fecha de baja en el formato adecuado
						Date dFechaBaja = GstDate.convertirFechaHora(insGuardiaBaja.getFechaBaja());
						
						// Le sumo un dia
						dFechaBaja.setTime(dFechaBaja.getTime() + ClsConstants.DATE_MORE);
						String sFechaBaja = sdf.format(dFechaBaja); // Fecha con formato dd/MM/yyyy					
																						
						// Obtenemos la fecha mayor
						int comparacion = GstDate.compararFechas(fechaAltaInscripcionGuardia, sFechaBaja);
						if (comparacion < 0) {
							fechaAltaInscripcionGuardia = sFechaBaja;
						}			
					}			
					
					// Obtiene la inscripcion de turno de la persona, que esta validada y sin dar de baja
					ScsInscripcionTurnoBean insTurnoGuardia = this.getInscripcionTurnoValidadaSinBaja(insGuardia.getIdInstitucion(), insGuardia.getIdTurno(), insGuardia.getIdPersona());
					
					if(insTurnoGuardia!=null && insTurnoGuardia.getFechaValidacion()!=null && !insTurnoGuardia.getFechaValidacion().equals("")) {				
						Date dFechaValidacionTurno = GstDate.convertirFechaHora(insTurnoGuardia.getFechaValidacion());
						String sFechaValidacionTurno = sdf.format(dFechaValidacionTurno);	
						
						// Obtenemos la fecha mayor
						int comparacion = GstDate.compararFechas(fechaAltaInscripcionGuardia, sFechaValidacionTurno);
						if (comparacion < 0) {
							fechaAltaInscripcionGuardia = sFechaValidacionTurno;
						}								
					}
					
					/* JPT: FIN - Calculo la fecha de alta para la inscripcion de guardia */				
					
					// Creo un objeto inscripcion de guardia y valido la guardia
					InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardia);	
					
					// JPT: Como es pendiente, nunca debe llamar a solicitarAlta
					// Da de alta la inscripcion de guardia
					inscripcion.setAltas(null, fechaAltaInscripcionGuardia, sMotivo);
					inscripcion.validarAlta(this.usrbean);		
				}
			}	
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarAltasPendientes()");
		}				
	}
	
	/**
	 * Valida toda inscripcion de baja de turno o guardia pendiente
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param sMotivo
	 * @throws ClsExceptions
	 */
	private void validarBajasPendientes(String idInstitucion, String idTurno, String sMotivo) throws ClsExceptions {		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			
			/* 1. VALIDO TODAS LAS INSCRIPCIONES DE TURNO PENDIENTES DE BAJA (PUEDE HABER VARIAS, UNA POR CADA COLEGIADO INSCRITO AL TURNO) */ 
			
			// Obtiene una lista con las inscripciones de baja de turno pendientes 
			Vector<ScsInscripcionTurnoBean> listaTurnosPendientes = this.getInscripcionesTurnoPendientesValidar(idInstitucion, idTurno, false);
			
			if (listaTurnosPendientes!=null){
				for (int i=0; i<listaTurnosPendientes.size(); i++) {
					
					// Obtiene los datos del turno
					ScsInscripcionTurnoBean insTurno = (ScsInscripcionTurnoBean)listaTurnosPendientes.get(i);
					
					/* JPT: INICIO - Calculo la fecha de baja para la inscripcion de turno */
					
					InscripcionTGForm miForm = new InscripcionTGForm();
					GestionInscripcionesTGAction gInscripciones = new GestionInscripcionesTGAction();				
					
					// Relleno con los datos necesarios para el metodo que calcula la fecha de baja 
					miForm.setIdInstitucion(insTurno.getIdInstitucion().toString());
					miForm.setIdTurno(insTurno.getIdTurno().toString());
					miForm.setIdPersona(insTurno.getIdPersona().toString());
					miForm.setFechaValidacionTurno(insTurno.getFechaValidacion());
					
					// Obtengo la fecha de baja minima para la inscripcion de turno
					String fechaBajaInscripcionTurno = gInscripciones.calcularFechaBajaInscripcionTurno(miForm, this.usrbean);		
					
					// Obtengo la fecha actual y la convierto en el formato adecuado
					Date dFechaHoy = new Date(); 										
					String sFechaHoy = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy							
						
					// Obtenemos la fecha mayor
					int comparacion = GstDate.compararFechas(fechaBajaInscripcionTurno, sFechaHoy);
					if (comparacion < 0) {
						fechaBajaInscripcionTurno = sFechaHoy;
					}					
					
					/* JPT: FIN - Calculo la fecha de baja para la inscripcion de turno */				
					
					// Creo un objeto inscripcion de turno y valido el turno
					InscripcionTurno inscripcion = new InscripcionTurno(insTurno);	
					
					// JPT: Como es pendiente, nunca debe llamar a solicitarBaja
					// Da de baja la inscripcion de turno y todas las guardias que queden activas del turno
					inscripcion.validarBaja(
						fechaBajaInscripcionTurno, 
						insTurno.getFechaValidacion(),
						sMotivo,
						null, 
						this.usrbean);				
				}
			}		
			
			/* 2. VALIDO TODAS LAS INSCRIPCIONES DE GUARDIAS PENDIENTES DE BAJA (PUEDE HABER VARIAS DENTRO DE INSCRIPCIONES DE TURNOS NO PENDIENTES DE BAJA Y UNA POR CADA COLEGIADO INSCRITO) */ 
	
			// Obtiene una lista con las inscripciones de baja de turno pendientes 
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.usrbean);
			Vector<ScsInscripcionGuardiaBean> listaGuardiasPendientes = inscripcionGuardiaAdm.getInscripcionesGuardiasPendientesValidar(idInstitucion, idTurno, false);
			
			if (listaGuardiasPendientes!=null){
				for (int i=0; i<listaGuardiasPendientes.size(); i++) {
					
					// Obtiene los datos de la guardia
					ScsInscripcionGuardiaBean insGuardia = (ScsInscripcionGuardiaBean)listaGuardiasPendientes.get(i);
					
					/* JPT: INICIO - Calculo la fecha de baja para la inscripcion de guardia */
					
					String fechaBajaInscripcionGuardia = "";						
					if (insGuardia!=null && insGuardia.getFechaValidacion()!=null && !insGuardia.getFechaValidacion().equals("")) { 
						String sFechaValidacionGuardia = insGuardia.getFechaValidacion();
						Date dFechaValidacionGuardia = GstDate.convertirFechaHora(sFechaValidacionGuardia);
						fechaBajaInscripcionGuardia = sdf.format(dFechaValidacionGuardia);
					}				
					
					// Obtengo la fecha actual y la convierto en el formato adecuado
					Date dFechaHoy = new Date(); 										
					String sFechaHoy = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy							
						
					// Obtenemos la fecha mayor
					int comparacion = GstDate.compararFechas(fechaBajaInscripcionGuardia, sFechaHoy);
					if (comparacion < 0) {
						fechaBajaInscripcionGuardia = sFechaHoy;
					}					
					
					/* JPT: FIN - Calculo la fecha de baja para la inscripcion de guardia */				
					
					// Creo un objeto inscripcion de guardia y valido la guardia
					InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardia);	
					
					// JPT: Como es pendiente, nunca debe llamar a solicitarBaja
					// Da de baja la inscripcion de guardia
					inscripcion.setBajas(null, null, fechaBajaInscripcionGuardia, sMotivo);
					inscripcion.validarBaja(this.usrbean, null);		
				}
			}	
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarBajasPendientes()");
		}				
	}
	
	/**
	 * Cambia la configuracion de un turno, modificando sus tipos de guardias
	 * 
	 * iTipoGuardias=0 Obligatorias
     * iTipoGuardias=1 Todas o Ninguna
     * iTipoGuardias=2 Eleccion
     * 
     *  Si (iTipoGuardiasOld=0) y (iTipoGuardiasNew=0) entonces no cambia de tipo
     *  Si (iTipoGuardiasOld=0) y (iTipoGuardiasNew=1) entonces hacemos el tratamiento principal
     *  Si (iTipoGuardiasOld=0) y (iTipoGuardiasNew=2) entonces no hace falta modificar las guardias (ya que los turnos por eleccion, no obligan a cumplir unos requisitos en sus guardias)
     *  
     *  Si (iTipoGuardiasOld=1) y (iTipoGuardiasNew=0) entonces hacemos el tratamiento principal 
     *  Si (iTipoGuardiasOld=1) y (iTipoGuardiasNew=1) entonces no cambia de tipo
     *  Si (iTipoGuardiasOld=1) y (iTipoGuardiasNew=2) entonces no hace falta modificar las guardias (ya que los turnos por eleccion, no obligan a cumplir unos requisitos en sus guardias)
     *  
     *  Si (iTipoGuardiasOld=2) y (iTipoGuardiasNew=0) entonces hacemos el tratamiento principal
     *  Si (iTipoGuardiasOld=2) y (iTipoGuardiasNew=1) entonces hacemos el tratamiento secundario
     *  Si (iTipoGuardiasOld=2) y (iTipoGuardiasNew=2) entonces no cambia de tipo
	 *
	 *
	 *  Tratamiento Principal
	 *  ---------------------
     *  a. Si la inscripcion de turno esta cancelada o denegada el alta o pendiente de alta, no hay que cambiar nada.
     *  
     *  b. Si no se cumple la opciona a:
     *  b.1. Doy de baja todas las guardias que no esten canceladas o denegadas de alta.
     *  b.2. Doy de alta todas las guardias del turno.
     *  
     *  
     *  Tratamiento Secundario
     *  ----------------------
     *  a. Si la inscripcion de turno esta cancelada o denegada el alta o pendiente de alta, no hay que cambiar nada.
     *  
     *  b. Si no se cumple la opciona a:
     *  b.1. Doy de baja todas las guardias que no esten canceladas o denegadas de alta.
     *  b.2. Si he borrado alguna guardia, doy de alta todas las guardias del turno.
     *  
     *  
     *  Tratamiento Final
     *  -----------------
     *  
     *  c. Si la inscripcion de turno esta pendiente de alta:
     *  c.1. Doy de baja todas las guardias que no esten canceladas o denegadas de alta.
     *  c.2. Doy de alta pendiente todas las guardias del turno.
	 * 
	 * @param bPrincipal
	 * @param idInstitucion
	 * @param idTurno
	 * @throws ClsExceptions
	 */
	public void cambiarConfiguracionTiposGuardias(boolean bPrincipal, String idInstitucion, String idTurno) throws ClsExceptions {
		String sMotivo = "Cambios de configuración del turno, modificando el tipo de guardias del turno";		
		
		try {					
			/*
			 * A. Obtiene las inscripciones de turno de un turno, que no estan dadas de baja
			 */
			Vector<ScsInscripcionTurnoBean> listaInscripcionesTurno = this.getInscripcionesTurnosSinBajaTurno(idInstitucion, idTurno, true);
			
			if (listaInscripcionesTurno != null) {
			    	
		    	// Recorro la lista de inscripciones de turno
		    	for (int i = 0; i<listaInscripcionesTurno.size(); i++) {
		    		
		    		// Obtengo la inscripcion de turno
		    		ScsInscripcionTurnoBean beanInscripcionTurno = (ScsInscripcionTurnoBean) listaInscripcionesTurno.get(i);	    		
		    		
					/* JPT: INICIO - Calculo la fecha de baja para las inscripciones de turno */
					
					InscripcionTGForm miForm = new InscripcionTGForm();
					GestionInscripcionesTGAction gInscripciones = new GestionInscripcionesTGAction();
					SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
					
					// Relleno con los datos necesarios para el metodo que calcula la fecha de baja 
					miForm.setIdInstitucion(beanInscripcionTurno.getIdInstitucion().toString());
					miForm.setIdTurno(beanInscripcionTurno.getIdTurno().toString());
					miForm.setIdPersona(beanInscripcionTurno.getIdPersona().toString());
					miForm.setFechaValidacionTurno(beanInscripcionTurno.getFechaValidacion());
					
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
					
					String fechaBajaInscripcionesLarga = fechaBajaInscripciones;
					if(fechaBajaInscripcionesLarga.length()==10) {
						try {
							fechaBajaInscripcionesLarga = UtilidadesString.formatoFecha(fechaBajaInscripcionesLarga, ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA);						
						} catch (Exception e) {
							// Si ha fallado será porque el formato ya es el adecuado DATE_FORMAT_JAVA  
						}					
					}				
					
					/* JPT: FIN - Calculo la fecha de baja para las inscripciones de turno */	    		
		    		
		    		/* 
		    		 * B.1. Doy de abaja todas las guardias que no esten canceladas o denegadas de alta 
		    		 */			   
		    			    		
					ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(this.usrbean);
					List<ScsInscripcionGuardiaBean> vInscripcionesGuardiasTurno = admInscripcionGuardia.getInscripcionesGuardias(beanInscripcionTurno.getIdInstitucion(), beanInscripcionTurno.getIdTurno(), beanInscripcionTurno.getIdPersona(), null);	
		    		
					if(vInscripcionesGuardiasTurno!=null){
						for(int x=0; x<vInscripcionesGuardiasTurno.size(); x++) {
							
							// Obtengo la inscripcion de guardia
							ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vInscripcionesGuardiasTurno.get(x);
							
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud + fechaBaja + observacionesBaja
							InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);																
							
							if (beanInscripcionGuardia.getFechaSolicitudBaja()!=null && !beanInscripcionGuardia.getFechaSolicitudBaja().equals("")) {
								inscripcionGuardia.setBajas(null, null, fechaBajaInscripciones, sMotivo);
								inscripcionGuardia.validarBaja(this.usrbean, null);
								
							} else {
								// La fecha de solicitud de baja sera la misma para todas las inscripciones de guardia 
								inscripcionGuardia.setBajas(sMotivo, fechaBajaInscripcionesLarga, fechaBajaInscripciones, sMotivo);						
								inscripcionGuardia.solicitarBaja(this.usrbean, null);
							}											
						}
					}
					
					// Compruebo si es el tratamiento principal o tenia alguna guardia para borrar
					if (bPrincipal || (vInscripcionesGuardiasTurno!=null && vInscripcionesGuardiasTurno.size()>0)) {
					
			    		/* 
			    		 * B.2. Doy de alta todas las guardias del turno.
			    		 */						
						
						// Le sumo un dia a la baja
						Date dFechaAltaGuardias = GstDate.convertirFechaHora(fechaBajaInscripcionesLarga);
						dFechaAltaGuardias.setTime(dFechaAltaGuardias.getTime() + ClsConstants.DATE_MORE);				
						String sFechaAltaGuardias = sdf.format(dFechaAltaGuardias);			
						
						String fechaAltaInscripcionesLarga = sFechaAltaGuardias;
						if(fechaAltaInscripcionesLarga.length()==10) {
							try {
								fechaAltaInscripcionesLarga = UtilidadesString.formatoFecha(fechaAltaInscripcionesLarga, ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA);						
							} catch (Exception e) {
								// Si ha fallado será porque el formato ya es el adecuado DATE_FORMAT_JAVA  
							}					
						}						
						
						// Obtengo las guardias del turno
						ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.usrbean);
						List<ScsGuardiasTurnoBean> vGuardiasTurno = admGuardiasTurno.getGuardiasTurnos(beanInscripcionTurno.getIdTurno(), beanInscripcionTurno.getIdInstitucion(), false);
						
						if(vGuardiasTurno!=null){
							for(int x=0; x<vGuardiasTurno.size(); x++) {
								
								// Obtengo los datos de la guardia
								ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) vGuardiasTurno.get(x);
								
								// Transformo el objeto guardia al objeto inscripcion de guardia
								ScsInscripcionGuardiaBean beanInscripcionGuardia = new ScsInscripcionGuardiaBean();
									beanInscripcionGuardia.setIdInstitucion(beanInscripcionTurno.getIdInstitucion());					
									beanInscripcionGuardia.setIdTurno(beanInscripcionTurno.getIdTurno());					
									beanInscripcionGuardia.setIdGuardia(beanGuardia.getIdGuardia());					
									beanInscripcionGuardia.setIdPersona(beanInscripcionTurno.getIdPersona());
								
									// La fecha de suscripcion sera la misma para todas las inscripciones de guardia 
									beanInscripcionGuardia.setFechaSuscripcion(fechaAltaInscripcionesLarga);
								
								// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSubscripcion 
								InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);									
								
								// Realiza el alta de la inscripcion de guardia
								inscripcionGuardia.setAltas(sMotivo, sFechaAltaGuardias, sMotivo);
								inscripcionGuardia.solicitarAlta(this.usrbean);																
							}
						}					
					}
		    	}
			}
	    	
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar cambiarConfiguracionTiposGuardias()");
		}		    	
	}	
}