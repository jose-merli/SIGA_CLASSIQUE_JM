<!-- busquedaDesignas.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String idInstitucion = usr.getLocation();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String[] dato = {usr.getLocation()};

	String nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	String nombreUserBean =  request.getAttribute("nombreUserBean")==null?"":(String)request.getAttribute("nombreUserBean");

	Hashtable datos = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
	String anio="", codigo="", fechaAperturaInicio="", fechaAperturaFin="", idTurno ="", tipoDesigna="", sufijo="";
	String nif="", nombre="", apellido1="", apellido2="", nombreMostrado="";
	String estado="",calidad="",procedimiento="",asunto="",actuacionesPendientes="", nig="";
	ArrayList juzgadoSel   = new ArrayList();
	ArrayList juzgado   = new ArrayList();
	ArrayList juzgadoActu   = new ArrayList();
	ArrayList acreditacion   = new ArrayList();
	ArrayList modulo   = new ArrayList();
	anio = UtilidadesBDAdm.getYearBD("");
		String calidadidinstitucion="";
	String idcalidad="";
	ArrayList calidadSel = new ArrayList();
String[] getdatos = { usr.getLocation() };
	
	// inc6845 // fechaAperturaInicio=UtilidadesBDAdm.getFechaBD("");
	fechaAperturaInicio="";
	
	String BUSQUEDAREALIZADA = (String)request.getSession().getAttribute("BUSQUEDAREALIZADA");
		
		if (BUSQUEDAREALIZADA!=null && datos!=null){
		 
			anio=(String)datos.get("ANIO");
			codigo=(String)datos.get("CODIGO");
			sufijo=(String)datos.get("SUFIJO");
			fechaAperturaInicio=(String)datos.get("FECHAENTRADAINICIO");
			fechaAperturaFin=(String)datos.get("FECHAENTRADAFIN");
			idTurno =(String)datos.get("IDTURNO");
			tipoDesigna=(String)datos.get("IDTIPODESIGNACOLEGIO");
			actuacionesPendientes=(String)datos.get("ACTUACIONES_PENDIENTES");
			estado =(String)datos.get("ESTADO");
			calidad =(String)datos.get("CALIDAD");		
			juzgado.add((String)datos.get("JUZGADO"));
			procedimiento =(String)datos.get("PROCEDIMIENTO");						
			asunto =(String)datos.get("ASUNTO");		
			nig =(String)datos.get("NIG");		
			modulo.add((String)datos.get("MODULO"));			
			juzgadoActu.add((String)datos.get("JUZGADOACTU"));
			acreditacion.add((String)datos.get("ACREDITACION"));						
			
			if (datos.get("NCOLEGIADO")!=null)
			nColegiado=(String)datos.get("NCOLEGIADO");
			
			nif=(String)datos.get("NIF");
			if (nif==null) nif="";
			
			if (datos.get("NOMBRE")!=null)
			nombre=(String)datos.get("NOMBRE");
			if (datos.get("APELLIDO1")!=null)
			apellido1=(String)datos.get("APELLIDO1");
			if (datos.get("APELLIDO2")!=null)
			apellido2=(String)datos.get("APELLIDO2");
			
			if (datos.get("NombreMostrado")!=null)
			nombreMostrado = (String)datos.get("NombreMostrado");
			
			if (datos.get("IDTIPOENCALIDAD")!=null){
				if (datos.get("CALIDADIDINSTITUCION")!=null){
					calidadidinstitucion	=  datos.get("CALIDADIDINSTITUCION").toString();
					idcalidad = datos.get("IDTIPOENCALIDAD").toString();
					calidadSel.add(0,idcalidad+","+calidadidinstitucion);
				} 
			}else{
				if (!calidad.equals("")&&(calidad!=null)){					
					calidadSel.add(0,calidad+","+idInstitucion);
				}
			}
			
		}
	

