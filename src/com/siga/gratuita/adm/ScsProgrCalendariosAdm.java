package com.siga.gratuita.adm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.general.SIGAException;
import com.siga.gratuita.beans.ScsHcoConfProgCalendariosBean;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.ConjuntoGuardiasForm;
import com.siga.gratuita.form.ProgrCalendariosForm;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;

public class ScsProgrCalendariosAdm extends MasterBeanAdministrador{

	
	public ScsProgrCalendariosAdm(UsrBean _usrBean) {
		super (ScsProgCalendariosBean.T_NOMBRETABLA, _usrBean);
	}
	protected String[] getCamposBean() {
		String [] campos = 
		{
				ScsProgCalendariosBean.C_IDINSTITUCION,
				ScsProgCalendariosBean.C_IDCONJUNTOGUARDIA,
				ScsProgCalendariosBean.C_IDPROGCALENDARIO,
				ScsProgCalendariosBean.C_ESTADO,
				ScsProgCalendariosBean.C_FECHA_PROGRAMACION,
				ScsProgCalendariosBean.C_FECHACALINICIO,
				ScsProgCalendariosBean.C_FECHACALFIN,
				
				ScsProgCalendariosBean.C_FECHAMODIFICACION,
				ScsProgCalendariosBean.C_USUMODIFICACION
				
		};
		return campos;
	}

	@Override
	protected String[] getClavesBean() {
		String [] claves = 
		{
				ScsProgCalendariosBean.C_IDPROGCALENDARIO,
				ScsProgCalendariosBean.C_IDINSTITUCION
		};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = 
		{
				ScsProgCalendariosBean.C_FECHA_PROGRAMACION
		};
		return orden;
	}
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsProgCalendariosBean bean = null;
		
