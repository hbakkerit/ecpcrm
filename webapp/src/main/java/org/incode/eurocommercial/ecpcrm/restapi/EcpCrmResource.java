package org.incode.eurocommercial.ecpcrm.restapi;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.assertj.core.util.Strings;
import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulMediaType;
import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
import org.apache.isis.viewer.restfulobjects.rendering.service.conneg.PrettyPrinting;
import org.apache.isis.viewer.restfulobjects.server.resources.ResourceAbstract;

import org.incode.eurocommercial.ecpcrm.app.services.api.ApiService;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

@Path("/crm/api/6.0")
public class EcpCrmResource extends ResourceAbstract  {

    @Override
    protected void init(
            final RepresentationType representationType,
            final Where where,
            final RepresentationService.Intent intent) {
        super.init(representationType, where, intent);
        this.getServicesInjector().injectServicesInto(this);
    }

    @POST
    @Path("/card-check")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardCheck(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        /* Marshalling request JSON string to parameters */
        String cardNumber = null;
        String origin = null;
        if(!Strings.isNullOrEmpty(request)) {
            JsonParser jsonParser = new JsonParser();

            JsonElement cardNumberJson = jsonParser.parse(request).getAsJsonObject().get("card");
            cardNumber = cardNumberJson == null ? null : cardNumberJson.getAsString();

            JsonElement originJson = jsonParser.parse(request).getAsJsonObject().get("origin");
            origin = originJson == null ? null : originJson.getAsString();
        }

        return apiService.cardCheck(deviceName, deviceSecret, cardNumber, origin).asResponse();
    }

    @POST
    @Path("/card-game")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardGame(@FormParam("request") String request) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        /* Marshalling request JSON string to parameters */
        String cardNumber, win, desc;
        cardNumber = win = desc = null;
        if(!Strings.isNullOrEmpty(request)) {
            JsonParser jsonParser = new JsonParser();

            JsonElement cardNumberJson = jsonParser.parse(request).getAsJsonObject().get("card");
            cardNumber = cardNumberJson == null ? null : cardNumberJson.getAsString();

            JsonElement winJson = jsonParser.parse(request).getAsJsonObject().get("win");
            win = winJson == null ? null : winJson.getAsString();

            JsonElement descJson = jsonParser.parse(request).getAsJsonObject().get("desc");
            desc = descJson == null ? null : descJson.getAsString();
        }

        return apiService.cardGame(cardNumber, win, desc).asResponse();
    }

    @POST
    @Path("/card-request")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardRequest(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        /* Marshalling request JSON string to parameters */
        String origin = null;
        String hostess = null;
        Title title = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        LocalDate birthdate = null;
        String children = null;
        String nbChildren = null;
        Boolean hasCar = null;
        String address = null;
        String zipcode = null;
        String city = null;
        String phoneNumber = null;
        boolean promotionalemails = false;
        String checkItem = null;
        boolean lost = false;

        if(!Strings.isNullOrEmpty(request)) {
            JsonParser jsonParser = new JsonParser();

            JsonElement originJson = jsonParser.parse(request).getAsJsonObject().get("origin");
            origin = originJson == null ? null : originJson.getAsString();

            JsonElement titleJson = jsonParser.parse(request).getAsJsonObject().get("title");
            String titleString = titleJson.getAsString();
            title = titleString == null ? null : Title.valueOf(titleString.toUpperCase());

            JsonElement firstNameJson = jsonParser.parse(request).getAsJsonObject().get("first_name");
            firstName = firstNameJson == null ? null : firstNameJson.getAsString();

            JsonElement lastNameJson = jsonParser.parse(request).getAsJsonObject().get("last_name");
            lastName = lastNameJson == null ? null : lastNameJson.getAsString();

            JsonElement emailJson = jsonParser.parse(request).getAsJsonObject().get("email");
            email = emailJson == null ? null : emailJson.getAsString();

            JsonElement birthdateJson = jsonParser.parse(request).getAsJsonObject().get("birthdate");
            String birthdateString = birthdateJson.getAsString();
            birthdate = birthdateString == null ? null : LocalDate.parse(birthdateString);

            JsonElement childrenJson = jsonParser.parse(request).getAsJsonObject().get("children");
            children = childrenJson == null ? null : childrenJson.getAsString();

            JsonElement nbChildrenJson = jsonParser.parse(request).getAsJsonObject().get("nb_children");
            nbChildren = nbChildrenJson == null ? null : nbChildrenJson.getAsString();

            JsonElement hasCarJson = jsonParser.parse(request).getAsJsonObject().get("car");
            String hasCarString = hasCarJson == null ? null : hasCarJson.getAsString();
            hasCar = hasCarString == null ? null : asBoolean(hasCarString);

            JsonElement addressJson = jsonParser.parse(request).getAsJsonObject().get("address");
            address = addressJson == null ? null : addressJson.getAsString();

            JsonElement zipcodeJson = jsonParser.parse(request).getAsJsonObject().get("zipcode");
            zipcode = zipcodeJson == null ? null : zipcodeJson.getAsString();

            JsonElement cityJson = jsonParser.parse(request).getAsJsonObject().get("city");
            city = cityJson == null ? null : cityJson.getAsString();

            JsonElement phoneNumberJson = jsonParser.parse(request).getAsJsonObject().get("phone");
            phoneNumber = phoneNumberJson == null ? null : phoneNumberJson.getAsString();

            JsonElement promotionalEmailsJson = jsonParser.parse(request).getAsJsonObject().get("optin");
            String promotionalEmailsString = promotionalEmailsJson == null ? null : promotionalEmailsJson.getAsString();
            promotionalemails = promotionalEmailsString != null && asBoolean(promotionalEmailsString);

            JsonElement checkItemJson = jsonParser.parse(request).getAsJsonObject().get("check_item");
            checkItem = checkItemJson == null ? null : checkItemJson.getAsString();

            JsonElement lostJson = jsonParser.parse(request).getAsJsonObject().get("lost");
            String lostString = lostJson == null ? null : lostJson.getAsString();
            lost = lostString != null && asBoolean(lostString);
        }

        return apiService.cardRequest(
                deviceName, deviceSecret, origin, hostess, title, firstName, lastName, email, birthdate, children, nbChildren,
                hasCar, address, zipcode, city, phoneNumber, promotionalemails, checkItem, lost).asResponse();
    }

