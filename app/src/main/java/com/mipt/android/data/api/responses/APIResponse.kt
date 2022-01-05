package com.mipt.android.data.api.responses

sealed class APIResponse<Response>(
    val data: Response? = null,
    val error: APIError? = null
){
    class Success<Response> (data : Response) : APIResponse<Response>(data = data)
    class Failure<Response> (error : APIError) : APIResponse<Response>(error = error)
}