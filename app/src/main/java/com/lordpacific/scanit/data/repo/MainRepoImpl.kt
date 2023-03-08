package com.lordpacific.scanit.data.repo

import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.lordpacific.scanit.domain.repo.MainRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainRepoImpl @Inject constructor(
    private val scanner: GmsBarcodeScanner
) : MainRepo {

    override fun startScanning(): Flow<String?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    launch {
                        send(getDetails(barcode))
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }

            awaitClose { }
        }
    }


    private fun getDetails(barcode: Barcode): String {
        return when (barcode.valueType) {
            Barcode.TYPE_WIFI -> {
                val ssid = barcode.wifi!!.ssid
                val password = barcode.wifi!!.password
                val type = barcode.wifi!!.encryptionType
                "ssid: $ssid, password: $password, type: $type"
            }

            Barcode.TYPE_URL -> {
                "url: ${barcode.url!!.url}"
            }

            Barcode.TYPE_PRODUCT -> {
                "productType: ${barcode.displayValue}"
            }

            Barcode.TYPE_EMAIL -> {
                "email: ${barcode.email}"
            }

            Barcode.TYPE_CONTACT_INFO -> {
                "contact: ${barcode.contactInfo}"
            }

            Barcode.TYPE_PHONE -> {
                "phone: ${barcode.phone}"
            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                "calendar event: ${barcode.calendarEvent}"
            }

            Barcode.TYPE_GEO -> {
                "geo point: ${barcode.geoPoint}"
            }

            Barcode.TYPE_ISBN -> {
                "isbn: ${barcode.displayValue}"
            }

            Barcode.TYPE_SMS -> {
                "sms: ${barcode.sms}"
            }

            Barcode.TYPE_TEXT -> {
                "text: ${barcode.rawValue}"
            }
            Barcode.TYPE_UNKNOWN -> {
                "unknown: ${barcode.rawValue}"
            }
            else -> {
              barcode.rawValue ?: "Couldn't determine"
            }

        }

    }

}