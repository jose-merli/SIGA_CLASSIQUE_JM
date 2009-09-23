package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class PCAJGTablasMaestrasAdm extends MasterBeanAdministrador {
	public PCAJGTablasMaestrasAdm(UsrBean usuario) {
		super(PCAJGTablasMaestrasBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = { PCAJGTablasMaestrasBean.C_IDENTIFICADOR
				, PCAJGTablasMaestrasBean.C_ALIAS_TABLA
				, PCAJGTablasMaestrasBean.C_IDTABLAMAESTRASIGA	
				, PCAJGTablasMaestrasBean.C_TABLARELACION
				, PCAJGTablasMaestrasBean.C_FECHAMODIFICACION,
				PCAJGTablasMaestrasBean.C_USUMODIFICACION };

		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = { PCAJGTablasMaestrasBean.C_IDENTIFICADOR };

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PCAJGTablasMaestrasBean bean = null;

		try {
			bean = new PCAJGTablasMaestrasBean();

			bean.setIdentificador(UtilidadesHash.getString(hash, PCAJGTablasMaestrasBean.C_IDENTIFICADOR));
			bean.setAliasTabla(UtilidadesHash.getString(hash, PCAJGTablasMaestrasBean.C_ALIAS_TABLA));
			bean.setFechaMod(UtilidadesHash.getString(hash, PCAJGTablasMaestrasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, PCAJGTablasMaestrasBean.C_USUMODIFICACION));
			bean.setIdTablaMaestraSIGA(UtilidadesHash.getString(hash, PCAJGTablasMaestrasBean.C_IDTABLAMAESTRASIGA));
			bean.setTablaRelacion(UtilidadesHash.getString(hash, PCAJGTablasMaestrasBean.C_TABLARELACION));			
		}

		catch (Exception e) {
			bean = null;

			throw new ClsExceptions(e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;

		try {
			htData = new Hashtable();

			PCAJGTablasMaestrasBean b = (PCAJGTablasMaestrasBean) bean;

			UtilidadesHash.set(htData, PCAJGTablasMaestrasBean.C_IDENTIFICADOR, b.getIdentificador());
			UtilidadesHash.set(htData, PCAJGTablasMaestrasBean.C_ALIAS_TABLA, b.getAliasTabla());
			UtilidadesHash.set(htData, PCAJGTablasMaestrasBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, PCAJGTablasMaestrasBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, PCAJGTablasMaestrasBean.C_IDTABLAMAESTRASIGA, b.getIdTablaMaestraSIGA());						
			UtilidadesHash.set(htData, PCAJGTablasMaestrasBean.C_TABLARELACION, b.getTablaRelacion());
		}

		catch (Exception e) {
			htData = null;

			throw new ClsExceptions(e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	protected String[] getOrdenCampos() {
		String[] ordenCampos = { PCAJGTablasMaestrasBean.C_ALIAS_TABLA };
		return ordenCampos;
	}

	/**
	 * 
	 * @param nombreTablaMaestra
	 * @param codigo
	 * @param descripcion
	 */
	public String getSQLTM(String nombreTablaMaestra, String codigo, String descripcion, String languaje, int idInstitucion) throws ClsExceptions {
		String select = "";
		String from = "";
		String where = "";

		String sSQL = "SELECT P.IDTABLAMAESTRASIGA, P.TABLARELACION, S.LOCAL, S.IDCAMPOCODIGO, S.IDCAMPODESCRIPCION"
				+ " FROM PCAJG_TABLAS_MAESTRAS P, GEN_TABLAS_MAESTRAS S"
				+ " WHERE P.IDTABLAMAESTRASIGA = S.IDTABLAMAESTRA" 
				+ " AND P.IDENTIFICADOR = '" + nombreTablaMaestra + "'";
		RowsContainer rcTM = new RowsContainer();
		rcTM.find(sSQL);

		if (rcTM.size() > 0) {
			Row rowTM = (Row) rcTM.get(0);
			String idTablaMaestraSiga = rowTM.getString("IDTABLAMAESTRASIGA");
			String tablaRelacion = rowTM.getString("TABLARELACION");
			if (tablaRelacion != null && !tablaRelacion.trim().equals("")) {
				String idCampoDescripcion = rowTM.getString("IDCAMPODESCRIPCION");
				String campoFKSIGA = rowTM.getString("IDCAMPOCODIGO");
				String local = rowTM.getString("LOCAL");
	
				select = ", S." + rowTM.getString("IDCAMPOCODIGO") + " AS CODIGOSIGA" +
						", F_SIGA_GETRECURSO(S." + idCampoDescripcion + ", "
						+ languaje + ") AS DESCRIPCIONSIGA";
				from = ", " + tablaRelacion + " TR," + idTablaMaestraSiga + " S";
				where = " AND P.IDENTIFICADOR = TR.IDENTIFICADOR(+)" +
						" AND P.IDINSTITUCION = TR.IDINSTITUCION(+)" +
						" AND TR." + campoFKSIGA + " = S." + campoFKSIGA + "(+)";
				if (local != null && local.trim().equals("S")) {
					where += " AND TR.IDINSTITUCION = S.IDINSTITUCION(+)";
				}
			}
		}

		sSQL = "SELECT P.IDENTIFICADOR, P.CODIGO" +
				", F_SIGA_GETRECURSO(P.DESCRIPCION, " + languaje + ") AS DESCRIPCION" +
				", P.ABREVIATURA" +	select + 
				" FROM " + nombreTablaMaestra + " P" + from +
				" WHERE 1=1 " + where;
		
		sSQL += (!codigo.trim().equals("")) ? " AND " + ComodinBusquedas.prepararSentenciaCompleta(codigo.trim(), "P.CODIGO") : "";
		sSQL += (!descripcion.equals("")) ? " AND "
				+ ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(), "F_SIGA_GETRECURSO(P.DESCRIPCION, "
						+ languaje + ")") : "";
		sSQL += " AND P.IDINSTITUCION = " + idInstitucion;
		sSQL += " ORDER BY P.CODIGO";
		
		return sSQL;
	}
}