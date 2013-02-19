/*
 * Created on Dec 27, 2004
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.expedientes.form.BusquedaExpedientesForm;
import com.siga.general.SIGAException;


/**
 * Administrador del bean de expedientes
 */
public class ExpExpedienteAdm extends MasterBeanAdministrador {
	
	public ExpExpedienteAdm(UsrBean usuario)
	{
	    super(ExpExpedienteBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpExpedienteBean.C_IDINSTITUCION,
			ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
			ExpExpedienteBean.C_IDTIPOEXPEDIENTE,
			ExpExpedienteBean.C_NUMEROEXPEDIENTE,
			ExpExpedienteBean.C_ANIOEXPEDIENTE,			
			ExpExpedienteBean.C_NUMEXPDISCIPLINARIO,
			ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO,
			ExpExpedienteBean.C_FECHA,
			ExpExpedienteBean.C_ASUNTO,
			ExpExpedienteBean.C_JUZGADO,
			ExpExpedienteBean.C_PROCEDIMIENTO,
			ExpExpedienteBean.C_IDINSTITUCION_JUZGADO,
			ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO,
			ExpExpedienteBean.C_NUMASUNTO,
			ExpExpedienteBean.C_FECHAINICIALESTADO,
			ExpExpedienteBean.C_FECHAFINALESTADO,
			ExpExpedienteBean.C_FECHAPRORROGAESTADO,
			ExpExpedienteBean.C_DESCRIPCIONRESOLUCION,
			ExpExpedienteBean.C_ACTUACIONESPRESCRITAS,
			ExpExpedienteBean.C_ANOTACIONESCANCELADAS,
			ExpExpedienteBean.C_SANCIONPRESCRITA,
			ExpExpedienteBean.C_SANCIONFINALIZADA,
			ExpExpedienteBean.C_SANCIONADO,
			ExpExpedienteBean.C_ALERTAGENERADA,
			ExpExpedienteBean.C_ALERTAGENERADACAD,
			ExpExpedienteBean.C_ALERTAFASEGENERADA,
			ExpExpedienteBean.C_ALERTACADUCIDADGENERADA,
			ExpExpedienteBean.C_ALERTAFINALGENERADA,
			ExpExpedienteBean.C_IDCLASIFICACION,
			ExpExpedienteBean.C_ESVISIBLE,
			ExpExpedienteBean.C_ESVISIBLEENFICHA,
			ExpExpedienteBean.C_IDFASE,
			ExpExpedienteBean.C_IDESTADO,
			ExpExpedienteBean.C_FECHAMODIFICACION,
			ExpExpedienteBean.C_USUMODIFICACION,
			ExpExpedienteBean.C_FECHACADUCIDAD,
			ExpExpedienteBean.C_FECHARESOLUCION,
			ExpExpedienteBean.C_FECHAINICIALFASE,
			ExpExpedienteBean.C_OBSERVACIONES,
			ExpExpedienteBean.C_MINUTA,
			ExpExpedienteBean.C_IMPORTETOTAL,
			ExpExpedienteBean.C_MINUTAFINAL,
			ExpExpedienteBean.C_IMPORTETOTALFINAL,
			ExpExpedienteBean.C_DERECHOSCOLEGIALES,
			ExpExpedienteBean.C_PORCENTAJEIVA,
			ExpExpedienteBean.C_IDAREA,
			ExpExpedienteBean.C_IDMATERIA,
			ExpExpedienteBean.C_IDPRETENSION,
			ExpExpedienteBean.C_OTRASPRETENSIONES,
			ExpExpedienteBean.C_IDTIPOIVA,
			ExpExpedienteBean.C_IDRESULTADOJUNTAGOBIERNO,
			ExpExpedienteBean.C_IDENTIFICADORDS,
			ExpExpedienteBean.C_IDDIRECCION,
			ExpExpedienteBean.C_ANIOEJG,
			ExpExpedienteBean.C_NUMEROEJG,
			ExpExpedienteBean.C_IDTIPOEJG};

		return campos;
	}

	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpExpedienteBean.C_IDINSTITUCION, 
				ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, 
				ExpExpedienteBean.C_IDTIPOEXPEDIENTE, 
				ExpExpedienteBean.C_NUMEROEXPEDIENTE, 
				ExpExpedienteBean.C_ANIOEXPEDIENTE};

		return claves;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ExpExpedienteBean bean = null;

		try
		{
			bean = new ExpExpedienteBean();
			 if(hash.get(ExpExpedienteBean.C_IDTIPOEJG)!=null && !hash.get(ExpExpedienteBean.C_IDTIPOEJG).toString().equals("")){
				 bean.setTipoExpDisciplinario(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDTIPOEJG));
				 bean.setNumExpDisciplinario(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_NUMEROEJG));
				 bean.setAnioExpDisciplinario(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_ANIOEJG));
				 
			 }else{
				 bean.setAnioExpDisciplinario(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO));
				 bean.setNumExpDisciplinario(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_NUMEXPDISCIPLINARIO));
					
			 }
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_NUMEROEXPEDIENTE));
			bean.setFecha(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHA));
			bean.setAsunto(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ASUNTO));
			bean.setJuzgado(UtilidadesHash.getString(hash, ExpExpedienteBean.C_JUZGADO));
			bean.setProcedimiento(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_PROCEDIMIENTO));
			bean.setIdInstitucionJuzgado(UtilidadesHash.getString(hash, ExpExpedienteBean.C_IDINSTITUCION_JUZGADO));
			bean.setIdInstitucionProcedimiento(UtilidadesHash.getString(hash, ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO));
			bean.setNumAsunto(UtilidadesHash.getString(hash, ExpExpedienteBean.C_NUMASUNTO));
			bean.setFechaInicialEstado(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHAINICIALESTADO));
			bean.setFechaFinalEstado(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHAFINALESTADO));
			bean.setFechaProrrogaEstado(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHAPRORROGAESTADO));
			bean.setDescripcionResolucion(UtilidadesHash.getString(hash, ExpExpedienteBean.C_DESCRIPCIONRESOLUCION));
			bean.setSancionPrescrita(UtilidadesHash.getString(hash, ExpExpedienteBean.C_SANCIONPRESCRITA));
			bean.setActuacionesPrescritas(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ACTUACIONESPRESCRITAS));
			bean.setSancionFinalizada(UtilidadesHash.getString(hash, ExpExpedienteBean.C_SANCIONFINALIZADA));
			bean.setAlertaGenerada(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ALERTAGENERADA));
			bean.setAlertaGeneradaCad(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ALERTAGENERADACAD));
			bean.setAlertaFaseGenerada(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ALERTAFASEGENERADA));
			bean.setAlertaCaducidadGenerada(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ALERTACADUCIDADGENERADA));
			bean.setAlertaFinalGenerada(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ALERTAFINALGENERADA));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHAMODIFICACION));
			bean.setFechaInicialFase(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHAINICIALFASE));
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_USUMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDINSTITUCION));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_ANIOEXPEDIENTE));
			
			bean.setIdPersonaDenunciado(UtilidadesHash.getLong(hash, ExpDenunciadoBean.C_IDPERSONA));
			bean.setIdPersonaDenunciante(UtilidadesHash.getLong(hash, ExpDenuncianteBean.C_IDPERSONA));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
			bean.setIdInstitucion_tipoExpediente(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setEsVisible(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ESVISIBLE));
			bean.setEsVisibleEnFicha(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ESVISIBLEENFICHA));
			bean.setIdClasificacion(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDCLASIFICACION));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDFASE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDESTADO));
			bean.setIdArea(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDAREA));
			bean.setIdMateria(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDMATERIA));
			bean.setIdPretension(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDPRETENSION));
			bean.setOtrasPretensiones(UtilidadesHash.getString(hash, ExpExpedienteBean.C_OTRASPRETENSIONES));
			bean.setSancionado(UtilidadesHash.getString(hash, ExpExpedienteBean.C_SANCIONADO));
			bean.setAnotacionesCanceladas(UtilidadesHash.getString(hash, ExpExpedienteBean.C_ANOTACIONESCANCELADAS));
			
			bean.setFechaCaducidad(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHACADUCIDAD));
			bean.setFechaResolucion(UtilidadesHash.getString(hash, ExpExpedienteBean.C_FECHARESOLUCION));
			bean.setObservaciones(UtilidadesHash.getString(hash, ExpExpedienteBean.C_OBSERVACIONES));
			bean.setMinuta(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_MINUTA));
			bean.setImporteTotal(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_IMPORTETOTAL));
			bean.setPorcentajeIVA(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_PORCENTAJEIVA));
			bean.setPorcentajeIVAFinal(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_PORCENTAJEIVA));
			bean.setMinutaFinal(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_MINUTAFINAL));
			bean.setImporteTotalFinal(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_IMPORTETOTALFINAL));
			bean.setDerechosColegiales(UtilidadesHash.getDouble(hash, ExpExpedienteBean.C_DERECHOSCOLEGIALES));
			bean.setIdTipoIVA(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDTIPOIVA));
			bean.setIdResultadoJuntaGobierno(UtilidadesHash.getInteger(hash, ExpExpedienteBean.C_IDRESULTADOJUNTAGOBIERNO));
			
			bean.setIdentificadorDS(UtilidadesHash.getString(hash, ExpExpedienteBean.C_IDENTIFICADORDS));
			bean.setIdDireccion(UtilidadesHash.getString(hash, ExpExpedienteBean.C_IDDIRECCION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpExpedienteBean b = (ExpExpedienteBean) bean;

			 if(b.getTipoExpDisciplinario()==null){
				 UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO, b.getAnioExpDisciplinario());
				UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEXPDISCIPLINARIO, b.getNumExpDisciplinario());
			 }else{
				 UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEJG, b.getAnioExpDisciplinario());
				 UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEJG, b.getTipoExpDisciplinario());
				 UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEROEJG, b.getNumExpDisciplinario());
			 }
			UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ASUNTO, b.getAsunto());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_JUZGADO, b.getJuzgado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_PROCEDIMIENTO, b.getProcedimiento());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION_JUZGADO, b.getIdInstitucionJuzgado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO, b.getIdInstitucionProcedimiento());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMASUNTO, b.getNumAsunto());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAINICIALESTADO, b.getFechaInicialEstado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAFINALESTADO, b.getFechaFinalEstado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAPRORROGAESTADO, b.getFechaProrrogaEstado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_DESCRIPCIONRESOLUCION, b.getDescripcionResolucion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_SANCIONPRESCRITA, b.getSancionPrescrita());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ACTUACIONESPRESCRITAS, b.getActuacionesPrescritas());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_SANCIONFINALIZADA, b.getSancionFinalizada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTAGENERADA, b.getAlertaGenerada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTAGENERADACAD, b.getAlertaGeneradaCad());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTAFASEGENERADA, b.getAlertaFaseGenerada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTAFINALGENERADA, b.getAlertaFinalGenerada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTACADUCIDADGENERADA, b.getAlertaCaducidadGenerada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_USUMODIFICACION, b.getUsuModificacion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDPERSONA, b.getIdPersonaDenunciante());
			UtilidadesHash.set(htData, ExpDenunciadoBean.C_IDPERSONA, b.getIdPersonaDenunciado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_tipoExpediente());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ESVISIBLE, b.getEsVisible());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ESVISIBLEENFICHA, b.getEsVisibleEnFicha());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDCLASIFICACION, b.getIdClasificacion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDESTADO, b.getIdEstado());	
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDAREA, b.getIdArea());	
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDMATERIA, b.getIdMateria());	
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDPRETENSION, b.getIdPretension());	
			UtilidadesHash.set(htData, ExpExpedienteBean.C_OTRASPRETENSIONES, b.getOtrasPretensiones());	
			UtilidadesHash.set(htData, ExpExpedienteBean.C_SANCIONADO, b.getSancionado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ANOTACIONESCANCELADAS, b.getAnotacionesCanceladas());
			
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHACADUCIDAD, b.getFechaCaducidad());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAINICIALFASE, b.getFechaInicialFase());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHARESOLUCION, b.getFechaResolucion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_MINUTA, b.getMinuta());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IMPORTETOTAL, b.getImporteTotal());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_PORCENTAJEIVA, b.getPorcentajeIVA());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_PORCENTAJEIVA, b.getPorcentajeIVAFinal());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_MINUTAFINAL, b.getMinutaFinal());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IMPORTETOTALFINAL, b.getImporteTotalFinal());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_DERECHOSCOLEGIALES, b.getDerechosColegiales());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOIVA, b.getIdTipoIVA());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDRESULTADOJUNTAGOBIERNO, b.getIdResultadoJuntaGobierno());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDENTIFICADORDS, b.getIdentificadorDS());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDDIRECCION, b.getIdDireccion());

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	
	
	
	public Paginador getPaginadorAvanzadoExpedientes(BusquedaExpedientesForm form,UsrBean userBean) throws ClsExceptions 
	{
		//Hashtable codigosBind = new Hashtable();
		//int contador=0;
		//NOMBRES COLUMNAS PARA LA JOIN
		//Tabla cen_persona
		//NOMBRES COLUMNAS PARA LA JOIN
		//Tabla cen_persona

		//Tabla exp_tipoexpediente
		String T_IDINSTITUCION="T."+ExpTipoExpedienteBean.C_IDINSTITUCION;
		String T_IDTIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;

		//Tabla cen_institucion
		String I_IDINSTITUCION="I."+CenInstitucionBean.C_IDINSTITUCION;


		//Tabla exp_parte
		String PA_IDINSTITUCION="PA."+ExpPartesBean.C_IDINSTITUCION;
		String PA_IDINSTITUCION_TIPOEXPEDIENTE="PA."+ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
		String PA_IDTIPOEXPEDIENTE="PA."+ExpPartesBean.C_IDTIPOEXPEDIENTE;
		String PA_NUMEROEXPEDIENTE="PA."+ExpPartesBean.C_NUMEROEXPEDIENTE;
		String PA_ANIOEXPEDIENTE="PA."+ExpPartesBean.C_ANIOEXPEDIENTE;
		String PA_IDROL="PA."+ExpPartesBean.C_IDROL;
		String PA_IDPERSONA="PA."+ExpPartesBean.C_IDPERSONA;

		//Valores recogidos del formulario para la búsqueda

		String comboTipoExp = form.getComboTipoExpediente();
		String comboFases = form.getComboFases();
		String comboEstados = form.getComboEstados();
		String comboMaterias = form.getComboMaterias();
		String comboJuzgado = form .getComboJuzgados();

		//getComboTipoExpediente nos está devolviendo (idinstitucion,idtipoexpediente)
		String idinstitucion_tipoexpediente = "";
		if (comboTipoExp!=null && !comboTipoExp.equals("")){
			StringTokenizer st = new StringTokenizer(comboTipoExp,",");
			idinstitucion_tipoexpediente=st.nextToken();
			form.setTipoExpediente(st.nextToken());        	
		}else{
			idinstitucion_tipoexpediente="";
			form.setTipoExpediente("");  
		}

		//getComboFases nos está devolviendo (idinstitucion,idtipoexpediente,idfase)        
		if (comboFases!=null && !comboFases.equals("")){
			StringTokenizer st = new StringTokenizer(comboFases,",");
			st.nextToken();//idinstitucion_tipoexpediente
			st.nextToken();//idtipoexpediente
			form.setFase(st.nextToken());        	
		}else{        	
			form.setFase("");  
		}

		//getComboEstados nos está devolviendo (idinstitucion,idtipoexpediente,idfase,idestado)        
		if (comboEstados!=null && !comboEstados.equals("")){
			StringTokenizer st = new StringTokenizer(comboEstados,",");
			st.nextToken();//idinstitucion_tipoexpediente
			st.nextToken();//idtipoexpediente
			st.nextToken();//idfase
			form.setEstado(st.nextToken());        	
		}else{        	
			form.setEstado("");  
		}

		//getComboMaterias nos devuelve idInstitucion, idArea y, por supuesto, idMateria
		if (comboMaterias!=null && !comboMaterias.equals("")){
			StringTokenizer st = new StringTokenizer(comboMaterias,",");
			st.nextToken();//idinstitucion
			st.nextToken();//idArea
			form.setMateriaSel(st.nextToken());        	
		}else{        	
			form.setMateriaSel("");  
		}		
		
		//getComboMaterias nos devuelve en este orden idjuzgado y el idinstitucion
		if (comboJuzgado!=null && !comboJuzgado.equals("")){
			StringTokenizer st = new StringTokenizer(comboJuzgado,",");
			form.setComboJuzgados(st.nextToken());        	
		}else{        	
			form.setComboJuzgados("");  
		}	
		
		String tipoExpediente = form.getTipoExpediente();
		String institucion = form.getInstitucion();
		String numeroExpediente = form.getNumeroExpediente();
		String anioExpediente = form.getAnioExpediente();
		String numeroExpDisciplinario = form.getNumeroExpDisciplinario();
		String anioExpDisciplinario = form.getAnioExpDisciplinario();
		String fecha = "", fechaHasta = "";
		if (!form.getFecha().equals(""))
			fecha = GstDate.getApplicationFormatDate("", form.getFecha()); 
		if (!form.getFechaHasta().equals(""))
			fechaHasta = GstDate.getApplicationFormatDate("", form.getFechaHasta());
		String nombreDenunciado = form.getNombre();
		String ap1Denunciado = form.getPrimerApellido();
		String ap2Denunciado = form.getSegundoApellido();
		String nombreDenunciante = form.getNombreDenunciante();
		String ap1Denunciante = form.getPrimerApellidoDenunciante();
		String ap2Denunciante = form.getSegundoApellidoDenunciante();
		String asunto = form.getAsunto();
		String fase = form.getFase();
		String estado = form.getEstado();
		
		String nombreParte = form.getNombreParte();
		String ap1Parte = form.getPrimerApellidoParte();
		String ap2Parte = form.getSegundoApellidoParte();
		String observaciones = form.getObservaciones();
		String rol = form.getRol();
//		String numAsunto = form.getNumAsunto();
//		String idMateria = null; 
//		String idArea = null;
//		if(form.getIdMateria()!=null && !form.getIdMateria().equals("")){
//			
//			idMateria = form.getIdMateria();
//			idArea =form.getIdArea();
//		}
//		String idJuzgado = null;
//		String idInstJuzgado = null;
//		if(form.getJuzgado()!=null && !form.getJuzgado().equals("")){
//			idJuzgado = form.getJuzgado();
//			idInstJuzgado = form.getIdInstJuzgado();
//		}
//		
//		String otrasPretensiones = form.getOtrasPretensiones();
//		String idPretension = form.getIdPretension();
//		String idProcedimiento = form.getIdProcedimiento();
//		String idInstProcedimiento = form.getIdInstProcedimiento();
		
		boolean hayPartes = ((nombreParte!=null &&!nombreParte.equals("")) || (ap1Parte!=null && !ap1Parte.equals(""))
				|| (ap2Parte!=null && !ap2Parte.equals("")) || (rol!=null && !rol.equals("")));
		boolean esGeneral = form.getEsGeneral()!=null && form.getEsGeneral().equals("S")?true:false;

		//POR SI SE NECESITA:
		String instMenorRango = "SELECT "+CenInstitucionBean.C_IDINSTITUCION+" FROM "+CenInstitucionBean.T_NOMBRETABLA+" CONNECT BY PRIOR "+CenInstitucionBean.C_IDINSTITUCION+" = "+CenInstitucionBean.C_CEN_INST_IDINSTITUCION+" START WITH "+CenInstitucionBean.C_IDINSTITUCION+" = "+userBean.getLocation();

		String where = "WHERE ";

		//join de las tablas EXPEDIENTE E, TIPOEXPEDIENTE T, PERSONA P, ESTADOS ES, INSTITUCION I
		where += (institucion!=null && !institucion.equals("")) ? " E." + ExpExpedienteBean.C_IDINSTITUCION + " = " + institucion : " E." + ExpExpedienteBean.C_IDINSTITUCION + " IN (" +instMenorRango+ ")";
		//BEGIN BNS
		/*
		where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+T_IDINSTITUCION+"(+)";
		where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = "+T_IDTIPOEXPEDIENTE+"(+)";

		where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = "+I_IDINSTITUCION+"(+)";
		*/
		//END BNS
		
		if (esGeneral){
			where += " AND T."+ExpTipoExpedienteBean.C_ESGENERAL+"='S'";
		}
		
		if (hayPartes){
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = "+PA_IDINSTITUCION+"(+)";
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+PA_IDINSTITUCION_TIPOEXPEDIENTE+"(+)";
			where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = "+PA_IDTIPOEXPEDIENTE+"(+)";
			where += " AND E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+" = "+PA_NUMEROEXPEDIENTE+"(+)";
			where += " AND E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" = "+PA_ANIOEXPEDIENTE+"(+)";
		}
		where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = EJG."+ScsEJGBean.C_IDINSTITUCION+"(+) "+
				 " AND E."+ExpExpedienteBean.C_ANIOEJG+" = EJG."+ScsEJGBean.C_ANIO+"(+) "+
				 " AND E."+ExpExpedienteBean.C_NUMEROEJG+" = EJG."+ScsEJGBean.C_NUMERO+"(+) "+
				 " AND E."+ExpExpedienteBean.C_IDTIPOEJG+" = EJG."+ScsEJGBean.C_IDTIPOEJG+"(+) "+
				 " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = D."+ExpDenunciadoBean.C_IDINSTITUCION+"(+) "+
				 " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = D."+ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE+"(+) "+
				 " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = D."+ExpDenunciadoBean.C_IDTIPOEXPEDIENTE+"(+) "+
				 " AND E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+" = D."+ExpDenunciadoBean.C_NUMEROEXPEDIENTE+"(+) "+
				 " AND E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" = D."+ExpDenunciadoBean.C_ANIOEXPEDIENTE+"(+) "+
				 " AND 1 = D."+ExpDenunciadoBean.C_IDDENUNCIADO+"(+) "+
				 " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDINSTITUCION+
			     " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE+
			     " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = I."+CenInstitucionBean.C_IDINSTITUCION+
			     " AND E."+ExpExpedienteBean.C_IDFASE+" = F."+ExpFasesBean.C_IDFASE+"(+) "+
			     " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = F."+ExpFasesBean.C_IDINSTITUCION+"(+) "+
			     " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = F."+ExpFasesBean.C_IDTIPOEXPEDIENTE+"(+) "+
			     " AND E."+ExpExpedienteBean.C_IDFASE+" = ES."+ExpEstadosBean.C_IDFASE+"(+) "+
			     " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = ES."+ExpEstadosBean.C_IDINSTITUCION+"(+) "+
			     " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = ES."+ExpEstadosBean.C_IDTIPOEXPEDIENTE+"(+) "+
			     " AND E."+ExpExpedienteBean.C_IDESTADO+" = ES."+ExpEstadosBean.C_IDESTADO+"(+) ";
		//where += (form.getJuzgado()!=null && !form.getJuzgado().equals("")) ? " AND E."+ExpExpedienteBean.C_IDINSTITUCION_JUZGADO+" = juz." + ScsJuzgadoBean.C_IDINSTITUCION+"(+)": "";

		//campos de búsqueda
		where += (idinstitucion_tipoexpediente!=null && !idinstitucion_tipoexpediente.equals("")) ? " AND E." + ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = " + idinstitucion_tipoexpediente : "";
		where += (tipoExpediente!=null && !tipoExpediente.equals("")) ? " AND E." + ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = " + tipoExpediente : "";


		where += (numeroExpediente!=null && !numeroExpediente.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(numeroExpediente.trim(),"E." + ExpExpedienteBean.C_NUMEROEXPEDIENTE ): "";


		where += (anioExpediente!=null && !anioExpediente.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(anioExpediente.trim(),"E." + ExpExpedienteBean.C_ANIOEXPEDIENTE): "";
		if(numeroExpDisciplinario!=null && !numeroExpDisciplinario.equals("")){
			where += " AND (";
			where +=ComodinBusquedas.prepararSentenciaCompleta(numeroExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_NUMEXPDISCIPLINARIO);
			where += " OR ";
			where +=ComodinBusquedas.prepararSentenciaCompleta(numeroExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_NUMEROEJG);
			where += " ) ";
		}
		if(anioExpDisciplinario!=null && !anioExpDisciplinario.equals("")){
			where += " AND (";
			where +=ComodinBusquedas.prepararSentenciaCompleta(anioExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO);
			where += " OR ";
			where +=ComodinBusquedas.prepararSentenciaCompleta(anioExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_ANIOEJG);
			where += " ) ";
		}
		
		//confirmar que el formato de la fecha es correcto cuando se hagan inserts        
		where += ((fecha!=null && !fecha.equals("")) || (fechaHasta!=null && !fechaHasta.equals(""))) ? " AND " + GstDate.dateBetweenDesdeAndHasta(ExpExpedienteBean.C_FECHA,fecha,fechaHasta) : "";
		where += (fase!=null && !fase.equals("")) ? " AND E." + ExpExpedienteBean.C_IDFASE + " = " + fase : "";
		where += (asunto!=null && !asunto.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(asunto.trim(),"E." + ExpExpedienteBean.C_ASUNTO): "";
		where += (observaciones!=null && !observaciones.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(observaciones.trim(),"E." + ExpExpedienteBean.C_OBSERVACIONES): "";
		where += (estado!=null && !estado.equals("")) ? " AND E." + ExpExpedienteBean.C_IDESTADO + " = " + estado : "";
		where += (form.getIdMateria()!=null && !form.getIdMateria().equals("")) ? " AND E." + ExpExpedienteBean.C_IDMATERIA + " = " + form.getIdMateria() : "";
		where += (form.getIdArea()!=null && !form.getIdArea().equals("")) ? " AND E." + ExpExpedienteBean.C_IDAREA + " = " + form.getIdArea() : "";
		//where += (form.getJuzgado()!=null && !form.getJuzgado().equals("")) ? " AND juz." + ScsJuzgadoBean.C_IDJUZGADO + " = " + form.getJuzgado()+" AND juz." + ScsJuzgadoBean.C_IDINSTITUCION + " = " + form.getIdInstJuzgado() : "";
		where += (form.getJuzgado()!=null && !form.getJuzgado().equals("")) ? " AND e." + ExpExpedienteBean.C_JUZGADO + " = " + form.getJuzgado()+" AND e." + ExpExpedienteBean.C_IDINSTITUCION_JUZGADO + " = " + form.getIdInstJuzgado() : "";
		where += (form.getNumAsunto()!=null && !form.getNumAsunto().equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(form.getNumAsunto().trim(),"E." + ExpExpedienteBean.C_NUMASUNTO): "";
		where += (form.getOtrasPretensiones()!=null && !form.getOtrasPretensiones().equals("")) ? " AND E." + ExpExpedienteBean.C_OTRASPRETENSIONES + " = '" + form.getOtrasPretensiones()+"'" : "";
		where += (form.getIdPretension()!=null && !form.getIdPretension().equals("")) ? " AND E." + ExpExpedienteBean.C_IDPRETENSION + " = " + form.getIdPretension() : "";
		where += (form.getIdProcedimiento()!=null && !form.getIdProcedimiento().equals("")) ? " AND E." + ExpExpedienteBean.C_PROCEDIMIENTO + " = " + form.getIdProcedimiento()+ " AND E." + ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO + " = " + form.getIdInstProcedimiento() : "";

		if((nombreDenunciado!=null && !nombreDenunciado.equals(""))||(ap1Denunciado!=null && !ap1Denunciado.equals(""))||(ap2Denunciado!=null && !ap2Denunciado.equals(""))){
			StringBuffer sqlDenunciado = new StringBuffer();

			 	
			 String hay_nombre_denunciado = (nombreDenunciado!=null && !nombreDenunciado.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(nombreDenunciado.trim(),"per.nombre" ): "";
			 String hay_ap1_denunciado = (ap1Denunciado!=null && !ap1Denunciado.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(ap1Denunciado.trim(),"per.apellidos1" ): "";
			 String hay_ap2_denunciado = (ap2Denunciado!=null && !ap2Denunciado.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(ap2Denunciado.trim(),"per.apellidos2" ): "";
			 //BEGIN BNS
			 /*
			 sqlDenunciado.append(" AND (E.");
			 sqlDenunciado.append(ExpExpedienteBean.C_IDPERSONA);
			 sqlDenunciado.append(" IN (SELECT ");
			 sqlDenunciado.append(CenPersonaBean.C_IDPERSONA);
			 sqlDenunciado.append(" FROM ");
			 sqlDenunciado.append(CenPersonaBean.T_NOMBRETABLA);
			 sqlDenunciado.append(" per WHERE ");
			 sqlDenunciado.append(" 1=1 ");
			 sqlDenunciado.append((!hay_nombre_denunciado.equals("")) ? " and "+ hay_nombre_denunciado : "");
			 sqlDenunciado.append((!hay_ap1_denunciado.equals("")) ? " and "+hay_ap1_denunciado : "");
			 sqlDenunciado.append((!hay_ap2_denunciado.equals("")) ? " and "+hay_ap2_denunciado : "");
			 sqlDenunciado.append(" ) ");
			 
			 
			 sqlDenunciado.append(" or (select count(*) ");
			 */
			 //END BNS
			 sqlDenunciado.append(" and (select count(*) ");
			 sqlDenunciado.append(" from exp_denunciado ddo,cen_persona per ");
			 sqlDenunciado.append(" where ddo.IDINSTITUCION = E.IDINSTITUCION ");
			 sqlDenunciado.append(" and ddo.IDINSTITUCION_TIPOEXPEDIENTE =  E.IDINSTITUCION_TIPOEXPEDIENTE ");
			 sqlDenunciado.append(" and ddo.IDTIPOEXPEDIENTE = E.IDTIPOEXPEDIENTE ");
			 sqlDenunciado.append(" and ddo.NUMEROEXPEDIENTE = E.NUMEROEXPEDIENTE ");
			 sqlDenunciado.append(" and ddo.ANIOEXPEDIENTE = E.ANIOEXPEDIENTE ");
			 sqlDenunciado.append(" and ddo.idpersona = per.idpersona ");
			 
			 sqlDenunciado.append((!hay_nombre_denunciado.equals("")) ? " and "+ hay_nombre_denunciado : "");
			 sqlDenunciado.append((!hay_ap1_denunciado.equals("")) ? " and "+hay_ap1_denunciado : "");
			 sqlDenunciado.append((!hay_ap2_denunciado.equals("")) ? " and "+hay_ap2_denunciado : "");
			 
			 sqlDenunciado.append(" ) > 0) ");
			 where += sqlDenunciado.toString();
		}
		
		
		
		
		if((nombreDenunciante!=null && !nombreDenunciante.equals(""))||(ap1Denunciante!=null && !ap1Denunciante.equals(""))||(ap2Denunciante!=null && !ap2Denunciante.equals(""))){
			StringBuffer sqlDenunciante = new StringBuffer();
			 sqlDenunciante.append(" AND (select count(*) ");
			 sqlDenunciante.append(" from exp_denunciante den,cen_persona per ");
			 sqlDenunciante.append(" where den.IDINSTITUCION = E.IDINSTITUCION ");
			 sqlDenunciante.append(" and den.IDINSTITUCION_TIPOEXPEDIENTE =  E.IDINSTITUCION_TIPOEXPEDIENTE ");
			 sqlDenunciante.append(" and den.IDTIPOEXPEDIENTE = E.IDTIPOEXPEDIENTE ");
			 sqlDenunciante.append(" and den.NUMEROEXPEDIENTE = E.NUMEROEXPEDIENTE ");
			 sqlDenunciante.append(" and den.ANIOEXPEDIENTE = E.ANIOEXPEDIENTE ");
			 sqlDenunciante.append(" and den.idpersona = per.idpersona ");
			 	
			 String hay_nombre_denunciante = (nombreDenunciante!=null && !nombreDenunciante.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(nombreDenunciante.trim(),"per.nombre" ): "";
			 sqlDenunciante.append((!hay_nombre_denunciante.equals("")) ? " and "+ hay_nombre_denunciante : "");

			 String hay_ap1_denunciante = (ap1Denunciante!=null && !ap1Denunciante.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(ap1Denunciante.trim(),"per.apellidos1" ): "";
			 sqlDenunciante.append((!hay_ap1_denunciante.equals("")) ? " and "+hay_ap1_denunciante : "");
			 
			 String hay_ap2_denunciante = (ap2Denunciante!=null && !ap2Denunciante.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(ap2Denunciante.trim(),"per.apellidos2" ): "";
			 sqlDenunciante.append((!hay_ap2_denunciante.equals("")) ? " and "+hay_ap2_denunciante : "");
			 
			 sqlDenunciante.append(" ) > 0 ");
			 where += sqlDenunciante.toString();
		}
		
		
		
		
		
		
		if(form.getCampoConfigurado()!=null && !form.getCampoConfigurado().equals("")){
		where += " AND (select count(*) from exp_camposvalor cv where " +
				" cv.IDINSTITUCION = E.IDINSTITUCION	and	" +
				" cv.IDINSTITUCION_TIPOEXPEDIENTE = E.IDINSTITUCION_TIPOEXPEDIENTE" +
				" and cv.IDTIPOEXPEDIENTE = E.IDTIPOEXPEDIENTE" +
				" and cv.NUMEROEXPEDIENTE = E.NUMEROEXPEDIENTE" +
				" and cv.ANIOEXPEDIENTE = E.ANIOEXPEDIENTE and LOWER(valor)  like " +
				"'%" +
				form.getCampoConfigurado().toLowerCase()+
				"%')>0";
		
		}
		
		String hay_nombre_parte = (nombreParte!=null && !nombreParte.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(nombreParte.trim(),CenPersonaBean.C_NOMBRE ): "";

		String hay_ap1_parte = (ap1Parte!=null && !ap1Parte.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(ap1Parte.trim(),CenPersonaBean.C_APELLIDOS1 ): "";

		String hay_ap2_parte = (ap2Parte!=null && !ap2Parte.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(ap2Parte.trim(),CenPersonaBean.C_APELLIDOS2 ): "";
		where += (!hay_nombre_parte.equals("")) ? " AND "+PA_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_nombre_parte+")" : "";
		where += (!hay_ap1_parte.equals("")) ? " AND "+PA_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_ap1_parte+")" : "";
		where += (!hay_ap2_parte.equals("")) ? " AND "+PA_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_ap2_parte+")" : "";

		where += (rol!=null && !rol.equals("")) ? " AND " +PA_IDROL+ " = " + rol : "";

		if (esGeneral){
			//where += " AND T."+ExpTipoExpedienteBean.C_ESGENERAL+"='S'";
			where += " AND DECODE (E."+ExpExpedienteBean.C_IDINSTITUCION+",'"+userBean.getLocation()+"','S',E."+ExpExpedienteBean.C_ESVISIBLE+")='S'";
		}

		//LMS 08/08/2006
		//Se añade el control de permisos sobre el tipo de expediente.

		String[] aPerfiles = userBean.getProfile();
		where += " and (SELECT max(DERECHOACCESO)" +
				"        FROM EXP_PERMISOSTIPOSEXPEDIENTES pe" +
				"       WHERE pe.IDINSTITUCION = E.idinstitucion" +
				"         AND pe.IDPERFIL IN (";
				for (int i=0; i<aPerfiles.length; i++)
		{
					where += "'" + aPerfiles[i] + "',";
		}

		where = where.substring(0,where.length()-1);
		
		where +=")         AND pe.IDTIPOeXPEDIENTE = E.idtipoexpediente) is not null" ;
		
		
		
		//Tabla exp_tipoexpediente
		String T_NOMBRETIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_NOMBRE+" AS NOMBRETIPOEXPEDIENTE";
		
		//Tabla cen_institucion
		String I_ABREVIATURA = "I."+CenInstitucionBean.C_ABREVIATURA;
		
		//JOINS ESPECIALES
		//BEGIN BNS
		//String SEL_DENUNCIADO = "( SELECT PE.NOMBRE||' '||PE.APELLIDOS1||' '||PE.APELLIDOS2 FROM CEN_PERSONA PE WHERE PE.IDPERSONA= E.IDPERSONA) AS DENUNCIADO";
		String SEL_DENUNCIADO = "( SELECT PE.NOMBRE||' '||PE.APELLIDOS1||' '||PE.APELLIDOS2 FROM CEN_PERSONA PE WHERE PE.IDPERSONA= D.IDPERSONA) AS DENUNCIADO";
		//END BNS
		
		// Acceso a BBDD
		
		try{
		 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT  ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    for(int i=0;i<fields.length; i++){		    	
		    		sql += "E."+fields[i]+", ";
			}
		    sql += "D."+ExpDenunciadoBean.C_IDPERSONA+", ";
		   // sql += T_IDINSTITUCION+", ";	
		    //sql += T_IDTIPOEXPEDIENTE+", ";	
		    sql += "F."+ExpFasesBean.C_NOMBRE+" NOMBREFASE, ";
		    sql += T_NOMBRETIPOEXPEDIENTE+", ";
		    sql += SEL_DENUNCIADO+", ";
		    //sql += SEL_PARTE+", ";
		    //sql += SEL_IDROL+", ";
		    //sql += SEL_NOMBREROL+", ";
		    sql += I_ABREVIATURA+", ";
		    //sql += "nvl2((select decode(nvl(ejg."+ScsEJGBean.C_ANIO+",'')||'/'||nvl(ejg."+ScsEJGBean.C_NUMEJG+",''),'/',null,ejg."+ScsEJGBean.C_ANIO+"||'/'||ejg."+ScsEJGBean.C_NUMEJG+") from "+ScsEJGBean.T_NOMBRETABLA+" ejg where ejg."+ScsEJGBean.C_IDINSTITUCION+"=E."+ExpExpedienteBean.C_IDINSTITUCION+" AND ejg."+ScsEJGBean.C_ANIO+" =E."+ExpExpedienteBean.C_ANIOEJG+" AND ejg."+ScsEJGBean.C_NUMERO+" = E."+ExpExpedienteBean.C_NUMEROEJG+" AND ejg."+ScsEJGBean.C_IDTIPOEJG+" = e."+ExpExpedienteBean.C_IDTIPOEJG+")"++" EXPRELACIONADO,";
		    sql += " case when T."+ExpTipoExpedienteBean.C_RELACIONEJG+" != 0 then decode(nvl(ejg."+ScsEJGBean.C_ANIO+",'')||'/'||nvl(ejg."+ScsEJGBean.C_NUMEJG+",''),'/','',ejg."+ScsEJGBean.C_ANIO+"||'/'||ejg."+ScsEJGBean.C_NUMEJG+") else decode(nvl(E."+ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO+",'')||'/'||nvl(E."+ExpExpedienteBean.C_NUMEXPDISCIPLINARIO+",''),'/','',E."+ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO+"||'/'||E."+ExpExpedienteBean.C_NUMEXPDISCIPLINARIO+") end EXPRELACIONADO, ";
//		    PDM INC-3319: Se añade como campo de salida el nombre del estado de expediente
		    sql += "es. "+ExpEstadosBean.C_NOMBRE+" NOMBREESTADO";
		    /*
		    sql += "(select ex. "+ExpEstadosBean.C_NOMBRE+
		    	   " from "+ExpEstadosBean.T_NOMBRETABLA+" ex "+
				   " where "+ ExpEstadosBean.C_IDINSTITUCION+" = E.idinstitucion"+
				   "  and "+ExpEstadosBean.C_IDFASE+" = E.idfase"+
				   "  and "+ExpEstadosBean.C_IDESTADO+" = E.idestado"+
				   "  and "+ExpEstadosBean.C_IDTIPOEXPEDIENTE+" = T.idtipoexpediente) NOMBREESTADO";
		    */
		    //LMS 08/08/2006
			//Se añade la relación con la tabla de permisos de expedientes.
			sql += " FROM ";
			//BEGIN BNS			
		    //sql += ExpExpedienteBean.T_NOMBRETABLA+" E, "+CenInstitucionBean.T_NOMBRETABLA+" I, "+ExpTipoExpedienteBean.T_NOMBRETABLA+" T " ;//+ExpPermisosTiposExpedientesBean.T_NOMBRETABLA+" X";
			sql += ExpExpedienteBean.T_NOMBRETABLA+" E,"+ScsEJGBean.T_NOMBRETABLA+" EJG, "+ExpDenunciadoBean.T_NOMBRETABLA+" D, "+ExpTipoExpedienteBean.T_NOMBRETABLA+" T, "+CenInstitucionBean.T_NOMBRETABLA+" I," + ExpFasesBean.T_NOMBRETABLA+" F, " +ExpEstadosBean.T_NOMBRETABLA+" ES";
					
			//END BNS
		    //sql += (form.getJuzgado()!=null && !form.getJuzgado().equals("")) ? ", " + ScsJuzgadoBean.T_NOMBRETABLA + " juz" : "";
		    
		    if (hayPartes){
		    	sql +=", "+ExpPartesBean.T_NOMBRETABLA+" PA";
		    }
		    		    		
			sql += " " + where;
			String ascOdesc = "ASC";
			switch (Integer.parseInt(form.getTipoOrden())) 
			{
				case 1:
					ascOdesc = "ASC";
				break;
				case 2:
					ascOdesc = "DESC";
					break;

			}
			
			//MHG INC_10329_SIGA Se añade a todos los order by la ordenación por fecha y número de expediente descendente.
			switch (Integer.parseInt(form.getOrden())) 
			{
				case 1:
					sql += " ORDER BY E."+ExpExpedienteBean.C_IDINSTITUCION+",E."+ExpExpedienteBean.C_FECHA+" "+ascOdesc+" ,NOMBRETIPOEXPEDIENTE";
				break;
				case 2:
					sql += " ORDER BY E."+ExpExpedienteBean.C_IDINSTITUCION+",DENUNCIADO  "+ascOdesc+" ,NOMBRETIPOEXPEDIENTE";
					break;
				case 3:
					sql += " ORDER BY E."+ExpExpedienteBean.C_IDINSTITUCION;
					break;
					

			default:
				sql += " ORDER BY E."+ExpExpedienteBean.C_IDINSTITUCION+",NOMBRETIPOEXPEDIENTE";
			}
			//BEGIN BNS
			sql += ",E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" desc,E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+" desc";
			//END BNS

			Paginador paginador = new Paginador(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
	       	
			
			
			return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
	}
	
	public Vector selectBusqExp(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//NOMBRES COLUMNAS PARA LA JOIN
		//Tabla cen_persona
		String P_NOMBREPERSONA="P."+CenPersonaBean.C_NOMBRE+" AS NOMBREPERSONA";
		String P_APELLIDOS1="P."+CenPersonaBean.C_APELLIDOS1;
		String P_APELLIDOS2="P."+CenPersonaBean.C_APELLIDOS2;
		String P_IDPERSONA="P."+CenPersonaBean.C_IDPERSONA;
		
		//Tabla exp_tipoexpediente
		String P_IDINSTITUCION="T."+ExpTipoExpedienteBean.C_IDINSTITUCION;
		String P_IDTIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;
		String P_NOMBRETIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_NOMBRE+" AS NOMBRETIPOEXPEDIENTE";
		
		//Tabla cen_institucion
		String I_ABREVIATURA = "I."+CenInstitucionBean.C_ABREVIATURA;
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT distinct ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    for(int i=0;i<fields.length; i++){
			  sql += "E."+fields[i]+", ";
			}
		    
		    sql += P_NOMBREPERSONA+", ";	
		    sql += P_APELLIDOS1+", ";	
		    sql += P_APELLIDOS2+", ";	
		    sql += P_IDPERSONA+", ";	
		    sql += P_IDINSTITUCION+", ";	
		    sql += P_IDTIPOEXPEDIENTE+", ";	
		    sql += P_NOMBRETIPOEXPEDIENTE+", ";	
		    sql += I_ABREVIATURA+", ";
		  //PDM INC-3319: Se añade como campo de salida el nombre del estado de expediente
		    sql += "(select ex.nombre "+
		    	   " from "+ExpEstadosBean.T_NOMBRETABLA+" ex "+
				   " where "+ ExpEstadosBean.C_IDINSTITUCION+" = E.idinstitucion"+
				   "  and "+ExpEstadosBean.C_IDFASE+" = E.idfase"+
				   "  and "+ExpEstadosBean.C_IDESTADO+" = E.idestado"+
				   "  and "+ExpEstadosBean.C_IDTIPOEXPEDIENTE+" = T.idtipoexpediente) NOMBREESTADO";
		    
		    
			//LMS 08/08/2006
			//Se añade la relación con la tabla de permisos de expedientes.
			sql += " FROM ";
		    sql += ExpExpedienteBean.T_NOMBRETABLA+" E, "+CenPersonaBean.T_NOMBRETABLA+" P, "+ExpTipoExpedienteBean.T_NOMBRETABLA+" T, "+CenInstitucionBean.T_NOMBRETABLA+" I, "+ExpPermisosTiposExpedientesBean.T_NOMBRETABLA+" X";
		    		    		
			sql += " " + where;
			sql += " ORDER BY "+I_ABREVIATURA+", E."+ExpExpedienteBean.C_IDINSTITUCION+", E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+", E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+", E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" DESC, E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE;		
			
			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	
	/** Funcion selectBusqExpAvda (String where, boolean hayPartes)
	 * Consulta sobre la tabla de expedientes, para búsqueda avanzada de expedientes
	 * @param where: criteros para filtrar el select 
	 * @param hayPartes: true si se ha introducido algún criterio de búsqueda relacionado con 'Otras Partes'.
	 * @return vector con los registros encontrados. 
	 * @exception  ClsExceptions  En cualquier caso de error 
	 * */
	public Vector selectBusqExpAvda(String where, boolean hayPartes) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//Tabla exp_tipoexpediente
		String T_IDINSTITUCION="T."+ExpTipoExpedienteBean.C_IDINSTITUCION;
		String T_IDTIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;
		String T_NOMBRETIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_NOMBRE+" AS NOMBRETIPOEXPEDIENTE";
		
		//Tabla cen_institucion
		String I_ABREVIATURA = "I."+CenInstitucionBean.C_ABREVIATURA;
		
		//JOINS ESPECIALES
		String SEL_DENUNCIADO = "( SELECT PE.NOMBRE||' '||PE.APELLIDOS1||' '||PE.APELLIDOS2 FROM CEN_PERSONA PE WHERE PE.IDPERSONA= E.IDPERSONA) AS DENUNCIADO";
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT DISTINCT ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    for(int i=0;i<fields.length; i++){
			  sql += "E."+fields[i]+", ";
			}
		    
		    sql += T_IDINSTITUCION+", ";	
		    sql += T_IDTIPOEXPEDIENTE+", ";	
		    sql += T_NOMBRETIPOEXPEDIENTE+", ";
		    sql += SEL_DENUNCIADO+", ";
		    //sql += SEL_PARTE+", ";
		    //sql += SEL_IDROL+", ";
		    //sql += SEL_NOMBREROL+", ";
		    sql += I_ABREVIATURA+", ";
//		    PDM INC-3319: Se añade como campo de salida el nombre del estado de expediente
		    sql += "(select ex.nombre "+
		    	   " from "+ExpEstadosBean.T_NOMBRETABLA+" ex "+
				   " where "+ ExpEstadosBean.C_IDINSTITUCION+" = E.idinstitucion"+
				   "  and "+ExpEstadosBean.C_IDFASE+" = E.idfase"+
				   "  and "+ExpEstadosBean.C_IDESTADO+" = E.idestado"+
				   "  and "+ExpEstadosBean.C_IDTIPOEXPEDIENTE+" = T.idtipoexpediente) NOMBREESTADO";
		    
		    //LMS 08/08/2006
			//Se añade la relación con la tabla de permisos de expedientes.
			sql += " FROM ";
		    sql += ExpExpedienteBean.T_NOMBRETABLA+" E, "+CenInstitucionBean.T_NOMBRETABLA+" I, "+ExpTipoExpedienteBean.T_NOMBRETABLA+" T, "+ExpPermisosTiposExpedientesBean.T_NOMBRETABLA+" X";
		    
		    if (hayPartes){
		    	sql +=", "+ExpPartesBean.T_NOMBRETABLA+" PA";
		    }
		    		    		
			sql += " " + where;
			sql += " ORDER BY "+I_ABREVIATURA+", E."+ExpExpedienteBean.C_IDINSTITUCION+", E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+", E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+", E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" DESC, E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE;

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ExpExpedienteAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	/*
	 * ;étodo utilizado para obtener el número de denunciantes / impugnantes de un determinado expediente
	 */	
	
	public int getNumeroDenunciantes (String where) throws ClsExceptions {
		
		int resultado = 0;
	
		RowsContainer rc = null;
		
		Hashtable contador = new Hashtable();
		
		String resultadoStr;
		
		
        String sql = "SELECT COUNT(*) AS NUMERO ";

		sql += " FROM ";
		sql += ExpDenuncianteBean.T_NOMBRETABLA+" DEN, "+CenPersonaBean.T_NOMBRETABLA+" PER";
		
		sql += " " + where;
		
		try{
			
			contador = (Hashtable)((Vector)this.selectGenerico(sql)).get(0);
			//devolverá true si el contador es = 0
			resultado = Integer.parseInt(contador.get("NUMERO").toString());
			
			
		return resultado;	
		} catch (Exception e) { 	
		throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
	}	
	

	/** Funcion selectDatosGenerales (String where)
	 * Consulta sobre la tabla de expedientes, para búsqueda de datos generales de un expediente
	 * @param criteros para filtrar el select, campo where 
	 * @return vector con los datos de un expediente  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * */
	public Vector selectDatosGenerales(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    for(int i=0;i<fields.length; i++){
			  sql += "E."+fields[i]+", ";
			}
		    
		    sql += "T."+ExpTipoExpedienteBean.C_RELACIONEJG+", ";	
		    sql += "T."+ExpTipoExpedienteBean.C_IDINSTITUCION+", ";	
		    sql += "T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE+", ";	
		    sql += "T."+ExpTipoExpedienteBean.C_NOMBRE+" AS NOMBRETIPOEXPEDIENTE, ";
		    sql += "I."+CenInstitucionBean.C_ABREVIATURA+" AS NOMBREINSTITUCION, ";
		    /* BNS QUITO LOS DATOS DE DENUNCIANTE YA QUE SE OBTIENEN EN OTRA QUERY
		    sql += "P."+CenPersonaBean.C_NOMBRE+" AS NOMBREPERSONA, ";
		    sql += "P."+CenPersonaBean.C_APELLIDOS1+", ";
		    sql += "P."+CenPersonaBean.C_APELLIDOS2+", ";
		    sql += "P."+CenPersonaBean.C_NIFCIF+" ";
		    if (numeroCount > 0) {
		    	sql += ", PER."+CenPersonaBean.C_NOMBRE+" AS NOMBREDENUNCIANTE, ";
		    	sql += "PER."+CenPersonaBean.C_IDPERSONA+" AS IDPERSONADENUNCIANTE, ";
		    	sql += "PER."+CenPersonaBean.C_APELLIDOS1+" AS APELLIDO1DENUNCIANTE, ";
		    	sql += "PER."+CenPersonaBean.C_APELLIDOS2+" AS APELLIDO2DENUNCIANTE, ";
		    	sql += "PER."+CenPersonaBean.C_NIFCIF+" AS NIFDENUNCIANTE, ";
		    	sql += "CDEN.NCOLEGIADO AS NCOLDENUNCIANTE";
		    }		    
		    sql += ", C."+CenColegiadoBean.C_NCOLEGIADO+" ";
		    */	
		    sql += "(select IDTIPOIVA||','|| replace(VALOR,',','.')  from pys_tipoiva where idtipoiva = E.idtipoiva) VALOR_IVA ";
		    
			sql += " FROM ";
			//sql += ExpExpedienteBean.T_NOMBRETABLA+" E, "+CenInstitucionBean.T_NOMBRETABLA+" I, "+CenColegiadoBean.T_NOMBRETABLA+" C, "+ExpTipoExpedienteBean.T_NOMBRETABLA+" T, "+CenPersonaBean.T_NOMBRETABLA+" P";
			sql += ExpExpedienteBean.T_NOMBRETABLA+" E, "+CenInstitucionBean.T_NOMBRETABLA+" I, "+ExpTipoExpedienteBean.T_NOMBRETABLA+" T";
			/*
			if (numeroCount > 0) {
				sql += ", "+ExpDenuncianteBean.T_NOMBRETABLA+" DEN, "+CenPersonaBean.T_NOMBRETABLA+" PER,"+CenColegiadoBean.T_NOMBRETABLA+" CDEN ";
			}
			*/
		    		    		
			sql += " " + where;
			sql += " ORDER BY E."+ExpExpedienteBean.C_IDINSTITUCION+", E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+", E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+", E."+ExpExpedienteBean.C_ANIOEXPEDIENTE;
			/*
			if (numeroCount > 0) {
				sql += ", DEN."+ExpDenuncianteBean.C_IDDENUNCIANTE;
			}
			*/

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	/** Funcion selectDatosGenerales (String where)
	 * Consulta sobre la tabla de expedientes, para búsqueda de datos generales de un expediente
	 * @param criteros para filtrar el select, campo where 
	 * @return vector con los datos de un expediente  
	 * */
	public Integer selectTipoExpedienteEJG(String idInstitucion) throws ClsExceptions 
	{
		Vector datos = new Vector();
		Integer tipoExpediente = null;
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_TIPOEXPEDIENTE = ExpTipoExpedienteBean.T_NOMBRETABLA + " T";
		String E_IDINSTITUCION = ExpTipoExpedienteBean.C_IDINSTITUCION;
		String RELACIONEJG = ExpTipoExpedienteBean.C_RELACIONEJG;
		String IDTIPOEXPEDIENTE = ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;
		//Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String sql = "SELECT "+ IDTIPOEXPEDIENTE ;
	        sql += " FROM ";
		    sql += 	T_EXP_TIPOEXPEDIENTE+" " ;
			sql += " WHERE ";
			sql += E_IDINSTITUCION + " = " + idInstitucion + " and "+ RELACIONEJG + " = 1";

			ClsLogging.writeFileLog("ExpExpedienteAdm, selectTipoExpedienteEJG() -> QUERY: " + sql,3);

			if (rc.query(sql)) {
				if (rc.size()==1)	{
					Row fila = (Row) rc.get(0);										
					tipoExpediente=new Integer(fila.getString("IDTIPOEXPEDIENTE"));					
				}
				else
					return null;
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return tipoExpediente;
	}
	
	public Vector selectExpedientesCliente(long idPersona, int idInstitucion) throws ClsExceptions {
		return selectExpedientesCliente(idPersona, idInstitucion, null);
	}
	public Vector selectExpedientesCliente(long idPersona, int idInstitucion, String sSancionado) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_EXPEDIENTE = ExpExpedienteBean.T_NOMBRETABLA;
		String T_EXP_TIPOEXPEDIENTE = ExpTipoExpedienteBean.T_NOMBRETABLA + " T";
		String T_EXP_ESTADOS = ExpEstadosBean.T_NOMBRETABLA + " ES";
		//mhg
		String T_EXP_DENUNCIADO = ExpDenunciadoBean.T_NOMBRETABLA + " D";
		
		//NOMBRES COLUMNAS PARA LA JOIN
		//Tabla exp_expediente 
		String E_IDINSTITUCION = ExpExpedienteBean.C_IDINSTITUCION;
		String E_IDTIPOEXPEDIENTE = ExpExpedienteBean.C_IDTIPOEXPEDIENTE;
		String E_SANCIONFINALIZADA = ExpExpedienteBean.C_SANCIONFINALIZADA;
		String E_NUMEROEXPEDIENTE = ExpExpedienteBean.C_NUMEROEXPEDIENTE;
		String E_ANIOEXPEDIENTE = ExpExpedienteBean.C_ANIOEXPEDIENTE;
		String E_ESVISIBLEENFICHA = ExpExpedienteBean.C_ESVISIBLEENFICHA;
		String E_IDINSTITUCION_TIPOEXPEDIENTE = ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
		String E_IDFASE = ExpExpedienteBean.C_IDFASE;
		String E_FECHAFINALESTADO = ExpExpedienteBean.C_FECHAFINALESTADO;
		String E_FECHAPRORROGAESTADO = ExpExpedienteBean.C_FECHAPRORROGAESTADO;
		String E_SANCIONADO = ExpExpedienteBean.C_SANCIONADO;
		String E_ASUNTO = ExpExpedienteBean.C_ASUNTO;
		String E_IDESTADO = ExpExpedienteBean.C_IDESTADO;
		String E_IDINSTITUCIONTIPOEXPEDIENTE = ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
		
		//Tabla exp_estados
		String ES_IDINSTITUCION = "ES." + ExpEstadosBean.C_IDINSTITUCION;
		String ES_IDTIPOEXPEDIENTE = "ES." + ExpEstadosBean.C_IDTIPOEXPEDIENTE;
		String ES_IDFASE = "ES." + ExpEstadosBean.C_IDFASE;
		String ES_ESEJECUCIONSANCION = "ES." + ExpEstadosBean.C_ESEJECUCIONSANCION;
		String ES_IDESTADO = "ES." + ExpEstadosBean.C_IDESTADO;
		
		//Tabla exp_tipoexpediente
		String T_IDINSTITUCION = "T." + ExpTipoExpedienteBean.C_IDINSTITUCION;
		String T_IDTIPOEXPEDIENTE = "T." + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;
		String T_TIPOEXPEDIENTE = "T." + ExpTipoExpedienteBean.C_NOMBRE;
		
		//Subselect
		String subselect = "(select decode(nvl((e2." + E_FECHAFINALESTADO + "-e2." + E_FECHAPRORROGAESTADO +
							")-abs(e2." + E_FECHAFINALESTADO + "-e2." + E_FECHAPRORROGAESTADO + 
							"),0),0,e2." + E_FECHAFINALESTADO + ",e2." + E_FECHAPRORROGAESTADO + ") from " + 
							T_EXP_EXPEDIENTE + " e2 where " +
								"e2." + E_SANCIONFINALIZADA + " IS NULL and " +
								"e2." + E_IDINSTITUCION + " = e." + E_IDINSTITUCION + " and " +
								"e2." + E_IDTIPOEXPEDIENTE + " = e." + E_IDTIPOEXPEDIENTE + " and " +
								"e2." + E_NUMEROEXPEDIENTE + " = e." + E_NUMEROEXPEDIENTE + " and " +
								"e2." + E_ANIOEXPEDIENTE + " = e." + E_ANIOEXPEDIENTE + " and " +
								ES_ESEJECUCIONSANCION + " ='S' and " +
							    " e2."+E_IDINSTITUCIONTIPOEXPEDIENTE+" = e."+ ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+") as FECHAFINAL";
		 	
		//Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String sql = "SELECT ";
	        
	        sql += "e." + E_IDINSTITUCION + ", ";
	        sql += "e." + E_IDINSTITUCION_TIPOEXPEDIENTE + ", ";
	        sql += "e." + E_IDTIPOEXPEDIENTE + ", ";
	        sql += "e." + E_ANIOEXPEDIENTE + ", ";
	        sql += "e." + E_NUMEROEXPEDIENTE + ", ";
	        sql += E_SANCIONADO + ", ";
	        sql += E_SANCIONFINALIZADA + ", ";		    
		    sql += T_TIPOEXPEDIENTE + " as TIPOEXPEDIENTE, ";	
		    sql += E_ASUNTO + ", ";
		    sql += ES_ESEJECUCIONSANCION + " as EJECUCIONSANCION, ";
		    sql += subselect;	
		    
		    
		    //mhg Incidencia 179. Nueva query. Ahora la tabla principal es exp_denunciado y se han introducido Left Join
		    sql += " FROM ";
		    sql += T_EXP_DENUNCIADO + ", "+ T_EXP_EXPEDIENTE + " e, " + T_EXP_TIPOEXPEDIENTE+", " + T_EXP_ESTADOS;
		    sql += " WHERE ";
		    sql += "d." + ExpDenunciadoBean.C_IDINSTITUCION + "= e." + E_IDINSTITUCION + "(+) and ";
		    sql += "d." + ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE + "= e." + E_IDINSTITUCION_TIPOEXPEDIENTE + "(+) and ";
		    sql += "d." + ExpDenunciadoBean.C_NUMEROEXPEDIENTE + "= e." + E_NUMEROEXPEDIENTE + "(+) and ";
		    sql += "d." + ExpDenunciadoBean.C_ANIOEXPEDIENTE + "= e." + E_ANIOEXPEDIENTE + "(+) and ";
		    
		    sql += "e." + E_IDINSTITUCION + " = " + "t." + ExpTipoExpedienteBean.C_IDINSTITUCION + " (+) and ";
		    sql += "e." + E_IDTIPOEXPEDIENTE + " = " + "t." + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE + " (+) and ";
		    
		    sql += "e." + E_IDINSTITUCION + " = " + "es." + ExpEstadosBean.C_IDINSTITUCION + " (+) and ";
		    sql += "e." + E_IDTIPOEXPEDIENTE + " = " + "es." + ExpEstadosBean.C_IDTIPOEXPEDIENTE + " (+) and ";
		    sql += "e." + E_IDFASE + " = " + "es." + ExpEstadosBean.C_IDFASE + " (+) and ";
		    sql += "e." + E_IDESTADO + " = " + "es." + ExpEstadosBean.C_IDESTADO + " (+) and ";
		    
		    sql += "d." + E_IDINSTITUCION + " = " + idInstitucion + " and ";
		    sql += "d." + ExpDenunciadoBean.C_IDPERSONA + " = " + idPersona + " and ";
		    if (sSancionado != null)
		    	sql += "e." + ExpExpedienteBean.C_SANCIONADO + " = '"+sSancionado+"' and ";
			sql += "e." + E_ESVISIBLEENFICHA + " = 'S' ";
		    
		   		
			//Query antigua
		    /*sql += T_EXP_EXPEDIENTE + " e, " + 
		    		T_EXP_TIPOEXPEDIENTE+", " + 
		    		T_EXP_ESTADOS;
		    		    		
			sql += " WHERE ";
			sql += "e." + E_IDINSTITUCION + " = " + idInstitucion + " and ";
			sql += "e." + E_IDPERSONA + " = " + idPersona + " and ";
			sql += "e." + E_ESVISIBLEENFICHA + " = 'S' and ";
			sql += "e." + E_IDINSTITUCION + " = " + T_IDINSTITUCION + " and ";
			sql += "e." + E_IDTIPOEXPEDIENTE + " = " + T_IDTIPOEXPEDIENTE + " and ";
			sql += "e." + E_IDINSTITUCION_TIPOEXPEDIENTE + " = " + ES_IDINSTITUCION + " and ";
			sql += "e." + E_IDTIPOEXPEDIENTE + " = " + ES_IDTIPOEXPEDIENTE + " and ";
			sql += "e." + E_IDFASE + " = " + ES_IDFASE + "(+) and ";
			sql += "e." + E_IDESTADO + " = " + ES_IDESTADO + "(+)";
			*/
			ClsLogging.writeFileLog("ExpExpedienteAdm, selectExpedientesCliente() -> QUERY: " + sql,3);
	        
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;	
	}

	/** Funcion getNewNumAnioExpediente (Hashtable hash)
	 * Genera el numero y el anho de un nuevo expediente
	 * @param hash con la clave primaria sin el numero y el anio expediente
	 * @param anioFechaActual año con la fecha introducida por el usuario
	 * @return Hashtable con numero(Integer) y anho de expediente(Integer)
	 * */
	public Hashtable getNewNumAnioExpediente(Hashtable hash,String anioFechaActual) throws ClsExceptions 
	{		
		Hashtable resultado = new Hashtable();
		int nuevoNumExp = 1;
		Calendar fecha = Calendar.getInstance();		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpExpedienteBean.C_NUMEROEXPEDIENTE+") AS ULTIMOEXPEDIENTE ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    
		    
			sql += " FROM "+ExpExpedienteBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpExpedienteBean.C_IDINSTITUCION+" = "+hash.get(ExpExpedienteBean.C_IDINSTITUCION)+" AND ";
			sql += ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+" AND ";
			sql += ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = "+hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)+" AND ";
			sql += ExpExpedienteBean.C_ANIOEXPEDIENTE+" = "+anioFechaActual;
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMOEXPEDIENTE")).equals("")) {
					Integer ultimoNumExpInt = Integer.valueOf((String)htRow.get("ULTIMOEXPEDIENTE"));
					int ultimoNumExp=ultimoNumExpInt.intValue();
					ultimoNumExp++;
					nuevoNumExp = ultimoNumExp;					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		resultado.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,new Integer(nuevoNumExp));
		resultado.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,new Integer(anioFechaActual));
		return resultado;
	}
	
	/**
	 * Obtiene el bean de expediente a partir de los campos de clave
	 * @param String idTipoExpediente
	 * @param String idInstitucion
	 * @param String idInstitucionTipoExpediente
	 * @param String anioExpediente
	 * @param String numeroExpediente
	 */
	public ExpExpedienteBean getExpediente (String idTipoExpediente, String idInstitucion, String idInstitucionTipoExpediente, String anioExpediente, String numeroExpediente) throws ClsExceptions 
	{
		try {
		    Hashtable pk = new Hashtable();
		    pk.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
		    pk.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
		    pk.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucionTipoExpediente);
		    pk.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
		    pk.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numeroExpediente);		    
		    Vector vBean = this.selectByPK(pk);			
			return (ExpExpedienteBean)vBean.firstElement();
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar datos en B.D.");
		}
	}
	
	/**
	 * Obtiene datos de expediente y persona a partir de los campos clave de expediente
	 * @param String idTipoExpediente
	 * @param String idInstitucion
	 * @param String idInstitucionTipoExpediente
	 * @param String anioExpediente
	 * @param String numeroExpediente
	 */
	public Hashtable getExpedienteCenso (String idTipoExpediente, String idInstitucion, String idInstitucionTipoExpediente, String anioExpediente, String numeroExpediente) throws ClsExceptions 
	{
		Row fila = null;
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_EXPEDIENTES = ExpExpedienteBean.T_NOMBRETABLA + " E";
		String T_EXP_TIPOEXPEDIENTES = ExpTipoExpedienteBean.T_NOMBRETABLA + " T";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " C";
		String T_EXP_DENUNCIADOS = ExpDenunciadoBean.T_NOMBRETABLA + " D";
		
		//NOMBRES COLUMNAS PARA LA JOIN
		
		//Tabla cen_persona
		String C_NOMBRE="C." + CenPersonaBean.C_NOMBRE;
		String C_APELLIDOS1="C." + CenPersonaBean.C_APELLIDOS1;
		String C_APELLIDOS2="C." + CenPersonaBean.C_APELLIDOS2;
		String C_NIFCIF="C." + CenPersonaBean.C_NIFCIF;
		String C_IDPERSONA="C." + CenPersonaBean.C_IDPERSONA;
		
		//Tabla exp_expedientes
		String E_SANCIONFINALIZADA = "E." + ExpExpedienteBean.C_SANCIONFINALIZADA;
		String E_ANIOEXPEDIENTE = "E." + ExpExpedienteBean.C_ANIOEXPEDIENTE;
		String E_NUMEROEXPEDIENTE = "E." + ExpExpedienteBean.C_NUMEROEXPEDIENTE;
		String E_IDINSTITUCION = "E." + ExpExpedienteBean.C_IDINSTITUCION;
		String E_IDINSTITUCIONTIPOEXPEDIENTE = "E." + ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
		String E_IDTIPOEXPEDIENTE = "E." + ExpExpedienteBean.C_IDTIPOEXPEDIENTE;
		
//		Tabla exp_tipoexpediente
		String T_TIPOEXPEDIENTE = "T." + ExpTipoExpedienteBean.C_NOMBRE + " AS TIPOEXPEDIENTE ";
		String T_IDTIPOEXPEDIENTE = "T." + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;
		String T_IDINSTITUCION = "T." + ExpTipoExpedienteBean.C_IDINSTITUCION;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String 	sql = "SELECT ";	
						sql += C_IDPERSONA + ", ";	
					    sql += C_NOMBRE + ", ";	
					    sql += C_APELLIDOS1 + ", ";	
					    sql += C_APELLIDOS2 + ", ";	
					    sql += C_NIFCIF + ", ";		    
					    sql += E_SANCIONFINALIZADA + ", ";
					    sql += T_TIPOEXPEDIENTE + ", ";
					    sql += E_ANIOEXPEDIENTE + ", ";
					    sql += E_NUMEROEXPEDIENTE;
					    
					sql += " FROM ";
				    	sql += T_CEN_PERSONA + ", " + T_EXP_TIPOEXPEDIENTES + ", " + T_EXP_EXPEDIENTES+ ", " + T_EXP_DENUNCIADOS;
				    				    	
				    sql += " WHERE ";
				    	sql += E_IDTIPOEXPEDIENTE + " = " + idTipoExpediente + " AND ";
				    	sql += E_IDINSTITUCION + " = " + idInstitucion + " AND ";
				    	sql += E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + idInstitucionTipoExpediente + " AND ";
				    	sql += E_NUMEROEXPEDIENTE + " = " + numeroExpediente + " AND ";
				    	sql += E_ANIOEXPEDIENTE + " = " + anioExpediente + " AND ";
				    	sql += " E."+ExpExpedienteBean.C_IDINSTITUCION+" = D."+ExpDenunciadoBean.C_IDINSTITUCION+"(+) "+
								 " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = D."+ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE+"(+) "+
								 " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = D."+ExpDenunciadoBean.C_IDTIPOEXPEDIENTE+"(+) "+
								 " AND E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+" = D."+ExpDenunciadoBean.C_NUMEROEXPEDIENTE+"(+) "+
								 " AND E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" = D."+ExpDenunciadoBean.C_ANIOEXPEDIENTE+"(+) "+
								 " AND 1 = D."+ExpDenunciadoBean.C_IDDENUNCIADO+"(+) ";
				    	sql += E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + T_IDINSTITUCION + " AND ";
				    	sql += E_IDTIPOEXPEDIENTE + " = " + T_IDTIPOEXPEDIENTE;
				    	
	        ClsLogging.writeFileLog("ExpExpedienteAdm, getExpedienteCenso() -> sql: "+sql,3);
	        
			if (rc.query(sql)) {fila = (Row) rc.get(0);}							
			
			return fila.getRow();
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar datos en B.D.");
		}
	}
	

	/**
	 * Método para el cambio de estado de un bean de expediente
	 * @param ExpExpedienteBean
	 * @param ExpEstadosBean estadoAntiguo
	 * @param ExpEstadosBean estadoNuevo
	 * @return void
	 */	
	public void cambioEstado(ExpExpedienteBean exp, ExpEstadosBean estAntiguo, ExpEstadosBean estNuevo,boolean automatico) throws ClsExceptions {
	    // RGG si cambia de fase entonces hay que actualizar la marca para fase
	    if (estAntiguo!=null && !estAntiguo.getIdFase().equals(estNuevo.getIdFase())) {
	        // Cambio de fase
	        exp.setAlertaFaseGenerada("N");
	    }
	    
	    // actualizo la fase
	    if (estAntiguo==null || !estAntiguo.getIdFase().equals(estNuevo.getIdFase())) {
	    	// si ha cambiado la fase se pone fecha de inicio de fase
	    	exp.setFechaInicialFase(exp.getFechaInicialEstado());
	    }
	    exp.setIdFase(estNuevo.getIdFase());
	    
	    exp.setIdEstado(estNuevo.getIdEstado());
	    exp.setAlertaGenerada("N");
	    ExpFasesAdm fasesAdm = new ExpFasesAdm(this.usrbean);
	    
	    ExpFasesBean faseOldBean=new ExpFasesBean();
	    if (estAntiguo!=null){
	    	Hashtable htFase = new Hashtable();
		    htFase.put(ExpFasesBean.C_IDINSTITUCION,estAntiguo.getIdInstitucion());
		    htFase.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,estAntiguo.getIdTipoExpediente());
		    htFase.put(ExpFasesBean.C_IDFASE,estAntiguo.getIdFase());
		    faseOldBean = (ExpFasesBean)fasesAdm.selectByPK(htFase).firstElement();
	    }

	    ExpFasesBean faseNewBean=new ExpFasesBean();
	    if (estNuevo!=null){
	    	Hashtable htFase = new Hashtable();
		    htFase.put(ExpFasesBean.C_IDINSTITUCION,estNuevo.getIdInstitucion());
		    htFase.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,estNuevo.getIdTipoExpediente());
		    htFase.put(ExpFasesBean.C_IDFASE,estNuevo.getIdFase());
		    faseNewBean = (ExpFasesBean)fasesAdm.selectByPK(htFase).firstElement();
	    }

	    if (estAntiguo!=null){
	        if (estAntiguo.getPostActPrescritas().equalsIgnoreCase("N")){
	            exp.setActuacionesPrescritas("");
	        } else if (estAntiguo.getPostActPrescritas().equalsIgnoreCase("S")){
	            exp.setActuacionesPrescritas("SYSDATE");
	        }
	        if (estAntiguo.getPostSancionPrescrita().equalsIgnoreCase("N")){
	            exp.setSancionPrescrita("");
	        } else if (estAntiguo.getPostSancionPrescrita().equalsIgnoreCase("S")){
	            exp.setSancionPrescrita("SYSDATE");
	        }
	        if (estAntiguo.getPostSancionFinalizada().equalsIgnoreCase("N")){
	            exp.setSancionFinalizada("");
	        } else if (estAntiguo.getPostSancionFinalizada().equalsIgnoreCase("S")){
	            exp.setSancionFinalizada("SYSDATE");
	        }
	        if (estAntiguo.getPostAnotCanceladas().equalsIgnoreCase("N")){
	            exp.setAnotacionesCanceladas("");
	        } else if (estAntiguo.getPostAnotCanceladas().equalsIgnoreCase("S")){
	            exp.setAnotacionesCanceladas("SYSDATE");
	        }
	        if (estAntiguo.getPostVisible().equalsIgnoreCase("N") || estAntiguo.getPostVisible().equalsIgnoreCase("S"))
	        	exp.setEsVisible(estAntiguo.getPostVisible());
	        if (estAntiguo.getPostVisibleFicha().equalsIgnoreCase("N") || estAntiguo.getPostVisibleFicha().equalsIgnoreCase("S"))
	        	exp.setEsVisibleEnFicha(estAntiguo.getPostVisibleFicha());		    
	    }
	    
	    if (estNuevo.getPreSancionado().equalsIgnoreCase("N") || estNuevo.getPreSancionado().equalsIgnoreCase("S")) 
	    	exp.setSancionado(estNuevo.getPreSancionado());
	    if (estNuevo.getPreVisible().equalsIgnoreCase("N") || estNuevo.getPreVisible().equals("S")) 
	    	exp.setEsVisible(estNuevo.getPreVisible());
	    if (estNuevo.getPreVisibleFicha().equalsIgnoreCase("N") || estNuevo.getPreVisibleFicha().equalsIgnoreCase("S")) 
	    	exp.setEsVisibleEnFicha(estNuevo.getPreVisibleFicha());
	    
	    //Creamos la anotacion
	    ExpAnotacionBean anotBean = new ExpAnotacionBean();	    
	    anotBean.setIdInstitucion(exp.getIdInstitucion());
	    anotBean.setIdInstitucion_TipoExpediente(exp.getIdInstitucion_tipoExpediente());
	    anotBean.setIdTipoExpediente(exp.getIdTipoExpediente());
	    if (automatico) {
	    	anotBean.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoAutomatico);	    
	    } else {
	    	anotBean.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoCambioEstado);	    
	    }
	    anotBean.setFechaAnotacion("sysdate");
	    if (estAntiguo!=null)
	    	anotBean.setDescripcion(UtilidadesString.getMensajeIdioma(this.usrbean, "expedientes.auditoria.mensaje.cambioEstado")+" "+(automatico?"AUTOMÁTICO ":"")+UtilidadesString.getMensajeIdioma(this.usrbean, "expedientes.auditoria.mensaje.cambioDesde")+" "+estAntiguo.getNombre()+" ("+faseOldBean.getNombre()+")");
	    else
	    	anotBean.setDescripcion(UtilidadesString.getMensajeIdioma(this.usrbean, "expedientes.auditoria.mensaje.estadoInicial")+" "+estNuevo.getNombre()+" ("+faseNewBean.getNombre()+")");
	    anotBean.setIdEstado(estNuevo.getIdEstado());
	    anotBean.setIdFase(estNuevo.getIdFase());
	    anotBean.setNumeroExpediente(exp.getNumeroExpediente());
	    anotBean.setAnioExpediente(exp.getAnioExpediente());
	    anotBean.setIdUsuario(this.usuModificacion);
	    anotBean.setIdInstitucion_Usuario(exp.getIdInstitucion());
	    anotBean.setAutomatico(automatico?"S":"N");
	    anotBean.setFechaInicioEstado(exp.getFechaInicialEstado());
	    anotBean.setFechaFinEstado(exp.getFechaFinalEstado());
	    
	    //Nuevo idAnotacion
	    Hashtable datosAnot = new Hashtable();
	    datosAnot.put(ExpAnotacionBean.C_IDINSTITUCION,exp.getIdInstitucion());
	    datosAnot.put(ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE,exp.getIdInstitucion_tipoExpediente());
	    datosAnot.put(ExpAnotacionBean.C_IDTIPOEXPEDIENTE,exp.getIdTipoExpediente());
	    datosAnot.put(ExpAnotacionBean.C_NUMEROEXPEDIENTE,exp.getNumeroExpediente());
	    datosAnot.put(ExpAnotacionBean.C_ANIOEXPEDIENTE,exp.getAnioExpediente());
	    
	    //Insertamos el seguimiento y actualizamos/insertamos el registro de log
	    ExpLogestadoAdm logAdm = new ExpLogestadoAdm(this.usrbean);
	    //ExpAlertaAdm alertaAdm = new ExpAlertaAdm(this.usrbean);
	    ExpAnotacionAdm anotAdm = new ExpAnotacionAdm(this.usrbean);
	   // ExpFasesAdm fasesAdm = new ExpFasesAdm(this.usrbean);
	    Hashtable htPk = new Hashtable();
	    htPk.put(ExpFasesBean.C_IDINSTITUCION,exp.getIdInstitucion_tipoExpediente());
	    htPk.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,exp.getIdTipoExpediente());
	    htPk.put(ExpFasesBean.C_IDFASE,exp.getIdFase());

	    try {
           anotBean.setIdAnotacion(anotAdm.getNewIdAnotacion(datosAnot));
		   
           //mhg - Comprobamos si existe la anotación para el tipo de expediente. En caso que no exista la insertamos.
	   	   ExpAnotacionAdm expAnotacionAdm = new ExpAnotacionAdm(this.usrbean);
           boolean existe = expAnotacionAdm.existeAnotacion(exp.getIdInstitucion_tipoExpediente(), anotBean.getIdTipoAnotacion(), exp.getIdTipoExpediente());
	   	   if(!existe){
	   		   
	   		   String nombre = "expedientes.tipoAnotacion.cambioEstado.nombre";
			   String mensaje = "expedientes.tipoAnotacion.cambioEstado.mensaje";
	   		   
			   if(automatico){
	   			   nombre = "expedientes.tipoAnotacion.automatica.nombre";
	   			   mensaje = "expedientes.tipoAnotacion.automatica.mensaje";
	   		   }
			   expAnotacionAdm.insertarTipoAnotacion(this.usrbean, anotBean.getIdTipoAnotacion(), exp.getIdInstitucion_tipoExpediente(), exp.getIdTipoExpediente(), nombre, mensaje);
	   	   }
           
           anotAdm.insert(anotBean);
           logAdm.insertarEntrada(exp,estAntiguo,estNuevo);
        } catch (ClsExceptions e) {
            throw new ClsExceptions (e, "Error al recuperar datos en B.D.");
        }	    
	}
	
	public void chequearAlarmas()throws ClsExceptions{
	    
	    //UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    UsrBean userBean = new UsrBean();

	    //Iniciamos la transacción
        UserTransaction tx2 = userBean.getTransaction();

        CenInstitucionAdm institucionAdm = new CenInstitucionAdm(userBean);
	    ExpAlertaAdm alertaAdm = new ExpAlertaAdm(this.usrbean);
	    ExpAnotacionAdm anotacionAdm = new ExpAnotacionAdm(this.usrbean);
	    RowsContainer rc1 = new RowsContainer();

	    try {
       		
			/////////////////////////////////////////////
			/// CONTROL 1: CAMBIOS DE ESTADO  
			/////////////////////////////////////////////
			String sql_estado1="SELECT E.IDINSTITUCION, "+
			       " E.IDINSTITUCION_TIPOEXPEDIENTE, "+
			       " E.IDTIPOEXPEDIENTE, "+
			       " E.ANIOEXPEDIENTE, "+
			       " E.NUMEROEXPEDIENTE, "+
			       " decode(abs(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO) - "+
			       " (E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO), "+
			       " 0, "+
			       " E.FECHAPRORROGAESTADO, "+
			       " E.FECHAFINALESTADO) as FECHAFINAL "+
			       " FROM EXP_EXPEDIENTE E , exp_tipoexpediente t, exp_campotipoexpediente c "+
			       " where E.Idinstitucion_Tipoexpediente=t.idinstitucion "+
			       " and   E.IDTIPOEXPEDIENTE = t.idtipoexpediente "+
			       " and   E.Idinstitucion_Tipoexpediente=c.idinstitucion "+
			       " and   E.IDTIPOEXPEDIENTE = c.idtipoexpediente "+
			       " and   c.idcampo = 3 "+
			       " and   c.visible = 'S' "+
			       " and  E.ALERTAGENERADA <> 'S' "+
			       " AND E.FECHAFINALESTADO IS NOT NULL"+
			       " AND decode(abs(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO) - (E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO),0,E.FECHAPRORROGAESTADO,E.FECHAFINALESTADO) < SYSDATE ";
			
			if (rc1.query(sql_estado1)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);

						CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
						
						// Nueva anotación de estado caducado + cambio de estado automático. Ya no se inserta alerta.
						ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));

						if (!anotacionAdm.insertarAnotacionAutomatica(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.estadoVencido"))) {
						    throw new ClsExceptions("1.Error al insertar anotacion. "+anotacionAdm.getError());
						}
				        ClsLogging.writeFileLog("1.Anotacion insertada.",7);
				        if (!cambioEstadoAutomatico(expBean)) {
				            throw new SIGAException("1.No se ha cambiado el estado. "+this.getError());
				        }
				        ClsLogging.writeFileLog("1.Cambio de estado.",7);
				        if (!this.update(expBean)) {
				            throw new ClsExceptions("1.Error al actualizar expediente. "+this.getError());
				        }
				        ClsLogging.writeFileLog("1.Actualizado expediente.",7);
				        
						tx2.commit();
					}catch (SIGAException e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLog("1.Error controlado de condiciones. Control 1: " + this.getError(),7);
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("1.Se ha producido un error al chequear alarmas. Control 1",e,3);
			    	}			
				}
			}
			
			/////////////////////////////////////////////
			/// CONTROL 2: EXPEDIENTE CADUCADO SIN ESTADO FINAL  
			/////////////////////////////////////////////
			String sql_estado2="SELECT E.IDINSTITUCION, "+
			    	" E.IDINSTITUCION_TIPOEXPEDIENTE, "+
			    	" E.IDTIPOEXPEDIENTE, "+
				    " E.ANIOEXPEDIENTE, "+
				    " E.NUMEROEXPEDIENTE, "+
				    " ES.NOMBRE "+
				    " FROM   EXP_EXPEDIENTE E, EXP_ESTADO ES, exp_tipoexpediente tipo "+
				    " WHERE  E.IDESTADO = ES.IDESTADO (+) "+
				    " AND    E.IDFASE = ES.IDFASE (+) "+
				    " AND    E.IDINSTITUCION_TIPOEXPEDIENTE = ES.IDINSTITUCION (+) "+
				    " AND    E.IDTIPOEXPEDIENTE = ES.IDTIPOEXPEDIENTE (+) "+
				    " AND    e.idinstitucion = tipo.idinstitucion(+) "+
				    " AND    e.idtipoexpediente = tipo.idtipoexpediente(+) "+
				    " AND    E.ALERTAGENERADACAD <> 'S' "+ // COMPRUEBA QUE NO SE HA ANOTADO LA CADUCIDAD.
				    " AND    sysdate >" +
				    " case when tipo.TIEMPOCADUCIDAD > 0 " +
				  //BEGIN BNS COMPROBAR QUE DEBERÍA SER ASÍ
				    //SI SE CAMBIA EL TIPO DE EXPEDIENTE Y SE LE PONE TIEMPO DE CADUCIDAD A UNO QUE NO TENÍA, LOS
				  // EXPEDIENTES CON FECHA DE CADUCIDAD SEGUIRAN CON ESA FECHA
				    //" then nvl(e.fecha, sysdate) + tipo.TIEMPOCADUCIDAD)" +
				    " then nvl(e.fechacaducidad, e.fecha + tipo.TIEMPOCADUCIDAD)" +
				  //END BNS
				    " else nvl (e.fechacaducidad, sysdate)" + 
				    " end" +
				  //  " AND    nvl(e.fechacaducidad, e.fecha + tipo.TIEMPOCADUCIDAD) <= SYSDATE "+
				    " AND    ES.ESTADOFINAL(+) = 'N'";
			
			rc1 = new RowsContainer();
			if (rc1.query(sql_estado2)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);
						
						CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
						
						// Nueva alerta de expediente caducado.
						ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));

				        //if (!alertaAdm.insertarAlerta(expBean,UtilidadesString.getMensajeIdioma(this.usrbean,"expedientes.alertasyanotaciones.mensajes.expedienteCaducado"))) {
				        //    throw new ClsExceptions("2.Error al insertar alarma. "+alertaAdm.getError());
				        //}
				        //ClsLogging.writeFileLog("2.Alerta insertada.",7);
						if (!anotacionAdm.insertarAnotacionAutomatica(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.expedienteCaducado"))) {
						    throw new ClsExceptions("2.Error al insertar anotacion. "+anotacionAdm.getError());
						}
				        ClsLogging.writeFileLog("2.Anotacion insertada.",7);
				        expBean.setAlertaGeneradaCad("S");
				        if (!this.update(expBean)) {
				            throw new ClsExceptions("2.Error al actualizar expediente. "+this.getError());
				        }
				        ClsLogging.writeFileLog("2.Actualizado expediente.",7);

						tx2.commit();
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("2.Se ha producido un error al chequear alarmas. Control 2",e,3);
			    	}			
				}
			}
			
			/////////////////////////////////////////////
			/// CONTROL 3: ESTADO FINAL  
			/////////////////////////////////////////////
			String sql_estado3="SELECT E.IDINSTITUCION, "+
			    	" E.IDINSTITUCION_TIPOEXPEDIENTE, "+
				    " E.IDTIPOEXPEDIENTE, "+
				    " E.ANIOEXPEDIENTE, "+
				    " E.NUMEROEXPEDIENTE, "+
				    " ES.NOMBRE "+
				    " FROM   EXP_EXPEDIENTE E, EXP_ESTADO ES "+
				    " WHERE  E.IDESTADO = ES.IDESTADO "+
				    " AND    E.IDFASE = ES.IDFASE "+
				    " AND    E.IDINSTITUCION_TIPOEXPEDIENTE = ES.IDINSTITUCION "+
				    " AND    E.IDTIPOEXPEDIENTE = ES.IDTIPOEXPEDIENTE "+
				    " AND    E.ALERTAFINALGENERADA <> 'S' "+
				    " AND    ES.ESTADOFINAL = 'S'";
			
			rc1 = new RowsContainer();
    		if (rc1.query(sql_estado3)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);
						
						CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
						
						// Nueva alerta de aviso de que está en un estado final.
						ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));
						
				        if (!alertaAdm.insertarAlerta(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.estadoFinal") + " ("+fila1.getString("NOMBRE")+")")) {
				            throw new ClsExceptions("3.Error al insertar alarma. "+alertaAdm.getError());
				        }
				        ClsLogging.writeFileLog("3.Alerta insertada.",7);
				        expBean.setAlertaFinalGenerada("S");
				        if (!this.update(expBean)) {
				            throw new ClsExceptions("3.Error al actualizar expediente. "+this.getError());
				        }
				        ClsLogging.writeFileLog("3.Actualizado expediente.",7);
				        
						tx2.commit();
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("3.Se ha producido un error al chequear alarmas. Control 3",e,3);
			    	}			
				}
			}
			
			/////////////////////////////////////////////
			/// CONTROL 4: CAMBIOS DE FASE  
			/////////////////////////////////////////////
			String sql_fase4="SELECT E.IDINSTITUCION, "+
			       " E.IDINSTITUCION_TIPOEXPEDIENTE, "+
			       " E.IDTIPOEXPEDIENTE, "+
			       " E.ANIOEXPEDIENTE, "+
			       " E.NUMEROEXPEDIENTE, "+
			       " FA.NOMBRE,  "+
			       " E.FECHAINICIALFASE,  "+
			       " nvl(FA.DIASVENCIMIENTO,0) as DIASVENCIMIENTO,  "+
			       " E.FECHAINICIALFASE + nvl(FA.DIASVENCIMIENTO,0) AS FECHAFINALFASE "+
			       " FROM   EXP_EXPEDIENTE E, EXP_FASES FA "+
			       " WHERE  E.IDFASE = FA.IDFASE "+
			       " AND    E.IDINSTITUCION_TIPOEXPEDIENTE = FA.IDINSTITUCION "+
			       " AND    E.IDTIPOEXPEDIENTE = FA.IDTIPOEXPEDIENTE "+
			       " AND    E.ALERTAFASEGENERADA <> 'S' "+
			       " AND    E.FECHAINICIALFASE + nvl(FA.DIASVENCIMIENTO,0) <  SYSDATE" +
			       " and	nvl(FA.DIASVENCIMIENTO,0) > 0";
			
			rc1 = new RowsContainer();
			if (rc1.query(sql_fase4)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);
						
						CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
						
						// Nueva anotación de cambio de fase + cambio de fase automático
						ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));

				        if (!anotacionAdm.insertarAnotacionAutomatica(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.faseVencida")  + " ("+fila1.getString("NOMBRE")+")")) {
				            throw new ClsExceptions("4.Error al insertar anotación. "+anotacionAdm.getError());
				        }
				        ClsLogging.writeFileLog("4.Anotacion insertada.",7);
				        expBean.setAlertaFaseGenerada("S");
				        if (!this.update(expBean)) {
				            throw new ClsExceptions("4.Error al actualizar expediente. "+this.getError());
				        }
				        ClsLogging.writeFileLog("4.Actualizado expediente.",7);
				        
						tx2.commit();
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("4.Se ha producido un error al chequear alarmas. Control 4",e,3);
			    	}			
				}
			}
			
			/////////////////////////////////////////////
			/// CONTROL 5: AVISO DE CAMBIO DE FASE  
			/////////////////////////////////////////////
			String sql_fase5="SELECT E.IDINSTITUCION, "+
			    " E.IDINSTITUCION_TIPOEXPEDIENTE, "+
			    " E.IDTIPOEXPEDIENTE, "+
			    " E.ANIOEXPEDIENTE, "+
			    " E.NUMEROEXPEDIENTE, "+
			    " FA.NOMBRE,  "+
			    " E.FECHAINICIALFASE, "+ 
			    " E.FECHAINICIALFASE + nvl(FA.DIASVENCIMIENTO,0) AS FECHAFINALFASE, "+
			    " nvl(FA.DIASANTELACION,0) AS DIASANTELACION "+
			    " FROM   EXP_EXPEDIENTE E, EXP_FASES FA "+
			    " WHERE  E.IDFASE = FA.IDFASE "+
			    " AND    E.IDINSTITUCION_TIPOEXPEDIENTE = FA.IDINSTITUCION "+
			    " AND    E.IDTIPOEXPEDIENTE = FA.IDTIPOEXPEDIENTE "+
			    " AND    E.ALERTAFASEGENERADA = 'N' "+
			    " AND    (E.FECHAINICIALFASE + nvl(FA.DIASVENCIMIENTO,0)) - nvl(FA.DIASANTELACION,0) <  SYSDATE" +
			    " and	nvl(FA.DIASVENCIMIENTO,0) > 0" +
			    " and	nvl(FA.DIASANTELACION,0) >0";
			
			rc1 = new RowsContainer();
			if (rc1.query(sql_fase5)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);
						// SOLO SI EXISTEN DÍAS DE ANTELACIÓN
						if (!fila1.getString("DIASANTELACION").equals("0")) {
							
							CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
							
							// Nueva alerta de aviso de vencimiento de la fase
							ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));
					        
							if (!alertaAdm.insertarAlerta(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.antelacionFase",new String[] {GstDate.getFormatedDateShort("ES",fila1.getString("FECHAFINALFASE"))}) + " ("+fila1.getString("NOMBRE")+")")) {
					            throw new ClsExceptions("5.Error al insertar alarma. "+alertaAdm.getError());
					        }
					        ClsLogging.writeFileLog("5.Alerta insertada.",7);
					        expBean.setAlertaFaseGenerada("P");
					        if (!this.update(expBean)) {
					            throw new ClsExceptions("5.Error al actualizar expediente. "+this.getError());
					        }
					        ClsLogging.writeFileLog("5.Actualizado expediente.",7);
						}
						tx2.commit();
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("5.Se ha producido un error al chequear alarmas. Control 5",e,3);
			    	}			
				}
			}
			
			/////////////////////////////////////////////
			/// CONTROL 6: AVISO DE CAMBIO DE ESTADO  
			/////////////////////////////////////////////
			String sql_estado6="SELECT E.IDINSTITUCION, "+
			       " E.IDINSTITUCION_TIPOEXPEDIENTE, "+
			       " E.IDTIPOEXPEDIENTE, "+
			       " E.ANIOEXPEDIENTE, "+
			       " E.NUMEROEXPEDIENTE, "+
			       " ES.NOMBRE,  "+
			       " ES.MENSAJE,  "+
			       " E.FECHAINICIALESTADO, "+
			       " decode(abs(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO) - "+
			       " 	(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO), "+
			       " 	 0, "+
			       " 	 E.FECHAPRORROGAESTADO, "+
			       " 	 E.FECHAFINALESTADO) as FECHAFINALESTADO, "+
			       " decode(abs(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO) - "+
			       " 	(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO), "+
			       " 	 0, "+
			       " 	 E.FECHAPRORROGAESTADO, "+
			       " 	 E.FECHAFINALESTADO) - nvl(ES.DIASANTELACION,0) AS  FECHAAVISO, "+
			       //" E.FECHAFINALESTADO, "+
			       //" E.FECHAFINALESTADO - nvl(ES.DIASANTELACION,0) AS  FECHAAVISO, "+
			       " nvl(ES.DIASANTELACION,0) AS DIASANTELACION "+
			       " FROM   EXP_EXPEDIENTE E, EXP_ESTADO ES "+
			       " WHERE  E.IDESTADO = ES.IDESTADO "+
			       " AND    E.IDFASE = ES.IDFASE "+
			       " AND    E.IDINSTITUCION_TIPOEXPEDIENTE = ES.IDINSTITUCION "+
			       " AND    E.IDTIPOEXPEDIENTE = ES.IDTIPOEXPEDIENTE "+
			       " AND    E.ALERTAGENERADA = 'N' "+
			       " AND    ES.ACTIVARALERTAS = 'S' "+
			       " AND    decode(abs(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO) - "+
			       " 	(E.FECHAPRORROGAESTADO - E.FECHAFINALESTADO), "+
			       " 	 0, "+
			       " 	 E.FECHAPRORROGAESTADO, "+
			       " 	 E.FECHAFINALESTADO) - nvl(ES.DIASANTELACION,0) <  SYSDATE" +
			       " and nvl(ES.DIASANTELACION,0) > 0"; // para controlar si los días de antelación son 0.
		
			rc1 = new RowsContainer();
			if (rc1.query(sql_estado6)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);
						// SOLO SI EXISTEN DÍAS DE ANTELACIÓN
						if (!fila1.getString("DIASANTELACION").equals("0")) {
							
							CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
							
							// Nueva alerta de aviso de vencimiento del estado
							ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));
	
							if (!alertaAdm.insertarAlerta(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.aviso6")+" "+GstDate.getFormatedDateShort("ES",fila1.getString("FECHAFINALESTADO"))+". ("+fila1.getString("NOMBRE")+")")) {
					            throw new ClsExceptions("6.Error al insertar alarma. "+alertaAdm.getError());
					        }
					        ClsLogging.writeFileLog("6.Alerta insertada.",7);
					        expBean.setAlertaGenerada("P");
					        if (!this.update(expBean)) {
					            throw new ClsExceptions("6.Error al actualizar expediente. "+this.getError());
					        }
					        ClsLogging.writeFileLog("6.Actualizado expediente.",7);
						}

						tx2.commit();
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("5.Se ha producido un error al chequear alarmas. Control 6",e,3);
			    	}			
				}
			}
			
			/////////////////////////////////////////////
			/// CONTROL 7: AVISO DE CADUCIDAD  
			/////////////////////////////////////////////
			String sql_estado7="SELECT E.IDINSTITUCION, "+
			       	" E.IDINSTITUCION_TIPOEXPEDIENTE,  "+
					" E.IDTIPOEXPEDIENTE,  "+
					" E.ANIOEXPEDIENTE, "+ 
					" E.NUMEROEXPEDIENTE,  "+
					" E.FECHA,  "+
					" T.TIEMPOCADUCIDAD, "+
					//" E.FECHA + T.TIEMPOCADUCIDAD AS  FECHACADUCIDAD,  "+
					" E.FECHACADUCIDAD AS  FECHACADUCIDAD,  "+
					//" E.FECHA + T.TIEMPOCADUCIDAD - nvl(T.DIASANTELACIONCAD,0) AS  FECHAAVISO,  "+
					" E.FECHACADUCIDAD - nvl(T.DIASANTELACIONCAD,0) AS  FECHAAVISO,  "+
					" nvl(T.DIASANTELACIONCAD,0) AS DIASANTELACION "+ 
					" FROM   EXP_EXPEDIENTE E, EXP_TIPOEXPEDIENTE T "+
					" WHERE  E.IDINSTITUCION_TIPOEXPEDIENTE = T.IDINSTITUCION "+
					" AND    E.IDTIPOEXPEDIENTE = T.IDTIPOEXPEDIENTE "+
					" AND    E.ALERTACADUCIDADGENERADA = 'N'  "+
					" AND    nvl(E.FECHACADUCIDAD, e.fecha + t.TIEMPOCADUCIDAD) - nvl(T.DIASANTELACIONCAD,0) <  SYSDATE" +
					" and    nvl(T.DIASANTELACIONCAD,0)>0";
			
			rc1 = new RowsContainer();
			if (rc1.query(sql_estado7)) {
				for (int i = 0; i < rc1.size(); i++)	{
					try {
					    tx2.begin();
						Row fila1 = (Row) rc1.get(i);
						// SOLO SI EXISTEN DÍAS DE ANTELACIÓN
						if (!fila1.getString("DIASANTELACION").equals("0")) {
							
							CenInstitucionBean cenInstitucionBean = institucionAdm.getInstitucion(fila1.getString("IDINSTITUCION"));
							
							// Nueva alerta de aviso de vencimiento del estado
							ExpExpedienteBean expBean=this.getExpediente(fila1.getString("IDTIPOEXPEDIENTE"),fila1.getString("IDINSTITUCION"),fila1.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila1.getString("ANIOEXPEDIENTE"),fila1.getString("NUMEROEXPEDIENTE"));
	
							if (!alertaAdm.insertarAlerta(expBean,UtilidadesString.getMensajeIdioma(cenInstitucionBean.getIdLenguaje(),"expedientes.alertasyanotaciones.mensajes.aviso7")+" "+GstDate.getFormatedDateShort("ES",fila1.getString("FECHACADUCIDAD"))+".")) {
					            throw new ClsExceptions("7.Error al insertar alarma. "+alertaAdm.getError());
					        }
					        ClsLogging.writeFileLog("7.Alerta insertada.",7);
					        expBean.setAlertaCaducidadGenerada("S"); // CONTROLA SI SE HA PRODUCIDO LA ALERTA
					        if (!this.update(expBean)) {
					            throw new ClsExceptions("7.Error al actualizar expediente. "+this.getError());
					        }
					        ClsLogging.writeFileLog("7.Actualizado expediente.",7);
						}

						tx2.commit();
					}catch (Exception e) {
				        try {
				            tx2.rollback();
				        } catch (Exception e1) {
				        }
				        ClsLogging.writeFileLogError("7.Se ha producido un error al chequear alarmas. Control 7",e,3);
			    	}			
				}
			}
			
			
        } catch (Exception e) { 	
        	ClsLogging.writeFileLogError("ExpExpedienteAdm.chequearAlarmas -> Error al Buscar Los expedientes a tratar: " + e.getMessage(),e,3);
			throw new ClsExceptions (e, "ExpExpedienteAdm.chequearAlarmas -> Error al Buscar Los expedientes a tratar "); 
		}
	}
	
	public boolean cambioEstadoAutomatico(ExpExpedienteBean expBean) throws ClsExceptions{
	    
	    boolean resultado = true;
	    
	    //Obtenemos el bean del estado actual
	    ExpEstadosAdm expEstAdm = new ExpEstadosAdm(UsrBean.UsrBeanAutomatico(expBean.getIdInstitucion().toString()));
	    Hashtable htPkEst = new Hashtable();
	    htPkEst.put(ExpEstadosBean.C_IDINSTITUCION,expBean.getIdInstitucion_tipoExpediente());
	    htPkEst.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,expBean.getIdTipoExpediente());
	    htPkEst.put(ExpEstadosBean.C_IDFASE,expBean.getIdFase());
	    htPkEst.put(ExpEstadosBean.C_IDESTADO,expBean.getIdEstado());
	    ExpEstadosBean expEstOld;
	    ExpEstadosBean expEstNew = null;        
	    try {
            expEstOld = (ExpEstadosBean)expEstAdm.selectByPKForUpdate(htPkEst).firstElement();
        
	        if ((expEstOld.getAutomatico().equalsIgnoreCase("S")) && 
		        (expEstOld.getIdEstadoSiguiente()!=null))
		       {
			        Hashtable htPkEstNew = (Hashtable)htPkEst.clone();
			        htPkEstNew.put(ExpEstadosBean.C_IDESTADO,expEstOld.getIdEstadoSiguiente());
			        htPkEstNew.put(ExpEstadosBean.C_IDFASE,expEstOld.getIdFaseSiguiente());
			        Object obj=expEstAdm.selectByPK(htPkEstNew).firstElement();
			        if (obj!=null){
					    expEstNew = (ExpEstadosBean)obj;		        
					    
				        Date dFechaFinal;
				        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				        try {
				            dFechaFinal = sdf.parse(expBean.getFechaFinalEstado());
				        } catch (Exception e) {
				            throw new ClsExceptions("Error en parse de fecha: "+e.toString());
				        }
				        Calendar cal = Calendar.getInstance();
				        cal.setTime(dFechaFinal);
				        cal.set(Calendar.HOUR_OF_DAY,0);
				        cal.set(Calendar.MINUTE,0);
				        cal.set(Calendar.SECOND,0);
				        cal.set(Calendar.MILLISECOND,0);
				        // RGG 07/03/2007 Cambio de estado de modo que ahora la fecha de inicio del siguiente estado es la final del estado anterior
				        // 				  Los comentarios de arriba sobre la fecha +1 ya no valen
				        //cal.add(Calendar.DAY_OF_WEEK,1);
				        cal.add(Calendar.DAY_OF_WEEK,0);
				        
				        Date dIni = cal.getTime();
				        			        
				        String sIni = sdf.format(dIni);
				        expBean.setFechaInicialEstado(sIni);
				        ExpAlertaAdm alertaAdm = new ExpAlertaAdm(UsrBean.UsrBeanAutomatico(expBean.getIdInstitucion().toString()));
				        
				        try{
				        	ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm(UsrBean.UsrBeanAutomatico(expBean.getIdInstitucion().toString()));
				        	plazoAdm.establecerFechaFinal(expBean);
				        }catch(Exception e){
				        	expBean.setFechaFinalEstado(null);
				        	alertaAdm.insertarAlerta(expBean,"Se ha anulado la fecha final del estado");
				        }

				        cambioEstado(expBean,expEstOld,expEstNew,true);
				        

				        // RGG Ya no se inserta aquí el mensaje.
				        // En el control de aviso de antelacion del estado se pone este mensaje
					    //Insertamos alerta de estado antiguo			        
				        //if (!(expEstOld.getMensaje().equals("")||expEstOld.getMensaje().equals(null))) {
				        //    alertaAdm.insertarAlerta(expBean,expEstOld.getMensaje());
				        //}
				        
				        //ExpFasesAdm fasesAdm = new ExpFasesAdm(this.usrbean);
				        //Hashtable htPk = new Hashtable();
				        //htPk.put(ExpFasesBean.C_IDINSTITUCION,expEstOld.getIdInstitucion());
				        //htPk.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,expEstOld.getIdTipoExpediente());
				        //htPk.put(ExpFasesBean.C_IDFASE,expEstOld.getIdFase());
				        //ExpFasesBean fasesBean = (ExpFasesBean)fasesAdm.selectByPK(htPk).firstElement();
				        
					    // RGG ATENCION Aqui hay dudas sobre el funcionamiento. SI se deja lo de abajo nunca cambia el estado.
					    //al ser un expediente con estado automatico queremos que lo vuelva a procesar en la siguiente pasada del proceso
				        // automatico. 
				        //resultado=false;
				        //this.setError("Al ser un expediente con estado automatico queremos que lo vuelva a procesar en la siguiente pasada del proceso automático");
					    
					    // se queda como sin generar ya que ha cambiado el estado.
					    expBean.setAlertaGenerada("N");
		        	}	
		        } else {
		        	expBean.setAlertaGenerada("S");
		        	resultado=false; //No cumple la codición de ser automático y tener estado siguiente
			        this.setError("No cumple la codición de ser automático y tener estado siguiente");
		        }
	       
	        
        } catch (Exception e) {
            throw new ClsExceptions(e,"Se ha producido un error en el rollback");
        }
        
        return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	public Hashtable beanToHashTableForUpdate(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpExpedienteBean b = (ExpExpedienteBean) bean;
	        if(b.getTipoExpDisciplinario()!=null && !b.getTipoExpDisciplinario().toString().trim().equals("") ){
	        	if (b.getTipoExpDisciplinario()==null)
					UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEJG, "");
				else
					UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEJG, b.getTipoExpDisciplinario());
	        	if (b.getNumExpDisciplinario()==null)
					UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEROEJG, "");
				else
					UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEROEJG, b.getNumExpDisciplinario());
				if (b.getAnioExpDisciplinario()==null)
					UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEJG, "");
				else
					UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEJG, b.getAnioExpDisciplinario());
	        }else{
	        	if (b.getTipoExpDisciplinario()==null)
					UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEJG, "");
				else
					UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEJG, b.getTipoExpDisciplinario());
				if (b.getNumExpDisciplinario()==null)
					UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEXPDISCIPLINARIO, "");
				else
					UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEXPDISCIPLINARIO, b.getNumExpDisciplinario());
				if (b.getAnioExpDisciplinario()==null)
					UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO, "");
				else
					UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO, b.getAnioExpDisciplinario());
	        }
			if (b.getProcedimiento()==null)
				UtilidadesHash.set(htData, ExpExpedienteBean.C_PROCEDIMIENTO, "");
			else
				UtilidadesHash.set(htData, ExpExpedienteBean.C_PROCEDIMIENTO, b.getProcedimiento());
			if (b.getNumAsunto()==null)
				UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMASUNTO, "");
			else
				UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMASUNTO, b.getNumAsunto());
			if (b.getIdFase()==null)
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IDFASE, "");
			else
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IDFASE, b.getIdFase());
			
			UtilidadesHash.set(htData, ExpExpedienteBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());			
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ASUNTO, b.getAsunto());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_JUZGADO, b.getJuzgado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION_JUZGADO, b.getIdInstitucionJuzgado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO, b.getIdInstitucionProcedimiento());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAINICIALESTADO, b.getFechaInicialEstado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAFINALESTADO, b.getFechaFinalEstado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAPRORROGAESTADO, b.getFechaProrrogaEstado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_DESCRIPCIONRESOLUCION, b.getDescripcionResolucion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_SANCIONPRESCRITA, b.getSancionPrescrita());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ACTUACIONESPRESCRITAS, b.getActuacionesPrescritas());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_SANCIONFINALIZADA, b.getSancionFinalizada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTAGENERADA, b.getAlertaGenerada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTAGENERADACAD, b.getAlertaGeneradaCad());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ALERTACADUCIDADGENERADA, b.getAlertaCaducidadGenerada());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_USUMODIFICACION, b.getUsuModificacion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDDIRECCION, b.getIdDireccion());
			
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_tipoExpediente());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ESVISIBLE, b.getEsVisible());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDAREA, b.getIdArea());
			UtilidadesHash.setForCompare(htData, ExpExpedienteBean.C_IDMATERIA, b.getIdMateria());
			UtilidadesHash.setForCompare(htData, ExpExpedienteBean.C_IDPRETENSION, b.getIdPretension());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_OTRASPRETENSIONES, b.getOtrasPretensiones());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ESVISIBLEENFICHA, b.getEsVisibleEnFicha());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDCLASIFICACION, b.getIdClasificacion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDESTADO, b.getIdEstado());	
			UtilidadesHash.set(htData, ExpExpedienteBean.C_SANCIONADO, b.getSancionado());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_ANOTACIONESCANCELADAS, b.getAnotacionesCanceladas());
			
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHACADUCIDAD, b.getFechaCaducidad());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_FECHARESOLUCION, b.getFechaResolucion());
			UtilidadesHash.set(htData, ExpExpedienteBean.C_OBSERVACIONES, b.getObservaciones());
			if (b.getMinuta() != null) {
				UtilidadesHash.set(htData, ExpExpedienteBean.C_MINUTA, b.getMinuta());
			} else {
				UtilidadesHash.set(htData, ExpExpedienteBean.C_MINUTA, "");
			}
			if(b.getImporteTotal() != null){
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IMPORTETOTAL, b.getImporteTotal());
			}else{
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IMPORTETOTAL, "");
			}
			
			if(b.getMinutaFinal()!=null){
				UtilidadesHash.set(htData, ExpExpedienteBean.C_MINUTAFINAL, b.getMinutaFinal());
			}else{
				UtilidadesHash.set(htData, ExpExpedienteBean.C_MINUTAFINAL, "");
			}
			if(b.getImporteTotalFinal()!=null){
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IMPORTETOTALFINAL, b.getImporteTotalFinal());
			}else{
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IMPORTETOTALFINAL,"");
			}
			if(b.getDerechosColegiales()!= null){
				UtilidadesHash.set(htData, ExpExpedienteBean.C_DERECHOSCOLEGIALES, b.getDerechosColegiales());
			}else{
				UtilidadesHash.set(htData, ExpExpedienteBean.C_DERECHOSCOLEGIALES, "");
			}
			
			if(b.getPorcentajeIVA()!=null){
				UtilidadesHash.set(htData, ExpExpedienteBean.C_PORCENTAJEIVA, b.getPorcentajeIVA());
			}else{
				UtilidadesHash.set(htData, ExpExpedienteBean.C_PORCENTAJEIVA, "");
			}
			
			UtilidadesHash.set(htData, ExpExpedienteBean.C_IDTIPOIVA, b.getIdTipoIVA());
			if (b.getIdResultadoJuntaGobierno() != null)
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IDRESULTADOJUNTAGOBIERNO, b.getIdResultadoJuntaGobierno());
			else
				UtilidadesHash.set(htData, ExpExpedienteBean.C_IDRESULTADOJUNTAGOBIERNO, "");
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	public Vector getDatosInformeExpediente (String idInstitucion ,String idInstitucionTipoExp,String idTipoExp ,
			String anio,String numero, String idPersona, boolean isInforme, boolean isASolicitantes
	) throws ClsExceptions  
	{
		Vector datos = null;
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;

			htCodigos.put(new Integer(1), this.usrbean.getLanguage());
			htCodigos.put(new Integer(2), this.usrbean.getLanguage());
			htCodigos.put(new Integer(3), this.usrbean.getLanguage());
			htCodigos.put(new Integer(4), this.usrbean.getLanguage());
			keyContador=4;
			
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT SUBSTR (mate.nombre, 0, 25) || ' ('|| SUBSTR (area.nombre, 0, 25) || ')' AS MATERIA, "
					+ " TO_CHAR(SYSDATE, 'dd-mm-yyyy') AS FECHAACTUAL, "
					+ " EXP.IDINSTITUCION, "
					+ " EXP.IDINSTITUCION_TIPOEXPEDIENTE, "
					+ " EXP.IDTIPOEXPEDIENTE, "
					+ " EXP.ANIOEXPEDIENTE, "
					+ " EXP.NUMEROEXPEDIENTE, "
					+ " TO_CHAR(EXP.FECHA, 'dd-mm-yyyy') AS FECHA, "
					+ " TO_CHAR(EXP.FECHA, 'dd-mm-yyyy') AS FECHAINICIO, "
					+ " PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(EXP.FECHA, 'M', "+this.usrbean.getLanguage()+") FECHAINICIO_LETRA, "
					+ " EXP.ASUNTO, "
					+ " (SELECT F_SIGA_GETRECURSO(P.DESCRIPCION,:1) FROM SCS_PRETENSION P WHERE P.IDINSTITUCION=EXP.IDINSTITUCION AND P.IDPRETENSION=EXP.IDPRETENSION) AS PRETENSION, "
					+ " EXP.OTRASPRETENSIONES, "
					+ " EXP.ALERTAGENERADA, "
					+ " EXP.ESVISIBLE, "
					+ " EXP.ESVISIBLEENFICHA, "
					+ " EXP.SANCIONADO, "
					+ " to_char(EXP.SANCIONPRESCRITA, 'dd-mm-yyyy') as SANCIONPRESCRITA, "
					+ " to_char(EXP.ACTUACIONESPRESCRITAS, 'dd-mm-yyyy') as ACTUACIONESPRESCRITAS, "
					+ " to_char(EXP.SANCIONFINALIZADA, 'dd-mm-yyyy') as SANCIONFINALIZADA, "
					+ " to_char(EXP.ANOTACIONESCANCELADAS, 'dd-mm-yyyy') as ANOTACIONESCANCELADAS, "
					+ " EXP.ANIOEXPDISCIPLINARIO, "
					+ " EXP.NUMEXPDISCIPLINARIO, "
					+ " EXP.ANIOEJG,EXP.NUMEROEJG, EXP.IDTIPOEJG, "		       
					+ " EXP.NUMASUNTO, "
					+ " TO_CHAR(EXP.FECHAINICIALESTADO, 'dd-mm-yyyy') AS FECHAINICIALESTADO, "
					+ " nvl2(EXP.FECHAINICIALESTADO, PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(EXP.FECHAINICIALESTADO, 'M', "+this.usrbean.getLanguage()+"), '') AS FECHAINICIALESTADOLETRA, "
					+ " TO_CHAR(EXP.FECHAFINALESTADO, 'dd-mm-yyyy') AS FECHAFINALESTADO, "
					+ " nvl2(EXP.FECHAFINALESTADO, PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(EXP.FECHAFINALESTADO, 'M', "+this.usrbean.getLanguage()+"), '') AS FECHAFINALESTADOLETRA, "
					+ " TO_CHAR(EXP.FECHAPRORROGAESTADO, 'dd-mm-yyyy') AS FECHAPRORROGAESTADO, "
					+ " EXP.DESCRIPCIONRESOLUCION, "
					+ " TO_CHAR(EXP.FECHARESOLUCION, 'dd-mm-yyyy') AS FECHARESOLUCION, "
					+ " TO_CHAR(EXP.FECHACADUCIDAD, 'dd-mm-yyyy') AS FECHACADUCIDAD, "
					+ " EXP.OBSERVACIONES, "
					+ " f_siga_formatonumero(EXP.MINUTA,2) MINUTA, "
					+ " f_siga_formatonumero(EXP.PORCENTAJEIVA,2) PORCENTAJEIVA, "
					+ " f_siga_formatonumero(EXP.IMPORTETOTAL,2) IMPORTETOTAL, "
					+ " f_siga_formatonumero(EXP.MINUTAFINAL,2) MINUTAFINAL, "
					+ " EXP.DERECHOSCOLEGIALES, "
					+ " f_siga_formatonumero(EXP.IMPORTETOTALFINAL,2) IMPORTETOTALFINAL, "
					+ " EXP.IDDIRECCION as IDDIRECCION_PRIN, "
					+ " PER.IDPERSONA, "
					+ " INITCAP(PER.NOMBRE) PER_NOMBRE, "
					+ " UPPER(PER.NOMBRE) PER_NOMBRE_MAYUS, "
					+ " INITCAP(PER.APELLIDOS1) PER_APELLIDOS1, "
					+ " UPPER(PER.APELLIDOS1) PER_APELLIDOS1_MAYUS, "
					+ " INITCAP(PER.APELLIDOS2) PER_APELLIDOS2, "
					+ " UPPER(PER.APELLIDOS2) PER_APELLIDOS2_MAYUS, "
					+ " PER.NIFCIF PER_NIFCIF, "
					+ " PER.sexo PER_SEXO, "
					+ " DECODE(PER.SEXO, 'H','o','a') PER_O_A, "
					+ " DECODE(PER.SEXO, 'H','el','la') PER_EL_LA, "
					+ " DIR.DOMICILIO DIR_DOMICILIO, "
					+ " POB1.NOMBRE NOMBRE_POBLACION, "
					+ " PRO1.NOMBRE NOMBRE_PROVINCIA, "
					+ " DIR.CODIGOPOSTAL DIR_CODIGOPOSTAL, "			
					+ " TE.NOMBRE TE_NOMBRE, "
					+ " TE.ESGENERAL TE_ESGENERAL, "
					+ " CLA.NOMBRE CLA_NOMBRE, "
					+ " FASE.NOMBRE FASE_NOMBRE, "
					+ " EST.NOMBRE EST_NOMBRE, "
					+ " EST.IDFASE AS EST_IDFASE, "
					+ " EST.IDESTADO AS EST_IDESTADO, "
					+ " EST.ESEJECUCIONSANCION EST_ESEJECUCIONSANCION, "
					+ " EST.ESFINAL EST_ESFINAL, "
					+ " EST.ESAUTOMATICO EST_ESAUTOMATICO, "
					+ " EST.DESCRIPCION EST_DESCRIPCION, "
					+ " EST.IDFASE_SIGUIENTE EST_IDFASE_SIGUIENTE, "
					+ " EST.IDESTADO_SIGUIENTE EST_IDESTADO_SIGUIENTE, "
					+ " EST.MENSAJE EST_MENSAJE, "
					+ " EST.PRE_SANCIONADO EST_PRE_SANCIONADO, "
					+ " EST.PRE_VISIBLE EST_PRE_VISIBLE, "
					+ " EST.PRE_VISIBLEENFICHA EST_PRE_VISIBLEENFICHA, "
					+ " EST.POST_ACTPRESCRITAS EST_POST_ACTPRESCRITAS, "
					+ " EST.POST_SANCIONPRESCRITA EST_POST_SANCIONPRESCRITA, "
					+ " EST.POST_SANCIONFINALIZADA EST_POST_SANCIONFINALIZADA, "
					+ " EST.POST_ANOTCANCELADAS EST_POST_ANOTCANCELADAS, "
					+ " EST.POST_VISIBLE EST_POST_VISIBLE, "
					+ " EST.POST_VISIBLEENFICHA EST_POST_VISIBLEENFICHA, "
					+ " IVA.DESCRIPCION IVA_DESCRIPCION, "
					+ " F_SIGA_GETRECURSO(RES.DESCRIPCION,:2) RES_DESCRIPCION, "
					+ " RES.CODIGOEXT RES_CODIGOEXT, "
					+ " RES.BLOQUEADO RES_BLOQUEADO, "
					+ " JUZ.NOMBRE JUZ_NOMBRE, "
					+ " JUZ.DOMICILIO JUZ_DOMICILIO, "
					+ " JUZ.CODIGOPOSTAL JUZ_CODIGOPOSTAL, "
					+ " JUZ.IDPOBLACION JUZ_IDPOBLACION, "
					+ " JUZ.IDPROVINCIA JUZ_IDPROVINCIA, "
					+ " F_SIGA_GETRECURSO(POB.NOMBRE, :3) JUZ_POBLACION, "
					+ " F_SIGA_GETRECURSO(PRO.NOMBRE, :4) JUZ_PROVINCIA, "
					+ " JUZ.TELEFONO1 JUZ_TELEFONO1, "
					+ " JUZ.TELEFONO2 JUZ_TELEFONO2, "
					+ " JUZ.FAX1 JUZ_FAX1, "
					+ " TO_CHAR(JUZ.FECHABAJA, 'dd-mm-yyyy') AS JUZ_FECHABAJA, "
					+ " JUZ.CODIGOEXT JUZ_CODIGOEXT, "
					+ " JUZ.CODIGOPROCURADOR JUZ_CODIGOPROCURADOR, "
					+ " JUZ.VISIBLE JUZ_VISIBLE, "
					+ " PROC.NOMBRE PROC_NOMBRE, "
					+ " PROC.PRECIO PROC_PRECIO, "
					+ " PROC.IDJURISDICCION PROC_IDJURISDICCION, "
					+ " PROC.CODIGO PROC_CODIGO, "
					+ " PROC.COMPLEMENTO PROC_COMPLEMENTO, "
					+ " PROC.VIGENTE PROC_VIGENTE, "
					+ " PROC.ORDEN PROC_ORDEN ");
			sql.append(" FROM EXP_EXPEDIENTE EXP, "
					+ " EXP_DENUNCIADO DEN, "
					+ " CEN_CLIENTE CLI, "
					+ " CEN_PERSONA PER, "
					+ " EXP_TIPOEXPEDIENTE TE, "
					+ " EXP_CLASIFICACION CLA, "
					+ " EXP_FASES FASE, "
					+ " PYS_TIPOIVA IVA, "
					+ " EXP_ESTADO EST, "
					+ " EXP_TIPORESULTADORESOLUCION RES, "
					+ " SCS_JUZGADO  JUZ, "
					+ " CEN_POBLACIONES POB, "
					+ " CEN_PROVINCIAS PRO, "
					+ " CEN_DIRECCIONES DIR, "
					+ " CEN_PROVINCIAS PRO1, "
					+ " CEN_POBLACIONES POB1, "
					+ " SCS_PROCEDIMIENTOS PROC, "
					+ " SCS_MATERIA  MATE, "
					+ " SCS_AREA AREA ");
			sql.append(" WHERE EXP.IDINSTITUCION = DEN.IDINSTITUCION AND EXP.IDTIPOEXPEDIENTE = DEN.IDTIPOEXPEDIENTE AND EXP.IDINSTITUCION_TIPOEXPEDIENTE = DEN.IDINSTITUCION_TIPOEXPEDIENTE AND EXP.NUMEROEXPEDIENTE = DEN.NUMEROEXPEDIENTE AND EXP.ANIOEXPEDIENTE = DEN.ANIOEXPEDIENTE AND DEN.IDDENUNCIADO = "+ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL+" AND DEN.IDINSTITUCION = CLI.IDINSTITUCION AND DEN.IDPERSONA = CLI.IDPERSONA   AND CLI.IDPERSONA = PER.IDPERSONA   AND EXP.IDINSTITUCION_TIPOEXPEDIENTE = TE.IDINSTITUCION   AND EXP.IDTIPOEXPEDIENTE = TE.IDTIPOEXPEDIENTE   AND EXP.IDINSTITUCION_TIPOEXPEDIENTE = CLA.IDINSTITUCION(+)    AND EXP.IDCLASIFICACION = CLA.IDCLASIFICACION(+)  ");
			sql.append(" AND EXP.IDTIPOEXPEDIENTE = CLA.IDTIPOEXPEDIENTE(+)    AND EXP.IDINSTITUCION = FASE.IDINSTITUCION (+)   AND EXP.IDTIPOEXPEDIENTE = FASE.IDTIPOEXPEDIENTE (+)   AND EXP.IDFASE = FASE.IDFASE (+)   AND EXP.IDINSTITUCION_TIPOEXPEDIENTE = EST.IDINSTITUCION(+)   AND EXP.IDFASE = EST.IDFASE(+)   AND EXP.IDESTADO = EST.IDESTADO(+) ");
			sql.append(" AND EXP.IDTIPOEXPEDIENTE = EST.IDTIPOEXPEDIENTE(+)   AND EXP.IDTIPOIVA = IVA.IDTIPOIVA(+)   AND EXP.IDINSTITUCION = RES.IDINSTITUCION(+)   AND EXP.IDRESULTADOJUNTAGOBIERNO = RES.IDTIPORESULTADO(+)   AND EXP.IDINSTITUCION_JUZ = JUZ.IDINSTITUCION(+)   AND EXP.JUZGADO = JUZ.IDJUZGADO(+)   AND JUZ.IDPROVINCIA = PRO.IDPROVINCIA(+) ");
			sql.append(" AND JUZ.IDPOBLACION = POB.IDPOBLACION(+)   AND EXP.IDINSTITUCION_PROC = PROC.IDINSTITUCION(+)   AND EXP.PROCEDIMIENTO = PROC.IDPROCEDIMIENTO(+) ");
			sql.append(" AND exp.IDINSTITUCION = dir.idinstitucion(+)  AND EXP.IDPERSONA = dir.idpersona(+)  AND exp.iddireccion = dir.iddireccion(+)  AND dir.idpoblacion = pob1.idpoblacion(+)  AND dir.idprovincia = pro1.idprovincia(+) ");
			sql.append(" and EXP.idMateria = mate.idmateria(+) and EXP.idinstitucion =mate.idinstitucion(+) and EXP.idarea =mate.idarea(+) and mate.idarea = area.idarea(+) ");
   
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND EXP.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucionTipoExp);
			sql.append(" AND EXP.IDINSTITUCION_TIPOEXPEDIENTE = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTipoExp);
			sql.append(" AND EXP.IDTIPOEXPEDIENTE = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(" AND EXP.ANIOEXPEDIENTE = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(" AND EXP.NUMEROEXPEDIENTE = :");
			sql.append(keyContador);
			
/*
			if(idPersona!=null){
				keyContador++;
				htCodigos.put(new Integer(keyContador), idPersona); 
			
				sql.append(" AND CLI.IDPERSONA = :");
				sql.append(keyContador);
			}
*/			

			HelperInformesAdm helperInformes = new HelperInformesAdm();	

			if(isInforme){
				datos = helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos); 
				
			}else{
				RowsContainer rc = new RowsContainer();
				
				if (rc.queryBind(sql.toString(), htCodigos)) {
					datos = new Vector();
					for (int h=0;h<rc.size();h++) {
						datos.add(((Row)rc.get(h)).getRow());
					}
					
				}
			}
			
			if(datos!=null && datos.size()>0){
				 Hashtable expedienteHashtable = (Hashtable) datos.get(0);
				 //BNS FALLABA CUANDO VENÍA UN TIPOEJG COMO " "
				 String sTipoEJG = UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_IDTIPOEJG);
				 if (sTipoEJG == null)
					 sTipoEJG = "";
				 if(!sTipoEJG.trim().equals("")){
					 ScsEJGAdm ejgAdm = new ScsEJGAdm(usrbean);
					 Hashtable haste = ejgAdm.getDatosEjg(UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_IDINSTITUCION), UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_ANIOEJG), UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_NUMEROEJG), UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_IDTIPOEJG));
					 String SUFIJO = (String) haste.get("SUFIJO");
					 String CODIGO = (String) haste.get("CODIGO");
					 String codigoEjg = "";
					 if (SUFIJO != null && !SUFIJO.equals("")) {
							codigoEjg = CODIGO + "-" + SUFIJO;
							
					} else {
						codigoEjg = CODIGO;
					}
					 if(codigoEjg==null)
						 codigoEjg = "";	
					 expedienteHashtable.put("EJG_ANIO", UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_ANIOEJG));
					 expedienteHashtable.put("EJG_NUMEJG", UtilidadesHash.getString(expedienteHashtable, ExpExpedienteBean.C_ANIOEJG)+" / "+codigoEjg);
					 expedienteHashtable.put("EJG_CODIGO", codigoEjg);
					 expedienteHashtable.put("EJG_SOLICITANTE_NIF", UtilidadesHash.getString(haste, "NIF"));
					 expedienteHashtable.put("EJG_SOLICITANTE_NOMBRE", UtilidadesHash.getString(haste, "NOMBRE"));
					 expedienteHashtable.put("EJG_SOLICITANTE_APELLIDO1", UtilidadesHash.getString(haste, "APELLIDO1"));
					 expedienteHashtable.put("EJG_SOLICITANTE_APELLIDO2", UtilidadesHash.getString(haste, "APELLIDO2"));
					 
					 
				 }else{
					 expedienteHashtable.put("EJG_ANIO", "");
					 expedienteHashtable.put("EJG_NUMEJG", "");
					 expedienteHashtable.put("EJG_CODIGO", "");
					 expedienteHashtable.put("EJG_SOLICITANTE_NIF", "");
					 expedienteHashtable.put("EJG_SOLICITANTE_NOMBRE", "");
					 expedienteHashtable.put("EJG_SOLICITANTE_APELLIDO1", "");
					 expedienteHashtable.put("EJG_SOLICITANTE_APELLIDO2", "");
					
					 
				 }
				
			}
			
			// RGG 19/10/2009 PROCESO DE CARGA DE DATOS EN UN SEGUNDO RECORRIDO
			// campos configurables
			datos = helperInformes.getCamposConfigurablesExpediente(datos,idInstitucion,idInstitucionTipoExp,idTipoExp,anio, numero, idPersona);
			// historico de anotaciones
			datos = helperInformes.getAnotacionesExpediente(datos,idInstitucion,idInstitucionTipoExp,idTipoExp,anio, numero, idPersona);
			// Nombres implicados separados por comas
			datos = helperInformes.getNombresImplicadosExpediente(datos,idInstitucion,idInstitucionTipoExp,idTipoExp,anio, numero, idPersona);
			// Nombres de partes separados por comas
			datos = helperInformes.getNombresPartesExpediente(datos,idInstitucion,idInstitucionTipoExp,idTipoExp,anio, numero, idPersona, this.usrbean.getLanguage());			
			// Implicados y direcciones
			datos = helperInformes.getImplicadosDireccionesExpediente(datos,idInstitucion,idInstitucionTipoExp,idTipoExp,anio, numero, idPersona, this.usrbean.getLanguage(), isASolicitantes);		
			
		
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ExpExpedienteAdm.getDatosInformeExpediente.");
		}
		return datos;
	}
	
	/**
	 * @return lista de Expedientes relacionados con un EJG dado
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getRelacionadoConEjg(String institucion, String anio, String numero, String idTipo) throws ClsExceptions, SIGAException
	{
		StringBuffer sql = new StringBuffer();
		sql.append("");
		sql.append("SELECT 'EXPEDIENTE' AS SJCS, ");
		sql.append("       EXP.IDINSTITUCION IDINSTITUCION, ");
		sql.append("       EXP.ANIOEXPEDIENTE ANIO, ");
		sql.append("       ' ' AS NUMERO, ");
		sql.append("       EXP.IDPERSONA IDLETRADO, ");
		sql.append("       PER.APELLIDOS1||' '||PER.APELLIDOS2||', '||PER.NOMBRE AS NOMBRELETRADO, ");
		sql.append("       ' ' AS IDTURNO, ");
		sql.append("       EXP.IDTIPOEXPEDIENTE  AS IDTIPO, ");
		sql.append("       EXP.NUMEROEXPEDIENTE  AS CODIGO, ");
		sql.append("       EXT.NOMBRE AS DES_TIPO, ");
		sql.append("       ' ' AS DES_TURNO ");
		sql.append("  FROM EXP_EXPEDIENTE   EXP,  CEN_PERSONA   PER, EXP_TIPOEXPEDIENTE EXT ");
		sql.append(" WHERE EXP.ANIOEJG = " + anio + " ");
		sql.append("   AND EXP.NUMEROEJG = " + numero + " ");
		sql.append("   AND EXP.IDTIPOEJG = " + idTipo + " ");
		sql.append("   AND EXP.IDINSTITUCION_TIPOEXPEDIENTE = " + institucion + " ");
		sql.append("   AND EXP.IDPERSONA= PER.IDPERSONA ");
		sql.append("   AND EXP.IDTIPOEXPEDIENTE = EXT.IDTIPOEXPEDIENTE");
		sql.append("   AND Exp.Idinstitucion_Tipoexpediente = Ext.Idinstitucion");

		try {
			return this.selectGenerico(sql.toString());
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion sobre las relaciones de un ejg.");
		}
	}

	public boolean tienePermisos (String institucion, String perfil, Integer idTipoExpediente) throws ClsExceptions,SIGAException 
	{
		try {
	            	            
			String sql ="SELECT " +
		     " max(decode(DERECHOACCESO, 1, 10, DERECHOACCESO)) as TIPOACCESO "+
			" FROM EXP_PERMISOSTIPOSEXPEDIENTES WHERE IDINSTITUCION =" +institucion+" " +
		     " AND IDPERFIL IN ('"+perfil+"') AND idtipoexpediente = " +idTipoExpediente;
						
			Hashtable contador = (Hashtable)((Vector)this.selectGenerico(sql)).get(0);
			//devolverá true si el contador es = 0
			String resultado = (String)contador.get("TIPOACCESO");
			if (resultado!=null && resultado.equalsIgnoreCase("3"))
				return true;
			else 
				return false;
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de un ejg.");
		}

		
	}
	//aalg: función para obtener los idPersona de los denunciados del expediente
	public Vector getDenunciados(String institucion, String instiTipo, String anio, String numero, String idTipo) throws ClsExceptions, SIGAException
	{
		StringBuffer sql = new StringBuffer();
		sql.append("");
		sql.append("SELECT IDPERSONA ");
		sql.append("FROM EXP_DENUNCIADO ");
		sql.append("WHERE IDINSTITUCION = " + institucion + " ");
		sql.append("AND IDTIPOEXPEDIENTE = " + idTipo + " ");
		sql.append("AND NUMEROEXPEDIENTE = "  + numero + " ");
		sql.append("AND ANIOEXPEDIENTE = " + anio + " ");
		sql.append("AND IDINSTITUCION_TIPOEXPEDIENTE = " + instiTipo + " ");

		try {
			return this.selectGenerico(sql.toString());
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion sobre los denunciantes de un expediente.");
		}
	}

	
	
	
}