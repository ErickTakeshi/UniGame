/*
 *	SettingsPanel.java
 */

/*
 * Copyright (c) 1999 - 2004 by Matthias Pfisterer
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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import static org.jsresources.apps.chat.Constants.*;


public class SettingsPanel
extends JPanel
implements ActionListener
{
    private MasterModel m_masterModel;
	private JTextField		m_portTextField;
	private JComboBox		m_typeComboBox;
	private JComboBox		m_qualityComboBox;



	public SettingsPanel(MasterModel masterModel)
	{
	    m_masterModel = masterModel;
		setLayout(new BorderLayout());

		JPanel	centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		JPanel p = new JPanel();
		p.add(new JLabel("Port:"));
		m_portTextField = new JTextField(5);
		p.add(m_portTextField);
		centerPanel.add(p);

		p = new JPanel();
		p.add(new JLabel("Connection Type:"));
		m_typeComboBox = new JComboBox(CONNECTION_TYPE_NAMES);
		p.add(m_typeComboBox);
		centerPanel.add(p);

		p = new JPanel();
		p.add(new JLabel("Audio Quality:"));
		m_qualityComboBox = new JComboBox(Constants.FORMAT_NAMES);
		p.add(m_qualityComboBox);
		centerPanel.add(p);

		add(centerPanel, BorderLayout.CENTER);

		JPanel	buttonPanel = new JPanel();

		JButton applyButton = new JButton("Apply");
		applyButton.setActionCommand("apply");
		applyButton.addActionListener(this);
		buttonPanel.add(applyButton);
		JButton resetButton = new JButton("Reset");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		buttonPanel.add(resetButton);

		add(buttonPanel, BorderLayout.SOUTH);

		reset();
	}


    private MasterModel getMasterModel()
	{
	    return m_masterModel;
	}


    private ConnectionSettings getConnectionSettings()
	{
	    return getMasterModel().getConnectionSettings();
	}


	public void actionPerformed(ActionEvent ae)
	{
		String	strActionCommand = ae.getActionCommand();
		if (strActionCommand.equals("apply"))
		{
		    commit();
		}
		else if (strActionCommand.equals("reset"))
		{
		    reset();
		}
	}


    private void commit()
	{
	    int port = Integer.parseInt(m_portTextField.getText());
	    getConnectionSettings().setPort(port);
	    int nFormatCode = FORMAT_CODES[m_qualityComboBox.getSelectedIndex()];
	    getConnectionSettings().setFormatCode(nFormatCode);
	    int nConnectionType = CONNECTION_TYPES[m_typeComboBox.getSelectedIndex()];
	    getConnectionSettings().setConnectionType(nConnectionType);
	}


    private void reset()
	{
	    int port = getConnectionSettings().getPort();
	    m_portTextField.setText("" + port);
	    int nFormatCode = getConnectionSettings().getFormatCode();
	    int nIndex = -1;
	    for (int i = 0; i < FORMAT_CODES.length; i++)
	    {
		if (nFormatCode == FORMAT_CODES[i])
		{
		    nIndex = i;
		    break;
		}
	    }
	    m_qualityComboBox.setSelectedIndex(nIndex);

	    int nConnectionType = getConnectionSettings().getConnectionType();
	    nIndex = -1;
	    for (int i = 0; i < CONNECTION_TYPES.length; i++)
	    {
		if (nConnectionType == CONNECTION_TYPES[i])
		{
		    nIndex = i;
		    break;
		}
	    }
	    m_typeComboBox.setSelectedIndex(nIndex);
	}

} 



/*** SettingsPanel.java ***/
