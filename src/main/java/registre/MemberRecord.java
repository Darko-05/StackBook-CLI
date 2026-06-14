package registre;

import model.Membre;

import java.util.Map;

record MemberRecord(Map<String, Membre> membres) {

    MemberRecord {

        membres = Map.copyOf(membres);

    }

}
