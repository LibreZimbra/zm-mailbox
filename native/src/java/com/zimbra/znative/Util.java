/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2005, 2006, 2007, 2009, 2010, 2012, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.znative;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Util {

    private static final boolean mHaveNativeCode;

    public static boolean loadLibrary() {
        if (mHaveNativeCode) {
            return mHaveNativeCode;
        }

        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            /* We do not have the shared library for windows yet. */
            return false;
        } else {
            try {
                System.loadLibrary("zimbra-native");
                return true;
            } catch (UnsatisfiedLinkError ule) {
                //this lets unit tests run from command line (i.e. 'ant test') without setting up native
                String skipNative = System.getProperty("zimbra.native.required");
                if (skipNative != null && skipNative.equalsIgnoreCase("false")) {
                    System.err.println("WARNING: native libraries not available, " +
                        "however zimbra.native.required is false." +
                        "You are probably running unit tests; " +
                        "if you are not testing this is a problem. " +
                        "DO NOT run production with this warning");
                } else {
                    /* On non-Windows, we fail if the shared library is
                     * not present for two reasons: (a) it lets porters
                     * know that this is something they have to deal with
                     * and (b) if tomcat is started as root, and the
                     * shared library did not load for some reason, drop
                     * privileges would not work. */
                    halt("Failed to loadLibrary(zimbra-native)", ule);
                }
            }
            return false;
        }
    }

    static {
        mHaveNativeCode = loadLibrary();
    }

    public static boolean haveNativeCode() {
        return mHaveNativeCode;
    }

    /**
     * Logs the given message and shuts down the server.  This method
     * is for use during the early life of the server, where Log4j has
     * not been initialized and/or we are unable to call Zimbra.halt.
     * There is no native code involved here.
     *
     * @param message the message to log before shutting down
     */
    public static void halt(String message) {
        try {
            System.err.println("Fatal error: terminating: " + message);
        } finally {
            Runtime.getRuntime().halt(1);
        }
    }


    /**
     * Logs the given message and shuts down the server.  This method
     * is for use during the early life of the server, where Log4j has
     * not been initialized and/or we are unable to call Zimbra.halt.
     * There is no native code involved here.
     *
     * @param message the message to log before shutting down
     * @param t the exception that was thrown
     */
    public static void halt(String message, Throwable t) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println(message);
            t.printStackTrace(pw);
            System.err.println("Fatal error: terminating: " + sw.toString());
        } finally {
            Runtime.getRuntime().halt(1);
        }
    }
}
