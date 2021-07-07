package kr.co.bepo.shoppingapp.domain

import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity
import kr.co.bepo.shoppingapp.data.repository.ProductRepository

internal class OrderProductItemUseCase(
    private val productRepository: ProductRepository
) : UseCase {

    suspend operator fun invoke(productEntity: ProductEntity): Long {
        return productRepository.insertProductItem(productEntity)
    }
}