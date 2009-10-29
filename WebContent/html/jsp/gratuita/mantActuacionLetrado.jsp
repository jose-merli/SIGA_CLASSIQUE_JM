<!-- mantActuacionLetrado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.ScsActuacionAsistenciaBean" %>
<%@ page import="com.siga.gratuita.form.AsistenciasForm" %>
<%@ page import="com.siga.beans.ScsActuacionAsistCosteFijoBean"%>

<!-- JSP -->
<% 
	String app = request.getContextPath(); 
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	Properties src =(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	// Validamos si es una consulta o no.
	String modo 	= (String) request.getAttribute("MODO");
	String botones 	= (String) request.getAttribute("botones");
	
	

	// Arrays de los combos:
	ArrayList tipoDeclaracionSel = new ArrayList();
	ArrayList comisariaSel = new ArrayList();
	ArrayList juzgadoSel = new ArrayList();
	ArrayList tipoActuacionSel = new ArrayList();
	ArrayList tipoCosteActuacionSel = new ArrayList();

	//Claves de los combos:
	String idDeclaracion=null;
	String idJuzgado=null, idInstitucionJuzgado=null;
	String idComisaria=null, idInstitucionComisaria=null;
	
	// Obtenemos el resultado
	Vector vec = ((Vector) request.getAttribute("resultado"));
	Hashtable hash = new Hashtable();

	String idTipoAsistencia = "";
	String idTipoAsistenciaColegio = "";
	String AANIO="";
	String ANUMERO="";
	String PJGAPELLIDO2="";
	String PJGAPELLIDO1="";
	String PJGNIF="";
	String PJGNOMBRE="";
	String GTNOMBRE="";
	String TNOMBRE="";
	String CNCOLEGIADO="";
	String PNOMBRE="";
	String PAPELLIDOS2="";
	String PAPELLIDOS1="";
	String ACNUMERO="";
	String ACFECHA="";
	String ACLUGAR="";
	String ACANULACION="";
	String ACAEXTRAJUDICIAL="";
	String ACFJUSTIFICACION="";
	String ACDBREVE="";
	String ACOBSERVACIONES="";
	String ACDIADESPUES="";
	String ACNUMEROASUNTO="";
	String ACOJUSTIFICACION="";
	String ACIDACTUACION="";
	String validarJustificaciones = "";
	String actuacionValidada = "";
	String fechaAnulacion ="";
	String idTipoActuacion = "";
	String idCosteFijo = "";
	String descripcionCosteFijo = "";
	String IDTURNO = "";
	String facturada = "";
	String fechaDelDia = UtilidadesBDAdm.getFechaBD("");
	
	if(vec != null && vec.size()>0)
	{
		 hash =  (Hashtable) vec.get(0);
		 
	  
	
	 	// Datos del Juzgado seleccionado:
	 	idJuzgado = (String)hash.get(ScsActuacionAsistenciaBean.C_IDJUZGADO);
	 	idInstitucionJuzgado =  (String)hash.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO);
		if (idJuzgado!=null && idInstitucionJuzgado!=null)
			juzgadoSel.add(0,idJuzgado+","+idInstitucionJuzgado);	
		
	 	// Datos de la comisaria seleccionado:
	 	idComisaria = (String)hash.get(ScsActuacionAsistenciaBean.C_IDCOMISARIA);
	 	idInstitucionComisaria =  (String)hash.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA);
		if (idComisaria!=null && idInstitucionComisaria!=null)
			comisariaSel.add(0,idComisaria+","+idInstitucionComisaria);

	  	// Datos del Tipo Actuacion seleccionado:
	 	idTipoActuacion = (String)hash.get(ScsActuacionAsistenciaBean.C_IDTIPOACTUACION);
		if (idTipoActuacion!=null)
			tipoActuacionSel.add(0,idTipoActuacion);
	
	 	// Datos del tipo coste seleccionado:
		idCosteFijo = (String)hash.get(ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO);
		if (idCosteFijo!=null)
			tipoCosteActuacionSel.add(0,idCosteFijo);
		



		 
		 //Datos de la pagina:
		 AANIO=(String) hash.get("AANIO");
		 ANUMERO=(String) hash.get("ANUMERO");
		 PJGAPELLIDO2=(String) hash.get("PJGAPELLIDO2");
		 PJGAPELLIDO1=(String) hash.get("PJGAPELLIDO1");
		 PJGNIF=(String) hash.get("PJGNIF");
		 PJGNOMBRE=(String) hash.get("PJGNOMBRE");
		 GTNOMBRE=(String) hash.get("GTNOMBRE");
		 TNOMBRE=(String) hash.get("TNOMBRE");
		 CNCOLEGIADO=(String) hash.get("CNCOLEGIADO");
		 PNOMBRE=(String) hash.get("PNOMBRE");
		 PAPELLIDOS2=(String) hash.get("PAPELLIDOS2");
		 PAPELLIDOS1=(String) hash.get("PAPELLIDOS1");
		 ACNUMERO=(String) hash.get("ACNUMERO");
		 ACFECHA=(String) hash.get("ACFECHA");
		 ACLUGAR=(String) hash.get("ACLUGAR");
		 ACANULACION=(String) hash.get("ACANULACION");
		 ACAEXTRAJUDICIAL=(String) hash.get("ACAEXTRAJUDICIAL");
		 ACFJUSTIFICACION=(String) hash.get("ACFJUSTIFICACION");
		 ACDBREVE=(String) hash.get("ACDBREVE");
		 ACOBSERVACIONES=(String) hash.get("ACOBSERVACIONES");
		 ACDIADESPUES=(String) hash.get("ACDIADESPUES");
		 ACNUMEROASUNTO=(String) hash.get("ACNUMEROASUNTO");
		 ACOJUSTIFICACION=(String) hash.get("ACOJUSTIFICACION");
		 IDTURNO=(String) hash.get("IDTURNO");
		if(modo.equals("alta")) 
			ACIDACTUACION=(String) request.getAttribute("idactuacion");
		else
			ACIDACTUACION=(String) hash.get("ACIDACTUACION");
		
		validarJustificaciones = (String) hash.get("VALIDARJUSTIFICACIONES");
		actuacionValidada = hash.get("ACTUACIONVALIDADA") == null? "0":(String)hash.get("ACTUACIONVALIDADA");		
		fechaAnulacion = hash.get("FECHAANULACION") == null?"":(String)hash.get("FECHAANULACION");
		idTipoAsistencia = hash.get("IDTIPOASISTENCIA") == null?"":(String)hash.get("IDTIPOASISTENCIA");
		idTipoAsistenciaColegio = hash.get("IDTIPOASISTENCIACOLEGIO") == null?"":(String)hash.get("IDTIPOASISTENCIACOLEGIO");
		descripcionCosteFijo= hash.get("DESCRIPCIONCOSTEFIJO") == null?"":(String)hash.get("DESCRIPCIONCOSTEFIJO");
		
		facturada = hash.get("FACTURADA") == null? "0":(String)hash.get("FACTURADA");
	}

	//Construyo los parametros del combo de tipos:
	String[] parametroTipoActuacion = {usr.getLocation(), idTipoAsistenciaColegio};
	//Parametros para los combos:	
	String[] dato 	= {usr.getLocation(),IDTURNO};

	String estiloCombo=null, readOnlyCombo=null, estiloInput=null, comboSize=null;
	if (modo.equalsIgnoreCase("CONSULTA")) {
		estiloCombo="boxConsulta";
		readOnlyCombo = "true";
		estiloInput = "boxConsulta";
		comboSize="875";
	} else {
		estiloCombo="boxCombo";
		readOnlyCombo = "false";
		estiloInput = "box";
		comboSize="825";
	}
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<!-- validaciones struct -->
	<html:javascript formName="AsistenciasForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- fin validaciones struct -->
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.listadoActuacionesAsistencia.literal.titulo" 
		localizacion="gratuita.listadoActuacionesAsistencia.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>
		function recargarCombo() {
			<% if (!modo.equalsIgnoreCase("CONSULTA")) { %>
			document.forms[0].idTipoActuacion.onchange();
			<% } else { %>
			return;
			<% } %>
		}
		
		function refrescarLocal() {
			parent.refrescarLocal();
		}
		
		// Funcion que obtiene el juzgado buscando por codigo externo	
		 function obtenerJuzgado() 
			{ 
			  if (document.forms[0].codigoExtJuzgado.value!=""){
				   document.MantenimientoJuzgadoForm.codigoExt.value=document.forms[0].codigoExtJuzgado.value;
				   document.MantenimientoJuzgadoForm.submit();		
				   
				
			 }
			}
	//		
			
		function traspasoDatos(resultado){
		 //seleccionComboSiga("juzgado",resultado[0]);
		   document.forms[0].juzgado.value=resultado[0];
		}	
		// Funcion que obtiene la comisaria buscando por codigo externo	
			 function obtenerComisaria() 
			 { 
				  if (document.forms[0].codigoExtComisaria.value!=""){
					   document.MantenimientoComisariaForm.codigoExtBusqueda.value=document.forms[0].codigoExtComisaria.value;
					   document.MantenimientoComisariaForm.submit();		

				  }
			 }
		//			
		function traspasoDatosComisaria(resultado){
//		 seleccionComboSiga("comisaria",resultado[0]);
		 document.forms[0].comisaria.value=resultado[0];
		}			
	</script>
	
