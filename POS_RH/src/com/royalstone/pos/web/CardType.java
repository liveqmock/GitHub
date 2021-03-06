/*
 * Created on 2004-8-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.royalstone.pos.web;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.db.PosMinister;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CardType extends HttpServlet {
	
	public void doPost(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	System.err.println("CardType ...");
	OutputStream out = response.getOutputStream();

	Connection con = null;
	Context ctx = null;
	DataSource ds = null;

	try {
		response.setContentType("text/html");


		ctx = new InitialContext();
		if (ctx != null)
			ds = (DataSource) ctx.lookup("java:comp/env/dbpos");
		if (ds != null)
			con = ds.getConnection();

		if (con != null) {
			Element elm_card = PosMinister.getCardType(con);
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(elm_card), out);
		}

		out.flush();
		out.close();

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		DBConnection.closeAll(null, null, con);
	}
    }
}