//    @POST
//    @Path("/user-create")
//    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD, MediaType.APPLICATION_FORM_URLENCODED })
//    @Produces({
//            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
//            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
//    })
//    @PrettyPrinting
//    public Response userCreate(@FormParam("request") String request) {
//        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);
//
//        String cardNumber, title, firstName, lastName, address, zipcode, city, email;
//        cardNumber = title = firstName = lastName = address = zipcode = city = email = null;
//        int promotionalEmails = 0;
//
//        if(!Strings.isNullOrEmpty(request)) {
//            JsonParser jsonParser = new JsonParser();
//
//            JsonElement cardNumberJson = jsonParser.parse(request).getAsJsonObject().get("card");
//            JsonElement titleJson = jsonParser.parse(request).getAsJsonObject().get("title");
//            JsonElement firstNameJson = jsonParser.parse(request).getAsJsonObject().get("first_name");
//            JsonElement lastNameJson = jsonParser.parse(request).getAsJsonObject().get("last_name");
//            JsonElement addressJson = jsonParser.parse(request).getAsJsonObject().get("address");
//            JsonElement zipcodeJson = jsonParser.parse(request).getAsJsonObject().get("zipcode");
//            JsonElement cityJson = jsonParser.parse(request).getAsJsonObject().get("city");
//            JsonElement emailJson = jsonParser.parse(request).getAsJsonObject().get("email");
//            JsonElement promotionalEmailsJson = jsonParser.parse(request).getAsJsonObject().get("optin");
//            cardNumber = cardNumberJson == null ? null : cardNumberJson.getAsString();
//            title = titleJson == null ? null : titleJson.getAsString();
//            firstName = firstNameJson == null ? null : firstNameJson.getAsString();
//            lastName = lastNameJson == null ? null : lastNameJson.getAsString();
//            address = addressJson == null ? null : addressJson.getAsString();
//            zipcode = zipcodeJson == null ? null : zipcodeJson.getAsString();
//            city = cityJson == null ? null : cityJson.getAsString();
//            email = emailJson == null ? null : emailJson.getAsString();
//            promotionalEmails = promotionalEmailsJson == null ? 0 : promotionalEmailsJson.getAsInt();
//        }
//        if(title == null || Strings.isNullOrEmpty(firstName) || Strings.isNullOrEmpty(lastName)) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 302)
//                            .add("message", "Invalid parameter")
//                            .toJsonString())
//                    .build();
//        }
//
//        if(!cardRepository.cardNumberIsValid(cardNumber)) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 312)
//                            .add("message", "Invalid card number")
//                            .toJsonString())
//                    .build();
//        }
//
//        Card card = cardRepository.findByExactNumber(cardNumber);
//
//        if(card != null) {
//            if(card.getStatus() != CardStatus.ENABLED) {
//                return Response
//                        .ok()
//                        .type(MediaType.APPLICATION_JSON_TYPE)
//                        .entity(new JsonBuilder()
//                                .add("status", 303)
//                                .add("message", "Invalid card")
//                                .toJsonString())
//                        .build();
//            }
//
//            //TODO: Check against center of hostess
//
//            if(card.getOwner() != null) {
//                return Response
//                        .ok()
//                        .type(MediaType.APPLICATION_JSON_TYPE)
//                        .entity(new JsonBuilder()
//                                .add("status", 308)
//                                .add("message", "Card is already bound to another user")
//                                .toJsonString())
//                        .build();
//            }
//        }
//
//        User user = userRepository.findByExactEmailAndCenter(email, );
//        if(user != null) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 309)
//                            .add("message", "Email already exists in our system")
//                            .toJsonString())
//                    .build();
//        }
//
//        //TODO: Extract center of request
//        user = userRepository.findOrCreate(true, Title.valueOf(title), firstName, lastName, email,
//                                           address, zipcode, city, null, null, cardNumber,
//                                           asBoolean(promotionalEmails), null, null);
//        if(user == null) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 316)
//                            .add("message", "Failed to create or update user")
//                            .toJsonString())
//                    .build();
//        }
//        card = cardRepository.findByExactNumber(cardNumber);
//
//        if(card != null && card.getOwner() != user) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 314)
//                            .add("message", "Failed to bind user to card")
//                            .toJsonString())
//                    .build();
//        }
//
//        return Response
//                .ok()
//                .type(MediaType.APPLICATION_JSON_TYPE)
//                .entity(new JsonBuilder()
//                        .add("status", 200)
//                        .toJsonString())
//                .build();
//    }

