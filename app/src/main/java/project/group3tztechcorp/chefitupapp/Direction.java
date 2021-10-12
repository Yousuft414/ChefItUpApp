package project.group3tztechcorp.chefitupapp;

public class Direction {
    public String Name;
    public Boolean isSelected;

    public Direction() {
    }

    public Direction(String name, Boolean isSelected) {
        Name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
