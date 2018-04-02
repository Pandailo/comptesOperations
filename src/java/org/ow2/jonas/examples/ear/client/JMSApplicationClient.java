/**
 * JOnAS: Java(TM) Open Application Server
 * Copyright (C) 2008 Bull S.A.S.
 * Contact: jonas-team@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 *
 * --------------------------------------------------------------------------
 * $Id: JMSApplicationClient.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.client;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.text.MessageFormat;

import javax.jms.*;
import javax.ejb.EJB;
import javax.annotation.Resource;

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;
import org.ow2.jonas.examples.ear.init.Initializer;
import org.ow2.jonas.examples.ear.reader.RemoteReader;

/**
 * This application-client shows usage of JMS destinations to
 * interact with the server-side application.
 * @author Guillaume Sauthier
 */
public final class JMSApplicationClient {

    /**
     * Number of Books to be created.
     */
    private static final int ITERATION_NUMBER = 10;

    /**
      * Link to the initializer bean.
      */
     @EJB
     private static  Initializer initializerBean;

    /**
     *  JMS conectionFactoery
     */
    // Resource injection
     @Resource(mappedName="JQCF")
      private static ConnectionFactory factory;

    /**
     *   JMS Queue  SampleQueue
     */
    // Resource injection
     @Resource(mappedName="SampleQueue")
     private static Queue queue;


    /**
      * Link to the Remote Reader bean. Bean will be injected by JOnAS.
      */
     @EJB
     static private RemoteReader readerBean;

    /**
     * Empty default constructor for utility class.
     */
    private JMSApplicationClient() {

    }

    /**
     * @param args Command line arguments
     * @throws Exception InitialContext creation failure / JMS Exception
     */
    public static void main(final String[] args) throws Exception {

        PrintStream out = System.out;

        // Print Header
        out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        out.println("OW2 JOnAS :: EAR Sample :: Messager Application Client     ");
        out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");




        // Init. data if needed
        out.print("Initialization ... ");

        initializerBean.initializeEntities();
        out.println("Done.");

        // Send Book creation Messages
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer= session.createProducer(queue);

        //for (int i = 0; i < ITERATION_NUMBER; i++) {
		String Type="debit";
		double montant=5.5;	
		int idCompte = 7;
		String infos = Type +";"+montant+";"+idCompte;
		Message message = session.createTextMessage(infos);
		messageProducer.send(message);
		out.println("Sent creation order for '" + infos + "'");
        //}

        // Close JMS objects
        messageProducer.close();
        session.close();
        connection.close();

        // Wait for some time ...
        // Remember JMS is for asynchronous messages :)
        final long period = 2500;
        out.println(MessageFormat.format("Wait for {0} ms...", period));
        Thread.sleep(period);

        out.println(MessageFormat.format("The RemoteReader Bean reference is: {0}", readerBean));
        // List Authors and Books
        List<Compte> comptes = readerBean.listAllComptes();
        for (Compte compte : comptes) {
            out.println(MessageFormat.format(" * {0}", compte.getId()));
            Collection<Mouvement> movs = compte.getOperations();
            for (Mouvement mvt : movs) {
                out.println(MessageFormat.format(" -> {0} [id: {1}]", mvt.getAmount(), mvt.getId()));
            }
        }

        out.println("Success.");

    }
}
