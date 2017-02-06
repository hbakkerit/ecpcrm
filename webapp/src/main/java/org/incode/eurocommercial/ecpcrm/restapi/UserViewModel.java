package org.incode.eurocommercial.ecpcrm.restapi;

import java.util.List;
import java.util.stream.Collectors;

import org.incode.eurocommercial.ecpcrm.dom.user.User;

import lombok.Data;
import static org.incode.eurocommercial.ecpcrm.restapi.EcpCrmResource.asString;

@Data(staticConstructor = "create")
public class UserViewModel {
    private final String id;
    private final String title;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String address;
    private final String zipcode;
    private final String city;
    private final String full_address;
    private final String phoneNumber;
    private final String birthDate;
    private final String center;
    private final String enabled;
    private final String optin;
//    TODO: Fully implement this viewmodel
//    private final String car;
//    private final List<String> boutiques;
    private final String haschildren;
    private final String nb_children;
    private final List<ChildViewModel> children;
    private final List<CardViewModel> cards;
//    private final SortedSet<ChildCareViewModel> child_cares;

    public static UserViewModel fromUser(final User user) {
        List<ChildViewModel> userChildren = user.getChildren().stream()
                .map(ChildViewModel::fromChild)
                .collect(Collectors.toList());
        List<CardViewModel> userCards = user.getCards().stream()
                .map(CardViewModel::fromCard)
                .collect(Collectors.toList());

        return UserViewModel.create(
                user.getReference(),
                user.getTitle().toString().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getZipcode(),
                user.getCity(),
                user.getAddress() + " - " + user.getZipcode() + " - " + user.getCity(),
                user.getPhoneNumber(),
                asString(user.getBirthDate()),
                user.getCenter().title(),
                asString(user.isEnabled()),
                asString(user.isPromotionalEmails()),
                asString(userChildren.size() != 0),
                asString(userChildren.size()),
                userChildren,
                userCards
        );
    }
}
