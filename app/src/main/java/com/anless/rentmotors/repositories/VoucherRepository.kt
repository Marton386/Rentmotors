package com.anless.rentmotors.repositories

import java.io.File
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import okhttp3.ResponseBody
import java.io.FileOutputStream
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.VoucherAPI
import com.anless.rentmotors.api.ResultCallback

class VoucherRepository(
    private val voucherApi: VoucherAPI,
    private val cacheDirectory: File
) {
    fun loadVoucher(numReservation: String, email: String, callback: ResultCallback<File>) {
        val url = "https://rentmotors.ru/php/pass/create.php?res=$numReservation&email=$email"

        voucherApi.getVoucher(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val fileName = "Book ${numReservation}.pkpass"
                            val voucherFile = File(cacheDirectory, fileName)

                            val bytes = response.body()!!.bytes()

                            val fos = FileOutputStream(voucherFile)
                            fos.write(bytes)
                            fos.flush()
                            fos.close()
                            callback.onDataResult(voucherFile)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            callback.onError(ErrorCodes.FAILURE)
                        }
                    } else {
                        callback.onError(ErrorCodes.FAILURE)
                    }
                } else {
                    callback.onError(response.code())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onError(ErrorCodes.REQUEST_FAILURE)
            }
        })
    }
}