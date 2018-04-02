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
 * $Id: Writer.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.writer;

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;

/**
 * Remote interface for the bean Writer.
 * @author JOnAS team
 */
public interface Writer {

	/**
     * Persists a new {@link Compte}.
     * @param compte {@link Compte} to add.
     */
    void addCompte(final Compte compte);

    /**
     * Persists a new {@link Operation}.
     * @param operation {@link Operation} to add.
     */
    void addOperation(final Mouvement operation);

    /**
     * Cascade remove an {@link Compte}.
     * @param compte {@link Compte} to be removed.
     */
    void removeCompte(final Compte compte);

    /**
     * Cascade remove a {@link Operation}.
     * @param operation {@link Operation} to be removed.
     */
    void removeOperation(final Mouvement operation);
}
