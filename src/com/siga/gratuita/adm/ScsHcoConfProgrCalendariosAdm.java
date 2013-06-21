package com.siga.gratuita.adm;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsCalendarioGuardiasBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.gratuita.beans.ScsHcoConfProgCalendariosBean;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;
import com.siga.gratuita.form.DefinirTurnosForm;
import com.siga.gratuita.form.HcoConfProgrCalendarioForm;
import com.siga.gratuita.form.ProgrCalendariosForm;
import com.siga.tlds.FilaExtElement;

public class ScsHcoConfProgrCalendariosAdm extends MasterBeanAdministrador{

	public ScsHcoConfProgrCalendariosAdm(UsrBean _usrBean) {
		super (ScsHcoConfProgCalendariosBean.T_NOMBRETABLA, _usrBean);
	}
	protected String[] getCamposBean() {
		String [] campos = 
		{
				ScsHcoConfProgCalendariosBean.C_IDINSTITUCION,
				ScsHcoConfProgCalendariosBean.C_IDPROGCALENDARIO,
				ScsHcoConfProgCalendariosBean.C_IDCONJUNTOGUARDIA,
				ScsHcoConfProgCalendariosBean.C_IDTURNO,
				ScsHcoConfProgCalendariosBean.C_IDGUARDIA,
				ScsHcoConfProgCalendariosBean.C_ORDEN,
				ScsHcoConfProgCalendariosBean.C_ESTADO,
				
				ScsHcoConfProgCalendariosBean.C_FECHAMODIFICACION,
				ScsHcoConfProgCalendariosBean.C_USUMODIFICACION
				
		};
		return campos;
	}

	@Override
	protected String[] getClavesBean() {
		String [] claves = 
		{
				ScsHcoConfProgCalendariosBean.C_IDPROGCALENDARIO,
				ScsHcoConfProgCalendariosBean.C_IDCONJUNTOGUARDIA,
				ScsHcoConfProgCalendariosBean.C_IDINSTITUCION,
				ScsHcoConfProgCalendariosBean.C_IDTURNO,
				ScsHcoConfProgCalendariosBean.C_IDGUARDIA
		};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = 
		{
				ScsHcoConfProgCalendariosBean.C_ORDEN
		};
		return orden;
	}
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsHcoConfProgCalendariosBean bean = null;
		
