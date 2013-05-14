<!-- modalContrariosDesigna.jsp -->
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
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String dato[] = {(String)usr.getLocation()};
	
	// Procurador seleccionado:
	ArrayList procuradorSel = new ArrayList();
	String idProcurador=null, idInstitucionProcurador=null;
		
	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");
	String accion = (String) ses.getAttribute("accion");
	if (accion==null) accion="Nuevo";
	String idPersona="", nif="", nombre="", apellido1="", apellido2="", direccion="", cp="", fechaNacimiento="", idRegimenConyugal="", modo="";	
	String representante="", observaciones="";
	String nuevo ="si";
	ArrayList idPais = new ArrayList(), idProvincia = new ArrayList(), idPoblacion = new ArrayList(), idEstadoCivil = new ArrayList();
	String estiloCombo=null, readOnly=null, alturaFrame=null;
	
	// Si no existe el beneficiario miHash no tendrá nada
	try {
		ses.removeAttribute("seleccionado");
		ScsPersonaJGBean bean = (ScsPersonaJGBean) request.getAttribute("resultado");

		if (accion.equalsIgnoreCase("ver")) {
				estiloCombo = "boxConsulta";
				readOnly = "true";
				alturaFrame = "160";
		} else {
				estiloCombo = "boxCombo";
				readOnly = "false";
				alturaFrame = "180";
		}

	 	// Datos del procurador seleccionado:
		if (miHash!=null && miHash.get(ScsContrariosDesignaBean.C_IDPROCURADOR)!=null && miHash.get(ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR)!=null) {
		 	idProcurador =  (String)miHash.get(ScsContrariosDesignaBean.C_IDPROCURADOR);
		 	idInstitucionProcurador =  (String)miHash.get(ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR);
			procuradorSel.add(0,idProcurador+","+idInstitucionProcurador);
		}
		
		//Resto de datos:
		if (miHash!=null) {	
				idPersona = miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString();
				nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();
				nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();
				apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();
				apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
				direccion = miHash.get(ScsPersonaJGBean.C_DIRECCION).toString();
				cp = miHash.get(ScsPersonaJGBean.C_CODIGOPOSTAL).toString();
				fechaNacimiento = GstDate.getFormatedDateShort(usr.getLanguage(),(String)miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO));
				idRegimenConyugal = miHash.get(ScsPersonaJGBean.C_REGIMENCONYUGAL).toString();
				representante = (String)miHash.get(ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE);
				observaciones = (String)miHash.get("OBSERVACIONES");
				modo  = "Modificar";
				nuevo = "no";
				idPais.add(miHash.get(ScsPersonaJGBean.C_IDPAIS).toString());
				idProvincia.add(miHash.get(ScsPersonaJGBean.C_IDPROVINCIA).toString());
				idPoblacion.add(miHash.get(ScsPersonaJGBean.C_IDPOBLACION).toString());
				idEstadoCivil.add(miHash.get(ScsPersonaJGBean.C_ESTADOCIVIL).toString());
				

		} else if(bean != null) {
						// Marcamos el registro como seleccionado
						ses.setAttribute("seleccionado","si");
						idPersona 	= String.valueOf(bean.getIdPersona());
						nif 		    = bean.getNif();
						nombre 	= bean.getNombre();
						apellido1 	= bean.getApellido1();
						apellido2 	= bean.getApellido2();
						direccion 	= bean.getDireccion();
						cp 			= bean.getCodigoPostal();
						
						String pais = "", provincia = "", poblacion = "", ecivil = ""; 
						pais 		= String.valueOf(bean.getIdPais());
						provincia 	= String.valueOf(bean.getIdProvincia());
						poblacion	= String.valueOf(bean.getIdPoblacion());
						ecivil		= String.valueOf(bean.getIdEstadoCivil());
						fechaNacimiento 	= GstDate.getFormatedDateShort(usr.getLanguage(),bean.getFechaNacimiento());
						idRegimenConyugal 	= bean.getRegimenConyugal();
						while(pais.length()<3) pais = "0"+pais;
						while(provincia.length()<2) provincia = "0"+provincia;
						while(poblacion.length()<6) poblacion = "0"+poblacion;
						idPais.add(pais);
						idProvincia.add(provincia);
						idPoblacion.add(poblacion);
						idEstadoCivil.add(ecivil);
						modo	= "Insertar";
				} else 
						modo  = "Insertar";
	} catch (Exception e) {
		modo="Insertar";
	}
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.busquedaSOJ.literal.expedientesSOJ" 
		localizacion="SJCS > Maestros > Expedientes SOJ > Mantenimiento"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	<script>
		
		function recargar(){
			<%if (!accion.equalsIgnoreCase("ver")){%>
				var tmp1 = document.getElementsByName("idPais");
				var tmp2 = tmp1[0];
				tmp2.onchange();
			<%}%>
		}
		
	</script>
	
	
