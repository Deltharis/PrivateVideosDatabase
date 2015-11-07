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
    protected int id;

    @Column(name = "text", length = 25)
    protected String text = "";

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTag that = (AbstractTag) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
