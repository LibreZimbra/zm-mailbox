/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2004, 2005, 2006, 2007, 2009, 2010, 2011, 2013 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.common.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @since 2004. 10. 26.
 * @author jhahm
 */
public class TcpServerOutputStream extends BufferedOutputStream {

    protected static final byte[] CRLF = { (byte) 13, (byte) 10 };

    public TcpServerOutputStream(OutputStream out) {
        super(out);
    }

    public TcpServerOutputStream(OutputStream out, int size) {
        super(out, size);
    }

    public void writeLine() throws IOException {
        write(CRLF, 0, CRLF.length);
    }

    public void writeLine(String str) throws IOException {
        byte[] data = str.getBytes();
        write(data, 0, data.length);
        writeLine();
    }
}
