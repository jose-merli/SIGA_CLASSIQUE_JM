/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.SIGAServiceException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.atos.utils.ClsConstants;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpDenunciadoAdm;
import com.siga.beans.ExpDenunciadoBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.expedientes.form.ExpDocumentacionForm;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.DocumentacionRegTelAction;

/**
 * Action de la documentaci�n de un expediente
 */
public class ExpDocumentacionRegTelAction extends DocumentacionRegTelAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
    	String salto=null;
    	
        try{
        	
        	request.getSession().removeAttribute("DATAPAGINADOR");
        	request.getSession().removeAttribute("DATABACKUP_REGTEL");
        	
	        ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
	        
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");	
			
			
	               
			
	        //Recuperamos los datos del expediente			
	        Hashtable hash = new Hashtable();		
			hash.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
			hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
			
			
	        ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
	        Vector vExp = expAdm.selectByPK(hash);        
	        ExpExpedienteBean expExpedienteBean = (ExpExpedienteBean)vExp.elementAt(0);
	        
	        //recuperamos el nombre del denunciado principal
	        ExpDenunciadoAdm denunciadoAdm = new ExpDenunciadoAdm (this.getUserBean(request));
	        CenPersonaBean denunciadoPpal = denunciadoAdm.getPersonaDenunciadoById(Integer.valueOf(idInstitucion), Integer.valueOf(idInstitucion_TipoExpediente), Integer.valueOf(idTipoExpediente), numExpediente,Integer.valueOf(anioExpediente), ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL);
	        String nombrePersona = denunciadoPpal.getNombreCompleto();
	        request.setAttribute("denunciado", nombrePersona);
	        	        
	        String denunciado="";
			//Chapuza para identificar si se llama denunciante o impugnante
			StringBuffer sbWhere= new StringBuffer("where ");
			sbWhere.append(ExpCampoTipoExpedienteBean.C_IDINSTITUCION).append("=").append(idInstitucion)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE).append("=").append(idTipoExpediente)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDCAMPO).append("=").append(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_NOMBRE).append(" like ").append("'%impugnante%'");
			ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
			Vector resultado=campoTipoExpedienteAdm.select(sbWhere.toString());
	        if (resultado!=null && resultado.size()>0) {
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.impugnado");
	    	} else {
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.denunciado");
	        }

	        StringBuffer tituloVentana = new StringBuffer();
	        tituloVentana.append(denunciado).append(": ").append(nombrePersona).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	        			 .append(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.tipo"))
	        			 .append(": ").append((String)request.getParameter("nombreTipoExpediente")).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	        			 .append(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.nexpediente"))
	        			 .append(": ").append((String)request.getParameter("anioExpediente")).append(" / ").append((String)request.getParameter("numeroExpediente"));

	        form.setTituloVentana(tituloVentana.toString());
	        	        
	        if (expExpedienteBean.getIdentificadorDS() == null || expExpedienteBean.getIdentificadorDS().trim().equals("")) {
	        	GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		        String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		        
	        	if (valor!=null && valor.equals("1")){
	        		ExpTipoExpedienteAdm expTipoExpedienteAdm = new ExpTipoExpedienteAdm(getUserBean(request));
	        		if (expExpedienteBean.getIdInstitucion_tipoExpediente() != null && expExpedienteBean.getIdTipoExpediente() != null) {
	        			Vector v = expTipoExpedienteAdm.select(expExpedienteBean.getIdInstitucion_tipoExpediente().toString(), expExpedienteBean.getIdTipoExpediente().toString());
	        			if (v != null && v.size() == 1) {
	        				ExpTipoExpedienteBean expTipoExpedienteBean = (ExpTipoExpedienteBean) v.get(0);
	        				short idInstitucionShort = getIDInstitucion(request).shortValue();
	        				DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucionShort);
	        				String identificadorDS = docuShareHelper.buscaCollectionExpedientes(DocuShareHelper.getTitleExpedientes(expTipoExpedienteBean.getNombre(), anioExpediente, numExpediente));
	        				if (identificadorDS != null && !identificadorDS.trim().equals("")) {
	        					expExpedienteBean.setIdentificadorDS(identificadorDS);
	        					expAdm.update(expExpedienteBean);
	        				}
	        			}
	        		}
		        }
			}
	        	        	        
			request.setAttribute("IDENTIFICADORDS", expExpedienteBean.getIdentificadorDS());
			request.getSession().setAttribute("DATABACKUP_REGTEL", hash);
						
			request.getSession().removeAttribute("MIGAS_DS");						
			
			salto = "inicioDS";
			
		} catch (SIGAServiceException e) {
			throw new SIGAException(e.getMessage(), e);

		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
        }
        
        return salto;
	}



	@Override
	protected String createCollection(MasterForm formulario, HttpServletRequest request) throws Exception {		
		Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP_REGTEL");
		String idDS = null;
		
		ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
        Vector vExp = expAdm.selectByPK(hash);
        ExpExpedienteBean expExpedienteBean = (ExpExpedienteBean)vExp.elementAt(0);
        
        if (expExpedienteBean != null && expExpedienteBean.getIdInstitucion_tipoExpediente() != null && expExpedienteBean.getIdTipoExpediente() != null) {
        	ExpTipoExpedienteAdm expTipoExpedienteAdm = new ExpTipoExpedienteAdm(getUserBean(request));
			Vector v = expTipoExpedienteAdm.select(expExpedienteBean.getIdInstitucion_tipoExpediente().toString(), expExpedienteBean.getIdTipoExpediente().toString());
			if (v != null && v.size() == 1) {
				ExpTipoExpedienteBean expTipoExpedienteBean = (ExpTipoExpedienteBean) v.get(0);
				String title = DocuShareHelper.getTitleExpedientes(expTipoExpedienteBean.getNombre(), expExpedienteBean.getAnioExpediente().toString(), expExpedienteBean.getNumeroExpediente().toString());
				
				short idInstitucionShort = getIDInstitucion(request).shortValue();
				
				DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucionShort);
				idDS = docuShareHelper.createCollectionExpedientes(title,"");
				expExpedienteBean.setIdentificadorDS(idDS);
				expAdm.updateDirect(expExpedienteBean);
			}
        }
		
		request.getSession().removeAttribute("DATABACKUP_REGTEL");
		return idDS;
	}
}
