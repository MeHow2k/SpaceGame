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
        enStrings.put("Punkty", "Points");
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
        enStrings.put("Rekordy", "Leaderboards");
        enStrings.put("Gracz", "Player");
        enStrings.put("Czas gry", "Playtime");



        enStrings.put("Osiągnięcia", "Achievements");
        enStrings.put("Lista osiągnięć", "Achievements list");
        enStrings.put("Ach0opis", "Beat a game.");
        enStrings.put("Ach1opis", "Complete 10 level.");
        enStrings.put("Ach2opis", "Complete 20 level.");
        enStrings.put("Ach3opis", "Complete 30 level.");
        enStrings.put("Ach4opis", "Complete 40 level.");
        enStrings.put("Ach5opis", "Complete 50 level.");
        enStrings.put("Ach6opis", "Beat 1 boss hitless.");
        enStrings.put("Ach7opis", "Beat 2 boss hitless.");
        enStrings.put("Ach8opis", "Beat 3 boss hitless.");
        enStrings.put("Ach9opis", "Beat 4 boss hitless.");
        enStrings.put("Ach10opis", "Beat 5 boss hitless.");
        enStrings.put("Ach11opis", "Earn 50 additional lives.");
        enStrings.put("Ach12opis", "Earn 50 firerate upgrades.");
        enStrings.put("Ach13opis", "Earn 50 bonus shields.");
        enStrings.put("Ach14opis", "Earn 50 ally aid bonuses.");
        enStrings.put("Ach15opis", "Earn 50 weapon upgrades.");
        enStrings.put("Ach16opis", "Earn 50 bonus points boxes.");
        enStrings.put("Ach17opis", "Earn 10000 points.");
        enStrings.put("Ach18opis", "Earn 20000 points.");
        enStrings.put("Ach19opis", "Earn 30000 points.");
        enStrings.put("Ach20opis", "Earn 50000 points.");
        enStrings.put("Ach21opis", "Shot 7777 times.");
        enStrings.put("Ach22opis", "Play 5 games in one session.");
        enStrings.put("Ach23opis", "Fail on tutorial level.");


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
        plStrings.put("Punkty", "Punkty");
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
        plStrings.put("Rekordy", "Tabela wyników");
        plStrings.put("Gracz", "Gracz");
        plStrings.put("Czas gry", "Czas gry");

        plStrings.put("Osiągnięcia", "Osiągnięcia");
        plStrings.put("Lista osiągnięć", "Lista osiągnięć");
        plStrings.put("Ach0opis", "Ukończ grę.");
        plStrings.put("Ach1opis", "Ukończ 10 poziom.");
        plStrings.put("Ach2opis", "Ukończ 20 poziom.");
        plStrings.put("Ach3opis", "Ukończ 30 poziom.");
        plStrings.put("Ach4opis", "Ukończ 40 poziom.");
        plStrings.put("Ach5opis", "Ukończ 50 poziom.");
        plStrings.put("Ach6opis", "Pokonaj bossa 1 bez utraty HP.");
        plStrings.put("Ach7opis", "Pokonaj bossa 2 bez utraty HP.");
        plStrings.put("Ach8opis", "Pokonaj bossa 3 bez utraty HP.");
        plStrings.put("Ach9opis", "Pokonaj bossa 4 bez utraty HP.");
        plStrings.put("Ach10opis", "Pokonaj bossa 5 bez utraty HP.");
        plStrings.put("Ach11opis", "Zbierz 50 dodatkowych żyć.");
        plStrings.put("Ach12opis", "Zbierz 50 ulepszeń szybkostrzelności.");
        plStrings.put("Ach13opis", "Zbierz 50 dodatkowych tarcz.");
        plStrings.put("Ach14opis", "Zbierz 50 przywołań sojusznika.");
        plStrings.put("Ach15opis", "Zbierz 50 ulepszeń broni.");
        plStrings.put("Ach16opis", "Zbierz 50 skrzynek z punktami.");
        plStrings.put("Ach17opis", "Zdobądź 10000 punktów.");
        plStrings.put("Ach18opis", "Zdobądź 20000 punktów.");
        plStrings.put("Ach19opis", "Zdobądź 30000 punktów.");
        plStrings.put("Ach20opis", "Zdobądź 50000 punktów.");
        plStrings.put("Ach21opis", "Wystrzel 7777 pocisków.");
        plStrings.put("Ach22opis", "Rozegraj 5 gier w jednej sesji.");
        plStrings.put("Ach23opis", "Zakończ grę w samouczku.");

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
