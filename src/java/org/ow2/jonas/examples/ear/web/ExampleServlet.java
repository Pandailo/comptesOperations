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
 * $Id: ExampleServlet.java 17350 2009-05-13 14:15:43Z fornacif $
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

import org.ow2.jonas.examples.ear.entity.Compte;
import org.ow2.jonas.examples.ear.entity.Mouvement;
import org.ow2.jonas.examples.ear.init.Initializer;
import org.ow2.jonas.examples.ear.reader.LocalReader;

/**
 * Defines a servlet that is accessing the two entities through a local session
 * bean.
 * @author Florent Benoit
 */
public class ExampleServlet extends HttpServlet {

    /**
     * Serializable class uid.
     */
    private static final long serialVersionUID = -3172627111841538912L;


    /**
     * Link to the Local Reader bean. Bean will be injected by JOnAS.
     */
    @EJB
    private LocalReader readerBean;

    /**
     * Link to the initializer bean.
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
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("  <head>");
        out.println("    <link type=\"text/css\" href=\"ow2_jonas.css\" rel=\"stylesheet\" id=\"stylesheet\" />");
        out.println("    <title>Ear Sample of Servlet accessing an EJB</title>");
        out.println("  </head>");
        out.println("<body style=\"background : white; color : black;\">");

        out.println("  <div><a href=\"http://www.ow2.org\"><img src=\"img/logoOW2.png\" alt=\"logo\"/></a></div>");
        out.println("  <div class=\"logos\">");
        out.println("    <img src=\"img/tomcat.gif\" alt=\"Tomcat Logo\"/>");
        out.println("    <img src=\"img/jetty.gif\" alt=\"Jetty Logo\"/>");
        out.println("    <img src=\"img/ow_jonas_logo.gif\" alt=\"JOnAS Logo\"/>");
        out.println("  </div>");

        out.println("  <div class=\"titlepage\">Ear sample of Servlet accessing an EJB</div>");


        out.println("  <div class=\"links\">");
        init(out);
        out.println("    <br />");
        out.println("  </div>");

        out.println("  <div class=\"links\">");
        displayComptes(out);
        
        out.println("  </div>");

        out.println("  <div class=\"links\">");
        out.println("    <form action=\"secured/Admin\" method=\"get\">");
        out.println("      <div><input type=\"submit\" value=\"Modify Library Content\"/></div>");
        out.println("    </form>");
        out.println("  </div>");

        out.println("  <div class=\"footer\">");
        out.println("    <p>");
        out.println("      <a href=\"http://validator.w3.org/check/referer\">");
        out.println("        <img src=\"img/valid-xhtml11.png\" alt=\"Valid XHTML 1.1!\"");
        out.println("             title=\"Valid XHTML 1.1!\" height=\"31\" width=\"88\" />");
        out.println("      </a>");
        out.println("      <a href=\"http://jigsaw.w3.org/css-validator/\">");
        out.println("        <img style=\"border:0;width:88px;height:31px\" src=\"img/vcss.png\"");
        out.println("             title=\"Valid CSS!\" alt=\"Valid CSS!\" />");
        out.println("      </a>");
        out.println("    </p>");
        out.println("  </div>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     * Init list of authors/books.
     * @param out the given writer
     */
    private void init(final PrintWriter out) {
        out.println("Initialize authors and their books...<br/>");

        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            displayException(out, "Cannot init list of authors with their books", e);
            return;
        }
    }

   
    	 /**
     * Display comptes.
     * @param out the given writer
     */
    private void displayComptes(final PrintWriter out) {
        out.println("Getting comptes");
        out.println("<br /><br />");

        // Get list of Comptes
        List<Compte> comptes = null;
        try {
			System.out.println("Trying to call listAllComptes!");
            comptes = readerBean.listAllComptes();
        } catch (Exception e) {
            displayException(out, "Cannot call listAllComptes on the bean", e);
            return;
        }

        // List for each author, the name of books
        if (comptes != null) {
            for (Compte compte : comptes) {
                out.println("List of operations on compte '" + compte.getId() + "' :");
                out.println("<ul>");
                // Propre
                Collection<Mouvement> operations = compte.getOperations();
                if (operations == null) {
                    out.println("<li>No operation !</li>");
                } else {
                    for (Mouvement operation : operations) {
                        out.println("<li>Title '" + operation.toString() + "'.</li>");
                    }
                }
                // Fin propre
                
                // Crade
				 // Get list of Mouvements
				/*List<Mouvement> movs = null;
				try {
					movs = readerBean.listAllOperations();
				} catch (Exception e) {
					displayException(out, "Cannot call listAllOperationss on the bean", e);
					return;
				}
				for (Mouvement mov : movs) {
					if (mov.getCompte().getId() ==  compte.getId()) {
						out.println("<li>Ope : '" + mov.toString() + "'.</li>");
					} 
				}	  */
				// Fin crade
                out.println("</ul>");

            }
        } else {
            out.println("No compte found !");
        }

    }

    /**
     * If there is an exception, print the exception.
     * @param out the given writer
     * @param errMsg the error message
     * @param e the content of the exception
     */
    private void displayException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<p>Exception : " + errMsg);
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre></p>");
    }

}
