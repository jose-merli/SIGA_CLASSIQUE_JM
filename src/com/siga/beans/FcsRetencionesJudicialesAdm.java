//VERSIONES:
//Creacion: julio.vicente 31-03-2005

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;



/**
* Administrador de Pagos de Justicia Gratuita de la tabla FCS_PAGOSJG
* 
* @author david.sanchezp
*/
public class FcsRetencionesJudicialesAdm extends MasterBeanAdministrador {
	
	public FcsRetencionesJudicialesAdm(UsrBean usuario) {
		super(FcsRetencionesJudicialesBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsRetencionesJudicialesBean.C_IDINSTITUCION, 		FcsRetencionesJudicialesBean.C_IDPERSONA,
							FcsRetencionesJudicialesBean.C_IDRETENCION, 		FcsRetencionesJudicialesBean.C_IDDESTINATARIO,
							FcsRetencionesJudicialesBean.C_FECHAALTA,			FcsRetencionesJudicialesBean.C_FECHAINICIO,			
							FcsRetencionesJudicialesBean.C_FECHAFIN,			FcsRetencionesJudicialesBean.C_TIPORETENCION, 		
							FcsRetencionesJudicialesBean.C_IMPORTE,				FcsRetencionesJudicialesBean.C_OBSERVACIONES,	 	
							FcsRetencionesJudicialesBean.C_FECHAMODIFICACION,	FcsRetencionesJudicialesBean.C_USUMODIFICACION,		
							FcsRetencionesJudicialesBean.C_CONTABILIZADO,       FcsRetencionesJudicialesBean.C_ESDETURNO,
							FcsRetencionesJudicialesBean.C_DESCDESTINATARIO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsRetencionesJudicialesBean.C_IDINSTITUCION,  FcsRetencionesJudicialesBean.C_IDRETENCION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}


	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsRetencionesJudicialesBean bean = null;
		
		try {
			bean = new FcsRetencionesJudicialesBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsRetencionesJudicialesBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,FcsRetencionesJudicialesBean.C_IDPERSONA));
			bean.setIdRetencion(UtilidadesHash.getInteger(hash,FcsRetencionesJudicialesBean.C_IDRETENCION));
			bean.setIdDestinatario(UtilidadesHash.getInteger(hash,FcsRetencionesJudicialesBean.C_IDDESTINATARIO));
			bean.setFechaAlta(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_FECHAALTA));
			bean.setFechaInicio(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_FECHAINICIO));
			bean.setFechaFin(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_FECHAFIN));
			bean.setTipoRetencion(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_TIPORETENCION));
			bean.setImporte(UtilidadesHash.getDouble(hash,FcsRetencionesJudicialesBean.C_IMPORTE));
			bean.setObservaciones(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_OBSERVACIONES));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsRetencionesJudicialesBean.C_USUMODIFICACION));
			bean.setContabilizado(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_CONTABILIZADO));
			bean.setDescDestinatario(UtilidadesHash.getString(hash,FcsRetencionesJudicialesBean.C_DESCDESTINATARIO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		FcsRetencionesJudicialesBean beanRJ = (FcsRetencionesJudicialesBean) bean;
		try {
			htData = new Hashtable();			
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_IDINSTITUCION, beanRJ.getIdInstitucion());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_IDPERSONA, beanRJ.getIdPersona());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_IDRETENCION, beanRJ.getIdRetencion());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_IDDESTINATARIO, beanRJ.getIdDestinatario());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_FECHAALTA, beanRJ.getFechaAlta());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_FECHAINICIO, beanRJ.getFechaInicio());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_FECHAFIN, beanRJ.getFechaFin());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_TIPORETENCION, beanRJ.getTipoRetencion());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_IMPORTE, beanRJ.getImporte());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_OBSERVACIONES, beanRJ.getObservaciones());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_FECHAMODIFICACION, beanRJ.getFechaMod());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_USUMODIFICACION, beanRJ.getUsuMod());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_CONTABILIZADO, beanRJ.getContabilizado());
			UtilidadesHash.set(htData, FcsRetencionesJudicialesBean.C_DESCDESTINATARIO, beanRJ.getDescDestinatario());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.query(consulta)) {
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
	 * identificador de la retencion que se va a insertar.
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;		
		int contador = 0;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ FcsRetencionesJudicialesBean.C_IDRETENCION + ") + 1) AS IDRETENCION FROM " + nombreTabla;
			sql += " WHERE " + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = " + entrada.get(FcsRetencionesJudicialesBean.C_IDINSTITUCION); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDRETENCION").equals("")) {
					entrada.put(FcsRetencionesJudicialesBean.C_IDRETENCION,"1");
				}
				else entrada.put(FcsRetencionesJudicialesBean.C_IDRETENCION,(String)prueba.get("IDRETENCION"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDRETENCION");
		};		
		// Ahora ponemos la fecha en el formato adecuado		
		entrada.put(FcsRetencionesJudicialesBean.C_FECHAINICIO,GstDate.getApplicationFormatDate("",entrada.get(FcsRetencionesJudicialesBean.C_FECHAINICIO).toString()));
		if (entrada.get(FcsRetencionesJudicialesBean.C_FECHAFIN)!=null && !entrada.get(FcsRetencionesJudicialesBean.C_FECHAFIN).equals("") ){
		 entrada.put(FcsRetencionesJudicialesBean.C_FECHAFIN,GstDate.getApplicationFormatDate("",entrada.get(FcsRetencionesJudicialesBean.C_FECHAFIN).toString()));
		}
		
		return entrada;
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_ZONA con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
				
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = " + hash.get(FcsRetencionesJudicialesBean.C_IDINSTITUCION) + " AND " + 
						    FcsRetencionesJudicialesBean.C_IDRETENCION + " = " + hash.get(FcsRetencionesJudicialesBean.C_IDRETENCION);		
			datos = this.selectAll(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}		
	
	
	/** Funcion select (String where). Devuele todos los campos de los registros que cumplan los criterios.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectAll(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT * FROM " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + where;			
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	
	/** Funcion listadoRetenciones (String consulta). Devuelve un listado de retenciones en funcion de los 
	 * criterios de búsqueda  
	 */
	public Vector listadoRetenciones(Hashtable entrada) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String consulta = "";
		
		//Hacemos la join de las tablas que necesitamos		
		consulta = " SELECT " +
	       		   " (SELECT " + FcsDestinatariosRetencionesBean.C_NOMBRE + " FROM " +  FcsDestinatariosRetencionesBean.T_NOMBRETABLA + " D WHERE  D." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION + " = C." + 
	       		   FcsCobrosRetencionJudicialBean.C_IDINSTITUCION + " AND D." + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO + " = R." + FcsRetencionesJudicialesBean.C_IDDESTINATARIO + ") AS NOMBREDESTINATARIO," +
				   " (SELECT DECODE(" + CenColegiadoBean.C_COMUNITARIO + ", '1', " + CenColegiadoBean.C_NCOMUNITARIO+", " + CenColegiadoBean.C_NCOLEGIADO+") FROM " + CenColegiadoBean.T_NOMBRETABLA + " COL WHERE  COL." + CenColegiadoBean.C_IDPERSONA + " = C." + FcsCobrosRetencionJudicialBean.C_IDPERSONA + " AND COL." + CenColegiadoBean.C_IDINSTITUCION + " = C." + FcsCobrosRetencionJudicialBean.C_IDINSTITUCION + ") AS " + CenColegiadoBean.C_NCOLEGIADO +
				   ", (SELECT PER." + CenPersonaBean.C_NOMBRE + " || ' ' || PER." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || PER." + CenPersonaBean.C_APELLIDOS2 + " FROM " + CenPersonaBean.T_NOMBRETABLA + " PER WHERE PER." + CenPersonaBean.C_IDPERSONA + " = C." + FcsCobrosRetencionJudicialBean.C_IDPERSONA + ") AS NOMBRECOLEGIADO,(SELECT PAG." + FcsPagosJGBean.C_NOMBRE +
				   " FROM " + FcsPagosJGBean.T_NOMBRETABLA + " PAG WHERE PAG." + FcsPagosJGBean.C_IDINSTITUCION + " = C." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION + " AND PAG." + FcsPagosJGBean.C_IDPAGOSJG + " = C." + FcsCobrosRetencionJudicialBean.C_IDPAGOSJG + ") AS NOMBREPAGO, C." + FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO + ", C." + FcsCobrosRetencionJudicialBean.C_FECHARETENCION + 
				   " FROM " + FcsCobrosRetencionJudicialBean.T_NOMBRETABLA + " C, " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + " R WHERE C." + FcsCobrosRetencionJudicialBean.C_IDINSTITUCION + " = R." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " AND C." + FcsCobrosRetencionJudicialBean.C_IDRETENCION + " = R." + FcsRetencionesJudicialesBean.C_IDRETENCION + " AND C." + FcsCobrosRetencionJudicialBean.C_IDPERSONA + " = R." + FcsRetencionesJudicialesBean.C_IDPERSONA;
		
		// Ahora anhadimos los criterios de búsqueda
		if ((entrada.containsKey("nombreDestinatario")) && (!entrada.get("nombreDestinatario").toString().equals("")))
			consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)entrada.get("nombreDestinatario")).trim(),"(SELECT " + FcsDestinatariosRetencionesBean.C_NOMBRE + " FROM " +  FcsDestinatariosRetencionesBean.T_NOMBRETABLA + " D WHERE  D." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION + " = C." + 
			 			FcsCobrosRetencionJudicialBean.C_IDINSTITUCION + " AND D." + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO + " = R." + FcsRetencionesJudicialesBean.C_IDDESTINATARIO + ")" );
			            

		String fechaDesde = (String)entrada.get("fechaIniListado");
		String fechaHasta = (String)entrada.get("fechaFinListado");		
		consulta += fechaDesde!=null && !fechaDesde.equals("") || fechaHasta!=null && !fechaHasta.equals("") ?  " AND " + GstDate.dateBetweenDesdeAndHasta(FcsCobrosRetencionJudicialBean.C_FECHARETENCION, fechaDesde!=null && !fechaDesde.equals("") ? GstDate.getApplicationFormatDate("", fechaDesde) : null, fechaHasta!=null && !fechaHasta.equals("") ? GstDate.getApplicationFormatDate("", fechaHasta) : null) : ""; 
		
		// Finalmente se anhaden los criterios de ordenación
		consulta += " ORDER BY NOMBREDESTINATARIO, NCOLEGIADO";

		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.queryNLS(consulta)) {
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
	
}