%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.busquedaDesignas.literal.titulo" 
		localizacion="gratuita.busquedaDesignas.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">	
	
		//Selecciona datos de la busqueda ultima antes de editar un registro
		function seleccionDatos(){
			var turnoSel = "<%=usr.getLocation()%>,<%=idTurno%>";
			var tipoDesignaSel = "<%=tipoDesigna%>";
			var f = document.forms[1];
			
			//Seleccion combo turno
			for (var i=1; i<f.idTurno.length; i++) {
				if (f.idTurno.options[i].value==turnoSel){
					f.idTurno.options[i].selected=true;
					break;
				}
			}

			//Seleccion combo tipo designa
			for (var i=1; i<f.tipoDesigna.length; i++) {
				if (f.tipoDesigna.options[i].value==tipoDesignaSel){
					f.tipoDesigna.options[i].selected=true;
					break;
				}
			}
			
			//Seleccion del letrado
			f.ncolegiado.value = "<%=nColegiado%>";
			//f.nombreMostrado.value = "<%=nombreMostrado%>";
			
			
			//Seleccion del interesado:
			f.nombre.value = "<%=nombre%>";
			f.apellido1.value = "<%=apellido1%>";
			f.apellido2.value = "<%=apellido2%>";
		}
		function inicio(){
		<%if (BUSQUEDAREALIZADA!=null){%>
			seleccionDatos();
			buscarPaginador();
		<%}%>
		}
	</script>
	
</head>

