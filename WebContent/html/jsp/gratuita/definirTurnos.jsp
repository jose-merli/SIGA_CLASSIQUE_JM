<!-- definirTurnos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.form.DefinirTurnosForm"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String entrada = "1";
	ses.setAttribute("entrada",entrada);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole = (Colegio)ses.getAttribute("colegio");
	String dato[] = {usr.getLocation()};
	String hayBusqueda ="0";
	try{
		if(((String)request.getSession().getAttribute("BUSQUEDAREALIZADA")).equalsIgnoreCase("SI")) hayBusqueda="1";
	}catch(Exception e){}
	Hashtable hash = null;
	request.getSession().setAttribute("pestanas","1");
	if((hayBusqueda).equals("1")){ hash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");}
	request.getSession().removeAttribute("pestanas");    	//borrar esta variable de sesion que habría quedado almacenada y ya no sirve
	request.getSession().removeAttribute("accionTurno");	//borrar esta variable de sesion que habría quedado almacenada y ya no sirve
	
	request.getSession().removeAttribute("CenBusquedaClientesTipo");
	DefinirTurnosForm miform = (DefinirTurnosForm) request.getAttribute("DefinirTurnosForm");
	String turnosBajaLogica = miform.getTurnosBajaLogica();
%>	

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	

	
	<!-- TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.maestroGuardias.literal.mantenimientoTurnos" 
		localizacion="gratuita.turnos.localizacion"/>
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
			sub();
			if(document.forms[0].turnosBajaLogica.value = "on"){
				document.forms[0].turnosBajaLogica.value = "S";
			}
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}		
		
		//Funcion asociada a boton limpiar
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		//Funcion asociada a boton Nuevo
		function nuevo() 
		{		
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].submit();
		}
</script>
		
<script language="JavaScript">
		function mostrarPartido(obj)
		{
			if (document.partidosJud) { 
				document.partidosJud.action= "<%=app%>/html/jsp/gratuita/partidosJudiciales.jsp";
				document.partidosJud.idinstitucion.value="<%=usr.getLocation()%>";
				document.partidosJud.idzona.value=document.forms[0].zona.value;
				document.partidosJud.idsubzona.value=obj.value;			
				
				document.partidosJud.submit();
			};
		}
</script>
		 
</head>
<body  onLoad="ajusteAlto('resultado');<%if((turnosBajaLogica).equalsIgnoreCase("S")){%>activarCheckBajaLogica();<%}%>;<%if((hayBusqueda).equals("1")){%>buscar();<%}%>" >


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>
		<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.titulo">
	<table width="100%" border=0 align="center">

		<html:form action="/DefinirTurnosAction.do" method="POST" target="resultado" enctype="multipart/form-data" onSubmit="return Buscar()">
			<input type="hidden" name="modal" value="">
			<input type="hidden" name="modo" value="">
			<input type="hidden" name="materia" value="">
			<input type="hidden" name="limpiarFilaSeleccionada" value="">
				
