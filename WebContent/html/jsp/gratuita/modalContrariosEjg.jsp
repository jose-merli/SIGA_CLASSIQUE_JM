<!DOCTYPE html>
<html>
<head>
<!-- modalContrariosAsistencia.jsp -->
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
<%@ page import="com.siga.gratuita.form.ContrariosEjgForm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	
	String idPersonaJG="", nif="", nombre="", apellido1="", apellido2="", direccion="", cp="", fechaNacimiento="", idRegimenConyugal="", modo="";	
	String pais="", provincia="", poblacion="", ecivil="";
	
	ArrayList selPais 		= new ArrayList();
	ArrayList idProvincia 	= new ArrayList();
	ArrayList idPoblacion 	= new ArrayList();
	ArrayList idEstadoCivil = new ArrayList();

	String accion = (String)request.getAttribute("accion");
	ScsPersonaJGBean bean = (ScsPersonaJGBean) request.getAttribute("beanPersonaJG");

	String estiloCaja="boxConsulta", topBotones="";
	boolean lectura = false;

	//Estilo:
	if (accion!=null && accion.equalsIgnoreCase("VER")) {
			estiloCaja = "boxConsulta";
			lectura = true;
			topBotones="393";
	} else {
			estiloCaja = "box";
			lectura = false;
			topBotones="397";
	}
	
	// Recupero los datos si estamos en edicion o consulta:
	if (accion!=null && !accion.equalsIgnoreCase("NUEVO")) {
			idPersonaJG 	= String.valueOf(bean.getIdPersona());
			nif 		    = bean.getNif();
			nombre 	= bean.getNombre();
			apellido1 	= bean.getApellido1();
			apellido2 	= bean.getApellido2();
			direccion 	= bean.getDireccion();
			cp 			= bean.getCodigoPostal();
			pais 		    = String.valueOf(bean.getIdPais());
			provincia 	= String.valueOf(bean.getIdProvincia());
			poblacion	= String.valueOf(bean.getIdPoblacion());
			ecivil		    = String.valueOf(bean.getIdEstadoCivil());
			fechaNacimiento 	= GstDate.getFormatedDateShort(usr.getLanguage(),bean.getFechaNacimiento());
			idRegimenConyugal 	= bean.getRegimenConyugal();

			//Combos:
			selPais.add(pais);
			idProvincia.add(provincia);
			idPoblacion.add(poblacion);
			idEstadoCivil.add(ecivil);
			
			modo = "Modificar";
	} else {
			modo  = "Insertar";
	}

	// Obtenemos la descripcion de pais,provincia,problacion,estado civil
	ScsContrariosEJGAdm scsAsistenciaAdm = new ScsContrariosEJGAdm(usr);
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
		sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma("DESCRIPCION", usr.getLanguage()) + " FROM CEN_ESTADOCIVIL 	WHERE IDESTADOCIVIL = "+ecivil;
		vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if (vect!=null && vect.size()>0)
			ESTADODESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
	}
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- validaciones struct -->
	<html:javascript formName="ContrariosEjgForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- fin validaciones struct -->


	<script>
			function recargarCombo() {
				<% if (accion!=null && accion.equalsIgnoreCase("editar")){%>
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
				document.forms[0].nif.value = resultado[1]; 
				document.forms[0].nombre.value = resultado[2]; 
				document.forms[0].apellido1.value = resultado[3]; 
				document.forms[0].apellido2.value = resultado[4]; 
				document.forms[0].direccion.value = resultado[5]; 
				document.forms[0].codigoPostal.value = resultado[6]; 
				document.forms[0].idEstadoCivil.value = resultado[10];
				document.forms[0].regimenConyugal.value = resultado[11];				
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
				window.setTimeout("recargarComboHijo()",500,"Javascript");				
							
				//Actualizo los telefonos de esta personaJG:
				if (resultado!=null && resultado[0]!="") {
						var p1 = document.getElementById("resultado");
						p1.src = "JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona="+resultado[0]+"&idInstitucion=<%=usr.getLocation()%>";
						document.forms[0].target="_self";				
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
					
			function refrescarLocal() {
				parent.buscarContrarios();
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

<body onLoad="recargarCombo();">

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				   <siga:Idioma key="gratuita.mantAsistencias.literal.tituloCO"/>
			</td>
		</tr>
	</table>

<div id="camposRegistro" class="posicionModalGrande" align="center">

	<html:form action="JGR_ContrariosEjg.do" method="POST" target="mainPestanas">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idProfesion" value = ""/>
		<html:hidden property = "idPersona" value 	= "<%=idPersonaJG%>"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "anio" />
		<html:hidden property = "numero" />
		<html:hidden property = "nuevo"/>
	
	<!-- CAMPOS DEL REGISTRO -->
	<table class="tablaCentralCamposGrande" align="center" border="0">
	<tr>
	<td>

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.tituloCO">
	<table class="tablaCampos" align="center">	
	<tr width="100%">
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.nif"/>:		
		</td>
		<td colspan="5">
		<% if (accion!=null && accion.equalsIgnoreCase("nuevo")) { %>
			<html:text name="ContrariosEjgForm" property="nif" size="10" maxlength="20" styleClass="box"  value="<%=nif%>" onBlur="obtenerNif();"></html:text>
		<% } else { %>
			<html:text name="ContrariosEjgForm" property="nif" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=nif%>" readonly="true"></html:text>
		<% } %>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.nombre"/>:
		</td>
		<td>
			<html:text name="ContrariosEjgForm" property="nombre" size="15" maxlength="100" styleClass="<%=estiloCaja%>"  readOnly="<%=lectura%>" value="<%=nombre%>"></html:text>
		</td>
		<td class="labelText">	
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.apellido1"/>:
		</td>		
		<td>
			<html:text name="ContrariosEjgForm" property="apellido1" size="15" maxlength="100" styleClass="<%=estiloCaja%>"  readOnly="<%=lectura%>" value="<%=apellido1%>"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.apellido2"/>:
		</td>
		<td>
			<html:text name="ContrariosEjgForm" property="apellido2" size="15" maxlength="100" styleClass="<%=estiloCaja%>"  readOnly="<%=lectura%>"  value="<%=apellido2%>"></html:text>
		</td>	
	</tr>	
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.cp"/>:
		</td>
		<td>
			<html:text name="ContrariosEjgForm" property="codigoPostal" size="10" maxlength="20" styleClass="<%=estiloCaja%>"  readOnly="<%=lectura%>"  value="<%=cp%>"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.direccion"/>:
		</td>
		<td colspan="3">
			<html:text name="ContrariosEjgForm" property="direccion" size="50" maxlength="100" styleClass="<%=estiloCaja%>"  readOnly="<%=lectura%>"  value="<%=direccion%>"></html:text>
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.nacionalidad"/>:
		</td>
		<td colspan="2">
		<% if (accion!=null && !accion.equalsIgnoreCase("ver")) { %>
				<siga:ComboBD nombre="idPais" tipo="pais" estilo="true" clase="boxCombo" obligatorio="false" elementoSel="<%=selPais%>" /> 
		<% } else { %>
				<html:text name="ContrariosEjgForm" property="idPais" styleClass="boxConsulta"  readOnly="true"  value="<%=PAISDESC%>"></html:text>
		<% } %>
		</td>
		<td class="labelText" colspan="3">
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.provincia" />:
		</td>
		<td colspan="2">
		<% if (accion!=null && !accion.equalsIgnoreCase("ver")) { %>
				<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="boxCombo" obligatorio="false" accion="Hijo:idPoblacion" elementoSel="<%=idProvincia%>" />
		<% } else { %>
				<html:text name="ContrariosEjgForm" property="idProvincia" styleClass="boxConsulta"  readOnly="true"  value="<%=PROVINCIADESC%>"></html:text>
		<% } %>
		</td>
		<td class="labelText" colspan="3">	
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.poblacion"/>:
		</td>
		<td colspan="2">
		<% if(accion != null && !accion.equalsIgnoreCase("ver")) { %>
				<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="false" accion="" hijo="t" elementoSel="<%=idPoblacion%>" />
		<% } else { %>
				<html:text name="ContrariosEjgForm" property="idPoblacion" styleClass="boxConsulta"  readOnly="true"  value="<%=POBLACIONDESC%>"></html:text>
		<% } %>
		</td>
		<td class="labelText" colspan="3">	
		</td>
	</tr>

	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.fechaNacimiento"/>:
		</td>
		<td>
			<% if (accion==null || accion.equalsIgnoreCase("ver")) { %>
				<siga:Fecha nombreCampo="fechaNacimiento" valorInicial="<%=fechaNacimiento%>" disabled="true"></siga:Fecha>
			<%} else {%>
				<siga:Fecha nombreCampo="fechaNacimiento" valorInicial="<%=fechaNacimiento%>"></siga:Fecha>
			<%}%>
		</td>
		<td class="labelText" colspan="4">		
		</td>		
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.estadoCivil"/>:
		</td>
		<td>
			<% if (accion!=null && !accion.equalsIgnoreCase("ver")) { %>
				<siga:ComboBD nombre="idEstadoCivil" tipo="estadoCivil" estilo="true" clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoCivil %>" pestana="t"/>
			<% } else { %>
				<html:text name="ContrariosEjgForm" property="idEstadoCivil" styleClass="boxConsulta"  readOnly="true"  value="<%=ESTADODESC%>"></html:text>
			<% } %>
		</td>
		<td class="labelText" colspan="4">		
		</td>		
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.aisRespAsistencias.literal.regimenConyugal"/>:
		</td>
		<td>
			<% if (accion!=null && !accion.equalsIgnoreCase("ver")) { %>
				<select name="regimenConyugal" class="boxCombo">
					<option value="G" <%if(idRegimenConyugal.equals("G")){%>selected<%}%>><siga:Idioma key="gratuita.aisRespAsistencias.literal.gananciales"/></option>
					<option value="S" <%if(idRegimenConyugal.equals("S")){%>selected<%}%>><siga:Idioma key="gratuita.aisRespAsistencias.literal.sbienes"/></option>
				</select>
			<% } else { %>
				<font class="boxConsulta">
					<% if(idRegimenConyugal.equals("G"))%><siga:Idioma key="gratuita.aisRespAsistencias.literal.gananciales"/>
					&nbsp;
					<% if(idRegimenConyugal.equals("S"))%><siga:Idioma key="gratuita.aisRespAsistencias.literal.sbienes"/>
				</font>
			<% } %>
		</td>
		<td class="labelText" colspan="4">
		</td>		
	</tr>		

	<tr>
		<td colspan="6">
			&nbsp;
		</td>
	</tr>		
	
	<tr>
		<td class="labelText">
				<siga:Idioma key="gratuita.mantAsistencias.literal.observaciones" />
		</td>
		<td colspan="5">				
				<html:textarea name="ContrariosEjgForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)"	property="observaciones" rows="3" cols="100" styleClass="<%=estiloCaja%>" readonly="<%=lectura%>" />
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
	
	</td>
	</tr>	
	</html:form>
</table>
<!-- FIN: SUBMIT AREA -->	
</div>

	<div style="position:absolute;top:<%=topBotones%>;z-index:3;left:15">
		<siga:ConjBotonesAccion botones="G,R,C" modo="<%=accion%>" modal="G"/>
	</div>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- INICIO: SCRIPTS BOTONES -->

	<iframe align="center" src="JGR_TelefonosPersonasJG.do?accion=<%=accion%>&idPersona=<%=idPersonaJG%>&idInstitucion=<%=usr.getLocation()%>"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0";					 
						style="position:absolute; width:440; height:200; z-index:2; top: 143px; left: 514px">
	</iframe>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
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
			if(validateContrariosEjgForm(document.ContrariosEjgForm)) {
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