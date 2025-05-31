package pl.koncerty.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.koncerty.gui.ListaBiletController;
import pl.koncerty.gui.ShowTicketController;
import pl.koncerty.gui.UzytkownikController;
import pl.koncerty.model.Uzytkownik;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;
import javafx.scene.control.Alert;


public class SceneUtil {

    private static final Stack<SceneData> historiaScen = new Stack<>();
    private static Uzytkownik aktualnyUzytkownik;
    public static void setAktualnyUzytkownik(Uzytkownik uzytkownik) {
        aktualnyUzytkownik = uzytkownik;
        System.out.println("SceneUtil - ustawiono aktualnego użytkownika: " +
                (uzytkownik != null ? uzytkownik.getLogin() + " (ID: " + uzytkownik.getId() + ")" : "NULL"));
    }

    public static Uzytkownik getAktualnyUzytkownik() {
        return aktualnyUzytkownik;
    }

    private static void dodajStyle(Scene scene) {
        scene.getStylesheets().add(Objects.requireNonNull(SceneUtil.class.getResource("/pl/koncerty/style/style.css")).toExternalForm());
    }

    public static void otworzPanel(String fxmlPath, String title, Node componentToClose) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneUtil.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Object controller = fxmlLoader.getController();

            if (aktualnyUzytkownik != null) {
                if (controller instanceof UzytkownikController) {
                    System.out.println("Przekazano użytkownika do UzytkownikController: " + aktualnyUzytkownik.getLogin());
                    ((UzytkownikController) controller).initUzytkownik(aktualnyUzytkownik);
                } else if (controller instanceof ListaBiletController) {
                    System.out.println("Przekazano użytkownika do ListaBiletController: " + aktualnyUzytkownik.getLogin());
                    ((ListaBiletController) controller).initUzytkownik(aktualnyUzytkownik);
                } else if (controller instanceof ShowTicketController) {
                    System.out.println("Przekazano użytkownika do ShowTicketController: " + aktualnyUzytkownik.getLogin());
                    ((ShowTicketController) controller).initUzytkownik(aktualnyUzytkownik);
                }
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            dodajStyle(scene);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            if (componentToClose != null && componentToClose.getScene() != null && componentToClose.getScene().getWindow() instanceof Stage) {
                Stage currentStage = (Stage) componentToClose.getScene().getWindow();
                historiaScen.push(new SceneData(currentStage.getScene(), currentStage.getTitle(), currentStage));
                System.out.println("Dodano do historii: " + currentStage.getTitle());
            }

            if (componentToClose != null && componentToClose.getScene() != null && componentToClose.getScene().getWindow() instanceof Stage) {
                ((Stage) componentToClose.getScene().getWindow()).close();
                System.out.println("Zamknięto poprzednie okno.");
            } else {
                System.out.println("UWAGA: Komponent do zamknięcia nie ma sceny lub jest nullem. Nie można zamknąć poprzedniego okna.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd ładowania sceny");
            alert.setHeaderText("Nie można załadować panelu.");
            alert.setContentText("Wystąpił błąd podczas ładowania pliku FXML: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void powrot(Node aktualnyKomponent) {
        if (!historiaScen.isEmpty()) {
            SceneData poprzednieDane = historiaScen.pop();
            Stage poprzedniStage = poprzednieDane.stage;

            poprzedniStage.setTitle(poprzednieDane.tytul);
            poprzedniStage.setScene(poprzednieDane.scene);
            dodajStyle(poprzednieDane.scene);
            poprzedniStage.show();

            if (aktualnyKomponent != null && aktualnyKomponent.getScene() != null && aktualnyKomponent.getScene().getWindow() instanceof Stage) {
                ((Stage) aktualnyKomponent.getScene().getWindow()).close();
            }
        }
    }

    public static void wyloguj(Node komponentZOknaDoZamkniecia) {
        aktualnyUzytkownik = null;
        System.out.println("SceneUtil - użytkownik wylogowany");
        otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", komponentZOknaDoZamkniecia);
    }

    private static class SceneData {
        Scene scene;
        String tytul;
        Stage stage;

        public SceneData(Scene scene, String tytul, Stage stage) {
            this.scene = scene;
            this.tytul = tytul;
            this.stage = stage;
        }
    }
}