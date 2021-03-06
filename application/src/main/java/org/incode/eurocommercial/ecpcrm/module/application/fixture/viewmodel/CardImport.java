package org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.wrapper.WrapperFactory;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.module.application.fixture.jdbc.Importable;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.CardMenu;

import lombok.Getter;
import lombok.Setter;

public class CardImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String number;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String status;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String userId;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String centerCode;

    @Getter @Setter
    @Property
    private String createdAt;

    @Getter @Setter
    @Property
    private String givenToUserAt;

    @Getter @Setter
    @Property
    private String sentToUserAt;

    @Override
    public List<Class> importAfter() {
        return Lists.newArrayList();
    }

    @Override
    public List<Object> handleRow(final FixtureScript.ExecutionContext executionContext, final ExcelFixture excelFixture, final Object previousRow) {
        return importData(previousRow);
    }

    @Override
    public List<Object> importData(Object previousRow) {
        CardStatus status = CardStatus.valueOf(getStatus());
        Center center = centerRepository.findByCode(getCenterCode());

        if (cardMenu.validateNewCard(getNumber(), center) != null) {
            return null;
        }

        Card card = wrapperFactory.wrap(cardMenu).newCard(
                getNumber(),
                center
        );

        card.setStatus(status);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdAtDate = getCreatedAt() == null ? card.getCreatedAt() : dtf.parseLocalDateTime(getCreatedAt().replace("T", " ").replace(".000", ""));
        LocalDateTime givenToUserAtDate = getGivenToUserAt() == null ? null : dtf.parseLocalDateTime(getGivenToUserAt().replace("T", " ").replace(".000", ""));
        LocalDateTime sentToUserAtDate = getSentToUserAt() == null ? null : dtf.parseLocalDateTime(getSentToUserAt().replace("T", " ").replace(".000", ""));

        card.setCreatedAt(createdAtDate);
        card.setGivenToUserAt(givenToUserAtDate);
        card.setSentToUserAt(sentToUserAtDate);

        return null;
    }

    @Inject private CardMenu cardMenu;
    @Inject private CardRepository cardRepository;
    @Inject private WrapperFactory wrapperFactory;
    @Inject private CenterRepository centerRepository;
}
