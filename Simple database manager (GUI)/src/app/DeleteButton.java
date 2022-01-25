package app;

import javafx.scene.control.Button;

public class DeleteButton extends Button{   //extends Button -> id, set id, get id - id calculated in tablecreatorcontroller, same id to delete things in the list

    private int id = 0;

    public int getDeleteId() {
        return id;
    }

    public void setDeleteId(int id1) {
        id = id1;
    }

}
