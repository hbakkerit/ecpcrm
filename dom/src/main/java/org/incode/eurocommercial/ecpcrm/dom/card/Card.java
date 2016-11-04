/**
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.eurocommercial.ecpcrm.dom.card;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.util.ObjectContracts;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByExactNumber", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE number == :number "),
        @Query(
                name = "findByNumberContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE number.indexOf(:number) >= 0 ")
})
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
public class Card implements Comparable<Card> {

    @Override
    public int compareTo(final Card other) {
        return ObjectContracts.compare(this, other, "email");
    }

    public String title() {
        return getNumber();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String number;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private CardStatus status;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String clientId;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private Center center;

}
