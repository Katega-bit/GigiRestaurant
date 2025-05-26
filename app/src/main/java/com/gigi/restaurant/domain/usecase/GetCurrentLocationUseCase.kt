package com.gigi.restaurant.domain.usecase

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class LatLon(val lat: Double, val lon: Double)

class GetCurrentLocationUseCase @Inject constructor(
    private val client: FusedLocationProviderClient
) {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend operator fun invoke(): LatLon? {
        return try {
            val cts = CancellationTokenSource()
            val loc = client
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                .await()
            loc?.let { LatLon(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }
}
