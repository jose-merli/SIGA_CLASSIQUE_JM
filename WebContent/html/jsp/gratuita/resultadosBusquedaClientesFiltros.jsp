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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.gratuita.form.BusquedaClientesFiltrosForm"%>

<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="java.util.HashMap" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idInstitucion = usrbean.getLocation();
	BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesFiltrosForm");
	String titu = "censo.busquedaClientes.literal.titulo";
	String sConcepto = "";
	BusquedaClientesFiltrosForm miForm = (BusquedaClientesFiltrosForm)request.getAttribute("busquedaClientesFiltrosForm");
	if (miForm != null)
		sConcepto = miForm.getConcepto();
		
	if (sConcepto == null) 
		sConcepto = new String ("");
	
	/** PAGINADOR ***/

	Vector resultado          = new Vector();
	String idioma             = usrbean.getLanguage().toUpperCase();
	String action             = app + "/JGR_BusquedaClientesFiltros.do?noReset=true";
	String totalRegistros     = "0";
	String paginaSeleccionada = "0";
	String registrosPorPagina = "0";
	HashMap hm = (HashMap)ses.getAttribute("DATAPAGINADOR_VECTOR");

	if (hm != null && hm.get("datos")!=null && !hm.get("datos").equals("")){
		resultado = (Vector)hm.get("datos");
		Paginador paginador = (Paginador)hm.get("paginador");
		totalRegistros      = String.valueOf(paginador.getNumeroTotalRegistros());
		paginaSeleccionada  = String.valueOf(paginador.getPaginaActual());
		registrosPorPagina  = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
		if (sConcepto == null || sConcepto.equals("")){
			sConcepto = (String) hm.get("concepto");
			
		}	
    }
	
    /**************/
%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<html:javascript formName="/JGR_BusquedaClientesFiltros.do" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="<%=titu %>" localizacion="<%=titu %>"/>


  <script language="JavaScript">
	
	function seleccionar(fila) {
		var persona=document.getElementById('oculto' + fila + '_1').value;
		var colegiado=document.getElementById('oculto' + fila + '_2').value;
		var nombre=document.getElementById('oculto' + fila + '_3').value;
		var apellido1=document.getElementById('oculto' + fila + '_4').value;
		var apellido2=document.getElementById('oculto' + fila + '_5').value;
		var salto=document.getElementById('oculto' + fila + '_6').value;
		var compensacion=document.getElementById('oculto' + fila + '_7').value;
		var turnoLetrado=document.getElementById('oculto' + fila + '_8').value;
		var guardiaLetrado=document.getElementById('oculto' + fila + '_9').value;
		var sustitucion=document.getElementById('oculto' + fila + '_10').value;
		//alert("<->"+sustitucion+"<-->");
		var vForm=document.forms[1];
		vForm.idPersona.value=persona;
		vForm.sustituta.value=sustitucion;
		//alert("<->"+vForm.name+"<-->");
		var res = ventaModalGeneral(vForm.name,"P");
		
		if(res=="MODIFICADO"){
			aa = new Array(persona,colegiado,nombre, apellido1, apellido2,salto,compensacion,turnoLetrado,guardiaLetrado,sustitucion);
			window.returnValue= aa;
			alert("<siga:Idioma key="gratuita.busquedaSJCS.literal.seleccionadoA"/> "+nombre+" "+apellido1+" "+apellido2);
			window.close();
		}
	}
	
  </script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_BusquedaClientesFiltros.do" method="POST" target="submitArea"  style="display:none">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		<form name="aux" action="<%=app%>/JGR_BusquedaClientesFiltros.do" method="post" style="display:none">
			<input type="hidden" name="actionModal" value="BusquedaClientesFiltrosForm"/>
			<input type="hidden" name="idInstitucion" value="<%=idInstitucion%>"/>
			<input type="hidden" name="idPersona" value=""/>
			<input type="hidden" name="sustituta" value=""/>
			<input type="hidden" name="modo" value="editar"/>
		</form>
		
<%		String tamanosCol="";
		String nombresCol="";%>
					
		
<%		if (sConcepto!=null && !sConcepto.equals("") && !sConcepto.equals("DESIGNACION")){
        tamanosCol="3,9,9,22,18,14,10,8";//turno, guardia y posicion
		nombresCol=" ,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,censo.busquedaClientesAvanzada.literal.nombre,gratuita.busquedaEJG.literal.turno,gratuita.busquedaEJG.literal.guardia,sjcs.letradoguardia.telefono,gratuita.busquedaSJCS.literal.guardiasPendientes,";
		}else{
		tamanosCol="4,10,10,34,18,16,4";//turno
		nombresCol=" ,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,censo.busquedaClientesAvanzada.literal.nombre,gratuita.busquedaEJG.literal.turno,";
		}
