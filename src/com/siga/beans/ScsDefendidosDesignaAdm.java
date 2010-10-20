
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_DESIGNA
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsDefendidosDesignaAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDefendidosDesignaAdm (UsrBean usuario) {
		super( ScsDefendidosDesignaBean.T_NOMBRETABLA, usuario);
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsDefendidosDesignaBean.C_IDINSTITUCION,			ScsDefendidosDesignaBean.C_IDTURNO,
				ScsDefendidosDesignaBean.C_ANIO,					ScsDefendidosDesignaBean.C_NUMERO,
				ScsDefendidosDesignaBean.C_FECHAMODIFICACION,		ScsDefendidosDesignaBean.C_USUMODIFICACION,
				ScsDefendidosDesignaBean.C_IDPERSONA,				ScsDefendidosDesignaBean.C_OBSERVACIONES,
				ScsDefendidosDesignaBean.C_CALIDAD,                 ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE, 
				ScsDefendidosDesignaBean.C_IDTIPOENCALIDAD,         ScsDefendidosDesignaBean.C_CALIDADIDINSTITUCION };
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsDefendidosDesignaBean.C_IDINSTITUCION,			ScsDefendidosDesignaBean.C_IDTURNO,
				ScsDefendidosDesignaBean.C_ANIO,					ScsDefendidosDesignaBean.C_NUMERO,
				ScsDefendidosDesignaBean.C_IDPERSONA};
		return campos;
	}
	
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDefendidosDesignaBean bean = null;
		try{
			bean = new ScsDefendidosDesignaBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_ANIO));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDefendidosDesignaBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_IDPERSONA));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_IDTURNO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_NUMERO));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsDefendidosDesignaBean.C_OBSERVACIONES));
			bean.setCalidad(UtilidadesHash.getString(hash,ScsDefendidosDesignaBean.C_CALIDAD));
			bean.setNombreRepresentante(UtilidadesHash.getString(hash,ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_USUMODIFICACION));
			bean.setIdTipoenCalidad(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_IDTIPOENCALIDAD));
			bean.setCalidadIdinstitucion(UtilidadesHash.getInteger(hash,ScsDefendidosDesignaBean.C_CALIDADIDINSTITUCION));
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsDefendidosDesignaBean b = (ScsDefendidosDesignaBean) bean;
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_ANIO,b.getAnio());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE,b.getNombreRepresentante());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_IDINSTITUCION,b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_IDTURNO,b.getIdTurno());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_NUMERO,b.getNumero());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_IDPERSONA,b.getIdPersona());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_CALIDAD,b.getCalidad());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_IDTIPOENCALIDAD,b.getIdTipoenCalidad());
			UtilidadesHash.set(hash, ScsDefendidosDesignaBean.C_CALIDADIDINSTITUCION,b.getCalidadIdinstitucion());
			
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
	protected String[] getOrdenCampos() {
		return null;
	}
	
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
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
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del turno que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	/*public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	 {
	 String values;	
	 RowsContainer rc = null;
	 int contador = 0;
	 
	 try { rc = new RowsContainer(); }
	 catch(Exception e) { e.printStackTrace(); }
	 
	 String sql ="SELECT (MAX(NUMERO) + 1) AS NUMERO FROM " + nombreTabla +" ";	
	 try {		
	 if (rc.query(sql)) {
	 Row fila = (Row) rc.get(0);
	 Hashtable prueba = fila.getRow();			
	 if (prueba.get("NUMERO").equals("")) {
	 entrada.put(ScsDefendidosDesignaBean.C_NUMERO,"1");
	 }
	 else entrada.put(ScsDefendidosDesignaBean.C_NUMERO,(String)prueba.get("NUMERO"));								
	 }
	 //Ponemos el resto de campos para inssertar la nueva Designa
	  entrada.put(ScsDefendidosDesignaBean.C_ANIO,((((String)entrada.get("FECHAENTRADAINICIO")).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)));
	  entrada.put(ScsDefendidosDesignaBean.C_ESTADO,"V");
	  entrada.put(ScsDefendidosDesignaBean.C_FECHAENTRADA,GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAENTRADAINICIO")));
	  
	  }	
	  catch (ClsExceptions e) {		
	  throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
	  }
	  return entrada;
	  }*/
	
	
	/** 
	 * Recoge los identificadores y nombres de las personas defendidas 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno
	 * @param  anio - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getListaDefendidos(Integer institucion, Integer anio, Integer numero, Integer turno, String lenguaje) throws ClsExceptions,SIGAException {
		Vector datos=new Vector();
		try {
			String lg=lenguaje.toUpperCase();
			String demandante=UtilidadesString.getMensajeIdioma(lg,"gratuita.insertarSOJ.literal.demandante");
			String demandado=UtilidadesString.getMensajeIdioma(lg,"gratuita.insertarSOJ.literal.demandado");
			
			String sql="Select (Select Decode(Ejg.Idtipoencalidad, Null,'', f_Siga_Getrecurso(Tipcal.Descripcion,"+ lenguaje + ")) "+
                              "  From Scs_Tipoencalidad Tipcal Where Tipcal.Idtipoencalidad = dd."+ ScsDefendidosDesignaBean.C_IDTIPOENCALIDAD+
                              "  And Tipcal.Idinstitucion = dd."+ ScsDefendidosDesignaBean.C_CALIDADIDINSTITUCION+") as CALIDAD_DEFENDIDO, "+                              
			//String sql ="select " +
			//"decode(dd."+ScsDefendidosDesignaBean.C_CALIDAD+",'D','"+demandante+"','O','"+demandado+"','') CALIDAD_DEFENDIDO,"+
			"pd."+ScsPersonaJGBean.C_NIF +" NIF_DEFENDIDO,"+
			"pd." + ScsPersonaJGBean.C_APELLIDO1 +
			"||' '||pd." + ScsPersonaJGBean.C_APELLIDO2 +
			"||', '||pd." + ScsPersonaJGBean.C_NOMBRE + " NOMBRE_DEFENDIDO," +
			"pd." + ScsPersonaJGBean.C_DIRECCION +
			"||' '||pd." + ScsPersonaJGBean.C_CODIGOPOSTAL +
			"||' '||p." + CenPoblacionesBean.C_NOMBRE + " DIRECCION_DEFENDIDO," +
			"(select tp."+ScsTelefonosPersonaBean.C_NUMEROTELEFONO+" from "+ScsTelefonosPersonaBean.T_NOMBRETABLA+" tp"+
			" where tp."+ScsTelefonosPersonaBean.C_IDINSTITUCION+"=dd."+ScsDefendidosDesignaBean.C_IDINSTITUCION+
			" and tp."+ScsTelefonosPersonaBean.C_IDPERSONA+"=dd."+ScsDefendidosDesignaBean.C_IDPERSONA+
			" and rownum=1 ) TELEFONO_DEFENDIDO"+
			" from "+
			ScsDefendidosDesignaBean.T_NOMBRETABLA +" dd,"+
			ScsPersonaJGBean.T_NOMBRETABLA +" pd,"+
			CenPoblacionesBean.T_NOMBRETABLA +" p "+
			"where dd."+ScsDefendidosDesignaBean.C_IDPERSONA+"=pd." +ScsPersonaJGBean.C_IDPERSONA+"(+)"+			 
			"  and dd."+ScsDefendidosDesignaBean.C_IDINSTITUCION+"=pd." +ScsPersonaJGBean.C_IDINSTITUCION+"(+)"+	
			"  and pd." +ScsPersonaJGBean.C_IDPOBLACION+"=p." +CenPoblacionesBean.C_IDPOBLACION +"(+)"+
			"  and dd."+ScsDefendidosDesignaBean.C_IDINSTITUCION+"="+institucion+
			"  and dd."+ScsDefendidosDesignaBean.C_ANIO+"="+anio+
			"  and dd."+ScsDefendidosDesignaBean.C_IDTURNO+"="+turno+
			"  and dd."+ScsDefendidosDesignaBean.C_NUMERO+"="+numero+
			" order by NOMBRE_DEFENDIDO";
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql)) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable resultado=fila.getRow();	                  
					datos.add(resultado);
				}
			} 
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre los defendidos de una designa.");
		}
		return datos;                        
	}
	public Vector getDefendidosDesigna(Integer institucion, Integer anio, Integer numero, Integer turno) throws ClsExceptions,SIGAException {
		Vector datos=new Vector();
		try {
			
			StringBuffer sql = new StringBuffer();
			sql.append(" select def.idpersona  ");
			sql.append(" from SCS_DEFENDIDOSDESIGNA def  ");
 
			sql.append(" WHERE def.idinstitucion = ");
			sql.append(institucion);
			sql.append(" AND def.ANIO = ");
			sql.append(anio);
			sql.append(" and def.NUMERO =  ");
			sql.append(numero);
			
			sql.append(" and def.IDTURNO = ");
			sql.append(turno);
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql.toString())) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable resultado=fila.getRow();	                  
					datos.add(resultado);
				}
			} 
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre los defendidos de una designa.");
		}
		return datos;                        
	}

}