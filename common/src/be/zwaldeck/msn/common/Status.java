package be.zwaldeck.msn.common;

/**
 * @Author Wout Schoovaerts
 */
public enum Status {
    ONLINE,
    BUSY,
    AWAY,
    APPEAR_OFFLINE,
    OFFLINE;

    public String toString() {
        switch (this) {
            case OFFLINE:
                return "Offline";
            case ONLINE:
                return "Online";
            case BUSY:
                return "Busy";
            case AWAY:
                return "Away";
            case APPEAR_OFFLINE:
                return "Appear Offline";
        }

        return "";
    }
}
