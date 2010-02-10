/**
 * 
 */
package com.siga.eejg;

import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaInfoConsultaInfoAAPP.RespuestaConsultaInfoAAPP;
import org.w3c.dom.Node;

import com.atos.utils.ClsLogging;



/**
 * @author angelcpe
 *
 */
public class LogHandler extends BasicHandler {
	

	/* (non-Javadoc)
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		Message msg = messageContext.getCurrentMessage();		
		ClsLogging.writeFileLog(msg.getSOAPPartAsString(), 3);				
	}
	
}
