package main;

import lombok.experimental.UtilityClass;
import view.*;

@UtilityClass
public final class AppView {

    // Views

    public static MainMenuView menuView = new MainMenuView();
    public static AuthorView authorView = new AuthorView();
    public static HomeEditorView homeEditorView = new HomeEditorView();
    public static ImportExportView importExportView = new ImportExportView();
    public static ReservationView reservationView = new ReservationView();
    public static ReportView reportView = new ReportView();
    public static MemberView memberView = new MemberView();
    public static BorrowView borrowView = new BorrowView();
    public static BookView bookView = new BookView();

}
