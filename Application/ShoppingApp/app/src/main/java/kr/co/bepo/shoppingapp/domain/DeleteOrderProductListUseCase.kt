package kr.co.bepo.shoppingapp.domain

import kr.co.bepo.shoppingapp.data.repository.ProductRepository

internal class DeleteOrderProductListUseCase(
    private val productRepository: ProductRepository
) : UseCase {

    suspend operator fun invoke() {
        return productRepository.deleteAll()
    }

}