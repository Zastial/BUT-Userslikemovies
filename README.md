# UsersLikeMovies


## Log

Pour les logs, nous avons pensé qu'il y avait une petite coquille dans la consigne à cet endroit : "il faut un log DEBUG à chaque appel reçu par l’application, le profile DEV passe le niveau de log en DEBUG pour les contrôlleurs"

Nous sommes donc partis du principe qu'il fallait mettre des logs INFO lors des appels aux méthodes du controleur et qu'il fallait que des logs DEBUG apparaissent si le profil DEV est activé. 

## Swagger

Nous avons utilisé Swagger pour documenter notre API. Pour accéder à la documentation, il suffit de lancer l'application et de se rendre à l'adresse suivante : http://localhost:8080/swagger-ui.html

Pour rendre le code du controller plus lisible, nous avions pensé à implémenter une interface dans laquelle les commentaires seraient présents.
Le code, serait lui, dans une classe qui implémente cette interface. Cela permettrait de séparer les commentaires du code et de rendre le code plus lisible.

Faute de temps, cela n'a pas été fait.

## Tests

Nous avons écrit des tests pour les méthodes du controller. Nous avons utilisé JUnit et Mockito pour les tests.

### Manque de tests

Dans UserRepositoryTest, les cas de tests suivants ne sont pas implémentés :

    @Test
    fun addUserFavoriteMovie() {}

    @Test
    fun removeUserFavoriteMovie() {}

    @Test
    fun movieDeleted() {}

    @Test
    fun getMoviePreferenceNumber() {}

En effet, à cause du restTemplate, nous n'avons pas réussi à mocker les appels à l'API externe. Nous avons donc décidé de ne pas implémenter ces tests.

Solution : Résoudre le problème de conception

## Base de données

Nous avons utilisé une base de données H2 pour stocker les données. Nous avons utilisé JPA pour la persistance des données.
