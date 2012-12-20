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
import org.redabogacia.sigaservices.app.autogen.model.ScsDeBieninmuebleExample;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeBienmueble;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeBienmuebleExample;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeCargaeconomica;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeCargaeconomicaExample;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeIngresos;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeIrpf20;
import org.redabogacia.sigaservices.app.autogen.model.ScsDeIrpf20Example;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosBienesInmueblesService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosBienesMueblesService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosCargaEconomicaService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosIngresosService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgDatosEconomicosIrpf20Service;
import org.redabogacia.sigaservices.app.vo.ScsDeIngresosExtends;
import org.redabogacia.sigaservices.app.vo.ScsDeIrpf20Extends;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DatosEconomicosEJGForm;

import es.satec.businessManager.BusinessManager;

public class DatosEconomicosEJGAdm {
	
	public String verDatosEconomicos (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("modosDatosEconomicosEJG", formulario.getModos());
		return "inicio";		
	}	

	public String listarDatosEconomicosIngresos (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE INGRESOS
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", UtilidadesHash.getShort(hash,"IDTIPOEJG"));
			parametrosMap.put("anio", UtilidadesHash.getShort(hash,"ANIO"));
			parametrosMap.put("numero", UtilidadesHash.getLong(hash,"NUMERO"));			
													
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
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='200px'>";
			String[] tdsNuevas = new String[5];
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:190px' id='select_TiposIngresos_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTiposIngresos.size(); i++) {
				ScsDeIngresosExtends dato = (ScsDeIngresosExtends) listaTiposIngresos.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdtipoingreso()+"'>"+dato.getDescripcionTipoIngreso()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			
			trNuevas+="<td align='left' width='150px'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:140px' id='select_Periodicidades_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[1]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaPeriodicidades.size(); i++) {
				ScsDeIngresosExtends dato = (ScsDeIngresosExtends) listaPeriodicidades.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdperiodicidad()+"'>"+dato.getDescripcionPeriodicidad()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='250px'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:240px' id='select_Perceptores_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[2]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaPerceptores.size(); i++) {
				ScsDeIngresosExtends dato = (ScsDeIngresosExtends) listaPerceptores.get(i);
				tdsNuevas[2]+="<option value='"+dato.getIdpersona()+"'>"+dato.getPerceptor()+"</option>";
			}				
			tdsNuevas[2]+="</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			trNuevas+="<td align='right' width='150px'>";
			tdsNuevas[3]="<input type='text' id='input_Importe_1' class='box' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)'/>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='center' width='50px'>";
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
			Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP");						
			
			for (int i=0; i<arrayIngresos.length; i++)  {
				String[] arrayDatosIngreso = arrayIngresos[i].split("---");
				
				ScsDeIngresos ingreso = new ScsDeIngresos();
				ingreso.setIdinstitucion(new Short(usuario.getLocation()));
				ingreso.setIdtipoejg(UtilidadesHash.getShort(hash,"IDTIPOEJG"));
				ingreso.setAnio(UtilidadesHash.getShort(hash,"ANIO"));
				ingreso.setNumero(UtilidadesHash.getLong(hash,"NUMERO"));
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
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosBienesInmueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesInmueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesInmueblesService.class);
			
			ScsDeBieninmueble bi = new ScsDeBieninmueble();
			ScsDeBieninmuebleExample example = new ScsDeBieninmuebleExample();
			org.redabogacia.sigaservices.app.autogen.model.ScsDeBieninmuebleExample.Criteria criteria = example.createCriteria();
			criteria.andIdtipoejgEqualTo(bi.getIdtipoejg());
			criteria.andIdinstitucionEqualTo(bi.getIdinstitucion());
			criteria.andAnioEqualTo(bi.getAnio());
			criteria.andNumeroEqualTo(bi.getNumero());
			
