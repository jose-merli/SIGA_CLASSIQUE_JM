package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.gratuita.beans.ScsConjuntoGuardiasBean;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
import com.siga.gratuita.form.AsistenciaForm;
import com.siga.gratuita.form.ProgrCalendariosForm;
import com.siga.gratuita.vos.VolantesExpressVo;

// Clase: ScsActuacionAsistenciaAdm 
// Autor: carlos.vidal@atosorigin.com
// Ultima modificación: 20/12/2004
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...  
 */
public class ScsActuacionAsistenciaAdm extends MasterBeanAdministrador {
		
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsActuacionAsistenciaAdm(UsrBean usuario) {
		super(ScsActuacionAsistenciaBean.T_NOMBRETABLA, usuario);		
	}
	
    /**
	 * Efectúa un SELECT en la tabla SCS_ACTUACIONASISTENCIA con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector();		
				
		try { 			 		
			String where = " WHERE " + ScsActuacionAsistenciaBean.C_IDINSTITUCION+ " = " + hash.get(ScsActuacionAsistenciaBean.C_IDINSTITUCION)+ "AND "+
			ScsActuacionAsistenciaBean.C_ANIO + " = " + hash.get(ScsActuacionAsistenciaBean.C_ANIO)+ " AND "+
			ScsActuacionAsistenciaBean.C_NUMERO + " = " + hash.get(ScsActuacionAsistenciaBean.C_NUMERO+ "AND "+
			ScsActuacionAsistenciaBean.C_IDACTUACION + " = " + hash.get(ScsActuacionAsistenciaBean.C_IDACTUACION));
			datos = this.select(where);
		} 
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}
	
	
	/**
	 * Efectúa un DELETE en la tabla SCS_ACTUACIONASISTENCIA del registro seleccionado 
	 * 
	 * @param hash Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return boleano que indica si la inserción fue correcta o no. 
	 */
	public boolean delete(Hashtable hash) throws ClsExceptions{
	
		try {
			Row row = new Row();	
			row.load(hash);
	
			String [] campos = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, campos) == 1) {
				return true;
			}		
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BORRADO");
		}
		return false;
	}	
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	public String[] getCamposBean() {
		String[] campos =  {ScsActuacionAsistenciaBean.C_IDINSTITUCION, 
							ScsActuacionAsistenciaBean.C_ANIO,
							ScsActuacionAsistenciaBean.C_NUMERO,
							ScsActuacionAsistenciaBean.C_IDACTUACION,
							ScsActuacionAsistenciaBean.C_FECHA,
							ScsActuacionAsistenciaBean.C_DIADESPUES,
							ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL,
							ScsActuacionAsistenciaBean.C_FECHAMODIFICACION,
							ScsActuacionAsistenciaBean.C_USUMODIFICACION,
							ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION,
							ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE,
							ScsActuacionAsistenciaBean.C_LUGAR,
							ScsActuacionAsistenciaBean.C_NUMEROASUNTO,
							ScsActuacionAsistenciaBean.C_ANULACION,
							ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION ,
							ScsActuacionAsistenciaBean.C_OBSERVACIONES,
							ScsActuacionAsistenciaBean.C_FACTURADO,
							ScsActuacionAsistenciaBean.C_PAGADO,
							ScsActuacionAsistenciaBean.C_IDFACTURACION,
							ScsActuacionAsistenciaBean.C_VALIDADA,
							ScsActuacionAsistenciaBean.C_IDJUZGADO,							
							ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO,
							ScsActuacionAsistenciaBean.C_IDCOMISARIA,
							ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA,							
							ScsActuacionAsistenciaBean.C_IDPRISION,
							ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION,
							ScsActuacionAsistenciaBean.C_IDTIPOACTUACION,
							ScsActuacionAsistenciaBean.C_NIG,
							ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA,
							ScsActuacionAsistenciaBean.C_FECHACREACION,
							ScsActuacionAsistenciaBean.C_USUCREACION,
							ScsActuacionAsistenciaBean.C_IDMOVIMIENTO
							};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	public String[] getClavesBean() {
		String[] campos= {	ScsActuacionAsistenciaBean.C_IDINSTITUCION, 
				ScsActuacionAsistenciaBean.C_ANIO, 
				ScsActuacionAsistenciaBean.C_NUMERO,
				ScsActuacionAsistenciaBean.C_IDACTUACION};
		return campos;
	}
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsActuacionAsistenciaBean bean = null;
		
		try {
			bean = new ScsActuacionAsistenciaBean();		
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_ANIO));				
			bean.setNumero(UtilidadesHash.getLong(hash,ScsActuacionAsistenciaBean.C_NUMERO));				
			bean.setIdActuacion(UtilidadesHash.getLong(hash,ScsActuacionAsistenciaBean.C_IDACTUACION));				
			bean.setFecha(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_FECHA));				
			bean.setDiaDespues(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_DIADESPUES));				
			bean.setAcuerdoExtrajudicial(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL));				
			bean.setFechaJustificacion(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION));				
			bean.setDescripcionBreve(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE));				
			bean.setLugar(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_LUGAR));				
			bean.setNumeroAsunto(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_NUMEROASUNTO));				
			bean.setAnulacion(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_ANULACION));				
			bean.setObservacionesJustificacion(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION));				
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_OBSERVACIONES));				
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDFACTURACION));				
			bean.setValidada(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_VALIDADA));				
			bean.setIdJuzgado(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDJUZGADO));
			bean.setIdInstitucionJuzgado(UtilidadesHash.getLong(hash,ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO));
			bean.setIdComisaria(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDCOMISARIA));
			bean.setIdInstitucionComisaria(UtilidadesHash.getLong(hash,ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA));
			bean.setFechaMod(UtilidadesHash.getString (hash,ScsActuacionAsistenciaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_USUMODIFICACION));
			bean.setFechaCreacion(UtilidadesHash.getString (hash,ScsActuacionAsistenciaBean.C_FECHACREACION));
			bean.setUsuCreacion(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_USUCREACION));
			bean.setIdPrision(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDPRISION));
			bean.setIdInstitucionPrision(UtilidadesHash.getLong(hash,ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION));
			bean.setIdTipoActuacion(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDTIPOACTUACION));
			bean.setIdTipoAsistencia(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA));
			bean.setPagado(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_PAGADO));
			bean.setFacturado(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_FACTURADO));
			bean.setNIG(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_NIG));
			bean.setIdMovimiento(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDMOVIMIENTO));
		} catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}		
		return bean;
	
	}
	
	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsActuacionAsistenciaBean b = (ScsActuacionAsistenciaBean) bean;
			
			
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDINSTITUCION             , b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_ANIO                      , b.getAnio());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_NUMERO                    , b.getNumero());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDACTUACION               , b.getIdActuacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_FECHA                     , b.getFecha());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_DIADESPUES                , b.getDiaDespues());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL      , b.getAcuerdoExtrajudicial());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION        , b.getFechaJustificacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE          , b.getDescripcionBreve());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_LUGAR                     , b.getLugar());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_NUMEROASUNTO              , b.getNumeroAsunto());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_ANULACION                 , b.getAnulacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_PAGADO                    , b.getPagado());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_FACTURADO                 , b.getFacturado());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION, b.getObservacionesJustificacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_OBSERVACIONES             , b.getObservaciones());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDFACTURACION             , b.getIdFacturacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_VALIDADA				  , b.getValidada());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDJUZGADO				  , b.getIdJuzgado());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO      , b.getIdInstitucionJuzgado());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDCOMISARIA               , b.getIdComisaria());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA	  , b.getIdInstitucionComisaria());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDTIPOACTUACION			  , b.getIdTipoActuacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA	  	  , b.getIdTipoAsistencia());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDPRISION			  	  , b.getIdPrision());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION	  , b.getIdInstitucionPrision());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_USUMODIFICACION	  		  , b.getUsuMod());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_FECHAMODIFICACION		  , b.getFechaMod());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_NIG						  , b.getNIG());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_USUCREACION				  , b.getUsuCreacion());
			UtilidadesHash.set(htData, ScsActuacionAsistenciaBean.C_FECHACREACION			  , b.getFechaCreacion());
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_ACTUACIONASISTENCIA con los datos introducidos. 
	 * 
	 * @param where String la clausula where del select 
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector selectTabla(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(ScsActuacionAsistenciaBean.T_NOMBRETABLA, this.getCamposSelect());
			sql += where;
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
	 * Efectúa un SELECT en la tabla SCS_ACTUACIONASISTENCIA con los datos introducidos. 
	 * 
	 * @param sql String  
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector select(String sql){
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
	 * Devuelve los campos de en la consulta
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con los campos del select
	 */
	
	protected String[] getCamposSelect(){
			String[] campos = {	"IDINSTITUCION",
					"ANIO",
					"NUMERO",
					"IDACTUACION",
					"FECHA",
					"DIADESPUES",
					"ACUERDOEXTRAJUDICIAL",
					"FECHAMODIFICACION",
					"USUMODIFICACION",
					"FECHAJUSTIFICACION",
					"DESCRIPCIONBREVE",
					"LUGAR",
					"NUMEROASUNTO",
					"ANULACION",
					"OBSERVACIONESJUSTIFICACION ",
					"IDFACTURACION ",
					"OBSERVACIONES", "FACTURADO"};
					//"FACTURADO"};
			return campos;
	}

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	public Hashtable obtenerIdsLugar(Integer institucion, Integer anio, Integer numero) throws ClsExceptions {
		Hashtable datos=null;
		
		try {
			String sql=
				"select rownum ,idinstitucion_comis, idcomisaria, idinstitucion_juzg, idjuzgado " +
				" from  scs_actuacionasistencia "+
				"where idinstitucion = "+ institucion+
				"  and anio = "+ anio+
				"  and numero = "+ numero+
				"  and rownum <2 "+
				"order by idactuacion";
			
			//Consulta:
			RowsContainer rc = this.find(sql);		
			if(rc!=null && rc.size()==1){
				datos= ((Row)rc.get(0)).getRow();
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	public void updateActuacionVolanteExpress(ScsActuacionAsistenciaBean actuacion) throws ClsExceptions 
	{
		String claves [] ={ScsActuacionAsistenciaBean.C_ANIO,ScsActuacionAsistenciaBean.C_NUMERO, ScsActuacionAsistenciaBean.C_IDINSTITUCION, ScsActuacionAsistenciaBean.C_IDACTUACION};
		String campos [] = { ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION, ScsActuacionAsistenciaBean.C_NUMEROASUNTO, ScsActuacionAsistenciaBean.C_IDJUZGADO, ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO, ScsActuacionAsistenciaBean.C_IDCOMISARIA, ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA, ScsActuacionAsistenciaBean.C_IDTIPOACTUACION};
		this.updateDirect(actuacion,claves,campos);
	}

	public  List<ScsActuacionAsistenciaBean> getActuacionesAsistenciaVolantesExpres(ScsAsistenciasBean asistencia,String lugar) 
	throws ClsExceptions{
		
	Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
	int contador = 0;

		StringBuffer sql =  new StringBuffer();
		sql.append("SELECT  "); 
		sql.append( ScsActuacionAsistenciaBean.C_IDACTUACION);
		sql.append(", "); 
		sql.append( ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION);
		sql.append(", "); 
		sql.append( ScsActuacionAsistenciaBean.C_FACTURADO);
		sql.append(" FROM ");
		sql.append(ScsActuacionAsistenciaBean.T_NOMBRETABLA); 
		sql.append( "  ");
		sql.append(" WHERE ");
		 
		sql.append( ScsActuacionAsistenciaBean.C_IDINSTITUCION); 
		sql.append( " = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getIdInstitucion());
		sql.append(" AND " );
		sql.append( ScsActuacionAsistenciaBean.C_ANIO); 
		sql.append( " = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getAnio());
		sql.append(" AND "); 
		sql.append( ScsActuacionAsistenciaBean.C_NUMERO); 
		sql.append( " = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getNumero());
		if(lugar.equals("centro")){
			
				sql.append(" AND "); 
				sql.append( ScsActuacionAsistenciaBean.C_IDCOMISARIA);
				sql.append(" IS NOT NULL ");
		}else{
				sql.append(" AND "); 
				sql.append( ScsActuacionAsistenciaBean.C_IDJUZGADO);
				sql.append(" IS NOT NULL ");
		
		}
		
				
		List<ScsActuacionAsistenciaBean> alActuacionesAsistencias = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alActuacionesAsistencias = new ArrayList<ScsActuacionAsistenciaBean>();
            	ScsActuacionAsistenciaBean actuacionAsistenciaBean = null;
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		actuacionAsistenciaBean = new ScsActuacionAsistenciaBean();
            		actuacionAsistenciaBean.setIdActuacion(new Long((String)htFila.get(ScsActuacionAsistenciaBean.C_IDACTUACION)));
            		actuacionAsistenciaBean.setFechaJustificacion((String)htFila.get(ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION));
            		actuacionAsistenciaBean.setFacturado((String)htFila.get(ScsActuacionAsistenciaBean.C_FACTURADO));
					alActuacionesAsistencias.add(actuacionAsistenciaBean);
            	}
            }else{
            	alActuacionesAsistencias = new ArrayList<ScsActuacionAsistenciaBean>();
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
	
	return alActuacionesAsistencias;
}
	public  List<ActuacionAsistenciaForm> getActuacionesAsistencia(ScsAsistenciasBean asistencia, UsrBean usrBean) throws ClsExceptions{
		StringBuffer sql =  new StringBuffer();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		sql.append("SELECT  AA.IDINSTITUCION, AA.ANIO, AA.NUMERO, AA.IDACTUACION, ");
		sql.append("AA.FECHA, AA.NUMEROASUNTO,  ");
		sql.append("AA.FECHAJUSTIFICACION,       AA.VALIDADA, AA.ANULACION, ");
		sql.append("AA.IDFACTURACION, AA.FACTURADO, ");
		
		
		sql.append("F_SIGA_GETRECURSO(TA.DESCRIPCION, "+usrBean.getLanguage()+") DESCRIPCIONACTUACION, ");
		
		sql.append("DECODE(AA.IDFACTURACION,null, null, ");
		sql.append("FJG.NOMBRE || ' (' || TO_CHAR(FJG.FECHADESDE, 'DD/MM/YYYY') || '-' || ");
		sql.append("TO_CHAR(FJG.FECHAHASTA, 'DD/MM/YYYY') || ')') NOMBREFACTURACION,  ");
		
		sql.append("TO_CHAR(A.FECHAHORA, 'DD/MM/YYYY') FECHAHORAASISTENCIA, ");
		sql.append("TU.LETRADOACTUACIONES ");
		sql.append(",A.FECHAANULACION FECHAANULACIONASISTENCIA ");
		sql.append(" ,A.NUMERODILIGENCIA , A.NUMEROPROCEDIMIENTO ");
		sql.append(" ,A.COMISARIA , A.JUZGADO ");
		
//		sql.append(",A.ANIO||'/'||A.NUMERO||DECODE(A.IDPERSONAJG,null,null,' - '||PJG.NOMBRE ||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2) DESCRIPCIONASISTENCIA "); 
		sql.append("FROM SCS_ACTUACIONASISTENCIA AA,SCS_ASISTENCIA A, SCS_TURNO TU ");
		sql.append(",SCS_TIPOACTUACION TA, FCS_FACTURACIONJG FJG  ");
		sql.append("WHERE "); 
		sql.append("FJG.IDFACTURACION(+) =      AA.IDFACTURACION ");
		sql.append("AND FJG.IDINSTITUCION(+) = AA.IDINSTITUCION ");
		sql.append("AND TA.IDINSTITUCION(+) = AA.IDINSTITUCION ");
		sql.append("AND TA.IDTIPOASISTENCIA(+) = AA.IDTIPOASISTENCIA ");
		sql.append("AND TA.IDTIPOACTUACION(+) = AA.IDTIPOACTUACION ");
		sql.append("AND TU.IDTURNO(+) = A.IDTURNO ");
		sql.append("AND TU.IDINSTITUCION(+) = A.IDINSTITUCION ");
		sql.append("AND AA.IDINSTITUCION = A.IDINSTITUCION ");
		sql.append("AND AA.ANIO = A.ANIO ");
		sql.append("AND AA.NUMERO =  A.NUMERO ");
		sql.append("AND  AA.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getIdInstitucion());
		sql.append(" AND AA.ANIO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getAnio());
		sql.append(" AND AA.NUMERO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getNumero());
		sql.append(" ORDER BY AA.IDACTUACION ");

		List<ActuacionAsistenciaForm> alActuacionesAsistencias = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alActuacionesAsistencias = new ArrayList<ActuacionAsistenciaForm>();
            	ScsActuacionAsistenciaBean actuacionAsistenciaBean = null;
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		actuacionAsistenciaBean =  (ScsActuacionAsistenciaBean)this.hashTableToBean(htFila);
            		ActuacionAsistenciaForm actuacionAsistenciaForm = actuacionAsistenciaBean.getActuacionAsistenciaForm();
            		actuacionAsistenciaForm.setDescripcionActuacion(UtilidadesHash.getString(htFila, "DESCRIPCIONACTUACION"));
            		actuacionAsistenciaForm.setNombreFacturacion(UtilidadesHash.getString(htFila, "NOMBREFACTURACION"));
            		actuacionAsistenciaForm.setFechaHoraAsistencia(UtilidadesHash.getString(htFila, "FECHAHORAASISTENCIA"));
            		actuacionAsistenciaForm.setLetradoActuaciones(UtilidadesHash.getString(htFila, "LETRADOACTUACIONES"));
            		actuacionAsistenciaForm.setFechaAnulacionAsistencia(UtilidadesHash.getString(htFila, "FECHAANULACIONASISTENCIA"));
            		actuacionAsistenciaForm.setFichaColegial(asistencia.getFichaColegial());
            		actuacionAsistenciaForm.setNumeroDiligenciaAsistencia(UtilidadesHash.getString(htFila, "NUMERODILIGENCIA"));
            		actuacionAsistenciaForm.setNumeroProcedimientoAsistencia(UtilidadesHash.getString(htFila, "NUMEROPROCEDIMIENTO"));
            		actuacionAsistenciaForm.setComisariaAsistencia(UtilidadesHash.getString(htFila, "COMISARIA"));
            		actuacionAsistenciaForm.setJuzgadoAsistencia(UtilidadesHash.getString(htFila, "JUZGADO"));
            		actuacionAsistenciaForm.setLetrado(usrbean.isLetrado());
            		
            		alActuacionesAsistencias.add(actuacionAsistenciaForm);
            	}
            }else{
            	alActuacionesAsistencias = new ArrayList<ActuacionAsistenciaForm>();
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
	
	
		return alActuacionesAsistencias;
	
	}
	public ScsActuacionAsistenciaBean getActuacionAsistencia(
			ActuacionAsistenciaForm actuacionAsistenciaForm) throws ClsExceptions {
		Hashtable actuacionAsistenciaHashtable = new Hashtable();
		
		actuacionAsistenciaHashtable.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION,actuacionAsistenciaForm.getIdInstitucion());
		actuacionAsistenciaHashtable.put(ScsActuacionAsistenciaBean.C_ANIO,actuacionAsistenciaForm.getAnio());
		actuacionAsistenciaHashtable.put(ScsActuacionAsistenciaBean.C_NUMERO,actuacionAsistenciaForm.getNumero());
		actuacionAsistenciaHashtable.put(ScsActuacionAsistenciaBean.C_IDACTUACION,actuacionAsistenciaForm.getIdActuacion());
		
		Vector  actuacionAsistenciaVector = selectByPK(actuacionAsistenciaHashtable);
		
		
		return (ScsActuacionAsistenciaBean) actuacionAsistenciaVector.get(0);
	}
	public Integer getNuevaActuacionAsistencia(AsistenciaForm asistencia) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(IDACTUACION)+1 MAXVALOR FROM scs_actuacionasistencia WHERE "+
				" IDINSTITUCION = "+asistencia.getIdInstitucion()+
				" AND ANIO = "+asistencia.getAnio()+
				" AND NUMERO = "+asistencia.getNumero();
        int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("MAXVALOR")).equals("")) {
                Integer valorInt=Integer.valueOf((String)htRow.get("MAXVALOR"));
                valor=valorInt.intValue();
                
            }
            
        }
        return new Integer(valor);        
    }
	public Hashtable<String , Object> actualizaHashActuacionAsistenciaParaHistorico(Hashtable<String, Object> actuacionAsistenciaHashtable, UsrBean usr) throws ClsExceptions{
		
		Map<String,Hashtable<String, Object>> fksAsistenciaMap = new HashMap<String, Hashtable<String,Object>>(); 
		//Como el turno es obligarotio
		Hashtable<String, Object> fksAsistenciaHashtable = new Hashtable<String, Object>();
		if(actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION)!=null && !actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION).toString().equals("")){
			fksAsistenciaHashtable = new Hashtable<String, Object>();
			fksAsistenciaHashtable.put("TABLA_FK", ScsTipoActuacionBean.T_NOMBRETABLA);
			fksAsistenciaHashtable.put("SALIDA_FK", ScsTipoActuacionBean.C_DESCRIPCION);
			fksAsistenciaHashtable.put(ScsTipoActuacionBean.C_IDINSTITUCION, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDINSTITUCION));
			fksAsistenciaHashtable.put(ScsTipoActuacionBean.C_IDTIPOACTUACION, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION));
			fksAsistenciaHashtable.put(ScsTipoActuacionBean.C_IDTIPOASISTENCIA, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA));
			fksAsistenciaMap.put(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION,fksAsistenciaHashtable);
		}
		
		if(actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDJUZGADO)!=null && !actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDJUZGADO).toString().equals("")){
			
			fksAsistenciaHashtable = new Hashtable<String, Object>();
			fksAsistenciaHashtable.put("TABLA_FK", ScsJuzgadoBean.T_NOMBRETABLA);
			fksAsistenciaHashtable.put("SALIDA_FK", ScsJuzgadoBean.C_NOMBRE);
			fksAsistenciaHashtable.put(ScsJuzgadoBean.C_IDINSTITUCION, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDINSTITUCION));
			fksAsistenciaHashtable.put(ScsJuzgadoBean.C_IDJUZGADO,actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDJUZGADO));
			fksAsistenciaMap.put(ScsActuacionAsistenciaBean.C_IDJUZGADO,fksAsistenciaHashtable);
			
		}
		if(actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDCOMISARIA)!=null && !actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDCOMISARIA).toString().equals("")){
			fksAsistenciaHashtable = new Hashtable<String, Object>();
			fksAsistenciaHashtable.put("TABLA_FK", ScsComisariaBean.T_NOMBRETABLA);
			fksAsistenciaHashtable.put("SALIDA_FK", ScsComisariaBean.C_NOMBRE);
			fksAsistenciaHashtable.put(ScsComisariaBean.C_IDINSTITUCION, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA));
			fksAsistenciaHashtable.put(ScsComisariaBean.C_IDCOMISARIA,actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDCOMISARIA));
			fksAsistenciaMap.put(ScsActuacionAsistenciaBean.C_IDCOMISARIA,fksAsistenciaHashtable);
		}
		if(actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDPRISION)!=null && !actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDPRISION).toString().equals("")){
			fksAsistenciaHashtable = new Hashtable<String, Object>();
			fksAsistenciaHashtable.put("TABLA_FK", ScsPrisionBean.T_NOMBRETABLA);
			fksAsistenciaHashtable.put("SALIDA_FK", ScsPrisionBean.C_NOMBRE);
			fksAsistenciaHashtable.put(ScsActuacionAsistenciaBean.C_IDPRISION, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDPRISION));
			fksAsistenciaHashtable.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION, actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDINSTITUCION));
			fksAsistenciaMap.put(ScsActuacionAsistenciaBean.C_IDPRISION,fksAsistenciaHashtable);
		}
		//aQUI METEMOS EL COSTE FIJO QUE ES OBLIGATORIO
