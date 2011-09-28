package com.siga.ws.i2064.je.error;

public class ErrorEnvioWS extends Exception {
	private static final long serialVersionUID = 1884379709733633380L;	
	
	public ErrorEnvioWS(String s, Throwable t){
        super(s, t);
    }

	public ErrorEnvioWS(String s) {
		super(s);
	}
}
