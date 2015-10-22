/*
 *	ConnectionSettings.java
 */

/*
 * Copyright (c) 2004 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.jsresources.apps.chat;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import static org.jsresources.apps.chat.Constants.*;


public class ConnectionSettings
{
	private int		m_nPort;
	private int		m_nConnectionType;
	private int		m_nFormatCode;

	public ConnectionSettings(MasterModel masterModel)
	{
	    setPort(DEFAULT_PORT);
	    setFormatCode(DEFAULT_FORMAT_CODE);
	    setConnectionType(DEFAULT_CONNECTION_TYPE);
	}



	public void setPort(int nPort)
	{
		m_nPort = nPort;
	}


	public int getPort()
	{
		return m_nPort;
	}

	public void setConnectionType(int nConnectionType)
	{
		m_nConnectionType = nConnectionType;
	}


	public int getConnectionType()
	{
		return m_nConnectionType;
	}

	public void setFormatCode(int nFormatCode)
	{
		m_nFormatCode = nFormatCode;
	}


	public int getFormatCode()
	{
		return m_nFormatCode;
	}
}


/*** ConnectionSettings.java ***/
