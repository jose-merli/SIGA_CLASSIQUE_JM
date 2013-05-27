<!-- informeJustificacionMasivaResultados.jsp -->

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

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Vector v = (Vector)request.getAttribute("resultado");
	
	
	String letrado = ((String)request.getAttribute("letrado")==null)?"":(String)request.getAttribute("letrado");
	String fecha   = ((String)request.getAttribute("fecha")  ==null)?"":(String)request.getAttribute("fecha");
%>	



<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	
</head>

<body onload="deshabilitarCabecera()">

		<html:form action="/JGR_InformeJustificacionMasiva.do" method="post" target="submitArea" >
			<html:hidden property="modo"    value=""/>
			
			<html:hidden property="letrado" value="<%=letrado%>"/>

			<input type="hidden" name="actionModal" value="">
			
			
			<div>
			<table width="100%" border="0">
			<tr>
				<td width="75%">
					&nbsp;
				</td>
				<td class="labelText">					
					<siga:Fecha nombreCampo="fecha"></siga:Fecha>
				</td>
			</tr>
				
			</table>
			</div>
			</html:form>
			
		
			
		
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="gratuita.informeJustificacionMasiva.literal.orden,gratuita.informeJustificacionMasiva.literal.expedienteCausa,gratuita.informeJustificacionMasiva.literal.juzgado,gratuita.informeJustificacionMasiva.literal.fechaSalida,gratuita.informeJustificacionMasiva.literal.asunto,gratuita.informeJustificacionMasiva.literal.cliente,gratuita.informeJustificacionMasiva.literal.categoria,gratuita.informeJustificacionMasiva.literal.fechaInicio,gratuita.informeJustificacionMasiva.literal.finalizacion,gratuita.informeJustificacionMasiva.literal.baja"
		   columnSizes="7,10,15,8,10,20,4,11,11,4">
				
		<% if (v == null || v.size()==0){%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
		<% } else { %>
		
			  <%
		    	
				String[] datoJuzgado = {usr.getLocation()};	

				for (int i = 0; i < v.size(); i++)
				{	
					Hashtable hash = (Hashtable)v.get(i);

					String clase = "";					
					String idInstitucion = " ", idTurno = " ", anio = " ", numero = " ", idJuzgado = " ",idInstJuzgado=" ", 
					       fDesigna=" ",fFinAct=" ", fIniAct=" ", estado=" ", cliente=" ",expedientes=" ",
					       codigoDesigna="", asunto=" ", categoria=" ",idJurisdiccion="",idProcedimiento=" ",descJuzgado=" ";
					String idPersona = "";
					String numJustificaciones = "";
					

					
					
					
						anio = UtilidadesHash.getString(hash, "ANIO");
						numero = UtilidadesHash.getString(hash, "NUMERO");
						idTurno = UtilidadesHash.getString(hash, "IDTURNO");
						idInstitucion = UtilidadesHash.getString(hash, "IDINSTITUCION");
						idJuzgado = UtilidadesHash.getString(hash, "IDJUZGADO");
						descJuzgado = UtilidadesHash.getString(hash, "DESC_JUZGADO");
						idInstJuzgado = UtilidadesHash.getString(hash, "IDINSTITUCION_JUZG");
						fDesigna = UtilidadesHash.getString(hash, "FECHADESIGNA");
						fFinAct = UtilidadesHash.getString(hash, "ACREDITACION_FIN");
						fIniAct = UtilidadesHash.getString(hash, "ACREDITACION_INI");
						estado = UtilidadesHash.getString(hash, "ESTADO");
						expedientes = UtilidadesHash.getString(hash, "EXPEDIENTES");
						cliente = UtilidadesHash.getString(hash, "CLIENTE");
						codigoDesigna = UtilidadesHash.getString(hash, "CODIGO");
						asunto = UtilidadesHash.getString(hash, "ASUNTO");
						categoria = UtilidadesHash.getString(hash, "CATEGORIA");
						idJurisdiccion = UtilidadesHash.getString(hash, "IDJURISDICCION");
						idProcedimiento = UtilidadesHash.getString(hash, "IDPROCEDIMIENTO");
						idPersona =  UtilidadesHash.getString(hash, "IDPERSONA");
						numJustificaciones = UtilidadesHash.getString(hash, "NUMJUSTIFICACIONES");

							
					
					if ((i%2) == 0)
						clase = "filaTablaPar";
					else 
						clase = "filaTablaImpar";
						
			 	%>	

		  		<tr class="<%=clase%>">
					<td>
						<input type="hidden" name="modificado<%=i%>"   value="false">
						<input type="hidden" name="idInstitucion<%=i%>" value="<%=idInstitucion%>">
						<input type="hidden" name="idTurno<%=i%>"      value="<%=idTurno%>">
						<input type="hidden" name="anio<%=i%>"         value="<%=anio%>">
						<input type="hidden" name="numero<%=i%>"       value="<%=numero%>">
						<input type="hidden" name="idProcedimiento<%=i%>"  value="<%=idProcedimiento%>">
						<input type="hidden" name="idJuzgado<%=i%>"  value="<%=idJuzgado%>">
						<input type="hidden" name="idInstJuzgado<%=i%>"  value="<%=idInstJuzgado%>">
						<input type="hidden" name="fIniAct<%=i%>" value="<%=GstDate.getFormatedDateShort("",fIniAct)%>">
						<input type="hidden" name="idJurisdiccion<%=i%>"  value="<%=idJurisdiccion%>">
						<input type="hidden" name="idPersona<%=i%>"  value="<%=idPersona%>">
						<input type="hidden" name="codigoDesigna<%=i%>"  value="<%=codigoDesigna%>">
						<input type="hidden" name="numJustificaciones<%=i%>"  value="<%=numJustificaciones%>">
						
						<%=UtilidadesString.mostrarDatoJSP(codigoDesigna)%>
					</td>
					
					<td><%if (expedientes != null && expedientes.indexOf("##") > -1) {
							String[] ejgs = expedientes.split(",");
							String salida = "";
							for (String ejg:ejgs) {
								String[] ejgDoc = ejg.split("##");
								
								salida+=", " + ejgDoc[0].trim();	
								
							}
							expedientes=salida;
							if (expedientes.length() > 2){
								expedientes = expedientes.substring(1);
							}
						}%>
						<%=(expedientes==null || expedientes.trim().equals(""))?"&nbsp":expedientes%>
					</td>
					
					<td>
					<%=UtilidadesString.mostrarDatoJSP(descJuzgado)%>
					 	
					</td>				
					
					<td align="center">
					
						<%=GstDate.getFormatedDateShort("",fDesigna)%>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(asunto)%>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(cliente)%>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(categoria)%>
					</td>
					
					<% if (idJuzgado == null ||  idJuzgado.equals("")) {%>
						<td colspan="2" align="center">
							<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.sinJuzgado"/>
						</td>
					<%	}else  if (idProcedimiento == null ||  idProcedimiento.equals("")) {%>
							<td colspan="2" align="center">
							<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.sinModulo"/>
						</td>
					 
					<%	}else {%> 
					<td align="center">
						<% if (fIniAct != null && !fIniAct.equals("")) { %> 
							&nbsp;<%=GstDate.getFormatedDateShort("",fIniAct)%>
						<% }  else { %>
							<input type="checkBox" name="checkFechaIni<%=i%>" value="checkFechaIni" onClick="accionCheckInicio('<%=i%>');"  <% if (estado != null && estado.equals("F")) out.print("disabled "); %> >
							<% }%>
					</td>

					<td align="center" >
						<% if (fFinAct != null && !fFinAct.equals("")) { %> 
							&nbsp;<%=GstDate.getFormatedDateShort("",fFinAct)%>
						<% }  else { %>
							<input type="checkBox" name="checkFechaFin<%=i%>" value="checkFechaFin" onClick="accionCheckFin('<%=i%>');"   <% if (estado != null && estado.equals("F")) out.print("disabled "); %> >
						<% }%>
					</td>
					<%}%>
					<td align="center">
						<input type="checkBox" name="baja<%=i%>" value="baja"  onclick="accionCheckBaja('<%=i%>');" <% if (estado != null && estado.equals("F")) out.print("checked disabled "); %> >
					</td>
				</tr>
					
			<% 	} // for%>	
		<%}%>

	</siga:Table>
	

	

