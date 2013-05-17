<!-- asisRespAsistencias.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.AsistenciasForm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	Properties src = (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	String idPersona="", nif="", nombre="", apellido1="", apellido2="", direccion="", cp="", fechaNacimiento="", idRegimenConyugal="", modo="";	
	String pais="", provincia="", poblacion="", ecivil="";
	ArrayList selPais 		= new ArrayList();
	ArrayList idProvincia 	= new ArrayList();
	ArrayList idPoblacion 	= new ArrayList();
	ArrayList idEstadoCivil = new ArrayList();

	String dato[] = {(String)usr.getLocation()};
	String accion = (String) request.getSession().getAttribute("accion");
	String titulo = (String)request.getAttribute("TITULO");
	String localizacion = (String)request.getAttribute("LOCALIZACION");
	String action = (String)request.getAttribute("ACTION");
	String anio = (String)request.getAttribute("anio");
	String numero = (String)request.getAttribute("numero");
	ScsPersonaJGBean bean = (ScsPersonaJGBean) request.getAttribute("resultado");
	
	// Si no existe el beneficiario miHash no tendrá nada por eso hay que meterlo en un try-catch
	if (bean != null) {
			idPersona 	= String.valueOf(bean.getIdPersona());
			nif 		= bean.getNif();
			nombre 		= bean.getNombre();
			apellido1 	= bean.getApellido1();
			apellido2 	= bean.getApellido2();
			direccion 	= bean.getDireccion();
			cp 			= bean.getCodigoPostal();
			pais 		= String.valueOf(bean.getIdPais());
			provincia 	= String.valueOf(bean.getIdProvincia());
			poblacion	= String.valueOf(bean.getIdPoblacion());
			ecivil		= String.valueOf(bean.getIdEstadoCivil());
			fechaNacimiento 	= GstDate.getFormatedDateShort(usr.getLanguage(),bean.getFechaNacimiento());
			idRegimenConyugal 	= bean.getRegimenConyugal();
			modo	= "Modificar";
	} else
			modo  = "Insertar";

	selPais.add(pais);
	idProvincia.add(provincia);
	idPoblacion.add(poblacion);
	idEstadoCivil.add(ecivil);
	
	// Obtenemos la descripcion de pais,provincia,problacion,estado civil
	ScsAsistenciasAdm scsAsistenciaAdm = new ScsAsistenciasAdm(usr);
	String PAISDESC = "";
	String PROVINCIADESC = "";
	String POBLACIONDESC = "";
	String ESTADODESC = "";
	
	if (pais!=null && !pais.equals("")) {
		String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidiomaSimple("NOMBRE", usr.getLanguage()) + " DESCRIPCION FROM CEN_PAIS WHERE IDPAIS = '"+pais+"'";
		Vector vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if (vect!=null && vect.size()>0)
			PAISDESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
		sql = " SELECT NOMBRE DESCRIPCION FROM CEN_POBLACIONES 	WHERE IDPOBLACION = '"+poblacion+"'";
		vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if (vect!=null && vect.size()>0)
			POBLACIONDESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
		sql = " SELECT NOMBRE DESCRIPCION FROM CEN_PROVINCIAS 	WHERE IDPROVINCIA = '"+provincia+"'";
		vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if (vect!=null && vect.size()>0)
			PROVINCIADESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
		sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma("DESCRIPCION", usr.getLanguage()) + " FROM CEN_ESTADOCIVIL WHERE IDESTADOCIVIL = "+ecivil;
		vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if (vect!=null && vect.size()>0)
			ESTADODESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
	}
	
	//Para el refresco:
	ses.setAttribute("anioRes",anio);
	ses.setAttribute("numeroRes",numero);
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
	<!-- validaciones struct -->
	<html:javascript formName="Asistencias1Form" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<siga:Titulo 
		titulo="<%=titulo%>" 
		localizacion="<%=localizacion%>"/>

	<script>
			function fLoad() {
				<% if(accion != null && accion.equalsIgnoreCase("modificar")) { %>
						var tmp1 = document.getElementsByName("idProvincia");
						var tmp2 = tmp1[0]; 
						tmp2.onchange();
				<% } %>
			}
						
			// Para almacenar la poblacion seleccionada y cargarla despues de introducir un nuevo nif
			var poblacionSeleccionada;
			
			function traspasoDatos(resultado,bNuevo) {
				document.forms[0].nuevo.value = bNuevo;				
				document.forms[0].idPersona.value = resultado[0]; 
				// RGG 15-03-2006 para no pisar el dni
				if (trim(resultado[1])!="") {
					document.forms[0].nif.value = resultado[1]; 
				}
				document.forms[0].nombre.value = resultado[2]; 
				document.forms[0].apellido1.value = resultado[3]; 
				document.forms[0].apellido2.value = resultado[4]; 
				document.forms[0].direccion.value = resultado[5]; 
				document.forms[0].codigoPostal.value = resultado[6]; 

				//Estado Civil:
				document.forms[0].idEstadoCivil.value = resultado[10];
				
				//Regimen Conyugal:
				document.forms[0].regimenConyugal.value = resultado[11];				
				
				//Fecha Nacimiento:
				document.forms[0].fechaNacimiento.value = resultado[12]; 
								
				//Pais:
				document.forms[0].idPais.value = resultado[7];
				document.forms[0].idPais.onchange();

				//Provincia:
				document.forms[0].idProvincia.value = resultado[8];
				document.forms[0].idProvincia.onchange();

				//Poblacion:				
				poblacionSeleccionada = resultado[9];
				//A los 500 milisegundos (tiempo para recargar el combo provincias) se selecciona la poblacion:
				window.setTimeout("recargarComboHijo()",100,"Javascript");
								
				if (resultado!=null && resultado[0]!="") {
					var p1 = document.getElementById("resultado");
					p1.src = "JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona="+resultado[0]+"&idInstitucion=<%=usr.getLocation()%>";					
					document.forms[0].target="mainPestanas";				
					document.forms[0].modo.value="editar";
				}
			}
			
			function recargarComboHijo() {
				var acceso = idPoblacionFrame.document.getElementsByTagName("select");
				acceso[0].value = poblacionSeleccionada;
				document.forms[0].idPoblacion.value = poblacionSeleccionada;
			}
			
			function obtenerNif() {
				var nif = (document.forms[0].nif.value).toUpperCase();
				var numero = nif.substr(0,nif.length-1);
				
				if (letraDNI(numero)!=nif) {
					alert("<siga:Idioma key='gratuita.asisRespAsistencias.literal.alert1'/>");
					return false;
				}		
		
				//LMSP En lugar de abrir la ventana modal, se manda al frame oculto, y éste se encarga de todo :)
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].target="submitArea";
				document.forms[0].submit();
			}
					
			function addOption(combo,text, value) {
				var comboBox = document.getElementById(combo);
				var newOption = new Option(text, value);
				comboBox.options[comboBox.options.length] = newOption;
			}
			
			function refrescarLocal() {
				parent.buscar();
			}
	
			function letraDNI(num) 	{
				let = 'TRWAGMYFPDXBNJZSQVHLCKE';
				if (isNaN(num) == false) {
					var posicion = num % 23;
					var letra = let.substring(posicion,posicion+1) ;
			    	numlet=num + letra;
			    	return numlet ;
				} else {
					alert("<siga:Idioma key='gratuita.asisRespAsistencias.literal.alert1'/>");
				}
			}
	</script>

