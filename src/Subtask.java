public class Subtask extends Task {

    int idOfHostEpic;

    public Subtask(String name, String description, StatusOfTask status, int idOfHostEpic) {
        super(name, description, status);
        this.idOfHostEpic = idOfHostEpic;
    }

    public int getIdOfHostEpic() {
        return idOfHostEpic;
    }

    public void setIdOfHostEpic(int idOfHostEpic) {
        this.idOfHostEpic = idOfHostEpic;
    }

    @Override
    public KindOfTask getTypeofTask(){
        return KindOfTask.SUBTASK;
    }

    @Override
    public String toString(){
        return "Subtask{" +
                "name = " + "'" + super.getName() + "'"
                + ", description = " + "'" + super.getDescription()+ "'"
                + ", id = " + "'" + super.getId() + "'"
                + ", status = " + "'" + super.getStatus() + "'" + ", idOfHostEpic = " + "'" + idOfHostEpic + "'" + "}" ;
    }
}


