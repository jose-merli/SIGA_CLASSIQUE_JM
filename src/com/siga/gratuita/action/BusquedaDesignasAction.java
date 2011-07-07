package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsContrariosAsistenciaBean;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsDelitosDesignaAdm;
import com.siga.beans.ScsDelitosDesignaBean;
import com.siga.beans.ScsDelitosEJGAdm;
import com.siga.beans.ScsDelitosEJGBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsDesignasProcuradorAdm;
import com.siga.beans.ScsDesignasProcuradorBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.certificados.Plantilla;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BuscarDesignasForm;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;
import com.siga.informes.InformeBusquedaDesignas;



/**
 * @author ruben.fernandez
 * @since 3/2/2005
 * @version 28/02/2005 Modificados los metodos de "Insertar"
 * @version 26/01/2006 (david.sanchezp): modificaciones para los combos de procurador y juzgado y maquetacion y eliminado de comentarios.
 * @version 20/03/2006 (raul.ggonzalez): modificaciones para insertar en la designa letrados obtenidos desde la busqueda SJCS y sus casos posibles
 */

public class BusquedaDesignasAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)  
	final String[] clavesBusqueda={ScsDesignaBean.C_IDINSTITUCION,ScsDesignaBean.C_ANIO
			,ScsDesignaBean.C_IDTURNO,ScsDesignaBean.C_NUMERO};
	
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */

	protected ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			// La primera vez que se carga el formulario 
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				BuscarDesignasForm form = (BuscarDesignasForm)miForm;
				form.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				form.reset(mapping,request);
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);						
			}else if (accion.equalsIgnoreCase("generarCarta")){
				mapDestino = generarCarta(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("finalizarCarta")){
				mapDestino = finalizarCarta(mapping, miForm, request, response);

			} else if (accion.equalsIgnoreCase("volverBusqueda")){
				mapDestino = volverBusqueda(mapping, miForm, request, response);

			}else if (accion.equalsIgnoreCase("buscarInicio")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscarPor(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("insertar")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = insertar(mapping, miForm, request, response); 
			} else if (accion.equalsIgnoreCase("getAjaxTurnos")) {
				ClsLogging.writeFileLog("BUSQUEDA DESIGNA:getAjaxTurnos", 10);
				getAjaxTurnos(mapping, miForm, request, response);
				return null;
			}else {
				return super.executeInternal(mapping,
						formulario,
						request, 
						response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				if (miForm.getModal().equalsIgnoreCase("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}

		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
		return mapping.findForward(mapDestino);
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
			{
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("BUSQUEDAREALIZADA");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			CenPersonaAdm persona = new CenPersonaAdm(this.getUserBean(request));
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				String nombreApellidos = persona.obtenerNombreApellidos(new Long(usr.getIdPersona()).toString());
				request.setAttribute("nColegiado",numeroColegiado);
				request.setAttribute("nombreUserBean",nombreApellidos);

			}catch (Exception e) {
				throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			}
		}		
		return "inicio";
	}
	
	
	
	/**Funcion que transforma los datos de entrada para poder hacer la consulta a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se consultar meter en BBDD
	 */
	protected String prepararConsulta (Hashtable datos) throws ClsExceptions{
		String aux=" ";
		aux += (((UtilidadesHash.getString(datos,"IDTURNO") == null)||(UtilidadesHash.getString(datos,"IDTURNO") == "-1")||(UtilidadesHash.getString(datos,"IDTURNO") == ""))?"":" and des.idTurno = "+((String)UtilidadesHash.getString(datos,"IDTURNO")));
//		aux += (((UtilidadesHash.getString(datos,"NCOLEGIADO") == null)||(UtilidadesHash.getString(datos,"NCOLEGIADO").equalsIgnoreCase("")))?"":" and "+ComodinBusquedas.tratarNumeroColegiado((String)UtilidadesHash.getString(datos,"NCOLEGIADO"),"F_SIGA_CALCULONCOLEGIADO (colegiado.idinstitucion,  colegiado.idpersona)"));
		if(UtilidadesHash.getString(datos,"NCOLEGIADO")!=null && !((String)UtilidadesHash.getString(datos,"NCOLEGIADO")).equals("") )
			aux += " and F_SIGA_GETIDLETRADO_DESIGNA(des.idinstitucion,des.idturno,des.anio,des.numero) = "+UtilidadesHash.getString(datos,"NCOLEGIADO");

		if (UtilidadesHash.getString(datos,"ANIO") != null && !UtilidadesHash.getString(datos,"ANIO").equalsIgnoreCase("")) {
			if (UtilidadesHash.getString(datos,"ANIO").indexOf('*') >= 0)
				aux += " AND " + ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"ANIO")).trim(),"des.anio" );
			else
				aux += " AND des.anio = " + (String)UtilidadesHash.getString(datos,"ANIO");
		}
		if (UtilidadesHash.getString(datos,"CODIGO") != null && !UtilidadesHash.getString(datos,"CODIGO").equalsIgnoreCase("")) {
			if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(datos,"CODIGO")))		    	
				aux += " AND " + ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"CODIGO")).trim(),"des.codigo" ); 
			else {
				// Si es numerico
				if (UtilidadesHash.getInteger(datos,"CODIGO") != null) {
					aux += " AND des.codigo =" + (String)UtilidadesHash.getString(datos,"CODIGO");//El código para la busqueda se trata como un numerico para que si no meten comodines(*) 
					// haga la busqueda exacta y si intentan buscar el numero 3 devuelve el 3,03,003, etc...
				}
				else { // no es numerico
					aux += " AND des.codigo = '" + (String)UtilidadesHash.getString(datos,"CODIGO") + "' "; 
				}
			}
		}
		if (UtilidadesHash.getString(datos,"JUZGADO") != null && !UtilidadesHash.getString(datos,"JUZGADO").equalsIgnoreCase("")) {
			String a[]=((String)UtilidadesHash.getString(datos,"JUZGADO")).split(",");
			aux += " AND des.idjuzgado = '" + a[0].trim() + "' ";
		}
		if (UtilidadesHash.getString(datos,"ASUNTO") != null && !UtilidadesHash.getString(datos,"ASUNTO").equalsIgnoreCase("")) {
			aux += " AND des.resumenasunto = '" + (String)UtilidadesHash.getString(datos,"ASUNTO").trim() + "' ";
		}
		if (UtilidadesHash.getString(datos,"ESTADO") != null && !UtilidadesHash.getString(datos,"ESTADO").equalsIgnoreCase("")&& !UtilidadesHash.getString(datos,"ESTADO").equalsIgnoreCase("N")) {
			aux += " AND des.estado = '" + (String)UtilidadesHash.getString(datos,"ESTADO").trim() + "' ";
		}
		if (UtilidadesHash.getString(datos,"PROCEDIMIENTO") != null && !UtilidadesHash.getString(datos,"PROCEDIMIENTO").equalsIgnoreCase("")) {
			aux += " AND des.numprocedimiento = '" + (String)UtilidadesHash.getString(datos,"PROCEDIMIENTO").trim() + "' ";
		}
		if (UtilidadesHash.getString(datos,"ACTUACIONES_PENDIENTES") != null && !UtilidadesHash.getString(datos,"ACTUACIONES_PENDIENTES").equalsIgnoreCase("")&& !UtilidadesHash.getString(datos,"ACTUACIONES_PENDIENTES").equalsIgnoreCase("N")) {
			aux += " and upper(F_SIGA_ACTDESIG_NOVALIDAR(des.idinstitucion,des.idturno,des.anio,des.numero))=upper('" + (String)UtilidadesHash.getString(datos,"ACTUACIONES_PENDIENTES").trim() + "' )";
		}





		if (UtilidadesHash.getString(datos,"CALIDAD") != null && !UtilidadesHash.getString(datos,"CALIDAD").equalsIgnoreCase("")) {
			aux += " and (select count(*)"+
			"    from SCS_DEFENDIDOSDESIGNA def"+
			" where"+
			" def.ANIO = des.anio"+
			" and def.NUMERO = des.numero"+
			" and def.IDINSTITUCION = des.idinstitucion"+
			" and def.IDTURNO = des.idturno"+
			" and def.calidad = '" + (String)UtilidadesHash.getString(datos,"CALIDAD").trim() + "') > 0";

		}