%>
			
		

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol%>"
		   tamanoCol="<%=tamanosCol%>"
		   alto="100%"
		   ajustePaginador="true"	>

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.isEmpty()) { %>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>	
<%	
	} else { 
	
		FilaExtElement[] elems=new FilaExtElement[1];
		elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	

		// recorro el resultado
		
		// int rownumOld = 0;
		int contador = Integer.parseInt(registrosPorPagina) * (Integer.parseInt(paginaSeleccionada) - 1);
		for (int i=0;i<resultado.size();i++) {
		 
			Row registro = (Row) resultado.get(i);
			String cont = new Integer(i+1).toString();
			
			// calculo de campos
			String idPersona = (registro.getString(CenColegiadoBean.C_IDPERSONA)==null||((String)registro.getString(CenColegiadoBean.C_IDPERSONA)).equals(""))?"&nbsp;":(String)registro.getString(CenColegiadoBean.C_IDPERSONA);
			int rownum = new Integer(registro.getString("N")).intValue();
		/********************	
			if (rownum!=rownumOld){//Como el rownum puede dar distintos valores, 
			                       //cuando el valor es el mismo se incrementa el contador y cuando cambia se inicializa de nuevo el contador a 0,
			 contador=0;
			}
			contador++;
		**********************/	
			
			
			String nif = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NIFCIF));
			String ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
			String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
			String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
			String turno = UtilidadesString.mostrarDatoJSP(registro.getString("TURNO"));
			String guardia = UtilidadesString.mostrarDatoJSP(registro.getString("GUARDIA"));
			String posicion = UtilidadesString.mostrarDatoJSP(registro.getString("POSICION"));
			String salto = UtilidadesString.mostrarDatoJSP(registro.getString("SALTO"));
			String idTurnoLetrado = UtilidadesString.mostrarDatoJSP(registro.getString("IDTU"));
			String idGuardiaLetrado = UtilidadesString.mostrarDatoJSP(registro.getString("IDGU"));
			String sustitucion = UtilidadesString.mostrarDatoJSP(registro.getString("GUARDIA_SUSTITUCION"));			
			
			String compensacion = UtilidadesString.mostrarDatoJSP(registro.getString("COMPENSACION"));
			String telefono = UtilidadesString.mostrarDatoJSP(registro.getString("TELEFONO"));%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
			
			<siga:FilaConIconos fila="<%=cont %>" botones="" modo="consulta" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="false">
			
				<td class='tableTitlePrimero'>
					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idPersona %>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=ncolegiado %>">
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=nombre %>">
					<input type="hidden" name="oculto<%=cont %>_4" value="<%=apellido1 %>">
					<input type="hidden" name="oculto<%=cont %>_5" value="<%=apellido2 %>">
	
					<%if(i==0 &&sConcepto.equals("DESIGNACION")){%>
						<input type="hidden" name="oculto<%=cont %>_6" value="0">
					<%}else{%>
						<input type="hidden" name="oculto<%=cont %>_6" value="<%=salto%>">
					<%}%>

					<input type="hidden" name="oculto<%=cont %>_7" value="<%=compensacion%>">
					
					<input type="hidden" name="oculto<%=cont %>_8" value="<%=idTurnoLetrado %>">
					<input type="hidden" name="oculto<%=cont %>_9" value="<%=idGuardiaLetrado %>">
					<input type="hidden" name="oculto<%=cont %>_10" value="<%=sustitucion%>">					
					<%=contador+i+1%>
				</td>
				<td>
					<%=nif %>
				</td>
				<td>
					<%=ncolegiado %>
				</td>
				<td>
					<%=apellido1+" "+apellido2+", "+nombre %>
				</td>
				<td>
					<%=turno%>
				</td>
		
	<%	if (sConcepto!=null && !sConcepto.equals("") && !sConcepto.equals("DESIGNACION")){%>
				<td>
					<%=guardia%>
				</td>
				<td>
					<%=telefono%>
				</td>				
				<td>
					<%=posicion%>
				</td>
	<%}%>			
		


			</siga:FilaConIconos>		

          
			<!-- FIN REGISTRO -->
<%		// rownumOld=rownum;
         } // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:TablaCabecerasFijas>

	<!-- FIN: LISTA DE VALORES -->

	 <%if (hm.get("datos")!=null && !hm.get("datos").equals("")){%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						idioma="<%=idioma%>"
						modo="buscarPaginador"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
						distanciaPaginas=""
						action="<%=action%>" />
	 <% } %>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
