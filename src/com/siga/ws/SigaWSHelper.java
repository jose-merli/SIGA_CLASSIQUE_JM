/**
 * 
 */
package com.siga.ws;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;

/**
 * @author angelcpe
 *
 */
public class SigaWSHelper {

	private static final String SEPARADOR_RUTA_NODO = ">";

	/**
	 * 
	 */
	public SigaWSHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param nodeParent
	 * @throws Exception
	 */
	public static void deleteEmptyNode(Node nodeParent) throws Exception {
		List arrayNodes = new ArrayList();
		arrayNodes.add(nodeParent);
		Node node = null;
		while(!arrayNodes.isEmpty()){
			node = (Node) arrayNodes.remove(0);
			if (node != null) {	
//				if (node.getNodeValue() != null) { //ñapa del euro TODO					
//					node.setNodeValue(new String(node.getNodeValue().getBytes(), "ISO-8859-1"));					
//				}
				if (node.getNodeType() != Node.TEXT_NODE) {
					StringBuffer contenido = getContenido(node);
					if (contenido.length() == 0) {				
						node.getParentNode().removeChild(node);
					} else {						
						NodeList nodeList = node.getChildNodes();
						for (int i = 0; i < nodeList.getLength(); i++) {
							arrayNodes.add(nodeList.item(i));	
						}	
					}
				}
			}	
		}
			
	}
	
