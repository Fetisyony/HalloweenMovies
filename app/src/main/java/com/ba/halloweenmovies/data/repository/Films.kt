package com.ba.halloweenmovies.data.repository

import com.ba.halloweenmovies.data.model.Film
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed class FetchError {
    data object Ok : FetchError()
    data object NetworkError : FetchError()
    data object NoDataLeftError : FetchError() // Last page reached
    data object UnexpectedError : FetchError() // Any other
}


sealed class FilmsRequestResult {
    data class Success(val gifs: List<Film>) : FilmsRequestResult()
    data object Empty : FilmsRequestResult()
    data class Error(val fetchError: FetchError) : FilmsRequestResult()
}


class Films {
    private val allFilms = mutableListOf(
        Film(
            id = 1,
            title = "28 недель спустя",
            description = "Мир оказался на грани исчезновения после эпидемии вируса, превращающего людей в зомби. Спустя 28 недель, группа выживших пытается вернуть хоть какую-то нормальность в разрушенном Лондоне. Жизнь кажется возможной, но опасность всегда рядом.",
            rating = 7.0f,
            year = 2007,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgU7bKCEORvoDPHI16nLDvjKZXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 2,
            title = "Константин: Повелитель тьмы",
            description = "Джон Константин — эксцентричный детектив, который способен взаимодействовать с потусторонними силами. В его руках судьба мира, когда он сталкивается с демонами и загадочными происшествиями, угрожающими людям.",
            rating = 7.9f,
            year = 2005,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgU7bKCMLRfsDPHI16nXDvzSaXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 4,
            title = "Сплит",
            description = "История человека с раздвоенной личностью, который похищает трех девушек. Каждая из личностей его разума обладает своими особенностями, и девушки должны найти способ выбраться, прежде чем они столкнутся с самой опасной из них.",
            rating = 7.0f,
            year = 2017,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgUDbLScJQvUDPHI16nbGujmcXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 5,
            title = "Улыбка",
            description = "После трагического события врач-психиатр становится целью сверхъестественного существа, которое заставляет людей проявлять пугающую и зловещую улыбку. Она должна выяснить, что стоит за этим проклятием, прежде чем оно поглотит её.",
            rating = 6.4f,
            year = 2022,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgU7bKSYNRPQDPHI16nzDuDWYXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 6,
            title = "Чужой: Ромул",
            description = "Космическая экспедиция на дальнюю планету сталкивается с ужасным открытием — останками чуждых существ, которые проявляют все черты жестоких инопланетных форм жизни. Выжившие должны бороться за свою жизнь, сражаясь с инопланетной угрозой.",
            rating = 7.0f,
            year = 2024,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgUPbLCYMQfUDM3gx4HbHvDGfXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 7,
            title = "Морбиус",
            description = "Биохимик Майкл Морбиус, страдающий от редкой болезни крови, случайно превращается в вампира, став обладателем сверхъестественных способностей. Однако с этим даром приходит и тяжкое проклятие.",
            rating = 5.6f,
            year = 2022,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 9,
            title = "Война миров Z",
            description = "Зараженная зомби-эпидемией Земля находится на грани исчезновения. Главный герой, бывший сотрудник ООН, ищет решение, чтобы остановить распространение вируса и спасти человечество.",
            rating = 7.0f,
            year = 2013,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgUXdKSMNQfIDPn0w6nXEtjGaXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 10,
            title = "От заката до рассвета",
            description = "Группа преступников скрывается в баре в Мексике, но они оказываются среди вампиров, с которыми им предстоит вступить в жестокую борьбу. Стремительное превращение драмы в комедию ужасов.",
            rating = 7.8f,
            year = 1996,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 11,
            title = "Очень страшное кино",
            description = "Пародия на популярные ужастики, где персонажи сталкиваются с комичными и пугающими ситуациями, пытаясь выжить в мире, полном жестоких и странных существ.",
            rating = 7.0f,
            year = 2000,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 12,
            title = "Стекло",
            description = "Продолжение фильма 'Неуязвимый' и 'Сплит', в котором встречи героев с необычными способностями приводят к поединкам и раскрытию загадок их прошлого, пока не становятся частью сверхъестественного заговора.",
            rating = 6.6f,
            year = 2019,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 13,
            title = "Проклятие",
            description = "Американка приезжает в Японию и становится свидетелем ужасающего проклятия, которое поражает тех, кто попадает в дом, где когда-то произошло трагическое убийство. Теперь этот дом полон зловещих духов.",
            rating = 6.7f,
            year = 2002,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 14,
            title = "Астрал",
            description = "триллер. Семья сталкивается с ужасами, когда их сын впадает в кому после странного инцидента. Вскоре они начинают замечать сверхъестественные явления, и выясняется, что мальчик находится в другом измерении, где его душу преследуют зловещие существа.",
            rating = 6.8f,
            year = 2010,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 15,
            title = "Астрал 2",
            description = "триллер. Молодая пара сталкивается с темными силами, когда они начинают жить в новом доме. Пытаясь разгадать тайны своего прошлого и пережить сверхъестественные явления, они сталкиваются с демонами и страшными событиями, что привлекает их в мир 'Астрала'.",
            rating = 6.6f,
            year = 2013,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 16,
            title = "Астрал 3",
            description = "триллер. В этот раз события разворачиваются вокруг подростка, который сталкивается с призраком в своем доме. Когда родители начинают расследовать, они обнаруживают, что у девочки есть необычные способности, которые могут помочь в борьбе с демонами.",
            rating = 6.1f,
            year = 2015,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 17,
            title = "Звонок",
            description = "мистика. После того, как журналистка смотрит таинственную видеокассету, она оказывается в смертельной игре с призраком, который забирает жизни людей. Чтобы выжить, ей нужно раскрыть страшную тайну, скрывающуюся за этой видеозаписью.",
            rating = 7.1f,
            year = 2002,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgUXdKSMNQfIDPn0w6nXEtjGaXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 18,
            title = "Оно",
            description = "Группа детей, столкнувшихся с ужасными событиями в своем маленьком городке, обнаруживает, что причиной всех бед является зловещий клоун по имени Пеннивайз. Они решают объединиться, чтобы победить его и раскрыть тайну этого ужаса.",
            rating = 7.3f,
            year = 2017,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
        Film(
            id = 19,
            title = "Крик",
            description = "триллер. В маленьком городке начинают происходить убийства, и группа подростков оказывается в центре этих событий. Серийный убийца, известный как 'Призрачное лицо', скрывается среди них. Чтобы выжить, им предстоит раскрыть его личность, следуя правилам ужасных фильмов.",
            rating = 7.3f,
            year = 1996,
            posterUrl = "https://yastatic.net/naydex/yandex-search/BhQ12l374/eee224dhZXS/JN4gsmRA-pkJUL4DFx9G52n8H1OmIvenzTnQW8mVlxWHxprsxxBgULYIScKRvoDPXI77H3DtjeXXNPCOmFBBAvAU9HDvXLGQRQzzj97Xjer8P82SMKz5A"
        ),
    )

    private val favouriteFilmIds = mutableSetOf<Int>()

    private val filmsAmountToRequest = 6
    private var nextPosition = 0

    fun getAllFilmsImmediately(): FilmsRequestResult {
        return FilmsRequestResult.Success(allFilms)
    }

    suspend fun getInitFilms(): FilmsRequestResult {
        delay(500)
        val films = allFilms.subList(0, minOf(filmsAmountToRequest, allFilms.size))
        nextPosition = films.size
        return FilmsRequestResult.Success(films)
    }

    suspend fun getNextFilms(): FilmsRequestResult {
        delay(500)

        if (nextPosition >= allFilms.size) {
            return FilmsRequestResult.Success(emptyList())
        }

        val nextBatchSize = minOf(filmsAmountToRequest, allFilms.size - nextPosition)
        val films = allFilms.subList(nextPosition, nextPosition + nextBatchSize)
        nextPosition += nextBatchSize
        return FilmsRequestResult.Success(films)
    }

    suspend fun getFavouriteFilms(): List<Film> =
        allFilms.filter { it.id in favouriteFilmIds }

    fun isFavourite(filmId: Int): Boolean = filmId in favouriteFilmIds

    fun toggleFavourite(filmId: Int) {
        if (isFavourite(filmId)) {
            favouriteFilmIds.remove(filmId)
        } else {
            favouriteFilmIds.add(filmId)
        }
    }
}
