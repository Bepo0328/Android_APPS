package kr.co.bepo.shoppingapp.data.response

data class ProductsResponse(
    val items: List<ProductResponse>,
    val count: Int
)