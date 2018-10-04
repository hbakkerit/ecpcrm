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

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulMediaType;
import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
import org.apache.isis.viewer.restfulobjects.rendering.service.conneg.PrettyPrinting;
import org.apache.isis.viewer.restfulobjects.server.resources.ResourceAbstract;

import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck.CardCheckRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardgame.CardGameRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardrequest.CardRequestRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websitecardrequest.WebsiteCardRequestRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate.WebsiteUserCreateRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail.WebsiteUserDetailRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusermodify.WebsiteUserModifyRequestViewModel;

@Path("/crm/api/7.0")
public class EcpCrmResource extends ResourceAbstract  {

    Gson gson = new Gson();

    @Override
    protected void init(
            final RepresentationType representationType,
            final Where where,
            final RepresentationService.Intent intent
    ) {
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

        CardCheckRequestViewModel requestViewModel = gson.fromJson(request, CardCheckRequestViewModel.class);
        if (requestViewModel == null) {
            requestViewModel = new CardCheckRequestViewModel();
        }

        return apiService.cardCheck(
                deviceName,
                deviceSecret,
                requestViewModel.getCardNumber(),
                requestViewModel.getOrigin()
        ).asResponse();
    }

    @POST
    @Path("/card-game")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardGame(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        CardGameRequestViewModel requestViewModel = gson.fromJson(request, CardGameRequestViewModel.class);

        return apiService.cardGame(device, requestViewModel).asResponse();
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
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        CardRequestRequestViewModel requestViewModel = gson.fromJson(request, CardRequestRequestViewModel.class);

        return apiService.cardRequest(device, requestViewModel).asResponse();
    }

    @POST
    @Path("/website-card-request")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteCardRequest(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        WebsiteCardRequestRequestViewModel requestViewModel = gson.fromJson(request, WebsiteCardRequestRequestViewModel.class);
        if (requestViewModel == null) {
            requestViewModel = new WebsiteCardRequestRequestViewModel();
        }

        return apiService.websiteCardRequest(
                deviceName,
                deviceSecret,
                requestViewModel.getOrigin(),
                requestViewModel.getCenterId(),
                ApiService.asTitle(requestViewModel.getTitle()),
                requestViewModel.getFirstName(),
                requestViewModel.getLastName(),
                requestViewModel.getEmail(),
                requestViewModel.getPasswword(),
                ApiService.asLocalDate(requestViewModel.getBirthdate()),
                requestViewModel.getChildren(),
                requestViewModel.getNbChildren(),
                ApiService.asBoolean(requestViewModel.getHasCar()),
                requestViewModel.getAddress(),
                requestViewModel.getZipcode(),
                requestViewModel.getCity(),
                requestViewModel.getPhoneNumber(),
                ApiService.asBoolean(requestViewModel.getPromotionalEmails()),
                requestViewModel.getCheckCode(),
                requestViewModel.getBoutiques()
        ).asResponse();
    }

    @POST
    @Path("/website-user-create")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteUserCreate(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        WebsiteUserCreateRequestViewModel requestViewModel = gson.fromJson(request, WebsiteUserCreateRequestViewModel.class);
        if (requestViewModel == null) {
            requestViewModel = new WebsiteUserCreateRequestViewModel();
        }

        return apiService.websiteUserCreate(
                deviceName,
                deviceSecret,
                requestViewModel.getCenterId(),
                requestViewModel.getCheckCode(),
                ApiService.asTitle(requestViewModel.getTitle()),
                requestViewModel.getFirstName(),
                requestViewModel.getLastName(),
                requestViewModel.getEmail(),
                ApiService.asLocalDate(requestViewModel.getBirthdate()),
                requestViewModel.getChildren(),
                requestViewModel.getNbChildren(),
                ApiService.asBoolean(requestViewModel.getHasCar()),
                requestViewModel.getAddress(),
                requestViewModel.getZipcode(),
                requestViewModel.getCity(),
                requestViewModel.getPhoneNumber(),
                ApiService.asBoolean(requestViewModel.getPromotionalEmails())
        ).asResponse();
    }

    @POST
    @Path("/website-user-modify")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteUserModify(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        WebsiteUserModifyRequestViewModel requestViewModel = gson.fromJson(request, WebsiteUserModifyRequestViewModel.class);
        if (requestViewModel == null) {
            requestViewModel = new WebsiteUserModifyRequestViewModel();
        }

        return apiService.websiteUserModify(
                deviceName,
                deviceSecret,
                requestViewModel.getCheckCode(),
                requestViewModel.getCardNumber(),
                requestViewModel.getEmail(),
                ApiService.asTitle(requestViewModel.getTitle()),
                requestViewModel.getFirstName(),
                requestViewModel.getLastName(),
                ApiService.asLocalDate(requestViewModel.getBirthdate()),
                requestViewModel.getChildren(),
                requestViewModel.getNbChildren(),
                ApiService.asBoolean(requestViewModel.getHasCar()),
                requestViewModel.getAddress(),
                requestViewModel.getZipcode(),
                requestViewModel.getCity(),
                requestViewModel.getPhoneNumber(),
                ApiService.asBoolean(requestViewModel.getPromotionalEmails())
        ).asResponse();
    }

    @POST
    @Path("/website-user-detail")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteUserDetail(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        WebsiteUserDetailRequestViewModel requestViewModel = gson.fromJson(request, WebsiteUserDetailRequestViewModel.class);
        if (requestViewModel == null) {
            requestViewModel = new WebsiteUserDetailRequestViewModel();
        }

        return apiService.websiteUserDetail(
                deviceName,
                deviceSecret,
                requestViewModel.getEmail(),
                requestViewModel.getCheckCode()
        ).asResponse();
    }

    @Inject
    AuthenticationDeviceRepository authenticationDeviceRepository;
    @Inject ApiService apiService;
}
