package org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.center;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.CenterMenu;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class CenterCreate extends FixtureScript {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String code;

    @Getter @Setter
    private String id;

    @Getter @Setter
    private String mailchimpListId;

    @Getter @Setter
    private String contactEmail;

    @Getter
    private Center center;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        if (name == null)
            name = faker.gameOfThrones().city();
        if (id == null)
            id = "" + faker.number().randomNumber(2, true);
        if (code == null) {
            do {
                code = "0" + faker.number().randomNumber(2, true);
            } while (centerRepository.findByCode(code) != null);
        }
        if (mailchimpListId == null) {
            mailchimpListId = "ea56e8863e";
        }

        if (contactEmail == null) {
            contactEmail = "contact@lesportesdetaverny.com";
        }

        center = wrap(menu).newCenter(code(), name(), id(), mailchimpListId(), contactEmail());

        ec.addResult(this, center);
    }

    @Inject private CenterMenu menu;
    @Inject private CenterRepository centerRepository;
}
