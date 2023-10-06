public class Subtask extends Task {
    int idOfHostEpic;
    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public int getIdOfHostEpic() {
        return idOfHostEpic;
    }

    public void setIdOfHostEpic(int idOfHostEpic) {
        this.idOfHostEpic = idOfHostEpic;
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


