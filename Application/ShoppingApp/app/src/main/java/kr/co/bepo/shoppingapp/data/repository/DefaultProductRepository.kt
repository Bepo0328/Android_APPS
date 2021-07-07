package kr.co.bepo.shoppingapp.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.bepo.shoppingapp.data.db.dao.ProductDao
import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity
import kr.co.bepo.shoppingapp.data.network.ProductApiService

class DefaultProductRepository(
    private val productApi: ProductApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun getProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        val response = productApi.getProducts()
        return@withContext if (response.isSuccessful) {
            response.body()?.items?.map { it.toEntity() } ?: listOf()
        } else {
            listOf()
        }
    }

    override suspend fun getLocalProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        productDao.getAll()
    }

    override suspend fun insertProductItem(productItem: ProductEntity): Long =
        withContext(ioDispatcher) {
            productDao.insert(productItem)
        }

    override suspend fun insertProductList(ProductList: List<ProductEntity>) =
        withContext(ioDispatcher) {
            TODO("Not yet implemented")
        }

    override suspend fun updateProductItem(productItem: ProductEntity) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductItem(itemId: Long): ProductEntity? = withContext(ioDispatcher) {
        val response = productApi.getProduct(itemId)
        return@withContext if (response.isSuccessful) {
            response.body()?.toEntity()
        } else {
            null
        }
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        productDao.deleteAll()
    }

    override suspend fun deleteProductItem(itemId: Long) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}