<body  onLoad="ajusteAlto('resultado');inicio();">

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE CLIENTES -->
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>

	<!-- FIN: FORMULARIO DE BUSQUEDA DE CLIENTES -->	
	
	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action = "/JGR_Designas.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property ="actionModal" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden name="BuscarDesignasForm" property="ncolegiado" value=""/>
		<html:hidden property="seleccionarTodos" />

	<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.datosDesigna">
	<table width="100%" border="0" >
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/> / <siga:Idioma key="gratuita.busquedaSOJ.literal.codigo"/>
		</td>
		<td>	
			<html:text name="BuscarDesignasForm" property="anio"  style="width:40" maxlength="4" styleClass="box" value="<%=anio%>"></html:text> / <html:text name="BuscarDesignasForm" property="codigo" size="5" maxlength="10" styleClass="box" value="<%=codigo%>"></html:text> 
		</td>
		<td class="labelText">
			&nbsp;
		</td>
		<td class="labelText" >	
			&nbsp;
		</td>	
		<td class="labelText">	
			<siga:Idioma key="gratuita.busquedaDesignas.literal.fechaApertura"/>
			 &nbsp;  <siga:Idioma key="gratuita.busquedaDesignas.literal.entre"/>
		</td>
		<td>
			<siga:Fecha nombreCampo="fechaAperturaInicio" valorInicial="<%=fechaAperturaInicio%>" />
			&nbsp;<a onClick="return showCalendarGeneral(fechaAperturaInicio);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>
		<td class="labelText" width="10">	
			<siga:Idioma key="gratuita.busquedaDesignas.literal.y"/>
		</td>	
		<td>	
			<siga:Fecha nombreCampo="fechaAperturaFin" valorInicial="<%=fechaAperturaFin%>" />
			&nbsp;<a onClick="return showCalendarGeneral(fechaAperturaFin);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>	
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>
		</td>
		<td colspan="3">	
			<siga:ComboBD nombre="idTurno" tipo="turnosDesignacion" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="380"/>
		</td>	
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaDesignas.literal.tipoDesigna"/>
		</td>
		<td colspan="3">	
			<siga:ComboBD nombre="tipoDesigna" tipo="tipoDesignaColegio" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="250" />
		</td>	
		
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.editarDesigna.literal.estado"/>
		</td>
		<td >	
			<Select name="estado" class="boxCombo">
			<%if(estado!=null && !estado.equals("")){%>
				<%if(estado.equals("V")){%>			
					<option value=''   ></option>
					<option value='V' selected ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></option>
					<option value='F' ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></option>
					<option value='A' ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></option>
				<%}%>
				<%if(estado.equals("F")){%>
					<option value='' ></option>
					<option value='V' ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></option>
					<option value='F' selected><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></option>
					<option value='A' ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></option>
				<%}%>
				<%if(estado.equals("A")){%>
					<option value='' ></option>
					<option value='V' ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></option>
					<option value='F' ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></option>
					<option value='A' selected><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></option>
				<%}%>
			<%}else{%>	
				<option value='' ></option>
				<option value='V' selected><siga:Idioma key="gratuita.estadoDesignacion.activo"/></option>
				<option value='F' ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></option>
				<option value='A' ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></option>
			<%}%>
			</Select>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaDesignas.literal.actuacionesValidadas"/>
		</td>
		<td>	
			<Select name="actuacionesPendientes" class="boxCombo">
				<option value='' selected></option>
				<%if((actuacionesPendientes!=null)&&(actuacionesPendientes.equalsIgnoreCase("No"))){%>		
						<option value='No' selected><siga:Idioma key="general.no"/></option>
				<%}else{%>
					<option value='No' ><siga:Idioma key="general.no"/></option>
					<%}%>
				<%if((actuacionesPendientes!=null)&&(actuacionesPendientes.equalsIgnoreCase("Si"))){%>
						<option value='Si' selected><siga:Idioma key="general.yes"/></option>
				<%}else{%>	
					<option value='Si' ><siga:Idioma key="general.yes"/></option>
				<%}%>			
				<%if((actuacionesPendientes!=null)&&(actuacionesPendientes.equalsIgnoreCase("SinActuaciones"))){%>
					<option value='SinActuaciones' selected><siga:Idioma key="gratuita.busquedaDesignas.literal.sinActuaciones"/></option>
				<%}else{%>
					<option value='SinActuaciones' ><siga:Idioma key="gratuita.busquedaDesignas.literal.sinActuaciones"/></option>
				<% } %>
			</Select>
		</td>
		<td class="labelText" >
			<siga:Idioma key="gratuita.busquedaDesignas.literal.mostrarArt27"/>
		</td>
		<td>	
			<Select name="mostrarArt27" class="boxCombo">
				<option value='N'> <siga:Idioma key="general.no"/></option>
				<option value='S'> <siga:Idioma key="general.yes"/></option>
				<option value='T' selected> <siga:Idioma key="general.todas"/></option>
			</Select>
		</td>
	</tr>
	
	</table>
	</siga:ConjCampos>


	<table width="100%">
	<tr>
		<td>
		<siga:BusquedaPersona tipo="colegiado" titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="ncolegiado">
		</siga:BusquedaPersona> 
	</tr>
	</table>

	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.defensa" desplegable="true" oculto="true">
		<table  border="0" align="center" width="100%">
		<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.personaJG.literal.calidad" /></td>
				<td colspan="1">
						&nbsp;<siga:ComboBD nombre="calidad" tipo="ComboCalidades" ancho="140" clase="boxCombo" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=dato%>" elementoSel="<%=calidadSel%>" hijo="t" readonly="false"/>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado" /></td>
				<td class="labelText">
					<input type="text" name="codigoExtJuzgado" class="box" size="7" style="margin-top: 3px;" maxlength="10" onBlur="obtenerJuzgado();" />
				</td>
				<td class="labelText" colspan="3">
					<siga:ComboBD nombre="juzgado" tipo="comboJuzgados" ancho="450" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" hijo="t" elementoSel="<%=juzgado%>" parametro="<%=dato%>" />
				</td>
		</tr>
		<tr>
				<td class="labelText" >
					<siga:Idioma key="informes.cartaAsistencia.procedimiento"/>
				</td>
				<td class="labelText" >
					<html:text name="BuscarDesignasForm" property="procedimiento" size="17" maxlength="100" styleClass="box"  value="<%=procedimiento%>"></html:text>
				</td>
				<td class="labelText">	
					<siga:Idioma key="informes.cartaAsistencia.asunto"/>
				</td>	
				<td class="labelText">
					<html:text name="BuscarDesignasForm" property="asunto" size="15" maxlength="100" styleClass="box"  value="<%=asunto%>"></html:text>
				</td>
				<td class="labelText">	
					<siga:Idioma key="gratuita.mantAsistencias.literal.NIG"/>
				</td>	
				<td class="labelText" >
					<html:text name="BuscarDesignasForm" property="nig2" size="15" maxlength="50" styleClass="box" value="<%=nig%>"></html:text>
				</td>
		</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="pestana.justiciagratuitadesigna.actuaciones" desplegable="true" oculto="true">
		<table  border="0" align="center" width="100%">
		<tr>
			<td class="labelText" colspan="1">	
				<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo"/>
			</td>	
			<td class="labelText" colspan="2">
				<siga:ComboBD nombre="modulo" tipo="comboTiposProcedimientos" ancho="740" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=modulo%>" parametro="<%=dato%>" />           	   
			</td>
		</tr>
		<tr>
			<td class="labelText" colspan="1">
				<siga:Idioma key="gratuita.procedimientos.literal.acreditacion"/>
			</td>
			<td class="labelText" colspan="2">
				<siga:ComboBD nombre="acreditacion" tipo="acreditacionSCJS" ancho="740" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=acreditacion%>" obligatorio="false"  />           	   
			</td>
		</tr>
		<tr>
			<td class="labelText" >
				<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado"/>
			</td>
			<td class="labelText" >
				<input type="text" name="codigoExtJuzgadoActu" class="box" size="8"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado2();" />
				<siga:ComboBD nombre="juzgadoActu" tipo="comboJuzgados" ancho="680" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  hijo="t" elementoSel="<%=juzgadoActu%>" parametro="<%=dato%>"/>           	   
			</td>
		</tr>
		
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.defendido" desplegable="true" oculto="true">
	<table width="100%">
	<tr>
		<td class="labelText">	
			<siga:Idioma key="expedientes.auditoria.literal.nif"/>
		</td>	
		<td>
			<html:text name="BuscarDesignasForm" property="nif" size="10" maxlength="10" styleClass="box" value="<%=nif%>"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
		</td>
		<td>	
			<html:text name="BuscarDesignasForm" property="nombre" size="15" maxlength="100" styleClass="box" value="<%=nombre%>" ></html:text>
		</td>	
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>
		</td>
		<td >	
			<html:text name="BuscarDesignasForm" property="apellido1" size="15" maxlength="100" styleClass="box" value="<%=apellido1%>" ></html:text>
		</td>	
		<td class="labelText">	
			<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>
		</td>
		<td>
			<html:text name="BuscarDesignasForm" property="apellido2" size="15" maxlength="100" styleClass="box" value="<%=apellido2%>" ></html:text>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
	
	</html:form>
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea23">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>

	<html:form action = "/JGR_InformeJustificacionMasiva.do" method="POST" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
	</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	

		
		<!-- Funcion asociada a boton buscar -->
		function buscar(modo) 
		{
			if ( !validarObjetoAnio(document.getElementById("anio")) ){
				alert("<siga:Idioma key="fecha.error.anio"/>");
				return false;
			}

			if((validarFecha(document.forms[1].fechaAperturaInicio.value))&&
			   (validarFecha(document.forms[1].fechaAperturaFin.value))){
				sub();
				/*var codigo = document.forms[1].codigo.value;
				if ((document.forms[1].codigo.value !="") && isNaN(codigo)) 
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
				else {*/
					//document.forms[1].action = "<%=app%>/JGR_Designas.do";
					if (isNaN(document.forms[1].anio.value)) {
						fin();
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
						return false;
					}
					
					
					document.forms[1].target="resultado";
					if(modo)
						document.forms[1].modo.value = modo;
					else
						document.forms[1].modo.value = "buscarInicio";
					
					document.forms[1].submit();
				//}
			}else{
				setFocusFormularios();
			}
		}
			function seleccionarTodos(pagina) 
		{
				document.forms[1].seleccionarTodos.value = pagina;
				buscar('buscarPor');
				
		}		
			function buscarPaginador() 
		{
				/*var codigo = document.forms[1].codigo.value;
				if ((document.forms[1].codigo.value !="") && isNaN(codigo)) 
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
				else {*/
					//document.forms[1].action = "<%=app%>/JGR_Designas.do";
					document.forms[1].target="resultado";
					document.forms[1].modo.value = "buscarPor";
					document.forms[1].submit();
				//}
		}			
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[1].reset();
			document.forms[1].ncolegiado.value="";
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[1].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
			if (resultado!=null && resultado[0]=='MODIFICADO')
			{
				redireccionar();
			}
		}
		// Funcion que obtiene el juzgado buscando por codigo externo	
		 function obtenerJuzgado() 
			{ 
			  if (document.forms[1].codigoExtJuzgado.value!=""){
/*				if(document.forms[2].identificador.selectedIndex <= 0 ){
					alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1'/>");
					return;
				}else{	*/
				   document.MantenimientoJuzgadoForm.codigoExt.value=document.forms[1].codigoExtJuzgado.value;
				   document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";		
				   document.MantenimientoJuzgadoForm.submit();		
				   
				//}
			 }
			}
			
			
		// Funcion que obtiene el juzgado buscando por codigo externo	
		 function obtenerJuzgado2() 
			{ 
			  if (document.forms[1].codigoExtJuzgadoActu.value!=""){
/*				if(document.forms[2].identificador.selectedIndex <= 0 ){
					alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1'/>");
					return;
				}else{	*/
				   document.MantenimientoJuzgadoForm.codigoExt.value=document.forms[1].codigoExtJuzgadoActu.value;
				   document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgadoActu";		
				   document.MantenimientoJuzgadoForm.submit();		
				   
				//}
			 }
			}			
	//		
		function traspasoDatos(resultado){
		
			if(resultado[1] && resultado[1]!=""){
				if(resultado[1]=="juzgado")
					 seleccionComboSiga("juzgado",resultado[0]);
				else
					 seleccionComboSiga("juzgadoActu",resultado[0]);
			}	 
		}	
		
		function refrescarLocal()
		{
			buscar();
		}
		
		function redireccionar ()
		{ 
			document.forms[1].target="mainWorkArea";
			document.forms[1].modo.value="editar";
			document.forms[1].submit();
		}
		

		function informeJustificacion () 
		{
			document.InformeJustificacionMasivaForm.submit();
			
		}
		
		
	</script>


