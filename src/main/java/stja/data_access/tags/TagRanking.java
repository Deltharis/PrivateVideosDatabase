package stja.data_access.tags;

/**
 * Created by Delth on 25.10.2015.
 */
public class TagRanking {

    private String text;
    private int countz;

    public TagRanking(String text, int countz) {
        this.text = text;
        this.countz = countz;
    }

    public String getText() {
        return text;
    }

    public void setName(String namez) {
        this.text = namez;
    }

    public int getCountz() {
        return countz;
    }

    public void setCountz(int countz) {
        this.countz = countz;
    }
}
