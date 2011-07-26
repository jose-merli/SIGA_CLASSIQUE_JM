package com.siga.gratuita.adm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.gratuita.beans.ScsConfConjuntoGuardiasBean;
import com.siga.gratuita.form.ConfConjuntoGuardiasForm;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;
import com.siga.gratuita.form.DefinirTurnosForm;

public class ScsConfConjuntoGuardiasAdm extends MasterBeanAdministrador{

	public ScsConfConjuntoGuardiasAdm(UsrBean _usrBean) {
		super (ScsConfConjuntoGuardiasBean.T_NOMBRETABLA, _usrBean);
	}
	protected String[] getCamposBean() {
		String [] campos = 
		{
				ScsConfConjuntoGuardiasBean.C_IDINSTITUCION,
				ScsConfConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA,
				ScsConfConjuntoGuardiasBean.C_IDTURNO,
				ScsConfConjuntoGuardiasBean.C_IDGUARDIA,
				ScsConfConjuntoGuardiasBean.C_ORDEN,
				
				ScsConfConjuntoGuardiasBean.C_FECHAMODIFICACION,
				ScsConfConjuntoGuardiasBean.C_USUMODIFICACION
				
		};
		return campos;
	}

	@Override
	protected String[] getClavesBean() {
		String [] claves = 
		{
				ScsConfConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA,
				ScsConfConjuntoGuardiasBean.C_IDINSTITUCION,
				ScsConfConjuntoGuardiasBean.C_IDTURNO,
				ScsConfConjuntoGuardiasBean.C_IDGUARDIA
		};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = 
		{
				ScsConfConjuntoGuardiasBean.C_ORDEN
		};
		return orden;
	}
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsConfConjuntoGuardiasBean bean = null;
		
