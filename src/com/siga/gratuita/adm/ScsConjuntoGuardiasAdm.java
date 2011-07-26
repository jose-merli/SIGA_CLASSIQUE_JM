package com.siga.gratuita.adm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.administracion.form.InformeForm;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.gratuita.beans.ScsConjuntoGuardiasBean;
import com.siga.gratuita.form.ConjuntoGuardiasForm;

public class ScsConjuntoGuardiasAdm extends MasterBeanAdministrador{

	public ScsConjuntoGuardiasAdm(UsrBean _usrBean) {
		super (ScsConjuntoGuardiasBean.T_NOMBRETABLA, _usrBean);
	}
	protected String[] getCamposBean() {
		String [] campos = 
		{
				ScsConjuntoGuardiasBean.C_IDINSTITUCION,
				ScsConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA,
				ScsConjuntoGuardiasBean.C_DESCRIPCION,
				ScsConjuntoGuardiasBean.C_FECHAMODIFICACION,
				ScsConjuntoGuardiasBean.C_USUMODIFICACION
				
		};
		return campos;
	}

	@Override
	protected String[] getClavesBean() {
		String [] claves = 
		{
				ScsConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA,
				ScsConjuntoGuardiasBean.C_IDINSTITUCION
		};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = 
		{
				ScsConjuntoGuardiasBean.C_DESCRIPCION
		};
		return orden;
	}
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsConjuntoGuardiasBean bean = null;
		
		try {
			bean = new ScsConjuntoGuardiasBean();
			bean.setIdConjuntoGuardia(			UtilidadesHash.getLong(hash, ScsConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA));
			bean.setFechaMod		(UtilidadesHash.getString(hash, ScsConjuntoGuardiasBean.C_FECHAMODIFICACION));
			bean.setDescripcion		(UtilidadesHash.getString(hash, ScsConjuntoGuardiasBean.C_DESCRIPCION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, ScsConjuntoGuardiasBean.C_IDINSTITUCION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, ScsConjuntoGuardiasBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsConjuntoGuardiasBean b = (ScsConjuntoGuardiasBean) bean;
			
			UtilidadesHash.set(htData, ScsConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA, 	b.getIdConjuntoGuardia());
			UtilidadesHash.set(htData, ScsConjuntoGuardiasBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsConjuntoGuardiasBean.C_DESCRIPCION, 		b.getDescripcion());
			UtilidadesHash.set(htData, ScsConjuntoGuardiasBean.C_USUMODIFICACION, 	b.getUsuMod());
			UtilidadesHash.set(htData, ScsConjuntoGuardiasBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;		
	}
	public List<ConjuntoGuardiasForm> getConjuntosGuardia(String idInstitucion)throws ClsExceptions{
    	Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT * ");
    	sql.append(" FROM SCS_ConjuntoGuardias I ");
    	sql.append(" WHERE");
    	sql.append("  I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),idInstitucion);
    	
    	
		sql.append(" ORDER BY DESCRIPCION");
		
		
	
    	List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				ConjuntoGuardiasForms = new ArrayList<ConjuntoGuardiasForm>();
				
				ScsConjuntoGuardiasBean ConjuntoGuardiasBean = null;
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					ConjuntoGuardiasBean =  (ScsConjuntoGuardiasBean)this.hashTableToBean(htFila);
					ConjuntoGuardiasForms.add(ConjuntoGuardiasBean.getConjuntoGuardiaForm());
					
				}
			}else{
				ConjuntoGuardiasForms = new ArrayList<ConjuntoGuardiasForm>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return ConjuntoGuardiasForms;
    }
	public Long getNewIdConjuntoGuardias() throws ClsExceptions{
        Long idConjuntoGuardias = getSecuenciaNextVal(ScsConjuntoGuardiasBean.SEQ_SCS_CONJUNTO_GUARDIAS);
        return idConjuntoGuardias;
    }
	public ScsConjuntoGuardiasBean getUltimoConjuntoGuardiaInsertado(String idInstitucion)throws ClsExceptions{
    	Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT * ");
    	sql.append(" FROM SCS_ConjuntoGuardias I ");
    	sql.append(" WHERE");
    	sql.append("  I.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),idInstitucion);
    	
    	
		sql.append(" ORDER BY IDCONJUNTOGUARDIA DESC");
		
		
	
		ScsConjuntoGuardiasBean ConjuntoGuardiasBean = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				
				InformeForm informeForm = null;
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					ConjuntoGuardiasBean =  (ScsConjuntoGuardiasBean)this.hashTableToBean(htFila);
					
				}
			}else  throw new ClsExceptions ("Error al buscar getUltimoConjuntoGuardiaInsertado");
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return ConjuntoGuardiasBean;
    }
	/**
	 * 
	 * @param bean
	 * @param idConjuntoGuardiaCopiar.Se copiara la configuracion de este grupo de guardias
	 * @return
	 * @throws ClsExceptions
	 */
//	public boolean insert(ScsConjuntoGuardiasBean bean, String idConjuntoGuardiaCopiar) throws ClsExceptions{
//		boolean insertOk = true;
//		try {
//			insertOk = super.insert(this.beanToHashTable(bean)); 
//			if(insertOk){
//				
//				if(idConjuntoGuardiaCopiar!=null && !idConjuntoGuardiaCopiar.equals("")){
//					ScsConfConjuntoGuardiasAdm confConjuntoGuardiasAdm = new ScsConfConjuntoGuardiasAdm(this.usrbean);
//					confConjuntoGuardiasAdm.duplicar(bean.getIdConjuntoGuardia(),bean.getIdInstitucion(),idConjuntoGuardiaCopiar);
//				}
//				
//				
//			}
//		}
//		catch (Exception e)	{
//			throw new ClsExceptions (e,  e.getMessage());
//		}
//		return insertOk;
//	}

}
