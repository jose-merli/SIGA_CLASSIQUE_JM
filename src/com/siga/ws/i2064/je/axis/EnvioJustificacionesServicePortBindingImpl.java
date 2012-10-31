package com.siga.ws.i2064.je.axis;

import java.rmi.RemoteException;

public class EnvioJustificacionesServicePortBindingImpl implements
		EnvioJustificacionesService_PortType {

	public EnvioJustificacionesServicePortBindingImpl() {
	}



	public Resposta envioJustificacion(int codAplicacion, String usuario, String datosJustificaciones) throws RemoteException {
		Resposta resposta = new Resposta();
		
		resposta.setCodResposta("E0008");
		resposta.setDescricion("Os datos das actuacións non son válidos. Revise o .xml de resposta. (error de prueba servidor local)");
//		resposta.setCodResposta("C0001");
//		resposta.setDescricion("OK");
		

		resposta.setFicheiroResposta("");
		return resposta;
	}



	@Override
	public Resposta descargaIncidentesCertf(int arg0, String arg1, String arg2)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RespostaPeticion envioCertificacion(int arg0, String arg1,
			String arg2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RespostaPeticion envioReintegros(int arg0, String arg1, String arg2)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RespostaPeticion solicitudDescargaIncidentesCertf(int arg0,
			String arg1, String arg2, int arg3) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Resposta verificaEnvioCertificacion(int arg0, String arg1,
			String arg2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Resposta verificaEnvioReintegros(int arg0, String arg1, String arg2)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


}
