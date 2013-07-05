package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.gratuita.form.InscripcionTGForm;

/**
 * Implementa las operaciones sobre la base de datos a la tabla SCS_INSCRIPCIONGUARDIA
 */
public class ScsInscripcionGuardiaAdm extends MasterBeanAdministrador {
	
	private static final String camposSelect =  ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDPERSONA + ", " +
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDINSTITUCION + ", " +
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDTURNO + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDGUARDIA + ", " +
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION + ", " +
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHABAJA + ", " +
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHAVALIDACION + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHADENEGACION + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION + ", " +												
												ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA;

	/**
	 * Constructor 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsInscripcionGuardiaAdm (UsrBean usuario) {
		super(ScsInscripcionGuardiaBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsInscripcionGuardiaBean.C_IDPERSONA,
							ScsInscripcionGuardiaBean.C_IDINSTITUCION,
							ScsInscripcionGuardiaBean.C_IDTURNO,
							ScsInscripcionGuardiaBean.C_IDGUARDIA,
							ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,
							ScsInscripcionGuardiaBean.C_FECHABAJA,
							ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,
							ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,
							ScsInscripcionGuardiaBean.C_FECHAVALIDACION,
							ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,
							ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,
							ScsInscripcionGuardiaBean.C_USUMODIFICACION,
							ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,
							ScsInscripcionGuardiaBean.C_FECHADENEGACION,
							ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,
							ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA};		
		return campos;
	}
	
	/**
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsInscripcionGuardiaBean.C_IDINSTITUCION,
							ScsInscripcionGuardiaBean.C_IDPERSONA,
							ScsInscripcionGuardiaBean.C_IDTURNO,
							ScsInscripcionGuardiaBean.C_IDGUARDIA,
							ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
		return campos;
	}
	
	/**
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected ScsInscripcionGuardiaBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		try {
			ScsInscripcionGuardiaBean bean = new ScsInscripcionGuardiaBean();
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDINSTITUCION)));
			bean.setIdPersona(Long.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDPERSONA)));
			bean.setIdTurno(Integer.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDTURNO)));
			bean.setIdGuardia(Integer.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDGUARDIA)));
			bean.setFechaSuscripcion((String)hash.get(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION));
			bean.setFechaBaja((String)hash.get(ScsInscripcionTurnoBean.C_FECHABAJA));
			bean.setObservacionesSuscripcion((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION));
			bean.setObservacionesBaja((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA));			
			bean.setFechaValidacion((String)hash.get(ScsInscripcionGuardiaBean.C_FECHAVALIDACION));
			bean.setFechaSolicitudBaja((String)hash.get(ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA));
			bean.setObservacionesValidacion((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION));
			bean.setObservacionesDenegacion((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION));
			bean.setFechaDenegacion((String)hash.get(ScsInscripcionGuardiaBean.C_FECHADENEGACION));
			bean.setObservacionesValBaja((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA));
		
			return bean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar hashTableToBean()");
		}		
	}
	
	/**
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		try {
			Hashtable hash = new Hashtable();
			ScsInscripcionGuardiaBean b = (ScsInscripcionGuardiaBean) bean;
			hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsInscripcionGuardiaBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, b.getFechaSuscripcion());
			hash.put(ScsInscripcionGuardiaBean.C_FECHABAJA, b.getFechaBaja());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION, b.getObservacionesSuscripcion());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA, b.getObservacionesBaja());
			hash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, b.getFechaValidacion());
			hash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA, b.getFechaSolicitudBaja());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, b.getObservacionesValidacion());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION, b.getObservacionesDenegacion());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, b.getObservacionesValBaja());
			hash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION, b.getFechaDenegacion());
			
			return hash;
			
		} catch (Exception e){
			throw new ClsExceptions (e, "Error al ejecutar beanToHashTable()");			
		}		
	}
	
	/**
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return null;
	}
	
	/**
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions {
		try {
			Vector datos = new Vector();
			
			// Acceso a BBDD
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
			
			return datos;
			
		} catch (Exception e){
			throw new ClsExceptions (e, "Error al ejecutar ejecutaSelect()");			
		}			
	}
	
	/** 
	 * @param idpersona
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public Hashtable obtenerNumCuenta(Long idpersona,Integer idInstitucion) throws ClsExceptions {
		try {
			String consulta = "select count(C.IDCUENTA)AS NUM from cen_cuentasbancarias C where C.IDPERSONA = "+ idpersona+" AND C.IDINSTITUCION = "+idInstitucion+" AND (c.ABONOCARGO = 'C' OR c.ABONOCARGO = 'T') and c.fechabaja IS NULL";
			
			Vector vector = (Vector)ejecutaSelect(consulta);
			String numcuentas=(String)((Hashtable)vector.get(0)).get("NUM");
			Hashtable cuentaelegida=new Hashtable();
			
			if(numcuentas.equals("1")){
				
				consulta="select (C.CBO_CODIGO || '-' ||C.CODIGOSUCURSAL || '-' || C.DIGITOCONTROL || '-' ||LPAD(SUBSTR(C.NUMEROCUENTA, 7), 10, '*')) as DESCRIPCION, C.IDCUENTA A from cen_cuentasbancarias C where C.IDPERSONA = "+idpersona+" AND C.IDINSTITUCION = "+idInstitucion+" AND (c.ABONOCARGO = 'C' OR c.ABONOCARGO = 'T') and c.fechabaja IS NULL";
				vector=(Vector)ejecutaSelect(consulta);
				cuentaelegida.put("NUMCUENTA",(String)((Hashtable)vector.get(0)).get("DESCRIPCION"));
				cuentaelegida.put("IDCUENTA",(String)((Hashtable)vector.get(0)).get("A"));
				
			} else {
				cuentaelegida.put("NUMCUENTA","");
				cuentaelegida.put("IDCUENTA","");
			}
			
			return (cuentaelegida);
			
		} catch(Exception e){
			throw new ClsExceptions (e, "Error al ejecutar obtenerNumCuenta()");
		}
	}	
	
	/**
	 * Obtiene la inscripciones de guardia de la inscripcion de turno
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param idGuardia
	 * @param inscripcionTurno
	 * @return
	 * @throws ClsExceptions
	 */
	public Hashtable getInscripcionGuardiaInscripcionTurno(String idInstitucion, String idTurno, String idPersona, Integer idGuardia, ScsInscripcionTurnoBean inscripcionTurno) throws ClsExceptions {
		try {
			Hashtable datos = new Hashtable();
			StringBuffer sql = new StringBuffer();
			Hashtable htCodigos = new Hashtable();
			int contador = 0;
			
			sql.append(" SELECT " + camposSelect);
				sql.append(", NVL(FECHADENEGACION, FECHAVALIDACION) FECHAVALOR ");			
			//Sacamos este campo, ya que solo se podran incribie en guardia cuando la incripcion en el turno este validado
			//Esto es para los turnos con guardia a elegir y todas-ninguna.
			
			sql.append(" FROM SCS_INSCRIPCIONGUARDIA ");
			
			contador++;
			sql.append(" WHERE IDINSTITUCION = :" + contador);
			htCodigos.put(contador, idInstitucion);
			
			contador++;
			sql.append(" AND IDTURNO = :" + contador);		
			htCodigos.put(contador, idTurno);
			
			contador++;
			sql.append(" AND IDPERSONA = :" + contador);		
			htCodigos.put(contador, idPersona);
			
			contador++;
			sql.append(" AND IDGUARDIA = :" + contador);
			htCodigos.put(contador, idGuardia);
			
			if (inscripcionTurno.getFechaValidacion() != null && !inscripcionTurno.getFechaValidacion().equals("")) {
				sql.append(" AND ((FECHAVALIDACION IS NULL ");
				sql.append(" AND FECHADENEGACION IS NULL) ");
				sql.append(" OR TRUNC(FECHAVALIDACION) >= TRUNC(TO_DATE('" + inscripcionTurno.getFechaValidacion() + "', '" + ClsConstants.DATE_FORMAT_SQL + "'))) ");
				
			} else {
				if (inscripcionTurno.getFechaDenegacion() != null && !inscripcionTurno.getFechaDenegacion().equals("")) {
					sql.append(" AND TRUNC(FECHADENEGACION) = TRUNC(TO_DATE('" + inscripcionTurno.getFechaDenegacion() + "', '" + ClsConstants.DATE_FORMAT_SQL + "')) ");			
				}
			}
			
			if (inscripcionTurno.getFechaBaja() != null && !inscripcionTurno.getFechaBaja().equals("")) {
				sql.append(" AND TRUNC(FECHABAJA) <= TRUNC(TO_DATE('" + inscripcionTurno.getFechaBaja() + "', '" + ClsConstants.DATE_FORMAT_SQL + "')) ");
			}
	
			// OBTENGO LA ULTIMA FECHA DE SUSCRIPCION Y VALIDACION
			sql.append(" ORDER BY FECHASUSCRIPCION DESC, FECHAVALIDACION DESC ");
			
			// Acceso a BBDD
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(sql.toString(), htCodigos)) {
				if(rc!=null && rc.size()>0){
					Row fila = (Row) rc.get(0);
					datos = (Hashtable) fila.getRow(); 
				}
			}
			
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesGuardiaInscripcionTurno()");
		}				
	}	
	
	/**
	 * 
	 * @param institucion
	 * @param turno
	 * @param idguardia
	 * @param fecha
	 * @return
	 * @throws ClsExceptions
	 */
	public String getQueryNumeroColegiadosIncritos(String institucion, String turno, String idguardia, String fecha) throws ClsExceptions {
		try {
			String consulta = " select count(*) as NLETRADOSINSCRITOS from " + ScsInscripcionGuardiaBean.T_NOMBRETABLA
				+ " where idinstitucion =" + institucion + " and idturno=" + turno + " and idguardia=" + idguardia;

			if (fecha == null) {
				// consulta += " and FECHABAJA IS NULL";
			} else {
				if (fecha.equalsIgnoreCase("sysdate")) {
					fecha = "TRUNC(SYSDATE)";

				} else {
					fecha = "'" + fecha + "'";

				}
				consulta += " and (fechavalidacion IS NOT NULL AND trunc(fechavalidacion)<=" + fecha
						+ ") and (FECHABAJA IS NULL or trunc(FECHABAJA)>" + fecha + ")";

			}

			return consulta;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getQueryNumeroColegiadosIncritos()");
		}			
	}
	
	/**
	 * Busca inscripciones de guardia
	 * 
	 * @param inscripcionguardiaForm
	 * @param isEdicion
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsInscripcionGuardiaBean> getInscripcionesGuardias(InscripcionTGForm inscripcionguardiaForm) throws ClsExceptions {
		try {
			Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
			int contador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT  ");
				sql.append(" GT.NOMBRE NOMBREGUARDIA, ");
				sql.append(" T.NOMBRE NOMBRETURNO, ");
				sql.append(" T.ABREVIATURA ABREVIATURATURNO, ");
				sql.append(" T.VALIDARINSCRIPCIONES, ");
				sql.append(" T.GUARDIAS, ");
				sql.append(" DECODE(COL.COMUNITARIO,'1', COL.NCOMUNITARIO, COL.NCOLEGIADO) NCOLEGIADO, ");
				sql.append(" PER.NOMBRE, ");
				sql.append(" PER.APELLIDOS1, ");
				sql.append(" PER.APELLIDOS2, ");
				sql.append(" I.IDINSTITUCION, ");
				sql.append(" I.IDPERSONA, ");
				sql.append(" I.IDTURNO, ");
				sql.append(" I.IDGUARDIA, ");
				sql.append(" I.FECHASUSCRIPCION, ");
				sql.append(" I.OBSERVACIONESSUSCRIPCION, ");
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
				
			sql.append(" FROM SCS_INSCRIPCIONGUARDIA I, ");
				sql.append(" CEN_COLEGIADO COL, ");
				sql.append(" CEN_PERSONA PER, ");
				sql.append(" SCS_GUARDIASTURNO GT, ");
				sql.append(" SCS_TURNO T ");
				
			sql.append(" WHERE I.IDINSTITUCION = GT.IDINSTITUCION ");
				sql.append(" AND I.IDTURNO = GT.IDTURNO ");
				sql.append(" AND I.IDGUARDIA = GT.IDGUARDIA ");
				sql.append(" AND I.IDINSTITUCION = COL.IDINSTITUCION ");
				sql.append(" AND I.IDPERSONA = COL.IDPERSONA ");
				sql.append(" AND COL.IDPERSONA = PER.IDPERSONA ");
				sql.append(" AND GT.IDINSTITUCION = T.IDINSTITUCION ");
				sql.append(" AND GT.IDTURNO = T.IDTURNO ");
			
			
			// Sacamos las guardias en potencia de ser validadas(excepto las obligatorias)
			// sql.append(" AND T.GUARDIAS!=");
			// sql.append(ScsTurnoBean.TURNO_GUARDIAS_OBLIGATORIAS);
	
			sql.append(" AND I.IDINSTITUCION = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdInstitucion());
			
			if (inscripcionguardiaForm.getIdTurno() != null && !inscripcionguardiaForm.getIdTurno().equalsIgnoreCase("") && !inscripcionguardiaForm.getIdTurno().equalsIgnoreCase("-1")) {
				sql.append(" AND I.IDTURNO = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdTurno());
			}
			
			if (inscripcionguardiaForm.getIdPersona() != null && !inscripcionguardiaForm.getIdPersona().equalsIgnoreCase("")) {
				sql.append(" AND I.IDPERSONA = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdPersona());
			}
			
			if (inscripcionguardiaForm.getIdGuardia() != null && !inscripcionguardiaForm.getIdGuardia().equalsIgnoreCase("") && !inscripcionguardiaForm.getIdGuardia().equalsIgnoreCase("-1")) {
				sql.append(" AND I.IDGUARDIA = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdGuardia());
			}
	
			String campoFecha = null;
			StringBuffer orderBy = new StringBuffer();
			
			// OBTIENE LA ULTIMA GUARDIA
			sql.append(" AND I.FECHASUSCRIPCION = ");
			sql.append(" (SELECT MAX(IG2.FECHASUSCRIPCION) ");
			sql.append(" FROM SCS_INSCRIPCIONGUARDIA IG2 ");
			sql.append(" WHERE IG2.IDINSTITUCION = I.IDINSTITUCION ");
				sql.append(" AND IG2.IDTURNO = I.IDTURNO ");
				sql.append(" AND IG2.IDGUARDIA = I.IDGUARDIA ");
				sql.append(" AND IG2.IDPERSONA = I.IDPERSONA) ");
			
			if (inscripcionguardiaForm.getTipo().equals("A")) {
				campoFecha = "I.FECHASUSCRIPCION";
				orderBy.append(campoFecha);
				sql.append(" AND I.FECHABAJA IS NULL ");
				sql.append(" AND I.FECHASOLICITUDBAJA IS NULL ");
				
				// TIENEN QUE TENER LA INSCRIPCION AL TURNO VALIDADAS
				sql.append(" AND (SELECT count(*) ");
				sql.append(" FROM SCS_INSCRIPCIONTURNO IT ");
				sql.append(" WHERE IT.IDINSTITUCION = I.IDINSTITUCION ");
				sql.append(" AND IT.IDTURNO = I.IDTURNO ");
				sql.append(" AND IT.IDPERSONA = I.IDPERSONA ");
				sql.append(" AND IT.FECHAVALIDACION IS NULL ");
				sql.append(" AND IT.FECHABAJA IS NULL ");
				sql.append(" AND IT.FECHASOLICITUDBAJA IS NULL ");
				sql.append(" AND IT.FECHADENEGACION IS NULL) = 0 ");
				
				if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("S")) {
					
					
				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("P")) {
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NULL ");
	
				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("C")) {
					orderBy.append(",I.FECHAVALIDACION");
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NOT NULL ");
	
				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("D")) {
					orderBy.append(",I.FECHADENEGACION");
					sql.append(" AND I.FECHADENEGACION IS NOT NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NULL ");
				}
	
			} else if (inscripcionguardiaForm.getTipo().equals("B")) {
				campoFecha = "I.FECHASOLICITUDBAJA";
				orderBy.append(campoFecha);
				sql.append(" AND I.FECHASOLICITUDBAJA IS NOT NULL ");
				
				if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("P")) {
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHABAJA IS NULL ");
	
				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("C")) {
					orderBy.append(",I.FECHABAJA");
					sql.append(" AND I.FECHABAJA IS NOT NULL ");
					sql.append(" AND I.FECHADENEGACION IS NULL ");
	
				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("D")) {
					orderBy.append(",I.FECHADENEGACION");
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHADENEGACION IS NOT NULL ");
				}
			}
			
			if (inscripcionguardiaForm.getFechaDesde() != null && !inscripcionguardiaForm.getFechaDesde().equals("")) {
				sql.append(" AND ");
				sql.append(campoFecha);
				sql.append(">=:");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), inscripcionguardiaForm.getFechaDesde());
			}
			
			if (inscripcionguardiaForm.getFechaHasta() != null && !inscripcionguardiaForm.getFechaHasta().equals("")) {
				sql.append(" AND ");
				sql.append(campoFecha);
				sql.append("<=:");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), inscripcionguardiaForm.getFechaHasta());
			}
	
			sql.append(" ORDER BY ");		
			//sql.append(orderBy);
			sql.append(" NOMBRETURNO, NOMBREGUARDIA ");
	
			List<ScsInscripcionGuardiaBean> alInscripcion = null;
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				alInscripcion = new ArrayList<ScsInscripcionGuardiaBean>();
				ScsInscripcionGuardiaBean inscripcionBean = null;
				ScsTurnoBean turno = null;
				ScsGuardiasTurnoBean guardia = null;
				CenPersonaBean persona = null;
				CenColegiadoBean colegiado = null;

				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					inscripcionBean = (ScsInscripcionGuardiaBean) this.hashTableToBean(htFila);
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
					
					guardia = new ScsGuardiasTurnoBean();
					inscripcionBean.setGuardia(guardia);
										
					colegiado = new CenColegiadoBean();					
					persona = new CenPersonaBean();
					persona.setColegiado(colegiado);
					inscripcionBean.setPersona(persona);

					turno.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_IDINSTITUCION));
					turno.setIdTurno(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_IDTURNO));
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					turno.setAbreviatura(UtilidadesHash.getString(htFila, "ABREVIATURATURNO"));
					turno.setValidarInscripciones(UtilidadesHash.getString(htFila, ScsTurnoBean.C_VALIDARINSCRIPCIONES));
					turno.setGuardias(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_GUARDIAS));

					guardia.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDINSTITUCION));
					guardia.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDGUARDIA));
					guardia.setNombre(UtilidadesHash.getString(htFila, "NOMBREGUARDIA"));

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
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesGuardia()");
		}				
	}
	
	/**
	 * Obtiene las inscripciones de guardias de una inscripcion de turno, 
	 *  que se tienen que modificar al cambiar la fecha efectiva de validacion y baja de una inscripcion de turno
	 *  
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param fechaValidacion
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsInscripcionGuardiaBean> getInscripcionesGuardiasTurnoFechaEfectiva (Integer idInstitucion, Integer idTurno, Long idPersona, String fecha, boolean esAlta) throws ClsExceptions {
		try {
			String sql = " SELECT I.IDINSTITUCION, " +
					" I.IDPERSONA, " +
					" I.IDTURNO, " +
					" I.IDGUARDIA, " +				
					" I.FECHASUSCRIPCION, " +				
					" I.OBSERVACIONESSUSCRIPCION, " +				
					" I.FECHAVALIDACION, " +
					" I.OBSERVACIONESVALIDACION, " +				
					" I.FECHASOLICITUDBAJA, " +
					" I.OBSERVACIONESBAJA, " +
					" I.FECHABAJA, " +
					" I.OBSERVACIONESVALBAJA, " +
					" I.FECHADENEGACION, " +
					" I.OBSERVACIONESDENEGACION " +				
				" FROM SCS_INSCRIPCIONGUARDIA I " +
				" WHERE I.IDINSTITUCION = " + idInstitucion +
					" AND I.IDTURNO = " + idTurno +
					" AND I.IDPERSONA = " + idPersona;
			
			if (esAlta) {
				sql += " AND TRUNC(I.FECHAVALIDACION) = TRUNC(TO_DATE('" + fecha + "', '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "')) ";
				
			} else {
				sql += " AND TRUNC(I.FECHABAJA) = TRUNC(TO_DATE('" + fecha + "', '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "')) ";
			}
				
			List<ScsInscripcionGuardiaBean> alInscripcion = new ArrayList<ScsInscripcionGuardiaBean>();			
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql)) {				
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();					
					ScsInscripcionGuardiaBean inscripcionBean = (ScsInscripcionGuardiaBean) this.hashTableToBean(htFila);
					alInscripcion.add(inscripcionBean);
				}
			} 
			
			return alInscripcion;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getGuardiasTurnoFechaEfectiva()");
		}				
	}		

	/**
	 * JPT: Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja, con los siguientes datos: 
	 * - No tiene fecha de baja
	 * - No tiene fecha de denegacion o no tiene fecha de validacion
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsInscripcionGuardiaBean> getInscripcionesGuardias(Integer idInstitucion, Integer idTurno, Long idPersona, Integer idGuardia) throws ClsExceptions {
		try {
			Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
			int contador = 0;
			StringBuffer sql = new StringBuffer();
	
			sql.append(" SELECT GT.IDINSTITUCION, ");
				sql.append(" GT.IDTURNO, ");
				sql.append(" GT.IDGUARDIA, ");
				sql.append(" GT.NOMBRE, ");
				sql.append(" GT.NUMEROLETRADOSGUARDIA, ");
				sql.append(" GT.NUMEROSUSTITUTOSGUARDIA, ");
				sql.append(" GT.SELECCIONLABORABLES, ");
				sql.append(" GT.SELECCIONFESTIVOS, ");
				sql.append(" GT.TIPODIASGUARDIA, ");
				sql.append(" GT.DIASGUARDIA, ");
				sql.append(" GT.DIASPAGADOS, ");
				sql.append(" GT.DIASSEPARACIONGUARDIAS, ");
				sql.append(" GT.PORGRUPOS, ");
				sql.append(" IG.FECHASUSCRIPCION, ");
				sql.append(" IG.IDPERSONA ");
			sql.append(" FROM SCS_GUARDIASTURNO GT, ");
				sql.append(" SCS_INSCRIPCIONGUARDIA IG ");
			sql.append(" WHERE GT.IDINSTITUCION = IG.IDINSTITUCION ");
				sql.append(" AND GT.IDTURNO = IG.IDTURNO ");
				sql.append(" AND GT.IDGUARDIA = IG.IDGUARDIA ");
			
			contador++;
				sql.append(" AND IG.IDINSTITUCION = :" + contador);
			htCodigos.put(new Integer(contador), idInstitucion);
			
			contador++;
				sql.append(" AND IG.IDTURNO = :" + contador);
			htCodigos.put(new Integer(contador), idTurno);
			
			contador++;
				sql.append(" AND IG.IDPERSONA = :" + contador);		
			htCodigos.put(new Integer(contador), idPersona);
			
				sql.append(" AND IG.FECHABAJA IS NULL ");
				sql.append(" AND (IG.FECHADENEGACION IS NULL ");
					sql.append(" OR IG.FECHAVALIDACION IS NOT NULL) ");
	
			if (idGuardia != null) {
				contador++;
				sql.append(" AND IG.IDGUARDIA = :" + contador);
				htCodigos.put(new Integer(contador), idGuardia);
			}
	
			sql.append(" ORDER BY GT.NOMBRE ");
	
			List<ScsInscripcionGuardiaBean> alInscripcion = null;
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				alInscripcion = new ArrayList<ScsInscripcionGuardiaBean>();
				ScsInscripcionGuardiaBean inscripcionBean = null;
				ScsGuardiasTurnoBean guardia = null;

				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					inscripcionBean = new ScsInscripcionGuardiaBean();
					guardia = new ScsGuardiasTurnoBean();

					guardia.setIdTurno(idTurno);
					guardia.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDINSTITUCION));
					guardia.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDGUARDIA));
					guardia.setNombre(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_NOMBRE));
					guardia.setNumeroLetradosGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
					guardia.setNumeroSustitutosGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA));
					guardia.setSeleccionLaborables(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_SELECCIONLABORABLES));
					guardia.setSeleccionFestivos(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS));
					guardia.setDiasGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASGUARDIA));
					guardia.setTipodiasGuardia(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_TIPODIASGUARDIA));
					String literalTipoDias = "";
					if (guardia.getTipodiasGuardia().equalsIgnoreCase("D"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.dias";
					else if (guardia.getTipodiasGuardia().equalsIgnoreCase("S"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.semanas";
					else if (guardia.getTipodiasGuardia().equalsIgnoreCase("M"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.meses";
					else if (guardia.getTipodiasGuardia().equalsIgnoreCase("Q"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.quincenas";
					guardia.setDescripcionTipoDiasGuardia(literalTipoDias);

					guardia.setDiasPagados(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASPAGADOS));
					guardia.setDiasSeparacionGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS));

					String seleccionTiposDia = ScsGuardiasTurnoAdm.obtenerTipoDia(guardia.getSeleccionLaborables(), guardia.getSeleccionFestivos(), this.usrbean);
					guardia.setSeleccionTiposDia(seleccionTiposDia);

					guardia.setPorGrupos(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_PORGRUPOS));
					
					inscripcionBean.setGuardia(guardia);
					inscripcionBean.setFechaSuscripcion(UtilidadesHash.getString(htFila, ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION));
					inscripcionBean.setIdGuardia(guardia.getIdGuardia());
					inscripcionBean.setIdInstitucion(guardia.getIdInstitucion());
					inscripcionBean.setIdPersona(idPersona);
					inscripcionBean.setIdTurno(idTurno);

					alInscripcion.add(inscripcionBean);
				}
			}
			
			return alInscripcion;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getGuardiasInscripcion()");
		}			
	}
	
	/**
	 * Obtiene las guardias de un turno
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsInscripcionGuardiaBean> getGuardiasParaInscripcion (Integer idInstitucion, Integer idTurno, Long idPersona, Integer idGuardia) throws ClsExceptions {
		try {
			Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
			int contador = 0;
			StringBuffer sql = new StringBuffer();
	
			sql.append(" SELECT GT.IDINSTITUCION, ");
				sql.append(" GT.IDGUARDIA, ");
				sql.append(" GT.NOMBRE, ");
				sql.append(" GT.NUMEROLETRADOSGUARDIA, ");
				sql.append(" GT.NUMEROSUSTITUTOSGUARDIA, ");
				sql.append(" GT.SELECCIONLABORABLES, ");
				sql.append(" GT.SELECCIONFESTIVOS, ");
				sql.append(" GT.TIPODIASGUARDIA, ");
				sql.append(" GT.DIASGUARDIA, ");
				sql.append(" GT.DIASPAGADOS, ");
				sql.append(" GT.DIASSEPARACIONGUARDIAS, ");
				sql.append(" GT.PORGRUPOS  ");
			sql.append(" FROM SCS_GUARDIASTURNO GT ");
			
			sql.append(" WHERE GT.IDINSTITUCION = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), idInstitucion);
			
			sql.append(" AND GT.IDTURNO = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), idTurno);
			
			if (idGuardia != null) {
				sql.append(" AND GT.IDGUARDIA = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), idGuardia);
			}
	
			sql.append(" ORDER BY GT.NOMBRE ");
	
			List<ScsInscripcionGuardiaBean> alInscripcion = null;
			RowsContainer rc = new RowsContainer();
			if (rc.findBind(sql.toString(), htCodigos)) {
				alInscripcion = new ArrayList<ScsInscripcionGuardiaBean>();
				ScsInscripcionGuardiaBean inscripcionBean = null;
				ScsGuardiasTurnoBean guardia = null;

				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					inscripcionBean = new ScsInscripcionGuardiaBean();
					guardia = new ScsGuardiasTurnoBean();

					guardia.setIdTurno(idTurno);
					guardia.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDINSTITUCION));
					guardia.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDGUARDIA));
					guardia.setNombre(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_NOMBRE));
					guardia.setNumeroLetradosGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
					guardia.setNumeroSustitutosGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA));
					guardia.setSeleccionLaborables(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_SELECCIONLABORABLES));
					guardia.setSeleccionFestivos(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS));
					guardia.setDiasGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASGUARDIA));
					guardia.setTipodiasGuardia(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_TIPODIASGUARDIA));
					String literalTipoDias = "";
					if (guardia.getTipodiasGuardia().equalsIgnoreCase("D"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.dias";
					else if (guardia.getTipodiasGuardia().equalsIgnoreCase("S"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.semanas";
					else if (guardia.getTipodiasGuardia().equalsIgnoreCase("M"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.meses";
					else if (guardia.getTipodiasGuardia().equalsIgnoreCase("Q"))
						literalTipoDias = "gratuita.altaTurnos_2.literal.quincenas";
					guardia.setDescripcionTipoDiasGuardia(literalTipoDias);

					guardia.setDiasPagados(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASPAGADOS));
					guardia.setDiasSeparacionGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS));

					String seleccionTiposDia = ScsGuardiasTurnoAdm.obtenerTipoDia(guardia.getSeleccionLaborables(), guardia.getSeleccionFestivos(), this.usrbean);
					guardia.setSeleccionTiposDia(seleccionTiposDia);
					
					guardia.setPorGrupos(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_PORGRUPOS));

					inscripcionBean.setGuardia(guardia);
					inscripcionBean.setIdGuardia(guardia.getIdGuardia());
					inscripcionBean.setIdInstitucion(guardia.getIdInstitucion());
					inscripcionBean.setIdPersona(idPersona);
					inscripcionBean.setIdTurno(idTurno);

					alInscripcion.add(inscripcionBean);
				}
			}
			
			return alInscripcion;

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getGuardiasParaInscripcion()");
		}				
	}
	
	/**
	 * Obtiene las inscripciones ordenadas para formar la cola de una guardia dada una fecha.
	 * Importante: obtiene todas las inscripciones, no solo las que estan de alta, ya que luego 
	 * hay que ordenarlas segun el puntero, que puede estar apuntando a una inscripcion de baja
	 */
	public Vector<ScsInscripcionGuardiaBean> getColaGuardia(String idinstitucion, String idturno, String idguardia, String fechaInicio, String fechaFin, boolean porGrupos, String order) throws ClsExceptions {
		try {
			if (idinstitucion == null || idinstitucion.equals(""))
				return null;
			if (idturno == null || idturno.equals(""))
				return null;
			if (idguardia == null || idguardia.equals(""))
				return null;
			if (fechaInicio == null || fechaInicio.equals(""))
				fechaInicio = "null";
			else if (!fechaInicio.trim().equalsIgnoreCase("sysdate"))
				fechaInicio = "'" + fechaInicio.trim() + "'";
			if (fechaFin == null || fechaFin.equals(""))
				fechaFin = "null";
			else if (!fechaFin.trim().equalsIgnoreCase("sysdate"))
				fechaFin = "'" + fechaFin.trim() + "'";
			
			StringBuffer consulta = new StringBuffer();
			consulta.append("Select ");
			consulta.append("       (case when Ins.Fechavalidacion Is Not Null ");
			consulta.append("              And Trunc(Ins.Fechavalidacion) <= nvl("+fechaInicio+",  Ins.Fechavalidacion) ");
			consulta.append("              And (Ins.Fechabaja Is Null Or ");
			consulta.append("                   Trunc(Ins.Fechabaja) > nvl("+fechaFin+", '01/01/1900')) ");
			consulta.append("             then '1' ");
			consulta.append("             else '0' ");
			consulta.append("        end) Activo, ");
			consulta.append(getBaseConsultaInscripciones());
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Gua.Idinstitucion = "+idinstitucion+" ");
			consulta.append("   And Gua.Idturno = "+idturno+" ");
			consulta.append("   And Gua.Idguardia = "+idguardia+" ");
			
			if (! (order == null || order.equals("")))
				consulta.append(" order by " + order);
			else
				consulta.append(" order by Ins.FechaValidacion ");
			
			Vector<ScsInscripcionGuardiaBean> datos = null;
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta.toString())) {
				datos = new Vector<ScsInscripcionGuardiaBean>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					datos.add(beanToHashCola(htFila));
				}
			}
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getColaGuardia()");
		}				
	} //getColaGuardia()
	
	public boolean getOrdenGuardia(String idinstitucion, String idturno, String idguardia, String fecha) throws ClsExceptions {
		try  {
			if (idinstitucion == null || idinstitucion.equals(""))
				return false;
			if (idturno == null || idturno.equals(""))
				return false;
			if (idguardia == null || idguardia.equals(""))
				return false;
			if (fecha == null || fecha.equals(""))
				fecha = "null";
			else if (!fecha.trim().equalsIgnoreCase("sysdate"))
				fecha = "'" + fecha.trim() + "'";
			
			StringBuffer consulta = new StringBuffer();
			boolean existeOrdenEnGrupoGuardia = false;
			consulta.append("Select grupo, ordengrupo, count(*) from "); 
			consulta.append("	(select " );
	
			consulta.append(getBaseConsultaInscripciones());
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Gua.Idinstitucion = "+idinstitucion+" ");
			consulta.append("   And Gua.Idturno = "+idturno+" ");
			consulta.append("   And Gua.Idguardia = "+idguardia+" ");
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Trunc(Ins.Fechavalidacion) <= "+fecha);
			consulta.append("   And (Ins.Fechabaja Is Null Or  Trunc(Ins.Fechabaja) > "+ fecha +" ) ");
			consulta.append("   And gru.idgrupoguardia is not null ");	
			consulta.append(" ) ");	
			consulta.append(" group by grupo, ordengrupo having count(*) > 1");
			
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta.toString())) {
				if(rc.size() > 0) {
					existeOrdenEnGrupoGuardia = true;
				}
			}

			return existeOrdenEnGrupoGuardia;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getOrdenGuardia()");
		}				
	} //getOrdenGuardia()
		
	public boolean getGrupoGuardia(String idinstitucion, String idturno, String idguardia, String fecha) throws ClsExceptions {
		try {
			if (idinstitucion == null || idinstitucion.equals(""))
				return false;
			if (idturno == null || idturno.equals(""))
				return false;
			if (idguardia == null || idguardia.equals(""))
				return false;
			if (fecha == null || fecha.equals(""))
				fecha = "null";
			else if (!fecha.trim().equalsIgnoreCase("sysdate"))
				fecha = "'" + fecha.trim() + "'";
			
			StringBuffer consulta = new StringBuffer();
			boolean existePersonaEnGrupoGuardia = false;
			consulta.append("Select grupo, idpersona, fechasuscripcion, count(*) from "); 
			consulta.append("	(select " );
	
			consulta.append(getBaseConsultaInscripciones());
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Gua.Idinstitucion = "+idinstitucion+" ");
			consulta.append("   And Gua.Idturno = "+idturno+" ");
			consulta.append("   And Gua.Idguardia = "+idguardia+" ");
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Trunc(Ins.Fechavalidacion) <= "+fecha);
			consulta.append("   And (Ins.Fechabaja Is Null Or  Trunc(Ins.Fechabaja) > "+ fecha +" ) ");
			consulta.append("   And gru.idgrupoguardia is not null ");	
			consulta.append(" ) ");	
			consulta.append(" group by grupo, idpersona, fechasuscripcion having count(*) > 1");
			
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta.toString())) {
				if(rc.size() > 0) {
					existePersonaEnGrupoGuardia = true;
				}
			}

			return existePersonaEnGrupoGuardia;
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getGrupoGuardia()");
		}				
	} //getgrupoGuardia()
	
	/**
	 * Obtiene las inscripcion activa de la persona en la guardia en la fecha dada
	 */
	public ScsInscripcionGuardiaBean getInscripcionGuardiaActiva(String idinstitucion, String idturno, String idguardia, String idpersona, String fecha) throws ClsExceptions {
		try {
			if (idinstitucion == null || idinstitucion.equals(""))		return null;
			if (idturno == null || idturno.equals(""))					return null;
			if (idguardia == null || idguardia.equals(""))				return null;
			if (idpersona == null || idpersona.equals(""))				return null;
			if (fecha == null || fecha.equals(""))						return null;
			if(!fecha.equals("sysdate"))
				fecha = "'"+fecha+"'";
			String consulta = 
				"Select " +
				getBaseConsultaInscripciones() +
				
				"   And Ins.Fechavalidacion Is Not Null " +
				"   And Gua.Idinstitucion = "+idinstitucion+" " +
				"   And Gua.Idturno = "+idturno+" " +
				"   And Gua.Idguardia = "+idguardia+" " +
				
			    "   And Ins.Fechavalidacion Is Not Null " +
				"   And Trunc(Ins.Fechavalidacion) <= nvl("+fecha+",  Ins.Fechavalidacion) " +
				"   And (Ins.Fechabaja Is Null Or " +
				"        Trunc(Ins.Fechabaja) > nvl("+fecha+", '01/01/1900')) "+
				"    and Ins.idpersona ="+idpersona;
			
			Vector<ScsInscripcionGuardiaBean> datos = null;
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta)) {
				datos = new Vector<ScsInscripcionGuardiaBean>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					datos.add(beanToHashCola(htFila));
				}
			}else
				return null;
				
			return datos.get(0);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionActiva()");
		}				
	}
	
	/**
	 * Obtiene los letrados dado un grupo
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idGrupoGuardia
	 * @return Vector<Hashtable>
	 * @throws ClsExceptions
	 */
	public Vector<ScsInscripcionGuardiaBean> getLetradosGrupo(String idInstitucion, String idTurno, String idGuardia, String idGrupoGuardia, String fechaGuardia) throws ClsExceptions {
		try  {
			if (idGrupoGuardia == null || idGrupoGuardia.equals(""))
				return null;
			
			String consulta ="Select " ;
			if(fechaGuardia!=null && !fechaGuardia.equals("")){
				consulta+=" (case "+
			         " when Ins.Fechavalidacion Is Not Null And "+
			              " Trunc(Ins.Fechavalidacion) <= "+
			              " nvl('"+fechaGuardia+"', Ins.Fechavalidacion) And "+
			              " (Ins.Fechabaja Is Null Or "+
			              " Trunc(Ins.Fechabaja) > nvl('"+fechaGuardia+"', '01/01/1900')) then "+
			          " '1' "+
			         " else "+
			          " '0' "+
			       " end) Activo, ";
			}else{
				consulta+="'1' Activo,";
			}
			consulta+=getBaseConsultaInscripciones() +
				"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION+" = "+idInstitucion+" " +
				"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDTURNO+" = "+idTurno+" " +
				"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA+" = "+idGuardia+" " +
				"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGRUPO+" = "+idGrupoGuardia+" ";
			consulta+=" ORDER BY ORDENGRUPO ";
			Vector<ScsInscripcionGuardiaBean> datos = null;
			Hashtable hashGrupo;
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta)) {
				datos = new Vector<ScsInscripcionGuardiaBean>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					if(((String)htFila.get("ACTIVO"))!=null && ((String)htFila.get("ACTIVO")).equals("1") )
						datos.add(beanToHashCola(htFila));
				}
			}
			
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getLetradosGrupo()");
		}				
	}
	
	protected ScsInscripcionGuardiaBean beanToHashCola(Hashtable hash) throws ClsExceptions {
		try {
			ScsInscripcionGuardiaBean inscripcionBean = new ScsInscripcionGuardiaBean();
			
			inscripcionBean.setIdInstitucion(UtilidadesHash.getInteger(hash, ScsInscripcionGuardiaBean.C_IDINSTITUCION));
			inscripcionBean.setIdTurno(UtilidadesHash.getInteger(hash, ScsInscripcionGuardiaBean.C_IDTURNO));
			inscripcionBean.setIdGuardia(UtilidadesHash.getInteger(hash, ScsInscripcionGuardiaBean.C_IDGUARDIA));
			inscripcionBean.setIdPersona(UtilidadesHash.getLong(hash, ScsInscripcionGuardiaBean.C_IDPERSONA));
			inscripcionBean.setFechaSuscripcion(UtilidadesHash.getString(hash, ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION));
			inscripcionBean.setFechaValidacion(UtilidadesHash.getString(hash, ScsInscripcionGuardiaBean.C_FECHAVALIDACION));
			inscripcionBean.setFechaBaja(UtilidadesHash.getString(hash, ScsInscripcionGuardiaBean.C_FECHABAJA));
			inscripcionBean.setIdGrupoGuardiaColegiado(UtilidadesHash.getLong(hash, ScsInscripcionGuardiaBean.C_IDGRUPOGUARDIACOLEGIADO));
			inscripcionBean.setGrupo(UtilidadesHash.getInteger(hash, ScsInscripcionGuardiaBean.C_GRUPO));
			inscripcionBean.setOrdenGrupo(UtilidadesHash.getInteger(hash, ScsInscripcionGuardiaBean.C_ORDENGRUPO));
			inscripcionBean.setNumeroGrupo(UtilidadesHash.getString(hash, ScsGrupoGuardiaBean.C_NUMEROGRUPO));
			inscripcionBean.setEstado(UtilidadesHash.getString(hash, "ACTIVO"));
			
			CenPersonaBean personaBean = new CenPersonaBean(
				inscripcionBean.getIdPersona(), 
				(String) hash.get(CenPersonaBean.C_NOMBRE), 
				(String) hash.get(CenPersonaBean.C_APELLIDOS1),
				(String) hash.get(CenPersonaBean.C_APELLIDOS2), 
				(String) hash.get(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO));
			
			personaBean.setNIFCIF(UtilidadesHash.getString(hash, CenPersonaBean.C_NIFCIF));
			
			inscripcionBean.setPersona(personaBean);
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionDesdeHashCola()");
		}					
	}
	
	public String getBaseConsultaInscripciones() {
		return new String(
			"       Ins.Idinstitucion,"+
			"       Ins.Idturno, " +
			"       Ins.Idguardia, " +
			"       Per.Idpersona, " +
			"       Ins.fechasuscripcion As Fechasuscripcion, "+
			"       TO_CHAR(TRUNC(Ins.fechavalidacion),'DD/MM/YYYY') As Fechavalidacion, "+
		    "       TO_CHAR(trunc(Ins.fechabaja),'DD/MM/YYYY') As Fechabaja, "+
		    
		    "       Per.Nifcif,"+
			"       Per.Nombre, " +
			"       Per.Apellidos1, " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) apellidos2, " +
			"       Per.Apellidos1 || " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) "+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+", " +
			"       Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) "+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+", " +
			"       Per.Fechanacimiento "+ScsOrdenacionColasBean.C_FECHANACIMIENTO+", " +
			"       Ins.Fechavalidacion AS "+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+", " +
			"       Decode(Gua.Porgrupos, '1', Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO+", Null) As Idgrupoguardiacolegiado, " +
			"       Decode(Gua.Porgrupos, '1', Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGRUPO+", Null) As Grupo, " +
			"       Decode(Gua.Porgrupos, '1', Grg."+ScsGrupoGuardiaBean.C_NUMEROGRUPO+", Null) As numeroGrupo, " +
			"       Decode(Gua.Porgrupos, '1', Gru."+ScsGrupoGuardiaColegiadoBean.C_ORDEN+", Null) As Ordengrupo " +
			"  From Scs_Guardiasturno         Gua, " +
			"       Cen_Colegiado             Col, " +
			"       Cen_Persona               Per, " +
			"       Scs_Inscripcionguardia    Ins, " +
			"       "+ScsGrupoGuardiaColegiadoBean.T_NOMBRETABLA+" Gru, " +
			"       "+ScsGrupoGuardiaBean.T_NOMBRETABLA+" Grg " +
			" Where Col.Idpersona = Per.Idpersona " +
			"   And Ins.Idinstitucion = Gua.Idinstitucion " +
			"   And Ins.Idturno = Gua.Idturno " +
			"   And Ins.Idguardia = Gua.Idguardia " +
			"   And Ins.Idinstitucion = Col.Idinstitucion " +
			"   And Ins.Idpersona = Col.Idpersona " +
			"   And Ins.Idinstitucion = Gru.Idinstitucion(+) " +
			"   And Ins.Idturno = Gru.Idturno(+) " +
			"   And Ins.Idguardia = Gru.Idguardia(+) " +
			"   And Ins.Idpersona = Gru.Idpersona(+) " +
			"   And Ins.Fechasuscripcion = Gru.Fechasuscripcion(+) " +
			"   And Gru.Idgrupoguardia = Grg.Idgrupoguardia(+) ");
	}
	
	public Vector<ScsInscripcionGuardiaBean> getColegiadosInscritosRepetidos(String idinstitucion, String idturno, String idguardia, String fechaInicio, String fechaFin) throws ClsExceptions {
		try {	
			if (idinstitucion == null || idinstitucion.equals(""))
				return null;
			if (idturno == null || idturno.equals(""))
				return null;
			if (idguardia == null || idguardia.equals(""))
				return null;
			if (fechaInicio == null || fechaInicio.equals(""))
				fechaInicio = "null";
			else if (!fechaInicio.trim().equalsIgnoreCase("sysdate"))
				fechaInicio = "'" + fechaInicio.trim() + "'";
			if (fechaFin == null || fechaFin.equals(""))
				fechaFin = "null";
			else if (!fechaFin.trim().equalsIgnoreCase("sysdate"))
				fechaFin = "'" + fechaFin.trim() + "'";
			
					
			StringBuffer consulta = new StringBuffer();
			consulta.append("SELECT datos.Nombre AS NOMBRE_REP, datos.Apellidos1 AS APE1_REP, datos.apellidos2 AS APE2_REP, datos.NUMEROCOLEGIADO AS NCOLEGIADO_REP ");
			consulta.append("  FROM ( ");
			consulta.append("Select ");
			consulta.append("       (case when Ins.Fechavalidacion Is Not Null ");
			consulta.append("              And Trunc(Ins.Fechavalidacion) <= nvl("+fechaInicio+",  Ins.Fechavalidacion) ");
			consulta.append("              And (Ins.Fechabaja Is Null Or ");
			consulta.append("                   Trunc(Ins.Fechabaja) > nvl("+fechaFin+", '01/01/1900')) ");
			consulta.append("             then '1' ");
			consulta.append("             else '0' ");
			consulta.append("        end) Activo, ");
			consulta.append(getBaseConsultaInscripciones());
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Gua.Idinstitucion = "+idinstitucion+" ");
			consulta.append("   And Gua.Idturno = "+idturno+" ");
			consulta.append("   And Gua.Idguardia = "+idguardia+" ");
			consulta.append(" order by Ins.FechaValidacion ");
			
			
			consulta.append(" ) datos  ");
			consulta.append(" WHERE datos.activo = 1  ");
			consulta.append(" GROUP BY  datos.Nombre, datos.Apellidos1, datos.apellidos2, datos.NUMEROCOLEGIADO HAVING COUNT(*) > 1  ");
			consulta.append(" ORDER BY datos.Apellidos1 ");
			
			Vector datos = null;
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta.toString())) {
				datos = new Vector();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					datos.add(htFila);
				}
			}

			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getColegiadosInscritosRepetidos()");
		}				
	}
	
	
	public Vector<ScsInscripcionGuardiaBean> getListadoColegiadosInscritosRepetidosOrden(String idinstitucion, String idturno, String idguardia, String fechaInicio, String fechaFin) throws ClsExceptions {
		try {		
			if (idinstitucion == null || idinstitucion.equals(""))
				return null;
			if (idturno == null || idturno.equals(""))
				return null;
			if (idguardia == null || idguardia.equals(""))
				return null;
			if (fechaInicio == null || fechaInicio.equals(""))
				fechaInicio = "null";
			else if (!fechaInicio.trim().equalsIgnoreCase("sysdate"))
				fechaInicio = "'" + fechaInicio.trim() + "'";
			if (fechaFin == null || fechaFin.equals(""))
				fechaFin = "null";
			else if (!fechaFin.trim().equalsIgnoreCase("sysdate"))
				fechaFin = "'" + fechaFin.trim() + "'";
							
			StringBuffer consulta = new StringBuffer();
			consulta.append("SELECT datos.Nombre AS NOMBRE_REP, datos.Apellidos1 AS APE1_REP, datos.apellidos2 AS APE2_REP, datos.NUMEROCOLEGIADO AS NCOLEGIADO_REP ");
			consulta.append("  ,datos.numeroGrupo AS GRUPO_REP,datos.ordenGrupo AS ORDENGRUPO_REP");
			consulta.append("  FROM ( ");
			consulta.append("Select ");
			consulta.append("       (case when Ins.Fechavalidacion Is Not Null ");
			consulta.append("              And Trunc(Ins.Fechavalidacion) <= nvl("+fechaInicio+",  Ins.Fechavalidacion) ");
			consulta.append("              And (Ins.Fechabaja Is Null Or ");
			consulta.append("                   Trunc(Ins.Fechabaja) > nvl("+fechaFin+", '01/01/1900')) ");
			consulta.append("             then '1' ");
			consulta.append("             else '0' ");
			consulta.append("        end) Activo, ");
			consulta.append(getBaseConsultaInscripciones());
			
			consulta.append("   And Ins.Fechavalidacion Is Not Null ");
			consulta.append("   And Gua.Idinstitucion = "+idinstitucion+" ");
			consulta.append("   And Gua.Idturno = "+idturno+" ");
			consulta.append("   And Gua.Idguardia = "+idguardia+" ");
			consulta.append("   AND (ins.idinstitucion, ins.idturno, ins.idguardia, ins.idpersona,  ");
			consulta.append("   ins.fechasuscripcion) IN ");
			consulta.append("   (SELECT ins2.idinstitucion,	");
			consulta.append("   ins2.idturno, ");
			consulta.append("   ins2.idguardia, ");
			consulta.append("   ins2.idpersona, ");
			consulta.append("   ins2.fechasuscripcion ");
			consulta.append("   FROM scs_grupoguardiacolegiado ins2 ");
			consulta.append("   GROUP BY ins2.idinstitucion, ");
			consulta.append("   ins2.idturno, ins2.idguardia, ins2.idpersona, ins2.fechasuscripcion ");
			consulta.append("   HAVING COUNT(*) > 1)  ");		           		
			consulta.append(" ) datos  ");
			consulta.append(" WHERE datos.activo = 1  ");
			consulta.append(" ORDER BY datos.Apellidos1,datos.numeroGrupo  ");
			
			Vector datos = null;
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta.toString())) {
				datos = new Vector();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					datos.add(htFila);
				}
			}
				
			return datos;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getListadoColegiadosInscritosRepetidosOrden()");
		}				
	}
	
	public String getCantidadGrupos(String idinstitucion, String idturno, String idguardia, String fechaInicio, String fechaFin, String numeroGrupo) throws ClsExceptions {
		try {
			String cantidad="0";
			if (idinstitucion == null || idinstitucion.equals(""))
				return "0";
			if (idturno == null || idturno.equals(""))
				return "0";
			if (idguardia == null || idguardia.equals(""))
				return "0";
			if (fechaInicio == null || fechaInicio.equals(""))
				fechaInicio = "null";
			else if (!fechaInicio.trim().equalsIgnoreCase("sysdate"))
				fechaInicio = "'" + fechaInicio.trim() + "'";
			if (fechaFin == null || fechaFin.equals(""))
				fechaFin = "null";
			else if (!fechaFin.trim().equalsIgnoreCase("sysdate"))
				fechaFin = "'" + fechaFin.trim() + "'";
				
			StringBuffer consulta = new StringBuffer();
			consulta.append("SELECT count(*) AS CANTIDAD");
			consulta.append("  FROM ( ");
			consulta.append("Select ");
			consulta.append("       (case when Ins.Fechavalidacion Is Not Null ");
			consulta.append("              And Trunc(Ins.Fechavalidacion) <= nvl("+fechaInicio+",  Ins.Fechavalidacion) ");
			consulta.append("              And (Ins.Fechabaja Is Null Or ");
			consulta.append("                   Trunc(Ins.Fechabaja) > nvl("+fechaFin+", '01/01/1900')) ");
			consulta.append("             then '1' ");
			consulta.append("             else '0' ");
			consulta.append("        end) Activo ");
			consulta.append("   FROM Scs_Guardiasturno         Gua, ");
			consulta.append("  		 Scs_Inscripcionguardia    Ins,  ");
			consulta.append("  		 SCS_GRUPOGUARDIACOLEGIADO Gru,  ");
			consulta.append(" 		 SCS_GRUPOGUARDIA          Grg   ");
			consulta.append("  WHERE Ins.Idinstitucion = Gua.Idinstitucion  ");
			consulta.append("  		AND Ins.Idturno = Gua.Idturno  ");
			consulta.append("  		AND Ins.Idguardia = Gua.Idguardia  ");
			consulta.append("  		AND Ins.Idinstitucion = Gru.Idinstitucion(+)  ");
			consulta.append("  		AND Ins.Idturno = Gru.Idturno(+)  ");
			consulta.append("  		AND Ins.Idguardia = Gru.Idguardia(+)  ");
			consulta.append(" 		AND Ins.Idpersona = Gru.Idpersona(+)   ");
			consulta.append("  		AND Ins.Fechasuscripcion = Gru.Fechasuscripcion(+)  ");
			consulta.append("   	AND Gru.Idgrupoguardia = Grg.Idgrupoguardia(+) ");
			consulta.append("   	And Grg.NUMEROGRUPO = "+numeroGrupo+" ");
			consulta.append("  		And Gua.Idinstitucion = "+idinstitucion+" ");
			consulta.append("   	And Gua.Idturno = "+idturno+" ");
			consulta.append("   	And Gua.Idguardia = "+idguardia+" ");
			consulta.append(" ) datos ");
			consulta.append(" WHERE datos.activo = 1  ");
			
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta.toString())) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					cantidad = (String)(htFila.get("CANTIDAD"));
				}
			}
			
			return cantidad;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getCantidadGrupos()");
		}				
	}
	
	/**
	 * Obtiene la ultima inscripcion de guardia de baja, de la guardia indicada
	 * - Debe tener fecha validacion
	 * - Debe tener fecha de baja
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public ScsInscripcionGuardiaBean getInscripcionGuardiaUltimaBaja (Integer idInstitucion, Integer idTurno, Long idPersona, Integer idGuardia) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +				
				" FROM SCS_INSCRIPCIONGUARDIA " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND IDPERSONA = " + idPersona +
					" AND IDGUARDIA = " + idGuardia + 
					" AND FECHAVALIDACION IS NOT NULL " +
					" AND FECHABAJA IS NOT NULL " +
				" ORDER BY FECHABAJA DESC";
					
			ScsInscripcionGuardiaBean inscripcionBean = null;
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionGuardiaBean) this.hashTableToBean(htFila);
				}
			} 
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionBaja()");
		}				
	}		
	
	/**
	 * Obtiene la ultima inscripcion de la guardia de alta
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public ScsInscripcionGuardiaBean getInscripcionGuardiaUltimaSinBaja (Integer idInstitucion, Integer idTurno, Long idPersona, Integer idGuardia) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +			
				" FROM SCS_INSCRIPCIONGUARDIA " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND IDPERSONA = " + idPersona +
					" AND IDGUARDIA = " + idGuardia + 
					" AND FECHABAJA IS NULL " +
					" AND (FECHADENEGACION IS NULL OR FECHAVALIDACION IS NOT NULL) " +
				" ORDER BY FECHAVALIDACION DESC";
					
			ScsInscripcionGuardiaBean inscripcionBean = null;
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					inscripcionBean =  (ScsInscripcionGuardiaBean) this.hashTableToBean(htFila);
				}
			} 
			
			return inscripcionBean;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionUltima()");
		}					
	}
	
	/**
	 * Obtiene las inscripciones de guardia pendientes de validar
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param isValidacionInscripcion
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<ScsInscripcionGuardiaBean> getInscripcionesGuardiasPendientesValidar(String idInstitucion, String idTurno, boolean bAltaInscripcion) throws ClsExceptions {
		try {
			String sql = " SELECT " + camposSelect +
				" FROM SCS_INSCRIPCIONGUARDIA " +
				" WHERE IDINSTITUCION = " + idInstitucion +
					" AND IDTURNO = " + idTurno +
					" AND FECHADENEGACION IS NULL " + 
					" AND FECHABAJA IS NULL ";
			
			if (bAltaInscripcion)
				sql += " AND FECHAVALIDACION IS NULL ";
			else
				sql += " AND FECHASOLICITUDBAJA IS NOT NULL ";
			
			Vector<ScsInscripcionGuardiaBean> datos = null;
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql)) {
        	   
				datos = new Vector<ScsInscripcionGuardiaBean>();
    			for (int i = 0; i < rc.size(); i++){

					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					ScsInscripcionGuardiaBean inscripcionBean = (ScsInscripcionGuardiaBean) this.hashTableToBean(htFila);
					
					datos.add(inscripcionBean);
				}
            }
			
	       return datos;		
	       
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getInscripcionesGuardiaPendientesValidar()");
		}		       
	}		
}