	/**
	 * Obtiene el contenido de un nodo junto con sus hijos de manera recursiva
	 * @param node
	 * @return
	 */
	private static StringBuffer getContenido(Node node) {
		StringBuffer contenido = new StringBuffer();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			contenido.append(nodeList.item(i).getNodeValue()!=null?nodeList.item(i).getNodeValue().replaceAll("\\n", "").trim():"");
			contenido.append(getContenido(nodeList.item(i)));
		}
		return contenido;
	}

	/**
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Calendar getCalendar(String fecha) throws Exception {		
		Calendar cal = null;		
		if (fecha != null && !fecha.trim().equals("")) {
			cal = Calendar.getInstance();
			cal.setTime(GstDate.convertirFecha(fecha));
			clearCalendar(cal);
		}	
		
		return cal;
	}
	
	public static Calendar clearCalendar(Calendar cal) {
		cal.clear(Calendar.ZONE_OFFSET);
		//cal.clear(Calendar.DST_OFFSET);
		return cal;
	}
	
	/**
	 * 
	 * @param st
	 * @param campo
	 * @return
	 */
	public static Integer getInteger(String campo, String valor) {		
		Integer in = null;
		if (valor != null && !valor.trim().equals("")) {
			try {
				in = Integer.valueOf(valor.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Compruebe el valor del campo " + campo + " (valor='" + valor + "'). No es un número válido.");
			}
		}
		return in;
	}	
	
	/**
	 * 
	 * @param st
	 * @param campo
	 * @return
	 */
	public static Double getDouble(String campo, String valor) {		
		Double in = null;
		if (valor != null && !valor.trim().equals("")) {
			try {
				in = Double.valueOf(valor.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Compruebe el valor del campo " + campo + " (valor='" + valor + "'). No es un número válido.");
			}
		}
		return in;
	}
	
	
	/**
	 * 
	 * @param st
	 * @param campo
	 * @return
	 */
	public static BigInteger getBigInteger(String campo, String valor) {		
		BigInteger in = null;
		if (valor != null && !valor.trim().equals("")) {
			try {
				in = new BigInteger(valor.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Compruebe el valor del campo " + campo + " (valor='" + valor + "'). No es un número válido.");
			}
		}
		return in;
	}
	
	/**
	 * 
	 * @param st
	 * @param campo
	 * @return
	 */
	public static Short getShort(String campo, String valor) {		
		Short in = null;
		if (valor != null && !valor.trim().equals("")) {
			try {
				in = Short.valueOf(valor.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Compruebe el valor del campo " + campo + " (valor='" + valor + "'). No es un número válido. Rango [" + Short.MIN_VALUE + " - " + Short.MAX_VALUE + "]");
			}
		}
		return in;
	}
	
	
	public static Long getLong(String campo, String valor) {		
		Long in = null;
		if (valor != null && !valor.trim().equals("")) {
			try {
				in = Long.valueOf(valor.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Compruebe el valor del campo " + campo + " (valor='" + valor + "'). No es un número válido.");
			}
		}
		return in;
	}

	public static BigDecimal getBigDecimal(String campo, String valor) {		
		BigDecimal in = null;
		if (valor != null && !valor.trim().equals("")) {
			try {
				in = new BigDecimal(valor.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Compruebe el valor del campo " + campo + " (valor='" + valor + "'). No es un número válido.");
			}
		}
		return in;
	}
	
	/**
	 * 
	 * @param xmlObject
	 * @return
	 * @throws Exception
	 */
	public static List<String> validate(XmlObject xmlObject) throws Exception {
		
		deleteEmptyNode(xmlObject.getDomNode());
		List<String> list = new ArrayList<String>();
		XmlOptions xmlOptions = new XmlOptions();
		List<XmlValidationError> errores = new ArrayList<XmlValidationError>();
		xmlOptions.setErrorListener(errores);
				
		if (!xmlObject.validate(xmlOptions)){
				
			String st = null;
			for (XmlValidationError error : errores) {
				Node node = error.getCursorLocation().getDomNode();
				String mensaje="";
				
				if (XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$EXPECTED_DIFFERENT_ELEMENT.equals(error.getErrorCode()) && node.getParentNode() != null) {
					//el error es pq no se ha rellenado el hermano del nodo
					node = node.getParentNode();
				}
				if (XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$MISSING_ELEMENT.equals(error.getErrorCode()) || XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$EXPECTED_DIFFERENT_ELEMENT.equals(error.getErrorCode())) {
					String campos = "";
					if (error.getExpectedQNames() != null) {
						for (int i = 0; i < error.getExpectedQNames().size(); i++) {
							QName qName = (QName) error.getExpectedQNames().get(i);
							if (qName != null) {
								if (i == 0){
									campos += qName.getLocalPart();
								} else {
									campos += ", " + qName.getLocalPart();
								}
							}
						}
					}
					
					mensaje += XmlError.formattedMessage(error.getErrorCode(), new String[]{null, campos});
				} else if (XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID.equals(error.getErrorCode())) {					
					mensaje += XmlError.formattedMessage(error.getErrorCode(), new String[]{null, getContenido(node).toString(), node.getNodeName()});
				} else if (XmlErrorCodes.DATATYPE_MAX_LENGTH_VALID$STRING.equals(error.getErrorCode()) || XmlErrorCodes.DATATYPE_MIN_LENGTH_VALID$STRING.equals(error.getErrorCode())) {
					mensaje += error.getMessage() + "'" + getContenido(node) + "'";
				} else if (XmlErrorCodes.ELEM_LOCALLY_VALID$NOT_NILLABLE.equals(error.getErrorCode())) {
					mensaje += XmlError.formattedMessage(error.getErrorCode(), new String[]{node.getNodeName()});
				} else {
					mensaje += error.getMessage();
				}
				st = getRutaNodo(node);
				st += ";" + mensaje;
				if (!list.contains(st)) {
					list.add(st);
					ClsLogging.writeFileLog(st, 3);
				}				
			}		
			
		}
		return list;
	}

	private static String getRutaNodo(Node domNode) {
		String ruta = domNode.getLocalName();
		Node padre = domNode.getParentNode();
		while (padre != null && padre.getLocalName() != null) {
			ruta = padre.getLocalName() + " " + SEPARADOR_RUTA_NODO + " " + ruta;
			padre = padre.getParentNode();
		}
		return ruta;
	}
}