//		if(actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA)!=null && !actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA).toString().equals("")){
			fksAsistenciaHashtable = new Hashtable<String, Object>();
			fksAsistenciaHashtable.put("TABLA_FK","SCS_ACTUACIONASISTCOSTEFIJO TIPO,SCS_COSTEFIJO COSTE");
			fksAsistenciaHashtable.put("SALIDA_FK", "DESCRIPCION");
			fksAsistenciaHashtable.put("TIPO.IDTIPOACTUACION", actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION));
			fksAsistenciaHashtable.put("TIPO.IDINSTITUCION", actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDINSTITUCION));
			fksAsistenciaHashtable.put("TIPO.ANIO", actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_ANIO));
			fksAsistenciaHashtable.put("TIPO.NUMERO", actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_NUMERO));
			fksAsistenciaHashtable.put("TIPO.IDACTUACION", actuacionAsistenciaHashtable.get(ScsActuacionAsistenciaBean.C_IDACTUACION));
			fksAsistenciaHashtable.put("TIPO.IDINSTITUCION", "COSTE.IDINSTITUCION");
			fksAsistenciaHashtable.put("TIPO.IDCOSTEFIJO", "COSTE.IDCOSTEFIJO");
			fksAsistenciaMap.put("COSTEFIJO",fksAsistenciaHashtable);
			actuacionAsistenciaHashtable.put("COSTEFIJO", "");
