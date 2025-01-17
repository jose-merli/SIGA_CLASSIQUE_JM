package com.siga.gratuita.adm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeBieninmueble;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeBienmueble;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeCargaeconomica;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeIngresos;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeIrpf20;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosBienesInmueblesService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosBienesMueblesService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosCargaEconomicaService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosIngresosService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosIrpf20Service;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgUnidadesFamiliaresService;
import org.redabogacia.sigaservices.app.vo.ScsDeBienInmuebleExtends;
import org.redabogacia.sigaservices.app.vo.ScsDeBienMuebleExtends;
import org.redabogacia.sigaservices.app.vo.ScsDeCargaEconomicaExtends;
import org.redabogacia.sigaservices.app.vo.ScsDeIngresosExtends;
import org.redabogacia.sigaservices.app.vo.ScsDeIrpf20Extends;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DatosEconomicosEJGForm;

import es.satec.businessManager.BusinessManager;

public class DatosEconomicosEJGAdm {
	
	public String verDatosEconomicos (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) {
		BusinessManager businessManager = BusinessManager.getInstance();				
		
		String anio = (String) request.getParameter("ANIO");
		String numero = (String) request.getParameter("NUMERO");
		
		Map<String, Object> parametrosMap = new HashMap<String, Object>();
		
		parametrosMap.put("idpersona",(String) request.getParameter("idPersonaJG"));
		parametrosMap.put("idinstitucion",(String) request.getParameter("idInstitucionJG"));
		parametrosMap.put("idtipoejg",(String) request.getParameter("IDTIPOEJG"));
		parametrosMap.put("anio", anio);
		parametrosMap.put("numero", numero);
		
		ScsEjgUnidadesFamiliaresService datosEconomicosService = (ScsEjgUnidadesFamiliaresService) businessManager.getService(ScsEjgUnidadesFamiliaresService.class);
		
		String nombre = datosEconomicosService.obtenerPersonaEJG(parametrosMap);
		if (nombre == null)
			nombre="";
		
		while (numero.length()<5) {
			numero = "0" + numero;
		}
		
		//String datoEJG = anio + "/" + numero + " - " + nombre;
		//request.setAttribute("datoEJG", datoEJG);
		
		request.setAttribute("modosDatosEconomicosEJG", formulario.getModos());		
		
		return "inicio";		
	}	

	public String listarDatosEconomicosIngresos (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE INGRESOS			
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", (String)request.getParameter("idtipoejg"));
			parametrosMap.put("anio", (String)request.getParameter("anio"));
			parametrosMap.put("numero", (String)request.getParameter("numero"));
													
			ScsEjgDatosEconomicosIngresosService datosEconomicosService = (ScsEjgDatosEconomicosIngresosService) businessManager.getService(ScsEjgDatosEconomicosIngresosService.class);					
			List<ScsDeIngresosExtends> listaIngresos = datosEconomicosService.obtenerListaIngresosTotal(parametrosMap);
			// FIN LISTA DE INGRESOS			
			
			
			// 2. OBTENER LISTAS AUXILIARES		
			List<ScsDeIngresosExtends> listaTiposIngresos = datosEconomicosService.obtenerListaTiposIngresosTotal(parametrosMap);
			List<ScsDeIngresosExtends> listaPeriodicidades = datosEconomicosService.obtenerListaPeriodicidadesTotal(parametrosMap);
			List<ScsDeIngresosExtends> listaPerceptores = datosEconomicosService.obtenerListaPerceptoresTotal(parametrosMap);
			// FIN LISTAS AUXILIARES
			
			String claseFila;
			if((listaIngresos.size()+2)%2==0)
		   		claseFila = "filaTablaPar";
		   	else
		   		claseFila = "filaTablaImpar";
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='22%'>";
			String[] tdsNuevas = new String[5];
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:200px' id='select_TiposIngresos_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTiposIngresos.size(); i++) {
				ScsDeIngresosExtends dato = (ScsDeIngresosExtends) listaTiposIngresos.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdtipoingreso()+"'>"+dato.getDescripcionTipoIngreso()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			
			trNuevas+="<td align='left' width='22%'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:200px' id='select_Periodicidades_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[1]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaPeriodicidades.size(); i++) {
				ScsDeIngresosExtends dato = (ScsDeIngresosExtends) listaPeriodicidades.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdperiodicidad()+"'>"+dato.getDescripcionPeriodicidad()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='35%'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:330px' id='select_Perceptores_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[2]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaPerceptores.size(); i++) {
				ScsDeIngresosExtends dato = (ScsDeIngresosExtends) listaPerceptores.get(i);
				tdsNuevas[2]+="<option value='"+dato.getIdpersona()+"'>"+dato.getPerceptor()+"</option>";
			}				
			tdsNuevas[2]+="</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			trNuevas+="<td align='right' width='16%'>";
			tdsNuevas[3]="<input type='text' id='input_Importe_1' class='boxNumber' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)' onkeypress='return soloNumerosDecimales(event)'/>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='center' width='5%'>";
			tdsNuevas[4]="<img src='/SIGA/html/imagenes/bborrar_off.gif' style='cursor:pointer;' title='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' alt='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' name='' border='0' "+ 
				" onclick='borrarFila(1)'>";
			trNuevas+=tdsNuevas[4]+"</td></tr>";
						
