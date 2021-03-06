package org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.wrapper.WrapperFactory;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.module.application.fixture.jdbc.Importable;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.CenterMenu;

import lombok.Getter;
import lombok.Setter;

public class CenterImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String code;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String id;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String name;

    @Getter @Setter
    @Property
    private String mailchimpListId;

    @Getter @Setter
    @Property
    private String contactEmail;

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
        wrapperFactory.wrap(centerMenu).newCenter(getCode(), getName(), getId(), getMailchimpListId(), getContactEmail());
        return null;
    }

    @Inject private CenterMenu centerMenu;
    @Inject private WrapperFactory wrapperFactory;
}
