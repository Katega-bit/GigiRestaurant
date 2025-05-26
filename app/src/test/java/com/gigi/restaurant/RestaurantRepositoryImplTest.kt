package com.gigi.restaurant

import android.util.Log
import com.gigi.restaurant.data.local.RestaurantDao
import com.gigi.restaurant.data.remote.GeoapifyService
import com.gigi.restaurant.data.remote.dto.PlaceFeature
import com.gigi.restaurant.data.remote.dto.PlaceProperties
import com.gigi.restaurant.data.remote.dto.PlacesResponse
import com.gigi.restaurant.data.repository.RestaurantRepositoryImpl
import com.gigi.restaurant.domain.model.Restaurant
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class RestaurantRepositoryImplTest {

    private lateinit var service: GeoapifyService
    private lateinit var dao: RestaurantDao

    // Sistema bajo prueba
    private lateinit var repository: RestaurantRepositoryImpl

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        service = mockk()
        dao     = mockk(relaxed = true)
        repository = RestaurantRepositoryImpl(service, dao)
    }

    @Test
    fun `getNearbyRestaurants devuelve lista mapeada correctamente`() = runTest {
        // 1) Creamos un fake response con dos features
        val fakeFeatures = listOf(
            PlaceFeature(
                properties = PlaceProperties(
                    placeId   = "id1",
                    name      = "Rest1",
                    distance  = 100.0,
                    categories = listOf("cafe")
                )
            ),
            PlaceFeature(
                properties = PlaceProperties(
                    placeId   = "id2",
                    name      = "Rest2",
                    distance  = 200.0,
                    categories = listOf("bar")
                )
            )
        )
        val fakeResponse = PlacesResponse(features = fakeFeatures)

        // 2) Stub del servicio: cuando llame con cualquier filter/bias/apiKey
        coEvery {
            service.getNearbyRestaurants(
                filter = any(),
                bias   = any(),
                apiKey = any()
            )
        } returns fakeResponse

        // 3) Ejecutamos el método con coordenadas arbitrarias
        val result: List<Restaurant> = repository.getNearbyRestaurants(40.4168, -3.7038)

        // 4) Verificamos que recibimos dos restaurantes con los campos correctos
        assertEquals(2, result.size)

        assertEquals("id1",   result[0].id)
        assertEquals("Rest1", result[0].name)
        assertEquals(100.0,   result[0].distance, 0.0)
        assertEquals(listOf("cafe"), result[0].categories)

        assertEquals("id2",   result[1].id)
        assertEquals("Rest2", result[1].name)
        assertEquals(200.0,   result[1].distance, 0.0)
        assertEquals(listOf("bar"), result[1].categories)
    }
}