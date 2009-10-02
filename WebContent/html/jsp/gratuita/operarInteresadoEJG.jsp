<!-- operarInteresadoEJG.jsp -->
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
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsUnidadFamiliarEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String dato[] = {(String)usr.getLocation()};
	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");
	String accion = (String) ses.getAttribute("accion");
	String idPersona="", nif="", nombre="", apellido1="", apellido2="", direccion="", cp="", fechaNacimiento="", idRegimenConyugal="", modo="Insertar", anio ="", numero ="", idTipoEJG = "";
	String descripcionIngresos = "", importeIngresos = "", bienesMuebles = "", importeMuebles = "", bienesInmuebles = "", importeInmuebles = "", otrosBienes = "", importeOtrosBienes = "";	
	// Si no existe el beneficiario miHash no tendrá nada
	ArrayList idPais = new ArrayList(), idProvincia = new ArrayList(), idPoblacion = new ArrayList(), idEstadoCivil = new ArrayList();
	ScsPersonaJGBean bean = (ScsPersonaJGBean) request.getAttribute("resultado");
	// Si no existe el beneficiario miHash no tendrá nada
	try {
		ses.removeAttribute("seleccionado");
		if ((miHash != null) && ((!miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString().equals("")) &&  (miHash.get(ScsPersonaJGBean.C_IDPERSONA)!= null))) {	
				anio = miHash.get(ScsUnidadFamiliarEJGBean.C_ANIO).toString();
				numero = miHash.get(ScsUnidadFamiliarEJGBean.C_NUMERO).toString();
				idTipoEJG = miHash.get(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG).toString();
				idPersona = miHash.get(ScsPersonaJGBean.C_IDPERSONA).toString();
				nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();
				nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();
				apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();
				apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
				direccion = miHash.get(ScsPersonaJGBean.C_DIRECCION).toString();
				cp = miHash.get(ScsPersonaJGBean.C_CODIGOPOSTAL).toString();
				fechaNacimiento = GstDate.getFormatedDateShort(usr.getLanguage(),(String)miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO));
				idRegimenConyugal = miHash.get(ScsPersonaJGBean.C_REGIMENCONYUGAL).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES)) descripcionIngresos = miHash.get(ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES)) importeIngresos = miHash.get(ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES)) bienesInmuebles = miHash.get(ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES)) importeInmuebles = miHash.get(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES)) bienesMuebles = miHash.get(ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES)) importeMuebles = miHash.get(ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES).toString();
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_OTROSBIENES)) otrosBienes = miHash.get(ScsUnidadFamiliarEJGBean.C_OTROSBIENES).toString();				
				if (miHash.containsKey(ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES)) importeOtrosBienes = miHash.get(ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES).toString();
				modo  = "Modificar";
				idPais.add(miHash.get(ScsPersonaJGBean.C_IDPAIS).toString());
				idProvincia.add(miHash.get(ScsPersonaJGBean.C_IDPROVINCIA).toString());
				idPoblacion.add(miHash.get(ScsPersonaJGBean.C_IDPOBLACION).toString());
				idEstadoCivil.add(miHash.get(ScsPersonaJGBean.C_ESTADOCIVIL).toString());
		}		
		else if(bean != null)
		{
			// Marcamos el registro como seleecionado
			ses.setAttribute("seleccionado","si");
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
	} catch (Exception e) {};				
%>	

<html>

