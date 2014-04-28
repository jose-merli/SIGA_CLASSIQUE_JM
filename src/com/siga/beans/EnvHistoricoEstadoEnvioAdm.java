package com.siga.beans;

import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

public class EnvHistoricoEstadoEnvioAdm extends MasterBeanAdministrador {

	public EnvHistoricoEstadoEnvioAdm(UsrBean _usrBean) {
		super(EnvHistoricoEstadoEnvioBean.T_NOMBRETABLA, _usrBean);
	}

	@Override
	protected String[] getCamposBean() {
		return EnvHistoricoEstadoEnvioBean.getCamposBean();
	}

	@Override
	protected String[] getClavesBean() {
		return EnvHistoricoEstadoEnvioBean.getPK();
	}

	@Override
	protected String[] getOrdenCampos() {
		return EnvHistoricoEstadoEnvioBean.getCamposBean();
	}

	@Override
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		EnvHistoricoEstadoEnvioBean bean = null;
		try{
			bean = new EnvHistoricoEstadoEnvioBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvHistoricoEstadoEnvioBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvHistoricoEstadoEnvioBean.C_IDENVIO));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, EnvHistoricoEstadoEnvioBean.C_IDESTADO));
			bean.setIdHistorico(UtilidadesHash.getInteger(hash, EnvHistoricoEstadoEnvioBean.C_IDHISTORICO));
			bean.setFechaEstado(UtilidadesHash.getDate(hash, EnvHistoricoEstadoEnvioBean.C_FECHAESTADO));
		}catch (Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	@Override
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try{
			htData = new Hashtable();
			EnvHistoricoEstadoEnvioBean b = (EnvHistoricoEstadoEnvioBean) bean;
			UtilidadesHash.set(htData, EnvHistoricoEstadoEnvioBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvHistoricoEstadoEnvioBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvHistoricoEstadoEnvioBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, EnvHistoricoEstadoEnvioBean.C_IDHISTORICO, b.getIdHistorico());
			UtilidadesHash.set(htData, EnvHistoricoEstadoEnvioBean.C_FECHAESTADO, b.getFechaEstado());
		}catch (Exception e){
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;
	}
	
	
	/**
	 * Recibe un EnvEnvioBean y nos devuelve sus estados. 
	 * Lo unico que hay que rellenar del envio es su idInstitucion e idEnvio
	 * 
	 * @param envio EnvEnvioBean con idInstitucion e idEnvio
	 * @return Lista con beans de estados por los que ha pasado el envio
	 * @throws ClsExceptions 
	 */
	public List<EnvHistoricoEstadoEnvioBean> getEstadosEnvio(EnvEnviosBean envio) throws ClsExceptions{
		StringBuilder sql = new StringBuilder();
		sql.append(" where ");
		sql.append(EnvHistoricoEstadoEnvioBean.C_IDINSTITUCION + "=" + envio.getIdInstitucion());
		sql.append(" and ");
		sql.append(EnvHistoricoEstadoEnvioBean.C_IDENVIO + "=" + envio.getIdEnvio());
		try {
			return this.select(sql.toString());
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar los estados del envio");
		}
	} 
	
	public List<Hashtable<String, String>> getHistoricoEnvio(EnvEnviosBean envio) throws ClsExceptions{
		StringBuilder sql = new StringBuilder();
		// Recuperamos todos los datos del historico y ademas nos traemos el nombre del creador del estado y el nombre del estado
		sql.append(" select his.*");
		sql.append(", to_char(his.fechaestado,'dd/mm/yyyy hh24:mi:ss') as FECHACAMBIOESTADO ");
		sql.append(", nvl(usu.descripcion,'') as USUARIO, f_siga_getrecurso(est.nombre, " + usrbean.getLanguage() + ") as ESTADO ");
		sql.append(" from env_historicoestadoenvio his, adm_usuarios usu, env_estadoenvio est ");
		sql.append(" where usu.idusuario(+) = his.usumodificacion ");
		sql.append(" and usu.idinstitucion(+) = his.idinstitucion ");
		sql.append(" and est.idestado = his.idestado ");
		sql.append(" and his." + EnvHistoricoEstadoEnvioBean.C_IDINSTITUCION + "=" + envio.getIdInstitucion());
		sql.append(" and his." + EnvHistoricoEstadoEnvioBean.C_IDENVIO + "=" + envio.getIdEnvio());
		sql.append(" order by his." + EnvHistoricoEstadoEnvioBean.C_IDHISTORICO);
		try {
			return this.selectGenerico(sql.toString());
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los estados del envio");
		}
		
	}

}
