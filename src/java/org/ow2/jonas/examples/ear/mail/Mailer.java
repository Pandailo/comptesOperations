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
 * $Id: Mailer.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.mail;

/**
 * The {@link Mailer} business interface is used to send a status
 * mail, with all the {@link org.ow2.jonas.examples.ear.entity.Author}s
 * and {@link org.ow2.jonas.examples.ear.entity.Book}s in the library.
 * @author Guillaume Sauthier
 */
public interface Mailer {

    /**
     * Send a mail to the given mail address.
     * @param address target mail address (must be of the form: xyz@abc.z)
     */
    void sendStatusMail(final String address);
}
