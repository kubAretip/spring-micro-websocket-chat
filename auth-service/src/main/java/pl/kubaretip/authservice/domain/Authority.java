package pl.kubaretip.authservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "authority")
@Getter
@Setter
@NoArgsConstructor
public class Authority implements Serializable {

    public static final long serialVersionUID = -8053205789790776096L;

    @Id
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    // To uppercase the authority name before saving it to db.
    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                '}';
    }
}