			request.setAttribute("LISTA_INGRESOS", listaIngresos);
			request.setAttribute("TR_NEW", trNuevas);
			request.setAttribute("TDS_NEW", tdsNuevas);
						
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}		

		return "ingresos";		
	}
	
	public String borrarDatosEconomicosIngresos (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosIngresosService datosEconomicosService = (ScsEjgDatosEconomicosIngresosService) businessManager.getService(ScsEjgDatosEconomicosIngresosService.class);
			
			ScsDeIngresos ingreso = new ScsDeIngresos();
			ingreso.setIddeingresos(new Long(formulario.getId()));				
			
			if (datosEconomicosService.delete(ingreso) < 1) {
				throw new SIGAException("Error al borrar un ingreso");
			}
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosIngresos(mapping, formulario, request, response);	
	}		
	
	public String guardarDatosEconomicosIngresos (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosIngresosService datosEconomicosService = (ScsEjgDatosEconomicosIngresosService) businessManager.getService(ScsEjgDatosEconomicosIngresosService.class);
			
			String listaIngresos = formulario.getId();			
			String[] arrayIngresos = listaIngresos.split("%%%");					
			ArrayList arrayScsIngresos = new ArrayList();
			
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");									
			
			for (int i=0; i<arrayIngresos.length; i++)  {
				String[] arrayDatosIngreso = arrayIngresos[i].split("---");
				
				ScsDeIngresos ingreso = new ScsDeIngresos();
				ingreso.setIdinstitucion(new Short(usuario.getLocation()));
				ingreso.setIdtipoejg(new Short((String)request.getParameter("idtipoejg")));
				ingreso.setAnio(new Short((String)request.getParameter("anio")));
				ingreso.setNumero(new Long((String)request.getParameter("numero")));
				ingreso.setFechamodificacion(Calendar.getInstance().getTime());
				ingreso.setUsumodificacion(new Integer(usuario.getUserName()));
				
				ingreso.setIdtipoingreso(new Integer(arrayDatosIngreso[0]));
				ingreso.setIdperiodicidad(new Integer(arrayDatosIngreso[1]));
				ingreso.setIdpersona(new Long(arrayDatosIngreso[2]));
				ingreso.setImporte(new BigDecimal(arrayDatosIngreso[3]));
				
				arrayScsIngresos.add(ingreso);
			}
			
			if (!datosEconomicosService.insertTotal(arrayScsIngresos)) {
				throw new SIGAException("Error al insertar los ingresos");
			}				
					
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosIngresos(mapping, formulario, request, response);	
	}		
	
	public String listarDatosEconomicosBienInmueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE BIENES INMUEBLES
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", (String)request.getParameter("idtipoejg"));
			parametrosMap.put("anio", (String)request.getParameter("anio"));
			parametrosMap.put("numero", (String)request.getParameter("numero"));		
													
			ScsEjgDatosEconomicosBienesInmueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesInmueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesInmueblesService.class);					
			List<ScsDeBienInmuebleExtends> listaBienesInmuebles = datosEconomicosService.obtenerListaBienesInmueblesTotal(parametrosMap);
			// FIN LISTA DE BIENES INMUEBLES			
			
			
			// 2. OBTENER LISTAS AUXILIARES		
			List<ScsDeBienInmuebleExtends> listaOrigenValoraciones = datosEconomicosService.obtenerListaOrigenValoracionesTotal(parametrosMap);
			List<ScsDeBienInmuebleExtends> listaTiposViviendas = datosEconomicosService.obtenerListaTiposViviendasTotal(parametrosMap);
			List<ScsDeBienInmuebleExtends> listaTiposInmuebles = datosEconomicosService.obtenerListaTiposInmueblesTotal(parametrosMap);
			List<ScsDeBienInmuebleExtends> listaTitulares = datosEconomicosService.obtenerListaTitularesTotal(parametrosMap);
			// FIN LISTAS AUXILIARES
			
			String claseFila;
			if((listaBienesInmuebles.size()+2)%2==0)
		   		claseFila = "filaTablaPar";
		   	else
		   		claseFila = "filaTablaImpar";
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='14%'>";
			String[] tdsNuevas = new String[6];
			String opcionSeleccionar = "<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:120px' id='select_OrigenValoraciones_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0] += opcionSeleccionar;
			for (int i=0; i<listaOrigenValoraciones.size(); i++) {
				ScsDeBienInmuebleExtends dato = (ScsDeBienInmuebleExtends) listaOrigenValoraciones.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdorigenvalbi()+"'>"+dato.getDescripcionorigenvaloracion()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			

			
			trNuevas+="<td align='left' width='27%'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:250px' id='select_TiposViviendas_1' value='' onChange='cambiaFila(1);CambiarTipoVivienda(1,this.value)'>";
			tdsNuevas[1] += opcionSeleccionar;
			for (int i=0; i<listaTiposViviendas.size(); i++) {
				ScsDeBienInmuebleExtends dato = (ScsDeBienInmuebleExtends) listaTiposViviendas.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdtipovivienda()+"'>"+dato.getDescripciontipovivienda()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='15%'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:130px' id='select_TiposInmuebles_1' value='' onChange='cambiaFila(1)'>"+opcionSeleccionar+"</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			Hashtable hashTipoInmuebles = new Hashtable();			
			for (int i=0; i<listaTiposInmuebles.size(); i++) {
				ScsDeBienInmuebleExtends dato = (ScsDeBienInmuebleExtends) listaTiposInmuebles.get(i);
				String idTipoVivienda = dato.getIdtipovivienda().toString();
				
				String opcionTipoInmueble ="<option value='"+dato.getIdtipoinmueble()+"'>"+dato.getDescripciontipoinmueble()+"</option>";
				
				if (hashTipoInmuebles.containsKey(idTipoVivienda)) {
					hashTipoInmuebles.put(idTipoVivienda,hashTipoInmuebles.get(idTipoVivienda)+opcionTipoInmueble);
				} else {
					hashTipoInmuebles.put(idTipoVivienda,opcionSeleccionar+opcionTipoInmueble);
				}				
			}					
			
			trNuevas+="<td align='left' width='23%'>";
			tdsNuevas[3]="<select class='boxCombo' style='width:210px' id='select_Titulares_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[3] += opcionSeleccionar;
			for (int i=0; i<listaTitulares.size(); i++) {
				ScsDeBienInmuebleExtends dato = (ScsDeBienInmuebleExtends) listaTitulares.get(i);
				tdsNuevas[3]+="<option value='"+dato.getIdpersona()+"'>"+dato.getTitular()+"</option>";
			}				
			tdsNuevas[3]+="</select>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='right' width='16%'>";
			tdsNuevas[4]="<input type='text' id='input_Valoracion_1' class='boxNumber' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)' onkeypress='return soloNumerosDecimales(event)'/>";
			trNuevas+=tdsNuevas[4]+"</td>";
			
			trNuevas+="<td align='center' width='5%'>";
			tdsNuevas[5]="<img src='/SIGA/html/imagenes/bborrar_off.gif' style='cursor:pointer;' title='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' alt='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' name='' border='0' "+ 
				" onclick='borrarFila(1)'>";
			trNuevas+=tdsNuevas[5]+"</td></tr>";
						
			request.setAttribute("LISTA_BIENES", listaBienesInmuebles);
			request.setAttribute("TR_NEW", trNuevas);
			request.setAttribute("TDS_NEW", tdsNuevas);
			request.setAttribute("HASH_TIPOS_INMUEBLES", hashTipoInmuebles);
						
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}		

		return "bieninmueble";		
	}
	
	public String borrarDatosEconomicosBienInmueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosBienesInmueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesInmueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesInmueblesService.class);
			
			ScsDeBieninmueble bienInmueble = new ScsDeBieninmueble();
			bienInmueble.setIdbieninmueble(new Long(formulario.getId()));				
			
			if (datosEconomicosService.delete(bienInmueble) < 1) {
				throw new SIGAException("Error al borrar un bien inmueble");
			}
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosBienInmueble(mapping, formulario, request, response);	
	}		
	
	public String guardarDatosEconomicosBienInmueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosBienesInmueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesInmueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesInmueblesService.class);
			
			String listaBienes = formulario.getId();			
			String[] arrayBienes = listaBienes.split("%%%");					
			ArrayList arrayListBienes = new ArrayList();
			
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");							
			
			for (int i=0; i<arrayBienes.length; i++)  {
				String[] arrayBien = arrayBienes[i].split("---");
				
				ScsDeBieninmueble bienInmueble = new ScsDeBieninmueble();
				bienInmueble.setIdinstitucion(new Short(usuario.getLocation()));
				bienInmueble.setIdtipoejg(new Short((String)request.getParameter("idtipoejg")));
				bienInmueble.setAnio(new Short((String)request.getParameter("anio")));
				bienInmueble.setNumero(new Long((String)request.getParameter("numero")));
				bienInmueble.setFechamodificacion(Calendar.getInstance().getTime());
				bienInmueble.setUsumodificacion(new Integer(usuario.getUserName()));
				
				bienInmueble.setIdorigenvalbi(new Integer(arrayBien[0]));
				bienInmueble.setIdtipovivienda(new Integer(arrayBien[1]));
				if (!arrayBien[2].equals("")) {
					bienInmueble.setIdtipoinmueble(new Integer(arrayBien[2]));
				}
				bienInmueble.setIdpersona(new Long(arrayBien[3]));
				bienInmueble.setValoracion(new BigDecimal(arrayBien[4]));
				
				arrayListBienes.add(bienInmueble);
			}
			
			if (!datosEconomicosService.insertTotal(arrayListBienes)) {
				throw new SIGAException("Error al insertar los bienes inmuebles");
			}				
					
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosBienInmueble(mapping, formulario, request, response);	
	}				
	
	public String listarDatosEconomicosBienMueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE BIENES MUEBLES
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", (String)request.getParameter("idtipoejg"));
			parametrosMap.put("anio", (String)request.getParameter("anio"));
			parametrosMap.put("numero", (String)request.getParameter("numero"));		
													
			ScsEjgDatosEconomicosBienesMueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesMueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesMueblesService.class);					
			List<ScsDeBienMuebleExtends> listaBienesMuebles = datosEconomicosService.obtenerListaBienesMueblesTotal(parametrosMap);
			// FIN LISTA DE BIENES MUEBLES			
			
			
			// 2. OBTENER LISTAS AUXILIARES		
			List<ScsDeBienMuebleExtends> listaOrigenValoraciones = datosEconomicosService.obtenerListaOrigenValoracionesTotal(parametrosMap);
			List<ScsDeBienMuebleExtends> listaTiposMuebles = datosEconomicosService.obtenerListaTiposMueblesTotal(parametrosMap);
			List<ScsDeBienMuebleExtends> listaTitulares = datosEconomicosService.obtenerListaTitularesTotal(parametrosMap);
			// FIN LISTAS AUXILIARES
			
			String claseFila;
			if((listaBienesMuebles.size()+2)%2==0)
		   		claseFila = "filaTablaPar";
		   	else
		   		claseFila = "filaTablaImpar";
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='22%'>";
			String[] tdsNuevas = new String[5];
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:200px' id='select_OrigenValoraciones_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaOrigenValoraciones.size(); i++) {
				ScsDeBienMuebleExtends dato = (ScsDeBienMuebleExtends) listaOrigenValoraciones.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdorigenvalbm()+"'>"+dato.getDescripcionorigenvaloracion()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			
			trNuevas+="<td align='left' width='22%'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:200px' id='select_TiposMuebles_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[1]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTiposMuebles.size(); i++) {
				ScsDeBienMuebleExtends dato = (ScsDeBienMuebleExtends) listaTiposMuebles.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdtipomueble()+"'>"+dato.getDescripciontipomueble()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='35%'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:330px' id='select_Titulares_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[2]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTitulares.size(); i++) {
				ScsDeBienMuebleExtends dato = (ScsDeBienMuebleExtends) listaTitulares.get(i);
				tdsNuevas[2]+="<option value='"+dato.getIdpersona()+"'>"+dato.getTitular()+"</option>";
			}				
			tdsNuevas[2]+="</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			trNuevas+="<td align='right' width='16%'>";
			tdsNuevas[3]="<input type='text' id='input_Valoracion_1' class='boxNumber' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)' onkeypress='return soloNumerosDecimales(event)'/>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='center' width='5%'>";
			tdsNuevas[4]="<img src='/SIGA/html/imagenes/bborrar_off.gif' style='cursor:pointer;' title='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' alt='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' name='' border='0' "+ 
				" onclick='borrarFila(1)'>";
			trNuevas+=tdsNuevas[4]+"</td></tr>";
						
			request.setAttribute("LISTA_BIENES", listaBienesMuebles);
			request.setAttribute("TR_NEW", trNuevas);
			request.setAttribute("TDS_NEW", tdsNuevas);
						
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}		

		return "bienmueble";		
	}
	
	public String borrarDatosEconomicosBienMueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosBienesMueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesMueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesMueblesService.class);
			
			ScsDeBienmueble bienMueble = new ScsDeBienmueble();
			bienMueble.setIdbienmueble(new Long(formulario.getId()));				
			
			if (datosEconomicosService.delete(bienMueble) < 1) {
				throw new SIGAException("Error al borrar un bien mueble");
			}
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosBienMueble(mapping, formulario, request, response);	
	}		
	
	public String guardarDatosEconomicosBienMueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosBienesMueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesMueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesMueblesService.class);
			
			String listaBienes = formulario.getId();			
			String[] arrayBienes = listaBienes.split("%%%");					
			ArrayList arrayListBienes = new ArrayList();
			
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");								
			
			for (int i=0; i<arrayBienes.length; i++)  {
				String[] arrayBien = arrayBienes[i].split("---");
				
				ScsDeBienmueble bienMueble = new ScsDeBienmueble();
				bienMueble.setIdinstitucion(new Short(usuario.getLocation()));
				bienMueble.setIdtipoejg(new Short((String)request.getParameter("idtipoejg")));
				bienMueble.setAnio(new Short((String)request.getParameter("anio")));
				bienMueble.setNumero(new Long((String)request.getParameter("numero")));
				bienMueble.setFechamodificacion(Calendar.getInstance().getTime());
				bienMueble.setUsumodificacion(new Integer(usuario.getUserName()));
				
				bienMueble.setIdorigenvalbm(new Integer(arrayBien[0]));
				bienMueble.setIdtipomueble(new Integer(arrayBien[1]));
				bienMueble.setIdpersona(new Long(arrayBien[2]));
				bienMueble.setValoracion(new BigDecimal(arrayBien[3]));
				
				arrayListBienes.add(bienMueble);
			}
			
			if (!datosEconomicosService.insertTotal(arrayListBienes)) {
				throw new SIGAException("Error al insertar los bienes muebles");
			}				
					
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosBienMueble(mapping, formulario, request, response);	
	}			
	
	public String listarDatosEconomicosCargaEconomica (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE CARGAS ECONOMICAS
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", (String)request.getParameter("idtipoejg"));
			parametrosMap.put("anio", (String)request.getParameter("anio"));
			parametrosMap.put("numero", (String)request.getParameter("numero"));	
													
			ScsEjgDatosEconomicosCargaEconomicaService datosEconomicosService = (ScsEjgDatosEconomicosCargaEconomicaService) businessManager.getService(ScsEjgDatosEconomicosCargaEconomicaService.class);
			List<ScsDeCargaEconomicaExtends> listaCargasEconomicas = datosEconomicosService.obtenerListaCargasEconomicasTotal(parametrosMap);
			// FIN LISTA DE CARGAS ECONOMICAS			
			
			
			// 2. OBTENER LISTAS AUXILIARES		
			List<ScsDeCargaEconomicaExtends> listaTiposCargasEconomicas = datosEconomicosService.obtenerListaTiposCargasEconomicasTotal(parametrosMap);
			List<ScsDeCargaEconomicaExtends> listaPeriodicidades = datosEconomicosService.obtenerListaPeriodicidadesTotal(parametrosMap);
			List<ScsDeCargaEconomicaExtends> listaTitulares = datosEconomicosService.obtenerListaTitularesTotal(parametrosMap);
			// FIN LISTAS AUXILIARES
			
			String claseFila;
			if((listaCargasEconomicas.size()+2)%2==0)
		   		claseFila = "filaTablaPar";
		   	else
		   		claseFila = "filaTablaImpar";
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='22%'>";
			String[] tdsNuevas = new String[5];
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:200px' id='select_TiposCargasEconomicas_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTiposCargasEconomicas.size(); i++) {
				ScsDeCargaEconomicaExtends dato = (ScsDeCargaEconomicaExtends) listaTiposCargasEconomicas.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdtipocargaeconomica()+"'>"+dato.getDescripciontipocargaeconomica()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			
			trNuevas+="<td align='left' width='22%'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:200px' id='select_Periodicidades_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[1]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaPeriodicidades.size(); i++) {
				ScsDeCargaEconomicaExtends dato = (ScsDeCargaEconomicaExtends) listaPeriodicidades.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdperiodicidad()+"'>"+dato.getDescripcionperiodicidad()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='35%'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:330px' id='select_Titulares_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[2]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTitulares.size(); i++) {
				ScsDeCargaEconomicaExtends dato = (ScsDeCargaEconomicaExtends) listaTitulares.get(i);
				tdsNuevas[2]+="<option value='"+dato.getIdpersona()+"'>"+dato.getTitular()+"</option>";
			}				
			tdsNuevas[2]+="</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			trNuevas+="<td align='right' width='16%'>";
			tdsNuevas[3]="<input type='text' id='input_Importe_1' class='boxNumber' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)' onkeypress='return soloNumerosDecimales(event)'/>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='center' width='5%'>";
			tdsNuevas[4]="<img src='/SIGA/html/imagenes/bborrar_off.gif' style='cursor:pointer;' title='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' alt='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' name='' border='0' "+ 
				" onclick='borrarFila(1)'>";
			trNuevas+=tdsNuevas[4]+"</td></tr>";
						
			request.setAttribute("LISTA_CARGASECONOMICAS", listaCargasEconomicas);
			request.setAttribute("TR_NEW", trNuevas);
			request.setAttribute("TDS_NEW", tdsNuevas);
						
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}		

		return "cargaeconomica";		
	}
	
	public String borrarDatosEconomicosCargaEconomica (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosCargaEconomicaService datosEconomicosService = (ScsEjgDatosEconomicosCargaEconomicaService) businessManager.getService(ScsEjgDatosEconomicosCargaEconomicaService.class);
			
			ScsDeCargaeconomica cargaeconomica = new ScsDeCargaeconomica();
			cargaeconomica.setIdcargaeconomica(new Long(formulario.getId()));				
			
			if (datosEconomicosService.delete(cargaeconomica) < 1) {
				throw new SIGAException("Error al borrar una carga econůmica");
			}
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosCargaEconomica(mapping, formulario, request, response);	
	}		
	
	public String guardarDatosEconomicosCargaEconomica (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosCargaEconomicaService datosEconomicosService = (ScsEjgDatosEconomicosCargaEconomicaService) businessManager.getService(ScsEjgDatosEconomicosCargaEconomicaService.class);
			
			String listaCargasEconomicas = formulario.getId();			
			String[] arrayCargasEconomicas = listaCargasEconomicas.split("%%%");					
			ArrayList arrayListaCargasEconomicas = new ArrayList();
			
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");							
			
			for (int i=0; i<arrayCargasEconomicas.length; i++)  {
				String[] arrayCargaEconomica = arrayCargasEconomicas[i].split("---");
				
				ScsDeCargaeconomica cargaeconomica = new ScsDeCargaeconomica();
				cargaeconomica.setIdinstitucion(new Short(usuario.getLocation()));
				cargaeconomica.setIdtipoejg(new Short((String)request.getParameter("idtipoejg")));
				cargaeconomica.setAnio(new Short((String)request.getParameter("anio")));
				cargaeconomica.setNumero(new Long((String)request.getParameter("numero")));
				cargaeconomica.setFechamodificacion(Calendar.getInstance().getTime());
				cargaeconomica.setUsumodificacion(new Integer(usuario.getUserName()));
				
				cargaeconomica.setIdtipocargaeconomica(new Integer(arrayCargaEconomica[0]));
				cargaeconomica.setIdperiodicidad(new Integer(arrayCargaEconomica[1]));
				cargaeconomica.setIdpersona(new Long(arrayCargaEconomica[2]));
				cargaeconomica.setImporte(new BigDecimal(arrayCargaEconomica[3]));
				
				arrayListaCargasEconomicas.add(cargaeconomica);
			}
			
			if (!datosEconomicosService.insertTotal(arrayListaCargasEconomicas)) {
				throw new SIGAException("Error al insertar las cargas econůmicas");
			}				
					
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosCargaEconomica(mapping, formulario, request, response);	
	}				
	
	public String listarDatosEconomicosIrpf20 (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE IRPF
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", (String)request.getParameter("idtipoejg"));
			parametrosMap.put("anio", (String)request.getParameter("anio"));
			parametrosMap.put("numero", (String)request.getParameter("numero"));		
													
			ScsEjgDatosEconomicosIrpf20Service datosEconomicosService = (ScsEjgDatosEconomicosIrpf20Service) businessManager.getService(ScsEjgDatosEconomicosIrpf20Service.class);					
			List<ScsDeIrpf20Extends> listaIrpfs = datosEconomicosService.obtenerListaIrpfsTotal(parametrosMap);
			// FIN LISTA DE IRPF			
			
			
			// 2. OBTENER LISTAS AUXILIARES		
			List<ScsDeIrpf20Extends> listaTiposRendimientos = datosEconomicosService.obtenerListaTiposRendimientosTotal(parametrosMap);
			List<ScsDeIrpf20Extends> listaEjercicios = datosEconomicosService.obtenerListaEjerciciosTotal(parametrosMap);
			List<ScsDeIrpf20Extends> listaTitulares = datosEconomicosService.obtenerListaTitularesTotal(parametrosMap);
			// FIN LISTAS AUXILIARES
			
			String claseFila;
			if((listaIrpfs.size()+2)%2==0)
		   		claseFila = "filaTablaPar";
		   	else
		   		claseFila = "filaTablaImpar";
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='27%'>";
			String[] tdsNuevas = new String[5];
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:250px' id='select_TiposRendimientos_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTiposRendimientos.size(); i++) {
				ScsDeIrpf20Extends dato = (ScsDeIrpf20Extends) listaTiposRendimientos.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdtiporendimiento()+"'>"+dato.getDescripciontiporendimiento()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			
			trNuevas+="<td align='left' width='17%'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:150px' id='select_Ejercicios_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[1]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaEjercicios.size(); i++) {
				ScsDeIrpf20Extends dato = (ScsDeIrpf20Extends) listaEjercicios.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdejercicio()+"'>"+dato.getDescripcionejercicio()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='35%'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:330px' id='select_Titulares_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[2]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTitulares.size(); i++) {
				ScsDeIrpf20Extends dato = (ScsDeIrpf20Extends) listaTitulares.get(i);
				tdsNuevas[2]+="<option value='"+dato.getIdpersona()+"'>"+dato.getTitular()+"</option>";
			}				
			tdsNuevas[2]+="</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			trNuevas+="<td align='right' width='16%'>";
			tdsNuevas[3]="<input type='text' id='input_Importe_1' class='boxNumber' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)' onkeypress='return soloNumerosDecimales(event)'/>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='center' width='5%'>";
			tdsNuevas[4]="<img src='/SIGA/html/imagenes/bborrar_off.gif' style='cursor:pointer;' title='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' alt='"+UtilidadesString.getMensajeIdioma(usuario,"general.boton.borrar")+"' name='' border='0' "+ 
				" onclick='borrarFila(1)'>";
			trNuevas+=tdsNuevas[4]+"</td></tr>";
						
			request.setAttribute("LISTA_IRPFS", listaIrpfs);
			request.setAttribute("TR_NEW", trNuevas);
			request.setAttribute("TDS_NEW", tdsNuevas);
						
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}		

		return "irpf20";		
	}
	
	public String borrarDatosEconomicosIrpf20 (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosIrpf20Service datosEconomicosService = (ScsEjgDatosEconomicosIrpf20Service) businessManager.getService(ScsEjgDatosEconomicosIrpf20Service.class);
			
			ScsDeIrpf20 irpf = new ScsDeIrpf20();
			irpf.setIdirpf20(new Long(formulario.getId()));				
			
			if (datosEconomicosService.delete(irpf) < 1) {
				throw new SIGAException("Error al borrar un irpf");
			}
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosIrpf20(mapping, formulario, request, response);	
	}		
	
	public String guardarDatosEconomicosIrpf20 (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosIrpf20Service datosEconomicosService = (ScsEjgDatosEconomicosIrpf20Service) businessManager.getService(ScsEjgDatosEconomicosIrpf20Service.class);
			
			String listaIrpf = formulario.getId();			
			String[] arrayIrpf = listaIrpf.split("%%%");					
			ArrayList arrayListIrpf = new ArrayList();
			
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");							
			
			for (int i=0; i<arrayIrpf.length; i++)  {
				String[] arrayDatosIrpf = arrayIrpf[i].split("---");
				
				ScsDeIrpf20 irpf = new ScsDeIrpf20();
				irpf.setIdinstitucion(new Short(usuario.getLocation()));
				irpf.setIdtipoejg(new Short((String)request.getParameter("idtipoejg")));
				irpf.setAnio(new Short((String)request.getParameter("anio")));
				irpf.setNumero(new Long((String)request.getParameter("numero")));
				irpf.setFechamodificacion(Calendar.getInstance().getTime());
				irpf.setUsumodificacion(new Integer(usuario.getUserName()));
				
				irpf.setIdtiporendimiento(new Integer(arrayDatosIrpf[0]));
				irpf.setIdejercicio(new Integer(arrayDatosIrpf[1]));
				irpf.setIdpersona(new Long(arrayDatosIrpf[2]));
				irpf.setImporte(new BigDecimal(arrayDatosIrpf[3]));
				
				arrayListIrpf.add(irpf);
			}
			
			if (!datosEconomicosService.insertTotal(arrayListIrpf)) {
				throw new SIGAException("Error al insertar los irpf");
			}				
					
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosIrpf20(mapping, formulario, request, response);	
	}			
}