</head>

<body class="tablaCentralCampos" onLoad="recargar();">
	<div id="camposRegistro"  align="center">
	
	<!-- CAMPOS DEL REGISTRO -->
	<table align="center"  width="100%" height="350" class="tablaCentralCampos">

	<html:form action="/JGR_ContrariosDesignas.do" method="POST" target="mainPestanas">
	
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idProfesion" value = ""/>
	<html:hidden property = "nuevo" value = "<%=nuevo%>"/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>

	<html:hidden property = "idPaisAux" />
	<html:hidden property = "idProvinciaAux" />
	<html:hidden property = "idPoblacionAux" />
	<html:hidden property = "idEstadoCivilAux" />
	<html:hidden property = "regimenConyugalAux" />
		
	<tr>				
	<td width="100%" align="center">

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.solicitante">

	<table  align="center">
	<tr width="100%">
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaSOJ.literal.nif"/>		
			</td>
			<td class="labelText"  colspan="5">
				<%if (!accion.equalsIgnoreCase("nuevo")){%>
					<html:text name="ContrariosDesignasForm" property="nif" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=nif%>"></html:text>
				<%} else  {%>
					<html:text name="ContrariosDesignasForm" property="nif" onBlur="obtenerNif()" size="10" maxlength="20" styleClass="box"  value="<%=nif%>"></html:text>
				<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
			</td>
			<td class="labelText">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="ContrariosDesignasForm" property="nombre" size="15" maxlength="100" styleClass="boxConsulta"  value="<%=nombre%>"></html:text>
				<%} else  {%>
					<html:text name="ContrariosDesignasForm" property="nombre" size="15" maxlength="100" styleClass="box"  value="<%=nombre%>"></html:text>
				<%}%>		
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido1"/>
			</td>		
			<td class="labelText">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="ContrariosDesignasForm" property="apellido1" size="15" maxlength="100" styleClass="boxConsulta"  value="<%=apellido1%>"></html:text>
				<%} else  {%>
					<html:text name="ContrariosDesignasForm" property="apellido1" size="15" maxlength="100" styleClass="box"  value="<%=apellido1%>"></html:text>
				<%}%>		
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido2"/>
			</td>
			<td class="labelText">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="ContrariosDesignasForm" property="apellido2" size="15" maxlength="100" styleClass="boxConsulta"  value="<%=apellido2%>"></html:text>
				<%} else  {%>
					<html:text name="ContrariosDesignasForm" property="apellido2" size="15" maxlength="100" styleClass="box"  value="<%=apellido2%>"></html:text>
				<%}%>
			</td>	
			<td class="labelText">
			</td>
	</tr>	
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.codigoPostal"/>
			</td>
			<td class="labelText">
			<%if (accion.equalsIgnoreCase("ver")){%>
				<html:text name="ContrariosDesignasForm" property="codigoPostal" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=cp%>"></html:text>
			<%} else  {%>
				<html:text name="ContrariosDesignasForm" property="codigoPostal" size="10" maxlength="20" styleClass="box"  value="<%=cp%>"></html:text>
			<%}%>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.direccion"/>
			</td>
			<td class="labelText" colspan="3">
			<%if (accion.equalsIgnoreCase("ver")){%>
				<html:text name="ContrariosDesignasForm" property="direccion" size="50" maxlength="100" styleClass="boxConsulta"  value="<%=direccion%>"></html:text>
			<%} else  {%>
				<html:text name="ContrariosDesignasForm" property="direccion" size="50" maxlength="100" styleClass="box"  value="<%=direccion%>"></html:text>
			<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.pais"/>
			</td>
			<td class="labelText" colspan="5">
				<% if (accion.equalsIgnoreCase("ver")) {
					CenPaisBean resultadoPais = new CenPaisBean();
					try {
							CenPaisAdm paisAdm = new CenPaisAdm(usr);
							String consultaPais = " where idpais='"+(String)miHash.get(ScsPersonaJGBean.C_IDPAIS)+"' ";
							resultadoPais = (CenPaisBean)((Vector)paisAdm.select(consultaPais)).get(0);
					} catch(Exception e) {
					}
					%>
					<html:text name="ContrariosDesignasForm" property="idPais" size="10" styleClass="boxConsulta" value='<%=(String)resultadoPais.getNombre()%>' readonly="true"></html:text>&nbsp;&nbsp;
				<% } else { %> 
					<siga:ComboBD nombre="idPais" tipo="pais" estilo="true" clase="boxCombo" obligatorio="false" accion="Hijo:idProvincia" elementoSel="<%=idPais%>" />
				<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.provincia"/>
			</td>
			<td class="labelText" colspan="5">
				<% if (accion.equalsIgnoreCase("ver")) {
					CenProvinciaBean resultadoProvincia = new CenProvinciaBean();
					String provinciaVer="";
					try{
							CenProvinciaAdm provinciaAdm = new CenProvinciaAdm(usr);
							String consultaProv = " where idprovincia='"+(String)miHash.get(ScsPersonaJGBean.C_IDPROVINCIA)+"' ";
							resultadoProvincia = (CenProvinciaBean)((Vector)provinciaAdm.select(consultaProv)).get(0);
							if (resultadoProvincia==null)
								provinciaVer="";
							else
								provinciaVer=(String)resultadoProvincia.getNombre();
					} catch(Exception e) {
							provinciaVer="";
					}
					%>
					<html:text name="ContrariosDesignasForm" property="idProvincia" size="10" styleClass="boxConsulta" value='<%=provinciaVer%>' readonly="true"></html:text>&nbsp;&nbsp;
				<%} else  {%>
					<siga:ComboBD nombre="idProvincia" 	tipo="provinciapais" clase="boxCombo" hijo="t" accion="Hijo:idPoblacion" elementoSel="<%=idProvincia%>" />
				<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.poblacion"/>
			</td>
			<td class="labelText" colspan="5">
				<%if (accion.equalsIgnoreCase("ver")){
					CenPoblacionesBean resultadoPoblacion = new CenPoblacionesBean();
					String poblacionVer="";
					try{
							CenPoblacionesAdm poblacionAdm = new CenPoblacionesAdm(usr);
							String consultaPobl = " where idpoblacion='"+(String)miHash.get(ScsPersonaJGBean.C_IDPOBLACION)+"' ";
							resultadoPoblacion = (CenPoblacionesBean)((Vector)poblacionAdm.select(consultaPobl)).get(0);
							if (resultadoPoblacion==null)
								poblacionVer = "";
							else
							poblacionVer = (String)resultadoPoblacion.getNombre();
					}catch(Exception e){
							poblacionVer = "";
					}
					%>
					<html:text name="ContrariosDesignasForm" property="idPoblacion" size="10" styleClass="boxConsulta" value='<%=poblacionVer%>' readonly="true"></html:text>&nbsp;&nbsp;
				<%} else  {%>
					<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="false" hijo="t"  elementoSel="<%=idPoblacion%>" />
				<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.fechaNacimiento"/>
			</td>
			<td class="labelText" colspan="5">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="ContrariosDesignasForm" property="fechaNacimiento" size="10" styleClass="boxConsulta" value="<%=fechaNacimiento%>" readonly="true"></html:text>&nbsp;&nbsp;
				<%} else  {%>
					<html:text name="ContrariosDesignasForm" property="fechaNacimiento" size="10" styleClass="box" value="<%=fechaNacimiento%>" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaNacimiento);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
				<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.estadoCivil"/>
			</td>
			<td class="labelText" colspan="5">
			<%if (accion.equalsIgnoreCase("ver")){%>
				<siga:ComboBD nombre = "idEstadoCivil" tipo="estadoCivil" clase="boxConsulta" obligatorio="false" elementoSel="<%=idEstadoCivil%>" readonly="true"/>
			<%} else  {%>
				<siga:ComboBD nombre = "idEstadoCivil" tipo="estadoCivil" clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoCivil%>" />
			<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.regimenConyugal"/>
			</td>
			<td class="labelText">
			<%if (accion.equalsIgnoreCase("ver")){
				String regimen = (idRegimenConyugal.equalsIgnoreCase("G")?"Gananciales":"Separación de bienes");%>
				<html:text name="ContrariosDesignasForm" property="regimenConyugal" size="18" styleClass="boxConsulta" value="<%=regimen%>" readonly="true"></html:text>
			<%} else  {%>
				<select name="regimenConyugal" class="boxCombo">
					<option value="G" <%if(idRegimenConyugal.equalsIgnoreCase("G")){ %>selected<%}%>>Gananciales</option>
					<option value="S" <%if(idRegimenConyugal.equalsIgnoreCase("S")){ %>selected<%}%>>Separación de bienes</option>
				</select>
			<%}%>
			</td>
			<td class="labelText">
					<siga:Idioma key="gratuita.editarDesigna.literal.procurador"/>
			</td>		
			<td class="labelText" colspan="3">
					<siga:ComboBD nombre="procurador" tipo="comboProcuradores" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnly%>" parametro="<%=dato%>" elementoSel="<%=procuradorSel%>"/>
			</td>		
	</tr>		
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.defendidosDesigna.literal.representante"/>
			</td>
			<td class="labelText" colspan="5">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="ContrariosDesignasForm" property="nombreRepresentante" size="190" styleClass="boxConsulta" value="<%=representante%>" readonly="true"></html:text>
				<%} else  {%>
					<html:text name="ContrariosDesignasForm" property="nombreRepresentante" size="190" styleClass="box" value="<%=representante%>"></html:text>
				<%}%>
			</td>
	</tr>
	<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.editarDesigna.literal.observaciones"/>
			</td>
			<td class="labelText" colspan="5">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<textarea class="boxConsulta" scroll="none" name="observaciones" rows="2" cols="190" readonly="true"><%=observaciones%></textarea>
				<%} else  {%>
					<textarea class="box" scroll="none" name="observaciones" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="2" cols="190" ><%=observaciones%></textarea>
				<%}%>
			</td>
	</tr>
	</table>
	</siga:ConjCampos>
	</td>
	</tr>	

	</html:form>
	
	<html:form action="/JGR_ResponsableAsistencia.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirAvanzada">
		<input type="hidden" name="nif" 	value="elNif">
	</html:form>
	
<div style="position:absolute;top:492;z-index:3;left:0">
	<siga:ConjBotonesAccion botones="Y,R,C" modal="G" modo="<%=accion%>"/>
<!-- FIN: BOTONES REGISTRO -->
</div>

</table>
</div>

<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoTelefonos.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					style="position:absolute; width:440; height:<%=alturaFrame%>; z-index:2; top: 140px; left: 514px">
</iframe>


	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{	
			top.cierraConParametros("MODIFICADO");
		}
		
		function buscar() 
		{		
			document.forms[0].modo.value = "";
			document.forms[0].submit();
		}

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardarCerrar()	
		{	
			if (document.forms[0].fechaNacimiento.value.length<2){
				alert('El campo Fecha de nacimiento es obligatorio');			
			}
			else{
				if (document.forms[0].idPais.selectedIndex<1){
					alert('El campo Pais es obligatorio');
				}else{
					document.forms[0].target="submitArea";
					document.forms[0].modo.value="<%=modo%>";
					document.forms[0].submit();
					window.top.returnValue="MODIFICADO";
				}
			}
		}			
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	

	<script>
		function obtenerNif ()
		{
			var nif = (document.forms[0].nif.value).toUpperCase();
			var numero = nif.substr(0,nif.length-1);
			if(letraDNI(numero)!=nif)
			{
				alert("El NIF es incorrecto");
				return false;
			}		
			document.forms[1].modo.value="abrirAvanzada";
			document.forms[1].nif.value=document.forms[0].nif.value;
			var resultado = ventaModalGeneral(document.forms[1].name,"M");
			if(resultado != null && resultado[0] != "") 
			{ 
				var p1 = document.getElementById("resultado");
				p1.scr = "<%=app%>/html/jsp/gratuita/listadoTelefonos.jsp?idPers="+resultado[0];
				document.forms[0].nif.value 			= resultado[1]; 
				document.forms[0].nombre.value	 		= resultado[2]; 
				document.forms[0].apellido1.value 		= resultado[3]; 
				document.forms[0].apellido2.value 		= resultado[4]; 
				document.forms[0].direccion.value 		= resultado[5]; 
				document.forms[0].codigoPostal.value 	= resultado[6];
				document.forms[0].idPersona.value		= resultado[0];
				document.forms[0].fechaNacimiento.value = resultado[12];
				document.forms[0].nuevo.value="no"; 

				document.forms[0].idPaisAux.value		= resultado[7];
				document.forms[0].idProvinciaAux.value	= resultado[8];
				document.forms[0].idPoblacionAux.value	= resultado[9];
				document.forms[0].idEstadoCivilAux.value	= resultado[10];
				document.forms[0].regimenConyugalAux.value	= resultado[11];
				document.forms[0].modo.value="editar2";
				document.forms[0].target="_self";
				document.forms[0].submit();
			}
		}
		
		function sigueRecarga(resultado){
		
				for(var x=0;x<document.forms[0].idProvincia.length;x++)
				{
					var laProvincia = resultado[8];
					while(laProvincia.length < 1) laProvincia = "0"+laProvincia;
					if(document.forms[0].idProvincia.options[x].value == laProvincia)
						document.forms[0].idProvincia.options[x].selected = 'true';
				}

				for(var x=0;x<document.forms[0].idPoblacion.length;x++)
				{
					var laPoblacion = resultado[9];
					while(laPoblacion.length < 5) laPoblacion = "0"+laPoblacion;
					if(document.forms[0].idPoblacion.options[x].value == laPoblacion)
						document.forms[0].idPoblacion.options[x].selected = 'true';
				}
				for(var x=0;x<document.forms[0].idEstadoCivil.length;x++)
					if(document.forms[0].idEstadoCivil.options[x].value == resultado[10])
						document.forms[0].idEstadoCivil.options[x].selected = 'true';
				
				if(resultado[11]=='G')
				{
					document.forms[0].regimenConyugal.options[1].selected = 'false';
					document.forms[0].regimenConyugal.options[0].selected = 'true';
				}
				else if(resultado[11] == 'S') 
				{
					document.forms[0].regimenConyugal.options[0].selected = 'false';
					document.forms[0].regimenConyugal.options[1].selected = 'true';
				}
		}
		
		
		function letraDNI(num) 
		{
			let = 'TRWAGMYFPDXBNJZSQVHLCKE';
			if (isNaN(num) == false)
			{
				var posicion = num % 23;
				var letra = let.substring(posicion,posicion+1) ;
		    	        numlet=num + letra;
				return numlet ;
			}
		}
		function refrescarLocal()
		{
			top.cierraConParametros("MODIFICADO");
		}
		
</script>	

</body>
</html>