//		}
		
		actuacionAsistenciaHashtable.put("fks", fksAsistenciaMap);
		return actuacionAsistenciaHashtable;
	}
	
	//Permite actualizar la tabla añadiendo el campo de movimientos varios
			public void actualizarActuacionesMovimientosVarios(Hashtable entrada) throws ClsExceptions{
				String consulta = "UPDATE "+ScsActuacionAsistenciaBean.T_NOMBRETABLA;
				consulta += " SET "+ScsActuacionAsistenciaBean.C_IDMOVIMIENTO+" = "+entrada.get(ScsActuacionAsistenciaBean.C_IDMOVIMIENTO);
				consulta += " WHERE "+ScsActuacionAsistenciaBean.C_IDINSTITUCION+" = "+ entrada.get(ScsActuacionAsistenciaBean.C_IDINSTITUCION);
				consulta += " and "+ScsActuacionAsistenciaBean.C_ANIO+" = "+entrada.get(ScsActuacionAsistenciaBean.C_ANIO);
				consulta += " and "+ScsActuacionAsistenciaBean.C_NUMERO+" =  "+ entrada.get(ScsActuacionAsistenciaBean.C_NUMERO);
				consulta += " and "+ScsActuacionAsistenciaBean.C_IDACTUACION+" =  "+ entrada.get(ScsActuacionAsistenciaBean.C_IDACTUACION);
				try{
					if (!this.updateSQL(consulta)){
						throw new ClsExceptions (this.getError());
					}
				} catch (Exception e) {
					throw new ClsExceptions (e, "Error al ejecutar el 'actualizaMovimientosVarios' en B.D.");
				}
			}

	

}