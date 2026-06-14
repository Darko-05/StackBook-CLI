package main;

import controlleur.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.TextModifier;

import java.util.Map;

import static java.lang.IO.println;
import static main.AppView.menuView;
import static util.MenuUtil.*;
import static util.DataUtil.nombreTotalLivres;
import static util.DataUtil.nombreTotalMembres;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainController {

    private final static MainController MAIN_CONTROLLER = new MainController();

    public void launchControllers() {

        // Menu principal

        menuView.showMainMenu(nombreTotalLivres(), nombreTotalMembres());

        // Main Handler

        handleUserChoice(
            Map.of(
            "1", new BookController()::handleBookManagement,
            "2", new MemberController()::handleMemberManagement,
            "3", new BorrowController()::handleBorrowManagement,
            "4", new ReservationController()::handleReservationManagement,
            "5", new AuthorController()::handleAuthorManagement,
            "6", new HomeEditorController()::handleHomeEditorManagement,
            "7", new ReportController()::handleReportSystem,
            "8", new ImportExportController()::handleImportExportManagement,
            "0", this::handleEnd
            )
        );

    }

     private void handleEnd() {

        try {

            throw new RuntimeException();

        } catch (RuntimeException e) {

            println(TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.TURQUOISE, "Merci d'avoir utiliser l'application !!!")));

            System.exit(0);

        }

    }

    // Recuperer le Main Controller

    public static MainController getInstance() {

        return MAIN_CONTROLLER;

    }

}