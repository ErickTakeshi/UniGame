/*
 *	AudioPanel.java
 */

/*
 * Copyright (c) 2004 by Florian Bomers
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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.sound.sampled.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import static org.jsresources.apps.chat.Constants.*; // $$ static import

public class AudioPanel extends JPanel implements ActionListener, ItemListener,
						  ChangeListener, PropertyChangeListener {
    private JButton disconnectButton;
    private JButton testButton;

    private JComboBox[] mixerSelector = new JComboBox[2];
    private JProgressBar[] volumeMeter = new JProgressBar[2];
    private JSlider[] volumeSlider = new JSlider[2];
    private JComboBox[] volumePort = new JComboBox[2];
    private JCheckBox[] muteButton = new JCheckBox[2];

    private JComboBox[] bufferSelector = new JComboBox[2];

    private MasterModel master;


    public AudioPanel(MasterModel masterModel) {
    	this.master = masterModel;
	setLayout(new GridLayout(1,2));

	createGUI(0, "Microphone");
	createGUI(1, "Speaker");

	getChatModel().addPropertyChangeListener(this);
	enableButtons();
    }

    private void createGUI(int d, String title) {
	JPanel p = new JPanel();
	p.setLayout(new StripeLayout(0, 2, 0, 2, 3));
	p.add(new JLabel(title));

	p.add(new JLabel("Mixer:"));
	mixerSelector[d] = new JComboBox(getAudioSettings().getMixers(d).toArray());
	mixerSelector[d].addItemListener(this);
	p.add(mixerSelector[d]);

	p.add(new JLabel("Level:"));
	volumeMeter[d] = new JProgressBar(JProgressBar.HORIZONTAL, 0, 128);
	p.add(volumeMeter[d]);

	volumeSlider[d] = new JSlider(0, 100, 100);
	volumeSlider[d].addChangeListener(this);
	p.add(volumeSlider[d]);

	volumePort[d] = new JComboBox(getAudioSettings().getPorts(d).toArray());
	volumePort[d].addItemListener(this);
	p.add(volumePort[d]);

	muteButton[d] = new JCheckBox("Mute");
	muteButton[d].addItemListener(this);
	p.add(muteButton[d]);

	p.add(new JLabel("Buffer size in millis:"));
	bufferSelector[d] = new JComboBox(BUFFER_SIZE_MILLIS_STR);
	bufferSelector[d].setSelectedIndex(getAudioSettings().getBufferSizeIndex(d));
	bufferSelector[d].addItemListener(this);
	p.add(bufferSelector[d]);

	if (d == DIR_SPK) {
	    disconnectButton = new JButton("Disconnect");
	    disconnectButton.addActionListener(this);
	    p.add(disconnectButton);
	} else {
	    testButton = new JButton("Start Mic Test");
	    testButton.addActionListener(this);
	    p.add(testButton);
	}
	add(p);
	// init with first port
	getAudioSettings().setSelPort(d, 0);
	initNewPort(d);
    }

    private ChatModel getChatModel() {
    	return master.getChatModel();
    }

    private AudioSettings getAudioSettings() {
    	return master.getAudioSettings();
    }

    private ConnectionSettings getConnectionSettings() {
    	return master.getConnectionSettings();
    }

    private AudioBase getAudio(int d) {
	return getChatModel().getAudio(d);
    }

    private void initNewPort(int d) {
	Port port = getAudioSettings().getSelPort(d);
	FloatControl c = getAudioSettings().getSelVolControl(d);
	volumeSlider[d].setEnabled(port != null && c != null);
	updateVolumeSlider(d);
    }

    private void updateVolumeSlider(int d) {
	FloatControl c = getAudioSettings().getSelVolControl(d);
	if (c != null && volumeSlider[d].isEnabled()) {
	    int newPos = (int) (((c.getValue() - c.getMinimum())
				 / (c.getMaximum() - c.getMinimum())) * 100.0f);
	    if (newPos != volumeSlider[d].getValue()) {
		volumeSlider[d].setValue(newPos);
		//if (VERBOSE) out("Setting slider to: "+newPos);
	    }
	}
    }

    private void updateVolume(int d) {
	FloatControl c = getAudioSettings().getSelVolControl(d);
	if (c != null && volumeSlider[d].isEnabled()) {

	    float newVol = ((volumeSlider[d].getValue() / 100.0f)
			    * (c.getMaximum() - c.getMinimum())) + c.getMinimum();
	    c.setValue(newVol);
	    //if (VERBOSE) out("Setting vol: "+newVol);
	}
    }

    private void initNewMixer(int d) {
	try {
	    getAudio(d).setMixer(getAudioSettings().getSelMixer(d));
	    if (d == DIR_MIC) {
	    	getChatModel().initAudioStream();
	    }
	} catch (Exception e) {
	    if (DEBUG) e.printStackTrace();
	    out(e.getMessage());
	}
    }

    private void initNewBufferSize(int d) {
	try {
	    getAudio(d).setBufferSizeMillis(getAudioSettings().getBufferSizeMillis(d));
	    if (d == DIR_MIC) {
	    	getChatModel().initAudioStream();
	    }
	} catch (Exception e) {
	    if (DEBUG) e.printStackTrace();
	    out(e.getMessage());
	}
    }

    public void itemStateChanged(ItemEvent e) {
	int d = -1;
	if (e.getSource() == volumePort[0]) {
	    d = 0;
	} else if (e.getSource() == volumePort[1]) {
	    d = 1;
	}
	if ((d >= 0) && (e.getStateChange() == ItemEvent.SELECTED)) {
	    getAudioSettings().setSelPort(d, volumePort[d].getSelectedIndex());
	    initNewPort(d);
	    return;
	}
	d = -1;
	if (e.getSource() == mixerSelector[0]) {
	    d = 0;
	} else if (e.getSource() == mixerSelector[1]) {
	    d = 1;
	}
	if ((d >= 0) && (e.getStateChange() == ItemEvent.SELECTED)) {
	    getAudioSettings().setSelMixer(d, mixerSelector[d].getSelectedIndex());
	    initNewMixer(d);
	    return;
	}
	d = -1;
	if (e.getSource() == bufferSelector[0]) {
	    d = 0;
	} else if (e.getSource() == bufferSelector[1]) {
	    d = 1;
	}
	if ((d >= 0) && (e.getStateChange() == ItemEvent.SELECTED)) {
	    getAudioSettings().setBufferSizeIndex(d, bufferSelector[d].getSelectedIndex());
	    initNewBufferSize(d);
	    return;
	}

	d = -1;
	if (e.getSource() == muteButton[0]) {
	    d = 0;
	} else if (e.getSource() == muteButton[1]) {
	    d = 1;
	}
	if (d >= 0 && getAudio(d) != null) {
	    getAudio(d).setMuted(muteButton[d].isSelected());
	}
    }

    public void stateChanged(ChangeEvent e) {
	int d;
	if (e.getSource() == volumeSlider[0]) {
	    d = 0;
	} else if (e.getSource() == volumeSlider[1]) {
	    d = 1;
	} else {
	    return;
	}
	updateVolume(d);
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == testButton) {
	    getChatModel().toggleTestAudio();
	} else 	if (e.getSource() == disconnectButton) {
	    getChatModel().disconnect();
	}

    }

    private void enableButtons() {
	if (getChatModel().isConnected()) {
	    disconnectButton.setEnabled(true);
	    testButton.setEnabled(false);
	    testButton.setText("Start Mic Test");
	} else {
	    disconnectButton.setEnabled(false);
	    testButton.setEnabled(true);
	    if (getChatModel().isAudioActive()) {
		testButton.setText("Stop Mic Test");
	    } else {
		testButton.setText("Start Mic Test");
	    }
	}
    }

    public void propertyChange(PropertyChangeEvent e) {
	if (DEBUG) out("Property change. "
		       +"AudioActive:"+getChatModel().isAudioActive()
		       +"Connected:"+getChatModel().isConnected());
	if (e.getPropertyName().equals(AUDIO_PROPERTY)) {
	    if (getChatModel().isAudioActive()) {
		startLevelMeterThread();
	    }
	}
	enableButtons();
    }

    private void startLevelMeterThread() {
	new Thread(new Runnable() {
		public void run() {
		    if (DEBUG) out("Meter Thread: start");
		    try {
			while (getChatModel().isAudioActive()) {
			    for (int d = 0; d < 2; d++) {
			    	AudioBase ab = getAudio(d);
			    	if (ab != null) {
				    int level = ab.getLevel();
				    if (level >= 0) {
					volumeMeter[d].setValue(level);
				    } else {
					volumeMeter[d].setValue(0);
				    }

				}
			    }
			    Thread.sleep(30);
			}

		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    volumeMeter[0].setValue(0);
		    volumeMeter[1].setValue(0);
		    if (DEBUG) out("Meter Thread: stop");

		}
	    }).start();
    }


}

/*** AudioPanel.java ***/