			List<ScsDeBieninmueble> listaBienesInmuebles = datosEconomicosService.obtenerListaBienesInmuebles(example);
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return "bieninmueble";		
	}	
	
	public String listarDatosEconomicosBienMueble (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosBienesMueblesService datosEconomicosService = (ScsEjgDatosEconomicosBienesMueblesService) businessManager.getService(ScsEjgDatosEconomicosBienesMueblesService.class);
			
			ScsDeBienmueble bm = new ScsDeBienmueble();
			ScsDeBienmuebleExample example = new ScsDeBienmuebleExample();
			org.redabogacia.sigaservices.app.autogen.model.ScsDeBienmuebleExample.Criteria criteria = example.createCriteria();
			criteria.andIdtipoejgEqualTo(bm.getIdtipoejg());
			criteria.andIdinstitucionEqualTo(bm.getIdinstitucion());
			criteria.andAnioEqualTo(bm.getAnio());
			criteria.andNumeroEqualTo(bm.getNumero());
			
			List<ScsDeBienmueble> listaBienesInmuebles = datosEconomicosService.obtenerListaBienesMuebles(example);
		
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
			
		return "bienmueble";		
	}	
	
	public String listarDatosEconomicosCargaEconomica (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosCargaEconomicaService datosEconomicosService = (ScsEjgDatosEconomicosCargaEconomicaService) businessManager.getService(ScsEjgDatosEconomicosCargaEconomicaService.class);
			
			ScsDeCargaeconomica ce = new ScsDeCargaeconomica();
			ScsDeCargaeconomicaExample example = new ScsDeCargaeconomicaExample();
			org.redabogacia.sigaservices.app.autogen.model.ScsDeCargaeconomicaExample.Criteria criteria = example.createCriteria();
			criteria.andIdtipoejgEqualTo(ce.getIdtipoejg());
			criteria.andIdinstitucionEqualTo(ce.getIdinstitucion());
			criteria.andAnioEqualTo(ce.getAnio());
			criteria.andNumeroEqualTo(ce.getNumero());
			
			List<ScsDeCargaeconomica> listaCargasEconomicas = datosEconomicosService.obtenerListaCargasEconomicas(example);
			
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return "cargaeconomica";		
	}	
	
	public String listarDatosEconomicosIrpf202 (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();		
			ScsEjgDatosEconomicosIrpf20Service datosEconomicosService = (ScsEjgDatosEconomicosIrpf20Service) businessManager.getService(ScsEjgDatosEconomicosIrpf20Service.class);
			
			ScsDeIrpf20 irpf = new ScsDeIrpf20();
			ScsDeIrpf20Example example = new ScsDeIrpf20Example();
			org.redabogacia.sigaservices.app.autogen.model.ScsDeIrpf20Example.Criteria criteria = example.createCriteria();
			criteria.andIdtipoejgEqualTo(irpf.getIdtipoejg());
			criteria.andIdinstitucionEqualTo(irpf.getIdinstitucion());
			criteria.andAnioEqualTo(irpf.getAnio());
			criteria.andNumeroEqualTo(irpf.getNumero());
			
			List<ScsDeIrpf20> listaIrpfs = datosEconomicosService.obtenerListaIrpfs(example);
			
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
			
		return "irpf20";		
	}		
	
	

	
	
	public String listarDatosEconomicosIrpf20 (ActionMapping mapping, DatosEconomicosEJGForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {		
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			BusinessManager businessManager = BusinessManager.getInstance();
			
			String idInstitucion = usuario.getLocation();
			Short shortIdInstitucion = new Short(idInstitucion);
			String idioma = usuario.getLanguage();
			
			// 1. LISTA DE IRPF
			parametrosMap.put("idioma", idioma);
			parametrosMap.put("idinstitucion", shortIdInstitucion);
			parametrosMap.put("idtipoejg", UtilidadesHash.getShort(hash,"IDTIPOEJG"));
			parametrosMap.put("anio", UtilidadesHash.getShort(hash,"ANIO"));
			parametrosMap.put("numero", UtilidadesHash.getLong(hash,"NUMERO"));			
													
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
			
			String trNuevas = "<tr class='"+claseFila+"' id='fila_1'><td align='left' width='250px'>";
			String[] tdsNuevas = new String[5];
					
			tdsNuevas[0] = "<select class='boxCombo' style='width:240px' id='select_TiposRendimientos_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[0]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTiposRendimientos.size(); i++) {
				ScsDeIrpf20Extends dato = (ScsDeIrpf20Extends) listaTiposRendimientos.get(i);
				tdsNuevas[0]+="<option value='"+dato.getIdtiporendimiento()+"'>"+dato.getDescripciontiporendimiento()+"</option>";
			}				
			tdsNuevas[0]+="</select>";
			trNuevas+=tdsNuevas[0]+"</td>";
			
			trNuevas+="<td align='left' width='150px'>";
			tdsNuevas[1]="<select class='boxCombo' style='width:140px' id='select_Ejercicios_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[1]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaEjercicios.size(); i++) {
				ScsDeIrpf20Extends dato = (ScsDeIrpf20Extends) listaEjercicios.get(i);
				tdsNuevas[1]+="<option value='"+dato.getIdejercicio()+"'>"+dato.getDescripcionejercicio()+"</option>";
			}				
			tdsNuevas[1]+="</select>";
			trNuevas+=tdsNuevas[1]+"</td>";
			
			trNuevas+="<td align='left' width='250px'>";
			tdsNuevas[2]="<select class='boxCombo' style='width:240px' id='select_Titulares_1' value='' onChange='cambiaFila(1)'>";
			tdsNuevas[2]+="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			for (int i=0; i<listaTitulares.size(); i++) {
				ScsDeIrpf20Extends dato = (ScsDeIrpf20Extends) listaTitulares.get(i);
				tdsNuevas[2]+="<option value='"+dato.getIdpersona()+"'>"+dato.getTitular()+"</option>";
			}				
			tdsNuevas[2]+="</select>";
			trNuevas+=tdsNuevas[2]+"</td>";
			
			trNuevas+="<td align='right' width='150px'>";
			tdsNuevas[3]="<input type='text' id='input_Importe_1' class='box' style='width:140' value='' maxlength='13' onChange='cambiaFila(1)'/>";
			trNuevas+=tdsNuevas[3]+"</td>";
			
			trNuevas+="<td align='center' width='50px'>";
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
			ArrayList arrayScsIrpf = new ArrayList();
			
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP");						
			
			for (int i=0; i<arrayIrpf.length; i++)  {
				String[] arrayDatosIrpf = arrayIrpf[i].split("---");
				
				ScsDeIrpf20 irpf = new ScsDeIrpf20();
				irpf.setIdinstitucion(new Short(usuario.getLocation()));
				irpf.setIdtipoejg(UtilidadesHash.getShort(hash,"IDTIPOEJG"));
				irpf.setAnio(UtilidadesHash.getShort(hash,"ANIO"));
				irpf.setNumero(UtilidadesHash.getLong(hash,"NUMERO"));
				irpf.setFechamodificacion(Calendar.getInstance().getTime());
				irpf.setUsumodificacion(new Integer(usuario.getUserName()));
				
				irpf.setIdtiporendimiento(new Integer(arrayDatosIrpf[0]));
				irpf.setIdejercicio(new Integer(arrayDatosIrpf[1]));
				irpf.setIdpersona(new Long(arrayDatosIrpf[2]));
				irpf.setImporte(new BigDecimal(arrayDatosIrpf[3]));
				
				arrayScsIrpf.add(irpf);
			}
			
			if (!datosEconomicosService.insertTotal(arrayScsIrpf)) {
				throw new SIGAException("Error al insertar los irpf");
			}				
					
		}catch (Exception exc){
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}	
		
		return this.listarDatosEconomicosIrpf20(mapping, formulario, request, response);	
	}			
}