//    @POST
//    @Path("/user-update")
//    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD, MediaType.APPLICATION_FORM_URLENCODED })
//    @Produces({
//            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
//            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
//    })
//    @PrettyPrinting
//    public Response userUpdate(@FormParam("request") String request) {
//        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);
//
//        String cardNumber, title, firstName, lastName, address, zipcode, city, email;
//        cardNumber = title = firstName = lastName = address = zipcode = city = email = null;
//        int promotionalEmails = 0;
//
//        if(!Strings.isNullOrEmpty(request)) {
//            JsonParser jsonParser = new JsonParser();
//
//            JsonElement cardNumberJson = jsonParser.parse(request).getAsJsonObject().get("card");
//            JsonElement titleJson = jsonParser.parse(request).getAsJsonObject().get("title");
//            JsonElement firstNameJson = jsonParser.parse(request).getAsJsonObject().get("first_name");
//            JsonElement lastNameJson = jsonParser.parse(request).getAsJsonObject().get("last_name");
//            JsonElement addressJson = jsonParser.parse(request).getAsJsonObject().get("address");
//            JsonElement zipcodeJson = jsonParser.parse(request).getAsJsonObject().get("zipcode");
//            JsonElement cityJson = jsonParser.parse(request).getAsJsonObject().get("city");
//            JsonElement emailJson = jsonParser.parse(request).getAsJsonObject().get("email");
//            JsonElement promotionalEmailsJson = jsonParser.parse(request).getAsJsonObject().get("optin");
//            cardNumber = cardNumberJson == null ? null : cardNumberJson.getAsString();
//            title = titleJson == null ? null : titleJson.getAsString();
//            firstName = firstNameJson == null ? null : firstNameJson.getAsString();
//            lastName = lastNameJson == null ? null : lastNameJson.getAsString();
//            address = addressJson == null ? null : addressJson.getAsString();
//            zipcode = zipcodeJson == null ? null : zipcodeJson.getAsString();
//            city = cityJson == null ? null : cityJson.getAsString();
//            email = emailJson == null ? null : emailJson.getAsString();
//            promotionalEmails = promotionalEmailsJson == null ? 0 : promotionalEmailsJson.getAsInt();
//        }
//
//        if(Strings.isNullOrEmpty(title) || Strings.isNullOrEmpty(firstName) || Strings.isNullOrEmpty(lastName)
//                || Strings.isNullOrEmpty(cardNumber) || Strings.isNullOrEmpty(email)) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 302)
//                            .add("message", "Invalid parameter")
//                            .toJsonString())
//                    .build();
//        }
//
//        if(!cardRepository.cardNumberIsValid(cardNumber)) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 312)
//                            .add("message", "Invalid card number")
//                            .toJsonString())
//                    .build();
//        }
//
//        Card card = cardRepository.findByExactNumber(cardNumber);
//
//        if(card != null) {
//            if(card.getStatus() != CardStatus.ENABLED) {
//                return Response
//                        .ok()
//                        .type(MediaType.APPLICATION_JSON_TYPE)
//                        .entity(new JsonBuilder()
//                                .add("status", 303)
//                                .add("message", "Invalid card")
//                                .toJsonString())
//                        .build();
//            }
//
//            //TODO: Check against center of hostess
//
//            if(card.getOwner() != null) {
//                return Response
//                        .ok()
//                        .type(MediaType.APPLICATION_JSON_TYPE)
//                        .entity(new JsonBuilder()
//                                .add("status", 308)
//                                .add("message", "Card is already bound to another user")
//                                .toJsonString())
//                        .build();
//            }
//        }
//
//        User user = userRepository.findByExactEmailAndCenter(email, );
//        if(user != null && !(firstName.equals(user.getFirstName()) && lastName.equals(user.getLastName()))) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 309)
//                            .add("message", "Email already exists in our system")
//                            .toJsonString())
//                    .build();
//        }
//
//        //TODO: Extract center of request
//        user = userRepository.findOrCreate(true, Title.valueOf(title), firstName, lastName, email,
//                address, zipcode, city, null, null, cardNumber,
//                asBoolean(promotionalEmails), null, null);
//        if(user == null) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 316)
//                            .add("message", "Failed to create or update user")
//                            .toJsonString())
//                    .build();
//        }
//        user.setTitle(Title.valueOf(title));
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setEmail(email);
//        user.setAddress(address);
//        user.setZipcode(zipcode);
//        user.setCity(city);
//        user.setPromotionalEmails(asBoolean(promotionalEmails));
//        user.newCard(cardNumber);
//
//        card = cardRepository.findByExactNumber(cardNumber);
//
//        if(card != null && card.getOwner() != user) {
//            return Response
//                    .ok()
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(new JsonBuilder()
//                            .add("status", 314)
//                            .add("message", "Failed to bind user to card")
//                            .toJsonString())
//                    .build();
//        }
//
//        return Response
//                .ok()
//                .type(MediaType.APPLICATION_JSON_TYPE)
//                .entity(new JsonBuilder()
//                        .add("status", 200)
//                        .toJsonString())
//                .build();
//    }

    @POST
    @Path("/user-detail")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD, MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR
    })
    @PrettyPrinting
    public Response userDetail(@FormParam("request") String request) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        String reference = jsonParser.parse(request).getAsJsonObject().get("id").getAsString();

        //TODO: Not sure how to implement 302 and 310

        if(reference == null) {
            return Response
                    .ok()
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new JsonBuilder()
                            .add("status", 302)
                            .add("message", "Invalid parameter")
                            .toJsonString())
                    .build();
        }

        User user = userRepository.findByReference(reference);

        if(user == null) {
            return Response
                    .ok()
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new JsonBuilder()
                            .add("status", 304)
                            .add("message", "Invalid user")
                            .toJsonString())
                    .build();
        }

        JsonObject userJson = gson.toJsonTree(UserViewModel.fromUser(user)).getAsJsonObject();

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new JsonBuilder()
                        .add("status", 200)
                        .add("response", userJson)
                        .toJsonString())
                .build();
    }

    public static boolean asBoolean(final int i) {
        return i > 0;
    }
    public static boolean asBoolean(final String s) {
        if(s.toUpperCase().equals("TRUE")) {
            return true;
        } else if(Integer.parseInt(s) > 0) {
            return true;
        }
        return false;
    }
    public static String asString(final int i) {
        return "" + i;
    }
    public static String asString(final boolean bool) {
        return bool ? "true" : "false";
    }
    public static String asString(final LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }

    @Inject UserRepository userRepository;
    @Inject CardRepository cardRepository;
    @Inject CenterRepository centerRepository;
    @Inject ClockService clockService;
    @Inject ApiService apiService;
}
