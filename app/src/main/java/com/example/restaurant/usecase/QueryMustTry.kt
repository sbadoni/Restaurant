package com.example.restaurant.usecase

import com.example.restaurant.data.Response
import com.example.restaurant.source.DataSource
import io.reactivex.Flowable

class QueryMustTry(private val dataSource: DataSource) {
    operator fun invoke() : Flowable<Response> = dataSource.getStaticData()
}
