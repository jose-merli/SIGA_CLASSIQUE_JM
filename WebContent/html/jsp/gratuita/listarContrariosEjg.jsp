<!-- listarContrariosEJG.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsDelitoBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.ContrariosEjgForm"%>
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile(); 
	
	//Hashtable hash = (Hashtable)ses.getAttribute("DATABACKUP");
	ContrariosEjgForm miForm = (ContrariosEjgForm) request.getAttribute("ContrariosEjgForm");

	Hashtable hash = (Hashtable)ses.getAttribute("DATABACKUP");
	
	String anio = hash.get("ANIO").toString();
	String numero = hash.get("NUMERO").toString();
	String idTipoEJG = hash.get("IDTIPOEJG").toString();
	
	
	String modopestanha = request.getSession().getAttribute("accion")==null?"":(String)request.getSession().getAttribute("accion");
	boolean esFichaColegial = false;
	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null) && (sEsFichaColegial.equalsIgnoreCase("1"))) {
		esFichaColegial = true;
	}
	

	//Datos propios del jsp:	
	Vector vContrariosEJG = (Vector) request.getAttribute("vContrariosEJG");
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<script>
		function refrescarLocal(){
			parent.refrescarLocal();
		}

		
					
function accionNuevo() 
		{   document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"G"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}


	</script>
</head>

<body >

	
	
	
	<%
				String sAction = esFichaColegial ? "/JGR_ContrariosEjgPerJGLetrado.do" : "/JGR_ContrariosEjgPerJG.do" ;%>

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="<%=sAction%>" method="post" target="submitArea" style="display:none">
			<input type="hidden" name="modo" value="abrirPestana">
			
			<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
			<input type="hidden" name="idPersonaJG" value="">
	
			<input type="hidden" name="idInstitucionEJG" value="<%=usr.getLocation() %>">
			<input type="hidden" name="anioEJG" value="<%=anio %>">
			<input type="hidden" name="numeroEJG" value="<%=numero %>">
			<input type="hidden" name="idTipoEJG" value="<%=idTipoEJG %>">
	
			<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.EJG_CONTRARIOS %>">
			<input type="hidden" name="tituloE" value="gratuita.mantEjg.literal.tituloCO">
			<input type="hidden" name="localizacionE" value="">
			<input type="hidden" name="accionE" value="nuevo">
			<input type="hidden" name="actionE" value="<%=sAction%>">
			<input type="hidden" name="pantallaE" value="M">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
	
	
	
	  <!-- INICIO: RESULTADO -->
		<siga:TablaCabecerasFijas 		   
			   nombre="listadoInicial"
			   borde="2"
			   clase="tableTitle"		   
			   nombreCol="gratuita.mantenimientoTablasMaestra.literal.nif,gratuita.mantenimientoTablasMaestra.literal.nombreyapellidos,envios.etiquetas.tipoCliente.abogado,gratuita.personaJG.literal.procurador,"
			   tamanoCol="10,30,25,25,15"
			   alto="60%"
			   modal="G"
		>
	<% if (vContrariosEJG!= null && !vContrariosEJG.isEmpty()) { %>
			<%
				String descripcion="";
				String nif="";
				String representante="";
				String idPersona="";
				int recordNumber=1;
				String abogadoContrario="";
				String procurador="";
				while ((recordNumber) <= vContrariosEJG.size())
				{
					
					Hashtable hashContrariosEJG = (Hashtable)vContrariosEJG.get(recordNumber-1);
					nif = UtilidadesString.mostrarDatoJSP(hashContrariosEJG.get("NIF"));
					descripcion = (String)hashContrariosEJG.get("DATOS_CONTRARIO");					
					idPersona=(String)hashContrariosEJG.get("IDPERSONA");
					abogadoContrario=(String)hashContrariosEJG.get("NOMBREABOGADOCONTRARIOEJG");
					procurador=(String)hashContrariosEJG.get("PROCURADOR");
					if (abogadoContrario != null && !"".equals(abogadoContrario)){
						abogadoContrario=(String)hashContrariosEJG.get("NOMBREABOGADOCONTRARIOEJG");
					}
					else 
						//se agrega un espacio de esta forma porque es la unica forma para que no de error al borrar o editar, si el campo esta vacio o a null.
					   abogadoContrario="&nbsp";
					
					if (procurador != null && !"".equals(procurador)){
						procurador=(String)hashContrariosEJG.get("PROCURADOR");
					}
					else 
						//se agrega un espacio de esta forma porque es la unica forma para que no de error al borrar o editar, si el campo esta vacio o a null.
					    procurador="&nbsp";
			%>
			
	
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="B,E,C"  clase="listaNonEdit" modo="<%=modopestanha%>" >
				<td>
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=PersonaJGAction.EJG_CONTRARIOS%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="gratuita.contrariosAsistencia.literal.titulo">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="gratuita.contrariosAsistencia.literal.titulo">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="editar">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=idPersona%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_8" value="<%=anio %>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_9" value="<%=numero %>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_10" value="<%=idTipoEJG %>">
						<%=nif%>
				</td>
				<td>
						
						<%=descripcion%>
				</td>
				<td>
						 <%=abogadoContrario%>
						
				</td>
				<td>
				  	 <%=procurador%>
				</td>
			</siga:FilaConIconos>
				<% 		recordNumber++; %>
				<% } %>
	<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } %>
	</siga:TablaCabecerasFijas> 
	
	
	
	
	
	
	
		
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>