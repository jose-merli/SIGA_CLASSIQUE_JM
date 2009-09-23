package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;


/**
 * Clase que realiza la incripcion/baja de guardias para un turno determinado 
 * 
 * @author carlos.vidal
 * 
 */

public class InscribirseEnGuardiaAction extends MasterAction {
	/**
	 * Desde las pestanhas no es necesario pasar un formulario.
	 * Este execute lanza el abrir de este action en caso de que no se reciba ningun formulario.
	 * En caso contrario, lanza el execute normal del MasterAction.
	 */
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		try {
			MasterForm miForm = null;
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				return super.executeInternal(mapping, formulario,request,response);
			}
			else{
				return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
			}
		} 
		catch (SIGAException es) {
			throw new SIGAException("messages.general.error",es,new String[] {"modulo.gratuita"});
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

		
	protected String abrir(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
			return "edicion";
	}
	
	
	/**
	 * Esta clase se encarga de hacer una consulta para sacar todas las guardias
	 * para un turno seleccionado, o todas las guardias en las que está o ha estado
	 * inscrito el letrado logado. 
	 * El resutado lo manda como variable del request a la jsp que se encargará de mostrar una tabla.
	 * No se espera un formulario porque los dato de consulta se obtienen del UsrBean y de una variable de sesion
	 * que correspondería con el turno seleccionado.
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward="exception";
		try{
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
			Hashtable turno =(Hashtable) request.getSession().getAttribute("turnoElegido");
			String entrada = (String)request.getSession().getAttribute("entrada");  //esta variable solo es temporal , en el momento de tener acceso desde el menu, se debe consultar por Session.EsLetrado()
			String consulta="";
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			if (entrada.equalsIgnoreCase("1"))	{
					consulta =  " SELECT "+guardias.getCamposTabla(1)+
								" ,SCS_TURNO.GUARDIAS GUARDIAS FROM SCS_GUARDIASTURNO, SCS_TURNO" +
								" WHERE"+
								" SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION AND " +
								" SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO AND " +
								" SCS_TURNO.IDINSTITUCION = "+(String)usr.getLocation()+//la del turno del que procedemos
								" AND SCS_TURNO.IDTURNO = "+(String)turno.get("IDTURNO");//la del turno del que procedemos
					
			}else{					//Es letrado
					consulta =	" SELECT "+guardias.getCamposTabla(3)+ 
							  	" ,SCS_TURNO.GUARDIAS GUARDIAS FROM SCS_GUARDIASTURNO, SCS_TURNO"+
							 	" WHERE "+
							 	" SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION AND " +
								" SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO AND " +
								" SCS_TURNO.IDINSTITUCION = "+(String)usr.getLocation()+//la del turno del que procedemos
								" AND SCS_TURNO.IDTURNO = "+(String)turno.get("IDTURNO");//la del turno del que procedemos
					
			}
			Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
			request.getSession().setAttribute("resultado",resultado);
			forward="listado";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
			return forward;
	}

	protected String buscar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{
		try
		{
			//Controles
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			
			String consulta =
				"SELECT guardi.nombre," +
				"       DECODE (turnos.Guardias," +
				"               0, 'Obligatorias'," +
				"               1, 'Todas o Ninguna'," +
				"               2, 'A Elección'" +
				"              ) GUARDIAS," +
				"       guardi.seleccionlaborables," +
				"       guardi.seleccionfestivos," +
				"       guardi.diasguardia DIASGUARDIA," +
				"       guardi.numeroletradosguardia NUMEROLETRADOSGUARDIA," +
				"       guardi.numerosustitutosguardia NUMEROSUSTITUTOSGUARDIA" +
				"" +
				"  FROM SCS_TURNO turnos, SCS_GUARDIASTURNO guardi" +
				"" +
				" WHERE turnos.idinstitucion 	= guardi.idinstitucion" +
				"   and turnos.idturno 		= guardi.idturno" +
				"   and turnos.idinstitucion 	= "+(String)usr.getLocation() +
				"   and turnos.idturno 		= "+request.getSession().getAttribute("IDTURNO") +
				"" +
				" ORDER BY guardi.nombre";
			Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
			request.setAttribute("resultado",resultado);
			request.setAttribute("fechainscripcion",miForm.getFechaInscripcion());
			request.setAttribute("observacionesinscripcion",miForm.getObservacionesInscripcion());
			// Metemos el action, modo y el titulo ya que es una pagina compartida
			request.setAttribute("TITULO","gratuita.editarGuardiaTurno.literal.alta3_3");
			request.setAttribute("modo","insertar");
			request.setAttribute("action","JGR_InscribirseEnGuardia.do");
			request.setAttribute("texto","gratuita.altaGuardia2.literal.infTodasNinguna");
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		
		return "listado";
	} //buscar ()
	
	/**
	 * Antes de editar se hace una consulta a bbddm para recuperar todos los campos de la
	 * guardia seleccionada.
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException {
		String forward="exception";
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		Vector ocultos = (Vector) miForm.getDatosTablaOcultos(0);
		Hashtable hash = new Hashtable();
		Hashtable turno =(Hashtable)request.getSession().getAttribute("turnoElegido");
		hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
		hash.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)turno.get("IDTURNO"));
		hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA,miForm.getGuardiaElegida());
      
		try{
			//Vector vP = (Vector)guardias.select(hash);
			ScsGuardiasTurnoBean resultado = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);			//aqui está seleccionada la guardia que queremos editar
			request.getSession().setAttribute("DATABACKUPPESTANA",resultado);
			request.getSession().setAttribute("inscripcion","1");
			request.getSession().setAttribute("modo","ver");
			request.getSession().setAttribute("IDTURNO",(String)turno.get("IDTURNO"));
			
			// Dependiendo de numero de pantallas que sean, ponemos un titulo u otro.
			request.getSession().setAttribute("GUARDIAS",miForm.getGuardias());
			if(miForm.getGuardias().equals("2"))
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.alta1_2");
			else
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.alta1_3");
			
			request.setAttribute("action","/JGR_InscribirseEnGuardia.do");
			forward="editar";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Antes de ver se hace una consulta a bbddm para recuperar todos los campos de la
	 * guardia seleccionada.
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward="exception";
		try{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			String entrada = (String)request.getSession().getAttribute("entrada");
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			Vector ocultos = (Vector) miForm.getDatosTablaOcultos(0);
			Hashtable hash = new Hashtable();
			Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
			hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hash.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)turno.get("IDTURNO"));
			hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA,(String)ocultos.get(1));
      
			ScsGuardiasTurnoBean resultado = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);			//aqui está seleccionada la guardia que queremos editar
			request.getSession().setAttribute("DATABACKUP",resultado);
			request.getSession().setAttribute("modo","ver");
			if (entrada.equalsIgnoreCase("1"))forward="ver";
			else forward="verLetrado";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "nuevo";
		try
		{
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			request.setAttribute("GUARDIA",miForm.getGuardia());
			if(request.getSession().getAttribute("GUARDIAS").equals("2"))
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.alta2_2");
			else
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.alta2_3");
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	protected String insertar(	ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response) throws SIGAException {
		String forward="exito";
		UserTransaction tx = null;
		RowsContainer rc=null;
		boolean existe=false;
		try
		{
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector ocultos 	= (Vector)request.getSession().getAttribute("ocultos");
			Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardia = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			// si todas o ninguna, se hace el insert de todas las guardias. (Repetir la select anterior)
			String guardias = (String) request.getSession().getAttribute("GUARDIAS"); 
			if(guardias.equals("2"))
			{
				Hashtable hash = new Hashtable();
				hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,usr.getLocation());
				hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA,request.getSession().getAttribute("idPersonaTurno"));
				hash.put(ScsInscripcionGuardiaBean.C_IDTURNO,request.getSession().getAttribute("IDTURNO"));
				hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA,miForm.getGuardia());
				hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,miForm.getObservacionesInscripcion());

				tx = usr.getTransaction(); 
				tx.begin();
				/*
				// Si es una actualizacion, se borran los registros y se realiza el insert.
				if(request.getSession().getAttribute("esActualizacion")!=null)
				{
					// Ponemos la fechainscripcion antigua para el borrado y seguidamente ponemos la nueva
					hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,(String)request.getSession().getAttribute("esActualizacion")); 
					request.getSession().removeAttribute("esActualizacion");
					inscripcionGuardia.delete(hash);
				}*/
				//hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInscripcion())); 
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,"sysdate");	
				
