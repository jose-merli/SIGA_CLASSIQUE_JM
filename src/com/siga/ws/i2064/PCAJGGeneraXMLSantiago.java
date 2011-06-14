/**
 * 
 */
package com.siga.ws.i2064;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import org.apache.xmlbeans.XmlOptions;

import com.atos.utils.ClsConstants;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.informes.MasterWords;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.SigaWSHelper;
import com.siga.ws.i2064.xsd.ANEXOITYPE;
import com.siga.ws.i2064.xsd.AVOGADOTYPE;
import com.siga.ws.i2064.xsd.BENINMUEBLETYPE;
import com.siga.ws.i2064.xsd.BENMUEBLETYPE;
import com.siga.ws.i2064.xsd.DATOSCONTACTO;
import com.siga.ws.i2064.xsd.DIRECCIONTYPE;
import com.siga.ws.i2064.xsd.FAMILIARTYPE;
import com.siga.ws.i2064.xsd.INGRESOSTYPE;
import com.siga.ws.i2064.xsd.NOMEAPELIDOSTYPE;
import com.siga.ws.i2064.xsd.PERSOATYPE;
import com.siga.ws.i2064.xsd.PROCURADORTYPE;
import com.siga.ws.i2064.xsd.SOLICITUDEAXGDocument;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSPERSOAIS;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DOCUMENTOSADXUNTOS;
import com.siga.ws.i2064.xsd.ANEXOITYPE.NUMEROSOX;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.CONXUXE;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.SOLICITANTE;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.FAMILIA.FAMILIAR;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.SOLICITANTE.BENS;
import com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSPERSOAIS.FAMILIA;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.TIPOPROCEDEMENTO;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.XURISDICCION;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.TIPOPROCEDEMENTO.ADMINISTRATIVO;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.TIPOPROCEDEMENTO.XUDICIAL;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.TIPOPROCEDEMENTO.ADMINISTRATIVO.DILIXENCIA;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.TIPOPROCEDEMENTO.XUDICIAL.ORGANO;
import com.siga.ws.i2064.xsd.ANEXOITYPE.PROCEDEMENTO.TIPOPROCEDEMENTO.XUDICIAL.ORGANO.ORGANO2;
import com.siga.ws.i2064.xsd.AVOGADOTYPE.DESIGNACION;
import com.siga.ws.i2064.xsd.SOLICITUDEAXGDocument.SOLICITUDEAXG;

/**
 * @author angelcpe
 *
 */
public class PCAJGGeneraXMLSantiago extends SIGAWSClientAbstract implements PCAJGConstantes {
	
	Map<String, List<Map<String, String>>> htDtUnidadFamiliar = new Hashtable<String, List<Map<String,String>>>();
	Map<String, List<Map<String, String>>> htParteContraria = new Hashtable<String, List<Map<String,String>>>();
	Map<String, List<Map<String, String>>> htDocumentosAdjuntos = new Hashtable<String, List<Map<String,String>>>();
	
	
	private static final String TRUE = "1";
	private static final String TIPO_PROCEDEMENTO_XUDICIAL = "0";
	private static final String TIPO_PROCEDEMENTO_ADMINISTRATIVO = "1";
	private static String numeroEnvioValor = null;
	private static final String VISTA_EJGs = "V_WS_2064_EJG";
	
