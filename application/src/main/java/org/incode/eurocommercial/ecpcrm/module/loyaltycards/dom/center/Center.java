package org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center;

import java.util.Comparator;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.numerator.Numerator;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
        @Query(
                name = "findByExactName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center "
                        + "WHERE name == :name "),
        @Query(
                name = "findByNameContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @Query(
                name = "findByCode", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center "
                        + "WHERE code == :code ")
})
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
public class Center implements Comparable<Center>, HasAtPath {

    public String title() {
        return getName();
    }

    @Getter @Setter
    @Column(allowsNull = "false")
    private String code;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.EVERYWHERE)
    /* Imported from old database */
    private String id;

    @Getter @Setter
    @Column(allowsNull = "false")
    @Property(editing = Editing.ENABLED)
    private String name;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.EVERYWHERE)
    private Numerator numerator;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String atPath;

    @Getter @Setter
    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private String mailchimpListId;

    @Getter @Setter
    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private String contactEmail;

    @Programmatic
    public String nextValidCardNumber() {
        return cardRepository.nextCardNumber(numerator);
    }

    @Override
    public int compareTo(final Center other) {
        return Comparator
                .comparing(Center::getCode)
                .thenComparing(Center::getName)
                .compare(this, other);
    }

    @Inject private CardRepository cardRepository;

}
