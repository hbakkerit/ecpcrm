/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package org.incode.eurocommercial.ecpcrm.dom.user;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
public class UserMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<User> listAll() {
        return userRepository.listAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<User> findByEmail(
            @ParameterLayout(named = "Email")
            final String email
    ) {
        return userRepository.findByEmailContains(email);
    }

    public static class CreateDomainEvent extends ActionDomainEvent<UserMenu> {}

    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "3")
    public User newUser(
            final @ParameterLayout(named = "Enabled") boolean enabled,
            final @ParameterLayout(named = "Gender") Gender gender,
            final @ParameterLayout(named = "Title") Title title,
            final @ParameterLayout(named = "First Name") String firstName,
            final @ParameterLayout(named = "Last Name") String lastName,
            final @ParameterLayout(named = "Email") String email,
            final @ParameterLayout(named = "Center") Center center,
            final @ParameterLayout(named = "Card") Card card,
            final @ParameterLayout(named = "Promotional Emails") boolean promotionalEmails
    ) {
        return userRepository.findOrCreate(
                enabled, gender, title, firstName, lastName, email, center, card, promotionalEmails);
    }

    @Inject
    UserRepository userRepository;

    @Inject
    CenterRepository centerRepository;
}
