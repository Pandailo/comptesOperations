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
 * $Id: Reader.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.reader;

import java.util.List;

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;
/**
 * The {@link Reader} business interface is an un-restricted
 * read only view on the entities.
 * @author Guillaume Sauthier
 */
public interface Reader {
	/**
     * @return the list of all the persisted {@link Compte}s.
     */
    List<Compte> listAllComptes();

    /**
     * @return the list of all the persisted {@link Operation}s.
     */
    List<Mouvement> listAllOperations();

    /**
     * Find a given {@link Compte} using it's name as a key.
     * @param name {@link Compte}'s name.
     * @return the first {@link Compte} that matches the given name.
     */
    Compte findCompte(final int id);

    /**
     * Find a given {@link Operation} using it's name as a key.
     * @param name {@link Operation}'s name.
     * @return the first {@link Operation} that matches the given name.
     */
    Mouvement findOperation(final int id);
}