<!-- HEAD -->
<head>
	<html:javascript formName="DefinirInteresadoEJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script type="text/javascript">
		function recargar(){
			<%if (!accion.equalsIgnoreCase("ver")){%>
				var tmp1 = document.getElementsByName("idProvincia");
				var tmp2 = tmp1[0];
				tmp2.onchange();
			<%}%>
		}
		function refrescarLocal()
		{	
			buscar();
		}
	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.busquedaEJG.interesado" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<script type="text/javascript">
	
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
               document.forms[1].existia.value="1";
               document.forms[1].descripcionIngresosAnuales.value="";
               document.forms[1].importeIngresosAnuales.value="";
               document.forms[1].bienesMuebles.value="";
               document.forms[1].importeBienesMuebles.value="";
               document.forms[1].bienesInmuebles.value="";
               document.forms[1].importeBienesInmuebles.value="";               
               
				document.forms[1].idPaisAux.value		= resultado[7];
				document.forms[1].idProvinciaAux.value	= resultado[8];
				document.forms[1].idPoblacionAux.value	= resultado[9];
				document.forms[1].idEstadoCivilAux.value	= resultado[10];
				document.forms[1].regimenConyugalAux.value	= resultado[11];
				document.forms[1].modo.value="editar";
				document.forms[1].submit();

