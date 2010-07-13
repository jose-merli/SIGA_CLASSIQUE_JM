package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;

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
							ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA
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
			bean.setIdPrision(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDPRISION));
			bean.setIdInstitucionPrision(UtilidadesHash.getLong(hash,ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION));
			bean.setIdTipoActuacion(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDTIPOACTUACION));
			bean.setIdTipoAsistencia(UtilidadesHash.getInteger(hash,ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA));
			bean.setPagado(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_PAGADO));
			bean.setFacturado(UtilidadesHash.getString(hash,ScsActuacionAsistenciaBean.C_FACTURADO));
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
			htData.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION             , String.valueOf(b.getIdInstitucion()));
			htData.put(ScsActuacionAsistenciaBean.C_ANIO                      , String.valueOf(b.getAnio()));
			htData.put(ScsActuacionAsistenciaBean.C_NUMERO                    , String.valueOf(b.getNumero()));
			htData.put(ScsActuacionAsistenciaBean.C_IDACTUACION               , String.valueOf(b.getIdActuacion()));
			htData.put(ScsActuacionAsistenciaBean.C_FECHA                     , String.valueOf(b.getFecha()));
			htData.put(ScsActuacionAsistenciaBean.C_DIADESPUES                , String.valueOf(b.getDiaDespues()));
			htData.put(ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL      , String.valueOf(b.getAcuerdoExtrajudicial()));
			htData.put(ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION        , String.valueOf(b.getFechaJustificacion()));
			htData.put(ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE          , String.valueOf(b.getDescripcionBreve()));
			htData.put(ScsActuacionAsistenciaBean.C_LUGAR                     , String.valueOf(b.getLugar()));
			htData.put(ScsActuacionAsistenciaBean.C_NUMEROASUNTO              , String.valueOf(b.getNumeroAsunto()));
			htData.put(ScsActuacionAsistenciaBean.C_ANULACION                 , String.valueOf(b.getAnulacion()));
			htData.put(ScsActuacionAsistenciaBean.C_PAGADO                    , String.valueOf(b.getPagado()));
			htData.put(ScsActuacionAsistenciaBean.C_FACTURADO                 , String.valueOf(b.getFacturado()));
			htData.put(ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION, String.valueOf(b.getObservacionesJustificacion()));
			htData.put(ScsActuacionAsistenciaBean.C_OBSERVACIONES             , String.valueOf(b.getObservaciones()));
			htData.put(ScsActuacionAsistenciaBean.C_IDFACTURACION             , String.valueOf(b.getIdFacturacion()));
			htData.put(ScsActuacionAsistenciaBean.C_VALIDADA				  , String.valueOf(b.getValidada()));
			htData.put(ScsActuacionAsistenciaBean.C_IDJUZGADO				  , String.valueOf(b.getIdJuzgado()));
			htData.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO      , String.valueOf(b.getIdInstitucionJuzgado()));
			htData.put(ScsActuacionAsistenciaBean.C_IDCOMISARIA               , String.valueOf(b.getIdComisaria()));
			htData.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA	  , String.valueOf(b.getIdInstitucionComisaria()));
			htData.put(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION			  , String.valueOf(b.getIdTipoActuacion()));
			htData.put(ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA	  	  , String.valueOf(b.getIdTipoAsistencia()));
			htData.put(ScsActuacionAsistenciaBean.C_IDPRISION			  	  , String.valueOf(b.getIdPrision()));
			htData.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION	  , String.valueOf(b.getIdInstitucionPrision()));
			htData.put(ScsActuacionAsistenciaBean.C_USUMODIFICACION	  		  , String.valueOf(b.getUsuMod()));
			htData.put(ScsActuacionAsistenciaBean.C_FECHAMODIFICACION		  , String.valueOf(b.getFechaMod()));
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

}