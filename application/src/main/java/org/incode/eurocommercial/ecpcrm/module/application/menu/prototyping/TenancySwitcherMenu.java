/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.incode.eurocommercial.ecpcrm.module.application.menu.prototyping;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.isisaddons.module.security.app.user.MeService;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyRepository;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import org.incode.eurocommercial.ecpcrm.module.application.service.homepage.HomePageService;
import org.incode.eurocommercial.ecpcrm.module.application.service.homepage.HomePageViewModel;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Prototyping",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY,
        menuOrder = "500.40"
)
public class TenancySwitcherMenu extends AbstractService {

    //region > switchTenancy (action)
    @Action(
            semantics = SemanticsOf.IDEMPOTENT,
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(cssClassFa = "fa-exchange")
    public HomePageViewModel switchTenancy(final ApplicationTenancy applicationTenancy) {
        final ApplicationUser applicationUser = meService.me();
        applicationUser.updateAtPath(applicationTenancy.getPath());
        return homePageService.homePage();
    }

    public List<ApplicationTenancy> choices0SwitchTenancy() {
        return applicationTenancyRepository.allTenancies();
    }

    public ApplicationTenancy default0SwitchTenancy() {
        final ApplicationUser applicationUser = meService.me();
        final String atPath = applicationUser.getAtPath();
        return applicationTenancyRepository.findByPath(atPath);
    }
    //endregion

    //region > injected services
    @Inject private MeService meService;
    @Inject private ApplicationTenancyRepository applicationTenancyRepository;
    @Inject private HomePageService homePageService;
    //endregion

}

