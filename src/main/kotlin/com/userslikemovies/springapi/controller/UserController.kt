package com.userslikemovies.springapi.controller

import com.userslikemovies.springapi.Exceptions.*
import com.userslikemovies.springapi.config.CustomProperties
import com.userslikemovies.springapi.controller.dto.AddMovieDTO
import com.userslikemovies.springapi.controller.dto.FavoriteMovieDTO
import com.userslikemovies.springapi.controller.dto.UserCreationDTO
import com.userslikemovies.springapi.controller.dto.UserDTO
import com.userslikemovies.springapi.error.FunctionnalError
import com.userslikemovies.springapi.service.IUserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Email
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class UserController(val userService: IUserService, private val customProperties : CustomProperties) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/api/v1/users")
    @Tag(name = "default")
    @Operation(
        summary = "Obtenir un utilisateur par son ID",
        description = "Cette méthode permet d'obtenir les détails d'un utilisateur en fonction de son âge. Si aucun âge n'est spécifié, tous les utilisateurs sont renvoyés. Si le nombre d'utilisateurs est supérieur à 10, seule une partie de la réponse est renvoyée.",
    )
    @Parameter(
        name = "age",
        description = "L'âge de l'utilisateur",
        required = false,
        example = "25",
        schema = Schema(type = "integer")
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Utilisateur trouvé",
            content = [
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = UserDTO::class))
                )
            ]
        ),
        ApiResponse(
            responseCode = "201",
            description = "Utilisateurs supérieurs à 10, réponse tronquée",
            content = [
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = UserDTO::class))
                )
            ]
        ),
        ApiResponse(responseCode = "400", description = "Mauvaise requête", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = [Content(schema = Schema(type = "string"))]
        )
    ])
    fun getUsers(@RequestParam age : Int?): ResponseEntity<out Any> {
        logger.info("Get all users")
        val result = userService.getUsers(age)
        if (result.isSuccess){
            if (result.getOrNull() != null){
                return if (result.getOrNull()!!.size < 10){
                    ResponseEntity.status(HttpStatus.OK).body(result)
                }else{
                    ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(result.getOrNull()!!.take(10))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Bad Request"))
    }

    @PostMapping("api/v1/users")
    @Tag(name = "admin")
    @Operation(
        summary = "Créer un nouvel utilisateur",
        description = "Cette méthode permet de créer un nouvel utilisateur. L'utilisateur doit être fourni dans le corps de la requête.",
        parameters = [
            Parameter(
                name = "apiKey",
                description = "La clé API pour l'autorisation",
                required = true,
                schema = Schema(type = "string")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Utilisateur créé avec succès",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "401", description = "Non autorisé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "409", description = "L'utilisateur existe déjà", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "400", description = "Mauvaise requête", content = [Content(schema = Schema(type = "string"))])
    ])
    fun createUser(@RequestHeader apiKey : String, @RequestBody userDTO : UserCreationDTO): ResponseEntity<out Any> {
        logger.info("Received request to get users.")
        if (apiKey != customProperties.apiKey) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
        val result = userService.createUser(userDTO.asUser())
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserAlreadyExistsException){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(FunctionnalError("409", "User already exist").toString())
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Bad Request").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PutMapping("api/v1/users/{email}")
    @Tag(name = "admin")
    @Operation(
        summary = "Mettre à jour un utilisateur existant",
        description = "Cette méthode permet de mettre à jour les détails d'un utilisateur existant. L'utilisateur doit être fourni dans le corps de la requête.",
        parameters = [
            Parameter(
                name = "apiKey",
                description = "La clé API pour l'autorisation",
                required = true,
                schema = Schema(type = "string")
            ),
            Parameter(
                name = "email",
                description = "L'email de l'utilisateur à mettre à jour",
                required = true,
                schema = Schema(type = "string")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Utilisateur mis à jour avec succès",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "401", description = "Non autorisé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "400", description = "Mauvaise requête", content = [Content(schema = Schema(type = "string"))])
    ])
    fun updateUser(@RequestHeader apiKey : String, @PathVariable email : String, @RequestBody userDTO : UserDTO): ResponseEntity<out Any> {
        logger.info("Update user")
        if (apiKey != customProperties.apiKey){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
        val result = userService.updateUser(email, userDTO.asUser())
        if (result.isFailure){
            if (result.exceptionOrNull() != null) {
                if (result.exceptionOrNull() is UserNotFoundException) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }else if (result.exceptionOrNull() is InvalidPayload){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FunctionnalError("400", "Invalid payload").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/users/{email}")
    @Tag(name = "admin")
    @Operation(
        summary = "Supprimer un utilisateur existant",
        description = "Cette méthode permet de supprimer un utilisateur existant. L'email de l'utilisateur doit être fourni en tant que paramètre de chemin.",
        parameters = [
            Parameter(
                name = "apiKey",
                description = "La clé API pour l'autorisation",
                required = true,
                schema = Schema(type = "string")
            ),
            Parameter(
                name = "email",
                description = "L'email de l'utilisateur à supprimer",
                required = true,
                schema = Schema(type = "string")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Utilisateur supprimé avec succès",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "401", description = "Non autorisé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = [Content(schema = Schema(type = "string"))])
    ])
    fun deleteUser(@RequestHeader apiKey : String, @PathVariable email : String): ResponseEntity<out Any> {
        logger.info("Delete user")
        if (apiKey != customProperties.apiKey) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
        val result = userService.deleteUser(email)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("api/v1/users/{email}")
    @Tag(name = "default")
    @Operation(
        summary = "Obtenir un utilisateur par son email",
        description = "Cette méthode permet d'obtenir les détails d'un utilisateur en fonction de son email.",
        parameters = [
            Parameter(
                name = "email",
                description = "L'email de l'utilisateur",
                required = true,
                schema = Schema(type = "string")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Utilisateur trouvé",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = [Content(schema = Schema(type = "string"))])
    ])
    fun getUserByEmail(@PathVariable @Email email: String): ResponseEntity<out Any> {
        logger.info("Get user by email")
        val result = userService.getUserByEmail(email)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PostMapping("api/v1/users/{email}/favoriteMovies")
    @Tag(name = "default")
    @Operation(
        summary = "Ajouter un film préféré à un utilisateur",
        description = "Cette méthode permet d'ajouter un film préféré à un utilisateur. L'email de l'utilisateur et l'ID du film doivent être fournis.",
        parameters = [
            Parameter(
                name = "email",
                description = "L'email de l'utilisateur",
                required = true,
                schema = Schema(type = "string")
            ),
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Film ajouté avec succès",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "404", description = "Film non trouvé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "409", description = "Film déjà dans les favoris", content = [Content(schema = Schema(type = "string"))])
    ])
    fun addFavoriteMovies(@PathVariable @Email email : String, @RequestBody addMovie : AddMovieDTO): ResponseEntity<out Any> {
        logger.info("Add a favorite movie of a user")
       val result = userService.addUserFavoriteMovie(email, addMovie.movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is MovieNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
                }else if (result.exceptionOrNull() is UserNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }else if (result.exceptionOrNull() is MovieAlreadyInFavorites){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(FunctionnalError("409", "Movie already in favorites").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/users/{email}/favoriteMovies/{movieId}")
    @Tag(name = "default")
    @Operation(
        summary = "Supprimer un film préféré d'un utilisateur",
        description = "Cette méthode permet de supprimer un film préféré d'un utilisateur. L'email de l'utilisateur et l'ID du film doivent être fournis.",
        parameters = [
            Parameter(
                name = "email",
                description = "L'email de l'utilisateur",
                required = true,
                schema = Schema(type = "string")
            ),
            Parameter(
                name = "movieId",
                description = "L'ID du film à supprimer",
                required = true,
                schema = Schema(type = "integer")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Film supprimé avec succès",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "404", description = "Film non trouvé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "400", description = "Film non dans les favoris", content = [Content(schema = Schema(type = "string"))])
    ])
    fun deleteFavoriteMovie(@PathVariable @Email email : String, @PathVariable movieId : Int): ResponseEntity<out Any> {
        logger.info("Delete a favorite movie of a user")
        val result = userService.removeUserFavoriteMovie(email, movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                return if (result.exceptionOrNull() is UserNotFoundException){
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "User not found").toString())
                }else if (result.exceptionOrNull() is MovieNotInFavorites){
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("400", "Movie not in favorites").toString())
                }else{
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("api/v1/movies/{movieId}")
    @Tag(name = "default")
    @Operation(
        summary = "Obtenir le nombre d'utilisateurs qui ont marqué le film",
        description = "Cette méthode permet d'obtenir le nombre d'utilisateurs qui ont marqué le film. L'ID du film doit être fourni en tant que paramètre de chemin.",
        parameters = [
            Parameter(
                name = "movieId",
                description = "L'ID du film",
                required = true,
                schema = Schema(type = "integer")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Nombre d'utilisateurs qui ont marqué le film obtenu avec succès",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = FavoriteMovieDTO::class)
                )
            ]
        ),
        ApiResponse(responseCode = "404", description = "Film non trouvé", content = [Content(schema = Schema(type = "string"))])
    ])
    fun getMoviePreferenceNumber(@PathVariable movieId : Int): ResponseEntity<out Any> {
        logger.info("Get the number of users who have bookmarked the movie")
        val result = userService.getMoviePreferenceNumber(movieId)
        if (result.isFailure){
            if (result.exceptionOrNull() != null){
                if (result.exceptionOrNull() is MovieNotFoundException){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @DeleteMapping("api/v1/movies/{movieId}")
    @Tag(name = "admin")
    @Operation(
        summary = "Supprimer un film",
        description = "Cette méthode permet de supprimer un film. L'ID du film doit être fourni en tant que paramètre de chemin.",
        parameters = [
            Parameter(
                name = "apiKey",
                description = "La clé API pour l'autorisation",
                required = true,
                schema = Schema(type = "string")
            ),
            Parameter(
                name = "movieId",
                description = "L'ID du film à supprimer",
                required = true,
                schema = Schema(type = "integer")
            )
        ]
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Film supprimé avec succès",
            content = [Content(schema = Schema(type = "string"))]
        ),
        ApiResponse(responseCode = "401", description = "Non autorisé", content = [Content(schema = Schema(type = "string"))]),
        ApiResponse(responseCode = "404", description = "Film non trouvé", content = [Content(schema = Schema(type = "string"))])
    ])
        fun movieDeleted(@RequestHeader apiKey : String, @PathVariable movieId : Int): ResponseEntity<out Any> {
        logger.info("Delete a movie")
        return if (apiKey == customProperties.apiKey){
            val result = userService.movieDeleted(movieId)
            if (result != null){
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(FunctionnalError("404", "Movie not found").toString())
            }
            ResponseEntity.status(HttpStatus.OK).body(result)
        }else{
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FunctionnalError("401", "Unauthorized").toString())
        }
    }
}