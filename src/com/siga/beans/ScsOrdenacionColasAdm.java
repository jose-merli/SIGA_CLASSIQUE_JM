
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_ORDENACIONCOLAS
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsOrdenacionColasAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsOrdenacionColasAdm (UsrBean usuario) {
		super( ScsOrdenacionColasBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,		ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,
							ScsOrdenacionColasBean.C_FECHANACIMIENTO,		ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,
							ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,		ScsOrdenacionColasBean.C_USUMODIFICACION,
							ScsOrdenacionColasBean.C_FECHAMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsOrdenacionColasBean.C_IDORDENACIONCOLAS};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsOrdenacionColasBean bean = null;
		try{
			bean = new ScsOrdenacionColasBean();
			bean.setIdOrdenacionColas(Integer.valueOf((String)hash.get(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS)));
			bean.setAlfabeticoApellidos(Integer.valueOf((String)hash.get(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS)));
			bean.setFechaNacimiento(Integer.valueOf((String)hash.get(ScsOrdenacionColasBean.C_FECHANACIMIENTO)));
			bean.setNumeroColegiado(Integer.valueOf((String)hash.get(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO)));
			bean.setAntiguedadCola(Integer.valueOf((String)hash.get(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA)));
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
			ScsOrdenacionColasBean b = (ScsOrdenacionColasBean) bean;
			hash.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,String.valueOf(b.getIdOrdenacionColas()));
			hash.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS, String.valueOf(b.getAlfabeticoApellidos()));
			hash.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO, String.valueOf(b.getFechaNacimiento()));
			hash.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO, String.valueOf(b.getNumeroColegiado()));
			hash.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA, String.valueOf(b.getAntiguedadCola()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return null;
	}
	
	/** Funcion prepararInsert (MasterBean entrada)
	 *  @param entrada con los datos del nuevo registro que se quiere crear
	 *  @return hashtable de entrada pero con la información del identificador del nuevo registro de ordenacion de colas
	 * 
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDORDENACIONCOLAS) + 1) AS IDORDENACIONCOLAS FROM " + nombreTabla;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDORDENACIONCOLAS").equals("")) {
					entrada.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,"1");
				}
				else entrada.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,(String)prueba.get("IDORDENACIONCOLAS"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return entrada;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	/**
	 * Obtiene la clausula order by para el select de inscripciones
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param usr
	 * @return Order by sin el "order by"
	 * @throws ClsExceptions
	 */
	public static String getOrderBy(String idOrdenacionColas, UsrBean usr)
			throws ClsExceptions
	{
		ScsOrdenacionColasAdm ordadm = new ScsOrdenacionColasAdm(usr);
		Hashtable hashOrden = new Hashtable();
		
		// obteniendo el orden
		hashOrden.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS, idOrdenacionColas);
		Vector vOrden = ordadm.select(hashOrden);
		ScsOrdenacionColasBean ordenBean = (ScsOrdenacionColasBean) vOrden.get(0);
		Integer apellidos = ordenBean.getAlfabeticoApellidos();
		Integer antiguedad = ordenBean.getAntiguedadCola();
		Integer fechanacimiento = ordenBean.getFechaNacimiento();
		Integer numerocolegiado = ordenBean.getNumeroColegiado();
		
		// calculando order by
		String orden = "";
		for (int i=4; i>0; i--) {
			if (Math.abs(apellidos) == i) {
				orden += ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+","+ScsOrdenacionColasBean.AUX_ALFABETICOAPELLIDOS_NOMBRE;
				if (Math.abs(apellidos) != apellidos) orden += " desc";
				orden += ", ";
			}
			if (Math.abs(antiguedad) == i) {
				orden += ScsOrdenacionColasBean.C_ANTIGUEDADCOLA;
				if (Math.abs(antiguedad) != antiguedad) orden += " desc";
				orden += ", ";
			}
			if (Math.abs(fechanacimiento) == i) {
				orden += ScsOrdenacionColasBean.C_FECHANACIMIENTO;
				if (Math.abs(fechanacimiento) != fechanacimiento) orden += " desc";
				orden += ", ";
			}
			if (Math.abs(numerocolegiado) == i) {
//				orden += "to_number("+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+")";
				orden += ScsOrdenacionColasBean.C_NUMEROCOLEGIADO;
				if (Math.abs(numerocolegiado) != numerocolegiado) orden += " desc";
				orden += ", ";
			}
		}
		if (orden.length() > 0)
			orden = orden.substring(0, orden.length()-2);
		
		return orden;
	} //getOrderBy()
			
}