package com.example.bromoindah.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://YOUR_PROJECT_ID.supabase.co", // TODO: Ganti dengan URL Supabase Anda
        supabaseKey = "YOUR_ANON_KEY" // TODO: Ganti dengan Anon Key Supabase Anda
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
