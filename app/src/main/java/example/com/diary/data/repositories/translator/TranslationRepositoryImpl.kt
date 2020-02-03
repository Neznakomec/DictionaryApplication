package example.com.diary.data.repositories.translator

import androidx.annotation.VisibleForTesting
import example.com.diary.data.ITranslationService
import example.com.diary.data.yandextranslator.entity.*
import example.com.diary.domain.models.RequestToTranslate
import example.com.diary.domain.models.Result
import example.com.diary.domain.models.Translation
import example.com.diary.domain.history.TranslationRepository
import io.reactivex.Maybe
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(private val service: ITranslationService) : TranslationRepository {

    override fun translateText(request: RequestToTranslate): Maybe<Result<Translation>> {
        return Maybe.just(convertRequest(request))
            .flatMap { req: TranslationRequest ->
                if (req.text.isEmpty()) {
                    return@flatMap Maybe.just(TranslationResponse.empty(req))
                } else {
                    return@flatMap service.requestTranslation(req)
                }
            }
            .map { mapResponse(it, request) }
            .onErrorResumeNext { throwable: Throwable -> Maybe.just(Result.Failure(code = -1, exception = throwable)) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun convertRequest(requestFromDomain: RequestToTranslate): TranslationRequest {
        return TranslationRequest(text = requestFromDomain.text,
            langFrom = requestFromDomain.langFrom,
            langTo = requestFromDomain.langTo)
    }

    private fun parseLanguages(languages: String?): Array<String> {
        var langFrom = ""
        var langTo = ""
        val LANG_FROM_POSITION = 0
        val LANG_TO_POSITION = 1
        languages?.let {
            val languageCodes = it.split('-')
            if (languageCodes.indices.contains(LANG_FROM_POSITION)) {
                langFrom = languageCodes[LANG_FROM_POSITION]
            }
            if (languageCodes.indices.contains(LANG_TO_POSITION)) {
                langTo = languageCodes[LANG_TO_POSITION]
            }
        }

        return arrayOf(langFrom, langTo)
    }

    private fun mapResponse(response: TranslationResponse, request: RequestToTranslate): Result<Translation> {
        if (response.code == CODE_OK) {
            val (langFrom, langTo) = parseLanguages(response.lang)
            val textTo = response.getResponseText()

            val data = Translation(
                langFrom = langFrom, textFrom = request.text,
                langTo = langTo, textTo = textTo, isFavourite = false
            )

            return Result.Success(data)
        } else {
            val error: Throwable = when (response.code) {
                CODE_API_KEY_INVALID -> Throwable("Неправильный API-ключ")
                CODE_API_KEY_BLOCKED -> Throwable("API-ключ заблокирован")
                CODE_LIMIT_EXCEEDED -> Throwable("Превышено суточное ограничение на объем переведенного текста")
                CODE_TEXT_LIMIT_EXCEEDED -> Throwable("Превышен максимально допустимый размер текста")
                CODE_TEXT_CANT_BEEN_TRANSLATED -> Throwable("Текст не может быть переведен")
                CODE_TRANSLATION_DIRECTION_NOT_SUPPORTED -> Throwable("Заданное направление перевода не поддерживается")
                else -> Throwable("Неизвестная ошибка, код ${response.code}")
            }
            return Result.Failure(exception = error, code = response.code)
        }
    }
}