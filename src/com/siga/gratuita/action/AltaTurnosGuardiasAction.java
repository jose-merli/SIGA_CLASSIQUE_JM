package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.CenComponentesAdm;
import com.siga.beans.CenComponentesBean;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsRetencionesAdm;
import com.siga.beans.ScsRetencionesBean;
import com.siga.beans.ScsRetencionesIRPFAdm;
import com.siga.beans.ScsRetencionesIRPFBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AltaTurnosGuardiasForm;
import com.siga.gratuita.form.DefinirTurnosForm;




/**
 * @author carlos.vidal
 */

public class AltaTurnosGuardiasAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */

	public ActionForward executeInternal (ActionMapping mapping,ActionForm formulario,HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir(mapping, miForm, request, response);
						
					}  else if (accion.equalsIgnoreCase("editarTelefonoGuardia")){
						// buscarPersona
						mapDestino = editarTelefonoGuardia(mapping, miForm, request, response);
					
				    }  else if (accion.equalsIgnoreCase("insertarMasivo")){
						// buscarPersona
						mapDestino = insertarMasivo(mapping, miForm, request, response);
					
				    }
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				
				}		
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
		
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("BUSQUEDAREALIZADA");
		request.getSession().removeAttribute("accionTurno");
		request.getSession().removeAttribute("pestanas");
		return "inicio";
			}
	
	private String editarTelefonoGuardia(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		String forward = "";
		String siGuardia="N";
		String idTurno=request.getParameter("idTurno");
		String origen=request.getParameter("origen");
		try {
			// INICIALIZAMOS EL DATABUCKUP
			if(request.getSession().getAttribute("DATABACKUP") != null)
				request.getSession().removeAttribute("DATABACKUP");
			AltaTurnosGuardiasForm altaTurnosForm=(AltaTurnosGuardiasForm) miForm;
			String guardiasSel="";
			guardiasSel=altaTurnosForm.getGuardiasSel();
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm (this.getUserBean(request));
			CenDireccionesAdm dirAdm=new CenDireccionesAdm(this.getUserBean(request));
			CenDireccionesBean dirBean=new CenDireccionesBean();
			String idPersona=(String)request.getSession().getAttribute("idPersonaTurno");
			Integer idInstitucion=altaTurnosForm.getIdInstitucion();
			Hashtable claves=new Hashtable();
			//BUSCAMOS SI EL CLIENTE TIENE ALGUNA DIRECCION DE TIPO GUARDIA
			claves.put(CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
			claves.put(CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
			Vector v = tipoDirAdm.select(claves);
			Hashtable direccion=new Hashtable();
			if(v != null && v.size() != 0)
			{
				for(int i=0;i<v.size();i++){
					CenDireccionTipoDireccionBean beanTipoDirec=new CenDireccionTipoDireccionBean();
					beanTipoDirec=(CenDireccionTipoDireccionBean)v.elementAt(i);
					int tipoDirec=((Integer)beanTipoDirec.getIdTipoDireccion()).intValue();
					Long indicadorDirec=(Long)beanTipoDirec.getIdDireccion();
					if(tipoDirec == ClsConstants.TIPO_DIRECCION_GUARDIA){
						//obtenemos los datos de la dirección
						direccion=dirAdm.selectDirecciones(new Long(idPersona),idInstitucion,indicadorDirec);
						if (direccion !=null){
							//ponemos los datos de la dirección de guardia en el form
							altaTurnosForm.setTelefono1((String)direccion.get(CenDireccionesBean.C_TELEFONO1));
							altaTurnosForm.setTelefono2((String)direccion.get(CenDireccionesBean.C_TELEFONO2));
							altaTurnosForm.setMovil((String)direccion.get(CenDireccionesBean.C_MOVIL));
							altaTurnosForm.setFax1((String)direccion.get(CenDireccionesBean.C_FAX1));
							altaTurnosForm.setFax2((String)direccion.get(CenDireccionesBean.C_FAX2));
							//hacemos el backup
							request.getSession().setAttribute("DATABACKUP", direccion);
							
							siGuardia="S";
							request.setAttribute("idDireccion",String.valueOf(indicadorDirec.longValue()));
						}
						
						//pasamos por request el indicador de que ya tiene dirección de guardia y el indicador de direccion
						// de la direccion de guardia. Nos haran falta a la hora de insertar el nuevo turno
						
						//request.setAttribute("idDireccion",String.valueOf(indicadorDirec.longValue()));
					}
				}
			}
			String GUARDIASSEL		= altaTurnosForm.getGuardiasSel();
			request.setAttribute("tieneYaDirecGuardia",siGuardia);	
			request.setAttribute("titulo","gratuita.editartelefonosGuardia.literal.title");
			request.setAttribute("botones","C,F");
			request.setAttribute("GUARDIASSEL"	,GUARDIASSEL);
		}
		catch(Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		if (origen!=null && origen.equals("altaTurnosMasivo")){
			request.setAttribute("idturno",idTurno);
			return "editarTelefonoGuardiaMasivo";
		}else{
		  return "editarTelefonoGuardia";
		}
	}

	
	/**
	 * Esta clase se encarga de hacer una consulta para sacar todos los turnos disponibles para que un letrado
	 * se inscriba. 
	 * El resutado lo manda como variable del request a la jsp que se encargará de mostrar una tabla.
	/** 
	 *  Funcion que atiende a la peticion 'buscarPor'. Busca los turnos disponibles para un letrado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		protected String buscarPor(ActionMapping mapping, 
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws SIGAException {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			String forward = "listadoResultados";
			try {

				String entrada = (String)request.getSession().getAttribute("entrada");
				ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
				Hashtable hash = (Hashtable)formulario.getDatos();
				String sql = 
					 " SELECT SCS_TURNO.IDTURNO IDTURNO, SCS_TURNO.NOMBRE NOMBRE, SCS_TURNO.ABREVIATURA ABREVIATURA,  SCS_AREA.NOMBRE AREA,   "+
					 " SCS_AREA.IDAREA IDAREA, SCS_MATERIA.NOMBRE MATERIA, SCS_MATERIA.IDMATERIA IDMATERIA,  SCS_ZONA.NOMBRE ZONA,   "+
					 " SCS_ZONA.IDZONA IDZONA, SCS_SUBZONA.NOMBRE SUBZONA, SCS_SUBZONA.IDSUBZONA IDSUBZONA,    "+
					 " Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES("+usr.getLocation()+",SCS_SUBZONA.IDSUBZONA,SCS_ZONA.IDZONA) PARTIDOS,CEN_PARTIDOJUDICIAL.NOMBRE PARTIDOJUDICIAL, CEN_PARTIDOJUDICIAL.IDPARTIDO IDPARTIDOJUDICIAL,    "+
					 " SCS_PARTIDAPRESUPUESTARIA.NOMBREPARTIDA PARTIDAPRESUPUESTARIA,    "+
					 " SCS_PARTIDAPRESUPUESTARIA.IDPARTIDAPRESUPUESTARIA IDPARTIDAPRESUPUESTARIA,    "+
					 UtilidadesMultidioma.getCampoMultidiomaSimple("SCS_GRUPOFACTURACION.NOMBRE", usr.getLanguage())+" GRUPOFACTURACION, SCS_GRUPOFACTURACION.IDGRUPOFACTURACION IDGRUPOFACTURACION,    "+
					 " SCS_TURNO.GUARDIAS GUARDIAS, SCS_TURNO.VALIDARJUSTIFICACIONES VALIDARJUSTIFICACIONES,    "+
					 " SCS_TURNO.VALIDARINSCRIPCIONES VALIDARINSCRIPCIONES, SCS_TURNO.DESIGNADIRECTA DESIGNADIRECTA,    "+
					 " SCS_TURNO.DESCRIPCION DESCRIPCION, SCS_TURNO.REQUISITOS REQUISITOS,    "+
					 " SCS_TURNO.IDPERSONA_ULTIMO IDPERSONAULTIMO, SCS_TURNO.IDPERSONA_ULTIMO IDORDENACIONCOLAS,    "+
					 " SCS_ORDENACIONCOLAS.ALFABETICOAPELLIDOS A8LFABETICOAPELLIDOS, SCS_ORDENACIONCOLAS.FECHANACIMIENTO EDAD,    "+
					 " SCS_ORDENACIONCOLAS.NUMEROCOLEGIADO ANTIGUEDAD, SCS_ORDENACIONCOLAS.ANTIGUEDADCOLA ANTIGUEDADENCOLA,   "+
					 " (select count(1) "+
					 "  from scs_guardiasturno g "+
					 "  where g.idinstitucion=SCS_TURNO.Idinstitucion "+
					 "     and g.idturno=scs_turno.idturno) NGUARDIAS "+
					 
					 " FROM  SCS_TURNO,SCS_PARTIDAPRESUPUESTARIA,SCS_GRUPOFACTURACION,SCS_MATERIA,SCS_AREA,SCS_SUBZONA,SCS_ZONA, CEN_PARTIDOJUDICIAL,SCS_ORDENACIONCOLAS  " +
					 
					 " WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion (+)= SCS_TURNO.idinstitucion    "+
					 " AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria (+)= SCS_TURNO.idpartidapresupuestaria   "+
					 " AND  SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion   "+
					 " AND  SCS_GRUPOFACTURACION.idgrupofacturacion = SCS_TURNO.idgrupofacturacion   "+
					 " AND  SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion " +
					 " AND  SCS_MATERIA.Idmateria = SCS_TURNO.Idmateria " +
					 " AND SCS_Area.idarea = SCS_TURNO.idarea   "+
					 " AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion   "+
					 " AND SCS_AREA.idarea = SCS_MATERIA.idarea " + 
					 " AND SCS_SUBZONA.idinstitucion = SCS_TURNO.idinstitucion    "+
					 " AND SCS_SUBZONA.IDSUBZONA= SCS_TURNO.IDSUBZONA    "+
					 " AND SCS_ZONA.idinstitucion = SCS_TURNO.idinstitucion"+
					 " AND SCS_ZONA.idzona = SCS_TURNO.idzona     "+
					 " and scs_zona.idzona=scs_subzona.idzona "+
					 " AND CEN_PARTIDOJUDICIAL.idpartido (+)= SCS_SUBZONA.idpartido  "+
					 " and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas  " +
					 " AND SCS_TURNO.idinstitucion = "+usr.getLocation()+
					 " AND SCS_TURNO.IDTURNO NOT IN (select idturno from scs_inscripcionturno t where t.idpersona = "+request.getSession().getAttribute("idPersonaTurno")+
					 " and t.idinstitucion = "+usr.getLocation()+" and t.fechabaja is null)";

				try{
					Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"));
				}catch(Exception e){hash.put("IDPARTIDOJUDICIAL","-1");}
								
				if(!((String)hash.get("ABREVIATURA")).equalsIgnoreCase("")){
					sql += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hash.get("ABREVIATURA")).trim(),"SCS_TURNO."+ScsTurnoBean.C_ABREVIATURA);
				}
				if(!((String)hash.get("NOMBRE")).equalsIgnoreCase("")){
					sql += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hash.get("NOMBRE")).trim(),"SCS_TURNO."+ScsTurnoBean.C_NOMBRE);
				}
				//if((ant)&&(Integer.parseInt((String)hash.get("IDAREA"))>0))where+=" and ";
				if(Integer.parseInt((String)hash.get("IDAREA"))>0){
					sql+=	" AND SCS_AREA.idarea = "+(String)hash.get("IDAREA");
						//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
				}
				//if((ant)&&(Integer.parseInt((String)hash.get("IDMATERIA"))>0))where+=" and ";
				try{
					if(Integer.parseInt((String)hash.get("IDMATERIA"))>0){
					sql+=	" AND SCS_MATERIA.idmateria ="+(String)hash.get("IDMATERIA");
					// ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
					}
				}catch(Exception e){}
				//if((ant)&&(Integer.parseInt((String)hash.get("IDZONA"))>0))where+=" and ";
				String idzon = "";
				if (hash.get("IDZONA")!=null && !hash.get("IDZONA").equals("0")&& !hash.get("IDZONA").equals("")) {
					idzon=(String)hash.get("IDZONA");
					//idzon=idzon.substring(idzon.indexOf(","),idzon.length());
					if(Integer.parseInt(idzon)>0){
						sql+=	" AND SCS_ZONA.idzona ="+idzon;
						//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDZONA+" = "+((String)hash.get("IDZONA")).toUpperCase();
					}
				}
				//if((ant)&&(Integer.parseInt((String)hash.get("IDSUBZONA"))>0))where+=" and ";
				try{
					if(Integer.parseInt((String)hash.get("IDSUBZONA"))>0){
					sql+=	" AND SCS_SUBZONA.idsubzona = "+(String)hash.get("IDSUBZONA");
					}
				}catch(Exception e){}
				//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0))where+=" and ";
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDSUBZONA+"="+form.getSubzona()+" and "+
				sql+=" order by SCS_TURNO.NOMBRE";
				request.getSession().setAttribute("DATOSFORMULARIO",hash);
				request.getSession().setAttribute("BUSQUEDAREALIZADA","SI");
				Vector vTurno = turno.ejecutaSelect(sql); //el segundo parámetro sirve para indicarle al método que los campos a recuperar en el select son:
															  //abreviatura,nombre, area, materia, zona, subzona, partidoJudicial,partidaPresupuestaria, grupoFacturacion
				request.setAttribute("resultado",vTurno);
				request.setAttribute("mantTurnos","1");

			}
			catch (Exception e) {
				throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			}
			return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'ver'. Muestra el detalle del turno seleccionado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		UsrBean usr;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			AltaTurnosGuardiasForm miForm = (AltaTurnosGuardiasForm) formulario;			
			String paso	= miForm.getPaso();			
			// Dependiendo de si se solicita la consulta del turno o de las guardias, se ejecuta
			// una opcion u otra.
			if(paso.equals("turno"))
			{
				
				// OBTENEMOS LOS VALORES PARA EL WHERE DE LA CONSULTA.
				// Dependiendo de si es alta o es validacion se obtinenen
				// los datos necesarios. 
				// Alta: idturno
				// Validacion: IDINSTITUCION,IDPERSONA,IDTURNO
				Integer IDTURNO			= miForm.getIdTurno();
				// Consultamos los datos para el turno seleccionado
				String sql = 
					 " SELECT SCS_TURNO.IDTURNO IDTURNO, SCS_TURNO.NOMBRE NOMBRE, SCS_TURNO.ABREVIATURA ABREVIATURA,  SCS_AREA.NOMBRE AREA,   "+
					 " SCS_AREA.IDAREA IDAREA, SCS_MATERIA.NOMBRE MATERIA, SCS_MATERIA.IDMATERIA IDMATERIA,  SCS_ZONA.NOMBRE ZONA,   "+
					 " SCS_ZONA.IDZONA IDZONA, SCS_SUBZONA.NOMBRE SUBZONA, SCS_SUBZONA.IDSUBZONA IDSUBZONA,    "+
					 " Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES("+usr.getLocation()+",SCS_SUBZONA.IDSUBZONA,SCS_ZONA.IDZONA) PARTIDOS,CEN_PARTIDOJUDICIAL.NOMBRE PARTIDOJUDICIAL, CEN_PARTIDOJUDICIAL.IDPARTIDO IDPARTIDOJUDICIAL,    "+
					 " SCS_PARTIDAPRESUPUESTARIA.NOMBREPARTIDA PARTIDAPRESUPUESTARIA,    "+
					 " SCS_PARTIDAPRESUPUESTARIA.IDPARTIDAPRESUPUESTARIA IDPARTIDAPRESUPUESTARIA,    "+
					 UtilidadesMultidioma.getCampoMultidiomaSimple("SCS_GRUPOFACTURACION.NOMBRE",this.getUserBean(request).getLanguage()) + " GRUPOFACTURACION, SCS_GRUPOFACTURACION.IDGRUPOFACTURACION IDGRUPOFACTURACION,    "+
					 " SCS_TURNO.GUARDIAS GUARDIAS, SCS_TURNO.VALIDARJUSTIFICACIONES VALIDARJUSTIFICACIONES,    "+
					 " SCS_TURNO.VALIDARINSCRIPCIONES VALIDARINSCRIPCIONES, SCS_TURNO.DESIGNADIRECTA DESIGNADIRECTA,    "+
					 " SCS_TURNO.DESCRIPCION DESCRIPCION, SCS_TURNO.REQUISITOS REQUISITOS,    "+
					 " SCS_TURNO.IDPERSONA_ULTIMO IDPERSONAULTIMO, SCS_TURNO.IDORDENACIONCOLAS IDORDENACIONCOLAS,    "+
					 " SCS_ORDENACIONCOLAS.ALFABETICOAPELLIDOS ALFABETICOAPELLIDOS, SCS_ORDENACIONCOLAS.FECHANACIMIENTO EDAD,    "+
					 " SCS_ORDENACIONCOLAS.NUMEROCOLEGIADO ANTIGUEDAD, SCS_ORDENACIONCOLAS.ANTIGUEDADCOLA ANTIGUEDADENCOLA   "+
					 " FROM  SCS_TURNO,SCS_PARTIDAPRESUPUESTARIA,SCS_GRUPOFACTURACION,SCS_MATERIA,SCS_AREA,SCS_SUBZONA,SCS_ZONA,    "+
					 " CEN_PARTIDOJUDICIAL,SCS_ORDENACIONCOLAS  WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion (+)= SCS_TURNO.idinstitucion    "+
					 " AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria (+)= SCS_TURNO.idpartidapresupuestaria   "+
					 " AND  SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion   "+
					 " AND  SCS_GRUPOFACTURACION.idgrupofacturacion = SCS_TURNO.idgrupofacturacion   "+
					 " AND  SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion AND SCS_MATERIA.idarea = SCS_TURNO.idarea   "+
					 " AND  SCS_MATERIA.idmateria = SCS_TURNO.idmateria AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion   "+
					 " AND  SCS_AREA.idarea = SCS_MATERIA.idarea AND SCS_SUBZONA.idinstitucion (+)= SCS_TURNO.idinstitucion    "+
					 " AND SCS_SUBZONA.idzona (+)= SCS_TURNO.idzona AND SCS_SUBZONA.idsubzona (+)= SCS_TURNO.idsubzona     "+
					 " AND SCS_ZONA.idinstitucion (+)= SCS_TURNO.idinstitucion AND SCS_ZONA.idzona (+)= SCS_TURNO.idzona     "+
					 " AND CEN_PARTIDOJUDICIAL.idpartido (+)= SCS_SUBZONA.idpartido  "+
					 " and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas "+
					 " AND SCS_TURNO.idinstitucion = "+usr.getLocation()+" and SCS_TURNO.IDTURNO="+IDTURNO +
					 " ORDER BY SCS_TURNO.NOMBRE ";
				Vector vTurno = turno.ejecutaSelect(sql);
				request.setAttribute("action","JGR_AltaTurnosGuardias.do");
				request.setAttribute("modo","nuevo");
				request.setAttribute("paso","1");
				request.setAttribute("idturno",IDTURNO);
				request.setAttribute("titulo","gratuita.consultaTurno.literal.title");
				request.setAttribute("resultado",vTurno);
				request.setAttribute("accion",miForm.getModo());
				
				Hashtable miTurno = (Hashtable)vTurno.get(0);

				// RGG cambio para ordenacion
				ScsOrdenacionColasBean orden = new ScsOrdenacionColasBean();
				CenPersonaBean personaBean = new CenPersonaBean();
				ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(this.getUserBean(request));
				String condicion =" where "+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+"="+(String)miTurno.get("IDORDENACIONCOLAS")+" ";
				Vector vOrdenacion = ordenacion.select(condicion);
				orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);
				
				DefinirTurnosForm form = new DefinirTurnosForm();
				form.setAlfabeticoApellidos(orden.getAlfabeticoApellidos().toString());
				form.setAntiguedad(orden.getNumeroColegiado().toString());
				form.setAntiguedadEnCola(orden.getAntiguedadCola().toString());
				form.setEdad(orden.getFechaNacimiento().toString());
				request.setAttribute("DefinirTurnosFormOrdenacion",form);
				
				forward = "verTurno";
			}
			else if(paso.equals("guardia"))
			{
				Integer IDPERSONA		= miForm.getIdPersona();			
				Integer IDINSTITUCION	= miForm.getIdInstitucion();			
				Integer IDTURNO			= miForm.getIdTurno();			
				Integer GUARDIAS		= miForm.getGuardias();			
				String FECHASOLICITUD	= miForm.getFechaSolicitud();			
				String OBSERVACIONESSOLICITUD	= miForm.getObservacionesSolicitud();			
				String FECHAVALIDACION			= miForm.getFechaValidacion();			
				String FECHABAJA			= miForm.getFechaBaja();			
				String OBSERVACIONESVALIDACION	= miForm.getObservacionesValidacion();			
				String FECHASOLICITUDBAJA		= miForm.getFechaSolicitudBaja();			
				String OBSERVACIONESBAJA		= miForm.getObservacionesBaja();			
				String VALIDARINSCRIPCIONES		= miForm.getValidarInscripciones();	
				String GUARDIASSEL		= miForm.getGuardiasSel();	
				String sql=
					"Select IDGUARDIA,NOMBRE,NUMEROLETRADOSGUARDIA,NUMEROSUSTITUTOSGUARDIA,SELECCIONLABORABLES,SELECCIONFESTIVOS,TIPODIASGUARDIA,DIASGUARDIA,DIASPAGADOS,DIASSEPARACIONGUARDIAS from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
					" where "+	ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION +" = " + IDINSTITUCION+
					" and "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + IDTURNO + 
					" ORDER BY NOMBRE ";
				Vector vTurno = turno.ejecutaSelect(sql);
				request.setAttribute("resultado",vTurno);
				request.setAttribute("idturno"	,IDTURNO);
				request.setAttribute("GUARDIAS"	,GUARDIAS);
				request.setAttribute("IDPERSONA"	,IDPERSONA);
				request.setAttribute("IDINSTITUCION"	,IDINSTITUCION);
				request.setAttribute("FECHASOLICITUD"	,FECHASOLICITUD);
				request.setAttribute("FECHABAJA"	,FECHABAJA);
				request.setAttribute("OBSERVACIONESSOLICITUD"	,OBSERVACIONESSOLICITUD);
				request.setAttribute("FECHAVALIDACION"	,FECHAVALIDACION);
				request.setAttribute("OBSERVACIONESVALIDACION"	,OBSERVACIONESVALIDACION);
				request.setAttribute("FECHASOLICITUDBAJA"	,FECHASOLICITUDBAJA);
				request.setAttribute("OBSERVACIONESBAJA"	,OBSERVACIONESBAJA);
				request.setAttribute("VALIDARINSCRIPCIONES"	,VALIDARINSCRIPCIONES);
				request.setAttribute("VALIDARINSCRIPCIONES"	,VALIDARINSCRIPCIONES);
				request.setAttribute("GUARDIASSEL"	,GUARDIASSEL);
				
				request.setAttribute("botones","X,S");
				request.setAttribute("origen","/JGR_AltaTurnosGuardias");
				request.setAttribute("titulo","gratuita.altaTurnos_2.literal.title");
				forward = "nuevo2";
				
				request.setAttribute("idretencion",miForm.getIdRetencion());

			}
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'nuevo'. Existen dos posibilidades dependiendo del valor del paso.
	 *  Si el paso es = 1, consulta las guardias para el turno seleccionado, por el contrario, si el paso != 1
	 *  nos muestra una pantalla donde se solicita fecha de solicitud y observaciones a la solicitud. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "nuevo";
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			AltaTurnosGuardiasForm form = (AltaTurnosGuardiasForm) formulario;
			String sql="";
			Vector vTurno=new Vector();
			String turnoSel="";
			String idTurnoSel="";
			String validarTurno="";
			
			Integer idTurno = (Integer)form.getIdTurno();
			if (form.getPaso().equals("turnoMasivo")){
				 turnoSel=request.getParameter("turnosSel");
				 GstStringTokenizer st1 = new GstStringTokenizer(turnoSel,",");
				    while (st1.hasMoreTokens()) {
				        Hashtable ht = new Hashtable();
				        String registro = st1.nextToken();
				           String d[]= registro.split("##");
					        idTurnoSel+=","+d[0];
					    
					    
					    
				    }   
				    idTurnoSel=idTurnoSel.substring(1);
				 validarTurno=request.getParameter("validarTurno");
				forward = "nuevoMasivo";
			}
			//Validamos el paso en el que nos encontramos.
			if(form.getPaso().equals("1")||form.getPaso().equals("turnoMasivo"))
			{
				request.setAttribute("botones","X,S");
				request.setAttribute("titulo","gratuita.altaTurnos.literal.title");
				// obtenemos las guardias para el turno.
				ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
				
				if(form.getPaso().equals("turnoMasivo")){ 
					 sql=
						"Select IDGUARDIA,NOMBRE,NUMEROLETRADOSGUARDIA,NUMEROSUSTITUTOSGUARDIA,"+
						" SELECCIONLABORABLES,SELECCIONFESTIVOS,DIASGUARDIA,DIASPAGADOS,DIASSEPARACIONGUARDIAS from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
						" where "+	ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
						" and "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" in ( " + idTurnoSel+")";
					 vTurno = turno.ejecutaSelect(sql);
					request.setAttribute("action","/JGR_AltaTurnosGuardias");
					request.setAttribute("GUARDIAS",form.getGuardias());
					request.setAttribute("idturno",turnoSel);
					request.setAttribute("VALIDARINSCRIPCIONES",validarTurno);
					request.setAttribute("resultado",vTurno);
				}else{
//					 Se van a obtener los siguientes campos: 
					// Nombre,NºLetrado,NºSustitutos,Tipo Dia,Dias Guardia,
					// Dias Pagados,Dias de Separacion. !!Hacer como en el resto de los beans¡¡
				 sql=
					"Select IDGUARDIA,NOMBRE,NUMEROLETRADOSGUARDIA,NUMEROSUSTITUTOSGUARDIA,"+
					" SELECCIONLABORABLES,SELECCIONFESTIVOS,DIASGUARDIA,DIASPAGADOS,DIASSEPARACIONGUARDIAS from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
					" where "+	ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
					" and "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + idTurno.intValue();
				 vTurno = turno.ejecutaSelect(sql);
				request.setAttribute("action","/JGR_AltaTurnosGuardias");
				request.setAttribute("GUARDIAS",form.getGuardias());
				request.setAttribute("idturno",idTurno);
				request.setAttribute("VALIDARINSCRIPCIONES",form.getValidarInscripciones());
				request.setAttribute("resultado",vTurno);
				}
				// Hay que ver si existe retencion definida
				// Comprobamos si el letrado actua como sociedad.
				CenComponentesAdm cenComponentesAdm = new CenComponentesAdm(this.getUserBean(request));
				String where = " where CEN_CLIENTE_IDINSTITUCION ="+usr.getLocation()+
				" and CEN_CLIENTE_IDPERSONA = "+request.getSession().getAttribute("idPersonaTurno") +
				" and SOCIEDAD = " + ClsConstants.DB_TRUE;
				Vector vCenComponentes = cenComponentesAdm.select(where);
				// Si es 0, el letrado actua en modo propio
				where = 
					" where "+	ScsRetencionesIRPFBean.T_NOMBRETABLA+"."+ScsRetencionesIRPFBean.C_IDINSTITUCION +" = " + usr.getLocation()+
					" and "+ScsRetencionesIRPFBean.T_NOMBRETABLA+"."+ScsRetencionesIRPFBean.C_IDPERSONA+" = " + request.getSession().getAttribute("idPersonaTurno");
				ScsRetencionesIRPFAdm irpf = new ScsRetencionesIRPFAdm(this.getUserBean(request));
				Vector vIrpf = irpf.selectTabla(where);
				request.setAttribute("IRPF",String.valueOf(vIrpf.size()));
				// Si no existe irpf, buscamos los tipos
				if(vIrpf.size()==0)
				{
					if(vCenComponentes.size() == 0)
					{
						ScsRetencionesAdm tipos = new ScsRetencionesAdm(this.getUserBean(request));
						String wh = " where letranifsociedad is null or letranifsociedad = ''";
						Vector vTipos = tipos.select(wh);
						request.setAttribute("TIRPF",vTipos);
						if (form.getPaso().equals("turnoMasivo")){
						forward = "nuevoMasivo";
						}else{
							forward = "nuevo";	
						}
					}
					// Si es > 0, el letrado actua como sociedad
					else
					{
						CenComponentesBean sociedad = (CenComponentesBean) vCenComponentes.get(0);
						CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(this.getUserBean(request));
						where = " where IDPERSONA = "+sociedad.getIdPersona();
						Vector vCenPersona = cenPersonaAdm.select(where);
						if(vCenPersona.size()>0)
						{
							String letra = ((CenPersonaBean) vCenPersona.get(0)).getNIFCIF().substring(0,1);
							// Miramos si la letra tiene rentencion asociada.
							where = " where "+	ScsRetencionesBean.T_NOMBRETABLA+"."+ScsRetencionesBean.C_LETRANIFSOCIEDAD +" = '" + letra+"'";
							ScsRetencionesAdm irpf2 = new ScsRetencionesAdm(this.getUserBean(request));
							Vector vIrpf2 = irpf2.select(where);
							if(vIrpf2.size()==0)
							{
								request.setAttribute("mensaje","gratuita.retencionesIRPF.mensaje.error1");
								request.setAttribute("modal","1");
								forward = "exito";
							}
							else
							{
								// Con la letra, consultamos maestroretenciones y obtenemos la retencion a aplicar
								ScsRetencionesAdm adm = new ScsRetencionesAdm(this.getUserBean(request));
								where = " WHERE LETRANIFSOCIEDAD = '"+letra+"' ";
								Vector reten = adm.select(where);
								request.setAttribute("idretencion",String.valueOf(((ScsRetencionesBean)reten.get(0)).getIdRetencion()));
								request.setAttribute("retencion",String.valueOf(((ScsRetencionesBean)reten.get(0)).getRetencion()));
								if (form.getPaso().equals("turnoMasivo")){
								forward = "nuevoMasivo";
								}else{
									forward = "nuevo";
								}
							}
						}
						else
						{
							request.setAttribute("descOperation","gratuita.retencionesIRPF.literal.titulo");
							request.setAttribute("modal","1");
							forward = "exito";
						}
					}
				}
			}
			else
			{
				request.setAttribute("idturno"	,(Integer)form.getIdTurno());
				request.setAttribute("IDPERSONA"	,(Integer)form.getIdPersona());
				request.setAttribute("IDINSTITUCION"	,(Integer)form.getIdInstitucion());
				request.setAttribute("FECHASOLICITUD"	,(String)form.getFechaSolicitud());
				request.setAttribute("botones","C,F");
				request.setAttribute("titulo","gratuita.maestroTurnos.literal.validatitle3");
				if(form.getPaso().equals("2")) forward = "nuevo2";
			}
			request.setAttribute("accion",form.getModo());
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'insertar'. Esta funcion dara de alta para el letrado el turno seleccionado. 
	 *  Si el letrado ha seleccionado apuntarse a las guardias (valor 0), se le dara de alta en todas las guardias
	 *  del turno.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		UserTransaction tx = null;
		RowsContainer rc=null;
		boolean existe=false;
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			// Obtenemos los datos para realizar el alta en turno.
			// Si guardias = todas (0), se guardan todas las guardias en la tabla, en caso contrario
			// quedara pendiente a realizar por el colegiado.
			AltaTurnosGuardiasForm form = (AltaTurnosGuardiasForm) formulario;
			
			
			Integer idTurno = (Integer)form.getIdTurno();
			String fechaSolicitud 		= (String)form.getFechaSolicitud();
			String fechaValidacion 		= (String)form.getFechaValidacion();
			String fechaSolicitudBaja 	= (String)form.getFechaSolicitudBaja();
			String fechaBaja 			= (String)form.getFechaBaja();
			String validarInscripciones = (String)form.getValidarInscripciones();
			String fValidacion = null;
			String guardiasSeleccionadas=(String)form.getGuardiasSel();
			if (guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")&& guardiasSeleccionadas.indexOf("@")>=0){
			  guardiasSeleccionadas=guardiasSeleccionadas.substring(0,guardiasSeleccionadas.lastIndexOf("@"));
			}  
			String guardiasSel[];
			String fv = null;
			String observacionesValidacion = null;
			Hashtable miHash = new Hashtable();
			// Comprobamos si hay que validarinscripciones
			if(validarInscripciones.equalsIgnoreCase("N"))
			{
				miHash.put("FECHAVALIDACION","sysdate");
				miHash.put("OBSERVACIONESVALIDACION",".");
			}

			if(!fechaValidacion.equals(""))
			{
				observacionesValidacion = (String)form.getObservacionesValidacion();
				miHash.put("FECHAVALIDACION",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
				miHash.put("OBSERVACIONESVALIDACION",observacionesValidacion);
			}
			if(!fechaSolicitudBaja.equals("") && !fechaSolicitudBaja.equals("null"))
			{
				String observacionesBaja = (String)form.getObservacionesBaja();
				miHash.put("FECHASOLICITUDBAJA",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaSolicitudBaja));
				miHash.put("OBSERVACIONESBAJA",observacionesBaja);
			}
			if(!fechaBaja.equals("") && !fechaBaja.equals("null"))
			{
				String observacionesBaja = (String)form.getObservacionesBaja();
				miHash.put("FECHABAJA",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
			}
			// Si el campo del turno validarinscripciones = 'n', insertamos en la fechavalidacion y
			// en la fecha de baja la fecha del sistema.


			String observacionesSolicitud = (String)form.getObservacionesSolicitud();
			Integer guardias = (Integer)form.getGuardias();
			// Preparamos la hash
			miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));  
			miHash.put("IDINSTITUCION",usr.getLocation());
			miHash.put("IDTURNO",idTurno.toString());
			miHash.put("FECHASOLICITUD","sysdate");
			miHash.put("OBSERVACIONESSOLICITUD",observacionesSolicitud);
			// Vamos a realizar el alta. 
			// Comienzo control de transacciones
			// Hay que realizar un alta por cada registro de la tabla inscripcion turno.
			// Obtenemos todas las guardias del idinstitucion+idturno
			Vector vTurno = null;
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			String sql=
				"Select IDGUARDIA from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
				" where "+	ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
				" and "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + idTurno.intValue();
			vTurno = turno.ejecutaSelect(sql);
			tx = usr.getTransaction(); 
			tx.begin();
			
			// ACTUALIZAMOS LA DIRECCIÓN DE GUARDIA DEL CLIENTE. EN CASO DE QUE NO EXISTIERA UNA DIRECCIÓN DE GUARDIA
			// ENTONCES LA INSERTAMOS Y SI YA EXISTIA ACTUALIZAMOS LOS DATOS DE LA MISMA
			CenDireccionesBean beanDir  = new CenDireccionesBean ();
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean(request));
			beanDir.setFax1(form.getFax1());
			beanDir.setFax2(form.getFax2());
			beanDir.setIdInstitucion(form.getIdInstitucion());
			beanDir.setIdPersona(new Long((String)request.getSession().getAttribute("idPersonaTurno")));
			beanDir.setMovil(form.getMovil());
			beanDir.setTelefono1(form.getTelefono1());
			beanDir.setTelefono2(form.getTelefono2());
			
			String tieneYaDirecGuardia=request.getParameter("tieneYaDirecGuardia");
			if(tieneYaDirecGuardia.equalsIgnoreCase("S"))
			{
				beanDir.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP"));
				// Actualizamos el registro de la dirección de guardia
				beanDir.setIdDireccion(new Long(request.getParameter("idDireccion")));
				if (!direccionesAdm.update(direccionesAdm.beanToHashTable(beanDir),beanDir.getOriginalHash())) {
					throw new SIGAException (direccionesAdm.getError());
				}
			}
			else
			{
				
				//Insertamos la nueva direccion de guardia
				beanDir.setCodigoPostal("");
				beanDir.setCorreoElectronico("");
				beanDir.setDomicilio("");
				beanDir.setFechaBaja("");
				beanDir.setIdPais("");
				beanDir.setIdPoblacion("");
				beanDir.setIdProvincia("");
				beanDir.setPaginaweb("");
				beanDir.setPreferente("");
				beanDir.setIdDireccion(direccionesAdm.getNuevoID(beanDir));
				if (!direccionesAdm.insert(beanDir)) {
					throw new SIGAException (direccionesAdm.getError());
				}
				// insertamos el tipo de dirección
				CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (this.getUserBean(request));
				CenDireccionTipoDireccionBean beanTipoDir=new CenDireccionTipoDireccionBean();
				beanTipoDir.setIdDireccion(beanDir.getIdDireccion());
				beanTipoDir.setIdInstitucion(beanDir.getIdInstitucion());
				beanTipoDir.setIdPersona(beanDir.getIdPersona());
				beanTipoDir.setIdTipoDireccion(new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
				if (!admTipoDir.insert(beanTipoDir)){
					throw new SIGAException (admTipoDir.getError());
				}
					
				
			}
			
			ScsInscripcionTurnoAdm inscripcionTurno = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			
	 // Antes de insertar scs_inscripcionturno miramos si dicho turno no ha sido dado de alta por otro usuario
			String select=
				"Select 1 from "+ScsInscripcionTurnoBean.T_NOMBRETABLA+
				" where "+	ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
				" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO+" = " + idTurno.intValue()+
				" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA+" = " + request.getSession().getAttribute("idPersonaTurno")+
				" and "+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA +" is null";
			rc = new RowsContainer();
			
			if(rc.find(select)){
				existe=true;
				
			}else{
				existe=false;
			}
			
		if (!existe){// Todavia no se ha insertado el turno
			boolean result = inscripcionTurno.insert(miHash);
			ScsInscripcionGuardiaAdm inscripcionGuardia = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Hashtable hash = null; 
			if(result && guardias!=null ){
				 if ( guardias.intValue()==0){
				
					for(int x=0;x<vTurno.size() && result;x++)
					{
						hash = (Hashtable)vTurno.get(x);
						miHash = new Hashtable();
						miHash.put("IDINSTITUCION",usr.getLocation());
						miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
						miHash.put("IDTURNO",idTurno.toString());
						miHash.put("IDGUARDIA",hash.get("IDGUARDIA"));
						miHash.put("FECHASUSCRIPCION","sysdate");
						result = inscripcionGuardia.insert(miHash);
					}
				}else{
					if( guardias.intValue()==2){
						if( guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")){
						 guardiasSel=guardiasSeleccionadas.split("@");
						  for (int i=0; i<guardiasSel.length;i++){
						  	miHash = new Hashtable();
							miHash.put("IDINSTITUCION",usr.getLocation());
							miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
							miHash.put("IDTURNO",idTurno.toString());
							miHash.put("IDGUARDIA",guardiasSel[i]);
							miHash.put("FECHASUSCRIPCION","sysdate");
							result = inscripcionGuardia.insert(miHash);
						  }
					    
					    }
						
						
					}
				}
			}
			// Si se ha incluido retencion, se da de alta.
			if(form.getIdRetencion() != null && !form.getIdRetencion().equals("") && !form.getIdRetencion().equalsIgnoreCase("null"))
			{
				ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
				miHash = new Hashtable();
				miHash.put("IDINSTITUCION",usr.getLocation());
				miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
				miHash.put("IDRETENCION",form.getIdRetencion());
				miHash.put("DESCRIPCION",".");
				miHash.put("FECHAINICIO",GstDate.getApplicationFormatDate(usr.getLanguage(),"01/01/2005"));
				result = scsRetencionesIRPFAdm.insert(miHash);
			}
			if (result)
			{
				request.setAttribute("mensaje","messages.inserted.success");
				tx.commit();
			}
			else
			{
				request.setAttribute("mensaje","messages.inserted.error");
				tx.rollback();
			}
		 }else{// Ya existe el turno
		  	   request.setAttribute("mensaje","update.compare.diferencias");
		  	   tx.rollback();
		  	 
		 }	
		    request.setAttribute("urlAction", "/SIGA/JGR_DefinirTurnosLetrado.do?modo=abrir");
			request.setAttribute("hiddenFrame", "1");
			request.setAttribute("modal","1");
			String GUARDIASSEL		= form.getGuardiasSel();
			request.setAttribute("GUARDIASSEL"	,GUARDIASSEL);
		    forward = "exito";
//		  INICIALIZAMOS EL DATABUCKUP
			if(request.getSession().getAttribute("DATABACKUP") != null)
				request.getSession().removeAttribute("DATABACKUP");
			
	 
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return forward;
	}
	protected String insertarMasivo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		UserTransaction tx = null;
		RowsContainer rc=null;
		boolean existe=false;
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			// Obtenemos los datos para realizar el alta en turno.
			// Si guardias = todas (0), se guardan todas las guardias en la tabla, en caso contrario
			// quedara pendiente a realizar por el colegiado.
			AltaTurnosGuardiasForm form = (AltaTurnosGuardiasForm) formulario;
			
			String idTurno ="";
			String validarTurno="";
			String idTurnoSel = request.getParameter("idTurnoSel");
			String fechaSolicitud 		= (String)form.getFechaSolicitud();
			String fechaValidacion 		= (String)form.getFechaValidacion();
			String fechaSolicitudBaja 	= (String)form.getFechaSolicitudBaja();
			String fechaBaja 			= (String)form.getFechaBaja();
			String validarInscripciones = (String)form.getValidarInscripciones();
			String fValidacion = null;
			/*String guardiasSeleccionadas=(String)form.getGuardiasSel();
			if (guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")&& guardiasSeleccionadas.indexOf("@")>=0){
			  guardiasSeleccionadas=guardiasSeleccionadas.substring(0,guardiasSeleccionadas.lastIndexOf("@"));
			}  */
			String guardiasSel[];
			String fv = null;
			String observacionesValidacion = null;
			Hashtable miHash = new Hashtable();
			GstStringTokenizer st1 = new GstStringTokenizer(idTurnoSel,",");
		    boolean actualizaDir=false;
		    boolean actualizaRet=false;
		    while (st1.hasMoreTokens()) {//por cada dupla de elementos recuperados 
		       miHash = new Hashtable(); 	
		       idTurno=st1.nextToken();
		       String d[]= idTurno.split("##");
		       idTurno=d[0];//idTurno
		       validarTurno=d[1];//necesita validacion
			// Comprobamos si hay que validarinscripciones
			if(validarTurno.equalsIgnoreCase("N"))
			{
				miHash.put("FECHAVALIDACION","sysdate");
				miHash.put("OBSERVACIONESVALIDACION",".");
			}

			if(!fechaValidacion.equals("")&& (validarTurno.equalsIgnoreCase("S")))
			{
				observacionesValidacion = (String)form.getObservacionesValidacion();
				miHash.put("FECHAVALIDACION",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
				miHash.put("OBSERVACIONESVALIDACION",observacionesValidacion);
			}
			if((!fechaSolicitudBaja.equals("") && !fechaSolicitudBaja.equals("null")) && (validarTurno.equalsIgnoreCase("S")))
			{
				String observacionesBaja = (String)form.getObservacionesBaja();
				miHash.put("FECHASOLICITUDBAJA",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaSolicitudBaja));
				miHash.put("OBSERVACIONESBAJA",observacionesBaja);
			}
			if((!fechaBaja.equals("") && !fechaBaja.equals("null"))&& (validarTurno.equalsIgnoreCase("S")))
			{
				String observacionesBaja = (String)form.getObservacionesBaja();
				miHash.put("FECHABAJA",GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
			}
			// Si el campo del turno validarinscripciones = 'n', insertamos en la fechavalidacion y
			// en la fecha de baja la fecha del sistema.


			String observacionesSolicitud = (String)form.getObservacionesSolicitud();
			Integer guardias = (Integer)form.getGuardias();
			
			
		      
			// Preparamos la hash
			miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));  
			miHash.put("IDINSTITUCION",usr.getLocation());
			miHash.put("IDTURNO",idTurno.toString());
			miHash.put("FECHASOLICITUD","sysdate");
			miHash.put("OBSERVACIONESSOLICITUD",observacionesSolicitud);
			// Vamos a realizar el alta. 
			// Comienzo control de transacciones
			// Hay que realizar un alta por cada registro de la tabla inscripcion turno.
			// Obtenemos todas las guardias del idinstitucion+idturno
			Vector vTurno = null;
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			String sql=
				"Select IDGUARDIA from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
				" where "+	ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
				" and "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + Integer.parseInt(idTurno);
			vTurno = turno.ejecutaSelect(sql);
			tx = usr.getTransaction(); 
			tx.begin();
			
			if (!actualizaDir){
			// ACTUALIZAMOS LA DIRECCIÓN DE GUARDIA DEL CLIENTE. EN CASO DE QUE NO EXISTIERA UNA DIRECCIÓN DE GUARDIA
			// ENTONCES LA INSERTAMOS Y SI YA EXISTIA ACTUALIZAMOS LOS DATOS DE LA MISMA
			CenDireccionesBean beanDir  = new CenDireccionesBean ();
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean(request));
			beanDir.setFax1(form.getFax1());
			beanDir.setFax2(form.getFax2());
			beanDir.setIdInstitucion(form.getIdInstitucion());
			beanDir.setIdPersona(new Long((String)request.getSession().getAttribute("idPersonaTurno")));
			beanDir.setMovil(form.getMovil());
			beanDir.setTelefono1(form.getTelefono1());
			beanDir.setTelefono2(form.getTelefono2());
			
			String tieneYaDirecGuardia=request.getParameter("tieneYaDirecGuardia");
			if(tieneYaDirecGuardia.equalsIgnoreCase("S"))
			{
				beanDir.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP"));
				// Actualizamos el registro de la dirección de guardia
				beanDir.setIdDireccion(new Long(request.getParameter("idDireccion")));
				if (!direccionesAdm.update(direccionesAdm.beanToHashTable(beanDir),beanDir.getOriginalHash())) {
					throw new SIGAException (direccionesAdm.getError());
				}
				actualizaDir=true;
			}
			else
			{
				
				//Insertamos la nueva direccion de guardia
				beanDir.setCodigoPostal("");
				beanDir.setCorreoElectronico("");
				beanDir.setDomicilio("");
				beanDir.setFechaBaja("");
				beanDir.setIdPais("");
				beanDir.setIdPoblacion("");
				beanDir.setIdProvincia("");
				beanDir.setPaginaweb("");
				beanDir.setPreferente("");
				beanDir.setIdDireccion(direccionesAdm.getNuevoID(beanDir));
				if (!direccionesAdm.insert(beanDir)) {
					throw new SIGAException (direccionesAdm.getError());
				}
				// insertamos el tipo de dirección
				CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (this.getUserBean(request));
				CenDireccionTipoDireccionBean beanTipoDir=new CenDireccionTipoDireccionBean();
				beanTipoDir.setIdDireccion(beanDir.getIdDireccion());
				beanTipoDir.setIdInstitucion(beanDir.getIdInstitucion());
				beanTipoDir.setIdPersona(beanDir.getIdPersona());
				beanTipoDir.setIdTipoDireccion(new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
				if (!admTipoDir.insert(beanTipoDir)){
					throw new SIGAException (admTipoDir.getError());
				}
					
				actualizaDir=true;
			}
		    }
			ScsInscripcionTurnoAdm inscripcionTurno = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			
	 // Antes de insertar scs_inscripcionturno miramos si dicho turno no ha sido dado de alta por otro usuario
			String select=
				"Select 1 from "+ScsInscripcionTurnoBean.T_NOMBRETABLA+
				" where "+	ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
				" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO+" = " + Integer.parseInt(idTurno)+
				" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA+" = " + request.getSession().getAttribute("idPersonaTurno")+
				" and "+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA +" is null";
			rc = new RowsContainer();
			
			if(rc.find(select)){
				existe=true;
				
			}else{
				existe=false;
			}
			
		if (!existe){// Todavia no se ha insertado el turno
			boolean result = inscripcionTurno.insert(miHash);
			ScsInscripcionGuardiaAdm inscripcionGuardia = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Hashtable hash = null; 
			if(result && guardias!=null ){
				 if ( guardias.intValue()==0){
				
					for(int x=0;x<vTurno.size() && result;x++)
					{
						hash = (Hashtable)vTurno.get(x);
						miHash = new Hashtable();
						miHash.put("IDINSTITUCION",usr.getLocation());
						miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
						miHash.put("IDTURNO",idTurno.toString());
						miHash.put("IDGUARDIA",hash.get("IDGUARDIA"));
						miHash.put("FECHASUSCRIPCION","sysdate");
						result = inscripcionGuardia.insert(miHash);
					}
				}
			}
		 if (!actualizaRet){
			// Si se ha incluido retencion, se da de alta.
			if(form.getIdRetencion() != null && !form.getIdRetencion().equals("") && !form.getIdRetencion().equalsIgnoreCase("null"))
			{
				ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
				miHash = new Hashtable();
				miHash.put("IDINSTITUCION",usr.getLocation());
				miHash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
				miHash.put("IDRETENCION",form.getIdRetencion());
				miHash.put("DESCRIPCION",".");
				miHash.put("FECHAINICIO",GstDate.getApplicationFormatDate(usr.getLanguage(),"01/01/2005"));
				result = scsRetencionesIRPFAdm.insert(miHash);
				actualizaRet=true;
			}
		 }	
			if (result)
			{
				request.setAttribute("mensaje","messages.inserted.success");
				tx.commit();
			}
			else
			{
				request.setAttribute("mensaje","messages.inserted.error");
				tx.rollback();
			}
		 }else{// Ya existe el turno
		  	   request.setAttribute("mensaje","update.compare.diferencias");
		  	   tx.rollback();
		  	 
		 }	
		}
		    request.setAttribute("urlAction", "/SIGA/JGR_DefinirTurnosLetrado.do?modo=abrir");
			request.setAttribute("hiddenFrame", "1");
			request.setAttribute("modal","1");
			String GUARDIASSEL		= form.getGuardiasSel();
			request.setAttribute("GUARDIASSEL"	,GUARDIASSEL);
		    forward = "exito";
//		  INICIALIZAMOS EL DATABUCKUP
			if(request.getSession().getAttribute("DATABACKUP") != null)
				request.getSession().removeAttribute("DATABACKUP");
			
	 
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return forward;
	}

}