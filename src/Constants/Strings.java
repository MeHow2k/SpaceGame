package Constants;

import java.util.HashMap;
import java.util.Map;

public class Strings {
    private Map<Integer, Map<String, String>> languageMap;

    public Strings() {
        //todo tut do zmiany jezyka w Gamestates!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Inicjalizacja mapy przechowującej tłumaczenia dla różnych języków
        languageMap = new HashMap<>();

        // Dodanie tłumaczeń dla poszczególnych stringów
        //ANGIELSKI
        Map<String, String> enStrings = new HashMap<>();
        enStrings.put("Start", "Start");
        enStrings.put("Jak grać?", "How to play?");
        enStrings.put("Opcje", "Settings");
        enStrings.put("Autorzy", "Authors");
        enStrings.put("Wyjście", "Exit"); //todo skomentowane do przetłumaczenia (lewo klucz zostaw, prawo String do przetł.)

        enStrings.put("Aby zatwierdzić", "(en)Aby zatwierdzić wciśnij ENTER");
        enStrings.put("Aby powrócićESC", "(en)Aby powrócić do menu wciśnij ESC");
        enStrings.put("Aby powrócićENTER", "(en)Aby powrócić do menu wciśnij ENTER");
        enStrings.put("Aby zmienić wartość", "(en)Aby zmienić wartość użyj strzałek");
        enStrings.put("Wybierz swój statek!", "(en)Wybierz swój statek!");
        enStrings.put("Wybrany statek:", "(en)Wybrany statek:");
        enStrings.put("Nazwa:", "(en)Nazwa:");
        enStrings.put("Dostępne statki:", "(en)Dostępne statki:");
        enStrings.put("Graj!", "(en)Graj!");
        enStrings.put("Głośność muzyki:", "(en)Głośność muzyki:");
        enStrings.put("Głośność dźwięków:", "(en)Głośność dźwięków:");
        enStrings.put("Wycisz wszystko:", "(en)Wycisz wszystko:");
        enStrings.put("TAK", "(en)TAK");
        enStrings.put("NIE", "(en)NIE");
        enStrings.put("Zresetuj wynik", "(en)Zresetuj najlepszy wynik:");
        enStrings.put("Nie ma zapisanego", "(en)Nie ma zapisanego najlepszego wyniku.");
        enStrings.put("Czy na pewno zresetować", "(en)Czy na pewno chcesz zresetować najlepszy wynik?");
        enStrings.put("Aktualny:", "(en)Aktualny:");
        enStrings.put("punktów", "(en)punktów");
        enStrings.put("Punkty:", "(en)Punkty:");
        enStrings.put("poziom", "(en)Poziom");
        enStrings.put("Aby odpauzować", "(en)Aby odpauzować naciśnij klawisz \"p\"");
        enStrings.put("lvl0_trafiaj", "(en)Trafiaj w przeciwników, unikając ich oraz ich strzałów.");
        enStrings.put("lvl0_użyj", "(en)Użyj strzałek aby się poruszać. Użyj spacji, aby strzelać.");
        enStrings.put("lvl0_gra", "(en)Gra skończy się gdy stracisz wszystkie życia!");
        enStrings.put("lvl0_podczas", "(en)Podczas gry pojawiają się sojusznicze statki wspierające bonusami.");
        enStrings.put("lvl0_po", "(en)Po zestrzeleniu wroga pojawiają się bonusy do zbierania.");
        enStrings.put("Pauza", "(en)Pauza");
        enStrings.put("Czy chcesz powrócić do menu?", "(en)Czy chcesz powrócić do menu?");
        enStrings.put("Koniec gry", "(en)Koniec gry");
        enStrings.put("Uzyskałeś", "(en)Uzyskałeś");
        enStrings.put("Przegrałeś!", "(en)Przegrałeś!");
        enStrings.put("Czy chcesz zagrać ponownie?", "(en)Czy chcesz zagrać ponownie?");
        enStrings.put("Gratulacje!", "(en)Gratulacje!");
        enStrings.put("Ukończyłeś grę!", "(en)Ukończyłeś grę!");
        enStrings.put("Wynik końcowy:", "(en)Wynik końcowy:");
        enStrings.put("Nowy rekord!", "(en)Nowy rekord!");
        enStrings.put("Wybierz język", "Choose language");
        enStrings.put("Polski", "Polish");
        enStrings.put("Angielski", "English");

        //POLSKI
        Map<String, String> plStrings = new HashMap<>();
        plStrings.put("Start", "Start");
        plStrings.put("Jak grać?", "Jak grać?");
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
        plStrings.put("Głośność muzyki:", "Głośność muzyki:");
        plStrings.put("Głośność dźwięków:", "Głośność dźwięków:");
        plStrings.put("Wycisz wszystko:", "Wycisz wszystko:");
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
        plStrings.put("lvl0_użyj", "Użyj strzałek aby się poruszać. Użyj spacji, aby strzelać.");
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
        plStrings.put("Polski", "Polski");
        plStrings.put("Angielski", "Angielski");


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