		try {
			bean = new ScsHcoConfProgCalendariosBean();
			bean.setIdProgrCalendario(			UtilidadesHash.getLong(hash, ScsHcoConfProgCalendariosBean.C_IDPROGCALENDARIO));
			bean.setIdConjuntoGuardia(			UtilidadesHash.getLong(hash, ScsHcoConfProgCalendariosBean.C_IDCONJUNTOGUARDIA));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, ScsHcoConfProgCalendariosBean.C_IDINSTITUCION));
			bean.setIdTurno(			UtilidadesHash.getInteger(hash, ScsHcoConfProgCalendariosBean.C_IDTURNO));
			bean.setIdGuardia(			UtilidadesHash.getInteger(hash, ScsHcoConfProgCalendariosBean.C_IDGUARDIA));
			bean.setOrden(			UtilidadesHash.getShort(hash, ScsHcoConfProgCalendariosBean.C_ORDEN));
			bean.setEstado(			UtilidadesHash.getShort(hash, ScsHcoConfProgCalendariosBean.C_ESTADO));
			bean.setFechaMod		(UtilidadesHash.getString(hash, ScsHcoConfProgCalendariosBean.C_FECHAMODIFICACION));
			
			
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, ScsHcoConfProgCalendariosBean.C_USUMODIFICACION));
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
			ScsHcoConfProgCalendariosBean b = (ScsHcoConfProgCalendariosBean) bean;
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_IDPROGCALENDARIO, 	b.getIdProgrCalendario());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_IDCONJUNTOGUARDIA, 	b.getIdConjuntoGuardia());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_IDTURNO, 	b.getIdTurno());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_IDGUARDIA, 	b.getIdGuardia());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_ORDEN, 		b.getOrden());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_ESTADO, 		b.getEstado());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_USUMODIFICACION, 	b.getUsuMod());
			UtilidadesHash.set(htData, ScsHcoConfProgCalendariosBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;		
	}
	public List<HcoConfProgrCalendarioForm> getHcoConfProgrCalendario(ProgrCalendariosForm progrCalendariosForm)throws ClsExceptions{
    	Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT T.IDTURNO,T.NOMBRE NOMBRETURNO,GT.IDGUARDIA,GT.NOMBRE NOMBREGUARDIA,PC.ORDEN ");
		sql.append(" ,PC.ESTADO,PC.IDCONJUNTOGUARDIA,PC.IDINSTITUCION,  ");
		sql.append(" PG.FECHACALINICIO,  PG.FECHACALFIN ");
		sql.append(" FROM SCS_HCO_CONF_PROG_CALENDARIOS PC,SCS_GUARDIASTURNO GT,SCS_TURNO T,SCS_PROG_CALENDARIOS PG WHERE  ");
		sql.append(" PC.IDINSTITUCION = GT.IDINSTITUCION");
		sql.append(" AND PC.IDTURNO = GT.IDTURNO");
		sql.append(" AND PC.IDGUARDIA = GT.IDGUARDIA ");
		sql.append(" AND T.IDINSTITUCION = GT.IDINSTITUCION ");
		sql.append(" AND T.IDTURNO = GT.IDTURNO ");
		sql.append(" AND PC.IDINSTITUCION = PG.IDINSTITUCION(+) ");
		sql.append(" AND PC.IDPROGCALENDARIO = PG.IDPROGCALENDARIO(+) ");
		
		sql.append(" AND PC.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progrCalendariosForm.getIdInstitucion());
		
		sql.append(" AND PC.IDPROGCALENDARIO");
		sql.append("=:");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progrCalendariosForm.getIdProgrCalendario());
		sql.append(" ORDER BY ORDEN,T.NOMBRE,GT.NOMBRE  ");
    	List<HcoConfProgrCalendarioForm> hcoConfProgrCalendarioForms = null;
    	ScsCalendarioGuardiasAdm calendarioGuardiasAdm = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String directorioLogCalendarios = rp.returnProperty("sjcs.directorioFisicoGeneracionCalendarios");
				hcoConfProgrCalendarioForms = new ArrayList<HcoConfProgrCalendarioForm>();
				
				ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean = null;
				DefinirTurnosForm turno = null;
				DefinirGuardiasTurnosForm guardia = null;
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					hcoConfProgCalendariosBean =  (ScsHcoConfProgCalendariosBean)this.hashTableToBean(htFila);
					HcoConfProgrCalendarioForm hcoConfProgCalendariosForm=hcoConfProgCalendariosBean.getHcoConfProgrCalendarioForm();
					turno = new DefinirTurnosForm();
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					turno.setId(UtilidadesHash.getString(htFila, "IDTURNO"));
					hcoConfProgCalendariosForm.setTurno(turno);
					guardia = new DefinirGuardiasTurnosForm();
					guardia.setNombreGuardia(UtilidadesHash.getString(htFila, "NOMBREGUARDIA"));
					guardia.setId(UtilidadesHash.getString(htFila, "IDGUARDIA"));
					hcoConfProgCalendariosForm.setGuardia(guardia);
					progrCalendariosForm.setFechaCalInicio(UtilidadesHash.getString(htFila, "FECHACALINICIO"));
					progrCalendariosForm.setFechaCalFin(UtilidadesHash.getString(htFila, "FECHACALFIN"));
					if(hcoConfProgCalendariosBean.getEstado().equals(ScsHcoConfProgCalendariosBean.estadoFinalizado)){
						if(calendarioGuardiasAdm==null)
							calendarioGuardiasAdm = new ScsCalendarioGuardiasAdm(usrbean);
						String fechaCalInicio = GstDate.getFormatedDateShort(usrbean.getLanguage(),progrCalendariosForm.getFechaCalInicio());
						String fechaCalFin = GstDate.getFormatedDateShort(usrbean.getLanguage(),progrCalendariosForm.getFechaCalFin());
						Integer idCalendarioGenerado = calendarioGuardiasAdm.getIdCalendarioGuardias(
								hcoConfProgCalendariosBean.getIdInstitucion(), hcoConfProgCalendariosBean.getIdTurno(),
								hcoConfProgCalendariosBean.getIdGuardia(), fechaCalInicio,
								fechaCalFin);
						if(idCalendarioGenerado!=null){
							StringBuffer sFicheroLog = new StringBuffer(directorioLogCalendarios);
							sFicheroLog.append(File.separator);
							sFicheroLog.append(hcoConfProgCalendariosForm.getIdInstitucion());
							sFicheroLog.append(File.separator);
							sFicheroLog.append(hcoConfProgCalendariosForm.getIdTurno());
							sFicheroLog.append(".");
							sFicheroLog.append(hcoConfProgCalendariosForm.getIdGuardia());
							sFicheroLog.append(".");
							sFicheroLog.append(idCalendarioGenerado);
							sFicheroLog.append("-");
							sFicheroLog.append(fechaCalInicio.replace('/', '.'));
							sFicheroLog.append("-");
							sFicheroLog.append(fechaCalFin.replace('/', '.'));
							sFicheroLog.append(".log.xls");
						
							File fichero = new File(sFicheroLog.toString());
							FilaExtElement[] elementosFila=new FilaExtElement[1];
							if(fichero!=null && fichero.exists()){
								//Boton de descarga del envio:
								elementosFila[0]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
							} else {
								elementosFila[0] = null;
							}
							hcoConfProgCalendariosForm.setElementosFila(elementosFila);
							hcoConfProgCalendariosForm.setIdCalendarioGuardias(idCalendarioGenerado.toString());
						}
					
						
					}
					
					hcoConfProgrCalendarioForms.add(hcoConfProgCalendariosForm);
					
				}
			}else{
				hcoConfProgrCalendarioForms = new ArrayList<HcoConfProgrCalendarioForm>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return hcoConfProgrCalendarioForms;
    }

	
	
	public ScsHcoConfProgCalendariosBean getNextGuardiaConfigurada(
			ScsProgCalendariosBean progCalendariosVO) throws ClsExceptions {
		Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM (" );
		sql.append(" SELECT ");
		sql.append(" PC.IDPROGCALENDARIO,PC.IDCONJUNTOGUARDIA, ");
		sql.append(" PC.IDINSTITUCION,PC.IDTURNO, ");
		sql.append(" PC.IDGUARDIA,	PC.ORDEN,	  PC.ESTADO ");
		  
		sql.append(" ,GT.NOMBRE	,GT.NUMEROLETRADOSGUARDIA	,GT.NUMEROSUSTITUTOSGUARDIA ");
		sql.append(" ,GT.DIASGUARDIA	,GT.DIASPAGADOS	,GT.VALIDARJUSTIFICACIONES,GT.DIASSEPARACIONGUARDIAS "); 
		sql.append(" ,GT.IDORDENACIONCOLAS,GT.NUMEROASISTENCIAS,GT.DESCRIPCION,GT.DESCRIPCIONFACTURACION ");
		sql.append(" ,GT.DESCRIPCIONPAGO	,GT.IDPARTIDAPRESUPUESTARIA,GT.NUMEROACTUACIONES ");
		sql.append(" ,GT.IDPERSONA_ULTIMO,GT.TIPODIASGUARDIA,GT.DIASPERIODO,GT.TIPODIASPERIODO ");        
		sql.append(" ,GT.FESTIVOS,GT.SELECCIONLABORABLES,GT.SELECCIONFESTIVOS,GT.IDTURNOSUSTITUCION ");     
		sql.append(" ,GT.IDGUARDIASUSTITUCION,GT.IDTIPOGUARDIA,GT.PORGRUPOS ");           
		sql.append(" ,GT.ROTARCOMPONENTES,GT.IDINSTITUCIONPRINCIPAL,GT.IDTURNOPRINCIPAL ");       
		sql.append(" ,GT.IDGUARDIAPRINCIPAL,GT.FECHASUSCRIPCION_ULTIMO,GT.IDGRUPOGUARDIA_ULTIMO ");
		
		
		sql.append(" FROM SCS_HCO_CONF_PROG_CALENDARIOS PC,SCS_GUARDIASTURNO GT WHERE  ");
		sql.append(" PC.IDINSTITUCION = GT.IDINSTITUCION");
		sql.append(" AND PC.IDGUARDIA = GT.IDGUARDIA");
		sql.append(" AND PC.IDTURNO = GT.IDTURNO");
		sql.append(" AND PC.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progCalendariosVO.getIdInstitucion());
		
		sql.append(" AND PC.IDPROGCALENDARIO");
		sql.append("=:");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progCalendariosVO.getIdProgrCalendario());
		
		sql.append(" AND ESTADO IN (:");
		
		
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),ScsHcoConfProgCalendariosBean.estadoProgramado);
		sql.append(" ,:");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),ScsHcoConfProgCalendariosBean.estadoReprogramado);
		sql.append(" )");
		
		
		
		sql.append(" ORDER BY ORDEN  ");
		sql.append(" ) WHERE  ROWNUM=1");
		ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			
			ScsGuardiasTurnoAdm guardiasTurnoAdm = new ScsGuardiasTurnoAdm(this.usrbean);
			if (rc.findBind(sql.toString(),codigosHashtable)&&rc.size()>0) {
				
				
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					hcoConfProgCalendariosBean =  (ScsHcoConfProgCalendariosBean)this.hashTableToBean(htFila);
					ScsGuardiasTurnoBean guardiasTurnoBean = (ScsGuardiasTurnoBean) guardiasTurnoAdm.hashTableToBean(htFila);
					hcoConfProgCalendariosBean.setGuardia(guardiasTurnoBean);
			}
					

 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return hcoConfProgCalendariosBean;
	}
	
	public void insertar(Long idProgrCalendario) throws ClsExceptions {
		Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" INSERT INTO SCS_HCO_CONF_PROG_CALENDARIOS "); 
		sql.append(" (SELECT PC.IDPROGCALENDARIO,CG.IDCONJUNTOGUARDIA,  CG.IDINSTITUCION , ");
		sql.append(" CG.IDTURNO ,  CG.IDGUARDIA,  CG.ORDEN, SYSDATE,");
		sql.append(usrbean.getUserName());
		sql.append(" , ");
		sql.append(ScsHcoConfProgCalendariosBean.estadoProgramado);
		sql.append(" FROM SCS_PROG_CALENDARIOS PC, SCS_CONF_CONJUNTO_GUARDIAS CG ");
		sql.append(" WHERE  PC.IDCONJUNTOGUARDIA = CG.IDCONJUNTOGUARDIA ");
		sql.append(" AND PC.IDINSTITUCION = CG.IDINSTITUCION AND PC.IDPROGCALENDARIO = ");
		sql.append(idProgrCalendario);
		sql.append(" ) ");
		super.insertSQL(sql.toString());
		

	}
	/**
	 * Solo se modifica lo que no es Pk
	 * @param confConjuntoGuardiasBean
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean updateEstado(ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean) throws ClsExceptions{
		String[] campos = {ScsHcoConfProgCalendariosBean.C_ESTADO,ScsHcoConfProgCalendariosBean.C_FECHAMODIFICACION,ScsHcoConfProgCalendariosBean.C_USUMODIFICACION};
		return super.updateDirect(beanToHashTable(hcoConfProgCalendariosBean),getClavesBean(),campos);
	}
	public void reprogramarHcoCalendarios(ScsProgCalendariosBean progCalendariosBean) throws ClsExceptions{
		StringBuffer sql = new StringBuffer(); 
		
		sql.append(" UPDATE SCS_HCO_CONF_PROG_CALENDARIOS ");
		sql.append(" SET ESTADO = ");
		sql.append(ScsHcoConfProgCalendariosBean.estadoReprogramado);
		sql.append(" , FECHAMODIFICACION = SYSDATE , USUMODIFICACION=");
		sql.append(usrbean.getUserName());
		sql.append(" WHERE IDPROGCALENDARIO = ");
		sql.append(progCalendariosBean.getIdProgrCalendario());
		sql.append(" AND IDINSTITUCION = ");
		sql.append(progCalendariosBean.getIdInstitucion());
		sql.append(" AND ESTADO <> "); 
		sql.append(ScsHcoConfProgCalendariosBean.estadoFinalizado);
		this.updateSQL(sql.toString());
		
		
	}
	public void cancelarHcoCalendarios(ScsProgCalendariosBean progCalendariosBean) throws ClsExceptions{
		StringBuffer sql = new StringBuffer(); 
		
		sql.append(" UPDATE SCS_HCO_CONF_PROG_CALENDARIOS ");
		sql.append(" SET ESTADO = ");
		sql.append(ScsHcoConfProgCalendariosBean.estadoCancelado);
		sql.append(" , FECHAMODIFICACION = SYSDATE , USUMODIFICACION=");
		sql.append(usrbean.getUserName());
		sql.append(" WHERE IDPROGCALENDARIO = ");
		sql.append(progCalendariosBean.getIdProgrCalendario());
		sql.append(" AND IDINSTITUCION = ");
		sql.append(progCalendariosBean.getIdInstitucion());
		sql.append(" AND ESTADO <> "); 
		sql.append(ScsHcoConfProgCalendariosBean.estadoFinalizado);
		sql.append(" AND ESTADO <> "); 
		sql.append(ScsHcoConfProgCalendariosBean.estadoProcesando);
		this.updateSQL(sql.toString());
		
		
	}
	public void delete(ScsProgCalendariosBean progCalendariosBean) throws ClsExceptions {
		StringBuffer sql = new StringBuffer(); 
		
		sql.append(" DELETE FROM SCS_HCO_CONF_PROG_CALENDARIOS ");
		sql.append(" WHERE IDPROGCALENDARIO = ");
		sql.append(progCalendariosBean.getIdProgrCalendario());
		sql.append(" AND IDINSTITUCION = ");
		sql.append(progCalendariosBean.getIdInstitucion());
		
		this.deleteSQL(sql.toString());
		
	}
	


}
