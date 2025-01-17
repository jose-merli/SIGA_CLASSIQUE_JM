package com.siga.Utilidades;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.GenWebServiceLogAdm;
import com.siga.beans.GenWebServiceLogBean;

public class LogBDDHandler extends BasicHandler {
	private static final long serialVersionUID = -550704611136593355L;
	
	private UsrBean usrBean = null;
	private String idInstitucion = null;
	private String logDescripcion = null;
	
	public LogBDDHandler(UsrBean usrBean, String idInstitucion, String logDescripcion) {
		super();
		this.usrBean = usrBean;
		this.idInstitucion = idInstitucion;
		this.logDescripcion = logDescripcion;		
	}
	
	public void invoke(MessageContext messageContext) throws AxisFault {
		String rqRs = "RS";
		Message msg = messageContext.getResponseMessage();
		
		if (msg==null){
			rqRs = "RQ";
			msg=messageContext.getRequestMessage();
		}

		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transform = tf.newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			transform.transform(new DOMSource(msg.getSOAPEnvelope().getAsDocument()), new StreamResult(baos));
			String xml = baos.toString();
//			if (xml != null && xml.length() > 500) {
//				xml = xml.substring(0, 200) + " ... " + xml.substring(xml.length() - 200);
//			}
			ClsLogging.writeFileLog(xml, 3);
//			XmlObject.Factory.parse(xml).save(new java.io.File("c:/tmp/" + rqRs + ".xml"));
			
			//TODO si los parámetros no son nulos insertamos en la tabla
			if (usrBean != null && idInstitucion != null) {		
				
				GenWebServiceLogBean bean = new GenWebServiceLogBean();
				bean.setIdInstitucion(Integer.parseInt(idInstitucion));
				bean.setRqRs(rqRs);
				bean.setXmlSoap(baos.toString());
				bean.setDescripcion(logDescripcion);
				
				GenWebServiceLogAdm genWebServiceLogAdm = new GenWebServiceLogAdm(usrBean);				
				genWebServiceLogAdm.insert(bean);
				
			} else {
				ClsLogging.writeFileLog("No se ha podido insertar en bdd la traza del webservice porque no se han recibido los parámetros de usuario o idinstitucion correctamente. " + baos.toString(), 3);
			}
		} catch (Exception e){
			ClsLogging.writeFileLogError("Error al dejar la traza con el mensaje SOAP. Idinstitucion = " + idInstitucion, e, 3);
		}
	}
}