<!-- boton informe generico por resultados -->
<script>
function creaFormInterno() {
	var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea23'>");
	formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=usr.getLocation() %>'>"));
	formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
	formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='OFICI'>"));
	formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
	
	formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
	document.appendChild(formu);
	return formu;
}

// EJEMPLO: var dat="idAbono==25##idinstitucion==2040%%%idAbono==26##idinstitucion==2040%%%idAbono==27##idinstitucion==2040%%%idAbono==28##idinstitucion==2040";
function generarCarta() {
	
    var dat = "";
    var datos = document.frames.resultado.document.getElementsByName("datosCarta");
    if (datos.length==0) return;
	for(i=0; i<datos.length; i++)
	{
		dat += datos[i].value+"%%%";
	}
	if (dat.length>3)
		dat = dat.substring(0,dat.length-3);
	
	var formularioInformes = creaFormInterno();
	formularioInformes.datosInforme.value=dat;
	formularioInformes.submit();
}
function generarCarta() {
    if(window.frames.resultado.ObjArray){
	 window.frames.resultado.accionComunicar();
	}
	
} 	


</script>
<!--<input type="button" name="descarga" value="Descargar Factura Rectificativa" onclick="generaInformeGenericoSimple();" class="button">-->



	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<%if(usr.isLetrado()){%>	
		<siga:ConjBotonesBusqueda botones="C,B,IJ"  titulo="gratuita.busquedaDesignas.literal.titulo"/>
	<%}else{%>
		<siga:ConjBotonesBusqueda botones="C,B,N,IJ"  titulo="gratuita.busquedaDesignas.literal.titulo"/>
	<%}%>

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
					
	</iframe>

	<!-- FIN: BOTONES BUSQUEDA -->

	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea23" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->



</body>
</html>