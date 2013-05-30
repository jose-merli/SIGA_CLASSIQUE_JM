<!-- detalleRegistroGruposUsuario.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
	request.removeAttribute("nuevo");

	AdmPerfilBean bean = (AdmPerfilBean)datos.elementAt(0);
	Vector vRoles = (Vector)datos.elementAt(1);
	
	Vector vRolesNO = new Vector();
	Vector vRolesSI = new Vector();
	
	for (int i=0; i<vRoles.size(); i++)
	{
		Vector vAux = new Vector();
		
		vAux.add(((AdmRolBean)vRoles.elementAt(i)).getIdRol());
		vAux.add(((AdmRolBean)vRoles.elementAt(i)).getDescripcion());
		
		vRolesNO.add(vAux);
	}

	String idsRoles = bean.getIdsRoles();
	String descRoles = bean.getDescRoles();
	String rolesAntiguos="";	
	
	if (idsRoles!=null && descRoles!=null)
	{
		StringTokenizer st1 = new StringTokenizer(idsRoles, ",");
		StringTokenizer st2 = new StringTokenizer(descRoles, ",");
		
		while (st1.hasMoreTokens())
		{
			String myIdRol = st1.nextToken();
			String myDescRol = st2.nextToken();

			Vector vAux = new Vector();
			
			vAux.add(myIdRol);
			vAux.add(myDescRol);
			
			vRolesSI.add(vAux);
			
			rolesAntiguos += myIdRol + ","; // Contiene los roles anteriores a la edición
			
			for (int i=0; i<vRolesNO.size(); i++)
			{
				String idRolAux = (String)((Vector)vRolesNO.elementAt(i)).elementAt(0);
				
				if (idRolAux.equals(myIdRol))
				{
					vRolesNO.remove(i);
					
					i=vRolesNO.size();
				}
			}
		}
	}
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				listadoGruposUsuarioForm.reset();
				
				oRolesCON = document.getElementById("rolesCON");
				oRolesSIN = document.getElementById("rolesSIN");
				
				var tamC = oRolesCON.length;
				
				for (i=0; i<tamC; i++)
				{
					oRolesCON.remove(0);
				}
				
<%
			for (int i=0; i<vRolesSI.size(); i++)
			{
%>			
				var codigo = "<%=((Vector)vRolesSI.elementAt(i)).elementAt(0)%>";
				var descripcion = "<%=((Vector)vRolesSI.elementAt(i)).elementAt(1)%>";
					
				oRolesCON.options[oRolesCON.options.length] = new Option(descripcion, codigo);
<%
			}
%>
	
				var tamS = oRolesSIN.length;
				
				for (i=0; i<tamS; i++)
				{
					oRolesSIN.remove(0);
				}
	
<%
			for (int i=0; i<vRolesNO.size(); i++)
			{
%>			
				var codigo = "<%=((Vector)vRolesNO.elementAt(i)).elementAt(0)%>";
				var descripcion = "<%=((Vector)vRolesNO.elementAt(i)).elementAt(1)%>";
					
				oRolesSIN.options[oRolesSIN.options.length] = new Option(descripcion, codigo);
<%
			}
%>
			}
			
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				if (document.getElementById("perfil").value == ''){
					var mensaje = '<siga:Idioma key="administracion.grupos.literal.id"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();	
					return false;
				}else{
					if (document.getElementById("perfil").value == ''){
						alert('<siga:Idioma key="messages.consultas.error.descripcion"/>');
						fin();
						return false;	
					}else {
						document.all.listadoGruposUsuarioForm.roles.value="";
						var rolesC = document.getElementById("rolesCON");
						
						for (i=0; i<rolesC.length; i++)
						{
							document.all.listadoGruposUsuarioForm.roles.value += rolesC.options[i].value + ",";
							
						}
			
						listadoGruposUsuarioForm.submit();
			
						window.top.returnValue="MODIFICADO";
					}
				}
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<script>
			function ponerRol()
			{
				var rolesC = document.getElementById("rolesCON");
				var rolesS = document.getElementById("rolesSIN");
				
				while (rolesS.selectedIndex!=-1)
				{
					var codigo = rolesS.options[rolesS.selectedIndex].value;
					var descripcion = rolesS.options[rolesS.selectedIndex].text;
					
					rolesC.options[rolesC.options.length] = new Option(descripcion, codigo);
					rolesS.remove(rolesS.selectedIndex);
				}
			}
			
			function quitarRol()
			{
				var rolesC = document.getElementById("rolesCON");
				var rolesS = document.getElementById("rolesSIN");
				
				while (rolesC.selectedIndex!=-1)
				{
					var codigo = rolesC.options[rolesC.selectedIndex].value;
					var descripcion = rolesC.options[rolesC.selectedIndex].text;
					
					rolesS.options[rolesS.options.length] = new Option(descripcion, codigo);
					rolesC.remove(rolesC.selectedIndex);
				}
			}
		</script>	
	</head>

	<body>
	
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="administracion.grupos.titulo"/>
			</td>
		</tr>
	</table>		
		<div id="camposRegistro" class="posicionModalMedia" align="center">
			<html:form action="/ADM_GestionarGruposUsuario.do" method="POST" target="submitArea">
