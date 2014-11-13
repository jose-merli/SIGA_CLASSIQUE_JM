package com.siga.beans;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.general.SIGAException;

public class AdmGestionPermisosAdm extends MasterBeanAdministrador{

	public AdmGestionPermisosAdm(UsrBean usuario) {
		super(GenProcesoBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Devuelve todos los procesos de la aplicacion para gestionar los permisos
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException 
	 */
	public Vector<GenProcesoBean> getProcesos() throws ClsExceptions, SIGAException{
		String query = "select idproceso as id, nvl(idparent,'arbol') as parent, descripcion as text From gen_procesos  order by parent desc, text asc";
		Vector result = this.selectGenerico(query);
		return result;
	}
	
	/**
	 * Devuelve los permisos para un perfil
	 * @param location
	 * @param perfil 
	 * @return
	 * @throws SIGAException 
	 * @throws ClsExceptions 
	 */
	public Vector getPermisos(String location, String perfil) throws ClsExceptions, SIGAException {
		String query = "select DERECHOACCESO,IDPROCESO From adm_tiposacceso where idinstitucion="+location+" and idperfil='"+perfil+"'";
		Vector result = this.selectGenerico(query);
		return result;
	}
	
	/**
	 * Devuelve los permisos para un perfil
	 * @param location
	 * @param perfil 
	 * @return
	 * @throws SIGAException 
	 * @throws ClsExceptions 
	 */
	public Vector getPermisosPagina(String location, String perfil, String inicio, String cantidad) throws ClsExceptions, SIGAException {
		int ini=Integer.parseInt(inicio);
		int can=Integer.parseInt(cantidad);
		String query = "select IDPROCESO, DERECHOACCESO from (select a.*, rownum r from (select DERECHOACCESO, IDPROCESO From adm_tiposacceso"
                 +" where idinstitucion="+location+" and idperfil='"+perfil+"' order by idproceso) a"
                + " where rownum <= "+ (inicio+cantidad)+" )"
                + " where r >="+inicio; 
		Vector result = this.selectGenerico(query);
		return result;
	}
	
	/**
	 * Actualiza el permiso que se indique en los parametros
	 * @param idInstitucion
	 * @param idPerfil
	 * @param idProceso
	 * @param permiso
	 * @throws ClsExceptions
	 */
	public void setPermiso(String idInstitucion, String idPerfil, String idProceso, String permiso) throws ClsExceptions{
		String sqlInsert = "insert into adm_tiposacceso (idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion)"
			+ "values ('"+idProceso+"', '"+idPerfil+"', sysdate, "+usrbean.getUserName()+", "+permiso+", "+idInstitucion+")";
		
		String sqlInsertHistorico = "insert into adm_tiposacceso_historico (idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion, usueliminacion, fechaeliminacion)"
				+ "(select idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion, "+usrbean.getUserName()+", sysdate from adm_tiposacceso " 
				+" where idinstitucion = " + idInstitucion +" and idproceso = '" + idProceso +"'"+" and idperfil = '"+ idPerfil+"')";
		
		String sqlUpdate = "update adm_tiposacceso set derechoacceso = "+permiso
			+" where idinstitucion = " + idInstitucion
			+"  and idproceso = '" + idProceso +"'"
			+"  and idperfil = '"+ idPerfil+"'";
		
		try {
			this.insertSQL(sqlInsert);
		} catch (ClsExceptions e) {
			// Capturamos la excepcion, porque ya exista el elemento
			this.insertSQL(sqlInsertHistorico);
			this.updateSQL(sqlUpdate);
		}
	}

	@Override
	protected String[] getCamposBean() {
		return GenProcesoBean.getCamposBean();
	}

	@Override
	protected String[] getClavesBean() {
		return GenProcesoBean.getClavesBean();
	}

	@Override
	protected String[] getOrdenCampos() {
		return this.getCamposBean();
	}

	@Override
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenProcesoBean bean = new GenProcesoBean();
		bean.setMap(hash);
		return bean;
	}

	@Override
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		GenProcesoBean genBean = (GenProcesoBean)bean;
		Hashtable hash = new Hashtable();
		hash.putAll(genBean.getMap());
		return hash;
	}


	
}
