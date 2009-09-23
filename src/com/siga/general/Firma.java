/*
 * Clase para tratar la Firma del TPV.
 * 
 * david.sanchezp
 * 
 * Version 1.0
 */
package com.siga.general;


import tpv.TPV3;




/**
 * Clase para calcular la Firma con el metodo externo "calcularFirma" y metodos para validarla.<br>
 * NOTA: He creado el jar firma.jar a partir del class TPV3.class
 *  
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Firma {
	
	private String firma = null;//String con la Firma cifrada
	
	private String clave = null;//Clave para obtener la Firma
	private String merchantid = null;//Identifica al Comercio
	private String acquirerbin = null;//Identifica la Caja
	private String terminalid = null;//Actaulmente para todos los TPVs es "00000003"
	private String operacion = null;//Operacion para el Comercio del pedido
	private String importe = null;//Entero con los ultimos digitos como parte decimal
	private String moneda = null;//Para Euros usar 978
	private String exponente = null;//Actualmente siempre 2
	private String referencia = null;//En las compras debe ser vacio

	
	public Firma (String firma) {
		this.firma = firma;
		this.clave = "";
		this.merchantid = "";
		this.acquirerbin = "";
		this.terminalid = "";
		this.operacion = "";
		this.importe = "";
		this.moneda = "";
		this.exponente = "";
		this.referencia = "";
	}

	public Firma () {
		this.firma = "";
		this.clave = "";
		this.merchantid = "";
		this.acquirerbin = "";
		this.terminalid = "";
		this.operacion = "";
		this.importe = "";
		this.moneda = "";
		this.exponente = "";
		this.referencia = "";
	}

	public Firma (String clave, String merchantid, String acquirerbin, String terminalid, String operacion,
				  String importe, String moneda, String exponente, String referencia) {
		this.clave = clave;
		this.merchantid = merchantid;
		this.acquirerbin = acquirerbin;
		this.terminalid = terminalid;
		this.operacion = operacion;
		this.importe = importe;
		this.moneda = moneda;
		this.exponente = exponente;
		this.referencia = referencia;
		//Calculo la firma:
		this.firma = this.obtenerFirma(this);
	}
	
	public Firma (String parametros[]) {
		this.clave = parametros[0];
		this.merchantid = parametros[1];
		this.acquirerbin = parametros[2];
		this.terminalid = parametros[3];
		this.operacion = parametros[4];
		this.importe = parametros[5];
		this.moneda = parametros[6];
		this.exponente = parametros[7];
		this.referencia = parametros[8];
		//Calculo la firma:
		this.firma = this.obtenerFirma(parametros);
	}
	
	//Devuelve un String con la firma a partir de un array de los datos necesarios como String
	private String obtenerFirma(String parametros[]){
		String salida = "";
		
	    try {	    		
	         TPV3 tpv = new TPV3(parametros[0]);	         	         
	         salida = tpv.calcularFirma(parametros[1],parametros[2],parametros[3],parametros[4],parametros[5],parametros[6],parametros[7],parametros[8]);
        } 
	    catch (Exception e) {
	    	com.atos.utils.ClsLogging.writeFileLogError("ERROR: FIRMA INCORRECTA, error en los parametros de entrada.", e, 3);
	    }
	    return salida;
    }
	
	public String getFirma(){
		return this.firma;
	}	
	public void setFirma(String firma){
		this.firma = firma;
	}
	
	public void setClave(String valor){
		this.clave = valor;
	}	
	public String getClave(){
		return this.clave;
	}
	
	public void setMerchantId(String valor){
		this.merchantid = valor;
	}
	public String getMerchantId(){
		return this.merchantid;
	}
	
	public void setAcquirerBin(String valor){
		this.acquirerbin = valor;
	}
	public String getAcquirerBin(){
		return this.acquirerbin;
	}
	
	public void setTerminalId(String valor){
		this.terminalid = valor;
	}
	public String getTerminalId(){
		return this.terminalid;
	}
	
	public void setOperacion(String valor){
		this.operacion = valor;
	}
	public String getOperacion(){
		return this.operacion;
	}
	
	public void setImporte(String valor){
		this.importe = valor;
	}
	public String getImporte(){
		return this.importe;
	}
	
	public void setMoneda(String valor){
		this.moneda = valor;
	}
	public String getMoneda(){
		return this.moneda;
	}
	
	public void setExponente(String valor){
		this.exponente = valor;
	}
	public String getExponente(){
		return this.exponente;
	}
	
	public void setReferencia(String valor){
		this.referencia = valor;
	}
	public String getReferencia(){
		return this.referencia;
	}

	//Devuelve cierto si la Firma Original y la Firma son iguales.
	public boolean validarFirma(String firmaOriginal, String firma){
		return firmaOriginal.equals(firma);
    }

	//Devuelve cierto si la Firma Original y la Firma de la Clase son iguales.
	//Para tratar con Firmas como String
	public boolean validarFirma(String firmaOriginal){
		return firmaOriginal.equals(this.firma);
    }
	
	//Devuelve cierto si la Firma Original y la Firma de la Clase son iguales.
	//NOTA: para tratar con instancias de Firmas
	public boolean equals(Firma firmaOriginal){
		return firmaOriginal.firma.equals(this.firma);
	}

	//Devuelve un String con la firma cifrada a partir de una instancia Firma.
	private String obtenerFirma(Firma firma){
		String salida = "";
		
	    try {
	         TPV3 tpv = new TPV3(firma.clave);	         	         
	         salida = tpv.calcularFirma(firma.merchantid,firma.acquirerbin,firma.terminalid,firma.operacion,firma.importe,firma.moneda,firma.exponente,firma.referencia);
        } 
	    catch (Exception e) {
	    	com.atos.utils.ClsLogging.writeFileLogError("ERROR: FIRMA INCORRECTA, error en los parametros de entrada.",e,3);
	    }
	    return salida;
    }
	
	//Devuelve un String con la firma cifrada a partir de una instancia Firma.
	public String calcularFirma(){
		String salida = "";
		
	    try {
	         TPV3 tpv = new TPV3(this.clave);	         	         
	         salida = tpv.calcularFirma(this.merchantid,this.acquirerbin,this.terminalid,this.operacion,this.importe,this.moneda,this.exponente,this.referencia);
        } 
	    catch (Exception e) {
	    	com.atos.utils.ClsLogging.writeFileLogError("ERROR: FIRMA INCORRECTA, error en los parametros de entrada.", e, 3);	    
	    }
	    return salida;
    }
	
	public void imprimirDatos(){
		String salida = "";
		salida =  "clave="+this.clave+", ";
		salida += "merchantid="+this.merchantid+", ";
		salida += "acquirerbin="+this.acquirerbin+", ";
		salida += "terminalid="+this.terminalid+", ";
		salida += "operacion="+this.operacion+", ";
		salida += "importe="+this.importe+", ";
		salida += "moneda="+this.moneda+", ";
		salida += "exponente="+this.exponente+", ";		
		salida += "referencia="+this.referencia+"\n";
		salida += "FIRMA="+this.firma;		
		com.atos.utils.ClsLogging.writeFileLog("DATOS FIRMA: "+salida,10);
	}
	
}