</head>

<body onload="recargarCombo()">
			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" heigth="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="gratuita.mantActuacion.literal.titulo"/>
					</td>
				</tr>
			</table>


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action = "/JGR_ActuacionAsistenciaLetrado.do" method="POST">
		<input type="hidden" name="acidactuacion" value="<%=ACIDACTUACION%>" />
		<input type="hidden" name="actuacionValidada" value="<%=actuacionValidada%>" />
		<input type="hidden" name="validarJustificacion" value="<%=validarJustificaciones%>" />
		<html:hidden name="AsistenciasForm" property="modo" value = "<%=modo%>"/>	
		<html:hidden name="AsistenciasForm" property="anio" />
		<html:hidden name="AsistenciasForm" property="numero" />
		<html:hidden name="AsistenciasForm" property="modoPestanha"/>
		<html:hidden name="AsistenciasForm" property="esFichaColegial" />
		<html:hidden name="AsistenciasForm" property="idTipoAsistencia" value="<%=idTipoAsistencia%>"/>
		<html:hidden name="AsistenciasForm" property="idTipoAsistenciaColegio" value="<%=idTipoAsistenciaColegio%>"/>

	<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.dasistencia">
	<table width="100%">
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.turno'/>
			</td>
			<td class="labelTextValor">	
				<%=TNOMBRE%>
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.guardia'/>
			</td>
			<td class="labelTextValor">	
				<%=GTNOMBRE%>
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.anio'/>
			</td>
			<td class="labelTextValor">	
				<%=AANIO%>
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.numero'/>
			</td>
			<td class="labelTextValor">	
				<%=ANUMERO%>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.asistido'/>
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.nif'/>
			</td>
			<td class="labelTextValor">	
				<%=PJGNIF%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.nombre'/>
			</td>
			<td class="labelTextValor">	
				<%=PJGNOMBRE%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.apellidos1'/>
			</td>
			<td class="labelTextValor">	
				<%=PJGAPELLIDO1%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.apellidos2'/>
			</td>
			<td class="labelTextValor">	
				<%=PJGAPELLIDO2%>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>
	<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.letrado">
	<table width="100%" border="0">
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.ncolegiado'/>
			</td>
			<td class="labelTextValor">	
				<%=CNCOLEGIADO%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.nombre'/>
			</td>
			<td class="labelTextValor">	
				<%=PNOMBRE%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.apellidos1'/>
			</td>
			<td class="labelTextValor">	
				<%=PAPELLIDOS1%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.apellidos2'/>
			</td>
			<td class="labelTextValor">	
				<%=PAPELLIDOS2%>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>
	<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.actuacion">
	<table width="100%" border="0">
		<tr>
			<%if(modo.equals("alta")) {%>
			<td class="labelText" width="150">	
				<siga:Idioma key='gratuita.mantActuacion.literal.nactuacion'/>&nbsp;(*)
			</td>
			<td>	
				<html:text name="AsistenciasForm" property="acnumero" size="10" maxlength="10" styleClass="box" value="<%=ACIDACTUACION%>"></html:text>
			</td>
			<%}else{%>
			<td class="labelText" width="150">	
				<siga:Idioma key='gratuita.mantActuacion.literal.nactuacion'/>&nbsp;(*)
			</td>
			<td>	
				<html:text name="AsistenciasForm" property="acnumero" size="10" maxlength="10" styleClass="boxconsulta" value="<%=ACIDACTUACION%>" readOnly="true"></html:text>
			</td>
			<%}%>
			<%
				String fecha = "";
				if(hash.get("ACFECHA")!=null && !hash.get("ACFECHA").equals("")) fecha= GstDate.getFormatedDateShort("",(String) hash.get("ACFECHA"));
			%>
			<%if(!modo.equals("consulta")) {%>				
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.fecha'/>&nbsp(*)
			</td>
			<td>	
				<html:text name="AsistenciasForm" property="acfecha" size="10" maxlength="10" styleClass="box" value="<%=fecha%>"  readOnly="true"></html:text><%if(!modo.equals("consulta")) {%>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(acfecha);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a><%}%>
			</td>
			<%}else{%>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.fecha'/>&nbsp(*)
			</td>
			<td class="labelTextValor">	
				<%=fecha%>
			</td>
			<%}%>
			<td class="labelText">
				<siga:Idioma key='gratuita.mantActuacion.literal.descripcion'/>
			</td>
			<td>	
				<html:text name="AsistenciasForm" property="acdbreve" size="30" styleClass="<%=estiloInput%>" value="<%=ACDBREVE%>"></html:text>
			</td>
		</tr>
		<tr>
			<%if(modo.equals("consulta")) {%>
			<td class="labelText" width="150">	
				<siga:Idioma key='gratuita.mantActuacion.literal.diadespues'/>
			</td>
			<td>	
				<INPUT NAME="acdiadespues" TYPE=CHECKBOX <% if (ACDIADESPUES!= null && ACDIADESPUES.equalsIgnoreCase("s")){%> checked<%}%> disabled>
			</td>
			<%}else{%>
			<td class="labelText">	
				<siga:Idioma key='gratuita.mantActuacion.literal.diadespues'/>
			</td>
			<td>	
				<INPUT NAME="acdiadespues" TYPE=CHECKBOX <% if (ACDIADESPUES!= null && ACDIADESPUES.equalsIgnoreCase("s")){%> checked<%}%>>
			</td>
			<%}%>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.mantActuacion.literal.tipoActuacion"/>&nbsp;(*)
			</td>
			<td colspan="5">
				<siga:ComboBD ancho="500" nombre="idTipoActuacion" tipo="comboTipoActuaciones" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorioSinTextoSeleccionar="true"  readOnly="<%=readOnlyCombo%>" elementoSel="<%=tipoActuacionSel%>" accion="Hijo:idCosteFijo" parametro="<%=parametroTipoActuacion%>" />
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.mantActuacion.literal.Coste"/>
			</td>
			<td colspan="5">
				<% if (!modo.equalsIgnoreCase("CONSULTA")) { %>
				<siga:ComboBD nombre="idCosteFijo" tipo="comboTipoCosteActuaciones" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" elementoSel="<%=tipoCosteActuacionSel%>" hijo="true" parametro="<%=parametroTipoActuacion%>" />
				<% } else { %>
				<input type="text" style="width:500px" name="descripcionCosteFijo" value="<%=descripcionCosteFijo%>" class="boxConsulta">
				<html:hidden name="AsistenciasForm" property="idCosteFijo" size="20" styleClass="boxConsulta" value="<%=idCosteFijo%>" />
				<% } %>				
			</td>
		</tr>
		<tr>
			<%if(modo.equals("consulta")) {%>
			<td class="labelText">
				<siga:Idioma key='gratuita.mantActuacion.literal.nasunto'/>
			</td>
			<td class="labelTextValor">	
				<%=ACNUMEROASUNTO%>
			</td>
			<%}else{%>
			<td class="labelText">
				<siga:Idioma key='gratuita.mantActuacion.literal.nasunto'/>
			</td>
			<td>	
				<html:text name="AsistenciasForm" property="acnumeroasunto" size="20" maxlength="10" styleClass="box" value="<%=ACNUMEROASUNTO%>"></html:text>
			</td>
			<%}%>
			<%if(modo.equals("consulta")) {%>
			<td class="labelText">
				<siga:Idioma key='gratuita.mantActuacion.literal.anulacion'/>
			</td>
			<td  class="labelText">	
				<INPUT NAME="acanulacion" TYPE=CHECKBOX <% if (ACANULACION!= null && !ACANULACION.equals("") && !ACANULACION.equals("0")){%> checked<%}%> disabled>
			</td>
			<%}else{%>
				<% if(!fechaAnulacion.equals("")) {%>
				<td  class="labelText">
					<siga:Idioma key='gratuita.mantActuacion.literal.anulacion'/>
				</td>
				<td >	
					<INPUT NAME="acanulacion" TYPE=CHECKBOX <% if (ACANULACION!= null && !ACANULACION.equals("") && !ACANULACION.equals("0")){%> checked<%}%> disabled>
				</td>
				<% } else {%>
				<td  class="labelText">
					<siga:Idioma key='gratuita.mantActuacion.literal.anulacion'/>
				</td>
				<td >	
					<INPUT NAME="acanulacion" TYPE=CHECKBOX <% if (ACANULACION!= null && !ACANULACION.equals("") && !ACANULACION.equals("0")){%> checked<%}%>>
				</td>
				<%}%>
			<%}%>
		</tr>
		
		<tr>
			<td class="labelText" >
				<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.comisaria"/>
			</td>
			<td colspan="5">
			 <%if(!modo.equals("consulta")){%>	
			    <input type="text" name="codigoExtComisaria" class="box" size="8"  style="margin-top:3px;" maxlength="10" onBlur="obtenerComisaria();" />
			 <%}%>			
				<siga:ComboBD ancho="200"  nombre="comisaria" tipo="comboComisariasTurno" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" parametro="<%=dato%>"  elementoSel="<%=comisariaSel%>"  />
			</td>
		</tr>
		<tr>
		 
		  <td class="labelText" >	
				 <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
				 <%if(!modo.equals("consulta")){%>
				    &nbsp;/&nbsp;<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
				 <%}%>
			  </td>	 
				
			<td colspan="5">	
			   <%if(!modo.equals("consulta")){%>
			   <input type="text" name="codigoExtJuzgado" class="box" size="8"  style="margin-top:0px;" maxlength="10" onBlur="obtenerJuzgado();" />
			   <%}%>
	
			  <siga:ComboBD ancho="<%=comboSize%>"  nombre="juzgado" tipo="comboJuzgadosTurno" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" parametro="<%=dato%>"  elementoSel="<%=juzgadoSel%>"  />
				
			</td>
		
		
			
		</tr>
		<tr>
			<%if(modo.equals("consulta")) {%>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.observaciones'/>
			</td>
			<td class="labelTextValor" colspan="5">	
				<%=ACOBSERVACIONES%>
			</td>
			<%}else{%>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantActuacion.literal.observaciones'/>
			</td>
			<td  colspan="5">	
				<html:textarea name="AsistenciasForm" property="acobservaciones" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="190" rows="3" style="overflow:auto" styleClass="boxCombo" value="<%=ACOBSERVACIONES%>"></html:textarea>
			</td>
			<%}%>
		</tr>
	</table>
	</siga:ConjCampos>
