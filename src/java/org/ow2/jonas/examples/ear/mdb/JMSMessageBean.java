/**
 * JOnAS: Java(TM) Open Application Server
 * Copyright (C) 2008 Bull S.A.S.
 * Contact: jonas-team@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * --------------------------------------------------------------------------
 * $Id: JMSMessageBean.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.mdb;

import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.math.BigDecimal;
import org.ow2.jonas.examples.ear.reader.LocalReader;
import org.ow2.jonas.examples.ear.writer.LocalWriter;

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;
/**
 * The {@link JMSMessageBean} is a message driven bean activated when a JMS
 * {@link Message} comes to a given Destination.
 * For each new {@link Message}, this bean will create and persists a new
 * {@link Book} instance.
 * This MDB is annotated with {@link RunAs} because it uses a secured
 * business interface.
 * @author Guillaume Sauthier
 */
@MessageDriven(activationConfig={
        @ActivationConfigProperty(propertyName="destination",
                                  propertyValue="SampleQueue"),
        @ActivationConfigProperty(propertyName="destinationType",
                                  propertyValue="javax.jms.Queue")
        })
@RunAs("earsample")
public class JMSMessageBean implements MessageListener {

    /**
     * Secured business interface.
     */
    @EJB
    private LocalWriter writer;

    /**
     * Unsecured {@link LocalReader} business interface.
     */
    @EJB
    private LocalReader reader;

    /**
     * Called when a new JMS {@link Message} is received on the destination.
     * This method will use the {@link LocalWriter} Bean interface to add
     * Books to a given Author.
     * @param message {@link Message} containing {@link Book} title.
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(final Message message) {

        // TODO to be removed
        System.out.println("Received JMS Message: " + message);
		String typeOpe = "";
		double montant = 0.0;
		int idCompte = 0;
        // Extract Message's text value
        String text = null;
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                text = textMessage.getText();
            } catch (JMSException e) {
                System.err.println("Unexpected Exception: " + e.getMessage());
                e.printStackTrace(System.err);
                return;
            }
        } else {
            // not a TextMessage, I don't know what to do with it
            return;
        }
        try {
			//décomposition du message reçu par JMS
			String[] decomp = text.split(";");
			typeOpe=decomp[0];
			montant=Double.parseDouble(decomp[1]);
			idCompte = Integer.parseInt(decomp[2]);
		}catch (Exception e) {
                System.err.println("Unexpected Exception: " + e.getMessage());
                e.printStackTrace(System.err);
                return;
		}
		if(text!=null){
			Compte compte = reader.findCompte(idCompte);
			if (compte == null) {
				compte = new Compte(0.0);
				writer.addCompte(compte);
			}	
			// Persist the new operation
			Mouvement newOpe = new Mouvement(montant,compte,typeOpe);
			writer.addOperation(newOpe);
		}else{
			System.err.println("Couldn't read received message");    
		}
    }

}
