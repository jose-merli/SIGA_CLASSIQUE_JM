//VERSIONES:

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsEJGAdm.TipoVentana;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BusquedaPorTipoSJCSForm;
import com.siga.gratuita.form.DefinirEJGForm;

/**
* @author AtosOrigin 21-04-2006
*/
public class BusquedaPorTipoSJCSAction extends MasterAction 
{
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{		
		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion.equalsIgnoreCase("enviar")) {
						mapDestino = enviar(mapping, miForm, request, response);
					} 
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	protected String abrir (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String destino = "";
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			BusquedaPorTipoSJCSForm miFormulario = (BusquedaPorTipoSJCSForm)formulario;
			miFormulario.setIdInstitucion(user.getLocation());
			miFormulario.setTipo(miFormulario.getTipo());
			destino = "abrir";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			Vector resultado = null;
			
			BusquedaPorTipoSJCSForm miFormulario = (BusquedaPorTipoSJCSForm)formulario;
			do {
				if (miFormulario.getTipo() == null) {
					return "";
				}
				if (miFormulario.getTipo().equalsIgnoreCase("EJG")) {
					resultado = buscarEJG(miFormulario, request);
					break;
				}
				if (miFormulario.getTipo().equalsIgnoreCase("DESIGNA")) {
					resultado = buscarDesigna(miFormulario, request);
					break;
				}
				if (miFormulario.getTipo().equalsIgnoreCase("SOJ")) {
					resultado = buscarSOJ(miFormulario, request);
					break;
				}
				
			} while (false);

			miFormulario.setTipo(miFormulario.getTipo());

			if (resultado == null) 
				resultado = new Vector ();
			request.setAttribute("resultado",resultado);
			return "resultado";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return "";
	   	}
	}
			
	
	private Vector buscarEJG(BusquedaPorTipoSJCSForm formulario, HttpServletRequest request) throws SIGAException
	{
		try {
			// obtener institucion
			UsrBean user = this.getUserBean(request);
			
			Hashtable miHash= new Hashtable();
			
			miHash.put("IDINSTITUCION",user.getLocation());
			if (formulario.getAnio() != null && !formulario.getAnio().equals("")){
				miHash.put("ANIO",formulario.getAnio());
			}
			if (formulario.getNumero() != null && !formulario.getNumero().equals("")){
				miHash.put(ScsEJGBean.C_NUMEJG,formulario.getNumero());
			}	
			if (formulario.getIdTipoEJG() != null && !formulario.getIdTipoEJG().equals("")){
				miHash.put(ScsEJGBean.C_IDTIPOEJG,formulario.getIdTipoEJG());
			}
			if (formulario.getNif() != null && !formulario.getNif().equals("")){
				miHash.put("NIF",formulario.getNif());						 
			}
			if (formulario.getNombre() != null && !formulario.getNombre().equals("")){
    			miHash.put("NOMBRE",formulario.getNombre());
			}
			if (formulario.getApellido1() != null && !formulario.getApellido1().equals("")){
				miHash.put("APELLIDO1",formulario.getApellido1());
			}
			if (formulario.getApellido2() != null && !formulario.getApellido2().equals("")){
				miHash.put("APELLIDO2",formulario.getApellido2());
			}
			
			ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
            Hashtable htConsultaBind  = adm.getBindBusquedaMantenimientoEJG(miHash,  new DefinirEJGForm(), TipoVentana.BUSQUEDA_EJG, user.getLocation());
            String consulta = (String) htConsultaBind.get("keyBindConsulta");
            Hashtable codigos = (Hashtable) htConsultaBind.get("keyBindCodigos");
			
            Vector datos = adm.selectGenericoBind(consulta, codigos);
			actualizarDatosEJG(request, adm, datos);
            return datos;
			
	     } catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return null;
	   	 }
	}
	
	private Vector buscarSOJ(BusquedaPorTipoSJCSForm formulario, HttpServletRequest request) throws SIGAException
	{
		try {
			// obtener institucion
			UsrBean user = this.getUserBean(request);
			
			// casting del formulario
			formulario.setIdInstitucion(user.getLocation());

			String sql = " WHERE " + ScsSOJBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request);
			
			if (formulario.getAnio() != null && !formulario.getAnio().equals(""))
				sql += " AND " + ScsSOJBean.C_ANIO + " = " + formulario.getAnio();

			if (formulario.getNumero() != null && !formulario.getNumero().equals(""))
				sql += " AND " +  ComodinBusquedas.prepararSentenciaCompleta(formulario.getNumero(), ScsSOJBean.C_NUMSOJ);
				
			if (formulario.getIdTipoSOJ() != null && !formulario.getIdTipoSOJ().equals(""))
				sql += " AND " +  ScsSOJBean.C_IDTIPOSOJ + " = " + formulario.getIdTipoSOJ();
			
			if (formulario.getNif() != null && !formulario.getNif().equals("")){
				sql += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNif().trim(),"persona.nif");
			}	
			if (formulario.getNombre() != null && !formulario.getNombre().equals("")){
				sql += " and "+ ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombre().trim(),"persona.nombre");
			}	
			if (formulario.getApellido1() != null && !formulario.getApellido1().equals("")){
				sql += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),"persona.apellido1");
			}	
			if (formulario.getApellido2() != null && !formulario.getApellido2().equals("")){
				sql += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),"persona.apellido2");
			}				

			ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(this.getUserBean(request));
			return adm.select(sql);
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return null;
	   	 }
	}
	
	private Vector buscarDesigna(BusquedaPorTipoSJCSForm formulario, HttpServletRequest request) throws SIGAException
	{
		try {
			// obtener institucion
			UsrBean user = this.getUserBean(request);			
			Hashtable miHash= new Hashtable();
			
			miHash.put("IDINSTITUCION",user.getLocation());
			if (formulario.getAnio() != null && !formulario.getAnio().equals("")){
				miHash.put("ANIO",formulario.getAnio());
			}
			if (formulario.getNumero() != null && !formulario.getNumero().equals("")){
				miHash.put(ScsDesignaBean.C_CODIGO,formulario.getNumero());
			}	
			if (formulario.getTurnoDesigna() != null && !formulario.getTurnoDesigna().equals("")) {
				String turno = formulario.getTurnoDesigna();
				String a[] = turno.split(",");
				miHash.put(ScsDesignaBean.C_IDTURNO,a[1]);
			}
			if (formulario.getNif() != null && !formulario.getNif().equals("")){
				miHash.put("NIF",formulario.getNif());						 
			}
			if (formulario.getNombre() != null && !formulario.getNombre().equals("")){
    			miHash.put("NOMBRE",formulario.getNombre());
			}
			if (formulario.getApellido1() != null && !formulario.getApellido1().equals("")){
				miHash.put("APELLIDO1",formulario.getApellido1());
			}
			if (formulario.getApellido2() != null && !formulario.getApellido2().equals("")){
				miHash.put("APELLIDO2",formulario.getApellido2());		
			}
			
			ScsDesignaAdm adm = new ScsDesignaAdm(this.getUserBean(request));
			Vector datos = adm.getBusquedaDesignaRelacionada(user.getLocation(), miHash);
			actualizarDatosDesigna(request, adm, datos);
			
			return datos;
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return null;
	   	 }
	}

	protected String enviar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			Hashtable claves = new Hashtable();
			
			BusquedaPorTipoSJCSForm miForm = (BusquedaPorTipoSJCSForm)formulario;

			Vector vOcultos      = miForm.getDatosTablaOcultos(0);
			String idInstitucion = miForm.getIdInstitucion();
			
			do {
				if (miForm.getTipo() == null) {
					return "";
				}
				if (miForm.getTipo().equalsIgnoreCase("EJG")) {
					UtilidadesHash.set(claves, ScsEJGBean.C_ANIO, (String)vOcultos.get(0));
					UtilidadesHash.set(claves, ScsEJGBean.C_NUMERO, (String)vOcultos.get(1));
					UtilidadesHash.set(claves, ScsEJGBean.C_IDTIPOEJG, (String)vOcultos.get(2));
					UtilidadesHash.set(claves, ScsEJGBean.C_IDINSTITUCION, idInstitucion);
					ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
					Vector v = adm.selectByPK(claves);
					
					if (v != null && v.size() > 0) {
						ScsEJGBean bean = (ScsEJGBean) v.get(0);
						Vector elemento = new Vector();
						elemento.add ("" + bean.getIdInstitucion());
						elemento.add ("" + bean.getAnio());
						elemento.add ("" + bean.getNumero());
						elemento.add ("" + bean.getIdTipoEJG());
						elemento.add ("" + bean.getNumEJG());
						request.setAttribute("elementoSeleccionado", elemento);		
					}					
					break;
				}
				
				if (miForm.getTipo().equalsIgnoreCase("DESIGNA")) {
					UtilidadesHash.set(claves, ScsDesignaBean.C_ANIO, (String)vOcultos.get(0));
					UtilidadesHash.set(claves, ScsDesignaBean.C_NUMERO, (String)vOcultos.get(1));
					UtilidadesHash.set(claves, ScsDesignaBean.C_IDTURNO, (String)vOcultos.get(2));
					UtilidadesHash.set(claves, ScsDesignaBean.C_IDINSTITUCION, idInstitucion);
					ScsDesignaAdm adm = new ScsDesignaAdm(this.getUserBean(request));
					Vector v = adm.selectByPK(claves);
					
					if (v != null && v.size() > 0) {
						ScsDesignaBean bean = (ScsDesignaBean) v.get(0);
						Vector elemento = new Vector();
						elemento.add ("" + bean.getIdInstitucion());
						elemento.add ("" + bean.getAnio());
						elemento.add ("" + bean.getNumero());
						elemento.add ("" + bean.getIdTurno());
						elemento.add ("" + bean.getCodigo());
						request.setAttribute("elementoSeleccionado", elemento);		
					}					
					break;
				}
				if (miForm.getTipo().equalsIgnoreCase("SOJ")) {
					UtilidadesHash.set(claves, ScsSOJBean.C_ANIO, (String)vOcultos.get(0));
					UtilidadesHash.set(claves, ScsSOJBean.C_NUMERO, (String)vOcultos.get(1));
					UtilidadesHash.set(claves, ScsSOJBean.C_IDTIPOSOJ, (String)vOcultos.get(2));
					UtilidadesHash.set(claves, ScsSOJBean.C_IDINSTITUCION, idInstitucion);
					ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(this.getUserBean(request));
					Vector v = adm.selectByPK(claves);
					
					if (v != null && v.size() > 0) {
						ScsSOJBean bean = (ScsSOJBean) v.get(0);
						Vector elemento = new Vector();
						elemento.add ("" + bean.getIdInstitucion());
						elemento.add ("" + bean.getAnio());
						elemento.add ("" + bean.getNumero());
						elemento.add ("" + bean.getIdTipoSOJ());
						request.setAttribute("elementoSeleccionado", elemento);		
					}					
					break;
				}
			} 
			while (false);			

			miForm.setTipo(miForm.getTipo());
			
			destino="seleccion";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}

	/**
	 * Actualizamos el paginador con los datos adicionales para que no se haga desde la jsp que queda feo y cutre 
	 * @throws SIGAException 
	 */
	private Vector actualizarDatosDesigna(HttpServletRequest request,ScsDesignaAdm desigAdm,Vector datos) throws ClsExceptions, SIGAException{
		String turnoDesig="";
		String actNoValida="";
		String defendidos="";
		String letradoDesig="";
		String IDletradoDesig="";
		String fechaEntrada="";
		String nColegiado="";
		String colegiadoOri = "";
		Row fila;
		// Obtenemos las relaciones de la EJG con designaciones y asistencias para mostrarlas en la busqueda
		try{

			for (int recordNumber = 1,contadorFila=1; recordNumber-1 < datos.size(); recordNumber++){	
			
				Hashtable registro = (Hashtable) datos.get(recordNumber-1);

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
				Hashtable letradoHashtable = desigAdm.obtenerLetradoDesigna(idInstitucion,  desIdTurno,  desAnio,  desNumero);
				String instiOrigen = UtilidadesHash.getString(letradoHashtable, "IDINSTITUCIONORIGEN");
				if(instiOrigen!=null && !instiOrigen.trim().equals("")){
					
					nColegiado = UtilidadesHash.getString(letradoHashtable, "NCOLEGIADOORIGEN")+"("+CenVisibilidad.getAbreviaturaInstitucion(instiOrigen)+")";
				}else 
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
	
	/**
	 * Actualiza el paginador con los datos del letrado relacionado con la designacion o asistencia 
	 * @throws SIGAException 
	 */
	private Vector actualizarDatosEJG(HttpServletRequest request,ScsEJGAdm admBean,Vector datos) throws ClsExceptions, SIGAException{
		String letradoDes = "";
		String turnoDes = "";
		// Obtenemos las relaciones de la EJG con designaciones y asistencias para mostrarlas en la busqueda
		try{
			for (int i=0; i<datos.size(); i++){
				Hashtable registro = (Hashtable) datos.get(i);
				String anio = registro.get("ANIO").toString();
				String idInstitucion = registro.get("IDINSTITUCION").toString();
				String idTipo = registro.get("IDTIPOEJG").toString();
				String numero = registro.get("NUMERO").toString();
				registro.put(ScsEJGBean.C_FECHAAPERTURA, admBean.getFechaAperturaEJG(idInstitucion, idTipo, anio, numero));
				// Obtenemos un vector con las relaciones del EJG
				Vector relacionados = new Vector();
				relacionados = admBean.getRelacionadoCon(idInstitucion, anio, numero, idTipo);
				letradoDes = "";
				turnoDes = "";
				if (relacionados.size()>0){
					// Nos recorremos las relaciones buscando informacion
					for (int r=0; r<relacionados.size(); r++){
						Hashtable elementoRel = new Hashtable();
						elementoRel = (Hashtable)relacionados.get(r);
						String tipoSJCS = UtilidadesHash.getString(elementoRel, "SJCS");
						if (tipoSJCS.equalsIgnoreCase("DESIGNA")){
							// Si se trata de una designa sacamos el idTurno y el año de la designa para poder 
							// recuperar el nombre del letrado
							String anioDes = UtilidadesHash.getString(elementoRel, "ANIO");
							ScsDesignaAdm desAdm = new ScsDesignaAdm(this.getUserBean(request));
							String idTurno = UtilidadesHash.getString(elementoRel, "IDTURNO");
							String numDes = UtilidadesHash.getString(elementoRel, "NUMERO");
							letradoDes = desAdm.getApeNomDesig(idInstitucion, idTurno, anioDes, numDes);
							turnoDes = UtilidadesHash.getString(elementoRel, "DES_TURNO");
						}else if (tipoSJCS.equalsIgnoreCase("ASISTENCIA")){
							// Damos preferencia a las designas. Solo usaremos las asistencias o SOJ cuando
							// No tengamos datos de la designa. Si luego aparecen sobreescribiran estos.
							String idLetrado = UtilidadesHash.getString(elementoRel, "IDLETRADO");
							if (idLetrado != null){
								Vector vPersona = new Vector();
								CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
								// Si se trata de una asistencia o SOJ buscamos al letrado en el censo
								vPersona = perAdm.getDatosPersonaTag(idInstitucion, idLetrado);
								if (vPersona.size()>0){
									turnoDes = UtilidadesHash.getString(elementoRel, "DES_TURNO");
									letradoDes = UtilidadesHash.getString((Hashtable)vPersona.get(0),"NOMBRE");
								}
							}
						}
					}
				}

				// Finalmente escribimos los datos en el paginador (si no ha encontrado nada que quede vacio)
				if((letradoDes.equals(", "))||(letradoDes.equals(""))){
					letradoDes="-";
				}
				if((turnoDes.equals(", "))||(turnoDes.equals(""))){
					turnoDes="-";
				}
				registro.put("LETRADODESIGNA", letradoDes);
				registro.put("TURNODESIGNA", turnoDes);

			} // JBD INC-CAT-5
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return datos;

	}	
}
