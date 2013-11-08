<!DOCTYPE html>
<html>
<head>
<!-- operarMateria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsMateriaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash" %>
<%@ page import="com.siga.Utilidades.UtilidadesString" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Hashtable miHash = (Hashtable)ses.getAttribute("elegido");
	String accion = (String)ses.getAttribute("accion");
	ses.removeAttribute("accion");
	ses.removeAttribute("elegido");
	
	String area = (String)miHash.get(ScsMateriaBean.C_IDAREA);
	String institucion = (String)miHash.get(ScsMateriaBean.C_IDINSTITUCION);
	String materia = (String)miHash.get(ScsMateriaBean.C_IDMATERIA);
	String botones="";
	//Procedimientos de este Juzgado:
		Vector vJurisdicciones = (Vector)request.getAttribute("vJurisdicciones");
	//Boton borrar Procedimiento:
	FilaExtElement[] elems = null;
	if(!accion.equalsIgnoreCase("ver")){
	
		elems = new FilaExtElement[1];	
		elems[0]=new FilaExtElement("borrar","borrarJurisdiccion",SIGAConstants.ACCESS_FULL);
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
			<td class="titulitosDatos">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<siga:Idioma key="gratuita.operarMateria.literal.consultarMateria"/>
				<% } else {%>
					<siga:Idioma key="gratuita.operarMateria.literal.modificarMateria"/>
				<%}%>
			</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
		<html:form action="/DefinirAreasMateriasAction.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = "Modificar"/>
			<html:hidden property = "accion" value = "materia"/>
			<html:hidden property = "idArea" value = "<%=area%>"/>
			<html:hidden property = "idInstitucion" value ="<%=institucion%>"/>
			<html:hidden property = "idMateria" value ="<%=materia%>"/>
			<html:hidden property = "idJurisdiccion" />
			<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
			<html:hidden property = "fechaMod" value = "sysdate"/>
			<input type="hidden" name="actionModal" value="">
	
			<tr>		
				<td>			
	
					<!-- SUBCONJUNTO DE DATOS -->
					<siga:ConjCampos leyenda="gratuita.insertarArea.literal.materia">	
						<table class="tablaCampos" align="center" heigth="25">
							<tr>	
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaAreas.literal.nombreMateria"/>
								</td>					
								<td>
<% 
									String nombre = (String)miHash.get(ScsMateriaBean.C_NOMBRE);
									String contenido = (String)miHash.get(ScsMateriaBean.C_CONTENIDO);
									
									if (accion.equalsIgnoreCase("ver")) {
%>
										<html:text name="DefinirAreasMateriasForm" property="nombreMateria" size="30" styleClass="boxConsulta" readonly="true" value="<%=nombre%>" />
<% 
									} else {
%>
										<html:text name="DefinirAreasMateriasForm" property="nombreMateria" size="30" styleClass="box" value="<%=nombre%>" />
<%
									}
%>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.listadoAreas.literal.contenidoMateria"/>
								</td>	
								<td>
<%
									if (accion.equalsIgnoreCase("ver")) {
%>
										<textarea name="contenidoMateria" 
											style="overflow-y:auto; overflow-x:hidden; width:550px; height:80px; resize:none;" 
											readOnly="true" class="boxConsulta"><%=contenido%></textarea>
<% 
									} else {
%>
										<textarea name="contenidoMateria" 
											onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" 
											style="overflow-y:auto; overflow-x:hidden; width:550px; height:80px; resize:none;"
											class="box"><%=contenido%></textarea>
<%
									}
%>	
								</td>
							</tr>
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
		</html:form>	
	</table>
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
<%
	if (accion.equalsIgnoreCase("ver")){
	     botones="C";
	} else {
	   botones="G,R,C";
	}
