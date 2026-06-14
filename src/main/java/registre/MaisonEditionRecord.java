package registre;

import model.MaisonEdition;

import java.util.List;

public record MaisonEditionRecord(List<MaisonEdition> maisonEditions) {

    public MaisonEditionRecord {

        maisonEditions = List.copyOf(maisonEditions);

    }

}
