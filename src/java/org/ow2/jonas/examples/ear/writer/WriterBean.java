/**
 * JOnAS: Java(TM) Open Application Server
 * Copyright (C) 1999-2008 Bull S.A.S.
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
 * $Id: WriterBean.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.writer;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;
/**
 * This is an example of Session Bean, stateless, secured, available
 * with a Local and a Remote interface (with the same methods).
 * @author JOnAS team
 */
@Stateless
@Remote(RemoteWriter.class)
@Local(LocalWriter.class)
@DeclareRoles("earsample")
@RolesAllowed("earsample")
public class WriterBean implements LocalWriter, RemoteWriter {

    /**
     * Entity manager used by this bean.
     */
    @PersistenceContext
    private EntityManager entityManager = null;

    /**
     * Persists a new {@link Compte}.
     * @param compte {@link Compte} to add.
     */
    public void addCompte(final Compte compte) {
        entityManager.persist(compte);
    }

    /**
     * Persists a new {@link Operation}.
     * @param operation {@link Operation} to add.
     */
    public void addOperation(final Mouvement operation) {
		Compte c = operation.getCompte();
		if(operation.getTypeOpe() == "credit"){
			c.setBalance(c.getBalance()+operation.getAmount());
		}else{
			c.setBalance(c.getBalance()-operation.getAmount());
		}
        entityManager.persist(operation);
        entityManager.persist(c);
    }

    /**
     * Cascade remove an {@link Compte}.
     * @param compte {@link Compte} to be removed.
     */
    public void removeCompte(final Compte compte) {
        entityManager.remove(compte);
    }

    /**
     * Cascade remove a {@link Operation}.
     * @param operation {@link Operation} to be removed.
     */
    public void removeOperation(final Mouvement operation) {
        entityManager.remove(operation);
    }
}