<%if (entrada.equalsIgnoreCase("1")){%>		<!--esto se quitara el dia en que se entre desde el menu-->
	
	<tr>
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/></td>
		<td><input name="abreviatura" type="text" class="box" size="15" maxlength="100" value="<%if((hayBusqueda).equals("1")){%><%=(String)hash.get("ABREVIATURA")%><%}%>" ></td>
		<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/></td>
		<td><input name="nombre" type="text" class="box" size="60" maxlength="1024" value="<%if((hayBusqueda).equals("1")){%><%=(String)hash.get("NOMBRE")%><%}%>"></td>
	</tr>
	
	<tr>
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/></td>
		<td>		
		<%
			String [] parametroMateria = null;
			ArrayList vArea = new ArrayList();
			try {
				String area = (String)hash.get("IDAREA");
				
				if (area!=null && !area.equals("-1")){					
					vArea.add(usr.getLocation()+","+(String)hash.get("IDAREA"));
					parametroMateria = new String[]{usr.getLocation(),area};
				}
				
				else {
					vArea.add("0");
					 
				}
			} catch (Exception e) {
				vArea.add("0");
			}
		%>									
			<siga:ComboBD nombre="area" tipo="area" clase="boxCombo"  parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vArea%>" obligatorio="false" accion="Hijo:materia"/>
			<script> document.forms[0].area[0].value=-1;</script>		
		</td>
		<td class="labelText"> 
		<%
			ArrayList vMateria = new ArrayList();
			try {
				String materia = (String)hash.get("IDMATERIA");
				if (materia != null && !materia.equals("-1")){
					vMateria.add ((String)hash.get("IDMATERIA"));
				}
				else {
					vMateria.add("0");
					 
				}
			} catch (Exception e) {
				vMateria.add("0");
			}
		%>			
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/>
		</td> 
		<td><siga:ComboBD nombre="materia" tipo="materia" parametro="<%=parametroMateria%>" elementoSel="<%=vMateria%>" clase="boxCombo"  hijo="t"/></td>
	</tr>
	
	<tr>
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/></td>						
		<td>
		<%
			String [] parametroSubZona = null;
			ArrayList vZona = new ArrayList();
			try {
				String zona = (String)hash.get("IDZONA");
				if (zona!=null && !zona.equals("-1")){
					vZona.add(usr.getLocation()+","+(String)hash.get("IDZONA"));
					parametroSubZona = new String[]{usr.getLocation(),zona};
				}
				else {
					vZona.add("0");
					 
				}
			} catch (Exception e) {
				vZona.add("0");
			}
		%>									
			<siga:ComboBD nombre="zona" tipo="zona" clase="boxCombo"  parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vZona%>" obligatorio="false" accion="Hijo:subzona"/>
			<script> document.forms[0].zona[0].value=-1;</script>			
		</td>
		<td class="labelText">
		<%
			ArrayList vSubzona = new ArrayList();
			try {
				String subZona = (String)hash.get("IDSUBZONA");
				if (subZona!=null && !subZona.equals("-1")){
					vSubzona.add((String)hash.get("IDSUBZONA"));
				}
				else {
					vSubzona.add("0");
					 
				}
			} catch (Exception e) {
				vSubzona.add("0");
				
			}
		%>	
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/>
		</td>
		<td><siga:ComboBD nombre="subzona" tipo="subzona"  parametro="<%=parametroSubZona%>" elementoSel="<%=vSubzona%>" clase="boxCombo"  hijo="t" accion="parent.mostrarPartido(this);"/></td>
	</tr>
	
	<tr>		
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/></td>
		<td colspan="3"><iframe ID="partidosjudiciales" name="partidosjudiciales"  src="<%=app%>/html/jsp/general/blank.jsp" WIDTH="600"  HEIGHT="19"  FRAMEBORDER="0"  MARGINWIDTH="0"  MARGINHEIGHT="1"  SCROLLING="NO"></iframe></td>
	</tr>
	
	<tr>
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidaPresupuestaria"/></td>
		<td>
		<%
			ArrayList vParPre = new ArrayList();
			try {
				if (hash.get("IDPARTIDAPRESUPUESTARIA")==null || hash.get("IDPARTIDAPRESUPUESTARIA").equals("-1")){
					vParPre.add("0");
				}
				else {
					vParPre.add((String)hash.get("IDPARTIDAPRESUPUESTARIA"));
				}
			} catch (Exception e) {
				vParPre.add("0");
			}
		%>
			<siga:ComboBD nombre="partidaPresupuestaria" tipo="partidaPresupuestaria" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vParPre%>" obligatorio="false" estilo="true" clase="boxCombo" parametro="<%=dato%>" />
			<script> document.forms[0].partidaPresupuestaria[0].value=-1;</script>
		</td>
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/></td>
		<td>
		<%
			ArrayList vGrupo = new ArrayList();
			try {
				if (hash.get("IDGRUPOFACTURACION")==null || hash.get("IDGRUPOFACTURACION").equals("-1")){
					vGrupo.add("0");
				}
				else {
					vGrupo.add((String)hash.get("IDGRUPOFACTURACION"));
				}
			} catch (Exception e) {
				vGrupo.add("0");
			}
		%>									
			<siga:ComboBD estilo="true" obligatorio="false" nombre="grupoFacturacion" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vGrupo%>" tipo="grupoFacturacion" clase="boxCombo"  parametro="<%=dato%>"/>
			<script> document.forms[0].grupoFacturacion[0].value=-1;</script>
		</td>
	</tr>
	<tr>		
		<td class="labelText"><siga:Idioma key="gratuita.definirTurnosIndex.literal.bajalogica"/></td>
		<td><html:checkbox property="turnosBajaLogica"  onclick="activarBajaLogica(this);" /></td>
	</tr>	
	<%}%>
	</html:form>
	</table>
		</siga:ConjCampos>	
	</td>
	</tr>
	</table>
	<siga:ConjBotonesBusqueda botones="N,B"/>
	
	<!-- IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<script language="JavaScript">

		function accionVolver() 
		{		
			history.back();
		}

		function accionCancelar() 
		{		
	
		}
		
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		function accionGuardar() 
		{			
		}
		
		function accionGuardarCerrar() 
		{		
	
		}
		
		function accionCerrar() 
		{		
	
		}
		
		function accionNuevo(){
			document.forms[0].modo.value="nuevo";
			document.forms[0].submit();
		}

		function activarBajaLogica(valorCheck){
			 if (valorCheck.checked){
				 document.forms[0].turnosBajaLogica.value = "S";			   
			 }else{
				 document.forms[0].turnosBajaLogica.value = "N";
			 }
		}

		function activarCheckBajaLogica(){
			document.forms[0].turnosBajaLogica.checked = true;
			document.forms[0].turnosBajaLogica.value = "S";			   
		}				
	</script>

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

<form name="partidosJud" action="" target="partidosjudiciales" method="POST">
<input type=hidden name="idinstitucion">
<input type=hidden name="idzona">
<input type=hidden name="idsubzona">
</form>

</body>

	
</html>