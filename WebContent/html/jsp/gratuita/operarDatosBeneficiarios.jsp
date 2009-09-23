<!-- operarDatosBeneficiarios.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsSOJBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String dato[] = {(String)usr.getLocation()};
	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");
	String accion = (String) ses.getAttribute("accion");
	String idPersona="", nif="", nombre="", apellido1="", apellido2="", direccion="", cp="", fechaNacimiento="", idRegimenConyugal="", modo="Insertar";	
	ArrayList idPais = new ArrayList(), idProvincia = new ArrayList(), idPoblacion = new ArrayList(), idEstadoCivil = new ArrayList();
	// Si no existe el beneficiario miHash no tendrá nada
	ScsPersonaJGBean bean = (ScsPersonaJGBean) request.getAttribute("resultado");
	try {
		if ((miHash != null) && ((miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString() != "") &&  (miHash.get(ScsPersonaJGBean.C_IDPERSONA)!= null))) {	
				idPersona = miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString();
				nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();
				nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();
				apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();
				apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
				direccion = miHash.get(ScsPersonaJGBean.C_DIRECCION).toString();
				cp = miHash.get(ScsPersonaJGBean.C_CODIGOPOSTAL).toString();
				fechaNacimiento = GstDate.getFormatedDateShort(usr.getLanguage(),(String)miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO));
				idRegimenConyugal = miHash.get(ScsPersonaJGBean.C_REGIMENCONYUGAL).toString();
				idPais.add(miHash.get(ScsPersonaJGBean.C_IDPAIS).toString());
				idProvincia.add(miHash.get(ScsPersonaJGBean.C_IDPROVINCIA).toString());
				idPoblacion.add(miHash.get(ScsPersonaJGBean.C_IDPOBLACION).toString());
				idEstadoCivil.add(miHash.get(ScsPersonaJGBean.C_ESTADOCIVIL).toString());
				modo  = "Modificar";
		}		
		else if(bean != null)
		{
			idPersona 	= String.valueOf(bean.getIdPersona());
			nif 		= bean.getNif();
			nombre 		= bean.getNombre();
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
		}
		else modo  = "Insertar";
		
	} catch (Exception e) { e.printStackTrace();};
%>	

<html>

