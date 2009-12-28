/*
 * Created on 17/09/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author fernando.gomez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgRemesaEstadosAdm extends MasterBeanAdministrador {

	public CajgRemesaEstadosAdm (UsrBean usu) {
		super (CajgRemesaEstadosBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgRemesaEstadosBean.C_IDINSTITUCION, CajgRemesaEstadosBean.C_IDREMESA,
							CajgRemesaEstadosBean.C_IDESTADO, 	   CajgRemesaEstadosBean.C_FECHAREMESA,
							CajgRemesaEstadosBean.C_FECHAMODIFICACION,	CajgRemesaEstadosBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgRemesaEstadosBean.C_IDINSTITUCION, 
							CajgRemesaEstadosBean.C_IDREMESA,
							CajgRemesaEstadosBean.C_IDESTADO,
							CajgRemesaEstadosBean.C_FECHAREMESA};
		return campos;
	}


	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgRemesaEstadosBean bean = null;
		try{
			bean = new CajgRemesaEstadosBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CajgRemesaEstadosBean.C_IDINSTITUCION));
			bean.setIdRemesa(UtilidadesHash.getInteger(hash,CajgRemesaEstadosBean.C_IDREMESA));
			bean.setIdestado(UtilidadesHash.getInteger(hash,CajgRemesaEstadosBean.C_IDESTADO));
			bean.setFecharemesa(UtilidadesHash.getString(hash, CajgRemesaEstadosBean.C_FECHAREMESA));
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgRemesaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_USUMODIFICACION));
			
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgRemesaEstadosBean b = (CajgRemesaEstadosBean) bean;
			
			UtilidadesHash.set(hash, CajgRemesaEstadosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgRemesaEstadosBean.C_IDREMESA, b.getIdRemesa());
			UtilidadesHash.set(hash, CajgRemesaEstadosBean.C_IDESTADO, b.getIdestado());
			UtilidadesHash.set(hash, CajgRemesaEstadosBean.C_FECHAREMESA, b.getFecharemesa());
			UtilidadesHash.set(hash, CajgRemesaEstadosBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgRemesaEstadosBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	public Vector busquedaEstadosRemesa(String idInstitucion, String idRemesa) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT r."+CajgRemesaEstadosBean.C_IDINSTITUCION+"";
			select  +=",r."+CajgRemesaEstadosBean.C_IDREMESA+"";
			select  +=",r."+CajgRemesaEstadosBean.C_IDESTADO+"";
			select  +=",r."+CajgRemesaEstadosBean.C_FECHAREMESA+"";
			select  +=",f_siga_getrecurso("+CajgTipoEstadoRemesaBean.C_DESCRIPCION+","+this.getLenguaje()+") AS DESCRIPCION";
			
			//FROM:
			select += " FROM "+CajgRemesaEstadosBean.T_NOMBRETABLA+" r";
			select += " ,"+CajgTipoEstadoRemesaBean.T_NOMBRETABLA+" t";

			//Filtro:
			select += " WHERE r."+CajgRemesaEstadosBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND r."+CajgRemesaEstadosBean.C_IDREMESA+"="+idRemesa;
			select += " AND r."+CajgRemesaEstadosBean.C_IDESTADO+"=t."+CajgTipoEstadoRemesaBean.C_IDESTADO+"";
			select += "  order by r."+CajgRemesaEstadosBean.C_IDESTADO;
			
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	public int UltimoEstadoRemesa(String idInstitucion, String idRemesa) throws ClsExceptions 
	{
		int ultimoEstado = -1;
		String select = null;
		
		try {
			select  = " SELECT r."+CajgRemesaEstadosBean.C_IDESTADO+"";
			
			
			//FROM:
			select += " FROM "+CajgRemesaEstadosBean.T_NOMBRETABLA+" r";
			

			//Filtro:
			select += " WHERE r."+CajgRemesaEstadosBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND r."+CajgRemesaEstadosBean.C_IDREMESA+"="+idRemesa;
			select += " AND r."+CajgRemesaEstadosBean.C_FECHAREMESA+"=(select max("+CajgRemesaEstadosBean.C_FECHAREMESA+")" +
																	  " FROM "+CajgRemesaEstadosBean.T_NOMBRETABLA+"" +
																	  " WHERE "+CajgRemesaEstadosBean.C_IDINSTITUCION+"=r."+CajgRemesaEstadosBean.C_IDINSTITUCION+"" +
																	  " AND "+CajgRemesaEstadosBean.C_IDREMESA+"=r."+CajgRemesaEstadosBean.C_IDREMESA+")";
			
			
			
			//Consulta:
			RowsContainer rc = new RowsContainer(); 
			rc = this.find(select);
            if (rc!=null) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  ultimoEstado=Integer.parseInt(fila.getString(CajgRemesaEstadosBean.C_IDESTADO));
                                  
               }
            }			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return ultimoEstado;
	}

	/**
	 * 
	 * @param usr
	 * @param idInstitucion
	 * @param idRemesa
	 * @param idEstado
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean nuevoEstadoRemesa(UsrBean usr, Integer idInstitucion, Integer idRemesa, Integer idEstado) throws ClsExceptions {
		boolean insertado = false;
		CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(usr);

		Vector vector = cajgRemesaEstadosAdm.select(" WHERE " + CajgRemesaEstadosBean.C_IDINSTITUCION + " = " + idInstitucion + " AND "
				+ CajgRemesaEstadosBean.C_IDREMESA + " = " + idRemesa + " AND " + CajgRemesaEstadosBean.C_IDESTADO + " IN (SELECT MAX("
				+ CajgRemesaEstadosBean.C_IDESTADO + ")" + " FROM " + CajgRemesaEstadosBean.T_NOMBRETABLA + " WHERE " + CajgRemesaEstadosBean.C_IDINSTITUCION
				+ " = " + idInstitucion + " AND " + CajgRemesaEstadosBean.C_IDREMESA + " = " + idRemesa + " )");

		if (vector != null && vector.size() > 0) {
			CajgRemesaEstadosBean estadoAnterior = (CajgRemesaEstadosBean) vector.get(0);
			if (estadoAnterior.getIdestado().intValue() == (idEstado.intValue() - 1)) {
				CajgRemesaEstadosBean cajgRemesaEstadosBean = new CajgRemesaEstadosBean();
				cajgRemesaEstadosBean.setIdInstitucion(idInstitucion);
				cajgRemesaEstadosBean.setIdRemesa(idRemesa);
				cajgRemesaEstadosBean.setIdestado(idEstado);
				cajgRemesaEstadosBean.setFecharemesa("SYSDATE");
				insertado = cajgRemesaEstadosAdm.insert(cajgRemesaEstadosBean);
			}
		}
		return insertado;

	}
	
}