%>
	<!-- FIN: BOTONES REGISTRO -->

	<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=accion%>" modal="m" titulo="gratuita.mantenimientoTablasMaestra.literal.jurisdicciones" clase="botonesSeguido"/>
			
	<siga:Table 
		name="tablaResultados"
		border="1"
		columnNames="gratuita.procedimientos.literal.Jurisdiccion,"
		columnSizes="90,10">
				
<% 
		if (vJurisdicciones != null && vJurisdicciones.size()>0) {
			for (int i = 0; i < vJurisdicciones.size(); i++) { 
				Hashtable hash = (Hashtable)vJurisdicciones.get(i);
				if (hash != null) {
					String descripcion = UtilidadesHash.getString (hash, "DESCRIPCION");
					String idJurisdiccion = UtilidadesHash.getString (hash, "IDJURISDICCION");
					String idArea = UtilidadesHash.getString (hash, "IDAREA");
					String idMateria = UtilidadesHash.getString (hash, "IDMATERIA");
					String idInstitucion = UtilidadesHash.getString (hash, "IDINSTITUCION");
%>
					<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' pintarEspacio="false" visibleConsulta="no" visibleEdicion="no" visibleBorrado="no" botones=''  elementos="<%=elems%>"  modo="<%=accion%>" clase="listaNonEdit">
						<td>
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=idJurisdiccion%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=idInstitucion%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_3" value="<%=idArea%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_4" value="<%=idMateria%>">
													
						   	<%=UtilidadesString.mostrarDatoJSP(descripcion)%>
						</td>											
					</siga:FilaConIconos>
<%
				}
			}
		} else {
%>				
			<tr class="notFound">
	  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
<%
		}
%>	
	</siga:Table>
	<!-- FIN: CAMPOS -->

<% 
	if (!accion.equalsIgnoreCase("nuevo")) {
%>
		 	<siga:ConjBotonesAccion botones="N" modal="M" modo="<%=accion%>" clase="botonesDetalle" />
<% 
	} 
%>
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->	
	<script language="JavaScript">	
	
		//Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar
		function accionGuardar() {	
			var nombre = document.forms[0].nombreMateria.value;
			var contenido = document.forms[0].contenidoMateria.value;
			if ((nombre.length <= 60) && (nombre != "")) {
				if (contenido.length<=4000) {
					window.top.returnValue="MODIFICADO";
					document.forms[0].submit();
				}
				else alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudDescripcion"/>');
			}
			else if (nombre == "") alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoNombre"/>');
			else alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudNombre"/>');		
		} 
				
		//Asociada al boton Cerrar
		function accionCerrar() {
			top.cierraConParametros("NORMAL");			
		}
		
		//Asociada al boton Nuevo
		function accionNuevo() {		
			document.forms[0].modo.value = "nuevaJurisdiccionModal";
			var resultado = ventaModalGeneral(document.forms[0].name,"P");
			if (resultado && resultado=='MODIFICADO'){
				refrescarLocal();
			}	
		}	
		
		function borrarJurisdiccion(fila) {				
			seleccionarFila(fila);
			
			document.forms[0].modo.value = "borrarJurisdiccion";
			document.forms[0].submit();
		}	
		
		function refrescarLocal() {		
			document.forms[0].modo.value="editar";
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}
		
		//Guardo los campos seleccionados
		function seleccionarFila(fila){		  
		    var idJurisdiccion = 'oculto' + fila + '_' + 1;
			var idInstitucion = 'oculto' + fila + '_' + 2;
		    var idArea = 'oculto' + fila + '_' + 3;
		    var idMateria = 'oculto' + fila + '_' + 4;
		    
			//Datos del elemento seleccionado:
			document.forms[0].idJurisdiccion.value = document.getElementById(idJurisdiccion).value;
			document.forms[0].idInstitucion.value = document.getElementById(idInstitucion).value;
			document.forms[0].idArea.value = document.getElementById(idArea).value;
			document.forms[0].idMateria.value = document.getElementById(idMateria).value;			
		}		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>