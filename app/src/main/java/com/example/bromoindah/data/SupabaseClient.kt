package com.example.bromoindah.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://sbwrcqjnfzxfrbeowved.supabase.co", // TODO: Ganti dengan URL Supabase Anda
        supabaseKey = "sb_publishable_mQ9M3HdGZuRWNa38k1kmPQ_aVbdpFP1" // TODO: Ganti dengan Anon Key Supabase Anda
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
