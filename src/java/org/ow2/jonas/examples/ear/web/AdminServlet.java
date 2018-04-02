/**
 * JOnAS: Java(TM) Open Application Server
 * Copyright (C) 2007-2008 Bull S.A.S.
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
 * $Id: AdminServlet.java 17350 2009-05-13 14:15:43Z fornacif $
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.examples.ear.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ow2.jonas.examples.ear.entity.*;
import org.ow2.jonas.examples.ear.init.Initializer;
import org.ow2.jonas.examples.ear.reader.LocalReader;
import org.ow2.jonas.examples.ear.writer.LocalWriter;

/**
 * Defines a servlet that is accessing the two entities through a local session
 * bean.
 * @author Florent Benoit
 * @author Guillaume Sauthier
 */
public class AdminServlet extends HttpServlet {

    /**
     * Serializable class uid.
     */
    private static final long serialVersionUID = 7724116000656853982L;

    /**
     * Local writer bean.
     */
    @EJB
    private LocalWriter writerBean;

    /**
     * Local reader bean.
     */
    @EJB
    private LocalReader readerBean;

    /**
     * Initializer bean.
     */
    @EJB
    private Initializer initializerBean;


    /**
     * Called by the server (via the service method) to allow a servlet to
     * handle a GET request.
     * @param request an HttpServletRequest object that contains the request the
     *        client has made of the servlet
     * @param response an HttpServletResponse object that contains the response
     *        the servlet sends to the client
     * @throws IOException if an input or output error is detected when the
     *         servlet handles the GET request
     * @throws ServletException if the request for the GET could not be handled
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Ear Sample of Servlet accessing an EJB";

        printHTMLHeader(out, title);

        out.println("  <div class=\"links\">");
        out.println("    <table width=\"100%\" cellpadding=\"0\">");
        out.println("      <tr>");
        out.println("        <td>");

        // Try to init the DB if this is the very first call to this application
        init(out);

        out.println("<br />");

        // Print the add-author Form
        printAddCompteForm(out);
	//	printAddOperationForm(out);
        out.println("        </td>");
        out.println("        <td>");

        // Try to add the new author
        if(request.getParameter("balance") != null)
			addCompte(out, Double.parseDouble(request.getParameter("balance")));
		/* if(request.getParameter("compteOpe") != null){
			if(request.getParameter("montantOpe") != null){
				if(request.getParameter("typeOpe") != null){
					addOperation(out,Double.parseDouble(request.getParameter("montantOpe")),Integer.parseInt(request.getParameter("compteOpe")),request.getParameter("typeOpe"));
				}
			}
		}*/
		// Display updated list of authors
        printComptes(out);

        out.println("        </td>");
        out.println("      </tr>");
        out.println("    </table>");
        out.println("  </div>");