//		aux += (((UtilidadesHash.getString(datos,"ANIO") == null)||(UtilidadesHash.getString(datos,"ANIO").equalsIgnoreCase("")))?"":" AND "+ComodinBusquedas.prepararSentenciaCompleta((String)UtilidadesHash.getString(datos,"ANIO"),"des.anio" ));
//		aux += (((UtilidadesHash.getString(datos,"NUMERO") == null)||(UtilidadesHash.getString(datos,"NUMERO").equalsIgnoreCase("")))?"":" and "+ComodinBusquedas.prepararSentenciaCompleta((String)UtilidadesHash.getString(datos,"NUMERO"),"des.numero" ));
		//aux += (((UtilidadesHash.getString(datos,"FECHAENTRADAINICIO") == null)||(UtilidadesHash.getString(datos,"FECHAENTRADAINICIO").equalsIgnoreCase("")))?"":" and des.fechaentrada > to_date('"+((String)UtilidadesHash.getString(datos,"FECHAENTRADAINICIO"))+"','DD/MM/YYYY')");
		//aux += (((UtilidadesHash.getString(datos,"FECHAENTRADAFIN") == null)||(UtilidadesHash.getString(datos,"FECHAENTRADAFIN").equalsIgnoreCase("")))?"":" and des.fechaentrada < to_date('"+((String)UtilidadesHash.getString(datos,"FECHAENTRADAFIN"))+"','DD/MM/YYYY')");
		if ((datos.containsKey("FECHAENTRADAINICIO") && !UtilidadesHash.getString(datos,"FECHAENTRADAINICIO").equalsIgnoreCase(""))
				||
				(datos.containsKey("FECHAENTRADAFIN")&& !UtilidadesHash.getString(datos,"FECHAENTRADAFIN").equalsIgnoreCase(""))
		){
			aux +=" and " + GstDate.dateBetweenDesdeAndHasta("des.fechaentrada",
					GstDate.getApplicationFormatDate("",UtilidadesHash.getString(datos,"FECHAENTRADAINICIO")),
					GstDate.getApplicationFormatDate("",UtilidadesHash.getString(datos,"FECHAENTRADAFIN")));
		}
		aux += (((UtilidadesHash.getString(datos,"IDTIPODESIGNACOLEGIO") == null)||(UtilidadesHash.getString(datos,"IDTIPODESIGNACOLEGIO").equalsIgnoreCase("")))?"":" and des.IDTIPODESIGNACOLEGIO ="+((String)UtilidadesHash.getString(datos,"IDTIPODESIGNACOLEGIO")));
		return aux;
	}


	/*protected String prepararSubConsulta (Hashtable datos){
		String aux1	=	" AND defe.idpersona = ( SELECT distinct defendido.idpersona"+
						" FROM SCS_DEFENDIDOSDESIGNA defendido, SCS_PERSONAJG persona"+
						" WHERE defendido.idinstitucion = persona.idinstitucion"+
						" AND defendido.idinstitucion = des.idinstitucion"+
						" AND defendido.idturno       = des.idturno"+
						" AND defendido.anio          = des.anio"+
						" AND defendido.numero        = des.numero"+
						" AND defendido.idpersona     = persona.idpersona ";
		String aux2 ="";

		aux2 += (((UtilidadesHash.getString(datos,"NOMBRE") == null)||(UtilidadesHash.getString(datos,"NOMBRE").equalsIgnoreCase("")))?"":" AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"NOMBRE")).trim(),"persona.nombre"));

		//Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
		   // de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
		   // la consulta adecuada.	
		    if((UtilidadesHash.getString(datos,"NIF") == null)||(UtilidadesHash.getString(datos,"NIF").equalsIgnoreCase(""))){
			aux2 += "";

		     }else{
		     	if (ComodinBusquedas.hasComodin((String)UtilidadesHash.getString(datos,"NIF"))){	
		     		aux2 +=	"AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"NIF")).trim(),"persona.nif");
		     	}else{
		     	aux2+="AND "+ComodinBusquedas.prepararSentenciaNIF((String)UtilidadesHash.getString(datos,"NIF")," UPPER (persona.nif) ");	
		     	}
		     }

		    aux2 += (((UtilidadesHash.getString(datos,"APELLIDO1") == null)||(UtilidadesHash.getString(datos,"APELLIDO1").equalsIgnoreCase("")))?"":" AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"APELLIDO1")).trim(),"persona.apellido1"));
		    aux2 += (((UtilidadesHash.getString(datos,"APELLIDO2") == null)||(UtilidadesHash.getString(datos,"APELLIDO2").equalsIgnoreCase("")))?"":" AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"APELLIDO2")).trim(),"persona.apellido2"));
		aux2 += ")";
		String aux =((aux2.length()<2)?"":aux1+aux2);
		return aux;
	}*/
	/**Funcion que transforma los datos de entrada para poder hacer la consulta a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se consultar meter en BBDD
	 */

	protected String prepararSubConsulta (Hashtable datos){

		StringBuffer consulta = new StringBuffer();
		boolean isFiltrado = false;
		consulta.append(" AND (select count(1) from V_SIGA_DEFENDIDOS_DESIGNA VDEF");
		consulta.append(" where VDEF.idInstitucion = des.idinstitucion");
		consulta.append(" and VDEF.anio = des.anio");
		consulta.append(" and VDEF.numero = des.numero");
		consulta.append(" and VDEF.IDTURNO = des.idturno");
		if(UtilidadesHash.getString(datos,"NIF") != null && !UtilidadesHash.getString(datos,"NIF").equalsIgnoreCase("")){
			isFiltrado = true;
			consulta.append(" and ");
			consulta.append(ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"NIF")).trim(),"VDEF.NIF"));
			//

		}
		if(UtilidadesHash.getString(datos,"NOMBRE") != null && !UtilidadesHash.getString(datos,"NOMBRE").equalsIgnoreCase("")){
			isFiltrado = true;
			consulta.append(" and ");
			consulta.append(ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"NOMBRE")).trim(),"VDEF.NOMBRE"));


		}
		if(UtilidadesHash.getString(datos,"APELLIDO1") != null && !UtilidadesHash.getString(datos,"APELLIDO1").equalsIgnoreCase("")){
			isFiltrado = true;
			consulta.append(" and ");
			consulta.append(ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"APELLIDO1")).trim(),"VDEF.APELLIDO1"));


		}
		if(UtilidadesHash.getString(datos,"APELLIDO2") != null && !UtilidadesHash.getString(datos,"APELLIDO2").equalsIgnoreCase("")){

			isFiltrado = true;
			consulta.append(" and ");
			consulta.append(ComodinBusquedas.prepararSentenciaCompleta(((String)UtilidadesHash.getString(datos,"APELLIDO2")).trim(),"VDEF.APELLIDO2"));

		}

		consulta.append(" )>0 ");




		if(!isFiltrado)
			consulta = new StringBuffer("");
		return consulta.toString();
	}



	protected String prepararSubConsultaActuacion (Hashtable datos){
		boolean tiene_juzg=UtilidadesHash.getString(datos,"JUZGADOACTU") != null && !UtilidadesHash.getString(datos,"JUZGADOACTU").equalsIgnoreCase("");
		boolean tiene_asunto=UtilidadesHash.getString(datos,"ASUNTOACTUACION") != null && !UtilidadesHash.getString(datos,"ASUNTOACTUACION").equalsIgnoreCase("");
		boolean tiene_acreditacion=UtilidadesHash.getString(datos,"ACREDITACION") != null && !UtilidadesHash.getString(datos,"ACREDITACION").equalsIgnoreCase("");
		boolean tiene_modulo=UtilidadesHash.getString(datos,"MODULO") != null && !UtilidadesHash.getString(datos,"MODULO").equalsIgnoreCase("");
		String aux1="";
		if (tiene_juzg||tiene_asunto||tiene_acreditacion||tiene_modulo){
			aux1	=	" and (des.idinstitucion, des.idturno, des.anio, des.numero) in"+
			" (select act.idinstitucion, act.idturno, act.anio, act.numero"+
			" from scs_actuaciondesigna act"+
			" where des.idinstitucion = act.idinstitucion"+
			" and des.idturno = act.idturno"+
			" and des.anio = act.anio"+
			" and des.numero = act.numero ";
			if (tiene_juzg) {
				String a[]=((String)UtilidadesHash.getString(datos,"JUZGADOACTU")).split(",");
				aux1 += " AND act.idjuzgado = '" + a[0].trim() + "' ";
			}
			if (tiene_asunto) {
				aux1 += " AND act.numeroasunto = '" + (String)UtilidadesHash.getString(datos,"ASUNTOACTUACION").trim() + "' ";
			}
			if (tiene_acreditacion) {
				//			String a[]=((String)UtilidadesHash.getString(datos,"ACREDITACION")).split(",");
				aux1 += " AND act.idacreditacion = '" + UtilidadesHash.getString(datos,"ACREDITACION").trim() + "' ";
			}
			if (tiene_modulo) {
				//			String a[]=((String)UtilidadesHash.getString(datos,"MODULO")).split(",");
				aux1 += " AND act.idprocedimiento = '" + UtilidadesHash.getString(datos,"MODULO").trim() + "' ";
			}
			aux1+=")";
		}
		return aux1;
	}


	protected Hashtable recogerDatosEntrada (Hashtable hashPrim, Hashtable hashSeg){
		try{
			if (!UtilidadesHash.getString(hashSeg,"IDTURNO").equals(""))UtilidadesHash.set(hashPrim,"IDTURNO",UtilidadesHash.getString(hashSeg,"IDTURNO"));
		}catch(Exception e){}
		try{
			if (!UtilidadesHash.getString(hashSeg,"FECHAAPERTURAINICIO").equals(""))UtilidadesHash.set(hashPrim,"FECHAAPERTURAINICIO",UtilidadesHash.getString(hashSeg,"FECHAAPERTURAINICIO"));
		}catch(Exception e){}
		try{
			if (!UtilidadesHash.getString(hashSeg,"TIPODESIGNA").equals(""))UtilidadesHash.set(hashPrim,"TIPODESIGNA",UtilidadesHash.getString(hashSeg,"TIPODESIGNA"));
		}catch(Exception e){}
		try{
			if (!UtilidadesHash.getString(hashSeg,"NCOLEGIADO").equals(""))UtilidadesHash.set(hashPrim,"NCOLEGIADO",UtilidadesHash.getString(hashSeg,"NCOLEGIADO"));
		}catch(Exception e){}
		return hashPrim;
	}

	/** 
	 *  Funcion que atiende la accion buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{

		HashMap databackup=new HashMap();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsDesignaAdm desigAdm=new ScsDesignaAdm(this.getUserBean(request));
		BuscarDesignasForm miForm =(BuscarDesignasForm)formulario;
		Hashtable miHash= new Hashtable();
		miHash = miForm.getDatos();
		String consulta= "";

		try {

			if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();


				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");



				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	

				// jbd //
				actualizarPagina(request, desigAdm, datos);
			 
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);




			}else{	

				databackup=new HashMap();

				//obtengo datos de la consulta 			
				PaginadorBind resultado = null;
				
				resultado=desigAdm.getBusquedaDesigna((String)usr.getLocation(),miHash);
				Vector datos = null;



				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					// jbd //
					actualizarPagina(request, desigAdm, datos);
				
					databackup.put("datos",datos);
					request.getSession().setAttribute("DATAPAGINADOR",databackup);
				} 


				//resultado = admBean.selectGenerico(consulta);
				//request.getSession().setAttribute("resultado",v);
			}
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado			 			 
			miHash.put("BUSQUEDAREALIZADA","1");
			request.getSession().setAttribute("DATOSFORMULARIO",miHash);	
			request.getSession().setAttribute("BUSQUEDAREALIZADA", consulta.toString());


		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}

		return "listado";
	}
	protected String volverBusqueda(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		return "inicio";
	}

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response)throws ClsExceptions,SIGAException  {


		Hashtable nuevaDesigna = (Hashtable)request.getSession().getAttribute("idDesigna");		//Recuperamos los datos de la nueva designa
		request.getSession().removeAttribute("idDesigna");										//lo borramos de la sesion
		request.setAttribute("idDesigna",nuevaDesigna);											//lo metemos en la request
		return "edicion";
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		return "Ver";
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException  
	{
		ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm(this.getUserBean(request));
		Hashtable hashAux=new Hashtable();
		try {
			BuscarDesignasForm miform = (BuscarDesignasForm)formulario;
			List<ScsTurnoBean> alTurnos = new ArrayList<ScsTurnoBean>();
			List<ScsGuardiasTurnoBean> alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
			ScsTurnoAdm admTurnos = new ScsTurnoAdm(this.getUserBean(request));
			alTurnos = admTurnos.getTurnosConTipo(this.getUserBean(request).getLocation(), "1");
			miform.setIdTurno("");
			miform.setTurnos(alTurnos);
			if ((miform.getDesdeAsistencia()!=null)&&(!miform.getDesdeAsistencia().equalsIgnoreCase(""))){
				request.getSession().setAttribute("asistencia","si");
				request.getSession().setAttribute("numeroAsistencia",(String)miform.getNumeroAsistencia());
				request.setAttribute("anioAsistencia", miform.getAnioAsistencia());

				//hashAux=asistenciaAdm.existeLetradoAsistencia(this.getIDInstitucion(request).toString(),(String)miform.getNumeroAsistencia(),miform.getAnioAsistencia());

				GenParametrosAdm adm = new GenParametrosAdm (this.getUserBean(request));
				String tipoDesigna = adm.getValor(""+this.getIDInstitucion(request),"SCS","TIPO_DESIGNACION_DESDE_ASISTENCIA", "");
				request.setAttribute("tipoDesignaAsistencia", tipoDesigna);
				request.setAttribute("idTurnoAsistencia", this.getIDInstitucion(request) + "," + miform.getIdTurno());
				request.setAttribute("idPersona", miform.getIdPersona());
				request.setAttribute("nColegiadoAsistencia", miform.getNcolegiado());
				request.setAttribute("nombreColegiadoAsistencia", miform.getNombre());
				request.setAttribute("juzgadoAsistencia", miform.getJuzgadoAsi()+ "," + miform.getJuzgadoInstitucionAsi());
			} 
			else {
				if((miform.getDesdeEjg()!=null)&&(!miform.getDesdeEjg().equalsIgnoreCase(""))) {
					ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));

					request.getSession().setAttribute("ejg","si");
					request.getSession().setAttribute("numeroEjg",(String)miform.getNumeroEjg());
					request.getSession().setAttribute("idTipoEjg",(String)miform.getIdTipoEjg());
					request.setAttribute("anioEJG", miform.getAnioEjg());
					request.setAttribute("turnoEJG", this.getIDInstitucion(request) + "," + miform.getIdTurnoEJG());
					miform.setIdTurno(miform.getIdTurnoEJG());
					request.setAttribute("idjuzgadoEJG", miform.getJuzgadoAsi()+ "," + miform.getJuzgadoInstitucionAsi());
					request.setAttribute("numProcedimiento",miform.getNumProcedimiento()); 

					hashAux=ejgAdm.procedeDeAsistencia((String)miform.getIdTipoEjg(),(String)miform.getNumeroEjg(),miform.getAnioEjg());
					if (hashAux.get("ASIANIO")!=null && !((String)hashAux.get("ASIANIO")).equals("")){
						//esta relacionado con una asistencia 
						hashAux=asistenciaAdm.existeLetradoAsistencia( String.valueOf(this.getIDInstitucion(request)),(String)hashAux.get("ASINUMERO"),(String)hashAux.get("ASIANIO"));
						request.setAttribute("idTurnoAsistencia", this.getIDInstitucion(request) + "," + (String)hashAux.get("TURNO"));
						request.setAttribute("idPersona", (String)hashAux.get("IDPERSONA"));
						request.setAttribute("idPersonaAsi", (String)hashAux.get("IDPERSONA"));
						request.setAttribute("nColegiadoAsistencia", (String)hashAux.get("NCOLEGIADO"));
						request.setAttribute("nombreColegiadoAsistencia", (String)hashAux.get("NOMCOLEGIADO"));
					}

				}
			}

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}

		return "nuevo";
	}

	
	
	protected synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException  
	{
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Hashtable formAvanzada = (Hashtable)request.getSession().getAttribute("formularioAvanzada");
		BuscarDesignasForm miform = (BuscarDesignasForm)formulario;
		String desdeAsistencia=(String)request.getSession().getAttribute("asistencia");
		String desdeEjg=(String)request.getSession().getAttribute("ejg");
		

		if ((desdeAsistencia!=null &&  desdeAsistencia.equalsIgnoreCase("si"))||(desdeEjg!=null &&  desdeEjg.equalsIgnoreCase("si"))){
			request.getSession().removeAttribute("DATAPAGINADOR");
		}
		Hashtable formDesignaHash = (Hashtable)miform.getDatos();
		formDesignaHash.put(ScsDesignaBean.C_IDTURNO,miform.getIdTurno());
		formDesignaHash.remove("registrosSeleccionados");
		formDesignaHash.remove("datosPaginador");
		if (((String)formDesignaHash.get("IDTURNO")==null)&&(formAvanzada!=null))formDesignaHash = this.recogerDatosEntrada(miform.getDatos(),formAvanzada);
		formDesignaHash.put("IDINSTITUCION",(String)usr.getLocation());


		Hashtable nuevaDesigna = new Hashtable();
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		UserTransaction tx = null;
		String idPersonaSel = miform.getIdPersona();
		LetradoInscripcion letradoTurno = null;
		try{
			
			tx = usr.getTransaction();
//			tx.begin();	
			// Obtenemos el idPersona
			// Si hemos introducido manualmente el numero de colegiado, no sabremos su idPersona lo consultamos de BD
			
			boolean isManual = true;
			if (idPersonaSel.equals("")) {
					isManual = false;
					String fecha = (String)formDesignaHash.get("FECHAENTRADAINICIO");
					//busqueda automatica
					
					ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
					
					CalendarioSJCS calendario = new CalendarioSJCS(new Integer(usr.getLocation()), new Integer(miform.getIdTurno()),fecha,usr);
					letradoTurno = calendario.getLetradoTurno();
					idPersonaSel = letradoTurno.getIdPersona().toString();					
					

			}
			
			if(isManual){
				//	comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
				CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
				Map<String,CenBajasTemporalesBean> mBajasTemporalesConfirmador =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersonaSel), new Integer(usr.getLocation()));
				if(mBajasTemporalesConfirmador.containsKey(formDesignaHash.get("FECHAENTRADAINICIO")))
					throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
			}
			//Seteamos si es manual o automatico
			formDesignaHash.put(ScsDesignasLetradoBean.C_MANUAL,isManual?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE);
			//Seteamos el letrado
			formDesignaHash.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersonaSel);

			String asistencia, ejg, numeroAsistencia, numeroEjg, idTipoEjg;

			asistencia       = (String)request.getSession().getAttribute("asistencia");
			numeroAsistencia = (String)request.getSession().getAttribute("numeroAsistencia");
			ejg       = (String)request.getSession().getAttribute("ejg");
			numeroEjg = (String)request.getSession().getAttribute("numeroEjg");
			idTipoEjg = (String)request.getSession().getAttribute("idTipoEjg");

			request.getSession().removeAttribute("asistencia");
			request.getSession().removeAttribute("numeroAsistencia");
			request.getSession().removeAttribute("ejg");
			request.getSession().removeAttribute("numeroEjg");
			request.getSession().removeAttribute("idTipoEjg");

			// 1. Creamos la Designa

			nuevaDesigna = designaAdm.prepararInsert(formDesignaHash);

			// 1.1 Si se crea desde asistencia
			if ((asistencia!=null)&&(asistencia.equalsIgnoreCase("si"))){
				ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm(this.getUserBean(request));
				// Obtenemos la asistencia
				Hashtable datos = new Hashtable ();
				UtilidadesHash.set(datos, ScsAsistenciasBean.C_ANIO, miform.getAnioAsistencia());
				UtilidadesHash.set(datos, ScsAsistenciasBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(datos, ScsAsistenciasBean.C_NUMERO, numeroAsistencia);
				ScsAsistenciasBean asistenciaBean = (ScsAsistenciasBean)((Vector)asistenciaAdm.selectByPK(datos)).get(0);

				// Creamos la designacion desde la asistencia
				if (!this.crearDesignacionDesdeAsistencia(nuevaDesigna, asistenciaBean, idPersonaSel,this.getUserBean(request), miform.getCalidad())) {
					throw new ClsExceptions ("Error al crear la designa desde asignación");
				}
			}// 1.2 Si se crea desde EJG
			else if((ejg!=null) && (ejg.equalsIgnoreCase("si"))){

					// Obtenemos el EJG
					Hashtable datos = new Hashtable ();
					UtilidadesHash.set(datos, ScsEJGBean.C_ANIO, miform.getAnioEjg());
					UtilidadesHash.set(datos, ScsEJGBean.C_IDINSTITUCION, usr.getLocation());
					UtilidadesHash.set(datos, ScsEJGBean.C_IDTIPOEJG, idTipoEjg);
					UtilidadesHash.set(datos, ScsEJGBean.C_NUMERO, numeroEjg);

					ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));
					ScsEJGBean ejgBean = (ScsEJGBean)((Vector)ejgAdm.selectByPK(datos)).get(0);
					UtilidadesHash.set(nuevaDesigna, ScsDesignaBean.C_NUMPROCEDIMIENTO, miform.getNumProcedimiento());
					UtilidadesHash.set(nuevaDesigna, "SOLICITANTE", miform.getIdSolicitante());


					// Creamos la designacion desde el EJG
					if (!this.crearDesignaDesdeEJG(nuevaDesigna, ejgBean, idPersonaSel,this.getUserBean(request))) {
						throw new ClsExceptions ("Error al crear la designa desde EJG");
					}
				
				
			}else {// 1.3 Si estoy creando la designa nueva
				if (!this.crearDesignacionNueva(nuevaDesigna, idPersonaSel, this.getUserBean(request))) {
					throw new ClsExceptions ("Error al crear la designa nueva");
				}
			}
			String idInstitucionSJCS=usr.getLocation();
			String idTurnoSJCS=miform.getIdTurno();
			String idGuardiaSJCS=null;
			String anioSJCS=miform.getAnio();
			String numeroSJCS=miform.getNumero();
			if(isManual){
				
	//			String idPersonaSJCS=miform.getIdPersona();
				String origenSJCS = "gratuita.busquedaDesignas.literal.nuevaDesigna"; 
				//-----------------------------------------------------
	
				String checkSalto = request.getParameter("checkSalto");
				//String checkCompensacion = request.getParameter("checkCompensacion");
				String motivoSaltoSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarSaltoPor") + " " +
				UtilidadesString.getMensajeIdioma(usr,origenSJCS);
				// Aplicar cambios (COMENTAR LO QUE NO PROCEDA) Revisar que no se hace algo ya en el action. 
				ScsSaltosCompensacionesAdm saltosCompAdm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
				if (checkSalto != null&&(checkSalto.equals("on") || checkSalto.equals("1")))
				// Tercero: Generación de salto (Designaciones y asistencias)
					saltosCompAdm.crearSaltoCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSel, motivoSaltoSJCS,ClsConstants.SALTOS);
				// Cuarto: Generación de compensación (Designaciones NO ALTAS)
				//admFiltros.crearCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkCompensacion,motivoCompensacionSJCS);
				///////////////////////////////////////////////////////////////////////////////////////////
			}

			// Anhadimos parametros para las pestanhas
			request.getSession().setAttribute("idDesigna", nuevaDesigna); 
			request.getSession().setAttribute("Modo", "editar");

			if ((miform.getDesdeAsistencia()!=null)&&(miform.getDesdeAsistencia().equalsIgnoreCase("si")))
				request.getSession().setAttribute("asistencia",(String)nuevaDesigna.get("NUMERO"));
			else{
				if((miform.getDesdeEjg()!=null)&&(miform.getDesdeEjg().equalsIgnoreCase("si")))
					request.getSession().setAttribute("ejg",(String)nuevaDesigna.get("NUMERO"));
			}

			// Se cierra la transacción
//			tx.commit();

			request.setAttribute("NUMERO",numeroSJCS);
			request.setAttribute("IDTURNO",idTurnoSJCS);
			request.setAttribute("INSTITUCION",idInstitucionSJCS);
			request.setAttribute("ANIO",anioSJCS);
		}catch (SIGAException e) {
			try {
//				tx.rollback();
			}catch(Exception rp){} 
			request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));	
			return "errorConAviso"; 
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		} 

		String mensaje = "messages.inserted.success";

		if (letradoTurno != null) {

			StringBuffer nombreCompletoLetrado  = new StringBuffer(letradoTurno.getPersona().getNombre());
			
			if (letradoTurno.getPersona().getApellido1() != null) {
				nombreCompletoLetrado.append(" ");
				nombreCompletoLetrado.append(letradoTurno.getPersona().getApellido1());
					
			}
			if (letradoTurno.getPersona().getApellido2() != null) {
				nombreCompletoLetrado.append(" ");
				nombreCompletoLetrado.append(letradoTurno.getPersona().getApellido2());
					
			}
			
			mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request), mensaje);
			mensaje += "\r\n" + UtilidadesString.getMensajeIdioma(getUserBean(request), "messages.nuevaDesigna.seleccionAutomaticaLetrado", new String[]{letradoTurno.getPersona().getColegiado().getNColegiado(), nombreCompletoLetrado.toString()});
		}

		return exitoModal(mensaje, request);
	}
	

	protected String exitoModal(String mensaje, HttpServletRequest request) 
	{
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("modal","");
		return "exitoDesigna"; 
	}

	/**
	 * 
	 * @param datosDesigna
	 * @param idPersonaSeleccionada
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	// DCG
	boolean crearDesignacionNueva (Hashtable datosDesigna, String idPersonaSeleccionada, UsrBean usuario) throws ClsExceptions 
	{

		ScsDesignaAdm designaAdm = new ScsDesignaAdm (usuario);

		try {
			// 1. Insertamos la designa
			ScsDesignaBean designaBean = new ScsDesignaBean ();
			designaBean.setAnio(new Integer(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_ANIO)));
			designaBean.setEstado(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_ESTADO));
			designaBean.setFechaAnulacion("");
			designaBean.setFechaEntrada(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_FECHAENTRADA));
			designaBean.setFechaEstado(GstDate.getApplicationFormatDate("",UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_FECHAESTADO)));
			designaBean.setIdInstitucion(UtilidadesHash.getInteger(datosDesigna, ScsDesignaBean.C_IDINSTITUCION));
			designaBean.setIdTipoDesignaColegio(UtilidadesHash.getInteger(datosDesigna, ScsDesignaBean.C_IDTIPODESIGNACOLEGIO));
			designaBean.setIdTurno(UtilidadesHash.getInteger(datosDesigna, ScsDesignaBean.C_IDTURNO));
			designaBean.setNumero(new Long(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_NUMERO)));
			designaBean.setCodigo(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_CODIGO));

			if (datosDesigna.get(ScsDesignaBean.C_IDJUZGADO)!=null && !((String)datosDesigna.get(ScsDesignaBean.C_IDJUZGADO)).equals(""))
				designaBean.setIdJuzgado(new Long(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_IDJUZGADO)));
			if (datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO)!=null && !((String)datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO)).equals(""))
				designaBean.setIdInstitucionJuzgado(new Integer(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_IDINSTITUCIONJUZGADO)));



			/*Vector vResultado = null;
			String codigo=null;
			String sql = "SELECT F_SIGA_OBTENERCODIGODESIGNA("+designaBean.getIdInstitucion()+") AS CODIGODESIGNA from dual";
			vResultado=(Vector)designaAdm.ejecutaSelect(sql);
			if(vResultado != null && vResultado.size()>-1)
				codigo = (String)((Hashtable)vResultado.get(0)).get("CODIGODESIGNA");
			designaBean.setCodigo(codigo);

			String sql = "PROC_ACTUALIZARGENPARAMETROS("+designaBean.getIdInstitucion()+", "+codigo,to_char(fechaInicio));*/
			/***/
			designaBean.setFechaAlta("SYSDATE");
			if (!designaAdm.insert(designaBean)) {
				return false;
			}

			// 2. Insertamos DesignaLetrado
			ScsDesignasLetradoBean designaLetradoBean = new ScsDesignasLetradoBean ();
			designaLetradoBean.setAnio(designaBean.getAnio());
			designaLetradoBean.setFechaDesigna(designaBean.getFechaEntrada());
			designaLetradoBean.setIdInstitucion(designaBean.getIdInstitucion());
			designaLetradoBean.setIdPersona(new Integer(idPersonaSeleccionada));
			designaLetradoBean.setIdTurno(designaBean.getIdTurno());
			designaLetradoBean.setLetradoDelTurno("S");
			designaLetradoBean.setManual(new Integer(UtilidadesHash.getString(datosDesigna,"MANUAL")));
			designaLetradoBean.setNumero(new Integer(designaBean.getNumero().intValue()));

			ScsDesignasLetradoAdm designaLetradoAdm = new ScsDesignasLetradoAdm (usuario);
			if (!designaLetradoAdm.insert(designaLetradoBean)) {
				return false;
			}
			return true;
		}
		catch (Exception e) {
			throw new ClsExceptions ("Error al crear la designa nueva: " + e.getMessage());
		}
	}


	/**
	 * 
	 * @param datosDesigna
	 * @param asistenciaBean
	 * @param manual
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	// DCG
	boolean crearDesignacionDesdeAsistencia (Hashtable datosDesigna, ScsAsistenciasBean asistenciaBean, String idPersonaSeleccionada, UsrBean usuario, String calidad) throws ClsExceptions 
	{
		try {
			// 1. Insertamos la designa
			ScsDesignaBean designaBean = new ScsDesignaBean ();
			designaBean.setAnio(new Integer(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_ANIO)));
			designaBean.setDelitos(asistenciaBean.getDelitosImputados());
			designaBean.setEstado(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_ESTADO));
			designaBean.setFechaAnulacion("");
			designaBean.setFechaEntrada(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_FECHAENTRADA));
			designaBean.setFechaEstado(GstDate.getApplicationFormatDate("",UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_FECHAESTADO)));
			designaBean.setIdInstitucion(asistenciaBean.getIdInstitucion());
			designaBean.setIdTipoDesignaColegio(UtilidadesHash.getInteger(datosDesigna, ScsDesignaBean.C_IDTIPODESIGNACOLEGIO));
			designaBean.setIdTurno(UtilidadesHash.getInteger(datosDesigna, ScsDesignaBean.C_IDTURNO));
			designaBean.setNumero(new Long(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_NUMERO)));
			designaBean.setObservaciones(asistenciaBean.getObservaciones());
			designaBean.setCodigo(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_CODIGO));

			if (datosDesigna.get(ScsDesignaBean.C_IDJUZGADO)!=null && !((String)datosDesigna.get(ScsDesignaBean.C_IDJUZGADO)).equals(""))
				designaBean.setIdJuzgado(new Long(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_IDJUZGADO)));
			if (datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO)!=null && !((String)datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO)).equals(""))
				designaBean.setIdInstitucionJuzgado(new Integer(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_IDINSTITUCIONJUZGADO)));

			if (datosDesigna.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)!=null && !((String)datosDesigna.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)).equals(""))
				designaBean.setNumProcedimiento(UtilidadesHash.getString(datosDesigna, ScsDesignaBean.C_NUMPROCEDIMIENTO));			

			ScsDesignaAdm designaAdm = new ScsDesignaAdm (usuario);
			designaBean.setFechaAlta("SYSDATE");
			if (!designaAdm.insert(designaBean)) {
				return false;
			}

			// 2. Insertamos DesignaLetrado
			ScsDesignasLetradoBean designaLetradoBean = new ScsDesignasLetradoBean ();
			designaLetradoBean.setAnio(designaBean.getAnio());
			designaLetradoBean.setFechaDesigna(designaBean.getFechaEntrada());
			designaLetradoBean.setIdInstitucion(designaBean.getIdInstitucion());
			designaLetradoBean.setIdPersona(new Integer(idPersonaSeleccionada));
			designaLetradoBean.setIdTurno(designaBean.getIdTurno());
			designaLetradoBean.setLetradoDelTurno("S");
			designaLetradoBean.setManual(new Integer(UtilidadesHash.getString(datosDesigna,"MANUAL")));
			designaLetradoBean.setNumero(new Integer(designaBean.getNumero().intValue()));

			ScsDesignasLetradoAdm designaLetradoAdm = new ScsDesignasLetradoAdm (usuario);
			if (!designaLetradoAdm.insert(designaLetradoBean)) {
				return false;
			}

			// 3. Insertamos DefendidosDesigna
			if (asistenciaBean.getIdPersonaJG() != null) {
				ScsDefendidosDesignaBean defendidosDesignaBean = new ScsDefendidosDesignaBean();
				defendidosDesignaBean.setAnio(designaBean.getAnio());
				defendidosDesignaBean.setIdInstitucion(designaBean.getIdInstitucion());
				defendidosDesignaBean.setIdPersona(asistenciaBean.getIdPersonaJG());
				defendidosDesignaBean.setIdTurno(designaBean.getIdTurno());
				defendidosDesignaBean.setNumero(new Integer(designaBean.getNumero().intValue()));
				if (calidad!=null && !calidad.equals("")){
					defendidosDesignaBean.setCalidad(calidad);
				}else{
					defendidosDesignaBean.setCalidad("D");
				}

				ScsDefendidosDesignaAdm defendidosDesignaAdm = new ScsDefendidosDesignaAdm (usuario);
				if (!defendidosDesignaAdm.insert(defendidosDesignaBean)) {
					return false;
				}
			}

			// 4. Relacionar designa con la Asistencia
			asistenciaBean.setDesignaAnio(designaBean.getAnio());
			asistenciaBean.setDesignaNumero(new Integer (designaBean.getNumero().intValue()));
			asistenciaBean.setDesignaTurno(designaBean.getIdTurno());

			ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (usuario);
			if (!asistenciaAdm.update(asistenciaBean)) {
				return false;
			}

			if (asistenciaBean.getEjgAnio()!=null && !asistenciaBean.getEjgAnio().equals("")){
			    ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
				ejgDesignabean.setIdInstitucion(new Integer (usuario.getLocation()));
				ejgDesignabean.setAnioDesigna(designaBean.getAnio());
				ejgDesignabean.setIdTurno(designaBean.getIdTurno());
				ejgDesignabean.setNumeroDesigna(new Integer (designaBean.getNumero().intValue()));
				ejgDesignabean.setAnioEJG(asistenciaBean.getEjgAnio());
				ejgDesignabean.setIdTipoEJG(asistenciaBean.getEjgIdTipoEjg());
				ejgDesignabean.setNumeroEJG(new Integer(asistenciaBean.getEjgNumero().intValue()));
				
				Hashtable hashEjgDesigna=new Hashtable();
				ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (usuario);
				
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIODESIGNA, 	designaBean.getAnio());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMERODESIGNA,  new Integer (designaBean.getNumero().intValue()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTURNO, 		designaBean.getIdTurno());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTIPOEJG, 		asistenciaBean.getEjgIdTipoEjg());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMEROEJG, 		new Integer(asistenciaBean.getEjgNumero().intValue()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIOEJG, 		asistenciaBean.getEjgAnio());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDINSTITUCION, 		new Integer (usuario.getLocation()));
				Vector existeRelacion = ejgDesignaAdm.select(hashEjgDesigna);
				
				if (existeRelacion.size()==0){//Si no existe la relación, la creamos
					if(!ejgDesignaAdm.insert(ejgDesignabean))
						throw new ClsExceptions ("Error al crear la relacion entre EJG y la designa");
				}
			
			}
			
			// 5. Insertamos en delitosDesigna todos los delitos de la asistencia
			ScsDelitosAsistenciaAdm delitosAsistenciaAdm = new ScsDelitosAsistenciaAdm (usuario);
			Hashtable aux = new Hashtable();
			UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_ANIO, asistenciaBean.getAnio());
			UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_IDINSTITUCION, asistenciaBean.getIdInstitucion());
			UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_NUMERO, asistenciaBean.getNumero());
			Vector delitos = delitosAsistenciaAdm.select(aux);

			ScsDelitosDesignaAdm delitosDesignaAdm = new ScsDelitosDesignaAdm (usuario); 
			for (int i = 0; (delitos != null) &&  (i < delitos.size()); i++) {
				ScsDelitosAsistenciaBean delitosAsistenciaBean = (ScsDelitosAsistenciaBean) delitos.get(i); 
				ScsDelitosDesignaBean delitosDesignaBean = new ScsDelitosDesignaBean();
				delitosDesignaBean.setAnio(designaBean.getAnio());
				delitosDesignaBean.setIdDelito(delitosAsistenciaBean.getIdDelito());
				delitosDesignaBean.setIdInstitucion(designaBean.getIdInstitucion());
				delitosDesignaBean.setIdTurno(designaBean.getIdTurno());
				delitosDesignaBean.setNumero(new Integer(designaBean.getNumero().intValue()));
				if (!delitosDesignaAdm.insert(delitosDesignaBean))
					return false;
			}

			// 6. Insertamos en ContrariosDesigna los contrario de la asistencia  
			ScsContrariosAsistenciaAdm contrariosAsistenciaAdm = new ScsContrariosAsistenciaAdm (usuario);
			aux.clear();
			UtilidadesHash.set(aux, ScsContrariosAsistenciaBean.C_ANIO, asistenciaBean.getAnio());
			UtilidadesHash.set(aux, ScsContrariosAsistenciaBean.C_IDINSTITUCION, asistenciaBean.getIdInstitucion());
			UtilidadesHash.set(aux, ScsContrariosAsistenciaBean.C_NUMERO, asistenciaBean.getNumero());
			Vector contrarios = contrariosAsistenciaAdm.select(aux);

			ScsContrariosDesignaAdm contrariosDesignaAdm = new ScsContrariosDesignaAdm (usuario); 
			for (int i = 0; (contrarios != null) &&  (i < contrarios.size()); i++) {
				ScsContrariosAsistenciaBean contrariosAsistenciaBean = (ScsContrariosAsistenciaBean) contrarios.get(i); 
				ScsContrariosDesignaBean contrariosDesignaBean = new ScsContrariosDesignaBean();
				//contrariosDesignaBean.setAnio(contrariosAsistenciaBean.getAnio());
				contrariosDesignaBean.setAnio(designaBean.getAnio());
				//contrariosDesignaBean.setIdInstitucion(contrariosAsistenciaBean.getIdInstitucion());
				contrariosDesignaBean.setIdInstitucion(designaBean.getIdInstitucion());
				contrariosDesignaBean.setIdInstitucionProcurador(designaBean.getIdInstitucionProcurador());
				contrariosDesignaBean.setIdPersona(new Integer(contrariosAsistenciaBean.getIdPersona().intValue()));
				contrariosDesignaBean.setIdProcurador((designaBean.getIdProcurador()!=null?new Integer(designaBean.getIdProcurador().intValue()):null));
				contrariosDesignaBean.setIdTurno(asistenciaBean.getIdTurno());
				//contrariosDesignaBean.setNumero(new Integer(contrariosAsistenciaBean.getNumero().intValue()));
				contrariosDesignaBean.setNumero(new Integer(designaBean.getNumero().intValue()));
				contrariosDesignaBean.setObservaciones(contrariosAsistenciaBean.getObservaciones());
				if (!contrariosDesignaAdm.insert(contrariosDesignaBean))
					return false;
			}
			return true;
		}
		catch (Exception e) {
			throw new ClsExceptions ("Error al crear la designa desde EJG: " + e.getMessage());
		}
	}


	/**
	 * 
	 * @param datosHash
	 * @param ejgBeanOld
	 * @param idPersonaSeleccionada
	 * @param usuario
	 * @return
	 */
	// DCG
	boolean crearDesignaDesdeEJG (Hashtable datosHash, ScsEJGBean ejgBean, String idPersonaSeleccionada, UsrBean usuario) throws ClsExceptions 
	{

		Vector contrarios=new Vector();
		Vector solicitantes=new Vector();
		Hashtable contrariosHash = new Hashtable ();
		Hashtable temporal = new Hashtable ();

		ScsDesignaAdm designaAdm = new ScsDesignaAdm (usuario);
		ScsContrariosDesignaAdm contrariosAdm =new ScsContrariosDesignaAdm(usuario);
		try {
			// 1. Insertamos la designa
			ScsDesignaBean designaBean = new ScsDesignaBean ();
			designaBean.setAnio(new Integer(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_ANIO)));
			//UtilidadesHash.set(datosHash, ScsDesignaBean.C_DELITOS, ejgBean.getDelitos());
			designaBean.setDelitos(ejgBean.getDelitos());
			designaBean.setEstado(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_ESTADO));
			designaBean.setFechaAnulacion("");
			designaBean.setFechaEntrada(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_FECHAENTRADA));
			designaBean.setFechaEstado(GstDate.getApplicationFormatDate("",UtilidadesHash.getString(datosHash, ScsDesignaBean.C_FECHAESTADO)));
			designaBean.setIdInstitucion(ejgBean.getIdInstitucion());
			designaBean.setIdInstitucionProcurador(ejgBean.getIdInstitucionProcurador());
			designaBean.setIdProcurador((ejgBean.getIdProcurador()!=null?new Long(ejgBean.getIdProcurador().longValue()):null));
			designaBean.setIdTipoDesignaColegio(UtilidadesHash.getInteger(datosHash, ScsDesignaBean.C_IDTIPODESIGNACOLEGIO));
			designaBean.setIdTurno(UtilidadesHash.getInteger(datosHash, ScsDesignaBean.C_IDTURNO));
			designaBean.setNumero(new Long(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_NUMERO)));
			//UtilidadesHash.set(datosHash, ScsDesignaBean.C_OBSERVACIONES, ejgBean.getObservaciones());
			//UtilidadesHash.set(datosHash, ScsDesignaBean.C_RESUMENASUNTO, ejgBean.getObservaciones());
			designaBean.setResumenAsunto(ejgBean.getObservaciones());
			designaBean.setObservaciones(ejgBean.getObservaciones());
			designaBean.setNumProcedimiento(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_NUMPROCEDIMIENTO));
			//designaBean.setFechaEntrada(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_FECHAENTRADA));
			String idPretension =  ejgBean.getIdPretension();
			if ((idPretension!=null)&&(!idPretension.equalsIgnoreCase(""))){ 
				designaBean.setIdPretension(new Integer(idPretension));
			}

			designaBean.setCodigo(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_CODIGO));


			contrariosHash.put(ScsContrariosDesignaBean.C_IDINSTITUCION,designaBean.getIdInstitucion());
			contrariosHash.put(ScsContrariosDesignaBean.C_ANIO,designaBean.getAnio());
			contrariosHash.put(ScsContrariosDesignaBean.C_IDTURNO,designaBean.getIdTurno());
			contrariosHash.put(ScsContrariosDesignaBean.C_NUMERO,designaBean.getNumero());

			if (datosHash.get(ScsDesignaBean.C_IDJUZGADO)!=null && !((String)datosHash.get(ScsDesignaBean.C_IDJUZGADO)).equals(""))
				designaBean.setIdJuzgado(new Long(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_IDJUZGADO)));
			if (datosHash.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO)!=null && !((String)datosHash.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO)).equals(""))
				designaBean.setIdInstitucionJuzgado(new Integer(UtilidadesHash.getString(datosHash, ScsDesignaBean.C_IDINSTITUCIONJUZGADO)));			

			designaBean.setFechaAlta("SYSDATE");
			if (!designaAdm.insert(designaBean)) {
				return false;
			}

			String sql="select ce.idpersona as IDPERSONA, ce.idprocurador as IDPROCURADOR, ce.idinstitucion_procu IDINSTPROCURADOR, ";
				  sql+=" ce.idrepresentanteejg  as IDREPRESENTANTE, ce.idabogadocontrarioejg as IDABOGADO, ce.nombreabogadocontrarioejg as NOMBREABOGADO, ";
                  sql+=" ce.nombrerepresentanteejg as NOMBREREPRESENTANTE from scs_contrariosejg ce where ce.anio="+ejgBean.getAnio()+" and ce.idinstitucion="+ejgBean.getIdInstitucion()+" and ce.idtipoejg="+ejgBean.getIdTipoEJG()+" and ce.numero="+ejgBean.getNumero() ;
			contrarios=designaAdm.selectGenerico(sql);			
			// Copiamos los contrarios del EJG

			if (contrarios!=null && contrarios.size()>0){
				for (int i=0;i<contrarios.size();i++){
					String idPersonaContrario = (String)((Hashtable)contrarios.get(i)).get("IDPERSONA");
					String idProcuradorContrario = (String)((Hashtable)contrarios.get(i)).get("IDPROCURADOR");
					String idInstProcuradorContrario = (String)((Hashtable)contrarios.get(i)).get("IDINSTPROCURADOR");
					String idAbogadoContrario = (String)((Hashtable)contrarios.get(i)).get("IDABOGADO");
					String idRepresentanteContrario = (String)((Hashtable)contrarios.get(i)).get("IDREPRESENTANTE");
					String nombreAbogado = (String)((Hashtable)contrarios.get(i)).get("NOMBREABOGADO");
					String nombreRepresentante = (String)((Hashtable)contrarios.get(i)).get("NOMBREREPRESENTANTE");
					contrariosHash.put(ScsContrariosDesignaBean.C_IDPERSONA,idPersonaContrario);
					contrariosHash.put(ScsContrariosDesignaBean.C_NOMBREABOGADOCONTRARIO,nombreAbogado);
					contrariosHash.put(ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE,nombreRepresentante);
					if(idProcuradorContrario!=null&&!idProcuradorContrario.equalsIgnoreCase(""))
						contrariosHash.put(ScsContrariosDesignaBean.C_IDPROCURADOR,idProcuradorContrario);
					if(idInstProcuradorContrario!=null&&!idInstProcuradorContrario.equalsIgnoreCase(""))
						contrariosHash.put(ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR,idInstProcuradorContrario);
					if(idInstProcuradorContrario!=null&&!idInstProcuradorContrario.equalsIgnoreCase(""))
						contrariosHash.put(ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR,idInstProcuradorContrario);
					if(idAbogadoContrario!=null&&!idAbogadoContrario.equalsIgnoreCase(""))
						contrariosHash.put(ScsContrariosDesignaBean.C_IDABOGADOCONTRARIO,idAbogadoContrario);
					// TODO // jbd // Con el representante tenemos un problema gordo, en EJG y asistencias es una personaJG y en designas es un colegiado
					// if(idRepresentanteContrario!=null&&!idRepresentanteContrario.equalsIgnoreCase(""))
					//	 contrariosHash.put(ScsContrariosDesignaBean.C_IDREPRESENTANTELEGAL,idRepresentanteContrario);
					contrariosAdm.insert(contrariosHash);
				}
			}			

			// 2. Insertamos DesignaLetrado
			ScsDesignasLetradoBean designaLetradoBean = new ScsDesignasLetradoBean ();
			designaLetradoBean.setAnio(designaBean.getAnio());
			designaLetradoBean.setFechaDesigna(designaBean.getFechaEntrada());
			designaLetradoBean.setIdInstitucion(designaBean.getIdInstitucion());
			designaLetradoBean.setIdPersona(new Integer(idPersonaSeleccionada));
			designaLetradoBean.setIdTurno(designaBean.getIdTurno());
			designaLetradoBean.setLetradoDelTurno("S");
			designaLetradoBean.setManual(new Integer(UtilidadesHash.getString(datosHash,"MANUAL")));
			designaLetradoBean.setNumero(new Integer(designaBean.getNumero().intValue()));

			ScsDesignasLetradoAdm designaLetradoAdm = new ScsDesignasLetradoAdm (usuario);
			if (!designaLetradoAdm.insert(designaLetradoBean)) {
				return false;
			}

			// 3. Insertamos DefendidosDesigna
			//if (ejgBean.getIdPersonaJG() != null) {
			if(Integer.parseInt(UtilidadesHash.getString(datosHash,"SOLICITANTE"))!=-1){
				ScsDefendidosDesignaBean defendidosDesignaBean = new ScsDefendidosDesignaBean();
				defendidosDesignaBean.setAnio(designaBean.getAnio());
				defendidosDesignaBean.setIdInstitucion(designaBean.getIdInstitucion());
				defendidosDesignaBean.setIdPersona(new Integer(UtilidadesHash.getString(datosHash,"SOLICITANTE")));
				defendidosDesignaBean.setIdTurno(designaBean.getIdTurno());
				defendidosDesignaBean.setNumero(new Integer(designaBean.getNumero().intValue()));
				defendidosDesignaBean.setCalidad(ejgBean.getCalidad());
				defendidosDesignaBean.setIdTipoenCalidad(ejgBean.getIdTipoenCalidad());
				defendidosDesignaBean.setCalidadIdinstitucion(ejgBean.getCalidadidinstitucion());
				ScsDefendidosDesignaAdm defendidosDesignaAdm = new ScsDefendidosDesignaAdm (usuario);
				if (!defendidosDesignaAdm.insert(defendidosDesignaBean)) {
					return false;
				}
			}else{ //Opcion de Todos
				String sqlDefendidos=" select scs_personajg.idpersona as ID" +
				"  from scs_personajg, scs_unidadfamiliarejg" +
				" where " +
				"   scs_unidadfamiliarejg.idpersona = scs_personajg.idpersona" +
				"   and scs_unidadfamiliarejg.idinstitucion = scs_personajg.idinstitucion" +
				"   and scs_unidadfamiliarejg.solicitante = '1'" +
				"   and scs_unidadfamiliarejg.idinstitucion = "+ejgBean.getIdInstitucion()+
				"   and scs_unidadfamiliarejg.anio = "+ejgBean.getAnio()+
				"   and scs_unidadfamiliarejg.numero = "+ejgBean.getNumero()+
				"   and scs_unidadfamiliarejg.idtipoejg = "+ejgBean.getIdTipoEJG();
				solicitantes=designaAdm.ejecutaSelect(sqlDefendidos);
				for(int i=0; i<solicitantes.size();i++ ){
					ScsDefendidosDesignaBean defendidosDesignaBean = new ScsDefendidosDesignaBean();
					defendidosDesignaBean.setAnio(designaBean.getAnio());
					defendidosDesignaBean.setIdInstitucion(designaBean.getIdInstitucion());
					temporal=(Hashtable)solicitantes.get(i);

					defendidosDesignaBean.setIdPersona(new Integer(UtilidadesHash.getString(temporal,"ID")));
					defendidosDesignaBean.setIdTurno(designaBean.getIdTurno());
					defendidosDesignaBean.setNumero(new Integer(designaBean.getNumero().intValue()));
					defendidosDesignaBean.setCalidad(ejgBean.getCalidad());
					defendidosDesignaBean.setIdTipoenCalidad(ejgBean.getIdTipoenCalidad());
					defendidosDesignaBean.setCalidadIdinstitucion(ejgBean.getCalidadidinstitucion());
					ScsDefendidosDesignaAdm defendidosDesignaAdm = new ScsDefendidosDesignaAdm (usuario);
					if (!defendidosDesignaAdm.insert(defendidosDesignaBean)) {
						return false;
					}
				}

			}

			//}

			// 4. Relacionar designa con EJG
			ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();

			ejgDesignabean.setIdInstitucion(ejgBean.getIdInstitucion());
			ejgDesignabean.setAnioDesigna(designaBean.getAnio());
			ejgDesignabean.setIdTurno(designaBean.getIdTurno());
			ejgDesignabean.setNumeroDesigna(new Integer(designaBean.getNumero().intValue()));
			ejgDesignabean.setAnioEJG(ejgBean.getAnio());
			ejgDesignabean.setIdTipoEJG(ejgBean.getIdTipoEJG());
			ejgDesignabean.setNumeroEJG(new Integer(ejgBean.getNumero().intValue()));
			ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (usuario);
			if(!ejgDesignaAdm.insert(ejgDesignabean))
				return false;

			// 5. Insertamos en delitosDesigna todos los delitos del EJG
			ScsDelitosEJGAdm delitosEJGAdm = new ScsDelitosEJGAdm (usuario);
			Hashtable aux = new Hashtable();
			UtilidadesHash.set(aux, ScsDelitosEJGBean.C_ANIO, ejgBean.getAnio());
			UtilidadesHash.set(aux, ScsDelitosEJGBean.C_IDINSTITUCION, ejgBean.getIdInstitucion());
			UtilidadesHash.set(aux, ScsDelitosEJGBean.C_IDTIPOEJG, ejgBean.getIdTipoEJG());
			UtilidadesHash.set(aux, ScsDelitosEJGBean.C_NUMERO, ejgBean.getNumero());
			Vector delitos = delitosEJGAdm.select(aux);

			ScsDelitosDesignaAdm delitosDesignaAdm = new ScsDelitosDesignaAdm (usuario); 
			for (int i = 0; (delitos != null) &&  (i < delitos.size()); i++) {
				ScsDelitosEJGBean delitosEJBBean = (ScsDelitosEJGBean) delitos.get(i); 
				ScsDelitosDesignaBean delitosDesignaBean = new ScsDelitosDesignaBean();
				delitosDesignaBean.setAnio(designaBean.getAnio());
				delitosDesignaBean.setIdDelito(delitosEJBBean.getIdDelito());
				delitosDesignaBean.setIdInstitucion(designaBean.getIdInstitucion());
				delitosDesignaBean.setIdTurno(designaBean.getIdTurno());
				delitosDesignaBean.setNumero(new Integer(designaBean.getNumero().intValue()));
				if (!delitosDesignaAdm.insert(delitosDesignaBean))
					return false;
			}

			//6. Insertamos el procurador del ejg en la designa

			if(ejgBean.getIdProcurador()!=null){

				ScsDesignasProcuradorAdm procuradorDesignaAdm = new ScsDesignasProcuradorAdm(usuario);

				// HASH DE INSERCION para el nuevo
				Hashtable procuradorNuevo = new Hashtable();
				procuradorNuevo.put(ScsDesignasProcuradorBean.C_IDINSTITUCION,designaBean.getIdInstitucion());
				procuradorNuevo.put(ScsDesignasProcuradorBean.C_IDTURNO,designaBean.getIdTurno());
				procuradorNuevo.put(ScsDesignasProcuradorBean.C_NUMERO,new Integer(designaBean.getNumero().intValue()));
				procuradorNuevo.put(ScsDesignasProcuradorBean.C_ANIO,designaBean.getAnio());
				procuradorNuevo.put(ScsDesignasProcuradorBean.C_IDPROCURADOR,ejgBean.getIdProcurador());
				procuradorNuevo.put(ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC,ejgBean.getIdInstitucionProcurador());
				if (ejgBean.getFechaProc()!=null && !ejgBean.getFechaProc().equals("")){
					procuradorNuevo.put(ScsDesignasProcuradorBean.C_FECHADESIGNA, ejgBean.getFechaProc());
				}else{
					procuradorNuevo.put(ScsDesignasProcuradorBean.C_FECHADESIGNA, "SYSDATE");	
				}
				if (ejgBean.getNumeroDesignaProc()!=null && !ejgBean.getNumeroDesignaProc().equals("")){
					procuradorNuevo.put(ScsDesignasProcuradorBean.C_NUMERODESIGNACION, ejgBean.getNumeroDesignaProc());
				}
				//procuradorNuevo.put(ScsDesignasProcuradorBean.C_IDTIPOMOTIVO,motivo);
				//procuradorNuevo.put(ScsDesignasProcuradorBean.C_NUMERODESIGNACION,numeroDesigna);
				//procuradorNuevo.put(ScsDesignasProcuradorBean.C_OBSERVACIONES,observ);


				if (!procuradorDesignaAdm.insert(procuradorNuevo)) 
					return false;
			}

			ScsEJGAdm ejgAdm =new ScsEJGAdm(usuario);
			// Despues de crear la designacion a partir de un ejg comprobamos que este ejg viene de una asistencia, para así
			// crear automaticamente la relación entre la asistencia y la designa
			Hashtable hashAux = ejgAdm.procedeDeAsistencia(ejgBean.getIdTipoEJG().toString(),ejgBean.getNumero().toString(),ejgBean.getAnio().toString());
			if (hashAux.get("ASIANIO")!=null && !((String)hashAux.get("ASIANIO")).equals("")){
				//esta relacionado con una asistencia
				// 4. Relacionar designa con la Asistencia siempre que la asistencia no tenga ninguna otra Designacion relacionada

				// Obtenemos la asistencia
				Hashtable datos = new Hashtable();
				ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (usuario);
				UtilidadesHash.set(datos, ScsAsistenciasBean.C_ANIO,  hashAux.get("ASIANIO").toString());
				UtilidadesHash.set(datos, ScsAsistenciasBean.C_IDINSTITUCION, usuario.getLocation());
				UtilidadesHash.set(datos, ScsAsistenciasBean.C_NUMERO, hashAux.get("ASINUMERO").toString());


				ScsAsistenciasBean asistenciaBean = (ScsAsistenciasBean)((Vector)asistenciaAdm.selectByPK(datos)).get(0);
				if (asistenciaBean.getDesignaAnio()==null || asistenciaBean.getDesignaAnio().equals("")){
					asistenciaBean.setDesignaAnio(designaBean.getAnio());
					asistenciaBean.setDesignaNumero(new Integer (designaBean.getNumero().intValue()));
					asistenciaBean.setDesignaTurno(designaBean.getIdTurno());
					
					if (!asistenciaAdm.update(asistenciaBean)) {
						return false;
					}
				}	
			}
			

			return true;
		}
		catch (Exception e) {
			throw new ClsExceptions ("Error al crear la designa desde EJG: " + e.getMessage());
		}
	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		return "refresh";
	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {

		return "exito";
	}


	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {

		ScsDesignaAdm desigAdm=new ScsDesignaAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		BuscarDesignasForm miFormulario =(BuscarDesignasForm)formulario;
		Hashtable miHash= new Hashtable();
		miHash = miFormulario.getDatos();
		if(miFormulario.getIdTurno()!=null && !miFormulario.getIdTurno().equals("")){
			String[] turnoEJG = miFormulario.getIdTurno().split(",");
			miHash.put("IDTURNO",turnoEJG[1]);
		}
		
		String consulta= "";

		try {

			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
				&& !miFormulario.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null&&!isSeleccionarTodos){
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();

				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");



				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				// jbd //
				actualizarPagina(request, desigAdm, datos);
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);




			}else{	

				databackup=new HashMap();

				//obtengo datos de la consulta 			
				PaginadorBind resultado = null;
				
				resultado=desigAdm.getBusquedaDesigna((String)usr.getLocation(),miHash);
				Vector datos = null;



				databackup.put("paginador",resultado);
				
				if (resultado!=null){ 
					
					
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)desigAdm.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
//					
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					// jbd //
					actualizarPagina(request, desigAdm, datos);
					databackup.put("datos",datos);
						
					
					
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
				

				//resultado = admBean.selectGenerico(consulta);
				//request.getSession().setAttribute("resultado",v);
			}
		
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado			
			miHash.put("BUSQUEDAREALIZADA","1");
			request.getSession().setAttribute("DATOSFORMULARIO",miHash);	
			request.getSession().setAttribute("BUSQUEDAREALIZADA", consulta.toString());


		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}

		return "listado";
	}

	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 */
	protected String generarCarta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {

		try {
			BuscarDesignasForm miForm =(BuscarDesignasForm)formulario;

			// Guardo el formulario en sesión para poder fijar a quienes hay que enviar la carta
			request.getSession().setAttribute("DATABACKUP", miForm.getDatos());

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "recogidaDatos";
	}

	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String finalizarCarta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {

		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma = usr.getLanguage().toUpperCase();

		String resultado="recogidaDatos";

		Vector vResultado= new Vector();
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;

		try {
			//obtener plantilla
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");			
			String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoCartaOficioJava")+rp.returnProperty("sjcs.directorioCartaOficioJava"))+ClsConstants.FILE_SEP+institucion;

			// RGG cambio de codigos 
			String lenguajeExt ="es";
			AdmLenguajesAdm al = new AdmLenguajesAdm(this.getUserBean(request));
			lenguajeExt=al.getLenguajeExt(idioma);

			String nombrePlantilla="plantillaCartaOficio_"+lenguajeExt+".fo";
			InformeBusquedaDesignas informe = new InformeBusquedaDesignas();
			String contenidoPlantilla = informe.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);

			//obtener la ruta de descarga
			String rutaServidor =
				Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava"))+
				ClsConstants.FILE_SEP+institucion;

			rutaFin=new File(rutaServidor);
			if (!rutaFin.exists()){
				if(!rutaFin.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}    
			String rutaServidorTmp=rutaServidor+ClsConstants.FILE_SEP+"tmp_oficios_"+System.currentTimeMillis();
			rutaTmp=new File(rutaServidorTmp);
			if(!rutaTmp.mkdirs()){
				throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
			}

			//obtener los datos comunes
			Hashtable datosComunes= this.obtenerDatosComunes(request);

			//buscar los registros seleccionados
			Hashtable miHash= (Hashtable) request.getSession().getAttribute("DATABACKUP");


			ScsDesignaAdm adm =  new ScsDesignaAdm(this.getUserBean(request));
			vResultado = adm.ejecutaSelect(this.obtenerConsultaCarta(institucion,miHash));

			if (vResultado!=null && !vResultado.isEmpty()){
				boolean correcto=true;
				Enumeration lista=vResultado.elements();

				while(correcto && lista.hasMoreElements()){
					Hashtable datosBase=(Hashtable)lista.nextElement();
					datosBase.putAll(datosComunes);
					File fPdf = informe.generarInforme(request,datosBase,rutaServidorTmp,contenidoPlantilla,rutaServidorTmp,"cartaOficio_" +numeroCarta);
					correcto=(fPdf!=null);
					if(correcto){
						ficherosPDF.add(fPdf);
						numeroCarta++;
					}
				}

				if(correcto){
					// Ubicacion de la carpeta donde se crean los ficheros PDF:
					String nombreFicheroZIP="cartasOficio_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip=rutaServidor + File.separator;

					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					request.setAttribute("nombreFichero", nombreFicheroZIP + ".zip");
					request.setAttribute("rutaFichero", rutaServidorDescargasZip+nombreFicheroZIP + ".zip");			
					request.setAttribute("borrarFichero", "true");			

					//resultado = "descargaFichero";				
					request.setAttribute("generacionOK","OK");			

				}else{
					request.setAttribute("generacionOK","ERROR");			
				}
				resultado = "recogidaDatos";


			}else{
				resultado=exitoModalSinRefresco("gratuita.retenciones.noResultados",request);
			}

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		} finally{ 
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
		return resultado;
	}

	/**
	 * 
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		BuscarDesignasForm miForm =(BuscarDesignasForm)formulario;
		request.setAttribute("nombreFichero", miForm.getFicheroDownload());
		request.setAttribute("rutaFichero", miForm.getRutaFicheroDownload());			
		request.setAttribute("borrarFichero", miForm.getBorrarFicheroDownload());			
		return "descargaFichero";
	}


	protected String obtenerConsultaCarta(String location, Hashtable filtros)throws ClsExceptions{
		String consulta =	
			" select des.estado, des.anio, to_char(des.fechaentrada,'DD/MM/YYYY') fechaentrada,"+
			" des.idturno, turno.nombre turno, des.numero,"+
			" des.resumenasunto, des.observaciones," +
			" des.idinstitucion_juzg, des.idjuzgado,"+
			" cliente.idpersona IDLETRADO "+
			" from scs_designa des, scs_designasletrado deslet, scs_turno turno, cen_colegiado colegiado, cen_persona cliente"+
			" where des.idinstitucion               = deslet.idinstitucion"+
			" and des.idturno                       = deslet.idturno"+
			" and des.anio                          = deslet.anio"+
			" and des.numero                        = deslet.numero"+
			" and deslet.idinstitucion              = colegiado.idinstitucion"+
			" and deslet.idpersona                  = colegiado.idpersona"+
			" and turno.idinstitucion               = des.idinstitucion"+
			" and turno.idturno                     = des.idturno"+
			" and colegiado.idpersona               = deslet.idpersona"+
			" and cliente.idpersona                 = colegiado.idpersona"+
			" and des.idinstitucion                 = "+ location+ " "+
			this.prepararConsulta(filtros)+
			this.prepararSubConsulta(filtros)+
			" order by turno, anio, numero";
		return consulta;

	}


	/**
	 * Este método reemplaza los valores comunes en las plantillas FO
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected Hashtable obtenerDatosComunes(HttpServletRequest request) throws ClsExceptions{
		BuscarDesignasForm miForm =(BuscarDesignasForm)request.getSession().getAttribute("BuscarDesignasForm");
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma = usr.getLanguage();

		Hashtable datos= new Hashtable();
		UtilidadesHash.set(datos,"CABECERA_CARTA_EJG",miForm.getCabeceraCarta());
		UtilidadesHash.set(datos,"MOTIVO_CARTA_EJG",miForm.getMotivoCarta());
		UtilidadesHash.set(datos,"PIE_CARTA_EJG",miForm.getPieCarta());
		UtilidadesHash.set(datos,"FECHA",UtilidadesBDAdm.getFechaBD(""));
		UtilidadesHash.set(datos,"TEXTO_TRATAMIENTO_DESTINATARIO",UtilidadesString.getMensajeIdioma(idioma,"informes.cartaAsistencia.estimado"));
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");			
		String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoCartaOficioJava")+rp.returnProperty("sjcs.directorioCartaOficioJava"))+ClsConstants.FILE_SEP+institucion;
		UtilidadesHash.set(datos,"RUTA_LOGO",rutaPlantilla+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"logo.jpg");

		return datos;
	}

	public static String[] ejecutarPL_OBTENERCODIGODESIGNA (String idInstitucion) {

		Object[] paramIn = new Object[1]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL

		try {
			// Parametros de entrada del PL
			paramIn[0] = idInstitucion.toString();

			resultado = ClsMngBBDD.callPLProcedure("{call proc_OBTENERCODIGODESIGNA(?,?,?)}", 
					2, 
					paramIn);
		} catch (Exception e) {
			resultado[0] = "-1"; 	// P_CONTADOR
			resultado[1] = "-1"; // ERROR P_DATOSERROR
		}
		//Resultado del PL        
		return resultado;
	}

	protected String buscarJuzgado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		try {
			BuscarDesignasForm miform = (BuscarDesignasForm)formulario;
			ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
			String codigoExt = miform.getCodigoExtJuzgado().toUpperCase();
			String where = " where upper(codigoext) = upper ('"+codigoExt+"')" +
			" and idinstitucion="+this.getUserBean(request).getLocation();
			Vector resultadoJuzgado = juzgadoAdm.select(where);
			if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
				ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			}
			request.setAttribute("resultadoJuzgado",resultadoJuzgado);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "buscarJuzgado";
	}

	/**
	 * Actualizamos el paginador con los datos adicionales para que no se haga desde la jsp que queda feo y cutre 
	 * @throws SIGAException 
	 */
	private Vector actualizarPagina(HttpServletRequest request,ScsDesignaAdm desigAdm,Vector datos) throws ClsExceptions, SIGAException{
		String turnoDesig="";
		String actNoValida="";
		String defendidos="";
		String letradoDesig="";
		String IDletradoDesig="";
		String fechaEntrada="";
		String nColegiado="";
		Row fila;
		// Obtenemos las relaciones de la EJG con designaciones y asistencias para mostrarlas en la busqueda
		try{

			for (int recordNumber = 1,contadorFila=1; recordNumber-1 < datos.size(); recordNumber++)
			{	
				fila = (Row)datos.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();

				String idInstitucion = (String) registro.get("IDINSTITUCION"); 
				String desAnio   = (String) registro.get("ANIO");
				String desNumero = (String) registro.get("NUMERO");
				String desIdTurno  = (String) registro.get("IDTURNO");

				turnoDesig =  desigAdm.getNombreTurnoDes(idInstitucion, desIdTurno);
				actNoValida =  desigAdm.getActDesig_NoValidar(idInstitucion, desIdTurno,desAnio,desNumero);
				defendidos =  desigAdm.getDefendidosDesigna(idInstitucion, desIdTurno,desAnio,desNumero,"1");
				letradoDesig =  desigAdm.getLetradoDesig(idInstitucion, desIdTurno, desAnio,desNumero);
				IDletradoDesig =  desigAdm.getIDLetradoDesig(idInstitucion, desIdTurno,desAnio,desNumero);
				fechaEntrada = GstDate.getFormatedDateShort("",desigAdm.getFechaEntrada(idInstitucion, desIdTurno,desAnio,desNumero));
				if (IDletradoDesig==null || IDletradoDesig.equals("")){
					 IDletradoDesig=" ";
				} 
				nColegiado =  desigAdm.getNColegiadoDesig(idInstitucion, desIdTurno,desAnio,desNumero);
				if (nColegiado==null || nColegiado.equals("")){
					 nColegiado=" ";
				} 
				registro.put( "TURNODESIG"    ,turnoDesig );
				registro.put( "ACTNOVALIDA"   ,actNoValida );
				registro.put( "DEFENDIDOS"    ,defendidos );
				registro.put( "LETRADODESIG"  ,letradoDesig );
				registro.put( "IDLETRADODESIG",IDletradoDesig );
				registro.put( "FECHAENTRADA"  ,fechaEntrada );
				registro.put( "NCOLEGIADO"    ,nColegiado );

			}
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return datos;

	}

	protected void getAjaxTurnos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		
		BuscarDesignasForm miForm = (BuscarDesignasForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		//Recogemos el parametro enviado por ajax
		String idTipoTurno = request.getParameter("idTipoTurno");

		miForm.setIdTipoTurno(idTipoTurno);
		ClsLogging.writeFileLog("BUSQUEDA DESIGNA:getAjaxTurnos.fechaGuardia:"+idTipoTurno+"/", 10);
		
		//Sacamos los turnos
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(this.getUserBean(request));
		List<ScsTurnoBean> alTurnos = admTurnos.getTurnosConTipo(usr.getLocation(),miForm.getIdTipoTurno());
		ClsLogging.writeFileLog("BUSQUEDA DESIGNA:Select Turnos", 10);
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
		}else{
			for(ScsTurnoBean turno:alTurnos){
				ClsLogging.writeFileLog("BUSQUEDA DESIGNA:turno:"+turno.getNombre(), 10);
			}
		}
		ClsLogging.writeFileLog("BUSQUEDA DESIGNA:Fin Select Turnos", 10);
	    respuestaAjax(new AjaxCollectionXmlBuilder<ScsTurnoBean>(), alTurnos,response);
	}		
	
}