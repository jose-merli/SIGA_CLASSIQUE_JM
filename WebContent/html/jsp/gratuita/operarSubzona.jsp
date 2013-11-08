<!DOCTYPE html>
<html>
<head>
<!-- operarSubzona.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
<%@ page import="com.siga.beans.CenPartidoJudicialAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Hashtable miHash = (Hashtable)ses.getAttribute("elegido");
	String accion = (String)ses.getAttribute("accion");
	ses.removeAttribute("accion");
	ses.removeAttribute("elegido");
	
		
	String zona = (String)miHash.get(ScsSubzonaBean.C_IDZONA);
	String institucion = (String)miHash.get(ScsSubzonaBean.C_IDINSTITUCION);
	String subzona = (String)miHash.get(ScsSubzonaBean.C_IDSUBZONA);
    String nombre = (String)miHash.get(ScsSubzonaBean.C_NOMBRE);
    //String municipios = (String)miHash.get(ScsSubzonaBean.C_MUNICIPIOS);
	
	String dato[] = {(String)usr.getLocation(),(String)usr.getLocation(),subzona,zona};

	ArrayList vInt = new ArrayList();
	String idpartido = "";
	try {
		Vector partidosSel = (Vector) request.getSession().getAttribute("resultadoPJ");
		for (int i = 0; i< partidosSel.size();i++) {
			Hashtable aux = (Hashtable) partidosSel.get(i);
			vInt.add((String)aux.get("IDPARTIDO"));
		}
/*	
		if (miHash.get(ScsSubzonaBean.C_IDPARTIDO)==null || ((String)miHash.get(ScsSubzonaBean.C_IDPARTIDO)).equalsIgnoreCase("NULL")) {			
			vInt.add("0");
			idpartido=null;
		}
		else {
			vInt.add((String)miHash.get(ScsSubzonaBean.C_IDPARTIDO));	
			idpartido = (String)miHash.get(ScsSubzonaBean.C_IDPARTIDO);
		}
*/	
	} catch (Exception e) {
		vInt.add("0");
		idpartido=null;
	}
	
	String nombrePartidoJudicial = "";
	try {
		CenPartidoJudicialAdm partidoJudicialAdm = new CenPartidoJudicialAdm(usr);
		Vector resultado = new Vector();
		if (idpartido!=null) {
			resultado = partidoJudicialAdm.selectGenerico(partidoJudicialAdm.getNombrePartido(idpartido));
			nombrePartidoJudicial = (String)(((Hashtable)resultado.elementAt(0)).get("NOMBRE"));
		} else {
			nombrePartidoJudicial ="";
		}
	}
	catch (Exception e){}

%>

<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:Idioma key="gratuita.operarSubzona.literal.consultarSubzona"/>
		<% } else {%>
			<siga:Idioma key="gratuita.operarSubzona.literal.modificarSubzona"/>
		<%}%>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_DefinirZonasSubzonas.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "accion" value = "subzona"/>
	<html:hidden property = "idZona" value = "<%=zona%>"/>
	<html:hidden property = "idInstitucionSubzona" value ="<%=institucion%>"/>
	<html:hidden property = "idSubzona" value ="<%=subzona%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	
	<tr>		
	<td>			
		<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaZonas.literal.subzona">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaZonas.literal.subzona"/>&nbsp;(*)
		</td>
		<td>
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirZonasSubzonasForm" property="nombreSubzona" size="30" styleClass="boxConsulta" readonly="true" value="<%=nombre%>"></html:text>
	<% } else {%>
		<html:text name="DefinirZonasSubzonasForm" property="nombreSubzona" size="30" styleClass="box" value="<%=nombre%>"></html:text>
	<%}%>
		</td>	
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaZonas.literal.partidoJudicial"/>&nbsp;(*)
		</td>				
		<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ComboBD nombre="partidosJudiciales" tipo="partidoJudicial" clase="boxConsulta" obligatorioSinTextoSeleccionar="true" seleccionMultiple="true" obligatorio="true" elementoSel="<%=vInt%>" parametro="<%=dato%>" readonly="true" filasMostrar="5"/>
		<% } else {%>
			<siga:ComboBD nombre="partidosJudiciales" tipo="partidoJudicial" clase="boxCombo"  obligatorioSinTextoSeleccionar="true" seleccionMultiple="true" obligatorio="true" elementoSel="<%=vInt%>" parametro="<%=dato%>"  filasMostrar="5"/>
		<%}%>
		</td>	
	</tr>
	</table>

	</siga:ConjCampos>

	
	</td>
	</tr>
	</html:form>
	</table>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ConjBotonesAccion botones="C" modal="P"  />
	<%} else {%>
		<siga:ConjBotonesAccion botones="G,R,C" modal="P"  />
	<%}%>
	<!-- FIN: BOTONES REGISTRO -->

	</div>
	<!-- INICIO: SCRIPTS BOTONES -->	
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{	
			sub();
			var nombre = document.forms[0].nombreSubzona.value;
			//var municipios = document.forms[0].municipiosSubzona.value;
			if (document.forms[0].partidosJudiciales.value == ""){
			  alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoPartido"/>');
			  fin();
			  return false;
			  
			}
			else if ((nombre.length <= 60) && (nombre != "")) {
				//if (municipios.length<=4000) {
					document.forms[0].submit();			
					window.top.returnValue="MODIFICADO";	
				//}
				//else alert('<siga:Idioma key="gratuita.zonasSubzonas.message.longitudMunicipios"/>');
			}
			else if (nombre == ""){
				 alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
				 fin();
			  	 return false;
			}
			else{
				 alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
				 fin();
			  	 return false;
			}	
		} 
				
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>

</html>
