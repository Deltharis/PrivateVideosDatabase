package stja.entities.tags;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Delth on 07.11.2015.
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "Tags")
@Entity
public abstract class AbstractTag implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "text", length = 25)
    private String text = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString(){
        return getText();
    }
}
