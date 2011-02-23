package com.atos.utils;

import java.util.ArrayList;
import java.util.List;

public enum ActionButtonsConstants {

	VOLVER ("V","general.boton.volver","accionVolver()",1),
	FINALIZAR_SEL ("fs","general.boton.finalizarSel","accionFinalizarSel()",2),
	IMPRIMIR_APAISADO ("ia","general.aviso.imprimirApaisado","accionImprimirApaisado()",3),
	NUEVO_LETRADO ("nl","gratuita.modalCambioLetradoDesigna.titulo","accionNuevoLetrado()",4),
	CREAR_EJG ("ce","general.boton.crearEJG","accionCrearEJG()",5),
	CREAR_DESIGNACION ("cd","general.boton.crearDesignacion","accionCrearDesignacion()",6),
	RELACIONAR_EJG ("re","general.boton.relacionarEJG","relacionarConEJG()",7),
	RELACIONAR_SOJ ("rs","general.boton.relacionarSOJ","relacionarConSOJ()",8),
	RELACIONAR_DESIGNACION ("rd","general.boton.relacionarDesigna","relacionarConDesigna()",9),
	SOLICITAR_NUEVO ("sn","general.boton.solicitarNuevo","accionSolicitarNuevo()",10),
	ANYADIR_EXPEDIENTES ("ae","general.boton.aniadirExpedientes","aniadirExpedientes(true)", "idButtonAniadirExpedientes",11),
	NUEVO ("n","general.boton.new","accionNuevo()",12),
	REALIZAR_PAGO ("rp","facturacion.abonosPagos.boton.realizarPago","accionRealizarPago()",13),
	GENERAR_INFORME ("gm","general.boton.generarInforme","accionGenerarInforme()",14),
	GENERAR_PDF ("gp","general.boton.generarPDF","accionGenerarPDF()",15),
	SOLICITAR ("l","general.boton.solicitarTurno","accionSolicitar()",16),
	DETALLE_LETRADO ("dLetrado","general.boton.detalleLetrado","accionDetalleLetrado()",17),
	DETALLE_CONCEPTO ("dConcepto","general.boton.detalleConcepto","accionDetalleConcepto()",18),
	SOLICITAR_MODIFICACION ("sm","general.boton.solicitarModificacion","accionSolicitarModificacion()",19),
	SOLICITAR_ASISTENCIA ("sas","gratuita.operarEJG.boton.solicitudAsistencia","accionSolicitudAsistencia()",20),
	ACEPTAR ("a","global.boton.aceptar","accionAceptar()",21),
	DETALLE_PAGO ("dpa","general.boton.detallePago","accionDetallePago()",22),
	DENEGAR_SOLICITUDES ("ds","general.boton.denegarSolicitud","accionDenegarSolicitud()",23),
	GUARDAR_EJECUTAR ("ge","general.boton.guardarEjecutar","accionGuardarEjecutar()",24),
	ENVIO_WS ("ws","general.boton.envioWS","envioWS(this)", "idButtonEnvioWS",25),
	IMPRIMIR ("i","general.boton.imprimir","accionImprimir()",26),
	CONTINUAR ("ct","general.boton.continuar","accionContinuar()",27),
	FINALIZAR ("f","general.boton.finalizar","accionFinalizar()",28),
	DESCARGAR_EJG ("dee","general.boton.descargaEejg","descargaEejg(true)", "idButtonDescargaEejg",29),
	MARCAR_TODOS ("mt","general.boton.marcarTodos","accionMarcarTodos()",30),
	DESCARGAS ("de","general.boton.descargar","accionDescargas()",31),
	ENVIAR ("en","general.boton.enviar","accionEnviar()",32),
	FINALIZAR_COMPARA ("fc","general.boton.finalizarCompra","accionfinalizarCompra()",33),
	PROCESAR_SOLICITUDES ("ps","general.boton.procesarSolicitud","accionProcesarSolicitud()",34),
	PROCESAR_DEVOLUCIONES ("pd","general.boton.procesarDevoluciones","accionProcesarDevoluciones()",35),
	DEMARCAR_TODOS ("dt","general.boton.cancel","accionDesmarcarTodos()",36),
	CONFIRMA_COMPRA ("cc","general.boton.confirmarCompra","accionConfirmarCompra()",37),
	LISTA_CONSEJO ("lc","general.boton.listaParaConsejo","accionListaConsejo()",38),
	DOWNLOAD ("d","general.boton.download","accionDownload()",39),
	NUEVA_REGULARIZACION ("nr","general.boton.nuevaRegularizacion","accionNuevaRegularizacion()",40),
	CERRAR_PAGO ("cp","general.boton.cerrarPago","accionCerrarPago()",41),
	VISUALIZAR_CRITERIOS ("vc","general.boton.visualizarCriterio","accionVisualizarCriterios()",42),
	DEFINIR_CRITERIO ("dc","general.boton.definirCriterio","accionDefinirCriterio()",43),
	EJECUTA_FACTURACION ("ef","general.boton.ejecutarFacturacion","accionEjecutaFacturacion()",44),
	BAJA_EN_TODOS_LOS_TURNOS ("bajaEnTodosLosTurnos","general.boton.bajaEnTodosLosTurnos","darDeBajaEnTodosLosTurnos(true)",45),
	ALTA_EN_TURNOS_SEL ("altaEnTurnosSel","general.boton.altaEnTurnosSel","darDeAltaEnTurnosSel(true)",46),
	BA ("ba","","",47),
	GENERAR_XML ("gxml","general.boton.generaXML","generaXML()","idButtonGeneraXML",48),
	GENERAR_FICHERO ("gf","general.boton.generarFichero","generarFichero(true)",49),
	GENERAR_CERRAR ("ygc","general.boton.generarCerrar","accionGenerarCerrar()",50),
	GUARDAR_CERRAR ("y","general.boton.guardarCerrar","accionGuardarCerrar()",51),
	GUARDAR_ANYADIR_HISTORICO ("gah","general.boton.guardarAnyadirHistorico","accionGuardarAnyadirHistorico()",52),
	GUARDAR ("g","general.boton.guardar","accionGuardar()",53),
	GENERAR_CARTA ("cg","general.boton.cartaInteresados","generarCarta()",54),
	INFORME_ENVIOS ("ie","general.boton.informeEnvios","accionInformeEnvios();",55),
	RESTABLECER ("r","general.boton.restablecer","accionRestablecer()",56),
	SIGUIENTE ("s","general.boton.siguiente","accionSiguiente()",57),
	GENERAR_ETIQUETAS ("get","general.boton.generarEtiquetas","accionGenerarEtiquetas()",58),
	COMUNICAR ("com","general.boton.comunicar","accionComunicar()",59),
	GENERAR_IMPRESO_190 ("gi","general.boton.generarImpreso","accionGenerarImpreso()",60),
	DETALLE_FACTURACION ("df","general.boton.detalleFacturacion","accionDetalleFacturacion()",61),
	DESGLOSE_PAGO ("dp","general.boton.desglosePago","acciondesglosePago()",62),
	FACTURACION_RAPIDA ("fr","general.aviso.facturacionRapida","accionFacturacionRapida()",63),
	CANCELAR ("x","general.boton.cancel","accionCancelar()",64),
	GENERAR_CALENDARIO ("gc","general.boton.generarCalendario","accionGenerarCalendario()",65),
	ANULAR ("an","general.boton.anular","accionAnular()",66),
	GENERAR_EXCELS ("gx","general.boton.generarExcels","accionGenerarExcels()",67),
	GENERAR_PDFS ("gps","general.boton.generarPDFS","accionGenerarPDFS()",68),
	LOG_FACTURACION ("lf","general.boton.descargaLogFacturacion","descargaLogFacturacion()",69),
	CERRAR ("c","general.boton.close","accionCerrar()",70),
	ENVIAR_SEL ("es","general.boton.enviarSel","accionEnviarSel()",71),
	ENVIO_FTP ("ftp","general.boton.envioFTP","envioFTP(this)", "idButtonEnvioFTP",72),
	RESPUESTA_FTP ("respFTP","general.boton.respuestaFTP","respuestaFTP(this)", "idButtonRespuestaFTP",73),
	RESOLUCION_FTP ("resolucionFTP","general.boton.resolucionFTP","resolucionFTP(this)", "idButtonResolucionFTP",74),
	INFORME_RETENCIONES_IRPF ("iri","general.boton.informeRetencionesIRPF","accionInformeRetencionesIRPF()",75),
	ARCHIVAR ("ar","general.botons.Archivar","accionArchivar()",76),
	INFORME_JUSTIFICACION ("ij","general.boton.informeJustificacion","informeJustificacion()",77),
	;


    private final String valor;   
	private final String label;   
    private final String accion;
    private final String idBoton; 
    private final int	 orden;
    
    ActionButtonsConstants(String valor, String label, String accion, int orden) {
        this(valor, label, accion, "idButton", orden);
    }

    ActionButtonsConstants(String valor, String label, String accion, String idBoton, int orden) {
        this.valor = valor;
        this.label = label;
        this.accion = accion;
        this.idBoton = idBoton;
        this.orden = orden;
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
	
	public String getIdBoton() {
		return idBoton;
	}
	public int getOrden() {
		return orden;
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
