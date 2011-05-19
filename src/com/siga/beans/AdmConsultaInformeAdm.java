package com.siga.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

public class AdmConsultaInformeAdm extends MasterBeanAdministrador {

	public AdmConsultaInformeAdm(UsrBean usuario) {
		super(AdmConsultaInformeBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = { AdmConsultaInformeBean.C_IDINSTITUCION,
				AdmConsultaInformeBean.C_IDPLANTILLA,
				AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA,
				AdmConsultaInformeBean.C_IDCONSULTA,
				AdmConsultaInformeBean.C_NOMBRE,
				AdmConsultaInformeBean.C_VARIASLINEAS,
				AdmConsultaInformeBean.C_FECHAMODIFICACION,
				AdmConsultaInformeBean.C_USUMODIFICACION };
		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = { AdmConsultaInformeBean.C_IDINSTITUCION,
				AdmConsultaInformeBean.C_IDCONSULTA,
				AdmConsultaInformeBean.C_IDPLANTILLA };
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		AdmConsultaInformeBean bean = null;
		try {
			bean = new AdmConsultaInformeBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmConsultaInformeBean.C_IDINSTITUCION));
			bean.setIdPlantilla(UtilidadesHash.getString(hash, AdmConsultaInformeBean.C_IDPLANTILLA));
			bean.setIdInstitucion_consulta(UtilidadesHash.getInteger(hash, AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, AdmConsultaInformeBean.C_IDCONSULTA));
			bean.setNombre(UtilidadesHash.getString(hash, AdmConsultaInformeBean.C_NOMBRE));
			bean.setVariasLineas(UtilidadesHash.getString(hash, AdmConsultaInformeBean.C_VARIASLINEAS));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmConsultaInformeBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmConsultaInformeBean.C_USUMODIFICACION));
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e,
					"Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	protected MasterBean hashTableToConsultaBean(Hashtable hash) throws ClsExceptions {
		AdmConsultaInformeConsultaBean bean = null;
		try {
			bean = new AdmConsultaInformeConsultaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmConsultaInformeConsultaBean.C_IDINSTITUCION));
			bean.setIdPlantilla(UtilidadesHash.getString(hash, AdmConsultaInformeConsultaBean.C_IDPLANTILLA));
			bean.setIdInstitucion_consulta(UtilidadesHash.getInteger(hash, AdmConsultaInformeConsultaBean.C_IDINSTITUCION_CONSULTA));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, AdmConsultaInformeConsultaBean.C_IDCONSULTA));
			bean.setNombre(UtilidadesHash.getString(hash, AdmConsultaInformeConsultaBean.C_NOMBRE));
			bean.setVariasLineas(UtilidadesHash.getString(hash, AdmConsultaInformeConsultaBean.C_VARIASLINEAS));
			bean.setDescripcion(UtilidadesHash.getString(hash, AdmConsultaInformeConsultaBean.C_DESCRIPCION));
			bean.setGeneral(UtilidadesHash.getString(hash, AdmConsultaInformeConsultaBean.C_GENERAL));
			bean.setSentencia(UtilidadesHash.getString(hash, AdmConsultaInformeConsultaBean.C_SENTENCIA));
			bean.setIdModulo(UtilidadesHash.getInteger(hash, AdmConsultaInformeConsultaBean.C_IDMODULO));
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e,
					"Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			AdmConsultaInformeBean b = (AdmConsultaInformeBean) bean;
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA, b.getIdInstitucion_consulta());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_VARIASLINEAS, b.getVariasLineas());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmConsultaInformeBean.C_USUMODIFICACION, b.getUsuMod());
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions(e,
					"Error al crear el hashTable a partir del bean");
		}
		return htData;
	}

	/**
	 * Sustituye los filtros en el texto de la sentencia
	 */
	public String sustituirFiltrosConsulta(AdmConsultaInformeConsultaBean consulta,
			ArrayList<HashMap<String, String>> listaFiltros)
	{
		String sentencia = consulta.getSentencia();
		
		// quitando saltos de linea (para que salga mejor en LOG)
		sentencia = sentencia.replaceAll("\n", " ");
		
		// reemplazando filtros
		String nombreCampo, obligatorio, valor;
		for (HashMap<String, String> filtro : listaFiltros) {
			nombreCampo = "%%" + filtro.get(AdmTipoFiltroInformeBean.C_NOMBRECAMPO).toUpperCase() + "%%";
			obligatorio = filtro.get(AdmTipoFiltroInformeBean.C_OBLIGATORIO);
			valor = filtro.get("VALOR");
			
			if (sentencia.indexOf(nombreCampo) > -1)
				sentencia = sentencia.replaceAll(nombreCampo, valor);
			else if (obligatorio == ClsConstants.DB_TRUE && consulta.getGeneral() == ClsConstants.DB_FALSE)
				return null; // no estan todos los filtros obligatorios
		}
		
		// comprobando los campos que faltan por sustituir en la sentencia
		if (sentencia.indexOf("%%") > -1)
			return null;
		
		return sentencia;
	} // sustituirFiltrosConsulta()
	
	/**
	 * Realiza una query de las consultas asociadas al informe (adm_consultainforme, con_consulta)
	 * @return Vector<>
	 * @throws SIGAException 
	 * @throws ClsExceptions 
	 */
	public Vector<AdmConsultaInformeConsultaBean> selectConsultas(Hashtable htPlantilla)
		throws ClsExceptions, SIGAException
	{
		String sentencia = 
			"select * " +
			"  from "+AdmConsultaInformeBean.T_NOMBRETABLA+" inf, "+ConConsultaBean.T_NOMBRETABLA+" con " +
			" where inf."+AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA+" = con."+ConConsultaBean.C_IDINSTITUCION+" " +
			"   and inf."+AdmConsultaInformeBean.C_IDCONSULTA+" = con."+ConConsultaBean.C_IDCONSULTA+" " +
			"   and inf."+AdmConsultaInformeBean.C_IDINSTITUCION+" = "+htPlantilla.get(AdmConsultaInformeBean.C_IDINSTITUCION)+" " +
			"   and inf."+AdmConsultaInformeBean.C_IDPLANTILLA+" = '"+htPlantilla.get(AdmConsultaInformeBean.C_IDPLANTILLA)+"' ";
		Vector hashtables = super.selectGenerico(sentencia);
		
		Hashtable ht;
		Vector<AdmConsultaInformeConsultaBean> salida = new Vector<AdmConsultaInformeConsultaBean>();
		AdmConsultaInformeConsultaBean consultaBean;
		for (Iterator iterHashtables = hashtables.iterator(); iterHashtables.hasNext();) {
			ht = (Hashtable) iterHashtables.next();
			consultaBean = (AdmConsultaInformeConsultaBean) hashTableToConsultaBean(ht);
			salida.add(consultaBean);
		}
		
		return salida;
	}
	
	/**
	 * Ejecuta una sentencia y devuelve un Vector con los campos ordenados, 
	 * de forma que la salida pueda ser igual que se formateo en la sentencia sql
	 * 
	 * @param select
	 * @return  Vector<Hashtable<Integer, Hashtable<String, String>>>
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector<Vector<Hashtable<String, String>>> selectCamposOrdenados(String select) throws ClsExceptions, SIGAException
	{
		// Variables
		RowsContainer rc; // manejador de BD
		Row fila; // cada una de las filas obtenidas de BD
		String[] cabeceras;
		Hashtable<String, String> registro; // cada uno de las celdas de la tabla obtenida por la sentencia
		Vector<Hashtable<String, String>> linea;
		// Salida
		Vector<Vector<Hashtable<String, String>>> datos = new Vector<Vector<Hashtable<String, String>>>();

		try {
			rc = new RowsContainer();
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++) {
					fila = (Row) rc.get(i);
					cabeceras = rc.getFieldNames();
					linea = new Vector<Hashtable<String, String>>();
					for (int j = 0; j < cabeceras.length; j++) {
						registro = new Hashtable<String, String>();
						registro.put(cabeceras[j], (String) fila.getValue(cabeceras[j]));
						if (registro != null)
							linea.add(registro);
					}
					datos.add(linea);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage() + "Consulta SQL:" + select);
		}
		return datos;
	} // selectCamposOrdenados()
	
	public void deleteConsultaInforme(AdmConsultaInformeBean consultaInforme) throws ClsExceptions {
		StringBuffer sql = new StringBuffer();
		try {
			sql.append(" delete from "+AdmConsultaInformeBean.T_NOMBRETABLA);
			sql.append(" where "+AdmConsultaInformeBean.C_IDINSTITUCION+"="+ consultaInforme.getIdInstitucion().toString());
			sql.append(" and "+AdmConsultaInformeBean.C_IDPLANTILLA+"="+ consultaInforme.getIdPlantilla());
			if(consultaInforme.getIdConsulta()!=null){
				sql.append(" and "+AdmConsultaInformeBean.C_IDCONSULTA+"="+consultaInforme.getIdConsulta().toString());
				sql.append(" and "+AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA+"="+consultaInforme.getIdInstitucion_consulta().toString());
			}
			
			
			
			deleteSQL(sql.toString());
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage() + "Consulta SQL:" + sql.toString());
		}
	}

}