		try {
			bean = new ScsConfConjuntoGuardiasBean();
			bean.setIdConjuntoGuardia(			UtilidadesHash.getLong(hash, ScsConfConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, ScsConfConjuntoGuardiasBean.C_IDINSTITUCION));
			bean.setIdTurno(			UtilidadesHash.getInteger(hash, ScsConfConjuntoGuardiasBean.C_IDTURNO));
			bean.setIdGuardia(			UtilidadesHash.getInteger(hash, ScsConfConjuntoGuardiasBean.C_IDGUARDIA));
			bean.setOrden(			UtilidadesHash.getShort(hash, ScsConfConjuntoGuardiasBean.C_ORDEN));
			bean.setFechaMod		(UtilidadesHash.getString(hash, ScsConfConjuntoGuardiasBean.C_FECHAMODIFICACION));
			
			
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, ScsConfConjuntoGuardiasBean.C_USUMODIFICACION));
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
			ScsConfConjuntoGuardiasBean b = (ScsConfConjuntoGuardiasBean) bean;
			
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_IDCONJUNTOGUARDIA, 	b.getIdConjuntoGuardia());
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_IDTURNO, 	b.getIdTurno());
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_IDGUARDIA, 	b.getIdGuardia());
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_ORDEN, 		b.getOrden());
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_USUMODIFICACION, 	b.getUsuMod());
			UtilidadesHash.set(htData, ScsConfConjuntoGuardiasBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;		
	}
	public List<ConfConjuntoGuardiasForm> getConfiguracionConjuntoGuardias(ConfConjuntoGuardiasForm confConjuntoGuardiasForm,boolean isMostrarSoloGuardiasConfiguradas)throws ClsExceptions{
    	Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ( ");
		sql.append(" (SELECT T.IDTURNO, ");
		sql.append(" T.NOMBRE NOMBRETURNO, ");
		sql.append(" GT.IDGUARDIA, ");
		sql.append(" GT.NOMBRE NOMBREGUARDIA, ");
		sql.append(" CGG.ORDEN  ");
		sql.append(" FROM SCS_GUARDIASTURNO GT, SCS_TURNO T, SCS_CONF_CONJUNTO_GUARDIAS CGG ");
		sql.append(" WHERE CGG.IDINSTITUCION = GT.IDINSTITUCION ");
		sql.append(" AND CGG.IDTURNO = GT.IDTURNO ");
		sql.append(" AND CGG.IDGUARDIA = GT.IDGUARDIA ");
		sql.append(" AND T.IDINSTITUCION = GT.IDINSTITUCION ");
		sql.append(" AND T.IDTURNO = GT.IDTURNO ");
		sql.append(" AND GT.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),confConjuntoGuardiasForm.getIdInstitucion());
		sql.append(" AND CGG.IDCONJUNTOGUARDIA = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),confConjuntoGuardiasForm.getIdConjuntoGuardia());
		sql.append(" ) ");
		if(!isMostrarSoloGuardiasConfiguradas){
			sql.append(" UNION ");
			sql.append(" (SELECT T.IDTURNO, ");
			sql.append("   T.NOMBRE NOMBRETURNO, ");
			sql.append(" GT.IDGUARDIA, ");
			sql.append("   GT.NOMBRE NOMBREGUARDIA, ");
			sql.append("   NULL ORDEN ");
			sql.append(" FROM SCS_GUARDIASTURNO GT, SCS_TURNO T ");
			sql.append(" WHERE  ");
			sql.append(" T.IDINSTITUCION = GT.IDINSTITUCION ");
			sql.append(" AND T.IDTURNO = GT.IDTURNO ");
			sql.append(" AND GT.IDINSTITUCION = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),confConjuntoGuardiasForm.getIdInstitucion());
			sql.append(" AND T.VISIBILIDAD=:");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),"1");
			
			sql.append(" AND (T.IDTURNO,GT.IDGUARDIA) NOT IN (SELECT T.IDTURNO, GT.IDGUARDIA ");
     
			sql.append(" FROM SCS_GUARDIASTURNO          GT, ");
			sql.append(" SCS_TURNO                  T, ");
			sql.append(" SCS_CONF_CONJUNTO_GUARDIAS CGG ");
			sql.append(" WHERE CGG.IDINSTITUCION = GT.IDINSTITUCION ");
			sql.append(" AND CGG.IDTURNO = GT.IDTURNO ");
			sql.append(" AND CGG.IDGUARDIA = GT.IDGUARDIA ");
			sql.append(" AND T.IDINSTITUCION = GT.IDINSTITUCION ");
			sql.append(" AND T.IDTURNO = GT.IDTURNO ");
			sql.append(" AND GT.IDINSTITUCION = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),confConjuntoGuardiasForm.getIdInstitucion());
			sql.append(" AND CGG.IDCONJUNTOGUARDIA = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),confConjuntoGuardiasForm.getIdConjuntoGuardia());
			sql.append(") ");
			
			
			sql.append(")  ");
		}
		sql.append(") ");
		sql.append(" ORDER BY ORDEN, NOMBRETURNO,NOMBREGUARDIA ");
		
		
		
		
	
    	List<ConfConjuntoGuardiasForm> confConjuntoGuardiasForms = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				confConjuntoGuardiasForms = new ArrayList<ConfConjuntoGuardiasForm>();
				
				ScsConfConjuntoGuardiasBean ConjuntoGuardiasBean = null;
				DefinirTurnosForm turno = null;
				DefinirGuardiasTurnosForm guardia = null;
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					ConjuntoGuardiasBean =  (ScsConfConjuntoGuardiasBean)this.hashTableToBean(htFila);
					ConfConjuntoGuardiasForm ConjuntoGuardiasForm2=ConjuntoGuardiasBean.getConfConjuntoGuardiasFormForm();
					turno = new DefinirTurnosForm();
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					turno.setId(UtilidadesHash.getString(htFila, "IDTURNO"));
					ConjuntoGuardiasForm2.setTurno(turno);
					guardia = new DefinirGuardiasTurnosForm();
					guardia.setNombreGuardia(UtilidadesHash.getString(htFila, "NOMBREGUARDIA"));
					guardia.setId(UtilidadesHash.getString(htFila, "IDGUARDIA"));
					ConjuntoGuardiasForm2.setGuardia(guardia);
					
					confConjuntoGuardiasForms.add(ConjuntoGuardiasForm2);
					
				}
			}else{
				confConjuntoGuardiasForms = new ArrayList<ConfConjuntoGuardiasForm>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return confConjuntoGuardiasForms;
    }
	/**
	 * Solo se modifica lo que no es Pk
	 * @param confConjuntoGuardiasBean
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean update(ScsConfConjuntoGuardiasBean confConjuntoGuardiasBean) throws ClsExceptions{
		String[] campos = {ScsConfConjuntoGuardiasBean.C_ORDEN,ScsConfConjuntoGuardiasBean.C_FECHAMODIFICACION,ScsConfConjuntoGuardiasBean.C_USUMODIFICACION};
		return super.updateDirect(beanToHashTable(confConjuntoGuardiasBean),getClavesBean(),campos);
	}
//	public void duplicar(Long idConjuntoGuardia, Integer idInstitucion,String idConjuntoGuardiaCopiar) throws ClsExceptions {
//		Hashtable codigosHashtable = new Hashtable();
//    	int contador = 0;
//		StringBuffer sql = new StringBuffer();
//		
//		
//		sql.append(" INSERT INTO SCS_CONF_CONJUNTO_GUARDIAS "); 
//		sql.append(" (SELECT ");
//		sql.append(idConjuntoGuardia);
//		sql.append(",  IDINSTITUCION,  IDTURNO ,");
//		
//		
//		sql.append(" IDGUARDIA  ,  ORDEN  ,  SYSDATE,   ");
//		sql.append(usrbean.getUserName());
//		sql.append(" FROM SCS_CONF_CONJUNTO_GUARDIAS WHERE ");
//		sql.append(" IDCONJUNTOGUARDIA = :");
//		contador++;
//		sql.append(contador);
//		codigosHashtable.put(new Integer(contador),idConjuntoGuardiaCopiar);
//		sql.append("AND IDINSTITUCION = :"); 
//		contador++;
//		sql.append(contador);
//		codigosHashtable.put(new Integer(contador),idInstitucion.toString());
//		sql.append(")");
//
//		super.insertSQLBind(sql.toString(),codigosHashtable);
//		
//		
//		 
//		
//
//	}

	

}
