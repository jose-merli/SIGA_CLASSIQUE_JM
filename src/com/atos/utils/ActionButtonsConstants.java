package com.atos.utils;

import java.util.ArrayList;
import java.util.List;

public enum ActionButtonsConstants {

	GUARDAR ("g","general.boton.guardar","accionGuardar()"),
	GUARDAR_CERRAR ("y","general.boton.guardarCerrar","accionGuardarCerrar()"),
	GUARDAR_EJECUTAR ("ge","general.boton.guardarEjecutar","accionGuardarEjecutar()"),
	GENERAR_CERRAR ("ygc","general.boton.generarCerrar","accionGenerarCerrar()"),
	VOLVER ("V","general.boton.volver","accionVolver()"),
	BA ("ba","",""),
	GUARDAR_ANYADIR_HISTORICO ("gah","general.boton.guardarAnyadirHistorico","accionGuardarAnyadirHistorico()"),
	RESTABLECER ("r","general.boton.restablecer","accionRestablecer()"),
	CERRAR ("c","general.boton.close","accionCerrar()"),
	CANCELAR ("x","general.boton.cancel","accionCancelar()"),
	NUEVO ("n","general.boton.new","accionNuevo()"),
	INFORME_ENVIOS ("ie","general.boton.informeEnvios","accionInformeEnvios();"),
	SIGUIENTE ("s","general.boton.siguiente","accionSiguiente()"),
	FINALIZAR ("f","general.boton.finalizar","accionFinalizar()"),
	SOLICITAR ("l","general.boton.solicitarTurno","accionSolicitar()"),
	SOLICITAR_NUEVO ("sn","general.boton.solicitarNuevo","accionSolicitarNuevo()"),
	SOLICITAR_MODIFICACION ("sm","general.boton.solicitarModificacion","accionSolicitarModificacion()"),
	MARCAR_TODOS ("mt","general.boton.marcarTodos","accionMarcarTodos()"),
	DEMARCAR_TODOS ("dt","general.boton.cancel","accionDesmarcarTodos()"),
	PROCESAR_SOLICITUDES ("ps","general.boton.procesarSolicitud","accionProcesarSolicitud()"),
	PROCESAR_DEVOLUCIONES ("pd","general.boton.procesarDevoluciones","accionProcesarDevoluciones()"),
	DENEGAR_SOLICITUDES ("dn","general.boton.denegarSolicitud","accionDenegarSolicitud()"),
	GENERAR_CALENDARIO ("gc","general.boton.generarCalendario","accionGenerarCalendario()"),
	IMPRIMIR ("i","general.boton.imprimir","accionImprimir()"),
	IMPRIMIR_APAISADO ("ia","general.aviso.imprimirApaisado","accionImprimirApaisado()"),
	FACTURACION_RAPIDA ("fr","general.aviso.facturacionRapida","accionFacturacionRapida()"),
	CONTINUAR ("ct","general.boton.continuar","accionContinuar()"),
	FINALIZAR_COMPARA ("fc","general.boton.finalizarCompra","accionfinalizarCompra()"),
	DESGLOSE_PAGO ("dp","general.boton.desglosePago","acciondesglosePago()"),
	CONFIRMA_COMPRA ("cc","general.boton.confirmarCompra","accionConfirmarCompra()"),
	LISTA_CONSEJO ("lc","general.boton.listaParaConsejo","accionListaConsejo()"),
	EJECUTA_FACTURACION ("ef","general.boton.ejecutarFacturacion","accionEjecutaFacturacion()"),
	NUEVA_REGULARIZACION ("nr","general.boton.nuevaRegularizacion","accionNuevaRegularizacion()"),
	CERRAR_PAGO ("cp","general.boton.cerrarPago","accionCerrarPago()"),
	DEFINIR_CRITERIO ("dc","general.boton.definirCriterio","accionDefinirCriterio()"),
	GENERAR_IMPRESO_190 ("gi","general.boton.generarImpreso","accionGenerarImpreso()"),
	DOWNLOAD ("d","general.boton.download","accionDownload()"),
	ACEPTAR ("a","global.boton.aceptar","accionAceptar()"),
	DESCARGAS ("de","general.boton.descargar","accionDescargas()"),
	DETALLE_PAGO ("dpa","general.boton.detallePago","accionDetallePago()"),
	DETALLE_FACTURACION ("df","general.boton.detalleFacturacion","accionDetalleFacturacion()"),
	GENERAR_EXCELS ("gx","general.boton.generarExcels","accionGenerarExcels()"),
	COMUNICAR ("com","general.boton.comunicar","accionComunicar()"),
	GENERAR_INFORME ("gm","general.boton.generarInforme","accionGenerarInforme()"),
	REALIZAR_PAGO ("rp","facturacion.abonosPagos.boton.realizarPago","accionRealizarPago()"),
	GENERAR_PDF ("gp","general.boton.generarPDF","accionGenerarPDF()"),
	GENERAR_PDFS ("gps","general.boton.generarPDFS","accionGenerarPDFS()"),
	CREAR_EJG ("ce","general.boton.crearEJG","accionCrearEJG()"),
	CREAR_DESIGNACION ("cd","general.boton.crearDesignacion","accionCrearDesignacion()"),
	DESCARGAR_EJG ("dee","general.boton.descargaEejg","descargaEejg(true)"),
	RELACIONAR_EJG ("re","general.boton.relacionarEJG","relacionarConEJG()"),
	RELACIONAR_SOJ ("rs","general.boton.relacionarSOJ","relacionarConSOJ()"),
	RELACIONAR_DESIGNACION ("rd","general.boton.relacionarDesigna","relacionarConDesigna()"),
	ANULAR ("an","general.boton.anular","accionAnular()"),
	SOLICITAR_ASISTENCIA ("sas","gratuita.operarEJG.boton.solicitudAsistencia","accionSolicitudAsistencia()"),
	DETALLE_LETRADO ("dLetrado","general.boton.detalleLetrado","accionDetalleLetrado()"),
	DETALLE_CONCEPTO ("dConcepto","general.boton.detalleConcepto","accionDetalleConcepto()"),
	VISUALIZAR_CRITERIOS ("vc","general.boton.visualizarCriterio","accionVisualizarCriterios()"),
	ENVIAR_SEL ("es","general.boton.enviarSel","accionEnviarSel()"),
	FINALIZAR_SEL ("fs","general.boton.finalizarSel","accionFinalizarSel()"),
	NUEVO_LETRADO ("nl","gratuita.modalCambioLetradoDesigna.titulo","accionNuevoLetrado()"),
	ENVIAR ("en","general.boton.enviar","accionEnviar()"),
	BAJA_EN_TODOS_LOS_TURNOS ("bajaEnTodosLosTurnos","general.boton.bajaEnTodosLosTurnos","darDeBajaEnTodosLosTurnos(true)"),
	ALTA_EN_TURNOS_SEL ("altaEnTurnosSel","general.boton.altaEnTurnosSel","darDeAltaEnTurnosSel(true)"),
	GENERAR_CARTA ("cg","general.boton.cartaInteresados","generarCarta()"),
	ANYADIR_EXPEDIENTES ("ae","general.boton.aniadirExpedientes","aniadirExpedientes(true)"),
	GENERAR_FICHERO ("gf","general.boton.generarFichero","generarFichero(true)"),
	GENERAR_XML ("gxml","general.boton.generaXML","generaXML()"),
	GENERAR_ETIQUETAS ("get","general.boton.generarEtiquetas","accionGenerarEtiquetas()"),
	INFORME_RETENCIONES_IRPF ("iri","general.boton.informeRetencionesIRPF","accionInformeRetencionesIRPF()"),
	LOG_FACTURACION ("lf","general.boton.descargaLogFacturacion","descargaLogFacturacion()"),
	ENVIO_FTP ("ftp","general.boton.envioFTP","envioFTP(this)"),
	ENVIO_WS ("ws","general.boton.envioWS","envioWS(this)"),
	RESPUESTA_FTP ("respFTP","general.boton.respuestaFTP","respuestaFTP(this)"),
	RESOLUCION_FTP ("resolucionFTP","general.boton.resolucionFTP","resolucionFTP(this)");

    private final String valor;   
	private final String label;   
    private final String accion;   
    
    ActionButtonsConstants(String valor, String label, String accion) {
        this.valor = valor;
        this.label = label;
        this.accion = accion;
    }

    public String getValor() {
		return valor;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getAccion() {
		return accion;
	}
	
	public static ActionButtonsConstants getEnum(String value){
		for(ActionButtonsConstants bc : values()){
			if (bc.getValor().equalsIgnoreCase(value)){
				return bc;
			}
		}
		return null;
	}
	
	public static List<ActionButtonsConstants> getEnums(String value, String separator){
		List<ActionButtonsConstants> list = new ArrayList<ActionButtonsConstants>();
		String [] datos = value.replaceAll(" ","").split(separator);
		for (String tipo : datos) {
			ActionButtonsConstants acb = ActionButtonsConstants.getEnum(tipo);
			if (acb!=null)
				list.add(acb);
		}
		return list;
	}
	
	
}
