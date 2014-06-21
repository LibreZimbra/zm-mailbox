/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra, Inc.
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
package com.zimbra.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;
import com.zimbra.common.mime.shim.JavaMailInternetAddress;
import com.zimbra.common.service.ServiceException.Argument;

public class MailUtil {
    public static void populateFailureDeliveryMessageFields(MimeMessage failedDeliverymm, String subject, String to, List<Argument> addressArgs, InternetAddress iAddr) throws MessagingException, UnsupportedEncodingException {		
        failedDeliverymm.setSubject("Send Partial Failure Notice");
        failedDeliverymm.setSentDate(new Date());
        failedDeliverymm.setFrom(iAddr);
        failedDeliverymm.setRecipient(RecipientType.TO, new JavaMailInternetAddress(to));

        StringBuilder text = new StringBuilder();
        String sentDate = new SimpleDateFormat("MMMMM d, yyyy").format(new Date());
        String sentTime = new SimpleDateFormat("h:mm a").format(new Date());

        text.append("Your message \"" + subject + "\" sent on " + sentDate + " at " + sentTime + " " + TimeZone.getDefault().getDisplayName() + " " +
                "could not be delivered to one or more recipients.\n\n" +
                "For further assistance, please send mail to postmaster.\n\n");

        List<String> invalidAddrs = new ArrayList<String> ();
        List<String> unsentAddrs = new ArrayList<String> ();

        if (addressArgs != null) {
            for (Argument arg : addressArgs) {
                if (arg.name != null && arg.value != null && arg.value.length() > 0) {
                    if (arg.name.equals("invalid"))
                        invalidAddrs.add(arg.value);
                    else if (arg.name.equals("unsent"))
                        unsentAddrs.add(arg.value);
                }
            }
        }

        for (String addr : invalidAddrs)
            text.append(addr + ":" + "Invalid Address\n");

        for (String addr : unsentAddrs)
            text.append(addr + ":" + "Unsent Address\n");

        failedDeliverymm.setText(text.toString());
        failedDeliverymm.saveChanges();
    }
    
    /* 
     * This is a wrapper around SMTPTransport meant for validating the recipient addresses.
     * This is helpful for capturing the addresses with 550 smtp error code.
     * The sendMessage() and send() methods will not send the message and always throws MessagingException.
     * The callsite can check to see if MessagingException is an instance of SendFailedException
     * and then get the list of invalid e-mail addresses. 
     */
    
    private class VerifyRcptSMTPTransport extends SMTPTransport {
        public VerifyRcptSMTPTransport(Session session, URLName urlname) {
            super(session, urlname);
        }

        @Override
        protected void rcptTo() throws MessagingException {
            super.rcptTo();
            // always throw the exception.
            throw new MessagingException();
        }
    }
    
    public static void validateRcptAddresses(Session session, Address[] addresses) throws MessagingException{
        VerifyRcptSMTPTransport.send(new MimeMessage(session), addresses);     
    }    
}