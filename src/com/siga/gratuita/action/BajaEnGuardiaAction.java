package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
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
 * @version adrian.ayala 16/05/2008: Algo de limpieza
 */

public class BajaEnGuardiaAction extends MasterAction
{
	/**
	 * Desde las pestanhas no es necesario pasar un formulario.
	 * Este execute lanza el abrir de este action en caso de que no se reciba ningun formulario.
	 * En caso contrario, lanza el execute normal del MasterAction.
	 */
	public ActionForward executeInternal (ActionMapping mapping,
										  ActionForm formulario,
										  HttpServletRequest request,
										  HttpServletResponse response)
			throws SIGAException
	{
		try
		{
			MasterForm miForm = null;
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				return super.executeInternal(mapping, formulario,request,response);
			}
			else{
				return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
			}
		} 
		catch(Exception e)
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	} //executeInternal ()
	
	
	protected String abrir(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		try
		{
			return "edicion";
		}
		catch(Exception e)
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try
		{
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
	
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			String consulta =  " SELECT guardi.nombre,"+
						" DECODE(turnos.Guardias, 0, 'Obligatorias',1,'Todas o Ninguna',2,'A Elección') GUARDIAS, "+
						" DECODE(guardi.tipodias, 'T', 'Todos', 'L', 'Laborables', 'F', 'Festivos', 'D', 'Festivos, Sábados y Domingos') TIPODIAS, "+
						" guardi.diasguardia DIASGUARDIA, guardi.numeroletradosguardia NUMEROLETRADOSGUARDIA, guardi.numerosustitutosguardia NUMEROSUSTITUTOSGUARDIA " +
						" FROM SCS_TURNO turnos, SCS_GUARDIASTURNO guardi "+
						" WHERE "+
						" turnos.idinstitucion 	= guardi.idinstitucion and "+
						" turnos.idturno 		= guardi.idturno and "+
						" turnos.idinstitucion 	= "+(String)usr.getLocation()+" AND "+
						" turnos.idturno 		= "+request.getSession().getAttribute("IDTURNO");
			Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
			request.setAttribute("resultado",resultado);
			request.setAttribute("fechabaja",miForm.getFechaBaja());
			request.setAttribute("observacionesbaja",miForm.getObservacionesBaja());
			// Metemos el action, modo y el titulo ya que es una pagina compartida
			request.setAttribute("TITULO","gratuita.editarGuardiaTurno.literal.baja3_3");
			request.setAttribute("texto","gratuita.bajaGuardia.literal.infTodasNinguna");
			request.setAttribute("modo","modificar");
			request.setAttribute("action","JGR_BajaEnGuardia.do");
		}
		catch (Exception e) 
		{
			throwExcp("error.messages.abrirAvanzada",e,null); 
		} 
		return "listado";
	}
	/**
	 * Antes de editar se hace una consulta a bbddm para recuperar todos los campos de la
	 * guardia seleccionada.
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException {
		String forward="exception";
		try{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			Hashtable hash = new Hashtable();
			Hashtable turno =(Hashtable)request.getSession().getAttribute("turnoElegido");
			hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hash.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)turno.get("IDTURNO"));
			hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA,miForm.getGuardiaElegida());
      
			ScsGuardiasTurnoBean resultado = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);			//aqui está seleccionada la guardia que queremos editar
			request.getSession().setAttribute("DATABACKUP",resultado);
			request.getSession().setAttribute("DATABACKUPPESTANA",resultado);
			request.getSession().setAttribute("IDTURNO",(String)turno.get("IDTURNO"));
			request.getSession().setAttribute("FECHASUSCRIPCION",miForm.getFechaInscripcion());
			request.getSession().setAttribute("inscripcion","0");
			request.getSession().setAttribute("modo","ver");
			
			// Dependiendo de numero de pantallas que sean, ponemos un titulo u otro.
			request.getSession().setAttribute("GUARDIAS",miForm.getGuardias());
			if (miForm.getGuardias().equals("2")) {
				CenClienteAdm admCliente = new CenClienteAdm(this.getUserBean(request)); 
				/*
				 * Devuelve el nivel de error:
				 * 0: NO hay error
				 * 1: Error, tiene guardias pendientes
				 * -1: Excepcion
				 */
				Long idPersona = new Long((String)request.getSession().getAttribute("idPersonaTurno"));
				Integer idInstitucion = new Integer((String)usr.getLocation());
				Integer idTurno = new Integer((String)turno.get("IDTURNO"));
				Integer idGuardia = new Integer(miForm.getGuardiaElegida());
				int error = admCliente.tieneGuardiasSJCSPendientes(idPersona, idInstitucion, idTurno, idGuardia);
				if (error == 1)
					return exitoModalSinRefresco("error.message.guardiasSolicitarBaja", request);
				else if (error == -1)
					throw new SIGAException (admCliente.getError());
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.baja1_2");
			} else
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.baja1_3");
			
			request.setAttribute("action","/JGR_BajaEnGuardia.do");
			forward="editar";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try
		{
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			request.setAttribute("GUARDIA",miForm.getGuardia());
			if(request.getSession().getAttribute("GUARDIAS").equals("2"))
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.baja2_2");
			else
				request.setAttribute("titulo","gratuita.editarGuardiaTurno.literal.baja2_3");
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return "nuevo";
	}
	
	protected String insertar(	ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;
		
		try
		{
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
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
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInscripcion()));
				hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,miForm.getObservacionesInscripcion());

				if(inscripcionGuardia.insert(hash))
				{
					request.setAttribute("mensaje","messages.inserted.success");
				}
				else
				{
					request.setAttribute("mensaje","messages.inserted.error");
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
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInscripcion()));
				hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,miForm.getObservacionesInscripcion());
								
				for(int x=0;x<resultado.size();x++)
				{
					hashSelect = (Hashtable)resultado.get(x);
					hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA,hashSelect.get("IDGUARDIA"));
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
		return "exito";
	}
					
		
	
	protected String modificar(	ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response) throws SIGAException {
		
		String forward="exception";
		try
		{
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardia = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Hashtable hash = new Hashtable();
			//claves
			hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA,request.getSession().getAttribute("idPersonaTurno"));
			hash.put(ScsInscripcionGuardiaBean.C_IDTURNO,request.getSession().getAttribute("IDTURNO"));
			//campos modificable
			hash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaBaja()));
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,miForm.getObservacionesBaja());
			hash.put(ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,"sysdate");
			hash.put(ScsInscripcionGuardiaBean.C_USUMODIFICACION,usr.getUserName());
			// preparamos el update
			String campos[] = {ScsInscripcionGuardiaBean.C_FECHABAJA,
					ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,
					ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,
					ScsInscripcionGuardiaBean.C_USUMODIFICACION};

			boolean result = false;
			String guardias = (String) request.getSession().getAttribute("GUARDIAS"); 
			if(guardias.equals("2"))
			{
				String claves[] = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA,
						ScsInscripcionGuardiaBean.C_IDTURNO,
						ScsInscripcionGuardiaBean.C_IDGUARDIA,
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA,miForm.getGuardia());
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,request.getSession().getAttribute("FECHASUSCRIPCION"));
				result = inscripcionGuardia.updateDirect(hash,claves,campos);
			}
			else
			{
				String claves[] = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA,
						ScsInscripcionGuardiaBean.C_IDTURNO};
				result = inscripcionGuardia.updateDirect(hash,claves,campos);
			}
			
			if(result)
				forward = this.exitoModal("messages.updated.success",request);
			else
				throw new ClsExceptions("Error al realizar la inscripcion en la guardia");
			
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 

		request.setAttribute("modal","1");
		return forward;
	}

}