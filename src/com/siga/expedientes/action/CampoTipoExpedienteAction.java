/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.atos.utils.Validaciones;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpPestanaConfAdm;
import com.siga.beans.ExpPestanaConfBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.expedientes.form.CampoTipoExpedienteForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de campos de expediente.<br/>
 * Gestiona la edicion, borrado y creación de los campos. 
 *
 */
public class CampoTipoExpedienteAction extends MasterAction {
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException
	{	
        
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        ses.removeAttribute("DATABACKUP");
        //Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        CampoTipoExpedienteForm form = (CampoTipoExpedienteForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        
//      Recuperamos el expediente a mostrar
        String institucion = userBean.getLocation();        
        
        String where = " WHERE ";
        
        where += ExpCampoTipoExpedienteBean.C_IDINSTITUCION + " = " + institucion+" AND "+ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
        //Recupero los campos de un tipo de expediente para una institución
        Vector camposExp = campoTipoExpedienteAdm.select(where);
        //Recupero el tipo de expediente para editar el nombre
        Vector tipoExp = tipoExpedienteAdm.select(where);
        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
        
        //seteo los campos del ActionForm de booleanos
        for (int i= 0; i < camposExp.size(); i++){
            ExpCampoTipoExpedienteBean bean = (ExpCampoTipoExpedienteBean)camposExp.elementAt(i);
            establecerCheck((bean.getIdCampo().intValue())-1,bean.getVisible(),bean.getNombre(),form);
        }   
        
        //Seteo el nombre del expediente 
        form.setNombre(beantipoexp.getNombre());
        if (beantipoexp.getTiempoCaducidad()!=null) {
            form.setTiempoCaducidad(beantipoexp.getTiempoCaducidad().toString());        
        }
        if (beantipoexp.getDiasAntelacionCad()!=null) {
            form.setDiasAntelacionCad(beantipoexp.getDiasAntelacionCad().toString());        
        }
        form.setRelacionEJG(beantipoexp.getRelacionEjg().equals(new Integer(1)) );
        //Metemos en el backup los resultados de los campos obtenidos y el tipo de expediente
        Vector backup=new Vector();
        backup.add(0,beantipoexp);
        backup.add(1,camposExp);
        ses.setAttribute("DATABACKUP",backup);       
        
        // RGG: Obtengo los valores de pestanas configuradas
        ExpPestanaConfAdm admPestana= new ExpPestanaConfAdm(this.getUserBean(request));
        ExpPestanaConfBean beanPestana = admPestana.getPestana(1,institucion,form.getIdTipoExpediente(),ClsConstants.IDCAMPO_PARAPESTANACONF1);
        if (beanPestana!=null) {
            form.setChkPestanaConf1((beanPestana.getSeleccionado().intValue()==1)?"1":"0");
            form.setPestanaConf1(beanPestana.getNombre());
        }
        beanPestana = admPestana.getPestana(2,institucion,form.getIdTipoExpediente(),ClsConstants.IDCAMPO_PARAPESTANACONF2);
        if (beanPestana!=null) {
            form.setChkPestanaConf2((beanPestana.getSeleccionado().intValue()==1)?"1":"0");
            form.setPestanaConf2(beanPestana.getNombre());
        }
        
        return "abrir";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    // Recuperamos los datos anteriores de la sesión
	    HttpSession ses=request.getSession();
	    Vector backup=(Vector)ses.getAttribute("DATABACKUP");
	        
	    CampoTipoExpedienteForm form = (CampoTipoExpedienteForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
        ExpTipoExpedienteAdm tipoExpAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));

        if (form.getChkPestanaConf1()!=null && form.getChkPestanaConf1().equals("1") &&
        	form.getChkPestanaConf2()!=null && form.getChkPestanaConf2().equals("1") &&
        	form.getPestanaConf1().equals(form.getPestanaConf2())) {
        	
        	return exito("messages.expedientes.nombrePestanyasIgual.error",request);
        }
        
        //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();     

	        //Actualizamos los registros de campos de expediente        
	        Vector camposExp=(Vector)backup.elementAt(1); 
	        
