package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

import lombok.Data;

@Data(staticConstructor = "create")
public class WebsiteUserCreateResponseViewModel {
    private final String user_id;
    private final String number;

    public static WebsiteUserCreateResponseViewModel fromUser(final User user) {
        Card card = user.getCards().first();
        if(card == null) {
            return null;
        }

        return WebsiteUserCreateResponseViewModel.create(
                user.getReference(),
                card.getNumber()
        );
    }
}
