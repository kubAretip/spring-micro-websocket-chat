package pl.kubaretip.authservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@ToString
@Entity
@Table(schema = "auth_service_database")
@Getter
@Setter
@NoArgsConstructor
@Immutable
public class Authority implements Serializable {

    public static final long serialVersionUID = -8053205789790776096L;

    @Id
    @Column(length = 50, nullable = false, unique = true)
    private String name;

}