	        // Actualizamos el nombre del expediente
	        ExpTipoExpedienteBean tipoExp=(ExpTipoExpedienteBean)backup.elementAt(0);	    
	        tipoExp.setNombre(form.getNombre());
	        if (Validaciones.validaNoInformado(form.getTiempoCaducidad())) {
		        tipoExp.setTiempoCaducidad(0);
	        } else {
	        	tipoExp.setTiempoCaducidad(new Integer(form.getTiempoCaducidad()));
	        }
	        if (Validaciones.validaNoInformado(form.getDiasAntelacionCad())) {
	        	tipoExp.setDiasAntelacionCad(0);
	        } else {
	        	tipoExp.setDiasAntelacionCad(new Integer(form.getDiasAntelacionCad()));
	        }
	  	        //  Modificamos el bean antiguo
	        tipoExp.setRelacionEjg(form.isRelacionEJG()?new Integer(1):new Integer(0));
	        tipoExpAdm.update(tipoExp);        
	        
	        ExpCampoTipoExpedienteBean beanNexpDisciplinario = (ExpCampoTipoExpedienteBean)camposExp.elementAt(0);
	        //  Modificamos el bean antiguo
	        beanNexpDisciplinario.setVisible(form.getNexpDisciplinario()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        beanNexpDisciplinario.setNombre(form.getNombreCampoNumExp());
	        campoTipoExpedienteAdm.update(beanNexpDisciplinario);
	        
	        ExpCampoTipoExpedienteBean beanEstado = (ExpCampoTipoExpedienteBean)camposExp.elementAt(1);
	        //  Modificamos el bean antiguo
	        beanEstado.setVisible(form.getEstado()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanEstado);
	       

	        ExpCampoTipoExpedienteBean beanInstitucion = (ExpCampoTipoExpedienteBean)camposExp.elementAt(2);
	        //  Modificamos el bean antiguo
	        beanInstitucion.setVisible(form.getInstitucion()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanInstitucion);
	       
	        ExpCampoTipoExpedienteBean beanAsuntoJudicial = (ExpCampoTipoExpedienteBean)camposExp.elementAt(3);
	        //  Modificamos el bean antiguo
	        beanAsuntoJudicial.setVisible(form.getAsuntoJudicial()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanAsuntoJudicial);
	       
	        ExpCampoTipoExpedienteBean beanAlertas = (ExpCampoTipoExpedienteBean)camposExp.elementAt(4);
	        //  Modificamos el bean antiguo
	        beanAlertas.setVisible(form.getAlertas()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanAlertas);
	       
	        ExpCampoTipoExpedienteBean beanDocumentacion = (ExpCampoTipoExpedienteBean)camposExp.elementAt(5);
	        //  Modificamos el bean antiguo
	        beanDocumentacion.setVisible(form.getDocumentacion()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanDocumentacion);
	       
	        ExpCampoTipoExpedienteBean beanSeguimiento = (ExpCampoTipoExpedienteBean)camposExp.elementAt(6);
	        //  Modificamos el bean antiguo
	        beanSeguimiento.setVisible(form.getSeguimiento()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanSeguimiento);
	       
	        ExpCampoTipoExpedienteBean beanDenunciantes = (ExpCampoTipoExpedienteBean)camposExp.elementAt(7);
	        //  Modificamos el bean antiguo
	        beanDenunciantes.setVisible(form.getDenunciantes()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        beanDenunciantes.setNombre(form.getNombreCampoDenunciante());
	        campoTipoExpedienteAdm.update(beanDenunciantes);
	        
	        ExpCampoTipoExpedienteBean beanDenunciados = (ExpCampoTipoExpedienteBean)camposExp.elementAt(14);
	        //  Modificamos el bean antiguo
	        beanDenunciados.setVisible(form.getDenunciados()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        beanDenunciados.setNombre(form.getNombreCampoDenunciado());
	        campoTipoExpedienteAdm.update(beanDenunciados);
	       
	        ExpCampoTipoExpedienteBean beanPartes = (ExpCampoTipoExpedienteBean)camposExp.elementAt(8);
	        //  Modificamos el bean antiguo
	        beanPartes.setVisible(form.getPartes()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanPartes);
		    
	        ExpCampoTipoExpedienteBean beanResolucion = (ExpCampoTipoExpedienteBean)camposExp.elementAt(9);
	        //  Modificamos el bean antiguo
	        beanResolucion.setVisible(form.getResolucion()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanResolucion);

	        ExpCampoTipoExpedienteBean beanMinuta = (ExpCampoTipoExpedienteBean)camposExp.elementAt(10);
	        //  Modificamos el bean antiguo
	        beanMinuta.setVisible(form.getMinutaInicial()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanMinuta);
	        
	        ExpCampoTipoExpedienteBean beanFinal = (ExpCampoTipoExpedienteBean)camposExp.elementAt(15);
	        //  Modificamos el bean antiguo
	        beanFinal.setVisible(form.getMinutaFinal()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanFinal);
	        
	        ExpCampoTipoExpedienteBean beanDerechos = (ExpCampoTipoExpedienteBean)camposExp.elementAt(16);
	        //  Modificamos el bean antiguo
	        beanDerechos.setVisible(form.getDerechos()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanDerechos);	        

	        ExpCampoTipoExpedienteBean beanResolucionInforme = (ExpCampoTipoExpedienteBean)camposExp.elementAt(11);
	        //  Modificamos el bean antiguo
	        beanResolucionInforme.setVisible(form.getResultadoInforme()?ExpCampoTipoExpedienteBean.si:ExpCampoTipoExpedienteBean.no);
	        campoTipoExpedienteAdm.update(beanResolucionInforme);
	        
	        // RGG: Actualizo las pestañas configurables
	        ExpPestanaConfAdm admPestana= new ExpPestanaConfAdm(this.getUserBean(request));
	        ExpPestanaConfBean beanPestana = admPestana.getPestana(1,userBean.getLocation(),form.getIdTipoExpediente(),ClsConstants.IDCAMPO_PARAPESTANACONF1);
	        if (beanPestana!=null) {
	            beanPestana.setSeleccionado((form.getChkPestanaConf1()!=null && form.getChkPestanaConf1().equals("1"))?new Integer(1):new Integer(0));
	            beanPestana.setNombre(form.getPestanaConf1());
	            if (!admPestana.update(beanPestana)) {
	                throw new ClsExceptions("Error al actualizar pestaña configurable. "+admPestana.getError());
	            }
	        }
	        admPestana= new ExpPestanaConfAdm(this.getUserBean(request));
	        beanPestana = admPestana.getPestana(2,userBean.getLocation(),form.getIdTipoExpediente(),ClsConstants.IDCAMPO_PARAPESTANACONF2);
	        if (beanPestana!=null) {
	            beanPestana.setSeleccionado((form.getChkPestanaConf2()!=null && form.getChkPestanaConf2().equals("1"))?new Integer(1):new Integer(0));
	            beanPestana.setNombre(form.getPestanaConf2());
	            if (!admPestana.update(beanPestana)) {
	                throw new ClsExceptions("Error al actualizar pestaña configurable. "+admPestana.getError());
	            }
	        }
	        
	        tx.commit();
	        ClsLogging.writeFileLog("Transacción de Campos realizada.",10);        
	        request.setAttribute("mensaje","messages.updated.success");
	               
	    } catch (Exception e) {        
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
	    }      
        
 	    return "exito";
	}	

	protected void establecerCheck(int idCampo,String visible,String nombre, CampoTipoExpedienteForm form){
		
		int campo = idCampo + 1;
	    switch(campo){	    
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_N_DISCIPLINARIO:
		    	if(nombre ==null||nombre.equals(""))
		    		nombre = ExpCampoTipoExpedienteBean.NUMEXPEDIENTE;
		    	form.setNombreCampoNumExp(nombre);
		        form.setNexpDisciplinario(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_ESTADO:
		        form.setEstado(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_INSTITUCION:
		        form.setInstitucion(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_ASUNTOJUDICIAL:
		        form.setAsuntoJudicial(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_ALERTAS:
		        form.setAlertas(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DOCUMENTACION:
		        form.setDocumentacion(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_SEGUIMIENTO:
		        form.setSeguimiento(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE:
		    	if(nombre ==null||nombre.equals(""))
		    		nombre = ExpCampoTipoExpedienteBean.DENUNCIANTE;
		    	form.setNombreCampoDenunciante(nombre);
		        form.setDenunciantes(visible.equals("S"));
		        break;
		    case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_PARTES:
		        form.setPartes(visible.equals("S"));
		        break;    
			case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_RESOLUCION:
		        form.setResolucion(visible.equals("S"));
		        break;	        	
			case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_MINUTA_INICIAL:
		        form.setMinutaInicial(visible.equals("S"));
		        break;	        
			case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_MINUTA_FINAL:
		        form.setMinutaFinal(visible.equals("S"));
		        break;
			case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_RESULTADO_INFORME:
		        form.setResultadoInforme(visible.equals("S"));
		        break;
			case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DERECHOS_COLEGIALES:
		        form.setDerechos(visible.equals("S"));
		        break;		        
			case ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIADO:
				if(nombre ==null||nombre.equals(""))
		    		nombre = ExpCampoTipoExpedienteBean.DENUNCIADO;
		    	form.setNombreCampoDenunciado(nombre);
		        form.setDenunciados(visible.equals("S"));
		        break;
	    }
	}
}
