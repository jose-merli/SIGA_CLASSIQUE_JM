package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirTurnosForm;


/**
 * @author ruben.fernandez
 */

public class BusquedaTurnosAction extends MasterAction {

	protected String editar(ActionMapping mapping, MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	protected String ver(	ActionMapping mapping, MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
			return null;
	}
	
	protected String modificar(	ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String borrar(ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String buscarPor(	ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {

		request.getSession().removeAttribute("ocultos");
		request.getSession().removeAttribute("campos");
		DefinirTurnosForm form = (DefinirTurnosForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		try {

			String entrada = (String)request.getSession().getAttribute("entrada");
			ScsTurnoAdm turno = new ScsTurnoAdm(usr);
			Hashtable hash = (Hashtable)form.getDatos();
			String where="  where scs_ordenacioncolas.idordenacioncolas = turnos.idordenacioncolas "+
			" AND area.idinstitucion    = materi.idinstitucion"+
			" AND area.idarea       = materi.idarea"+
			" AND materi.idinstitucion              = turnos.idinstitucion"+
			" AND materi.idarea                     = turnos.idarea"+
			" AND materi.idmateria                  = turnos.idmateria"+
			" AND zona.idinstitucion             	= turnos.idinstitucion"+
			" AND zona.idzona                    	= turnos.idzona"+
			" AND subzon.idinstitucion           (+)= turnos.idinstitucion"+
			" AND subzon.idzona              (+)= turnos.idzona"+
			" AND subzon.idsubzona           (+)= turnos.idsubzona"+
			" AND partid.idinstitucion           (+)= turnos.idinstitucion"+
			" AND partid.idpartidapresupuestaria (+)= turnos.idpartidapresupuestaria"+
			" AND grupof.idinstitucion              = turnos.idinstitucion"+
			" AND grupof.idgrupofacturacion         = turnos.idgrupofacturacion"+
			" AND parjud.idpartido               (+)= subzon.idpartido"+
			" AND turnos.idinstitucion =" + usr.getLocation();
			//this.prepararBusqueda(hash);
			try{
				Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"));
			}catch(Exception e){hash.put("IDPARTIDOJUDICIAL","-1");}

			if(!((String)hash.get("ABREVIATURA")).equalsIgnoreCase("")){
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hash.get("ABREVIATURA")).trim(),"turnos."+ScsTurnoBean.C_ABREVIATURA);
			}
			if(!((String)hash.get("NOMBRE")).equalsIgnoreCase("")){
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hash.get("NOMBRE")).trim(),"turnos."+ScsTurnoBean.C_NOMBRE);
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDAREA"))>0))where+=" and ";
			if(Integer.parseInt((String)hash.get("IDAREA"))>0){
				where+=	" AND area.idarea = "+(String)hash.get("IDAREA");
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDMATERIA"))>0))where+=" and ";
			try{
				if(Integer.parseInt((String)hash.get("IDMATERIA"))>0){
					where+=	" AND materi.idmateria ="+(String)hash.get("IDMATERIA");
					// ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
				}
			}catch(Exception e){}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDZONA"))>0))where+=" and ";
			String idzon = "";
			if (hash.get("IDZONA")!=null && !hash.get("IDZONA").equals("0")&& !hash.get("IDZONA").equals("")) {
				idzon=(String)hash.get("IDZONA");
				//idzon=idzon.substring(idzon.indexOf(","),idzon.length());
				if(Integer.parseInt(idzon)>0){
					where+=	" AND zona.idzona ="+idzon;
					//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDZONA+" = "+((String)hash.get("IDZONA")).toUpperCase();
				}
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDSUBZONA"))>0))where+=" and ";
			try{
				if(Integer.parseInt((String)hash.get("IDSUBZONA"))>0){
					where+=	" AND subzon.idsubzona = "+(String)hash.get("IDSUBZONA");
				}
			}catch(Exception e){}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0))where+=" and ";
			//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDSUBZONA+"="+form.getSubzona()+" and "+
			if(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0){
				where+=	" AND partid.idpartidapresupuestaria ="+((String)hash.get("IDPARTIDAPRESUPUESTARIA")).toUpperCase();
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDPARTIDAPRESUPUESTARIA+" = "+((String)hash.get("IDPARTIDAPRESUPUESTARIA")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDGRUPOFACTURACION"))>0))where+=" and ";
			if(Integer.parseInt((String)hash.get("IDGRUPOFACTURACION"))>0){
				where+=	" AND grupof.idgrupofacturacion        (+)= "+(String)hash.get("IDGRUPOFACTURACION");
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDGRUPOFACTURACION+" LIKE "+((String)hash.get("IDGRUPOFACTURACION")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"))>0))where+=" and ";
			//		try{
			//			if(Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"))>0){
			//			where+=	" AND parjud.idpartido               ="+(String)hash.get("IDPARTIDOJUDICIAL");
			//			}
			//		}catch(Exception e){}

			if(form.getTurnosBajaLogica().equalsIgnoreCase("N")){
				where+=	" AND turnos.visibilidad = '1'";
			}

			request.getSession().setAttribute("DATOSFORMULARIO",hash);
			request.getSession().setAttribute("BUSQUEDAREALIZADA","SI");

			Vector vTurno = turno.selectTurnos(where); //el segundo par�metro sirve para indicarle al m�todo que los campos a recuperar en el select son:
			//abreviatura,nombre, area, materia, zona, subzona, partidoJudicial,partidaPresupuestaria, grupoFacturacion
			request.setAttribute("BAJALOGICATURNOS",form.getTurnosBajaLogica());
			request.setAttribute("resultado",vTurno);
			request.setAttribute("mantTurnos","1");

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}

		return "listado";
	}


	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		
		String recargar = (String)request.getAttribute("recargar");
		if ((recargar!=null)&&(recargar.equalsIgnoreCase("si"))){
			return this.abrirAvanzada(mapping, formulario, request, response);
		}else{
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATOSFORMULARIO");
			request.getSession().removeAttribute("BUSQUEDAREALIZADA");
			request.getSession().removeAttribute("accionTurno");
			request.getSession().removeAttribute("pestanas");
			return "inicio";
		}
	}

	/** 
	 *  Funcion que atiende la accion abrirAvanzada
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirAvanzada (ActionMapping mapping, 		
		  	 MasterForm formulario, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws ClsExceptions{
			
		//borrar variables de sesi�n
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accionTurno");
		request.getSession().removeAttribute("pestanas");
		return "inicio";
	}
}