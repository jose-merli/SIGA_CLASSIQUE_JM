<!-- busquedaTurnosDisponibles.jsp -->
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
	
	//request.getSession().removeAttribute("CenBusquedaClientesTipo");
	//FilaExtElement[] elems=new FilaExtElement[1];
	//elems[0]=new FilaExtElement("solicitaralta","solicitaralta",SIGAConstants.ACCESS_FULL);	
%>	

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	<!-- TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.maestroGuardias.literal.mantenimientoTurnos" 
		localizacion="gratuita.turnos.localizacion"/>
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
			sub();
			document.forms[0].modo.value = "listadoTurnosDisponiblesBaja";
			document.forms[0].submit();
		}		
		
		//Funcion asociada a boton limpiar
		function limpiar() 
		{		
			document.forms[0].reset();
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
<body  onLoad="ajusteAlto('resultado');" >


	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.listarTurnosDisp.busqueda.tituloBaja"/>
				
		</td>
	</tr>    
	</table>


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>
		<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.titulo">
	<table width="100%" border=0 align="center">
	
	
	
		<html:form action = "/JGR_SolicitarBajaTurno.do" method="POST" target="resultado" enctype="multipart/form-data" onSubmit="return Buscar()" style="display:none">
<!--    <input type="hidden" name="origen" value="listarTurnosDisp">	-->
<!-- --><input type="hidden" name="modo"   value="listadoResultados">
		<input type="hidden" name="paso" value="turno"/>
		<input type="hidden" name="idInstitucion" />
		<input type="hidden" name="idPersona" />
		<input type="hidden" name="idTurno" />
		<input type="hidden" name="fechaSolicitud"/>
		<input type="hidden" name="observacionesSolicitud"/>
		
		<input type="hidden" name="actionModal" value="">

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>
			</td>
		<td>
			<input name="abreviatura" type="text" class="box" size="15" maxlength="100" value="<%if((hayBusqueda).equals("1")&&hash.get("ABREVIATURA")!=null){%><%=(String)hash.get("ABREVIATURA")%><%}%>" >
		</td>
		<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/> </td>
		<td>
			<input name="nombre" type="text" class="box" size="60" maxlength="1024" value="<%if((hayBusqueda).equals("1")&&hash.get("NOMBRE")!=null){%><%=(String)hash.get("NOMBRE")%><%}%>">
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>
		</td>
						
		<td>
			<%ArrayList vArea = new ArrayList();
			try {
				if (hash.get("IDAREA")==null || hash.get("IDAREA").equals("-1")){
					vArea.add("0");
				}
				else {
					vArea.add((String)hash.get("IDAREA")); 
				}
			} catch (Exception e) {
				vArea.add("0");
			} %>									
			<siga:ComboBD nombre="area" tipo="area" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" accion="Hijo:materia"/>
			<script> document.forms[0].area[0].value=-1;</script>
		</td>
		
		<td class="labelText"> 
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/>
		</td> 
		<td>      
			<siga:ComboBD nombre="materia" tipo="materia" clase="boxCombo"  hijo="t"/>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/>
		</td>
						
		<td>
			<%ArrayList vZona = new ArrayList();
			try {
				if (hash.get("IDZONA")==null || hash.get("IDZONA").equals("-1")){
					vZona.add("0");
				}
				else {
					vZona.add(usr.getLocation()+","+(String)hash.get("IDZONA")); 
				}
			} catch (Exception e) {
				vZona.add("0");
			}%>									
			<siga:ComboBD nombre="zona" tipo="zona" clase="boxCombo"  parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" accion="Hijo:subzona"/>			
		</td>
		
		<td class="labelText">
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/>
		</td>
		<td>
			<siga:ComboBD nombre="subzona"  accion="parent.mostrarPartido(this);" tipo="subzona" clase="boxCombo" hijo="t"/>
		</td>
	</tr>


	</html:form>
	</table>
		</siga:ConjCampos>	
	</td>
	</tr>
	</table>
	<siga:ConjBotonesBusqueda botones="B"/>
	
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

</body>
	
</html>