/*               for(var x=0;x<document.forms[1].idPais.length;x++)
               {
                   var elPais = resultado[7];
                   while(elPais.length < 3) elPais = "0"+elPais;
                   if(document.forms[1].idPais.options[x].value == elPais)
                       document.forms[1].idPais.options[x].selected = 'true';
               }
               recargar();
               var ok = sigueRecarga(resultado);
*/

           }
       }
             function sigueRecarga(resultado){

               for(var x=0;x<document.forms[1].idProvincia.length;x++)
               {
                   var laProvincia = resultado[8];
                   while(laProvincia.length < 1) laProvincia = "0"+laProvincia;
                   if(document.forms[1].idProvincia.options[x].value == laProvincia)
                      document.forms[1].idProvincia.options[x].selected = 'true';
               }

               for(var x=0;x<document.forms[1].idPoblacion.length;x++)
               {
                   var laPoblacion = resultado[9];
                   while(laPoblacion.length < 5) laPoblacion = "0"+laPoblacion;
                   if(document.forms[1].idPoblacion.options[x].value == laPoblacion)
                      document.forms[1].idPoblacion.options[x].selected = 'true';
               }
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
		


</head>

<body class="tablaCentralCampos" onLoad="ajusteAltoBotones('resultado');recargar();">

	<html:form action="/JGR_ResponsableAsistencia.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value = "abrirAvanzada">
		<input type="hidden" name="nif" value = "">
	</html:form>
	
	
	<!-- CAMPOS DEL REGISTRO -->
	<table align="center"  width="100%" height="360" class="tablaCentralCampos">

	<html:form action="/JGR_InteresadoEJG" method="POST" target="mainPestanas">
	
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idProfesion" value = ""/>
	<html:hidden property = "solicitante" value = "1"/>
	<html:hidden property = "existia" value = "0"/>
	<html:hidden property = "anio" value = "<%=anio%>"/>
	<html:hidden property = "numero" value = "<%=numero%>"/>
	<html:hidden property = "idTipoEJG" value = "<%=idTipoEJG%>"/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "nuevo" value = ""/>
	<html:hidden property = "fechaMod" value = "sysdate"/>

	<html:hidden property = "idPaisAux" />
	<html:hidden property = "idProvinciaAux" />
	<html:hidden property = "idPoblacionAux" />
	<html:hidden property = "idEstadoCivilAux" />
	<html:hidden property = "regimenConyugalAux" />
		
		
	<tr>				
	<td width="100%" align="center" valign="top">

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.solicitante">

	<table  align="center" width="100%">
	
	<tr width="100%">
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.nif"/>		
	</td>
	<td  colspan="7">
	<%if ((accion.equalsIgnoreCase("ver")) || (!nif.equals(""))){%>
		<html:text name="DefinirInteresadoEJGForm" property="nif" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=nif%>" readOnly="true"></html:text>
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="nif" size="10" maxlength="20" styleClass="box"  value="<%=nif%>" onBlur="obtenerNif()"></html:text>
	<%}%>
	</td>

	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
	</td>
	<td >
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirInteresadoEJGForm" property="nombre" size="15" maxlength="100" styleClass="boxConsulta"  value="<%=nombre%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="nombre" size="15" maxlength="100" styleClass="box"  value="<%=nombre%>"></html:text>
	<%}%>		
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido1"/>
	</td>		
	<td >
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirInteresadoEJGForm" property="apellido1" size="15" maxlength="100" styleClass="boxConsulta"  value="<%=apellido1%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="apellido1" size="15" maxlength="100" styleClass="box"  value="<%=apellido1%>"></html:text>
	<%}%>		
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido2"/>
	</td>
	<td >
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirInteresadoEJGForm" property="apellido2" size="15" maxlength="100" styleClass="boxConsulta"  value="<%=apellido2%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="apellido2" size="15" maxlength="100" styleClass="box"  value="<%=apellido2%>"></html:text>
	<%}%>
	</td>	
	<td class="labelText">
	</td>
	</tr>	
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.codigoPostal"/>
	</td>
	<td >
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirInteresadoEJGForm" property="codigoPostal" size="10" maxlength="20" styleClass="boxConsulta"  value="<%=cp%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="codigoPostal" size="10" maxlength="20" styleClass="box"  value="<%=cp%>"></html:text>
	<%}%>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.direccion"/>
	</td>
	<td  colspan="5">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirInteresadoEJGForm" property="direccion" size="50" maxlength="100" styleClass="boxConsulta"  value="<%=direccion%>"></html:text>
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="direccion" size="50" maxlength="100" styleClass="box"  value="<%=direccion%>"></html:text>
	<%}%>
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.pais"/>
	</td>
	<td  colspan="2">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "idPais" tipo="pais" clase="boxConsulta" obligatorio="false" elementoSel="<%=idPais%>" pestana="t" readonly="true"/>
	<%} else  {%>
		<siga:ComboBD nombre = "idPais" tipo="pais" clase="boxCombo" obligatorio="false" elementoSel="<%=idPais%>" pestana="t"/>
	<%}%>
	</td>
	<td class="labelText" colspan="5">
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.provincia"/>
	</td>
	<td >
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "idProvincia" tipo="provincia" clase="boxConsulta" accion="Hijo:idPoblacion" obligatorio="false" elementoSel="<%=idProvincia%>" pestana="t" readonly="true"/>
	<%} else  {%>
		<siga:ComboBD nombre = "idProvincia" tipo="provincia" clase="boxCombo" accion="Hijo:idPoblacion" obligatorio="false" elementoSel="<%=idProvincia%>" pestana="t"/>
	<%}%>
	</td>
	<td class="labelText" colspan="5">	
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.poblacion"/>
	</td>
	<td  colspan="5">
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
	<td >
	<%if (accion.equalsIgnoreCase("ver")){%>
		<html:text name="DefinirInteresadoEJGForm" property="fechaNacimiento" size="10" styleClass="boxConsulta" value="<%=fechaNacimiento%>" readonly="true"></html:text>&nbsp;&nbsp;
	<%} else  {%>
		<html:text name="DefinirInteresadoEJGForm" property="fechaNacimiento" size="10" styleClass="box" value="<%=fechaNacimiento%>" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaNacimiento);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
	<%}%>
	</td>
	<td class="labelText" colspan="5">		
	</td>		
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarDatosBeneficiario.literal.estadoCivil"/>
	</td>
	<td >
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
	<td >
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
<%
//preparamos los importes para visualizarlos.
String importeIngresosFormat = "";
String importeInmueblesFormat = "";
String importeMueblesFormat = "";
String importeOtrosBienesFormat = "";
java.text.DecimalFormatSymbols dfs = new java.text.DecimalFormatSymbols();
dfs.setDecimalSeparator(',');
dfs.setGroupingSeparator('.');
java.text.DecimalFormat dfEuro = new java.text.DecimalFormat("#,##0.00",dfs);
if(importeIngresos!=null && !importeIngresos.equals(""))
	importeIngresosFormat = dfEuro.format(Double.valueOf(importeIngresos));
if(importeInmuebles!=null && !importeInmuebles.equals(""))
	importeInmueblesFormat = dfEuro.format(Double.valueOf(importeInmuebles));
if(importeMuebles!=null && !importeMuebles.equals(""))
	importeMueblesFormat = dfEuro.format(Double.valueOf(importeMuebles));
