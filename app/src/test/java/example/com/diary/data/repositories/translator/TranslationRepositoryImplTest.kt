package example.com.diary.data.repositories.translator

import example.com.diary.data.ITranslationService
import example.com.diary.data.yandextranslator.entity.TranslationRequest
import example.com.diary.data.yandextranslator.entity.TranslationResponse
import example.com.diary.domain.models.RequestToTranslate
import example.com.diary.domain.models.Result
import example.com.diary.domain.models.Translation
import io.reactivex.Maybe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TranslationRepositoryImplTest {
    @Mock
    lateinit var service: ITranslationService

    private lateinit var repository: TranslationRepositoryImpl
    @Before
    fun setUp() {
        repository = TranslationRepositoryImpl(service)
    }

    @Test
    fun `check translation service receiving request and return correct response`() {
        // when
        translationRequest = repository.convertRequest(requestToTranslate)
        translationRequest2 = repository.convertRequest(requestToTranslate2)

        Mockito.`when`(service.requestTranslation(translationRequest)).thenReturn(Maybe.just(translationResponse))
        // action
        val result =
            repository.translateText(requestToTranslate)
                .blockingGet()
        val translation = (result as Result.Success).value
        // assert & verify
        assertEquals(translation, correctTranslation)
        Mockito.verify(service, Mockito.times(1)).requestTranslation(translationRequest)
        Mockito.verify(service, Mockito.times(0)).requestTranslation(translationRequest2)
    }

    @Test
    fun `check translation service incorrect language pair returns same text`() {
        // when
        translationRequestIncorrect = repository.convertRequest(requestToTranslateIncorrect)
        Mockito.`when`(service.requestTranslation(translationRequestIncorrect)).thenReturn(Maybe.just(
            translationResponseIncorrect
        ))
        // action
        val result =
            repository.translateText(requestToTranslateIncorrect)
                .blockingGet()
        val translation = (result as Result.Success).value
        // assert & verify
        assertEquals(translation.textFrom, translation.textTo)
    }

    @Test
    fun `incorrect response code passes into exception`() {
        // when
        translationRequest = repository.convertRequest(requestToTranslate)
        Mockito.`when`(service.requestTranslation(translationRequest)).thenReturn(Maybe.just(incorrectResponse))
        // action
        val result =
            repository.translateText(requestToTranslate)
                .blockingGet()

        assertTrue(result is Result.Failure)
        val failure = result as Result.Failure

        // assert & verify
        assertEquals(failure.code, incorrectResponse.code )
    }

    // <DATA>
    private val requestToTranslate =
        RequestToTranslate(
            text = "apple", langFrom = "en", langTo = "ru"
        )

    private val requestToTranslate2 =
        RequestToTranslate(
            text = "apple", langFrom = "en", langTo = "en"
        )
    private val requestToTranslateIncorrect =
        RequestToTranslate(
            text = "apple", langFrom = "ru", langTo = "en"
        )
    private val correctTranslation =
        Translation(
            langFrom = requestToTranslate.langFrom, langTo = requestToTranslate.langTo,
            textFrom = requestToTranslate.text, textTo = "яблоко",
            isFavourite = false
        )
    private lateinit var translationRequest2: TranslationRequest
    private lateinit var translationRequest: TranslationRequest
    private lateinit var translationRequestIncorrect: TranslationRequest


    private val translationResponse = TranslationResponse(
        200, "en-ru", listOf("яблоко")
    )
    private val translationResponseIncorrect = TranslationResponse(
        200, "ru-en", listOf("apple")
    )
    private val incorrectResponse = TranslationResponse(
        12345, "", listOf()
    )
}