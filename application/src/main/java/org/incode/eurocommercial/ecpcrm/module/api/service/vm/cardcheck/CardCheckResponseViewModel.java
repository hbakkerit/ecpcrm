package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck;

import lombok.Data;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractBaseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

@Data(staticConstructor = "create")
public class CardCheckResponseViewModel extends AbstractBaseViewModel {
    private final String id;
    private final String name;
    private final String email;
    private final String title;
    private final String first_name;
    private final String last_name;
    private final String birthdate;
    private final String optin;
    private final boolean game;
    private final String card_image;

    public CardCheckResponseViewModel fromCard(final Card card) {
        User user = card.getOwner();
        if(user == null) {
            return null;
        }

        return CardCheckResponseViewModel.create(
                user.getReference(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getTitle().toString().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                asString(user.getBirthDate()),
                asString(user.isPromotionalEmails()),
                card.canPlay(),
                ""
        );
    }
}