if(importeOtrosBienes!=null && !importeOtrosBienes.equals(""))
	importeOtrosBienesFormat = dfEuro.format(Double.valueOf(importeOtrosBienes));

%>	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarInteresado.literal.ingresosAnuales"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="descripcionIngresosAnuales" size="40" styleClass="boxConsulta" value="<%=descripcionIngresos%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="descripcionIngresosAnuales" size="40" styleClass="box" value="<%=descripcionIngresos%>"></html:text>
		<%}%>
	</td>
	<td class="labelTextValor">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="importeIngresosAnuales" size="10" styleClass="boxConsulta" value="<%=importeIngresosFormat%>" readonly="true"></html:text>&nbsp;&nbsp;&euro;
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="importeIngresosAnuales" size="10" styleClass="box" value="<%=importeIngresos%>"></html:text>&nbsp;&nbsp;&euro;
		<%}%>
	</td>
	<td class="labelText">
	</td>	
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarInteresado.literal.bienesInmuebles"/>
	</td>
	<td >
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="bienesInmuebles" size="40" styleClass="boxConsulta" value="<%=bienesInmuebles%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="bienesInmuebles" size="40" styleClass="box" value="<%=bienesInmuebles%>"></html:text>
		<%}%>
	</td>
	<td class="labelTextValor">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="importeBienesInmuebles" size="10" styleClass="boxConsulta" value="<%=importeInmueblesFormat%>" readonly="true"></html:text>&nbsp;&nbsp;&euro;
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="importeBienesInmuebles" size="10" styleClass="box" value="<%=importeInmuebles%>"></html:text>&nbsp;&nbsp;&euro;
		<%}%>
	</td>
	<td class="labelText">
	</td>	
	</tr>

	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarInteresado.literal.bienesMobiliarios"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="bienesMuebles" size="40" styleClass="boxConsulta" value="<%=bienesMuebles%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="bienesMuebles" size="40" styleClass="box" value="<%=bienesMuebles%>"></html:text>
		<%}%>
	</td>
	<td class="labelTextValor">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="importeBienesMuebles" size="10" styleClass="boxConsulta" value="<%=importeMueblesFormat%>" readonly="true"></html:text>&nbsp;&nbsp;&euro;
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="importeBienesMuebles" size="10" styleClass="box" value="<%=importeMuebles%>"></html:text>&nbsp;&nbsp;&euro;
		<%}%>
	</td>
	<td class="labelText" colspan="">
	</td>	
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarInteresado.literal.otrosBienes"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="otrosBienes" size="40" styleClass="boxConsulta" value="<%=otrosBienes%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="otrosBienes" size="40" styleClass="box" value="<%=otrosBienes%>"></html:text>
		<%}%>
	</td>
	<td class="labelTextValor">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirInteresadoEJGForm" property="importeOtrosBienes" size="10" styleClass="boxConsulta" value="<%=importeOtrosBienesFormat%>" readonly="true"></html:text>&nbsp;&nbsp;&euro;
		<%} else {%>
			<html:text name="DefinirInteresadoEJGForm" property="importeOtrosBienes" size="10" styleClass="box" value="<%=importeOtrosBienes%>"></html:text>&nbsp;&nbsp;&euro;
		<%}%>
	</td>
	<td class="labelText">
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
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>


	<siga:ConjBotonesAccion botones="V,G,R" modo="<%=accion%>" clase="botonesDetalle"/>
	
<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[1].action="./JGR_EJG.do";	
			document.forms[1].modo.value="buscar";
			document.forms[1].target="mainWorkArea"; 
			document.forms[1].submit(); 
		}
		
		function buscar() 
		{		
			document.forms[1].modo.value = "abrir";
			document.forms[1].target="mainPestanas";
			document.forms[1].submit();
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[1].reset();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar()	{				
			
			document.forms[1].modo.value='<%=modo%>';
			document.forms[1].target="submitArea2";
			if (validateDefinirInteresadoEJGForm(document.forms[1])){
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
