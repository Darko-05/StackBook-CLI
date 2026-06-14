package registre;

import model.Emprunt;

import java.util.Map;

record BorrowRecord(Map<String, Emprunt> emprunts) {

    BorrowRecord {

        emprunts = Map.copyOf(emprunts);

    }

}