<% if ((!usr.isLetrado())) { // Agente %>	
	<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.justificacion" >
	<table width="100%" border="0">
		<tr>
<% 
			String fecha = "";
			if(hash.get("ACFJUSTIFICACION")!=null  && !hash.get("ACFJUSTIFICACION").equals("")){
				fecha = GstDate.getFormatedDateShort("",(String) hash.get("ACFJUSTIFICACION"));
			//else {
			//	fecha = UtilidadesBDAdm.getFechaBD("");
			}
%>
			<td class="labelText"  valign="top">	
				<siga:Idioma key='gratuita.mantActuacion.literal.fecha'/>
			</td>	
				<%if(!modo.equals("consulta")&&(!actuacionValidada.equals("1"))) {%>				
					<td>	
						<html:text name="AsistenciasForm" property="acfjustificacion" size="10" maxlength="10" styleClass="box" value="<%=fecha%>"  readOnly="true"></html:text><%if(!modo.equals("consulta")) {%>&nbsp;&nbsp;<a name="calendarioTd" style="visibility:visible" onClick="showCalendarGeneral(acfjustificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a><%}%>
					</td>
				<%}else{%>
					<td class="labelTextValor">	
						<html:text name="AsistenciasForm" property="acfjustificacion" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true" ></html:text><%if(!modo.equals("consulta")) {%>&nbsp;&nbsp;<a name="calendarioTd" style="visibility:hidden" onClick="showCalendarGeneral(acfjustificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a><%}%>
					</td>
				<%}%>	

			<td  style="padding-top: 5px;vertical-align: top;">
				<% if(!modo.equals("consulta")) {%>
					<%// if((validarJustificaciones != null) && (validarJustificaciones.equalsIgnoreCase("S"))) { %>
						<% if (!usr.isLetrado()) { // Agente %>
							<% if ((facturada != null) && (!facturada.equals("1"))) {%>
									<input type="button" alt="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>" id="idbutton" onClick="validarJustif();" class="button" value="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>">
							<%}%>
						<%} %>
					<%} %>						
					<td class="labelText">
								<%if(actuacionValidada.equals("1")) {%>	
								<input name="estadoActuacion" type="text" class="boxConsulta" value='<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>' readonly="true" >
								<% }else{ %>
								<input name="estadoActuacion" type="text" class="boxConsulta" value="" readonly="true" >
								<% } %>
							</td>
				<%//}     // if modo consulta %>
			</td>
				<%if(modo.equals("consulta")) {%>
				<td class="labelText" >	
					<siga:Idioma key='gratuita.mantActuacion.literal.observaciones'/>
				</td>	
				<td class="labelTextValor" >
					<%=ACOJUSTIFICACION%>
				</td>
				<%}else{%>
				<td class="labelText" >	
					<siga:Idioma key='gratuita.mantActuacion.literal.observaciones'/>
				</td>	
				<td  style="padding-top: 5px;vertical-align: top;">
					<html:textarea name="AsistenciasForm" property="acojustificacion" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="90" rows="3" style="overflow:auto" styleClass="boxCombo" value="<%=ACOJUSTIFICACION%>"></html:textarea>
				</td>
				<%}%>
		</tr>
	</table>
	</siga:ConjCampos>
	 <%} %>

</html:form>

 <html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
</html:form>	
	<html:form action = "/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarComisaria">
		<html:hidden property = "codigoExtBusqueda" value=""/>
	</html:form>		
<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}

			function accionGuardarCerrar() 
			{
				// validaciones struct
				sub();
				fecha = document.getElementById("acfjustificacion");
			 if (fecha){
				if(!((fecha.value == null)||(fecha.value == ""))){
				
				   
					document.forms[0].actuacionValidada.value="1";
					document.forms[0].estadoActuacion.value = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
					
				}else{
					document.forms[0].actuacionValidada.value="0";
				}
			 }else{
			   <%if((validarJustificaciones != null) && (validarJustificaciones.equalsIgnoreCase("S"))) {%>
			      document.forms[0].actuacionValidada.value="0";
			   <%}else{%>
			     document.forms[0].actuacionValidada.value="1";
			     
			   <%}%>   
			 }
				if(validateAsistenciasForm(document.AsistenciasForm))
				{
					document.forms[0].target = "submitArea";							
					<% if(modo.equals("alta")) { %>
						document.forms[0].modo.value = "insertar";
					<% } else { %>
						document.forms[0].modo.value = "modificar";
					<% } %>
					document.forms[0].submit();
				}else{
				
					fin();
					return false;
				
				}
			}
			//Asociada al boton Cerrar
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("MODICADO");
			}

			function validarJustif() {
			 
				if(document.forms[0].actuacionValidada.value=="1"){
					document.forms[0].acfjustificacion.className="box";
					document.forms[0].actuacionValidada.value="0";
					document.forms[0].estadoActuacion.value = "";
					document.getElementById("calendarioTd").style.visibility="visible";
					document.getElementById("acfjustificacion").value="";
					
					
				}else{
					document.forms[0].acfjustificacion.className="boxConsulta";					
					document.forms[0].actuacionValidada.value="1";
					document.getElementById("calendarioTd").style.visibility="hidden";
					document.forms[0].estadoActuacion.value = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
					if((document.forms[0].acfjustificacion.value==null)||
					   (document.forms[0].acfjustificacion.value=="")){					
					document.forms[0].acfjustificacion.value="<%=UtilidadesBDAdm.getFechaBD("")%>";
				}
				}
				
				//document.forms[0].checkActuacionValidacion.checked=true;
			}

	</script>

	<!-- INICIO: BOTONES BUSQUEDA -->	
				<siga:ConjBotonesAccion botones="<%=botones%>" />	
	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>