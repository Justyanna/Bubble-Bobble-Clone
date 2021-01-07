package edu.uwb.ii.bubble_bobble.scenes.leaderboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LeaderBoardData {

    private StringProperty name;
    private IntegerProperty score;
    private StringProperty date;

    public LeaderBoardData(String detail, String detail1, String detail2) {
        name = new SimpleStringProperty();
        score = new SimpleIntegerProperty();
        date = new SimpleStringProperty();
        name.set(detail);
        score.set(Integer.parseInt(detail1));
        date.set(detail2);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(String score) {
        this.score.set(Integer.parseInt(score));
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public StringProperty dateProperty() {
        return date;
    }
}
