/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2013 Zimbra, Inc.
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
/**
 * 
 */
package com.zimbra.common.util;

import com.zimbra.common.util.Log.Level;

public class AccountLogger {
    private String mAccountName;
    private String mCategory;
    private Level mLevel;
    
    public AccountLogger(String category, String accountName, Level level) {
        mCategory = category;
        mAccountName = accountName;
        mLevel = level;
    }
    
    public String getAccountName() { return mAccountName; }
    public String getCategory() { return mCategory; }
    public Level getLevel() { return mLevel; }
}