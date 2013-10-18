<!--ficheros.jsp -->
<bean:define id="name" name="org.apache.struts.action.mapping.instance" property="name" scope="request"/>
<bean:define id="permisoFicheros" name="permisoFicheros"  scope="request"/>
<bean:define id="action" name="accionModo" scope="request"/>
<bean:define id="FicheroForm" name="${name}" scope="request"/>
<bean:define id="action" name="accionModo" scope="request"/>
<c:choose>
	<c:when test="${permisoFicheros=='Acceso Total'||permisoFicheros=='Sólo Lectura'}">


<div id ="divFicheros" style="display:none">
	<siga:ConjCampos leyenda="administracion.informes.literal.archivo">
		<c:choose>
			<c:when test="${FicheroForm.descripcionArchivo==null }">
				<c:choose>
					<c:when test="${action=='ver'}">
						<table>
							<tr class='titulitos' id="noRecordFound">
								<td class="titulitos"
									style="background-color: transparent; text-align: center;"
									colspan="4"><siga:Idioma key="messages.noRecordFound" /></td>
							</tr>
						</table>
					</c:when>
					<c:otherwise>
						<div>
							<table>
								<tr>
									<td colspan="4">&nbsp;</td>

								</tr>
								
								<tr>
									<td class="labelText"><siga:Idioma
											key="administracion.informes.literal.archivo" /></td>
									<td><html:file property="theFile" styleClass="boxCombo" style="width:500px;" />
									</td>
									<td class="labelText">&nbsp;</td>

									<td class="tdBotones">
										<!--  <input type="button" alt="<siga:Idioma key="administracion.informes.boton.archivo.anadir"/>" id="idButton" onclick="return upload();" class="button" value="<siga:Idioma key="administracion.informes.boton.archivo.anadir"/>">-->

									</td>
								</tr>
								<tr>
									<td colspan="4">&nbsp;</td>

								</tr>
							</table>
						</div>
					</c:otherwise>
				</c:choose>
			</c:when>

			<c:otherwise>
				<div>
					<table id='listadoArchivosCab' border='1' width='100%'
						cellspacing='0' cellpadding='0'>
						<tr>
							<td width='30%'></td>
							<td width='30%'></td>
							<td width='30%'></td>
							<td width='10%'></td>
						</tr>
						<tr class='tableTitle'>
							<td align='center' width='30%'><b><siga:Idioma
										key="administracion.informes.literal.archivo.nombre" /></b></td>
							<td align='center' width='30%'><b>Extension</b></td>
							<td align='center' width='30%'><b><siga:Idioma
										key="administracion.informes.literal.archivo.fecha" /></b></td>
							<td align='center' width='10%'>&nbsp;</td>
						</tr>
					</table>
				</div>
				<div id='listadoArchivosDiv'
					style='height: 400; width: 100%; overflow-y: auto'>

					<table class="tablaCampos" id='listadoArchivos' border='1'
						align='center' width='100%' cellspacing='0' cellpadding='0'
						style='table-layout: fixed'>

						<tr>
							<td width='30%'></td>
							<td width='30%'></td>
							<td width='30%'></td>
							<td width='10%'></td>
						</tr>

						<tr>
							<td width='30%'></td>
							<td width='30%'></td>
							<td width='30%'></td>
							<td width='10%'></td>
						</tr>
						<tr class="filaTablaPar">
							<td align='left'>
							<html:hidden styleId="idFichero" property = "idFichero" />
							<c:out value="${FicheroForm.descripcionArchivo}"/></td>
							<td align='left'><c:out value="${FicheroForm.extensionArchivo}"/></td>
							<td align='left'><c:out value="${FicheroForm.fechaArchivo}"/></td>
							<td name='celda' id='idFilaBotones_1' align="left">	&nbsp;
							<c:choose>
							<c:when test="${permisoFicheros=='Sólo Lectura'||action=='ver'}">
								<img id="iconoboton_borrar2"  src="/SIGA/html/imagenes/bborrar_disable.gif" alt="Borrar" title="Borrar" name="borrar_2" border="0">
							</c:when>
							<c:when test="${permisoFicheros=='Acceso Total'&&action!='ver'}">
								<img id="iconoboton_borrar1" src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" alt="Borrar" title="Borrar"  name="borrar_1" border="0" onClick="return eliminarFichero()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('borrar_1','','/SIGA/html/imagenes/bborrar_on.gif',1)">
							</c:when>
								
							
							</c:choose>
								
								<img id="iconoboton_download1" src="/SIGA/html/imagenes/bdownload_off.gif" style="cursor:pointer;" alt="Descargar" name="iconoFila" title="Descargar" border="0" onClick="return downloadFichero(); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('download_1','','/SIGA/html/imagenes/bdownload_on.gif',1)">
							</td>
						</tr>
					</table>

				</div>
			</c:otherwise>
		</c:choose>
	</siga:ConjCampos>
</diV>
</c:when>
</c:choose>

<script type="text/javascript">


</script>

