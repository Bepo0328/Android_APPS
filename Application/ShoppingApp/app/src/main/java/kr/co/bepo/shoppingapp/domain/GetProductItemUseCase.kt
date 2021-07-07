package kr.co.bepo.shoppingapp.domain

import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity
import kr.co.bepo.shoppingapp.data.repository.ProductRepository

internal class GetProductItemUseCase(
    private val productRepository: ProductRepository
) : UseCase {

    suspend operator fun invoke(productId: Long): ProductEntity? {
        return productRepository.getProductItem(productId)
    }
}