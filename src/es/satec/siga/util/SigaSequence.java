/**
 * 
 */
package es.satec.siga.util;

/**
 * @author angelcpe
 *
 */
public class SigaSequence {
	
	private Integer value = null;
	private String nombreSecuencia = null;	


	/**
	 * @param value
	 * @param nombreSecuencia
	 */
	public SigaSequence(String nombreSecuencia) {
		super();		
		this.nombreSecuencia = nombreSecuencia;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * @return the nombreSecuencia
	 */
	public String getNombreSecuencia() {
		return nombreSecuencia;
	}

	/**
	 * @param nombreSecuencia the nombreSecuencia to set
	 */
	public void setNombreSecuencia(String nombreSecuencia) {
		this.nombreSecuencia = nombreSecuencia;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return value!=null?value.toString():null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String nextVal() {
		return nombreSecuencia + ".NEXTVAL";
	}
	
	
}
