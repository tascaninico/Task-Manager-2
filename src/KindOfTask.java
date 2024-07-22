public enum KindOfTask {

    TASK("TASk"),

    SUBTASK("SUBTASK"),

    EPIC("EPIC");

    private final String kindInString;

    KindOfTask(String value){
        this.kindInString = value;
    }

    public String getString(){
        return this.kindInString;
    }

}