<!-- FIN: LISTA DE VALORES -->
		

	<script language="JavaScript">


		function deshabilitarCabecera () 
		{
/*			<% if (v != null && v.size() > 0) { %>
				parent.document.InformeJustificacionMasivaForm.numeroNifTagBusquedaPersonas.disabled = true;
				parent.document.InformeJustificacionMasivaForm.nombrePersona.disabled = true;
				parent.document.InformeJustificacionMasivaForm.buscarCliente.onclick = "";
				parent.document.InformeJustificacionMasivaForm.limpiar.onclick = "";
				parent.document.InformeJustificacionMasivaForm.fecha.disabled = true;
				parent.document.getElementById("iconoCalendarioA").onclick = "";
			<% } %>
*/		}
	
		function refrescarLocal() 
		{
			parent.buscar();
		} 
		
		function accionCheckFin(fila){
			
	
			objCheckFechaIni  = document.getElementById ("checkFechaIni"+fila);
			objCheckFechaFin  = document.getElementById("checkFechaFin"+fila);
			objModificado = document.getElementById("modificado" + fila);
			objBaja =  document.getElementById ("baja"+fila);
			
			objModificado.value = objCheckFechaFin.checked;
			if(objCheckFechaIni!=null){
				objCheckFechaIni.checked = objCheckFechaFin.checked;
				
			}
			if(objCheckFechaFin.checked==true)
					objBaja.checked = false;
			
			objBaja.disabled = objCheckFechaFin.checked;
		}
		function accionCheckInicio(fila){
			objCheckFechaIni  = document.getElementById ("checkFechaIni"+fila);
			objCheckFechaFin  = document.getElementById("checkFechaFin"+fila);
			objModificado = document.getElementById("modificado" + fila);
			objBaja =  document.getElementById ("baja"+fila);

			objModificado.value =objCheckFechaIni.checked;

			
			if(objCheckFechaIni.checked==true)
					objBaja.checked = false;
			else
				objCheckFechaFin.checked=false;
			
			objBaja.disabled = objCheckFechaIni.checked || objCheckFechaFin.checked;

		}
		
		
		function accionCheckBaja(fila)
		{
			objCheckFechaIni  = document.getElementById ("checkFechaIni"+fila);
			objCheckFechaFin  = document.getElementById("checkFechaFin"+fila);
			objModificado = document.getElementById("modificado" + fila);
			objBaja =  document.getElementById ("baja"+fila);
		
		
			objModificado.value = objBaja.checked;
			
			if(objCheckFechaIni!=null){
				if(objBaja.checked==true)
					objCheckFechaIni.checked = false;
				objCheckFechaIni.disabled = objBaja.checked;
					
				
			}
			if(objCheckFechaFin!=null){
				if(objBaja.checked==true)
						objCheckFechaFin.checked = false;
						objCheckFechaFin.disabled = objBaja.checked;
						
					
				
			}
		}
		
		
		
		
		function accionNuevo () 
		{
		
			filas = 0;
			<% if (v!=null) { %>
				filas = eval("<%=v.size()%>");
			<% } %>

			flag = false;			
			for (i = 0; i < filas && flag == false; i++) {
				modificado = document.getElementById ("modificado"+i).value;
				if (modificado == "false") 
					continue;
				else 
					flag = true;
			}
		
			if (!flag || confirm("<%=UtilidadesString.getMensajeIdioma (usr, "gratuita.informeJustificacionMasiva.mensaje.nuevoImpreso")%>")) {
				document.InformeJustificacionMasivaForm.target = "mainWorkArea";
				document.InformeJustificacionMasivaForm.modo.value = "nuevo";
				document.InformeJustificacionMasivaForm.submit();
			}
		}

		function accionGuardar () 
		{

			sub();
			filas = 0;
			<% if (v!=null) { %>
				filas = eval("<%=v.size()%>");
			<% } %>
			
			datos = "";
						
			for (i = 0; i < filas; i++) {

				modificado = document.getElementById ("modificado"+i).value;
				objCheckFechaIni  = document.getElementById ("checkFechaIni"+i);
				objFechaIni  = document.getElementById ("fIniAct"+i);
				objCheckFechaFin  = document.getElementById ("checkFechaFin"+i);
				
				if (modificado == "false") 
					continue;
				
				idInstitucion = document.getElementById ("idInstitucion"+i).value;
				
				idTurno = document.getElementById ("idTurno"+i).value;
				
				anio = document.getElementById ("anio"+i).value;
				
				numero = document.getElementById ("numero"+i).value;
				
				idProcedimiento = document.getElementById ("idProcedimiento"+i).value;
				idJuzgado = document.getElementById ("idJuzgado"+i).value;
				idInstJuzgado = document.getElementById ("idInstJuzgado"+i).value;
				idJurisdiccion = document.getElementById ("idJurisdiccion"+i).value;
				idPersona = document.getElementById ("idPersona"+i).value;
				codigoDesigna = document.getElementById ("codigoDesigna"+i).value;
				numJustificaciones = document.getElementById ("numJustificaciones"+i).value;
				fIniAct = " ";
				if(objFechaIni!=null)
					fIniAct = document.getElementById ("fIniAct"+i).value;
				
				
				var isCheckFechaIni = "false";
				var isCheckFechaFin = "false";
				if(idProcedimiento ==""){
					idProcedimiento= " ";
					isCheckFechaIni = "false";
					isCheckFechaFin = "false"
				}else{
				
					if(objCheckFechaIni!=null)
						isCheckFechaIni= document.getElementById ("checkFechaIni"+i).checked;
					
					if(objCheckFechaFin!=null)
						isCheckFechaFin= document.getElementById ("checkFechaFin"+i).checked;
					
					
				
				}
				
				
				
				if(document.getElementById ("baja"+i).checked) 
					baja = "si";
				else 
					baja = "no";
			    
			    if(idJuzgado=="")
				    idJuzgado = " ";
				   
				if(idInstJuzgado=="")
				    idInstJuzgado = " ";
				 if(fIniAct=="")
				    fIniAct = " ";
				 if(idJurisdiccion=="")
				    idJurisdiccion = " ";
				    
				    
				datos = datos + anio + "," + 
								numero + "," + 
								idInstitucion + "," + 
								idTurno + "," + 
								idJuzgado + "," + 
								idInstJuzgado + "," + 
								isCheckFechaIni + "," + 
								isCheckFechaFin + "," + 
								idProcedimiento + "," + 
								fIniAct + "," + 
								idJurisdiccion + "," + 
								idPersona + "," +
								codigoDesigna + "," +
								numJustificaciones + "," +
								
								baja + "#";
				
				
				if(baja=="no" && document.InformeJustificacionMasivaForm.fecha.value==""){
					alert ("<siga:Idioma key="gratuita.informeJustificacionMasiva.mensaje.aviso.fechaRequerida"/>");
					fin();
					return;
				}
			
								 
			}
			
			
			if (datos == "") {
				alert ("<siga:Idioma key="gratuita.informeJustificacionMasiva.mensaje.noDatosGuardar"/>");
				fin();
				return;
			}
			
			document.InformeJustificacionMasivaForm.tablaDatosDinamicosD.value = datos;
			document.InformeJustificacionMasivaForm.modo.value = "modificar";
			document.InformeJustificacionMasivaForm.submit();
		}
		
	</script>		
	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
		<siga:ConjBotonesAccion botones="g" clase="botonesDetalle"/>
		
	<!-- FIN: BOTONES BUSQUEDA -->

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>

</html>
		  
		
