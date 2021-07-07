package kr.co.bepo.shoppingapp.domain

import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity
import kr.co.bepo.shoppingapp.data.repository.ProductRepository

internal class GetProductListUseCase(
    private val productRepository: ProductRepository
) : UseCase {

    suspend operator fun invoke(): List<ProductEntity> {
        return productRepository.getProductList()
    }
}