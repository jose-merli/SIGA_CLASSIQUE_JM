package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.InscripcionTGForm;

/**
 * Implementa las operaciones sobre la base de datos a la tabla SCS_INSCRIPCIONGUARDIA
 */
public class ScsInscripcionGuardiaAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsInscripcionGuardiaAdm (UsrBean usuario) {
		super(ScsInscripcionGuardiaBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean()
	{
		String[] campos =
		{
				ScsInscripcionGuardiaBean.C_IDPERSONA,
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
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA
		};
		return campos;
	}
	
	/**
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos =
		{
				ScsInscripcionGuardiaBean.C_IDINSTITUCION,
				ScsInscripcionGuardiaBean.C_IDPERSONA,
				ScsInscripcionGuardiaBean.C_IDTURNO,
				ScsInscripcionGuardiaBean.C_IDGUARDIA,
				ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION
		};
		return campos;
	}
	
	/**
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsInscripcionGuardiaBean bean = null;
		try{
			bean = new ScsInscripcionGuardiaBean();
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
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	
	/** Esta funcion habria que borrarla de aqui:
	 * pero hay que tener en cuenta que se usa desde una JSP
	 */
	public Hashtable obtenerNumCuenta(Long idpersona,Integer idInstitucion) throws SIGAException, ClsExceptions {
		try{
			String consulta="select count(C.IDCUENTA)AS NUM from cen_cuentasbancarias C where C.IDPERSONA = "+ idpersona+" AND C.IDINSTITUCION = "+idInstitucion+" AND (c.ABONOCARGO = 'C' OR c.ABONOCARGO = 'T') and c.fechabaja IS NULL";
			
			Vector vector=null;
			vector=(Vector)ejecutaSelect(consulta);
			String numcuentas=(String)((Hashtable)vector.get(0)).get("NUM");
			Hashtable cuentaelegida=new Hashtable();
			
			if(numcuentas.equals("1")){
				
				consulta="select (C.CBO_CODIGO || '-' ||C.CODIGOSUCURSAL || '-' || C.DIGITOCONTROL || '-' ||LPAD(SUBSTR(C.NUMEROCUENTA, 7), 10, '*')) as DESCRIPCION, C.IDCUENTA A from cen_cuentasbancarias C where C.IDPERSONA = "+idpersona+" AND C.IDINSTITUCION = "+idInstitucion+" AND (c.ABONOCARGO = 'C' OR c.ABONOCARGO = 'T') and c.fechabaja IS NULL";
				vector=(Vector)ejecutaSelect(consulta);
				cuentaelegida.put("NUMCUENTA",(String)((Hashtable)vector.get(0)).get("DESCRIPCION"));
				cuentaelegida.put("IDCUENTA",(String)((Hashtable)vector.get(0)).get("A"));
			}
			else{
				cuentaelegida.put("NUMCUENTA","");
				cuentaelegida.put("IDCUENTA","");
			}
			
			return (cuentaelegida);
		}catch(Exception e){
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
	}
	
	/**
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
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
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return null;
	}
	
	/**
	 * Funcion ejecutaSelect(String select)
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
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

	public ScsInscripcionGuardiaBean getSiguienteInscripcion(String idInstitucion,
			String idTurno,
			String idPersona,
			Integer idGuardia,
			String fechaBajaTurno,
			String fechaValGuardia) throws ClsExceptions
	{

		Vector datos = new Vector();
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador = 0;
		sql.append(" SELECT * ");

		sql.append(" FROM SCS_INSCRIPCIONGUARDIA I ");
		sql.append(" WHERE I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idInstitucion);
		sql.append(" AND I.IDTURNO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idTurno);
		sql.append(" AND I.IDPERSONA = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idPersona);
		if (idGuardia != null) {
			sql.append(" AND I.IDGUARDIA = :");
			contador++;
			sql.append(contador);
			htCodigos.put(contador, idGuardia);
		}

		// NO SACAR LAS CANCELADAS
		sql.append(" AND  ");
		// VALIDADOS DE ALTA
		sql.append(" I.FECHAVALIDACION IS NOT NULL AND ");
		sql.append(" TRUNC(I.FECHAVALIDACION) >= :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, fechaValGuardia);
		if (fechaBajaTurno != null && !fechaBajaTurno.equals("")) {
			sql.append(" AND TRUNC(I.FECHAVALIDACION)< :");
			contador++;
			sql.append(contador);
			htCodigos.put(contador, fechaBajaTurno);
		}

		// BAJA DENEGADA

		sql.append(" ORDER BY I.FECHAVALIDACION ");

		ScsInscripcionGuardiaBean inscripcionBean = null;
		try {
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					inscripcionBean = (ScsInscripcionGuardiaBean) this.hashTableToBean(htFila);
					break;

				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return inscripcionBean;
	}
	
	public Vector getInscripcionActiva(String idInstitucion,String idTurno, String idPersona, Integer idGuardia, String fecha) throws ClsExceptions 
	{
		
		Vector datos = new Vector();
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador =0;
		sql.append(" SELECT I.IDINSTITUCION, ");
		sql.append(" I.IDPERSONA, ");
		sql.append(" I.IDTURNO, ");
		sql.append(" I.IDGUARDIA, ");
		sql.append(" I.FECHASUSCRIPCION, ");
		sql.append(" I.OBSERVACIONESSUSCRIPCION, ");
		sql.append(" I.FECHAVALIDACION, ");
		sql.append(" I.OBSERVACIONESVALIDACION, ");
		sql.append(" I.OBSERVACIONESSUSCRIPCION, ");
		sql.append(" I.FECHASOLICITUDBAJA, ");
		sql.append(" I.OBSERVACIONESBAJA, ");
		sql.append(" I.FECHABAJA ");
		sql.append(" ,I.OBSERVACIONESVALBAJA ");
		sql.append(",I.OBSERVACIONESDENEGACION,I.FECHADENEGACION,");
		sql.append("NVL(I.FECHADENEGACION,I.FECHAVALIDACION) FECHAVALOR");
		//Sacamos este campo, ya que solo se podran incribie en guardia cuando la incripcion en el turno este validado
		//Esto es para los turnos con guardia a elegir y todas-ninguna.
		
		sql.append(" FROM SCS_INSCRIPCIONGUARDIA I ");
		sql.append(" WHERE I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idInstitucion);
		sql.append(" AND I.IDTURNO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idTurno);
		sql.append(" AND I.IDPERSONA = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idPersona);
		if(idGuardia!=null){
			sql.append(" AND I.IDGUARDIA = :");
			contador++;
			sql.append(contador);
			htCodigos.put(contador, idGuardia);
		}
		
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
		sql.append( " AND ( ");
		   //PENDIENTES DE ALTA
		sql.append( " (I.FECHADENEGACION IS NULL AND I.FECHASOLICITUDBAJA IS NULL ");
		sql.append( " AND I.FECHAVALIDACION IS NULL) ");
		     
		sql.append( " OR ");
		     //VALIDADOS DE ALTA
		sql.append( " (I.FECHAVALIDACION IS NOT NULL AND ");
		sql.append( " TRUNC(I.FECHAVALIDACION) <= "); 
		sql.append(fecha);
		sql.append( " AND (I.FECHABAJA IS NULL ");
		sql.append( " OR (I.FECHABAJA IS NOT NULL AND ");
		sql.append( " TRUNC(I.FECHABAJA) > ");
		sql.append(fecha);
		sql.append( ")))" );
	  
		sql.append( " OR ");
		     // PENDIENTES DE BAJA
		sql.append( " (I.FECHASOLICITUDBAJA IS NOT NULL AND I.FECHABAJA IS NULL AND I.FECHADENEGACION IS NULL) ");
		       
		       // BAJA DENEGADA
		sql.append( " OR "); 
		sql.append( " (I.FECHADENEGACION IS NOT NULL AND I.FECHASOLICITUDBAJA IS NOT NULL) ");
		sql.append( " ) ");
		
		sql.append(" ORDER BY I.FECHASUSCRIPCION DESC ");
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(sql.toString(),htCodigos)) {
				if(rc!=null && rc.size()>0){
					Row fila = (Row) rc.get(0);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) {
						datos.add(registro);
					}
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	public boolean existenInscripcionesGuardiaActivas(Integer idInstitucion,Integer idTurno, Long idPersona, String fechaDesde,String fechaHasta) throws ClsExceptions 
	{
		
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador =0;
		sql.append(" SELECT COUNT(*) NUMINSCRIPCIONES ");
		sql.append(" FROM SCS_INSCRIPCIONGUARDIA I ");
		sql.append(" WHERE I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idInstitucion);
		sql.append(" AND I.IDTURNO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idTurno);
		sql.append(" AND I.IDPERSONA = :");
		contador++;
		sql.append(contador);
		htCodigos.put(contador, idPersona);
		
		sql.append(" AND I.FECHABAJA IS NOT NULL AND ");
		
		sql.append(" TRUNC(I.FECHABAJA) between ");
		
		contador ++;
		sql.append(":");
		sql.append(contador);
		htCodigos.put(new Integer(contador),fechaDesde);	
		
		sql.append(" AND ");
		
		contador ++;
		sql.append(":");
		sql.append(contador);
		htCodigos.put(new Integer(contador),fechaHasta);
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		boolean isInscripcionesActivas = false;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(sql.toString(),htCodigos)) {
				if(rc!=null && rc.size()>0){
					Row fila = (Row) rc.get(0);
					Hashtable registro = (Hashtable) fila.getRow();
					isInscripcionesActivas = Integer.parseInt((String)registro.get("NUMINSCRIPCIONES"))>0;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return isInscripcionesActivas;
	}
	
	public String getQueryNumeroColegiadosIncritos(String institucion, String turno, String idguardia, String fecha) throws ClsExceptions
	{

		String consulta = "";
		try {
			consulta = " select count(*) as NLETRADOSINSCRITOS from " + ScsInscripcionGuardiaBean.T_NOMBRETABLA
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
		} catch (Exception e) {
			throw new ClsExceptions(e,
					"Excepcion en ScsInscripcionGuardiaAdm.getQueryNumeroColegiadosIncritos() Consulta SQL:" + consulta);
		}

		return consulta;

	}
	
	public List<ScsInscripcionGuardiaBean> getInscripcionesGuardia(InscripcionTGForm inscripcionguardiaForm,
			boolean isEdicion) throws ClsExceptions
	{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append(" GT.NOMBRE NOMBREGUARDIA, ");
		sql.append(" T.NOMBRE NOMBRETURNO, ");
		sql.append(" T.ABREVIATURA ABREVIATURATURNO, ");
		sql.append(" T.VALIDARINSCRIPCIONES , T.GUARDIAS,");
		sql.append(" DECODE(COL.COMUNITARIO,'1',COL.NCOMUNITARIO, COL.NCOLEGIADO) NCOLEGIADO, ");
		sql.append(" PER.NOMBRE,PER.APELLIDOS1,PER.APELLIDOS2, ");
		sql.append(" I.IDINSTITUCION,    I.IDPERSONA,    I.IDTURNO, ");
		sql.append(" I.IDGUARDIA,  I.FECHASUSCRIPCION,  I.OBSERVACIONESSUSCRIPCION, ");
		sql.append(" I.FECHAVALIDACION,  I.OBSERVACIONESVALIDACION, ");
		sql.append(" I.FECHASOLICITUDBAJA,     I.OBSERVACIONESBAJA,    I.FECHABAJA,I.OBSERVACIONESVALBAJA ");
		sql.append(" ,I.FECHADENEGACION,     I.OBSERVACIONESDENEGACION ");
		sql.append(",TO_CHAR(NVL(I.FECHADENEGACION,I.FECHAVALIDACION),'dd/mm/yyyy') FECHAVALORALTA, ");
		sql.append("TO_CHAR(NVL(I.FECHADENEGACION, I.FECHABAJA), 'dd/mm/yyyy') FECHAVALORBAJA ");
		sql
				.append(" FROM SCS_INSCRIPCIONGUARDIA I,CEN_COLEGIADO COL,CEN_PERSONA PER, SCS_GUARDIASTURNO GT,SCS_TURNO T ");
		sql.append(" WHERE I.IDINSTITUCION = GT.IDINSTITUCION ");
		sql.append(" AND I.IDTURNO = GT.IDTURNO  AND I.IDGUARDIA = GT.IDGUARDIA ");
		sql.append(" AND I.IDINSTITUCION = COL.IDINSTITUCION  AND I.IDPERSONA = COL.IDPERSONA ");
		sql.append(" AND COL.IDPERSONA = PER.IDPERSONA ");
		sql.append(" AND GT.IDINSTITUCION = T.IDINSTITUCION  AND GT.IDTURNO= T.IDTURNO ");
		// Sacamos las guardias en potencia de ser validadas(excepto las obligatorias)
		// sql.append(" AND T.GUARDIAS!=");
		// sql.append(ScsTurnoBean.TURNO_GUARDIAS_OBLIGATORIAS);

		sql.append(" AND I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdInstitucion());
		if (inscripcionguardiaForm.getIdTurno() != null && !inscripcionguardiaForm.getIdTurno().equalsIgnoreCase("")
				&& !inscripcionguardiaForm.getIdTurno().equalsIgnoreCase("-1")) {
			sql.append(" AND I.IDTURNO = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdTurno());
		}
		if (inscripcionguardiaForm.getIdPersona() != null
				&& !inscripcionguardiaForm.getIdPersona().equalsIgnoreCase("")) {
			sql.append(" AND I.IDPERSONA = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdPersona());
		}
		if (inscripcionguardiaForm.getIdGuardia() != null
				&& !inscripcionguardiaForm.getIdGuardia().equalsIgnoreCase("")
				&& !inscripcionguardiaForm.getIdGuardia().equalsIgnoreCase("-1")) {
			sql.append(" AND I.IDGUARDIA = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), inscripcionguardiaForm.getIdGuardia());
		}

		String campoFecha = null;
		StringBuffer orderBy = new StringBuffer();
		if (!isEdicion) {
			if (inscripcionguardiaForm.getTipo().equals("A")) {
				campoFecha = "I.FECHASUSCRIPCION";
				orderBy.append(campoFecha);
				if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("S")) {
					// ORDENAMOS POR ALGO???
				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("P")) {
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NULL ");
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHASOLICITUDBAJA IS NULL ");
					// TIENEN QUE TENER LA INSCRIPCION AL TURNO VALIDADAS
					sql.append(" AND (SELECT count(*) ");
					sql.append(" FROM SCS_INSCRIPCIONTURNO IT ");
					sql.append(" WHERE  ");
					sql.append(" IT.IDINSTITUCION = I.IDINSTITUCION ");
					sql.append(" AND IT.IDTURNO = I.IDTURNO ");
					sql.append(" AND IT.IDPERSONA = I.IDPERSONA ");
					sql.append(" AND IT.FECHAVALIDACION IS NULL ");
					sql.append(" AND IT.FECHABAJA IS NULL ");
					sql.append(" AND IT.FECHASOLICITUDBAJA IS NULL)=0 ");

				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("C")) {
					orderBy.append(",I.FECHAVALIDACION");
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NOT NULL ");
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHASOLICITUDBAJA IS NULL ");

				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("D")) {
					orderBy.append(",I.FECHAVALIDACION");
					sql.append(" AND I.FECHADENEGACION IS NOT NULL ");
					sql.append(" AND I.FECHAVALIDACION IS NULL ");
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHASOLICITUDBAJA IS NULL ");

				}

			} else if (inscripcionguardiaForm.getTipo().equals("B")) {
				campoFecha = "I.FECHASOLICITUDBAJA";
				orderBy.append(campoFecha);
				if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("P")) {
					sql.append(" AND I.FECHADENEGACION IS NULL ");
					sql.append(" AND I.FECHASOLICITUDBAJA IS NOT NULL ");
					sql.append(" AND I.FECHABAJA IS NULL ");

				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("C")) {
					orderBy.append(",I.FECHABAJA");
					sql.append(" AND I.FECHASOLICITUDBAJA IS NOT NULL ");
					sql.append(" AND I.FECHABAJA IS NOT NULL ");
					sql.append(" AND I.FECHADENEGACION IS NULL ");

				} else if (inscripcionguardiaForm.getEstado() != null && inscripcionguardiaForm.getEstado().equals("D")) {
					orderBy.append(",I.FECHABAJA");
					sql.append(" AND I.FECHASOLICITUDBAJA IS NOT NULL ");
					sql.append(" AND I.FECHABAJA IS NULL ");
					sql.append(" AND I.FECHADENEGACION IS NOT NULL ");

				}

			}
		} else {
			if (inscripcionguardiaForm.getTipo().equals("A")) {
				orderBy.append(" I.FECHABAJA ");
			} else {
				orderBy.append(" I.FECHAVALIDACION ");

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
		sql.append(orderBy);
		if (!isEdicion)
			sql.append(" DESC ");

		List<ScsInscripcionGuardiaBean> alInscripcion = null;
		try {
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
					if (inscripcionBean.getFechaValidacion().equals("")) {
						if (inscripcionBean.getFechaSolicitudBaja().equals("")) {
							if (inscripcionBean.getFechaDenegacion().equals("")) {
								estado = "Alta Pendiente";
							} else {
								estado = "Alta Denegada";
							}

						} else {
							if (inscripcionBean.getFechaBaja().equals("")) {
								if (inscripcionBean.getFechaDenegacion().equals("")) {
									estado = "Baja Pendiente";
								} else {
									estado = "Baja Denegada";
								}

							} else {
								estado = "Baja Confirmada";

							}
						}
					} else {

						if (inscripcionBean.getFechaSolicitudBaja().equals("")) {
							estado = "Alta Confirmada";
						} else {
							if (inscripcionBean.getFechaBaja().equals("")) {
								if (inscripcionBean.getFechaDenegacion().equals("")) {
									estado = "Baja Pendiente";
								} else {
									estado = "Baja Denegada";
								}
							} else {
								estado = "Baja Confirmada";
							}
						}
					}
					inscripcionBean.setEstado(estado);

					turno = new ScsTurnoBean();
					inscripcionBean.setTurno(turno);
					guardia = new ScsGuardiasTurnoBean();
					inscripcionBean.setGuardia(guardia);
					persona = new CenPersonaBean();
					colegiado = new CenColegiadoBean();
					persona.setColegiado(colegiado);
					inscripcionBean.setPersona(persona);

					turno.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_IDINSTITUCION));
					turno.setIdTurno(UtilidadesHash.getInteger(htFila, ScsTurnoBean.C_IDTURNO));
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					turno.setAbreviatura(UtilidadesHash.getString(htFila, "ABREVIATURATURNO"));
					turno
							.setValidarInscripciones(UtilidadesHash.getString(htFila,
									ScsTurnoBean.C_VALIDARINSCRIPCIONES));
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
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return alInscripcion;

	}

	public List<ScsInscripcionGuardiaBean> getGuardiasInscripcion(Integer idInstitucion,
			Integer idTurno,
			Long idPersona,
			Integer idGuardia) throws ClsExceptions
	{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT GT.IDINSTITUCION,GT.IDTURNO, GT.IDGUARDIA, GT.NOMBRE, ");
		sql.append(" GT.NUMEROLETRADOSGUARDIA, GT.NUMEROSUSTITUTOSGUARDIA, ");
		sql.append(" GT.SELECCIONLABORABLES, GT.SELECCIONFESTIVOS, ");
		sql.append(" GT.TIPODIASGUARDIA ,  GT.DIASGUARDIA , ");
		sql.append(" GT.DIASPAGADOS ,  GT.DIASSEPARACIONGUARDIAS  ");
		sql.append(" ,IG.FECHASUSCRIPCION,IG.IDPERSONA  ");

		sql.append(" FROM SCS_GUARDIASTURNO GT, SCS_INSCRIPCIONGUARDIA IG ");
		sql.append(" WHERE  ");
		sql.append(" GT.IDINSTITUCION = IG.IDINSTITUCION ");
		sql.append(" AND GT.IDTURNO = IG.IDTURNO ");
		sql.append(" AND GT.IDGUARDIA = IG.IDGUARDIA ");
		sql.append(" AND IG.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idInstitucion);
		sql.append(" AND IG.IDTURNO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idTurno);
		sql.append(" AND IG.IDPERSONA = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idPersona);
		sql.append(" AND IG.FECHABAJA IS NULL ");
		sql.append(" AND IG.FECHADENEGACION IS NULL ");

		if (idGuardia != null) {
			sql.append(" AND IG.IDGUARDIA = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), idGuardia);

		}

		sql.append(" ORDER BY GT.NOMBRE ");

		List<ScsInscripcionGuardiaBean> alInscripcion = null;
		try {
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
					guardia.setNumeroLetradosGuardia(UtilidadesHash.getInteger(htFila,
							ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
					guardia.setNumeroSustitutosGuardia(UtilidadesHash.getInteger(htFila,
							ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA));
					guardia.setSeleccionLaborables(UtilidadesHash.getString(htFila,
							ScsGuardiasTurnoBean.C_SELECCIONLABORABLES));
					guardia.setSeleccionFestivos(UtilidadesHash.getString(htFila,
							ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS));
					guardia.setDiasGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASGUARDIA));
					guardia
							.setTipodiasGuardia(UtilidadesHash
									.getString(htFila, ScsGuardiasTurnoBean.C_TIPODIASGUARDIA));
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
					guardia.setDiasSeparacionGuardia(UtilidadesHash.getInteger(htFila,
							ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS));

					String seleccionTiposDia = ScsGuardiasTurnoAdm.obtenerTipoDia(guardia.getSeleccionLaborables(),
							guardia.getSeleccionFestivos(), this.usrbean);
					guardia.setSeleccionTiposDia(seleccionTiposDia);

					inscripcionBean.setGuardia(guardia);
					inscripcionBean.setFechaSuscripcion(UtilidadesHash.getString(htFila,
							ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION));
					inscripcionBean.setIdGuardia(guardia.getIdGuardia());
					inscripcionBean.setIdInstitucion(guardia.getIdInstitucion());
					inscripcionBean.setIdPersona(idPersona);
					inscripcionBean.setIdTurno(idTurno);

					alInscripcion.add(inscripcionBean);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return alInscripcion;

	}
	
	public List<ScsInscripcionGuardiaBean> getGuardiasParaInscripcion(Integer idInstitucion,
			Integer idTurno,
			Long idPersona,
			Integer idGuardia) throws ClsExceptions
	{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT GT.IDINSTITUCION,GT.IDGUARDIA , GT.NOMBRE, ");
		sql.append(" GT.NUMEROLETRADOSGUARDIA, GT.NUMEROSUSTITUTOSGUARDIA, ");
		sql.append(" GT.SELECCIONLABORABLES, GT.SELECCIONFESTIVOS, ");
		sql.append(" GT.TIPODIASGUARDIA ,  GT.DIASGUARDIA , ");
		sql.append(" GT.DIASPAGADOS ,  GT.DIASSEPARACIONGUARDIAS  ");
		sql.append(" FROM SCS_GUARDIASTURNO GT ");
		sql.append(" WHERE  ");
		sql.append(" GT.IDINSTITUCION = :");
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
		try {
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
					guardia.setNumeroLetradosGuardia(UtilidadesHash.getInteger(htFila,
							ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
					guardia.setNumeroSustitutosGuardia(UtilidadesHash.getInteger(htFila,
							ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA));
					guardia.setSeleccionLaborables(UtilidadesHash.getString(htFila,
							ScsGuardiasTurnoBean.C_SELECCIONLABORABLES));
					guardia.setSeleccionFestivos(UtilidadesHash.getString(htFila,
							ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS));
					guardia.setDiasGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_DIASGUARDIA));
					guardia
							.setTipodiasGuardia(UtilidadesHash
									.getString(htFila, ScsGuardiasTurnoBean.C_TIPODIASGUARDIA));
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
					guardia.setDiasSeparacionGuardia(UtilidadesHash.getInteger(htFila,
							ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS));

					String seleccionTiposDia = ScsGuardiasTurnoAdm.obtenerTipoDia(guardia.getSeleccionLaborables(),
							guardia.getSeleccionFestivos(), this.usrbean);
					guardia.setSeleccionTiposDia(seleccionTiposDia);

					inscripcionBean.setGuardia(guardia);
					inscripcionBean.setIdGuardia(guardia.getIdGuardia());
					inscripcionBean.setIdInstitucion(guardia.getIdInstitucion());
					inscripcionBean.setIdPersona(idPersona);
					inscripcionBean.setIdTurno(idTurno);

					alInscripcion.add(inscripcionBean);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return alInscripcion;

	}
	
	/**
	 * Obtiene las inscripciones ordenadas para formar la cola de una guardia dada una fecha
	 */
	public Vector<ScsInscripcionGuardiaBean> getColaGuardia(
			String idinstitucion,
			String idturno,
			String idguardia,
			String fechaInicio,
			String fechaFin,
			boolean porGrupos,
			String order) throws ClsExceptions
	{
		if (idinstitucion == null || idinstitucion.equals(""))		return null;
		if (idturno == null || idturno.equals(""))					return null;
		if (idguardia == null || idguardia.equals(""))				return null;
		if (fechaInicio == null || fechaInicio.equals(""))
			fechaInicio = "null";
		else if(!fechaInicio.trim().equalsIgnoreCase("sysdate"))
			fechaInicio = "'"+fechaInicio.trim()+"'";
		if (fechaFin == null || fechaFin.equals(""))
			fechaFin = "null";
		else if(!fechaFin.trim().equalsIgnoreCase("sysdate"))
			fechaFin = "'"+fechaFin.trim()+"'";
		
		String consulta = 
			"Select " +
		    "       (case when Ins.Fechavalidacion Is Not Null " +
			"              And Trunc(Ins.Fechavalidacion) <= nvl("+fechaInicio+",  Ins.Fechavalidacion) " +
			"              And (Ins.Fechabaja Is Null Or " +
			"                   Trunc(Ins.Fechabaja) > nvl("+fechaFin+", '01/01/1900')) " +
			"             then '1' " +
			"             else '0' " +
			"        end) Activo, " +
			getBaseConsultaInscripciones() +
			
			"   And Ins.Fechavalidacion Is Not Null " +
			"   And Gua.Idinstitucion = "+idinstitucion+" " +
			"   And Gua.Idturno = "+idturno+" " +
			"   And Gua.Idguardia = "+idguardia+" "/* +
		
			//cuando no se pasa fecha, se sacan todas las validadas (en cualquier fecha)
			"   And Trunc(Ins.Fechavalidacion) <= nvl("+fechaInicio+",  Ins.Fechavalidacion) " +
			//cuando no se pasa fecha, se sacan aunque esten de baja
			"   And (Ins.Fechabaja Is Null Or " +
			"        Trunc(Ins.Fechabaja) > nvl("+fechaFin+", '01/01/1900')) "*/;
		
		if (porGrupos)
			consulta +=
				"   And Gua.Idinstitucion = Gru.Idinstitucion " +
				"   And Gua.Idturno = Gru.Idturno " +
				"   And Gua.Idguardia = Gru.Idguardia ";
		
		if (! (order == null || order.equals("")))
			consulta += " order by " + order;
		else
			consulta += " order by Ins.FechaValidacion ";
		
		Vector<ScsInscripcionGuardiaBean> datos = null;
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta)) {
				datos = new Vector<ScsInscripcionGuardiaBean>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();

					ScsInscripcionGuardiaBean inscripcionBean = new ScsInscripcionGuardiaBean();
					inscripcionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_IDINSTITUCION));
					inscripcionBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_IDTURNO));
					inscripcionBean.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_IDGUARDIA));
					inscripcionBean.setIdPersona(UtilidadesHash.getLong(htFila, ScsInscripcionGuardiaBean.C_IDPERSONA));
					inscripcionBean.setFechaSuscripcion(UtilidadesHash.getString(htFila, ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION));
					inscripcionBean.setFechaValidacion(UtilidadesHash.getString(htFila, ScsInscripcionGuardiaBean.C_FECHAVALIDACION));
					inscripcionBean.setFechaBaja(UtilidadesHash.getString(htFila, ScsInscripcionGuardiaBean.C_FECHABAJA));
					inscripcionBean.setIdGrupoGuardiaColegiado(UtilidadesHash.getLong(htFila, ScsInscripcionGuardiaBean.C_IDGRUPOGUARDIACOLEGIADO));
					inscripcionBean.setGrupo(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_GRUPO));
					inscripcionBean.setOrdenGrupo(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_ORDENGRUPO));
					inscripcionBean.setNumeroGrupo(UtilidadesHash.getString(htFila, ScsGrupoGuardiaBean.C_NUMEROGRUPO));
					inscripcionBean.setEstado(UtilidadesHash.getString(htFila, "ACTIVO"));
					CenPersonaBean personaBean = new CenPersonaBean(inscripcionBean.getIdPersona(), (String) htFila
							.get(CenPersonaBean.C_NOMBRE), (String) htFila.get(CenPersonaBean.C_APELLIDOS1),
							(String) htFila.get(CenPersonaBean.C_APELLIDOS2), (String) htFila
									.get(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO));
					inscripcionBean.setPersona(personaBean);
					datos.add(inscripcionBean);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
	} //getColaGuardia()
	
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
	public Vector<ScsInscripcionGuardiaBean> getLetradosGrupo(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idGrupoGuardia) throws ClsExceptions
	{
		if (idGrupoGuardia == null || idGrupoGuardia.equals(""))
			return null;
		
		String consulta =
			"Select " +
			getBaseConsultaInscripciones() +
			"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION+" = "+idInstitucion+" " +
			"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDTURNO+" = "+idTurno+" " +
			"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA+" = "+idGuardia+" " +
			"   And Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGRUPO+" = "+idGrupoGuardia+" ";
		
		Vector<ScsInscripcionGuardiaBean> datos = null;
		Hashtable hashGrupo;
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta)) {
				datos = new Vector<ScsInscripcionGuardiaBean>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					
					ScsInscripcionGuardiaBean inscripcionBean = new ScsInscripcionGuardiaBean();
					inscripcionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_IDINSTITUCION));
					inscripcionBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_IDTURNO));
					inscripcionBean.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_IDGUARDIA));
					inscripcionBean.setIdPersona(UtilidadesHash.getLong(htFila, ScsInscripcionGuardiaBean.C_IDPERSONA));
					inscripcionBean.setFechaValidacion(UtilidadesHash.getString(htFila, ScsInscripcionGuardiaBean.C_FECHAVALIDACION));
					inscripcionBean.setFechaBaja(UtilidadesHash.getString(htFila, ScsInscripcionGuardiaBean.C_FECHABAJA));
					inscripcionBean.setIdGrupoGuardiaColegiado(UtilidadesHash.getLong(htFila, ScsInscripcionGuardiaBean.C_IDGRUPOGUARDIACOLEGIADO));
					inscripcionBean.setGrupo(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_GRUPO));
					inscripcionBean.setOrdenGrupo(UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_ORDENGRUPO));
					CenPersonaBean personaBean = new CenPersonaBean(inscripcionBean.getIdPersona(), (String) htFila
							.get(CenPersonaBean.C_NOMBRE), (String) htFila.get(CenPersonaBean.C_APELLIDOS1),
							(String) htFila.get(CenPersonaBean.C_APELLIDOS2), (String) htFila
									.get(CenColegiadoBean.C_NCOLEGIADO));
					inscripcionBean.setPersona(personaBean);
					datos.add(inscripcionBean);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
	} //getLetradosGrupo()
	
	/**
	 * Obtiene los grupos de una guardia dada, junto con el numero de componentes de cada uno
	 * 
	 * @param idinstitucion
	 * @param idturno
	 * @param idguardia
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable> getNumLetradosGrupos(
			String idinstitucion,
			String idturno,
			String idguardia) throws ClsExceptions
	{
		if (idinstitucion == null || idinstitucion.equals(""))
			return null;
		if (idturno == null || idturno.equals(""))
			return null;
		if (idguardia == null || idguardia.equals(""))
			return null;
		
		String consulta =
			" Select "+ScsInscripcionGuardiaBean.C_GRUPO+", Count(*) As Numero from " +
			" ( Select " +
			getBaseConsultaInscripciones() +
			
			"      And Gua.Idinstitucion = "+idinstitucion+" " +
			"      And Gua.Idturno = "+idturno+" " +
			"      And Gua.Idguardia = "+idguardia+" " +
			
			" ) " +
			"  Where "+ScsInscripcionGuardiaBean.C_GRUPO+" Is Not Null " +
			"  Group By "+ScsInscripcionGuardiaBean.C_GRUPO+" ";
		
		Vector<Hashtable> datos = null;
		Hashtable hashGrupo;
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.find(consulta)) {
				datos = new Vector<Hashtable>();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					
					hashGrupo = new Hashtable<Integer, Integer>();
					hashGrupo.put(ScsInscripcionGuardiaBean.C_GRUPO, UtilidadesHash.getInteger(htFila, ScsInscripcionGuardiaBean.C_GRUPO));
					hashGrupo.put("NUMERO", UtilidadesHash.getInteger(htFila, "NUMERO"));
					datos.add(hashGrupo);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
	} //getNumLetradosGrupos()
	
	public String getBaseConsultaInscripciones() {
		return new String(
			"       Ins.Idinstitucion,"+
			"       Ins.Idturno, " +
			"       Ins.Idguardia, " +
			"       Per.Idpersona, " +
			"       Ins.fechasuscripcion As Fechasuscripcion, "+
			"       TO_CHAR(TRUNC(Ins.fechavalidacion),'DD/MM/YYYY') As Fechavalidacion, "+
		    "       TO_CHAR(trunc(Ins.fechabaja),'DD/MM/YYYY') As Fechabaja, "+
		    
			"       Per.Nombre, " +
			"       Per.Apellidos1, " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) apellidos2, " +
			"       Per.Apellidos1 || " +
			"       Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) "+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+", " +
			"       Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) "+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+", " +
			"       Per.Fechanacimiento "+ScsOrdenacionColasBean.C_FECHANACIMIENTO+", " +
			"       Ins.Fechavalidacion AS "+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+", " +
			"       Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO+" As Idgrupoguardiacolegiado, " +
			"       Gru."+ScsGrupoGuardiaColegiadoBean.C_IDGRUPO+" As Grupo, " +
			"       Grg."+ScsGrupoGuardiaBean.C_NUMEROGRUPO+" As numeroGrupo, " +
			"       Gru."+ScsGrupoGuardiaColegiadoBean.C_ORDEN+" As Ordengrupo " +
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
	
}