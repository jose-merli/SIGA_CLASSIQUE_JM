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

public class LogHandler extends BasicHandler {
	private static final long serialVersionUID = -550704611136593355L;

	public void invoke(MessageContext messageContext) throws AxisFault {
		Message msg = messageContext.getResponseMessage();
		
		if (msg==null){
			msg=messageContext.getRequestMessage();
		}

		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transform = tf.newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			transform.transform(new DOMSource(msg.getSOAPEnvelope().getAsDocument()), new StreamResult(baos));
			ClsLogging.writeFileLog(baos.toString(), 3);
		} catch (Exception e){
			ClsLogging.writeFileLogError("Error al dejar la traza con el mensaje SOAP.", e, 3);
		}
	}
}
