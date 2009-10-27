<!-- volantesExpres.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>


<%
	//System.out.println("Entramos en la pagina");
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Boolean isDelitosVE = (Boolean) request.getAttribute("isDelitosVE");

	String[] dato = { usr.getLocation() };
	String[] datoTurno = null;
	String[] datoGuardia = { "1", "1", usr.getLocation(), "", "" };
	String[] datoSustituto = { usr.getLocation(), "", "" };
	String[] colegiadosGuardia = { usr.getLocation(), "", "" };
	String comboBD = "turnos";
	String bJuzgado = "";
	boolean primeraVisita = false;
	String tipoAsistenciaDefecto = (String) request
			.getAttribute("tipoAsistenciaDefecto");

	String modoAnterior = (request.getAttribute("modoAnterior") == null ? ""
			: (String) request.getAttribute("modoAnterior"));
	String diaGuardia = (request.getAttribute("diaGuardia") == null ? ""
			: (String) request.getAttribute("diaGuardia"));
	String idLetrado = (request.getAttribute("idpersona") == null ? ""
			: (String) request.getAttribute("idpersona"));
	String numLetrado = (request.getAttribute("ncolegiado") == null ? ""
			: (String) request.getAttribute("ncolegiado"));
	String nombreLetrado = (request.getAttribute("nombreletrado") == null ? ""
			: (String) request.getAttribute("nombreletrado"));
	String centroJuzgado = (request.getAttribute("centroJuzgado") == null ? ""
			: (String) request.getAttribute("centroJuzgado"));
	String idGuardia = (request.getAttribute("idGuardia") == null ? ""
			: (String) request.getAttribute("idGuardia"));
	String idGuardiaSelec = (request.getAttribute("idGuardiaSelec") == null ? ""
			: (String) request.getAttribute("idGuardiaSelec"));
	String idTurno = (request.getAttribute("idTurno") == null ? ""
			: (String) request.getAttribute("idTurno"));
	String tipoAsistencia = (request.getAttribute("tipoAsistencia") == null ? ""
			: (String) request.getAttribute("tipoAsistencia"));
	String tipoAsistenciaColegio = (request
			.getAttribute("tipoAsistenciaColegio") == null ? ""
			: (String) request.getAttribute("tipoAsistenciaColegio"));
	String sustitutoDe = (request.getAttribute("sustituto") == null ? ""
			: (String) request.getAttribute("sustituto"));
	String guardiaDelSustituto = (request
			.getAttribute("guardiaDelSustituto") == null ? ""
			: (String) request.getAttribute("guardiaDelSustituto"));
	String aviso = (request.getAttribute("aviso") == null ? ""
			: (String) request.getAttribute("aviso"));
	boolean desdeNuevo = (request.getAttribute("desdeNuevo") == null ? false
			: UtilidadesString.stringToBoolean((String) request
					.getAttribute("desdeNuevo")));
	boolean sinAvisos = (request.getAttribute("sinAvisos") == null ? false
			: UtilidadesString.stringToBoolean((String) request
					.getAttribute("sinAvisos")));

	String fechaJustificacion = (String) request
			.getAttribute("fechaJustificacion");
	if (fechaJustificacion == null || fechaJustificacion.equals(""))
		fechaJustificacion = UtilidadesBDAdm.getFechaBD("");
	ArrayList turnoSel = new ArrayList();
	ArrayList guardiaSel = new ArrayList();
	ArrayList colegioSel = new ArrayList();
	ArrayList tipoAsistenciaSel = new ArrayList();
	ArrayList tipoAsistenciaColegioSel = new ArrayList();

	// Si se ha refrescado buscando datos del colegiado 
	if (diaGuardia != null && !diaGuardia.equals("")
			&& (idLetrado == null || idLetrado.equals(""))) {
		//System.out.println("CON dia GUARDIA SIN LETRADO");
		comboBD = "turnosVolSinLetrado";
		datoTurno = new String[3];
		datoTurno[0] = usr.getLocation();
		datoTurno[1] = diaGuardia;
		datoGuardia[1] = "1"; // Para que busque cualquier guardia independiente del letrado y fecha
		datoGuardia[0] = "1"; // Para que no filtre por guardias de sustitucion
		datoGuardia[3] = diaGuardia;

		datoSustituto[1] = diaGuardia;
		datoSustituto[2] = idLetrado;

		colegiadosGuardia[1] = diaGuardia;

	} else if (diaGuardia != null && !diaGuardia.equals("")
			&& idLetrado != null && !idLetrado.equals("")) {
		//System.out.println("CON dia GUARDIA CON LETRADO");
		comboBD = "turnosVol";
		if (!tipoAsistencia.equals(""))
			tipoAsistenciaSel.add(tipoAsistencia);
		else {
			tipoAsistencia = tipoAsistenciaDefecto;
			tipoAsistenciaSel.add(tipoAsistencia); // Por defecto marcamos "Asistencia generada por volante Express
		}
		if (!tipoAsistenciaColegio.equals(""))
			tipoAsistenciaColegioSel.add(tipoAsistenciaColegio);
		else
			tipoAsistenciaColegioSel.add("2"); // Por defecto marcamos "Guardia 24h. Asistencia al detenido. Procedimiento general"

		// Si tiene guardias, ponemos combos con su informacion, sino los genericos
		//System.out.println("aviso:"+aviso);
		if (!aviso.equals("")) {

			if (aviso
					.equals(UtilidadesString
							.getMensajeIdioma(usr,
									"gratuita.volantesExpres.mensaje.letradoSinGuardia"))) {

				//System.out.println("LETRADO SIN GUARDIA");
				comboBD = "turnosVolSinLetrado";
				datoTurno = new String[3];
				datoTurno[0] = usr.getLocation();
				datoTurno[1] = diaGuardia;
			} else {
				//System.out.println("LETRADO CON MENSAJE DE GUARDIA");
				datoTurno = new String[3];
				datoTurno[0] = usr.getLocation();
				datoTurno[1] = idLetrado;
				datoTurno[2] = diaGuardia;
				datoGuardia[1] = "0";
			}

		} else {
			//.out.println("LETRADO CON GUARDIA");
			datoTurno = new String[3];
			datoTurno[0] = usr.getLocation();
			datoTurno[1] = idLetrado;
			datoTurno[2] = diaGuardia;
			datoGuardia[1] = "0"; // Para que busque cualquier guardia independiente del letrado y fecha

		}
		if (aviso.equals(UtilidadesString.getMensajeIdioma(usr,
				"gratuita.volantesExpres.mensaje.esLetradoSustituto"))) {
			//System.out.println("ES LETRADO SUSTITUTO");
			datoGuardia[0] = "1"; // Para que no filtre por guardias de sustitucion
		}
		datoGuardia[2] = idLetrado;
		datoGuardia[3] = diaGuardia;

		datoSustituto[1] = diaGuardia;
		datoSustituto[2] = idLetrado;

		colegiadosGuardia[1] = diaGuardia;

	} else {
		//System.out.println("SIN GUARDIA SIN LETRADO");
		tipoAsistenciaSel.add(tipoAsistenciaDefecto); // Por defecto marcamos "Asistencia generada por volante Express
		tipoAsistenciaColegioSel.add("2"); // Por defecto marcamos "Guardia 24h. Asistencia al detenido. Procedimiento general"
		primeraVisita = true;
	}

	if (centroJuzgado != null
			&& centroJuzgado.equalsIgnoreCase("juzgado")) {
		bJuzgado = "checked";
	}
	Vector vAsistencias = (Vector) request.getAttribute("asistencias");

	if (vAsistencias == null || vAsistencias.size() == 0) {
		//System.out.println("SIN ASISTENCIAS");
		turnoSel.add(idTurno);
		guardiaSel.add(idGuardia);
		colegioSel.add(numLetrado);
		tipoAsistenciaSel.add(tipoAsistencia);
		tipoAsistenciaColegioSel.add(tipoAsistenciaColegio);

	} else {
		//System.out.println("CON ASISTENCIAS");
		if (!(!desdeNuevo && !sinAvisos && aviso != null && !aviso
				.equals(""))) {
			//System.out.println("POR ESTE LADO");
			//System.out.println("DESDENUEVO"+desdeNuevo);
			//System.out.println("sinAvisos"+sinAvisos);
			//System.out.println("aviso"+aviso);

			Hashtable h = (Hashtable) vAsistencias.get(0);

			if (h.get(ScsAsistenciasBean.C_JUZGADO) != null
					&& !((String) h.get(ScsAsistenciasBean.C_JUZGADO))
							.equals("")) {
				bJuzgado = "checked";
			}

			datoGuardia[4] = (String) h
					.get(ScsAsistenciasBean.C_IDTURNO);
			turnoSel = new ArrayList();
			//turnoSel.add(usr.getLocation()+","+(String)h.get(ScsAsistenciasBean.C_IDTURNO));
			turnoSel.add(usr.getLocation() + ","
					+ (String) h.get(ScsAsistenciasBean.C_IDTURNO));
			guardiaSel = new ArrayList();
			guardiaSel.add((String) h
					.get(ScsAsistenciasBean.C_IDGUARDIA)
					+ ","
					+ (String) h.get(ScsAsistenciasBean.C_IDTURNO));
			colegioSel.add(numLetrado);
			tipoAsistenciaSel.add((String) h
					.get(ScsAsistenciasBean.C_IDTIPOASISTENCIA));
			tipoAsistenciaColegioSel.add((String) h
					.get(ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO));
		} else {

			//System.out.println("POR aqui");
		}
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="gratuita.volantesExpres.literal.titulo"  localizacion="gratuita.volantesExpres.literal.localizacion"/>
	
	
	<script language="JavaScript">	

		numFila = 0;
		datosTablaBorrar = "";
		esiniciocombo = 0;

		function crearFila (ejg,anioAsistencia,numeroAsistencia,idInstitucion,numEjg) 
		{ 
			t = document.getElementById("asistencias");
			fila = t.insertRow(t.rows.length);

			numFila = numFila + 1;
			fila.id = "fila_" + numFila;
			fila.align = "center";

			// hora (horas - minutos)  y Campos Clave
			celda = fila.insertCell(); 
			celda.className = "";
			celda.innerText="";
			celda.innerHTML = '<input type="hidden" id="claveAnio_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="claveNumero_' + numFila + '" value="">' + 
			                  '<input type="hidden" id="claveIdInstitucion_' + numFila + '" value="">' +
			                  '<input type="hidden" id="numero_designa_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="ejg_numero_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="ejg_anio_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="ejg_tipo_' + numFila + '" value=""> ' +
							  '<input type="text" id="hora_'   + numFila + '" class="box" style="width:21;margin-top:3px;text-align:center;" maxLength="2" value="" onBlur="validaHora(this);" /> : ' + 
			 				  '<input type="text" id="minuto_' + numFila + '" class="box" style="width:21;margin-top:3px;text-align:center;" maxLength="2" value="" onBlur="validaMinuto(this);" />';

			// centro detencion	/ Juzgado
			celda = fila.insertCell(); 
			celda.className = "";
			celda.innerText="";
			aux = '';
			
			// Centro detencion
			if (document.VolantesExpresForm.lugar[0].checked && 
			    document.VolantesExpresForm.lugar[0].value == "centro") {

				aux = '<input type="text" id="codComisaria_' + numFila + '" class="box" size="8"  style="width:21;margin-top:3px;margin-left:3px;" maxlength="10" onBlur="obtenerComisaria(' + numFila + ');" /> ' +
					  '<iframe ID="comisaria_' + numFila + 'Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=comisaria_' + numFila + '&tipo=comboComisariasTurno&clase=boxCombo&estilo=width:230px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros=<%=usr.getLocation()%>&id='+ document.VolantesExpresForm.turno.value +'&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> ' + 
				      '<input type="hidden" id="comisaria_' + numFila + '" value="">';
			}
			// Juzgado
			else {
				aux = '<input type="text" id="codJuzgado_' + numFila + '" class="box" size="8" style="width:21;margin-top:3px;margin-left:3px;" maxlength="10" onBlur="obtenerJuzgado(' + numFila + ');"/> ' +
					  '<iframe ID="juzgado_' + numFila + 'Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=juzgado_' + numFila + '&tipo=comboJuzgadosTurno&clase=boxCombo&estilo=width:230px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros=<%=usr.getLocation()%>&id='+ document.VolantesExpresForm.turno.value +'&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> ' + 
				      '<input type="hidden" id="juzgado_' + numFila + '" value="">';
			}
			
			celda.innerHTML = aux;

			// asistido (dni - nombre apellido1 apellido2)
			celda = fila.insertCell(); 
			celda.className = "";
			celda.innerText="";
			celda.innerHTML ='<input type="text" id="dni_' + numFila + '" class="box" style="width:70;margin-top:3px;" value="" maxlength="20" onBlur="obtenerPersona(' + numFila + ');" /> - ' +
			                 '<input type="text" id="nombre_' + numFila + '" class="box" style="width:80;margin-top:3px;" value="" maxlength="80"/>&nbsp;' + 
			                 '<input type="text" id="apellido1_' + numFila + '" class="box" style="width:80;margin-top:3px;" value="" maxlength="80"/>&nbsp;' +
			                 '<input type="text" id="apellido2_' + numFila + '" class="box" style="width:80;margin-top:3px;" value="" maxlength="80"/>' +
			                 '<input type="hidden" id="idPersona_' + numFila + '" class="box" value=""/>' +
			                 '<img id="info_existe_' + numFila + '" src="<%=app%>/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/>';
		
			// numero diligencia (num diligencia / anio)
			celda = fila.insertCell(); 
			celda.className = "";
			celda.innerText="";
			celda.innerHTML ='<input type="text" id="diligencia_' + numFila + '" class="box" maxlength="20" style="width:70;margin-top:3px;" value=""/> ';

			// delitos
			
			if(<%=isDelitosVE.booleanValue()%>){
				celda = fila.insertCell(); 
				celda.className = "";
				celda.innerText="";
				aux = '';
				
				// Delitos
				aux =  '<iframe ID="idDelito_' + numFila + 'Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=idDelito_' + numFila + '&tipo=comboDelitos&clase=boxCombo&estilo=width:215px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros=<%=usr.getLocation()%>&id='+ document.VolantesExpresForm.turno.value +'&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> ' + 
					      '<input type="hidden" id="idDelito_' + numFila + '" value="">'+
					      '<input type="hidden" id="observaciones_' + numFila + '" value="">';
					      
				
				celda.innerHTML = aux;
			}else{
			
				celda = fila.insertCell(); 
				celda.className = "";
				celda.innerText="";
				celda.innerHTML ='<input type="text" id="observaciones_' + numFila + '" class="box" style="width:130;margin-top:3px;" value=""/>'+
				'<input type="hidden" id="idDelito_' + numFila + '" value="">';
			}
			// boton borrar
			celda = fila.insertCell(); 
			celda.className = "";
			celda.innerText="";
			if(ejg){
				concatenado = '<img src="<%=app%>/html/imagenes/icono+.gif" style="cursor:hand;" alt="<%=UtilidadesString.getMensajeIdioma(usr, "general.boton.new")%>" name="" border="0" onclick="accionNuevaActuacion('+anioAsistencia+','+numeroAsistencia+','+idInstitucion+')">';
				concatenado += '<img src="<%=app%>/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<%=UtilidadesString.getMensajeIdioma(usr, "general.boton.borrar")%>" name="" border="0" onclick="borrarFila(\''+ fila.id +'\')">';
					if(numEjg)
						concatenado += '<input type="text" size="10" name="numEjg" value="'+numEjg+'" readOnly="readonly" class="box" />';
					else
						concatenado +=	'<input type="button"  id = "idButton" value="EJG" alt="EJG"  class="buttonEnTabla" onclick="accionCrearEJG('+anioAsistencia+','+numeroAsistencia+','+idInstitucion+');"/>';
						
				
				celda.innerHTML = concatenado;
			}else{
				// concatenado = '<img src="<%=app%>/html/imagenes/icono+.gif" style="cursor:hand;" alt="<%=UtilidadesString.getMensajeIdioma(usr, "general.boton.new")%>" name="" border="0" onclick="accionNuevaActuacion('+anioAsistencia+','+numeroAsistencia+','+idInstitucion+')">';
				concatenado = '<img src="<%=app%>/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<%=UtilidadesString.getMensajeIdioma(usr, "general.boton.borrar")%>" name="" border="0" onclick="borrarFila(\''+ fila.id +'\')">';
				celda.innerHTML = concatenado;
			}

			fila.scrollIntoView(false);
			validarAnchoTabla();
			habilitarCabecera (false);
		} //crearFila ()
		
		function accionCrearEJG(anioAsistencia,numeroAsistencia,idInstitucion) 
		{	
		document.DefinirEJGForm.modo.value = "nuevo";
			document.DefinirEJGForm.asistenciaAnio.value = anioAsistencia;
			document.DefinirEJGForm.asistenciaNumero.value = numeroAsistencia;
			document.DefinirEJGForm.origen.value = "A";
			document.DefinirEJGForm.idInstitucion.value = idInstitucion;
			var resultado=ventaModalGeneral(document.DefinirEJGForm.name,"M");

			if (resultado){
				if(resultado=="MODIFICADO"){
					refrescarLocal();
				}
		   }	
		}
		function accionNuevaActuacion(anioAsistencia,numeroAsistencia,idInstitucion) 
		{	
		
			document.AsistenciasForm.modo.value = "nuevo";
			document.AsistenciasForm.anio.value = anioAsistencia;
			document.AsistenciasForm.numero.value = numeroAsistencia;
			//document.AsistenciasForm.idInstitucion.value = idInstitucion;
			var resultado=ventaModalGeneral(document.AsistenciasForm.name,"G");

			if (resultado){
				if(resultado=="MODIFICADO"){
					refrescarLocal();
				}
		   }	
		}
		
		
		
		
		function  borrarFila (idFila) 
		{ 
			if (!confirm('<siga:Idioma key="gratuita.volantesExpres.mensaje.eliminarAsistencia"/>'))
				return;
				
			t = document.getElementById("asistencias"); 
			for (i = 0; i < t.rows.length; i++) {
				if (t.rows[i].id == idFila) {
					
					// Guardamos los datos a borrar
					fila = idFila.split("_")[1];
					
					error = '';
					numeroEjg = document.getElementById("ejg_numero_" + fila).value;
					if(numeroEjg &&numeroEjg!=''){
						error = '<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.volantesExpres.mensaje.borrar.ejgAsociado")%>';
					}
			
					
					numeroDesigna = document.getElementById("numero_designa_" + fila).value;
					
					if(numeroDesigna && numeroDesigna!=''){
						error += '\n'+'<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.volantesExpres.mensaje.borrar.designaAsociada")%>';
						//alert("No se puede eliminar:"+numeroDesigna);
						//return;
					}
					if(error!=''){
						alert(error);
						return;
					}
					
					
					claveAnio = document.getElementById("claveAnio_" + fila).value;
					
					claveNumero = document.getElementById("claveNumero_" + fila).value;
					claveIdInstitucion = document.getElementById("claveIdInstitucion_" + fila).value;
					
					if (claveAnio != "" && claveNumero != "" && claveIdInstitucion != "") {
						datosTablaBorrar = datosTablaBorrar + "--borrar--" + "," +
																claveAnio + "," +
																claveNumero + "," +
																claveIdInstitucion + "#";
					}

					t.deleteRow (i); 
					validarAnchoTabla();
					
					if (t.rows.length <= 1) habilitarCabecera (true);
					return;
				}
			}
		} 
		
		function habilitarCabecera (t) 
		{
			//document.VolantesExpresForm.numeroNifTagBusquedaPersonas.disabled = !t;
			//document.VolantesExpresForm.nombrePersona.disabled = !t;
			document.VolantesExpresForm.checkSustitutoDe.disabled = !t;
			document.VolantesExpresForm.sustitutoDe.disabled = !t;
	//		document.VolantesExpresForm.guardiaDia.disabled = !t;
		//	document.VolantesExpresForm.lugar[0].disabled = !t;
			//document.VolantesExpresForm.lugar[1].disabled = !t;
			//document.VolantesExpresForm.turno.disabled = !t;
			//document.frames.guardiaFrame.document.getElementById('guardiaSel').disabled = !t;
			//if (!t)	setTimeout("document.frames.guardiaFrame.document.getElementById('guardiaSel').disabled=true;", 200);
			//document.VolantesExpresForm.guardia.disabled = !t;
			//document.VolantesExpresForm.tipoAsistencia.disabled = !t;
			//document.VolantesExpresForm.tipoAsistenciaColegio.disabled = !t;

			if (t) {
			//	document.VolantesExpresForm.buscarCliente.onclick = buscarPersona;
			//	document.VolantesExpresForm.limpiar.onclick = limpiarPersona;
			//	document.getElementById("iconoCalendarioA").onclick = accionCalendario;
				setTimeout("activarComboSustituto ();", 1000);
			}
			//else {
			//	document.VolantesExpresForm.buscarCliente.onclick = "";
			//	document.VolantesExpresForm.limpiar.onclick = "";
				//document.getElementById("iconoCalendarioA").onclick = "";
			//}
			
			// if (primeraVisita) { // Solo dejamos los botones de letrado y fecha guardia %>
			//	document.VolantesExpresForm.numeroNifTagBusquedaPersonas.disabled = false;
			//	document.VolantesExpresForm.nombrePersona.disabled = false;
			//	document.VolantesExpresForm.buscarCliente.onclick = buscarPersona;
			//	document.VolantesExpresForm.limpiar.onclick = limpiarPersona;
			//	document.VolantesExpresForm.guardiaDia.disabled = false;
			//	document.getElementById("iconoCalendarioA").onclick = accionCalendario;
			// } %>
		}
	
		function activarComboSustituto()
		{
			//Solo activaremos el combo Sustituto cuando no seza letrado de guardi.
			//Esto se da cuando el colegiado del combo de colegiados de guardia
			//no es el mismo que el colegiado del textfield
			colegiadoGuardia = document.getElementById("colegiadosGuardia").value;
			colegiado="";
			
			if (document.getElementById('numeroNifTagBusquedaPersonas')){
			 colegiado = document.getElementById('numeroNifTagBusquedaPersonas').value;
			}
			if(colegiadoGuardia!=colegiado){
				
				
			
				o = document.VolantesExpresForm.checkSustitutoDe;
				
				if (o.checked) {
					if (document.VolantesExpresForm.letrado.value == "" || 
						document.VolantesExpresForm.guardiaDia.value == "" || 
						document.VolantesExpresForm.guardia.value == "") {
						o.checked = false;
						return;
					}
				}
				else {
					<% if (aviso.equals(UtilidadesString.getMensajeIdioma(usr, "gratuita.volantesExpres.mensaje.esLetradoSustituto"))) { %>
						 alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarSustitutoObligatoriamente"/>');
						 o.checked = true;
						 return;
					<% } %>
					
					document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').value = "";
				}
				
				document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').disabled=!o.checked;
				<% if (!primeraVisita) { %>
					<% if (modoAnterior.equalsIgnoreCase("insertarFila")) { %>
						o.disabled = true;
					<% } else { %>
						o.disabled = false;
					<% } %>
				<% } %>
			}else{
				o = document.VolantesExpresForm.checkSustitutoDe;
				o.disabled = true;
				o.checked = false;
				document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').value = "";
				document.VolantesExpresForm.sustitutoDe.disabled = true;
			
			}
		}
		
	

		function initPage () 
		{
			//alert("init page");
			document.VolantesExpresForm.letrado.value = "<%=idLetrado%>";
			document.VolantesExpresForm.numeroNifTagBusquedaPersonas.value = "<%=numLetrado%>";
			document.VolantesExpresForm.nombrePersona.value = "<%=nombreLetrado%>";
			document.VolantesExpresForm.guardiaDia.value = "<%=diaGuardia%>";
			document.VolantesExpresForm.turno.value =  "<%=idTurno%>";
			document.VolantesExpresForm.guardia.value =  "<%=idGuardia%>";
			document.VolantesExpresForm.sustitutoDe.value = "<%=sustitutoDe%>";
			
			
			turno = "<%=idTurno%>";
			if(!turno)
				turno = document.VolantesExpresForm.turno.value;
			
			
			<% if (primeraVisita) { %>
					habilitarCabecera(false);
			<% } else { %>
				
					esiniciocombo = 1;
					
					document.VolantesExpresForm.turno.onchange();
					

				<% if (modoAnterior.equalsIgnoreCase("insertarFila")) { %>
					habilitarCabecera(false);
				<% } else { %>

					<% if (!desdeNuevo && !sinAvisos && aviso != null && !aviso.equals("")) { 
					%>
						<% if (!desdeNuevo && !sinAvisos && aviso != null && !aviso.equals("")) { %>
							<% if (aviso.equals(UtilidadesString.getMensajeIdioma(usr, "gratuita.volantesExpres.mensaje.esLetradoSustituto"))) { %>
								 document.VolantesExpresForm.checkSustitutoDe.checked = true;
								 habilitarCabecera(false);
								 return;
							<% } else { %>
								setTimeout("alert ('<%=aviso%>')",50);
								return;
							<% } %>
					<% } %>
				<% } %>
				<% } %>
			<% } %>
			
			<% 
			//System.out.println("POR AQUI"+sustitutoDe+":");
			if (!sustitutoDe.equals("")) { %>
				document.VolantesExpresForm.checkSustitutoDe.checked = true;
				document.VolantesExpresForm.sustitutoDe.value = "<%=sustitutoDe%>";
				seleccionComboSiga ("sustitutoDe", "<%=sustitutoDe%>");
				setTimeout("setTimeout(\"document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').disabled=true;\", 100);",1000);
			<% }else{%>
				document.VolantesExpresForm.checkSustitutoDe.checked = false;
			<% }%>

			<% // Creamos las lineas de asistencias
			   if (vAsistencias != null && vAsistencias.size() > 0 ) {

					for (int i = 0; i < vAsistencias.size(); i++) { 

						Hashtable h = (Hashtable) vAsistencias.get(i);
						String vHoras="";
						String vMinutos="";
						if (h != null) {
			%>
							anioAsistencia = "<%=UtilidadesHash.getString(h, "ANIO")%>";
							numeroAsistencia = "<%=UtilidadesHash.getString(h, "NUMERO")%>";
							idInstitucion = "<%=UtilidadesHash.getString(h, "IDINSTITUCION")%>";

							numeroEjg = "<%=UtilidadesHash.getString(h, "EJGNUMERO")%>";
							anioEjg = "<%=UtilidadesHash.getString(h, "EJGANIO")%>";
							idTipoEjg = "<%=UtilidadesHash.getString(h, "EJGIDTIPOEJG")%>";
							numeroDesigna = "<%=UtilidadesHash.getString(h, "DESIGNA_NUMERO")%>";
							<%
								String numEJG = (String)UtilidadesHash.getString(h, "NUMEJG"); 
								
								if(numEJG!=null){
									StringBuffer numeroEJG = new StringBuffer(UtilidadesHash.getString(h, "EJGANIO"));
									numeroEJG.append("/");
									numeroEJG.append(numEJG);
									
									%>
									crearFila ("ejg",anioAsistencia,numeroAsistencia,idInstitucion,'<%=numeroEJG.toString()%>');
								<%}else{%>
									crearFila ("ejg",anioAsistencia,numeroAsistencia,idInstitucion);		
								<%}
							%>

							document.getElementById("claveAnio_<%=i+1%>").value = anioAsistencia;
							document.getElementById("claveNumero_<%=i+1%>").value = numeroAsistencia
							document.getElementById("claveIdInstitucion_<%=i+1%>").value = idInstitucion;
							document.getElementById("ejg_numero_<%=i+1%>").value = numeroEjg;
							document.getElementById("ejg_anio_<%=i+1%>").value = anioEjg;
							document.getElementById("ejg_tipo_<%=i+1%>").value = idTipoEjg;
							document.getElementById("numero_designa_<%=i+1%>").value = numeroDesigna;
							
							
							
							
							<%if( (!(UtilidadesHash.getString(h, "HORA").equals("00")))||(!(UtilidadesHash.getString(h, "MINUTO").equals("00")) && (UtilidadesHash.getString(h, "HORA").equals("00")) )){
							 vHoras=UtilidadesHash.getString(h, "HORA");
							}%>
							<%if ((!(UtilidadesHash.getString(h, "MINUTO").equals("00")))||((UtilidadesHash.getString(h, "MINUTO").equals("00")) && !(UtilidadesHash.getString(h, "HORA").equals("00"))) ){
							 vMinutos=UtilidadesHash.getString(h, "MINUTO");
							}%>
							document.getElementById("hora_<%=i+1%>").value = "<%=vHoras%>";
							document.getElementById("minuto_<%=i+1%>").value = "<%=vMinutos%>";
							document.getElementById("dni_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "NIF")%>";
							document.getElementById("nombre_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "NOMBRE")%>";
							document.getElementById("apellido1_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "APELLIDO1")%>";
							document.getElementById("apellido2_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "APELLIDO2")%>";
							ponerIconoIdentPersona ("<%=i+1%>", true);
							document.getElementById("idPersona_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "IDPERSONAJG")%>";
							document.getElementById("diligencia_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "NUMERODILIGENCIA")%>";
							document.getElementById("observaciones_<%=i+1%>").value = "<%=UtilidadesHash.getString(h, "OBSERVACIONES")%>";
							
							idDelito = "<%=UtilidadesHash.getString(h, "IDDELITO")%>";
							if(document.getElementById("idDelito_<%=i+1%>"))
								seleccionComboSiga ("idDelito_<%=i+1%>", idDelito);
							
							
							
							<% if (!bJuzgado.equals("")) { %>
								valor = "<%=UtilidadesHash.getString(h, "JUZGADO")%>,<%=UtilidadesHash.getString(h, "JUZGADOIDINSTITUCION")%>";
								if(document.getElementById("juzgado_<%=i+1%>"))
								seleccionComboSiga ("juzgado_<%=i+1%>", valor);
							<% } else { %>
								valor = "<%=UtilidadesHash.getString(h, "COMISARIA")%>,<%=UtilidadesHash.getString(h, "COMISARIAIDINSTITUCION")%>";
								if(document.getElementById("comisaria_<%=i+1%>"))
									seleccionComboSiga ("comisaria_<%=i+1%>", valor);
							<% } %>

						<% } // if hashtable %>
					<% } // for asistencias %>
			<%  } // if asistencias %>
			
			<% if (desdeNuevo) { %>
				crearFila ();
			<% } %>
		} //initPage ()

		
		
		function accionComboGuardia(){
			//alert("accionComboGuardia:"+esiniciocombo);
			if(esiniciocombo==0){
				accionObtenerDatosCabecera(false,'');
			}
			
			esiniciocombo = 0;
			
			
		
		}
		
		function accionLetradoGuardia(){
			
				
				colegiadoGuardia = document.getElementById("colegiadosGuardia").value;
				colegiado="";
			
				if (document.getElementById('numeroNifTagBusquedaPersonas')){
				 colegiado = document.getElementById('numeroNifTagBusquedaPersonas').value;
				}
				 
				if(colegiadoGuardia!=colegiado){
					if(colegiadoGuardia&&colegiadoGuardia!=''){
						
						document.getElementById('numeroNifTagBusquedaPersonas').value = colegiadoGuardia;
						obtenerPersonas();
					
					}else{
						//Aqui habria que intentar limpiar
		
						
					
					}
				}
			
			
			
		}
		
		function accionBusquedaPersona(){
			//if(escambiocolegiado==0){
				accionObtenerDatosCabecera(false,'');
			//}

			//escambiocolegiado = 1;
			
		
		}
		
		
		
		function accionObtenerDatosCabecera (reset, modo, target)
		{
			
			
			if(document.VolantesExpresForm.guardia.value){
				if (!document.VolantesExpresForm.letrado.value && document.VolantesExpresForm.guardiaDia.value ) {
					if (reset) {
						//document.VolantesExpresForm.lugar.value = "centro";
						//document.VolantesExpresForm.lugar[0].checked = true;
						document.VolantesExpresForm.turno.value = "";
						document.VolantesExpresForm.guardia.value = "";
						document.VolantesExpresForm.sustitutoDe.value = "";
						document.VolantesExpresForm.guardiaDelSustituto.value = "";
						document.VolantesExpresForm.tipoAsistencia.value = "";
						document.VolantesExpresForm.tipoAsistenciaColegio.value = "";
						document.frames.guardiaFrame.document.getElementById('guardiaSel').value = "";
						document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').value = "";
					}
					
					if (modo == "") modo = "obtenerDatosCabecera"
					document.VolantesExpresForm.modo.value = modo;
					document.VolantesExpresForm.target = "mainWorkArea";
					
					if(target)
						document.VolantesExpresForm.target = target ;
					habilitarCabecera (true);
					document.VolantesExpresForm.submit();
					habilitarCabecera (false);
					
				}else if (document.VolantesExpresForm.letrado.value && document.VolantesExpresForm.guardiaDia.value) {
	
					// Resetamos los datos
					if (reset) {
						//document.VolantesExpresForm.lugar.value = "centro";
						//document.VolantesExpresForm.lugar[0].checked = true;
						document.VolantesExpresForm.turno.value = "";
						document.VolantesExpresForm.guardia.value = "";
						document.VolantesExpresForm.sustitutoDe.value = "";
						document.VolantesExpresForm.guardiaDelSustituto.value = "";
						document.VolantesExpresForm.tipoAsistencia.value = "";
						document.VolantesExpresForm.tipoAsistenciaColegio.value = "";
						document.frames.guardiaFrame.document.getElementById('guardiaSel').value = "";
						document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').value = "";
					}
					
					if (modo == "") modo = "obtenerDatosCabecera"
					document.VolantesExpresForm.modo.value = modo;
					document.VolantesExpresForm.target = "mainWorkArea";
					
					if(target)
						document.VolantesExpresForm.target = target ;
					habilitarCabecera (true);
					document.VolantesExpresForm.submit();
					habilitarCabecera (false);
				}
			}else{
			if (!document.VolantesExpresForm.letrado.value && document.VolantesExpresForm.guardiaDia.value ) {
					if (reset) {
						//document.VolantesExpresForm.lugar.value = "centro";
						//document.VolantesExpresForm.lugar[0].checked = true;
						document.VolantesExpresForm.turno.value = "";
						document.VolantesExpresForm.guardia.value = "";
						document.VolantesExpresForm.sustitutoDe.value = "";
						document.VolantesExpresForm.guardiaDelSustituto.value = "";
						document.VolantesExpresForm.tipoAsistencia.value = "";
						document.VolantesExpresForm.tipoAsistenciaColegio.value = "";
						document.frames.guardiaFrame.document.getElementById('guardiaSel').value = "";
						document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').value = "";
					}
					
					if (modo == "") modo = "obtenerDatosCabecera"
					document.VolantesExpresForm.modo.value = modo;
					document.VolantesExpresForm.target = "mainWorkArea";
					
					if(target)
						document.VolantesExpresForm.target = target ;
					habilitarCabecera (true);
					document.VolantesExpresForm.submit();
					habilitarCabecera (false);
					
				}else if (document.VolantesExpresForm.letrado.value && document.VolantesExpresForm.guardiaDia.value) {
	
					// Resetamos los datos
					if (reset) {
						//document.VolantesExpresForm.lugar.value = "centro";
						//document.VolantesExpresForm.lugar[0].checked = true;
						document.VolantesExpresForm.turno.value = "";
						document.VolantesExpresForm.guardia.value = "";
						document.VolantesExpresForm.sustitutoDe.value = "";
						document.VolantesExpresForm.guardiaDelSustituto.value = "";
						document.VolantesExpresForm.tipoAsistencia.value = "";
						document.VolantesExpresForm.tipoAsistenciaColegio.value = "";
						document.frames.guardiaFrame.document.getElementById('guardiaSel').value = "";
						document.frames.sustitutoDeFrame.document.getElementById('sustitutoDeSel').value = "";
					}
					
					if (modo == "") modo = "obtenerDatosCabecera"
					document.VolantesExpresForm.modo.value = modo;
					document.VolantesExpresForm.target = "mainWorkArea";
					
					if(target)
						document.VolantesExpresForm.target = target ;
					habilitarCabecera (true);
					document.VolantesExpresForm.submit();
					habilitarCabecera (false);
				}
			
			
			}
		}


		function accionCalendario() 
		{
		
			// Abrimos el calendario 
			var resultado = showModalDialog("<%=app%>/html/jsp/general/calendarGeneral.jsp?valor="+document.VolantesExpresForm.guardiaDia.value,document.VolantesExpresForm.guardiaDia,"dialogHeight:275px;dialogWidth:400px;help:no;scroll:no;status:no;");
			//var resultado = showModelessDialog("<%=app%>/html/jsp/general/calendarGeneral.jsp?valor="+document.VolantesExpresForm.guardiaDia.value,document.VolantesExpresForm.guardiaDia,"dialogHeight:275px;dialogWidth:400px;help:no;scroll:no;status:no;");	
			if (resultado) {
				document.VolantesExpresForm.guardiaDia.value = resultado;
				//if(document.VolantesExpresForm.idGuardia.value){
					var ie6 = document.getElementById && document.all&&(navigator.appVersion.indexOf("MSIE 6.")>=0);
					if(ie6){
						document.VolantesExpresForm.target = "submitArea";
						accionObtenerDatosCabecera(true, "", "submitArea");
						return habilitarCabecera (true);
					}else{
						return accionObtenerDatosCabecera(false, "");
						
					}
				//}
				
				
				
			} 
		}

		function accionInsertarRegistroTabla () 
		{
			
			if (document.VolantesExpresForm.checkSustitutoDe && document.VolantesExpresForm.checkSustitutoDe.checked && document.VolantesExpresForm.sustitutoDe && document.VolantesExpresForm.sustitutoDe.value == "") {
				 alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarSustitutoObligatoriamente"/>');				
				 return;
			}
			if (document.VolantesExpresForm.turno.value == "") {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarTurno"/>');
				return;
			}
			
			if (document.VolantesExpresForm.guardia.value == "") {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarGuardia"/>');
				return;
			}
			if (document.VolantesExpresForm.letrado.value == "") {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarColegiado"/>');
				return;
			}
			
			filas = document.getElementById("asistencias").rows.length-1;
			if (filas < 1) {
				document.VolantesExpresForm.desdeNuevo.value="true";
				accionObtenerDatosCabecera (false, "insertarFila");
			}
			else {
				crearFila();
			} 
		} //accionInsertarRegistroTabla ()

		function accionNuevo() 
		{		
			if (confirm("<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.mensaje.nuevaCabecera")%>")) {
				document.VolantesExpresForm.modo.value = "nuevo";
				document.VolantesExpresForm.target = "mainWorkArea";
				document.VolantesExpresForm.submit();
			}
		}

		function accionGuardar() 
		{	
			if (document.VolantesExpresForm.turno.value == "") {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarTurno"/>');
				return;
			}
			
			if (document.VolantesExpresForm.guardia.value == "") {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarGuardia"/>');
				return;
			}
			if (document.VolantesExpresForm.letrado.value == "") {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarColegiado"/>');
				return;
			}
			filas = document.getElementById("asistencias").rows.length-1;
			sub();
			
			if (datosTablaBorrar == "" && filas < 1) {
			 

				// No se han creado asistencias
				// Valido la cabecera de guardia
				if (confirm("<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.mensaje.validarCabeceraGuardia")%>")) {
				

					parametroTurno = document.VolantesExpresForm.turno.value;
					parametroTurno = parametroTurno.split(",")[1];
	
					<% if (!guardiaDelSustituto.equals("")) { %>
						parametroGuardia = "<%=guardiaDelSustituto%>";
					<% } else {%>
						parametroGuardia = document.VolantesExpresForm.guardia.value;
					<% } %>
			
					var datosvalidar="<%=usr.getLocation()%>@@"+ parametroTurno +"@@" + parametroGuardia + "@@ @@<%=idLetrado%>@@<%=diaGuardia%>";
					
					if (trim(datosvalidar) != "") {
						document.ValidarVolantesGuardiasForm.datosValidar.value=datosvalidar;
						document.ValidarVolantesGuardiasForm.modo.value="insertar";
						document.ValidarVolantesGuardiasForm.desde.value="VOLANTE_EXPRES";
						document.ValidarVolantesGuardiasForm.target="submitArea";

						habilitarCabecera (true);
						document.ValidarVolantesGuardiasForm.submit();
						habilitarCabecera (false);
					}
				}
				fin();
				return false;
			}

			// Creo/modifico las asistencias de la tabla de asistencias
			
			// Datos Dinamicos Asistencias
			var lugar = "juzgado";
			if (document.VolantesExpresForm.lugar[0].checked && document.VolantesExpresForm.lugar[0].value == "centro") {
			    lugar = "comisaria";
			}
              	
			var datos = "", accion = "";
			
			for (aa = 1; aa <= filas && aa <= numFila; aa++) {
			 

				i = t.rows[aa].id.split("_")[1];
				if (validarDatosFila (i) == false) {
					fin();
					return;
				}
	              
				claveAnio = document.getElementById("claveAnio_" + i).value;
				claveNumero = document.getElementById("claveNumero_" + i).value;
				claveIdInstitucion = document.getElementById("claveIdInstitucion_" + i).value;
				if (claveAnio == "" && claveNumero == "" && claveIdInstitucion == "") {
					accion = "--insertar--";
					claveAnio = "--vacio--";
					claveNumero = "--vacio--";
					claveIdInstitucion = "--vacio--";
				}
				else {
					accion = "--modificar--";
				}
				
				hora = document.getElementById("hora_" + i).value;
				if (hora == "") hora = "--vacio--";
				
				minuto = document.getElementById("minuto_" + i).value;
				if (minuto == "") minuto = "--vacio--";
	
				dni =  document.getElementById("dni_" + i).value;
				if (dni == "") dni = "--vacio--";
				else dni = replaceAll(dni,',','~');
					
				                 
				ap2 = document.getElementById("apellido2_" + i).value;
				if (ap2 == "") ap2 = "--vacio--";
				else ap2 = replaceAll(ap2,',','~');
				
				idPer = document.getElementById("idPersona_" + i).value;
				if (idPer == "") idPer = "--vacio--";

                diligencia = document.getElementById("diligencia_" + i).value;
				if (diligencia == "") diligencia = "--vacio--";
				else diligencia = replaceAll(diligencia,',','~');

				
				obs = document.getElementById("observaciones_" + i).value;
				
				idDelito = document.getElementById("idDelito_" + i).value;
				if(idDelito=='')
					idDelito = ' ';
				if (obs == "") obs = "--vacio--";
				else obs = replaceAll(obs,',','~');
				
				numeroEjg = document.getElementById("ejg_numero_" + i).value;
				if (numeroEjg == "") numeroEjg = "--vacio--";
				
				anioEjg = document.getElementById("ejg_anio_" + i).value;
				if (anioEjg == "") anioEjg = "--vacio--";
				
				idTipoEjg = document.getElementById("ejg_tipo_" + i).value;
				if (idTipoEjg == "") idTipoEjg = "--vacio--";
				
				nombre = replaceAll(document.getElementById("nombre_" + i).value,',','~');
				apellido1 = replaceAll(document.getElementById("apellido1_" + i).value,',','~');

				//if (document.getElementById("hora_" + i)) {
					datos = datos + accion + "," +
									claveAnio + "," +
									claveNumero + "," +
									claveIdInstitucion + "," +
									hora + "," +
                                    minuto + "," +
                                    document.getElementById(lugar + "_" + i).value + "," +
                                    dni + "," +
                                    nombre+ "," +
                                    apellido1 + "," +
                                    ap2 + "," +
                                    idPer + "," +
                                    diligencia + "," +
                                    obs + "," +
                                    
                                    numeroEjg + "," +
                                    anioEjg + "," +
                                    idTipoEjg + "," + 
                                   idDelito + "," +
                                    
                                    "#";
				//}
				
			}
	
			document.VolantesExpresForm.tablaDatosDinamicosD.value = datos + datosTablaBorrar;
			datosTablaBorrar = "";
			document.VolantesExpresForm.modo.value = "insertar";
			document.VolantesExpresForm.target = "submitArea";
		
			habilitarCabecera (true);
			document.VolantesExpresForm.submit();
			habilitarCabecera (false);
		}
		
		function validarDatosFila (fila)  
		{
			var campo = "";
			var obligatorio = "<%=UtilidadesString.getMensajeIdioma (usr, "messages.campoObligatorio.error")%>";

			/*if (!document.getElementById("hora_" + fila).value) {
				campo = "<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.hora")%>";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}*/

			var lugar = "juzgado";
			if (document.VolantesExpresForm.lugar[0].checked && document.VolantesExpresForm.lugar[0].value == "centro") {
			    lugar = "comisaria";
			}
 
			if (!document.getElementById(lugar+ "_" + fila).value) {
				campo = "<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.centroDetencion")%>" + " / " + "<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.juzgado")%>";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}
			
			if (!document.getElementById("nombre_" + fila).value) {
				campo = "<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.nombre")%> (<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.asistido")%>)";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}

			if (!document.getElementById("apellido1_" + fila).value) {
				campo = "<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.apellido1")%> (<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.volantesExpres.literal.asistido")%>)";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}

			return true;
		}

		function validaHora (o) 
		{
			var horas = trim(o.value);
			var mensajehoras = "<siga:Idioma key="messages.programarFacturacionForm.mensajeHoras"/>";
			
            
			
			if (horas.length==1) {
				o.value = "0" + horas;
			}
			if (horas!="" && (horas>23 || horas<0 || !isNumero(horas))) {
				alert(mensajehoras);
				o.focus();
				return false;
			}
		}

		function validaMinuto (o) 
		{
			var minutos = trim(o.value);
			var mensajeminutos="<siga:Idioma key="messages.programarFacturacionForm.mensajeMinutos"/>";
		
			/*if (minutos.length==0) {
				o.value = "00";
			}*/
			if (minutos.length==1) {
				o.value = "0" + minutos;
			}

			if (minutos!="" && (minutos>59 || minutos<0 || !isNumero(minutos) )) {
				alert(mensajeminutos);
				o.focus();
				return false;
			}
		}

		function validarAnchoTabla () 
		{
			if (document.all.asistencias.clientHeight < document.all.asistenciasDiv.clientHeight) {
				document.all.asistenciasCabeceras.width='100%';
			}
			else {
				document.all.asistenciasCabeceras.width='98.30%';
			}
		}
		
		function obtenerComisaria(fila) 
		{
			if (document.getElementById("codComisaria_"+fila).value!=""){
				seleccionComboSiga ("comisaria_"+fila, "");
				document.MantenimientoComisariaForm.codigoExtBusqueda.value = document.getElementById("codComisaria_"+fila).value;
				document.MantenimientoComisariaForm.nombreObjetoDestino.value = "comisaria_" + fila;
				document.MantenimientoComisariaForm.submit();	
			}
		}
		function obtenerJuzgado(fila) 
		{ 
			if (document.getElementById("codJuzgado_"+fila).value!=""){
				seleccionComboSiga ("juzgado_"+fila, "");
				document.MantenimientoJuzgadoForm.codigoExt.value = document.getElementById("codJuzgado_"+fila).value;
				document.MantenimientoJuzgadoForm.nombreObjetoDestino.value = "juzgado_" + fila;
				document.MantenimientoJuzgadoForm.submit();	
			}
		}
		
		function obtenerPersona (fila) 
		{
			o = document.getElementById ("dni_" + fila);
			o.value = o.value.toUpperCase ();

			if (o.value != "") {
				document.PersonaJGForm.NIdentificacion.value = o.value;
				document.PersonaJGForm.nombreObjetoDestino.value = fila;
				document.PersonaJGForm.submit ();
			}				
		} //obtenerPersona ()
		
		function traspasoDatosJuzgado (resultado)
		{
			if (resultado.length > 1) {
				seleccionComboSiga (resultado[1], resultado[0]);
				document.getElementById(resultado[1]).value = resultado[0];
			}
		} //traspasoDatosJuzgado ()
		function traspasoDatosComisaria (resultado)
		{
			traspasoDatosJuzgado (resultado);
		} //traspasoDatosComisaria ()
		
		// Para la busqueda por dni
		function traspasoDatos (resultado, bNuevo, fila) 
		{
			// Porque todas las funciones se llaman igual, las separamos
			if (!(bNuevo && fila)) 
				return traspasoDatosJuzgado (resultado);
			
			if (resultado && resultado.length > 7 && (bNuevo == 1)) { // Existe
				document.getElementById("dni_" + fila).value = resultado[2];
				document.getElementById("nombre_" + fila).value = resultado[3];
				document.getElementById("apellido1_" + fila).value = resultado[4];
				document.getElementById("apellido2_" + fila).value = resultado[5];
				
				document.getElementById("idPersona_" + fila).value = resultado[1];
				ponerIconoIdentPersona (fila, true);
			}
			else {	// Nuevo
				document.getElementById("idPersona_" + fila).value = "nuevo";
				ponerIconoIdentPersona (fila, false);
			}
		} //traspasoDatos ()
		
		function ponerIconoIdentPersona (fila, encontrado)
		{
			if (encontrado) {
				document.getElementById("info_existe_" + fila).src = "<%=app%>/html/imagenes/encontrado.gif";
				document.getElementById("info_existe_" + fila).alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esYaExistentePersonaJG'/>";
				document.getElementById("nombre_" + fila).disabled = true;
				document.getElementById("apellido1_" + fila).disabled = true;
				document.getElementById("apellido2_" + fila).disabled = true;
			}
			else {
				document.getElementById("info_existe_" + fila).src = "<%=app%>/html/imagenes/nuevo.gif";
				document.getElementById("info_existe_" + fila).alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esNuevaPersonaJG'/>";
				document.getElementById("nombre_" + fila).disabled = false;
				document.getElementById("apellido1_" + fila).disabled = false;
				document.getElementById("apellido2_" + fila).disabled = false;
			}
		} //ponerIconoIdentPersona ()
		
		function refrescarLocal () 
		{
			document.VolantesExpresForm.sinAvisos.value = "true";
			accionObtenerDatosCabecera (false, "");
		} //refrescarLocal ()
				
	</script>
	
</head>

<body onLoad="initPage();">

	<!-- INICIO: CAMPOS DE BUSQUEDA-->	
	<html:form action = "/JGR_VolantesExpres" method="POST" target="mainWorkArea">

		<input type="hidden" name="modo"       value=""/>
		<input type="hidden" name="letrado"    value=""/>
		<input type="hidden" name="sinAvisos"  value=""/>
		<input type="hidden" name="desdeNuevo" value=""/>

	 	<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.cabeceraVolante">

			<table width="100%" border="0">
			<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.volantesExpres.literal.GuardiaDia"/>&nbsp;(*)
					</td>
					<td>
					

						<input type="text" name="guardiaDia" size="10"  value="" readOnly="readonly" class="box" />&nbsp;
						<a href='javascript://' id="iconoCalendarioA" onClick="accionCalendario();" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img id="iconoCalendario" src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
						
					</td>

					<td class="labelText">
						<siga:Idioma key="gratuita.volantesExpres.literal.Lugar"/>&nbsp;(*)
					</td>
					<td>
						<table>
							<tr>
								<td class="boxConsulta">
									<input type="radio" name="lugar" value="centro" checked onclick=" accionObtenerDatosCabecera(false, '');" class ="radio">
									<siga:Idioma key="gratuita.volantesExpres.literal.centroDetencion"/>
								</td>
							
								<td class="boxConsulta">
									<input type="radio" name="lugar" value="juzgado" <%=bJuzgado%> onclick="accionObtenerDatosCabecera(false, '')" class ="radio">
									<siga:Idioma key="gratuita.volantesExpres.literal.juzgado"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.volantesExpres.literal.turno"/>&nbsp;(*)
					</td>
					<td><%
							if (datoTurno == null) {
						%>
							<siga:ComboBD nombre="turno" tipo="<%=comboBD%>" clase="boxCombo" obligatorio="false" accion="Hijo:guardia;" parametro="<%=dato%>" elementoSel="<%=turnoSel%>" obligatorio="si" ancho="320" />
						<%
							} else {
						%>
							<siga:ComboBD nombre="turno" tipo="<%=comboBD%>" clase="boxCombo" obligatorio="false" accion="Hijo:guardia" parametro="<%=datoTurno%>" elementoSel="<%=turnoSel%>"  ancho="320" />
						<%
							}
						%></td>	
					<td class="labelText">
						<siga:Idioma key="gratuita.volantesExpres.literal.guardia"/>&nbsp;(*)
					</td>
					<td><siga:ComboBD nombre="guardia" tipo="guardiasVol" clase="boxCombo" hijo="t" parametro="<%=datoGuardia%>" elementoSel="<%=guardiaSel%>" obligatorioSinTextoSeleccionar="si" ancho="470" accion="Hijo:sustitutoDe,Hijo:colegiadosGuardia;parent.accionComboGuardia();"/>
					</td>	
				</tr>
				<tr>
					<td colspan="2">
						<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.letradosGuardia">
							<table>
								<tr>
									<td  class="labelText">
										<siga:Idioma key="gratuita.volantesExpres.literal.colegiado"/>&nbsp;
										<siga:ComboBD nombre="colegiadosGuardia" tipo="colegiadosGuardia" hijo="t" clase="boxCombo" parametro="<%=colegiadosGuardia%>" ancho="200"  elementoSel="<%=colegioSel%>" accion="parent.accionLetradoGuardia();"/>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
						</td>
						<td colspan="2">&nbsp;
						</td>
				</tr>
			
			<tr>
							
					<td colspan="4">
						<table cellspacing=0 cellpadding=0>
						<tr>
						<td>
							<siga:BusquedaPersona  tipo="colegiado" titulo='<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.volantesExpres.literal.letrado") + " (*)"%>' idPersona="letrado" accion="accionBusquedaPersona();">
							</siga:BusquedaPersona>
						</td>
						<td  class="labelText" style="text-align:right">
							<input type="hidden" name="guardiaDelSustituto" value="<%=guardiaDelSustituto%>"/>&nbsp;
							<input type="checkbox" name="checkSustitutoDe" value="" onclick="activarComboSustituto();" <%if (!sustitutoDe.equals("")) out.print("checked"); %> >
							<siga:Idioma key="gratuita.volantesExpres.literal.sustitudoDe"/>&nbsp;
							<siga:ComboBD nombre="sustitutoDe" tipo="sustitutosGuardiaExcluyente" hijo="t" clase="boxCombo" parametro="<%=datoSustituto%>" ancho="200" accion="parent.activarComboSustituto();" />
						</td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<!--  
					<td class="labelText">
						<siga:Idioma key="gratuita.volantesExpres.literal.tipoAsistencia"/>&nbsp;(*)
					</td>
					<td>
						<siga:ComboBD nombre="tipoAsistencia" tipo="scstipoasistencia" clase="boxCombo" elementoSel="<%=tipoAsistenciaSel%>" obligatorio="si" ancho="320" accion="accionObtenerDatosCabecera(false,'');"/>
					</td>
					-->
					
					<td class="labelText">
						<siga:Idioma key="gratuita.volantesExpres.literal.tipoAsistenciaColegio"/>&nbsp;(*)
					</td>
					<td colspan="3">
						<siga:ComboBD nombre="tipoAsistenciaColegio" tipo="cscstipoasistenciacolegio" clase="boxCombo" parametro="<%=dato%>" elementoSel="<%=tipoAsistenciaColegioSel%>" obligatorio="si" ancho="750" obligatorioSinTextoSeleccionar="si" accion="accionObtenerDatosCabecera(false,'');"/>
						<input type="hidden" name="tipoAsistencia" value="<%=tipoAsistencia%>"/>
					</td>
				</tr>
			
		<tr>
		<td colspan="3" width="1000px">
			<p class="labelText">
				<siga:Idioma key="gratuita.volantesExpres.mensaje.indicaFormaIdentificacion"/>
			</p>
		</td>
		<td class="labelText" style="text-align: right"><siga:Idioma key="gratuita.volantesExpres.literal.fechaJustificacion" />
						<siga:Fecha nombreCampo="fechaJustificacion"
								valorInicial="<%=fechaJustificacion%>" /> &nbsp;<a
								onClick="return showCalendarGeneral(fechaJustificacion);"
								onMouseOut="MM_swapImgRestore();"
								onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img
								src="<%=app%>/html/imagenes/calendar.gif"
								alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"
								border="0"></a>
						</td>
		</tr>
		</td>
		</tr>
		</table>
		</siga:ConjCampos>

		<%
			String nCabeceras = "gratuita.volantesExpres.literal.hora, "
						+ UtilidadesString
								.getMensajeIdioma(usr,
										"gratuita.volantesExpres.literal.centroDetencion")
						+ " / "
						+ UtilidadesString.getMensajeIdioma(usr,
								"gratuita.volantesExpres.literal.juzgado")
						+ ", " + "gratuita.volantesExpres.literal.asistido, "
						+ "gratuita.volantesExpres.literal.numeroDiligencia, ";
				if (isDelitosVE.booleanValue())
					nCabeceras += "gratuita.volantesExpres.literal.delitos,";
				else
					nCabeceras += "gratuita.volantesExpres.literal.observaciones,";

				nCabeceras += "<input type='button'  id = 'idButton' "
						+ "value='"
						+ UtilidadesString.getMensajeIdioma(usr,
								"general.boton.insertar")
						+ "' "
						+ "alt='"
						+ UtilidadesString.getMensajeIdioma(usr,
								"general.boton.insertar")
						+ "' "
						+ "class='button' onclick='accionInsertarRegistroTabla();'"
						+ ">";
		%>

		<siga:TablaCabecerasFijas 
			   nombre="asistencias"
			   borde="2"
			   clase="tableTitle"
			   nombreCol="<%=nCabeceras%>"
			   tamanoCol="6,27,36,9,14,8"
			   alto="100%"
			   ajusteBotonera="" 
		>

		</siga:TablaCabecerasFijas>

	</html:form>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	
	<!-- Formularios auxiliares -->
	<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrirBusquedaModal">
	</html:form>

	<html:form action="/JGR_AsistidoAsistencia" method="POST" target="submitArea" type="" style="display:none">
		<input type="hidden" name="modo"        	    value="buscarNIF">
		<input type="hidden" name="conceptoE"   	    value="PersonaJG">
		<input type="hidden" name="NIdentificacion"     value="">
		<input type="hidden" name="nombreObjetoDestino" value="">
	</html:form>

	<html:form action = "/JGR_MantenimientoJuzgados" method="POST" target="submitArea">
		<input type="hidden" name="modo"                value="buscarJuzgado">
		<input type="hidden" name="codigoExt"           value="">
		<input type="hidden" name="nombreObjetoDestino" value="">
	</html:form>	

	<html:form action = "/JGR_MantenimientoComisarias" method="POST" target="submitArea">
		<input type="hidden" name="modo"                value="buscarComisaria">
		<input type="hidden" name="codigoExtBusqueda"   value="" >
		<input type="hidden" name="nombreObjetoDestino" value="" >
	</html:form>

	<html:form action = "/JGR_ValidarVolantesGuardias" method="POST" target="resultado">
		<input type="hidden" name="modo"         value="" >
		<input type="hidden" name="datosValidar" value="" >
		<input type="hidden" name="datosBorrar"  value="" >
		<input type="hidden" name="desde"        value="" >
	</html:form>
	<html:form action="/JGR_EJG" method="post" target="mainWorkArea">
		<input type="hidden" name = "actionModal" value = ""/>
		<input type="hidden" name="idTipoEJG"/>
		<input type="hidden" name="anio"/>
		<input type="hidden" name="numero"/>
		<input type="hidden" name="fechaApertura"/>
		<input type="hidden" name="idInstitucion" value="<%=usr.getLocation()%>"/>
		<input type="hidden" name="origen" value="A"/>
		<input type="hidden" name="modo" />
		<input type="hidden" name="asistenciaAnio" />
		<input type="hidden" name="asistenciaNumero" />
	</html:form>
	
	<html:form action="/JGR_ActuacionesAsistencia" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="nuevo"/>
		<html:hidden name="AsistenciasForm" property="anio" />
		<html:hidden name="AsistenciasForm" property="numero" />
		<html:hidden name="AsistenciasForm" property="esFichaColegial" />
		<html:hidden name="AsistenciasForm" property="modoPestanha"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
		<siga:ConjBotonesAccion botones="g,n" clase="botonesDetalle"/>
	<!-- FIN: BOTONES BUSQUEDA -->


	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

  </body>

	 <script>
	 function obtenerPersonas () 
		{
			
			
			document.getElementById('nombrePersona').value = "";
			if(document.getElementById('numeroNifTagBusquedaPersonas').value!=""){
			   if (!vForm) vForm=creaForm();
			   vForm.target="submitArea";
			   vForm.numeroNif.value=document.getElementById('numeroNifTagBusquedaPersonas').value;
			   vForm.modo.value="tagBusquedaPersona";
			   vForm.submit();
			}
			else{
				limpiarPersona();
			}
		
			
		}
		
		
    function limpiarPersona () 
	{
			document.getElementById('letrado').value = "";
			document.getElementById('numeroNifTagBusquedaPersonas').value = "";
			document.getElementById('nombrePersona').value = "";
			document.VolantesExpresForm.modo.value = "limpiarPersona";
			document.VolantesExpresForm.target = "mainWorkArea";
			document.VolantesExpresForm.submit();
	}	
    
	 </script>
  
</html>