	/* (non-Javadoc)
	 * @see com.siga.ws.SIGAWSClientAbstract#execute()
	 */
	@Override
	public void execute() throws Exception {
				
		String anio = null;
		String numejg = null;
		String numero = null;
		String idTipoEJG = null;		
		
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		WSSantiagoAdm wsSantiagoAdm = new WSSantiagoAdm();
		Map<String, String> mapExp = null;
		List<Hashtable<String, String>> listDtExpedientes = wsSantiagoAdm.getDtExpedientes(getIdInstitucion(), getIdRemesa());		
		construyeHTxEJG(wsSantiagoAdm.getDtUnidadFamiliar(getIdInstitucion(), getIdRemesa()), htDtUnidadFamiliar);
		construyeHTxEJG(wsSantiagoAdm.getDtParteContraria(getIdInstitucion(), getIdRemesa()), htParteContraria);
		construyeHTxEJG(wsSantiagoAdm.getDocumentosAdjuntos(getIdInstitucion(), getIdRemesa()), htDocumentosAdjuntos);
		
		UserTransaction tx = getUsrBean().getTransaction();
		
		try {			
			tx.begin();
			//elimino primero las posibles respuestas que ya tenga por si se ha relanzado
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			cajgRespuestaEJGRemesaAdm.insertaErrorEJGnoEnviados(getIdInstitucion(), getIdRemesa(), getUsrBean(), VISTA_EJGs);
				
			SOLICITUDEAXGDocument solicitudeaxgDocument = SOLICITUDEAXGDocument.Factory.newInstance(); 
			SOLICITUDEAXG solicitudeAXG = solicitudeaxgDocument.addNewSOLICITUDEAXG();
			
			for (int i = 0; i < listDtExpedientes.size(); i++) {
				ANEXOITYPE anexoIType = null;
				mapExp = listDtExpedientes.get(i);
				anio = mapExp.get(ANIO);
				numejg = mapExp.get(NUMEJG);
				numero = mapExp.get(NUMERO);
				idTipoEJG = mapExp.get(IDTIPOEJG);
				numeroEnvioValor = mapExp.get(NUMERO_ENVIO);
				
				try {
					anexoIType = solicitudeAXG.addNewANEXOI();
					rellenaNumeroSox(anexoIType.addNewNUMEROSOX(), mapExp);
					anexoIType.setDATARECEPCION(SigaWSHelper.getCalendar(mapExp.get(DATA_RECEPCION)));
					rellenaDatosPersoais(anexoIType.addNewDATOSPERSOAIS(), mapExp);
					rellenaDatosEconomicos(anexoIType.addNewDATOSECONOMICOS(), mapExp);
					rellenaProcedimiento(anexoIType.addNewPROCEDEMENTO(), mapExp);					
					rellenaParteContraria(anexoIType, mapExp);					
					rellenaAbogado(anexoIType.addNewAVOGADO(), mapExp);
					rellenaProcurador(anexoIType.addNewPROCURADOR(), mapExp);
					rellenaDocumentosAdjuntos(anexoIType, mapExp);
					anexoIType.setOBSERVACIONS(mapExp.get(OBSERVACIONES));
					
								
					if(!validateXML_EJG(anexoIType, anio, numejg, numero, idTipoEJG)){
						solicitudeAXG.removeANEXOI(solicitudeAXG.sizeOfANEXOIArray()-1);								
					}
				} catch (IllegalArgumentException e) {
					if (anexoIType != null) {
						solicitudeAXG.removeANEXOI(solicitudeAXG.sizeOfANEXOIArray()-1);
					}
					escribeErrorExpediente(anio, numejg, numero, idTipoEJG, e.getMessage(), CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
				} catch (Exception e) {
					e.printStackTrace();
					if (anexoIType != null) {
						solicitudeAXG.removeANEXOI(solicitudeAXG.sizeOfANEXOIArray()-1);
					}
					escribeErrorExpediente(anio, numejg, numero, idTipoEJG, "Se ha producido un error al recuperar los datos del expediente", CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
				}
			}
			
			if (solicitudeAXG.getANEXOIArray().length > 0) {			
				XmlOptions xmlOptions = new XmlOptions();
				xmlOptions.setSavePrettyPrintIndent(4);
				xmlOptions.setSavePrettyPrint();
			
				File file = new File(getDirXML(getIdInstitucion(), getIdRemesa()));
				
				file.mkdirs();
				SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
				String fecha = sdf.format(Calendar.getInstance().getTime());
												
				String nombreFichero = getIdInstitucion() + "-" + fecha + "-SL" + numeroEnvioValor + ".XML";
				file = new File(file, nombreFichero);
			
				solicitudeaxgDocument.save(file, xmlOptions);
								
				List<File> files = new ArrayList<File>();
				files.add(file);

				//hacemos el zip. Este método copia en un zip los xml y los borra del origen
				MasterWords.doZip((ArrayList<File>)files, getNombreRutaZIPconXMLs(getIdInstitucion(), getIdRemesa()));				
				
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());
				// Marcar como generada
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_GENERADA);				

			}
		} finally {
			tx.commit();
		}
	}
	
	
	/**
	 * 
	 * @param procurador
	 * @param mapExp
	 */
	private void rellenaProcurador(PROCURADORTYPE procurador, Map<String, String> mapExp) {
		Boolean b = getBoolean(mapExp.get(PR_RENUNCIA_PROCURADOR));
		if (b != null) {//RENUNCIA O NO RENUNCIA 
			com.siga.ws.i2064.xsd.PROCURADORTYPE.DESIGNACION designacion = procurador.addNewDESIGNACION();
			designacion.setCOLEXIO(getBigInteger(mapExp.get(PR_COLEXIO)));
			designacion.setNUMCOLEXIADO(getBigInteger(mapExp.get(PR_NUM_COLEXIADO)));			
			procurador.setRENUNCIAPROCURADOR(b);	
		}		
	}


