package com.zacoding.android.fallingwords.data

sealed class DataResource<out R> {
    data class Success<out T>(val data: T) : DataResource<T>()
    object Empty : DataResource<Nothing>()
    data class Error<out T>(val exception: Throwable) : DataResource<Nothing>()
    object Loading : DataResource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$exception]"
            is Empty -> "Empty"
            Loading -> "Loading"
        }
    }
}

fun <T> DataResource<T>.successOr(fallback: T): T {
    return (this as? DataResource.Success<T>)?.data ?: fallback
}

fun <T> DataResource<T>.succeeded(): Boolean {
    return this is DataResource.Success<T>
}

val <T> DataResource<T>.data: T?
    get() = (this as? DataResource.Success<T>)?.data

fun <T> T.isEmptyResponse(): Boolean {
    return this != null && this is List<*> && this.isEmpty()
}

fun <T> DataResource<T>.onSuccess(
    onResult: DataResource.Success<T>.() -> Unit
): DataResource<T> {
    if (this is DataResource.Success) {
        onResult(this)
    }
    return this
}

fun <T> DataResource<T>.onError(
    onResult: DataResource.Error<T>.() -> Unit
): DataResource<T> {
    if (this is DataResource.Error<*>) {
        onResult(this as DataResource.Error<T>)
    }
    return this
}

fun <T> DataResource<T>.onEmpty(
    onResult: DataResource.Empty.() -> Unit
): DataResource<T> {
    if (this is DataResource.Empty) {
        onResult(this)
    }
    return this
}

fun <T> DataResource<T>.onLoading(
    onResult: DataResource.Loading.() -> Unit
): DataResource<T> {
    if (this is DataResource.Loading) {
        onResult(this)
    }
    return this
}