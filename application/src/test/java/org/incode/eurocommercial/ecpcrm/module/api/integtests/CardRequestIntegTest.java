/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.module.api.integtests;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestType;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRequestIntegTest extends ApiModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;

    @Inject CardRepository cardRepository;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject UserRepository userRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    @Inject ApiService apiService;

    private ApiIntegTestFixture fs;
    private Card card;
    private Center center;
    private User user;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;
    private List<User> userList;
    private List<Card> cardList;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));

        cardList = cardRepository.findByCenter(center);
        userList = userRepository.findByCenter(center);
        deviceList = authenticationDeviceRepository.findByCenter(center);

        device = deviceList.get(new Random().nextInt(deviceList.size()));
        card = cardList.get(new Random().nextInt(cardList.size()));
        user = userList.get(new Random().nextInt(userList.size()));

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
        assertThat(device).isNotNull();
    }

    @Test
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = user.getFirstName() + " " + user.getLastName();
        Boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(301);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = user.getFirstName() + " " + user.getLastName();
        Boolean lost = false;

        /* Testing every required argument individually */
        Object[] args = {
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate,
                children, nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        };

        int[] mandatory = {2, 4, 5, 6, 7, 12, 13, 14, 16};
        for(int i : mandatory) {
            Object[] a = args.clone();
            a[i] = null;

            // when
            Method m = ApiService.class.getMethod(
                    "cardRequest",
                    String.class, String.class, String.class, String.class, Title.class, String.class, String.class, String.class,
                    LocalDate.class, String.class, String.class, Boolean.class, String.class, String.class, String.class, String.class,
                    Boolean.class, String.class, Boolean.class);
            Result result = (Result) m.invoke(apiService, a);

            // then
            assertThat(result.getStatus()).isEqualTo(302);
        }
    }

    @Test
    @Ignore
    // TODO: This can not happen in the new system
    public void when_email_exists_and_user_is_invalid_we_expect_304_error() throws Exception {
    }

    @Test
    /* Valid check means checkItem is equal to firstName + " " + lastName of user */
    public void when_email_exists_and_card_is_not_lost_and_check_is_valid_we_expect_318_error() throws Exception {
        // given
        Card cardWithOwner = cardList.stream()
                .filter(c -> c.getOwner() != null)
                .findAny().get();
        cardWithOwner.setStatus(CardStatus.ENABLED);
        User userWithCard = cardWithOwner.getOwner();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = userWithCard.getFirstName();
        String lastName = userWithCard.getLastName();
        String email = userWithCard.getEmail();
        LocalDate birthdate = userWithCard.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = userWithCard.getHasCar();
        String address = userWithCard.getAddress();
        String zipcode = userWithCard.getZipcode();
        String city = userWithCard.getCity();
        String phoneNumber = userWithCard.getPhoneNumber();
        Boolean promotionalEmails = userWithCard.isPromotionalEmails();
        String checkItem = userWithCard.getFirstName() + " " + userWithCard.getLastName();
        Boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(318);
    }

    @Test
    /* Invalid check means checkItem is not equal to firstName + " " + lastName of user */
    public void when_email_exists_and_card_is_not_lost_and_check_is_invalid_we_expect_306_error() throws Exception {
        // given
        Card cardWithOwner = cardList.stream()
                .filter(c -> c.getOwner() != null)
                .findAny().get();
        cardWithOwner.setStatus(CardStatus.ENABLED);
        User userWithCard = cardWithOwner.getOwner();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = userWithCard.getFirstName();
        String lastName = userWithCard.getLastName();
        String email = userWithCard.getEmail();
        LocalDate birthdate = userWithCard.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = userWithCard.getHasCar();
        String address = userWithCard.getAddress();
        String zipcode = userWithCard.getZipcode();
        String city = userWithCard.getCity();
        String phoneNumber = userWithCard.getPhoneNumber();
        Boolean promotionalEmails = userWithCard.isPromotionalEmails();
        String checkItem = "INVALID CHECK ITEM";
        Boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(306);
    }

    @Test
    /* When checkItem is not present, it checks the first and last name */
    public void when_email_exists_and_card_is_not_lost_and_check_is_not_set_and_user_does_not_match_we_expect_305_error() throws Exception {
        // given
        Card cardWithOwner = cardList.stream()
                .filter(c -> c.getOwner() != null)
                .findAny().get();
        cardWithOwner.setStatus(CardStatus.ENABLED);
        User userWithCard = cardWithOwner.getOwner();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = "INCORRECT FIRST NAME";
        String lastName = "INCORRECT LAST NAME";
        String email = userWithCard.getEmail();
        LocalDate birthdate = userWithCard.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = userWithCard.getHasCar();
        String address = userWithCard.getAddress();
        String zipcode = userWithCard.getZipcode();
        String city = userWithCard.getCity();
        String phoneNumber = userWithCard.getPhoneNumber();
        Boolean promotionalEmails = userWithCard.isPromotionalEmails();
        String checkItem = null;
        Boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(305);
    }

    @Test
    /* When checkItem is not present, it checks the first and last name */
    public void when_email_exists_and_card_is_not_lost_and_check_is_not_set_and_user_matches_we_expect_318_error() throws Exception {
        // given
        Card cardWithOwner = cardList.stream()
                .filter(c -> c.getOwner() != null)
                .findAny().get();
        cardWithOwner.setStatus(CardStatus.ENABLED);
        User userWithCard = cardWithOwner.getOwner();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = userWithCard.getFirstName();
        String lastName = userWithCard.getLastName();
        String email = userWithCard.getEmail();
        LocalDate birthdate = userWithCard.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = userWithCard.getHasCar();
        String address = userWithCard.getAddress();
        String zipcode = userWithCard.getZipcode();
        String city = userWithCard.getCity();
        String phoneNumber = userWithCard.getPhoneNumber();
        Boolean promotionalEmails = userWithCard.isPromotionalEmails();
        String checkItem = null;
        Boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(318);
    }

    @Test
    public void when_email_exists_and_lost_is_true_we_expect_card_to_be_tagged_as_lost() throws Exception {
        // given
        Card cardWithOwner = cardList.stream()
                .filter(c -> c.getOwner() != null)
                .findAny().get();
        cardWithOwner.setStatus(CardStatus.ENABLED);
        User userWithCard = cardWithOwner.getOwner();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = userWithCard.getFirstName();
        String lastName = userWithCard.getLastName();
        String email = userWithCard.getEmail();
        LocalDate birthdate = userWithCard.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = userWithCard.getHasCar();
        String address = userWithCard.getAddress();
        String zipcode = userWithCard.getZipcode();
        String city = userWithCard.getCity();
        String phoneNumber = userWithCard.getPhoneNumber();
        Boolean promotionalEmails = userWithCard.isPromotionalEmails();
        String checkItem = null;
        Boolean lost = true;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(cardWithOwner.getStatus()).isEqualTo(CardStatus.LOST);
    }

    @Test
    /* Can't request duplicate cards */
    public void when_email_exists_but_user_already_has_a_handled_request_we_expect_307_error() throws Exception {
        // given
        Card cardWithoutOwner = cardList.stream()
                .filter(c -> c.getOwner() == null)
                .findAny().get();

        cardRequestRepository.findOrCreate(user, CardRequestType.PICK_UP_IN_CENTER);
        CardRequest cardRequest = cardRequestRepository.openRequestForUser(user);
        cardRequest.approve(cardWithoutOwner.getNumber());

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = null;
        Boolean lost = true;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(307);
    }

    @Test
    @Ignore
    // TODO: This can not happen in the new system
    public void when_email_does_not_exist_but_user_cant_be_created_we_expect_316_error() throws Exception {
    }

    @Test
    /* If we want to use an existing user without card, we need to set lost = true */
    public void when_email_exists_and_lost_is_true_and_user_has_no_requests_we_expect_happy_response() throws Exception {
        // given
        User userWithoutCard = userList.stream()
                .filter(u -> u.getCards().isEmpty() && u.getOpenCardRequest() == null)
                .findAny().get();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = userWithoutCard.getTitle();
        String firstName = userWithoutCard.getFirstName();
        String lastName = userWithoutCard.getLastName();
        String email = userWithoutCard.getEmail();
        LocalDate birthdate = userWithoutCard.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = userWithoutCard.getHasCar();
        String address = userWithoutCard.getAddress();
        String zipcode = userWithoutCard.getZipcode();
        String city = userWithoutCard.getCity();
        String phoneNumber = userWithoutCard.getPhoneNumber();
        boolean promotionalEmails = userWithoutCard.isPromotionalEmails();
        String checkItem = "";
        /* If we want to use an existing user without card, we need to set lost = true */
        boolean lost = true;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(cardRequestRepository.openRequestForUser(userWithoutCard)).isNotNull();
    }

    @Test
    public void when_email_does_not_exist_and_user_can_be_created_we_expect_happy_response() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "borne";
        String hostess = "";
        Title title = Title.MR;
        String firstName = "Testy";
        String lastName = "McTestFace";
        String email = "testymctestface1991@emailio.com";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "";
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost
        );

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        User newUser = userRepository.findByExactEmailAndCenter(email, center);
        assertThat(newUser).isNotNull();
        assertThat(newUser.getOpenCardRequest()).isNotNull();
    }
}