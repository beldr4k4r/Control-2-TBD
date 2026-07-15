<template>
  <div class="auth-container">
    <h2>Registro de Usuario</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="reg-username">Nombre de Usuario:</label>
        <input type="text" id="reg-username" v-model="form.username" required />
      </div>

      <div class="form-group">
        <label for="reg-password">Contraseña:</label>
        <input type="password" id="reg-password" v-model="form.password" required />
      </div>

      <div class="form-group">
        <label for="reg-confirm-password">Confirmar Contraseña:</label>
        <input type="password" id="reg-confirm-password" v-model="form.confirmPassword" required />
      </div>

      <div class="form-group">
        <label>Selecciona tu dirección geográfica en el mapa:</label>
        <LocationPicker @update:location="handleLocationUpdate" />

        <p v-if="form.latitude" class="coords-display">
          📍 Ubicación capturada: {{ form.latitude.toFixed(5) }}, {{ form.longitude.toFixed(5) }}
        </p>
      </div>

      <button type="submit" class="btn-auth">Registrar e Iniciar Sesión</button>
    </form>

    <p class="auth-redirect">
      ¿Ya tienes cuenta? <router-link to="/login">Inicia sesión aquí</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'
import LocationPicker from '../components/LocationP.vue'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '', 
  latitude: null,
  longitude: null,
})

const handleLocationUpdate = (coords) => {
  form.value.latitude = coords.lat
  form.value.longitude = coords.lng
}

const handleRegister = async () => {
  if (!form.value.latitude || !form.value.longitude) {
    alert('Por favor, selecciona un punto en el mapa para registrar tu dirección geográfica.')
    return
  }

  if (form.value.password !== form.value.confirmPassword) {
    alert('Las contraseñas no coinciden.')
    return
  }

  try {
    const payload = {
      username: form.value.username,
      password: form.value.password,
      confirmPassword: form.value.confirmPassword,
      latitud: form.value.latitude,
      longitud: form.value.longitude
    };

    await api.post('/auth/register', payload);
    
    alert('¡Usuario registrado exitosamente! Ahora puedes iniciar sesión.');
    router.push('/login');

  } catch (error) {
    console.error("Error al registrar:", error);
    alert('Hubo un error al registrar el usuario: ' + (error.response?.data || error.message));
  }
}
</script>

<style scoped>
.auth-container {
  max-width: 500px;
  margin: 3rem auto;
  padding: 2rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.2rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 0.6rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.coords-display {
  margin-top: 0.5rem;
  font-size: 0.9rem;
  color: #28a745;
  font-weight: bold;
}

.btn-auth {
  width: 100%;
  padding: 0.7rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  margin-top: 1rem;
}

.btn-auth:hover {
  background-color: #218838;
}

.auth-redirect {
  text-align: center;
  margin-top: 1.5rem;
  font-size: 0.9rem;
}
</style>