<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "roles" value = ""/>
				<input type="hidden" name="rolesAntiguos" value = "<%=rolesAntiguos%>"/>

				<table class="tablaCentralCamposMedia" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.grupos.titulo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.grupos.literal.id"/>&nbsp;(*)
										</td>
										<td class="labelTextValue">
<%
										if (bNuevo)
										{
%>
											<html:text value="<%=bean.getIdPerfil()%>" property="perfil" styleClass="box" size="3" maxlength="3"/>
<%
										}
										
										else
										{
%>
											<%=bean.getIdPerfil()%>
											<html:hidden property = "perfil" value = "<%=bean.getIdPerfil()%>"/>
<%
										}
%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.grupos.literal.descripcion"/>&nbsp;(*)
										</td>				
										<td class="labelTextValue">
<%
										if (bEditable)
										{
%>
											<html:text value="<%=bean.getDescripcion()%>" property="descripcion" styleClass="box" size="70" maxlength="100"/>
<%
										}
										
										else
										{
%>
											<%=bean.getDescripcion()%>
<%
										}
%>
										</td>
									</tr>
									<tr>
										<td colspan=2 class="labelText">
											<table align=center>
												<tr>
													<td class="labelText"><siga:Idioma key="administracion.grupos.literal.rolesAsignados"/></td>
													<td>&nbsp;</td>
<%
													if (bEditable)
													{
%>
													<td class="labelText"><siga:Idioma key="administracion.grupos.literal.rolesSinAsignar"/></td>
<%
													}
%>
												</tr>
												<tr>
													<td class="labelText">
														<select size="8" class="boxCombo" id="rolesCON" multiple>
<%
													for (int i=0; i<vRolesSI.size(); i++)
													{
														String myIdRol = (String)((Vector)vRolesSI.elementAt(i)).elementAt(0);
														String myDescRol = (String)((Vector)vRolesSI.elementAt(i)).elementAt(1);
%>
															<option value="<%=myIdRol%>"><%=myDescRol%></option>
<%
													}
%>
														</select>
													</td>
													<script>document.getElementById("rolesCON").style.width="200";</script>
													<td>
<%
													if (bEditable)
													{
%>
														<img src="<%=app%>/html/imagenes/flecha_izquierda.gif" onClick="ponerRol();" style="cursor:hand;">&nbsp;&nbsp;&nbsp;<img src="<%=app%>/html/imagenes/flecha_derecha.gif" onClick="quitarRol();" style="cursor:hand;">
													</td>
													<td class="labelText">
														<select size="8" class="boxCombo" id="rolesSIN" multiple>
<%
													for (int i=0; i<vRolesNO.size(); i++)
													{
														String myIdRol = (String)((Vector)vRolesNO.elementAt(i)).elementAt(0);
														String myDescRol = (String)((Vector)vRolesNO.elementAt(i)).elementAt(1);
%>
															<option value="<%=myIdRol%>"><%=myDescRol%></option>
<%
													}
%>
														</select>
													</td>
													<script>document.getElementById("rolesSIN").style.width="200";</script>
<%
													}
													
													else
													{
%>
													</td>
<%
													}
%>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
			String botones = bEditable ? "R,Y,C" : "C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>