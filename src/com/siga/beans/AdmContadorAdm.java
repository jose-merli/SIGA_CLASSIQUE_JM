package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import org.redabogacia.sigaservices.app.exceptions.BusinessException;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * @author pilard
 */
public class AdmContadorAdm extends MasterBeanAdministrador
{
	public AdmContadorAdm(UsrBean usuario) {
		super(AdmContadorBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = { AdmContadorBean.C_CONTADOR,
				AdmContadorBean.C_DESCRIPCION,
				AdmContadorBean.C_FECHARECONFIGURACION,
				AdmContadorBean.C_IDCAMPOCONTADOR,
				AdmContadorBean.C_IDCAMPOPREFIJO,
				AdmContadorBean.C_IDCAMPOSUFIJO, AdmContadorBean.C_IDCONTADOR,
				AdmContadorBean.C_IDINSTITUCION, AdmContadorBean.C_IDTABLA,
				AdmContadorBean.C_LONGITUDCONTADOR,
				AdmContadorBean.C_MODIFICABLECONTADOR, AdmContadorBean.C_MODO,
				AdmContadorBean.C_NOMBRE, AdmContadorBean.C_PREFIJO,
				AdmContadorBean.C_RECONFIGURACIONCONTADOR,
				AdmContadorBean.C_RECONFIGURACIONPREFIJO,
				AdmContadorBean.C_RECONFIGURACIONSUFIJO,
				AdmContadorBean.C_SUFIJO, AdmContadorBean.C_GENERAL,
				AdmContadorBean.C_IDMODULO,
				AdmContadorBean.C_FECHAMODIFICACION, AdmContadorBean.C_USUMODIFICACION,
			    AdmContadorBean.C_FECHACREACION, AdmContadorBean.C_USUCREACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = { AdmContadorBean.C_IDINSTITUCION,
				AdmContadorBean.C_IDCONTADOR };
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		AdmContadorBean bean = null;

		try {
			bean = new AdmContadorBean();
			bean.setContador(UtilidadesHash.getLong(hash,
					AdmContadorBean.C_CONTADOR));
			bean.setDescripcion(UtilidadesHash.getString(hash,
					AdmContadorBean.C_DESCRIPCION));
			bean.setFechaReconfiguracion(UtilidadesHash.getString(hash,
					AdmContadorBean.C_FECHARECONFIGURACION));
			bean.setIdCampoContador(UtilidadesHash.getString(hash,
					AdmContadorBean.C_IDCAMPOCONTADOR));
			bean.setIdCampoPrefijo(UtilidadesHash.getString(hash,
					AdmContadorBean.C_IDCAMPOPREFIJO));
			bean.setIdCampoSufijo(UtilidadesHash.getString(hash,
					AdmContadorBean.C_IDCAMPOSUFIJO));
			bean.setIdContador(UtilidadesHash.getString(hash,
					AdmContadorBean.C_IDCONTADOR));
			bean.setIdinstitucion(UtilidadesHash.getInteger(hash,
					AdmContadorBean.C_IDINSTITUCION));
			bean.setIdTabla(UtilidadesHash.getString(hash,
					AdmContadorBean.C_IDTABLA));
			bean.setLongitudContador(UtilidadesHash.getInteger(hash,
					AdmContadorBean.C_LONGITUDCONTADOR));
			bean.setModificableContador(UtilidadesHash.getString(hash,
					AdmContadorBean.C_MODIFICABLECONTADOR));
			bean.setModoContador(UtilidadesHash.getInteger(hash,
					AdmContadorBean.C_MODO));
			bean.setNombre(UtilidadesHash.getString(hash,
					AdmContadorBean.C_NOMBRE));
			bean.setGeneral(UtilidadesHash.getString(hash,
					AdmContadorBean.C_GENERAL));
			bean.setPrefijo(UtilidadesHash.getString(hash,
					AdmContadorBean.C_PREFIJO));
			bean.setReconfiguracionContador(UtilidadesHash.getString(hash,
					AdmContadorBean.C_RECONFIGURACIONCONTADOR));
			bean.setReconfiguracionPrefijo(UtilidadesHash.getString(hash,
					AdmContadorBean.C_RECONFIGURACIONPREFIJO));
			bean.setReconfiguracionSufijo(UtilidadesHash.getString(hash,
					AdmContadorBean.C_RECONFIGURACIONSUFIJO));
			bean.setSufijo(UtilidadesHash.getString(hash,
					AdmContadorBean.C_SUFIJO));
			bean.setIdModulo(UtilidadesHash.getInteger(hash,
					AdmContadorBean.C_IDMODULO));			
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmContadorBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmContadorBean.C_USUMODIFICACION));
			bean.setFechacreacion(UtilidadesHash.getString(hash, AdmContadorBean.C_FECHACREACION));
			bean.setUsucreacion(UtilidadesHash.getInteger(hash, AdmContadorBean.C_USUCREACION));	

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
			AdmContadorBean b = (AdmContadorBean) bean;
			UtilidadesHash.set(htData, AdmContadorBean.C_CONTADOR, b
					.getContador());
			UtilidadesHash.set(htData, AdmContadorBean.C_DESCRIPCION, b
					.getDescripcion());
			UtilidadesHash.set(htData, AdmContadorBean.C_FECHARECONFIGURACION,
					b.getFechaReconfiguracion());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDCAMPOCONTADOR, b
					.getIdCampoContador());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDCAMPOPREFIJO, b
					.getIdCampoPrefijo());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDCAMPOSUFIJO, b
					.getIdCampoSufijo());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDCONTADOR, b
					.getIdContador());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDINSTITUCION, b
					.getIdinstitucion());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDTABLA, b
					.getIdTabla());
			UtilidadesHash.set(htData, AdmContadorBean.C_LONGITUDCONTADOR, b
					.getLongitudContador());
			UtilidadesHash.set(htData, AdmContadorBean.C_MODIFICABLECONTADOR, b
					.getModificableContador());
			UtilidadesHash.set(htData, AdmContadorBean.C_MODO, b
					.getModoContador());
			UtilidadesHash.set(htData, AdmContadorBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, AdmContadorBean.C_GENERAL, b
					.getGeneral());
			UtilidadesHash.set(htData, AdmContadorBean.C_PREFIJO, b
					.getPrefijo());
			UtilidadesHash.set(htData,
					AdmContadorBean.C_RECONFIGURACIONCONTADOR, b
							.getReconfiguracionContador());
			UtilidadesHash.set(htData,
					AdmContadorBean.C_RECONFIGURACIONPREFIJO, b
							.getReconfiguracionPrefijo());
			UtilidadesHash.set(htData, AdmContadorBean.C_RECONFIGURACIONSUFIJO,
					b.getReconfiguracionSufijo());
			UtilidadesHash.set(htData, AdmContadorBean.C_SUFIJO, b.getSufijo());
			UtilidadesHash.set(htData, AdmContadorBean.C_IDMODULO, b
					.getIdModulo());			
			UtilidadesHash.set(htData, AdmContadorBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmContadorBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, AdmContadorBean.C_FECHACREACION, b.getFechacreacion());
			UtilidadesHash.set(htData, AdmContadorBean.C_USUCREACION, b.getUsucreacion());	
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions(e,
					"Error al crear el hashTable a partir del bean");
		}
		return htData;
	}

	public Vector getContadores(Integer idInstitucion, String idModulo,
			String codigo, String nombre, String descripcion) {
		Vector datos = new Vector();

		try {
			String where = " WHERE " + AdmContadorBean.C_IDINSTITUCION + " = "
					+ idInstitucion;
			if (idModulo != null && !idModulo.equals("")) {
				where += " AND " + AdmContadorBean.C_IDMODULO + " = "
						+ idModulo;
			}
			if (codigo != null && !codigo.equals("")) {
				where += " AND "
						+ ComodinBusquedas.prepararSentenciaNLS(codigo.trim(),
								AdmContadorBean.T_NOMBRETABLA + "."
										+ AdmContadorBean.C_IDCONTADOR);
			}
			if (nombre != null && !nombre.equals("")) {
				where += " AND "
						+ ComodinBusquedas.prepararSentenciaNLS(nombre.trim(),
								AdmContadorBean.T_NOMBRETABLA + "."
										+ AdmContadorBean.C_NOMBRE);
			}
			if (descripcion != null && !descripcion.equals("")) {
				where += " AND "
						+ ComodinBusquedas.prepararSentenciaNLS(descripcion
								.trim(), AdmContadorBean.T_NOMBRETABLA + "."
								+ AdmContadorBean.C_DESCRIPCION);
			}

			datos = (Vector) this.select(where);

		} catch (Exception e) {
			return new Vector();
		}
		return datos;
	}

	public String obtenerNuevoMaxContadorFacturas(String idInstitucion) throws ClsExceptions, SIGAException {
		String salida = "";
		Vector datos = new Vector();

		StringBuilder select = new StringBuilder();
		select.append("select nvl(max(to_number(substr(idcontador, instr(idcontador, '_',1,2)+1))), 0) + 1 as ID ");
		select.append("  from ADM_CONTADOR ");
		select.append(" where idmodulo = 6 ");
		select.append("	  and substr(idcontador, 1, 3) = 'FAC' ");
		select.append("   and upper(idcontador) <> 'FAC_GENERAL' ");
		select.append("   and upper(idcontador) not like 'FAC_ABONOS%' ");
		select.append("   and general = '0' ");
		select.append("   and idinstitucion = ");
		select.append(idInstitucion);
		datos = this.selectGenerico(select.toString());
		if (datos == null || datos.size() == 0) {
			salida = "1";
		} else {
			salida = (String) ((Hashtable) datos.get(0)).get("ID");
			if (salida.trim().equals(""))
				salida = "1";
		}

		return salida;
	}

	public String obtenerNuevoMaxContadorAbonos(String idInstitucion) throws ClsExceptions, SIGAException {
		String salida = "";
		Vector datos = new Vector();

		StringBuilder select = new StringBuilder();
		select.append("select nvl(max(to_number(substr(idcontador, instr(idcontador, '_',1,3)+1))), 0) + 1 as ID ");
		select.append("  from ADM_CONTADOR ");
		select.append(" where idmodulo = 6 ");
		select.append("	  and substr(idcontador, 1, 10) = 'FAC_ABONOS' ");
		select.append("   and upper(idcontador) <> 'FAC_ABONOS_GENERAL' ");
		select.append("   and general = '0' ");
		select.append("   and idinstitucion = ");
		select.append(idInstitucion);
		datos = this.selectGenerico(select.toString());
		if (datos == null || datos.size() == 0) {
			salida = "1";
		} else {
			salida = (String) ((Hashtable) datos.get(0)).get("ID");
			if (salida.trim().equals(""))
				salida = "1";
		}

		return salida;
	}

	/**
	 * Borra el contador de facturas que esta relacionado con la serie
	 * si no es general ni tiene otras relaciones
	 */
	public boolean borrarContadorLibre(String idInstitucion, String idContador)
			throws ClsExceptions {

		String delete = 
			"DELETE ADM_CONTADOR " +
			" WHERE idmodulo = 6 " +
			"   AND IDCONTADOR = '"+idContador+"' " +
			"   AND IDINSTITUCION = "+idInstitucion+" " +
			"   AND GENERAL = '0' " +
			"   AND IDCONTADOR NOT IN (SELECT IDCONTADOR " +
			"                            FROM FAC_SERIEFACTURACION) " +
			"   AND IDCONTADOR NOT IN (SELECT IDCONTADOR_ABONOS " +
			"                            FROM FAC_SERIEFACTURACION) ";

		return this.deleteSQL(delete);
	}
	
	public void insertaNuevoContador(AdmContadorBean contadorBean,boolean isModificacleContador,
			boolean isReconfiguracionContador,boolean isGeneral,UsrBean usrBean)throws BusinessException {
		
		// Lo damos de alta en contadores y lo relacionamos.
		Hashtable<String,Object> htContador = new Hashtable<String,Object>();
		htContador.put(AdmContadorBean.C_IDINSTITUCION,contadorBean.getIdinstitucion());
		htContador.put(AdmContadorBean.C_IDCONTADOR,contadorBean.getIdContador());
		htContador.put(AdmContadorBean.C_NOMBRE,contadorBean.getNombre());
		htContador.put(AdmContadorBean.C_DESCRIPCION,contadorBean.getDescripcion());
		htContador.put(AdmContadorBean.C_CONTADOR,contadorBean.getContador());
		htContador.put(AdmContadorBean.C_PREFIJO,contadorBean.getPrefijo());
		htContador.put(AdmContadorBean.C_SUFIJO,contadorBean.getSufijo());
		htContador.put(AdmContadorBean.C_LONGITUDCONTADOR,contadorBean.getLongitudContador());
		htContador.put(AdmContadorBean.C_MODO,contadorBean.getModoContador());
		
		htContador.put(AdmContadorBean.C_MODIFICABLECONTADOR,isModificacleContador?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE);
		
		if(isReconfiguracionContador){
			if(contadorBean.getFechaReconfiguracion()==null)
				throw new BusinessException("Faltan parametros de reconfiguraci�n");
			htContador.put(AdmContadorBean.C_FECHARECONFIGURACION,contadorBean.getFechaReconfiguracion());
			htContador.put(AdmContadorBean.C_RECONFIGURACIONPREFIJO,contadorBean.getReconfiguracionPrefijo());
			htContador.put(AdmContadorBean.C_RECONFIGURACIONSUFIJO,contadorBean.getReconfiguracionSufijo());
			htContador.put(AdmContadorBean.C_RECONFIGURACIONCONTADOR,contadorBean.getReconfiguracionContador());				
						
		}else{
			htContador.put(AdmContadorBean.C_RECONFIGURACIONCONTADOR,"0");
			
		}
		
		htContador.put(AdmContadorBean.C_GENERAL,isGeneral?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE);
		
		
		htContador.put(AdmContadorBean.C_IDMODULO,contadorBean.getIdModulo());
		htContador.put(AdmContadorBean.C_IDTABLA,contadorBean.getIdTabla());
		htContador.put(AdmContadorBean.C_IDCAMPOCONTADOR,contadorBean.getIdCampoContador());
		htContador.put(AdmContadorBean.C_IDCAMPOPREFIJO,contadorBean.getIdCampoPrefijo());
		htContador.put(AdmContadorBean.C_IDCAMPOSUFIJO,contadorBean.getIdCampoSufijo());
		
		htContador.put(AdmContadorBean.C_FECHAMODIFICACION,"SYSDATE");
        htContador.put(AdmContadorBean.C_USUMODIFICACION,(new Integer(usrBean.getUserName())));
        htContador.put(AdmContadorBean.C_FECHACREACION,"SYSDATE");
        htContador.put(AdmContadorBean.C_USUCREACION,(new Integer(usrBean.getUserName())));			
        
        try {
			if (!this.insert(htContador)){
				throw new BusinessException("messages.inserted.error");
			}
		} catch (Exception e) {
			throw new BusinessException("messages.inserted.error",e);
		}
        
		
	}
	

}
