package helper;

public enum Mode {

    SERVER("server"),
    CLIENT("client"),
    EXIT("exit");

    private String action;

    Mode(String value){
        this.action = value;
    }

    public static Mode getInstance(String action) throws NullPointerException {

        Mode result = null;

        for(Mode elem : values()) {
            if(elem.getValue().equals(action)) {
                result = elem;
            }
        }
        return result;
    }

    public String getValue() {
        return action;
    }
}
