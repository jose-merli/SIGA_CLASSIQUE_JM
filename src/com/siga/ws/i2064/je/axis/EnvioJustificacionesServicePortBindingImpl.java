package com.siga.ws.i2064.je.axis;

import java.rmi.RemoteException;

public class EnvioJustificacionesServicePortBindingImpl implements
		EnvioJustificacionesService_PortType {

	public EnvioJustificacionesServicePortBindingImpl() {
	}



	public Resposta envioJustificacion(int codAplicacion, String usuario, String datosJustificaciones) throws RemoteException {
		Resposta resposta = new Resposta();
		
		resposta.setCodResposta("E0008");
		resposta.setDescricion("Los c�digos no son correctos (error de prueba servidor local)");
//		resposta.setCodResposta("C0001");
//		resposta.setDescricion("OK");
		resposta.setFicheiroResposta("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><DatosJustificaciones><Periodo><Ano>2012</Ano><Trimestre>PRIMEIRO</Trimestre><dende>2012-01-01</dende><ata>2012-03-31</ata></Periodo><Colegio><IDColegio>A15078</IDColegio></Colegio><TurnoOficio><colegiado><codColegiado>1111</codColegiado><Asuntos><Fecha>2011-11-29</Fecha><SOXCLAVE><SOX_ANO>2020</SOX_ANO><SOX_NUMERO>2834</SOX_NUMERO></SOXCLAVE><IDExpAXG><Cons>PR</Cons><Proc>204A</Proc><Ano>2020</Ano><Num>113</Num><Prov>1</Prov></IDExpAXG><Datosxudiciais><Juzgado><NUMEROSALASECCION>002</NUMEROSALASECCION><PARTIDOXUDICIAL>1502</PARTIDOXUDICIAL><COD_ORGANO>43</COD_ORGANO></Juzgado><ProcBaremo><TIPO_BAREMO>20</TIPO_BAREMO><COD_BAREMO>208</COD_BAREMO><ANO_PROC>2011</ANO_PROC><NUM_PROC>5464</NUM_PROC></ProcBaremo></Datosxudiciais><Solicitante><NOMEAPELIDOS><Nome>JACOB</Nome><PRIMER_APELLIDO>AVENDA</PRIMER_APELLIDO><SEGUNDO_APELLIDO>ACOSTA</SEGUNDO_APELLIDO></NOMEAPELIDOS><IDENTIFICACION><DOCUMENTADO><TIPO_IDENTIFICADOR>1</TIPO_IDENTIFICADOR><IDENTIFICADOR>36174523G</IDENTIFICADOR></DOCUMENTADO></IDENTIFICACION></Solicitante><IMPORTE><IMPORTE>233.49</IMPORTE><IRPF>15</IRPF></IMPORTE><CargaAsunto><codigo>AE0008</codigo><descripcion>O DNI indicado non Ã© correcto</descripcion></CargaAsunto></Asuntos></colegiado></TurnoOficio></DatosJustificaciones>");
		return resposta;
	}


}