	/**
	 * 
	 * @param anexoIType
	 * @param mapExp
	 */
	private void rellenaDocumentosAdjuntos(ANEXOITYPE anexoIType, Map<String, String> mapExp) {
		List<Map<String, String>> list =  htDocumentosAdjuntos.get(getKey(mapExp));		
		if (list != null) {
			DOCUMENTOSADXUNTOS documentosAdjuntos = anexoIType.addNewDOCUMENTOSADXUNTOS();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				documentosAdjuntos.addNewDOCUMENTO().setBigIntegerValue(getBigInteger(map.get(DA_DOCUMENTO)));
			}
		}
		
	}

	/**
	 * 
	 * @param anexoIType
	 * @param mapExp
	 */
	private void rellenaParteContraria(ANEXOITYPE anexoIType,	Map<String, String> mapExp) {
		List<Map<String, String>> list =  htParteContraria.get(getKey(mapExp));
		if (list != null) {			
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				anexoIType.addNewPARTECONTRARIA().setNOME(map.get(CONT_NOMBRE));
			}
		}
		
	}


	/**
	 * 
	 * @param abogado
	 * @param mapExp
	 */
	private void rellenaAbogado(AVOGADOTYPE abogado, Map<String, String> mapExp) {
		DESIGNACION designacion = abogado.addNewDESIGNACION();
		designacion.setCOLEXIO(getBigInteger(mapExp.get(A_COLEXIO)));
		designacion.setNUMCOLEXIADO(getBigInteger(mapExp.get(A_NUM_COLEXIADO)));
		designacion.setNIF(mapExp.get(A_NIF));
		NOMEAPELIDOSTYPE nomeApe = designacion.addNewNOMEAPELIDOS();
		nomeApe.setNOME(mapExp.get(A_NOME));
		nomeApe.setPRIMERAPELLIDO(mapExp.get(A_PRIMERAPELLIDO));
		nomeApe.setSEGUNDOAPELLIDO(mapExp.get(A_SEGUNDOAPELLIDO));
		DIRECCIONTYPE direcciontype = designacion.addNewENDEREZO();
		direcciontype.setENDEREZO(mapExp.get(A_ENDEREZO));
		direcciontype.setLOCALIDADE(mapExp.get(A_LOCALIDADE));
		direcciontype.setMUNICIPIO(mapExp.get(A_MUNICIPIO));
		direcciontype.setPROVINCIA(getBigInteger(mapExp.get(A_PROVINCIA)));
		direcciontype.setCODPOSTAL(mapExp.get(A_CODPOSTAL));
		abogado.setRENUNCIAAVOGADO(getBoolean(mapExp.get(A_RENUNCIA_AVOGADO)));
	}


	/**
	 * 
	 * @param procedemento
	 * @param mapExp
	 * @throws Exception
	 */
	private void rellenaProcedimiento(PROCEDEMENTO procedemento, Map<String, String> mapExp) throws Exception {		
		procedemento.xsetXURISDICCION(XURISDICCION.Factory.newValue(SigaWSHelper.getInteger("jurisdicción", mapExp.get(P_XURISDICCION))));		
		
		TIPOPROCEDEMENTO tipoProcedemento = procedemento.addNewTIPOPROCEDEMENTO();
		if (TIPO_PROCEDEMENTO_XUDICIAL.equals(mapExp.get(TIPO_PROCEDEMENTO_CHOICE))) {
			XUDICIAL xudicial = tipoProcedemento.addNewXUDICIAL();
			xudicial.setTIPOPROCEDEMENTOXUDICIAL(getBigInteger(mapExp.get(P_TPX_TIPOPROCXUDICIAL)));
			xudicial.setANO(getBigInteger(mapExp.get(P_TPX_ANIO)));
			xudicial.setNUMERO(getBigInteger(mapExp.get(P_TPX_NUMERO)));
			xudicial.setNIG(mapExp.get(P_TPX_NIG));
			xudicial.setDESCRICION(mapExp.get(P_TPX_DESCRIPCION));
			ORGANO organo = xudicial.addNewORGANO();			
			organo.xsetORGANO(ORGANO2.Factory.newValue(SigaWSHelper.getInteger("juzgado", mapExp.get(P_TPXO_ORGANO))));
			organo.setNUMEROSALASECCION(getBigInteger(mapExp.get(P_TPXO_NUMEROSALASECCION)));
			organo.setPARTIDOXUDICIAL(getBigInteger(mapExp.get(P_TPXO_PARTIDO_XUDICIAL)));
		} else if (TIPO_PROCEDEMENTO_ADMINISTRATIVO.equals(mapExp.get(TIPO_PROCEDEMENTO_CHOICE))) {
			ADMINISTRATIVO administrativo = tipoProcedemento.addNewADMINISTRATIVO();
			administrativo.setTIPOUNIDADE(getBigInteger(mapExp.get(P_TPXA_TIPOUNIDADE)));
			administrativo.setUNIDADE(mapExp.get(P_TPXA_UNIDADE));
			administrativo.setPARTIDOXUDICIAL(getBigInteger(mapExp.get(P_TPXA_PARTIDO_XUDICIAL)));
			DILIXENCIA dilixencia = administrativo.addNewDILIXENCIA();
			dilixencia.setNUMERO(getBigInteger(mapExp.get(P_TPA_NUMERO)));
			dilixencia.setANO(getBigInteger(mapExp.get(P_TPA_ANO)));
		} else {
			throw new IllegalArgumentException("No está definido si es un procedimiento judicial o administrativo.");
		}
		procedemento.setPRETENSION(mapExp.get(P_PRETENSION));
		procedemento.setCONDICION(mapExp.get(P_CONDICION));
		procedemento.setDATAINICIO(SigaWSHelper.getCalendar(mapExp.get(P_DATA_INICIO)));
	}


	/**
	 * 
	 * @param numeroSOX
	 * @param mapExp
	 */
	private void rellenaNumeroSox(NUMEROSOX numeroSOX, Map<String, String> mapExp) {
		numeroSOX.setCOLEXIO(getBigInteger(mapExp.get(NS_COLEXIO)));
		numeroSOX.setNUMEROEXPEDIENTE(getLong(mapExp.get(NS_NUMERO_EXPEDIENTE)));
		numeroSOX.setANO(getBigInteger(mapExp.get(NS_ANO)));
	}
	
	/**
	 * 
	 * @param datosEconomicos
	 * @param mapExp
	 * @throws Exception
	 */
	private void rellenaDatosEconomicos(DATOSECONOMICOS datosEconomicos, Map<String, String> mapExp) throws Exception {
		List<Map<String, String>> list =  htDtUnidadFamiliar.get(getKey(mapExp));
		if (list != null) {
			com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.FAMILIA familia = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (TRUE.equals(map.get(IS_SOLICITANTE))) { //que no sea el solicitante
					SOLICITANTE solicitante = datosEconomicos.addNewSOLICITANTE();
					rellenaIngresos(solicitante.addNewINGRESOS(), map);
					BENS bens = solicitante.addNewBENS();
					rellenaBienesInmuebles(bens.addNewBENSINMUEBLES(), map);
					rellenaBienesMuebles(bens.addNewBENSMUEBLES(), map);
					solicitante.setALUGUER(getBigDecimal(map.get(DE_ALUGUER)));
					solicitante.setOUTROSDATOS(map.get(DE_OUTROS_DATOS));
				} else if (TRUE.equals(map.get(IS_CONYUGE))) {
					CONXUXE conxuxe = datosEconomicos.addNewCONXUXE();					
					rellenaIngresos(conxuxe.addNewINGRESOS(), map);
					com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.CONXUXE.BENS bens = conxuxe.addNewBENS();
					rellenaBienesInmuebles(bens.addNewBENSINMUEBLES(), map);
					rellenaBienesMuebles(bens.addNewBENSMUEBLES(), map);
				} else {
					if (familia == null) {
						familia = datosEconomicos.addNewFAMILIA();
					}
					FAMILIAR familiar = familia.addNewFAMILIAR();
					NOMEAPELIDOSTYPE nomeapelidostype = familiar.addNewNOMEAPELIDOS();					
					nomeapelidostype.setNOME(map.get(NOME));
					nomeapelidostype.setPRIMERAPELLIDO(map.get(APELLIDO1));
					nomeapelidostype.setSEGUNDOAPELLIDO(map.get(APELLIDO2));					
					
					rellenaIngresos(familiar.addNewINGRESOS(), map);
					familiar.setPARENTESCO(getBigInteger(map.get(PARENTESCO)));
					com.siga.ws.i2064.xsd.ANEXOITYPE.DATOSECONOMICOS.FAMILIA.FAMILIAR.BENS bens = familiar.addNewBENS();
					rellenaBienesInmuebles(bens.addNewBENSINMUEBLES(), map);
					rellenaBienesMuebles(bens.addNewBENSMUEBLES(), map);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param beninmuebletype
	 * @param map
	 */	
	private void rellenaBienesInmuebles(BENINMUEBLETYPE  beninmuebletype, Map<String, String> map) {
		beninmuebletype.setTIPO(map.get(DE_BI_TIPO));
		beninmuebletype.setVALORACION(getBigDecimal(map.get(DE_BI_VALORACION)));
		beninmuebletype.setTIPOVALORACION(map.get(DE_BI_TIPOVALORACION));
		beninmuebletype.setCARGAS(map.get(DE_BI_CARGAS));
	}

	/**
	 * 
	 * @param benmuebletype
	 * @param map
	 */
	private void rellenaBienesMuebles(BENMUEBLETYPE benmuebletype , Map<String, String> map) {		
		benmuebletype.setTIPO(map.get(DE_BM_TIPO));
		benmuebletype.setVALORACION(getBigDecimal(map.get(DE_BE_VALORACION)));
		benmuebletype.setMATRICULA(map.get(DE_BE_MATRICULA));
		
	}

	/**
	 * 
	 * @param ingresostype
	 * @param map
	 */
	private void rellenaIngresos(INGRESOSTYPE ingresostype, Map<String, String> map) {		
		ingresostype.setIMPORTE(getBigDecimal(map.get(DE_INGRESO_IMP)));
		ingresostype.setCONCEPTO(map.get(DE_INGRESO_CONCEPTO));		
	}


	/**
	 * 
	 * @param datosPersoais
	 * @param mapExp
	 * @throws Exception
	 */
	private void rellenaDatosPersoais(DATOSPERSOAIS datosPersoais, Map<String, String> mapExp) throws Exception {
		
		List<Map<String, String>> list =  htDtUnidadFamiliar.get(getKey(mapExp));
		int numConyuge = 0;
		
		if (list != null) {
			FAMILIA familia = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (TRUE.equals(map.get(IS_SOLICITANTE))) { //que no sea el solicitante
					rellenaDatosPersonaType(datosPersoais.addNewSOLICITANTE(), map);
				} else if (TRUE.equals(map.get(IS_CONYUGE))) {
					if (numConyuge > 0) {
						throw new IllegalArgumentException("El solicitante tiene más de un conyuge. Revise los datos introducidos.");
					}
					rellenaDatosPersonaType(datosPersoais.addNewMATRIMONIO().addNewCONXUXE(), map);
					numConyuge++;
				} else {
					if (familia == null) {
						familia = datosPersoais.addNewFAMILIA();
					}
					rellenaDatosPersonalesFamilia(familia, map);					
				}
			}
		}
	}

	/**
	 * 
	 * @param familia
	 * @param map
	 */
	private void rellenaDatosPersonalesFamilia(FAMILIA familia, Map<String, String> map) {		
		FAMILIARTYPE familiar = familia.addNewFAMILIAR();
		NOMEAPELIDOSTYPE nomeapelidostype = familiar.addNewNOMEAPELIDOS();
		nomeapelidostype.setNOME(map.get(NOME));
		nomeapelidostype.setPRIMERAPELLIDO(map.get(APELLIDO1));
		nomeapelidostype.setSEGUNDOAPELLIDO(map.get(APELLIDO2));
		familiar.xsetIDADE(com.siga.ws.i2064.xsd.FAMILIARTYPE.IDADE.Factory.newValue(SigaWSHelper.getInteger("edad", map.get(IDADE))));
		familiar.setPARENTESCO(getBigInteger(map.get(PARENTESCO)));
	}


	/**
	 * 
	 * @param personaType
	 * @param map
	 * @throws Exception
	 */
	private void rellenaDatosPersonaType(PERSOATYPE personaType, Map<String, String> map) throws Exception {
		//solicitante
		NOMEAPELIDOSTYPE nombreApeSol = personaType.addNewNOMEAPELIDOS();
		nombreApeSol.setNOME(map.get(NOME));
		nombreApeSol.setPRIMERAPELLIDO(map.get(APELLIDO1));
		nombreApeSol.setSEGUNDOAPELLIDO(map.get(APELLIDO2));		
		personaType.xsetNACIONALIDADE(com.siga.ws.i2064.xsd.PERSOATYPE.NACIONALIDADE.Factory.newValue(SigaWSHelper.getInteger("nacionalidad", map.get(NACIONALIDADE))));		
		personaType.setESTADOCIVIL(getBigInteger(map.get(ESTADO_CIVIL)));
		personaType.setNIF(map.get(NIF));
		personaType.setDATANACEMENTO(SigaWSHelper.getCalendar(map.get(DATA_NACEMENTO)));
		DATOSCONTACTO datosContacto = personaType.addNewDATOSCONTACTO();
		DIRECCIONTYPE direccion = datosContacto.addNewENDEREZO();
		direccion.setENDEREZO(map.get(DC_ENDEREZO));
		direccion.setLOCALIDADE(map.get(DC_LOCALIDADE));
		direccion.setMUNICIPIO(map.get(DC_MUNICIPIO));
		direccion.setPROVINCIA(getBigInteger(map.get(DC_PROVINCIA)));
		direccion.setCODPOSTAL(map.get(DC_CODPOSTAL));		
		datosContacto.setTELEFONO(map.get(DC_TELEFONO));
		datosContacto.setFAX(map.get(DC_FAX));
		personaType.setPROFESION(map.get(PROFESION));
		personaType.setEMPRESA(map.get(EMPRESA));
		
	}



	/**
	 * 
	 * @param datos
	 * @param htCarga
	 */	
	private void construyeHTxEJG(List<Hashtable<String, String>> datos, Map<String, List<Map<String, String>>> htCarga) {
		if (datos != null) {
			for (int i = 0; i < datos.size(); i++) {
				Hashtable<String, String> ht = datos.get(i);
				String key = getKey(ht);
				
				List<Map<String, String>> list = htCarga.get(key);
				if (list != null) {
					list.add(ht);
				} else {
					list = new ArrayList<Map<String, String>>();
					list.add(ht);
					htCarga.put(key, list);
				}				
			}		
		}
	}
	
	/**
	 * 
	 * @param ht
	 * @return
	 */
	private String getKey(Map<String, String> ht) {
		if (isNull(ht.get(IDINSTITUCION)) || isNull(ht.get(ANIO)) || isNull(ht.get(NUMERO)) || isNull(ht.get(IDTIPOEJG))){
			throw new IllegalArgumentException("Los campos clave del EJG no pueden ser nulos");
		}
		String key = ht.get(IDINSTITUCION) + "##" + ht.get(ANIO) + "##" + ht.get(NUMERO) + "##" + ht.get(IDTIPOEJG);
				
		return key;
	}
	
	/**
	 * 
	 * @param st
	 * @return
	 */
	private boolean isNull(String st) {
		return st == null || st.trim().equals("");
	}
	
	/**
	 * 
	 * @param st
	 * @return
	 */
	private BigInteger getBigInteger(String st) {
		BigInteger bigInteger = null;
		if (st != null && !st.trim().equals("")) {
			bigInteger = new BigInteger(st);
		}
		return bigInteger;
	}
	
	/**
	 * 
	 * @param st
	 * @return
	 */
	private BigDecimal getBigDecimal(String st) {
		BigDecimal bigDecimal = null;
		if (st != null && !st.trim().equals("")) {
			bigDecimal = new BigDecimal(st);
		}
		return bigDecimal;
	}
	
	/**
	 * 
	 * @param st
	 * @return
	 */
	private Long getLong(String st) {
		Long lo = null;
		if (st != null && !st.trim().equals("")) {
			lo = Long.parseLong(st);
		}
		return lo;
	}
	

	
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean getBoolean(String value) {
		Boolean b = null;
		if (value != null && !value.trim().equals("")) {
			if (value.trim().equals("1")) {
				b = Boolean.TRUE;
			} else if (value.trim().equals("0")) {
				b = Boolean.FALSE;
			}
		}
		return b;
	}

}
