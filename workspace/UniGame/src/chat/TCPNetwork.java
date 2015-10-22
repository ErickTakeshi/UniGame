/*
 *	TCPNetwork.java
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
import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import static org.jsresources.apps.chat.Constants.*;


public class TCPNetwork
extends BaseNetwork
{
	private ServerSocket	m_serverSocket;
	private Socket		m_commSocket;


	public TCPNetwork(MasterModel masterModel)
	{
	    super(masterModel);
	}


    public void connect(InetAddress addr)
	{
	    Debug.out("connect(): begin");
	    try
	    {
		m_commSocket = new Socket(addr, getPort());
		setSocketOptions(m_commSocket);
	    }
	    catch (IOException e)
	    {
		Debug.out(e);
	    }
	}


    public InetAddress getPeer()
	{
	    return m_commSocket.getInetAddress();
	}

	public void disconnect()
	{
		try
		{
			m_commSocket.shutdownInput();
			m_commSocket.shutdownOutput();
		}
		catch (IOException e)
		{
			Debug.out(e);
		}
		try
		{
			m_commSocket.close();
		}
		catch (IOException e)
		{
			Debug.out(e);
		}
	}



	public boolean isConnected()
	{
		return m_commSocket != null && !m_commSocket.isClosed();
	}



    /** Enables listening.  This method has to be called with true
	before calling listen(). If listen() will no longer be used,
	setListen() should be called again with false to free system
	resources.
     */
	public void setListen(boolean bListen)
	{
	    if (bListen != isListening())
	    {
		if (bListen)
		{
		    try
		    {
			m_serverSocket = new ServerSocket(getPort());
			m_serverSocket.setSoTimeout(2000);
		    }
		    catch (IOException e)
		    {
			Debug.out(e);
		    }
		}
		else
		{
		    try
		    {
			m_serverSocket.close();
		    }
		    catch (IOException e)
		    {
			Debug.out(e);
		    }
		    m_serverSocket = null;
		}
	    }
	}


    private boolean isListening()
	{
	    return m_serverSocket != null;
	}


    public boolean listen()
	{
	    Socket s = null;
	    try
	    {
		s = m_serverSocket.accept();
		setSocketOptions(s);
	    }
	    catch (SocketTimeoutException e)
	    {
		//Debug.out(e);
	    }
	    catch (IOException e)
	    {
		Debug.out(e);
	    }
	    if (s != null)
	    {
		m_commSocket = s;
		return true;
	    }
	    else
	    {
		return false;
	    }
	}


	public InputStream createReceiveStream()
		throws IOException
	{
		return m_commSocket.getInputStream();
	}


	public OutputStream createSendStream()
		throws IOException
	{
		return m_commSocket.getOutputStream();
	}


    private static void setSocketOptions(Socket socket)
	{
	    try
	    {
		socket.setTcpNoDelay(TCP_NODELAY);
		if (TCP_SEND_BUFFER_SIZE > 0)
		    socket.setSendBufferSize(TCP_SEND_BUFFER_SIZE);
		if (TCP_RECEIVE_BUFFER_SIZE > 0)
		    socket.setReceiveBufferSize(TCP_RECEIVE_BUFFER_SIZE);
		//Debug.out("NODELAY: " + socket.getTcpNoDelay());
		Debug.out("TCP socket send buffer size: " + socket.getSendBufferSize());
		Debug.out("TCP socket receive buffer size: " + socket.getReceiveBufferSize());
	    }
	    catch (SocketException e)
	    {
		Debug.out(e);
	    }
	}
}


/*** TCPNetwork.java ***/
