package id.co.edtslib.tracker.data

import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            val code = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null) {
                    Result.success(body)
                } else {
                    Result.error("BODYNULL", "", null)
                }
            }
            else {
                Result.error(code.toString(), "", null)
            }
            return Result.error(code.toString(), response.message())
        } catch (e: Exception) {
            return Result.error("UNKNOWN", e.message, null)
        }
    }


}