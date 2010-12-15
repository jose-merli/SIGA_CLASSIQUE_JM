package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGrupoGuardiaAdm;
import com.siga.beans.ScsGrupoGuardiaBean;
import com.siga.beans.ScsGrupoGuardiaColegiadoAdm;
import com.siga.beans.ScsGrupoGuardiaColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.form.ColaGuardiasForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;
import com.siga.informes.InformeColaGuardias;

/**
 * Action de Colas de Oficios.
 * @author cristina.santos
 * @since 13/02/2006
 */
public class ColaGuardiasAction extends MasterAction {
	
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm != null)&&(miForm.getModo()!=null)&&(miForm.getModo().equals("imprimir"))){
				return mapping.findForward(this.imprimir(mapping, miForm, request, response));
			}
			else if((miForm != null)&&(miForm.getModo()!=null)&&(miForm.getModo().equals("fijarUltimoLetrado"))){
				return mapping.findForward(this.fijarUltimoLetrado(mapping, miForm, request, response));
			}
			else{
				return super.executeInternal(mapping, formulario, request, response);
			}
		
		} catch (SIGAException e) {
			throw e;
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		}
	}

	
	/** 
	 * Método que atiende la accion abrir. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(
			ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "inicio";
	}
	
	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String ver (
			ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		ColaGuardiasForm coForm=(ColaGuardiasForm)formulario;
		HttpSession ses = request.getSession();
		Hashtable turnoElegido = (Hashtable)ses.getAttribute("turnoElegido");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN"); 
		ScsGuardiasTurnoAdm guaAdm = new ScsGuardiasTurnoAdm(usr);
		
		Integer usuario=this.getUserName(request);
		String institucion =usr.getLocation();
		String turno =(String)turnoElegido.get("IDTURNO");
		String guardia=coForm.getIdGuardia();
		String fecha  = coForm.getFechaConsulta();
		fecha = (fecha!=null&&!fecha.trim().equals(""))?fecha:null;
		ScsSaltosCompensacionesAdm saltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
		
		//Cargar último letrado
		cargarUltimoLetrado(this.getUserBean(request), institucion, turno, guardia, coForm);
		
		//Cargar listado de letrados en cola
		ArrayList<LetradoGuardia> letradosColaGuardiaList = InscripcionGuardia.getColaGuardia(new Integer(institucion),new Integer(turno), new Integer(guardia), fecha,fecha, usr);
		
		
		if(letradosColaGuardiaList!=null && !letradosColaGuardiaList.isEmpty()){
			request.setAttribute("letradosColaGuardiaList",letradosColaGuardiaList);
			if(letradosColaGuardiaList.get(0).getGrupo()!=null){
				request.setAttribute("porGrupos","1");	
			}
		}
		
		//Cargar listado de compensaciones
		Vector vCompensaciones=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, guardia, ClsConstants.COMPENSACIONES);
		if(vCompensaciones!=null && !vCompensaciones.isEmpty()){
			request.setAttribute("vCompensaciones",vCompensaciones);
		}
		//Cargar listado de saltos
		Vector vSaltos=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, guardia, ClsConstants.SALTOS);
		if(vSaltos!=null && !vSaltos.isEmpty()){
			request.setAttribute("vSaltos",vSaltos);
		}

		request.setAttribute("idGuardia", guardia);
		request.setAttribute("idInstitucion", institucion);
		request.setAttribute("idTurno", turno);
		
		ScsInscripcionGuardiaAdm InscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
		Vector letradosinscritos = new Vector();
		String NLetradosInscritos="";
		 letradosinscritos= InscripcionGuardiaAdm.selectGenerico(InscripcionGuardiaAdm.getQueryNumeroColegiadosIncritos(institucion, turno, guardia,fecha));
		 if( letradosinscritos!=null  ||  letradosinscritos.size()>0){			 
			NLetradosInscritos=(String)(((Hashtable)(letradosinscritos.get(0))).get("NLETRADOSINSCRITOS"));
			request.setAttribute("NLETRADOSINSCRITOS",NLetradosInscritos);	
		 }else{			
			request.setAttribute("NLETRADOSINSCRITOS",NLetradosInscritos);	
		 }

		return "ver";
	}
	
	/** 
	 *  Funcion que atiende la accion imprimir. Imprime listas de cola de guardias
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String imprimir (
			ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		String generacionPdfOK = "ERROR";
		String salida = null;
		try {		
			//Obtengo el bean de la facturacion:
			// Nombre de la plantilla FO:
			String nombreFicheroFO = ClsConstants.PLANTILLA_FO_COLAGUARDIAS;
			
			InformeColaGuardias reportColaGuardias = new InformeColaGuardias(this.getUserBean(request));
			
			//Generamos el Informe si la hash no es null:			
			if (reportColaGuardias.generarInforme(request, nombreFicheroFO))
				generacionPdfOK = "OK";			
			else
				generacionPdfOK = "ERROR";
			
			request.setAttribute("generacionOK",generacionPdfOK);			
			salida = "descarga";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return salida;	
	}
	
	/**
	 * Invoca varias clases para obtener el ultimo letrado con numero de colegiado, nombre y apellidos
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param turno Codigo turno seleccionado
	 * @param turno Codigo guardia seleccionada
	 * @param coForm Formulario en que se cargan los valores
	 * @throws ClsExceptions Error interno
	 */	
	protected void cargarUltimoLetrado(
			UsrBean usuario, 
			String institucion, 
			String turno,
			String guardia,
			ColaGuardiasForm coForm) throws ClsExceptions{
		
		ScsGuardiasTurnoAdm guardiasTurnoAdm = new ScsGuardiasTurnoAdm(usuario);
		CenPersonaAdm personaAdm= new CenPersonaAdm(usuario);
		CenColegiadoAdm colegiadoAdm= new CenColegiadoAdm(usuario);
		
		//buscar persona ultimo turno 
		Hashtable hashTurno = new Hashtable();
		hashTurno.put(ScsGuardiasTurnoBean.C_IDTURNO,turno);
		hashTurno.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,institucion);
		hashTurno.put(ScsGuardiasTurnoBean.C_IDGUARDIA,guardia);
		ScsGuardiasTurnoBean guardiasTurnoBean = (ScsGuardiasTurnoBean)((Vector)guardiasTurnoAdm.select(hashTurno)).get(0);
		Long ultimo=guardiasTurnoBean.getIdPersona_Ultimo();
		
		if(ultimo!=null){
			//buscar numero colegiado
			Hashtable hashColegiado = new Hashtable();
			hashColegiado.put(CenColegiadoBean.C_IDINSTITUCION,institucion);
			hashColegiado.put(CenColegiadoBean.C_IDPERSONA,ultimo);
			CenColegiadoBean colegiadoBean = (CenColegiadoBean)((Vector)colegiadoAdm.select(hashColegiado)).get(0);
			coForm.setNColegiado(colegiadoAdm.getIdentificadorColegiado (colegiadoBean));
			
			//buscar datos persona
			Hashtable hashPersona = new Hashtable();
			hashPersona.put(CenPersonaBean.C_IDPERSONA,ultimo);
			CenPersonaBean personaBean= (CenPersonaBean)((Vector)personaAdm.select(hashPersona)).get(0);

			coForm.setNombre(personaBean.getNombre());
			coForm.setApellido1(personaBean.getApellido1() + " " + personaBean.getApellido2());
//			coForm.setApellido2(personaBean.getApellido2());
		}
	}
	

	protected String fijarUltimoLetrado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		try {
			UsrBean usr = this.getUserBean(request);
			ColaGuardiasForm miForm =(ColaGuardiasForm)formulario;
			String guardia = miForm.getIdGuardia();
			Hashtable turnoElegido = (Hashtable)request.getSession().getAttribute("turnoElegido");
			String turno   = (String)turnoElegido.get("IDTURNO");
	
			Hashtable h = new Hashtable ();
			UtilidadesHash.set (h, ScsGuardiasTurnoBean.C_IDGUARDIA, guardia);
			UtilidadesHash.set (h, ScsGuardiasTurnoBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set (h, ScsGuardiasTurnoBean.C_IDTURNO, turno);
			ScsGuardiasTurnoAdm adm = new ScsGuardiasTurnoAdm (usr);
			
			// iniciando transaccion
			tx = usr.getTransactionPesada();
			tx.begin();
			Vector v = adm.selectByPKForUpdate(h);
			if (v != null && v.size() == 1) {
				ScsGuardiasTurnoBean b = (ScsGuardiasTurnoBean) v.get(0);
				// jbd // Si es por grupos tenemos que poner al letrado como el ultimo del grupo
				if(b.getPorGrupos()!=null && b.getPorGrupos().equalsIgnoreCase("1")){
					ScsGrupoGuardiaColegiadoAdm admGrupoGuardia = new ScsGrupoGuardiaColegiadoAdm(this.getUserBean(request));
					admGrupoGuardia.setUltimoDeGrupo(miForm.getIdGrupoGuardiaColegiado());
				}
				b.setIdPersona_Ultimo(new Long(miForm.getIdPersona()));
				if (!adm.update(b)) {
					return exito("messages.updated.error",request);
				}
			}
			tx.commit();
		}
		catch (Exception e) {
			throwExcp("messages.updated.error", new String[] { "modulo.gratuita" }, e, tx);
		}
		return exitoRefresco("messages.updated.success",request);	
	}
	
	protected String modificar(
			ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		UserTransaction tx = null;
		UsrBean usr = this.getUserBean(request);
		ColaGuardiasForm form = (ColaGuardiasForm) formulario;
		ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);
		ScsGrupoGuardiaAdm admGrupo = new ScsGrupoGuardiaAdm(usr);
		String a = form.getDatosModificados();
		
		if (a.length() < 0) {
			throw new ClsExceptions ("messages.updated.error");
		}
			
		String[] elementos = a.split("#;;#");
		String idGrupoGuardiaColegiado;
		String numeroGrupo;
		String idGrupo="";
		String orden;
		Hashtable hash = null;
		String[] campos = {ScsGrupoGuardiaColegiadoBean.C_IDGRUPO, ScsGrupoGuardiaColegiadoBean.C_ORDEN};
		String[] claves = {ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO};
		// iniciando transaccion
		tx = usr.getTransactionPesada();
		try {
			tx.begin();

			for (int i = 0; i < elementos.length; i++) {
				hash=new Hashtable();
				String []aux = elementos[i].split("#;#");
				idGrupoGuardiaColegiado = aux[0];
				numeroGrupo = aux[1];
				orden = aux[2];
				
				String idInstitucion = form.getIdInstitucion();
				String idTurno = form.getIdTurno();
				String idGuardia = form.getIdGuardia();
				
				// Buscamos el idGrupo que corresponde con el numero del grupo
				Hashtable grupoGuardia=admGrupo.getGrupoGuardia(idInstitucion, idTurno, idGuardia, numeroGrupo);
				if(grupoGuardia!=null && grupoGuardia.get(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA)!=null){
					idGrupo=(String)grupoGuardia.get(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
				}else{
					// Si el grupo no existe lo creamos y lo usamos para meter la guardia
					idGrupo = admGrupo.getSecuenciaNextVal(ScsGrupoGuardiaBean.S_SECUENCIA).toString();
					ScsGrupoGuardiaBean bean = new ScsGrupoGuardiaBean();
					bean.setIdGrupoGuardia(Long.valueOf(idGrupo));
					bean.setIdInstitucion(Integer.valueOf(idInstitucion));
					bean.setIdTurno(Integer.valueOf(idTurno));
					bean.setIdGuardia(Integer.valueOf(idGuardia));
					bean.setNumeroGrupo(numeroGrupo);
					bean.setFechaCreacion("sysdate");
					bean.setUsuCreacion(Integer.valueOf(this.getUserBean(request).getUserName()));
					try {
						admGrupo.insert(bean);				
					} catch (Exception e) {
						throwExcp("Error al crear el grupo", new String[] { "modulo.gratuita" }, e, tx);
					}
				}
				
	
				hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO, idGrupoGuardiaColegiado);
				hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO, idGrupo);
				hash.put(ScsGrupoGuardiaColegiadoBean.C_ORDEN, orden);
				try {
					admGrupoColegiado.updateDirect(hash, claves, campos);			
				} catch (Exception e) {
					throwExcp("Error al modificar los grupos de la guardia", new String[] { "modulo.gratuita" }, e, tx);
				}
			}
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.updated.error", new String[] { "modulo.gratuita" }, e, tx);
			
		}
		
		// TODO // jbd // No estaria mal un pequeño control de errores
		// TODO // jbd // Cuando se haya recorrido la lista de cambios hay que comprobar que los grupos se sigan usando porque igual se pueden eliminar los que ya no se usen
		
		return exitoRefresco("messages.updated.success",request);
	}
}