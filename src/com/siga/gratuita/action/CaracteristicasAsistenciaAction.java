package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CaracteristicasAsistenciasAdm;
import com.siga.beans.CaracteristicasAsistenciasBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.CaracteristicasForm;
import com.siga.ws.CajgConfiguracion;

public class CaracteristicasAsistenciaAction extends MasterAction
{
	
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try 
		{ 
			miForm = (MasterForm) formulario;
			String accion = miForm.getModo();

			//La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
			{
				mapDestino = abrir(mapping, miForm, request, response);				
			}else if (accion.equalsIgnoreCase("guardar"))
			{
				mapDestino = guardar(mapping, miForm, request, response);			
			}else if ( accion.equalsIgnoreCase("getAjaxColegiado"))
			{			
				getAjaxColegiado(mapping, miForm, request, response);
				return null;
			}else if (accion.equalsIgnoreCase("getAjaxJuzgado"))
			{
			    getAjaxJuzgado(mapping, miForm, request, response);
			    return null;		    	
			}
			else 
			{
				return super.executeInternal(mapping,formulario,request,response);
			}			
		} catch (SIGAException es) 
		{
			throw es;
		} catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		return mapping.findForward(mapDestino);
	}	
	
	
	/**
	 * Metodo que implementa el modo abrir
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response)
			throws SIGAException
	{
		try 
		{					
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CaracteristicasForm miform = (CaracteristicasForm)formulario;
     	
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(user.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
     										
			// para la clave de Asistencia
			String idInstitucionAsistencia="", anioAsistencia="", numeroAsistencia="";
											
			// obtener Accion
			String accion = request.getParameter("accionE");
									     
			idInstitucionAsistencia=request.getParameter("idInstitucionASI");
			anioAsistencia=request.getParameter("anioASI");
			numeroAsistencia=request.getParameter("numeroASI");
		
			// busco si se ha actualizado el valor de idpersonaJG
			CaracteristicasAsistenciasAdm adm = new CaracteristicasAsistenciasAdm(this.getUserBean(request));
			Hashtable ht = new Hashtable();
			ht.put(CaracteristicasAsistenciasBean.C_IDINSTITUCION,idInstitucionAsistencia);
			ht.put(CaracteristicasAsistenciasBean.C_ANIO,anioAsistencia);
			ht.put(CaracteristicasAsistenciasBean.C_NUMERO,numeroAsistencia);
			Vector v = adm.selectByPK(ht);
			if (v!=null && v.size()>0) 
			{
				CaracteristicasAsistenciasBean b = (CaracteristicasAsistenciasBean) v.get(0);
			
				miform.setAccion(accion);
				miform.setAnio(b.getAnio().toString());
				miform.setAsesoramiento(b.getAsesoramiento());
				miform.setAsistenciaDeclaracion(b.getAsistenciaDeclaracion());
				miform.setCivil(b.getCivil());
				miform.setCivilesPenales(b.getCivilesPenales());
				miform.setContraLibertadSexual(b.getContraLibertadSexual());
				miform.setDerechosJusticiaGratuita(b.getDerechosJusticiaGratuita());
				miform.setDerivaActuacionesJudiciales(b.getDerivaActuacionesJudiciales());
				miform.setEntrevistaLetradoDemandante(b.getEntrevistaLetradoDemandante());
				miform.setIdInstitucion(b.getIdInstitucion().toString());
			
				miform.setDescripcionContacto(b.getDescripcionContacto());
				miform.setDescripcionJuzgado(b.getDescripcionJuzgado());
				miform.setDescripcionPretension(b.getDescripcionPretension());
						
				if(b.getIdInstitucionJuzgado()!=null)
					miform.setIdInstitucionJuzgado(b.getIdInstitucionJuzgado().toString());
				else
					miform.setIdInstitucionJuzgado("");
			
				if(b.getIdJuzgado()!=null)
					miform.setIdJuzgado(b.getIdJuzgado().toString());
				else
					miform.setIdJuzgado("");
			
				if(b.getIdOrigenContacto()!=null)
					miform.setIdOrigenContacto(b.getIdOrigenContacto().toString());
				else
					miform.setIdOrigenContacto("0");
						
				if(b.getIdPersona()!=null)				
					miform.setIdPersona(b.getIdPersona().toString());
				else
					miform.setIdPersona("");
			
				if(b.getIdPretension()!=null)
					miform.setIdPretension(b.getIdPretension().toString());
				else
					miform.setIdPretension("");
			
				miform.setInterposicionDenuncia(b.getInterposicionDenuncia());
				miform.setInterposicionMinistFiscal(b.getInterposicionMinistFiscal());
				miform.setIntervencionMedicoForense(b.getIntervencionMedicoForense());
				miform.setJudicial(b.getJudicial());
				miform.setLetradoDesignadoContiActuJudi(b.getLetradoDesignadoContiActuJudi());
				miform.setMalosTratos(b.getMalosTratos());
				miform.setMedidasProvisionales(b.getMedidasProvisionales());
				miform.setNig(b.getNig());
				miform.setNumero(b.getNumero().toString());
				miform.setNumeroProcedimiento(b.getNumeroProcedimiento());
				miform.setObligadaDesalojoDomicilio(b.getObligadaDesalojoDomicilio());
				miform.setOrdenProteccion(b.getOrdenProteccion());
				miform.setOtras(b.getOtras());
				miform.setOtrasDescripcion(b.getOtrasDescripcion());
				miform.setOtroDescripcionOrigenContacto(b.getOtroDescripcionOrigenContacto());
				miform.setPenal(b.getPenal());
				miform.setSolicitudMedidasCautelares(b.getSolicitudMedidasCautelares());
				miform.setVictimaLetradoAnterioridad(b.getVictimaLetradoAnterioridad());															
						
				String idTurno = request.getParameter("idTurno");
				if(idTurno==null)
					idTurno = "";
				List<ScsJuzgadoBean> alJuzgados = null;
				ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(user);
				
			
				alJuzgados = admJuzgados.getJuzgados((String)user.getLocation(),idTurno,user,true, false, "");
				if(alJuzgados==null)
				{
					alJuzgados = new ArrayList<ScsJuzgadoBean>();			
				}
				miform.setJuzgados(alJuzgados);
									
				request.setAttribute("CaracteristicasForm",miform);
			}
			else
			{
				miform.setAccion(accion);
				miform.setAnio(anioAsistencia);
				miform.setAsesoramiento("");
				miform.setAsistenciaDeclaracion("");
				miform.setCivil("");
				miform.setCivilesPenales("");
				miform.setContraLibertadSexual("");
				miform.setDerechosJusticiaGratuita("");
				miform.setDerivaActuacionesJudiciales("");
				miform.setEntrevistaLetradoDemandante("");
				miform.setIdInstitucion(idInstitucionAsistencia);
				miform.setIdInstitucionJuzgado("");
				miform.setIdJuzgado("");
				miform.setIdOrigenContacto("");
				miform.setIdPersona("");
				miform.setIdPretension("");
				miform.setInterposicionDenuncia("");
				miform.setInterposicionMinistFiscal("");
				miform.setIntervencionMedicoForense("");
				miform.setJudicial("");
				miform.setLetradoDesignadoContiActuJudi("");
				miform.setMalosTratos("");
				miform.setMedidasProvisionales("");
				miform.setNig("");
				miform.setNumero(numeroAsistencia);
				miform.setNumeroProcedimiento("");
				miform.setObligadaDesalojoDomicilio("");
				miform.setOrdenProteccion("");
				miform.setOtras("");
				miform.setOtrasDescripcion("");
				miform.setOtroDescripcionOrigenContacto("");
				miform.setPenal("");
				miform.setSolicitudMedidasCautelares("");
				miform.setVictimaLetradoAnterioridad("");				
				miform.setDescripcionContacto("");
				miform.setDescripcionJuzgado("");
				miform.setDescripcionPretension("");
							
				String idTurno = request.getParameter("idTurno");
				if(idTurno==null)
					idTurno = "";
				List<ScsJuzgadoBean> alJuzgados = null;
				ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(user);
						
				alJuzgados = admJuzgados.getJuzgados((String)user.getLocation(),idTurno,user,true, false, "");
				if(alJuzgados==null)
				{
					alJuzgados = new ArrayList<ScsJuzgadoBean>();			
				}
				miform.setJuzgados(alJuzgados);
			
				request.setAttribute("CaracteristicasForm",miform);
			}
		
		}catch (Exception e) 
		{
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
     	
		return "abrir";
	} //fin del método
	
	
	
	
	/**
	 * Metodo que implementa el guardado de una persona para luego crear la relacion pertinente
	 * con un asunto (ejg, soj,...)
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{				
		UserTransaction tx = null;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		CaracteristicasForm miform = (CaracteristicasForm)formulario;								
										
		try 
		{	     	
	     	// Datos Persona
			Hashtable caracteristica= new Hashtable();
			CaracteristicasAsistenciasAdm caractAdm = new CaracteristicasAsistenciasAdm(this.getUserBean(request));
			UtilidadesHash.set(caracteristica,CaracteristicasAsistenciasBean.C_IDINSTITUCION,user.getLocation());
									
			String descripcionContacto=request.getParameter("descripcionContactoHidden");
			String descripcionJuzgado=request.getParameter("descripcionJuzgadoHidden");
			String descripcionPretension=request.getParameter("descripcionPretensionHidden");
									
			//String idInstitucionAsistencia=request.getParameter("idInstitucionASI");
			//String anioAsistencia=request.getParameter("anioASI");
			//String numeroAsistencia=request.getParameter("numeroASI");
			// Comprobamos si tenemos que hacer un insert o un update
			Hashtable ht = new Hashtable();
			ht.put(CaracteristicasAsistenciasBean.C_IDINSTITUCION,miform.getIdInstitucion());
			ht.put(CaracteristicasAsistenciasBean.C_ANIO,miform.getAnio());
			ht.put(CaracteristicasAsistenciasBean.C_NUMERO,miform.getNumero());			
			Vector v = caractAdm.selectByPK(ht);
			
			boolean nuevo = false;
			CaracteristicasAsistenciasBean b = new CaracteristicasAsistenciasBean();
			
			//Si el vector viene con datos es que ls caracteristicas ya existen y hay que hacer update
			if (v!=null && v.size()>0) 
			{
				b = (CaracteristicasAsistenciasBean) v.get(0);																							
			}
			else
			{																
				nuevo = true;
			}
						
			// Se inicializa el objeto bean para hacer el update o el insert segun corresponda
			b.setAnio(new Integer(miform.getAnio()));
			b.setAsesoramiento(miform.getAsesoramiento());
			b.setAsistenciaDeclaracion(miform.getAsistenciaDeclaracion());
			b.setCivil(miform.getCivil());
			b.setCivilesPenales(miform.getCivilesPenales());
			b.setContraLibertadSexual(miform.getContraLibertadSexual());
			b.setDerechosJusticiaGratuita(miform.getDerechosJusticiaGratuita());
			b.setDerivaActuacionesJudiciales(miform.getDerivaActuacionesJudiciales());
			b.setEntrevistaLetradoDemandante(miform.getEntrevistaLetradoDemandante());
			
			b.setDescripcionContacto(descripcionContacto);
			b.setDescripcionJuzgado(descripcionJuzgado);
			b.setDescripcionPretension(descripcionPretension);
			
			if(miform.getIdInstitucion().equals(""))					
				b.setIdInstitucion(null);
			else
				b.setIdInstitucion(new Integer (miform.getIdInstitucion()));
			
			if(miform.getIdInstitucionJuzgado().equals(""))
				b.setIdInstitucionJuzgado(null);
			else
				b.setIdInstitucionJuzgado(new Integer(miform.getIdInstitucionJuzgado()));
			
			if(miform.getIdJuzgado().equals(""))					
				b.setIdJuzgado(null);
			else
				b.setIdJuzgado(new Long (miform.getIdJuzgado()));
			
			if(miform.getIdOrigenContacto().equals(""))
			{
				b.setIdOrigenContacto(0);
				b.setDescripcionContacto("");
				b.setOtroDescripcionOrigenContacto("");
			}
			else if(!miform.getIdOrigenContacto().equals("9"))
			{
				b.setIdOrigenContacto(new Integer (miform.getIdOrigenContacto()));
				b.setOtroDescripcionOrigenContacto("");
			}
			else // Es la opcion de Otros que lleva incorporada la caja de texto de Descripcion
			{
				b.setIdOrigenContacto(new Integer (miform.getIdOrigenContacto()));
				b.setOtroDescripcionOrigenContacto(miform.getOtroDescripcionOrigenContacto());
			}
			
			if(miform.getIdPersona().equals(""))
				b.setIdPersona(null);
			else
				b.setIdPersona(new Long (miform.getIdPersona()));
											
			if(miform.getIdPretension().equals(""))
				b.setIdPretension(null);
			else
				b.setIdPretension(new Integer(miform.getIdPretension()));
			
			b.setInterposicionDenuncia(miform.getInterposicionDenuncia());
			b.setInterposicionMinistFiscal(miform.getInterposicionMinistFiscal());
			b.setIntervencionMedicoForense(miform.getIntervencionMedicoForense());
			b.setJudicial(miform.getJudicial());
			b.setLetradoDesignadoContiActuJudi(miform.getLetradoDesignadoContiActuJudi());
			b.setMalosTratos(miform.getMalosTratos());
			b.setMedidasProvisionales(miform.getMedidasProvisionales());
			b.setNig(miform.getNig());
			b.setNumero(new Double (miform.getNumero()));
			b.setNumeroProcedimiento(miform.getNumeroProcedimiento());
			b.setObligadaDesalojoDomicilio(miform.getObligadaDesalojoDomicilio());
			b.setOrdenProteccion(miform.getOrdenProteccion());
			b.setOtras(miform.getOtras());
			b.setOtrasDescripcion(miform.getOtrasDescripcion());			
			b.setPenal(miform.getPenal());
			b.setSolicitudMedidasCautelares(miform.getSolicitudMedidasCautelares());
			b.setVictimaLetradoAnterioridad(miform.getVictimaLetradoAnterioridad());
							
			// Comienzo control de transacciones 
			
			tx = user.getTransaction();			
			tx.begin();
			
			if (nuevo)
			{
				// YA existia // Update Persona
				if (!caractAdm.insert(b)) 
				{
					throw new ClsExceptions("Error en update caracteristicas Asistencias. " + caractAdm.getError());
				}
			} 
			else 
			{
				// NO existia // Insert Persona
				if (!caractAdm.update(b)) 
				{
					throw new ClsExceptions("Error en insert caracteristicas Asistencias. " + caractAdm.getError());
				}
			}
		
			tx.commit();
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},new ClsExceptions(e, "Excepcion en Caracteristicas Asistencias."),tx);
		}
		
		// result envia la orden de exito al jsp y muestra una ventana con le mensaje de datos guardados correctamente.
		String result = this.exitoRefresco("messages.updated.success",request);		
		return result;
						
	}
	
	
	protected void getAjaxColegiado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
	{
		CaracteristicasForm miForm = (CaracteristicasForm) formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		Hashtable<String, Object> htCliente = null;
		String colegiadoNumero = miForm.getColegiadoNumero();
		UsrBean usrBean = this.getUserBean(request);
											
		if(colegiadoNumero!= null && !colegiadoNumero.equals(""))
		{
			CenClienteAdm admCli = new CenClienteAdm(usrBean);
			Vector<Hashtable<String, Object>> vClientes = admCli.getClientePorNColegiado(miForm.getIdInstitucion(),miForm.getColegiadoNumero());
			if(vClientes!=null &&vClientes.size()>0)
				htCliente = vClientes.get(0);
		}
		String colegiadoNombre = null;
		String idPersona = null;
		
		if(htCliente!=null)
		{
			colegiadoNombre = (String)htCliente.get("NOMCOLEGIADO");
			
			idPersona = (String)htCliente.get("IDPERSONA");
			
		}
		else
		{
			colegiadoNombre = "";
			idPersona = "";
			colegiadoNumero = "";			
		}

		List listaParametros = new ArrayList();
		listaParametros.add(idPersona);
		listaParametros.add(colegiadoNumero);
		listaParametros.add(colegiadoNombre);
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
	}
	
	
	@SuppressWarnings("unchecked")
	protected void getAjaxJuzgado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
	{
		//Sacamos las guardias si hay algo selccionado en el turno
		String codigoExt = request.getParameter("codigoExtJuzgado");		
		
		CaracteristicasForm miform = (CaracteristicasForm)formulario;
		ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
		String where = " where upper(idjuzgado) = upper ('"+codigoExt+"')" +
				       " and idinstitucion="+this.getUserBean(request).getLocation();
		Vector resultadoJuzgado = juzgadoAdm.select(where);
		String idJuzgado ="";
		if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
			ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			idJuzgado = juzgadoBean.getIdJuzgado().toString();
			miform.setIdJuzgado(idJuzgado);
		}
		
		List listaParametros = new ArrayList();
		listaParametros.add(idJuzgado);
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );		
	}

}
