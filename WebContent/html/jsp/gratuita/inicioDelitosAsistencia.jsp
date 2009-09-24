<!-- inicioDelitosAsistencia.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri ="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"  prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"   prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoAsistenciaForm"%>



<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String modopestanha = request.getSession().getAttribute("accion")==null?"":(String)request.getSession().getAttribute("accion");

	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null)
			&& ((sEsFichaColegial.equalsIgnoreCase("1"))||(sEsFichaColegial.equalsIgnoreCase("true"))  )) {
		esFichaColegial = true;
	}
	
	PestanaDelitoAsistenciaForm form = (PestanaDelitoAsistenciaForm)request.getAttribute("pestanaDelitoAsistenciaForm");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.EJG.delitos" 
		localizacion="gratuita.mantAsistencias.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->			
	
</head>

<body onload="ajusteAltoMain('resultado','100');buscar();document.getElementById('delitoAux').value=document.forms[0].delito.value;">
	
    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos">
			
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaAsistencia(usr.getLocation(), ""+form.getAnio(), ""+form.getNumero());
					if (hTitulo != null) {
						t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
						t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
						t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
						t_anio      = (String)hTitulo.get(ScsAsistenciasBean.C_ANIO);
						t_numero    = (String)hTitulo.get(ScsAsistenciasBean.C_NUMERO);
					}
				%>
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>	

	
	<html:form action = "/JGR_DelitosAsistencia.do" method="POST" target="resultado" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>			
		<!-- Datos de la pestanha -->
		<html:hidden name="pestanaDelitoAsistenciaForm" property="anio" />
		<html:hidden name="pestanaDelitoAsistenciaForm" property="numero" />
		<html:hidden name="pestanaDelitoAsistenciaForm" property="delito" />
		<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
	</html:form>
	

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
<%
		String sTop = esFichaColegial ? "300" : "350";
%>

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<div style="position:absolute; width:60%;left:200px;bottom:32px;z-index:0;">
		<table class="tablaCampos" align="center" border="0">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.general.literal.comentariosDelitosFaltas"/>
			</td>
			<td>
				<textarea name="delitoAux" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)"  id="delitoAux" cols="60" rows="3" style="overflow:auto" class="box"></textarea>
			</td>
		</tr>
		</table>
	</div>



<%
		String sClasePestanas = esFichaColegial ? "botonesDetalle3" : "botonesDetalle";
%>

	<siga:ConjBotonesAccion botones="N,R,G"  clase="botonesDetalle" modo="<%=modopestanha%>"/>	
		
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">

		<!-- Funcion asociada al boton accionRestablecer -->
		function accionRestablecer() 
		{
			document.forms[0].reset();
		}		

		<!-- Funcion asociada al refresco -->
		function refrescarLocal() 
		{
			document.forms[0].target = '_self';
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}		

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
<%			if (esFichaColegial) {%>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistenciaLetrado.do";
<%			} else { %>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistencia.do";
<%			}%>

			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}		

		<!-- Funcion asociada a boton Nuevo -->
		function accionNuevo() 
		{		
<%			if (esFichaColegial) {%>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistenciaLetrado.do";
<%			} else { %>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistencia.do";
<%			}%>

			document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}

		<!-- Funcion asociada a boton Guardar -->
		function accionGuardar() 
		{
			sub();		
<%			if (esFichaColegial) {%>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistenciaLetrado.do";
				
<%			} else { %>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistencia.do";
				
<%			}%>

			document.forms[0].delito.value = document.getElementById('delitoAux').value;
			
			document.forms[0].modo.value = "modificar";
			document.forms[0].target = "submitArea";
			document.forms[0].submit();
		}
				
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>