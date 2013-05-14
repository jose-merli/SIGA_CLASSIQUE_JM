<!-- resultadoBusqDireccion.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
-->
	 
 
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

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.EnvDestinatariosBean"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	Vector direcciones = (Vector) request.getAttribute("direcciones");

%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

<%--
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_BusquedaClientesModal.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu %>" 
		localizacion="<%=titu %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
--%>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	function seleccionar(fila) {
		var datos;
		datos = document.getElementById('tablaDatosDinamicosD');
		datos.value = ""; 
		var j;
		var tabla;
		tabla = document.getElementById('tablaDatos');
		var flag = true;
		j = 1;
		while (flag) {
		  var aux = 'oculto' + fila + '_' + j;
		  var oculto = document.getElementById(aux);
		  if (oculto == null)  { flag = false; }
		  else { 
		  
		  	datos.value = datos.value + escape(oculto.value) + ','; 
		  }
		  j++;
		}
		datos.value =datos.value + "%%%";
		
		document.forms[0].datosSeleccionados.value=datos.value;
        document.forms[0].modo.value = "enviarDireccion";
	   	document.forms[0].submit();
	}
	</script>

</head>


<body class="tablaCentralCampos">

	
		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/ENV_Remitentes.do" method="POST" target="submitArea" style="display:none">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "" />

			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="datosSeleccionados" value="">
		</html:form>

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="censo.datosDireccion.literal.tipoDireccion,censo.datosDireccion.literal.direccion,
		   		  			  censo.datosDireccion.literal.poblacion,
		   		  			  censo.datosDireccion.literal.provincia,
		   		  			  censo.datosDireccion.literal.pais,
		   		  			  censo.datosDireccion.literal.cp,
		   		  			  censo.datosDireccion.literal.telefono1,
		   		  			  censo.datosDireccion.literal.movil,
		   		  			  censo.datosDireccion.literal.fax1,
		   		  			  censo.datosDireccion.literal.correo,"
		  columnSizes="10,19,7,7,7,5,7,7,7,11,13">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (direcciones==null || direcciones.size()==0) { %>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>	 		
<%	
	} else { 

		FilaExtElement[] elems=new FilaExtElement[1];
		elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	

		// recorro las direcciones
		for (int i=0;i<direcciones.size();i++) {
			Hashtable direccion = (Hashtable) direcciones.elementAt(i);
			String cont = new Integer(i+1).toString();

			String modo = "consulta";
			String idInstitucion = usrbean.getLocation();
			
			String idTipoEnvio = UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvEnviosBean.C_IDTIPOENVIOS)));
			String tipoDireccion = UtilidadesString.mostrarDatoJSP((String) (direccion.get("CEN_TIPODIRECCION.DESCRIPCION")));
			
			String domicilio =UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_DOMICILIO)));
			String domicilio1 =(String) (direccion.get(EnvDestinatariosBean.C_DOMICILIO));
			String poblacion =UtilidadesString.mostrarDatoJSP((String) (direccion.get("POBLACION")));
			
			String provincia =UtilidadesString.mostrarDatoJSP((String) (direccion.get("PROVINCIA")));
			String provincia1=(String) (direccion.get("PROVINCIA"));
			String pais =UtilidadesString.mostrarDatoJSP((String) (direccion.get("PAIS")));
			String cp =UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_CODIGOPOSTAL)));
			String fax1 =UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_FAX1)));
			String fax2 =UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_FAX2)));
			String correoElectronico =UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_CORREOELECTRONICO)));

			String idPais = UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_IDPAIS)));
			String idProvincia = UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_IDPROVINCIA)));
			String idPoblacion = UtilidadesString.mostrarDatoJSP((String) (direccion.get(EnvDestinatariosBean.C_IDPOBLACION)));
			
			String idDireccion = UtilidadesString.mostrarDatoJSP((String) (direccion.get(CenDireccionesBean.C_IDDIRECCION)));
			String telefono1   = UtilidadesString.mostrarDatoJSP((String) (direccion.get(CenDireccionesBean.C_TELEFONO1)));
			String movil       = UtilidadesString.mostrarDatoJSP((String) (direccion.get(CenDireccionesBean.C_MOVIL)));

			String idPais2=String.valueOf(direccion.get("IDPAIS"));
			String pob=UtilidadesString.mostrarDatoJSP(String.valueOf(direccion.get("POBLACION")));
			
			String pobExt=String.valueOf(direccion.get("POBLACIONEXTRANJERA"));
			if (pobExt==null) pobExt="";
			if (!idPais2.equals(ClsConstants.ID_PAIS_ESPANA)) {
				pob=UtilidadesString.mostrarDatoJSP(String.valueOf(direccion.get("POBLACIONEXTRANJERA")));
			}
			


%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
			
			<siga:FilaConIconos fila="<%=cont %>" botones="" modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit">
			
				<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=domicilio1%>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=pob %>">
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=provincia1%>">
					<input type="hidden" name="oculto<%=cont %>_4" value="<%=pais %>">
					<input type="hidden" name="oculto<%=cont %>_5" value="<%=cp%>">
					<input type="hidden" name="oculto<%=cont %>_6" value="<%=fax1 %>">
					<input type="hidden" name="oculto<%=cont %>_7" value="<%=fax2%>">
					<input type="hidden" name="oculto<%=cont %>_8" value="<%=correoElectronico %>">
					<input type="hidden" name="oculto<%=cont %>_9" value="<%=idPoblacion %>">
					<input type="hidden" name="oculto<%=cont %>_10" value="<%=idProvincia%>">
					<input type="hidden" name="oculto<%=cont %>_11" value="<%=idPais%>">
					<input type="hidden" name="oculto<%=cont %>_12" value="<%=idDireccion%>">					
					<input type="hidden" name="oculto<%=cont %>_13" value="<%=telefono1%>">					
					<input type="hidden" name="oculto<%=cont %>_14" value="<%=movil%>">					
					<input type="hidden" name="oculto<%=cont %>_15" value="<%=pobExt%>">					
				<td>				
					<%=tipoDireccion%>
				</td>
				<td>				
					<%=domicilio%>
				</td>
				<td>
					<%=pob %>
				</td>
				<td>				
					<%=provincia%>
				</td>
				<td>
					<%=pais %>
				</td>
				<td>				
					<%=cp%>
				</td>
				<td>
					<%=telefono1%>
				</td>
				<td>				
					<%=movil%>
				</td>
				<td>				
					<%=fax1%>
				</td>
				<td>
					<%=correoElectronico %>
				</td>

			</siga:FilaConIconos>

			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>


		<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
