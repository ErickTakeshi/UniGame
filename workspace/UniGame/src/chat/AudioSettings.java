/*
 *	AudioSettings.java
 */

/*
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

import java.util.*;
import javax.sound.sampled.*;
import static org.jsresources.apps.chat.Constants.*; // $$ static import

class AudioSettings {

    /** the selected port */
    private  Port[] port = new Port[2];
    /** the selected control for port volume */
    private FloatControl[] portVolume = new FloatControl[2];
    /** the selected control for port select (only for Source Ports) */
    private BooleanControl[] portSelect = new BooleanControl[2];

    /** all compound controls available on the system, as a display list */
    private List<String> portNames[] = new List[2]; // $$ generics

    /** all Ports available on the system (maybe the same ones) */
    private List<Port>[] ports = new List[2];

    /** the index of the controls array for the respective compound control */
    private List<Integer>[] controlIndex = new List[2];

    /** the selected mixer. If null, use default */
    private Mixer[] mixer = new Mixer[2];

    /** all mixers */
    private List<Mixer>[] mixers = new List[2];

    /** index in BUFFER_SIZE_MILLIS */
    private int[] bufferSizeIndex = new int[2];

    private boolean inited = false;

    private MasterModel master;

    public AudioSettings (MasterModel master) {
	portNames[0] = new ArrayList<String>();
	portNames[1] = new ArrayList<String>();
	ports[0] = new ArrayList<Port>();
	ports[1] = new ArrayList<Port>();
	controlIndex[0] = new ArrayList<Integer>();
	controlIndex[1] = new ArrayList<Integer>();
	mixers[0] = new ArrayList<Mixer>();
	mixers[1] = new ArrayList<Mixer>();
	bufferSizeIndex[0] = BUFFER_SIZE_INDEX_DEFAULT;
	bufferSizeIndex[1] = BUFFER_SIZE_INDEX_DEFAULT;
	this.master = master;
    }

    public void init() {
	for (int d = 0; d < 2; d++) {
	    portNames[d].clear();
	    ports[d].clear();
	    controlIndex[d].clear();
	    mixers[d].clear();
	}
	// go through all mixer and fill all lists
	Mixer.Info[] infos = AudioSystem.getMixerInfo();
	for (int i = 0; i < infos.length; i++) {
	    try {
		Mixer mixer = AudioSystem.getMixer(infos[i]);
		addMixer(mixer, mixer.getSourceLineInfo(), DIR_SPK);
		addMixer(mixer, mixer.getTargetLineInfo(), DIR_MIC);
	    } catch (Exception e) {
		if (DEBUG) e.printStackTrace();
	    }
	}
	// add defaults, if multiples exist
	for (int d = 0; d < 2; d++) {
	    if (mixers[d].size() > 1) {
		mixers[d].add(0, null);
	    }
	}
	inited = true;
	if (VERBOSE) {
	    out("Microphone Ports:  "+ports[DIR_MIC].size());
	    out("Microphone Mixers: "+mixers[DIR_MIC].size());
	    out("Speaker Ports:  "+ports[DIR_SPK].size());
	    out("Speaker Mixers: "+mixers[DIR_SPK].size());
	}
    }

    public void exit() {
	for (int d = 0; d < 2; d++) {
	    closePort(d);
	    portNames[d].clear();
	    controlIndex[d].clear();
	    ports[d].clear();
	    mixers[d].clear();
	}
    }

    private void addMixer(Mixer mixer, Line.Info[] infos, int dirDataLine) {

	for (int i = 0; i<infos.length; i++) {
	    try {
		if (infos[i] instanceof Port.Info) {
		    Port.Info info = (Port.Info) infos[i];
		    int d;
		    if (info.isSource()) {
		    	// microphone port
		    	d = DIR_MIC;
		    } else {
		    	d = DIR_SPK;
		    }
		    // walk through all top-level controls
		    Port p = (Port) mixer.getLine(info);
		    p.open();
		    try {
		    	Control[] cs = p.getControls();
		    	for (int c = 0 ; c < cs.length; c++) {
			    if (cs[c] instanceof CompoundControl) {
				ports[d].add(p);
				portNames[d].add(mixer.getMixerInfo().getName()+": "+cs[c].getType().toString());
				// $$ autoboxing
				controlIndex[d].add(c);
			    }
		    	}
		    } finally {
		    	p.close();
		    }
		}
		if (infos[i] instanceof DataLine.Info) {
		    if (!mixers[dirDataLine].contains(mixer)) {
		    	mixers[dirDataLine].add(mixer);
		    }
		}
	    } catch (Exception e) {
		if (DEBUG) e.printStackTrace();
	    }
	}
    }

    // MIXER HANDLING //

    public List<String> getMixers(int d) {
	if (!inited) init();
	List<String> res = new ArrayList<String>();
	for (Mixer m : mixers[d]) {   // $$ enhanced for
	    if (m == null) {
		res.add("(Default)");
	    } else {
		res.add(m.getMixerInfo().getName());
	    }
	}
	return res;
    }

    public Mixer getSelMixer(int d) {
	return mixer[d];
    }

    /** set index in list returned in getMixers */
    public void setSelMixer(int d, int index) {
	if (index < 0 || index >= mixers[d].size()) {
	    if (DEBUG) out("setSelMixer out of range: iondex="+index);
	    return;
	} else {
	    mixer[d] = mixers[d].get(index);
	}
    }



    // PORT HANDLING //

    public List<String> getPorts(int d) {
	if (!inited) init();
	return portNames[d];
    }


    public Port getSelPort(int d) {
	return port[d];
    }

    public FloatControl getSelVolControl(int d) {
	return portVolume[d];
    }

    /** set index in list returned in getMixers */
    public void setSelPort(int d, int index) {
    	setSelPort(d, index, true);
    }

    public void setSelPort(int d, int index, boolean doSelect) {
	if (index < 0 || index >= ports[d].size()) {
	    if (DEBUG) out("setSelPort out of range: iondex="+index);
	    closePort(d);
	    return;
	} else {
	    setupPort(d, ports[d].get(index), controlIndex[d].get(index)); // $$ autoboxing
	    if (doSelect && d == DIR_MIC && portSelect[d] != null) {
		portSelect[d].setValue(true);
	    }
	    if (DEBUG) out("Selected "+portNames[d].get(index));
	}
    }

    private void closePort(int d) {
	if (port[d] != null) {
	    port[d].close();
	    port[d] = null;
	}
	portVolume[d] = null;
	portSelect[d] = null;
    }

    private void setupPort(int d, Port p, int controlIndex) {
	if (p != port[d] && port[d] != null) {
	    port[d].close();
	}
	portVolume[d] = null;
	portSelect[d] = null;
	port[d] = p;
	try {
	    p.open();
	    Control[] cs = p.getControls();
	    if (controlIndex >= cs.length) {
		throw new Exception("control not found!");
	    }
	    if (!(cs[controlIndex] instanceof CompoundControl)) {
		throw new Exception("control not found!");
	    }
	    CompoundControl cc = (CompoundControl) cs[controlIndex];
	    cs = cc.getMemberControls();
	    // find VolumeControl and select
	    for (Control c : cs) {  // $$ enhanced for
		if ((c instanceof FloatControl)
		    && c.getType().equals(FloatControl.Type.VOLUME)
		    && (portVolume[d] == null)) {
		    portVolume[d] = (FloatControl) c;
		}
		if ((c instanceof BooleanControl)
		    && c.getType().toString().contains("elect")
		    && (portSelect[d] == null)) {
		    portSelect[d] = (BooleanControl) c;
		}
	    }

	} catch (Exception e) {
	    if (DEBUG) e.printStackTrace();
	    closePort(d);
	}
    }

    // buffer size //

    public int getBufferSizeMillis(int d) {
    	return BUFFER_SIZE_MILLIS[bufferSizeIndex[d]];
    }

    public int getBufferSizeIndex(int d) {
    	return bufferSizeIndex[d];
    }

    public void setBufferSizeIndex(int d, int bufferSizeIndex) {
    	this.bufferSizeIndex[d] = bufferSizeIndex;
    }
}

