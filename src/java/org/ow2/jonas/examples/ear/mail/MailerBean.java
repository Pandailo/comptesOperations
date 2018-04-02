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
 * $Id: MailerBean.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.mail;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessageContext;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimePartDataSource;

import org.ow2.jonas.examples.ear.reader.LocalReader;

/**
 * @author Guillaume Sauthier
 */
@Stateless(mappedName="myMailerBean")
@Remote(Mailer.class)
public class MailerBean implements Mailer {

    /**
     * Mail Session used to send the Mail.
     */
    @Resource(mappedName="mailSession_1")
    private Session mailSession;

    /**
     * Template for the message's content.
     */
    @Resource(mappedName="mailMimePartDS_1")
    private MimePartDataSource mimePartDatasource;

    /**
     * {@link LocalReader} EJB (Local interface).
     */
    @EJB
    private LocalReader reader;

    /**
     * Send a mail to the given mail address.
     * @param address target mail address (must be of the form: xyz@abc.z)
     * @see org.ow2.jonas.examples.ear.mail.Mailer#sendStatusMail(java.lang.String)
     */
    public void sendStatusMail(final String address) {

        Address mailAddress = null;
        try {
            mailAddress = new InternetAddress(address);
        } catch (AddressException e) {
            System.err.println("Invalid mail address: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        MessageContext context = mimePartDatasource.getMessageContext();
        Message message = context.getMessage();
        try {
            message.setContent(getContent(), "text/plain");
        } catch (MessagingException e) {
            System.err.println("Cannot set message content:" + e.getMessage());
            e.printStackTrace(System.err);
            return;
        }

        Transport transport = null;
        try {
            transport = mailSession.getTransport(mailAddress);
        } catch (NoSuchProviderException e) {
            System.err.println("No provider found for @:" + address);
            e.printStackTrace(System.err);
            return;
        }
        try {
            transport.connect();
            transport.sendMessage(message, new Address[] {mailAddress});
            transport.close();
        } catch (MessagingException e) {
            System.err.println("Cannot send message:" + e.getMessage());
            e.printStackTrace(System.err);
            return;
        }

        System.out.println("Mail successfully sent to: " + address);
    }

    /**
     * Generate the mail's content.
     * @return the mail message content.
     */
    private String getContent() {

        StringBuilder sb = new StringBuilder();

        // Print Header
        sb.append("---------------------------------------------------\n");
        sb.append(" OW2 JOnAS EAR Sample Mailer Bean.\n");
        sb.append("---------------------------------------------------\n");
        sb.append("Generated the " + new Date() + "\n");
        sb.append("\n");

     
        // Print the footer
        sb.append("\n");
        sb.append("Enjoy your new JOnAS !\n");
        sb.append("\n");
        sb.append("       -- JOnAS Team\n");

        return sb.toString();
    }

}
