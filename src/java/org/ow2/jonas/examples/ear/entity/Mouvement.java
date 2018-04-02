/**
 * JOnAS: Java(TM) Open Application Server
 * Copyright (C) 2007 Bull S.A.S.
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
 * $Id: Book.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
/**
 * @author Moi
 */
@Entity
@NamedQueries({@NamedQuery(name=Mouvement.QN.ALL_OPERATIONS, query="select o FROM Mouvement o"),
               @NamedQuery(name=Mouvement.QN.FIND_OPERATION, query="select o FROM Mouvement o WHERE o.id = :MYID")
})
public class Mouvement implements Serializable {

    /**
     * Defines Query names.
     */
    public static interface QN {
        /**
         * Search all operations.
         */
        String ALL_OPERATIONS = "Operation.allOperations";

        /**
         * Search a operation.
         */
        String FIND_OPERATION = "Operation.findOperation";
    }

    /**
     * Serial Version UID.
     */	
    private static final long serialVersionUID = 0L;

    /**
     * Primary key.
     */
    private long id;
    /**
     * Type d'opération
     */
    private String typeOpe;

    /**
     * Author's book.
     */
    private Compte compte;

    /**
     * title of the book.
     */
    private BigDecimal amount;

    /**
     * Default constructor.
     */
    public Mouvement() {

    }

    /**
     * Constructor. Build a new Book with the given title and written by the
     * given author.
     * @param title the given title
     * @param author the given author.
     */
    public Mouvement(final double amount, final Compte compte,final String typeOpe) {
        setAmount(amount);
        setCompte(compte);
		setTypeOpe(typeOpe);
    }

    /**

     */
    @ManyToOne
    @JoinColumn(name="compte")
    public Compte getCompte() {
        return compte;
    }

    /**

     */
    public void setCompte(final Compte compte) {
        this.compte = compte;
    }

    /**
     * @return the title of this book.
     */
    public double getAmount() {
        return amount.doubleValue();
    }

    /**
     * Set the title of the book.
     * @param title - the title of the book
     */
    public void setAmount(final double montant) {
        BigDecimal a = new BigDecimal(montant, MathContext.DECIMAL64);
        this.amount = a;
    }

	/**
     * @return the type of this opeartion.
     */
    public String getTypeOpe() {
        return typeOpe;
    }

    /**
     * Set the type of this operation.
     * @param type - the type wanted for the operation
     */
    public void setTypeOpe(final String type) {
        this.typeOpe = type;
    }
    /**
     * @return an id for this object (incremented automatically)
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * Sets the id of this author object.
     * @param id the given id of this author
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return String representation of this entity object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append("[id=");
        sb.append(getId());
        sb.append(", amount=");
        sb.append(getAmount());
        sb.append("]");
        return sb.toString();
    }
    
}
