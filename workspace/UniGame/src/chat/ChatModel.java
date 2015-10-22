/*
 *	ChatModel.java
 */

/*
 * Copyright (c) 2004 by Matthias Pfisterer
 * Copyright (c) 2004 by Florian Bomers
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

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import static org.jsresources.apps.chat.Constants.*;


public class ChatModel
{

    private MasterModel m_masterModel;
    private PropertyChangeSupport m_propertyChangeSupport;
    private Network			m_network;
    private ListenThread		m_listenThread;
    private DataInputStream		m_receiveStream;
    private OutputStream		m_sendStream;

    // audio related: owned by ChatModel
    private AudioBase[] audio = new AudioBase[2];
    private boolean m_audioActive;



    public ChatModel(MasterModel masterModel) {
	m_masterModel = masterModel;
	m_propertyChangeSupport = new PropertyChangeSupport(this);
	initNetwork();
	audio[DIR_MIC] = new AudioCapture(getConnectionSettings().getFormatCode(),
					  getAudioSettings().getSelMixer(DIR_MIC),
					  getAudioSettings().getBufferSizeMillis(DIR_MIC));
	audio[DIR_SPK] = new AudioPlayback(getConnectionSettings().getFormatCode(),
					   getAudioSettings().getSelMixer(DIR_SPK),
					   getAudioSettings().getBufferSizeMillis(DIR_SPK));
	m_audioActive = false;
    }


    private MasterModel getMasterModel()
    {
	return m_masterModel;
    }


    private ConnectionSettings getConnectionSettings()
    {
	return getMasterModel().getConnectionSettings();
    }

    private AudioSettings getAudioSettings() {
    	return getMasterModel().getAudioSettings();
    }


    private void initNetwork()
    {
	if (getConnectionSettings().getConnectionType() == CONNECTION_TYPE_TCP)
	    {
		m_network = new TCPNetwork(getMasterModel());
	    }
	else
	    {
		m_network = new UDPNetwork(getMasterModel());
	    }
    }


    private Network getNetwork()
    {
	return m_network;
    }


    public AudioBase getAudio(int d)
    {
	return audio[d];
    }

    /** Connect to a given host or IP.
	@param strHostname a hostname or an IP address in the form
	xx.xx.xx.xx
    */
    public void connect(String strHostname)
    {
	InetAddress addr = null;
	try
	    {
		addr = InetAddress.getByName(strHostname);
	    }
	catch (UnknownHostException e)
	    {
		Debug.out(e);
		JOptionPane.showMessageDialog(null, "Unknown host " + strHostname);
	    }
	if (addr != null)
	    {
		getNetwork().connect(addr);
	    }
	if (! getNetwork().isConnected())
	    {
		JOptionPane.showMessageDialog(null, new Object[]{"Could not establish connection to " + strHostname}, "Error", JOptionPane.ERROR_MESSAGE);
	    }
	else
	    {
		initConnection(true);
	    }
    }


    public void disconnect()
	{
	    Debug.out("closing audio...");
	    closeAudio();
	    Debug.out("...closed");
	    if (isConnected())
	    {
		Debug.out("diconnecting network...");
		getNetwork().disconnect();
		Debug.out("disconnected...");
		notifyConnection();
	    }
    }

    public void setListen(boolean bListen)
    {
	if (bListen != isListening())
	    {
		if (bListen)
		    {
			m_listenThread = new ListenThread();
			m_listenThread.start();
		    }
		else
		    {
			m_listenThread.setTerminate();
		    }
	    }
    }


    public boolean isListening()
    {
	return m_listenThread != null;
    }



    /** Set up after socket connection has been established.
	This negotiates the audio format and inits the streams.

	@param bActive true if called for the initiating (active)
	endpoint. false for the accepting (passive) endpoint.
    */
    private void initConnection(boolean bActive)
    {
	Debug.out("initConnection(" + bActive + "): begin");
	try
	    {
		m_receiveStream = new DataInputStream(getNetwork().createReceiveStream());
		m_sendStream = getNetwork().createSendStream();
		Debug.out("initConnection(): receiveStream: " + m_receiveStream);
		Debug.out("initConnection(): sendStream: " + m_sendStream);
	    }
	catch (IOException e)
	    {
		Debug.out(e);
		streamError("Problems while setting up the connection");
	    }

	/* To agree on the audio data format, the active side sends a
	   32 bit integer format code so that the passive side can
	   adapt to it.

	   This mechanism could be extended to a real negotiation
	   where the passive side sends a list of possible formats and
	   the active one chooses one of them. */
	boolean bHandshakeSuccessful = false;
	if (bActive)
	    {
		bHandshakeSuccessful = doHandshakeActive();
	    }
	else //passive
	    {
		bHandshakeSuccessful = doHandshakePassive();
	    }
	if (bHandshakeSuccessful)
	{
	    Debug.out("connection established");
	    if (isConnected())
	    {
		initNetworkAudio();
	    }
	    notifyConnection();
	}
	else
	{
	    m_receiveStream = null;
	    m_sendStream = null;
	}
	Debug.out("initConnection(" + bActive + "): end");
    }


    // returns true if successful
    private boolean doHandshakeActive()
    {
	DataOutputStream dos = new DataOutputStream(getSendStream());
	// phase I
	try
	    {
		dos.writeInt(PROTOCOL_MAGIC);
		Debug.out("written magic: " + PROTOCOL_MAGIC);
		dos.writeInt(PROTOCOL_VERSION);
		Debug.out("written protocol version: " + PROTOCOL_VERSION);
		dos.writeInt(getConnectionSettings().getFormatCode());
		Debug.out("written format code: " + getConnectionSettings().getFormatCode());
	    }
	catch (IOException e)
	    {
		Debug.out(e);
		streamError("I/O error during handshake (active, phase I)");
		return false;
	    }

	// phase II
	byte[] abBuffer = new byte[4];
	try
	    {
		getReceiveStream().readFully(abBuffer);
	    }
	catch (IOException e)
	    {
		Debug.out(e);
		streamError("I/O error during handshake (active, phase II)");
		return false;
	    }
	    //Debug.out("abBuffer[0]: " + abBuffer[0]);
	    //Debug.out("abBuffer[1]: " + abBuffer[1]);
	    //Debug.out("abBuffer[2]: " + abBuffer[2]);
	    //Debug.out("abBuffer[3]: " + abBuffer[3]);
	    int w = ((abBuffer[0] &0xFF) << 24) | ((abBuffer[1] & 0xFF) << 16) | ((abBuffer[2] & 0xFF) << 8) | (abBuffer[3] & 0xFF);
	    //Debug.out("received code: " + w);
	    if (w != PROTOCOL_ACK)
	    {
		streamError("error on remote peer");
		return false;
	    }
	return true;
    }


    // returns true if successful
    private boolean doHandshakePassive()
    {
	boolean bSuccess = true;

	// phase I
	byte[] abBuffer = new byte[12];
	try
	    {
		int nRead = getReceiveStream().read(abBuffer);
		if (nRead != 12)
		    {
			streamError("I/O Error during handshake (passive, phase I)");
			bSuccess = false;
		    }
	    }
	catch (IOException e)
	    {
		Debug.out(e);
		streamError("I/O error during handshake (passive, phase I)");
		bSuccess = false;
	    }
	if (bSuccess)
	    {
		int w = (abBuffer[0] << 24) | (abBuffer[1] << 16) | (abBuffer[2] << 8) | abBuffer[3];
		Debug.out("received magic: " + w);
		if (w != PROTOCOL_MAGIC)
		    {
			streamError("wrong magic");
			bSuccess = false;
		    }
		else
		    {
			w = (abBuffer[4] << 24) | (abBuffer[5] << 16) | (abBuffer[6] << 8) | abBuffer[7];
			Debug.out("received protocol version: " + w);
			if (w != PROTOCOL_VERSION)
			    {
				streamError("wrong protocol version");
				bSuccess = false;
			    }
			else
			    {
				w = (abBuffer[8] << 24) | (abBuffer[9] << 16) | (abBuffer[10] << 8) | abBuffer[11];
				Debug.out("received format code: " + w);
				if (w < 0 || w > FORMAT_CODES.length)
				    {
					streamError("wrong format code");
					bSuccess = false;
				    }
			    }
		    }
	    }

	// phase II
	DataOutputStream dos = new DataOutputStream(getSendStream());
	try
	    {
		if (bSuccess)
		    {
			dos.writeInt(PROTOCOL_ACK);
			Debug.out("written ACK");
		    }
		else
		    {
			dos.writeInt(PROTOCOL_ERROR);
			Debug.out("written ERROR");
		    }
	    }
	catch (IOException e)
	    {
		Debug.out(e);
		streamError("I/O error during handshake (passive, phase II)");
		bSuccess = false;
	    }
	return bSuccess;
    }


    public boolean isConnected() {
	return getNetwork().isConnected();
    }

    // audio related


    /* Set up audio connections. */
    private void initNetworkAudio() {
	//Debug.out("initNetworkAudio(): receiveStream: " + getReceiveStream());
	//Debug.out("initNetworkAudio(): sendStream: " + getSendStream());
	try {
	    Debug.out("audio: " + getAudio(DIR_MIC));
	    ((AudioCapture) getAudio(DIR_MIC)).setOutputStream(getSendStream());
	    ((AudioPlayback) getAudio(DIR_SPK)).setAudioInputStream(AudioUtils.createNetAudioInputStream(getConnectionSettings().getFormatCode(), getReceiveStream()));
	    startAudio(DIR_MIC);
	    startAudio(DIR_SPK);
	    setAudioActive(true);
	} catch (Exception e) {
	    Debug.out(e);
	    streamError(e.getMessage());
	}
    }

    public void initAudioStream() {
	// only necessary for test mode on microphone side
	if (isMicrophoneTest()) {
	    ((AudioPlayback) getAudio(DIR_SPK)).setAudioInputStream(((AudioCapture) getAudio(DIR_MIC)).getAudioInputStream());
	}
    }

    private void closeAudio() {
    	setAudioActive(false);
    	closeAudio(DIR_SPK);
    	closeAudio(DIR_MIC);
    }

    public boolean isMicrophoneTest() {
	return isAudioActive() && (((AudioCapture) getAudio(DIR_MIC)).getOutputStream() == null);
    }

    public boolean isAudioActive() {
	return m_audioActive;
    }

    public void setAudioActive(boolean active) {
	m_audioActive = active;
	notifyAudio();
    }

    private void closeAudio(int d) {
	if (getAudio(d) != null) {
	    getAudio(d).close();
	}
    }

    private void startAudio(int d) throws Exception {
	if (!isAudioActive()) {
	    getAudio(d).setFormatCode(getConnectionSettings().getFormatCode());
	}
	getAudio(d).open();
	getAudio(d).start();
    }


    // returns if testing audio
    public void toggleTestAudio() {
    	if (isConnected()) {
	    Debug.out("Call to ChatModel.toggleTestAudio, but connection active!");
	    return;
    	}
	try {
	    if (m_audioActive) {
		closeAudio(DIR_MIC);
		closeAudio(DIR_SPK);
		setAudioActive(false);
	    } else {
		startAudio(DIR_MIC);
		((AudioCapture) getAudio(DIR_MIC)).setOutputStream(null);
		startAudio(DIR_SPK);
		setAudioActive(true);
		initAudioStream();
	    }
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(null, new Object[]{"Error: ", ex.getMessage()}, "Error", JOptionPane.ERROR_MESSAGE);
	    closeAudio(0);
	    closeAudio(1);
	    setAudioActive(false);
	    notifyAudio();
	}
    }


    public  DataInputStream getReceiveStream()
    {
	return m_receiveStream;
    }


    public  OutputStream getSendStream()
    {
	return m_sendStream;
    }


    private void streamError(String strError)
    {
	JOptionPane.showMessageDialog(null, new Object[]{strError, "Connection will be terminated"}, "Error", JOptionPane.ERROR_MESSAGE);
	getNetwork().disconnect();
	closeAudio();
	notifyConnection();
    }


    /*
     * Properties Support
     */

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
	m_propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
	m_propertyChangeSupport.removePropertyChangeListener(listener);
    }


    private void notifyConnection()
	{
	    m_propertyChangeSupport.firePropertyChange(CONNECTION_PROPERTY, isConnected(), ! isConnected());
	}


    private void notifyAudio()
	{
	    m_propertyChangeSupport.firePropertyChange(AUDIO_PROPERTY, isAudioActive(), ! isAudioActive());
	}





    /*
     * Inner Classes
     */
    private class ListenThread
	extends Thread
    {
	private boolean		m_bTerminate;


	public void setTerminate()
	{
	    m_bTerminate = true;
	}


	public void run()
	{
	    /* For the termination mechanism to work, it is necessary
	     * that listen() has a finite timeout (it must not block
	     * forever) or that setListen(false) interrupts it. */
	    getNetwork().setListen(true);
	    while (!m_bTerminate)
		{
		    if (getNetwork().listen())
			{
			    String strMessage = "Call received from " + getNetwork().getPeer() + ". Do you want to accept?";
			    int nAnswer = JOptionPane.showConfirmDialog(null, new Object[]{strMessage}, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			    if (nAnswer == JOptionPane.YES_OPTION)
				{
				    initConnection(false);
				    setListen(false);
				}
			    else
				{
				    getNetwork().disconnect();
				}
			}
		}
	    getNetwork().setListen(false);
	}
    }


}


/*** ChatModel.java ***/
