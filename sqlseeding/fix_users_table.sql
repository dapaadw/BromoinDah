-- SQL Script to fix the "Key is not present in table 'users'" error
-- Copy and paste this script into your Supabase project's SQL Editor and click "Run".

-- 1. First, insert any existing users from auth.users into your public.users table
INSERT INTO public.users (id, email, nama_lengkap, role)
SELECT 
    id, 
    email, 
    COALESCE(raw_user_meta_data->>'nama_lengkap', 'User'), 
    COALESCE(raw_user_meta_data->>'role', 'user')
FROM auth.users
ON CONFLICT (id) DO NOTHING;

-- 2. Create a function to automatically add new users to public.users when they sign up
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger AS $$
BEGIN
  INSERT INTO public.users (id, email, nama_lengkap, role)
  VALUES (
      new.id, 
      new.email, 
      COALESCE(new.raw_user_meta_data->>'nama_lengkap', 'User'), 
      COALESCE(new.raw_user_meta_data->>'role', 'user')
  );
  RETURN new;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- 3. Create the trigger that calls the function every time a new user signs up
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE PROCEDURE public.handle_new_user();
