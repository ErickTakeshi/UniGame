/*
 *	ConnectionPanel.java
 */

/*
 * Copyright (c) 1999 - 2002 by Matthias Pfisterer
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import static org.jsresources.apps.chat.Constants.*;

public class ConnectionPanel
extends JPanel
    implements ActionListener, ItemListener, PropertyChangeListener
{
    private MasterModel m_masterModel;
	private JTextField		m_nameTextField;
	private JButton			m_connectButton;




	public ConnectionPanel(MasterModel masterModel)
	{
	    m_masterModel = masterModel;

		setLayout(new GridLayout(0, 1));

		JPanel		panel = null;

		panel = new JPanel();
		panel.add(new JLabel("Name or IP:"));
		m_nameTextField = new JTextField(20);
		panel.add(m_nameTextField);
		add(panel);

		panel = new JPanel();
		m_connectButton = new JButton("Connect...");
		m_connectButton.addActionListener(this);
		m_connectButton.setActionCommand("connect");
		panel.add(m_connectButton);
		add(panel);


		panel = new JPanel();
		JCheckBox listenCheckBox = new JCheckBox("Listen to incoming connections", true);
		listenCheckBox.addItemListener(this);
		panel.add(listenCheckBox);
		add(panel);
		getChatModel().addPropertyChangeListener(this);
		getChatModel().setListen(true);
	}


    private MasterModel getMasterModel()
	{
	    return m_masterModel;
	}


    private ChatModel getChatModel()
	{
	    return getMasterModel().getChatModel();
	}

	public void itemStateChanged(ItemEvent e)
	{
		switch (e.getStateChange())
		{
		case ItemEvent.DESELECTED:
			getChatModel().setListen(false);
			break;

		case ItemEvent.SELECTED:
			getChatModel().setListen(true);
			break;
		}
	}


	public void actionPerformed(ActionEvent ae)
	{
		String	strActionCommand = ae.getActionCommand();
		if (strActionCommand.equals("connect"))
		{
			String strHostname = m_nameTextField.getText();
			getChatModel().connect(strHostname);
		}
	}


	/** Reflect the active state of the connection in the GUI.
	    This method should be called each time the state of the
	    connection changes (i.e. disconnected -> connected,
	    connected -> disconnected).
	 */
	private void setConnectionActive(boolean bActive)
	{
		m_connectButton.setEnabled(! bActive);
	}


    public void propertyChange(PropertyChangeEvent e)
	{
	    if (e.getPropertyName().equals(CONNECTION_PROPERTY))
	    {
		boolean bConnected = ((Boolean) e.getNewValue()).booleanValue();
		setConnectionActive(bConnected);
	    }
	}
}



/*** ConnectionPanel.java ***/
