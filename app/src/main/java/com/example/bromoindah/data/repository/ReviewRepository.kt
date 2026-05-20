package com.example.bromoindah.data.repository

import com.example.bromoindah.data.SupabaseClient
import com.example.bromoindah.data.model.Review
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

object ReviewRepository {
    private val client = SupabaseClient.client

    suspend fun addReview(review: Review): Result<Unit> {
        return try {
            client.postgrest.from("reviews").insert(review)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByWisata(wisataId: String): Result<List<Review>> {
        return try {
            val reviews = client.postgrest.from("reviews")
                .select(Columns.raw("*, profiles(*)")) {
                    filter {
                        eq("wisata_id", wisataId)
                    }
                }
                .decodeList<Review>()
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasReviewed(pesananId: String): Result<Boolean> {
        return try {
            val reviews = client.postgrest.from("reviews")
                .select {
                    filter {
                        eq("pesanan_id", pesananId)
                    }
                }
                .decodeList<Review>()
            Result.success(reviews.isNotEmpty())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
