package org.apache.bookkeeper.bookie.entities;

import org.apache.bookkeeper.conf.ServerConfiguration;

public class Format {

    private ServerConfiguration conf;
    private Boolean isInteractive;
    private Boolean force;
    private Boolean expected;

    public Format(ServerConfiguration conf, Boolean isInteractive, Boolean force, Boolean expected){

        this.conf = conf;
        this.isInteractive = isInteractive;
        this.force = force;
        this.expected = expected;
    }

    public ServerConfiguration getConf() {
        return conf;
    }

    public void setConf(ServerConfiguration conf) {
        this.conf = conf;
    }

    public Boolean getInteractive() {
        return isInteractive;
    }

    public void setInteractive(Boolean interactive) {
        isInteractive = interactive;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    public Boolean getExpected() {
        return expected;
    }

    public void setExpected(Boolean expected) {
        this.expected = expected;
    }
}