<!-- HEAD -->
<head>
	<html:javascript formName="DefinirPersonaJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script type="text/javascript">
		
		function refrescarLocal(){
			buscar();
		}
		
		function recargar(){		
			<%if (!accion.equalsIgnoreCase("ver")){%>
				var tmp1 = document.getElementsByName("idPais");
				var tmp2 = tmp1[0];
				tmp2.onchange();
			<%}%>
		}
		
		function obtenerNif ()
		{
			var nif = (document.forms[1].nif.value).toUpperCase();
			var numero = nif.substr(0,nif.length-1);
			if(letraDNI(numero)!=nif)
			{
				alert("El NIF es incorrecto");
				return false;
			}		
           document.forms[0].modo.value="abrirAvanzada";
           document.forms[0].nif.value=document.forms[1].nif.value;
           var resultado = ventaModalGeneral(document.forms[0].name,"M");
           if(resultado != null && resultado[0] != "")
           {	
           	   var pagina = document.getElementById("resultado");
           	   pagina.src="<%=app%>/html/jsp/gratuita/listadoTelefonos.jsp?idPers="+resultado[0];
               document.forms[1].nif.value             = resultado[1];
               document.forms[1].nombre.value          = resultado[2];
               document.forms[1].apellido1.value       = resultado[3];
               document.forms[1].apellido2.value       = resultado[4];
               document.forms[1].direccion.value       = resultado[5];
               document.forms[1].codigoPostal.value    = resultado[6];
               document.forms[1].idPersona.value       = resultado[0];
               document.forms[1].fechaNacimiento.value = resultado[12];        
               document.forms[1].existia.value = "1";

				document.forms[1].idPaisAux.value		= resultado[7];
				document.forms[1].idProvinciaAux.value	= resultado[8];
				document.forms[1].idPoblacionAux.value	= resultado[9];
				document.forms[1].idEstadoCivilAux.value	= resultado[10];
				document.forms[1].regimenConyugalAux.value	= resultado[11];
				document.forms[1].modo.value="editar";
				document.forms[1].submit();
               
//               recargar();
//               var ok = sigueRecarga(resultado);

           }
       }
             function sigueRecarga(resultado){

               for(var x=0;x<document.forms[1].idEstadoCivil.length;x++)
                   if(document.forms[1].idEstadoCivil.options[x].value == resultado[10])
                      document.forms[1].idEstadoCivil.options[x].selected = 'true';
                              if(resultado[11]=='G')
               {
                  document.forms[1].regimenConyugal.options[1].selected = 'false';
                  document.forms[1].regimenConyugal.options[0].selected = 'true';
               }
               else if(resultado[11] == 'S')
               {
                  document.forms[1].regimenConyugal.options[0].selected = 'false';
                  document.forms[1].regimenConyugal.options[1].selected = 'true';
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
		
	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.busquedaSOJ.beneficiarios" 
		localizacion="gratuita.busquedaSOJ.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body class="tablaCentralCampos" onLoad="ajusteAltoBotones('resultado');recargar();">
	
	<!-- CAMPOS DEL REGISTRO -->
	<table align="center"  width="100%" height="350" class="tablaCentralCampos">
	
	<html:form action="/JGR_ResponsableAsistencia.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value = "abrirAvanzada">
		<input type="hidden" name="nif" value = "">
	</html:form>

	<html:form action="/JGR_PestanaSOJBeneficiarios" method="POST" target="mainPestanas">	
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idProfesion" value = ""/>
	<html:hidden property = "existia" value = "0"/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
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
	<td class="labelText"  colspan="7">
	<%if ((accion.equalsIgnoreCase("ver")) || (!nif.equals("") && bean == null)){%>
		<html:text name="DefinirPersonaJGForm" property="nif" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=nif%>" readonly="true"></html:text>
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="nif" size="10" maxlength="20" styleClass="box"  value="<%=nif%>" onBlur="obtenerNif()"></html:text>
	<%}%>
	</td>

	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
	</td>
	<td class="labelText">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirPersonaJGForm" property="nombre" size="15" styleClass="boxConsulta"  value="<%=nombre%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="nombre" size="15" styleClass="box"  value="<%=nombre%>"></html:text>
	<%}%>		
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido1"/>
	</td>		
	<td class="labelText">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirPersonaJGForm" property="apellido1" size="15" styleClass="boxConsulta"  value="<%=apellido1%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="apellido1" size="15"  styleClass="box"  value="<%=apellido1%>"></html:text>
	<%}%>		
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido2"/>
	</td>
	<td class="labelText">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirPersonaJGForm" property="apellido2" size="15" styleClass="boxConsulta"  value="<%=apellido2%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="apellido2" size="15" styleClass="box"  value="<%=apellido2%>"></html:text>
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
		<html:text name="DefinirPersonaJGForm" property="codigoPostal" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=cp%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="codigoPostal" size="10" maxlength="20" styleClass="box"  value="<%=cp%>"></html:text>
	<%}%>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.direccion"/>
	</td>
	<td class="labelText" colspan="5">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirPersonaJGForm" property="direccion" size="50" maxlength="100" styleClass="boxConsulta"  value="<%=direccion%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="direccion" size="50" maxlength="100" styleClass="box"  value="<%=direccion%>"></html:text>
	<%}%>
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.pais"/>
	</td>
	<td class="labelText" colspan="2">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "idPais" tipo="pais" clase="boxConsulta" obligatorio="false" accion="Hijo:idProvincia" elementoSel="<%=idPais%>" pestana="t" readonly="true"/>
	<%} else  {%>
		<siga:ComboBD nombre = "idPais" tipo="pais" clase="boxCombo" obligatorio="false" accion="Hijo:idProvincia" elementoSel="<%=idPais%>" pestana="t"/>
	<%}%>
	</td>
	<td class="labelText" colspan="5">
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.provincia"/>
	</td>
	<td class="labelText">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "idProvincia" tipo="provinciapais" clase="boxConsulta" accion="Hijo:idPoblacion" obligatorio="false" elementoSel="<%=idProvincia%>" pestana="t" hijo="t" readonly="true"/>
	<%} else  {%>	
		<siga:ComboBD nombre = "idProvincia" tipo="provinciapais" clase="boxCombo" accion="Hijo:idPoblacion" obligatorio="false" elementoSel="<%=idProvincia%>" pestana="t" hijo="t"/>
	<%}%>
	</td>
	<td class="labelText" colspan="5">	
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.poblacion"/>
	</td>
	<td class="labelText" colspan="5">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="false" elementoSel="<%=idPoblacion%>" pestana="t" hijo="t" readonly="true"/>
	<%} else  {%>
		<siga:ComboBD nombre = "idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="false" elementoSel="<%=idPoblacion%>" pestana="t" hijo="t"/>
	<%}%>
	</td>
	<td class="labelText" colspan="5">	
	</td>
	</tr>

	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.fechaNacimiento"/>
	</td>
	<td class="labelText">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirPersonaJGForm" property="fechaNacimiento" size="10" styleClass="boxConsulta" value="<%=fechaNacimiento%>" readonly="true"></html:text>&nbsp;&nbsp;
	<%} else  {%>
		<html:text name="DefinirPersonaJGForm" property="fechaNacimiento" size="10" styleClass="box" value="<%=fechaNacimiento%>" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaNacimiento);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
	<%}%>
	</td>
	<td class="labelText" colspan="5">		
	</td>		
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.estadoCivil"/>
	</td>
	<td class="labelText">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "idEstadoCivil" tipo="estadoCivil" clase="boxConsulta" obligatorio="false" elementoSel="<%=idEstadoCivil%>" pestana="t" readonly="true"/>
	<%} else  {%>
		<siga:ComboBD nombre = "idEstadoCivil" tipo="estadoCivil" clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoCivil%>" pestana="t"/>
	<%}%>
	</td>
	<td class="labelText" colspan="5">		
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
	<td class="labelText" colspan="5">
	</td>		
	</tr>		
	</table>
	</siga:ConjCampos>
	</td>
	</tr>	
	</html:form>
</table>

<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoTelefonos.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"
					class="frameGeneral">
	</iframe>

	<siga:ConjBotonesAccion botones="V,G,R" modo="<%=accion%>"  clase="botonesDetalle" />
<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[1].action="./JGR_ExpedientesSOJ.do";	
			document.forms[1].modo.value="abrirAvanzada";
			document.forms[1].target="mainWorkArea"; 
			document.forms[1].submit(); 
		}
		
		function buscar() 
		{
			document.forms[1].target = "mainPestanas";
			document.forms[1].modo.value = "abrir";
			document.forms[1].submit();
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[1].reset();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar()	{				
			if (validateDefinirPersonaJGForm(document.forms[1])){
				document.forms[1].modo.value='<%=modo%>';
				document.forms[1].target="submitArea2";
				document.forms[1].submit();
			}
		}			
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea2" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
