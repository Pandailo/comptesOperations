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
 * $Id: ReaderBean.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.reader;

import static org.ow2.jonas.examples.ear.entity.Compte.QN.ALL_COMPTES;
import static org.ow2.jonas.examples.ear.entity.Compte.QN.FIND_COMPTE;
import static org.ow2.jonas.examples.ear.entity.Mouvement.QN.ALL_OPERATIONS;
import static org.ow2.jonas.examples.ear.entity.Mouvement.QN.FIND_OPERATION;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;

/**
 *The {@link ReaderBean} EJB is an unrestricted, read-only, Stateless Bean.
 * @author Guillaume Sauthier
 */
@Stateless
@Local(LocalReader.class)
@Remote(RemoteReader.class)
public class ReaderBean implements LocalReader, RemoteReader {

    /**
     * Entity manager used by this bean.
     */
    @PersistenceContext
    private EntityManager entityManager = null;

	/**
     * Find a given {@link Compte} using it's name as a key.
     * @param name {@link Compte}'s name.
     * @return the first {@link Compte} that matches the given name.
     */
    @SuppressWarnings("unchecked")
    public Compte findCompte(final int id) {
        Query query = entityManager.createNamedQuery(FIND_COMPTE);
        query.setParameter("MYID", (long)(id));
		List<Compte> comptes = query.getResultList();
        if (comptes != null && comptes.size() > 0) {
            return comptes.get(0);
        }
        return null;
    }

    /**
     * Find a given {@link Operation} using it's name as a key.
     * @param name {@link Operation}'s name.
     * @return the first {@link Operation} that matches the given name.
     */
    @SuppressWarnings("unchecked")
    public Mouvement findOperation(final int id) {
        Query query = entityManager.createNamedQuery(FIND_OPERATION);
        query.setParameter("MYID", (long)(id));
        List<Mouvement> operations = query.getResultList();
        if (operations != null && operations.size() > 0) {
            return operations.get(0);
        }
        return null;
    }

    /**
     * @return the list of all the persisted {@link Author}s.
     */
    @SuppressWarnings("unchecked")
    public List<Compte> listAllComptes() {
		System.out.println("listAllComptes just got called !");
        return entityManager.createNamedQuery(ALL_COMPTES).getResultList();
    }

    /**
     * @return the list of all the persisted {@link Book}s.
     */
    @SuppressWarnings("unchecked")
    public List<Mouvement> listAllOperations() {
        return entityManager.createNamedQuery(ALL_OPERATIONS).getResultList();
    }

}
