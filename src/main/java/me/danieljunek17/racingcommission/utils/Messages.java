package me.danieljunek17.racingcommission.utils;

import me.danieljunek17.racingcommission.Racingcommission;

public enum Messages {

    RAINSUCCES(""),
    WRONGRAINUSAGE("wrongregenusage"),
    NOCAR("nocar"),
    NOCONTROLLERBLOCK("nocontrollerblock"),
    NOTEAM("noteam"),
    NOTONTHEROAD("notontheroad"),
    SAMESTATE("samestate"),
    BATTERYALREADYFULL("battery.alreadyfull"),
    BATTERYEMPTYNOBOOST("battery.emptynoboost"),
    BATTERYALMOSTEMPTYNOBOOST("battery.almostemptynoboost");

    private String path;
    private String message;
    private YAMLFile messagefile = Racingcommission.getMessages();

    Messages(String path) {
        this.path = path;
        this.message = messagefile.getString(path);
    }

    public String getMessage() {
        return Utils.color(this.message);
    }
}