</head>

<body class="tablaCentralCampos" onLoad="ajusteAlto('resultado');fLoad();">
	
	<!-- CAMPOS DEL REGISTRO -->
	<table align="center"  width="100%" height="300" class="tablaCentralCampos">
	
	<html:form action="<%=action%>" method="POST" target="mainPestanas">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idProfesion" value = ""/>
		<html:hidden property = "idPersona" value 		= "<%=idPersona%>"/>
		<html:hidden property = "idInstitucion" value 	= "<%=usr.getLocation()%>"/>
		<html:hidden property = "usuMod" value 			= "<%=usr.getUserName()%>"/>
		<html:hidden property = "fechaMod" value		= "sysdate"/>
		<html:hidden property = "actionActivo" value	= "<%=action%>"/>
		<html:hidden property = "titulo" value	= "<%=titulo%>"/>
		<html:hidden property = "localizacion" value = "<%=localizacion%>"/>
		<html:hidden property = "anio" value = "<%=anio%>"/>
		<html:hidden property = "numero" value = "<%=numero%>"/>
		<html:hidden property = "nuevo"/>
	<tr>				
	<td width="100%" align="center">

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="<%=titulo%>">

	<table  align="center">
	
	<tr width="100%">
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.nif"/>:		
		</td>
		<td class="labelText"  colspan="7">
		<% if(accion != null && accion.equalsIgnoreCase("modificar")) { %>
			<html:text name="AsistenciasForm" property="nif" size="10" maxlength="20" styleClass="box"  value="<%=nif%>" onBlur="obtenerNif();"></html:text>
		<% } else { %>
			<%=nif%>
		<% } %>
		</td>
		</tr>
		<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.nombre"/>:
		</td>
		<td class="labelText">
		<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
			<html:text name="AsistenciasForm" property="nombre" size="15" maxlength="100" styleClass="box"  value="<%=nombre%>"></html:text>
		<% } else { %>
			<%=nombre%>
		<% } %>
		</td>
		<td class="labelText">	
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.apellido1"/>:
		</td>		
		<td class="labelText">
		<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
			<html:text name="AsistenciasForm" property="apellido1" size="15" maxlength="100" styleClass="box"  value="<%=apellido1%>"></html:text>
		<% } else { %>
			<%=apellido1%>
		<% } %>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.apellido2"/>:
		</td>
		<td class="labelText">
		<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
			<html:text name="AsistenciasForm" property="apellido2" size="15" maxlength="100" styleClass="box"  value="<%=apellido2%>"></html:text>
		<% } else { %>
			<%=apellido2%>
		<% } %>
		</td>	
		<td class="labelText">
		</td>
	</tr>	
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.cp"/>:
		</td>
		<td class="labelText">
		<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
			<html:text name="AsistenciasForm" property="codigoPostal" size="10" maxlength="20" styleClass="box"  value="<%=cp%>"></html:text>
		<% } else { %>
			<%=cp%>
		<% } %>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.direccion"/>:
		</td>
		<td class="labelText" colspan="5">
		<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
			<html:text name="AsistenciasForm" property="direccion" size="50" maxlength="100" styleClass="box"  value="<%=direccion%>"></html:text>
		<% } else { %>
			<%=direccion%>
		<% } %>
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.nacionalidad"/>&nbsp;(*):
		</td>
		<td class="labelText" colspan="2">
		<% if(accion != null && accion.equalsIgnoreCase("modificar")) { %>
				<%--siga:ComboBD nombre="idPais" tipo="pais" estilo="true" clase="boxCombo" obligatorio="false" accion="Hijo:idProvincia"  pestana="t" elementoSel="<%=selPais%>" /--%> 
				<siga:ComboBD nombre="idPais" tipo="pais" estilo="true" clase="boxCombo" obligatorio="false" accion="" pestana="t" elementoSel="<%=selPais%>" /> 
		<% } else { %>
			<%=PAISDESC%>
		<% } %>
		</td>
		<td class="labelText" colspan="5">
		</td>
	</tr>

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.provincia" />&nbsp;(*):
		</td>
		<td class="labelText" colspan="2">
		<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
				<%--siga:ComboBD nombre="idProvincia" 	tipo="provinciapais" clase="boxCombo" hijo="t" pestana="t" accion="Hijo:idPoblacion" elementoSel="<%=idProvincia%>" /--%> 
				<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="boxCombo" obligatorio="false" accion="Hijo:idPoblacion" pestana="t" elementoSel="<%=idProvincia%>" />
		<% } else { %>
			<%=PROVINCIADESC%>
		<% } %>
		</td>
		<td class="labelText" colspan="5">	
		</td>
	</tr>

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.poblacion"/>&nbsp;(*):
		</td>
		<td class="labelText" colspan="5">
	
		<% if(accion != null && accion.equalsIgnoreCase("modificar")) { %>
				<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="false" accion="" hijo="t" pestana="t" elementoSel="<%=idPoblacion%>" />
		<% } else { %>
			<%=POBLACIONDESC%>
		<% } %>
		</td>
		<td class="labelText" colspan="5">	
		</td>
	</tr>

	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.aisRespAsistencias.literal.fechaNacimiento"/>:
	</td>
	<td class="labelText">
		<%if(accion != null && accion.equalsIgnoreCase("modificar")){%>
			<siga:Datepicker nombreCampo="fechaNacimiento" valorInicial="<%=fechaNacimiento%>"></siga:Datepicker>
		<%}else{%>
			<%=fechaNacimiento%>
		<%}%>
	</td>
	<td class="labelText" colspan="5">		
	</td>		
	</tr>

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.estadoCivil"/>:
		</td>
		<td class="labelText">
			<% if(accion != null && accion.equalsIgnoreCase("modificar")) { %>
				<siga:ComboBD nombre="idEstadoCivil" tipo="estadoCivil" estilo="true" clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoCivil %>" pestana="t"/>
			<% } else { %>
				<%=ESTADODESC%>
			<% } %>
		</td>
		<td class="labelText" colspan="5">		
		</td>		
	</tr>

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.regimenConyugal"/>:
		</td>
		<td class="labelText">
			<% if (accion != null && accion.equalsIgnoreCase("modificar")) { %>
				<select name="regimenConyugal" class="boxCombo">
					<option value="G" <%if(idRegimenConyugal.equals("G")){%>selected<%}%>><siga:Idioma key="gratuita.aisRespAsistencias.literal.gananciales"/></option>
					<option value="S" <%if(idRegimenConyugal.equals("S")){%>selected<%}%>><siga:Idioma key="gratuita.aisRespAsistencias.literal.sbienes"/></option>
				</select>
			<% } else { %>
				<%if(idRegimenConyugal.equals("G"))%><siga:Idioma key="gratuita.aisRespAsistencias.literal.gananciales"/>
				<%if(idRegimenConyugal.equals("S"))%><siga:Idioma key="gratuita.aisRespAsistencias.literal.sbienes"/>
			<% } %>
		</td>
		<td class="labelText" colspan="5">
		</td>		
	</tr>		
	<tr><td class="labelText" colspan="7">&nbsp;</td>
	<tr><td class="labelText" colspan="7">&nbsp;</td>
	</tr>
	</table>
	</siga:ConjCampos>
	</td>
	</tr>	
	</html:form>
</table>
<!-- FIN: SUBMIT AREA -->	

<% if(accion != null && accion.equalsIgnoreCase("modificar")) { %>
		<siga:ConjBotonesAccion botones="V,G,R" modo="julio"  clase="botonesDetalle" />
<% } %>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- INICIO: SCRIPTS BOTONES -->

	<iframe align="center" src="JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=<%=idPersona%>&idInstitucion=<%=usr.getLocation()%>"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Volver -->
		function accionVolver() {
			document.forms[0].target="mainWorkArea";
			document.forms[0].action = "/SIGA"+"/JGR_Asistencia.do";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 	{		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() {		
			sub();		
			var nif = (document.forms[0].nif.value).toUpperCase();
			var numero = nif.substr(0,nif.length-1);
			if(letraDNI(numero)!=nif) {
				alert("<siga:Idioma key='gratuita.asisRespAsistencias.literal.alert2'/>");
				fin();
				return false;
			}		
			if(validateAsistencias1Form(document.AsistenciasForm)) {
				// Validamos pais, provincia y poblacion
				document.forms[0].target="submitArea";
				document.forms[0].modo.value="<%=modo%>";
				document.forms[0].submit();
			}else{
				fin();
				return false;
			}
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	</body>
	
</html>