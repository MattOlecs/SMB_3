Mini-projekt 3 - Mateusz Oleksik (s29325)

Wykonano: 
=> Aplikacja frontendowa zarządzająca listą zakupów. [7 pkt]
=> Uwierzytelnianie użytkownika (rejestracja oraz logowanie). [3 pkt]

Tworzenie nowego produktu:
1. Należy się zalogować
2. Kliknąć przycisk "Shopping list"
3. Kliknąć przycisk z plusem w prawym dolnym rogu

Edycja produktu:
1. Należy się zalogować
2. Kliknąć przycisk "Shopping list"
3. Kliknąć w dowolnym miejscu na produkt który chcemy edytować

Usuwanie produktu:
1. Należy się zalogować
2. Kliknąć przycisk "Shopping list"
3. Kliknąć przycisk "delete" na produkcie który chcemy usunąć

Logowanie:
1. Otworzyć aplikację i wprowadzić dane

Rejestracja:
1. Otworzyć aplikację
2. Kliknąć przycisk "Create new account"
3. Wprowadzić dane

-------------------------------------
Aplikacja frontendowa zarządzająca listą zakupów:

Adapter wykorzystujący reference do firebase realtime database => FirebaseShoppingItemsAdapter.kt
Activity wykorzystujące adapter oraz wykonujące zapytania do bazy => FirebaseShoppingListActivity.kt
Wysyłanie nowego produktu do bazy => FirebaseShoppingListActivity.createShoppingItemActivityLauncher
Edycja istniejącego w bazie produktu => FirebaseShoppingListActivity.editShoppingItemActivityLauncher
	-> Edycja odbywa się poprzez pobranie produktu za pomocą UUID, który jest raz generowany dla każdego z nich
	-> Po edycji adapter zostaje powiadomiony przez referencje do bazy i podmienia produkt w liście (ponownie za pomocą UUID)

-------------------------------------
Uwierzytelnianie użytkownika (rejestracja oraz logowanie):

Implementacja logowania użytkownika => LoginActivity.kt
	-> zapytanie do firebase: od l. 44 do l. 58
Implementacja rejestracji użytkownika => RegisterActivity.kt
	-> zapytanie do firebase: od l. 42 do l. 67

-------------------------------------


Linki do repozytorium (w razie problemów z zip): 