		try {
			bean = new ScsProgCalendariosBean();
			bean.setIdConjuntoGuardia(			UtilidadesHash.getLong(hash, ScsProgCalendariosBean.C_IDCONJUNTOGUARDIA));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, ScsProgCalendariosBean.C_IDINSTITUCION));
			bean.setIdProgrCalendario(			UtilidadesHash.getLong(hash, ScsProgCalendariosBean.C_IDPROGCALENDARIO));
			bean.setFechaProgramacion(			UtilidadesHash.getString(hash, ScsProgCalendariosBean.C_FECHA_PROGRAMACION));
			bean.setFechaCalInicio(			UtilidadesHash.getString(hash, ScsProgCalendariosBean.C_FECHACALINICIO));
			bean.setFechaCalFin(			UtilidadesHash.getString(hash, ScsProgCalendariosBean.C_FECHACALFIN));
			bean.setEstado(			UtilidadesHash.getShort(hash, ScsProgCalendariosBean.C_ESTADO));
			bean.setFechaMod		(UtilidadesHash.getString(hash, ScsProgCalendariosBean.C_FECHAMODIFICACION));
			
			
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, ScsProgCalendariosBean.C_USUMODIFICACION));
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
			ScsProgCalendariosBean b = (ScsProgCalendariosBean) bean;
			
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_IDCONJUNTOGUARDIA, 	b.getIdConjuntoGuardia());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_IDPROGCALENDARIO, 	b.getIdProgrCalendario());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_ESTADO, 	b.getEstado());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_FECHA_PROGRAMACION, 	b.getFechaProgramacion());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_FECHACALINICIO, 	b.getFechaCalInicio());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_FECHACALFIN, 	b.getFechaCalFin());
			
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_USUMODIFICACION, 	b.getUsuMod());
			UtilidadesHash.set(htData, ScsProgCalendariosBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;		
	}
	 public List<ProgrCalendariosForm> getProgrCalendarios(ProgrCalendariosForm progrCalendariosForm)throws ClsExceptions{
    	Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT PC.IDPROGCALENDARIO,  PC.IDCONJUNTOGUARDIA , ");
		sql.append(" PC.IDINSTITUCION  ,  PC.FECHAPROGRAMACION ,  PC.FECHACALINICIO  , ");  
		sql.append(" PC.FECHACALFIN      ,  PC.ESTADO,GG.DESCRIPCION NOMBRECONJUNTOGUARDIAS ");
		sql.append(" FROM SCS_CONJUNTOGUARDIAS GG,SCS_PROG_CALENDARIOS PC ");
		sql.append(" WHERE GG.IDINSTITUCION = PC.IDINSTITUCION ");
		sql.append(" AND GG.IDCONJUNTOGUARDIA = PC.IDCONJUNTOGUARDIA ");

		sql.append(" AND PC.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progrCalendariosForm.getIdInstitucion());
		
		if(progrCalendariosForm.getIdConjuntoGuardia()!=null && !progrCalendariosForm.getIdConjuntoGuardia().equals("")){
			sql.append(" AND PC.IDCONJUNTOGUARDIA = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),progrCalendariosForm.getIdConjuntoGuardia());
			
		}
		if(progrCalendariosForm.getEstado()!=null && !progrCalendariosForm.getEstado().equals("")){
			sql.append(" AND PC.ESTADO = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),progrCalendariosForm.getEstado());
			
		}
		if(progrCalendariosForm.getFechaProgrDesde()!=null && !progrCalendariosForm.getFechaProgrDesde().equals("")){
			sql.append(" AND PC.FECHAPROGRAMACION >= :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),progrCalendariosForm.getFechaProgrDesde());
			
		}
		if(progrCalendariosForm.getFechaProgrHasta()!=null && !progrCalendariosForm.getFechaProgrHasta().equals("")){
			sql.append(" AND PC.FECHAPROGRAMACION <= :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),progrCalendariosForm.getFechaProgrHasta());
			
		}
		if(progrCalendariosForm.getFechaCalInicio()!=null && !progrCalendariosForm.getFechaCalInicio().equals("")){
			sql.append(" AND PC.FECHACALINICIO >= :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),progrCalendariosForm.getFechaCalInicio());
			
		}
		if(progrCalendariosForm.getFechaCalFin()!=null && !progrCalendariosForm.getFechaCalFin().equals("")){
			sql.append(" AND PC.FECHACALFIN <= :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),progrCalendariosForm.getFechaCalFin());
			
		}
		
		
		
		
		
		sql.append(" ORDER BY PC.FECHAPROGRAMACION ");
		
		
		
		
		
		
    	
    	
//		sql.append(" ORDER BY DESCRIPCION");
		
		
	
    	List<ProgrCalendariosForm> progrCalendariosForms = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				progrCalendariosForms = new ArrayList<ProgrCalendariosForm>();
				
				ScsProgCalendariosBean progCalendariosBean = null;
				ConjuntoGuardiasForm conjuntoGuardiaForm = null;
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					progCalendariosBean =  (ScsProgCalendariosBean)this.hashTableToBean(htFila);
					conjuntoGuardiaForm = new ConjuntoGuardiasForm();
					conjuntoGuardiaForm.setDescripcion(UtilidadesHash.getString(htFila, "NOMBRECONJUNTOGUARDIAS"));
					ProgrCalendariosForm progrCalendariosForm2=progCalendariosBean.getProgrCalendariosForm();
					progrCalendariosForm2.setConjuntoGuardias(conjuntoGuardiaForm);
					
					
					progrCalendariosForms.add(progrCalendariosForm2);
					
				}
			}else{
				progrCalendariosForms = new ArrayList<ProgrCalendariosForm>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return progrCalendariosForms;
    }
	public Long getNewIdProgrCalendarios() throws ClsExceptions{
        Long idProgrCalendarios = getSecuenciaNextVal(ScsProgCalendariosBean.SEQ_SCS_PROG_CALENDARIOS);
        return idProgrCalendarios;
    }
	public ScsProgCalendariosBean getProgrCalendario(
			ProgrCalendariosForm progrCalendariosForm) throws ClsExceptions {
		Hashtable progrCalendariosHashtable = new Hashtable();
		progrCalendariosHashtable.put(ScsProgCalendariosBean.C_IDPROGCALENDARIO,progrCalendariosForm.getIdProgrCalendario());
		progrCalendariosHashtable.put(ScsProgCalendariosBean.C_IDINSTITUCION,progrCalendariosForm.getIdInstitucion());
		
		Vector  progrCalendariosVector = selectByPK(progrCalendariosHashtable);
		return (ScsProgCalendariosBean) progrCalendariosVector.get(0);
	}
	public boolean updateEstado(ScsProgCalendariosBean progCalendariosBean) throws ClsExceptions{
		String[] campos = {ScsProgCalendariosBean.C_ESTADO,ScsProgCalendariosBean.C_FECHAMODIFICACION,ScsProgCalendariosBean.C_USUMODIFICACION};
		return super.updateDirect(beanToHashTable(progCalendariosBean),getClavesBean(),campos);
	}
	
	public ScsProgCalendariosBean getNextProgrCalendario()throws ClsExceptions{
    	Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM (SELECT PC.* ");
		sql.append(" FROM SCS_PROG_CALENDARIOS PC ");
		sql.append(" WHERE ");
//		sql.append(" PC.IDINSTITUCION = :");
//		contador++;
//		sql.append(contador);
//		codigosHashtable.put(new Integer(contador),idInstitucion);
		
		
		sql.append(" PC.ESTADO IN ( :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),ScsProgCalendariosBean.estadoProcesando);
		sql.append(",:");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),ScsProgCalendariosBean.estadoProgramado);
		sql.append(",:");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),ScsProgCalendariosBean.estadoReprogramado);
		sql.append(" )");

		sql.append(" AND PC.FECHAPROGRAMACION < SYSDATE ");
		
		sql.append(" ORDER BY PC.FECHAPROGRAMACION,PC.ESTADO) ");
		sql.append(" WHERE ROWNUM = 1 ");
	
		ScsProgCalendariosBean progCalendariosBean = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					progCalendariosBean =  (ScsProgCalendariosBean)this.hashTableToBean(htFila);
					
				
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return progCalendariosBean;
    }
	public void iniciarServicioProgramacion(UsrBean usrBean) throws ClsExceptions{
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getNextProgrCalendario();
		UserTransaction tx = null;
		ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean = null;
		try {
			tx = usrBean.getTransactionPesada();
			tx.begin();
		
			if(progCalendariosBean.getEstado().equals(ScsProgCalendariosBean.estadoProgramado)){
				//Insertamos en el historico
				hcoConfProgrCalendariosAdm.insertar(progCalendariosBean.getIdProgrCalendario());
				progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoProcesando);
				progrCalendariosAdm.updateEstado(progCalendariosBean);
				
			}
			//Obtenemos la sigueinte guardia programada y no generada
			hcoConfProgCalendariosBean = hcoConfProgrCalendariosAdm.getNextGuardiaConfigurada(progCalendariosBean);
			if(hcoConfProgCalendariosBean!=null){
			
				//Le cambiamos el estado a procesando
				hcoConfProgCalendariosBean.setEstado(ScsHcoConfProgCalendariosBean.estadoProcesando);
				hcoConfProgrCalendariosAdm.updateEstado(hcoConfProgCalendariosBean);
				// El metodo crear calerndario nos creara los calendarios. Hay mas de uno ya que pueden tener guardias vincualdas
				CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
				String observaciones = UtilidadesString.getMensajeIdioma(usrBean, "gratuita.calendarios.programacion.observaciones.automatica");
				int idCalendario =  calendarioSJCS.crearCalendario(hcoConfProgCalendariosBean.getIdInstitucion(), hcoConfProgCalendariosBean.getIdTurno(),
						hcoConfProgCalendariosBean.getIdGuardia(), progCalendariosBean.getFechaCalInicio(), progCalendariosBean.getFechaCalFin(),observaciones, null, null, null, usrBean);
				if(idCalendario<=0)
					throw new SIGAException("Error al crear el Calendario de guardias");
				
				calendarioSJCS.inicializaParaGenerarCalendario(hcoConfProgCalendariosBean.getIdInstitucion(),hcoConfProgCalendariosBean.getIdTurno(),
						hcoConfProgCalendariosBean.getIdGuardia(),new Integer(idCalendario), progCalendariosBean.getFechaCalInicio(),progCalendariosBean.getFechaCalFin(), usrBean);
				calendarioSJCS.generarCalendario();
				hcoConfProgCalendariosBean.setEstado(ScsHcoConfProgCalendariosBean.estadoFinalizado);
				hcoConfProgrCalendariosAdm.updateEstado(hcoConfProgCalendariosBean);
				
				
			}
			//si ya no quedan guardias pendientes de esta programacion la ponemos en estado finalizada
			if(hcoConfProgrCalendariosAdm.getNextGuardiaConfigurada(progCalendariosBean)==null){
				progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoFinalizado);
				progrCalendariosAdm.updateEstado(progCalendariosBean);
			}
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {e1.printStackTrace();}
			if(hcoConfProgCalendariosBean!=null){
				hcoConfProgCalendariosBean.setEstado(ScsHcoConfProgCalendariosBean.estadoError);
				hcoConfProgrCalendariosAdm.updateEstado(hcoConfProgCalendariosBean);
				
			}
			progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoError);
			progrCalendariosAdm.updateEstado(progCalendariosBean);
		}
		
	}
	public void reprogramarCalendarios(ScsProgCalendariosBean progCalendariosBean) throws ClsExceptions{
		//cambiamos ele stado y la fecha de programacion
		progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoReprogramado);
		progCalendariosBean.setFechaProgramacion("sysdate");
		updateDirect(progCalendariosBean);
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrbean);
		//cambiamos el estado de todos los historicos de esta programacion que no esten finalizados 
		hcoConfProgrCalendariosAdm.reprogramarHcoCalendarios(progCalendariosBean);

	}
	public void cancelarGeneracionCalendarios(
			ScsProgCalendariosBean progCalendariosBean)  throws ClsExceptions{
		progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoCancelado);
		updateDirect(progCalendariosBean);
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrbean);
		//cambiamos el estado de todos los historicos de esta programacion que no esten finalizados 
		hcoConfProgrCalendariosAdm.cancelarHcoCalendarios(progCalendariosBean);
		
	}
	
		
	
	

}
