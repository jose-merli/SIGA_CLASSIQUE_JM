package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class AdmInformeAdm extends MasterBeanAdministrador
{
	//
	//CONSTRUCTORES
	//
	public AdmInformeAdm (UsrBean usuario) 
	{
		super (AdmInformeBean.T_NOMBRETABLA, usuario);
	}
	
	
	//
	//METODOS DE ADM
	//
	protected String[] getCamposBean()
	{
		String [] campos = 
		{
				AdmInformeBean.C_ALIAS,
				AdmInformeBean.C_DESCRIPCION,
				AdmInformeBean.C_FECHAMODIFICACION,
				AdmInformeBean.C_IDINSTITUCION,
				AdmInformeBean.C_IDPLANTILLA,
				AdmInformeBean.C_IDTIPOINFORME,
				AdmInformeBean.C_NOMBREFISICO,
				AdmInformeBean.C_NOMBRESALIDA,
				AdmInformeBean.C_DIRECTORIO,
				AdmInformeBean.C_USUMODIFICACION,
				AdmInformeBean.C_PRESELECCIONADO,
				AdmInformeBean.C_VISIBLE,
				AdmInformeBean.C_ASOLICITANTES,
				AdmInformeBean.C_DESTINATARIOS,
				AdmInformeBean.C_TIPOFORMATO
		};
		return campos;
	} //getCamposBean()
	
	protected String[] getClavesBean()
	{
		String [] claves = 
		{
				AdmInformeBean.C_IDPLANTILLA,
				AdmInformeBean.C_IDINSTITUCION
		};
		return claves;
	} //getClavesBean()
	
	protected String[] getOrdenCampos()
	{
		return getClavesBean();
	} //getOrdenCampos()
	
	protected MasterBean hashTableToBean (Hashtable hash) throws ClsExceptions
	{
		AdmInformeBean bean = null;
		
		try {
			bean = new AdmInformeBean();
			bean.setAlias			(UtilidadesHash.getString(hash, AdmInformeBean.C_ALIAS));
			bean.setDescripcion		(UtilidadesHash.getString(hash, AdmInformeBean.C_DESCRIPCION));
			bean.setFechaMod		(UtilidadesHash.getString(hash, AdmInformeBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, AdmInformeBean.C_IDINSTITUCION));
			bean.setIdPlantilla		(UtilidadesHash.getString(hash, AdmInformeBean.C_IDPLANTILLA));
			bean.setIdTipoInforme	(UtilidadesHash.getString(hash, AdmInformeBean.C_IDTIPOINFORME));
			bean.setNombreFisico	(UtilidadesHash.getString(hash, AdmInformeBean.C_NOMBREFISICO));
			bean.setNombreSalida	(UtilidadesHash.getString(hash, AdmInformeBean.C_NOMBRESALIDA));
			bean.setDirectorio		(UtilidadesHash.getString(hash, AdmInformeBean.C_DIRECTORIO));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, AdmInformeBean.C_USUMODIFICACION));
			bean.setVisible			(UtilidadesHash.getString(hash, AdmInformeBean.C_VISIBLE));
			bean.setPreseleccionado	(UtilidadesHash.getString(hash, AdmInformeBean.C_PRESELECCIONADO));
			bean.setASolicitantes	(UtilidadesHash.getString(hash, AdmInformeBean.C_ASOLICITANTES));
			bean.setDestinatarios	(UtilidadesHash.getString(hash, AdmInformeBean.C_DESTINATARIOS));
			bean.setTipoformato		(UtilidadesHash.getString(hash, AdmInformeBean.C_TIPOFORMATO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean()
	
	protected Hashtable beanToHashTable (MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			AdmInformeBean b = (AdmInformeBean) bean;
			UtilidadesHash.set(htData, AdmInformeBean.C_ALIAS, 				b.getAlias());
			UtilidadesHash.set(htData, AdmInformeBean.C_DESCRIPCION, 		b.getDescripcion());
			UtilidadesHash.set(htData, AdmInformeBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, AdmInformeBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmInformeBean.C_IDPLANTILLA, 		b.getIdPlantilla());
			UtilidadesHash.set(htData, AdmInformeBean.C_IDTIPOINFORME, 		b.getIdTipoInforme());
			UtilidadesHash.set(htData, AdmInformeBean.C_NOMBREFISICO, 		b.getNombreFisico());
			UtilidadesHash.set(htData, AdmInformeBean.C_NOMBRESALIDA, 		b.getNombreSalida());
			UtilidadesHash.set(htData, AdmInformeBean.C_DIRECTORIO, 		b.getDirectorio());
			UtilidadesHash.set(htData, AdmInformeBean.C_USUMODIFICACION, 	b.getUsuMod());
			UtilidadesHash.set(htData, AdmInformeBean.C_VISIBLE, 			b.getVisible());
			UtilidadesHash.set(htData, AdmInformeBean.C_PRESELECCIONADO, 	b.getPreseleccionado());
			UtilidadesHash.set(htData, AdmInformeBean.C_ASOLICITANTES, 		b.getASolicitantes());
			UtilidadesHash.set(htData, AdmInformeBean.C_DESTINATARIOS, 		b.getDestinatarios());
			UtilidadesHash.set(htData, AdmInformeBean.C_TIPOFORMATO, 		b.getTipoformato());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	} //beanToHashTable()
	
	
	//
	//OTROS METODOS
	//
	/**
	 * Obtiene un AdmInformeBean por clave. Si no existe para su institucion lo busca para la 0.
	 * @param idInstitucion
	 * @param idInforme
	 * @return AdmInformeBean
	 * @throws ClsExceptions
	 */
	public AdmInformeBean obtenerInforme (String idInstitucion,
										  String idInforme)
			throws ClsExceptions
	{
		AdmInformeBean salida = null;
		try {
			String select =
				"SELECT "+AdmInformeBean.C_ALIAS+", " +
				"       "+AdmInformeBean.C_DESCRIPCION+", " +
				"       "+AdmInformeBean.C_DIRECTORIO+", " +
				"       "+AdmInformeBean.C_IDINSTITUCION+", " +
				"       "+AdmInformeBean.C_IDPLANTILLA+", " +
				"       "+AdmInformeBean.C_IDTIPOINFORME+", " +
				"       "+AdmInformeBean.C_NOMBREFISICO+", " +
				"       "+AdmInformeBean.C_NOMBRESALIDA+", " +
				"       "+AdmInformeBean.C_PRESELECCIONADO+", " +
				"       "+AdmInformeBean.C_VISIBLE+"," +
				"       "+AdmInformeBean.C_ASOLICITANTES+", " +
				"       "+AdmInformeBean.C_DESTINATARIOS+" " +
				// "       "+AdmInformeBean.C_TIPOFORMATO+" " +
				"  FROM "+AdmInformeBean.T_NOMBRETABLA+" " +
				" WHERE "+AdmInformeBean.C_IDPLANTILLA+" = '"+idInforme+"' " +
				"   AND "+AdmInformeBean.C_VISIBLE+" = 'S' " +
				"   AND "+AdmInformeBean.C_IDINSTITUCION+" IN (0, "+idInstitucion+") " +
				" ORDER BY "+AdmInformeBean.C_IDINSTITUCION+ " DESC ";
			
			Vector datos = this.selectGenerico (select);
			if (datos==null || datos.size()==0) {
				throw new ClsExceptions ("No existe el informe que se busca. ID=" + idInforme);
			}
			else {
				Hashtable ht = (Hashtable) datos.get(0);
				salida = new AdmInformeBean();
				salida.setAlias				((String)ht.get(AdmInformeBean.C_ALIAS));
				salida.setDescripcion		((String)ht.get(AdmInformeBean.C_DESCRIPCION));
				salida.setDirectorio		((String)ht.get(AdmInformeBean.C_DIRECTORIO));
				salida.setIdInstitucion		(new Integer((String)ht.get(AdmInformeBean.C_IDINSTITUCION)));
				salida.setIdPlantilla		((String)ht.get(AdmInformeBean.C_IDPLANTILLA));
				salida.setIdTipoInforme		((String)ht.get(AdmInformeBean.C_IDTIPOINFORME));
				salida.setNombreFisico		((String)ht.get(AdmInformeBean.C_NOMBREFISICO));
				salida.setNombreSalida		((String)ht.get(AdmInformeBean.C_NOMBRESALIDA));
				salida.setPreseleccionado	((String)ht.get(AdmInformeBean.C_PRESELECCIONADO));
				salida.setVisible			((String)ht.get(AdmInformeBean.C_VISIBLE));
				salida.setASolicitantes		((String)ht.get(AdmInformeBean.C_ASOLICITANTES));
				salida.setDestinatarios		((String)ht.get(AdmInformeBean.C_DESTINATARIOS));
				salida.setTipoformato		((String)ht.get(AdmInformeBean.C_TIPOFORMATO));
			}
		}
		catch (ClsExceptions e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la plantilla: "+ e.toString());
		}
		return salida;
	} //obtenerInforme()
	
	/**
	 * Obtiene un Vector de AdmInformeBean por tipo de informe: 
	 *  1. Primero busca informes en la institucion pasada como parametro; 
	 *  2. Si no encuentra nada, busca en el Consejo al que pertenece la institucion;
	 *  3. Y por ultimo lo intentara en plantillas generales (0)
	 * 
	 * @param idInstitucion
	 * @param idTipoInforme
	 * @param aSolicitantes
	 * @param destinatarios
	 * @return Vector de AdmInformeBean
	 * @throws ClsExceptions
	 */
	public Vector obtenerInformesTipo(String idInstitucion,
			String idTipoInforme,
			String aSolicitantes,
			String destinatarios) throws ClsExceptions
	{
		final int NUM_CONSULTAS = 3;
		final int CON_COLEGIO = 0;
		final int CON_CONSEJO = 1;
		final int CON_GENERAL = 2;
		StringBuffer[] where = new StringBuffer[NUM_CONSULTAS];
		int k;
		Vector salida;

		// 1. generando consulta de la institucion
		k = CON_COLEGIO;
		where[k] = new StringBuffer(
				"WHERE VISIBLE = 'S' " +
				"  AND IDTIPOINFORME = '" + idTipoInforme + "' " +
				"  AND IDINSTITUCION = " + idInstitucion);
		if (aSolicitantes != null && aSolicitantes.equals("S"))
			where[k].append(
				"  AND ASOLICITANTES = 'S'");

		if (destinatarios != null && !destinatarios.equals("")) {
			where[k].append(
				"  AND (");
			for (int i = 0; i < destinatarios.length(); i++) {
				where[i].append(" DESTINATARIOS like '%" + destinatarios.charAt(i) + "%'");
				if (i < destinatarios.length() - 1)
					where[i].append(" OR ");
			}
			where[k].append("" +
				"      ) ");
		}

		// 2. generando consulta del consejo autonomico
		CenInstitucionAdm insadm = new CenInstitucionAdm(this.usrbean);
		Integer insConsejo = null;
		try {
			insConsejo = insadm.getInstitucionPadre(new Integer(idInstitucion));
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener el Consejo de la institucion: " + e.toString());
		}
		if (insConsejo != null) {
			k = CON_CONSEJO;
			where[k] = new StringBuffer(
					"WHERE VISIBLE = 'S' " +
					"  AND IDTIPOINFORME = '" + idTipoInforme + "' " +
					"  AND IDINSTITUCION = " + insConsejo);
			if (aSolicitantes != null && aSolicitantes.equals("S"))
				where[k].append(
					"  AND ASOLICITANTES = 'S'");

			// No hay destinatarios, ��deberia haberlos??
			/*if (destinatarios != null && !destinatarios.equals("")) {
				where[k].append(
					"  AND (");
				for (int i = 0; i < destinatarios.length(); i++) {
					where[i].append(" DESTINATARIOS like '%" + destinatarios.charAt(i) + "%'");
					if (i < destinatarios.length() - 1)
						where[i].append(" OR ");
				}
				where[k].append(
					"      ) ");
			}*/
		}

		// 3. generando consulta del CGAE
		k = CON_GENERAL;
		where[k] = new StringBuffer(
				"WHERE VISIBLE = 'S' " +
				"  AND IDTIPOINFORME = '" + idTipoInforme + "' " +
				"  AND IDINSTITUCION = " + 0);
		if (aSolicitantes != null && aSolicitantes.equals("S"))
			where[k].append(
				"  AND ASOLICITANTES = 'S'");

		// No hay destinatarios, ��deberia haberlos??
		/*if (destinatarios != null && !destinatarios.equals("")) {
			where[k].append(
				"  AND (");
			for (int i = 0; i < destinatarios.length(); i++) {
				where[i].append(" DESTINATARIOS like '%" + destinatarios.charAt(i) + "%'");
				if (i < destinatarios.length() - 1)
					where[i].append(" OR ");
			}
			where[k].append(
				"      ) ");
		}*/

		try {
			k = CON_COLEGIO;
			do {
				salida = this.select(where[k].toString());
				k++;
			} while ((salida == null || salida.size() == 0) && k < NUM_CONSULTAS);
		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la plantilla: " + e.toString());
		}

		return salida;
	} // obtenerInformesTipo()
	
}
