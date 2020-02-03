package example.com.diary.domain.models

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Failure<T>(val exception: Throwable, val code: Int) : Result<T>()
}