/**
 * 
 */
package com.siga.ws;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author angelcpe
 *
 */
public class SigaWSHelper {

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

}
