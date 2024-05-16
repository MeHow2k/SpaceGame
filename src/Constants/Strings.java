package Constants;

import java.util.HashMap;
import java.util.Map;

public class Strings {
    private Map<Integer, Map<String, String>> languageMap;

    public Strings() {
        languageMap = new HashMap<>();

        // Dodanie tłumaczeń dla poszczególnych stringów
        //ANGIELSKI
        Map<String, String> enStrings = new HashMap<>();
        enStrings.put("Start", "Start");
        enStrings.put("Jak grać?", "How To Play?");
        enStrings.put("Opcje", "Settings");
        enStrings.put("Autorzy", "Authors");
        enStrings.put("Wyjście", "Exit");

        enStrings.put("Aby zatwierdzić", "To confirm press ENTER");
        enStrings.put("Aby powrócićESC", "To return to the menu press ESC");
        enStrings.put("Aby powrócićENTER", "To return to the menu press ENTER");
        enStrings.put("Aby zmienić wartość", "To change the value use arrows");
        enStrings.put("Wybierz swój statek!", "Choose your spaceship!");
        enStrings.put("Wybrany statek:", "Selected spaceship:");
        enStrings.put("Nazwa:", "Name:");
        enStrings.put("Dostępne statki:", "Available spaceships:");
        enStrings.put("Graj!", "Play!");
        enStrings.put("Głośność muzyki:", "Music Volume:");
        enStrings.put("Głośność dźwięków:", "Sound Volume:");
        enStrings.put("Wycisz wszystko:", "Mute All:");
        enStrings.put("TAK", "YES");
        enStrings.put("NIE", "NO");
        enStrings.put("Zresetuj wynik", "Reset your best score");
        enStrings.put("Nie ma zapisanego", "There is no best score saved.");
        enStrings.put("Czy na pewno zresetować", "Are you sure you want to reset your best score?");
        enStrings.put("Aktualny:", "Current:");
        enStrings.put("punktów", "points");
        enStrings.put("Punkty:", "Points:");
        enStrings.put("poziom", "Level");
        enStrings.put("Aby odpauzować", "To unpause press \"p\"");
        enStrings.put("lvl0_trafiaj", "Hit your opponents while dodging them and their shots.");
        enStrings.put("lvl0_użyj", "Use arrows to move. Use SPACEBAR to shoot.");
        enStrings.put("lvl0_gra", "The game ends when you lose all your lives!");
        enStrings.put("lvl0_podczas", "Allied spaceships appear during the game to support you with bonuses.");
        enStrings.put("lvl0_po", "After shooting down an enemy, bonuses appear to be picked up.");
        enStrings.put("Pauza", "Pause");
        enStrings.put("Czy chcesz powrócić do menu?", "Do you want to return to the menu?");
        enStrings.put("Koniec gry", "Game over");
        enStrings.put("Uzyskałeś", "Yoy have obtained");
        enStrings.put("Przegrałeś!", "You lost!");
        enStrings.put("Czy chcesz zagrać ponownie?", "Do you want to play again?");
        enStrings.put("Gratulacje!", "Congratulations!");
        enStrings.put("Ukończyłeś grę!", "You have completed the game!");
        enStrings.put("Wynik końcowy:", "Final score:");
        enStrings.put("Nowy rekord!", "A new record!");
        enStrings.put("Wybierz język", "Select language");
        enStrings.put("Język", "Language");
        enStrings.put("Polski", "Polski");
        enStrings.put("Angielski", "English");
        enStrings.put("Zapisz i wyjdź", "Save and exit");
        enStrings.put("Ustawienia gracza", "Player settings");
        enStrings.put("Wpisz korzystając", "Enter player name using a keyboard");
        enStrings.put("Wpisz nazwę gracza", "Enter player name:");
        enStrings.put("Zmień nazwę gracza", "Change player name");
        enStrings.put("TarczaON", "! PROTECTION SHIELD ACTIVATED !");
        enStrings.put("TarczaOFF", "PROTECTION SHIELD INACTIVE");
        enStrings.put("PokażFPS", "Show FPS:");
        enStrings.put("Zresetuj wszystko", "Reset whole progress");
        enStrings.put("Zresetuj wszystko2", "Reset all highscores and achievements");
        enStrings.put("Czy na pewno?", "Are you sure?");

        //POLSKI
        Map<String, String> plStrings = new HashMap<>();
        plStrings.put("Start", "Start");
        plStrings.put("Jak grać?", "Jak Grać?");
        plStrings.put("Opcje", "Opcje");
        plStrings.put("Autorzy", "Autorzy");
        plStrings.put("Wyjście", "Wyjście");
        plStrings.put("Aby zatwierdzić", "Aby zatwierdzić wciśnij ENTER");
        plStrings.put("Aby powrócićESC", "Aby powrócić do menu wciśnij ESC");
        plStrings.put("Aby powrócićENTER", "Aby powrócić do menu wciśnij ENTER");
        plStrings.put("Aby zmienić wartość", "Aby zmienić wartość użyj strzałek");
        plStrings.put("Wybierz swój statek!", "Wybierz swój statek!");
        plStrings.put("Wybrany statek:", "Wybrany statek:");
        plStrings.put("Nazwa:", "Nazwa:");
        plStrings.put("Dostępne statki:", "Dostępne statki:");
        plStrings.put("Graj!", "Graj!");
        plStrings.put("Głośność muzyki:", "Głośność Muzyki:");
        plStrings.put("Głośność dźwięków:", "Głośność Dźwięków:");
        plStrings.put("Wycisz wszystko:", "Wycisz Wszystko:");
        plStrings.put("TAK", "TAK");
        plStrings.put("NIE", "NIE");
        plStrings.put("Zresetuj wynik", "Zresetuj najlepszy wynik:");
        plStrings.put("Nie ma zapisanego", "Nie ma zapisanego najlepszego wyniku.");
        plStrings.put("Czy na pewno zresetować", "Czy na pewno chcesz zresetować najlepszy wynik?");
        plStrings.put("Aktualny:", "Aktualny:");
        plStrings.put("punktów", "punktów");
        plStrings.put("Punkty:", "Punkty: ");
        plStrings.put("poziom", "Poziom");
        plStrings.put("Aby odpauzować", "Aby odpauzować naciśnij klawisz \"p\"");
        plStrings.put("lvl0_trafiaj", "Trafiaj w przeciwników, unikając ich oraz ich strzałów.");
        plStrings.put("lvl0_użyj", "Użyj strzałek aby się poruszać. Użyj SPACJI, aby strzelać.");
        plStrings.put("lvl0_gra", "Gra skończy się gdy stracisz wszystkie życia!");
        plStrings.put("lvl0_podczas", "Podczas gry pojawiają się sojusznicze statki wspierające bonusami.");
        plStrings.put("lvl0_po", "Po zestrzeleniu wroga pojawiają się bonusy do zbierania.");
        plStrings.put("Pauza", "Pauza");
        plStrings.put("Czy chcesz powrócić do menu?", "Czy chcesz powrócić do menu?");
        plStrings.put("Koniec gry", "Koniec gry");
        plStrings.put("Uzyskałeś", "Uzyskałeś");
        plStrings.put("Przegrałeś!", "Przegrałeś!");
        plStrings.put("Czy chcesz zagrać ponownie?", "Czy chcesz zagrać ponownie?");
        plStrings.put("Gratulacje!", "Gratulacje!");
        plStrings.put("Ukończyłeś grę!", "Ukończyłeś grę!");
        plStrings.put("Wynik końcowy:", "Wynik końcowy: ");
        plStrings.put("Nowy rekord!", "Nowy rekord!");
        plStrings.put("Wybierz język", "Wybierz język");
        plStrings.put("Język", "Język");
        plStrings.put("Polski", "Polski");
        plStrings.put("Angielski", "English");
        plStrings.put("Zapisz i wyjdź", "Zapisz i wyjdź");
        plStrings.put("Ustawienia gracza", "Ustawienia gracza");
        plStrings.put("Wpisz korzystając", "Wpisz nazwę gracza korzystając z klawiatury");
        plStrings.put("Wpisz nazwę gracza", "Wpisz nazwę gracza:");
        plStrings.put("Zmień nazwę gracza", "Zmień nazwę gracza");
        plStrings.put("TarczaON", "! TARCZA OCHRONNA AKTYWOWANA !");
        plStrings.put("TarczaOFF", "TARCZA OCHRONNA NIEAKTYWNA");
        plStrings.put("PokażFPS", "Pokaż FPS:");
        plStrings.put("Zresetuj wszystko", "Zresetuj cały postęp");
        plStrings.put("Zresetuj wszystko2", "Zresetuj wszystkie wyniki i osiągnięcia");
        plStrings.put("Czy na pewno?", "Czy na pewno?");


        // Dodanie tłumaczeń do mapy głównej
        languageMap.put(0, enStrings);
        languageMap.put(1, plStrings);
    }

    public String getString(String key) {
        // Pobranie tłumaczenia dla danego klucza i języka
        Map<String, String> strings = languageMap.get(C.LANGUAGE);
        if (strings != null) {
            String translatedString = strings.get(key);
            if (translatedString != null) {
                return translatedString;
            }
        }
        // Jeśli nie znaleziono tłumaczenia, zwracamy oryginalny klucz
        return key;
    }

//    public static void main(String[] args) {
//        Strings strings = new Strings();
// //test
//        System.out.println(strings.getString("Wyjście"));
//    }

}
