package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.ScsActuacionAsistCosteFijoAdm;
import com.siga.beans.ScsActuacionAsistCosteFijoBean;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AsistenciasForm;

/**
 * @author carlos.vidal
 * @since 3/2/2005
 * @version 06/04/2006 (david.sanchezp): modificaciones para incluir los combos de tipo de actuacion y coste fijo.
 */

public class ActuacionesAsistenciaLetradoAction extends MasterAction {
	
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
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		AsistenciasForm miForm 	= (AsistenciasForm)formulario;
		Vector resultado = null;
		
		try {
			ScsActuacionAsistenciaAdm actuaciones = new ScsActuacionAsistenciaAdm(this.getUserBean(request));
			ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
			
			//Datos de la pestanha:
			String modoPestanha = request.getParameter("MODO");
			String anio 	= request.getParameter("ANIO");
			String numero 	= request.getParameter("NUMERO");
			
			//Modo de la pestanha: (inicialmente en la URL de la pestanha, luego en el formulario)
			if (modoPestanha==null)
				modoPestanha = miForm.getModoPestanha(); 
			miForm.setModoPestanha(modoPestanha);			
			
			//Anio y Numero seran nulos si no vengo de pulsar en la pestanha:
			if (anio == null || numero == null) {
				anio 	= miForm.getAnio();
				numero 	= miForm.getNumero();
			}
			miForm.setAnio(anio);
			miForm.setNumero(numero);

			//Consulto los datos a mostrar:
			/*String where = " where IDINSTITUCION = "+usr.getLocation()+" AND "+
			   " ANIO = "+anio+" AND NUMERO = "+numero;*/
			/** PDM Modificada la consulta para que se obtenga tambien el nombre de la facturacion**/
			String consulta=
				" SELECT IDINSTITUCION,"+
				" ANIO,"+
				" NUMERO,"+
				" IDACTUACION,"+
				" FECHA,"+
				" DIADESPUES,"+
				" ACUERDOEXTRAJUDICIAL,"+
				" FECHAMODIFICACION,"+
				" USUMODIFICACION,"+
				" FECHAJUSTIFICACION,"+
				" DESCRIPCIONBREVE,"+
				" LUGAR,"+
				" NUMEROASUNTO,"+
				" ANULACION,"+
				" OBSERVACIONESJUSTIFICACION,"+
				" IDFACTURACION,"+
				" OBSERVACIONES,"+
				" FACTURADO,"+
				" (select "+FcsFacturacionJGBean.C_NOMBRE+"||' ('||TO_CHAR("+FcsFacturacionJGBean.C_FECHADESDE+",'DD/MM/YYYY')||'-'||TO_CHAR("+FcsFacturacionJGBean.C_FECHAHASTA+",'DD/MM/YYYY')||')'"+
				" from "+FcsFacturacionJGBean.T_NOMBRETABLA+
				" where "+FcsFacturacionJGBean.C_IDINSTITUCION+" = "+usr.getLocation()+
				"   and "+FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_IDFACTURACION+" = "+ScsActuacionAsistenciaBean.T_NOMBRETABLA+"."+ScsActuacionAsistenciaBean.C_IDFACTURACION+") nombrefacturacion,"+
				/** pdm INC-xxx1**/
				 "DECODE("+ScsActuacionAsistenciaBean.T_NOMBRETABLA+"."+ScsActuacionAsistenciaBean.C_VALIDADA+",'1','Si','No') validada"+	
				/**/
				"  FROM "+ScsActuacionAsistenciaBean.T_NOMBRETABLA+
				"  where IDINSTITUCION = "+usr.getLocation()+" AND "+
				"  ANIO = "+anio+" AND NUMERO = "+numero;
			
			try {
				//resultado=(Vector)actuaciones.selectTabla(where);
				resultado = (Vector)actuaciones.select(consulta);
				
			} catch(Exception e_consulta) {
				resultado=null;
			}
			if (resultado.isEmpty())
				resultado=null;
			request.setAttribute("resultado",resultado);
		
			//Vemos si tiene boton nuevo:
			//Nota: Si tiene fechaAnulacion no lo tiene.
			request.setAttribute("botonNuevo", "si");
			Hashtable d = new Hashtable();
			UtilidadesHash.set(d, ScsAsistenciasBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set(d, ScsAsistenciasBean.C_ANIO, anio);
			UtilidadesHash.set(d, ScsAsistenciasBean.C_NUMERO, numero);
			Vector v = asistenciaAdm.select(d);
			if ((v != null) && (v.size() == 1)) {
				String fechaAnulacion = ((ScsAsistenciasBean)v.get(0)).getFechaAnulacion();
				if ((fechaAnulacion != null) && (!fechaAnulacion.equals("")))
					request.setAttribute("botonNuevo", "no");
			}
			
			//Vemos si viene de Ficha Colegial:
			String esFichaColegial = "1"; //request.getParameter("esFichaColegial");
			if (esFichaColegial == null) { 
				esFichaColegial = (String) request.getAttribute("esFichaColegial");
				if (esFichaColegial == null)
					esFichaColegial = miForm.getEsFichaColegial();
			}			
			miForm.setEsFichaColegial(esFichaColegial);						

			
			// Miramos si tiene permiso para la edicion de letrados
			try {
				String sql = "SELECT " + ScsTurnoBean.C_LETRADOACTUACIONES + 
				              " FROM " + ScsAsistenciasBean.T_NOMBRETABLA + " asistencia, " + 
							             ScsTurnoBean.T_NOMBRETABLA + " turno " + 
				             " WHERE asistencia." + ScsAsistenciasBean.C_ANIO + " = " + anio +
							   " AND asistencia." + ScsAsistenciasBean.C_NUMERO + " = " + numero + 
							   " AND asistencia." + ScsAsistenciasBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request) +
							   " AND asistencia." + ScsAsistenciasBean.C_IDTURNO + " = turno." + ScsTurnoBean.C_IDTURNO +  
							   " AND asistencia." + ScsAsistenciasBean.C_IDINSTITUCION + " = turno." + ScsTurnoBean.C_IDINSTITUCION;  
				Vector t = asistenciaAdm.selectGenerico(sql);
				if (t != null || t.size() == 1) {
					String tt = (String)((Hashtable)t.get(0)).get(ScsTurnoBean.C_LETRADOACTUACIONES);
					request.setAttribute("LETRADOACTUACIONES", tt);
				}
			}
			catch (Exception e) { }
			
			//Mando el formulario para tener el valor del modo de la pestanha:
			request.setAttribute("FormularioActuacionAsistencia",miForm);
		}
		catch (Exception e) 
		{
			throwExcp("error.messages.editar",e,null); 
		} 
		return "listado";
	}
    
	/**Funcion que transforma los datos de entrada para poder hacer la insercion a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos){
		return datos;
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

		try	{
			UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			// Obtenemos los datos de la asistencia y de la actuacion.
			Hashtable hash = new Hashtable();
			Vector ocultos = formulario.getDatosTablaOcultos(0); 
			String anio 	= (String) ocultos.get(0);
			String numero 	= (String) ocultos.get(1);
			String idactuacion 	= (String) ocultos.get(2);
			String select = 
				" SELECT "+
				" asistencia.anio aanio,"+
				" asistencia.numero anumero,"+
				" asistencia.fechaanulacion fechaanulacion,"+
				" turno.nombre tnombre,"+
				" guardiasturno.nombre gtnombre,"+
				" personajg.nif pjgnif,"+
				" personajg.nombre pjgnombre,"+
				" personajg.apellido1 pjgapellido1,"+
				" personajg.apellido2 pjgapellido2,"+
				" colegiado.ncolegiado cncolegiado,"+
				" persona.nombre pnombre,"+
				" persona.apellidos1 papellidos1,"+
				" persona.apellidos2 papellidos2,"+
				" actuacion.idactuacion acidactuacion,"+
				" actuacion.numero acnumero,"+
				" actuacion.fecha acfecha,"+
				" actuacion.diadespues acdiadespues,"+
				" actuacion.descripcionbreve acdbreve,"+
				" actuacion.lugar aclugar,"+
				" actuacion.numeroasunto acnumeroasunto,"+
				" actuacion.acuerdoextrajudicial acaextrajudicial,"+
				" actuacion.anulacion acanulacion,"+
				" actuacion.observaciones acobservaciones,"+
				" actuacion.fechajustificacion acfjustificacion,"+
				" actuacion.observacionesjustificacion acojustificacion, "+
				" turno." + ScsTurnoBean.C_VALIDARJUSTIFICACIONES + " validarJustificaciones, " +
				" actuacion." + ScsActuacionAsistenciaBean.C_VALIDADA + " actuacionValidada," +
				" actuacion." + ScsActuacionAsistenciaBean.C_IDJUZGADO+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDCOMISARIA+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDTIPOACTUACION+","+
				" asistencia." + ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO+" idtipoasistenciacolegio,"+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA+" idtipoasistencia,"+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDPRISION+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION+","+
				" coste." + ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO+", "+
				" turno."+ScsTurnoBean.C_IDTURNO+" idturno "+
				" FROM"+
				" scs_asistencia asistencia,"+
				" scs_Turno turno,"+
				" scs_guardiasturno guardiasturno, "+
				" scs_personajg personajg, "+
				" cen_colegiado colegiado, "+
				" cen_persona persona,"+
				" scs_actuacionasistencia actuacion,"+
				" SCS_ACTUACIONASISTCOSTEFIJO coste"+
				" WHERE"+
				" actuacion.IDINSTITUCION = "+usr.getLocation()+
				" AND actuacion.ANIO = "+anio+
				" AND actuacion.NUMERO = "+numero+
				" AND actuacion.IDACTUACION = "+idactuacion+
				//JOINS:
				//Para los costes:
				" and actuacion.idinstitucion = coste.idinstitucion"+" (+)"+
				" and actuacion.anio = coste.anio"+" (+)"+
				" and actuacion.numero = coste.numero"+" (+)"+
				" and actuacion.idactuacion = coste.idactuacion"+" (+)"+
				" and actuacion.idtipoasistencia = coste.idtipoasistencia"+" (+)"+
				" and actuacion.idtipoactuacion = coste.idtipoactuacion"+" (+)"+
				//Otros:
				" and actuacion.idinstitucion = asistencia.idinstitucion"+
				" and actuacion.anio = asistencia.anio"+
				" and actuacion.numero = asistencia.numero"+
				" and asistencia.idinstitucion = colegiado.idinstitucion"+
				" and asistencia.idpersonacolegiado = colegiado.idpersona"+
				" and asistencia.idinstitucion = guardiasturno.idinstitucion"+
				" and asistencia.idturno = guardiasturno.idturno"+
				" and asistencia.idguardia = guardiasturno.idguardia"+
				" and asistencia.idinstitucion = personajg.idinstitucion(+)"+
				" and asistencia.idpersonajg = personajg.idpersona(+)"+
				" and turno.idinstitucion = asistencia.idinstitucion"+
				" and turno.IDTURNO = asistencia.idturno"+
				" and colegiado.idpersona = persona.idpersona";

			ScsAsistenciasAdm asistencia = new ScsAsistenciasAdm(this.getUserBean(request));
			Vector resultado = null;
			resultado=(Vector)asistencia.ejecutaSelect(select);
			request.setAttribute("resultado",resultado);
			request.setAttribute("MODO","editar");
			//Botones GuardarCerrar y Cerrar
			request.setAttribute("botones","Y,C");

		} catch (Exception e) 
		{
			throwExcp("messages.select.error",e,null); 
		} 
		return "editar";
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
		
		try	{
			UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
			// Obtenemos los datos de la asistencia y de la actuacion.
			Hashtable hash = new Hashtable();
			Vector ocultos = formulario.getDatosTablaOcultos(0); 
			String anio 	= (String) ocultos.get(0);
			String numero 	= (String) ocultos.get(1);
			String idactuacion 	= (String) ocultos.get(2);
			String select = 
				" SELECT"+
				" asistencia.anio aanio,"+
				" asistencia.numero anumero,"+
				" turno.nombre tnombre,"+
				" guardiasturno.nombre gtnombre,"+
				" personajg.nif pjgnif,"+
				" personajg.nombre pjgnombre,"+
				" personajg.apellido1 pjgapellido1,"+
				" personajg.apellido2 pjgapellido2,"+
				" colegiado.ncolegiado cncolegiado,"+
				" persona.nombre pnombre,"+
				" persona.apellidos1 papellidos1,"+
				" persona.apellidos2 papellidos2,"+
				" actuacion.idactuacion acidactuacion,"+
				" actuacion.numero acnumero,"+
				" actuacion.fecha acfecha,"+
				" actuacion.diadespues acdiadespues,"+
				" actuacion.descripcionbreve acdbreve,"+
				" actuacion.lugar aclugar,"+
				" actuacion.numeroasunto acnumeroasunto,"+
				" actuacion.acuerdoextrajudicial acaextrajudicial,"+
				" actuacion.anulacion acanulacion,"+
				" actuacion.observaciones acobservaciones,"+
				" actuacion.fechajustificacion acfjustificacion,"+
				" actuacion.observacionesjustificacion acojustificacion, "+
				" turno." + ScsTurnoBean.C_VALIDARJUSTIFICACIONES + " validarJustificaciones, " +
				" actuacion." + ScsActuacionAsistenciaBean.C_VALIDADA + " actuacionValidada," +
				" actuacion." + ScsActuacionAsistenciaBean.C_IDJUZGADO+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDCOMISARIA+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDTIPOACTUACION+","+
				" asistencia." + ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDPRISION+","+
				" actuacion." + ScsActuacionAsistenciaBean.C_IDINSTITUCIONPRISION+","+
				" coste." + ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO+","+
				UtilidadesMultidioma.getCampoMultidiomaSimple("costeFijo.DESCRIPCION", usr.getLanguage()) + "  DESCRIPCIONCOSTEFIJO, "+
				" turno."+ScsTurnoBean.C_IDTURNO+
				" FROM"+
				" scs_asistencia asistencia,"+
				" scs_Turno turno,"+
				" scs_guardiasturno guardiasturno, "+
				" scs_personajg personajg, "+
				" cen_colegiado colegiado, "+
				" cen_persona persona,"+
				" scs_actuacionasistencia actuacion,"+
				" SCS_ACTUACIONASISTCOSTEFIJO coste,"+
				" SCS_COSTEFIJO costeFijo"+
				" where"+
				" actuacion.IDINSTITUCION = "+usr.getLocation()+
				" AND actuacion.ANIO = "+anio+
				" AND actuacion.NUMERO = "+numero+
				" AND actuacion.IDACTUACION = "+idactuacion+
				//JOINS:
				//Para los costes:
				" and actuacion.idinstitucion = coste.idinstitucion"+" (+)"+
				" and actuacion.anio = coste.anio"+" (+)"+
				" and actuacion.numero = coste.numero"+" (+)"+
				" and actuacion.idactuacion = coste.idactuacion"+" (+)"+
				" and actuacion.idtipoasistencia = coste.idtipoasistencia"+" (+)"+
				" and actuacion.idtipoactuacion = coste.idtipoactuacion"+" (+)"+
				//Join para la descripcion del coste:
				" and coste.idinstitucion = costeFijo.idinstitucion"+" (+)"+
				" and coste.idcostefijo = costeFijo.idcostefijo"+" (+)"+				
				//Otros:
				" and actuacion.idinstitucion = asistencia.idinstitucion"+
				" and actuacion.anio = asistencia.anio"+
				" and actuacion.numero = asistencia.numero"+
				" and asistencia.idinstitucion = colegiado.idinstitucion"+
				" and asistencia.idpersonacolegiado = colegiado.idpersona"+
				" and asistencia.idinstitucion = guardiasturno.idinstitucion"+
				" and asistencia.idturno = guardiasturno.idturno"+
				" and asistencia.idguardia = guardiasturno.idguardia"+
				" and asistencia.idinstitucion = personajg.idinstitucion(+)"+
				" and asistencia.idpersonajg = personajg.idpersona(+)"+
				" and turno.idinstitucion = asistencia.idinstitucion"+
				" and turno.IDTURNO = asistencia.idturno"+
				" and colegiado.idpersona = persona.idpersona";
			ScsAsistenciasAdm asistencia = new ScsAsistenciasAdm(this.getUserBean(request));
			Vector resultado = null;
			resultado=(Vector)asistencia.ejecutaSelect(select);
			request.setAttribute("resultado",resultado);
			request.setAttribute("botones","c");
			request.setAttribute("MODO","consulta");
			String esFichaColegial = ((AsistenciasForm)formulario).getEsFichaColegial();
			request.setAttribute("esFichaColegial",esFichaColegial);
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null); 
		} 
		return "editar";
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		try
		{
			UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			String anio = miForm.getAnio();
			String numero = miForm.getNumero();
			
			// Obtenemos los datos de la asistencia y de la actuacion.
			String select = 
				" SELECT"+
				" asistencia.anio aanio,"+
				" asistencia.numero anumero,"+
				" asistencia.fechaanulacion fechaanulacion,"+
				" turno.nombre tnombre,"+
				" guardiasturno.nombre gtnombre,"+
				" personajg.nif pjgnif,"+
				" personajg.nombre pjgnombre,"+
				" personajg.apellido1 pjgapellido1,"+
				" personajg.apellido2 pjgapellido2,"+
				" colegiado.ncolegiado cncolegiado,"+
				" persona.nombre pnombre,"+
				" persona.apellidos1 papellidos1,"+
				" persona.apellidos2 papellidos2, "+
				" turno." + ScsTurnoBean.C_VALIDARJUSTIFICACIONES + " validarJustificaciones ," +
				" asistencia."+ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO+" idtipoasistenciacolegio, "+
				" asistencia."+ScsAsistenciasBean.C_IDTIPOASISTENCIA+" idtipoasistencia, "+
				" turno."+ScsTurnoBean.C_IDTURNO+" idturno "+
				" FROM"+
				" scs_asistencia asistencia,"+
				" scs_Turno turno,"+
				" scs_guardiasturno guardiasturno, "+
				" scs_personajg personajg, "+
				" cen_colegiado colegiado, "+
				" cen_persona persona"+
				" where"+
				" asistencia.IDINSTITUCION = "+usr.getLocation()+
				" AND asistencia.ANIO = "+anio+
				" AND asistencia.NUMERO = "+numero+
				" and asistencia.idinstitucion = colegiado.idinstitucion"+
				" and asistencia.idpersonacolegiado = colegiado.idpersona"+
				" and asistencia.idinstitucion = guardiasturno.idinstitucion"+
				" and asistencia.idturno = guardiasturno.idturno"+
				" and asistencia.idguardia = guardiasturno.idguardia"+
				" and asistencia.idinstitucion = personajg.idinstitucion(+)"+
				" and asistencia.idpersonajg = personajg.idpersona(+)"+
				" and turno.idinstitucion = asistencia.idinstitucion"+
				" and turno.IDTURNO = asistencia.idturno"+
				" and colegiado.idpersona = persona.idpersona";
			ScsAsistenciasAdm asistencia = new ScsAsistenciasAdm(this.getUserBean(request));
			Vector resultado = null;
			resultado=(Vector)asistencia.ejecutaSelect(select);
			request.setAttribute("resultado",resultado);
			
			//Obtenemos el numero de actuacion.
			select = "SELECT MAX(IDACTUACION)+1 maxvalor FROM scs_actuacionasistencia WHERE "+
				" IDINSTITUCION = "+usr.getLocation()+
				" AND ANIO = "+anio+
				" AND NUMERO = "+numero;
			resultado=(Vector)asistencia.ejecutaSelect(select);
			
			String idact = (String) ((Hashtable) resultado.get(0)).get("MAXVALOR");
			if(idact.equals("")) idact = "1";
			request.setAttribute("idactuacion",idact);
			request.setAttribute("MODO","alta");
			request.setAttribute("botones","y,r,c");
			String esFichaColegial = miForm.getEsFichaColegial();
			request.setAttribute("esFichaColegial",esFichaColegial);
			
			//Mando el formulario para tener el valor del modo de la pestanha:
			request.setAttribute("FormularioActuacionAsistencia",miForm);
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null); 
		} 
		return "editar";
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected synchronized String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		UsrBean usr = null;
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		AsistenciasForm miForm 	= (AsistenciasForm)formulario;
		String idTipoActuacion = "";
		
		try {
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();

			ScsActuacionAsistenciaAdm admActuacionAsistencia = new ScsActuacionAsistenciaAdm(this.getUserBean(request));			
			
			//Para el combo tipo Actuacion:
			if (miForm.getIdTipoActuacion()!=null && !miForm.getIdTipoActuacion().equals(""))
				idTipoActuacion = miForm.getIdTipoActuacion();				

			
			//
			//1.- Preparamos el registro con el nuevo coste:
			//
			Hashtable hashCoste = new Hashtable();
			ScsActuacionAsistCosteFijoAdm admActuacionAsistCosteFijo = new ScsActuacionAsistCosteFijoAdm(this.getUserBean(request));
			if (miForm.getIdCosteFijo()!=null && !miForm.getIdCosteFijo().equals("")) {
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION, usr.getLocation());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_ANIO, miForm.getAnio());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_NUMERO, miForm.getNumero());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDACTUACION, miForm.getAcidactuacion());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION, idTipoActuacion);
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA, miForm.getIdTipoAsistenciaColegio());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO, miForm.getIdCosteFijo());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_FECHAMODIFICACION, "SYSDATE");
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_USUMODIFICACION, usr.getUserName());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_FACTURADO, "");
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_PAGADO, "");
			}
			

			//
			//2.- Preparamos el registro con la nueva actuacion:
			//
			// Campos a clave
			hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsActuacionAsistenciaBean.C_ANIO,miForm.getAnio());
			hash.put(ScsActuacionAsistenciaBean.C_NUMERO,miForm.getNumero());
			hash.put(ScsActuacionAsistenciaBean.C_IDACTUACION,miForm.getAcidactuacion());
			if(miForm.getAcaextrajudicial() != null && !miForm.getAcaextrajudicial().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL,"1");
			else
				hash.put(ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL,"0");
			
			if(miForm.getAcanulacion() != null && !miForm.getAcanulacion().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_ANULACION,"1");
			else
				hash.put(ScsActuacionAsistenciaBean.C_ANULACION,"0");
			hash.put(ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE,miForm.getAcdbreve());
			
			if(miForm.getAcdiadespues() != null && !miForm.getAcdiadespues().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_DIADESPUES,"S");
			else
				hash.put(ScsActuacionAsistenciaBean.C_DIADESPUES,"N");
			if(miForm.getAcfecha()!=null && !miForm.getAcfecha().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getAcfecha()));
			if(miForm.getAcfjustificacion()!=null && !miForm.getAcfjustificacion().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getAcfjustificacion()));

			hash.put(ScsActuacionAsistenciaBean.C_NUMEROASUNTO,miForm.getAcnumeroasunto());
			hash.put(ScsActuacionAsistenciaBean.C_OBSERVACIONES,miForm.getAcobservaciones());
			hash.put(ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION,miForm.getAcojustificacion());
			
			if (miForm.getActuacionValidada() != null) {
				if (UtilidadesString.stringToBoolean(miForm.getActuacionValidada())) {
					hash.put(ScsActuacionAsistenciaBean.C_VALIDADA, "1");
				} else { 
					hash.put(ScsActuacionAsistenciaBean.C_VALIDADA, "0");
				}
				
			}

			 			
			//Para el combo Comisaria:
			if (miForm.getComisaria()!=null && !miForm.getComisaria().equals("")) {
				String comisaria = miForm.getComisaria();				
				hash.put(ScsActuacionAsistenciaBean.C_IDCOMISARIA,comisaria.substring(0,comisaria.indexOf(",")));
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA,comisaria.substring(comisaria.indexOf(",")+1));
			} else {
				hash.put(ScsActuacionAsistenciaBean.C_IDCOMISARIA,"");
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA,"");
			}
			
			//Para el combo Juzgado:
			if (miForm.getJuzgado()!=null && !miForm.getJuzgado().equals("")) {
				String juzgado = miForm.getJuzgado();				
				hash.put(ScsActuacionAsistenciaBean.C_IDJUZGADO, juzgado.substring(0,juzgado.indexOf(",")));
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO, juzgado.substring(juzgado.indexOf(",")+1));
			} else {
				hash.put(ScsActuacionAsistenciaBean.C_IDJUZGADO, "");
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO, "");
			}

			//Para el combo tipo Actuacion:
			hash.put(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION, idTipoActuacion);
			//Para el combo tipo Costes de Actuacion:
			hash.put(ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA, miForm.getIdTipoAsistenciaColegio());

			
			//Inicio Transaccion:
			tx.begin();
			if (miForm.getActuacionValidada() != null) {
			// RGG valido la cabecera de guardia de la actuacion
				ScsCabeceraGuardiasAdm admCab = new ScsCabeceraGuardiasAdm(usr);
				admCab.actualizarValidacionCabecera(usr.getLocation(),miForm.getAnio(),miForm.getNumero(),UtilidadesString.stringToBoolean(miForm.getActuacionValidada()));
			}
			admActuacionAsistencia.insert(hash);
			if (miForm.getIdCosteFijo()!=null && !miForm.getIdCosteFijo().equals(""))
				admActuacionAsistCosteFijo.insert(hashCoste);			
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success",request);
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
			throws SIGAException  {
		UsrBean usr = null;
		UserTransaction tx = null;
		String idTipoActuacion = "";
		String idTipoAsistencia = "";
		
		try {
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			Hashtable hash = new Hashtable();
			
			ScsActuacionAsistenciaAdm admActuacionAsistencia = new ScsActuacionAsistenciaAdm(this.getUserBean(request));
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			request.setAttribute("mensaje","messages.updated.success");

			//Para el combo tipo Actuacion:
			if (miForm.getIdTipoActuacion()!=null && !miForm.getIdTipoActuacion().equals(""))
				idTipoActuacion = miForm.getIdTipoActuacion();				

			idTipoAsistencia = miForm.getIdTipoAsistenciaColegio();
			
			//
			//1.- Preparamos la sql con la que borramos todos los registros de coste asociados a la actuacion:
			//Nota: hacemos esto al modificar un campo clave.
			//
			//Presupongo que no se podra modificar ni borrar cuando este facturado este coste(...)
			Hashtable htCosteABorrar = new Hashtable();
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION, usr.getLocation());
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_ANIO, miForm.getAnio());
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_NUMERO, miForm.getNumero());
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_IDACTUACION, miForm.getAcidactuacion());
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA, miForm.getIdTipoAsistenciaColegio());
			String clavesCosteABorrar[] =  new String[]{ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION,
					ScsActuacionAsistCosteFijoBean.C_ANIO,ScsActuacionAsistCosteFijoBean.C_NUMERO,
					ScsActuacionAsistCosteFijoBean.C_IDACTUACION,ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA};
			
			
			//
			//2.- Preparamos el registro con el nuevo coste a insertar:
			//
			ScsActuacionAsistCosteFijoAdm admActuacionAsistCosteFijo = new ScsActuacionAsistCosteFijoAdm(this.getUserBean(request));
			Hashtable hashCoste = new Hashtable();
			if (miForm.getIdCosteFijo()!=null && !miForm.getIdCosteFijo().equals("")) {				
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION, usr.getLocation());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_ANIO, miForm.getAnio());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_NUMERO, miForm.getNumero());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDACTUACION, miForm.getAcidactuacion());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION, idTipoActuacion);
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA, miForm.getIdTipoAsistenciaColegio());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO, miForm.getIdCosteFijo());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_FECHAMODIFICACION, "SYSDATE");
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_USUMODIFICACION, usr.getUserName());
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_FACTURADO, "");
				hashCoste.put(ScsActuacionAsistCosteFijoBean.C_PAGADO, "");				
			}

			
			//
			//3.- Preparamos el registro de la actuacion a modificar:
			//
			// Campos a clave
			hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsActuacionAsistenciaBean.C_ANIO,miForm.getAnio());
			hash.put(ScsActuacionAsistenciaBean.C_NUMERO,miForm.getNumero());
			hash.put(ScsActuacionAsistenciaBean.C_IDACTUACION,miForm.getAcidactuacion());
			/*String[] campos =  {ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL,
								ScsActuacionAsistenciaBean.C_ANULACION,
								ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE,
								ScsActuacionAsistenciaBean.C_DIADESPUES,
								ScsActuacionAsistenciaBean.C_FECHA,
								ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION,
								ScsActuacionAsistenciaBean.C_FECHAMODIFICACION,
								ScsActuacionAsistenciaBean.C_NUMEROASUNTO,
								ScsActuacionAsistenciaBean.C_OBSERVACIONES,
								ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION,
								ScsActuacionAsistenciaBean.C_IDDECLARACION,
								ScsActuacionAsistenciaBean.C_IDJUZGADO,
								ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO,
								ScsActuacionAsistenciaBean.C_IDCOMISARIA,
								ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA,
								ScsActuacionAsistenciaBean.C_IDTIPOACTUACION};*/

			if(miForm.getAcaextrajudicial() != null && !miForm.getAcaextrajudicial().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL,"1");
			else
				hash.put(ScsActuacionAsistenciaBean.C_ACUERDOEXTRAJUDICIAL,"0");
			
			if(miForm.getAcanulacion() != null && !miForm.getAcanulacion().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_ANULACION,"1");
			else
				hash.put(ScsActuacionAsistenciaBean.C_ANULACION,"0");
			hash.put(ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE,miForm.getAcdbreve());
			
			if(miForm.getAcdiadespues() != null && !miForm.getAcdiadespues().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_DIADESPUES,"S");
			else
				hash.put(ScsActuacionAsistenciaBean.C_DIADESPUES,"N");
			if(miForm.getAcfecha()!=null && !miForm.getAcfecha().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getAcfecha()));
			if(miForm.getAcfjustificacion()!=null && !miForm.getAcfjustificacion().equals(""))
				hash.put(ScsActuacionAsistenciaBean.C_FECHAJUSTIFICACION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getAcfjustificacion()));
			
			hash.put(ScsActuacionAsistenciaBean.C_NUMEROASUNTO,miForm.getAcnumeroasunto());
			hash.put(ScsActuacionAsistenciaBean.C_OBSERVACIONES,miForm.getAcobservaciones());
			hash.put(ScsActuacionAsistenciaBean.C_OBSERVACIONESJUSTIFICACION,miForm.getAcojustificacion());
			
			if (miForm.getActuacionValidada() != null) {
				if (UtilidadesString.stringToBoolean(miForm.getActuacionValidada())) {
					hash.put(ScsActuacionAsistenciaBean.C_VALIDADA, "1");
				} else { 
					hash.put(ScsActuacionAsistenciaBean.C_VALIDADA, "0");
				}
				
			}
			
		 
			
			//Para el combo Comisaria:
			if (miForm.getComisaria()!=null && !miForm.getComisaria().equals("")) {
				String comisaria = miForm.getComisaria();				
				hash.put(ScsActuacionAsistenciaBean.C_IDCOMISARIA,comisaria.substring(0,comisaria.indexOf(",")));
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA,comisaria.substring(comisaria.indexOf(",")+1));
			} else {
				hash.put(ScsActuacionAsistenciaBean.C_IDCOMISARIA,"");
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA,"");
			}
			
			//Para el combo Juzgado:
			if (miForm.getJuzgado()!=null && !miForm.getJuzgado().equals("")) {
				String juzgado = miForm.getJuzgado();				
				hash.put(ScsActuacionAsistenciaBean.C_IDJUZGADO, juzgado.substring(0,juzgado.indexOf(",")));
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO, juzgado.substring(juzgado.indexOf(",")+1));
			} else {
				hash.put(ScsActuacionAsistenciaBean.C_IDJUZGADO, "");
				hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO, "");
			}

			//Para el combo tipo Actuacion:
			hash.put(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION, idTipoActuacion);
			hash.put(ScsActuacionAsistenciaBean.C_IDTIPOASISTENCIA, idTipoAsistencia);

			//Inicio de la transaccion
			tx.begin();
			if (miForm.getActuacionValidada() != null) {
				// RGG valido la cabecera de guardia de la actuacion
				ScsCabeceraGuardiasAdm admCab = new ScsCabeceraGuardiasAdm(usr);
				admCab.actualizarValidacionCabecera(usr.getLocation(),miForm.getAnio(),miForm.getNumero(),UtilidadesString.stringToBoolean(miForm.getActuacionValidada()));
			}
			admActuacionAsistCosteFijo.deleteDirect(htCosteABorrar,clavesCosteABorrar);			
			admActuacionAsistencia.updateDirect(hash,null,null);
			//Si no hay coste no inserto aqui:
			if (miForm.getIdCosteFijo()!=null && !miForm.getIdCosteFijo().equals(""))
				admActuacionAsistCosteFijo.insert(hashCoste);
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.updated.success",request);
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
		UsrBean usr = null;
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		AsistenciasForm miForm 	= (AsistenciasForm)formulario;
		
		try	{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();

			// Obtenemos los datos de la asistencia y de la actuacion.
			Vector ocultos = formulario.getDatosTablaOcultos(0); 
			String anio 	= (String) ocultos.get(0);
			String numero 	= (String) ocultos.get(1);
			String idactuacion 	= (String) ocultos.get(2);
			
			//
			//1.- Preparamos los datos para borrar los registros de costes fijos de la actuacion:
			//
			Hashtable htCosteABorrar = new Hashtable();
			String clavesCosteABorrar[] =  new String[]{ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION,
					ScsActuacionAsistCosteFijoBean.C_ANIO,ScsActuacionAsistCosteFijoBean.C_NUMERO,
					ScsActuacionAsistCosteFijoBean.C_IDACTUACION};
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION, usr.getLocation());
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_ANIO, anio);
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_NUMERO, numero);
			htCosteABorrar.put(ScsActuacionAsistCosteFijoBean.C_IDACTUACION, idactuacion);
			
			//
			//2.- Preparamos el borrado de la actuacion:
			//
			ScsActuacionAsistenciaAdm admActuacionAsistencia = new ScsActuacionAsistenciaAdm(this.getUserBean(request));
			hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsActuacionAsistenciaBean.C_ANIO,anio);
			hash.put(ScsActuacionAsistenciaBean.C_NUMERO,numero);
			hash.put(ScsActuacionAsistenciaBean.C_IDACTUACION,idactuacion);
			ScsActuacionAsistCosteFijoAdm admActuacionAsistCosteFijo = new ScsActuacionAsistCosteFijoAdm(this.getUserBean(request));
			tx.begin();
			
			//Borramos los costes Fijos
			admActuacionAsistCosteFijo.deleteDirect(hash, clavesCosteABorrar);
			
			// RGG invalido la cabecera de guardia de la actuacion si no hay mas
			ScsCabeceraGuardiasAdm admCab = new ScsCabeceraGuardiasAdm(usr);
			admCab.actualizarValidacionCabecera(usr.getLocation(),anio,numero,false);
			admActuacionAsistencia.delete(hash);
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.updated.error",e,tx); 
		} 
		return exitoRefresco("messages.updated.success",request);
	}

}