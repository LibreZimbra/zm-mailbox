/*
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.zimbra.com/license
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Original Code is: Zimbra Collaboration Suite Server.
 * 
 * The Initial Developer of the Original Code is Zimbra, Inc.
 * Portions created by Zimbra are Copyright (C) 2006 Zimbra, Inc.
 * All Rights Reserved.
 * 
 * Contributor(s): 
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.dav.service.method;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.zimbra.cs.dav.DavContext;
import com.zimbra.cs.dav.DavException;
import com.zimbra.cs.dav.DavProtocol;
import com.zimbra.cs.dav.resource.Collection;
import com.zimbra.cs.dav.resource.DavResource;
import com.zimbra.cs.dav.resource.MailItemResource;
import com.zimbra.cs.dav.resource.UrlNamespace;

public class Copy extends Move {
	public static final String COPY  = "COPY";
	public String getName() {
		return COPY;
	}
	
	public void handle(DavContext ctxt) throws DavException, IOException {
		String destination = ctxt.getRequest().getHeader(DavProtocol.HEADER_DESTINATION);
		if (destination == null)
			throw new DavException("no destination specified", HttpServletResponse.SC_BAD_REQUEST, null);
		DavResource rs = ctxt.getRequestedResource();
		Collection col = UrlNamespace.getCollectionAtUrl(ctxt, destination);
		if (!(col instanceof MailItemResource))
			throw new DavException("cannot copy", HttpServletResponse.SC_BAD_REQUEST, null);
		MailItemResource mir = (MailItemResource) rs;
		MailItemResource copy = mir.copy(ctxt, col);

		renameIfNecessary(ctxt, copy, destination);
		ctxt.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}
