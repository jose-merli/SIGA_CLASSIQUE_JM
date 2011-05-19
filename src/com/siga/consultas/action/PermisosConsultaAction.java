/*
 * Created on Mar 10, 2005
 * @author emilio.grau
 *
 */
package com.siga.consultas.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.administracion.form.InformeForm;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.ConConsultaAdm;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.ConConsultaPerfilAdm;
import com.siga.beans.ConConsultaPerfilBean;
import com.siga.consultas.form.PermisosConsultaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

/**
 * Action para los permisos sobre consultas
 */
public class PermisosConsultaAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
        try{
        	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        	
        	String idInstitucion = userBean.getLocation();
        	String idInstitucion_Consulta = request.getParameter("idInstitucion");
        	String idConsulta = request.getParameter("idConsulta");
        	String tipoConsulta = request.getParameter("tipoConsulta");
        	request.setAttribute("tipoConsulta",tipoConsulta);
        	
        	//Recuperamos la descripcion de la consulta
        	Hashtable hash = new Hashtable();		
			hash.put(ConConsultaBean.C_IDINSTITUCION,idInstitucion_Consulta);
			hash.put(ConConsultaBean.C_IDCONSULTA,idConsulta);
			ConConsultaAdm consAdm = new ConConsultaAdm (this.getUserBean(request));
			ConConsultaBean consBean = (ConConsultaBean)consAdm.selectByPK(hash).elementAt(0);
			request.setAttribute("nombreConsulta",consBean.getDescripcion());
			
			//Recuperamos los permisos de la consulta
			Vector datos = consAdm.obtenerPermisosGrupos(idConsulta,idInstitucion_Consulta,idInstitucion);
			request.setAttribute("datos",datos);
			InformeForm informeForm = new InformeForm();
			informeForm.setIdInstitucion(idInstitucion);
			informeForm.setAlias(consBean.getDescripcion());
			informeForm.setIdTipoInforme(AdmTipoInformeBean.TIPOINFORME_CONSULTAS);
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			List<InformeForm> listadoInformes = informeService.getInformesConsulta(consBean,informeForm,userBean);
			request.setAttribute("listadoInformes", listadoInformes);
			

        }catch(Exception e){
        	throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
        }        
    
        return "inicio";
	}
    
    /* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
    	try{
    		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
    		
	    	PermisosConsultaForm form = (PermisosConsultaForm)formulario;
	    	String gruposConAcceso = form.getGrupos();
	    	String gruposAntiguos = form.getGruposAntiguos();
	    	String idConsulta = form.getIdConsulta();
	    	String idInstitucion_Consulta = form.getIdInstitucion_Consulta();
	    	
	    	ConConsultaPerfilAdm cpAdm = new ConConsultaPerfilAdm (this.getUserBean(request));
	    	ConConsultaPerfilBean cpBean = new ConConsultaPerfilBean();
	    	cpBean.setIdConsulta(Integer.valueOf(idConsulta));
	    	cpBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
	    	cpBean.setIdInstitucion_Consulta(Integer.valueOf(idInstitucion_Consulta));
	    	
	    	UserTransaction tx = userBean.getTransaction();
	    	
	    	try
		    {
		        tx.begin();
		    
	    	
		    	if (gruposConAcceso!=null)
	    	    {
	    	        StringTokenizer st = new StringTokenizer(gruposConAcceso, ",");
	    	        
	    	        while (st.hasMoreTokens())
	    	        {
	    	            boolean bProcesar=true;
	    	            String grupo = st.nextToken();
	    	            StringTokenizer st2 = new StringTokenizer(gruposAntiguos, ",");
	    	            
	    	            while (st2.hasMoreTokens())
	    	            {
	    	                String grupoAux = st2.nextToken();
	    	                if (grupo.equals(grupoAux))
	    	                {
	    	                    bProcesar=false;        	                    
	    	                }
	    	            }
	    	              
	    	            if (bProcesar)
	    	            {
	        	            cpBean.setIdPerfil(grupo);
	        	            cpAdm.insert(cpBean);
	    	            }
	    	        }
	    	    }
	
	    	    if (gruposAntiguos!=null)
	    	    {
	    	        String gruposBorrar="";
	    	        StringTokenizer st = new StringTokenizer(gruposAntiguos, ",");
	    	        
	    	        while (st.hasMoreTokens())
	    	        {
	    	            boolean bProcesar=true;
	    	            String grupo = st.nextToken();
	    	            StringTokenizer st2 = new StringTokenizer(gruposConAcceso, ",");
	    	            
	    	            while (st2.hasMoreTokens())
	    	            {
	    	                String grupoAux = st2.nextToken();
	    	                if (grupo.equals(grupoAux))
	    	                {
	    	                    bProcesar=false;        	                    
	    	                }
	    	            }
	    	              
	    	            if (bProcesar)
	    	            {
	    	            	cpBean.setIdPerfil(grupo);
	    	            	cpAdm.delete(cpBean);
	    	            }
	    	        }
	    	    }
	    	    tx.commit();
	    	    
		    }catch (Exception e) {
		    	throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,tx); 
		        return exito("messages.updated.error",request);
		    }
	    	   
    	}catch (Exception e){
    		throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
    	}
    	
    	return exitoRefresco("messages.updated.success",request);
    	
	}
}