        printFooter(out);
        out.close();
    }

    /**
     * Print the page Footer.
     * @param out Servlet PrintWriter
     */
    private void printFooter(final PrintWriter out) {
        out.println("  <div class=\"footer\">");
        out.println("    <p>");
        out.println("      <a href=\"http://validator.w3.org/check/referer\">");
        out.println("        <img src=\"../img/valid-xhtml11.png\" alt=\"Valid XHTML 1.1!\"");
        out.println("             title=\"Valid XHTML 1.1!\" height=\"31\" width=\"88\" />");
        out.println("      </a>");
        out.println("      <a href=\"http://jigsaw.w3.org/css-validator/\">");
        out.println("        <img style=\"border:0;width:88px;height:31px\" src=\"../img/vcss.png\"");
        out.println("             title=\"Valid CSS!\" alt=\"Valid CSS!\" />");
        out.println("      </a>");
        out.println("    </p>");
        out.println("  </div>");

        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Print the page Header.
     * @param out Servlet {@link PrintWriter}
     * @param title page's title
     */
    private void printHTMLHeader(final PrintWriter out, final String title) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("  <head>");
        out.println("    <link type=\"text/css\" href=\"../ow2_jonas.css\" rel=\"stylesheet\" id=\"stylesheet\"/>");
        out.println("    <title>" + title + "</title>");
        out.println("  </head>");
        out.println("<body style=\"background : white; color : black;\">");

        out.println("  <div><a href=\"http://www.ow2.org\"><img src=\"../img/logoOW2.png\" alt=\"logo\"/></a></div>");
        out.println("  <div class=\"logos\">");
        out.println("    <img src=\"../img/tomcat.gif\" alt=\"Tomcat Logo\"/>");
        out.println("    <img src=\"../img/jetty.gif\" alt=\"Jetty Logo\"/>");
        out.println("    <img src=\"../img/ow_jonas_logo.gif\" alt=\"JOnAS Logo\"/>");
        out.println("  </div>");

        out.println("  <div class=\"titlepage\">" + title + "</div>");
    }

   
	/**
     * Add a named {@link Compte} in the model.
     * @param out {@link PrintWriter} used for Exception printing.
     * @param name Compte's name
     */
    private void addCompte(final PrintWriter out, final Double balance) {
		if(balance != null)
		{
			try {
				// Persists a new Compte
				Compte compte = new Compte(balance.doubleValue());
				writerBean.addCompte(compte);
			} catch (Exception e) {
				printException(out, "Cannot add a new compte (" + balance.doubleValue() + ")", e);
				return;
				}
		}
    }
    /**
     * Add a named {@link Compte} in the model.
     * @param out {@link PrintWriter} used for Exception printing.
     * @param name Compte's name
     */
    private void addOperation(final PrintWriter out, final double montant,final int id,final String type) {
		if(type != null)
		{
			try {
				Compte compte = readerBean.findCompte(id);
				// Persists a new Compte
				Mouvement ope = new Mouvement(montant,compte,type);
				writerBean.addOperation(ope);
			} catch (Exception e) {
				printException(out, "Cannot add a new operation", e);
				return;
				}
		}
    }
   
	 /**
     * Init list of comptes/operations.
     * @param out the given writer
     */
    private void init(final PrintWriter out) {
        out.println("Initialize comptes and their operations...<br/>");

        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            printException(out, "Cannot init list of  comptes and their operations", e);
            return;
        }
    }
    /**
     * Display authors.
     * @param out the given writer
     */
    private void printComptes(final PrintWriter out) {
       out.println("Getting comptes");
        out.println("<br /><br />");

        // Get list of Comptes
        List<Compte> comptes = null;
        try {
            comptes = readerBean.listAllComptes();
        } catch (Exception e) {
            printException(out, "Cannot call listAllComptes on the bean", e);
            return;
        }

        // List for each author, the name of books
        if (comptes != null) {
            for (Compte compte : comptes) {
                out.println("List of operations on compte '" + compte.getId() + "' :");
                out.println("<ul>");
                Collection<Mouvement> operations = compte.getOperations();
                if (operations == null) {
                    out.println("<li>No operation !</li>");
                } else {
                    for (Mouvement operation : operations) {
                        out.println("<li>Title '" + operation.toString() + "'.</li>");
                    }
                }
                out.println("</ul>");

            }
        } else {
            out.println("No compte found !");
        }

    }
	 /**
     * Display comptes.
     * @param out the given writer
     */
    private void printAddCompteForm(final PrintWriter out) {

        out.println("Add a new Compte:");
        out.println("<form action=\"add-compte\" method=\"get\">");
        out.println("  <div>");
        out.println("    <input name=\"balance\" type=\"text\" value=\"\"/>");
        out.println("    <input type=\"submit\" value=\"Add\"/>");
        out.println("  </div>");
        out.println("</form>");
    }
    /**
     * Display comptes.
     * @param out the given writer
     */
    private void printAddOperationForm(final PrintWriter out) {

        out.println("Add a new Operation:");
        out.println("<form action=\"add-compte\" method=\"get\">");
        out.println("  <div>");
		out.println("    <label value=\"montantOpe\" >");
        out.println("    <input name=\"montantOpe\" type=\"text\" value=\"\"/>");
        out.println("    <label value=\"typeOpe\" >");
        out.println("    <input name=\"typeOpe\" type=\"text\" value=\"\"/>");
        out.println("    <label value=\"compteOpe\" >");
        out.println("    <input name=\"compteOpe\" type=\"text\" value=\"\"/>");
        out.println("    <input type=\"submit\" value=\"Add\"/>");
        out.println("  </div>");
        out.println("</form>");
    }

    /**
     * If there is an exception, print the exception.
     * @param out the given writer
     * @param errMsg the error message
     * @param e the content of the exception
     */
    private void printException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<p>Exception : " + errMsg);
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre></p>");
    }

}