//		 Antes de insertar scs_inscripcionguardia miramos si dicha guardia no ha sido dado de alta por otro usuario
				String select=
					"Select 1 from "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+
					" where "+	ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDINSTITUCION +" = " + usr.getLocation()+
					" and "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDTURNO+" = " + request.getSession().getAttribute("IDTURNO")+
					" and "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDPERSONA+" = " + request.getSession().getAttribute("idPersonaTurno")+
					" and "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDGUARDIA+" = " + miForm.getGuardia()+
					" and "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHABAJA+" is null";
					
				rc = new RowsContainer();
				
				if(rc.find(select)){
					existe=true;
					
				}else{
					existe=false;
				}		
			  if (!existe){	
				if(inscripcionGuardia.insert(hash))
				{
					tx.commit();
					request.setAttribute("mensaje","messages.inserted.success");
				}
				else
				{
					tx.rollback();
					request.setAttribute("mensaje","messages.inserted.error");
				}
			 }else{
			  	 request.setAttribute("mensaje","update.compare.diferencias");
			  	   tx.rollback();
			  }
			}
			else
			{
				// Obtenemos todas las guardias del turno y hacemos los insert correspondientes
				ScsGuardiasTurnoAdm scsGuardiasTurnoAdm = new ScsGuardiasTurnoAdm(this.getUserBean(request));
				String consulta =  " SELECT IDGUARDIA"+
				" FROM SCS_GUARDIASTURNO "+
				" WHERE "+
				" idinstitucion 	= "+(String)usr.getLocation()+" AND "+
				" idturno 		= "+request.getSession().getAttribute("IDTURNO");
				Vector resultado = (Vector)scsGuardiasTurnoAdm.ejecutaSelect(consulta);
				Hashtable hash  = new Hashtable();
				Hashtable hashSelect  = new Hashtable();
				boolean result = false;
				// Recorremos el vector
				tx = usr.getTransaction(); 
				tx.begin();
				hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,usr.getLocation());
				hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA,request.getSession().getAttribute("idPersonaTurno"));
				hash.put(ScsInscripcionGuardiaBean.C_IDTURNO,request.getSession().getAttribute("IDTURNO"));
				//hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInscripcion())); 
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,"sysdate");

				hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,miForm.getObservacionesInscripcion());
								
				for(int x=0;x<resultado.size();x++)
				{
					hashSelect = (Hashtable)resultado.get(x);
					hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA,hashSelect.get("IDGUARDIA"));
					// Si es una actualizacion, se borra el registro y se realiza el insert.
					/*if(request.getSession().getAttribute("esActualizacion")!=null)
					{
						// Ponemos la fechainscripcion antigua para el borrado y seguidamente ponemos la nueva
						hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,(String)request.getSession().getAttribute("esActualizacion")); 
						request.getSession().removeAttribute("esActualizacion");
						inscripcionGuardia.delete(hash);
					}*/
					result = inscripcionGuardia.insert(hash);
				}
				
				if(result)
				{
					tx.commit();
					request.setAttribute("mensaje","messages.inserted.success");
				}
				else
				{
					tx.rollback();
					request.setAttribute("mensaje","messages.inserted.error");
				}
			}
		} 
		catch (Exception e) 
		{
			throwExcp("error.messages.insertar",e,tx); 
		} 

		request.setAttribute("modal","1");
		return forward;
	}
					
		
	
	protected String modificar(	ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response) throws SIGAException {
		return null;
	}
	
	protected String borrar(ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException {
		return null;
	}
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;
	}
	

}