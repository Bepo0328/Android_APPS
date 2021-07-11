package kr.co.bepo.deliverytracker.data.repository

import kr.co.bepo.deliverytracker.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?
}